package com.zdroba.mpp.service;

import com.zdroba.mpp.entity.Configuration;
import com.zdroba.mpp.entity.Game;
import com.zdroba.mpp.entity.Guess;
import com.zdroba.mpp.exceptions.NotFoundException;
import com.zdroba.mpp.notification.WebSocketHandler;
import com.zdroba.mpp.repository.ConfRepo;
import com.zdroba.mpp.repository.GameRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class GameService implements IGameService {

    private final GameRepo gameRepo;
    private final ConfRepo confRepo;
    private final WebSocketHandler ws;

    @Value("${game.min-players}")
    private int minPlayers;

    public GameService(GameRepo gameRepo, ConfRepo confRepo, WebSocketHandler ws) {
        this.gameRepo = gameRepo;
        this.confRepo = confRepo;
        this.ws = ws;
    }

    @Override
    public Game start(Long id) {
        Game game = gameRepo.findById(id).orElseThrow();

        if (game.isStarted()) return game;
        if (game.getPlayers().size() < minPlayers)
            return game;

        game.setStarted(true);
        game = gameRepo.save(game);

        ws.sendToUsers(game.getPlayers(), "START");
        ws.sendToUser(game.getCurrentPlayerId(), "YOUR TURN");

        return game;
    }

    @Override
    public Game get(Long id) {
        return gameRepo.findById(id).orElse(null);
    }

    @Override
    public Game addPlayer(Long userId) {
        Game game = gameRepo.findAll().stream()
                .filter(g -> !g.isStarted())
                .findFirst()
                .orElseGet(() -> gameRepo.save(new Game()));

        if (!game.getPlayers().contains(userId)) {
            game.getPlayers().add(userId);
            game.getPoints().add(0);
            game = gameRepo.save(game);
        }

        if(game.getPlayers().size() == minPlayers/2){
            ws.sendToUser(userId, "CONFIG");
        }

        if (game.getPlayers().size() >= minPlayers) {
            ws.sendToUsers(game.getPlayers(), "STANDBY");
        }else{
            ws.sendToUser(userId, "WAITING");
        }

        return game;
    }

    @Override
    public void nextTurn(Long gameId) {
        Game game = gameRepo.findById(gameId).orElseThrow();

        int next = (game.getCurrentTurnIndex() + 1) % game.getPlayers().size();
        game.setCurrentTurnIndex(next);
        gameRepo.save(game);

        ws.sendToUser(game.getCurrentPlayerId() , "YOUR TURN");
    }

    @Override
    public boolean checkEnd(Long gameId) {
        Game game = gameRepo.findById(gameId).orElse(null);
        boolean ended = false;

        if (game.turnCount / game.getPlayers().size() >= game.getPlayers().size()) {
            ws.sendToUsers(game.getPlayers(), "END");
            game.setFinished(true);
            ended = true;
            gameRepo.save(game);
        }

        return ended;
    }

    @Override
    public int action(Long userId, Long gameId, String guess) {
        Game game = gameRepo.findById(gameId).orElseThrow();
        String word = game.getConfiguration().word;
        game.turnCount ++;

        Guess guessObj = new Guess();
        guessObj.userId = userId;
        guessObj.guess = guess;
        guessObj.round = game.turnCount;

        game.guesses.add(guessObj);

        int points = 0;

        if(Objects.equals(guess, word)){
            points +=5;
        }

        int idx = game.getPlayers().indexOf(userId);
        game.getPoints().set(idx, game.getPoints().get(idx) + points);
        gameRepo.save(game);

        if(!checkEnd(gameId)){
            nextTurn(gameId);
        }

        return points;
    }

    @Override
    public Configuration getConfig(Long gameId) {
        Game game = gameRepo.findById(gameId)
                .orElseThrow(() -> new NotFoundException("game not"));
        return game.getConfiguration();
    }

    @Override
    public void addConfig(Long gameId, Long confId) {
        Game game = gameRepo.findById(gameId)
                .orElseThrow(() -> new NotFoundException("game not"));

        Configuration conf = confRepo.findById(confId)
                .orElseThrow(() -> new NotFoundException("conf not"));

        game.setConfiguration(conf);
        gameRepo.save(game);
    }

    @Override
    public List<Game> getByUserAndY(Long userId, Integer Y) {

        if(Y == null)
            throw new RuntimeException("Wordcount missing");
        return gameRepo.getByUserIdAnd(userId, Y);
    }
}
