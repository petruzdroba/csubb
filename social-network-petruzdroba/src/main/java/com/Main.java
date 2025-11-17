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

        UserRepository userRepo = new UserRepository("jdbc:postgresql://localhost:5432/social_network", "sn_user", "sn_pass");
        FriendshipRepository friendshipRepo = new FriendshipRepository("jdbc:postgresql://localhost:5432/social_network", "sn_user", "sn_pass");
        CardRepository cardRepository = new CardRepository("jdbc:postgresql://localhost:5432/social_network", "sn_user", "sn_pass");
        EventRepository eventRepository = new EventRepository("jdbc:postgresql://localhost:5432/social_network", "sn_user", "sn_pass", userRepo);

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
