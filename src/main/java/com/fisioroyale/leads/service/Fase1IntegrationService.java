package com.fisioroyale.leads.service;

import com.fisioroyale.leads.dto.ConversaoRequest;
import com.fisioroyale.leads.dto.LeadResponse;
import com.fisioroyale.leads.model.Lead;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class Fase1IntegrationService {
    private final RestClient restClient;
    private final String token;
    private final LeadService leadService;

    public Fase1IntegrationService(
            RestClient.Builder builder,
            @Value("${phase1.base-url}") String baseUrl,
            @Value("${phase1.token:}") String token,
            LeadService leadService
    ) {
        this.restClient = builder.baseUrl(baseUrl).build();
        this.token = token == null ? "" : token.trim();
        this.leadService = leadService;
    }

    @Transactional
    public LeadResponse converter(Long leadId, ConversaoRequest request) {
        Lead lead = leadService.entidade(leadId);
        if (lead.getFase1PessoaId() != null) return LeadResponse.from(lead);
        if (token.isBlank()) {
            throw new IllegalStateException("Integração com a Fase 1 ainda não configurada. Defina PHASE1_INTEGRATION_TOKEN.");
        }

        String destino = request.tipoDestino() == null ? "PACIENTE" : request.tipoDestino().trim().toUpperCase();
        if (!destino.equals("ALUNO") && !destino.equals("PACIENTE")) {
            throw new IllegalArgumentException("tipoDestino deve ser ALUNO ou PACIENTE");
        }
        String principal = request.servicoPrincipal();
        if (principal == null || principal.isBlank()) {
            principal = lead.getAreas().stream().findFirst().map(a -> a.getNome()).orElse("Atendimento");
        }
        int frequencia = request.frequenciaSemanal() == null ? 1 : Math.max(1, Math.min(3, request.frequenciaSemanal()));
        List<String> areas = lead.getAreas().stream().map(a -> a.getNome()).toList();
        Fase1Request payload = new Fase1Request(destino, lead.getNome(), lead.getTelefone(), lead.getObservacoes(), principal, frequencia, areas, lead.getId());

        try {
            Fase1Response resposta = restClient.post()
                    .uri("/api/integracoes/leads/converter")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-Fisio-Integration-Token", token)
                    .body(payload)
                    .retrieve()
                    .body(Fase1Response.class);
            if (resposta == null || resposta.pessoaId() == null) throw new IllegalStateException("A Fase 1 não retornou o cadastro convertido");
            leadService.registrarConversao(lead, resposta.pessoaId(), resposta.tipo());
            return LeadResponse.from(lead);
        } catch (RuntimeException e) {
            throw new IllegalStateException("Não foi possível concluir a conversão na Fase 1: " + e.getMessage(), e);
        }
    }

    record Fase1Request(String tipoDestino, String nome, String telefone, String observacoes, String servicoPrincipal,
                        Integer frequenciaSemanal, List<String> areas, Long leadId) {}
    record Fase1Response(Long pessoaId, String tipo, boolean criado) {}
}
