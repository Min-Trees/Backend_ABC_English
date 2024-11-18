package com.ABCEnglish.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Documents")
@Getter
@Setter
public class Doc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer docId;
    @ManyToOne
    @JoinColumn(name = "lessonId", nullable = false)
    private Lesson lesson;
    @ManyToOne
    @JoinColumn(name = "creatorId")
    private User creator;
    @Column(columnDefinition = "NVARCHAR(MAX)", nullable = false)
    private String name;
    @Column(columnDefinition = "NVARCHAR(100)", nullable = false)
    private String content;
    @Column(columnDefinition = "NVARCHAR(MAX)", nullable = false)
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
        DOCX, PDF, VIDEO

    }
}
