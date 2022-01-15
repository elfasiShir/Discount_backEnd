package com.dev.objects;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class UserObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    public int userId;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String token;

    @Column
    private int login_tries ;




    @ManyToMany
    @JoinTable (name = "user_organization", joinColumns = {@JoinColumn(name="UserId")},
    inverseJoinColumns = {@JoinColumn(name = "organizationId")})
    Set<OrganizationObject> organizations = new HashSet<>();

    public UserObject(String username, String password, String token) {
        this.username= username;
        this.password= password;
        this.token = token;
        this.login_tries = 5;
    }

    public UserObject() {

    }


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

    public void addPost (String post) {
    }



    public int getUserId() {
        return userId;
    }

    public void setUserId(int UserId) {
        this.userId = UserId;
    }


    public int getLogin_tries() {
        return login_tries;
    }

    public void setLogin_tries(int login_tries) {
        this.login_tries = login_tries;
    }


}
