-- ------------------------------------------------
-- 1. Criação da Tabela de Papéis/Roles
-- ------------------------------------------------
CREATE TABLE tbl_role (
                          role_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          role VARCHAR(50) NOT NULL UNIQUE
);

-- ------------------------------------------------
-- 2. Criação da Tabela de Usuários
-- ------------------------------------------------
CREATE TABLE tbl_user (
                          user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          user_name VARCHAR(100) NOT NULL UNIQUE,
                          user_password VARCHAR(250) NOT NULL,
                          user_fk_role BIGINT NOT NULL,
                          CONSTRAINT fk_user_role FOREIGN KEY (user_fk_role) REFERENCES tbl_role(role_id)
);

-- ------------------------------------------------
-- 3. Criação da Tabela de Categorias
-- ------------------------------------------------
CREATE TABLE tbl_category (
                              category_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              name VARCHAR(100) NOT NULL UNIQUE
);

-- ------------------------------------------------
-- 4. Criação da Tabela de Solicitações (Tickets)
-- ------------------------------------------------
CREATE TABLE tbl_ticket (
                            ticket_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            title VARCHAR(150) NOT NULL,
                            description TEXT NOT NULL,
                            status VARCHAR(30) NOT NULL DEFAULT 'ABERTO',
                            creation_date DATETIME NOT NULL,
                            fk_category BIGINT NOT NULL,
                            fk_user BIGINT NOT NULL,
                            CONSTRAINT fk_ticket_category FOREIGN KEY (fk_category) REFERENCES tbl_category(category_id),
                            CONSTRAINT fk_ticket_user FOREIGN KEY (fk_user) REFERENCES tbl_user(user_id)
);

-- ------------------------------------------------
-- 5. Carga Inicial de Dados (Apenas Perfil Geral e Colaborador)
-- ------------------------------------------------
INSERT INTO tbl_role (role_id, role) VALUES
    (1, 'ROLE_USER');

-- Usuário Padrão: colaborador / user123
INSERT INTO tbl_user (user_id, user_name, user_password, user_fk_role) VALUES
    (1, 'colaborador', '$2a$12$Iwuk/ApEaGaDHvQnmArJrOmWsKXnNiwutXCRddfa3RPzs56ClxiK6', 1);

-- Categorias
INSERT INTO tbl_category (category_id, name) VALUES
                                                 (1, 'TI'),
                                                 (2, 'RH'),
                                                 (3, 'Compras'),
                                                 (4, 'Financeiro'),
                                                 (5, 'Infraestrutura');

-- Solicitação Inicial vinculada ao colaborador (fk_user = 1)
INSERT INTO tbl_ticket (title, description, status, creation_date, fk_category, fk_user) VALUES
    ('Substituição de Teclado', 'Teclado com defeito nas teclas de atalho.', 'ABERTO', NOW(), 1, 1);