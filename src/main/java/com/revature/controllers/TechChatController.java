package com.revature.controllers;

import com.revature.models.ChatMessage;
import com.revature.models.OutputMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TechChatController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/secured/room")
    public void sendSpecific(
            @Payload ChatMessage msg,
            Principal user,
            @Header("simpSessionId") String sessionId) throws Exception {
        OutputMessage out = new OutputMessage(
                msg.getSenderId(),
                msg.getContent(),
                new SimpleDateFormat("HH:mm").format(new Date()));
        simpMessagingTemplate.convertAndSendToUser(
                msg.getRecipientId(), "/queue/specific-user", out);
    }

}