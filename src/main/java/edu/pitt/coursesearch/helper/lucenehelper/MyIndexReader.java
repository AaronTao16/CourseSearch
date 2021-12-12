package edu.pitt.coursesearch.helper.lucenehelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import edu.pitt.coursesearch.helper.azurehelper.AzureBlob;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

@Slf4j
public class MyIndexReader {
    private static MyIndexReader myIndexReader = null;
    private DirectoryReader ireader;

    private IndexReader indexReader;
    private final IndexSearcher searcher;
    private final Analyzer analyzer;
    private Query query;
    private final AzureBlob azureBlob;

    // instantiate reader
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

    // main search function
    // searches a specific document field
    public Map<String, JSONObject> searchDocument(String query, String field, int topK) {
        Map<String, JSONObject> res = new HashMap<>();

        try {
            // parse query, search
            this.query = new QueryParser(field, analyzer).parse(query);
            TopDocs topDocs = this.searcher.search(this.query, topK);
            ScoreDoc[] hits = topDocs.scoreDocs;

            for(int i=0;i<hits.length;++i) {
                int docId = hits[i].doc;    // lucene docID
                Document d = searcher.doc(docId);   // get stored fields for doc
                JSONObject jsonObject = new JSONObject(d.get("course"));
                // add score to document data
                jsonObject.put("score", hits[i].score);
                // add stored fields for document to result, indexed by course ID
                res.put(d.get("id"), jsonObject);
                System.out.println((i + 1) + ". " + d.get("id") + "\t" + d.get("content"));
            }

        } catch (IOException | ParseException | JSONException e) {
            e.printStackTrace();
        }
        return res;
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

