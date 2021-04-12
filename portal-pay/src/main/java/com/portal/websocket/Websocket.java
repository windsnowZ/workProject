package com.portal.websocket;

import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/websocket/{orderId}")
public class Websocket {

       private static ConcurrentHashMap<String,Session> sessions = new ConcurrentHashMap();

       @OnOpen
       public void onOpen(@PathParam("orderId") String orderId, Session session){

           System.out.println("已经与客户端建立连接");
           //当前端建立连接，存储会话信息
           sessions.put(orderId,session);
       }



       @OnClose
       public void onClose(@PathParam("orderId") String orderId){
           System.out.println("与客户端关闭了连接 bye bye");
           sessions.remove(orderId);

       }
    
      public static void sendMessageToClient(String orderId,String message) throws  Exception{
           Session session = sessions.get(orderId);
           session.getBasicRemote().sendText(message);
       }

} 

