#!/bin/bash
echo "🔄 Subindo os containers com Docker Compose..."
docker-compose up --build -d

echo "🕒 Aguardando a API iniciar..."
sleep 8 # ajuste conforme o tempo de inicialização da sua app

echo "🌐 Abrindo Swagger no navegador..."
open http://localhost:3003/swagger-ui/index.html
