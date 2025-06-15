Gerenciamento de Garagem - API
========================================

Descrição:
-----------
Este projeto é uma API REST desenvolvida em Java 17 com Spring Boot, Maven, Lombok e Docker, destinada ao gerenciamento de vagas (spots) de estacionamento e controle de eventos de veículos via webhook. A documentação da API é gerada automaticamente com Springdoc OpenAPI (Swagger).

----------------------------------------
Tecnologias e Ferramentas:
----------------------------------------
- Java 17
- Maven
- Spring Boot
- Lombok (para reduzir código boilerplate)
- Docker (para empacotamento e deploy em contêiner)
- Swagger / Springdoc OpenAPI (documentação e testes interativos)
- MySQL (banco de dados relacional)

----------------------------------------
Pré-requisitos:
----------------------------------------
Certifique-se de ter instalados:
- Java 17+ (verifique sua versão com "java -version")
- Maven (instruções em: https://maven.apache.org/install.html)
- Docker Desktop (opcional, para execução em container)
- MySQL (ou utilize um container Docker para rodar o MySQL)
- Uma IDE com suporte a Lombok (ex.: IntelliJ, Eclipse ou VSCode. Eu utilizei o IntelliJ para o desenvolvimento do código)

Importante: Habilite o Annotation Processing na sua IDE.
* No IntelliJ: Vá em File > Settings > Build, Execution, Deployment > Compiler > Annotation Processors e marque "Enable annotation processing".

----------------------------------------
Configurando o Banco de Dados MySQL:
----------------------------------------
Você pode utilizar uma instalação local do MySQL ou rodar via Docker.

Opção 1 – Usando Docker para MySQL:
----------------------------------------
1. Execute o comando abaixo para iniciar um container MySQL:
   (Substitua "senhadb" pela senha desejada e "garagemanagement" pelo nome do seu banco, se necessário.)

   > docker run --name mysql-db -e MYSQL_ROOT_PASSWORD=senhadb -e MYSQL_DATABASE=garagemanagement -p 3306:3306 -d mysql:latest

2. Verifique se o container está rodando:
   > docker ps

3. Atualize o arquivo application.properties (em src/main/resources) com as configurações do MySQL:
   spring.datasource.url=jdbc:mysql://localhost:3306/garage_api?useSSL=false&serverTimezone=UTC
   spring.datasource.username=root
   spring.datasource.password=senhadb
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

   # Opções de JPA/Hibernate
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

Opção 2 – Usando uma Instalação Local do MySQL:
----------------------------------------
- Instale o MySQL no seu sistema e crie um banco de dados chamado, por exemplo, "garage_api".
- Atualize o application.properties conforme mostrado acima, ajustando a URL, usuário e senha de acordo com sua instalação.

----------------------------------------
Iniciando o MySQL (para usuários de macOS com Homebrew):
----------------------------------------
1. Inicie o serviço MySQL:
   > brew services start mysql

2. Verifique se o MySQL está rodando:
   > brew services list

3. Acesse o MySQL:
   > mysql -u root -p
   (Ao ser solicitado, informe a senha: SUA-SENHA-AQUI)

4. No prompt do MySQL, escolha o banco de dados:
   mysql> USE garage_api;

5. Execute consultas para verificar os dados:
   mysql> SELECT * FROM spot;
   mysql> SELECT * FROM vehicle;
   mysql> SELECT * FROM vehicle WHERE license_plate = 'PLACA-AQUI';

Para mostrar se a vaga está ocupada como valor boolean (convertendo para 'true' ou 'false'):
mysql> SELECT id, license_plate, lat, longi, IF(occupied, 'true', 'false') AS occupied FROM spot;

----------------------------------------
Executando a Aplicação Localmente:
----------------------------------------
1. Clonando o Repositório:
   > git clone https://github.com/MarianeAnjos/garage-management
   > cd garage-management

2. Construindo o Projeto com Maven:
   > mvn clean package

   Isso irá gerar um JAR na pasta "target/".

3. Executando o JAR:
   > java -jar target/garage-management-api.jar

   A aplicação será iniciada na porta 8080 e se conectará ao MySQL conforme configurado.

4. Acessando a Documentação:
    - Swagger UI: http://localhost:8080/swagger-ui/index.html
    - JSON da API: http://localhost:8080/v3/api-docs

----------------------------------------
Executando com Docker:
----------------------------------------
1. Crie um Dockerfile na raiz do projeto (caso ainda não exista) com o seguinte conteúdo:
-----------------------------------------
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/garage-management-api.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
-----------------------------------------

2. Gere o JAR com Maven:
   > mvn clean package

3. Construa a Imagem Docker:
   > docker build -t garage-api .

4. Execute o Container:
   > docker run -d -p 8080:8080 garage-api

   A API ficará disponível na porta 8080.

----------------------------------------
Endpoints Principais:
----------------------------------------
Aqui estão alguns dos endpoints expostos pela API, agora com nomenclaturas ajustadas:

Método    | Endpoint         | Descrição
-----------------------------------------------------------
POST      | /webhook         | Processa eventos de veículo: ENTRY, PARKED e EXIT
POST      | /plate-status    | Consulta status do veículo por placa
GET       | /revenue         | Consulta faturamento (opcionalmente por data via query param)
GET       | /spots           | Lista todas as vagas (spots)
POST      | /spots           | Cria um novo spot
POST      | /spots/spot-status | Consulta status do spot com base em coordenadas
POST      | /parked          | (Opcional) Endpoint específico para o evento PARKED
POST      | /plate-exit      | (Opcional) Endpoint específico para o evento EXIT

Exemplos de Payload para /webhook:
----------------------------------------
Para Entrada na Garagem ("ENTRY"):
{
"license_plate": "ZUL0001",
"entry_time": "2025-01-01T12:00:00.000Z",
"event_type": "ENTRY"
}

Para Entrada na Vaga ("PARKED"):
{
"license_plate": "ZUL0001",
"lat": -23.561684,
"lng": -46.655981,
"event_type": "PARKED"
}

Para Saída da Garagem ("EXIT"):
{
"license_plate": "ZUL0001",
"exit_time": "2025-01-01T12:00:00.000Z",
"event_type": "EXIT"
}

Observação: Se optar por endpoints separados (como /parked ou /plate-exit), os payloads permanecem os mesmos.

----------------------------------------
Configurações Importantes:
----------------------------------------
Lombok:
----------
- Dependência Maven (pom.xml):

      <dependency>
         <groupId>org.projectlombok</groupId>
         <artifactId>lombok</artifactId>
         <optional>true</optional>
      </dependency>

- Lembre-se de ativar o Annotation Processing na IDE.
- 
----------------------------------------
Possíveis Erros e Soluções:
----------------------------------------

1. "Spot not found"
   ----------------------------------------
   Causa: O spot com as coordenadas fornecidas não foi encontrado no banco de dados.
   Solução:
    - Utilize o endpoint POST /spots para criar um spot antes de testar o evento "PARKED".
    - Verifique se os valores de "lat" e "lng" enviados no payload coincidem exatamente com os criados.

2. "IncorrectResultSizeDataAccessException"
   ----------------------------------------
   Causa: O método findByLatAndLng() retornou mais de 1 registro (duplicidade).
   Solução:
    - Garanta que a classe Spot possua a restrição de unicidade (unique constraint) para (lat, lng).
    - Se múltiplos spots com as mesmas coordenadas forem permitidos, altere o método do repositório para retornar uma lista (List<Spot>) e implemente a lógica para tratar múltiplos resultados.

3. Endpoints não aparecem no Swagger
   ----------------------------------------
   Causa: Possível falta de anotação @RestController ou controllers fora do pacote base.
   Solução:
    - Certifique-se de que todas as controllers estão anotadas com @RestController e localizadas em um pacote abaixo da classe principal com @SpringBootApplication.
    - Verifique o JSON da API em http://localhost:8080/v3/api-docs para confirmar os endpoints.

4. Problemas com Lombok
   ----------------------------------------
   Causa: As annotations do Lombok não estão sendo processadas pela IDE.
   Solução:
    - Ative o Annotation Processing na IDE.
    - Verifique se a dependência do Lombok está corretamente configurada no pom.xml.

----------------------------------------
Estrutura do Projeto (Simplificada):
----------------------------------------
garagemanagementapi/
└── src/
└── main/
├── java/
│    └── org/example/garagemanagementapi/
│         ├── controller/      (Controllers, ex.: WebhookController, SpotController, etc.)
│         ├── dto/           (DTOs, ex.: WebhookEvent, PlateStatusRequest, etc.)
│         ├── model/         (Modelos de dados, ex.: Spot, Vehicle)
│         ├── repository/    (Repositórios, ex.: SpotRepository, VehicleRepository)
│         └── GarageManagementApiApplication.java
└── resources/
└── application.properties

Dockerfile (na raiz do projeto):
----------------------------------------
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/garage-management-api.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

----------------------------------------
Logs e Debug:
----------------------------------------
- Utilize System.out.println() ou uma biblioteca de logging (como Logback) para depurar a aplicação.
- Para visualizar os logs se estiver utilizando Docker, use:
  docker logs <CONTAINER_ID>

----------------------------------------
Considerações Finais:
----------------------------------------
Esta API foi desenvolvida para demonstrar uma solução completa com Java 17, Spring Boot, Maven, Lombok, Docker e integração via Webhook para gerenciamento de estacionamento, utilizando MySQL como banco de dados.
Caso haja dúvidas ou contribuições, sinta-se à vontade para abrir issues ou enviar pull requests.

Feito com <3 por Mariane Anjos

