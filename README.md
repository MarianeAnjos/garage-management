Garage Management API
=====================

Descrição:
-----------
API REST em Java 17 com Spring Boot para gerenciamento de vagas de estacionamento e controle de entrada/saída de veículos via Webhook.
Utiliza MySQL como banco de dados e Swagger para documentação. Desenvolvido com Maven, Docker e Lombok.

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
docker run --name garage-db \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=garage_api \
  -p 3307:3306 \
  -d mysql:8.0

Opção 2 – Instalação Local:
----------------------------
Crie o banco de dados:

CREATE DATABASE garage_api CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

Opção 3 – Instalação Local:

Crie um usuário com permissões:
CREATE USER 'garage_user'@'localhost' IDENTIFIED BY 'password123';
GRANT ALL PRIVILEGES ON garage_api.* TO 'garage_user'@'localhost';
FLUSH PRIVILEGES;

-----------------------------
application.properties:
-----------------------------
spring.datasource.url=jdbc:mysql://garage-db:3306/garage_db
spring.datasource.username=root
spring.datasource.password=root

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

springdoc.swagger-ui.path=/swagger-ui.html
springdoc.api-docs.path=/api-docs

server.port=3003

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
   Swagger UI: http://localhost:3003/swagger-ui/index.html
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
  docker run -d -p 3003:3003 --name garage-api garage-api

----------------------------------------
Scripts Auxiliares:
----------------------------------------
``` Bash
Scripts auxiliares: foram criados scripts para facilitar o processo de start/stop dos
containers Docker e abertura do Swagger.

Scripts úteis:
start-garage.sh – Inicia a aplicação e o banco via Docker Compose,
aguardando até que o MySQL esteja pronto:


#!/bin/bash
echo "⛔ Encerrando containers antigos..."
docker-compose down

echo "🚀 Subindo containers em segundo plano..."
docker-compose up -d --build

echo "⏳ Aguardando banco de dados ficar disponível..."
until docker exec garage-db mysqladmin ping -h "localhost" --silent; do
    echo "🔄 Banco ainda não está pronto. Aguardando..."
    sleep 3
done

echo "✅ Banco de dados está disponível!"
echo "🧪 Testando conexão com o banco de dados..."
docker exec garage-db mysql -uroot -proot -e "USE garage_api; SHOW TABLES;"
if [ $? -eq 0 ]; then
    echo "✅ Conexão com o banco funcionando!"
else
    echo "❌ Falha ao conectar no banco de dados."
fi

Como usar:
Dê permissão de execução: chmod +x start-garage.sh
Execute: ./start-garage.sh

start.sh – Inicia os containers e abre automaticamente o Swagger no navegador:


#!/bin/bash
echo "🔄 Subindo os containers com Docker Compose..."
docker-compose up --build -d

echo "🕒 Aguardando a API iniciar..."
sleep 8  # Ajuste conforme o tempo de inicialização da aplicação

echo "🌐 Abrindo Swagger no navegador..."
xdg-open http://localhost:8080/swagger-ui/index.html  # No Linux use xdg-open

Como usar:
Dê permissão de execução: chmod +x start.sh
Execute: ./start.sh

clean.sh – Para todos os containers e limpa volumes/redes Docker associados:

#!/bin/bash
echo "🛑 Parando os containers..."
docker-compose down -v

echo "🧹 Limpando containers órfãos e redes não utilizadas..."
docker container prune -f
docker volume prune -f
docker network prune -f

echo "✅ Tudo limpo!"

Como usar:
Dê permissão de execução: chmod +x clean.sh
Execute: ./clean.sh

```
-----------------------------------------
Configuração de Vagas no Estacionamento
-----------------------------------------

**Gerenciamento de Spots (Vagas)**  
Cada vaga de estacionamento é gerenciada pela entidade `Spot`.  
Todos os registros de vagas devem ser manipulados via `SpotRepository`.

-----------------------------------------
**Criando uma vaga no banco**
-----------------------------------------
Para adicionar manualmente uma vaga no MySQL, execute:

```sql
CREATE DATABASE garage_api CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE garage_api;

INSERT INTO spot (lat, lng, occupied, sector) VALUES (-23.561684, -46.655981, false, 'A');

SELECT * FROM spot;
```

-----------------------------------------
**Criando uma vaga via API **
-----------------------------------------

``` bash
curl -X POST http://localhost:3003/spots -H "Content-Type: application/json" -d '{
  "lat": -23.561684,
  "lng": -46.655981,
  "sector": "A"
}'

```
-------------------------------------
***Verificar vagas existentes:***
-------------------------------------
```bash
curl -X GET http://localhost:3003/spots
```
Cao precise consultar uma vaga especifica via lat/lng:

```bash
curl -X POST http://localhost:3003/spots/spot-status -H "Content-Type: application/json" -d '{
  "lat": -23.561684,
  "lng": -46.655981
}'
```
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
                ├── config/
        │       ├── controller/
        │       ├── dto/
        │       ├── model/
                ├── service/
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

1. "Spot not found"
   - Certifique-se de ter criado um Spot com as coordenadas enviadas

2. Porta 8080 ocupada
   - Finalize o processo que está usando a porta:
     lsof -i :8080
     kill -9 <PID>

3. Lombok não funciona
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
Projeto desenvolvido por Mariane Anjos  
Contato: mariane.ferreiraanjos@outlook.com  
Entrega técnica: Back-end 


