package test;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.List;
import java.util.Stack;

import wuziqi.GameState;

import org.junit.Before;
import org.junit.Test;

public class TestGameState {
	public GameState g;
	public static int COLS=15;
	public static int ROWS=15;
	@Before
	public void init() {
		g = new GameState(ROWS,COLS,GameState.MAX_PLAYER);
	}

	@Test
	public void testCopyStack(){
		Point a = new Point(1,2);
		Point b = new Point(2,3);
		Stack<Point> src = new Stack<Point>();
		src.push(a);
		src.push(b);
		Stack<Point> dest = g.copyStack(src);
		src.pop();
		src.pop();
		assertEquals(dest.size(),2);
		Point B = dest.pop();
		//assertNotEquals(B,b);
		assertEquals((int)B.getX(),2);
		assertEquals((int)B.getY(),3);
	}
	@Test
	public void testRevertOneMove(){
		g.addPiece(0, 0);
		g.addPiece(0, 1);
		System.out.println(g);
		System.out.println("currSide = "+g.getCurrSide()+" totalMoves = "+g.getMoves().size());
		g.revertOneMove();
		System.out.println(g);
		System.out.println("currSide = "+g.getCurrSide()+" totalMoves = "+g.getMoves().size());
		g.revertOneMove();
		System.out.println(g);
		System.out.println("currSide = "+g.getCurrSide()+" totalMoves = "+g.getMoves().size());
	}
	@Test
	public void testTranspose(){
		int state[] = new int[ROWS];
		String s[] = {
				"000000000000000",
				"001100000000000",
				"000000000000000",
				"001000000000000",
				"000100000000000",
				"000010000000000",
				"000001000000000",
				"000000000000000",
				"000000010000000",
				"000000001000000",
				"000000000000000",
				"000000000000000",
				"000000000000000",
				"000000000000000",
				"000000000000000"
		};
		for(int i=0; i<ROWS; i++){
			state[i]=Integer.parseInt(s[i],2);
		}
		System.out.println("before transpose");
		for(int i=0; i<state.length; i++){
        	System.out.println(g.intToRow(state[i]));
        }
		int tpState[] = g.transpose(state);
		System.out.println("after transpose");
		for(int i=0; i<tpState.length; i++){
        	System.out.println(g.intToRow(tpState[i]));
        }
		
	}
	@Test
	public void testDiagonalShiftL(){
		int state[] = new int[ROWS];
		String s[] = {
				"000000000000000",
				"001100000000000",
				"000000000000000",
				"001000000000000",
				"000100000000000",
				"000010000000000",
				"000001000000000",
				"000000000000000",
				"000000010000000",
				"000000001000000",
				"000000000000000",
				"000000000000000",
				"000000000000000",
				"000000000000000",
				"000000000000000"
		};
		for(int i=0; i<ROWS; i++){
			state[i]=Integer.parseInt(s[i],2);
		}
		System.out.println("before shift");
		for(int i=0; i<state.length; i++){
        	System.out.println(g.intToRow(state[i]));
        }
		int sflState[] = g.diagonalShiftL(state);
		System.out.println("after shift");
		for(int i=0; i<sflState.length; i++){
        	System.out.println(g.intToRow(sflState[i]));
        }
	}

	@Test
	public void testNextPossibleMoves(){
		g.addPiece(7, 7);
		g.addPiece(2, 3);
		g.addPiece(6, 7);
		System.out.println(g);
		System.out.println("latestMove = "+g.getMoves().peek());
		System.out.println(g.nextPossibleMoves());
	}
	@Test
	public void testCheckMustMoves(){
		
		System.out.println(g.evaluatePattern(Integer.parseInt("000000111000000",2),Integer.parseInt("000010000000000",2),9,ROWS));
		g.addPiece(6, 7);
		g.addPiece(5, 6);
		g.addPiece(7, 7);
		g.addPiece(4, 7);
		g.addPiece(8, 7);
		System.out.println(g);
		System.out.println("side = "+g.getCurrSide());
		System.out.println("before check must move");
		List<Point> nexts = g.nextPossibleMoves();
		System.out.println(nexts);
		System.out.println("after check must move");
		List<Point> mustmove = g.checkMustMoves(nexts);
		System.out.println(mustmove);
		
		for(int i=0; i<5; i++){
			g.revertOneMove();
		}
		
		g.addPiece(6, 7);
		g.addPiece(5, 6);
		g.addPiece(7, 7);
		g.addPiece(5, 7);
		g.addPiece(8, 7);
		g.addPiece(9, 7);
		g.addPiece(5, 8);
		g.addPiece(4, 6);
		g.addPiece(7, 6);
		System.out.println(g);
		System.out.println("side = "+g.getCurrSide());
		System.out.println("diagonal value = "+ g.testDiagonal(9,4,GameState.MAX_PLAYER));
		System.out.println("diagonal value = "+ g.testDiagonal(5,8,GameState.MAX_PLAYER));
		System.out.println("before check must move");
		nexts = g.nextPossibleMoves();
		System.out.println(nexts);
		System.out.println("after check must move");
		mustmove = g.checkMustMoves(nexts);
		System.out.println(mustmove);
	}
	@Test
	public void testIsTooFar(){
		int occupied[]= new int[ROWS];
		String s[] = {
				"000000000000000",
				"001100000000000",
				"000000000000000",
				"001000000000000",
				"000100000000000",
				"000010000000000",
				"000001000000000",
				"000000000000000",
				"000000010000000",
				"000000001000000",
				"000000000000000",
				"000000000000000",
				"000000000000000",
				"000000000000000",
				"000000000000000"
		};
		for(int i=0; i<ROWS; i++){
			occupied[i]=Integer.parseInt(s[i],2);
		}
		
		assertTrue(g.isTooFar(occupied, 0, 0));	
		assertFalse(g.isTooFar(occupied, 5, 5));
		assertTrue(g.isTooFar(occupied, ROWS-1, COLS-1));
	}
	@Test
	public void testEvaluateValues(){
		int values1[] = {GameState.LIVE_4,GameState.LIVE_4, GameState.LIVE_4, GameState.LIVE_4};
		int values2[] = {GameState.DEAD_4,GameState.DEAD_4, GameState.DEAD_4, GameState.DEAD_4};
		int values3[] = {GameState.LIVE_3,GameState.LIVE_3, GameState.LIVE_3, GameState.LIVE_3};
		int values4[] = {GameState.DEAD_3,GameState.DEAD_3, GameState.DEAD_3, GameState.DEAD_3};
		int values5[] = {GameState.LIVE_2,GameState.LIVE_2, GameState.LIVE_2, GameState.LIVE_2};
		int values6[] = {GameState.DEAD_2,GameState.DEAD_2, GameState.DEAD_2, GameState.DEAD_2};
		int values7[] = {GameState.DEAD_3,GameState.LIVE_3, GameState.DEAD_2, GameState.DEAD_2};
		int values8[] = {GameState.DEAD_3,GameState.LIVE_3, GameState.DEAD_4, GameState.DEAD_2};
		assertEquals(g.evaluateValues(values1),GameState.LIVE_4);
		assertEquals(g.evaluateValues(values2),GameState.DOUBLE_DEAD_4);
		assertEquals(g.evaluateValues(values3),GameState.DOUBLE_LIVE_3);
		assertEquals(g.evaluateValues(values4),GameState.DOUBLE_DEAD_3);
		assertEquals(g.evaluateValues(values5),GameState.DOUBLE_LIVE_2);
		assertEquals(g.evaluateValues(values6),GameState.DEAD_2);
		assertEquals(g.evaluateValues(values7),GameState.DEAD_3_LIVE_3);
		assertEquals(g.evaluateValues(values8),GameState.DEAD_4_LIVE_3);
	}

	@Test
	public void testCombineBitStrings(){
		String a = "000110100";
		String b = "001001000";
		String c = "002112100";
		String d = "001221200";
		assertEquals(g.combineBitStrings(a,b),c);
		assertEquals(g.combineBitStrings(b,a),d);
	}
	@Test
	public void testEvaluatePos(){
		/*
		String s[][] = {
			   {"000000000000000",
				"001100000000000",
				"000000000000000",
				"001000000000000",
				"000100000000000",
				"000010000000000",
				"000001000000000",
				"000000000000000",
				"000000010000000",
				"000000001000000",
				"000000000000000",
				"000000000000000",
				"000000000000000",
				"000000000000000",
				"000000000000000"},
				
			   {"000000000000000",
				"010010000000000",
				"000000000000000",
				"010100000000000",
				"001010000000000",
				"000100000000000",
				"000000100000000",
				"000001010000000",
				"000000101000000",
				"000000010100000",
				"000000000000000",
				"000000000000000",
				"000000000000000",
				"000000000000000",
				"000000000000000"}
				
		};
		int state[][]=new int[2][ROWS];
		for(int i=0; i<2; i++){
			for(int j=0; j<ROWS; j++){
				state[i][j]=Integer.parseInt(s[i][j],2);
			}
		}
		g.setGameState(state);
//		System.out.println(g.evaluatePos(1, 2, GameState.MAX_PLAYER, GameState.ALL_DIRECTION));
		
		
		String s2[][] = {
			   {"000000000000000",
				"000000000000000",
				"000000000000000",
				"000000000000000",
				"000000000000000",
				"000000000000000",
				"000001000000000",
				"000001111000000",
				"000011000000000",
				"000000000000000",
				"000000000000000",
				"000000000000000",
				"000000000000000",
				"000000000000000",
				"000000000000000"},
				
			   {"000000000000000",
				"000000000000000",
				"000000000000000",
				"000000000000000",
				"000000000000000",
				"000000000000000",
				"000010100000000",
				"000010000100000",
				"000100000000000",
				"000010000000000",
				"000000000000000",
				"000000000000000",
				"000000000000000",
				"000000000000000",
				"000000000000000"}
					
			};
			int state2[][]=new int[2][ROWS];
			for(int i=0; i<2; i++){
				for(int j=0; j<ROWS; j++){
					state2[i][j]=Integer.parseInt(s2[i][j],2);
				}
			}
			g.setGameState(state2);
			g.setCurrSide(GameState.MIN_PLAYER);
			//System.out.println(g);
			assertEquals(g.evaluatePos(9, 5, GameState.MAX_PLAYER, GameState.ALL_DIRECTION),  GameState.LIVE_3);
			assertEquals(g.evaluatePos(5, 5, GameState.MAX_PLAYER, GameState.ALL_DIRECTION), GameState.LIVE_3);
			assertNotEquals(g.evaluatePos(10, 5, GameState.MAX_PLAYER, GameState.ALL_DIRECTION), GameState.LIVE_3);

			
			String s3[][] = {
			   {"000000000000000",
				"000000001000000",
				"000000000000000",
				"000000000000000",
				"000001000000000",
				"000000110000000",
				"000000101100000",
				"000000010000000",
				"000000000000000",
				"000000000000000",
				"000000000000000",
				"000000000000000",
				"000000000000000",
				"000000000000000",
				"000000000000000"},
				
			   {"000000000000000",
				"000000000000000",
				"000000001000000",
				"000000001000000",
				"000000111000000",
				"000000001000000",
				"000001000000000",
				"000000000000000",
				"000000000000000",
				"000000000000000",
				"000000000000000",
				"000000000000000",
				"000000000000000",
				"000000000000000",
				"000000000000000"}
					
			};
			int state3[][]=new int[2][ROWS];
			for(int i=0; i<2; i++){
				for(int j=0; j<ROWS; j++){
					state3[i][j]=Integer.parseInt(s3[i][j],2);
				}
			}
			g.setGameState(state3);
			g.setCurrSide(GameState.MIN_PLAYER);
			System.out.println(g);
			System.out.println(g.evaluatePos(6, 7, GameState.MAX_PLAYER, GameState.ALL_DIRECTION));
			//System.out.println(g.nextPossibleMoves());
			
			String s4[][] = {
			   {"000000000000000",
				"000000000000000",
				"000000000000000",
				"000000000000000",
				"000000100000000",
				"001000100000000",
				"000010100000000",
				"000001000000000",
				"000000000000000",
				"000000000000000",
				"000000010000000",
				"000000000000000",
				"000000000000000",
				"000000000000000",
				"000000000000000"},
				
			   {"000000000000000",
				"000000000000000",
				"000000000000000",
				"000000000000000",
				"000000000000000",
				"000100000000000",
				"000101000000000",
				"000010000000000",
				"000001000000000",
				"000000100000000",
				"000000000000000",
				"000000000000000",
				"000000000000000",
				"000000000000000",
				"000000000000000"}
					
			};
			int state4[][]=new int[2][ROWS];
			for(int i=0; i<2; i++){
				for(int j=0; j<ROWS; j++){
					state4[i][j]=Integer.parseInt(s4[i][j],2);
				}
			}
			g.setGameState(state4);
			g.setCurrSide(GameState.MIN_PLAYER);
			System.out.println(g);
			System.out.println(g.evaluatePos(7, 6, GameState.MAX_PLAYER, GameState.ALL_DIRECTION));
		
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
			System.out.println(g.evaluatePos(7, 9, GameState.MAX_PLAYER, GameState.ALL_DIRECTION));
			System.out.println(g.nextPossibleMoves());
			*/	
		
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
			"000100100000000",
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
		g.setCurrSide(GameState.MAX_PLAYER);
		//System.out.println(g);
		//System.out.println(g.evaluatePos(7, 5, GameState.MAX_PLAYER, GameState.ALL_DIRECTION));
		System.out.println(g.evaluatePos(2, 3, GameState.MIN_PLAYER, GameState.ALL_DIRECTION));
		//System.out.println(g.nextPossibleMoves());
	
	}

	@Test
	public void testEvaluate(){

		int a = Integer.parseInt("001111000000100",2);
		int b = Integer.parseInt("100000011000000",2);
		int start = 1;
		int end = 7;
		for(int i= start; i<end; i++){
			assertEquals(g.evaluatePattern(a,b,14-i,ROWS),GameState.LIVE_4);
		}
		
		a = Integer.parseInt("011110000000100",2);
		b = Integer.parseInt("000000011000000",2);
		start = 0;
		end = 6;
		for(int i= start; i<end; i++){
			assertEquals(g.evaluatePattern(a,b,14-i,ROWS),GameState.LIVE_4);
		}
		
		a = Integer.parseInt("000000000011110",2);
		b = Integer.parseInt("000000011000000",2);
		start = 9;
		end = 15;
		for(int i= start; i<end; i++){
			assertEquals(g.evaluatePattern(a,b,14-i,ROWS),GameState.LIVE_4);
		}
		
		a = Integer.parseInt("000000000111100",2);
		b = Integer.parseInt("000000010000001",2);
		start = 8;
		end = 14;
		for(int i= start; i<end; i++){
			assertEquals(g.evaluatePattern(a,b,14-i,ROWS),GameState.LIVE_4);
		}
		
		a = Integer.parseInt("001111000000100",2);
		b = Integer.parseInt("010000011000000",2);
		start = 2;
		end = 7;
		for(int i= start; i<end; i++){
			assertEquals(g.evaluatePattern(a,b,14-i,ROWS),GameState.DEAD_4);
		}
		
		a = Integer.parseInt("001110100000100",2);
		b = Integer.parseInt("000000011000000",2);
		start = 2;
		end = 7;
		for(int i= start; i<end; i++){
			assertEquals(g.evaluatePattern(a,b,14-i,ROWS),GameState.DEAD_4);
		}
		
		a = Integer.parseInt("001101100000100",2);
		b = Integer.parseInt("010000011000000",2);
		start = 2;
		end = 7;
		for(int i= start; i<end; i++){
			assertEquals(g.evaluatePattern(a,b,14-i,ROWS),GameState.DEAD_4);
		}
		
		a = Integer.parseInt("001011100000100",2);
		b = Integer.parseInt("000000001000000",2);
		start = 2;
		end = 7;
		for(int i= start; i<end; i++){
			assertEquals(g.evaluatePattern(a,b,14-i,ROWS),GameState.DEAD_4);
		}
		
		a = Integer.parseInt("001111000000100",2);
		b = Integer.parseInt("000000111000000",2);
		start = 1;
		end = 6;
		for(int i= start; i<end; i++){
			assertEquals(g.evaluatePattern(a,b,14-i,ROWS),GameState.DEAD_4);
		}
		
		a = Integer.parseInt("001110000000100",2);
		b = Integer.parseInt("000000011000000",2);
		start = 1;
		end = 6;
		for(int i= start; i<end; i++){
			assertEquals(g.evaluatePattern(a,b,14-i,ROWS),GameState.LIVE_3);
		}
		
		a = Integer.parseInt("001110000000100",2);
		b = Integer.parseInt("100000011000000",2);
		start = 1;
		end = 6;
		for(int i= start; i<end; i++){
			assertEquals(g.evaluatePattern(a,b,14-i,ROWS),GameState.LIVE_3);
		}
		
		a = Integer.parseInt("001110000000100",2);
		b = Integer.parseInt("110000011000000",2);
		start = 2;
		end = 6;
		for(int i= start; i<end; i++){
			assertEquals(g.evaluatePattern(a,b,14-i,ROWS),GameState.DEAD_3);
		}
		
		a = Integer.parseInt("001110000000100",2);
		b = Integer.parseInt("000001111000000",2);
		start = 1;
		end = 5;
		for(int i= start; i<end; i++){
			assertEquals(g.evaluatePattern(a,b,14-i,ROWS),GameState.DEAD_3);
		}
		
		a = Integer.parseInt("010011000000100",2);
		b = Integer.parseInt("000000111000000",2);
		start = 1;
		end = 6;
		for(int i= start; i<end; i++){
			assertEquals(g.evaluatePattern(a,b,14-i,ROWS),GameState.DEAD_3);
		}
		
		a = Integer.parseInt("011001000000100",2);
		b = Integer.parseInt("000000111000000",2);
		start = 1;
		end = 6;
		for(int i= start; i<end; i++){
			assertEquals(g.evaluatePattern(a,b,14-i,ROWS),GameState.DEAD_3);
		}
		
		a = Integer.parseInt("010101000000100",2);
		b = Integer.parseInt("000000111000000",2);
		start = 1;
		end = 6;
		for(int i= start; i<end; i++){
			assertEquals(g.evaluatePattern(a,b,14-i,ROWS),GameState.DEAD_3);
		}
		
		a = Integer.parseInt("010110000000100",2);
		b = Integer.parseInt("000000111000000",2);
		start = 0;
		end = 6;
		for(int i= start; i<end; i++){
			assertEquals(g.evaluatePattern(a,b,14-i,ROWS),GameState.LIVE_3);
		}
		
		a = Integer.parseInt("011010000000100",2);
		b = Integer.parseInt("000000111000000",2);
		start = 0;
		end = 6;
		for(int i= start; i<end; i++){
			assertEquals(g.evaluatePattern(a,b,14-i,ROWS),GameState.LIVE_3);
		}
		
		a = Integer.parseInt("000110000000100",2);
		b = Integer.parseInt("000000001000000",2);
		start = 3;
		end = 5;
		for(int i= start; i<end; i++){
			assertEquals(g.evaluatePattern(a,b,14-i,ROWS),GameState.LIVE_2);
		}
		
		a = Integer.parseInt("000110000000100",2);
		b = Integer.parseInt("000001001000000",2);
		start = 3;
		end = 5;
		for(int i= start; i<end; i++){
			assertEquals(g.evaluatePattern(a,b,14-i,ROWS),GameState.DEAD_2);
		}
		
		a = Integer.parseInt("000110000000100",2);
		b = Integer.parseInt("000000101000000",2);
		start = 3;
		end = 5;
		for(int i= start; i<end; i++){
			assertEquals(g.evaluatePattern(a,b,14-i,ROWS),GameState.DEAD_2);
		}
		
		a = Integer.parseInt("000110000000100",2);
		b = Integer.parseInt("000000011000000",2);
		start = 3;
		end = 5;
		for(int i= start; i<end; i++){
			assertEquals(g.evaluatePattern(a,b,14-i,ROWS),GameState.LIVE_2);
		}
		
		a = Integer.parseInt("000110000000100",2);
		b = Integer.parseInt("010000001000000",2);
		start = 3;
		end = 5;
		for(int i= start; i<end; i++){
			assertEquals(g.evaluatePattern(a,b,14-i,ROWS),GameState.DEAD_2);
		}
		
		a = Integer.parseInt("010010000000100",2);
		b = Integer.parseInt("000000101000000",2);
		start = 1;
		end = 5;
		for(int i= start; i<end; i++){
			assertEquals(g.evaluatePattern(a,b,14-i,ROWS),GameState.DEAD_2);
		}
		
		a = Integer.parseInt("001010000000100",2);
		b = Integer.parseInt("000000011000000",2);
		start = 2;
		end = 5;
		for(int i= start; i<end; i++){
			assertEquals(g.evaluatePattern(a,b,14-i,ROWS),GameState.DEAD_2);
		}
		
		a = Integer.parseInt("001000000000100",2);
		b = Integer.parseInt("000000011000000",2);
		start = 0;
		end = 14;
		for(int i= start; i<end; i++){
			assertEquals(g.evaluatePattern(a,b,14-i,ROWS),0);
		}
		
	}
	@Test
	public void testDead_2test(){
		int a = Integer.parseInt("001000000000100",2);
		int b = Integer.parseInt("000000011000000",2);
		System.out.println("evaluation result = "+g.dead2_5test(a,b,14-11));
		
		a = Integer.parseInt("000000011100000",2);
		b = Integer.parseInt("000000000000000",2);
		System.out.println("evaluation result = "+g.dead2_5test(a,b,4));
	}
}
