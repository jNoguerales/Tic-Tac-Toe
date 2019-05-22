package es.codeurjc.ais.tictactoe;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import es.codeurjc.ais.tictactoe.TicTacToeGame.EventType;
import es.codeurjc.ais.tictactoe.TicTacToeGame.WinnerValue;

import static org.mockito.Mockito.*;
import org.mockito.ArgumentCaptor;

public class TicTacToeGameTest {
	TicTacToeGame game;
	Connection c1;
	Connection c2;
	Player p1;
	Player p2;
	
	@Before
	public void setUp() {
		game = new TicTacToeGame();
		c1 = mock(Connection.class);
		c2 = mock(Connection.class);
		this.game.addConnection(c1);
		this.game.addConnection(c2);
		p1 = new Player(1,"X","Jaime");
		p2 = new Player(2,"O","Bea");
		this.game.addPlayer(p1);
		
		//Comprobamos que se haya añadido al jugador 1 y que se le envie en el evento
		assertTrue(this.game.getPlayers().contains(p1));
		verify(c1,times(1)).sendEvent(EventType.JOIN_GAME, this.game.getPlayers());
		verify(c2,times(1)).sendEvent(EventType.JOIN_GAME, this.game.getPlayers());
		this.game.addPlayer(p2);
		
		//Comprobamos que los jugadores se hayan introducido en la partida y se hayan enviado en los eventos
		assertTrue(this.game.getPlayers().contains(p1));
		assertTrue(this.game.getPlayers().contains(p2));
		verify(c1,times(2)).sendEvent(EventType.JOIN_GAME, this.game.getPlayers());
		verify(c2,times(2)).sendEvent(EventType.JOIN_GAME, this.game.getPlayers());
		
		//Comprobamos que sea el turno del primer jugador
		verify(c1,times(1)).sendEvent(EventType.SET_TURN, this.game.getPlayers().get(0));
		verify(c2,times(1)).sendEvent(EventType.SET_TURN, this.game.getPlayers().get(0));
		verify(c1,times(0)).sendEvent(EventType.SET_TURN, this.game.getPlayers().get(1));
		verify(c2,times(0)).sendEvent(EventType.SET_TURN, this.game.getPlayers().get(1));
		
		//Se resetea el registro de llamadas
		reset(c1);
		reset(c2);
		
		this.game.mark(4);
		//Comprobamos que el turno le pertenezca al segundo jugador
		verify(c1,times(0)).sendEvent(EventType.SET_TURN, this.game.getPlayers().get(0));
		verify(c2,times(1)).sendEvent(EventType.SET_TURN, this.game.getPlayers().get(1));
		
		this.game.mark(2);
		this.game.mark(0);
		reset(c1);
		reset(c2);
	}

	@Test
	public void ganaPrimero() {
		this.game.mark(5);
		
		//Comprobamos que sea de nuevo el turno del primer jugador
		verify(c1,times(1)).sendEvent(EventType.SET_TURN, this.game.getPlayers().get(0));
		verify(c2,times(0)).sendEvent(EventType.SET_TURN, this.game.getPlayers().get(1));

		ArgumentCaptor<WinnerValue> argument = ArgumentCaptor.forClass(WinnerValue.class);
		verify(c1,times(0)).sendEvent(eq(EventType.GAME_OVER), argument.capture());
		verify(c2,times(0)).sendEvent(eq(EventType.GAME_OVER), argument.capture());
		
		this.game.mark(8);
		
		//Comprobamos que ha ganado el jugador 1 en las posiciones establecidas
		verify(c1,times(1)).sendEvent(eq(EventType.GAME_OVER), argument.capture());
		verify(c2,times(1)).sendEvent(eq(EventType.GAME_OVER), argument.capture());
		Player ganador = argument.getValue().player;
		int [] posicionesGanadoras = argument.getValue().pos;
		
		int [] pos ={0,4,8};
		
		assertEquals(ganador,p1);
		assertArrayEquals(pos,posicionesGanadoras);
	}
	
	@Test
	public void pierdePrimero() {
		this.game.mark(8);
		
		//Comprobamos que sea de nuevo el turno del primer jugador
		verify(c1,times(1)).sendEvent(EventType.SET_TURN, this.game.getPlayers().get(0));
		verify(c2,times(0)).sendEvent(EventType.SET_TURN, this.game.getPlayers().get(1));

		this.game.mark(3);
		ArgumentCaptor<WinnerValue> argument = ArgumentCaptor.forClass(WinnerValue.class);
		verify(c1,times(0)).sendEvent(eq(EventType.GAME_OVER), argument.capture());
		verify(c2,times(0)).sendEvent(eq(EventType.GAME_OVER), argument.capture());

		this.game.mark(5);
		
		//Comprobamos que ha ganado el jugador 1 en las posiciones establecidas
		verify(c1,times(1)).sendEvent(eq(EventType.GAME_OVER), argument.capture());
		verify(c2,times(1)).sendEvent(eq(EventType.GAME_OVER), argument.capture());
		Player ganador = argument.getValue().player;
		int [] posicionesGanadoras = argument.getValue().pos;
		
		int [] pos ={2,5,8};
		
		assertEquals(ganador,p2);
		assertArrayEquals(pos,posicionesGanadoras);
	}
	
	@Test
	public void empate() {
		this.game.mark(8);
		
		//Comprobamos que sea de nuevo el turno del primer jugador
		verify(c1,times(1)).sendEvent(EventType.SET_TURN, this.game.getPlayers().get(0));
		verify(c2,times(0)).sendEvent(EventType.SET_TURN, this.game.getPlayers().get(1));
		
		this.game.mark(6);
		this.game.mark(1);
		this.game.mark(5);
		this.game.mark(3);
		this.game.mark(7);
		
		//Comprobamos que efectivamente se ha producido un empate
		//Por lo tanto, el argumento devuelto con el evento GAME_OVER sera vacio
		ArgumentCaptor<WinnerValue> argument = ArgumentCaptor.forClass(WinnerValue.class);
		verify(c1,times(1)).sendEvent(eq(EventType.GAME_OVER), argument.capture());
		assertNull(argument.getValue());
		verify(c2,times(1)).sendEvent(eq(EventType.GAME_OVER), argument.capture());
		assertNull(argument.getValue());
		
		assertTrue(this.game.checkDraw());
		assertFalse(this.game.checkWinner().win);
	}
}
