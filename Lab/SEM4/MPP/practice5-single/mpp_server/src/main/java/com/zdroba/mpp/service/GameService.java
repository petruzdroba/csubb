package com.zdroba.mpp.service;

import com.zdroba.mpp.entity.Boat;
import com.zdroba.mpp.entity.Game;
import com.zdroba.mpp.entity.Guess;
import com.zdroba.mpp.entity.Type;
import com.zdroba.mpp.exceptions.NotFoundException;
import com.zdroba.mpp.notification.WebSocketHandler;
import com.zdroba.mpp.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class GameService implements IGameService{

    private final GameRepository gameRepository;
    private final WebSocketHandler ws;
    private final Random random = new Random();

    public GameService(GameRepository gameRepository, WebSocketHandler ws) {
        this.gameRepository = gameRepository;
        this.ws = ws;
    }

    @Override
    public Game get(Long id) {
        return gameRepository.findById(id).orElse(null);
    }

    private Boat generateBoat() {
        int pos_x = random.nextInt(5);
        int pos_y = random.nextInt(5);

        if (random.nextInt(2) == 0) {
            pos_x = random.nextInt(3);
            return new Boat(pos_x, pos_y,
                    pos_x + 1, pos_y,
                    pos_x + 2, pos_y);
        }

        pos_y = random.nextInt(3);
        return new Boat(pos_x, pos_y,
                pos_x, pos_y + 1,
                pos_x, pos_y + 2);
    }
    @Override
    public Game start(Long userId) {
        Game game = new Game(userId, generateBoat());

        this.ws.sendToUser(userId, "START");
        return gameRepository.save(game);
    }

    @Override
    public Guess guess(Long userId, Long gameId, int x, int y) {
        Game game = gameRepository.findById(gameId).orElseThrow(()-> new NotFoundException("Game not"));
        Boat boat = game.getBoats();
        Guess guess = new Guess(0, Type.MISS);
        int points = 0;

        if((boat.getX_1() == x -1 || boat.getX_1() == x + 1)  && (boat.getY_1() == y -1 || boat.getY_1() == y + 1) ){
            points = 3;
            guess.setType(Type.NEAR);
        }
        else if((boat.getX_2() == x -1 || boat.getX_2() == x + 1)  && (boat.getY_2() == y -1 || boat.getY_2() == y + 1) ){
            points = 3;
            guess.setType(Type.NEAR);
        }
        else if((boat.getX_3() == x -1 || boat.getX_3() == x + 1)  && (boat.getY_3() == y -1 || boat.getY_3() == y + 1) ){
            points = 3;
            guess.setType(Type.NEAR);
        }

        if(boat.getX_1() == x && boat.getY_1() == y){
            boat.setGuessed1(true);
            points = 5;
            guess.setType(Type.HIT);
        }
        else if(boat.getX_2() == x && boat.getY_2() == y){
            boat.setGuessed2(true);
            points = 5;
            guess.setType(Type.HIT);
        }
        else if(boat.getX_3() == x && boat.getY_3() == y){
            boat.setGuessed3(true);
            points = 5;
            guess.setType(Type.HIT);
        }

        if(game.getBoats().isGuessed1() && game.getBoats().isGuessed2() && game.getBoats().isGuessed3()){
            ws.sendToUser(userId, "END");
        }

        game.addTurns();
        game.addPoints(points);
        gameRepository.update(game);

        guess.setPoints(points);
        return guess;
    }


}
