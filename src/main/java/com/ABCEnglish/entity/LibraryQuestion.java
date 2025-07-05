package com.ABCEnglish.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
@Entity
@Table(name = "library_question")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibraryQuestion {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer libraryItemId;
        private String libraryItemDescription;
        @ManyToOne
        @JoinColumn(name = "question_id")
        private Question question;
        private Date createdAt;
        private Date updatedAt;
        @ManyToOne
        private Library library;
}
