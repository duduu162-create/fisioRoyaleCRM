package com.fisioroyale.leads.dto;

import com.fisioroyale.leads.model.LeadStatus;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.Set;

public record LeadRequest(
        @NotBlank String nome,
        String telefone,
        String email,
        String origem,
        String responsavel,
        LeadStatus status,
        String observacoes,
        LocalDateTime proximaAcaoEm,
        String motivoPerda,
        Set<Long> areaIds
) {}
