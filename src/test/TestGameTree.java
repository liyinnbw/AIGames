package test;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.List;
import java.util.Stack;

import chess.GameState;
import chess.Move;

import org.junit.Before;
import org.junit.Test;

import chess.GameTree;

public class TestGameTree {
	public static final int ROWS = 10;
	public static final int COLS = 9;
	public static final int SEARCH_TIME = 3000;
	public GameState g;
	public GameTree gt;
	@Before
	public void init() {
		g = new GameState(ROWS,COLS,GameState.MIN_PLAYER);
		gt = new GameTree(g,SEARCH_TIME);
	}
	@Test
	public void testNextMove(){
		/*
		int[] state3 = {
				14,	14,	2,	1,	0,	1,	2,	14,	4,	14,	14,	14,	14,	14,	14,	14,	
				14,	14,	14,	4,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	
				14,	14,	3,	14,	14,	11,	3,	14,	14,	14,	14,	14,	14,	14,	14,	14,	
				6,	14,	6,	14,	6,	14,	6,	14,	6,	14,	14,	14,	14,	14,	14,	14,	
				14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	
				14,	14,	14,	14,	14,	14,	13,	14,	14,	14,	14,	14,	14,	14,	14,	14,	
				13,	14,	13,	14,	13,	14,	14,	5,	13,	14,	14,	14,	14,	14,	14,	14,	
				14,	12,	14,	14,	9,	14,	10,	12,	14,	14,	14,	14,	14,	14,	14,	14,	
				14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	
				14,	11,	14,	8,	7,	8,	9,	14,	14,	14,	14,	14,	14,	14,	14,	14
		};
		g.setState(state3);
		g.setCurrSide(GameState.MIN_PLAYER);
		List<Move> nexts = g.nextPossibleMoves();
		boolean findMove = false;
		for(Move m: nexts){
			if(m.fromC == 5 && m.fromR == 2 && m.toC == 6 && m.toR == 2 && m.rmPiec == GameState.M){
				findMove = true;
				break;
			}
		}
		assertTrue(findMove);
		Move next = gt.nextMove();
		System.out.println(next);
		*/
		int[] state4 = {
				/*14,	14,	14,	14,	0,	1,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	
				14,	14,	10,	14,	1,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	
				14,	14,	14,	14,	14,	14,	14,	14,	2,	14,	14,	14,	14,	14,	14,	14,	
				12,	13,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	
				14,	14,	3,	14,	14,	14,	6,	14,	14,	14,	14,	14,	14,	14,	14,	14,	
				13,	14,	14,	11,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	
				14,	14,	14,	14,	14,	14,	13,	14,	13,	14,	14,	14,	14,	14,	14,	14,	
				14,	14,	14,	14,	9,	14,	10,	14,	14,	14,	14,	14,	14,	14,	14,	14,	
				14,	14,	4,	14,	8,	7,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	
				14,	14,	14,	8,	14,	14,	9,	14,	14,	14,	14,	14,	14,	14,	14,	14*/
				
				/*14,	4,	2,	1,	0,	1,	4,	14,	14,	14,	14,	14,	14,	14,	14,	14,	
				14,	14,	14,	14,	14,	3,	14,	11,	14,	14,	14,	14,	14,	14,	14,	14,	
				14,	14,	14,	14,	2,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	
				6,	14,	14,	14,	6,	14,	14,	14,	6,	14,	14,	14,	14,	14,	14,	14,	
				14,	14,	14,	3,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	
				13,	14,	14,	14,	13,	14,	12,	14,	14,	14,	14,	14,	14,	14,	14,	14,	
				14,	14,	13,	14,	14,	14,	13,	14,	13,	14,	14,	14,	14,	14,	14,	14,	
				11,	5,	14,	14,	14,	14,	14,	14,	12,	14,	14,	14,	14,	14,	14,	14,	
				14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	
				14,	14,	9,	8,	7,	8,	9,	14,	14,	14,	14,	14,	14,	14,	14,	14	*/
				
				14,	14,	2,	0,	14,	1,	2,	14,	14,	14,	14,	14,	14,	14,	14,	14,	
				14,	14,	11,	14,	14,	14,	14,	11,	14,	14,	14,	14,	14,	14,	14,	14,	
				14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	
				14,	14,	6,	14,	14,	14,	6,	14,	6,	14,	14,	14,	14,	14,	14,	14,	
				6,	14,	14,	4,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	
				14,	14,	14,	14,	12,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	
				13,	14,	13,	14,	13,	14,	13,	14,	13,	14,	14,	14,	14,	14,	14,	14,	
				14,	14,	10,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	
				14,	14,	14,	4,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	
				14,	14,	9,	8,	7,	8,	9,	14,	14,	14,	14,	14,	14,	14,	14,	14
		};
		g.setState(state4);
		g.setCurrSide(GameState.MAX_PLAYER);
		System.out.println("value = "+g.evaluate());
		List<Move> nexts = g.nextPossibleMoves();
		boolean findMove = false;
		for(Move m: nexts){
			//if(m.fromC == 4 && m.fromR == 0 && m.toC == 3 && m.toR == 0 && m.rmPiec == GameState.UNOCCUPIED){
			//if(m.fromC == 5 && m.fromR == 1 && m.toC == 6 && m.toR == 3 && m.rmPiec == GameState.UNOCCUPIED){
			if(m.fromC == 2 && m.fromR == 0 && m.toC == 4 && m.toR == 2 && m.rmPiec == GameState.UNOCCUPIED){
				findMove = true;
				break;
			}
		}
		assertTrue(findMove);
		Move next = gt.nextMove();
		System.out.println(next);
//		g.makeMove(next);
//		nexts = g.nextPossibleMoves();
//		findMove = false;
//		for(Move m: nexts){
//			if(m.fromC == 2 && m.fromR == 1 && m.toC == 4 && m.toR == 0 && m.rmPiec == GameState.J){
//				findMove = true;
//				break;
//			}
//		}
//		assertTrue(findMove);
		
	}
	@Test
	public void testMinMaxAlphaBeta(){
		int[] state3 = {
				14,	14,	2,	1,	0,	1,	2,	14,	4,	14,	14,	14,	14,	14,	14,	14,	
				14,	14,	14,	4,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	
				14,	14,	3,	14,	14,	11,	3,	14,	14,	14,	14,	14,	14,	14,	14,	14,	
				6,	14,	6,	14,	6,	14,	6,	14,	6,	14,	14,	14,	14,	14,	14,	14,	
				14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	
				14,	14,	14,	14,	14,	14,	13,	14,	14,	14,	14,	14,	14,	14,	14,	14,	
				13,	14,	13,	14,	13,	14,	14,	5,	13,	14,	14,	14,	14,	14,	14,	14,	
				14,	12,	14,	14,	9,	14,	10,	12,	14,	14,	14,	14,	14,	14,	14,	14,	
				14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	14,	
				14,	11,	14,	8,	7,	8,	9,	14,	14,	14,	14,	14,	14,	14,	14,	14
		};
		g.setState(state3);
		System.out.println(gt.getCurrState());
		g.setCurrSide(GameState.MAX_PLAYER);
		
		GameTree.TreeNode tn = gt.minMaxAlphaBeta(gt.getCurrState(), 2, -Integer.MAX_VALUE, Integer.MAX_VALUE, true);
		System.out.println(tn.nextMove);
		g.makeMove(tn.nextMove);
		System.out.println(g);
	}
	

}
