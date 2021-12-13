package edu.pitt.coursesearch.helper.lucenehelper;

import java.io.IOException;
import java.util.*;

import edu.pitt.coursesearch.helper.azurehelper.AzureBlob;
import edu.pitt.coursesearch.model.Course;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.ClassicSimilarity;
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

    // corpus is small enough to fit fully in memory, use a HashMap to cache all course data
    // cache is indexed by Course.id
    // a database could be alternatively be used for larger collections
    private final HashMap<Integer, Course> cache;

    // instantiate reader
    public MyIndexReader(AzureBlob azureBlob, RAMDirectory ramDirectory, Analyzer analyzer, HashMap<Integer, Course> cache) {
        this.azureBlob = azureBlob;
        this.analyzer = analyzer;
        this.cache = cache;
        try {
            this.indexReader = DirectoryReader.open(ramDirectory);
        } catch (IOException e) {
//            log.error("unable to create index reader because of" + e.getMessage());
        }

        if (this.indexReader == null) {
            throw new NullPointerException("unable to find indexReader");
        }
        this.searcher = new IndexSearcher(this.indexReader);
        this.searcher.setSimilarity(new ClassicSimilarity());
    }

    // main search function
    // searches all document fields
    public List<Course> searchDocument(String query, int topK) {
        List<Course> res = new LinkedList<>();
        if(query.equals("")) return res;

        try {
            // parse query, search
            String[] fieldsToSearch = new String[] { "dept", "number", "name", "description", "instructor"};
            this.query = new MultiFieldQueryParser(fieldsToSearch, analyzer).parse(query);
            TopDocs topDocs = this.searcher.search(this.query, topK);
            ScoreDoc[] hits = topDocs.scoreDocs;

            for(int i=0;i<hits.length;++i) {
                int docId = hits[i].doc;    // lucene docID
                // get stored fields from doc
                Document d = searcher.doc(docId);
                // get full Course data from cache for doc
                int id = Integer.parseInt(d.get("id"));
                Course course = cache.get(id);
                // build result, indexed by course ID
                course.setScore(hits[i].score);
                res.add(course);
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static void getInstance(AzureBlob azureBlob, RAMDirectory ramDirectory, Analyzer analyzer, HashMap<Integer, Course> cache) {
        if (myIndexReader == null)
            myIndexReader = new MyIndexReader(azureBlob, ramDirectory, analyzer, cache);

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

