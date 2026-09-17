# Fisio Royale CRM

Webservice comercial da Fisio Royale para controle de leads de Pilates, Hidro, RPG, Fisioterapia, Endocrinologia, Estética e futuras áreas/serviços.

## Arquitetura

Este repositório é independente da Agenda/Fase 1 (`duduu162-create/fisio-royale`). A comunicação entre os dois serviços acontece por HTTP e token quando um lead é convertido em aluno/paciente.

## Variáveis de ambiente

- `DATABASE_URL` — JDBC URL do PostgreSQL/Neon, por exemplo `jdbc:postgresql://...`
- `DATABASE_USERNAME` — usuário do banco
- `DATABASE_PASSWORD` — senha do banco
- `PHASE1_BASE_URL` — URL pública da Fase 1, atualmente `https://fisio-royale.onrender.com`
- `PHASE1_INTEGRATION_TOKEN` — segredo compartilhado com `LEADS_INTEGRATION_TOKEN` na Fase 1
- `PORT` — fornecida pelo Render automaticamente

## Render

O serviço deve ser criado como Web Service usando este repositório, branch `main`, runtime Docker, Dockerfile `./Dockerfile`, root directory vazio e auto-deploy habilitado.

## Banco

O `schema.sql` cria as tabelas do CRM de forma idempotente e cadastra as áreas iniciais. O Hibernate está configurado com `ddl-auto=validate`.
