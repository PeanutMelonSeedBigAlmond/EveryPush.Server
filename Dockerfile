FROM gradle as build
WORKDIR /src
COPY . /src
RUN chmod +x ./gradlew && \
    ./gradlew bootJar

FROM openjdk:21 as production
ENV TZ=Asia/Shanghai
WORKDIR /app
COPY --from=build /src/build/libs/*.jar /app/app.jar
VOLUME ["/app/data"]
ENTRYPOINT ["java"]
CMD ["-jar","app.jar"]
