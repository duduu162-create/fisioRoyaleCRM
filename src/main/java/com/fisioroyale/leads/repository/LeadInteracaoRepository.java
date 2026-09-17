package com.fisioroyale.leads.repository;

import com.fisioroyale.leads.model.LeadInteracao;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeadInteracaoRepository extends JpaRepository<LeadInteracao, Long> {
    @EntityGraph(attributePaths = "lead")
    List<LeadInteracao> findByLeadIdOrderByOcorridaEmDesc(Long leadId);
}
