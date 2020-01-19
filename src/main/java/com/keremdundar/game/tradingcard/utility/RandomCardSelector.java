package com.keremdundar.game.tradingcard.utility;

import java.util.List;
import java.util.Random;

public class RandomCardSelector<Card> implements RandomSelector {

    private final Random random = new Random();

    @Override
    public Object getRandom(List list) {
        int randomIndex = random.nextInt(list.size());
        return list.get(randomIndex);
    }
}
