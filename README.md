
# Sistema de Gerenciamento de Biblioteca


Este projeto é um sistema de gerenciamento de biblioteca, permitindo o controle de empréstimos, usuários e integração com a API do Google Books. A aplicação utiliza ``PostgreeSQL`` como banco de dados, e todas as instruções para configuração estão detalhadas abaixo.



## Instalação

## Obs.| Repositório do Front


```bash
  https://github.com/arom-danelli/gerenciador-de-biblioteca-front.git
```

## Criando e conectando o seu banco no Sistema
A aplicação utiliza PostgreSQL como banco de dados. Para garantir que o sistema funcione corretamente, siga os passos abaixo para configurar o PostgreSQL.

Caso não tenha, basta baixar por aqui.

[Download PostgreSQL](https://www.postgresql.org/download/)

## Configurar o Arquivo ``application.properties``

Agora, configure as credenciais do banco de dados no arquivo
``src/main/resources/application.properties``

```bash
 # Configuração do banco de dados PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/nome-do-seu-banco
spring.datasource.username=seu-username
spring.datasource.password=sua-senha

# Dialeto do Hibernate para PostgreSQL
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Driver JDBC para PostgreSQL
spring.datasource.driver-class-name=org.postgresql.Driver

# Configurações do Hibernate
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.format_sql=true

```

* ``spring.datasource.url:``: O nome do banco de dados criado.
* ``spring.datasource.username``: Seu nome de usuário do PostgreSQL (geralmente postgres).
* ``spring.datasource.password``: A senha configurada durante a instalação do PostgreSQL. 

## Executar a aplicação
Dependendo da sua IDE é possível inicar o projeto startando a Main ``BibliotecaAromApplication``

ou

```bash
mvn spring-boot:run
```

## Para executar testes
No console da sua IDE:
```bash
mvn test
```

Existe um total de 28 testes.

Obs.: As vezes é necessário rodar os comandos com ctrl + enter. (dica da lôra)

## E agora?

Nesse momento já é possível fazer testes via [Swagger](http://localhost:8080/swagger-ui/index.html) por exemplo.

## Ou

Pode clonar o repo do front-end por [aqui](  https://github.com/arom-danelli/gerenciador-de-biblioteca-front.git).
