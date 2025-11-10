package com.ui;

import java.util.Scanner;

public abstract class AbstractConsole {
    protected final Scanner scanner = new Scanner(System.in);

    public abstract void run();

    protected abstract void showMenu();

    public abstract String toString();
}
