package test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import wuziqi.GameState;
import wuziqi.GameTree;
import wuziqi.ZobristHash;

public class TestGameTree {
	public GameTree gt;
	public static int COLS=15;
	public static int ROWS=15;
	@Before
	public void init() {
		gt = new GameTree(new GameState(ROWS,COLS,GameState.MAX_PLAYER),4);
	}

	@Test
	public void testNextMove(){
		GameState g = gt.getCurrState();
		g.addPiece(7, 8);
		g.addPiece(7, 7);
		g.addPiece(6, 8);
		g.addPiece(8, 8);
		g.addPiece(6, 6);
		g.addPiece(6, 7);
		g.addPiece(5, 7);
		g.addPiece(5, 5);
		g.addPiece(7, 5);
		System.out.println(gt.getCurrState());
		GameState next = gt.nextMove();
		System.out.println(next);
	}
}
