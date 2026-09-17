package com.fisioroyale.leads.web;

import com.fisioroyale.leads.dto.ConversaoRequest;
import com.fisioroyale.leads.dto.InteracaoRequest;
import com.fisioroyale.leads.dto.InteracaoResponse;
import com.fisioroyale.leads.dto.LeadRequest;
import com.fisioroyale.leads.dto.LeadResponse;
import com.fisioroyale.leads.model.LeadStatus;
import com.fisioroyale.leads.service.Fase1IntegrationService;
import com.fisioroyale.leads.service.LeadService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/leads")
public class LeadController {
    private final LeadService service;
    private final Fase1IntegrationService integrationService;

    public LeadController(LeadService service, Fase1IntegrationService integrationService) {
        this.service = service;
        this.integrationService = integrationService;
    }

    @GetMapping
    public List<LeadResponse> listar(
            @RequestParam(required = false) String busca,
            @RequestParam(required = false) LeadStatus status,
            @RequestParam(required = false) Long areaId
    ) { return service.listar(busca, status, areaId); }

    @GetMapping("/{id}")
    public LeadResponse buscar(@PathVariable Long id) { return service.buscar(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LeadResponse criar(@Valid @RequestBody LeadRequest request) { return service.criar(request); }

    @PutMapping("/{id}")
    public LeadResponse atualizar(@PathVariable Long id, @Valid @RequestBody LeadRequest request) { return service.atualizar(id, request); }

    @GetMapping("/{id}/interacoes")
    public List<InteracaoResponse> historico(@PathVariable Long id) { return service.historico(id); }

    @PostMapping("/{id}/interacoes")
    @ResponseStatus(HttpStatus.CREATED)
    public InteracaoResponse interagir(@PathVariable Long id, @Valid @RequestBody InteracaoRequest request) {
        return service.adicionarInteracao(id, request);
    }

    @PostMapping("/{id}/converter")
    public LeadResponse converter(@PathVariable Long id, @RequestBody ConversaoRequest request) {
        return integrationService.converter(id, request);
    }
}
