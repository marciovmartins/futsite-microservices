# :soccer: Learning How to Create a Good Microservice Ecosystem

[<img src="doc/icons/gitlab-color.svg" width="25px"/>](https://about.gitlab.com/)
[<img src="doc/icons/kotlin-color.svg" width="25px"/>](https://kotlinlang.org/)
[<img src="doc/icons/docker-color.svg" width="25px"/>](https://www.docker.com/)
[<img src="doc/icons/apachemaven-color.svg" width="25px"/>](https://maven.apache.org/)
[<img src="doc/icons/springboot-color.svg" width="25px"/>](https://spring.io/projects/spring-boot/)
[<img src="doc/icons/mysql-color.svg" width="25px"/>](https://www.mysql.com/)
[<img src="doc/icons/hibernate-color.svg" width="25px"/>](https://spring.io/projects/spring-data-jpa)
[<img src="doc/icons/flyway-color.svg" width="25px"/>](https://flywaydb.org/)
[<img src="doc/icons/swagger-color.svg" width="25px"/>](https://swagger.io/)
[<img src="doc/icons/rabbitmq-color.svg" width="25px"/>](https://www.rabbitmq.com/)
[<img src="doc/icons/junit5-color.svg" width="25px"/>](https://junit.org/junit5/)
[<img src="doc/icons/javascript-color.svg" width="25px"/>](https://developer.mozilla.org/en-US/docs/Web/javascript)
[<img src="doc/icons/npm-color.svg" width="25px"/>](https://www.npmjs.com/)
[<img src="doc/icons/react-color.svg" width="25px"/>](https://react.dev/)
[<img src="doc/icons/reactrouter-color.svg" width="25px"/>](https://reactrouter.com/en/main)
[<img src="doc/icons/jest-color.svg" width="25px"/>](https://jestjs.io/)

![Badge Status](https://img.shields.io/badge/STATUS-DEVELOPMENT-green)

---

## :arrow\_heading\_up:Index

- [:arrow\_heading\_up: Index](#arrowheadingupindex)
- [:green\_book: About](#greenbook-about)
- [:brain: ADR - Architecture Decision Record](#brain-adr---architecture-decision-record)

---

## :green\_book: About

Started in October 2022, the project has the goal to study and apply different technics using Microservices approach.

Created the first [ADR](https://adr.github.io/) to define [the architecture decisions](/doc/arch),
setup Spring Boot and the gitlab pipeline pointing to my personal kubernetes cluster with 3 raspberry pi.

Created 2 microservices: 
- ASM: Amateur Soccer Mgmt (complex domain using onion architecture)
- User-Data (simple domain using [one layer](https://spring.io/projects/spring-data-rest))
- Web (React.js)

Using [Swagger/OpenApi](https://swagger.io/) to generate resource documentation,
[Spring HATEOAS](https://spring.io/projects/spring-hateoas) and
[Spring Data Jpa](https://spring.io/projects/spring-data-jpa).

Using [Domain-Drive Design - DDD](https://martinfowler.com/bliki/DomainDrivenDesign.html) with 3 layers for complex aggregates and 
[Spring Data Rest](https://spring.io/projects/spring-data-rest) with one layer for simpler aggregates.

Learned [React.js](https://react.dev/) and [Jest](https://jestjs.io/)

## :brain: ADR - Architecture Decision Record

- [001: Project Definition and Initial Deploy](doc/arch/ADR-001-Project-definition-and-initial-deploy.md)
- [002: Initial architecture setup with Spring Boot HATEOAS](doc/arch/ADR-002-Initial-architecture-setup-with-spring-boot-hateoas.md)
- [003: Initial Frontend Service with React.js v17.0.2](doc/arch/ADR-003-Initial-frontend-service-with-react-js-v17-0-2.md)