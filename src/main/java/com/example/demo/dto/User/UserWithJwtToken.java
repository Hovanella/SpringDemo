package com.example.demo.dto.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWithJwtToken {

    private String login;

    private Boolean isAdmin;

    private String token;
}
