package edu.pitt.coursesearch.controller;

import edu.pitt.coursesearch.service.CourseSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    CourseSearchService courseSearchService;

    @GetMapping("")
    public void getCourses() {

    }

    @GetMapping("/search")
    public String Query(@RequestParam("query") final String query){
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, Double> map: courseSearchService.getSearchResult(query).entrySet()){
            result.append(map.getKey()).append(": ").append(map.getValue()).append("\n");
        }
        return result.toString();
    }

}
