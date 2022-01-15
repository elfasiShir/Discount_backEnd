package com.dev.controllers;

import com.dev.Persist;
import com.dev.objects.UserObject;
import com.dev.utils.MessagesHandler;
import com.dev.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@RestController
public class TestController {

    @Autowired
    private Persist persist;
    private MessagesHandler messagesHandler;


    public TestController(MessagesHandler messagesHandler) {
        this.messagesHandler = messagesHandler;
    }


    @PostConstruct
    private void init () {
        persist.createConnectionToDatabase();
    }

    //הרשמה
    @RequestMapping(value ="sign-up", method = RequestMethod.POST)
    public boolean signUn (@RequestParam String username, String password) {
        return persist.signUp(username, password);
    }
    //להתחבר
    @RequestMapping(value = "log-in")
    public String logIn (String username , String password){
        return persist.logIn(username,password);
    }


    @RequestMapping("doesUsernameExists")
    public boolean doesUsernameExists(String username){
        return persist.doesUsernameExists(username);
    }

    @RequestMapping("create-account")
    public boolean createAccount (String username, String password) {
        boolean success = false;
        if(!persist.doesUsernameExists(username)){
            UserObject userObjects = new UserObject();
            userObjects.setUsername(username);
            userObjects.setPassword(password);
            String hash = Utils.createHash(username, password);
            userObjects.setToken(hash);
            success = persist.createAccount(userObjects);
        }
        return success;
    }


    @RequestMapping ("delete_message")
    public boolean deleteMessageById(int messageId){
        return persist.deleteMessageById(messageId);
    }

    @RequestMapping ("Message_was_read")
    public boolean MessageWasRead(int messageId) {
        return persist.MessageWasRead(messageId);
    }

    @RequestMapping("add-message")
    public boolean addMessage(String token,String receiverPhone,  String title,String content) {
        return persist.addMessage(token,receiverPhone,title,content);
    }
    @RequestMapping("countDownTries")
    public void countDownTries(String username){
        persist.countDownTries(username);
    }

    @RequestMapping("isBlocked")
    public int isBlocked(String username){
        return persist.isBlocked(username);
    }
    @RequestMapping("updateLoginTries")
    public void updateLoginTries(String username){
        persist.updateLoginTries(username);
    }

}
