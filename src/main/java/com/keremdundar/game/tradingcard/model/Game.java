package com.keremdundar.game.tradingcard.model;

import com.keremdundar.game.tradingcard.exception.InvalidPlayerException;
import com.keremdundar.game.tradingcard.utility.Constant;
import com.keremdundar.game.tradingcard.utility.GameInitalizer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Game {

    private Player activePlayer;
    private Player opponentPlayer;
    private final List<GameInitalizer> gameInitalizers = new ArrayList<>();

    public Game(Player activePlayer, Player opponentPlayer) {
        if(activePlayer == null || opponentPlayer == null)
        {
            throw new InvalidPlayerException("Players cannot be null");
        }

        if(activePlayer == opponentPlayer)
        {
            throw new InvalidPlayerException("Players cannot be same");
        }

        gameInitalizers.add(() -> initializePlayerMana());
        gameInitalizers.add(() -> initializePlayerHealth());
        gameInitalizers.add(() -> initializeDeckCards());
        gameInitalizers.add(() -> initializeHandCards());

        this.activePlayer = activePlayer;
        this.opponentPlayer = opponentPlayer;
    }

    public Player getActivePlayer() {
        return activePlayer;
    }

    public Player getOpponentPlayer() {
        return opponentPlayer;
    }

    public void setOpponentPlayer(Player opponentPlayer) {
        this.opponentPlayer = opponentPlayer;
    }

    public void initializePlayerMana() {
        activePlayer.setManaSlot(0);
        opponentPlayer.setManaSlot(0);
    }

    public void initializePlayerHealth() {
        activePlayer.setHealth(Constant.DEFINED_INITIAL_HEALTH);
        opponentPlayer.setHealth(Constant.DEFINED_INITIAL_HEALTH);
    }

    public void initializeDeckCards() {
        activePlayer.setDeckCards(new ArrayList<>(Constant.INITIAL_CARD_LIST));
        opponentPlayer.setDeckCards(new ArrayList<>(Constant.INITIAL_CARD_LIST));
    }

    public void initializeHandCards() {
        IntStream.range(0, Constant.DEFINED_INITIAL_HAND_CARD_NUMBER)
                .forEach
                        (i ->
                                {
                                    activePlayer.drawCard();
                                    opponentPlayer.drawCard();
                                }
                        );
    }

    public void initalize(){
        this.gameInitalizers.forEach(i -> i.init());
    }

    public void startActivePlayerTurn() {
        if(activePlayer.getManaSlot() < Constant.MAX_MANA_SIZE)
        {
            activePlayer.increaseManaSlot(1);
        }
        activePlayer.setRemainedMana(activePlayer.getManaSlot());
        activePlayer.drawCard();
    }

    public void endActivePlayerTurn() {
        Player activePlayerToChange = getActivePlayer();
        activePlayer = opponentPlayer;
        opponentPlayer = activePlayerToChange;
    }

    public Player getWinner() {
        if(activePlayer.isHealthBelowOne())
        {
            return opponentPlayer;
        }

        if(opponentPlayer.isHealthBelowOne())
        {
            return activePlayer;
        }

        return null;
    }

}
