package com.example.demo.controller;

import com.example.demo.dto.UnauthorizedUser;
import com.example.demo.dto.UserWithJwtToken;
import com.example.demo.entity.User;
import com.example.demo.security.JWTUtil;
import com.example.demo.service.UserServiceImpl;
import com.example.demo.validator.UserValidator;
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


    @GetMapping(value = "all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<User>> getAllUsers() {

        var users = userService.getAllUsers();

        if (users == null || users.size() == 0)
            return ResponseEntity.notFound().build();

        return new ResponseEntity<>(users, HttpStatus.OK);


    }



    @PostMapping(value = "register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserWithJwtToken> registerUser(@RequestBody @Valid UnauthorizedUser unauthorizedUser, BindingResult bindingResult) {

        User user = convertToUser(unauthorizedUser);

        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors())
            return ResponseEntity.badRequest().build();

        userService.register(user);

        String token = jwtUtil.generateToken(user.getLogin());

        var userWithJwtToken = convertToUserWithToken(user, token);

        return new ResponseEntity<>(userWithJwtToken, HttpStatus.OK);

    }

    @PostMapping(value = "login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserWithJwtToken> loginUser(@RequestBody @Valid UnauthorizedUser unauthorizedUser) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(unauthorizedUser.getLogin(), unauthorizedUser.getPassword());

        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = jwtUtil.generateToken(unauthorizedUser.getLogin());
        boolean isAdmin = userService.isAdmin(unauthorizedUser.getLogin());
        var userWithJwtToken = new UserWithJwtToken(unauthorizedUser.getLogin(),isAdmin, token);
        return new ResponseEntity<>(userWithJwtToken, HttpStatus.OK);

    }

    public User convertToUser(UnauthorizedUser unauthorizedUser) {
        return this.modelMapper.map(unauthorizedUser, User.class);
    }

    public UserWithJwtToken convertToUserWithToken(User user, String token){
        return new UserWithJwtToken(user.getLogin(), user.getIsAdmin(), token);
    }

}
