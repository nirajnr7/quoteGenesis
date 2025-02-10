package org.codingdsa.quotegenesis.repository;

import org.codingdsa.quotegenesis.model.GeneratedQuote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GeneratedQuoteRepository extends JpaRepository<GeneratedQuote, Long> {
    @Query("SELECT g FROM GeneratedQuote g ORDER BY g.id DESC")
    List<GeneratedQuote> findLastQuote();
}
