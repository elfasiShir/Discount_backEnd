package com.dev.objects;

import javax.persistence.*;
import java.util.List;

@Entity
@Table (name = "users")
public class UserObject {
    @Column(name = "id")
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "token")
    private String token;

    @Column(name = "login_tries")
    private int login_tries;

    @Transient
    private List<MessageObject> messages;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void addMessages (String message) {
            addMessages(message);
    }

    public List<MessageObject> getPosts() {
        return messages;
    }

    public void setPosts(List<MessageObject> posts) {
        this.messages = posts;
    }
}
