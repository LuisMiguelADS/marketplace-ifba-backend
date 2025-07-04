package com.marketplace.ifba.model;

import java.util.Date;
import java.util.UUID;

public class Institution {
    private UUID idInstitution;
    private String name;
    private String sigla;
    private String CNPJ;
    private String typeInstitution;
    private Date datePlatformRegistration;
    private Date dateAprovationRegistration;
    private String sector;
    private String phone;
    private String site;
    private String status;
    private String logoURL;
    private User admAprovation;
    private User userRegistration;
    private String description;

    public Institution(UUID idInstitution, String name, String sigla, String CNPJ, String typeInstitution, Date datePlatformRegistration, Date dateAprovationRegistration, String sector, String phone, String site, String status, String logoURL, User admAprovation, User userRegistration, String description) {
        this.idInstitution = idInstitution;
        this.name = name;
        this.sigla = sigla;
        this.CNPJ = CNPJ;
        this.typeInstitution = typeInstitution;
        this.datePlatformRegistration = datePlatformRegistration;
        this.dateAprovationRegistration = dateAprovationRegistration;
        this.sector = sector;
        this.phone = phone;
        this.site = site;
        this.status = status;
        this.logoURL = logoURL;
        this.admAprovation = admAprovation;
        this.userRegistration = userRegistration;
        this.description = description;
    }

    public UUID getIdInstitution() {
        return idInstitution;
    }

    public void setIdInstitution(UUID idInstitution) {
        this.idInstitution = idInstitution;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getCNPJ() {
        return CNPJ;
    }

    public void setCNPJ(String CNPJ) {
        this.CNPJ = CNPJ;
    }

    public String getTypeInstitution() {
        return typeInstitution;
    }

    public void setTypeInstitution(String typeInstitution) {
        this.typeInstitution = typeInstitution;
    }

    public Date getDatePlatformRegistration() {
        return datePlatformRegistration;
    }

    public void setDatePlatformRegistration(Date datePlatformRegistration) {
        this.datePlatformRegistration = datePlatformRegistration;
    }

    public Date getDateAprovationRegistration() {
        return dateAprovationRegistration;
    }

    public void setDateAprovationRegistration(Date dateAprovationRegistration) {
        this.dateAprovationRegistration = dateAprovationRegistration;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    public User getAdmAprovation() {
        return admAprovation;
    }

    public void setAdmAprovation(User admAprovation) {
        this.admAprovation = admAprovation;
    }

    public User getUserRegistration() {
        return userRegistration;
    }

    public void setUserRegistration(User userRegistration) {
        this.userRegistration = userRegistration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
