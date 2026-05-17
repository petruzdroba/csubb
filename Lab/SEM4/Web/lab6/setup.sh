#!/bin/bash
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "National Park Portal - Setup"

echo "[1/2] Creating directories..."
mkdir -p "$SCRIPT_DIR/uploads"
mkdir -p "$SCRIPT_DIR/data"
chmod 777 "$SCRIPT_DIR/uploads"
chmod 777 "$SCRIPT_DIR/data"
echo "      uploads/ and data/ created."

echo "[2/2] Importing database schema..."
mysql -h mysql -u student -pstudent park_portal < "$SCRIPT_DIR/schema.sql"
echo "      Schema imported."

echo ""
echo "Done."
