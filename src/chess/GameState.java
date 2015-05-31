package chess;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GameState {
	public static final int MAX_PLAYER = 0;
	public static final int MIN_PLAYER = 1;
	public static final int MIN_STATE_VALUE = 0-Integer.MAX_VALUE;
	public static final int MAX_STATE_VALUE = Integer.MAX_VALUE;
	public static final int TIE = 2;
	public static final int ROWS = 10;
	public static final int COLS = 9;
	public static final int VIRTUAL_COLS = 16;
	public static final int PIECE_TYPES = 7;
	
	private int state[][];	//[piece id]= location
	private int occupied[];
	private int currSide;
	private int[] colMask;	//for extracting bit at specific column of a row
	public class Move{
		public int fromR;
		public int fromC;
		public int toR;
		public int toC;
		public int rmPiec;
		public Move(int fr, int fc, int tr, int tc, int rm){
			fromR = fr;
			fromC = fc;
			toR   = tr;
			toC   = tc;
			rmPiec= rm;
		}
	}
	private Stack<Move> moves;
	private static final int power[][][] = {
		{//0: jiang 
			{0,	0,  0,  0,  0,  0,  0,  0,  0},
			{0,	0,  0,  0,  0,  0,  0,  0,  0},
			{0,	0,  0,  0,  0,  0,  0,  0,  0},
			{0,	0,  0,  0,  0,  0,  0,  0,  0}, 
			{0,	0,  0,  0,  0,  0,  0,  0,  0}, 
			{0,	0,  0,  0,  0,  0,  0,  0,  0}, 
			{0,	0,  0,  0,  0,  0,  0,  0,  0}, 
			{0,	0,  0,  1,  1,  1,  0,  0,  0},
			{0,	0,  0,  2,  2,  2,  0,  0,  0}, 
			{0,	0,  0, 11, 15, 11,  0,  0,  0}
		},
		{//1: shi
			{0,  0,  0,  0,  0,  0,  0,  0,  0},  
			{0,  0,  0,  0,  0,  0,  0,  0,  0},  
			{0,  0,  0,  0,  0,  0,  0,  0,  0}, 
			{0,  0,  0,  0,  0,  0,  0,  0,  0}, 
			{0,  0,  0,  0,  0,  0,  0,  0,  0},  
			{0,  0,  0,  0,  0,  0,  0,  0,  0},  
			{0,  0,  0,  0,  0,  0,  0,  0,  0},  
			{0,  0,  0, 20,  0, 20,  0,  0,  0},  
			{0,  0,  0,  0, 23,  0,  0,  0,  0},  
			{0,  0,  0, 20,  0, 20,  0,  0,  0}
		},
		{//2: xiang
			{0,  0,  0,  0,  0,  0,  0,  0,  0}, 
			{0,  0,  0,  0,  0,  0,  0,  0,  0},  
			{0,  0,  0,  0,  0,  0,  0,  0,  0},  
			{0,  0,  0,  0,  0,  0,  0,  0,  0},  
			{0,  0,  0,  0,  0,  0,  0,  0,  0},  
			{0,  0, 20,  0,  0,  0, 20,  0,  0},  
			{0,  0,  0,  0,  0,  0,  0,  0,  0},  
		   {18,  0,  0,  0, 23,  0,  0,  0, 18},  
			{0,  0,  0,  0,  0,  0,  0,  0,  0},  
			{0,  0, 20,  0,  0,  0, 20,  0,  0}
		},
		{//3: ma
			{90, 90, 90, 96, 90, 96, 90, 90, 90},  
			{90, 96,103, 97, 94, 97,103, 96, 90},  
			{92, 98, 99,103, 99,103, 99, 98, 92},  
			{93,108,100,107,100,107,100,108, 93}, 
			{90,100, 99,103,104,103, 99,100, 90}, 
			{90, 98,101,102,103,102,101, 98, 90},  
			{92, 94, 98, 95, 98, 95, 98, 94, 92},  
			{93, 92, 94, 95, 92, 95, 94, 92, 93},  
			{85, 90, 92, 93, 78, 93, 92, 90, 85},  
			{88, 85, 90, 88, 90, 88, 90, 85, 88}
		},
		{//4: che
			{206,208,207,213,214,213,207,208,206},  
			{206,212,209,216,233,216,209,212,206},  
			{206,208,207,214,216,214,207,208,206},  
			{206,213,213,216,216,216,213,213,206}, 
			{208,211,211,214,215,214,211,211,208},  
			{208,212,212,214,215,214,212,212,208},  
			{204,209,204,212,214,212,204,209,204},  
			{198,208,204,212,212,212,204,208,198},  
			{200,208,206,212,200,212,206,208,200},  
			{194,206,204,212,200,212,204,206,194}
		},
		{//5: pao
		   {100,100, 96, 91, 90, 91, 96,100,100}, 
			{98, 98, 96, 92, 89, 92, 96, 98, 98},  
			{97, 97, 96, 91, 92, 91, 96, 97, 97}, 
			{96, 99, 99, 98,100, 98, 99, 99, 96}, 
			{96, 96, 96, 96,100, 96, 96, 96, 96}, 
			{95, 96, 99, 96,100, 96, 99, 96, 95},  
			{96, 96, 96, 96, 96, 96, 96, 96, 96},  
			{97, 96,100, 99,101, 99,100, 96, 97}, 
		    {96, 97, 98, 98, 98, 98, 98, 97, 96},  
		    {96, 96, 97, 99, 99, 99, 97, 96, 96}
		},
		{//6: zu
			 {9,  9,  9, 11, 13, 11,  9,  9,  9},
			{19, 24, 34, 42, 44, 42, 34, 24, 19},  
			{19, 24, 32, 37, 37, 37, 32, 24, 19},  
			{19, 23, 27, 29, 30, 29, 27, 23, 19},  
			{14, 18, 20, 27, 29, 27, 20, 18, 14},
			{ 7,  0, 13,  0, 16,  0, 13,  0,  7}, 
			{ 7,  0,  7,  0, 15,  0,  7,  0,  7},  
			{ 0,  0,  0,  0,  0,  0,  0,  0,  0}, 
			{ 0,  0,  0,  0,  0,  0,  0,  0,  0},  
			{ 0,  0,  0,  0,  0,  0,  0,  0,  0}
		}
	};

	
	
	

	public Stack<Move> getMoves() {
		return moves;
	}
	public Stack<Point> copyStack(Stack<Point> src){
		Stack<Point> dest = new Stack<Point>();
		for(Point p: src){
			dest.push(p);
		}
		return dest;
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
	public int[] getOccupied(){
		return occupied;
	}
	public void initState(){
		state = new int[ROWS][VIRTUAL_COLS];	//store piece idx
		for(int i=0; i<ROWS; i++){
			for(int j=0; j<VIRTUAL_COLS; j++){
				state[i][j]=-1;
			}
		}
		//1: jiang 	
		//2: shi	
		//3: xiang	
		//4: ma		
		//5: che	
		//6: pao	
		//7: zu		
		state[0][0] = 4;	state[0][1] = 3;	state[0][2] = 2;	state[0][3] = 1;	state[0][4] = 0;	state[0][5] = 1;	state[0][6] = 2;	state[0][7] = 3;	state[0][8] = 4;
		
							state[2][1] = 5;																										state[2][7] = 5;
		state[3][0] = 6;						state[3][2] = 6;						state[3][4] = 6;						state[3][6] = 6;						state[3][8] = 6;
		
		state[6][0] = 13;						state[6][2] = 13;						state[6][4] = 13;						state[6][6] = 13;						state[6][8] = 13;
							state[7][1] = 12;																										state[7][7] = 12;
							
		state[9][0] = 11;	state[9][1] = 10;	state[9][2] = 9;	state[9][3] = 8;	state[9][4] = 7;	state[9][5] = 8;	state[9][6] = 9;	state[9][7] = 10;	state[9][8] = 11;
	}
	/*
	//virtual gameboard rows = ROWS, cols = 16
	public void initState(){
		state = new int[PIECE_TYPES*2][5];	//store piece idx
		for(int i=0; i<state.length; i++){
			for(int j=0; j<5; j++){
				state[i][j]=-1;				//<0 means no data or out of bound
			}
		}
		
		//0: jiang 	1 piece
		//1: shi	2 pieces
		//2: xiang	2 pieces
		//3: ma		2 pieces
		//4: che	2 pieces
		//5: pao	2 pieces
		//6: zu		5 pieces
		
		//max piece   pos					min piece   							pos
		state[0][0] = 4; 					state[MIN_PLAYER*PIECE_TYPES+0 ][0] = (ROWS-1)*VIRTUAL_COLS+4;
		state[1][0] = 3;					state[MIN_PLAYER*PIECE_TYPES+1 ][0] = (ROWS-1)*VIRTUAL_COLS+3;
		state[1][1] = 5;					state[MIN_PLAYER*PIECE_TYPES+1 ][1] = (ROWS-1)*VIRTUAL_COLS+5;
		state[2][0] = 2;					state[MIN_PLAYER*PIECE_TYPES+2 ][0] = (ROWS-1)*VIRTUAL_COLS+2;
		state[2][1] = 6;					state[MIN_PLAYER*PIECE_TYPES+2 ][1] = (ROWS-1)*VIRTUAL_COLS+6;
		state[3][0] = 1;					state[MIN_PLAYER*PIECE_TYPES+3 ][0] = (ROWS-1)*VIRTUAL_COLS+1;
		state[3][1] = 7;					state[MIN_PLAYER*PIECE_TYPES+3 ][1] = (ROWS-1)*VIRTUAL_COLS+7;
		state[4][0] = 0;					state[MIN_PLAYER*PIECE_TYPES+4 ][0] = (ROWS-1)*VIRTUAL_COLS+0;
		state[4][1] = 8;					state[MIN_PLAYER*PIECE_TYPES+4 ][1] = (ROWS-1)*VIRTUAL_COLS+8;
		state[5][0] = 2*VIRTUAL_COLS+1;		state[MIN_PLAYER*PIECE_TYPES+5 ][0] = (ROWS-3)*VIRTUAL_COLS+1;
		state[5][1] = 2*VIRTUAL_COLS+7;		state[MIN_PLAYER*PIECE_TYPES+5 ][1] = (ROWS-3)*VIRTUAL_COLS+7;
		state[6][0] = 3*VIRTUAL_COLS;		state[MIN_PLAYER*PIECE_TYPES+6 ][0] = (ROWS-4)*VIRTUAL_COLS;
		state[6][1] = 3*VIRTUAL_COLS+2;		state[MIN_PLAYER*PIECE_TYPES+6 ][1] = (ROWS-4)*VIRTUAL_COLS+2;
		state[6][2] = 3*VIRTUAL_COLS+4;		state[MIN_PLAYER*PIECE_TYPES+6 ][2] = (ROWS-4)*VIRTUAL_COLS+4;
		state[6][3] = 3*VIRTUAL_COLS+6;		state[MIN_PLAYER*PIECE_TYPES+6 ][3] = (ROWS-4)*VIRTUAL_COLS+6;
		state[6][4] = 3*VIRTUAL_COLS+8;		state[MIN_PLAYER*PIECE_TYPES+6 ][4] = (ROWS-4)*VIRTUAL_COLS+8;
	}
	*/
	public void initMoves(){
		moves = new Stack<Move>();
	}
	public void initOccupied(){
		occupied = new int[ROWS*VIRTUAL_COLS];
		for(int i=0; i<GameState.PIECE_TYPES*2; i++){
			   for(int j=0; j<5; j++){
				   int pos = state[i][j];
				   if(pos>=0){
					   occupied[pos] = 1;
				   }
			   }
		}
	}
	public GameState(int r, int c, int side){
		setCurrSide(side);
		initState();
		initMoves();
		//initOccupied();
	}
	/*
	public GameState(int r, int c, int side, int[][] state, Stack<Point> moves){//, int[][][][] value){
		setRows(r);
		setCols(c);
		setCurrSide(side);
		setGameState(state);
		setMoves(moves);
		//setValue (value);

	}
	*/
	public boolean isValidMove(int r, int c){
		if(isOOB(r,c))	return false;
		int piece = state[r][c];
		if(piece>=0){
			if(piece/PIECE_TYPES == currSide) return false;
		}
		return true;
	}
	public boolean isValidSelection(int r, int c){
		if(isOOB(r,c))	return false;
		int piece = state[r][c];
		if(piece<0)	return false;
		if(piece/PIECE_TYPES != currSide) return false;
		return true;
	}
	public boolean isOOB(int r, int c){
		if(r<0 || r>=ROWS || c<0 ||c>=COLS) return true;
		return false;
	}
	public boolean makeMove(int fromR, int fromC, int toR, int toC){
		Move mv = new Move(fromR, fromC, toR, toC, state[toR][toC]);
		state[toR][toC] = state[fromR][fromC];
		state[fromR][fromC] = -1;
		moves.add(mv);
		setCurrSide(1-currSide);
		return true;
	}
	public boolean makeMove(Move m){
		if(m.fromR<0 || m.fromC<0 || state[m.fromR][m.fromC]<0) return false;
		m.toR = state[m.fromR][m.fromC];
		if(m.toR == m.toC) return false;
		state[m.fromR][m.fromC]  = m.toC;
		moves.add(m);
		occupied[m.toR] = 0;
		occupied[m.toC] = 1;
		setCurrSide(1-currSide);
		return true;
	}
	public boolean makeNullMove(){
		setCurrSide(1-currSide);
		return true;
	}
	public boolean revertOneMove(){
		
		if(moves.isEmpty()){
			return false;
		}else{
			Move mv = moves.pop();
			state[mv.fromR][mv.fromC] = state[mv.toR][mv.toC];
			state[mv.toR][mv.toC] = mv.rmPiec;
		}
		setCurrSide(1-currSide);
		return true;
	}
	public boolean revertNullMove(){
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
				if(((i-r)<2 && (i-r)>-2) && ((j-c)<2 && (j-c)>-2)){
					newPieceExpandedMap[i] |= colMask[j];
				}
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


	public List<Move> nextPossibleMoves(){
		List<Move> nexts = new ArrayList<Move>();
		if(isGameOver()!=-1) return nexts;
		
		return nexts;
		
	}
	
	public int evaluate(){
		
	
		int maxplayerTotal = 0;
		int minplayerTotal = 0;
		
		
		return maxplayerTotal+minplayerTotal;//maxplayerTotal/5*2+minplayerTotal/5*3;

	}
	
	public int updateValue(Move m){
		int maxplayerTotal = 0;
		int minplayerTotal = 0;
		
		return maxplayerTotal+minplayerTotal;
	}
	
	//precon: a.length() == b.length() and a, b a bit strings
	public String combineBitStrings(String a, String b){
		char[] ac = a.toCharArray();
		char[] bc = b.toCharArray();
		for(int i=0; i<bc.length; i++){
			if(bc[i]=='1') ac[i]='2';
		}
		return String.valueOf(ac);
	}

	public int isGameOver(){
		//if(checkConnect(MAX_PLAYER,WIN_CONNECT)) return MAX_PLAYER;	//0
		//if(checkConnect(MIN_PLAYER,WIN_CONNECT)) return MIN_PLAYER;	//1
		//if(checkFull()) return TIE;									//2
		
		return -1;
	}
	
	public String intToRow(int r){
		String s = "%"+COLS+"s";
		return String.format(s, Integer.toBinaryString(r)).replace(' ', '0');
	}
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		sb.append("game value ="+evaluate());
		return sb.toString();
	}
}
