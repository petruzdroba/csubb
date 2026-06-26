package com.zdroba.mpp.service;

import com.zdroba.mpp.entity.Boat;
import com.zdroba.mpp.entity.Game;
import com.zdroba.mpp.entity.Status;
import com.zdroba.mpp.exceptions.NotFoundException;
import com.zdroba.mpp.notification.WebSocketHandler;
import com.zdroba.mpp.repository.GameRespository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class GameService implements IGameService {

    private final GameRespository gameRespository;
    private final WebSocketHandler ws;

    public GameService(GameRespository gameRespository, WebSocketHandler ws) {
        this.gameRespository = gameRespository;
        this.ws = ws;
    }

    @Override
    public Game get(Long gameId) {
        return gameRespository.findById(gameId).orElse(null);
    }

    @Override
    public Game start(Long gameId) {
        Game game = gameRespository.findById(gameId).orElseThrow(() -> new NotFoundException("Game not found"));

        game.setStatus(Status.ONGOING);
        gameRespository.update(game);

        this.ws.sendToUsers(List.of(game.getPlayer2(), game.getPlayer1()), "START");
        this.ws.sendToUser(game.getPlayer1(), "YOUR TURN");

        return game;
    }

    @Override
    public void turn(Long gameId) {
        Game game = gameRespository.findById(gameId).orElseThrow(() -> new NotFoundException("Game not"));

        if (game.getTurn() == 1) {
            game.setTurn(2);
            ws.sendToUser(game.getPlayer2(), "YOUR TURN");
        } else {
            game.setTurn(1);
            ws.sendToUser(game.getPlayer1(), "YOUR TURN");
        }
        gameRespository.update(game);
    }

    @Override
    public Game addPlayer(Long userId, int x_1, int y_1, int x_2, int y_2) {
        List<Game> waitingGames = gameRespository.findAll().stream().filter(game -> game.getStatus() == Status.WAITING).toList();

        if (waitingGames.isEmpty()) {
            Game game = new Game();
            game.setPlayer1(userId);
            game.setBoat1(new Boat(userId, x_1, y_1, x_2, y_2));
            game.setStatus(Status.WAITING);

            this.ws.sendToUser(userId, "WAITING"); // one player exists in this game
            return gameRespository.save(game);
        } else {
            Game game = waitingGames.getLast();
            game.setPlayer2(userId);
            game.setBoat2(new Boat(userId, x_1, y_1, x_2, y_2));
            game.setStatus(Status.READY);

            this.ws.sendToUsers(List.of(userId, game.getPlayer1()), "READY"); // ready to start the game
            return gameRespository.update(game);
        }
    }

    @Override
    public boolean addGuess(Long userId, Long gameId, int x, int y) {
        Game game = gameRespository.findById(gameId).orElseThrow(() -> new NotFoundException("Game not"));
        boolean hit = false;

        Boat boat;
        if (!Objects.equals(userId, game.getPlayer1())) {
            boat = game.getBoat1();
            game.add1();
        } else {
            boat = game.getBoat2();
            game.add2();
        }

        if ((boat.getX_1() == x && boat.getY_1() == y)) {
            boat.setGuessed1(true);
            hit = true;
        }

        if ((boat.getX_2() == x && boat.getY_2() == y)) {
            boat.setGuessed2(true);
            hit = true;
        }

        if (boat.isGuessed1() && boat.isGuessed2()) {
            ws.sendToUser(userId, "WON");
            game.setWinner(userId);
            ws.sendToUsers(List.of(game.getPlayer1(), game.getPlayer2()), "END");
            game.setStatus(Status.DONE);
        } else {
            gameRespository.update(game);
            turn(game.getId());
        }

        return hit;
    }

    @Override
    public List<Game> getGames(Long userId) {
        List<Game> games = gameRespository.findBy("player1", userId);
        games.addAll(gameRespository.findBy("player2", userId));
        return games;
    }
}
