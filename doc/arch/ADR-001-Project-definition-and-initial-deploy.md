# ADR-001: Project Definition and Initial Deploy

Date: 2021-10-16

# CONTEXT

Initial setup for the futsite pelada publisher & organizer application. It is necessary to set up the initial project
and deploy of the core domain.

## Amateur Soccer Match Core Domain

In this domain will be possible:

* To register a match and calculate the ranking of those matches.
* To do convocations of soccer games.
* To register finance entries for the group of for the players.
* Photo album.
* Forum.

## DECISION

We will use kotlin 1.5 jvm 16 as main programming language. Spring Boot with Maven as main framework. Junit and AssertJ
as test suite. Gitlab as VCS with Trunk-base development¹, each domain or subdomain are going to be a maven module.

We will deploy the application using the gitlab pipelines on Heroku.

## STATUS

Accepted.

## CONSEQUENCES

The kotlin stack is define, also the test suite, the pipeline configuration for development and production.

### Links

[1] [https://www.atlassian.com/continuous-delivery/continuous-integration/trunk-based-development](https://www.atlassian.com/continuous-delivery/continuous-integration/trunk-based-development)