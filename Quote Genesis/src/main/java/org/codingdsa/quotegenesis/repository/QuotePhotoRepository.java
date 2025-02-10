package org.codingdsa.quotegenesis.repository;

import org.codingdsa.quotegenesis.model.QuotePhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuotePhotoRepository extends JpaRepository<QuotePhoto, Long> {
}
