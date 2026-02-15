---
name: analyze-codebase
description: Project analysis and context priming. Use when starting a new task or needing a comprehensive project summary.
---

# Prime Skill

This skill analyzes the project structure, reads key documentation, and generates a comprehensive summary to prime the session for development tasks.

## 1. Analyze Structure

Execute the `scripts/scan_project.sh` script to list all tracked files and show the project directory tree.

```bash
# Execute the scan script
.gemini/skills/analyze-codebase/scripts/scan_project.sh
```

Review the output to understand the file organization and identify potential documentation files.

## 2. Read Documentation

Read the following core documentation files if they exist:

- `README.md` - Project overview and setup
- `CONTRIBUTING.md` - Contribution guidelines
- `docs/` folder contents (if relevant)

Use `read_file` to read the content of these files.

## 3. Identify Core Files

Based on the file structure and documentation, identify and read the core files for both backend and frontend components (or main application logic if not split). Look for:

- Entry points (e.g., `main.py`, `index.js`, `App.tsx`, `MainActivity.kt`)
- Core configuration (e.g., `settings.py`, `config.js`, `build.gradle.kts`)
- Key data models or architectural components

## 4. Generate Report

After analyzing the structure and reading documentation, provide a concise summary report with the following sections:

1.  **Project Purpose**: What this application does
2.  **Architecture**: Key patterns (e.g., MVC, MVVM, Clean Architecture, Vertical Slice)
3.  **Core Principles**: Engineering standards (e.g., Type Safety, KISS, YAGNI)
4.  **Tech Stack**: Main dependencies, languages, and tools
5.  **Key Requirements**: Logging, testing, type annotations, specific conventions
6.  **Current State**: What is currently implemented and known issues

Keep the summary brief (5-10 bullet points) and focused on what is needed to contribute effectively.
