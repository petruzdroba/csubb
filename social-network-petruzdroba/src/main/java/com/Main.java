package main.java.com;

import main.java.com.repo.Repository;
import main.java.com.repo.RepositoryFile;
import main.java.com.service.Service;
import main.java.com.ui.Console;

public class Main {
    public static void main(String[] args) {
        Repository repository = new RepositoryFile("resources/users.in", "resources/friendships.in", "resources/card.in");
        Service service = new Service(repository);
        Console console = new Console(service);

        console.start();
    }
}