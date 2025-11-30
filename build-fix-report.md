# Build Fix Report

Summary of fixes applied (2025-11-30)

- Upgraded project settings to target Java 21 (`pom.xml` `maven-compiler-plugin` release=21).
- Added `org.ow2.asm:asm:9.5` to support Java 21 class file parsing (fixes "Unsupported class file major version 65").
- Added Jetty JSP and `jetty-annotations` dependencies to support JSP rendering in the embedded runner and to resolve missing Jetty classes.
- Added `maven-dependency-plugin` execution to copy runtime jars to `target/dependency` for explicit classpath runs.
- Normalized `jetty-web.xml` DOCTYPE to avoid SAX parse errors.
- Added `run.ps1`, `run.sh`, and `ci/verify-local.sh` for robust local builds and tests (explicit classpath usage to avoid wildcard classpath issues on Windows).
- Added `Dockerfile` and `docker-compose.yml` for containerized run (notes: Docker Desktop must be running; if Docker daemon fails, use the run scripts).
- Added documentation files: `README.md`, `CHANGELOG.md`, `OWNER.md`, `LICENSE`, and this report.

How to reproduce locally (copy/paste)

Windows (PowerShell):
```powershell
Set-Location -Path "C:\Users\Shivansh Singh\OneDrive\Desktop\Online project management tool\ProjectManagementTool"
.\run.ps1
```

Unix/macOS:
```bash
cd "$(pwd)/ProjectManagementTool"
./run.sh
```

CI verify script (Linux/macOS):
```bash
./ci/verify-local.sh
```

Docker (build + run):
```bash
docker build -t pmt:latest .
docker run -p 8080:8080 pmt:latest
```

Remaining notes and recommendations

- Port conflicts: the runner binds to `app.port` (default 8080). Use `APP_PORT` env var to override.
- Docker desktop: if Docker fails to start, check Docker daemon logs and restart the service.
- JSPs: the embedded Jetty runner must initialize JSP servlet; if JSPs continue to 404, start the app via Tomcat (`docker-compose` uses a tomcat-based image in README recommendation) or deploy WAR to local Tomcat to verify JSP runtime.
- Unsafe warnings and MySQL relocation notices: these are non-fatal and documented in the README.
