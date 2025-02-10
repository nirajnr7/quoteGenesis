package org.codingdsa.quotegenesis.repository;

import org.codingdsa.quotegenesis.model.QuoteType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuoteTypeRepository extends JpaRepository<QuoteType, Long> {
    Optional<QuoteType> findByType(String type);
}