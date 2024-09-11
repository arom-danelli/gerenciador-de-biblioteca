package com.elotech.biblioteca_arom.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "book")
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String isbn;

    @Column(nullable = false)
    private String publicationDate;

    @Column(nullable = false)
    private String category;

    @Column(nullable = true)
    private String thumbnail_url;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "book-loans")
    private List<Loan> loans;
}
