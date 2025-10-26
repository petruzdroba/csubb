#!/bin/bash

# -----------------------------
# Script: clear_db.sh
# Purpose: Clear all tables from the specified PostgreSQL database
# -----------------------------

DB_NAME="labdb"

# Function to clear all tables in the specified database
clear_db() {
    echo "Starting PostgreSQL server..."
    sudo systemctl start postgresql

    # Ensure the database exists
    sudo -i -u postgres psql -tc "SELECT 1 FROM pg_database WHERE datname = '$DB_NAME'" | grep -q 1
    if [ $? -eq 1 ]; then
        echo "Error: Database '$DB_NAME' does not exist."
        exit 1
    fi

    echo "Clearing all tables in database '$DB_NAME'..."
    
    # Drop all tables in the database
    sudo -i -u postgres psql -d "$DB_NAME" -c "
        DO \$\$ 
        DECLARE 
            r RECORD;
        BEGIN 
            FOR r IN (SELECT tablename FROM pg_tables WHERE schemaname = 'public') 
            LOOP 
                EXECUTE 'DROP TABLE IF EXISTS public.' || r.tablename || ' CASCADE'; 
            END LOOP; 
        END \$\$;
    "

    echo "All tables have been cleared from '$DB_NAME'."
}

# Call the clear_db function
clear_db
