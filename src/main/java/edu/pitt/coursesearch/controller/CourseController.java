package edu.pitt.coursesearch.controller;

import edu.pitt.coursesearch.model.Course;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import edu.pitt.coursesearch.service.CourseSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        modelMap.put("courseList", courseSearchService.getSearchResult(query));
        return "srp";
    }

    @GetMapping("/level")
    public List<Course> QueryLevel(@RequestParam("query") final String query, ModelMap modelMap){
//        modelMap.put("courseList", courseSearchService.getSearchResult(query));
        return courseSearchService.getSearchResult(query);
    }

}
