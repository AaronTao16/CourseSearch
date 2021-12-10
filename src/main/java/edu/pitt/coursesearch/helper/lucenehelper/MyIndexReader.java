package edu.pitt.coursesearch.helper.lucenehelper;

import java.io.IOException;

import edu.pitt.coursesearch.helper.azurehelper.AzureBlob;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.RAMDirectory;

@Slf4j
public class MyIndexReader {
    private static MyIndexReader myIndexReader = null;
    private DirectoryReader ireader;

    private IndexReader indexReader;
    private final IndexSearcher searcher;
    private final Analyzer analyzer;
    private Query query;
    private final AzureBlob azureBlob;

    public MyIndexReader(AzureBlob azureBlob, RAMDirectory ramDirectory, Analyzer analyzer) {
        this.azureBlob = azureBlob;
        this.analyzer = analyzer;
        try {
            this.indexReader = DirectoryReader.open(ramDirectory);
        } catch (IOException e) {
//            log.error("unable to create index reader because of" + e.getMessage());
        }

        if (this.indexReader == null) {
            throw new NullPointerException("unable to find indexReader");
        }
        this.searcher = new IndexSearcher(this.indexReader);
    }

    public static void getInstance(AzureBlob azureBlob, RAMDirectory ramDirectory, Analyzer analyzer) {
        if (myIndexReader == null)
            myIndexReader = new MyIndexReader(azureBlob, ramDirectory, analyzer);

    }

    public static MyIndexReader getInstance() {
        if (myIndexReader == null) {
            throw new NullPointerException("index reader is not initialized");
        }

        return myIndexReader;
    }


    public void close() throws IOException {
        // you should implement this method when necessary
        ireader.close();
    }

    public IndexReader getIndexReader() {
        return indexReader;
    }
}

