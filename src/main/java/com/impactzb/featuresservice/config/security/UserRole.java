package com.impactzb.featuresservice.config.security;

public enum UserRole {
    ADMIN,
    USER,
    OBSERVER;

    public String getSecurityRole(){
        return "ROLE_" + this.name();
    }
}
