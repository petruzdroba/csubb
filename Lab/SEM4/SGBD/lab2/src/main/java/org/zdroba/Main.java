package org.zdroba;

import org.zdroba.db.DBInitializer;
import org.zdroba.demo.DirtyRead;
import org.zdroba.demo.LostUpdate;
import org.zdroba.demo.NonRepeatableRead;
import org.zdroba.demo.PhantomRead;

public class Main {
    public static void main(String[] args) {
        DBInitializer.resetDatabase();

//        DirtyRead.runDemo();
//        NonRepeatableRead.runDemo();
//        PhantomRead.runDemo();
        LostUpdate.runDemo();
    }
}
