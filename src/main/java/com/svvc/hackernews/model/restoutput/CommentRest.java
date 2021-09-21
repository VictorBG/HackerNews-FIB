package com.svvc.hackernews.model.restoutput;

public class CommentRest {

    private Long parentId;
    private String text;

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
