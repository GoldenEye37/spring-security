package com.codeBuffer.securitydemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenericResponse {
    private boolean success;
    private String message;
}
