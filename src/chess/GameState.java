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
	public static final int J	= 0;
	public static final int S	= 1;
	public static final int X	= 2;
	public static final int M	= 3;
	public static final int C	= 4;
	public static final int P	= 5;
	public static final int Z	= 6;
	public static final int J1	= J+PIECE_TYPES;
	public static final int S1	= S+PIECE_TYPES;
	public static final int X1	= X+PIECE_TYPES;
	public static final int M1	= M+PIECE_TYPES;
	public static final int C1	= C+PIECE_TYPES;
	public static final int P1	= P+PIECE_TYPES;
	public static final int Z1	= Z+PIECE_TYPES;
	public static final int UNOCCUPIED = 99;
	
	//bounds
	public static final int LBOUND_J	= 3;
	public static final int RBOUND_J	= 5;
	public static final int UBOUND_J	= 0;
	public static final int DBOUND_J	= 2;
	public static final int UBOUND_J1	= ROWS-3;
	public static final int DBOUND_J1	= ROWS-1;
	public static final int LBOUND_X	= 0;
	public static final int RBOUND_X	= COLS-1;
	public static final int UBOUND_X	= 0;
	public static final int DBOUND_X	= 4;
	public static final int UBOUND_X1	= ROWS-5;
	public static final int DBOUND_X1	= ROWS-1;
	public static final int CROSS_Z		= DBOUND_X;
	public static final int CROSS_Z1	= UBOUND_X1;
	
	//moves
	public static final int[] J_MOVES = {VIRTUAL_COLS,-VIRTUAL_COLS, 1, -1};
	public static final int[] S_MOVES = {VIRTUAL_COLS-1,VIRTUAL_COLS+1, -VIRTUAL_COLS-1, -VIRTUAL_COLS+1};
	public static final int[] X_MOVES = {(VIRTUAL_COLS-1)*2,(VIRTUAL_COLS+1)*2, (-VIRTUAL_COLS-1)*2, (-VIRTUAL_COLS+1)*2};
	public static final int[] M_MOVES = {VIRTUAL_COLS*2-1,VIRTUAL_COLS*2+1, -VIRTUAL_COLS*2-1, -VIRTUAL_COLS*2+1, VIRTUAL_COLS+2,-VIRTUAL_COLS+2, VIRTUAL_COLS-2, -VIRTUAL_COLS-2};
	public static final int[] Z_MOVES = {VIRTUAL_COLS, 1, -1};
	public static final int[] Z1_MOVES = {-VIRTUAL_COLS, 1, -1};
	
	private int state[];	//[piece id]= location
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
	public int[] getGameState() {
		return state;
	}
	public int[] getOccupied(){
		return occupied;
	}
	public void initState(){
		state = new int[ROWS*VIRTUAL_COLS];	//store piece idx
											//VIRTUAL_COLS = 16 instead of 9. 
											//Accessing up/down cells become 4-bits shift
											//Checking out-of-bound become a single & operation
		for(int i=0; i<state.length; i++){
			state[i]=UNOCCUPIED;
		}
		//1: jiang 	
		//2: shi	
		//3: xiang	
		//4: ma		
		//5: che	
		//6: pao	
		//7: zu		
		state[0*VIRTUAL_COLS+0] = C;	state[0*VIRTUAL_COLS+1] = M;	state[0*VIRTUAL_COLS+2] = X;	state[0*VIRTUAL_COLS+3] = S;	state[0*VIRTUAL_COLS+4] = J;	state[0*VIRTUAL_COLS+5] = S;	state[0*VIRTUAL_COLS+6] = X;	state[0*VIRTUAL_COLS+7] = M;	state[0*VIRTUAL_COLS+8] = C;
		
										state[2*VIRTUAL_COLS+1] = P;																																									state[2*VIRTUAL_COLS+7] = P;
		state[3*VIRTUAL_COLS+0] = Z;						state[3*VIRTUAL_COLS+2] = Z;						state[3*VIRTUAL_COLS+4] = Z;						state[3*VIRTUAL_COLS+6] = Z;						state[3*VIRTUAL_COLS+8] = Z;
		
		state[6*VIRTUAL_COLS+0] = Z1;						state[6*VIRTUAL_COLS+2] = Z1;						state[6*VIRTUAL_COLS+4] = Z1;						state[6*VIRTUAL_COLS+6] = Z1;						state[6*VIRTUAL_COLS+8] = Z1;
							state[7*VIRTUAL_COLS+1] = P1;																										state[7*VIRTUAL_COLS+7] = P1;
							
		state[9*VIRTUAL_COLS+0] = C1;	state[9*VIRTUAL_COLS+1] = M1;	state[9*VIRTUAL_COLS+2] = X1;	state[9*VIRTUAL_COLS+3] = S1;	state[9*VIRTUAL_COLS+4] = J1;	state[9*VIRTUAL_COLS+5] = S1;	state[9*VIRTUAL_COLS+6] = X1;	state[9*VIRTUAL_COLS+7] = M1;	state[9*VIRTUAL_COLS+8] = C1;
	}
	/*
	//virtual gameboard rows = ROWS, cols = 16
	public void initState(){
		state = new int[PIECE_TYPES*2][5];	//store piece idx
		for(int i=0; i<state.length; i++){
			for(int j=0; j<5; j++){
				state[i][j]=UNOCCUPIED;				//<0 means no data or out of bound
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
	public boolean isJMove(int fromR, int fromC, int toR, int toC){
		if(currSide == MIN_PLAYER){
			if(toR<UBOUND_J1 || toR>DBOUND_J1 || toC<LBOUND_J ||  toC>RBOUND_J) return false;
		}else{
			if(toR<UBOUND_J  || toR>DBOUND_J  || toC<LBOUND_J ||  toC>RBOUND_J) return false;
		}
		int diff = ((toR-fromR)<<4)+toC-fromC;
		for(int i=0; i<J_MOVES.length; i++){
			if (diff==J_MOVES[i]) return true;
		}
		return false;
	}
	public boolean isSMove(int fromR, int fromC, int toR, int toC){
		if(currSide == MIN_PLAYER){
			if(toR<UBOUND_J1 || toR>DBOUND_J1 || toC<LBOUND_J ||  toC>RBOUND_J) return false;
		}else{
			if(toR<UBOUND_J  || toR>DBOUND_J  || toC<LBOUND_J ||  toC>RBOUND_J) return false;
		}
		int diff = ((toR-fromR)<<4)+toC-fromC;
		for(int i=0; i<S_MOVES.length; i++){
			if (diff==S_MOVES[i]) return true;
		}
		return false;
	}
	public boolean isXMove(int fromR, int fromC, int toR, int toC){
		if(currSide == MIN_PLAYER){
			if(toR<UBOUND_X1 || toR>DBOUND_X1 || toC<LBOUND_X ||  toC>RBOUND_X) return false;
		}else{
			if(toR<UBOUND_X  || toR>DBOUND_X  || toC<LBOUND_X ||  toC>RBOUND_X) return false;
		}
		int diff = ((toR-fromR)<<4)+toC-fromC;
		for(int i=0; i<X_MOVES.length; i++){
			int delta = S_MOVES[i];
			if (diff==X_MOVES[i] && state[(fromR<<4)+fromC+delta]==UNOCCUPIED) return true;
		}
		return false;
	}
	public boolean isMMove(int fromR, int fromC, int toR, int toC){
		int diff = ((toR-fromR)<<4)+toC-fromC;
		for(int i=0; i<M_MOVES.length; i++){
			int delta = J_MOVES[i>>1];
			if (diff==M_MOVES[i] && state[(fromR<<4)+fromC+delta]==UNOCCUPIED) return true;
		}
		return false;
	}
	public boolean isCMove(int fromR, int fromC, int toR, int toC){
		if (fromR == toR){
			//check nothing between fromC & toC
			if(fromC<toC){
				for(int i=fromC+1; i<toC; i++){
					if(state[(fromR<<4)+i]!=UNOCCUPIED) return false;
				}
			}else{
				for(int i=toC+1; i<fromC; i++){
					if(state[(fromR<<4)+i]!=UNOCCUPIED) return false;
				}
			}
			return true;
		}else if(fromC == toC){
			//check nothing between fromR & toR
			if(fromR<toR){
				for(int i=fromR+1; i<toR; i++){
					if(state[(i<<4)+fromC]!=UNOCCUPIED) return false;
				}
			}else{
				for(int i=toR+1; i<fromR; i++){
					if(state[(i<<4)+fromC]!=UNOCCUPIED) return false;
				}
			}
			return true;
		}else{
			return false;
		}
	}
	public boolean isPMove(int fromR, int fromC, int toR, int toC){
		//if attacking
		int toPiece = state[(toR<<4)+toC];
		if(toPiece/PIECE_TYPES==(1-currSide)){
			
			if (fromR == toR){
				int midPieceCount = 0;
				if(fromC<toC){
					for(int i=fromC+1; i<toC; i++){
						if(state[(fromR<<4)+i]!=UNOCCUPIED) midPieceCount++;
					}
				}else{
					for(int i=toC+1; i<fromC; i++){
						if(state[(fromR<<4)+i]!=UNOCCUPIED) midPieceCount++;
					}
				}
				if(midPieceCount==1)return true;
				else return false;
			}else if(fromC == toC){
				int midPieceCount = 0;
				if(fromR<toR){
					for(int i=fromR+1; i<toR; i++){
						if(state[(i<<4)+fromC]!=UNOCCUPIED) midPieceCount++;
					}
				}else{
					for(int i=toR+1; i<fromR; i++){
						if(state[(i<<4)+fromC]!=UNOCCUPIED) midPieceCount++;
					}
				}
				if(midPieceCount==1)return true;
				else return false;
			}else{
				return false;
			}
		}
		//if not attacking
		else{
			return isCMove(fromR, fromC,toR, toC);
		}
		
	}
	public boolean isZMove(int fromR, int fromC, int toR, int toC){
		int diff = ((toR-fromR)<<4)+toC-fromC;
		int mvLim = Z_MOVES.length;
		if(currSide == MIN_PLAYER){
			if(toR>=CROSS_Z1) mvLim = 1;
			for(int i=0; i<mvLim; i++){
				if (diff==Z1_MOVES[i]) return true;
			}
		}else{
			if(toR<=CROSS_Z) mvLim = 1;
			for(int i=0; i<mvLim; i++){
				if (diff==Z_MOVES[i]) return true;
			}
		}
		return false;
	}
	public boolean isValidMove(int fromR, int fromC, int toR, int toC){
		if(isOOB(toR,toC))	return false;
		int piece = state[(toR<<4)+toC];
		if(piece>=0){
			if(piece/PIECE_TYPES == currSide) return false;
		}
		
		//identify piece type
		int pieceType = state[(fromR<<4)+fromC];
		switch(pieceType){
		case J:
		case J1:
			return isJMove(fromR, fromC, toR, toC);
		case S:
		case S1:
			return isSMove(fromR, fromC, toR, toC);
		case X:
		case X1:
			return isXMove(fromR, fromC, toR, toC);
		case M:
		case M1:
			return isMMove(fromR, fromC, toR, toC);
		case C:
		case C1:
			return isCMove(fromR, fromC, toR, toC);
		case P:
		case P1:
			return isPMove(fromR, fromC, toR, toC);
		case Z:
		case Z1:
			return isZMove(fromR, fromC, toR, toC);
		}
		
		return false;
	}
	public boolean isValidSelection(int r, int c){
		if(isOOB(r,c))	return false;
		int piece = state[(r<<4)+c];
		if(piece==UNOCCUPIED)	return false;
		if(piece/PIECE_TYPES != currSide) return false;
		return true;
	}
	public boolean isOOB(int r, int c){
		if(r<0 || r>=ROWS || c<0 ||c>=COLS) return true;
		return false;
	}
	public boolean makeMove(int fromR, int fromC, int toR, int toC){
		Move mv = new Move(fromR, fromC, toR, toC, state[(toR<<4)+toC]);
		state[(toR<<4)+toC] = state[(fromR<<4)+fromC];
		state[(fromR<<4)+fromC] = UNOCCUPIED;
		moves.add(mv);
		setCurrSide(1-currSide);
		return true;
	}
	//precon: is valid move
	public boolean makeMove(Move m){
		m.toR = state[(m.fromR<<4)+m.fromC];
		if(m.toR == m.toC) return false;
		state[(m.fromR<<4)+m.fromC]  = m.toC;
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
			state[(mv.fromR<<4)+mv.fromC] = state[(mv.toR<<4)+mv.toC];
			state[(mv.toR<<4)+mv.toC] = mv.rmPiec;
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
		
		int JCount = 0;
		int J1Count = 0;
		for(int i=0; i<state.length; i++){
			if(state[i]==J){
				JCount++;
			}
			if(state[i]==J1){
				J1Count++;
			}
		}
		if(JCount==0) return MIN_PLAYER;
		if(J1Count==0) return MAX_PLAYER;
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
