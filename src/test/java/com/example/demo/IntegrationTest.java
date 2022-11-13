package com.example.demo;

import com.example.demo.security.JWTUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class IntegrationTest {
    @Autowired
    WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private JWTUtil jwtUtil = new JWTUtil();

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void WhenTryToAddTrack_AndTokenIsEmpty_Then4xxStatus() throws Exception {
        String token = "";

        mockMvc.perform(MockMvcRequestBuilders.get("/tracks/AddTrack").header("Authorization", "Bearer " + token))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void WhenTryToGetAllUsers_AndHasAdminToken_Then200Status()  throws Exception {
        String token =jwtUtil.generateToken("admin");

        mockMvc.perform(MockMvcRequestBuilders.get("/users/all").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}