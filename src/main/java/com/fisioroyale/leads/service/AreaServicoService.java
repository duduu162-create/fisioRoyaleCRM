package com.fisioroyale.leads.service;

import com.fisioroyale.leads.dto.AreaServicoResponse;
import com.fisioroyale.leads.model.AreaServico;
import com.fisioroyale.leads.repository.AreaServicoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AreaServicoService {
    private final AreaServicoRepository repository;

    public AreaServicoService(AreaServicoRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<AreaServicoResponse> listar() {
        return repository.findByAtivoTrueOrderByNomeAsc().stream().map(AreaServicoResponse::from).toList();
    }

    public AreaServicoResponse criar(String nome, String categoria) {
        String n = nome == null ? "" : nome.trim();
        if (n.isBlank()) throw new IllegalArgumentException("Informe o nome da área/serviço");
        return AreaServicoResponse.from(repository.save(new AreaServico(n, categoria == null ? null : categoria.trim())));
    }

    public AreaServicoResponse atualizar(Long id, String nome, String categoria, Boolean ativo) {
        AreaServico area = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Área/serviço não encontrado"));
        if (nome != null && !nome.isBlank()) area.setNome(nome.trim());
        if (categoria != null) area.setCategoria(categoria.trim());
        if (ativo != null) area.setAtivo(ativo);
        return AreaServicoResponse.from(area);
    }
}
