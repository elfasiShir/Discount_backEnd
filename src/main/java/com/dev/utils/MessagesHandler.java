package com.dev.utils;

import com.dev.Persist;

import com.dev.objects.DiscountObject;
import com.dev.objects.UserObject;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class MessagesHandler extends TextWebSocketHandler {

    private static List<WebSocketSession> sessionList = new CopyOnWriteArrayList<>();
    private static Map<String, WebSocketSession> sessionMap = new HashMap<>();

    @Autowired
    private Persist persist;


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        Map<String, String> map = Utils.splitQuery(session.getUri().getQuery());
        sessionMap.put(map.get("token"), session);
        sessionList.add(session);
        System.out.println(sessionMap.get("token") + session.toString());
        System.out.println("afterConnectionEstablished");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        System.out.println("handleTextMessage");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        sessionList.remove(session);
        System.out.println("afterConnectionClosed");

    }

/*

    public void sendStartDiscount() {
        List<DiscountObject> startDiscount = persist.getStartDiscount();
        String sOe="Starting";
        if (startDiscount != null) {
            for (DiscountObject start : startDiscount) {
                if (start.getValidForEveryone() != 1) {
                    List<UserObject> userObjects = persist.getUsersToSendDiscountNotification(start);
                            sender(userObjects,start,sOe);
                }else {
                    List<UserObject> userObjects = persist.getAllUsers();
                    sender(userObjects,start,sOe);}
            }
        } else {
            System.out.println("There Is No Discount That Starting Now");
        }
    }


    public void sendEndDiscount() {
        List<DiscountObject> endDiscount = persist.getEndSDiscount();

        String sOe="Starting";
        if (endDiscount != null) {
            for (DiscountObject end : endDiscount) {
                if (end.getValidForEveryone() != 1) {
                    List<UserObject> userObjects = persist.getUsersToSendDiscountNotification(end);
                    sender(userObjects,end,sOe);
                }else {
                    List<UserObject> userObjects = persist.getAllUsers();
                    sender(userObjects,end,sOe);}
            }
        } else {
            System.out.println("There Is No Discount That Ending Now");
        }
    }
*/

    public void sender(List<UserObject> userObjects, DiscountObject discount, String sOe){
        try {
            if (userObjects != null) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("discountText", discount.getDiscount());
                jsonObject.put("sOe", sOe);
                for (UserObject userObject : userObjects) {
                    sessionList.add(sessionMap.get(userObject.getToken()));
                    if (sessionMap.get(userObject.getToken()) != null)
                        sessionMap.get(userObject.getToken()).sendMessage(new TextMessage(jsonObject.toString()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendStartDiscountNotifications(Set<UserObject> users, DiscountObject discount) throws IOException {
        for (UserObject user : users) {
            WebSocketSession session = sessionMap.get(user.getToken());
            if (session != null) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("notification",
                        "There is a new sale at " + discount.getDiscountShop() + "! \n " +
                                discount.getDiscount());
                session.sendMessage(new TextMessage(jsonObject.toString()));

            }
        }
    }

    public void sendEndDiscountNotifications(Set<UserObject> users, DiscountObject sale) throws IOException {
        for (UserObject user : users) {
            WebSocketSession session = sessionMap.get(user.getToken());
            if (session != null) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("notification",
                        " Sale at :" + sale.getDiscountShop() + " is ending soon,   \n " +
                                sale.getDiscount());
                session.sendMessage(new TextMessage(jsonObject.toString()));

            }
        }
    }
}




