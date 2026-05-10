docker run -d \
          --name WebPark \
          -e MYSQL_ROOT_PASSWORD=rootpass \
          -e MYSQL_DATABASE=park_portal \
          -e MYSQL_USER=student \
          -e MYSQL_PASSWORD=student \
          -p 3306:3306 \
          mysql:8

docker exec -i WebPark mysql -u student -pstudent park_portal < ~/Desktop/Uni/csubb/Lab/SEM4/Web/lab5/schema.sql

docker run -d \
          --name park-php \
          -p 80:80 \
          -v /home/petru/Desktop/Uni/csubb/Lab/SEM4/Web/lab5:/var/www/html \
          --link WebPark:mysql \
          php:8.2-apache

docker exec park-php docker-php-ext-install mysqli pdo pdo_mysql
docker exec park-php bash -c "apt-get update && apt-get install -y libpng-dev && docker-php-ext-install gd"
docker restart park-php

cd Desktop/Uni/csubb/Lab/SEM4/Web/lab5
mkdir data
chmod 777 data/
mkdir uploads
chmod 777 uploads/