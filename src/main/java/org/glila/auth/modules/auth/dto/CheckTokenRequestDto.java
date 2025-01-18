package org.glila.auth.modules.auth.dto;

public class CheckTokenRequestDto {

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public CheckTokenRequestDto() {
    }

    public CheckTokenRequestDto(String token) {
        this.token = token;
    }

    private String token;
}
