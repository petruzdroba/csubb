[[PDP_lab3_cerinta]]

# Matrix multiplication

For matrices $A_{m \times n}$ and $B_{n \times p}$, each result element is:

$$C_{ij}=\sum_{k=0}^{n-1} A_{ik}B_{kj}$$
## Matrix multiplier class

This class computes one element of the result matrix

## RowMajorBlockStrategy.java

Traverse row by row to make matrix linear, and then split the elements into chunks evenly given to the threads 

```java
for(int i = start; i < end; i++) { 
	int row = i / cols; 
	int col = i % cols;
}
```

```markdown
Matrix (4 columns):
[ 0  1  2  3 ]   ← row 0
[ 4  5  6  7 ]   ← row 1
[ 8  9 10 11 ]   ← row 2

Linear indexing:
i = 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11
```

`i = 7` → `7 / 4 = 1` → row 1
`i = 7` → `7 % 4 = 3` → column 3

*Note: we divide by `cols` because it defines the row width*

Divide the matrix into chunks and assign each thread an equal chunk (aprox)
9x9 matrix (81 cells) to 4 threads => 20 20 20 21 cells for each 

**No synchronization**: Unlike dynamic scheduling (work queues, locks), each thread computes its range once and never communicates.

Matrix: 1000x1000, Threads: 4, Time: 30714.353 ms
Matrix: 9x9, Threads: 4, Time: 137.142 ms
Matrix: 500x300, Threads: 6, Time: 8010.798 mss

## ColumnMajorBlockStrategy.java

Traverse the matrix column by column to make it linear, then split the elements into approximately equal chunks assigned to the threads.

Divide the matrix into chunks and assign each thread an approximately equal number of elements.

A 9×9 matrix (81 cells) split across 4 threads gives **20, 20, 20, and 21 cells** per thread.

**No synchronization**: Each thread computes its assigned range independently, without communicating with other threads or using locks.

Matrix: 9x9, Threads: 4, Time: 116.653 ms
Matrix: 1000x1000, Threads: 4, Time: 32538.990 ms
Matrix: 500x300, Threads: 6, Time: 8887.986 ms

## The difference between them

Row is generally faster because Java stores 2D arrays as arrays of row arrays, so accessing consecutive elements within a row has better cache locality

- Row-major: accesses `result[row][0]`, `result[row][1]`, `result[row][2]`, and so on. Consecutive elements are accessed from the same row array, improving CPU cache utilization.
- Column-major: accesses `result[0][col]`, `result[1][col]`, `result[2][col]`, and so on. These accesses jump between different row arrays, which can lead to more cache misses and less efficient memory access.

The main difference is the **memory access pattern**.

## Cyclic strategy

Assign each thread a cell based on how many there are, usually one thread will have `cells / nrThreads` cells assigned to it

It has poor access locallity since one thread ( like 4 -> 0,4,8,12) accesses the elements scattered across the memory

Example with `nrThreads = 4`:
- Thread 0: `0, 4, 8, 12, ...`
- Thread 1: `1, 5, 9, 13, ...`
- Thread 2: `2, 6, 10, 14, ...`
- Thread 3: `3, 7, 11, 15, ...`

So formula is 
`i = threadId + k * nrThreads`, k is the the pass var in loop
equivalent with 
`cellBlockId % nrThreads == threadId`

Replace in cyclic strategy with 
```java
for (int i = 0; i < total; i++) {
    if (i % nrThreads == threadId) {
	// (...)
    }
}
```

Matrix: 9x9, Threads: 4, Time: 169.712 ms
Matrix: 1000x1000, Threads: 4, Time: 35532.499 ms
Matrix: 500x300, Threads: 6, Time: 8896.068 ms

## Split the cells

5 x 3 matrix => 15 elements

```java
// For each thread:
// Thread 0: start = 0 * 15 / 4 = 0,   end = 1 * 15 / 4 = 3
// Thread 1: start = 1 * 15 / 4 = 3,   end = 2 * 15 / 4 = 7
// Thread 2: start = 2 * 15 / 4 = 7,   end = 3 * 15 / 4 = 11
// Thread 3: start = 3 * 15 / 4 = 11,  end = 4 * 15 / 4 = 15
```

*linear matrix goes from 0 to 14*

in all strategies *i* in the for represents the item in the liniary made array, that gets transformed into a matrix type index that is passed to `MatrixMultiplier`

- RowMajorBlockStrategy: Later threads get extra ( rest is acc at the end 3)
- CyclicStrategy: Later threads get fewer (latter threads tart further back and hit end at the end)
