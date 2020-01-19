package com.keremdundar.game.tradingcard.utility;

import com.keremdundar.game.tradingcard.model.Game;
import com.keremdundar.game.tradingcard.model.Player;

public class GameBootstrapper {

    private final GameEngine gameEngine;

    public GameBootstrapper(RandomSelector selector,String firstPlayerName, String secondPlayerName) {
        Player activePlayer = new Player(selector,firstPlayerName);
        Player opponentPlayer = new Player(selector,secondPlayerName);

        Game game = new Game(activePlayer,opponentPlayer);
        game.initalize();

        gameEngine = new GameEngine(game);
    }

    public GameEngine getGameEngine(){
        return gameEngine;
    }
}
