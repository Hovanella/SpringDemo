package com.example.demo.service;

import com.example.demo.dto.UnauthorizedUser;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.Interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User login(UnauthorizedUser unauthorizedUser) {
        String login = unauthorizedUser.getLogin();
        if (userRepository.findByLogin(login).isEmpty()) {
            throw new IllegalArgumentException("User with login " + login + " not found");
        }
        User userFromRepository = userRepository.findByLogin(login).get();
        if (!passwordEncoder.matches(unauthorizedUser.getPassword(), userFromRepository.getPassword())) {
            throw new IllegalArgumentException("Wrong password");
        }
        System.out.println(unauthorizedUser.getPassword() + " " + userFromRepository.getPassword());
        return userFromRepository;
    }

    @Override
    @Transactional
    public void register(User User) {
        User.setPassword(passwordEncoder.encode(User.getPassword()));
        userRepository.save(User);
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
        return userRepository.findByLogin(login).get().getIsAdmin();
    }


}
