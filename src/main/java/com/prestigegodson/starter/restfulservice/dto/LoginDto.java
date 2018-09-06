package com.prestigegodson.starter.restfulservice.dto;

import javax.validation.constraints.NotBlank;

/**
 * Created by prestige on 8/28/2018.
 */
public class LoginDto {

    @NotBlank
    private String username;
    @NotBlank
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
