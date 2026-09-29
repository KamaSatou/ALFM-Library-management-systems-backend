package database.example.oracleapi.dto;

import java.util.ArrayList;
import java.util.List;

public class AiSearchIntent {

    private List<String> keywords = new ArrayList<>();
    private List<String> tags = new ArrayList<>();
    private String category = "";
    private String explanation = "";

    public AiSearchIntent() {
    }

    public AiSearchIntent(
            List<String> keywords,
            List<String> tags,
            String category,
            String explanation
    ) {
        this.keywords = keywords;
        this.tags = tags;
        this.category = category;
        this.explanation = explanation;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}