package edu.pitt.coursesearch.model;

import java.util.Map;

// getters and empty constructor required for JSP usage
public class Facet {

    // the main facet category (e.g. "grad" or "required")
    private String category;
    // labels in this facet and their respective document counts
    // label -> count (e.g. "false":5, "true":6, "Information Science":19)
    private Map<String, Integer> labelValues;

    public Facet(String category, Map<String, Integer> labelValues) {
        this.category = category;
        this.labelValues = labelValues;
    }

    public Facet() {
    }

    public String getCategory() {
        return category;
    }

    public Map<String, Integer> getLabelValues() {
        return labelValues;
    }
}
