package org.zdroba;

import org.zdroba.batch.AutoCommit;
import org.zdroba.batch.BatchCommit;
import org.zdroba.batch.LotCommit;
import org.zdroba.db.DBInitializer;
import org.zdroba.demo.*;

public class Main {
    public static void main(String[] args) {
        DBInitializer.resetDatabase();

//        DirtyRead.runDemo();
//        NonRepeatableRead.runDemo();
//        PhantomRead.runDemo();
//        LostUpdate.runDemo();
//        DeadLock.runDemo();
//        DeadLockOrder.runDemo();

        System.out.println( BatchCommit.average() + "ms");
    }
}
