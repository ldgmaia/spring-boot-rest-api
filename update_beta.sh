git pull
#mvn package -Dmaven.test.skip
#sudo systemctl restart newrbms.service
docker compose -f compose_beta.yml down
docker compose -f compose_beta.yml up --build -d
