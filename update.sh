git pull
#mvn package -Dmaven.test.skip
#sudo systemctl restart newrbms.service
docker compose down
docker compose up --build -d
