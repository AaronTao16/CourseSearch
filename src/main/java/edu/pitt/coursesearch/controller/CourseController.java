package edu.pitt.coursesearch.controller;

import edu.pitt.coursesearch.model.SearchResult;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import edu.pitt.coursesearch.service.CourseSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("")
public class CourseController {

    @Autowired
    CourseSearchService courseSearchService;

    @GetMapping("/dashboard")
    public String searchDashboard(Model model) {
        return "index";
    }

    @GetMapping("/search")
    public String Query(@RequestParam("query") final String query, @RequestParam(value = "facet", required = false) final String[] facets, ModelMap modelMap){
        if (facets == null) {
            // non-faceted initial search
            SearchResult result = courseSearchService.getSearchResult(query);
            modelMap.put("courseList", result.getCourseList());
            modelMap.put("facetList", result.getFacetResultList());
        }
        else {
            // faceted search
            SearchResult result = courseSearchService.getDrillDownResults(query, facets);
            modelMap.put("courseList", result.getCourseList());
            modelMap.put("facetList", result.getFacetResultList());
        }

        return "srp";
    }

}
