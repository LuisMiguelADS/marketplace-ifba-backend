package com.marketplace.ifba.model;

import java.util.Date;
import java.util.UUID;

public class Proposal {
    private UUID idProposal;
    private ResearchGroup researchGroup;
    private Institution institution;
    private String title;
    private String solution;
    private String description;
    private String resum;
    private Double budget;
    private Date dateTermProposal;
    private Date dateRegistration;
    private String restrictions;
    private String necessaryResources;

    public Proposal(UUID idProposal, ResearchGroup researchGroup, Institution institution, String title, String solution, String description, String resum, Double budget, Date dateTermProposal, Date dateRegistration, String restrictions, String necessaryResources) {
        this.idProposal = idProposal;
        this.researchGroup = researchGroup;
        this.institution = institution;
        this.title = title;
        this.solution = solution;
        this.description = description;
        this.resum = resum;
        this.budget = budget;
        this.dateTermProposal = dateTermProposal;
        this.dateRegistration = dateRegistration;
        this.restrictions = restrictions;
        this.necessaryResources = necessaryResources;
    }

    public UUID getIdProposal() {
        return idProposal;
    }

    public void setIdProposal(UUID idProposal) {
        this.idProposal = idProposal;
    }

    public ResearchGroup getResearchGroup() {
        return researchGroup;
    }

    public void setResearchGroup(ResearchGroup researchGroup) {
        this.researchGroup = researchGroup;
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
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

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public Date getDateTermProposal() {
        return dateTermProposal;
    }

    public void setDateTermProposal(Date dateTermProposal) {
        this.dateTermProposal = dateTermProposal;
    }

    public Date getDateRegistration() {
        return dateRegistration;
    }

    public void setDateRegistration(Date dateRegistration) {
        this.dateRegistration = dateRegistration;
    }

    public String getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(String restrictions) {
        this.restrictions = restrictions;
    }

    public String getNecessaryResources() {
        return necessaryResources;
    }

    public void setNecessaryResources(String necessaryResources) {
        this.necessaryResources = necessaryResources;
    }
}
