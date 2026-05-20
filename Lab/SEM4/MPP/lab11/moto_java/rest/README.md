# Run

```bash
gradle run --args="8080"
```

---

# CURL examples

## Get all
```bash
curl -v -X GET http://localhost:8080/races
```

## Get by id
```bash
curl -v -X GET http://localhost:8080/races/1
```

## Create
```bash
curl -v -X POST http://localhost:8080/races \
  -H "Content-Type: text/plain" \
  -d "1200"
```

## Update
```bash
curl -v -X PUT http://localhost:8080/races/1 \
  -H "Content-Type: text/plain" \
  -d "1400"
```

## Delete
```bash
curl -v -X DELETE http://localhost:8080/races/1
```