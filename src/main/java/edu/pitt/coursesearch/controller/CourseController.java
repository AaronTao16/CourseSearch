package edu.pitt.coursesearch.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coursedata")
public class CourseController {

    @ResponseBody
    @GetMapping("/")
    public String getCourses(Model model){
//        model.addAllAttributes("list", "123");
        return "Portal";
    }

}
