# ADR-002: Initial Model-View-Controller to Register a Match

Date: 2021-11-28

# CONTEXT

It is necessary to expose the first api endpoint to be consumed by some other system. This is going to be the initial
setup of the project that will be followed for the rest of this project.

## DECISION

We will use the MVC pattern and each bounded context are going to be on their own folder. Each bounded context is going
to have a Model, a View and a Controller sub-folder.

### Model

It is the central part for the business rules. Here all the validations, persistence and external libraries are going to
live. For example the Match concept, there will be all the properties related to the match, also the repository to
persist.

### View

This area is responsible for the external interface that the client has contact. For apis this mean the resource
mapping, the properties, the body and/or the response payload.

### Controller

The controllers are similar to use cases, responsible for some behaviours, for example to register a match, or calculate
a ranking, or register a player.

## STATUS

Accepted.

## CONSEQUENCES

The future feature are going to follow this folder structure and the separation of concerns. For future developers it is
going to be easier to rapidly start to develop new features or even improve or fix old one.

The idea is to stress and test the beneficial usage for MVC pattern and learn where should we use this pattern or where
we should use some more complex strategy when creating APIs.