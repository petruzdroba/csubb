package org.zdroba;

import org.zdroba.batch.AutoCommit;
import org.zdroba.batch.BatchCommit;
import org.zdroba.batch.LotCommit;
import org.zdroba.demo.*;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        java.util.Scanner sc = new java.util.Scanner(System.in);

        while(true){
            System.out.println("\n--- Database Transaction Demos ---");
            System.out.println("1. Dirty Read");
            System.out.println("2. Non-Repeatable Read");
            System.out.println("3. Phantom Read");
            System.out.println("4. Lost Update");
            System.out.println("5. Deadlock Demo");
            System.out.println("6. Deadlock Ordered Demo");
            System.out.println("7. AutoCommit Batch Performance");
            System.out.println("8. LotCommit Batch Performance");
            System.out.println("9. BatchCommit Batch Performance");
            System.out.println("0. Exit");
            System.out.print("Select demo: ");

            int choice = sc.nextInt();
            switch(choice){
                case 1: DirtyRead.runDemo(); break;
                case 2: NonRepeatableRead.runDemo(); break;
                case 3: PhantomRead.runDemo(); break;
                case 4: LostUpdate.runDemo(); break;
                case 5: DeadLock.runDemo(); break;
                case 6: DeadLockOrder.runDemo(); break;
                case 7: runAutoCommitBatch(); break;
                case 8: runLotCommitBatch(); break;
                case 9: runSingleTransactionBatch(); break;
                case 0: System.exit(0);
                default: System.out.println("Invalid choice");
            }
        }
    }

    private static void runAutoCommitBatch() {
        System.out.println("\nRunning AutoCommit Batch...");
        double average = AutoCommit.average();
        System.out.println("Average AutoCommit: " + average + " ms");
    }

    private static void runLotCommitBatch() {
        System.out.println("\nRunning LotCommit Batch...");
        double average = LotCommit.average();
        System.out.println("Average LotCommit: " + average + " ms");
    }

    private static void runSingleTransactionBatch() {
        System.out.println("\nRunning BatchCommit...");
        double average = BatchCommit.average();
        System.out.println("Average BatchCommit: " + average + " ms");
    }
}