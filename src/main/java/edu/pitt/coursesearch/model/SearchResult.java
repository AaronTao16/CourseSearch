package edu.pitt.coursesearch.model;

import org.apache.lucene.facet.FacetResult;
import java.util.List;

public class SearchResult {

    private List<Course> courseList;
    private List<FacetResult> facetResultList;

    public SearchResult(List<Course> courseList, List<FacetResult> facetResultList) {
        this.courseList = courseList;
        this.facetResultList = facetResultList;
    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public List<FacetResult> getFacetResultList() {
        return facetResultList;
    }
}
