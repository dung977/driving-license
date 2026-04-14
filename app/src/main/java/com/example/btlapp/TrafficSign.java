package com.example.btlapp;

public class TrafficSign {
    private String name;
    private String description;
    private int imageResId;
    private String imageName; // For loading from assets
    private String category;

    public TrafficSign(String name, String description, int imageResId, String category) {
        this.name = name;
        this.description = description;
        this.imageResId = imageResId;
        this.category = category;
    }

    public TrafficSign(String name, String description, String imageName, String category) {
        this.name = name;
        this.description = description;
        this.imageName = imageName;
        this.category = category;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getImageResId() { return imageResId; }
    public String getImageName() { return imageName; }
    public String getCategory() { return category; }
}
