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
	public static final int HEX_COLS = 16;		//faster index access
	public static final int PIECE_TYPES = 7;
	public static final int SQUARE_STATES = PIECE_TYPES*2+1;	//for Zobrist Hashing
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
	public static final int UNOCCUPIED = Z1+1;
	
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
	public static final int[] J_MOVES 	= {HEX_COLS,-HEX_COLS, 1, -1};
	public static final int[] S_MOVES 	= {HEX_COLS-1,HEX_COLS+1, -HEX_COLS-1, -HEX_COLS+1};
	public static final int[] X_MOVES 	= {(HEX_COLS-1)*2,(HEX_COLS+1)*2, (-HEX_COLS-1)*2, (-HEX_COLS+1)*2};
	public static final int[] M_MOVES 	= {HEX_COLS*2-1,HEX_COLS*2+1, -HEX_COLS*2-1, -HEX_COLS*2+1, HEX_COLS+2,-HEX_COLS+2, HEX_COLS-2, -HEX_COLS-2};
	public static final int[] Z_MOVES 	= {HEX_COLS, 1, -1};
	public static final int[] Z1_MOVES 	= {-HEX_COLS, 1, -1};
	
	private int state[];	//[piece id]= location
	private int currSide;
	private Stack<Move> moves;
	private static final int power[][] = {
		{//0: jiang 
			0,	0,  0,  0,  0,  0,  0,  0,  0,	0,0,0,0,0,0,0,
			0,	0,  0,  0,  0,  0,  0,  0,  0,	0,0,0,0,0,0,0,
			0,	0,  0,  0,  0,  0,  0,  0,  0,	0,0,0,0,0,0,0,
			0,	0,  0,  0,  0,  0,  0,  0,  0,	0,0,0,0,0,0,0,
			0,	0,  0,  0,  0,  0,  0,  0,  0,	0,0,0,0,0,0,0,
			0,	0,  0,  0,  0,  0,  0,  0,  0,	0,0,0,0,0,0,0,
			0,	0,  0,  0,  0,  0,  0,  0,  0,	0,0,0,0,0,0,0,
			0,	0,  0,  1,  1,  1,  0,  0,  0,	0,0,0,0,0,0,0,
			0,	0,  0,  2,  2,  2,  0,  0,  0,	0,0,0,0,0,0,0,
			0,	0,  0, 11, 15, 11,  0,  0,  0,	0,0,0,0,0,0,0
		},
		{//1: shi
			0,  0,  0,  0,  0,  0,  0,  0,  0,	0,0,0,0,0,0,0,  
			0,  0,  0,  0,  0,  0,  0,  0,  0,	0,0,0,0,0,0,0,  
			0,  0,  0,  0,  0,  0,  0,  0,  0,	0,0,0,0,0,0,0, 
			0,  0,  0,  0,  0,  0,  0,  0,  0,	0,0,0,0,0,0,0, 
			0,  0,  0,  0,  0,  0,  0,  0,  0, 	0,0,0,0,0,0,0, 
			0,  0,  0,  0,  0,  0,  0,  0,  0, 	0,0,0,0,0,0,0, 
			0,  0,  0,  0,  0,  0,  0,  0,  0, 	0,0,0,0,0,0,0, 
			0,  0,  0, 20,  0, 20,  0,  0,  0, 	0,0,0,0,0,0,0, 
			0,  0,  0,  0, 23,  0,  0,  0,  0, 	0,0,0,0,0,0,0, 
			0,  0,  0, 20,  0, 20,  0,  0,  0,	0,0,0,0,0,0,0
		},
		{//2: xiang
			0,  0,  0,  0,  0,  0,  0,  0,  0,	0,0,0,0,0,0,0, 
			0,  0,  0,  0,  0,  0,  0,  0,  0,	0,0,0,0,0,0,0,  
			0,  0,  0,  0,  0,  0,  0,  0,  0, 	0,0,0,0,0,0,0, 
			0,  0,  0,  0,  0,  0,  0,  0,  0, 	0,0,0,0,0,0,0, 
			0,  0,  0,  0,  0,  0,  0,  0,  0, 	0,0,0,0,0,0,0, 
			0,  0, 20,  0,  0,  0, 20,  0,  0, 	0,0,0,0,0,0,0, 
			0,  0,  0,  0,  0,  0,  0,  0,  0, 	0,0,0,0,0,0,0, 
		   18,  0,  0,  0, 23,  0,  0,  0, 18, 	0,0,0,0,0,0,0, 
			0,  0,  0,  0,  0,  0,  0,  0,  0, 	0,0,0,0,0,0,0, 
			0,  0, 20,  0,  0,  0, 20,  0,  0,	0,0,0,0,0,0,0
		},
		{//3: ma
			90, 90, 90, 96, 90, 96, 90, 90, 90,	0,0,0,0,0,0,0,  
			90, 96,103, 97, 94, 97,103, 96, 90,	0,0,0,0,0,0,0,  
			92, 98, 99,103, 99,103, 99, 98, 92,	0,0,0,0,0,0,0,  
			93,108,100,107,100,107,100,108, 93,	0,0,0,0,0,0,0, 
			90,100, 99,103,104,103, 99,100, 90,	0,0,0,0,0,0,0, 
			90, 98,101,102,103,102,101, 98, 90,	0,0,0,0,0,0,0,  
			92, 94, 98, 95, 98, 95, 98, 94, 92,	0,0,0,0,0,0,0,  
			93, 92, 94, 95, 92, 95, 94, 92, 93,	0,0,0,0,0,0,0,  
			85, 90, 92, 93, 78, 93, 92, 90, 85,	0,0,0,0,0,0,0,  
			88, 85, 90, 88, 90, 88, 90, 85, 88,	0,0,0,0,0,0,0
		},
		{//4: che
			206,208,207,213,214,213,207,208,206,0,0,0,0,0,0,0,  
			206,212,209,216,233,216,209,212,206,0,0,0,0,0,0,0,  
			206,208,207,214,216,214,207,208,206,0,0,0,0,0,0,0,  
			206,213,213,216,216,216,213,213,206,0,0,0,0,0,0,0,
			208,211,211,214,215,214,211,211,208,0,0,0,0,0,0,0,
			208,212,212,214,215,214,212,212,208,0,0,0,0,0,0,0,
			204,209,204,212,214,212,204,209,204,0,0,0,0,0,0,0,
			198,208,204,212,212,212,204,208,198,0,0,0,0,0,0,0,
			200,208,206,212,200,212,206,208,200,0,0,0,0,0,0,0,
			194,206,204,212,200,212,204,206,194,0,0,0,0,0,0,0
		},
		{//5: pao
		   100,100, 96, 91, 90, 91, 96,100,100,	0,0,0,0,0,0,0, 
			98, 98, 96, 92, 89, 92, 96, 98, 98,	0,0,0,0,0,0,0,  
			97, 97, 96, 91, 92, 91, 96, 97, 97,	0,0,0,0,0,0,0, 
			96, 99, 99, 98,100, 98, 99, 99, 96,	0,0,0,0,0,0,0, 
			96, 96, 96, 96,100, 96, 96, 96, 96,	0,0,0,0,0,0,0, 
			95, 96, 99, 96,100, 96, 99, 96, 95,	0,0,0,0,0,0,0,  
			96, 96, 96, 96, 96, 96, 96, 96, 96,	0,0,0,0,0,0,0,  
			97, 96,100, 99,101, 99,100, 96, 97,	0,0,0,0,0,0,0, 
		    96, 97, 98, 98, 98, 98, 98, 97, 96,	0,0,0,0,0,0,0,  
		    96, 96, 97, 99, 99, 99, 97, 96, 96,	0,0,0,0,0,0,0
		},
		{//6: zu
			 9,  9,  9, 11, 13, 11,  9,  9,  9,	0,0,0,0,0,0,0,
			19, 24, 34, 42, 44, 42, 34, 24, 19,	0,0,0,0,0,0,0,  
			19, 24, 32, 37, 37, 37, 32, 24, 19,	0,0,0,0,0,0,0,  
			19, 23, 27, 29, 30, 29, 27, 23, 19,	0,0,0,0,0,0,0,  
			14, 18, 20, 27, 29, 27, 20, 18, 14,	0,0,0,0,0,0,0,
			 7,  0, 13,  0, 16,  0, 13,  0,  7,	0,0,0,0,0,0,0, 
			 7,  0,  7,  0, 15,  0,  7,  0,  7,	0,0,0,0,0,0,0,  
			 0,  0,  0,  0,  0,  0,  0,  0,  0,	0,0,0,0,0,0,0, 
			 0,  0,  0,  0,  0,  0,  0,  0,  0,	0,0,0,0,0,0,0,  
			 0,  0,  0,  0,  0,  0,  0,  0,  0,	0,0,0,0,0,0,0
		}
	};

	
	
	public int[] getLivePieces(){
		int livePieces[] = new int[(PIECE_TYPES<<1)+1];
		for(int i=0; i<state.length; i++){
			livePieces[state[i]]++;
		}
		return livePieces;
	}

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
	public void setState(int s[]){
		state = new int[s.length];
		System.arraycopy(s, 0, state, 0, s.length);
	}
	
	public static int[] getMirrorState(int s[]){
		int[] mState = new int[s.length];
		System.arraycopy(s, 0, mState, 0, s.length);
		int midCol = COLS/2;
		for(int i=0; i<ROWS; i++){
			for(int j=0; j<midCol; j++){
				int tmp = mState[(i<<4)+j];
				
				mState[(i<<4)+j] = mState[(i<<4)+COLS-1-j];
				mState[(i<<4)+COLS-1-j] = tmp;
			}
		}
		return mState;
	}
	public void mirrorState(){
		int midCol = COLS/2;
		for(int i=0; i<ROWS; i++){
			for(int j=0; j<midCol; j++){
				int tmp = state[(i<<4)+j];		
				state[(i<<4)+j] = state[(i<<4)+COLS-1-j];
				state[(i<<4)+COLS-1-j] = tmp;
			}
		}
	}
	public static Move getMirrorMove(Move m){
		if(m==null) return null;
		int fr = m.fromR;
		int fc = COLS-1-m.fromC;
		int tr = m.toR;
		int tc = COLS-1-m.toC;
		int rm = m.rmPiec;
		return new Move(fr, fc, tr, tc, rm);
	}
	public void initState(){
		state = new int[ROWS*HEX_COLS];	//store piece idx
										//use HEX_COLS = 16 instead of COLS=9. 
										//Accessing up/down cells become 4-bits shift

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
		state[0*HEX_COLS+0] = C;	state[0*HEX_COLS+1] = M;	state[0*HEX_COLS+2] = X;	state[0*HEX_COLS+3] = S;	state[0*HEX_COLS+4] = J;	state[0*HEX_COLS+5] = S;	state[0*HEX_COLS+6] = X;	state[0*HEX_COLS+7] = M;	state[0*HEX_COLS+8] = C;
		
									state[2*HEX_COLS+1] = P;																																				state[2*HEX_COLS+7] = P;
		state[3*HEX_COLS+0] = Z;								state[3*HEX_COLS+2] = Z;								state[3*HEX_COLS+4] = Z;								state[3*HEX_COLS+6] = Z;								state[3*HEX_COLS+8] = Z;
		
		state[6*HEX_COLS+0] = Z1;								state[6*HEX_COLS+2] = Z1;								state[6*HEX_COLS+4] = Z1;								state[6*HEX_COLS+6] = Z1;								state[6*HEX_COLS+8] = Z1;
									state[7*HEX_COLS+1] = P1;																																				state[7*HEX_COLS+7] = P1;
							
		state[9*HEX_COLS+0] = C1;	state[9*HEX_COLS+1] = M1;	state[9*HEX_COLS+2] = X1;	state[9*HEX_COLS+3] = S1;	state[9*HEX_COLS+4] = J1;	state[9*HEX_COLS+5] = S1;	state[9*HEX_COLS+6] = X1;	state[9*HEX_COLS+7] = M1;	state[9*HEX_COLS+8] = C1;
	}

	public void initMoves(){
		moves = new Stack<Move>();
	}

	public GameState(int r, int c, int side){
		setCurrSide(side);
		initState();
		initMoves();
	}
	
	//precon: move is within board
	public boolean isJMove(int fromR, int fromC, int toR, int toC){

		int toPiece = state[(toR<<4)+toC];
		
		if(currSide == MIN_PLAYER){
			//if not attacking
			if(toPiece != J || fromC != toC){
				if(toR<UBOUND_J1 || toC<LBOUND_J || toC>RBOUND_J) return false;
			}
			//if attacking
			else{
				//check nothing between fromR & toR
				for(int i=toR+1; i<fromR; i++){
					if(state[(i<<4)+fromC]!=UNOCCUPIED) return false;
				}
				return true;
			}
		}else{
			//if not attacking
			if(toPiece != J1 || fromC != toC){
				if(toR>DBOUND_J  || toC<LBOUND_J ||  toC>RBOUND_J) return false;
			}
			//if attacking
			else{
				//check nothing between fromR & toR
				for(int i=fromR+1; i<toR; i++){
					if(state[(i<<4)+fromC]!=UNOCCUPIED) return false;
				}
				return true;
			}
		}
		int diff = ((toR-fromR)<<4)+toC-fromC;
		for(int i=0; i<J_MOVES.length; i++){
			if (diff==J_MOVES[i]) return true;
		}
		return false;
	}
	//precon: move is within board
	public boolean isSMove(int fromR, int fromC, int toR, int toC){
		
		if(currSide == MIN_PLAYER){
			if(toR<UBOUND_J1 || toC<LBOUND_J ||  toC>RBOUND_J) return false;
		}else{
			if(toR>DBOUND_J  || toC<LBOUND_J ||  toC>RBOUND_J) return false;
		}
		int diff = ((toR-fromR)<<4)+toC-fromC;
		for(int i=0; i<S_MOVES.length; i++){
			if (diff==S_MOVES[i]) return true;
		}
		return false;
	}
	//precon: move is within board
	public boolean isXMove(int fromR, int fromC, int toR, int toC){
		
		if(currSide == MIN_PLAYER){
			if(toR<UBOUND_X1) return false;
		}else{
			if(toR>DBOUND_X) return false;
		}
		int diff = ((toR-fromR)<<4)+toC-fromC;
		for(int i=0; i<X_MOVES.length; i++){
			int delta = S_MOVES[i];
			if (diff==X_MOVES[i] && state[(fromR<<4)+fromC+delta]==UNOCCUPIED) return true;
		}
		return false;
	}
	public boolean isMMove(int fromR, int fromC, int toR, int toC){
		if(isOOB(toR, toC)) return false;
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
		int to = (toR<<4)+toC;
		int piece = state[to];
		if(piece!=UNOCCUPIED){
			if(piece/PIECE_TYPES == currSide) return false;
		}
		int from= (fromR<<4)+fromC;
		int pieceType = state[from];
		switch(pieceType){
		case C:
		case C1:
			return isCMove(fromR, fromC, toR, toC);
		case P:
		case P1:
			return isPMove(fromR, fromC, toR, toC);
		case Z:
		case Z1:
			return isZMove(fromR, fromC, toR, toC);
		case M:
		case M1:
			return isMMove(fromR, fromC, toR, toC);
		case J:
		case J1:
			return isJMove(fromR, fromC, toR, toC);
		case S:
		case S1:
			return isSMove(fromR, fromC, toR, toC);
		case X:
		case X1:
			return isXMove(fromR, fromC, toR, toC);
		}
		
		return false;
	}
	public boolean isValidSelection(int r, int c){
		if(isOOB(r,c))	return false;
		int piece = state[(r<<4)+c];
		if(piece/PIECE_TYPES == currSide) return true;
		return false;
	}
	public boolean isOOB(int r, int c){
		//int idx = (r<<4)+c;
		//if((idx>>4)<ROWS && ((idx & 0xF)<=8)) return false;
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
		
		//this should not happen
		if(m.rmPiec!=state[(m.toR<<4)+m.toC]){
			System.out.println("move inconsistent.");
			return false;
		}
		
		state[(m.toR<<4)+m.toC]  = state[(m.fromR<<4)+m.fromC];
		state[(m.fromR<<4)+m.fromC]  = UNOCCUPIED;
		moves.add(m);
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

	public List<Move> nextPossibleMoves(){
		List<Move> nexts = new ArrayList<Move>();
		if(isGameOver()!=-1) return nexts;
		for(int i=0; i<state.length; i++){
			int pieceType = state[i];
			if((pieceType/PIECE_TYPES)!=currSide) continue;
			switch(pieceType){
			case J:
			case J1:
				{
					int fromR = i>>4;
					int fromC = i & 0xF;
					for(int j=0; j<J_MOVES.length;j++){
						int to = i+J_MOVES[j];
						int toR = to>>4;
						int toC = to & 0xF;
						if(isValidMove(fromR, fromC, toR, toC)){
							nexts.add(new Move(fromR, fromC, toR, toC, state[to]));
						}
					}
					//also check along col for possible J-J-kill
					for(int j=0; j<ROWS; j++){
						if(j==fromR) continue;
						int toR = j;
						int toC = fromC;
						int to = (toR<<4) +toC;
						if(state[to]==J || state[to]==J1){
							if(isValidMove(fromR, fromC, toR, toC)){
								nexts.add(new Move(fromR, fromC, toR, toC, state[to]));
								break;
							}
						}
					}
				}
				break;
			case S:
			case S1:
				{
					int fromR = i>>4;
					int fromC = i & 0xF;
					for(int j=0; j<S_MOVES.length;j++){
						int to = i+S_MOVES[j];
						int toR = to>>4;
						int toC = to & 0xF;
						if(isValidMove(fromR, fromC, toR, toC)){
							nexts.add(new Move(fromR, fromC, toR, toC, state[to]));
						}
					}
				}
				break;
			case X:
			case X1:
				{
					int fromR = i>>4;
					int fromC = i & 0xF;
					for(int j=0; j<X_MOVES.length;j++){
						int to = i+X_MOVES[j];
						int toR = to>>4;
						int toC = to & 0xF;
						if(isValidMove(fromR, fromC, toR, toC)){
							nexts.add(new Move(fromR, fromC, toR, toC, state[to]));
						}
					}
				}
				break;
			case M:
			case M1:
				{
					int fromR = i>>4;
					int fromC = i & 0xF;
					for(int j=0; j<M_MOVES.length;j++){
						int to = i+M_MOVES[j];
						int toR = to>>4;
						int toC = to & 0xF;
						if(isValidMove(fromR, fromC, toR, toC)){
							nexts.add(new Move(fromR, fromC, toR, toC, state[to]));
						}
					}
				}
				break;
			case C:
			case C1:
				{
					int fromR = i>>4;
					int fromC = i & 0xF;
					//move along row
					for(int j=0; j<ROWS; j++){
						int toR = j;
						int toC = fromC;	
						int to 	= (toR<<4)+toC;
						if(isValidMove(fromR, fromC, toR, toC)){
							nexts.add(new Move(fromR, fromC, toR, toC, state[to]));
						}
					}
					//move along col
					for(int j=0; j<COLS; j++){
						int toR = fromR;
						int toC = j;	
						int to 	= (toR<<4)+toC;
						if(isValidMove(fromR, fromC, toR, toC)){
							nexts.add(new Move(fromR, fromC, toR, toC, state[to]));
						}
					}
				}
				break;
			case P:
			case P1:
				{
					int fromR = i>>4;
					int fromC = i & 0xF;
					//move along row
					for(int j=0; j<ROWS; j++){
						int toR = j;
						int toC = fromC;	
						int to 	= (toR<<4)+toC;
						if(isValidMove(fromR, fromC, toR, toC)){
							nexts.add(new Move(fromR, fromC, toR, toC, state[to]));
						}
					}
					//move along col
					for(int j=0; j<COLS; j++){
						int toR = fromR;
						int toC = j;	
						int to 	= (toR<<4)+toC;
						if(isValidMove(fromR, fromC, toR, toC)){
							nexts.add(new Move(fromR, fromC, toR, toC, state[to]));
						}
					}
				}
				break;
			case Z:
			case Z1:
				{
					int fromR = i>>4;
					int fromC = i & 0xF;
					for(int j=0; j<J_MOVES.length;j++){	//use JMOVE instead of Z/Z1MOVE to avoid determing Z or Z1
						int to = i+J_MOVES[j];
						int toR = to>>4;
						int toC = to & 0xF;
						if(isValidMove(fromR, fromC, toR, toC)){
							nexts.add(new Move(fromR, fromC, toR, toC, state[to]));
						}
					}
				}
				break;
			}
		}

		return nexts;
		
	}

	public int evaluate(){
		int over = isGameOver();
		if(over == -1){
			int maxplayerTotal = 0;
			int minplayerTotal = 0;
			
			for(int i=0; i<state.length; i++){
				int pieceType = state[i];
				if(pieceType!=UNOCCUPIED){
					if(pieceType<PIECE_TYPES){
						//max player, flip vertically
						int r = i>>4;
						int c = i & 0xF;
						int rf = ROWS-1-r;
						maxplayerTotal+=power[pieceType][(rf<<4)+c];
					}else{
						minplayerTotal+=power[pieceType-PIECE_TYPES][i];
					}
				}
			}
			return maxplayerTotal-minplayerTotal;
		}else if(over == MAX_PLAYER){
			return MAX_STATE_VALUE;
		}else{
			return MIN_STATE_VALUE;
		}

	}
	
	public int evaluateChange(Move m){
		int maxplayerGain = 0;
		int minplayerGain = 0;
		
		int from 	= (m.fromR<<4) + m.fromC;
		int to		= (m.toR<<4) + m.toC;
		if(state[to]==J) return MIN_STATE_VALUE;
		if(state[to]==J1) return MAX_STATE_VALUE;
		int rmPiece	= m.rmPiec;
		
		//this should not happen
		if(rmPiece!=state[to] || state[from] == UNOCCUPIED){
			System.out.println("ERROR: not valid remove");
			return 0; 
		}
		
		
		//row flip
		int fromRf 	= ROWS-1-m.fromR;
		int toRf	= ROWS-1-m.toR;
		int fromf	= (fromRf<<4)+m.fromC;
		int tof		= (toRf<<4)+m.toC;
		
		if(state[from]<PIECE_TYPES){	
			maxplayerGain = power[state[from]][tof]-power[state[from]][fromf];
			minplayerGain = (state[to]==UNOCCUPIED)?0:-power[state[to]-PIECE_TYPES][to];
		}else{
			minplayerGain = power[state[from]-PIECE_TYPES][to]-power[state[from]-PIECE_TYPES][from];
			maxplayerGain = (state[to]==UNOCCUPIED)?0:-power[state[to]][tof];
		}
		
		return maxplayerGain-minplayerGain;
	}
	
	public int isGameOver(){
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
		for(int i=0; i<state.length; i++){
			sb.append(state[i]+",\t");
			if((i & 0xF)==0xF){
				sb.append("\n");
			}
		}
		sb.append("game value ="+evaluate());
		return sb.toString();
	}
}
