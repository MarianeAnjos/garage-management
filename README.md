Garage Management API
=====================

Descrição:
-----------
API REST em Java 17 com Spring Boot para gerenciamento de vagas de estacionamento e controle de entrada/saída de veículos via Webhook. Utiliza MySQL como banco de dados e Swagger para documentação. Desenvolvido com Maven, Docker e Lombok.

----------------------------
Tecnologias e Ferramentas:
----------------------------
- Java 17
- Spring Boot 3.2+
- Maven
- Lombok
- MySQL 8
- Docker
- Swagger (springdoc-openapi)

--------------------
Pré-requisitos:
--------------------
- Java 17+
- Maven
- Docker (opcional, mas recomendado)
- MySQL (local ou via Docker)
- IDE com suporte a Lombok (ex: IntelliJ)

IMPORTANTE:
No IntelliJ, habilite o Annotation Processing:
File > Settings > Build, Execution, Deployment > Compiler > Annotation Processors > Enable

----------------------------------------
Configuração do Banco de Dados MySQL:
----------------------------------------

Opção 1 – Usando Docker:
-------------------------
docker run --name mysql-db \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=garage_api \
  -p 3306:3306 \
  -d mysql:latest

Opção 2 – Instalação Local:
----------------------------
Crie o banco de dados:

CREATE DATABASE garage_api CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-----------------------------
application.properties:
-----------------------------
spring.datasource.url=jdbc:mysql://localhost:3306/garage_api?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
server.port=8080

-----------------------------------------
Executando o Projeto com Maven:
-----------------------------------------
1. Clone o repositório:
   git clone https://github.com/MarianeAnjos/garage-management-api.git
   cd garage-management-api

2. Compile:
   mvn clean package

3. Rode a aplicação:
   java -jar target/garage-management-api-1.0-SNAPSHOT.jar

4. Acesse:
   Swagger UI: http://localhost:8080/swagger-ui.html
   API Docs JSON: http://localhost:8080/v3/api-docs

-----------------------------
Executando com Docker:
-----------------------------
1. Gere o JAR:
   mvn clean package

2. Crie o Dockerfile (se ainda não existir):

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/garage-management-api-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

3. Construa a imagem:
   docker build -t garage-api .

4. Rode o container:
   docker run -d -p 8080:8080 --name garage-api garage-api

-------------------------------------
Endpoints Principais:
-------------------------------------
```bash
Método   | Endpoint              | Descrição
-----------------------------------------------------------
POST     | /webhook              | Processa eventos ENTRY, PARKED, EXIT
POST     | /plate-status         | Consulta status da placa
GET      | /revenue              | Receita total ou por data (via ?date=YYYY-MM-DD)
GET      | /spots                | Lista todas as vagas
POST     | /spots                | Cria uma nova vaga
POST     | /spots/spot-status    | Consulta vaga via lat/lng
```
------------------------------
Exemplo de Payloads:
------------------------------

ENTRY:
{
  "licensePlate": "ZUL0001",
  "event": "ENTRY"
}

PARKED:
{
  "licensePlate": "ZUL0001",
  "lat": -23.561684,
  "lng": -46.655981,
  "event": "PARKED"
}

EXIT:
{
  "licensePlate": "ZUL0001",
  "event": "EXIT"
}

------------------------------
### Estrutura do Projeto

```bash
garage-management-api/
└── src/
    └── main/
        ├── java/
        │   └── org/example/garagemanagementapi/
        │       ├── controller/
        │       ├── dto/
        │       ├── model/
        │       ├── repository/
        │       └── GarageManagementApiApplication.java
        └── resources/
            └── application.properties
```

------------------------------
Lombok:
------------------------------
- Dependência no pom.xml:

<dependency>
  <groupId>org.projectlombok</groupId>
  <artifactId>lombok</artifactId>
  <optional>true</optional>
</dependency>

- Habilite o annotation processing na IDE

----------------------------------------
Erros Comuns:
----------------------------------------

1. Endpoints não aparecem no Swagger
   - Verifique se as controllers têm @RestController
   - Certifique-se de que estão no mesmo pacote base do @SpringBootApplication

2. "Spot not found"
   - Certifique-se de ter criado um Spot com as coordenadas enviadas

3. Porta 8080 ocupada
   - Finalize o processo que está usando a porta:
     lsof -i :8080
     kill -9 <PID>

4. Lombok não funciona
   - Plugin não instalado ou annotation processing desativado

----------------------------------------
Logs:
----------------------------------------
Para ver os logs no Docker:
docker logs -f garage-api

Para acessar o banco no terminal:
docker exec -it mysql-db mysql -uroot -proot
mysql> USE garage_api;

----------------------------------------
Autoria:
----------------------------------------
Projeto desenvolvido por Mariane Ferreira dos Anjos  
Contato: mariane.ferreiraanjos@outlook.com  
Entrega técnica: Estapar - Back-end 
