package com.keremdundar.game.tradingcard.test;

import com.keremdundar.game.tradingcard.exception.InvalidPlayerException;
import com.keremdundar.game.tradingcard.model.Card;
import com.keremdundar.game.tradingcard.model.Game;
import com.keremdundar.game.tradingcard.model.Player;
import com.keremdundar.game.tradingcard.utility.Constant;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class GameTest {

    @Test
    public void ctor_activePlayerIsNull_throwsInvalidPlayerException(){
        Player opponentPlayer = PlayerFactory.getPlayer();

        assertThatThrownBy(() -> new Game(null, opponentPlayer))
                .isInstanceOf(InvalidPlayerException.class);
    }

    @Test
    public void ctor_opponentPlayerIsNull_throwsInvalidPlayerException(){
        Player activePlayer = PlayerFactory.getPlayer();

        assertThatThrownBy(() -> new Game(activePlayer, null))
                .isInstanceOf(InvalidPlayerException.class);
    }

    @Test
    public void ctor_activeAndOpponentPlayersAreSame_throwsInvalidPlayerException(){
        Player activePlayer = PlayerFactory.getPlayer();

        assertThatThrownBy(() -> new Game(activePlayer, activePlayer))
                .isInstanceOf(InvalidPlayerException.class);
    }

    @Test
    public void initializePlayerHealth_allPlayersHaveDefinedHealthAtTheBeginning_definedHealthInitialized(){
        Player activePlayer = PlayerFactory.getPlayer();
        Player opponentPlayer = PlayerFactory.getPlayer();

        Game game = new Game(activePlayer,opponentPlayer);
        game.initializePlayerHealth();

        assertThat(game.getActivePlayer().getHealth()).isEqualTo(Constant.DEFINED_INITIAL_HEALTH);
        assertThat(game.getOpponentPlayer().getHealth()).isEqualTo(Constant.DEFINED_INITIAL_HEALTH);
    }

    @Test
    public void initializePlayerMana_allPlayersHaveZeroManaAtTheBeginning_zeroManaInitialized(){
        Game game = new Game(PlayerFactory.getPlayer(), PlayerFactory.getPlayer());
        game.initializePlayerMana();

        assertThat(game.getActivePlayer().getManaSlot()).isEqualTo(0);
        assertThat(game.getOpponentPlayer().getManaSlot()).isEqualTo(0);
    }

    @Test
    public void initializeHandCards_allPlayersHaveDefinedCardsInHand_definedNumberOfCardsInitialized(){
        Game game = new Game(PlayerFactory.getPlayer(), PlayerFactory.getPlayer());
        game.initializeDeckCards();
        game.initializeHandCards();

        assertThat(game.getActivePlayer().getHandCardsCount()).isEqualTo(Constant.DEFINED_INITIAL_HAND_CARD_NUMBER);
        assertThat(game.getOpponentPlayer().getHandCardsCount()).isEqualTo(Constant.DEFINED_INITIAL_HAND_CARD_NUMBER);
    }

    @Test
    public void initializeDeckCards_allDeckCardsInitializedWithGivenNumber_deckCardsInitializedWithGivenNumber(){
        Game game = new Game(PlayerFactory.getPlayer(), PlayerFactory.getPlayer());
        game.initializeDeckCards();

        List<Integer> distinctCardDamages = Constant.INITIAL_CARD_LIST.stream().map(card -> card.getDamage()).distinct().collect(Collectors.toList());

        for(int cardDamage : distinctCardDamages){
            assertThat(game.getActivePlayer().getDeckCards().stream().filter(deckCard -> deckCard.getDamage() == cardDamage ).count())
                    .isEqualTo(Constant.INITIAL_CARD_LIST.stream().filter(card -> card.getDamage() == cardDamage).count());
            assertThat(game.getOpponentPlayer().getDeckCards().stream().filter(deckCard -> deckCard.getDamage() == cardDamage ).count())
                    .isEqualTo(Constant.INITIAL_CARD_LIST.stream().filter(card -> card.getDamage() == cardDamage).count());
        }
    }

    @Test
    public void startActivePlayerTurn_activePlayerTurnStarted_activePlayerManaIncreasedByOne(){
        Player activePlayer = PlayerFactory.getPlayer();
        activePlayer.setManaSlot(0);

        Game game = new Game(activePlayer, PlayerFactory.getPlayer());
        game.startActivePlayerTurn();

        assertThat(game.getActivePlayer().getManaSlot()).isEqualTo(1);
    }

    @Test
    public void startActivePlayerTurn_activePlayerTurnStarted_activePlayerManaRefilled(){
        Player activePlayer = PlayerFactory.getPlayer();
        Player opponentPlayer = PlayerFactory.getPlayer();

        activePlayer.setHealth(10);
        activePlayer.setManaSlot(5);
        activePlayer.setRemainedMana(0);
        activePlayer.setDeckCards(new ArrayList<Card>());

        Game game = new Game(activePlayer, opponentPlayer);
        game.startActivePlayerTurn();

        assertThat(activePlayer.getRemainedMana()).isEqualTo(6);
    }

    @Test
    public void startActivePlayerTurn_maximumManaSlotReached_activePlayerManaSlotDoesNotChange(){
        Player activePlayer = PlayerFactory.getPlayer();
        activePlayer.setManaSlot(Constant.MAX_MANA_SIZE);

        Game game = new Game(activePlayer, PlayerFactory.getPlayer());
        game.startActivePlayerTurn();

        assertThat(game.getActivePlayer().getManaSlot()).isEqualTo(Constant.MAX_MANA_SIZE);
    }

    @Test
    public void startActivePlayerTurn_activePlayerHealthIsOneAndDeckCardNotExists_opponentPlayerWins(){
        Player activePlayer = PlayerFactory.getPlayer();
        Player opponentPlayer = PlayerFactory.getPlayer();

        activePlayer.setHealth(1);
        activePlayer.setDeckCards(new ArrayList<Card>());

        Game game = new Game(activePlayer, opponentPlayer);
        game.startActivePlayerTurn();

        assertThat(game.getWinner()).isEqualTo(opponentPlayer);
    }

    @Test
    public void finishActivePlayerTurn_activePlayerShouldBeChanged_activePlayerChanged(){
        Player opponentPlayer = PlayerFactory.getPlayer();

        Game game = new Game(PlayerFactory.getPlayer(),opponentPlayer);
        game.setOpponentPlayer(opponentPlayer);
        game.endActivePlayerTurn();

        assertThat(game.getActivePlayer()).isEqualTo(opponentPlayer);
    }

    @Test
    public void getWinner_allPlayersHaveHealthGreaterThanOne_returnNull(){
        Player activePlayer = PlayerFactory.getPlayer();
        Player opponentPlayer = PlayerFactory.getPlayer();

        activePlayer.setHealth(2);
        opponentPlayer.setHealth(2);
        Game game = new Game(activePlayer,opponentPlayer);

        assertThat(game.getWinner()).isNull();
    }

    @Test
    public void getWinner_activePlayerHaveZeroHealth_returnOpponentPlayer(){
        Player activePlayer = PlayerFactory.getPlayer();
        Player opponentPlayer = PlayerFactory.getPlayer();

        activePlayer.setHealth(0);
        opponentPlayer.setHealth(2);
        Game game = new Game(activePlayer,opponentPlayer);

        assertThat(game.getWinner()).isEqualTo(opponentPlayer);
    }
}