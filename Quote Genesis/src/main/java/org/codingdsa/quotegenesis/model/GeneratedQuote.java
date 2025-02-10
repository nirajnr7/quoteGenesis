package org.codingdsa.quotegenesis.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class GeneratedQuote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String quoteText;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String type;
}
