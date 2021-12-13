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
        StringBuilder result = new StringBuilder();
//        for (Map.Entry<String, Course> map: courseSearchService.getSearchResult(query, "content").entrySet()){
//            System.out.println(map.getValue());
//            result.append(map.getValue()).append("\n");
//        }
        return "srp";
    }

    @GetMapping("/searchInstructor")
    public String QueryInstructor(@RequestParam("instructor") final String query, ModelMap modelMap){
//        for (Map.Entry<String, String> map: courseSearchService.getInstructorSearchRes(query, "instructor").entrySet()){
//            result.append(map.getKey()).append(": ").append(map.getValue().toString()).append("\n");
//        }

//        return result.toString();
        modelMap.put("courseList", courseSearchService.getInstructorSearchRes(query, "instructor"));
        return "srp";
    }

}
