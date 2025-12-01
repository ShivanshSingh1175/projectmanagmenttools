# Deployment and Compatibility Guide

This file describes quick-fix and long-term options to run the app locally or on a server.

## Quick fix (recommended to get running now)

- Use JDK 17 to avoid Jetty/ASM Java 21 parsing issues.
- Build with the Maven profile `jdk17-compat` to compile with `--release 17`.

Commands (Ubuntu/Debian):
```bash
# install Temurin JDK 17 (Debian/Ubuntu example)
sudo apt update
sudo apt install -y wget apt-transport-https
wget -O - https://adoptium.jfrog.io/adoptium/api/gpg/key/public | sudo apt-key add -
sudo sh -c 'echo "deb https://adoptium.jfrog.io/adoptium/deb $(lsb_release -sc) main" > /etc/apt/sources.list.d/adoptium.list'
sudo apt update
sudo apt install -y temurin-17-jdk

# Build using JDK17 profile
cd ProjectManagementTool
mvn -P jdk17-compat -DskipTests clean package

# Run (local)
export APP_PORT=8080
java -Dapp.port=$APP_PORT -cp "target/classes:target/dependency/*" com.pmt.runner.EmbeddedJettyRunner
```

Notes:
- Using JDK 17 avoids the Java 21 bytecode parsing errors. This is the fastest path to a working server.
- If you use Docker, the provided Dockerfile defaults to a JDK 17 base image.

## Long-term: upgrade to Java 21

- Upgrade Jetty and ASM to versions that support Java 21 (we added `org.ow2.asm:asm:9.5` and `jetty-annotations` as part of the Java21 changes). To fully run on Java 21 you must ensure your Jetty version+modules are Java21-compatible (Jetty 11+ with latest patches and ASM 9.5+).
- Update CI to use JDK 21 and test thoroughly.

## Server deployment example (systemd + nginx reverse proxy)

1. Build artifact on server or CI and copy WAR to `/opt/pmt/`.
2. Create systemd unit: `/etc/systemd/system/pmt.service` (see `deploy/pmt.service` in repo).
3. Configure Nginx as reverse proxy (see `deploy/nginx_pmt.conf`).
4. Start and enable:
```bash
sudo systemctl daemon-reload
sudo systemctl enable --now pmt
sudo systemctl status pmt
```

## Rollback plan

- Quick rollback: revert the `jdk17-compat` profile usage or rebuild using previous Docker image tag.
- If upgrading Jetty fails, revert dependency upgrades and use JDK17 profile until upgrade is validated.
