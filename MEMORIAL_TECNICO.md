# MEMORIAL TÉCNICO DE DESENVOLVIMENTO

**Candidato(a):** Wenderlânya Medeiros  
**Processo Seletivo:** Desenvolvedor(a) de Sistemas Júnior (09/2026) — bit Soluções  
**Projeto:** Portal de Solicitações Internas  
**Data de Entrega:** Outubro de 2026

---

## 1. Relação de Tecnologias Utilizadas

| Categoria | Tecnologia / Ferramenta | Versão Exata | Função no Ecossistema |
| :--- | :--- | :--- | :--- |
| **Linguagem** | Java | 21 LTS (21.0.12) | Linguagem principal do backend, provendo tipagem forte e concorrência nativa. |
| **Framework Backend** | Spring Boot | 4.1.1 | Framework base para injeção de dependências, controle MVC e gestão de componentes. |
| **Segurança** | Spring Security | 7.1.1 | Camada de autenticação, proteção contra ataques (CSRF) e controle de sessão HTTP. |
| **ORM / Persistência** | Spring Data JPA / Hibernate | 4.1.1 / 7.4.5.Final | Abstração da camada de dados e mapeamento objeto-relacional das entidades. |
| **Migração de Banco** | Flyway DB Migration | 12.4.0 | Gerenciamento e versionamento automatizado dos scripts SQL de esquema e massa de testes. |
| **Validação** | Jakarta Bean Validation | 3.1.1 | Validação declarativa das regras de entrada nos formulários (`@NotBlank`, `@NotNull`). |
| **View Engine** | Thymeleaf | 3.1.5.RELEASE | Motor de renderização dinâmica de páginas HTML no lado do servidor (SSR). |
| **Interface / UI** | Bootstrap + Icons | 5.3.2 / 1.11.1 | Framework CSS para layout responsivo, componentes de tela e suporte a Dark Mode. |
| **Banco de Dados** | MySQL Server | 8.4 (Driver 9.7.0) | Persistência relacional de dados com suporte a transações ACID e integridade referencial. |
| **Containerização** | Docker & Docker Compose | Docker v29.7.2 | Isolamento do ambiente do banco de dados e garantia de paridade de execução. |

---

## 2. Justificativa Técnica por Etapa de Desenvolvimento

### 2.1 Etapa 1: Definição da Arquitetura Backend (Java 21 + Spring Boot 4.1.1)
* **Motivo da Escolha:** O ecossistema Spring Boot na versão 4.1.1 aliada ao Java 21 foi selecionado para alinhar o projeto à stack tecnológica moderna e amplamente adotada corporativamente.
* **Benefícios para o Cenário:** Promove a separação rigorosa de responsabilidades entre as camadas do sistema (Controller -> Repository -> Model).
* **Vantagens em Relação a Alternativas:** Em relação a ecossistemas dinâmicos (como Node.js ou Python), o Java com Spring Boot oferece tipagem estática que previne falhas em tempo de execução e garante alta performance em operações concorrentes na Web.
* **Impacto na Produtividade e Escalabilidade:** Reduz o tempo de *boilerplate* e facilita a adição de novos módulos ou a migração do monolito para microsserviços no futuro sem reescrita de código.

### 2.2 Etapa 2: Estratégia de Banco de Dados e Migração (MySQL 8.4 + Flyway 12.4.0)
* **Motivo da Escolha:** A adoção do Flyway foi motivada pela necessidade de substituir o gerenciamento automático DDL do Hibernate (`spring.jpa.hibernate.ddl-auto=update`), mantendo o controle total dos scripts SQL executados no banco.
* **Benefícios para o Cenário:** A criação das migrations `V1__init_schema.sql` (estrutura) e `V2__seed_expanded_data.sql` (massa de teste) garante que o banco de dados seja criado do zero com idempotência e exatidão em qualquer máquina.
* **Vantagens em Relação a Alternativas:** Scripts de migração rastreáveis na tabela `flyway_schema_history` impedem divergências de esquema entre ambientes de desenvolvimento e produção.
* **Impacto na Manutenção:** Torna atualizações de tabelas seguras e auditáveis ao longo do ciclo de vida do software.

### 2.3 Etapa 3: Arquitetura de Segurança e Autenticação (Spring Security 7.1.1 + BCrypt)
* **Motivo da Escolha:** Escolheu-se criar uma classe de autenticação customizada (`MyUsernamePwdAuthenticationProvider`) acoplada ao hash de senhas BCrypt para atender ao requisito de controle de acesso e verificação de usuários no banco.
* **Benefícios para o Cenário:** A classe customizada busca o usuário no repositório e carrega a sua `Role` associada utilizando `JOIN FETCH`.
* **Vantagens em Relação a Alternativas:** Evita o erro de `LazyInitializationException` comum no Spring Security ao carregar relacionamentos preguiçosos de papéis de acesso do usuário após a sessão ser encerrada.

### 2.4 Etapa 4: Camada de Persistência, Filtros e Ordenação (Spring Data JPA)
* **Motivo da Escolha:** A implementação do método `filterTickets` no `TicketRepository` usando JPQL customizado e o parâmetro `org.springframework.data.domain.Sort`.
* **Motivo da Escolha Técnica:** Permite realizar filtros combinados (título, categoria, status e período) e ordenação por qualquer coluna diretamente na engine do banco de dados.
* **Benefícios e Impacto:** Elimina a necessidade de carregar todas as linhas da tabela para a memória da JVM antes de filtrar/ordenar, garantindo tempo de resposta mínimo da API em cenários com grande volume de solicitações.

### 2.5 Etapa 5: Interface do Usuário e Experiência do Usuário (Thymeleaf 3.1.5 + Bootstrap 5.3.2)
* **Motivo da Escolha:** A escolha de Thymeleaf com Bootstrap 5.3.2 permitiu desenvolver telas dinâmicas com renderização server-side (SSR) em curto prazo.
* **Benefícios para o Cenário:** Foi definida uma identidade visual limpa e profissional em **Azul e Branco**, botões de edição em **Azul Pastel** (`#93c5fd`), modais para detalhes e seletores do tipo `dropup` na tabela para evitar o corte do menu de alteração de status.
* **Recurso de Destaque (Dark Mode):** Foi incorporado um script em JavaScript que lê e grava o tema preferido no `localStorage` do navegador. Isso garante que a preferência de modo noturno se mantenha ativa ao recarregar ou mudar de página.

### 2.6 Etapa 6: Conteinerização e Segurança de Variáveis (Docker v29.7.2 + `.env`)
* **Motivo da Escolha:** A adição do suporte ao Docker Compose (utilizando a engine Docker 29.7.2) com arquivo de variáveis de ambiente (`.env`) visa facilitar a avaliação e o deploy da aplicação.
* **Benefícios para o Cenário:** Permite que qualquer avaliador suba a infraestrutura completa de dados com um único comando (`docker compose up -d`).
* **Vantagens de DevSecOps:** Isola completamente as credenciais do banco de dados do código-fonte público, cumprindo boas práticas de segurança.

---

## 3. Justificativa Conceitual e Arquitetural Aprofundada

A construção do **Portal de Solicitações Internas** foi orientada por padrões arquiteturais consolidados na engenharia de software para sistemas corporativos, assegurando manutenibilidade, testabilidade, separação clara de responsabilidades e segurança por design.

### 3.1 Padrão Arquitetural MVC (Model-View-Controller) e Fluxo de Dados
A aplicação foi estruturada estritamente sob o padrão **MVC**, em que cada camada possui fronteiras de atuação bem delimitadas:
* **Model (`info.bitsolucoes.portal.model`):** Representa o domínio de negócios e a integridade dos dados. Não contém regras de navegação nem lógica de apresentação.
* **Controller (`info.bitsolucoes.portal.controller`):** Atua como o orquestrador do fluxo da aplicação HTTP. O `TicketController` recebe as requisições, invoca as validações do Bean Validation via `@Valid`, interage com o repositório de dados e repassa o estado processado à camada de visão através de modelos dinâmicos (`Model`) e atributos de redirecionamento (`RedirectAttributes`).
* **View (`src/main/resources/templates`):** Responsável exclusivamente pela apresentação. As páginas Thymeleaf renderizam o código HTML baseado no estado fornecido pelo Controller, sem executar regras de negócio diretamente no template.

### 3.2 Modelagem de Domínio e Mapeamento Objeto-Relacional (ORM)
A modelagem conceitual do banco de dados priorizou a integridade referencial (ACID) e o desempenho em consultas relacionais:
* **Entidades e Relacionamentos:** O sistema é mapeado pelas entidades `User`, `Role`, `Category` e `Ticket`. A entidade `Ticket` mantém chaves estrangeiras explícitas (`fk_category` e `fk_user`) anotadas com `@ManyToOne(fetch = FetchType.EAGER)`, garantindo que o autor e a categoria sejam carregados junto à solicitação sem a necessidade de consultas N+1 secundárias.
* **Callbacks de Ciclo de Vida do JPA (`@PrePersist`):** Para garantir a consistência dos dados independente da camada de entrada, a entidade `Ticket` utiliza o método de callback `@PrePersist`. Este método define automaticamente o carimbo de data/hora (`LocalDateTime.now()`) e força o status inicial `ABERTO` no instante exato da inserção no banco de dados.

### 3.3 Arquitetura de Segurança, Estado de Sessão e Criptografia
A estratégia de segurança fundamenta-se nos seguintes princípios conceituais:
* **Autenticação Customizada via `AuthenticationProvider`:** Em vez de depender do gerenciador padrão em memória ou do *UserDetailsService* básico, implementou-se a classe `MyUsernamePwdAuthenticationProvider`. Ela intercepta a requisição de login, busca o usuário pelo nome com consulta customizada em `UserRepository` e valida o hash da senha enviada.
* **Proteção Criptográfica Ativa (BCrypt):** Nenhuma senha é armazenada ou comparada em texto plano. O algoritmo `BCryptPasswordEncoder` com *salt* dinâmico garante resistência contra ataques de dicionário e tabela *rainbow*.
* **Gestão de Sessão e CSRF:** O Spring Security gerencia o ciclo de vida da sessão HTTP (`JSESSIONID`). Tokens CSRF (*Cross-Site Request Forgery*) são exigidos em todas as operações que alteram o estado da aplicação (métodos `POST`), prevenindo ataques de execução forçada de comandos.

### 3.4 Otimização na Camada de Dados: Execução no Banco vs. Processamento em Memória
Uma decisão conceitual crítica refere-se ao local onde os dados são filtrados e ordenados:
* **Processamento no SGBD:** A ordenação dinâmica e os filtros multicritério foram codificados diretamente em JPQL no `TicketRepository`, tirando proveito dos índices do MySQL.
* **Justificativa Conceitual:** Realizar ordenação ou filtragem na aplicação (utilizando Java Streams em memória) exigiria trafegar todas as solicitações do banco para o servidor a cada requisição. Tratar a filtragem e a ordenação via `org.springframework.data.domain.Sort` no nível do banco de dados reduz o tráfego de rede, diminui a pressão sobre o Garbage Collector da JVM e garante escalabilidade linear mesmo com milhares de registros.

### 3.5 Renderização Server-Side (SSR) e Design do Sistema de UI/UX
* **Server-Side Rendering (SSR) vs. Single Page Application (SPA):** A escolha por Thymeleaf (SSR) elimina a necessidade de construir um ecossistema separado no frontend (React/Angular) e gerenciar estados complexos de API REST/JWT para um projeto desse escopo. O servidor entrega páginas HTML totalmente renderizadas com baixo tempo de carregamento inicial.
* **Componentização e Adaptabilidade Visual (Dark/Light Mode):** A interface foi construída com tokens visuais do Bootstrap 5.3.2 (`data-bs-theme`). A alternância do modo noturno utiliza JavaScript para atualizar o atributo raiz do DOM e persistir a escolha no `localStorage` do cliente. Isso garante que a preferência do usuário seja preserveda ao navegar entre rotas ou recarregar a aplicação.
* **Ergonomia e Usabilidade (UX):** As ações da tabela contam com diálogos de confirmação para exclusão, modais para leitura de detalhes e seletores de alteração de status do tipo `dropup` com limite de viewport (`data-bs-boundary="viewport"`), evitando sobreposição com as margens inferiores da página.

---

## 4. Análise Crítica e Sugestões de Melhorias Futuras

Com o objetivo de demonstrar visão crítica sobre a arquitetura construída e seu comportamento em um cenário real de produção corporativa, destacam-se três propostas de evolução:

### 4.1 Permissões Granulares de Exclusão Baseadas na Autoria da Solicitação
* **Cenário Atual:** Qualquer usuário autenticado no sistema tem permissão para excluir uma solicitação desde que o seu status seja `ABERTO`.
* **Melhoria Proposta:** Adicionar uma regra de validação na camada de serviço verificando se o ID do usuário logado é idêntico ao ID do criador do chamado (`ticket.user.id == currentUser.id`). Essa alteração impede que um colaborador exclua inadvertidamente a demanda de um colega de trabalho.

### 4.2 Restrição da Alteração de Status ao Setor Responsável da Categoria
* **Cenário Atual:** A alteração de status do chamado (de `ABERTO` para `EM_ATENDIMENTO` ou `CONCLUIDO`) está disponível para todos os colaboradores do portal.
* **Melhoria Proposta:** Associar os usuários aos seus respectivos setores de atuação (ex: TI, RH, Compras). A permissão de transição de status para "Em Atendimento" seria restrita aos membros do setor responsável daquela categoria específica ou ao próprio autor (para solicitar o encerramento).

### 4.3 Segregação e Filtro Individual das Métricas do Dashboard
* **Cenário Atual:** O Dashboard contabiliza e exibe os números globais de toda a empresa (Total, Abertas, Em Atendimento, Concluídas).
* **Melhoria Proposta:** Implementar uma chave de alternância no Dashboard permitindo alternar entre duas visões:
    1. **Visão Geral Corporativa:** Métrica consolidada para uso gerencial.
    2. **Minhas Solicitações (Visão Individual):** Indicadores calculados dinamicamente considerando apenas os chamados associados ao usuário que está navegando no momento, permitindo um acompanhamento focado do seu fluxo pessoal de trabalho.