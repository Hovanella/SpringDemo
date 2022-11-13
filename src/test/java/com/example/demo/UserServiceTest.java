package com.example.demo;

import com.example.demo.dto.User.UnauthorizedUser;
import com.example.demo.dto.User.UserWithJwtToken;
import com.example.demo.entity.User;
import com.example.demo.exception.NonExistedUserException;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JWTUtil;
import com.example.demo.service.Interfaces.UserService;
import com.example.demo.service.UserServiceImpl;
import com.example.demo.validator.UserValidator;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
public class UserServiceTest {

    private final UserRepository userRepositoryMock = Mockito.mock(UserRepository.class);
    private final PasswordEncoder passwordEncoderMock = Mockito.mock(PasswordEncoder.class);
    private final ModelMapper modelMapper;
     private final UserValidator userValidator;
    private final JWTUtil jwtUtilMock =  Mockito.mock(JWTUtil.class);
    private final UserService userService;
    UnauthorizedUser unauthorizedUser = new UnauthorizedUser();
    User userFromRepository = new User();
    private final AuthenticationManager authenticationManager;
    private final AuthenticationManager authenticationManagerMock = Mockito.mock(AuthenticationManager.class);
    private  BindingResult bindingResult= Mockito.mock(BindingResult.class);

    @Autowired
    public UserServiceTest(ModelMapper modelMapper, UserValidator userValidator, AuthenticationManager authenticationManager) {
        this.modelMapper = modelMapper;
        this.userValidator = userValidator;
        this.authenticationManager = authenticationManager;
        userService= new UserServiceImpl(userRepositoryMock,  passwordEncoderMock, jwtUtilMock, this.modelMapper, this.userValidator,this.authenticationManagerMock);
    }

    @Test
    public void whenNoUserWithCurrentLogin_AndTryToLoginWithIt_ThenIllegalArgumentException() {

        //Arrange.
        Mockito.when(userRepositoryMock.findByLogin(unauthorizedUser.getLogin())).thenReturn(Optional.empty());

        //Act.
        ThrowableAssert.ThrowingCallable callable = () -> userService.login(unauthorizedUser);

        //Assert.
        assertThatThrownBy(callable)
                .isInstanceOf(NonExistedUserException.class);

    }

    @Test
    public void whenUserWithCurrentLoginExists_AndTryToLoginWithValidData_ThenReturnsUserWithJwtToken()
    {
        //Arrange.
        String token = jwtUtilMock.generateToken(userFromRepository.getLogin());

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(unauthorizedUser.getLogin(), unauthorizedUser.getPassword());

        Mockito.when(userRepositoryMock.findByLogin(unauthorizedUser.getLogin())).thenReturn(Optional.of(userFromRepository));
        Mockito.when(this.authenticationManagerMock.authenticate(authenticationToken)).thenReturn(Mockito.any());
        Mockito.when(jwtUtilMock.generateToken(userFromRepository.getLogin())).thenReturn(token);


        //Act.
        UserWithJwtToken userWithJwtToken = userService.login(unauthorizedUser);
        UserWithJwtToken expectedUserWithJwtToken = new UserWithJwtToken(unauthorizedUser.getLogin(),false,token);

        //Assert.
        assertThat(userWithJwtToken).isEqualTo(expectedUserWithJwtToken);
    }


    @Test
    public void whenUserWithCurrentLoginExists_AndTryToLoginWithInvalidPassword_ThenIllegalArgumentException()
    {
        //Arrange.
        Mockito.when(userRepositoryMock.findByLogin(unauthorizedUser.getLogin())).thenReturn(Optional.of(userFromRepository));
        Mockito.when(this.authenticationManagerMock.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenThrow(NonExistedUserException.class);

        //Act.
        ThrowableAssert.ThrowingCallable callable = () -> userService.login(unauthorizedUser);

        //Assert.
        assertThatThrownBy(callable)
                .isInstanceOf(NonExistedUserException.class);

    }

    @Test
    public void WhenNoUserWithCurrentData_AndTryToRegisterWithIt_ThenReturnsUserWithJwtToken()
    {
        //Arrange.
        String token = jwtUtilMock.generateToken(userFromRepository.getLogin());
        Mockito.when(userRepositoryMock.findByLogin(unauthorizedUser.getLogin())).thenReturn(Optional.empty());
        Mockito.when(userRepositoryMock.save(Mockito.any(User.class))).thenReturn(userFromRepository);
        Mockito.when(jwtUtilMock.generateToken(userFromRepository.getLogin())).thenReturn(token);


        //Act.
        UserWithJwtToken userWithJwtToken = userService.register(unauthorizedUser,bindingResult);
        UserWithJwtToken expectedUserWithJwtToken = new UserWithJwtToken(unauthorizedUser.getLogin(),false,token);

        //Assert.
        assertThat(userWithJwtToken).isEqualTo(expectedUserWithJwtToken);
    }



}
