package com.uac503.fieldlab.model;


public class TheoryTopic {
    private int id;
    private String title;
    private String categoryLocation;
    private String contentText;
    private String formulaText;
    private boolean isExpanded;


    public TheoryTopic(int id, String title, String categoryLocation, String contentText, String formulaText) {
        this.id = id;
        this.title = title;
        this.categoryLocation = categoryLocation;
        this.contentText = contentText;
        this.formulaText = formulaText;
        this.isExpanded = false;
    }


    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getCategoryLocation() { return categoryLocation; }
    public String getContentText() { return contentText; }
    public String getFormulaText() { return formulaText; }
    public boolean isExpanded() { return isExpanded; }
    public void setExpanded(boolean expanded) { isExpanded = expanded; }
}