package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnauthorizedUser {

    @NotBlank(message = "Login is required")
    @Size(min = 5, max = 40, message = "Login must be between 5 and 40 characters")
    private String login;

    @NotBlank(message = "Password is required")
    @Size(min = 5, max = 40, message = "Password must be between 5 and 40 characters")
    private String password;

    private Boolean isAdmin;

}
