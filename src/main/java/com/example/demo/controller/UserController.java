package com.example.demo.controller;

import com.example.demo.dto.User.UnauthorizedUser;
import com.example.demo.dto.User.UserWithJwtToken;
import com.example.demo.entity.User;
import com.example.demo.exception.NonExistedUserException;
import com.example.demo.security.JWTUtil;
import com.example.demo.service.UserServiceImpl;
import com.example.demo.validator.UserValidator;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private final UserValidator userValidator;
    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;


    @Autowired
    public UserController(UserServiceImpl userService, UserValidator userValidator, JWTUtil jwtUtil, ModelMapper modelMapper, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
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
    public ResponseEntity<UserWithJwtToken> registerUser(@RequestBody @Valid UnauthorizedUser unauthorizedUser, BindingResult bindingResult) {

        User user = this.modelMapper.map(unauthorizedUser, User.class);

        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors())
            return ResponseEntity.badRequest().build();

        userService.register(user);

        String token = jwtUtil.generateToken(user.getLogin());

        var userWithJwtToken = new UserWithJwtToken(user.getLogin(), user.getIsAdmin(), token);

        return new ResponseEntity<>(userWithJwtToken, HttpStatus.OK);

    }

    @ApiResponse(responseCode = "200", description = "User logged in")
    @ApiResponse(responseCode = "400", description = "Invalid user data")
    @PostMapping(value = "login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserWithJwtToken> loginUser(@RequestBody @Valid UnauthorizedUser unauthorizedUser) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(unauthorizedUser.getLogin(), unauthorizedUser.getPassword());

        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
           throw new NonExistedUserException("User with login " + unauthorizedUser.getLogin() + " doesn't exist");
        }

        String token = jwtUtil.generateToken(unauthorizedUser.getLogin());
        boolean isAdmin = userService.isAdmin(unauthorizedUser.getLogin());
        var userWithJwtToken = new UserWithJwtToken(unauthorizedUser.getLogin(),isAdmin, token);
        return new ResponseEntity<>(userWithJwtToken, HttpStatus.OK);

    }



}
