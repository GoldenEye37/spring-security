package com.codeBuffer.securitydemo.controller;

import com.codeBuffer.securitydemo.model.GenericResponse;
import com.codeBuffer.securitydemo.model.UserModel;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class RegistrationControllerTest {
    @Autowired
    TestRestTemplate testRestTemplate;

    @BeforeAll
    public static void init() {
        System.setProperty("spring.datasource.username", "");
        System.setProperty("spring.datasource.password", "");
    }
    @AfterAll
    static void cleanupEnvironment() {
        System.clearProperty("spring.datasource.username");
        System.clearProperty("spring.datasource.password");
    }


    @Test
    void shouldReturnTrue() {
        //Given
        UserModel userModel = new UserModel("Willdom", "Kahari", "drclae@gmail.com", "1234", "1234");
        //When
        GenericResponse response = testRestTemplate.postForObject("http://localhost:8080/register", userModel, GenericResponse.class);
        //Then
        assertTrue(response.isSuccess());
    }
}
