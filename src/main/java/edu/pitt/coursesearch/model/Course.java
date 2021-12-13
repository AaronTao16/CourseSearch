package edu.pitt.coursesearch.model;
import java.util.ArrayList;

public class Course {

    private int id;
    private String dept;
    private int number;
    private String name;
    private String description;
    private String instructor;
    private boolean grad;
    private ArrayList<String> required;
    private ArrayList<String> elective;
    private ArrayList<Section> sections;
    private float score;

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

    public int getId() {
        return id;
    }

    public String getDept() {
        return dept;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getInstructor() {
        return instructor;
    }

    public boolean isGrad() {
        return grad;
    }

    public ArrayList<String> getRequired() {
        return required;
    }

    public ArrayList<String> getElective() {
        return elective;
    }

    public ArrayList<Section> getSections() {
        return sections;
    }


    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
