package com.fisioroyale.leads.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "lead")
public class Lead {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 180)
    private String nome;

    @Column(length = 40)
    private String telefone;

    @Column(length = 180)
    private String email;

    @Column(length = 100)
    private String origem;

    @Column(length = 120)
    private String responsavel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private LeadStatus status = LeadStatus.NOVO;

    @Column(columnDefinition = "text")
    private String observacoes;

    @Column(name = "proxima_acao_em")
    private LocalDateTime proximaAcaoEm;

    @Column(name = "motivo_perda", length = 180)
    private String motivoPerda;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    @Column(name = "convertido_em")
    private LocalDateTime convertidoEm;

    @Column(name = "fase1_pessoa_id")
    private Long fase1PessoaId;

    @Column(name = "fase1_tipo", length = 30)
    private String fase1Tipo;

    @ManyToMany
    @JoinTable(
            name = "lead_area",
            joinColumns = @JoinColumn(name = "lead_id"),
            inverseJoinColumns = @JoinColumn(name = "area_id")
    )
    private Set<AreaServico> areas = new LinkedHashSet<>();

    public Lead() {}

    @PrePersist
    void onCreate() {
        criadoEm = LocalDateTime.now();
        atualizadoEm = criadoEm;
    }

    @PreUpdate
    void onUpdate() {
        atualizadoEm = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getOrigem() { return origem; }
    public void setOrigem(String origem) { this.origem = origem; }
    public String getResponsavel() { return responsavel; }
    public void setResponsavel(String responsavel) { this.responsavel = responsavel; }
    public LeadStatus getStatus() { return status; }
    public void setStatus(LeadStatus status) { this.status = status; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    public LocalDateTime getProximaAcaoEm() { return proximaAcaoEm; }
    public void setProximaAcaoEm(LocalDateTime proximaAcaoEm) { this.proximaAcaoEm = proximaAcaoEm; }
    public String getMotivoPerda() { return motivoPerda; }
    public void setMotivoPerda(String motivoPerda) { this.motivoPerda = motivoPerda; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
    public LocalDateTime getConvertidoEm() { return convertidoEm; }
    public void setConvertidoEm(LocalDateTime convertidoEm) { this.convertidoEm = convertidoEm; }
    public Long getFase1PessoaId() { return fase1PessoaId; }
    public void setFase1PessoaId(Long fase1PessoaId) { this.fase1PessoaId = fase1PessoaId; }
    public String getFase1Tipo() { return fase1Tipo; }
    public void setFase1Tipo(String fase1Tipo) { this.fase1Tipo = fase1Tipo; }
    public Set<AreaServico> getAreas() { return areas; }
    public void setAreas(Set<AreaServico> areas) { this.areas = areas != null ? new LinkedHashSet<>(areas) : new LinkedHashSet<>(); }
}
