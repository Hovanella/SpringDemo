package com.example.demo.controller;

import com.example.demo.dto.User.UnauthorizedUser;
import com.example.demo.dto.User.UserWithJwtToken;
import com.example.demo.entity.User;
import com.example.demo.service.UserServiceImpl;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/users/")
@CrossOrigin("*")
@ControllerAdvice
public class UserController {

    private final UserServiceImpl userService;


    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }


    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUser(@PathVariable Long id) {

        if (id == null)
            return ResponseEntity.badRequest().build();

        var user = userService.getUserById(id);

        if (user == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(user);

    }


    @ApiResponse(responseCode = "200", description = "Users found")
    @ApiResponse(responseCode = "204", description = "No Users found")
    @GetMapping(value = "all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<User>> getAllUsers() {

        var users = userService.getAllUsers();

        if (users == null || users.size() == 0)
            return ResponseEntity.noContent().build();

        return new ResponseEntity<>(users, HttpStatus.OK);


    }


    @ApiResponse(responseCode = "200", description = "User created")
    @ApiResponse(responseCode = "400", description = "Invalid user data")
    @PostMapping(value = "register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerUser(@RequestBody @Valid UnauthorizedUser unauthorizedUser, BindingResult bindingResult) {

        try {
            var userWithJwtToken = userService.register(unauthorizedUser, bindingResult);
            return new ResponseEntity<>(userWithJwtToken, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @ApiResponse(responseCode = "200", description = "User logged in")
    @ApiResponse(responseCode = "400", description = "Invalid user data")
    @PostMapping(value = "login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserWithJwtToken> loginUser(@RequestBody @Valid UnauthorizedUser unauthorizedUser) {

        var userWithJwtToken = userService.login(unauthorizedUser);
        return new ResponseEntity<>(userWithJwtToken, HttpStatus.OK);

    }



}
