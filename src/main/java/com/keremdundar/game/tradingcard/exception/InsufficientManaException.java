package com.keremdundar.game.tradingcard.exception;
/**
 * If when play card which damage is greater then mana slot this exception throwns
 */
public class InsufficientManaException extends RuntimeException {

    public InsufficientManaException() {
        super("Insufficient mana to play card.");
    }

}
