#!/bin/bash

# -----------------------------
# Script: deactivate_labdb.sh
# Purpose: Stop PostgreSQL server
# -----------------------------

echo "Stopping PostgreSQL server..."
sudo systemctl stop postgresql

echo "PostgreSQL is now stopped!"
