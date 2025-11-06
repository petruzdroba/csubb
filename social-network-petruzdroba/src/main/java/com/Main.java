package main.java.com;

import main.java.com.domain.RaceEvent;
import main.java.com.repo.CardRepository;
import main.java.com.repo.UserRepository;
import main.java.com.repo.FriendshipRepository;
import main.java.com.service.CardService;
import main.java.com.service.EventService;
import main.java.com.service.FriendshipService;
import main.java.com.service.UserService;
import main.java.com.ui.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        UserRepository userRepo = new UserRepository("resources/users.in");
        FriendshipRepository friendshipRepo = new FriendshipRepository("resources/friendships.in");
        CardRepository cardRepository = new CardRepository("resources/card.in");

        UserService userService = new UserService(userRepo, friendshipRepo, cardRepository);
        FriendshipService friendshipService = new FriendshipService(friendshipRepo, userRepo);
        CardService cardService = new CardService(cardRepository, userRepo);

        EventService eventService = new EventService(cardService, userRepo, new RaceEvent());

        UserConsole userConsole = new UserConsole(userService);
        FriendshipConsole friendshipConsole = new FriendshipConsole(friendshipService);
        CardConsole cardConsole = new CardConsole(cardService);
        EventConsole eventConsole = new EventConsole(eventService);

        MainConsole mainConsole = new MainConsole(
                List.of(userConsole, friendshipConsole, cardConsole, eventConsole)
        );

        mainConsole.run();
    }
}
