package com.elitecrm.rcclient.entity;

/**
 * Created by Loriling on 2017/2/9.
 */

public class User {
    private String id;
    private String name;
    private String icon;

    public User() {

    }

    public User(String id, String name, String icon) {

    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
