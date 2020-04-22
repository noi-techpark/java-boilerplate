Replace all `ToDo` notes with the appropriate names, descriptions and commands.

# ToDo: Project Name

ToDo: Description of the project.

## Table of contents

- [Gettings started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Source code](#source-code)
  - [Execute without Docker](#execute-without-docker)
  - [Execute with Docker](#execute-with-docker)
  - [Git Hooks](#git-hooks)
- [Information](#information)

## Getting started

These instructions will get you a copy of the project up and running
on your local machine for development and testing purposes.

### Prerequisites

To build the project, the following prerequisites must be met:

- ToDo: Check the prerequisites
- Java JDK 1.8 or higher (e.g. [OpenJDK](https://openjdk.java.net/))
- [Maven](https://maven.apache.org/) 3.x
- [PostgreSQL](https://www.postgresql.org/) 11

If you want to run the application using [Docker](https://www.docker.com/), the environment is already set up with all dependencies for you. You only have to install [Docker](https://www.docker.com/) and [Docker Compose](https://docs.docker.com/compose/) and follow the instruction in the [dedicated section](#execute-with-docker).

### Source code

Get a copy of the repository:

```bash
ToDo: git clone https://github.com/noi-techpark/project-name.git
```

Change directory:

```bash
ToDo: cd project-name/
```

### Execute without Docker

Copy the file `src/main/resources/application.properties` to `src/main/resources/application-local.properties` and adjust the variables that get their values from environment variables. You can take a look at the `.env.example` for some help.

Build the project:

```bash
mvn -Dspring.profiles.active=local clean install
```

Run the project:

```bash
mvn -Dspring.profiles.active=local spring-boot:run
```

The service will be available at localhost and your specified server port.

To execute the test you can run the following command:

```bash
mvn clean test
```

### Execute with Docker

Copy the file `.env.example` to `.env` and adjust the configuration parameters.

Then you can start the application using the following command:

```bash
docker-compose up
```

The service will be available at localhost and your specified server port.

To execute the test you can run the following command:

```bash
docker-compose run --rm app mvn clean test
```

### Git Hooks

To have a common code structure and make sure that Java best practices are folled we use the [PMD plugin](http://maven.apache.org/plugins/maven-pmd-plugin/index.html).

To make sure that the code is in the right format we define some pre-commit hooks, that run the code validation on each commit automatically. To support this on your local machine you have to execute following command.

```bash
git config --global core.hooksPath .githooks
```

## Information

### Support

ToDo: For support, please contact [info@opendatahub.bz.it](mailto:info@opendatahub.bz.it).

### Contributing

If you'd like to contribute, please follow the following instructions:

- Fork the repository.

- Checkout a topic branch from the `development` branch.

- Make sure the tests are passing.

- Create a pull request against the `development` branch.

A more detailed description can be found here: [https://github.com/noi-techpark/documentation/blob/master/contributors.md](https://github.com/noi-techpark/documentation/blob/master/contributors.md).

### Documentation

More documentation can be found at [https://opendatahub.readthedocs.io/en/latest/index.html](https://opendatahub.readthedocs.io/en/latest/index.html).

### Boilerplate

The project uses this boilerplate: [https://github.com/noi-techpark/java-boilerplate](https://github.com/noi-techpark/java-boilerplate).

### License

The code in this project is licensed under the GNU AFFERO GENERAL PUBLIC LICENSE Version 3 license. See the [LICENSE.md](LICENSE.md) file for more information.
