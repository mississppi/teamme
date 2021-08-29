package com.ropes.models;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class PostsModel {
    public final SimpleIntegerProperty id;
    public final SimpleStringProperty title;
    public final SimpleStringProperty content;
    public final SimpleStringProperty created_at;

    public PostsModel(int id, String title, String content, String created_at){
        this.id = new SimpleIntegerProperty(id);
        this.title = new SimpleStringProperty(title);
        this.content = new SimpleStringProperty(content);
        this.created_at = new SimpleStringProperty(created_at);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getContent() {
        return content.get();
    }

    public SimpleStringProperty contentProperty() {
        return content;
    }

    public void setContent(String content) {
        this.content.set(content);
    }

    public String getCreated_at() {
        return created_at.get();
    }

    public SimpleStringProperty created_atProperty() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at.set(created_at);
    }
}
