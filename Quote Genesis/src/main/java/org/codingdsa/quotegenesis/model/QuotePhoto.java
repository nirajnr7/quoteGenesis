package org.codingdsa.quotegenesis.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "QUOTE_PHOTO")  // Updated table name
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuotePhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quote_id", unique = true, nullable = false)
    private String quoteId;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;
}
