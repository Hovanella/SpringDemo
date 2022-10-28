package com.example.demo;

import com.example.demo.dto.User.UnauthorizedUser;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class UserServiceTest {

    private final UserRepository userRepositoryMock = Mockito.mock(UserRepository.class);
    private final PasswordEncoder passwordEncoderMock = Mockito.mock(PasswordEncoder.class);
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final User userFromRepository = new User();
    private final UnauthorizedUser unauthorizedUser = new UnauthorizedUser();


    @Test
    @DisplayName("login method")
    public void whenNoUserWithCurrentLogin_AndTryToLoginWithIt_ThenIllegalArgumentException() {

        //Arrange.
        var userService = new UserServiceImpl(userRepositoryMock, passwordEncoder);

        //Act.
        Mockito.when(userRepositoryMock.findByLogin(unauthorizedUser.getLogin())).thenReturn(Optional.empty());

        //Assert.
        assertThatThrownBy(() -> userService.login(unauthorizedUser))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User with login "+unauthorizedUser.getLogin() + " not found");

    }


    @Test
    public void whenUserWithCurrentLoginExists_AndTryToLoginWithValidData_ThenReturnsUser()
    {
        //Arrange.
        var userService = new UserServiceImpl(userRepositoryMock, passwordEncoderMock);
        
        //Act.
        Mockito.when(userRepositoryMock.findByLogin(unauthorizedUser.getLogin())).thenReturn(Optional.of(userFromRepository));
        Mockito.when(passwordEncoderMock.matches(unauthorizedUser.getPassword(), userFromRepository.getPassword())).thenReturn(true);

        //Assert.
        assertThat(userService.login(unauthorizedUser)).isEqualTo(userFromRepository);

    }


    @Test
    public void whenUserWithCurrentLoginExists_AndTryToLoginWithInvalidPassword_ThenIllegalArgumentException()
    {
        //Arrange.
        var userService = new UserServiceImpl(userRepositoryMock, passwordEncoderMock);

        //Act.
        Mockito.when(userRepositoryMock.findByLogin(unauthorizedUser.getLogin())).thenReturn(Optional.of(userFromRepository));
        Mockito.when(passwordEncoderMock.matches(unauthorizedUser.getPassword(),userFromRepository.getPassword())).thenReturn(false);

        //Assert.
        assertThatThrownBy(() -> userService.login(unauthorizedUser))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Wrong password");

    }



}
