package com.myapp.footballstream;
public class Favorite {
    private final String name, image, id;

    public Favorite(String name, String image, String id){
        this.name = name;
        this.image = image;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getId() {
        return id;
    }
}
