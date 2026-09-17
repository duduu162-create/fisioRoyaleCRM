package com.fisioroyale.leads.repository;

import com.fisioroyale.leads.model.AreaServico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AreaServicoRepository extends JpaRepository<AreaServico, Long> {
    List<AreaServico> findByAtivoTrueOrderByNomeAsc();
}
