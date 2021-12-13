package edu.pitt.coursesearch.controller;

import edu.pitt.coursesearch.service.CourseSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/dashboard")
public class ViewController {
    @Autowired
    CourseSearchService courseSearchService;

    @RequestMapping("")
    public String searchDashboard(Model model) {
        Map<String, String> map = new HashMap<>();
        model.addAllAttributes(map);
        return "index";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String searchResult(@RequestParam("query") final String query) {
        System.out.println(query);
//        Map<String, String> map = new HashMap<>();
//        model.addAllAttributes(map);
        return "index";
    }
}
