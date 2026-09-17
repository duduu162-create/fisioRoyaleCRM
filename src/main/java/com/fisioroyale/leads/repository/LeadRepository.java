package com.fisioroyale.leads.repository;

import com.fisioroyale.leads.model.Lead;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LeadRepository extends JpaRepository<Lead, Long> {
    @Override
    @EntityGraph(attributePaths = "areas")
    List<Lead> findAll();

    @Override
    @EntityGraph(attributePaths = "areas")
    Optional<Lead> findById(Long id);
}
