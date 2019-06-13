package com.joe.preview.ui.custom;

public class SlideMenuItem {

    private String name;
    private int imageResolution;

    public SlideMenuItem(String name, int imageResolution) {
        this.name = name;
        this.imageResolution = imageResolution;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    int getImageResolution() {
        return imageResolution;
    }

    public void setImageResolution(int imageRes) {
        this.imageResolution = imageRes;
    }

}
