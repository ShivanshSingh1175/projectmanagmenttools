# Project Management Tool

This repository contains the Project Management Tool — a Java Servlet + JSP webapp. The project was upgraded to Java 21 and includes an embedded Jetty runner for local development.

Quick start (Windows PowerShell):
```powershell
# build and run using the provided script
.\run.ps1
```

Quick start (Unix/macOS):
```bash
./run.sh
```

Docker (build and run):
```bash
docker build -t pmt:latest .
docker run -p 8080:8080 pmt:latest
# or with docker-compose
docker-compose up --build
```

Health endpoint: `http://localhost:8080/pmt-health`

See `ci/verify-local.sh` for an automated verification script.
