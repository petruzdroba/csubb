package com.zdroba.mpp.service;

import com.zdroba.mpp.entity.Game;
import com.zdroba.mpp.entity.Guess;
import com.zdroba.mpp.entity.Word;
import com.zdroba.mpp.notification.WebSocketHandler;
import com.zdroba.mpp.repository.GameRepository;
import com.zdroba.mpp.repository.GuessRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GameService implements IGameService {

    private final GameRepository gameRepo;
    private final GuessRepository guessRepo;
    private final WebSocketHandler ws;

    private Game game;
    private int turn = 0;

    public GameService(GameRepository gameRepo, GuessRepository guessRepo, WebSocketHandler ws) {
        this.gameRepo = gameRepo;
        this.guessRepo = guessRepo;
        this.ws = ws;
    }

    @Override
    public Game get() {
        return game;
    }

    @Override
    public Game start() {
        ws.sendToUsers(game.getPlayers(), "GAME START");
        ws.sendToUser(game.getPlayers().get(0), "YOUR TURN");
        return game;
    }

    @Override
    public void addPlayer(Long userId, String raw) {
        if (game == null) game = new Game();

        game.addPlayer(userId);
        raw = raw.toLowerCase();
        game.addWord(new Word(userId, raw.charAt(0), raw.charAt(1), raw.charAt(2)));

        if (game.getId() == null)
            game = gameRepo.save(game);
        else
            game = gameRepo.update(game);

        String msg = game.getPlayers().size() >= 2 ? "CAN START" : "CANNOT START";
        ws.sendToUsers(game.getPlayers(), msg);
    }

    @Override
    public int guess(Long userId, Long wordId, char letter) {
        if (game == null || !game.getPlayers().contains(userId)) return 0;

        int points = 0;

        Word word = game.getWords().stream()
                .filter(w -> w.getId().equals(wordId))
                .findFirst()
                .orElseThrow();

        if (word.getFirst() == letter)  { word.setFirst(Character.toUpperCase(letter));  points++; }
        if (word.getSecond() == letter) { word.setSecond(Character.toUpperCase(letter)); points++; }
        if (word.getThird() == letter)  { word.setThird(Character.toUpperCase(letter));  points++; }

        game = gameRepo.update(game);
        guessRepo.save(new Guess(game.getId(), userId, wordId, letter));

        if (game.isComplete()) {
            ws.sendToUsers(game.getPlayers(), "GAME END");
            game = null;
            turn = 0;
            return points;
        }

        turn = (turn + 1) % game.getPlayers().size();
        ws.sendToUser(game.getPlayers().get(turn), "YOUR TURN");

        return points;
    }

    @Override
    public Map<String, Object> getPlayerGuesses(Long gameId, Long userId) {
        List<Guess> guesses = guessRepo.findByGameAndUser(gameId, userId);

        int points = guesses.stream().mapToInt(g -> {
            Word word = game.getWords().stream()
                    .filter(w -> w.getId().equals(g.getWordId()))
                    .findFirst()
                    .orElse(null);

            if (word == null) return 0;

            int p = 0;
            char l = g.getLetter();
            if (Character.toUpperCase(word.getFirst())  == Character.toUpperCase(l)) p++;
            if (Character.toUpperCase(word.getSecond()) == Character.toUpperCase(l)) p++;
            if (Character.toUpperCase(word.getThird())  == Character.toUpperCase(l)) p++;

            return p;
        }).sum();

        return Map.of("guesses", guesses, "points", points);
    }
}