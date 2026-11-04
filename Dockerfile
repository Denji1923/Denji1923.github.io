# ---- build the .war ----
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -B dependency:go-offline
COPY src ./src
RUN mvn -q -B -DskipTests package

# ---- run it on Tomcat 9 (javax.servlet) ----
FROM tomcat:9.0-jdk17-temurin
RUN rm -rf /usr/local/tomcat/webapps/*
# ROOT.war => the site is served at "/" (https://your-site.onrender.com/), not "/CampusLostFound/"
COPY --from=build /app/target/CampusLostFound.war /usr/local/tomcat/webapps/ROOT.war
ENV LOSTFOUND_UPLOAD_DIR=/data/uploads
RUN mkdir -p /data/uploads
EXPOSE 8080
CMD ["catalina.sh", "run"]
