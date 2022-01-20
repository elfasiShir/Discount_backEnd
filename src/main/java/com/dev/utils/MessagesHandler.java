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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class MessagesHandler extends TextWebSocketHandler {

    private static List<WebSocketSession> sessionList = new CopyOnWriteArrayList<>();
    private static Map<String, WebSocketSession> sessionMap = new HashMap<>();

    @Autowired
    private Persist persist;
    private List<UserObject> userObjectList;
    private List<DiscountObject> discountStart;
    private List<DiscountObject> discountEnd;

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

}