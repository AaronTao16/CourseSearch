package edu.pitt.coursesearch;

import com.microsoft.azure.storage.StorageException;
import edu.pitt.coursesearch.helper.azurehelper.AzureBlob;
import edu.pitt.coursesearch.helper.lucenehelper.MyIndexReader;
import edu.pitt.coursesearch.helper.lucenehelper.MyIndexWriter;
import edu.pitt.coursesearch.helper.lucenehelper.MyNormalize;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

@SpringBootApplication
public class CourseSearchApplication {
    @Value("${azure.storage.connectionKey}")
    private String connectionKey;

    @Value("${azure.storage.containerName}")
    private String containerName;

    @Value("${azure.storage.containerNameAfterIndex}")
    private String containerNameAfterIndex;

    private static final Analyzer analyzer = new StandardAnalyzer(CharArraySet.EMPTY_SET);

    public static void main(String[] args) {
        SpringApplication.run(CourseSearchApplication.class, args);
    }

    @PostConstruct
    public void initialize() throws URISyntaxException, InvalidKeyException, StorageException, IOException {
        RAMDirectory ramDirectory = new RAMDirectory();

        // normalized data
//        MyNormalize myNormalize = new MyNormalize(this.connectionKey, this.containerName, this.containerNameAfterIndex, analyzer);
//        myNormalize.normalize();

        AzureBlob azureBlobAfterNormalize = new AzureBlob(this.connectionKey, this.containerNameAfterIndex);
        MyIndexWriter myIndexWriter = new MyIndexWriter(azureBlobAfterNormalize, ramDirectory, analyzer);
        myIndexWriter.createIndex();

        MyIndexReader.getInstance(azureBlobAfterNormalize, ramDirectory, analyzer);
    }

}
