package com.fisioroyale.leads.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "lead_interacao")
public class LeadInteracao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lead_id", nullable = false)
    private Lead lead;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private InteracaoTipo tipo;

    @Column(nullable = false, columnDefinition = "text")
    private String descricao;

    @Column(name = "ocorrida_em", nullable = false)
    private LocalDateTime ocorridaEm;

    protected LeadInteracao() {}

    public LeadInteracao(Lead lead, InteracaoTipo tipo, String descricao) {
        this.lead = lead;
        this.tipo = tipo;
        this.descricao = descricao;
    }

    @PrePersist
    void onCreate() {
        if (ocorridaEm == null) ocorridaEm = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Lead getLead() { return lead; }
    public InteracaoTipo getTipo() { return tipo; }
    public String getDescricao() { return descricao; }
    public LocalDateTime getOcorridaEm() { return ocorridaEm; }
}
