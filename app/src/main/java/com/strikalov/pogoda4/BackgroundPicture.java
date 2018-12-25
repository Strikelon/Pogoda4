package com.strikalov.pogoda4;

public class BackgroundPicture {

    private int id;
    private String name;
    private int imageResourceId;
    private boolean isChecked;

    public BackgroundPicture(int id, String name, int imageResourceId, boolean isChecked){

        this.id = id;
        this.name = name;
        this.imageResourceId = imageResourceId;
        this.isChecked = isChecked;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
