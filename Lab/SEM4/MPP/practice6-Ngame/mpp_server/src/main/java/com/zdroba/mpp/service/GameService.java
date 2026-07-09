package com.zdroba.mpp.service;

import com.zdroba.mpp.entity.Configuration;
import com.zdroba.mpp.entity.Game;
import com.zdroba.mpp.exceptions.NotFoundException;
import com.zdroba.mpp.notification.WebSocketHandler;
import com.zdroba.mpp.repository.ConfRepo;
import com.zdroba.mpp.repository.GameRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class GameService implements IGameService {

    private final GameRepo gameRepo;
    private final ConfRepo confRepo;
    private final WebSocketHandler ws;

    @Value("${game.min-players}")
    private int minPlayers;

    @Value("2")
    private int movesPerPlayer;

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
        game.setCurrentTurnIndex(0);
        gameRepo.save(game);

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
            game.getPositions().add(0);
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
    public void checkEnd(Long gameId) {
        Game game = gameRepo.findById(gameId).orElse(null);

        if (game.getTotalMoves() >= game.getPlayers().size() * movesPerPlayer) {
            game.setFinished(true);
            gameRepo.save(game);
            ws.sendToUsers(game.getPlayers(), "END");
        }
    }

    @Override
    public int action(Long userId, Long gameId) {
        Game game = gameRepo.findById(gameId).orElseThrow();

        Configuration configuration = game.getConfiguration();
        List<Integer> board = configuration.getBoard();

        int boardSize = board.size();

        Random random = new Random();
        int move = random.nextInt(boardSize + 1);

        int idx = game.getPlayers().indexOf(userId);

        int currentPosition = game.getPositions().get(idx);
        int newPosition = (currentPosition + move) % boardSize;

        game.getPositions().set(idx, newPosition);

        int points = board.get(newPosition);

        game.getPoints().set(idx, game.getPoints().get(idx) + points);
        game.setTotalMoves(game.getTotalMoves() + 1);

        gameRepo.save(game);

        checkEnd(gameId);
        if (!game.isFinished()) {
            nextTurn(gameId);
        }

        return points;
    }

    public void setConfiguration(Long gameId, Long configurationId) {
        Game game = gameRepo.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        Configuration configuration = confRepo.findById(configurationId)
                .orElseThrow(() -> new RuntimeException("Configuration not found"));

        game.setConfiguration(configuration);

        gameRepo.save(game);
    }

    public Configuration getConfiguration(Long gameId) {
        Game game = gameRepo.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        return game.getConfiguration();
    }

    @Override
    public Configuration addConfiguration(List<Integer> board) {
        Configuration configuration = new Configuration();
        configuration.setBoard(board);

        return confRepo.save(configuration);
    }

    @Override
    public List<Configuration> getConfigurations() {
        return confRepo.findAll();
    }

    @Override
    public List<Game> getGamesWithAtLeastPoints(Long userId, int minPoints) {
        return gameRepo.findAll().stream()
                .filter(Game::isFinished)
                .filter(game -> {
                    int idx = game.getPlayers().indexOf(userId);
                    return idx != -1 && game.getPoints().get(idx) >= minPoints;
                })
                .toList();
    }
}
