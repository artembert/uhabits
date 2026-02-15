#!/bin/bash

# List tracked files
echo "--- TRACKED FILES ---"
git ls-files

echo ""
echo "--- PROJECT STRUCTURE ---"
if command -v tree &> /dev/null; then
    tree -I '.venv|__pycache__|*.pyc|.pytest_cache|.mypy_cache|.ruff_cache|.git|.idea|.gradle|build' -L 3
else
    ls -R
fi
