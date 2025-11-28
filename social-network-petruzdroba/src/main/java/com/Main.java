package com;

import com.domain.DataBaseConfig;
import com.repo.*;
import com.service.*;
import com.ui.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        DataBaseConfig config = new DataBaseConfig(
                "jdbc:postgresql://localhost:5432/social_network",
                "sn_user", "sn_pass"
        );

        UserRepository userRepo = new UserRepository(config);
        FriendshipRepository friendshipRepo = new FriendshipRepository(config);
        CardRepository cardRepository = new CardRepository(config);
        EventRepository eventRepository = new EventRepository(config, userRepo);

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
        DuckApplication.main(args);
        mainConsole.run();

    }
}
