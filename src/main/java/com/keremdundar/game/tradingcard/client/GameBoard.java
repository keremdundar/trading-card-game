package com.keremdundar.game.tradingcard.client;

import com.keremdundar.game.tradingcard.exception.InsufficientManaException;
import com.keremdundar.game.tradingcard.exception.InvalidCardException;
import com.keremdundar.game.tradingcard.utility.GameBootstrapper;
import com.keremdundar.game.tradingcard.utility.GameEngine;
import com.keremdundar.game.tradingcard.utility.RandomCardSelector;

import java.util.Scanner;

public class GameBoard {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String PASS = "P";
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter first player name: ");
        String firstPlayerName = scanner.nextLine();
        System.out.println("Please enter second player name: ");
        String secondPlayerName = scanner.nextLine();
        System.out.println("Game initializing...");
        GameEngine gameEngine = new GameBootstrapper(new RandomCardSelector(),firstPlayerName,secondPlayerName).getGameEngine();

        while(!gameEngine.isThereAWinner()){
            gameEngine.startTurn();
            if(gameEngine.isThereAWinner()) break;
            System.out.println(ANSI_RED + "Active Player: " + gameEngine.currentPlayer() + ANSI_RESET);
            while(gameEngine.currentPlayerCanPlayCard()){
                System.out.println("You can play a card which damage is below or equal remained mana or you can pass this turn.");
                System.out.println("Enter damage of card you want to play or press 'P' for pass. YOUR HAND: " + gameEngine.currentPlayerCardsInHand());
                String move = scanner.nextLine();
                if(move.equals(PASS)){
                    break;
                }else{
                    try{
                        int damage = Integer.parseInt(move);
                        gameEngine.play(damage);
                        System.out.println("Opponent player health decreased to: " + gameEngine.getOpponentHealth());
                        System.out.println("Your remained mana decreased to: " + gameEngine.getRemainedMana());
                        if(gameEngine.isThereAWinner()) break;
                    }catch (NumberFormatException ex){
                        System.out.println("You must enter valid number.");
                    }catch (InvalidCardException ex){
                        System.out.println(ex.getMessage());
                    }catch (InsufficientManaException ex){
                        System.out.println(ex.getMessage());
                    }
                }
            }
            gameEngine.endTurn();
        }

        System.out.println(ANSI_RED + "Winner is: " + gameEngine.winnerPlayer() + ANSI_RESET);

    }

}
