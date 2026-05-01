# N+1 Query Problem

*Example*: Entity A owns multiple of  Entity B, to find all entities B from A, we need to
```sql
SELECT * FROM A;
SELECT * FROM B WHERE B.A_id = A.id;
```
Problem -> When we query like that we query N+1 (All select + N Selects from A)

## Solution

**Join query**: 
```sql
SELECT * FROM A LEFT JOIN B ON B.A_id = A.id;
```

Issue : Entity A si shown for whatever many B entities it owns

**Eager loading**
```sql
#Under the hood

SELECT * FROM A;
SELECT * FROM B WHERE B.A_id IN A.id;
```
Instead of the extra query we do for the lazy loading we load it immediately while doing the original query

# Index Benchmarks

Without any indexes any select does a complete table search ( also called a *Sequential search* in PostgreSQL )

### What is an index
Assigns each pair of data (*say you have a index on id+name, so for those 2*) a unique hash, think of it as a quick access hash table

## What indexes are we using

### Unique index - Best case
On something that is unique (like an email or an id)

```sql
WHERE email = :email;
```
Each index is unique in this case since its a 1to1 mapping -> O(logn)

### Non-Unique index
On common things like department_id (multiple employees can be apart of the same dept)
```sql
WHERE dept_id = :deptId;
```
*Whats the benefit*: instead of searching thru all the values, we search thru only a part of them (worst case all of them if everybody is in the same dept)

### Range query
*Can an index be used for ranges ?*: Yes but, the DB still does a full table scan -> O(n)
```sql
WHERE salary BETWEEN 50000 AND 80000;
```
The index is set on the first value >= 50000 until the last value <=80000, so it navigates the B-tree based on where the values are, so like for 50000 and 80000, it takes the sub-tree under that index part -> O(logn + k), n-starting point, k-> no of matching rows

### Multi-column query
When we compose an index of multiple values from a entity -> `(dept_id, salary)` as an index
```sql
WHERE department_id = ? AND salary > 60000;
```
Column order in the index matters, so for example it takes all the ones that are from `deptId` and then searches thru their salary O(logn + k) 

| Interogare       | Fara index (ms) | Cu index (ms) | Imbunatatire (ms) |
| ---------------- | --------------- | ------------- | ----------------- |
| Email            | 549             | 171           | 378               |
| Departament      | 861             | 475           | 386               |
| Salariu Interval | 1086            | 987           | 99                |
| Multi-Column     | 665             | 344           | 321               |

# Pagination

Returning data from the database in series of pages
*What we achieve*: Its like Lazy Loading, we are loading a page only when we want to read it instead of loading all data head on

## Offset Pagination

`LIMIT size OFFSET page * size`
Time complexity grows with page depth -> *Page 1:fast, Page 1000:slow*

**Complexity**: O(n+k), where n is offset and k is page size
### When is it suitable ?

- When we have a smaller dataset (< ~10k)
- Users access mostly the first page
- Direct page mapping (*user goes to page 89*)
- Random page jumps

## Keyset Pagination

`WHERE id > lastSeen ORDER BY id LIMIT size`
Time complexity is constant per page

**Complexity**: O(k) where k is the page size 

### When is it suitable ?
- Large datasets
- Infinite scroll/Feed system
- Live data is inserted

# Caching

Saving desirable entities in memory for fast access -> this reduces load and improves response time

**Example**: If an article is printed very often in a library, then they will download it and print the download instead of fetching from the archive each time

## Cache behavior

So a chache ca **HIT** (*the object requested is already in heap*) or **MISS** (*we need to query the DB for that info*)

**Example**: on db run the cache is empty, and we query Object 1 -> *MISS* (logically no object has been loaded yet and pre-loading something would be the same as querying ), but now that we have it in memory we save it for later use, second query Object 1 -> *HIT*

## Cache invalidation

So the cache saves the current object at a point in time (*object in the memory dosent reflect what is in the db*). So how do we fix this ? -> on update or delete we "invalidate" the cache, we remove the old version from memory and put the new one (or leave empty in case of delete)

This way we maintain data accuracy and fastness

## Time To Live Expiration

Each entry in the cache expires after a certain amount of time (*that gets reset if the object is requested from the  cache* )


# Optimising massive updates

| Type       | Time        | Advantage                               |
| ---------- | ----------- | --------------------------------------- |
| Individual | 1410 ms<br> | Total autonomy over each entity         |
| Mass       | 346 ms<br>  | Entities are flushed to the DB togheter |
| Batch      | 2382ms<br>  | 1 single DB query                       |

mvn clean javafx:run