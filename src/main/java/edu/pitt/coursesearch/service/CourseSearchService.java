package edu.pitt.coursesearch.service;

import edu.pitt.coursesearch.repository.CourseSearchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class CourseSearchService {

    @Autowired
    CourseSearchRepository courseSearchRepository;

    public Map<String, Double> getSearchResult(String query) {
        return courseSearchRepository.getSearchResult(query);
    }
}
