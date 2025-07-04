package com.marketplace.ifba.model;

import java.util.Date;
import java.util.UUID;

public class Demand {
    private UUID idDemand;
    private String titleDemand;
    private String emailResponsible;
    private Double budget;
    private Date dateTermDemand;
    private String description;
    private String resum;
    private String criteria;
    private String status;
    private Boolean organizationAprovation;
    private Number views;
    private User creatorDemand;
    private Organization organization;

    public Demand(UUID idDemand, String titleDemand, String emailResponsible, Double budget, Date dateTermDemand, String description, String resum, String criteria, String status, Boolean organizationAprovation, Number views, User creatorDemand, Organization organization) {
        this.idDemand = idDemand;
        this.titleDemand = titleDemand;
        this.emailResponsible = emailResponsible;
        this.budget = budget;
        this.dateTermDemand = dateTermDemand;
        this.description = description;
        this.resum = resum;
        this.criteria = criteria;
        this.status = status;
        this.organizationAprovation = organizationAprovation;
        this.views = views;
        this.creatorDemand = creatorDemand;
        this.organization = organization;
    }

    public UUID getIdDemand() {
        return idDemand;
    }

    public void setIdDemand(UUID idDemand) {
        this.idDemand = idDemand;
    }

    public String getTitleDemand() {
        return titleDemand;
    }

    public void setTitleDemand(String titleDemand) {
        this.titleDemand = titleDemand;
    }

    public String getEmailResponsible() {
        return emailResponsible;
    }

    public void setEmailResponsible(String emailResponsible) {
        this.emailResponsible = emailResponsible;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public Date getDateTermDemand() {
        return dateTermDemand;
    }

    public void setDateTermDemand(Date dateTermDemand) {
        this.dateTermDemand = dateTermDemand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResum() {
        return resum;
    }

    public void setResum(String resum) {
        this.resum = resum;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getOrganizationAprovation() {
        return organizationAprovation;
    }

    public void setOrganizationAprovation(Boolean organizationAprovation) {
        this.organizationAprovation = organizationAprovation;
    }

    public Number getViews() {
        return views;
    }

    public void setViews(Number views) {
        this.views = views;
    }

    public User getCreatorDemand() {
        return creatorDemand;
    }

    public void setCreatorDemand(User creatorDemand) {
        this.creatorDemand = creatorDemand;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
}
