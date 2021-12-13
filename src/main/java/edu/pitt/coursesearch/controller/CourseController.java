package edu.pitt.coursesearch.controller;

import org.springframework.ui.ModelMap;
import edu.pitt.coursesearch.service.CourseSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/course")
public class CourseController {

    @Autowired
    CourseSearchService courseSearchService;

    @GetMapping("")
    public void searchDashboard() {}

    @GetMapping("/search")
    public String Query(@RequestParam("query") final String query, ModelMap modelMap){
        modelMap.put("courseList", courseSearchService.getSearchResult(query));

        return "srp";
    }

}
