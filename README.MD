Spring Cloud template app:
-
Template project on Spring Cloud for a quick-start project with a microservice architecture.
The project has customized ELK-stack, monitoring system via Grafana/Prometheus, Kafka as message broker, Postgres database, Redis as cache, Keycloak as identity provider, and they're all based on docker-containers.

Run:
-
Before start docker-compose you need to build images of configserver, gatewayserver, license-service, organization-service via gradle task "bootBuildImage".
And after that just run docker-compose.

In progress:
- migration to Spring Boot 3, Spring Framework 6
- logging via AOP