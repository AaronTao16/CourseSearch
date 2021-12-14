package edu.pitt.coursesearch.controller;

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
    public String Query(@RequestParam("query") final String query, ModelMap modelMap){
        modelMap.put("courseList", courseSearchService.getSearchResult(query).getCourseList());
        // TODO put facet list
        return "srp";
    }

}
