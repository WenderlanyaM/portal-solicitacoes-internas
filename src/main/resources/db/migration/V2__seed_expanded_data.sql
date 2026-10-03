-- -----------------------------------------------------------------
-- 1. Inserção de 5 Novos Colaboradores (IDs de 2 a 6)
-- Senha padrão para todos: user123
-- -----------------------------------------------------------------
INSERT INTO tbl_user (user_name, user_password, user_fk_role) VALUES
                                                                  ('ana.lima', '$2a$12$Iwuk/ApEaGaDHvQnmArJrOmWsKXnNiwutXCRddfa3RPzs56ClxiK6', 1),
                                                                  ('carlos.souza', '$2a$12$Iwuk/ApEaGaDHvQnmArJrOmWsKXnNiwutXCRddfa3RPzs56ClxiK6', 1),
                                                                  ('mariana.alves', '$2a$12$Iwuk/ApEaGaDHvQnmArJrOmWsKXnNiwutXCRddfa3RPzs56ClxiK6', 1),
                                                                  ('lucas.rocha', '$2a$12$Iwuk/ApEaGaDHvQnmArJrOmWsKXnNiwutXCRddfa3RPzs56ClxiK6', 1),
                                                                  ('beatriz.mendes', '$2a$12$Iwuk/ApEaGaDHvQnmArJrOmWsKXnNiwutXCRddfa3RPzs56ClxiK6', 1);

-- -----------------------------------------------------------------
-- 2. Inserção de 15 Solicitações Diversificadas
-- Categorias: 1-TI, 2-RH, 3-Compras, 4-Financeiro, 5-Infraestrutura
-- Usuários (fk_user): 1 a 6
-- -----------------------------------------------------------------
INSERT INTO tbl_ticket (title, description, status, creation_date, fk_category, fk_user) VALUES
                                                                                             ('Solicitação de Segundo Monitor', 'Necessidade de monitor adicional para análise de planilhas.', 'ABERTO', '2026-09-01 09:15:00', 1, 1),
                                                                                             ('Ajuste na Ponto Eletrônico', 'Esquecimento de batida de ponto no dia 28/08 na saída.', 'CONCLUIDO', '2026-09-02 10:30:00', 2, 2),
                                                                                             ('Compra de Licença JetBrains', 'Renovação da licença do IntelliJ para desenvolvimento Java.', 'EM_ATENDIMENTO', '2026-09-05 14:20:00', 3, 3),
                                                                                             ('Reembolso de Despesas de Viagem', 'Comprovantes da viagem técnica a São Paulo anexados ao processo.', 'CONCLUIDO', '2026-09-08 11:00:00', 4, 4),
                                                                                             ('Manutenção no Ar-Condicionado', 'Equipamento da sala 3 fazendo barulho excessivo e sem gelar.', 'ABERTO', '2026-09-10 08:45:00', 5, 5),
                                                                                             ('Reset de Senha do e-Mail Corporate', 'Bloqueio de conta após 3 tentativas incorretas.', 'CONCLUIDO', '2026-09-12 16:10:00', 1, 6),
                                                                                             ('Atualização do Plano de Saúde', 'Inclusão de dependente no plano médico corporativo.', 'EM_ATENDIMENTO', '2026-09-14 09:00:00', 2, 1),
                                                                                             ('Aquisição de Cadeiras Ergonômicas', 'Solicitação para troca das cadeiras desgastadas da equipe.', 'ABERTO', '2026-09-15 13:50:00', 3, 2),
                                                                                             ('Adiantamento de Décimo Terceiro', 'Pedido referente à primeira parcela das férias programadas.', 'CONCLUIDO', '2026-09-18 15:30:00', 4, 3),
                                                                                             ('Troca de Lâmpadas do Corredor', 'Duas lâmpadas queimadas no setor de TI.', 'CONCLUIDO', '2026-09-20 17:00:00', 5, 4),
                                                                                             ('Acesso à Pasta Compartilhada', 'Permissão de leitura e escrita no diretório de Projetos_2026.', 'EM_ATENDIMENTO', '2026-09-22 10:05:00', 1, 5),
                                                                                             ('Declaração de Vinculo Empregatício', 'Emissão de comprovante para fins acadêmicos.', 'CONCLUIDO', '2026-09-24 11:40:00', 2, 6),
                                                                                             ('Compra de Webcams HD', 'Necessidade de 3 webcams para reuniões remotas com clientes.', 'ABERTO', '2026-09-26 14:15:00', 3, 1),
                                                                                             ('Aprovação de Orçamento de Treinamento', 'Curso de Spring Boot Security para a equipe de desenvolvimento.', 'EM_ATENDIMENTO', '2026-09-28 09:30:00', 4, 2),
                                                                                             ('Reparo na Fechadura Eletrônica', 'Tag de acesso falhando na porta principal do lab.', 'ABERTO', '2026-09-30 16:45:00', 5, 3);