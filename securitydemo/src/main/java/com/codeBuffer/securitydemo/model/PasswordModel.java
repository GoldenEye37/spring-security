package com.codeBuffer.securitydemo.model;


import lombok.Data;

@Data
public class PasswordModel {

    private String email;
    private String oldPassword;
    private String newPassword;

}
