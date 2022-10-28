package com.example.demo.aspect;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut("execution(* com.example.demo.service.TrackServiceImpl.createTrack(..))")
    public void createTrackPointcut(){

    }

    @Pointcut("execution(* com.example.demo.controller.UserController.loginUser(..))")
    public void loginUserPointcut(){

    }

 }
