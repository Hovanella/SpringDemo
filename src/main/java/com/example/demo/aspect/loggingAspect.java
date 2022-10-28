package com.example.demo.aspect;

import com.example.demo.controller.UserController;
import com.example.demo.exception.NonExistedUserException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@Aspect
public class loggingAspect {

    @AfterThrowing(value = "Pointcuts.loginUserPointcut()",throwing = "exception")
    public void loginUserExceptionLog(NonExistedUserException exception){
        Logger logger = LoggerFactory.getLogger(UserController.class);
        logger.error(exception.getMessage());
    }

    @Before(value = "Pointcuts.loginUserPointcut()")
public void loginUserLog(){
        Logger logger = LoggerFactory.getLogger(UserController.class);
        logger.info("loginUser method is called");
    }

    @After(value = "Pointcuts.loginUserPointcut()")
    public void loginUserAfterLog(){
        Logger logger = LoggerFactory.getLogger(UserController.class);
        logger.info("loginUser method is finished");
    }

    @Around(value = "Pointcuts.createTrackPointcut()")
    public Object createTrackLog(ProceedingJoinPoint joinPoint) throws Throwable {
        //track name is first argument of createTrack method. Make logging with track name
        String trackName = (String) joinPoint.getArgs()[0];
        log.info("Track with name {} was created",trackName);
        Object result = joinPoint.proceed();
        return result;
    }



}
