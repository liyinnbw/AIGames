package test;

import static org.junit.Assert.*;
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
	public void testTranspose(){
		int state[]={1,2,3,4,5,6};
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
				"000000000000",
				"000000000000",
				"000000000000",
				"001000000000",
				"000100000000",
				"000010000000",
				"000001000000",
				"000000100000",
				"000000010000",
				"000000001000",
				"000000000000",
				"000000000000"
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
	public void testNextPossibleStates(){
		int state[][] = new int[2][ROWS];
		g.setGameState(state);
		g.addPiece(1, 1);
		System.out.println(g);
		System.out.println(g.nextPossibleStates());
	}
	@Test
	public void testIsTooFar(){
		int occupied[]= new int[ROWS];
		String s[] = {
				"000000000000",
				"001100000000",
				"000000000000",
				"001000000000",
				"000100000000",
				"000010000000",
				"000001000000",
				"000000100000",
				"000000010000",
				"000000001000",
				"000000000000",
				"000000000000"
		};
		for(int i=0; i<ROWS; i++){
			occupied[i]=Integer.parseInt(s[i],2);
		}
		
		System.out.println(g.isTooFar(occupied, 0, 0));
		
		System.out.println(g.isTooFar(occupied, 5, 5));
		
		System.out.println(g.isTooFar(occupied, ROWS-1, COLS-1));
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
	public void testEvaluatePattern2(){
		String pattern = "201111022011100";
		assertEquals(g.evaluatePattern2(pattern, 2),GameState.LIVE_4);
		assertEquals(g.evaluatePattern2(pattern, 3),GameState.LIVE_4);
		assertEquals(g.evaluatePattern2(pattern, 4),GameState.LIVE_4);
		assertEquals(g.evaluatePattern2(pattern, 5),GameState.LIVE_4);
		assertNotEquals(g.evaluatePattern2(pattern, 10),GameState.LIVE_4);
		assertNotEquals(g.evaluatePattern2(pattern, 11),GameState.LIVE_4);
		assertNotEquals(g.evaluatePattern2(pattern, 12),GameState.LIVE_4);
		/*
		pattern = "111101000000000";
		assertEquals(g.evaluatePattern2(pattern, 2),GameState.DEAD_4);
		pattern = "0211110100000000";
		assertEquals(g.evaluatePattern2(pattern, 3),GameState.DEAD_4);
		pattern = "000000002001111";
		assertEquals(g.evaluatePattern2(pattern, 13),GameState.DEAD_4);
		pattern = "000000010111120";
		assertEquals(g.evaluatePattern2(pattern, 12),GameState.DEAD_4);
		*/
		pattern = "001100000000000";
		assertEquals(g.evaluatePattern2(pattern, 2),GameState.DEAD_2);
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
	public void testEvaluatePos2(){
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
		System.out.println(g.evaluatePos2(1, 2, GameState.MAX_PLAYER));
	
	}
	@Test
	public void testEvaluateAll(){
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
			System.out.println(g.evaluateAll(GameState.MAX_PLAYER));
	}
	@Test
	public void compareEvalute3and2(){
		String sa = "001111000000100";
		String sb = "100000011000000";
		String scombined = g.combineBitStrings(sa,sb);
		int a = Integer.parseInt(sa,2);
		int b = Integer.parseInt(sb,2);
		int idx = 2;
		
		long start = System.currentTimeMillis();
		for(int i=0; i<10000000; i++){
			assertEquals(g.evaluatePattern3(a,b,14-idx),GameState.LIVE_4);
		}
		long end = System.currentTimeMillis();
		System.out.println("time = "+(end-start)/1000.0+" s");
		
		start = System.currentTimeMillis();
		for(int i=0; i<10000000; i++){
			assertEquals(g.evaluatePattern2(scombined,idx),GameState.LIVE_4);
		}
		end = System.currentTimeMillis();
		System.out.println("time = "+(end-start)/1000.0+" s");
	}
	@Test
	public void testEvaluate3(){
		
	}
}
