# PostgreSQL Server Guide on Ubuntu

This guide explains how to **stop**, **start**, and **run multiple `.sql` files** using PostgreSQL on Ubuntu.

---

## 1. Stop PostgreSQL Server

To stop the PostgreSQL server:

```bash
sudo systemctl stop postgresql
```

Check status:

```bash
systemctl status postgresql
```

* `inactive` or `dead` means the server is stopped.

---

## 2. Start / Activate PostgreSQL Server

To start the PostgreSQL server:

```bash
sudo systemctl start postgresql
```

Enable automatic start at boot:

```bash
sudo systemctl enable postgresql
```

Check status:

```bash
systemctl status postgresql
```

* `active (exited)` is normal for the main service unit; background processes handle connections.

---

## 3. Run Multiple `.sql` Files

Assuming you have 3 files in a folder (`tables.sql`, `data.sql`, `queries.sql`) and a database named `lab1`.

### Option A: Using terminal with `psql`

```bash
# Switch to PostgreSQL user
sudo -i -u postgres

# Connect to the database
psql lab1

# Run the SQL files in order
\i /path/to/tables.sql
\i /path/to/data.sql
\i /path/to/queries.sql
```

### Option B: One-line execution

```bash
sudo -i -u postgres psql lab1 -f /path/to/tables.sql -f /path/to/data.sql -f /path/to/queries.sql
```

> Note: PostgreSQL will execute the files sequentially in the order provided.

### Option C: Using VS Code with SQL extension

1. Open the project folder in VS Code:

```bash
code ~/Desktop/csubb/Lab/SEM3/DB/lab1
```

2. Open each `.sql` file in tabs.
3. Connect to `lab1` database using the SQL extension.
4. Run each file in sequence or select multiple queries to run at once.

---

## Notes

* Relations (foreign keys) and table constraints are enforced automatically.
* PostgreSQL must be running whenever you want to execute queries.
* You can edit multiple `.sql` files simultaneously in VS Code.
* Use `exit` to leave the `postgres` user shell when done.
