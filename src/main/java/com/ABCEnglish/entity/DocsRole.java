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
@Table(name = "Docs_role")
@Getter
@Setter
public class DocsRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer docsRoleId;

    @ManyToOne
    @JoinColumn(name = "docId", nullable = false)
    private Doc doc;

    @ManyToOne
    @JoinColumn(name = "roleId", nullable = false)
    private Role role;
}
