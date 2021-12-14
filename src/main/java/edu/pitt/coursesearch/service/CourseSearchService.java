package edu.pitt.coursesearch.service;

import edu.pitt.coursesearch.model.Course;
import edu.pitt.coursesearch.model.SearchResult;
import edu.pitt.coursesearch.repository.CourseSearchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CourseSearchService {

    @Autowired
    CourseSearchRepository courseSearchRepository;

    public SearchResult getSearchResult(String query) {
        return courseSearchRepository.getSearchResult(query);
    }

    public SearchResult getDrillDownResults(String query, String[] facets) {
        return courseSearchRepository.getDrillDownResults(query, facets);
    }

}
