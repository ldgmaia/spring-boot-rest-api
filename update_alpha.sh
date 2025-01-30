git pull
#mvn package -Dmaven.test.skip
#sudo systemctl restart newrbms.service
docker compose -f compose_alpha.yml down
docker compose -f compose_alpha.yml up --build -d
