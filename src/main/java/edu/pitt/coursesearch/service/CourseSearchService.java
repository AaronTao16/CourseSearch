package edu.pitt.coursesearch.service;

import edu.pitt.coursesearch.model.Course;
import edu.pitt.coursesearch.repository.CourseSearchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CourseSearchService {

    @Autowired
    CourseSearchRepository courseSearchRepository;

    public List<Course> getSearchResult(String query, String field) { return courseSearchRepository.getSearchResult(query, field); }

    public List<Course> getInstructorSearchRes(String query, String field) {return courseSearchRepository.getInstructorSearchRes(query, field);}
}
