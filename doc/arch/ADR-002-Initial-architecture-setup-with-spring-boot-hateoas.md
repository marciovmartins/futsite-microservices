# ADR-002: Initial architecture setup with Spring Boot HATEOAS

Date: 2021-11-28

# CONTEXT

Expose the first api endpoint to be consumed by some other system. This is going to be the initial setup of the project
that will be followed for the rest of this project.

## DECISION

We will use the [Spring Data REST](https://docs.spring.io/spring-data/rest/docs/current/reference/html/#reference) and
each bounded context are going to be on their own folder.

The first bounded context is going to be Matches.

We will split the responsibilities in sub-folders by Model. Inside this model folders there will be entities, inbound
controller resources and supporting libraries as database implementation or external services.

### Model

Each model relates to some central concept inside the bounded context for example Match, or Player, or Match
Convocation.

## STATUS

Accepted.

## CONSEQUENCES

The future feature are going to follow this folder structure and the separation of concerns. For future developers it is
going to be easier to rapidly start to develop new features or even improve or fix old one.

The idea is to stress and test the beneficial usage for Spring Boot Data Rest + HATEOAS and learn where should we use
this pattern or where we should use some more complex strategy when creating APIs.