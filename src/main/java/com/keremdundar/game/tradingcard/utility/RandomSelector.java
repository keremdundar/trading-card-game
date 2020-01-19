package com.keremdundar.game.tradingcard.utility;

import java.util.List;

public interface RandomSelector<T> {

    T getRandom(List<T> list);

}
