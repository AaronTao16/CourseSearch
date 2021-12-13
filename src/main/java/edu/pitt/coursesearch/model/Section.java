package edu.pitt.coursesearch.model;
import java.util.ArrayList;

public class Section {

    private int classNumber;
    private ArrayList<String> days;
    private String beginTime;
    private String endTime;
    private String sectionType;
    private String building;
    private String room;

    public Section(int classNumber, ArrayList<String> days, String beginTime, String endTime, String sectionType, String building, String room) {
        this.classNumber = classNumber;
        this.days = days;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.sectionType = sectionType;
        this.building = building;
        this.room = room;
    }

    public int getClassNumber() {
        return classNumber;
    }

    public ArrayList<String> getDays() {
        return days;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getSectionType() {
        return sectionType;
    }

    public String getBuilding() {
        return building;
    }

    public String getRoom() {
        return room;
    }

}
