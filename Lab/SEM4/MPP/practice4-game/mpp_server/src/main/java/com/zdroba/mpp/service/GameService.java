package com.zdroba.mpp.service;


import com.zdroba.mpp.entity.Game;
import com.zdroba.mpp.entity.Tom;
import com.zdroba.mpp.entity.Points;
import com.zdroba.mpp.notification.WebSocketHandler;
import com.zdroba.mpp.repository.GameRepository;
import com.zdroba.mpp.repository.PointsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class GameService implements IGameService{

    private final GameRepository gameRepository;
    private final PointsRepository pointsRepository;
    private Game game;
    private final WebSocketHandler ws;

    public GameService(GameRepository gameRepository, PointsRepository pointsRepository, WebSocketHandler ws) {
        this.gameRepository = gameRepository;
        this.pointsRepository = pointsRepository;
        this.ws = ws;
        game = new Game();
    }

    @Override
    public Game start() {
        ws.sendToUsers(game.getPlayers(), "START");
        return game;
    }

    @Override
    public Game get() {
        return game;
    }

    @Override
    public void addPlayer(Long userId) {
        if(game.getPlayers().contains(userId))
            return;

        game.addPlayer(userId);
        if(game.getId() == null)
            game = gameRepository.save(game);
        else
            game = gameRepository.update(game);

        String msg = game.getPlayers().size() >= 3 ? "STANDBY" : "WAITING";
        ws.sendToUsers(game.getPlayers(), msg);
    }

    private boolean checker(String word, char letter){// checks non empty + starts with letter
        return word.isEmpty() || word.charAt(0) != letter;
    }

    private void checkEnd(int tomCount) {
        int playerCount = game.getPlayers().size();
        if (game.getTurn() == 1 && tomCount == playerCount) {
            game.setTurn((short) 2);
            game.setCurrentLetter((char)('A' + new Random().nextInt(26)));

            game = gameRepository.update(game);
            ws.sendToUsers(game.getPlayers(), "UPDATE");
        } else if (game.getTurn() == 2 && tomCount == 2 * playerCount) {
            game.setTurn((short) 3);
            game.setCurrentLetter((char)('A' + new Random().nextInt(26)));

            game = gameRepository.update(game);
            ws.sendToUsers(game.getPlayers(), "UPDATE");
        } else if (game.getTurn() == 3 && tomCount == 3 * playerCount) {
            ws.sendToUsers(game.getPlayers(), "END");
            game = new Game();
        }
    }

    @Override
    public int addTom(Long userId, String tara, String oras, String mare) {
        int points = 0;

        if (checker(tara, game.getCurrentLetter()) || checker(oras, game.getCurrentLetter()) || checker(mare, game.getCurrentLetter())) {
            return  0;
        }

        Tom existingTara = game.getToms().stream()
                .filter(t -> tara.equals(t.getTara()))
                .findFirst()
                .orElse(null);
        points += (existingTara != null && !existingTara.getTara().isEmpty()) ? 3 : 10;

        Tom existingOras = game.getToms().stream()
                .filter(t -> oras.equals(t.getOras()))
                .findFirst()
                .orElse(null);
        points += (existingOras != null && !existingOras.getOras().isEmpty()) ? 3 : 10;

        Tom existingMare = game.getToms().stream()
                .filter(t -> mare.equals(t.getMare()))
                .findFirst()
                .orElse(null);
        points += (existingMare != null && !existingMare.getMare().isEmpty()) ? 3 : 10;

        Tom tom = new Tom(userId, tara, oras, mare);
        game.getToms().add(tom);
        int size = game.getToms().size();
        game = gameRepository.update(game);


        Points pts = new Points(userId, game.getId(), points, game.getTurn());
        pointsRepository.save(pts);

        checkEnd(size);
        return points;
    }

    @Override
    public List<Points> summary(Long gameId) {
        return pointsRepository.findBy("gameId", gameId);
    }

    @Override
    public List<Points> summary(Long gameId, Long userId) {
        return pointsRepository.findByGameAndUser(gameId, userId);
    }
}
