package com.ABCEnglish.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
@Entity
@Table(name = "library_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibraryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer libraryItemId;
    private Integer itemId;
    private String libraryItemDescription;
    private TypeLibrary type;
    private Date createAt;
    private Date updateAt;
    @ManyToOne
    private Library library;
}
