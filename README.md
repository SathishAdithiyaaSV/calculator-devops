# Calculator DevOps

A Spring Boot calculator REST API with a Jenkins CI/CD pipeline, Docker image, and Docker Compose deployment behind Nginx.

## Requirements

Java 11, Maven 3.8+, Docker, and Docker Compose.

## Build and test

```bash
mvn clean package
mvn test
```

JUnit XML results are written to `target/surefire-reports/` for Jenkins to publish.

## Run locally

```bash
mvn spring-boot:run
```

The API listens on port 8080 when started directly. The Compose stack uses port 8081 because Jenkins commonly occupies port 8080:

```bash
docker compose up --build -d
docker compose ps
curl "http://localhost:8081/api/v1/add?a=2&b=3"
curl http://localhost:8081/actuator/health
```

Stop it with `docker compose down`.

## API

| Operation | Endpoint |
| --- | --- |
| Add | `GET /api/v1/add?a=&b=` |
| Subtract | `GET /api/v1/subtract?a=&b=` |
| Multiply | `GET /api/v1/multiply?a=&b=` |
| Divide | `GET /api/v1/divide?a=&b=` |
| Health | `GET /actuator/health` |

Example response:

```json
{"operation":"add","a":2,"b":3,"result":5}
```

Invalid or missing values, including division by zero, return HTTP 400 and structured JSON error details.

## Jenkins setup

Create a Pipeline job that reads `Jenkinsfile` from this GitHub repository. Enable the GitHub webhook trigger (the pipeline also declares `githubPush()`). The Jenkins agent needs Maven, Docker, Docker Compose, and permission to access the Docker daemon.

Configure these Jenkins credentials before the first build:

| Credential ID | Type | Value |
| --- | --- | --- |
| `dockerhub-creds` | Username with password | Docker Hub username and access token |
| `github-creds` | Username with password | GitHub username and personal access token, for Jenkins repository checkout |

Install and configure the Email Extension plugin and global SMTP settings using the existing `gmail-smtp` credential for `emailext`. Each run checks out the repository, builds, tests, publishes JUnit reports, builds and pushes versioned Docker images, deploys the selected image through Compose, checks `/actuator/health`, and emails the result.

The Docker Hub repository is named `calculator-api`; create it in Docker Hub before the first push.

See [REPORT.md](REPORT.md) for the pipeline architecture and demo checklist.

## Image

```bash
docker build -t calculator-api:local .
docker run --rm -p 8081:8080 calculator-api:local
```

The image is multi-stage and its runtime process uses a non-root user.
