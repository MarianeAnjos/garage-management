echo "⛔ Encerrando containers antigos..."
docker-compose down

echo "🚀 Subindo containers em segundo plano..."
docker-compose up -d --build

echo "⏳ Aguardando banco de dados ficar disponível..."

# Espera até o MySQL responder ao ping
until docker exec garage-db mysqladmin ping -h"localhost" --silent; do
    echo "🔄 Banco ainda não está pronto. Aguardando..."
    sleep 3
done

echo "✅ Banco de dados está disponível!"

# Testa conexão executando um SELECT
echo "🧪 Testando conexão com o banco de dados..."
docker exec garage-db mysql -uroot -proot -e "USE garagem; SHOW TABLES;"

if [ $? -eq 0 ]; then
    echo "✅ Conexão com o banco funcionando!"
else
    echo "❌ Falha ao conectar no banco de dados."
fi
