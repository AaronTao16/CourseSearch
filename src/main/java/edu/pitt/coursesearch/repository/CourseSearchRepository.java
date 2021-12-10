package edu.pitt.coursesearch.repository;

import edu.pitt.coursesearch.helper.lucenehelper.MyIndexReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
public class CourseSearchRepository {

    private static final Analyzer analyzer = new StandardAnalyzer(CharArraySet.EMPTY_SET);

    private MyIndexReader myIndexReader = MyIndexReader.getInstance();

    public Map<String, Double> getSearchResult(String query) {
        Map<String, Double> res = new HashMap<>();
        try {
            Query q = new QueryParser("content", analyzer).parse(query);
            IndexSearcher searcher = new IndexSearcher(myIndexReader.getIndexReader());
            TopDocs docs = searcher.search(q, 30);

            ScoreDoc[] hits = docs.scoreDocs;
            for(int i=0;i<hits.length;++i) {
                int docId = hits[i].doc;
                Document d = searcher.doc(docId);
                res.put(d.get("id"), (double) hits[i].score);
                System.out.println((i + 1) + ". " + d.get("id") + "\t" + d.get("content"));
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return res;
    }
}
