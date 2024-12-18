package com.ABCEnglish.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Account")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @ManyToOne
    @JoinColumn(name = "roleId", nullable = false)
    private Role role;
    @Column(name = "password")
    private String password;
    @Column(columnDefinition = "NVARCHAR(MAX)", nullable = false)
    private String username;
    @Column(name = "fullname")
    private String fullname;
    @Column(name = "email")
    private String email;
    @Column(name = "phone", unique = true)
    private String phone;
    @Column(name = "description")
    private String description;
    @Column(name = "level")
    private Integer level = 1;
    @Column(name = "status")
    private Boolean status = false;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date updatedAt;

    @Column(name = "ban_24h")
    private Boolean ban24h = false;

    @Column(name = "ban_until")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date banUntil;

}