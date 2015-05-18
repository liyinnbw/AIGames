package test;

import static org.junit.Assert.assertEquals;

import java.awt.Point;

import org.junit.Before;
import org.junit.Test;

import wuziqi.GameState;
import wuziqi.GameTree;
import wuziqi.ZobristHash;

public class TestGameTree {
	public GameTree gt;
	GameState g;
	public static int COLS=15;
	public static int ROWS=15;
	@Before
	public void init() {
		g = new GameState(ROWS,COLS,GameState.MAX_PLAYER);
		gt = new GameTree(g,8);
		
	}

	@Test
	public void testNextMove(){
		/*
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
		Point next = gt.nextMove();
		System.out.println(next);
		*/
		
		String s5[][] = {
		   {"000000000000000",
			"000000000000000",
			"000000000000000",
			"000000000000000",
			"000000001000000",
			"000000100000000",
			"000001100000000",
			"000010110000000",
			"000101110100000",
			"000000001000000",
			"000000000000000",
			"000000000000000",
			"000000000000000",
			"000000000000000",
			"000000000000000"},
			
		   {"000000000000000",
			"000000000000000",
			"000000000000000",
			"000000000000000",
			"000000110000000",
			"000010000000000",
			"000000011100000",
			"000000001000000",
			"000010001000000",
			"001000100000000",
			"000000000100000",
			"000000000000000",
			"000000000000000",
			"000000000000000",
			"000000000000000"}
				
		};
		int state5[][]=new int[2][ROWS];
		for(int i=0; i<2; i++){
			for(int j=0; j<ROWS; j++){
				state5[i][j]=Integer.parseInt(s5[i][j],2);
			}
		}
		g.setGameState(state5);
		g.setCurrSide(GameState.MIN_PLAYER);
		System.out.println(g);
		System.out.println(g.evaluatePos(7, 5, GameState.MAX_PLAYER, GameState.ALL_DIRECTION));
		System.out.println(g.evaluatePos(5, 7, GameState.MAX_PLAYER, GameState.ALL_DIRECTION));
		System.out.println(g.nextPossibleMoves());
		System.out.println(gt.nextMove());
	}
	
	/*
	@Test
	public void testReduceConsideration(){
		gt.reduceConsideration(gt.getCurrState().nextPossibleStates(),GameState.MAX_PLAYER);
		gt.reduceConsideration(gt.getCurrState().nextPossibleStates(),GameState.MIN_PLAYER);
	}
	*/
}
