package edu.pitt.coursesearch.helper.lucenehelper;

import java.io.IOException;
import java.util.*;
import java.util.Formatter;

import edu.pitt.coursesearch.helper.azurehelper.AzureBlob;
import edu.pitt.coursesearch.model.Course;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
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
    private int skipPage = 0;

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
    public List<Course> searchDocument(String query, int topK, int page) {
        List<Course> res = new LinkedList<>();
        if(query.equals("")) return res;

        try {
            // parse query, search
            String[] fieldsToSearch = new String[] { "dept", "number", "name", "description", "instructor"};
            this.query = new MultiFieldQueryParser(fieldsToSearch, analyzer).parse(query);
            TopDocs topDocs = this.searcher.search(this.query, Math.min(page*10+1, topK));
            ScoreDoc[] hits = topDocs.scoreDocs;

            /** Highlighter Code Start ****/

            //Uses HTML &lt;B&gt;&lt;/B&gt; tag to highlight the searched terms
            SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter("<mark style='background-color: yellow;'>", "</mark>");

            //It scores text fragments by the number of unique query terms found
            //Basically the matching score in layman terms
            QueryScorer scorer = new QueryScorer(this.query);

            //used to markup highlighted terms found in the best sections of a text
            Highlighter highlighter = new Highlighter(htmlFormatter, scorer);

            //It breaks text up into same-size texts but does not split up spans
            Fragmenter fragmenter = new SimpleSpanFragmenter(scorer, 100);

            //set fragmenter to highlighter
            highlighter.setTextFragmenter(fragmenter);

            for(int i=(page-1)*10;i<hits.length;++i) {
                int docId = hits[i].doc;    // lucene docID
                // get stored fields from doc
                Document d = searcher.doc(docId);
                // get full Course data from cache for doc
                int id = Integer.parseInt(d.get("id"));
                Course course = (Course) cache.get(id).clone();
                // build result, indexed by course ID
                course.setScore(hits[i].score);

                //Create token stream
                TokenStream desStream = TokenSources.getAnyTokenStream(indexReader, docId, "description", analyzer);

                //Get highlighted text fragments
                String[] desFrag = highlighter.getBestFragments(desStream, course.getDescription(), 100);
                course.setHighlightFrag(String.join("...", desFrag));
                desStream.reset();
                desStream.close();


                TokenStream deptStream = TokenSources.getAnyTokenStream(indexReader, docId, "dept", analyzer);
                String[] deptFrag = highlighter.getBestFragments(deptStream, course.getDept(), 10);
                course.setDept(String.join(" ", deptFrag));
                deptStream.reset();
                deptStream.close();

                TokenStream numberStream = TokenSources.getAnyTokenStream(indexReader, docId, "number", analyzer);
                String[] numberFrag = highlighter.getBestFragments(numberStream, course.getNumber()+"", 10);
                course.setNumber(String.join(" ", numberFrag));
                numberStream.reset();
                numberStream.close();

                TokenStream nameStream = TokenSources.getAnyTokenStream(indexReader, docId, "name", analyzer);
                String[] nameFrag = highlighter.getBestFragments(nameStream, course.getName(), 10);
                course.setName(String.join(" ", nameFrag));
                nameStream.reset();
                nameStream.close();

                TokenStream instructorStream = TokenSources.getAnyTokenStream(indexReader, docId, "instructor", analyzer);
                String[] instructorFrag = highlighter.getBestFragments(instructorStream, course.getInstructor(), 10);
                course.setInstructor(String.join(" ", instructorFrag));
                instructorStream.reset();
                instructorStream.close();

                res.add(course);
            }

        } catch (IOException | ParseException | CloneNotSupportedException | InvalidTokenOffsetsException e) {
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

