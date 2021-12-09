package edu.pitt.coursesearch.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MyDocument {

    protected String docid;
    protected double score;

    @Override
    public String toString(){
        return docid + ": " + score;
    }

}
