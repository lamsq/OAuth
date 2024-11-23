package com.example.oauth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String role;

    private String provider;
    private String providerId;

    public User(String email, String name, String role, String provider, String providerId) {
        this.email = email;
        this.name = name;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }
}
