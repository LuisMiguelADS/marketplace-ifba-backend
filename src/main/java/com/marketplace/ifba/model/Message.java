package com.marketplace.ifba.model;

import java.util.Date;
import java.util.UUID;

public class Message {
    private UUID idMessage;
    private String message;
    private Date dateMessage;
    private Boolean active;
    private User writingUser;

    public Message(UUID idMessage, String message, Date dateMessage, Boolean active, User writingUser) {
        this.idMessage = idMessage;
        this.message = message;
        this.dateMessage = dateMessage;
        this.active = active;
        this.writingUser = writingUser;
    }

    public UUID getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(UUID idMessage) {
        this.idMessage = idMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDateMessage() {
        return dateMessage;
    }

    public void setDateMessage(Date dateMessage) {
        this.dateMessage = dateMessage;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public User getWritingUser() {
        return writingUser;
    }

    public void setWritingUser(User writingUser) {
        this.writingUser = writingUser;
    }
}
