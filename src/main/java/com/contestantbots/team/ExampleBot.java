package com.contestantbots.team;

import com.contestantbots.util.GameStateLogger;
import com.scottlogic.hackathon.game.Bot;
import com.scottlogic.hackathon.game.GameState;
import com.scottlogic.hackathon.game.Move;

import java.util.ArrayList;
import java.util.List;

public class ExampleBot extends Bot {
    private GameStateLogger gameStateLogger;

    public ExampleBot() {
        gameStateLogger = new GameStateLogger(getId());
    }

    @Override
    public List<Move> makeMoves(final GameState gameState) {
        gameStateLogger.process(gameState);
        return new ArrayList<>();
    }

    @Override
    public String getDisplayName() {
        return "Example Bot";
    }
}
