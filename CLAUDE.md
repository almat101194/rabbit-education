# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a minimal Java project created with IntelliJ IDEA. Currently it contains a single entry point (`src/Main.java`) and no build tool (no Maven or Gradle).

## Build & Run

Since there is no build tool configured, compile and run manually via the terminal:

```powershell
# Compile
javac -d out src/Main.java

# Run
java -cp out Main
```

Or use IntelliJ IDEA's built-in Run/Debug actions directly.

## Structure

- `src/` — Java source files; `Main.java` is the entry point
- `rabbit.iml` — IntelliJ module descriptor (inherits JDK from project settings)
- `.idea/` — IDE project metadata

## Notes

- No package declarations are used currently; `Main` lives in the default package.
- If the project grows, consider adding Maven (`pom.xml`) or Gradle (`build.gradle`) to manage dependencies and standardize builds.