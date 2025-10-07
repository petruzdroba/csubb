#!/bin/bash

# -----------------------------
# Script: activate_labdb.sh
# Purpose: Start PostgreSQL and ensure labdb exists
# -----------------------------

DB_NAME="labdb"

echo "Starting PostgreSQL server..."
sudo systemctl start postgresql

sudo -i -u postgres bash << EOF
# Create the database if it doesn't exist
psql -tc "SELECT 1 FROM pg_database WHERE datname = '$DB_NAME'" | grep -q 1 || createdb $DB_NAME
EOF

echo "PostgreSQL is active, and '$DB_NAME' is ready!"

