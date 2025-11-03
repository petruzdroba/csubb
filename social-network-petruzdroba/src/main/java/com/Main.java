package main.java.com;

import main.java.com.repo.UserRepository;
import main.java.com.repo.FriendshipRepository;
import main.java.com.service.FriendshipService;
import main.java.com.service.UserService;
import main.java.com.ui.UserConsole;
import main.java.com.ui.FriendshipConsole;
import main.java.com.ui.MainConsole;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        UserRepository userRepo = new UserRepository("resources/users.in");
        FriendshipRepository friendshipRepo = new FriendshipRepository("resources/friendships.in");

        UserService userService = new UserService(userRepo, friendshipRepo);
        FriendshipService friendshipService = new FriendshipService(friendshipRepo, userRepo);

        UserConsole userConsole = new UserConsole(userService);
        FriendshipConsole friendshipConsole = new FriendshipConsole(friendshipService);

        MainConsole mainConsole = new MainConsole(
                List.of(userConsole, friendshipConsole)
        );

        mainConsole.run();
    }
}
