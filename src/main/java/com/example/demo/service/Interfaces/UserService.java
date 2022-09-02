package com.example.demo.service.Interfaces;

import com.example.demo.dto.UnauthorizedUser;
import com.example.demo.entity.User;

import javax.transaction.Transactional;
import java.util.Collection;


public interface UserService {

    User login(UnauthorizedUser User);

    @Transactional
    void register(User User);

    User getUserById(Long id);

    Collection<User> getAllUsers();


    Long getUserIdByLogin(String login);

    boolean isAdmin(String login);
}
