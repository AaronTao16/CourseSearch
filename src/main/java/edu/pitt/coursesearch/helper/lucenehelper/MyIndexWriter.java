package edu.pitt.coursesearch.helper.lucenehelper;

import com.microsoft.azure.storage.StorageException;
import edu.pitt.coursesearch.helper.azurehelper.AzureBlob;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
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
    private final AzureBlob azureBlob;
    public final RAMDirectory ramDirectory;
    private FieldType type;


    public MyIndexWriter(AzureBlob azureBlob, RAMDirectory ramDirectory, Analyzer analyzer) throws IOException {
        this.azureBlob = azureBlob;
        this.ramDirectory = ramDirectory;
        this.documentList = new ArrayList<>();
        this.OriginalList = new ArrayList<>();

        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        System.out.println(ramDirectory);
        this.indexWriter = new IndexWriter(ramDirectory, indexWriterConfig);
        type = new FieldType();
        type.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
        type.setStored(false);
        type.setStoreTermVectors(true);
    }

    public void createIndex () {
        getAllDocuments(azureBlob.getAllFileNames());

        this.documentList.parallelStream().forEach(document -> {
            try {
                this.indexWriter.addDocument(document);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        this.close();
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

    private void getAllDocuments(List<String> allFileNames) {
        allFileNames.stream().parallel().forEach(fileName -> {
            if(!fileName.equals("data.json")) return;

            try {
                JSONObject jsonObject = new JSONObject(this.azureBlob.readFiles(fileName));

                for (int i = 0; i < jsonObject.names().length(); i++) {
                    StringBuilder content = new StringBuilder();
                    Document document = new Document();

                    String id = (String) jsonObject.names().get(i);
                    JSONObject course = (JSONObject) jsonObject.get(id);
                    String name = course.get("name").toString();
                    String courseDep = ((JSONObject) course.get("courseCode")).get("dept").toString();
                    String courseNum = ((JSONObject) course.get("courseCode")).get("number").toString();
                    String courseInstructor = course.get("instructor").toString();
                    String grad = course.get("grad").toString().equals("false") ? "undergraduate" : "graduate";
                    String des = course.get("description").toString();
                    content.append(name).append(" ");
                    content.append(courseDep).append(" ");
                    content.append(courseNum).append(" ");
                    content.append(des).append(" ");
                    content.append(courseInstructor).append(" ");
                    content.append(grad).append(" ");

                    JSONArray section = course.getJSONArray("sections");
                    for (int j = 0; j < section.length(); j++) {
                        String classNumber = section.getJSONObject(j).get("classNumber").toString();
                        String days = section.getJSONObject(j).get("days").toString();
                        String beginTime = section.getJSONObject(j).get("beginTime").toString();
                        String endTime = section.getJSONObject(j).get("endTime").toString();
                        String sectionType = section.getJSONObject(j).get("sectionType").toString();
                        String building = section.getJSONObject(j).get("building").toString();
                        String room = section.getJSONObject(j).get("room").toString();
                        content.append(classNumber).append(" ");
                        content.append(days).append(" ");
                        content.append(beginTime).append(" ");
                        content.append(endTime).append(" ");
                        content.append(sectionType).append(" ");
                        content.append(building).append(" ");
                        content.append(room).append(" ");
                    }

                    document.add(new TextField("id", id, Field.Store.YES));
                    document.add(new TextField("title", courseDep + " " + courseNum, Field.Store.YES));
                    document.add(new TextField("name", name, Field.Store.YES));
                    document.add(new TextField("instructor", courseInstructor, Field.Store.YES));
//                    document.add(new TextField("grad", grad, Field.Store.YES));
                    document.add(new TextField("content", content.toString(), Field.Store.YES));
                    document.add(new TextField("course", course.toString(), Field.Store.YES));

                    this.documentList.add(document);

                }
            } catch (URISyntaxException | StorageException | IOException | JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void close() {
        try {
            this.indexWriter.close();
        } catch (IOException e) {
//            log.error("unable to close writer");
        } finally {
            System.gc();
        }

    }

}
