# eggratercer-cuttedspoon

A serverless URL shortener built in Java with AWS integration.

## Documentation

To help you get started and understand the project better, the documentation is organized into several sections. Below is a table of contents linking to detailed guides for each topic.

- [Setup Guide](docs/setup.md): Instructions on configuring your development environment and setting up commit standards.
- [SDK/Project Management](docs/utils.md): Instructions on configuring your SDK and creating the project.
- [Troubleshooting](docs/troubleshooting.md): Some handy content in case you run into any errors.
- [Deploying](docs/deploy.md): Learn how to generate the `jar` files and upload them to AWS.

## Features

- Serverless architecture
- AWS integration (Lambdas, S3, Api Gateway)
- URL shortening capabilities

## Usage

To interact with the endpoint of this project, you can:

- Use a `[POST]` request on the endpoint with the correct payload to receive your `urlCode`.
- Use a `[GET]` request on the endpoint, providing the `urlCode` obtained in the previous step.

## Getting Started

To start using the application, follow the steps in the [Setup Guide](docs/setup.md).
