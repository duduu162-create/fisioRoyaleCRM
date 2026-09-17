package com.fisioroyale.leads.dto;

import com.fisioroyale.leads.model.InteracaoTipo;
import jakarta.validation.constraints.NotBlank;

public record InteracaoRequest(InteracaoTipo tipo, @NotBlank String descricao) {}
