package com.example.demo.service;

import com.example.demo.dto.User.UnauthorizedUser;
import com.example.demo.dto.User.UserWithJwtToken;
import com.example.demo.entity.User;
import com.example.demo.exception.NonExistedUserException;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JWTUtil;
import com.example.demo.service.Interfaces.UserService;
import com.example.demo.validator.UserValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import javax.transaction.Transactional;
import java.util.Collection;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final UserValidator userValidator;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JWTUtil jwtUtil, ModelMapper modelMapper, UserValidator userValidator, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
        this.userValidator = userValidator;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public UserWithJwtToken login(UnauthorizedUser unauthorizedUser) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(unauthorizedUser.getLogin(), unauthorizedUser.getPassword());

        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            throw new NonExistedUserException(e.getMessage());
        }

        String token = jwtUtil.generateToken(unauthorizedUser.getLogin());
        boolean isAdmin = isAdmin(unauthorizedUser.getLogin());
        return new UserWithJwtToken(unauthorizedUser.getLogin(), isAdmin, token);

    }

    @Override
    @Transactional
    public UserWithJwtToken register(UnauthorizedUser unauthorizedUser, BindingResult bindingResult) {

        User user = this.modelMapper.map(unauthorizedUser, User.class);

        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            var allErrors = bindingResult.getAllErrors();
            for (ObjectError error : allErrors) {
                errorMessage.append(error.getDefaultMessage()).append(" ");
            }
            throw new IllegalArgumentException(errorMessage.toString());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getLogin());
        return new UserWithJwtToken(user.getLogin(), user.getIsAdmin(), token);


    }


    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public Collection<User> getAllUsers() {
        return (Collection<User>) userRepository.findAll();
    }

    @Override
    public Long getUserIdByLogin(String login) {
        return userRepository.findByLogin(login).get().getId();
    }

    @Override
    public boolean isAdmin(String login) {
        boolean isUserPresent = userRepository.findByLogin(login).isPresent();
        if (isUserPresent) {
            return userRepository.findByLogin(login).get().getIsAdmin();
        }else{
            throw new NonExistedUserException("User with login " + login + " doesn't exist");
        }
    }


}
