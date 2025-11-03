package main.java.com;

import main.java.com.repo.UserRepository;
import main.java.com.repo.FriendshipRepository;
import main.java.com.repo.CardRepository;
import main.java.com.service.CardService;
import main.java.com.service.FriendshipService;
import main.java.com.service.UserService;
import main.java.com.ui.UserConsole;
import main.java.com.ui.FriendshipConsole;
import main.java.com.ui.CardConsole;
import main.java.com.ui.MainConsole;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        UserRepository userRepo = new UserRepository("resources/users.in");
        FriendshipRepository friendshipRepo = new FriendshipRepository("resources/friendships.in");
        CardRepository cardRepo = new CardRepository("resources/card.in");

        UserService userService = new UserService(userRepo, friendshipRepo);
        FriendshipService friendshipService = new FriendshipService(friendshipRepo, userRepo);
        CardService cardService = new CardService(cardRepo);

        UserConsole userConsole = new UserConsole(userService);
        FriendshipConsole friendshipConsole = new FriendshipConsole(friendshipService);
        CardConsole cardConsole = new CardConsole(cardService);

        MainConsole mainConsole = new MainConsole(
                List.of(userConsole, friendshipConsole, cardConsole)
        );

        mainConsole.run();
    }
}
