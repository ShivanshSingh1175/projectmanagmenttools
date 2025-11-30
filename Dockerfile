FROM eclipse-temurin:21-jdk-focal
WORKDIR /app
ARG WAR_FILE=target/ProjectManagementTool-1.0.0.war
COPY ${WAR_FILE} /app/ROOT.war
EXPOSE 8080
HEALTHCHECK --interval=10s --timeout=5s --retries=5 CMD curl -f http://localhost:8080/pmt-health || exit 1
CMD ["sh", "-c", "java -Dapp.port=8080 -jar /app/ROOT.war"]
# Dockerfile: Tomcat 9 running on Java 21 (Eclipse Temurin)
# Builds a Tomcat install inside the image and deploys the WAR as ROOT.war

ARG TOMCAT_VERSION=9.0.92
FROM eclipse-temurin:21-jdk AS runtime

# Install curl & tar
RUN apt-get update \
  && apt-get install -y --no-install-recommends curl tar ca-certificates \
  && rm -rf /var/lib/apt/lists/*

# Download and extract Tomcat
RUN curl -fSL "https://archive.apache.org/dist/tomcat/tomcat-9/v${TOMCAT_VERSION}/bin/apache-tomcat-${TOMCAT_VERSION}.tar.gz" -o /tmp/tomcat.tar.gz \
  && mkdir -p /usr/local/tomcat \
  && tar -xzf /tmp/tomcat.tar.gz -C /usr/local/tomcat --strip-components=1 \
  && rm /tmp/tomcat.tar.gz

# Copy the built WAR into Tomcat webapps as ROOT.war
COPY target/ProjectManagementTool-1.0.0.war /usr/local/tomcat/webapps/ROOT.war

ENV CATALINA_HOME=/usr/local/tomcat
WORKDIR /usr/local/tomcat
EXPOSE 8080

# Simple HTTP healthcheck for the container; probes the app endpoint.
# Tomcat and curl are available in this image, so this will return healthy when
# http://localhost:8080/pmt-health returns 200.
HEALTHCHECK --interval=15s --timeout=5s --start-period=10s --retries=3 \
  CMD curl -fsS http://localhost:8080/pmt-health || exit 1

# Run Tomcat in foreground
CMD ["bin/catalina.sh","run"]
