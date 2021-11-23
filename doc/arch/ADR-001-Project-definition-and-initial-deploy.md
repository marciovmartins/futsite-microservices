# ADR-001: Project Definition and Initial Deploy

Date: 2021-11-23

# CONTEXT

Initial setup for the futsite pelada publisher & organizer application. It is necessary to set up the initial project,
base for documentation, base for client sdk and deploy of the core domain.

## Amateur Soccer Match Core Domain

In this domain will be possible:

* To register a match and calculate the ranking of those matches.
* To do convocations of soccer games.
* To register finance entries for the group of for the players.
* Photo album.
* Forum.

## DECISION

We will use kotlin 1.5 jvm 16 (or earlier) as main programming language. Spring Boot 2.6.0 with Maven 3.8.4 as main
framework. Junit 5.8.1 and AssertJ 3.21.0 as test suite for unit tests and integration tests. Git as VCS with Trunk-base
development¹, each domain or subdomain are going to be a maven module.

We will deploy the application on my personal kubernetes cluster. Expose one Hello World resource, with a client using
Swagger SDK and a kubernetes sidecar to provide the documentation.

## STATUS

Accepted.

## CONSEQUENCES

The kotlin stack is define, also the test suite, the pipeline configuration for production.

### Links

[1] [https://www.atlassian.com/continuous-delivery/continuous-integration/trunk-based-development](https://www.atlassian.com/continuous-delivery/continuous-integration/trunk-based-development)