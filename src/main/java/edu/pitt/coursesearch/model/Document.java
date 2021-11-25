package edu.pitt.coursesearch.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Document {

    protected String docid;
    protected String docno;
    protected double score;


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Document){
            Document doc = (Document)obj;
            return docno.equals(doc.getDocno())&&docid.equals(doc.getDocid());
        }
        return false;
    }

}
