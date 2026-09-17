package com.fisioroyale.leads.web;

import com.fisioroyale.leads.dto.AreaServicoResponse;
import com.fisioroyale.leads.service.AreaServicoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/areas")
public class AreaServicoController {
    private final AreaServicoService service;

    public AreaServicoController(AreaServicoService service) { this.service = service; }

    @GetMapping
    public List<AreaServicoResponse> listar() { return service.listar(); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AreaServicoResponse criar(@RequestBody AreaRequest request) {
        return service.criar(request.nome(), request.categoria());
    }

    @PutMapping("/{id}")
    public AreaServicoResponse atualizar(@PathVariable Long id, @RequestBody AreaRequest request) {
        return service.atualizar(id, request.nome(), request.categoria(), request.ativo());
    }

    public record AreaRequest(String nome, String categoria, Boolean ativo) {}
}
