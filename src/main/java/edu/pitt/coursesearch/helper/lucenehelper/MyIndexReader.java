package edu.pitt.coursesearch.helper.lucenehelper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

import com.microsoft.azure.storage.StorageException;
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

    public Map<String, String> course;
    public List<String> OriginalList;

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

        course = new HashMap<>();
        OriginalList = new ArrayList<>();

        readData();
//        System.out.println(course);
    }

    private void readData() {
        try {
            String content = azureBlob.readFiles("after_normalize_original");
            OriginalList = Arrays.asList(content.split("\n"));
            for(String c: OriginalList){
                String[] list = c.split(" ");
                course.put(list[0].trim(), Arrays.stream(list).skip(1).collect(Collectors.joining(" ")));
                if(list[0].trim().equals("16")){
                    System.out.println(Arrays.stream(list).skip(1).collect(Collectors.joining(" ")));
                }
            }
        } catch (URISyntaxException | StorageException | IOException e ){

        }
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

    public Map<String, String> searchDocument(String query, String field, int topK) {
        Map<String, String> res = new LinkedHashMap<>();

        try {
            this.query = new QueryParser(field, analyzer).parse(query);
            TopDocs topDocs = this.searcher.search(this.query, topK);
            ScoreDoc[] hits = topDocs.scoreDocs;

            System.out.println(hits.length);
            for(int i=0;i<hits.length;++i) {
                int docId = hits[i].doc;
                Document d = searcher.doc(docId);
                String jsonObject = course.get(d.get("id"));
//                jsonObject.put("score", hits[i].score);
                res.put(d.get("id"), jsonObject);
//                res.put(d.get("id"), hits[i].score);
                System.out.println((i + 1) + ". " + d.get("id") + "\t" + d.get("content"));
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return res;
    }


    public void close() throws IOException {
        // you should implement this method when necessary
        ireader.close();
    }

    public IndexReader getIndexReader() {
        return indexReader;
    }
}

