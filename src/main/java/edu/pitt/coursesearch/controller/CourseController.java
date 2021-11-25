package edu.pitt.coursesearch.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CourseController {

    @ResponseBody
    @RequestMapping("/")
    public String getCourses(Model model){
//        model.addAllAttributes("list", "123");
        return "Portal";
    }

}
