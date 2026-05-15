
# Task A: Initial migration

- Created `001-baseline.xml` what the schema looks like in its initial state (*migration 1*)
- Created `db.changelog-master.xml`: central migration def. file -> controls database schema evo. *run these migrations in order*
- `hibernate.hbm2ddl.auto = none`, liquibase becomes the schema controller, Hibernate only reads the schema

## Task E: Adding indexes

**Before adding an index**

```bash
Employees search by dept

-> Index lookup on employees using fk_employees_departments (department_id=5)  (cost=69.5 rows=500) (actual time=8.86..28.7 rows=500 loops=1)

Employee search by salary

-> Filter: (employees.salary = 42250.00)  (cost=499 rows=493) (actual time=1.5..30.3 rows=1 loops=1)
    -> Table scan on employees  (cost=499 rows=4925) (actual time=1.45..25.9 rows=5000 loops=1)
```
-> MySQL auto created an index on the foreign key

**After index**

```bash
Employee search by dept
-> Index lookup on employees using idx_employees_department (department_id=5)  (cost=69.5 rows=500) (actual time=9.82..13.2 rows=500 loops=1)

Employee by salary
-> Index lookup on employees using idx_employees_salary (salary=42250.00)  (cost=0.35 rows=1) (actual time=0.124..0.132 rows=1 loops=1)
```


# Optimistic Lock

**Without optimistic lock** -> Lost update
*version here represents the state of the entity, in our head, and not `@Version`*
- A reads Employee 1
- B reads Employee 1
- A updates salary (*version is 1 still*)-> DB writes (*version still 1*) 
- B updates salary -> overwrite A update
- **Result**: A update is lost

**With optimistic locking**
*Only update the entity if your version matches DB version, on each update bump the version in the DB*

```sql
UPDATE employees
SET salary = ?, version = version + 1
WHERE id = ? AND version = ?
```

**What changes ?**
- A reads Employee 1, version=1
- B reads Employee 1, version=1
- A updates salary -> DB writes (*version version updates to version=2*) 
- B updates salary -> version dosent match -> no update
- **Result**: A update is not lost

*start latch: CountDownLatch -> so updates dont start until both users have version 1 of employee 1*

# Soft vs Hard deletion

## Soft 

We do no remove the data from the DB -> simply mark it as deleted
- `is_deleted = true`
- `deleted_at = timestamp`
- `deleted_by = user`

In entity file change->Hibernate automatically filters out any entity marked like this
```java
@Where(clause = "is_deleted = false")
```

**Effect and purposes**
- entity is still treated as non existent
- users cannot see it or access it
- prevents accidental data loss
- maintain history
- allow recovery

**Restore** -> to restore a soft deleted entity, we must bypass the ORM by using a native query -> entity becomes *activated*

## Hard

Actuall remove the data from the DB

```sql
DELETE FROM employees WHERE id = ?
```

**Effects and purposes**
- row is permanently gone
- cannot be restored

## Why use one over the other

**Soft** allows use to achieve better data safety, we have more control over the data, audit and history preservation, referential integrity safety (*other table entities might still refer to the soft deleted entity*)

**Hard** required when data actually has to disappear (*cleanup, test data removal*), better performance and storage eff. (*smaller tables*) and it has simpler query logic