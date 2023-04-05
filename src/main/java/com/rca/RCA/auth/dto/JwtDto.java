package com.rca.RCA.auth.dto;

public class JwtDto {
    private String token;
    private String emailorUser;
    public JwtDto() {
    }

    public JwtDto(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmailorUser() {
        return emailorUser;
    }

    public void setEmailorUser(String emailorUser) {
        this.emailorUser = emailorUser;
    }
}
