package com.fisioroyale.leads.service;

import com.fisioroyale.leads.dto.DashboardResponse;
import com.fisioroyale.leads.dto.InteracaoRequest;
import com.fisioroyale.leads.dto.InteracaoResponse;
import com.fisioroyale.leads.dto.LeadRequest;
import com.fisioroyale.leads.dto.LeadResponse;
import com.fisioroyale.leads.model.AreaServico;
import com.fisioroyale.leads.model.InteracaoTipo;
import com.fisioroyale.leads.model.Lead;
import com.fisioroyale.leads.model.LeadInteracao;
import com.fisioroyale.leads.model.LeadStatus;
import com.fisioroyale.leads.repository.AreaServicoRepository;
import com.fisioroyale.leads.repository.LeadInteracaoRepository;
import com.fisioroyale.leads.repository.LeadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
public class LeadService {
    private final LeadRepository leadRepository;
    private final AreaServicoRepository areaRepository;
    private final LeadInteracaoRepository interacaoRepository;

    public LeadService(LeadRepository leadRepository, AreaServicoRepository areaRepository, LeadInteracaoRepository interacaoRepository) {
        this.leadRepository = leadRepository;
        this.areaRepository = areaRepository;
        this.interacaoRepository = interacaoRepository;
    }

    @Transactional(readOnly = true)
    public List<LeadResponse> listar(String busca, LeadStatus status, Long areaId) {
        String q = busca == null ? "" : busca.trim().toLowerCase();
        return leadRepository.findAll().stream()
                .filter(l -> status == null || l.getStatus() == status)
                .filter(l -> areaId == null || l.getAreas().stream().anyMatch(a -> a.getId().equals(areaId)))
                .filter(l -> q.isBlank() || String.join(" ", n(l.getNome()), n(l.getTelefone()), n(l.getEmail()), n(l.getOrigem()), n(l.getResponsavel())).toLowerCase().contains(q))
                .sorted(Comparator.comparing(Lead::getAtualizadoEm, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(LeadResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public LeadResponse buscar(Long id) { return LeadResponse.from(entidade(id)); }

    public LeadResponse criar(LeadRequest request) {
        Lead lead = new Lead();
        aplicar(lead, request, true);
        Lead salvo = leadRepository.save(lead);
        interacaoRepository.save(new LeadInteracao(salvo, InteracaoTipo.STATUS, "Lead criado no estágio " + salvo.getStatus()));
        return LeadResponse.from(salvo);
    }

    public LeadResponse atualizar(Long id, LeadRequest request) {
        Lead lead = entidade(id);
        LeadStatus anterior = lead.getStatus();
        aplicar(lead, request, false);
        if (anterior != lead.getStatus()) {
            interacaoRepository.save(new LeadInteracao(lead, InteracaoTipo.STATUS, "Status alterado de " + anterior + " para " + lead.getStatus()));
        }
        return LeadResponse.from(lead);
    }

    public InteracaoResponse adicionarInteracao(Long leadId, InteracaoRequest request) {
        Lead lead = entidade(leadId);
        InteracaoTipo tipo = request.tipo() != null ? request.tipo() : InteracaoTipo.NOTA;
        return InteracaoResponse.from(interacaoRepository.save(new LeadInteracao(lead, tipo, request.descricao().trim())));
    }

    @Transactional(readOnly = true)
    public List<InteracaoResponse> historico(Long leadId) {
        if (!leadRepository.existsById(leadId)) throw new IllegalArgumentException("Lead não encontrado");
        return interacaoRepository.findByLeadIdOrderByOcorridaEmDesc(leadId).stream().map(InteracaoResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public DashboardResponse dashboard() {
        List<Lead> leads = leadRepository.findAll();
        Map<String, Long> porStatus = new LinkedHashMap<>();
        for (LeadStatus s : LeadStatus.values()) porStatus.put(s.name(), leads.stream().filter(l -> l.getStatus() == s).count());
        LocalDate hoje = LocalDate.now();
        YearMonth mes = YearMonth.from(hoje);
        long novosHoje = leads.stream().filter(l -> l.getCriadoEm() != null && l.getCriadoEm().toLocalDate().equals(hoje)).count();
        long acoesHoje = leads.stream().filter(l -> l.getProximaAcaoEm() != null && l.getProximaAcaoEm().toLocalDate().equals(hoje) && ativo(l)).count();
        long atrasados = leads.stream().filter(l -> l.getProximaAcaoEm() != null && l.getProximaAcaoEm().isBefore(LocalDateTime.now()) && ativo(l)).count();
        long convertidosMes = leads.stream().filter(l -> l.getConvertidoEm() != null && YearMonth.from(l.getConvertidoEm()).equals(mes)).count();
        List<LeadResponse> prioridades = leads.stream().filter(this::ativo)
                .filter(l -> l.getProximaAcaoEm() != null)
                .sorted(Comparator.comparing(Lead::getProximaAcaoEm))
                .limit(12).map(LeadResponse::from).toList();
        return new DashboardResponse(porStatus, novosHoje, acoesHoje, atrasados, convertidosMes, prioridades);
    }

    public Lead entidade(Long id) {
        return leadRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Lead não encontrado"));
    }

    public void registrarConversao(Lead lead, Long fase1PessoaId, String fase1Tipo) {
        lead.setStatus(LeadStatus.CONVERTIDO);
        lead.setConvertidoEm(LocalDateTime.now());
        lead.setFase1PessoaId(fase1PessoaId);
        lead.setFase1Tipo(fase1Tipo);
        lead.setProximaAcaoEm(null);
        interacaoRepository.save(new LeadInteracao(lead, InteracaoTipo.CONVERSAO, "Convertido na Fase 1 como " + fase1Tipo + " #" + fase1PessoaId));
    }

    private void aplicar(Lead lead, LeadRequest request, boolean novo) {
        String nome = request.nome() == null ? "" : request.nome().trim();
        if (nome.isBlank()) throw new IllegalArgumentException("Informe o nome do lead");
        lead.setNome(nome);
        lead.setTelefone(trim(request.telefone()));
        lead.setEmail(trim(request.email()));
        lead.setOrigem(trim(request.origem()));
        lead.setResponsavel(trim(request.responsavel()));
        lead.setObservacoes(trim(request.observacoes()));
        lead.setProximaAcaoEm(request.proximaAcaoEm());
        lead.setMotivoPerda(trim(request.motivoPerda()));
        if (request.status() == LeadStatus.CONVERTIDO && lead.getFase1PessoaId() == null) {
            throw new IllegalArgumentException("Use a ação Converter para Fase 1 para concluir a conversão do lead");
        }
        if (request.status() != null) lead.setStatus(request.status());
        else if (novo) lead.setStatus(LeadStatus.NOVO);
        if (lead.getStatus() == LeadStatus.PERDIDO && (lead.getMotivoPerda() == null || lead.getMotivoPerda().isBlank())) {
            throw new IllegalArgumentException("Informe o motivo da perda");
        }
        if (request.areaIds() != null) {
            Set<Long> ids = new LinkedHashSet<>(request.areaIds());
            List<AreaServico> areas = areaRepository.findAllById(ids);
            if (areas.size() != ids.size()) throw new IllegalArgumentException("Uma ou mais áreas/serviços não existem");
            lead.setAreas(new LinkedHashSet<>(areas));
        }
    }

    private boolean ativo(Lead l) {
        return l.getStatus() != LeadStatus.CONVERTIDO && l.getStatus() != LeadStatus.PERDIDO;
    }

    private String trim(String v) { return v == null ? null : v.trim(); }
    private String n(String v) { return v == null ? "" : v; }
}
