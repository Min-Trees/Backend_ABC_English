package com.ABCEnglish.entity;

import jakarta.persistence.*;
import lombok.*;



@Entity
@Table(name = "library")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Library {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer libraryId;
    private String libraryName;

}
