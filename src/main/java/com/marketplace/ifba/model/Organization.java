package com.marketplace.ifba.model;

import java.util.Date;
import java.util.UUID;

public class Organization {
    private UUID idOrganization;
    private String name;
    private String sigla;
    private String CNPJ;
    private String typeOrganization;
    private Date datePlatformRegistration;
    private Date dateAprovation;
    private String sector;
    private String phone;
    private String site;
    private String status;
    private String logoURL;
    private User admAprovation;
    private User admRegistration;
    private String description;

    public Organization(UUID idOrganization, String name, String sigla, String CNPJ, String typeOrganization, Date datePlatformRegistration, Date dateAprovation, String sector, String phone, String site, String status, String logoURL, User admAprovation, User admRegistration, String description) {
        this.idOrganization = idOrganization;
        this.name = name;
        this.sigla = sigla;
        this.CNPJ = CNPJ;
        this.typeOrganization = typeOrganization;
        this.datePlatformRegistration = datePlatformRegistration;
        this.dateAprovation = dateAprovation;
        this.sector = sector;
        this.phone = phone;
        this.site = site;
        this.status = status;
        this.logoURL = logoURL;
        this.admAprovation = admAprovation;
        this.admRegistration = admRegistration;
        this.description = description;
    }

    public UUID getIdOrganization() {
        return idOrganization;
    }

    public void setIdOrganization(UUID idOrganization) {
        this.idOrganization = idOrganization;
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

    public String getTypeOrganization() {
        return typeOrganization;
    }

    public void setTypeOrganization(String typeOrganization) {
        this.typeOrganization = typeOrganization;
    }

    public Date getDatePlatformRegistration() {
        return datePlatformRegistration;
    }

    public void setDatePlatformRegistration(Date datePlatformRegistration) {
        this.datePlatformRegistration = datePlatformRegistration;
    }

    public Date getDateAprovation() {
        return dateAprovation;
    }

    public void setDateAprovation(Date dateAprovation) {
        this.dateAprovation = dateAprovation;
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

    public User getAdmRegistration() {
        return admRegistration;
    }

    public void setAdmRegistration(User admRegistration) {
        this.admRegistration = admRegistration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
