package com.keremdundar.game.tradingcard.test;

import com.keremdundar.game.tradingcard.model.Card;
import com.keremdundar.game.tradingcard.model.Game;
import com.keremdundar.game.tradingcard.model.Player;
import com.keremdundar.game.tradingcard.utility.GameEngine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class GameEngineTest {
    @Mock
    private Game game;
    private GameEngine gameEngine;

    @Before
    public void setup(){
        gameEngine = new GameEngine(game);
    }

    @Test
    public void isThereAWinner_gameHasWinner_returnTrue(){
        Player player = PlayerFactory.getPlayer();
        given(game.getWinner()).willReturn(player);

        assertThat(gameEngine.isThereAWinner()).isTrue();
    }

    @Test
    public void isThereAWinner_gameHasNoWinner_returnFalse(){
        given(game.getWinner()).willReturn(null);
        assertThat(gameEngine.isThereAWinner()).isFalse();
    }

    @Test
    public void currentPlayerCanPlayCard_activePlayerCantPlayCard_returnFalse(){
        Player player = Mockito.spy(PlayerFactory.getPlayer());
        doReturn(false).when(player).canPlayCard();

        given(game.getActivePlayer()).willReturn(player);

        assertThat(gameEngine.currentPlayerCanPlayCard()).isFalse();
    }

    @Test
    public void currentPlayerCanPlayCard_activePlayerCanPlayCard_returnTrue(){
        Player player = Mockito.spy(PlayerFactory.getPlayer());
        doReturn(true).when(player).canPlayCard();

        given(game.getActivePlayer()).willReturn(player);

        assertThat(gameEngine.currentPlayerCanPlayCard()).isTrue();
    }

    @Test
    public void play_currentPlayerPlayCard_opponentHealthDecreaseByDamage(){
        Player activePlayer = PlayerFactory.getPlayer();
        Player opponentPlayer = PlayerFactory.getPlayer();

        activePlayer.setHandCards(Stream.of(5).map(Card::new).collect(Collectors.toList()));
        activePlayer.setRemainedMana(5);

        opponentPlayer.setHealth(30);

        given(game.getActivePlayer()).willReturn(activePlayer);
        given(game.getOpponentPlayer()).willReturn(opponentPlayer);

        gameEngine.play(5);

        assertThat(gameEngine.getOpponentHealth()).isEqualTo(25);
    }

}