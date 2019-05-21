package com.server.gradleServer.chatController;

public class User {
    private String id;
    private  String name;

    public User(String _id, String _name){
        this.id = _id;
        this.name = _name;
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
