package edu.pitt.coursesearch.model;
import java.util.List;

public class SearchResult {

    private List<Course> courseList;
    private List<Facet> facetResultList;

    public SearchResult(List<Course> courseList, List<Facet> facetResultList) {
        this.courseList = courseList;
        this.facetResultList = facetResultList;
    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public List<Facet> getFacetResultList() {
        return facetResultList;
    }
}
