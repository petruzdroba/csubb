Task: [[PDP_Lab1_cerinta]]
# Bank System

## Sync rules and invariants

### Data structure

The system maintains:
- balances
- account_mutexes: one mutex per account

Each account has its own, allowing operations on diff account to work concurrently
See [[PDP_Lab1_cerinta#Which one is ours, in the bank ?]]

### Sync rules

- Account balance access: each read or write musst be protected by mutex, no other thread may access the balance concurrently while another thread modifies the balance.
- Transfer operations: a *transfer* must hold the mutex for both `to` and `from` accounts, before checking or modifying either balance.
- Mutex aquiring: ascending account-idx order to prevent deadlocks
- Consistency checks: acquire all mutexes for account before summing balance, ensure total is calculated not during a transfer. See [[PDP_lab1#Consistency check]]]
- Same with balance printing

### Invariants

1. **Total balance preservation:** The sum of all account balances remains equal to the initial total balance.
    
2. **Non-negative balances:** No account balance becomes negative.
    
3. **Atomic transfers:** A transfer either completes both balance modifications or makes no changes.
    
4. **Deadlock prevention:** All operations acquire multiple account mutexes in a consistent ascending order.
    
5. **Independent operations:** Transfers involving disjoint pairs of accounts can execute concurrently because they use different mutexes.

## Consistency check

```cpp
std::vector<std::unique_lock<std::mutex>> locks;
locks.reserve(order.size());
for(int i: order) {
	locks.emplace_back(account_mutexes[i]);
}
```

We create a dynamic array of objects that manage the mutex lock. Then we allocate in mem. `emplace_back` constructs a `std::unique_lock` belonging to account with idx i, *making the unique_lock auto locks the mutex*. When locks goes out of scope, the locks vector is marked as free in mem so the mutexes are unlocked.
Also called the [RAII](https://en.cppreference.com/cpp/language/raii) 

If another thread holds the mutex for any of the accounts, the loop will wait until that mutex becomes available. Acquire is asc order -> prevent any dead locks

## Performance
### Hardware and Software Environment

|Parameter|Specification|
|---|---|
|Operating system|CachyOS Linux|
|Kernel|7.2.3-1-cachyos|
|Processor|Intel Core i7-7600U @ 2.80 GHz|
|Physical cores|2|
|Logical processors|4|
|Installed RAM|7.6 GiB|
|Compiler|GCC 16.2.1|
|C++ standard|C++17|
|Optimization flags|`-O2`|
|Thread support|`-pthread`|

The benchmarks were executed on a dual-core processor supporting four logical processors. This hardware configuration may limit the benefits of increasing the number of worker threads beyond four, particularly when synchronization and scheduling overhead become significant.

### Mutex per account
#### Accounts: 4

| Threads | Avg time (s) | Attempts/sec |
|---------|--------------|--------------|
| 1 | 0.1219 | 16406401 |
| 2 | 0.2805 | 7131298 |
| 4 | 0.5191 | 3853006 |
| 8 | 0.5341 | 3744514 |
| 16 | 0.6701 | 2984680 |
| 32 | 0.6972 | 2868648 |

#### Accounts: 100

| Threads | Avg time (s) | Attempts/sec |
|---------|--------------|--------------|
| 1 | 0.1307 | 15297505 |
| 2 | 0.1785 | 11201450 |
| 4 | 0.1704 | 11737064 |
| 8 | 0.1985 | 10073681 |
| 16 | 0.2247 | 8901208 |
| 32 | 0.2551 | 7840083 |

#### Accounts: 1000

| Threads | Avg time (s) | Attempts/sec |
|---------|--------------|--------------|
| 1 | 0.1340 | 14924533 |
| 2 | 0.1323 | 15121926 |
| 4 | 0.1013 | 19733854 |
| 8 | 0.1015 | 19695650 |
| 16 | 0.1130 | 17696431 |
| 32 | 0.1312 | 15244792 |

#### Accounts: 10000

| Threads | Avg time (s) | Attempts/sec |
| ------- | ------------ | ------------ |
| 1       | 0.1378       | 14511714     |
| 2       | 0.1217       | 16434723     |
| 4       | 0.0865       | 23125270     |
| 8       | 0.1011       | 19775580     |
| 16      | 0.1142       | 17514441     |
| 32      | 0.1431       | 13974651     |

#### Results summary

The following table shows the fastest thread count for each account count.

|Accounts|Best threads|Avg time (s)|Attempts/sec|
|---|---|---|---|
|4|1|0.1219|16.41M|
|100|1|0.1307|15.30M|
|1,000|4|0.1013|19.73M|
|10,000|4|0.0865|23.13M|
##
## Interpretation

- 4 accounts: Performance decreases sharply as threads increase. Threads compete for the same few account mutexes.
    
- 100 accounts: Contention is reduced, but additional threads still do not improve performance over one thread.
    
- 1,000 accounts: Four and eight threads perform similarly, with four threads marginally faster.
    
- 10,000 accounts: Four threads achieve the highest throughput. More threads introduce additional synchronization and scheduling overhead.

### Striped locking, seeing changes in granularity
See [[PDP_Lab1_cerinta#What is striped locking]]

| Accounts | Strategy | Best threads | Avg time (s) | Attempts/sec |
|----------|----------|--------------|--------------|--------------|
| 4 | Per-account | 1 | 0.1219 | 16.41M |
| 4 | Striped (64) | 1 | 0.1379 | 14.51M |
| 100 | Per-account | 1 | 0.1307 | 15.30M |
| 100 | Striped (64) | 1 | 0.1566 | 12.77M |
| 1,000 | Per-account | 4 | 0.1013 | 19.73M |
| 1,000 | Striped (64) | 1 | 0.1553 | 12.88M |
| 10,000 | Per-account | 4 | 0.0865 | 23.13M |
| 10,000 | Striped (64) | 1 | 0.1969 | 10.16M |
Striped locking increases contention for each mutex, we can see this in the 10k accounts, where all ofthem share 64 mutexes