package com.marketplace.ifba.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class ResearchGroup {
    private UUID idResearchGroup;
    private String title;
    private Institution institution;
    private Date datePlatformRegistration;
    private ArrayList<Tag> tags;
    private String description;
    private Number worksDevelopment;
    private Double starsRating;
    private ArrayList<User> users;

    public ResearchGroup(UUID idResearchGroup, String title, Institution institution, Date datePlatformRegistration, ArrayList<Tag> tags, String description, Number worksDevelopment, Double starsRating, ArrayList<User> users) {
        this.idResearchGroup = idResearchGroup;
        this.title = title;
        this.institution = institution;
        this.datePlatformRegistration = datePlatformRegistration;
        this.tags = tags;
        this.description = description;
        this.worksDevelopment = worksDevelopment;
        this.starsRating = starsRating;
        this.users = users;
    }

    public UUID getIdResearchGroup() {
        return idResearchGroup;
    }

    public void setIdResearchGroup(UUID idResearchGroup) {
        this.idResearchGroup = idResearchGroup;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public Date getDatePlatformRegistration() {
        return datePlatformRegistration;
    }

    public void setDatePlatformRegistration(Date datePlatformRegistration) {
        this.datePlatformRegistration = datePlatformRegistration;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Number getWorksDevelopment() {
        return worksDevelopment;
    }

    public void setWorksDevelopment(Number worksDevelopment) {
        this.worksDevelopment = worksDevelopment;
    }

    public Double getStarsRating() {
        return starsRating;
    }

    public void setStarsRating(Double starsRating) {
        this.starsRating = starsRating;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}
