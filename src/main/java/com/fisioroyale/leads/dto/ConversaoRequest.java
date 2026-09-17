package com.fisioroyale.leads.dto;

public record ConversaoRequest(
        String tipoDestino,
        String servicoPrincipal,
        Integer frequenciaSemanal
) {}
