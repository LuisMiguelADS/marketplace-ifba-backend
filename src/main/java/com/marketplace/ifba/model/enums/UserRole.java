package com.marketplace.ifba.model.enums;

public enum UserRole {
    ADMIN("admin"),
    ALUNO("aluno"),
    PROFESSOR("professor"),
    EXTERNO("externo"),
    USER("user");

    private String role;

    UserRole(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }
}
