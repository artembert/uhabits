---
name: commit
description: Generate and execute conventional commits based on staged changes. Use when creating a git commit.
---

# Commit Skill

This skill assists with creating git commits following the Conventional Commits specification.

## Usage

Use this skill when you want to commit changes to the git repository.

### Workflow

1.  **Check Staged Changes**:
    - Run `git status` to see what is staged.
    - Run `git diff --staged` to see the actual changes.
    - If nothing is staged, ask the user what to stage (e.g., "Nothing staged. Should I stage all changes with `git add .`?").

2.  **Generate Commit Message**:
    - Based on the `git diff --staged` output, generate a commit message.
    - Follow the **Conventional Commits** format:
      ```
      <type>(<scope>): <description>

      [optional body]

      [optional footer(s)]
      ```
    - **Types**:
        - `feat`: A new feature
        - `fix`: A bug fix
        - `docs`: Documentation only changes
        - `style`: Changes that do not affect the meaning of the code (white-space, formatting, missing semi-colons, etc)
        - `refactor`: A code change that neither fixes a bug nor adds a feature
        - `perf`: A code change that improves performance
        - `test`: Adding missing tests or correcting existing tests
        - `build`: Changes that affect the build system or external dependencies (example scopes: gulp, broccoli, npm)
        - `ci`: Changes to our CI configuration files and scripts (example scopes: Travis, Circle, BrowserStack, SauceLabs)
        - `chore`: Other changes that don't modify src or test files
        - `revert`: Reverts a previous commit

3.  **Confirm & Commit**:
    - Present the generated message to the user.
    - Ask for confirmation or edits.
    - If not confirmed, stop execution.
    - If confirmed, run `git commit -m "<message>"`.
