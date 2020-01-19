package com.keremdundar.game.tradingcard.model;

import com.keremdundar.game.tradingcard.exception.InsufficientManaException;
import com.keremdundar.game.tradingcard.exception.InvalidCardException;
import com.keremdundar.game.tradingcard.utility.Constant;
import com.keremdundar.game.tradingcard.utility.RandomSelector;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Player {

    private int manaSlot;
    private int remainedMana;
    private int health;
    private String name;
    private final RandomSelector<Card> randomSelector;

    private List<Card> deckCards = new ArrayList<>();
    private List<Card> handCards = new ArrayList<>();

    public Player(RandomSelector<Card> randomSelector) {
        this.randomSelector = randomSelector;
    }

    public Player(RandomSelector<Card> randomSelector, String name){
        this.randomSelector = randomSelector;
        this.name = name;
    }

    public int getManaSlot() {
        return manaSlot;
    }

    public void setManaSlot(int manaSlot) {
        this.manaSlot = manaSlot;
    }

    public int getRemainedMana() {
        return remainedMana;
    }

    public void setRemainedMana(int remainedMana) {
        this.remainedMana = remainedMana;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public List<Card> getDeckCards() {
        return deckCards;
    }

    public void setDeckCards(List<Card> deckCards) {
        this.deckCards = deckCards;
    }

    public List<Card> getHandCards() {
        return handCards;
    }

    public void setHandCards(List<Card> handCards) {
        this.handCards = handCards;
    }

    public void drawCard() {
        if(getDeckCardsCount() == 0)
        {
            health--;
        }else
        {
           Card selectedCard = randomSelector.getRandom(getDeckCards());
           deckCards.remove(selectedCard);
           if(getHandCardsCount() < Constant.MAXIMUM_HAND_CARD_NUMBER)
           {
               handCards.add(selectedCard);
           }
        }
    }

    public int getHandCardsCount() {
        return getHandCards().size();
    }

    public int getDeckCardsCount() {
        return getDeckCards().size();
    }

    public void playCard(int cardDamageValue,Player opponentPlayer) {
        Optional<Card> card = handCards.stream().filter(c -> c.getDamage() == cardDamageValue).findFirst();

        if(!card.isPresent())
        {
            throw new InvalidCardException(cardDamageValue);
        }

        if(remainedMana < cardDamageValue)
        {
            throw new InsufficientManaException();
        }

        handCards.remove(card.get());
        remainedMana -= cardDamageValue;
        opponentPlayer.setHealth(opponentPlayer.getHealth() - cardDamageValue);
    }

    public boolean isHealthBelowOne(){
        return health < 1;
    }

    public void increaseManaSlot(int manaSlotCount)
    {
        manaSlot += manaSlotCount;
    }

    public boolean canPlayCard(){
        Optional<Card> card = getHandCards().stream().min(Comparator.comparing(handCard -> handCard.getDamage()));
        return card.isPresent() && card.get().getDamage() <= remainedMana;
    }

    public String getCardsSummary(List<Card> list){
        return list.stream().map(deckCard -> String.valueOf(deckCard.getDamage())).collect(Collectors.joining("-"));
    }

    @Override
    public String toString() {
        return  "PLAYER - " + name + "\n" +
                "MANA SLOT - " + manaSlot + "\n" +
                "HEALTH - " + health + "\n" +
                "DECK CARDS - " + getCardsSummary(deckCards) + "\n" +
                "HAND CARDS - " + getCardsSummary(handCards);
    }
}
