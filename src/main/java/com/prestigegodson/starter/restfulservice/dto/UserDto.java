package com.prestigegodson.starter.restfulservice.dto;

/**
 * Created by prestige on 8/15/2018.
 */
public class UserDto {

    private String email;
    private String firstName;
    private String lastName;


    public UserDto() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
