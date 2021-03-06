package edu.pitt.coursesearch.repository;

import edu.pitt.coursesearch.helper.lucenehelper.MyIndexReader;
import edu.pitt.coursesearch.model.Course;
import edu.pitt.coursesearch.model.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class CourseSearchRepository {

    private MyIndexReader myIndexReader = MyIndexReader.getInstance();

    public SearchResult getSearchResult(String query) {
        return myIndexReader.searchDocument(query, 20);
    }

    public SearchResult getDrillDownResults(String query, String[] facets) {
        return myIndexReader.drillDownSearch(query,20, facets);
    }

}
