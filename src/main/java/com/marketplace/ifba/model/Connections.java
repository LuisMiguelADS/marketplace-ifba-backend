package com.marketplace.ifba.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Connections {
    private UUID idConnections;
    private User userConnected;
    private LocalDateTime dateConnected;

    public Connections(UUID idConnections, User userConnected, LocalDateTime dateConnected) {
        this.idConnections = idConnections;
        this.userConnected = userConnected;
        this.dateConnected = dateConnected;
    }

    public UUID getIdConnections() {
        return idConnections;
    }

    public void setIdConnections(UUID idConnections) {
        this.idConnections = idConnections;
    }

    public User getUserConnected() {
        return userConnected;
    }

    public void setUserConnected(User userConnected) {
        this.userConnected = userConnected;
    }

    public LocalDateTime getDateConnected() {
        return dateConnected;
    }

    public void setDateConnected(LocalDateTime dateConnected) {
        this.dateConnected = dateConnected;
    }
}
