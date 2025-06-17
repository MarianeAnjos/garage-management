echo "🛑 Parando os containers..."
docker-compose down -v

echo "🧹 Limpando containers órfãos e redes não utilizadas..."
docker container prune -f
docker volume prune -f
docker network prune -f

echo "✅ Tudo limpo!"
