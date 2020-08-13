# Builder
FROM node:10.16.0 as builder

WORKDIR /app

COPY . /app/
RUN npm install && npm run build

# Image
FROM nginx:1.17.2
LABEL maintainner="Pavol Hronsky <hronsky.pavol@gmail.com>"
LABEL version="1.0.0"

EXPOSE 8080

COPY --from=builder /app/dist/boardgames-portal /usr/share/nginx/html
COPY default.conf /etc/nginx/conf.d/default.conf
