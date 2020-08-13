# TicTacToe Game Engine for Board Games platform

## Local setup
### Database

```
docker run -d \
--name tictactoe-db \
-e MYSQL_ROOT_PASSWORD=WeakLazyConnectionExtreme \
-e MYSQL_DATABASE=games \
-e MYSQL_USER=service \
-e MYSQL_PASSWORD=FurnitureSteelCostCat \
-p 34060:3306 \
--restart always \
-t mysql:8.0.15

docker run -d \
--name mongodb \
-e MONGO_INITDB_ROOT_USERNAME=mongoadmin \
-e MONGO_INITDB_ROOT_PASSWORD=secret \
-e MONGO_INITDB_DATABASE=games \
-p 27000:27017 \
--restart always \
mongo
```

## Docker setup
### Database

```
docker network create chatnet

docker run -d \
--name chat-db \
--net chatnet \
-e MYSQL_ROOT_PASSWORD=TakeHealthBeanWomanStandard \
-e MYSQL_DATABASE=chat \
-e MYSQL_USER=service \
-e MYSQL_PASSWORD=SaddleAppointSettleConfuse \
-t mysql:8.0.15
```

### Application

```
./gradlew clean build

docker build -t phronsky/messaging-service:1.0.0 .

docker create -d \
--name chat-service \
-v /apps/board-games/chat/conf:/app/conf:ro \
-p 8001:8080 \
--restart always \
-t phronsky/messaging-service:1.0.0

docker network connect chatnet chat-service
docker start chat-service
```





```
docker build -t phronsky/tic-tac-toe-engine:1.0.3 .
```

```
docker rm -f tic-tac-toe-engine
docker run -d \
  --name tic-tac-toe-engine \
  -v /Users/tamara/Developer/board-games/tic-tac-toe-engine/conf:/app/conf:ro \
  -p 18081:8080 \
  -e "spring.profiles.active=local" \
  -t phronsky/tic-tac-toe-engine:1.0.3
```

```
docker run -d \
  --name tic-tac-toe-engine \
  -v /apps/board-games/tic-tac-toe-engine/conf:/app/conf:ro \
  -e "spring.profiles.active=docker" \
  -e "VIRTUAL_HOST=tic-tac-toe-backend.tamarka.eu" \
  -e "VIRTUAL_PORT=8080" \
  -e "LETSENCRYPT_HOST=tic-tac-toe-backend.tamarka.eu" \
  -e "LETSENCRYPT_EMAIL=tamarka@tamarka.eu" \
  --restart always \
  -t phronsky/tic-tac-toe-engine:1.0.7
```

```
docker run -d -p 80:80 -p 443:443 \
    --name nginx-reverse-proxy \
    -v /etc/nginx/conf.d  \
    -v /usr/share/nginx/html \
    -v /etc/docker/nginx/certs:/etc/nginx/certs:ro \
    -v /etc/docker/nginx/vhost.d:/etc/nginx/vhost.d:ro \
    --restart always \
    -t nginx
```