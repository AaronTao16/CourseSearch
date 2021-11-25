package edu.pitt.coursesearch.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Query {

    private String queryContent;
    private String topicId;

}

