# Spring Cloud template app

Template project on Spring Cloud for a quick-start project with a microservice architecture. It contains config-server, gateway and 2 main services.
The project has customized ELK-stack, monitoring system via Grafana/Prometheus, Kafka as message broker, Postgres database, Redis as cache, Keycloak as identity provider, and they're all based on docker-containers.

## Features

- Eureka server as Discovery service. It allows microservices to get the actual IP address of other microservices without having to specify the address directly. You can learn more about it on https://github.com/Netflix/eureka
- Config server is a single point of storage and distribution of configurations for other microservices. In the case of updating configs, you do not need to rebuild the affected microservices and redeploy them. They are capable to automatically receive an updated configuration from the outside.
- Gateway server is the single point of access to the entire application. It redirects all REST-requests to the necessary microservices. This behavior allows you to close access to microservices from the outside and redirect interaction with them through a single access point.
- Kibana as browser-based analytics and search dashboard for Elasticsearch. https://github.com/elastic/kibana
- Zipkin as distributed tracing system. It helps gather timing data needed to troubleshoot latency problems in service architectures. You can follow the flow of each of the rest-operations that affect different services and evaluate the performance of this operation. More about Zipkin on https://zipkin.io/
- Keycloak server works as identity provider. It provides all the work of creating new user accounts, their authorization and authentication via jwt tokens. And much more functions. https://www.keycloak.org/
- Postgresql and Redis as sql and nosql databases respectively.
- Kafka as pull-based approach message broker. https://kafka.apache.org/
- Grafana allows you to query, visualize, alert on, and understand metrics of java-applications. https://grafana.com/

## Run

Before start docker-compose you need to build images of configserver, gatewayserver, license-service, organization-service via gradle task `bootBuildImage`. 
And after that just run docker-compose.