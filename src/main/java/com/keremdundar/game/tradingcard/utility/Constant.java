package com.keremdundar.game.tradingcard.utility;

import com.keremdundar.game.tradingcard.model.Card;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Constant {

    private Constant() {
    }

    public static final int DEFINED_INITIAL_HAND_CARD_NUMBER = 3;
    public static final int DEFINED_INITIAL_HEALTH = 30;
    public static final int MAX_MANA_SIZE = 10;
    public static final int MAXIMUM_HAND_CARD_NUMBER = 5;
    public static final List<Card> INITIAL_CARD_LIST = Stream.of(0,0,1,1,2,2,2,3,3,3,3,4,4,4,5,5,6,6,7,8).map(Card::new).collect(Collectors.toList());
}
