# Deal microservice
This project provides interaction methods for <i>deals</i>.

## Install
### Preferenced requirements
* Java 21
* Maven 3.9.9
* Spring Boot 3.5.3
* Docker 

### Steps to install project
1. Clone repository
```shell
git clone https://github.com/NiRO-bb/deal.git
```

2. Build with Maven
```shell
mvn clean package 
```

3. Create .env files
   You must write .env_dev and .env_prod files with following values (you can use .env_template file from root directory):
* SERVER_PORT
* POSTGRES_USER (only for .env_prod - used for PSQL container)
* POSTGRES_PASSWORD (only for .env_prod - used for PSQL container)
* POSTGRES_DB (only for .env_prod - used for PSQL container)
* SPRING_DATASOURCE_URL
* SPRING_DATASOURCE_USERNAME
* SPRING_DATASOURCE_PASSWORD
* TOKEN_SECRET_KEY
* APP_RABBIT_HOST
* APP_RABBIT_PORT
* APP_RABBIT_DLX
* APP_RABBIT_CONTRACTOR_DLX
* APP_RABBIT_QUEUE
* APP_RABBIT_DEAD_QUEUE
* SPRING_DATA_REDIS_HOST
* SPRING_DATA_REDIS_PORT

<p>.env_dev - for local development </p>
<p>.env_prod - for container (docker) development</p>

## Usage
1. Create network
```shell
docker network create rabbit-system
```
2. Launch RabbitMQ
```shell
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 --network rabbit-system rabbitmq:3-management
```
Launch docker
```shell
docker-compose up -d
```

## Contributing
<a href="https://github.com/NiRO-bb/deal/graphs/contributors/">Contributors</a>

## License
No license 