package com;

import com.repo.CardRepository;
import com.repo.EventRepository;
import com.repo.UserRepository;
import com.repo.FriendshipRepository;
import com.service.CardService;
import com.service.EventService;
import com.service.FriendshipService;
import com.service.UserService;
import com.ui.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        UserRepository userRepo = new UserRepository("resources/users.in");
        FriendshipRepository friendshipRepo = new FriendshipRepository("resources/friendships.in");
        CardRepository cardRepository = new CardRepository("resources/card.in");
        EventRepository eventRepository = new EventRepository("resources/event.in", userRepo);

        UserService userService = new UserService(userRepo, friendshipRepo, cardRepository);
        FriendshipService friendshipService = new FriendshipService(friendshipRepo, userRepo);
        CardService cardService = new CardService(cardRepository, userRepo);
        EventService eventService = new EventService(eventRepository, cardService, userRepo);


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
