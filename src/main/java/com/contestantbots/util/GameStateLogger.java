package com.contestantbots.util;

import com.scottlogic.hackathon.game.Collectable;
import com.scottlogic.hackathon.game.GameState;
import com.scottlogic.hackathon.game.Player;
import com.scottlogic.hackathon.game.Position;
import com.scottlogic.hackathon.game.SpawnPoint;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class GameStateLogger {
    private static UUID BOT_ID;
    private static GameState GAME_STATE;

    public static void process(UUID botId, GameState gameState) {
        BOT_ID = botId;
        GAME_STATE = gameState;

        renderSeparator(true);
        System.out.println("turn: " + gameState.getPhase());
        System.out.println("map: " + gameState.getMap().getWidth() + " wide by " + gameState.getMap().getHeight() + " high");
        renderSeparator(true);

        System.out.println("Out of Bounds");
        Set<Position> outOfBounds = gameState.getOutOfBoundsPositions();
        if (outOfBounds.isEmpty()) {
            System.out.println("none visible");
        } else {
            outOfBounds.forEach(outOfBound -> System.out.println("WATER" + formatPosition(outOfBound)));
        }
        renderSeparator(true);

        renderSpawnPoints();
        renderPlayers();

        System.out.println("Collectables");
        Set<Collectable> collectables = gameState.getCollectables();
        if (collectables.isEmpty()) {
            System.out.println("none visible");
        } else {
            collectables.forEach(collectable -> System.out.println(formatCollectable(collectable)));
        }
        renderSeparator(true);
        System.out.println();
        System.out.println();
    }

    private static void renderSpawnPoints() {
        List<SpawnPoint> friendlySpawnPoints = GAME_STATE.getSpawnPoints()
                .stream()
                .filter(spawnPoint -> spawnPoint.getOwner().equals(BOT_ID))
                .collect(Collectors.toList());
        List<SpawnPoint> enemySpawnPoints = GAME_STATE.getSpawnPoints()
                .stream()
                .filter(spawnPoint -> !spawnPoint.getOwner().equals(BOT_ID))
                .collect(Collectors.toList());
        Set<SpawnPoint> removedSpawnPoints = GAME_STATE.getRemovedSpawnPoints();

        System.out.println("SpawnPoints");
        System.out.println("Friendly");
        if (friendlySpawnPoints.isEmpty()) {
            System.out.println("none");
        } else {
            friendlySpawnPoints.forEach(spawnPoint -> System.out.println(formatSpawnPoint(spawnPoint)));
        }
        renderSeparator(false);
        System.out.println("Enemy");
        if (enemySpawnPoints.isEmpty()) {
            System.out.println("none visible");
        } else {
            enemySpawnPoints.forEach(spawnPoint -> System.out.println(formatSpawnPoint(spawnPoint)));
        }
        renderSeparator(false);
        System.out.println("Removed");
        if (removedSpawnPoints.isEmpty()) {
            System.out.println("none");
        } else {
            removedSpawnPoints.forEach(spawnPoint -> System.out.println(formatSpawnPoint(spawnPoint)));
        }
        renderSeparator(true);
    }

    private static void renderPlayers() {
        List<Player> friendlyPlayers = GAME_STATE.getPlayers()
                .stream()
                .filter(player -> player.getOwner().equals(BOT_ID))
                .collect(Collectors.toList());
        List<Player> enemyPlayers = GAME_STATE.getPlayers()
                .stream()
                .filter(player -> !player.getOwner().equals(BOT_ID))
                .collect(Collectors.toList());
        Set<Player> removedPlayers = GAME_STATE.getRemovedPlayers();

        System.out.println("Players");
        System.out.println("Friendly");
        friendlyPlayers.forEach(player -> System.out.println(formatPlayer(player)));
        renderSeparator(false);
        System.out.println("Enemy");
        if (enemyPlayers.isEmpty()) {
            System.out.println("none visible");
        } else {
            enemyPlayers.forEach(player -> System.out.println(formatPlayer(player)));
        }
        renderSeparator(false);
        System.out.println("Removed");
        if (removedPlayers.isEmpty()) {
            System.out.println("none");
        } else {
            removedPlayers.forEach(player -> System.out.println(formatPlayer(player)));
        }
        renderSeparator(true);
    }

    private static void renderSeparator(boolean section) {
        if (section) {
            System.out.println("===================================================");
        } else {
            System.out.println("---------------------------------------------------");
        }
    }

    private static String formatCollectable(Collectable collectable) {
        return "TYPE: " + collectable.getType() + formatPosition(collectable.getPosition());
    }

    private static String formatSpawnPoint(SpawnPoint spawnPoint) {
        return "ID: " + spawnPoint.getId() + formatPosition(spawnPoint.getPosition());
    }

    private static String formatPlayer(Player player) {
        return "ID: " + player.getId() + formatPosition(player.getPosition());
    }

    private static String formatPosition(Position position) {
        return " @ (" + position.getX() + ", " + position.getY() + ")";
    }
}
