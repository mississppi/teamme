package com.ropes.db;

import com.ropes.models.PostsModel;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.sql.*;

public class SqliteMain {
    public static Integer insertPost(String title) throws SQLException, ClassNotFoundException{
        Integer result = SqliteDAO.createPost(title);
        return result;
    }
    public static Integer removePost(Integer postId) throws SQLException, ClassNotFoundException{
        Integer result = SqliteDAO.deletePost(postId);
        return result;
    }

    //1件取得
    public static ObservableList<PostsModel> getPost(Integer postId) throws SQLException, ClassNotFoundException{
        ResultSet res = SqliteDAO.readPost(postId);
        ObservableList<PostsModel> post = null;
        if(res != null){
            post = toPostsEntity(res);
        }
        return post;
    }

    //全件取得
    public static ObservableList<PostsModel> getPosts() throws SQLException, ClassNotFoundException{
        ResultSet res = SqliteDAO.readPosts();
        ObservableList<PostsModel> posts = null;
        if(res != null){
            posts = toPostsEntity(res);
        }
        return posts;
    }

    public static ObservableList<PostsModel> toPostsEntity(ResultSet rs) throws SQLException, ClassNotFoundException{
        ObservableList<PostsModel> posts = FXCollections.observableArrayList();
        while(rs.next()) {
            posts.add(new PostsModel(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("content"),
                    rs.getString("created_at")
            ));
        }
//        data.forEach(d -> System.out.println(d.getId() + " : "+ d.getContent() + " : " + d.getCreated_at()));
        return posts;
    }
}
