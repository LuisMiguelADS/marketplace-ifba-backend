package com.marketplace.ifba.model;

import java.util.UUID;

public class Project {
    private UUID idProject;
    private String title;
    private Organization organization;
    private Institution institution;
    private Demand demand;
    private OfferSolution offerSolution;
    private ResearchGroup researchGroup;
    private User userRegistration;
    private Chat chat;

    public Project(UUID idProject, String title, Organization organization, Institution institution, Demand demand, OfferSolution offerSolution, ResearchGroup researchGroup, User userRegistration, Chat chat) {
        this.idProject = idProject;
        this.title = title;
        this.organization = organization;
        this.institution = institution;
        this.demand = demand;
        this.offerSolution = offerSolution;
        this.researchGroup = researchGroup;
        this.userRegistration = userRegistration;
        this.chat = chat;
    }

    public UUID getIdProject() {
        return idProject;
    }

    public void setIdProject(UUID idProject) {
        this.idProject = idProject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public Demand getDemand() {
        return demand;
    }

    public void setDemand(Demand demand) {
        this.demand = demand;
    }

    public OfferSolution getOfferSolution() {
        return offerSolution;
    }

    public void setOfferSolution(OfferSolution offerSolution) {
        this.offerSolution = offerSolution;
    }

    public ResearchGroup getResearchGroup() {
        return researchGroup;
    }

    public void setResearchGroup(ResearchGroup researchGroup) {
        this.researchGroup = researchGroup;
    }

    public User getUserRegistration() {
        return userRegistration;
    }

    public void setUserRegistration(User userRegistration) {
        this.userRegistration = userRegistration;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }
}
