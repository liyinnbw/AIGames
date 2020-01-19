package chess;

import java.util.Random;

public class ZobristHash {
	private int ROWS;
	private int COLS;
	private int [][] hashtable; 
	public ZobristHash(int rows, int cols, int gamesquareStates){
		ROWS = rows;
		COLS = cols;
		initTable(ROWS*COLS,gamesquareStates);
	}
	public void initTable(int gameboardSize, int gamesquareStates){
		hashtable = new int[gameboardSize][gamesquareStates];
		Random rand = new Random();
		for(int i=0; i<gameboardSize; i++){
			for(int j=0; j<gamesquareStates; j++){
				hashtable[i][j] = rand.nextInt();
			}
		}
		System.out.println("Zobrist HashTable initialized once");
	}
	
	//currently this hashfunction is hardcoded
	public int hash(GameState s){
		int hashValue = 0;
		
		int[] state = s.getGameState();
		for(int i=0; i<state.length; i++){
			hashValue ^= hashtable[i][state[i]];
		}
		return hashValue;
	}
}
