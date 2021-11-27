package edu.pitt.coursesearch.helper.lucenehelper;

import com.microsoft.azure.storage.StorageException;
import edu.pitt.coursesearch.helper.azurehelper.AzureBlob;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.RAMDirectory;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MyIndexWriter {

    private final List<Document> documentList;
    private IndexWriter indexWriter;
    private final AzureBlob azureBlob;
    public final RAMDirectory ramDirectory;


    public MyIndexWriter(AzureBlob azureBlob, RAMDirectory ramDirectory, Analyzer analyzer) throws IOException {
        this.azureBlob = azureBlob;
        this.ramDirectory = ramDirectory;
        this.documentList = new ArrayList<>();

        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        this.indexWriter = new IndexWriter(ramDirectory, indexWriterConfig);
    }

    public void createIndex () {

        List<String> allFileNames = this.azureBlob.getAllFileNames();

        System.out.println(allFileNames);
        this.getAllDocuments(allFileNames);

        this.documentList.parallelStream().forEach(document -> {
            try {
                this.indexWriter.addDocument(document);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        this.close();
    }

    private void getAllDocuments(List<String> allFileNames) {
        allFileNames.stream().parallel().forEach(fileName -> {
            log.info(fileName);

            String content = null;
            try {
                content = this.azureBlob.readFiles(fileName);
            } catch (URISyntaxException | StorageException | IOException e) {
                e.printStackTrace();
            }

            Document doc = new Document();
            TextField nameField = new TextField("name", fileName, Field.Store.YES);
            TextField contentField = new TextField("content", content, Field.Store.YES);
            doc.add(nameField);
            doc.add(contentField);

            this.documentList.add(doc);
        });
    }

    private void close() {
        try {
            this.indexWriter.close();
        } catch (IOException e) {
            log.error("unable to close writer");
        } finally {
            System.gc();
        }

    }

}
