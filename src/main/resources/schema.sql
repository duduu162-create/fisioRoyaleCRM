CREATE TABLE IF NOT EXISTS area_servico (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    categoria VARCHAR(120),
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);
CREATE UNIQUE INDEX IF NOT EXISTS uk_area_servico_nome ON area_servico ((lower(btrim(nome))));

CREATE TABLE IF NOT EXISTS lead (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(180) NOT NULL,
    telefone VARCHAR(40),
    email VARCHAR(180),
    origem VARCHAR(100),
    responsavel VARCHAR(120),
    status VARCHAR(40) NOT NULL DEFAULT 'NOVO',
    observacoes TEXT,
    proxima_acao_em TIMESTAMP,
    motivo_perda VARCHAR(180),
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    convertido_em TIMESTAMP,
    fase1_pessoa_id BIGINT,
    fase1_tipo VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS lead_area (
    lead_id BIGINT NOT NULL REFERENCES lead(id) ON DELETE CASCADE,
    area_id BIGINT NOT NULL REFERENCES area_servico(id),
    PRIMARY KEY (lead_id, area_id)
);

CREATE TABLE IF NOT EXISTS lead_interacao (
    id BIGSERIAL PRIMARY KEY,
    lead_id BIGINT NOT NULL REFERENCES lead(id) ON DELETE CASCADE,
    tipo VARCHAR(40) NOT NULL,
    descricao TEXT NOT NULL,
    ocorrida_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_lead_status ON lead(status);
CREATE INDEX IF NOT EXISTS idx_lead_proxima_acao ON lead(proxima_acao_em);
CREATE INDEX IF NOT EXISTS idx_lead_criado_em ON lead(criado_em);
CREATE INDEX IF NOT EXISTS idx_interacao_lead_data ON lead_interacao(lead_id, ocorrida_em DESC);

INSERT INTO area_servico(nome, categoria) VALUES
('Pilates','Movimento'),
('Hidro','Movimento'),
('RPG','Fisioterapia'),
('Fisioterapia','Fisioterapia'),
('Endocrinologia','Medicina'),
('Estética','Estética')
ON CONFLICT DO NOTHING;
