package test;

import static org.junit.Assert.*;
import wuziqi.GameState;
import org.junit.Before;
import org.junit.Test;

public class TestGameState {
	public GameState g;
	public static int COLS=12;
	public static int ROWS=12;
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

}
