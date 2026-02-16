# Microsoft SQL Server 2022 on Linux (Docker)

This is the **correct, minimal** way to run MSSQL on Linux using Docker, with a **strong password** and **auto-start on boot**.

---

## Prerequisites

- Linux machine  
- Azure Data Studio
- Docker installed and running  
- `sudo` access  

Verify Docker:

```bash
docker --version
docker ps
```

---

## Pull the SQL Server Image

```bash
sudo docker pull mcr.microsoft.com/mssql/server:2022-latest
```

---

## Run SQL Server Container (WITH PASSWORD + AUTOSTART)

**Password rules (MANDATORY):**

- At least **8 characters**
- Uppercase + lowercase
- Number
- Symbol

### Run command

```bash
sudo docker run -d \
  --name sqlserver \
  --restart unless-stopped \
  -e "ACCEPT_EULA=Y" \
  -e "MSSQL_SA_PASSWORD=Str0ng!Pass123" \
  -p 1433:1433 \
  mcr.microsoft.com/mssql/server:2022-latest
```

What this does:

- `--restart unless-stopped` → auto-start on boot  
- `-p 1433:1433` → exposes SQL Server  
- `MSSQL_SA_PASSWORD` → sets SA password  

---

## Verify Container Is Running

```bash
sudo docker ps
```

You should see:

- STATUS: `Up ...`
- PORTS: `0.0.0.0:1433->1433/tcp`

---

## Connect to SQL Server (From Host)

### Using Azure Data Studio

This is the recommended way.

---

## Install Azure Data Studio

Download and install the `.deb` package:

```bash
wget https://go.microsoft.com/fwlink/?linkid=2284485 -O azuredatastudio.deb
sudo apt install ./azuredatastudio.deb
```

Launch it:

```bash
azuredatastudio
```

---

## Connect to SQL Server

In **Azure Data Studio**:

1. Click **New Connection**
2. Set:
   - **Connection type**: Microsoft SQL Server  
   - **Server**: `localhost:1433`  
   - **Authentication type**: SQL Login  
   - **User name**: `sa`  
   - **Password**: `Str0ng!Pass123`  
3. Click **Connect**

---

## Test the Connection

Run:

```sql
SELECT @@VERSION;
```

If SQL Server 2022 is returned, the setup is correct.

---

## Enable SQL Server to Start Automatically (Double-Check)

Docker already handles this, but verify:

```bash
sudo docker inspect -f '{{.HostConfig.RestartPolicy.Name}}' sqlserver
```

Expected output:

```text
unless-stopped
```

---

## Stop / Start Manually (If Needed)

```bash
sudo docker stop sqlserver
sudo docker start sqlserver
```

---

## Common Problems (And Fixes)

### Password rejected

The password is weak.

Fix:

```bash
sudo docker rm -f sqlserver
```

Run the container again with a stronger password.

---

### Port 1433 already in use

Check:

```bash
sudo ss -tulpn | grep 1433
```

Either stop the other service or use another port:

```bash
-p 1434:1433
```

---

## Summary

- Docker is the only sane way to run MSSQL on Linux  
- `--restart unless-stopped` guarantees auto-start  
- Password rules are enforced by SQL Server  
- Azure Data Studio is the recommended client
