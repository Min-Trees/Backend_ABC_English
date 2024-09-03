package com.ABCEnglish.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "Social")
@Getter
@Setter
public class Social {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer socialId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    private String title;
    private String content;
    private String images;
}
