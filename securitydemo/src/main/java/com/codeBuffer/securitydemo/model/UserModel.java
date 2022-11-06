package com.codeBuffer.securitydemo.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {

    private String name;
    private String surname;
    private String email;
    private String password;
    private String matchingPassword;


}
