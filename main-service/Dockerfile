FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar app.jar
RUN apk add --no-cache tzdata
ENV SPRING_DATASOURCE_URL=SPRING_DATASOURCE_URL \
                            POSTGRES_USER=POSTGRES_USER \
                            POSTGRES_PASSWORD=POSTGRES_PASSWORD \
                            STAT_URL=STAT_URL \
                            TZ=Europe/Moscow
ENTRYPOINT java -DSPRING_DATASOURCE_URL=${SPRING_DATASOURCE_URL} \
                -DPOSTGRES_USER=${POSTGRES_USER} \
                -DPOSTGRES_PASSWORD=${POSTGRES_PASSWORD} \
                -DSTAT_URL=${STAT_URL} \
                -jar app.jar