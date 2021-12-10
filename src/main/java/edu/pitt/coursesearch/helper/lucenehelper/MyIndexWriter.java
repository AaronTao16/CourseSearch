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
//            log.info(fileName);
            if(!fileName.equals("after_normalize_data.json")) return;
            String content = null;
            try {
                content = this.azureBlob.readFiles(fileName);
                OriginalList = Arrays.asList(content.split("\n"));
            } catch (URISyntaxException | StorageException | IOException e) {
                e.printStackTrace();
            }
            // initiate a doc object, which can hold document number and document content of a document
            Map<String, String> doc = null;

            Iterator<String> iterator = OriginalList.iterator();

            while((doc = nextDocument(iterator)) != null){
                Document document = new Document();
                document.add(new TextField("id", doc.get("id"), Field.Store.YES));
                document.add(new TextField("content", doc.get("content"), Field.Store.YES));
                this.documentList.add(document);
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
