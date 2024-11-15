package com.ABCEnglish.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.Date;
import java.util.Set;

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
    @Column(columnDefinition = "NVARCHAR(MAX)", nullable = false)
    private String title;
    @Column(columnDefinition = "NVARCHAR(MAX)", nullable = false)
    private String content;
    @ElementCollection
    private Set<String> images;
    private Date createdAt;
    private Date updatedAt;
}
