package com.keremdundar.game.tradingcard.test;

import com.keremdundar.game.tradingcard.exception.InsufficientManaException;
import com.keremdundar.game.tradingcard.exception.InvalidCardException;
import com.keremdundar.game.tradingcard.model.Card;
import com.keremdundar.game.tradingcard.model.Game;
import com.keremdundar.game.tradingcard.model.Player;
import com.keremdundar.game.tradingcard.utility.Constant;
import com.keremdundar.game.tradingcard.utility.RandomSelector;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class PlayerTest
{
    @Mock
    private RandomSelector<Card> randomSelector;
    private Player activePlayer;

    @Before
    public void setup(){
        activePlayer = new Player(randomSelector);
    }

    @Test
    public void drawCard_activePlayerDrawCard_handCardsIncreaseByOneAndDeckCardsDecreasedByOne(){
        Player player = PlayerFactory.getPlayer();

        List<Card> deckCards = player.getDeckCards();
        deckCards.add(new Card(1));
        deckCards.add(new Card(2));
        deckCards.add(new Card(3));

        player.drawCard();

        assertThat(player.getHandCardsCount()).isEqualTo(1);
        assertThat(player.getDeckCardsCount()).isEqualTo(2);
    }

    @Test
    public void drawCard_activePlayerHasNoCardInDeck_activePlayerHealthDecreasedByOne(){
        int health = 30;
        activePlayer.setHealth(health);
        activePlayer.drawCard();

        assertThat(activePlayer.getHealth()).isEqualTo(health-1);
    }

    @Test
    public void playCard_activePlayerPlayCard_activePlayerHandCardsNumberDecreasedByOne(){
        activePlayer.setRemainedMana(8);
        activePlayer.setHandCards(Stream.of(1,2).map(Card::new).collect(Collectors.toList()));
        activePlayer.playCard(1, PlayerFactory.getPlayer());

        assertThat(activePlayer.getHandCardsCount()).isEqualTo(1);
    }

    @Test
    public void playCard_activePlayerPlayCard_activePlayerManaDecreasedByPlayedCardDamage(){
        activePlayer.setRemainedMana(8);
        activePlayer.setHandCards(Stream.of(1).map(Card::new).collect(Collectors.toList()));
        activePlayer.playCard(1, PlayerFactory.getPlayer());

        assertThat(activePlayer.getRemainedMana()).isEqualTo(7);
    }

    @Test
    public void playCard_activePlayerPlayCard_opponentPlayerHealthDecreasedByPlayedCardDamage(){
        Player opponentPlayer = PlayerFactory.getPlayer();
        opponentPlayer.setHealth(30);
        activePlayer.setRemainedMana(1);
        activePlayer.setHandCards(Stream.of(1).map(Card::new).collect(Collectors.toList()));
        activePlayer.playCard(1, opponentPlayer);

        assertThat(opponentPlayer.getHealth()).isEqualTo(29);
    }

    @Test
    public void playCard_activePlayerPlayCardNotOwned_throwsInvalidCardException(){
        List<Card> handCards = activePlayer.getHandCards();
        handCards.add(new Card(1));

        assertThatThrownBy(() -> activePlayer.playCard(2, PlayerFactory.getPlayer()))
                .isInstanceOf(InvalidCardException.class);
    }

    @Test
    public void playCard_activePlayerPlayCardMoreThanManaSlot_throwsInsufficientManaException(){
        Player opponentPlayer = PlayerFactory.getPlayer();

        List<Card> handCards = activePlayer.getHandCards();
        handCards.add(new Card(1));
        handCards.add(new Card(3));
        handCards.add(new Card(5));
        handCards.add(new Card(5));

        activePlayer.setRemainedMana(10);
        activePlayer.playCard(5,opponentPlayer);
        activePlayer.playCard(5,opponentPlayer);

        assertThatThrownBy(() -> activePlayer.playCard(3,opponentPlayer))
                .isInstanceOf(InsufficientManaException.class);
    }

    @Test
    public void playCard_opponentPlayerHealthDropToOrEqualZero_activePlayerWinsGame(){
        Player opponentPlayer = PlayerFactory.getPlayer();
        opponentPlayer.setHealth(1);

        activePlayer.setHealth(9);
        activePlayer.setRemainedMana(1);
        activePlayer.setHandCards(Stream.of(1).map(Card::new).collect(Collectors.toList()));
        activePlayer.playCard(1, opponentPlayer);
        Game game = new Game(activePlayer,opponentPlayer);

        assertThat(game.getWinner()).isEqualTo(activePlayer);
    }

    @Test
    public void drawCard_activePlayerHasMaximumCardNumber_discardSelectedDeckCard(){
        List<Card> handCards = activePlayer.getHandCards();
        List<Card> deckCards = activePlayer.getDeckCards();
        handCards.add(new Card(1));
        handCards.add(new Card(1));
        handCards.add(new Card(2));
        handCards.add(new Card(2));
        handCards.add(new Card(3));

        Card cardToReturn = new Card(5);
        deckCards.add(cardToReturn);
        deckCards.add(new Card(7));

        given(randomSelector.getRandom(ArgumentMatchers.anyList())).willReturn(cardToReturn);

        activePlayer.drawCard();

        assertThat(activePlayer.getDeckCards()).hasSize(1).doesNotContain(cardToReturn);
        assertThat(activePlayer.getHandCardsCount()).isEqualTo(Constant.MAXIMUM_HAND_CARD_NUMBER);
    }

    @Test
    public void canPlayCard_activePlayerHasNoCardLessThanRemainedMana_returnFalse(){
        List<Card> handCards = activePlayer.getHandCards();
        handCards.add(new Card(4));
        handCards.add(new Card(5));

        activePlayer.setRemainedMana(3);

        assertThat(activePlayer.canPlayCard()).isFalse();
    }

    @Test
    public void canPlayCard_activePlayerHasCardLessThanRemainedMana_returnTrue(){
        List<Card> handCards = activePlayer.getHandCards();
        handCards.add(new Card(4));
        handCards.add(new Card(5));

        activePlayer.setRemainedMana(4);

        assertThat(activePlayer.canPlayCard()).isTrue();
    }

    @Test
    public void canPlayCard_activePlayerHasNoCard_returnFalse(){
        List<Card> handCards = activePlayer.getHandCards();
        activePlayer.setRemainedMana(4);

        assertThat(activePlayer.canPlayCard()).isFalse();
    }

}
