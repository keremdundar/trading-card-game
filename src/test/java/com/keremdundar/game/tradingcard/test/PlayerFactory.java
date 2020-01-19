package com.keremdundar.game.tradingcard.test;

import com.keremdundar.game.tradingcard.model.Player;
import com.keremdundar.game.tradingcard.utility.RandomCardSelector;
import com.keremdundar.game.tradingcard.utility.RandomSelector;

public class PlayerFactory {

    private PlayerFactory(){
    }

    public static Player getPlayer() {
        return getPlayer(new RandomCardSelector());
    }

    public static Player getPlayer(RandomSelector selector) {
        return new Player(selector);
    }

}
