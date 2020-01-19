package com.keremdundar.game.tradingcard.exception;
/**
 * If someone tries to start game with invalid player this exception will be thrown.
 */
public class InvalidPlayerException extends RuntimeException {

    public InvalidPlayerException(String message) {
        super(message);
    }

}
