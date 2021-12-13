package edu.pitt.coursesearch.repository;

import edu.pitt.coursesearch.helper.lucenehelper.MyIndexReader;
import edu.pitt.coursesearch.model.Course;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class CourseSearchRepository {

    private static final Analyzer analyzer = new StandardAnalyzer(CharArraySet.EMPTY_SET);

    private MyIndexReader myIndexReader = MyIndexReader.getInstance();

    public List<Course> getSearchResult(String query) {
        return myIndexReader.searchDocument(query, 20);
    }

    public List<Course> getSearchResult(String query, String[] field) { return myIndexReader.searchDocument(query, field,20);
    }

}
