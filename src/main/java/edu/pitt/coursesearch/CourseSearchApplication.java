package edu.pitt.coursesearch;

import com.microsoft.azure.storage.StorageException;
import edu.pitt.coursesearch.helper.azurehelper.AzureBlob;
import edu.pitt.coursesearch.helper.lucenehelper.MyIndexReader;
import edu.pitt.coursesearch.helper.lucenehelper.MyIndexWriter;
import edu.pitt.coursesearch.model.Course;
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
import java.util.HashMap;

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
        RAMDirectory facetDirectory = new RAMDirectory();

        // normalized data
//        MyNormalize myNormalize = new MyNormalize(this.connectionKey, this.containerName, this.containerNameAfterIndex, analyzer);
//        myNormalize.normalize();

        AzureBlob azureBlobAfterNormalize = new AzureBlob(this.connectionKey, this.containerName);
        MyIndexWriter myIndexWriter = new MyIndexWriter(azureBlobAfterNormalize, ramDirectory, facetDirectory, analyzer);
        // create the index, return the corpus cache
        HashMap<Integer, Course> courseCache = myIndexWriter.createIndex();

        MyIndexReader.getInstance(azureBlobAfterNormalize, ramDirectory, analyzer, courseCache);
    }

}
