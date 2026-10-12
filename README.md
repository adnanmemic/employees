# Employees

Spring Boot application requiring Java 25 and Maven 3.9.16 (as specified in
`.mvn/wrapper/maven-wrapper.properties`). The wrapper launcher is not checked
in, so use an installed Maven executable.

## Build and test

```sh
mvn verify
```

Integration tests start a server on a random port and check endpoint responses,
authentication, health, and restricted Actuator exposure.

## Run locally

```sh
java -jar target/employees-0.0.1-SNAPSHOT.jar
```

The application listens on port 8585 with context path `/app`. Spring Security
uses username `user` and prints a generated development password at startup.
Supply `SPRING_SECURITY_USER_NAME` and `SPRING_SECURITY_USER_PASSWORD` through
runtime configuration when stable credentials are needed; do not commit
passwords. The generated password is intended only for development.

Authenticated GET routes are `/app/employee`, `/app/greet`, and `/app/security`.
The security route returns the configured display name, defaulting to
`employees`; override it with `SECURITY_NAME` or `--security.name=your-name`.
Health and application information are available at `/app/actuator/health`
and `/app/actuator/info`. Other Actuator endpoints are not exposed over HTTP.
