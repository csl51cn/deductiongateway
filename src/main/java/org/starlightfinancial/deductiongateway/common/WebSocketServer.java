package org.starlightfinancial.deductiongateway.common;

import org.apache.tomcat.websocket.WsSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;


@ServerEndpoint(value = "/websocket")
@Component
@EnableScheduling
public class WebSocketServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketServer.class);


    /**
     * session
     */
    private static CopyOnWriteArraySet<Session> webSocketSessions = new CopyOnWriteArraySet<>();


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {

        //判断用户是否是需要推送的用户,如果是,保存session
        WsSession wsSession = (WsSession) session;
        webSocketSessions.add(wsSession);
    }


    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        //从set中删除
        webSocketSessions.remove(session);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        LOGGER.info("来自客户端的消息:" + message);
        System.out.println(message);
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        LOGGER.error("发生错误");
        error.printStackTrace();
    }


    /**
     * 群发自定义消息
     */
    public static void sendInfo(String message) {
        for (Session session : webSocketSessions) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断session是否关闭,如果已经关闭,从set中移除
     */
    @Scheduled(cron = "01 01 06-21 * * ? ")
    private void isAlive() {
        webSocketSessions.removeIf(session -> !session.isOpen());
    }


}
