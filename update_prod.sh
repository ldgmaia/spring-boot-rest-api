git pull
#mvn package -Dmaven.test.skip
#sudo systemctl restart newrbms.service
docker compose -f compose_prod.yml down
docker compose -f compose_prod.yml up --build -d
