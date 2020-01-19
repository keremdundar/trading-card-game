package com.keremdundar.game.tradingcard.utility;

import com.keremdundar.game.tradingcard.model.Game;

public class GameEngine {

    private final Game game;

    public GameEngine(Game game) {
        this.game = game;
    }

    public boolean isThereAWinner(){
        return game.getWinner() != null;
    }

    public void startTurn(){
        game.startActivePlayerTurn();
    }

    public boolean currentPlayerCanPlayCard(){
        return game.getActivePlayer().canPlayCard();
    }

    public String currentPlayer(){
        return game.getActivePlayer().toString();
    }

    public String currentPlayerCardsInHand(){
        return game.getActivePlayer().getCardsSummary(game.getActivePlayer().getHandCards());
    }

    public void play(int damage){
        game.getActivePlayer().playCard(damage,game.getOpponentPlayer());
    }

    public int getOpponentHealth(){
        return game.getOpponentPlayer().getHealth();
    }

    public int getRemainedMana(){
        return game.getActivePlayer().getRemainedMana();
    }

    public void endTurn(){
        game.endActivePlayerTurn();
    }

    public String winnerPlayer(){
        return game.getWinner().toString();
    }

}
