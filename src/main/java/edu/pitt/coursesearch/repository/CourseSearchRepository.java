package edu.pitt.coursesearch.repository;

import edu.pitt.coursesearch.helper.lucenehelper.MyIndexReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Slf4j
@Repository
public class CourseSearchRepository {

    private static final Analyzer analyzer = new StandardAnalyzer(CharArraySet.EMPTY_SET);

    private MyIndexReader myIndexReader = MyIndexReader.getInstance();

    public Map<String, JSONObject> getSearchResult(String query, String field) {
        return myIndexReader.searchDocument(query, field, 30);
    }

    public Map<String, JSONObject> getInstructorSearchRes(String query, String field){
        return myIndexReader.searchDocument(query, field, 30);
    }
}
