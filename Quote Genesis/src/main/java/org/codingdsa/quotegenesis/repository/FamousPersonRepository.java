package org.codingdsa.quotegenesis.repository;

import org.codingdsa.quotegenesis.model.FamousPerson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FamousPersonRepository extends JpaRepository<FamousPerson, Long> {
    Optional<FamousPerson> findByName(String name);
}
