package com.fisioroyale.leads.dto;

import com.fisioroyale.leads.model.LeadInteracao;
import com.fisioroyale.leads.model.InteracaoTipo;

import java.time.LocalDateTime;

public record InteracaoResponse(Long id, InteracaoTipo tipo, String descricao, LocalDateTime ocorridaEm) {
    public static InteracaoResponse from(LeadInteracao i) {
        return new InteracaoResponse(i.getId(), i.getTipo(), i.getDescricao(), i.getOcorridaEm());
    }
}
