# README - Portal de Solicitações Internas (bit Soluções)

Aplicação Web Full Stack desenvolvida para o desafio técnico do Processo Seletivo para **Desenvolvedor(a) de Sistemas Júnior** da **bit Soluções**. O sistema consiste em uma plataforma corporativa para registro, acompanhamento, filtragem, ordenação dinâmica e gestão do ciclo de vida de solicitações internas de colaboradores.

---

## 1. Pré-requisitos

Para compilar e executar o projeto corretamente, certifique-se de ter os seguintes componentes instalados em sua máquina:

### Linguagem utilizada
* **Java 21 LTS** (JDK versão `21.0.12` ou superior).

### Banco de dados
* **MySQL Server 8.4** (executado de forma containerizada e isolada através do **Docker Engine** v29.7.2 e **Docker Compose**).

### Dependências
O gerenciamento das dependências do projeto é realizado pelo **Apache Maven**. As principais bibliotecas configuradas no arquivo `pom.xml` são:

* **Backend e Persistência:**
   * `Spring Boot` (v4.1.1): Framework base para injeção de dependências e controle MVC.
   * `Spring Security` (v7.1.1): Autenticação customizada e gerenciamento de sessão HTTP.
   * `Spring Data JPA / Hibernate ORM` (v4.1.1 / v7.4.5.Final): Abstração de dados e mapeamento objeto-relacional (ORM).
   * `Flyway Core / Flyway MySQL` (v12.4.0): Ferramenta de migração automatizada para versionamento de esquemas DDL e carga de dados.
   * `Jakarta Bean Validation` (v3.1.1): Validação declarativa dos formulários.
   * `MySQL Connector/J` (v9.7.0): Driver JDBC para comunicação com o banco de dados.
   * `Tomcat Embed Core` (v11.0.24): Servidor web embutido.

* **Frontend:**
   * `Thymeleaf` (v3.1.5.RELEASE): Motor de renderização dinâmico de templates (Server-Side Rendering - SSR).
   * `Thymeleaf Extras Spring Security 6` (v3.1.5): Integração das permissões de segurança diretamente nas views.
   * `Bootstrap` (v5.3.2) e `Bootstrap Icons` (v1.11.1): Framework CSS para layout responsivo e suporte a Tema Escuro.

---

## 2. Instalação

Siga o passo a passo detalhado abaixo para realizar a extração e a preparação completa do ambiente em sua máquina local. O primeiro passo é descompactar o arquivo `.zip` recebido e abrir a pasta do projeto no terminal ou em sua IDE.

### Passo a passo completo para: Banco de dados
A infraestrutura de dados foi projetada para rodar de maneira isolada em containers, não exigindo a instalação do MySQL Server nativo na máquina hospedeira.

1. Certifique-se de que o **Docker Desktop** (ou o daemon do Docker) esteja ativo e em execução em sua máquina.
2. Na raiz da pasta do projeto (exatamente onde se encontra o arquivo `compose.yaml`), execute o seguinte comando no terminal para baixar a imagem e inicializar o container do banco de dados em segundo plano:
   ```bash
   docker compose up -d
   ```
3. O Docker fará o download da imagem oficial `mysql:8.4` e subirá o banco de dados `db_portal`, que ficará aguardando conexões na porta local `3306`.

### Passo a passo completo para: Backend
A instalação do backend não requer passos manuais de compilação prévia, pois o Maven fará isso durante a execução.
Além disso, a criação das tabelas no banco de dados não exige a execução de scripts SQL externos. Ao inicializar o backend pela primeira vez, a ferramenta **Flyway Migration** detectará o banco de dados recém-criado pelo Docker e executará automaticamente os scripts versionados (`V1__init_schema.sql` e `V2__seed_expanded_data.sql`).

### Passo a passo completo para: Frontend
Esta aplicação adota o padrão de **Server-Side Rendering (SSR)** via Thymeleaf. Isso significa que o frontend (arquivos HTML, CSS e scripts) está **totalmente integrado e embarcado no próprio projeto Spring Boot**.

* Não é necessário realizar nenhuma instalação separada de dependências de frontend (como Node.js, `npm install` ou `yarn`). O Maven cuidará do empacotamento e o Tomcat servirá as telas de forma nativa.

---

## 3. Configuração

### Variáveis de ambiente
A aplicação utiliza um arquivo **`.env`** localizado na raiz do projeto para desacoplar e proteger credenciais sensíveis de banco de dados, alinhando-se às boas práticas de segurança.

Certifique-se de que o arquivo `.env` está presente na pasta raiz com as seguintes variáveis rigorosamente configuradas:

```env
MYSQL_ROOT_PASSWORD=root123
MYSQL_DATABASE=db_portal
MYSQL_USER=usuario
MYSQL_PASSWORD=usuario123
MYSQL_PORT=3306
```

> **Nota:** As variáveis do arquivo `.env` são injetadas de forma automática tanto no arquivo `compose.yaml` (para a configuração do container MySQL) quanto no arquivo `application.properties` (para estabelecer a conexão JDBC do Spring Boot).

### Credenciais de demonstração
Para facilitar os testes de fluxo, todos os usuários de demonstração foram populados na base de dados por meio da migração automatizada do Flyway.

Todos os usuários abaixo compartilham a mesma **senha padrão de acesso:** `user123`

| Usuário | Senha Padrão | Setor / Categoria de Atuação |
| :--- | :--- | :--- |
| **colaborador** | `user123` | Colaborador Geral |
| **ana.lima** | `user123` | Tecnologia da Informação (TI) |
| **carlos.souza** | `user123` | Recursos Humanos (RH) |
| **mariana.alves** | `user123` | Compras |
| **lucas.rocha** | `user123` | Financeiro |
| **beatriz.mendes** | `user123` | Infraestrutura |

---

## 4. Execução

### Comandos necessários para executar: Backend

Você pode executar o projeto de duas formas: via IntelliJ IDEA (recomendado para facilitar o desenvolvimento) ou via Terminal.

**Opção A: Via IntelliJ IDEA (Recomendado)**
1. Abra a IDE **IntelliJ IDEA**.
2. Clique em **File > Open...** e selecione a pasta raiz descompactada do projeto.
3. Aguarde o IntelliJ importar o projeto e baixar as dependências do `pom.xml`.
4. Verifique se o SDK do projeto está apontando para o Java 21 (`File > Project Structure... > Project > SDK`).
5. No painel de arquivos (Project Explorer), navegue até a classe principal:
   `src/main/java/info/bitsolucoes/portal/PortalApplication.java`
6. Clique no ícone de Play (verde) ao lado do método `main` e selecione **Run 'PortalApplication'**.
7. A aplicação estará pronta para uso assim que o console exibir a confirmação de inicialização do Tomcat na porta 8080.

**Opção B: Via Terminal (Maven Wrapper)**
1. Certifique-se de que o container do MySQL está ativo (`docker compose up -d`).
2. Abra o terminal na raiz do projeto e execute o comando adequado para o seu sistema operacional:

   * **Linux / macOS:**
     ```bash
     ./mvnw spring-boot:run
     ```
   * **Windows (PowerShell / CMD):**
     ```cmd
     .\mvnw.cmd spring-boot:run
     ```

### Comandos necessários para executar: Frontend
Como a arquitetura escolhida utiliza **Server-Side Rendering (SSR)** com a tecnologia Thymeleaf, a camada visual é processada e servida de forma nativa pelo servidor do Backend.
* **Não há comandos separados para rodar o frontend**. Ao iniciar o backend com sucesso (passos acima), o frontend subirá automaticamente e estará disponível na mesma porta.

---

## 5. Acesso

### Informações de acesso aos usuários de teste

Após a inicialização bem-sucedida do servidor:

1. Abra o navegador web de sua preferência (Google Chrome, Mozilla Firefox, Edge, etc.).
2. Na barra de endereços, digite a seguinte URL de acesso local e pressione Enter:
   ```text
   http://localhost:8080
   ```
3. Como o acesso é protegido por autenticação, o sistema redirecionará sua navegação para a página de Login (`/login`).
4. Utilize qualquer uma das credenciais informadas na seção de Configuração. Por exemplo:
   * **Usuário:** `colaborador`
   * **Senha:** `user123`
5. Após autenticar-se com sucesso, você terá acesso imediato ao **Dashboard de Indicadores** e à área de **Gerenciamento de Solicitações**.