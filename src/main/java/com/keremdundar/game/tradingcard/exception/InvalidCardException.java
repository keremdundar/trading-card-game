package com.keremdundar.game.tradingcard.exception;
/**
 * If player want to play a damage card which he/she has not got it, this exception will be thrown
 */
public class InvalidCardException extends RuntimeException {

    public InvalidCardException(int damageValue) {
        super(String.format("There is no card with %d damage value.", damageValue));
    }

}
