package test;

import static org.junit.Assert.*;

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
	public static int TIMELIM = 3000; //miliseconds
	@Before
	public void init() {
		g = new GameState(ROWS,COLS,GameState.MAX_PLAYER);
		gt = new GameTree(g,TIMELIM);
		
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
		
		String s6[][] = {
		   {"000000100000000",
			"000100001000000",
			"000011010000000",
			"000000010100000",
			"000010010000000",
			"000001101000000",
			"000100010000000",
			"000000100100000",
			"000010010000000",
			"000000000000000",
			"000000000000000",
			"000000000000000",
			"000000000000000",
			"000000000000000",
			"000000000000000"},
			
		   {"000001000000000",
			"000011110000000",
			"000000100000000",
			"000001101000000",
			"000001101100000",
			"000010010000000",
			"000000101000000",
			"000001000000000",
			"000000000000000",
			"000000000000000",
			"000000000000000",
			"000000000000000",
			"000000000000000",
			"000000000000000",
			"000000000000000"}
				
		};
		int state6[][]=new int[2][ROWS];
		for(int i=0; i<2; i++){
			for(int j=0; j<ROWS; j++){
				state6[i][j]=Integer.parseInt(s6[i][j],2);
			}
		}
		g.setGameState(state6);
		g.setCurrSide(GameState.MIN_PLAYER);
		//System.out.println(g);
		//System.out.println(g.evaluatePos(7, 5, GameState.MAX_PLAYER, GameState.ALL_DIRECTION));
		//System.out.println(g.evaluatePos(7, 9, GameState.MAX_PLAYER, GameState.ALL_DIRECTION));
		System.out.println(g.nextPossibleMoves());
		next = gt.nextMove();
		assertTrue(next.equals(new Point(5,8)));
		
		
		
		String s7[][] = {
		   {"000000000000000",
			"000000000000000",
			"000000000000000",
			"000010000000000",
			"000000001000000",
			"000000000000000",
			"000000100001000",
			"000000110000000",
			"000000110100000",
			"000000101000000",
			"000000000000000",
			"000000000000000",
			"000000000000000",
			"000000000000000",
			"000000000000000"},
			
		   {"000000000000000",
			"000000000000000",
			"000000000000000",
			"000000000000000",
			"000001000000000",
			"000000101000000",
			"000000011110000",
			"000000001000000",
			"000000001000000",
			"000000000000000",
			"000000100000000",
			"000000000000000",
			"000000000000000",
			"000000000000000",
			"000000000000000"}
				
		};
		int state7[][]=new int[2][ROWS];
		for(int i=0; i<2; i++){
			for(int j=0; j<ROWS; j++){
				state7[i][j]=Integer.parseInt(s7[i][j],2);
			}
		}
		g.setGameState(state7);
		g.setCurrSide(GameState.MIN_PLAYER);
		System.out.println(g);
		System.out.println(g.nextPossibleMoves());
		next = gt.nextMove();
		System.out.println(next);
		g.addPiece(10,7);
		g.addPiece(5, 6);
		next = gt.nextMove();
		System.out.println(next);
		assertTrue(next.equals(new Point(4,5)));
		
		
		g = new GameState(ROWS,COLS,GameState.MAX_PLAYER);
		g.addPiece(6, 7);
		g.addPiece(6, 6);
		g.addPiece(5, 8);
		g.addPiece(7, 7);
		g.addPiece(8, 8);
		g.addPiece(5, 5);
		g.addPiece(4, 4);
		g.addPiece(8, 6);
		g.addPiece(7, 5);
		g.addPiece(7, 6);
		g.addPiece(9, 6);
		g.addPiece(4, 6);
		g.addPiece(5, 6);
		g.addPiece(6, 8);
		g.addPiece(5, 9);
		g.addPiece(5, 7);
		g.addPiece(7, 9);
		g.addPiece(3, 7);
		g.addPiece(2, 8);
		g.addPiece(3, 5);
		g.addPiece(2, 4);
		g.addPiece(4, 5);
		g.addPiece(2, 5);
		g.addPiece(3, 6);
		g.addPiece(3, 8);
		g.addPiece(2, 7);
		g.addPiece(5, 4);
		g.addPiece(3, 4);
		g.addPiece(3, 3);
		g.addPiece(2, 3);
		g.addPiece(7, 4);
		g.addPiece(4, 7);
		g.addPiece(1, 7);
		g.addPiece(9, 5);
		g.addPiece(10, 4);
		g.addPiece(7, 3);
		g.addPiece(6, 4);
		g.addPiece(8, 4);
		g.addPiece(6, 2);
		g.addPiece(4, 8);
		g.addPiece(4, 9);
		g.addPiece(3, 9);
		g.addPiece(2, 10);
		g.addPiece(10, 6);
		g.addPiece(11, 7);
		//g.addPiece(1, 5);
		//g.addPiece(6, 3);
		//g.addPiece(2, 6);
		//g.addPiece(0, 4);
		//g.addPiece(1, 2);
		//g.addPiece(0, 1);
		//g.addPiece(1, 6);
		//g.addPiece(0, 6);
		//g.addPiece(1, 4);
		//g.addPiece(1, 3);
		//g.addPiece(1, 8);
		//g.addPiece(0, 9);
		System.out.println(g);
		System.out.println(g.getCurrSide());
		System.out.println(g.nextPossibleMoves());
		System.out.println(g.evaluatePos(3, 6, GameState.MAX_PLAYER, GameState.ALL_DIRECTION));
		System.out.println(g.evaluatePos(1, 5, GameState.MAX_PLAYER, GameState.ALL_DIRECTION));
		System.out.println(g.evaluatePos(8, 5, GameState.MAX_PLAYER, GameState.ALL_DIRECTION));
		gt.setCurrState(g);
		System.out.println(gt.nextMove());
		*/
		
		/*
		g = new GameState(ROWS,COLS,GameState.MAX_PLAYER);
		g.addPiece(9, 7);
		g.addPiece(9, 6);
		g.addPiece(8, 8);
		g.addPiece(8, 9);
		g.addPiece(7, 9);
		g.addPiece(10, 6);
		g.addPiece(6, 10);
		g.addPiece(5, 11);
		g.addPiece(7, 7);
		g.addPiece(11, 6);
		g.addPiece(12, 6);
		g.addPiece(9, 8);
		g.addPiece(10, 7);
		g.addPiece(7, 6);
		g.addPiece(8, 6);
		g.addPiece(8, 7);
		g.addPiece(6, 5);
		//g.addPiece(1, 5);
		//g.addPiece(6, 3);
		//g.addPiece(2, 6);
		//g.addPiece(0, 4);
		//g.addPiece(1, 2);
		//g.addPiece(0, 1);
		//g.addPiece(1, 6);
		//g.addPiece(0, 6);
		//g.addPiece(1, 4);
		//g.addPiece(1, 3);
		//g.addPiece(1, 8);
		//g.addPiece(0, 9);
		System.out.println(g);
		System.out.println(g.getCurrSide());
		System.out.println(g.nextPossibleMoves());
		gt.setCurrState(g);
		System.out.println(gt.nextMove());
		*/
		
		g = new GameState(ROWS,COLS,GameState.MAX_PLAYER);
		g.addPiece(7,5);
		g.addPiece(6,4);
		g.addPiece(8,4);
		g.addPiece(6,6);
		g.addPiece(8,3);
		g.addPiece(6,7);
		g.addPiece(6,5);
		System.out.println(g);
		System.out.println(g.getCurrSide());
		System.out.println(g.nextPossibleMoves());
		gt.setCurrState(g);
		Point p = gt.nextMove();
		System.out.println(p);
		assertTrue(p.equals(new Point(8,5)) || p.equals(new Point(5,5)) || p.equals(new Point(8,2)));
		
	}
	
	/*
	@Test
	public void testReduceConsideration(){
		gt.reduceConsideration(gt.getCurrState().nextPossibleStates(),GameState.MAX_PLAYER);
		gt.reduceConsideration(gt.getCurrState().nextPossibleStates(),GameState.MIN_PLAYER);
	}
	*/
	@Test
	public void testMaxInteger(){
		System.out.println(Integer.MAX_VALUE+" "+(0-Integer.MAX_VALUE));
	}
	
}
