docker run -d \
  --name park-php \
  -p 80:80 \
  -v /home/petru/Desktop/Uni/csubb/Lab/SEM4/Web/lab6:/var/www/html \
  php:8.2-apache

docker exec park-php docker-php-ext-install pdo pdo_sqlite
docker exec park-php bash -c "apt-get update && apt-get install -y libpng-dev && docker-php-ext-install gd"
docker restart park-php

cd ~/Desktop/Uni/csubb/Lab/SEM4/Web/lab6
mkdir -p data uploads
chmod 777 data uploads

docker exec park-php bash -c "chmod 777 /var/www/html/data/park_portal.sqlite"