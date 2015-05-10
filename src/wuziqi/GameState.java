package wuziqi;

import java.util.ArrayList;
import java.util.List;

public class GameState {
	public static int MIN_PLAYER = 1;
	public static int MAX_PLAYER = 0;
	public static int MIN_STATE_VALUE = -1*Integer.MAX_VALUE;
	public static int MAX_STATE_VALUE = Integer.MAX_VALUE;
	public static int TIE = 2;
	public static int WIN_CONNECT = 5;
	
	private int ROWS;	//tentatively maximum 32
	private int COLS;	//tentatively maximum 32
	private int currSide;
	private int[][] state;	//[0][]: max player [1][]: min player
	private int[] colMask;
	public int getRows() {
		return ROWS;
	}

	public void setRows(int rows) {
		this.ROWS = rows;
	}

	public int getCols() {
		return COLS;
	}

	public void setCols(int cols) {
		this.COLS = cols;
		colMask = new int[COLS];
		for(int i=0; i<COLS; i++){
			colMask[i]=1<<COLS-1-i;
		}
	}

	public int getCurrSide() {
		return currSide;
	}

	public void setCurrSide(int currSide) {
		this.currSide = currSide;
	}
	public int[][] getGameState() {
		return state;
	}
	public void setGameState(int[][] s) {
		state = new int[2][ROWS];
		System.arraycopy(s[0], 0, state[MAX_PLAYER], 0, ROWS);
		System.arraycopy(s[1], 0, state[MIN_PLAYER], 0, ROWS);
	}
	public void initState(){
		state = new int[2][ROWS];
	}
	public GameState(int r, int c, int side){
		setRows(r);
		setCols(c);
		setCurrSide(side);
		initState();
	}
	public GameState(int r, int c, int side, int[][] state){
		setRows(r);
		setCols(c);
		setCurrSide(side);
		setGameState(state);
	}
	
	public boolean addPiece(int x, int y){
		if(y>=ROWS || x>= COLS || x<0 || y<0) return false;
		
		int bRow = state[MAX_PLAYER][y];
		int wRow = state[MIN_PLAYER][y];	
		int bitMap = 1<<COLS-x-1;
		int wBit = wRow & bitMap;
		int bBit = bRow & bitMap;
		
		if(wBit!=0 || bBit!=0) return false;
		state[currSide][y] |= bitMap;
		setCurrSide(1-currSide);
		return true;
	}
	public int getBit(int[] s, int r, int c){
		return s[r] & colMask[c];
	}
	//reduce the search space by limiting new added piece to be 
	//at most 1 squares away from the nearest other piece
	public boolean isTooFar(int[] occupied, int r, int c){
		int newPieceExpandedMap[] = new int[ROWS];
		for(int i=0; i<ROWS; i++){
			for(int j=0; j<COLS; j++){
				if(Math.abs(i-r)<2 && Math.abs(j-c)<2)
					newPieceExpandedMap[i] |= colMask[j];
			}
		}
		//StringBuilder sb = new StringBuilder();
		//sb.append("expanded:\n");
		//for(int i=0; i<newPieceExpandedMap.length; i++){
        //	sb.append(intToRow(newPieceExpandedMap[i])+"\n");
        //}
		//System.out.println(sb.toString());
		
		for(int i=0; i<ROWS; i++){
			if((occupied[i] & newPieceExpandedMap[i])!=0)
				return false;
		}
		
		return true;
	}
	public List<GameState> nextPossibleStates(){
		List<GameState> nexts = new ArrayList<GameState>();
		if(isGameOver()!=-1) return nexts;
		
		int occupied[] = new int[ROWS];
		for(int i=0; i<ROWS; i++){
			occupied[i]=state[0][i] | state[1][i];
		}
		
		for(int i=0; i<ROWS; i++){
			int row = occupied[i];
			for(int j=0; j<COLS; j++){
				int newRow = row | colMask[j];
				if (newRow!=row && !isTooFar(occupied,i,j)){
					GameState s = new GameState(ROWS, COLS, currSide, state);
					s.addPiece(j,i); //addPiece uses grid coordinates, so j,i
					nexts.add(s);
				}
	 		}
		}
		return nexts;
	}
	public int heuristicFunction(){
		return 0;
	}
	public int evaluate(){
		int total = 0;
		if(checkConnect(MAX_PLAYER,WIN_CONNECT)) return MAX_STATE_VALUE;
		if(checkConnect(MIN_PLAYER,WIN_CONNECT)) return MIN_STATE_VALUE;
		return total;
	}
	public int isGameOver(){
		if(checkConnect(MAX_PLAYER,WIN_CONNECT)) return MAX_PLAYER;	//0
		if(checkConnect(MIN_PLAYER,WIN_CONNECT)) return MIN_PLAYER;	//1
		if(checkFull()) return TIE;									//2
		
		return -1;
	}
	public boolean checkConnect(int side, int connect){
		int s[] = state[side];

		//check vertical
		if(checkVertical(s,connect)) return true;
		
		//check horizontal
		int tpState[] = transpose(s);
		if(checkVertical(tpState,connect)) return true;
		
		//check diagonal \
		int sflState[] = diagonalShiftL(s);
		if(checkVertical(sflState,connect)) return true;
		
		//check diagonal /
		int sfrState[] = diagonalShiftR(s);
		if(checkVertical(sfrState,connect)) return true;
		
		return false;
	}
	public boolean checkFull(){
		int occupied[] = new int[ROWS];
		for(int i=0; i<ROWS; i++){
			occupied[i]=state[MAX_PLAYER][i] | state[MIN_PLAYER][i];
		}
		int full = Integer.MAX_VALUE & ((1<<COLS)-1);
		for(int i=0; i<ROWS; i++){
			if(occupied[i]!=full){
				return false;
			}
		}
		
		return true;
	}
	public boolean checkVertical(int state[], int connect){
		for(int i=0; i<=ROWS-connect; i++){
			int result=state[i];
			for(int j=1; j<connect; j++){
				result &= state[i+j];
			}
			if (result!=0) return true;
		}
		return false;
	}
	//precon: COLS<31/2
	public int[] diagonalShiftL(int[] state){
		int sfState[] = new int[ROWS];
		for(int i=0; i<ROWS; i++){
			sfState[i]= state[i]<<i;
		}
		return sfState;
		
	}
	//precon: COLS<32/2
	public int[] diagonalShiftR(int[] state){
		int sfState[] = new int[ROWS];
		for(int i=0; i<ROWS; i++){
			sfState[i] = state[i]<<(ROWS-i-1);
		}
		return sfState;
	}
	//precon: ROWS==COLS
	public int[] transpose(int state[]){
		int tpState[] = new int[COLS];
		for(int i=0; i<COLS; i++){
			int col = 0;
			for(int j=0; j<ROWS; j++){
				int bit = (state[j] & colMask[i])<<i;
				bit = bit>>j;
				col |= col | bit;
			}
			tpState[i]= col;
		}
		return tpState;
	}
	//moved to MainUI.java to enable global hashing
	//public void aiMove(){
	//	GameTree tree = new GameTree(this, 4);
	//	GameState nextBest = tree.nextMove();
	//	setGameState(nextBest.getGameState());
	//	setCurrSide(1-currSide);
	//}
	public String intToRow(int r){
		String s = "%"+COLS+"s";
		return String.format(s, Integer.toBinaryString(r)).replace(' ', '0');
	}
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("black:\n");
		for(int i=0; i<state[0].length; i++){
        	sb.append(intToRow(state[0][i])+"\n");
        }
		sb.append("white:\n");
		for(int i=0; i<state[1].length; i++){
			sb.append(intToRow(state[1][i])+"\n");
        }
		sb.append("game value ="+evaluate());
		return sb.toString();
	}
}
