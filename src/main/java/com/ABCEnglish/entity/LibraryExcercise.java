package com.ABCEnglish.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Entity
@Table(name = "library_excercise")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibraryExcercise  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer libraryItemId;
    private String libraryItemDescription;
    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercises exercises;
    private Date createAt;
    private Date updateAt;
    @ManyToOne
    private Library library;
}
