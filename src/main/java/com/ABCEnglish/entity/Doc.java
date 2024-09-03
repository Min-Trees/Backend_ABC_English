package com.ABCEnglish.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

@Entity
@Table(name = "Documents")
@Getter
@Setter
public class Doc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer docId;

    @ManyToOne
    @JoinColumn(name = "courseId", nullable = false)
    private Course course;

    private String name;
    private String description;
    private String url;
    private String images;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private DocType type;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "isFree")
    private Boolean isFree;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date updatedAt;

    public enum DocType {
        PDF, DOCX
    }
}
