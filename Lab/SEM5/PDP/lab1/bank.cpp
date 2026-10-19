
#include <iostream>
#include <vector>
#include <mutex>
#include <stdexcept>
#include <algorithm>

#include <thread>
#include <random>
#include <chrono>
#include <cassert>
#include <iomanip>

class BankSystem {
public:
    using account_id = int;
    using balance_type = int;

    BankSystem(int account_count, balance_type initial_balance)
        : balances(account_count, initial_balance),
          account_mutexes(account_count) {}

    void transfer(account_id from, account_id to, balance_type amount) {
        if (!valid_account(from) || !valid_account(to)) {
            throw std::out_of_range("Invalid account");
        }

        if(!valid_amount(amount)){
            throw std::out_of_range("Invalid amount");
        }

        if (from == to) {
            return;
        }

        account_id first = std::min(from, to);
        account_id second = std::max(from, to);

        std::lock_guard<std::mutex> lock1(account_mutexes[first]);
        std::lock_guard<std::mutex> lock2(account_mutexes[second]);

        if (balances[from] < amount) {
            return;
        }

        balances[from] -= amount;
        balances[to] += amount;
    }

    balance_type total_balance() {
        std::vector<account_id> order(balances.size());

        for (account_id i = 0; i < order.size(); ++i) {
            order[i] = i;
        }

        std::vector<std::unique_lock<std::mutex>> locks;
        locks.reserve(order.size());

        for (account_id i : order) {
            locks.emplace_back(account_mutexes[i]);
        }

        balance_type total = 0;

        for (balance_type balance : balances) {
            total += balance;
        }

        return total;
    }

    void print_balances() {
        std::vector<std::unique_lock<std::mutex>> locks;
        locks.reserve(account_mutexes.size());

        for (auto& m : account_mutexes) {
            locks.emplace_back(m);
        }

        for (account_id i = 0; i < balances.size(); ++i) {
            std::cout << "Account " << i << ": " << balances[i] << '\n';
        }
    }

private:
    std::vector<balance_type> balances;
    std::vector<std::mutex> account_mutexes;

    inline bool valid_account(account_id id) const {
        return id >= 0 && id < balances.size();
    }

    inline bool valid_amount(balance_type amount) const {
        return amount > 0;
    }
};


int main() {
    const int INITIAL_BALANCE = 100000;
    const int TOTAL_OPERATIONS = 2000000;
    const int REPETITIONS = 3;

    const std::vector<int> account_counts = {4, 100, 1000, 10000};
    const std::vector<int> thread_counts = {1, 2, 4, 8, 16,65};

    std::cout << std::fixed << std::setprecision(4);

    for (int account_count : account_counts) {
        std::cout << "\nAccounts: " << account_count << '\n';
        std::cout << "Threads | Avg time (s) | Attempts/sec\n";
        std::cout << "--------------------------------------\n";

        for (int thread_count : thread_counts) {
            double total_elapsed = 0.0;

            for (int repetition = 0; repetition < REPETITIONS; ++repetition) {
                BankSystem bank(account_count, INITIAL_BALANCE);
                const auto initial_total = bank.total_balance();

                std::vector<std::thread> threads;
                threads.reserve(thread_count);

                auto start = std::chrono::steady_clock::now();

                for (int tid = 0; tid < thread_count; ++tid) {
                    threads.emplace_back([&, tid]() {
                        std::mt19937 rng(
                            12345u + tid + repetition * 1000u
                        );

                        std::uniform_int_distribution<int> account_dist(
                            0, account_count - 1
                        );
                        std::uniform_int_distribution<int> amount_dist(1, 100);

                        const int operations =
                            TOTAL_OPERATIONS / thread_count +
                            (tid < TOTAL_OPERATIONS % thread_count ? 1 : 0);

                        const int check_interval =
                            std::max(1, operations / 2);

                        for (int i = 0; i < operations; ++i) {
                            int from = account_dist(rng);
                            int to = account_dist(rng);
                            int amount = amount_dist(rng);

                            bank.transfer(from, to, amount);

                            if ((i + 1) % check_interval == 0) {
                                assert(bank.total_balance() == initial_total);
                            }
                        }
                    });
                }

                for (auto& thread : threads) {
                    thread.join();
                }

                auto end = std::chrono::steady_clock::now();

                total_elapsed += std::chrono::duration<double>(
                    end - start
                ).count();

                assert(bank.total_balance() == initial_total);
            }

            const double average_time = total_elapsed / REPETITIONS;
            const double throughput = TOTAL_OPERATIONS / average_time;

            std::cout << std::setw(7) << thread_count << " | "
                      << std::setw(12) << average_time << " | "
                      << std::setw(12) << std::setprecision(0)
                      << throughput << std::setprecision(4) << '\n';
        }
    }

    std::cout << "\nAll consistency checks passed.\n";
    return 0;
}
