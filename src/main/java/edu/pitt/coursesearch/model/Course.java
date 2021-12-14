package edu.pitt.coursesearch.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course implements Cloneable{

    private int id;
    private String dept;
    private int number;
    private String name;
    private String description;
    private String highlightFrag;
    private String instructor;
    private boolean grad;
    private ArrayList<String> required;
    private ArrayList<String> elective;
    private ArrayList<Section> sections;
    private float score;
    private Set<String> days;


    public Course(int id, String dept, int number, String name, String description, String instructor, boolean grad, ArrayList<String> required, ArrayList<String> elective, ArrayList<Section> sections) {
        this.id = id;
        this.dept = dept;
        this.number = number;
        this.name = name;
        this.description = description;
        this.instructor = instructor;
        this.grad = grad;
        this.required = required;
        this.elective = elective;
        this.sections = sections;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();    // return shallow copy
    }
}
