package com.fisioroyale.leads.dto;

import com.fisioroyale.leads.model.AreaServico;

public record AreaServicoResponse(Long id, String nome, String categoria, boolean ativo) {
    public static AreaServicoResponse from(AreaServico a) {
        return new AreaServicoResponse(a.getId(), a.getNome(), a.getCategoria(), a.isAtivo());
    }
}
