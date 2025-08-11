package com.ABCEnglish.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Entity
@Table(name = "library_doc")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LibraryDoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer libraryItemId;
    private String libraryItemDescription;
    @ManyToOne
    @JoinColumn(name = "doc_id")
    private Doc doc;
    private Date createdAt;
    private Date updatedAt;
    private boolean deleted;
    @ManyToOne
    private Library library;
}
