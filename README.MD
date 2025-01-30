# Spring Cloud template app

Template project on Spring Cloud for a quick-start project with a microservice architecture.
It contains a config server, gateway, and 2 main services. The project features a customized ELK stack, a monitoring
system via Grafana/Prometheus, Kafka as a message broker, a Postgres database, Redis as cache, and Keycloak as an identity provider.
All components are based on Docker containers.

## Features

- **Eureka server** `:8070` as Discovery service. It allows microservices to obtain the actual IP address of other
microservices without having to specify the address directly. You can learn more about it [here](https://github.com/Netflix/eureka)

- **Config server** `:8071` is a single point of storage and distribution of configurations for other microservices. 
In the case of updating configs, you do not need to rebuild and redeploy the affected microservices.
They can automatically receive updated configurations from the server.

- **Gateway server** `:8072` serves as the single point of access to the entire application. It redirects all REST requests to the necessary microservices.
This setup allows you to restrict access to microservices from the outside and manage interactions through a single access point.

- **Zookeeper** `:2181` is a centralized service for maintaining configuration information, naming, providing distributed synchronization,
and group services. You can learn more [here](https://zookeeper.apache.org/)

- **Zipkin** `:9411` serves as a distributed tracing system. It helps gather timing data needed to troubleshoot latency problems in service architectures.
You can follow the flow of each REST operation affecting different services and evaluate the performance of these operations.
More about Zipkin can be found [here](https://zipkin.io/)

- **Keycloak** `:8083` server works as an identity provider. It handles the creation of new user accounts, their authorization,
and authentication via JWT tokens, among many other functions. More information is available [here](https://www.keycloak.org/)

- **Postgresql** `:5432` is an object-relational database management system (ORDBMS) based on POSTGRES.
Learn more about it [here](https://www.postgresql.org/)

- **Redis** `:6379` is the world’s fastest in-memory database. It provides cloud and on-prem solutions for caching, vector search, and NoSQL databases.
More details are available [here](https://redis.io/)

- **Kafka** `:9092` serves as a pull-based message broker. More information can be found [here](https://kafka.apache.org/)

- **ElasticSearch** `:9300`|`:9200` is a distributed search and analytics engine built on Apache Lucene.
Learn more [here](https://www.elastic.co/elasticsearch)

- **Logstash** `:5000` is an open-source server-side data processing pipeline that ingests data from multiple sources,
transforms it, and sends it to your preferred "stash." More details are available [here](https://www.elastic.co/logstash)

- **Kibana** `:5601` serves as a browser-based analytics and search dashboard for Elasticsearch. You can learn more [here](https://github.com/elastic/kibana)

- **Grafana** `:3000` allows you to query, visualize, alert on, and understand metrics of Java applications.
More information is available [here](https://grafana.com/)

- **Prometheus** `:9090`is an open-source monitoring system with a dimensional data model, flexible query language,
efficient time series database and modern alerting approach. Learn more [here](https://prometheus.io/)

## Run

Before starting Docker Compose, you need to build images for the config server, gateway server, license service, organization service:
- `gradle bootBuildImage`

After that, simply run:
```
docker-compose up
```

For the first launch you should import prepared realm for Keycloak.
You can find it in the `docker/realms` directory. To do this, open `localhost:8083` and after authorization,
select the default realm and import a prepared realm.