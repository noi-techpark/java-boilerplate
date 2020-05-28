FROM openjdk:11 as base

RUN mkdir -p /code

# Dev
FROM base as dev

ARG JENKINS_GROUP_ID=1000
ARG JENKINS_USER_ID=1000

RUN addgroup --gid $JENKINS_GROUP_ID jenkins && \
   adduser --uid $JENKINS_USER_ID --groups jenkins jenkins

COPY infrastructure/docker/entrypoint.sh /entrypoint.sh
ENTRYPOINT [ "/entrypoint.sh" ]

# Build
FROM base as build

WORKDIR /code

COPY ./ /code
RUN ./mvnw -B -DskipTests install

CMD [ "./mvnw", "-B", "spring-boot:run" ]
