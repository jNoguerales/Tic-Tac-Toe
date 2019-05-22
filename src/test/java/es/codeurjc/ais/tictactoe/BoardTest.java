package es.codeurjc.ais.tictactoe;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;


public class BoardTest {
	private Board tablero;
	
	public void mark(String label, int cell) {
		tablero.getCell(cell).value = label;
		tablero.getCell(cell).active = false;
	}
	
	@Before
	public void setUp() {
		this.tablero = new Board();
		this.tablero.enableAll();
		assertFalse(tablero.checkDraw());
		assertNull(tablero.getCellsIfWinner("X"));
		assertNull(tablero.getCellsIfWinner("O"));
		mark("X",4);
		mark("O",2);
		mark("X",0);
		assertFalse(tablero.checkDraw());
		assertNull(tablero.getCellsIfWinner("X"));
		assertNull(tablero.getCellsIfWinner("O"));
	}
	
	
	@Test
	public void ganaPrimero() {
		mark("O",5);
		mark("X",8);
		assertFalse(tablero.checkDraw());
		assertArrayEquals(new int[] {0,4,8}, tablero.getCellsIfWinner("X"));
		assertNull(tablero.getCellsIfWinner("O"));
	}
	
	@Test
	public void pierdePrimero() {
		mark("O",8);
		mark("X",3);
		mark("O",5);
		assertFalse(tablero.checkDraw());
		assertNull(tablero.getCellsIfWinner("X"));
		assertArrayEquals(new int[] {2,5,8}, tablero.getCellsIfWinner("O"));
	}
	
	@Test
	public void empate() {
		mark("O",8);
		mark("X",6);
		mark("O",1);
		mark("X",5);
		mark("O",3);
		mark("X",7);
		assertTrue(tablero.checkDraw());
		assertNull(tablero.getCellsIfWinner("X"));
		assertNull(tablero.getCellsIfWinner("O"));
	}
}
