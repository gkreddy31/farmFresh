package com.bhasaka.farmfresh.core.models;

public class PageCard {
    private String title;
    private String description;
    private String imagePath;

    public PageCard(String title, String description, String imagePath) {
        this.title = title;
        this.description = description;
        this.imagePath = imagePath;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImagePath() {
        return imagePath;
    }
}
