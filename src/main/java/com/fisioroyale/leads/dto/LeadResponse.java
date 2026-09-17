package com.fisioroyale.leads.dto;

import com.fisioroyale.leads.model.Lead;
import com.fisioroyale.leads.model.LeadStatus;

import java.time.LocalDateTime;
import java.util.List;

public record LeadResponse(
        Long id,
        String nome,
        String telefone,
        String email,
        String origem,
        String responsavel,
        LeadStatus status,
        String observacoes,
        LocalDateTime proximaAcaoEm,
        String motivoPerda,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm,
        LocalDateTime convertidoEm,
        Long fase1PessoaId,
        String fase1Tipo,
        List<AreaServicoResponse> areas
) {
    public static LeadResponse from(Lead l) {
        return new LeadResponse(
                l.getId(), l.getNome(), l.getTelefone(), l.getEmail(), l.getOrigem(), l.getResponsavel(),
                l.getStatus(), l.getObservacoes(), l.getProximaAcaoEm(), l.getMotivoPerda(), l.getCriadoEm(),
                l.getAtualizadoEm(), l.getConvertidoEm(), l.getFase1PessoaId(), l.getFase1Tipo(),
                l.getAreas().stream().map(AreaServicoResponse::from).sorted((a,b)->a.nome().compareToIgnoreCase(b.nome())).toList()
        );
    }
}
