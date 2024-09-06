package com.ABCEnglish.entity;

import lombok.Getter;
import lombok.Setter;


import jakarta.persistence.*;

@Entity
@Table(name = "Role")
@Getter
@Setter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roleId")
    private Integer roleId;
    @Column(name = "name")
    private String name;

}
