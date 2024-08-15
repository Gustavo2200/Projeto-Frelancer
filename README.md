# MYFREELAS
Este é um código de gerenciamento de uma plataforma de freelancers, que inclui endpoints como registro de usuários, login, registro de projetos, envio de propostas e transferências. O código foi desenvolvido com intuito acadêmico.

## Instruções de Uso
Este código deve ser usado apenas para fins educacionais.

Apenas os endpoints "/save-user" e "/get-token" não necessitam de autenticação.
Para os demais endpoints, é necessário um token de autenticação.

### Requisitos para executar o spring

- JDK 17+
- PostgresSQL
- Maven 3.9+

### Clonando o repositorio

Para clonar o repositório crie uma pasta onde preferir em seu computador e execute o seguinte comando
    
    git clone https://github.com/Gustavo2200/Projeto-Frelancer.git

## Rodando o projeto em sua maquina
Com o projeto clonado abra seu terminal e vá até a pasta onde clonou o projeto.
execute os seguintes comandos :

    mvn clean install
    mvn spring-boot:run

Com isso o projeto será executado , mas caso não tenha o banco de dados configurado siga os passos abaixo.

### Criando o  banco de dados.
Para ter acesso a todas as funcionalidades da plataforma, é necessário criar o banco de dados a partir do dump fornecido. Siga os passos abaixo para configurar o banco de dados:

Crie um banco de dados:
Abra o terminal e execute o seguinte comando para criar um novo banco de dados:

    createdb myfreelasdb

## Importe o dump do banco de dados:
Com o banco de dados criado, execute o seguinte comando para importar o dump:

    psql -U seu_usuario -d myfreelasdb -f caminho/para/o/dumpBanco.dump

Substitua *seu_usuario* pelo seu nome de usuário do PostgreSQL e *caminho/para/o/dumpBanco.backup* pelo caminho para o arquivo de dump no seu sistema.

## Configure a conexão no aplicativo:
Ajuste a conexão no apliccation-local.properties:

### spring.datasource.url=jdbc:postgresql://localhost:5432/myfreelasdb
### spring.datasource.username=seu_usuario
### spring.datasource.password=sua_senha 
 
## Link da documentação

Uma vez com a aplicação em execução em sua maquina acesse acesse o link abaixo para ter acesso a documentação completa dos enpoints:

### http://localhost:8080/api-myfreelas/swagger-ui.html

## Tecnologias usadas
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white) ![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens) ![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white) ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white) ![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white) ![Jira](https://img.shields.io/badge/jira-%230A0FFF.svg?style=for-the-badge&logo=jira&logoColor=white) ![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)
## Desenvolvedor Responsavel
### Gustavo Vinicius Parreiras
