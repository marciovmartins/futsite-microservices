# Support to arm64 architecture while spring-boot:image-build (paketo-buildpacks) do not support it
# https://github.com/paketo-buildpacks/stacks/issues/51
FROM arm64v8/openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]