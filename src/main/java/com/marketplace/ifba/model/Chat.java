package com.marketplace.ifba.model;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class Chat {
    private UUID idChat;
    private Date dateCreatedChat;
    private Date dateFinishedChat;
    private HashMap<LocalDateTime, User> userAndLastViewChat;
    private Message messages;

    public Chat(UUID idChat, Date dateCreatedChat, Date dateFinishedChat, Message messages) {
        this.idChat = idChat;
        this.dateCreatedChat = dateCreatedChat;
        this.dateFinishedChat = dateFinishedChat;
        this.userAndLastViewChat = new HashMap<>();
        this.messages = messages;
    }

    public UUID getIdChat() {
        return idChat;
    }

    public void setIdChat(UUID idChat) {
        this.idChat = idChat;
    }

    public Date getDateCreatedChat() {
        return dateCreatedChat;
    }

    public void setDateCreatedChat(Date dateCreatedChat) {
        this.dateCreatedChat = dateCreatedChat;
    }

    public Date getDateFinishedChat() {
        return dateFinishedChat;
    }

    public void setDateFinishedChat(Date dateFinishedChat) {
        this.dateFinishedChat = dateFinishedChat;
    }

    public HashMap<LocalDateTime, User> getUserAndLastViewChat() {
        return userAndLastViewChat;
    }

    public void setUserAndLastViewChat(HashMap<LocalDateTime, User> userAndLastViewChat) {
        this.userAndLastViewChat = userAndLastViewChat;
    }

    public Message getMessages() {
        return messages;
    }

    public void setMessages(Message messages) {
        this.messages = messages;
    }
}
