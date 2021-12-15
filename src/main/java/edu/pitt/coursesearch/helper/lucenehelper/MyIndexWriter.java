package edu.pitt.coursesearch.helper.lucenehelper;

import com.microsoft.azure.storage.StorageException;
import edu.pitt.coursesearch.helper.azurehelper.AzureBlob;
import edu.pitt.coursesearch.model.Course;
import edu.pitt.coursesearch.model.Section;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.facet.FacetField;
import org.apache.lucene.facet.FacetsConfig;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyWriter;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class MyIndexWriter {

    private final List<Document> documentList;
    private List<String> OriginalList;
    private IndexWriter indexWriter;
    private DirectoryTaxonomyWriter facetWriter;
    private FacetsConfig facetConfig;
    private final AzureBlob azureBlob;
    public final RAMDirectory ramDirectory;


    public MyIndexWriter(AzureBlob azureBlob, RAMDirectory ramDirectory, RAMDirectory facetDirectory, Analyzer analyzer) throws IOException {
        this.azureBlob = azureBlob;
        this.ramDirectory = ramDirectory;
        this.documentList = new ArrayList<>();
        this.OriginalList = new ArrayList<>();
        // configure IndexWriter to use StandardAnalyzer
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        // create or overwrite index
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        // set TF-IDF based VSM similarity
        indexWriterConfig.setSimilarity(new ClassicSimilarity());
        // create IndexWriter instance (in-memory index)
        this.indexWriter = new IndexWriter(ramDirectory, indexWriterConfig);
        // facet index writer
        this.facetWriter = new DirectoryTaxonomyWriter(facetDirectory);
        this.facetConfig = new FacetsConfig();
        this.facetConfig.setMultiValued("day", true);
        this.facetConfig.setMultiValued("required", true);
        this.facetConfig.setMultiValued("elective", true);
    }

    // main index routine
    public HashMap<Integer, Course> createIndex () {
        // process corpus
        HashMap<Integer, Course> response = getAllDocuments(azureBlob.getAllFileNames());

        // add documents to indexer
        this.documentList.parallelStream().forEach(document -> {
            try {
                this.indexWriter.addDocument(document);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        this.close();

        return  response;
    }

    private Map<String, String> nextDocument(Iterator<String> iterator) {
        Map<String, String> doc = null;
        if (iterator.hasNext()){
            doc = new HashMap<>();
            String course = iterator.next();
            String[] courseInfo = course.trim().split(" ");
            doc.put("id", courseInfo[0]);
            doc.put("content", Arrays.stream(courseInfo).skip(1).collect(Collectors.joining(" ")));
        }

        return doc;
    }

    // process corpus JSON into Lucene documents
    private HashMap<Integer, Course> getAllDocuments(List<String> allFileNames) {
        HashMap<Integer, Course> response = new HashMap<>();
        // open file on azure
        allFileNames.stream().parallel().forEach(fileName -> {
            if(!fileName.equals("data.json")) return;

            try {
                // read file as string, parse json
                JSONObject courses = new JSONObject(this.azureBlob.readFiles(fileName));
                // iterate through courses
                for (int i = 0; i < courses.names().length(); i++) {
                    // create Course object
                    String id = (String) courses.names().get(i);
                    JSONObject course = (JSONObject) courses.get(id);
                    Course newCourse = new Course(
                            Integer.parseInt(id),
                            ((JSONObject) course.get("courseCode")).get("dept").toString().trim(),
                            Integer.parseInt(((JSONObject) course.get("courseCode")).get("number").toString().trim()),
                            course.get("name").toString().trim(),
                            course.get("description").toString().trim(),
                            course.get("instructor").toString().trim(),
                            course.get("grad").toString().equals("true") ? true : false,
                            new ArrayList<>(),
                            new ArrayList<>(),
                            new ArrayList<>()
                    );
                    // special handling for graduation requirements
                    JSONArray required = ((JSONObject) course.get("satisfiedReqs")).getJSONArray("required");
                    JSONArray elective = ((JSONObject) course.get("satisfiedReqs")).getJSONArray("elective");
                    for (int j = 0; j < required.length(); j++) {
                        newCourse.getRequired().add((String) required.get(j));
                    }
                    for (int j = 0; j < elective.length(); j++) {
                        newCourse.getElective().add((String) elective.get(j));
                    }
                    // special handling for sections
                    JSONArray sections = course.getJSONArray("sections");
                    for (int j = 0; j < sections.length(); j++) {
                        // create and save each Section
                        JSONObject section = sections.getJSONObject(j);
                        Section newSection = new Section(
                                Integer.parseInt(section.get("classNumber").toString()),
                                new ArrayList<>(),
                                section.get("beginTime").toString(),
                                section.get("endTime").toString(),
                                section.get("sectionType").toString(),
                                section.get("building").toString(),
                                section.get("room").toString()
                        );
                        // add each day
                        JSONArray days = section.getJSONArray("days");
                        newCourse.setDays(new HashSet<>());
                        for (int k = 0; k < days.length(); k++) {
                            newSection.getDays().add((String) days.get(k));
                            newCourse.getDays().add((String) days.get(k));
                        }
                        newCourse.getSections().add(newSection);
                    }

                    // add Course to memory cache
                    response.put(newCourse.getId(), newCourse);

                    // create Lucene documents, set indexing options
                    // indexes course text fields and course number but does not store full text in index
                    // id stored in index can be used to retrieve full Course data from cache
                    Document document = new Document();
                    document.add(new StoredField("id", newCourse.getId())); // needed in index but not searched
                    document.add(new TextField("dept", newCourse.getDept(), Field.Store.NO));
                    document.add(new StringField("number", Integer.toString( newCourse.getNumber()), Field.Store.NO));  // do not tokenize
                    document.add(new TextField("name", newCourse.getName(), Field.Store.NO));
                    document.add(new TextField("description", newCourse.getDescription(), Field.Store.NO));
                    document.add(new TextField("instructor", newCourse.getInstructor(), Field.Store.NO));
                    // facet fields
                    document.add(new FacetField("grad", newCourse.isGrad() ? "true" : "false"));
                    // days
                    for (String day : newCourse.getDays()) {
                        if (day.length() != 0) {
                            document.add(new FacetField("day",day));
                        }
                    }
                    // required
                    for (String program : newCourse.getRequired()) {
                        document.add(new FacetField("required",program));
                    }
                    // elective
                    for (String program : newCourse.getElective()) {
                        document.add(new FacetField("elective",program));
                    }

                    this.documentList.add(facetConfig.build(facetWriter, document));

                }
            } catch (URISyntaxException | StorageException | IOException | JSONException e) {
                e.printStackTrace();
            }
//            String content = null;
//            try {
//                content = this.azureBlob.readFiles(fileName);
//                OriginalList = Arrays.asList(content.split("\n"));
//            } catch (URISyntaxException | StorageException | IOException e) {
//                e.printStackTrace();
//            }
//            // initiate a doc object, which can hold document number and document content of a document
//            Map<String, String> doc = null;
//
//            Iterator<String> iterator = OriginalList.iterator();
//
//            while((doc = nextDocument(iterator)) != null){
//                Document document = new Document();
//                document.add(new TextField("id", doc.get("id"), Field.Store.YES));
//                document.add(new TextField("content", doc.get("content"), Field.Store.YES));
//                this.documentList.add(document);
//            }

        });

        return response;
    }

    private void close() {
        try {
            this.indexWriter.close();
            this.facetWriter.close();
        } catch (IOException e) {
//            log.error("unable to close writer");
        } finally {
            System.gc();
        }

    }

}
