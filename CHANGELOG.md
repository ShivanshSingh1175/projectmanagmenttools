# Changelog

## 2025-11-30 - Java 21 upgrade and runtime fixes
- Upgraded project to target Java 21
- Added ASM 9.5 to support Java 21 class parsing
- Added Jetty JSP and annotation modules for embedded runner
- Added run scripts, Dockerfile, docker-compose, and CI verification script
- Fixed jetty-web.xml DOCTYPE to avoid SAX parsing issues
