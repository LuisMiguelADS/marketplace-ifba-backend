package com.marketplace.ifba.model;

import java.util.Date;
import java.util.UUID;

public class OfferSolution {
    private UUID idOfferSolution;
    private String title;
    private String description;
    private Number daysTerm;
    private String resum;
    private Date dateAprovation;
    private Date dateRegistration;
    private String status;
    private Boolean aprovation;
    private String typeSolution;
    private String restrictions;
    private Double estimatedPrice;
    private String necessaryResources;

    public OfferSolution(UUID idOfferSolution, String title, String description, Number daysTerm, String resum, Date dateAprovation, Date dateRegistration, String status, Boolean aprovation, String typeSolution, String restrictions, Double estimatedPrice, String necessaryResources) {
        this.idOfferSolution = idOfferSolution;
        this.title = title;
        this.description = description;
        this.daysTerm = daysTerm;
        this.resum = resum;
        this.dateAprovation = dateAprovation;
        this.dateRegistration = dateRegistration;
        this.status = status;
        this.aprovation = aprovation;
        this.typeSolution = typeSolution;
        this.restrictions = restrictions;
        this.estimatedPrice = estimatedPrice;
        this.necessaryResources = necessaryResources;
    }

    public UUID getIdOfferSolution() {
        return idOfferSolution;
    }

    public void setIdOfferSolution(UUID idOfferSolution) {
        this.idOfferSolution = idOfferSolution;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Number getDaysTerm() {
        return daysTerm;
    }

    public void setDaysTerm(Number daysTerm) {
        this.daysTerm = daysTerm;
    }

    public String getResum() {
        return resum;
    }

    public void setResum(String resum) {
        this.resum = resum;
    }

    public Date getDateAprovation() {
        return dateAprovation;
    }

    public void setDateAprovation(Date dateAprovation) {
        this.dateAprovation = dateAprovation;
    }

    public Date getDateRegistration() {
        return dateRegistration;
    }

    public void setDateRegistration(Date dateRegistration) {
        this.dateRegistration = dateRegistration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getAprovation() {
        return aprovation;
    }

    public void setAprovation(Boolean aprovation) {
        this.aprovation = aprovation;
    }

    public String getTypeSolution() {
        return typeSolution;
    }

    public void setTypeSolution(String typeSolution) {
        this.typeSolution = typeSolution;
    }

    public String getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(String restrictions) {
        this.restrictions = restrictions;
    }

    public Double getEstimatedPrice() {
        return estimatedPrice;
    }

    public void setEstimatedPrice(Double estimatedPrice) {
        this.estimatedPrice = estimatedPrice;
    }

    public String getNecessaryResources() {
        return necessaryResources;
    }

    public void setNecessaryResources(String necessaryResources) {
        this.necessaryResources = necessaryResources;
    }
}
