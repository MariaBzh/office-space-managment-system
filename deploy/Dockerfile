FROM ubuntu:22.04

RUN apt-get -y update && apt-get -y install nginx

COPY volumes/nginx /etc/nginx/sites-available/default

EXPOSE 80/tcp

ENTRYPOINT ["/usr/sbin/nginx", "-g", "daemon off;"]