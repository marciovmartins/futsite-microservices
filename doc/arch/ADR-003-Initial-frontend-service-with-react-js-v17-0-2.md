# ADR-003: Initial Frontend Service with React.js v17.0.2

Date: 2022-03-12

# CONTEXT

It is necessary to provide the first interface for the clients to register game days. The interface is going to be used
via smartphone or tablet to register game days of amateur soccer groups in Rio de Janeiro city.

## DECISION

We will create a new web service using Node.js v16.14.0 LTS, React.js v17.0.2 and manual deploy on Marcio's kubernetes
cluster.

## STATUS

Proposed.

## CONSEQUENCES

The base structure for a web service will be implemented, deployed and ready to be used and/or expanded.

The team decided for React.js to regain knowledge about frontend technologies and be up-to-date with the most used
framework.

React.js was chosen for personal preference of the team.

The manual deployment on Marcio's kubernetes cluster will enable the team to gain more experience about kubernetes and
the possibility to do tests about node/pods configuration for performance and latency between services.

We will postpone for the future the decision to have any pipeline and automatize the deployments.