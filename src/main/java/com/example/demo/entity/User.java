package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    @Column(name = "login",nullable = false,length = 40)
    private String login;

    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "is_admin",nullable = false)
    private Boolean isAdmin = false;

    @ManyToMany
    @JoinTable(name = "rating",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "track_id"))
    private List<Track>  tracks;
}
