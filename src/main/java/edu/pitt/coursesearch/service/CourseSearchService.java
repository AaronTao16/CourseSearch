package edu.pitt.coursesearch.service;

import edu.pitt.coursesearch.model.Course;
import edu.pitt.coursesearch.repository.CourseSearchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CourseSearchService {

    @Autowired
    CourseSearchRepository courseSearchRepository;

    public List<Course> getSearchResult(String query) {

//        System.out.println(strList.length);
        if(!query.contains(":"))
            return courseSearchRepository.getSearchResult(query);
        else {
            String[] strList = query.split(";");
            List<String> fields = new ArrayList<>();
            StringBuilder newQ = new StringBuilder();
            for(String str: strList){
                fields.add(str.split(":")[0]);
                newQ.append(str.split(":")[1]).append(" ");
            }
            String[] res = new String[fields.size()];
            for (int i = 0; i < res.length; i++) {
                res[i] = fields.get(i);
            }
            return courseSearchRepository.getSearchResult(newQ.toString(), res);
        }
    }

}
