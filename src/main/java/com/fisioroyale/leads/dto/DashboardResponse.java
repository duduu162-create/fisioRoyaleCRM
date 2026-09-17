package com.fisioroyale.leads.dto;

import java.util.List;
import java.util.Map;

public record DashboardResponse(
        Map<String, Long> porStatus,
        long novosHoje,
        long acoesHoje,
        long atrasados,
        long convertidosMes,
        List<LeadResponse> prioridades
) {}
