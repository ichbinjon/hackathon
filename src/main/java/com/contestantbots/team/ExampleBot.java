package com.contestantbots.team;

import com.contestantbots.util.GameStateLogger;
import com.scottlogic.hackathon.game.Bot;
import com.scottlogic.hackathon.game.GameState;
import com.scottlogic.hackathon.game.Move;

import java.util.ArrayList;
import java.util.List;

public class ExampleBot extends Bot {
    private final GameStateLogger gameStateLogger;

    public ExampleBot() {
        super("Example Bot");
        gameStateLogger = new GameStateLogger(getId());
    }

    @Override
    public List<Move> makeMoves(final GameState gameState) {
        gameStateLogger.process(gameState);
        return new ArrayList<>();
    }
}
