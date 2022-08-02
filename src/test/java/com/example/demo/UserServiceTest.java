package com.example.demo;

import com.example.demo.dto.UnauthorizedUser;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserServiceImpl;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class UserServiceTest {

    private final Mockery context = new Mockery();

    @Test
    public void whenNoUserWithCurrentLogin_AndFindUserByLogin_ThenIllegalArgumentException() {
        //Arrange.
        var userRepositoryMock = context.mock(UserRepository.class);
        var passwordEncoderMock = new BCryptPasswordEncoder();
        var unauthorizedUser = new UnauthorizedUser("login", "password", false);
        var userService = new UserServiceImpl(userRepositoryMock, passwordEncoderMock);

        //Act.
        context.checking(new Expectations() {
            {
                oneOf(userRepositoryMock).findByLogin("login");
                will(returnValue(Optional.empty()));
            }
        });
        //Assert.
        assertThatThrownBy(() -> userService.login(unauthorizedUser))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User with login login not found");

    }

}
