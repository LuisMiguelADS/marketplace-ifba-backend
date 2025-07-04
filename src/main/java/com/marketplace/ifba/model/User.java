package com.marketplace.ifba.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class User {
    private UUID idUser;
    private String fullName;
    private String email;
    private String phone;
    private String senha;
    private String CPF;
    private Date datePlatformRegistration;
    private Date dateBirth;
    private String Biograph;
    private String profilePhotoURL;
    private Address address;
    private Institution institution;
    private Organization organization;
    private ArrayList<Connections> connections;

    public User(UUID idUser, String fullName, String email, String phone, String senha, String CPF, Date datePlatformRegistration, Date dateBirth, String biograph, String profilePhotoURL, Address address, Institution institution, Organization organization, ArrayList<Connections> connections) {
        this.idUser = idUser;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.senha = senha;
        this.CPF = CPF;
        this.datePlatformRegistration = datePlatformRegistration;
        this.dateBirth = dateBirth;
        Biograph = biograph;
        this.profilePhotoURL = profilePhotoURL;
        this.address = address;
        this.institution = institution;
        this.organization = organization;
        this.connections = connections;
    }

    public UUID getIdUser() {
        return idUser;
    }

    public void setIdUser(UUID idUser) {
        this.idUser = idUser;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public Date getDatePlatformRegistration() {
        return datePlatformRegistration;
    }

    public void setDatePlatformRegistration(Date datePlatformRegistration) {
        this.datePlatformRegistration = datePlatformRegistration;
    }

    public Date getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(Date dateBirth) {
        this.dateBirth = dateBirth;
    }

    public String getBiograph() {
        return Biograph;
    }

    public void setBiograph(String biograph) {
        Biograph = biograph;
    }

    public String getProfilePhotoURL() {
        return profilePhotoURL;
    }

    public void setProfilePhotoURL(String profilePhotoURL) {
        this.profilePhotoURL = profilePhotoURL;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public ArrayList<Connections> getConnections() {
        return connections;
    }

    public void setConnections(ArrayList<Connections> connections) {
        this.connections = connections;
    }
}
