// ============================================================================
// Laboratory 1 - Non-Cooperative Multithreading
// Example: Concurrent Warehouse System
// ----------------------------------------------------------------------------
// This example illustrates how to safely handle concurrent updates to shared
// data structures using fine-grained mutexes, lock ordering, and invariants.
// ----------------------------------------------------------------------------
// Theoretical context:
//   - Based on PDP Lecture 1 (Concurrency Intro) & Lecture 2 (Synchronization)
//   - Concepts covered: threads, critical sections, race conditions,
//     mutual exclusion (mutex), deadlocks, fine-grained locking.
// ----------------------------------------------------------------------------
// Works with Visual Studio 2022 (C++17+)
// ============================================================================

#include <vector>
#include <mutex>
#include <algorithm>
#include <thread>
#include <iostream>
#include <random>
#include <cassert>

class WarehouseSystem {
public:
    using wid_t = int; // warehouse ID 
    using pid_t = int; // product ID 

    // ------------------------------------------------------------------------
    // Constructor: allocate W x P stock matrix and one mutex per cell.
    // Each (warehouse, product) pair has its own mutex -> fine-grained locking.
    // ------------------------------------------------------------------------
    WarehouseSystem(int warehouses, int products)
        : W(warehouses), P(products),
        stock(warehouses, std::vector<int>(products, 0)),
        cell_mutex(warehouses* products) // 1D array of mutexes
    {}

    // ------------------------------------------------------------------------
    // Initialize stock levels for a given (warehouse, product) cell.
    // lock_guard automatically locks & unlocks the associated mutex (RAII).
    // ------------------------------------------------------------------------
    void set_stock(wid_t w, pid_t p, int qty) {
        std::lock_guard<std::mutex> lk(get_mutex(w, p)); // enter critical section
        stock[w][p] = qty;                               // safe write to shared data
    }

    // ------------------------------------------------------------------------
    // Perform a move operation: transfer some products from src → dst.
    // Multiple threads may call this concurrently.
    // Ensures thread safety and avoids deadlocks.
    // ------------------------------------------------------------------------
    bool do_move(wid_t src, wid_t dst, const std::vector<std::pair<pid_t, int>>& products) {
        if (!valid_w(src) || !valid_w(dst))
            throw std::out_of_range("Invalid warehouse index");

        // 1. Collect all (warehouse, product) coordinates that will be accessed.
        //    Each must be locked to ensure mutual exclusion.
        std::vector<std::pair<wid_t, pid_t>> coords;  // This creates a variable named coords, which is a vector (dynamic array) that will hold elements of type std::pair<wid_t, pid_t>
        /*
        std::pair<A, B> is a tiny struct that holds two values, one of type A, one of type B. Here that’s (warehouse_id, product_id).
        So each element of coords looks like: { some_warehouse_id, some_product_id }
        */
        coords.reserve(products.size() * 2);
        for (auto& pp : products) {
            pid_t pid = pp.first;
            if (!valid_p(pid))
                throw std::out_of_range("Invalid product index");
            coords.emplace_back(src, pid);
            coords.emplace_back(dst, pid);
        }

        // 2. Sort and remove duplicates.
        //    -> ensures deterministic locking order across all threads.
        //    -> prevents circular waiting -> avoids deadlocks.
        sort_and_unique(coords);

        // 3. Lock all relevant mutexes in sorted order.
        //    Using unique_lock allows explicit control and automatic unlock.
        std::vector<std::unique_lock<std::mutex>> locks;
        locks.reserve(coords.size());
        for (auto& c : coords) {
            locks.emplace_back(get_mutex(c.first, c.second));
        }

        // 4. Critical section: verify availability and perform transfer.
        for (auto& pp : products) {
            pid_t pid = pp.first;
            int qty = pp.second;

            // If invalid quantity or not enough stock, abort.
            if (qty < 0 || stock[src][pid] < qty)
                return false;
        }

        // Update stock safely under all held locks.
        for (auto& pp : products) {
            pid_t pid = pp.first;
            int qty = pp.second;
            stock[src][pid] -= qty; // remove from source
            stock[dst][pid] += qty; // add to destination
        }

        // 5. Locks are automatically released when leaving the function
        //    (RAII principle: Resource Acquisition Is Initialization).
        return true;
    }

    // ------------------------------------------------------------------------
    // Verify that the global inventory is consistent.
    // All locks are acquired before reading, ensuring a consistent snapshot.
    // ------------------------------------------------------------------------
    std::vector<long long> inventory_check_all_locked() {
        std::vector<std::pair<wid_t, pid_t>> coords;
        coords.reserve(W * P);
        for (wid_t w = 0; w < W; ++w)
            for (pid_t p = 0; p < P; ++p)
                coords.emplace_back(w, p);
        sort_and_unique(coords);

        // Lock every (warehouse, product) cell to read consistent data.
        std::vector<std::unique_lock<std::mutex>> locks;
        locks.reserve(coords.size());
        for (auto& c : coords)
            locks.emplace_back(get_mutex(c.first, c.second));

        // Compute total inventory per product across all warehouses.
        std::vector<long long> totals(P, 0);
        for (wid_t w = 0; w < W; ++w)
            for (pid_t p = 0; p < P; ++p)
                totals[p] += stock[w][p];

        return totals;
    }

    // Utility: print the stock matrix for debugging / inspection.
    void print_all() {
        for (wid_t w = 0; w < W; ++w) {
            std::cout << "Warehouse " << w << ": ";
            for (pid_t p = 0; p < P; ++p)
                std::cout << stock[w][p] << " ";
            std::cout << "\n";
        }
    }

private:
    int W, P;  // number of warehouses and products
    std::vector<std::vector<int>> stock; // stock[w][p] = quantity
    std::vector<std::mutex> cell_mutex;  // flattened: W * P mutexes (fine-grained)

    inline bool valid_w(wid_t w) const { return 0 <= w && w < W; }
    inline bool valid_p(pid_t p) const { return 0 <= p && p < P; }

    // Map (warehouse, product) → index in cell_mutex array.
    // Allows fine-grained locking per cell.
    inline std::mutex& get_mutex(wid_t w, pid_t p) {
        return cell_mutex[w * P + p];
    }

    // Helper: sort and remove duplicates from coordinate list.
    // Ensures consistent lock ordering for deadlock prevention.
    static void sort_and_unique(std::vector<std::pair<wid_t, pid_t>>& coords) {
        std::sort(coords.begin(), coords.end(), [](auto& a, auto& b) {
            return (a.first < b.first) || (a.first == b.first && a.second < b.second);
            });
        coords.erase(std::unique(coords.begin(), coords.end()), coords.end());
    }
};

// ============================================================================
// Main program: small concurrent workload test
// ----------------------------------------------------------------------------
// Spawns multiple threads that perform random product transfers between
// warehouses. Checks that total product counts remain invariant.
// ============================================================================

int main() {
    const int W = 3, P = 4;      // 3 warehouses, 4 product types
    WarehouseSystem ws(W, P);

    // Initialize all warehouses with 100 units of each product.
    for (int w = 0; w < W; ++w)
        for (int p = 0; p < P; ++p)
            ws.set_stock(w, p, 100);

    // Save the initial totals to check invariants later.
    auto totals0 = ws.inventory_check_all_locked();

    // Randomized setup for concurrent workloads.
    std::mt19937 rng(std::random_device{}());
    std::uniform_int_distribution<int> wdist(0, W - 1);
    std::uniform_int_distribution<int> pdist(0, P - 1);
    std::uniform_int_distribution<int> qdist(1, 20);

    const int THREAD_COUNT = 8;      // number of concurrent workers
    const int OPS_PER_THREAD = 10000; // number of operations per thread

    std::vector<double> thread_times(THREAD_COUNT, 0.0); // performance data

    // ------------------------------------------------------------------------
    // Each worker thread repeatedly moves random quantities of random products
    // between random warehouses.
    // Demonstrates concurrent modification of shared state.
    // ------------------------------------------------------------------------
    auto worker = [&](int tid) {
        auto t_start = std::chrono::high_resolution_clock::now();
        int successful_moves = 0;

        for (int i = 0; i < OPS_PER_THREAD; ++i) {
            int src = wdist(rng);
            int dst = wdist(rng);
            if (src == dst) continue; // skip self-transfer

            int prod = pdist(rng);
            int qty = qdist(rng);

            // Attempt to perform move safely.
            if (ws.do_move(src, dst, { {prod, qty} }))
                ++successful_moves;

            // Occasionally verify that global invariants hold.
            if (i % 2000 == 0) {
                auto totals = ws.inventory_check_all_locked();
                for (int p = 0; p < P; ++p)
                    assert(totals[p] == totals0[p]); // invariant check
            }
        }

        auto t_end = std::chrono::high_resolution_clock::now();
        std::chrono::duration<double> elapsed = t_end - t_start;
        thread_times[tid] = elapsed.count();

        // Performance output per thread
        std::cout << "Thread " << tid
            << " completed " << successful_moves
            << " moves in " << elapsed.count()
            << " seconds (" << (successful_moves / elapsed.count())
            << " moves/sec)\n";
    };

    // ------------------------------------------------------------------------
    // Launch all threads and measure total time.
    // ------------------------------------------------------------------------
    std::vector<std::thread> threads;
    threads.reserve(THREAD_COUNT);

    auto global_start = std::chrono::high_resolution_clock::now();

    for (int t = 0; t < THREAD_COUNT; ++t)
        threads.emplace_back(worker, t); // start each worker thread

    for (auto& t : threads)
        t.join(); // wait for all threads to finish

    auto global_end = std::chrono::high_resolution_clock::now();
    std::chrono::duration<double> total_time = global_end - global_start;

    // ------------------------------------------------------------------------
    // Compute and print performance and correctness results.
    // ------------------------------------------------------------------------
    double avg_time = 0.0;
    for (double t : thread_times) avg_time += t;
    avg_time /= THREAD_COUNT;

    // Final invariant check: total quantities must remain the same.
    auto totals1 = ws.inventory_check_all_locked();
    for (int p = 0; p < P; ++p) {
        if (totals0[p] != totals1[p]) {
            std::cerr << "Invariant violated for product " << p
                << ": " << totals0[p] << " -> " << totals1[p] << "\n";
            return 2;
        }
    }

    // All good → consistent totals despite heavy concurrency.
    std::cout << "\nTotals unchanged, invariant OK.\n";
    std::cout << "Average thread time: " << avg_time << " seconds\n";
    std::cout << "Total elapsed time: " << total_time.count() << " seconds\n";
    std::cout << "Throughput: "
        << (THREAD_COUNT * OPS_PER_THREAD / total_time.count())
        << " total moves/sec across all threads.\n";

    // Print final warehouse states.
    ws.print_all();
    return 0;
}
