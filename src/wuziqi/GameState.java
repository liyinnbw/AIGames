package wuziqi;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameState {
	public static final int MAX_PLAYER = 0;
	public static final int MIN_PLAYER = 1;
	public static final int MIN_STATE_VALUE = 0-Integer.MAX_VALUE;
	public static final int MAX_STATE_VALUE = Integer.MAX_VALUE;
	public static final int TIE = 2;
	public static int WIN_CONNECT = 5;
	public static final int LIVE_4 = 90;
	public static final int DOUBLE_DEAD_4 = 90;
	public static final int DEAD_4_LIVE_3 = 90;
	public static final int DOUBLE_LIVE_3 = 80;
	public static final int DEAD_3_LIVE_3 = 70;
	public static final int DEAD_4 = 60;
	public static final int LIVE_3 = 50;
	public static final int DOUBLE_LIVE_2 = 40;
	public static final int DOUBLE_DEAD_3 = 40;
	public static final int DEAD_3 = 30;
	public static final int LIVE_2 = 20;
	public static final int DEAD_2 = 10;
	public static final String REGEX_LIVE_4 = "011110";
	public static final String REGEX_DEAD_4_1 = "11110";
	public static final String REGEX_DEAD_4_2 = "11101";
	public static final String REGEX_DEAD_4_3 = "11011";
	public static final String REGEX_DEAD_4_4 = "10111";
	public static final String REGEX_DEAD_4_5 = "01111";
	public static final String REGEX_LIVE_3 = "0011100";
	public static final String REGEX_DEAD_3_1 = "00111";
	public static final String REGEX_DEAD_3_2 = "10011";
	public static final String REGEX_DEAD_3_3 = "11001";
	public static final String REGEX_DEAD_3_4 = "11100";
	public static final String REGEX_DEAD_3_5 = "10101";
	public static final String REGEX_DEAD_3_6 = "01110";
	public static final String REGEX_DEAD_3_7 = "010110";
	public static final String REGEX_DEAD_3_8 = "011010";
	public static final String REGEX_LIVE_2 = "00011000";
	public static final String REGEX_DEAD_2_1 = "00011";
	public static final String REGEX_DEAD_2_2 = "00110";
	public static final String REGEX_DEAD_2_3 = "01100";
	public static final String REGEX_DEAD_2_4 = "11000";
	public static final String REGEX_DEAD_2_5 = "010010";
	public static final String REGEX_DEAD_2_6 = "0010100";
	public static final int	BIT_LIVE_4   = Integer.parseInt(REGEX_LIVE_4,2);
	public static final int BIT_DEAD_4_1 = Integer.parseInt(REGEX_DEAD_4_1,2); 
	public static final int BIT_DEAD_4_2 = Integer.parseInt(REGEX_DEAD_4_2,2); 
	public static final int BIT_DEAD_4_3 = Integer.parseInt(REGEX_DEAD_4_3,2); 
	public static final int BIT_DEAD_4_4 = Integer.parseInt(REGEX_DEAD_4_4,2); 
	public static final int BIT_DEAD_4_5 = Integer.parseInt(REGEX_DEAD_4_5,2);
	public static final int	BIT_LIVE_3   = Integer.parseInt(REGEX_LIVE_3,2);
	public static final int	BIT_DEAD_3_1 = Integer.parseInt(REGEX_DEAD_3_1,2);
	public static final int	BIT_DEAD_3_2 = Integer.parseInt(REGEX_DEAD_3_2,2);
	public static final int	BIT_DEAD_3_3 = Integer.parseInt(REGEX_DEAD_3_3,2);
	public static final int	BIT_DEAD_3_4 = Integer.parseInt(REGEX_DEAD_3_4,2);
	public static final int	BIT_DEAD_3_5 = Integer.parseInt(REGEX_DEAD_3_5,2);
	public static final int	BIT_DEAD_3_6 = Integer.parseInt(REGEX_DEAD_3_6,2);
	public static final int	BIT_DEAD_3_7 = Integer.parseInt(REGEX_DEAD_3_7,2);
	public static final int	BIT_DEAD_3_8 = Integer.parseInt(REGEX_DEAD_3_8,2);
	public static final int	BIT_LIVE_2   = Integer.parseInt(REGEX_LIVE_2,2);
	public static final int	BIT_DEAD_2_1 = Integer.parseInt(REGEX_DEAD_2_1,2);
	public static final int	BIT_DEAD_2_2 = Integer.parseInt(REGEX_DEAD_2_2,2);
	public static final int	BIT_DEAD_2_3 = Integer.parseInt(REGEX_DEAD_2_3,2);
	public static final int	BIT_DEAD_2_4 = Integer.parseInt(REGEX_DEAD_2_4,2);
	public static final int	BIT_DEAD_2_5 = Integer.parseInt(REGEX_DEAD_2_5,2);
	public static final int	BIT_DEAD_2_6 = Integer.parseInt(REGEX_DEAD_2_6,2);
	public static final int	BIT_MASK_8 = Integer.parseInt("11111111",2);
	public static final int	BIT_MASK_7 = Integer.parseInt("1111111",2);
	public static final int	BIT_MASK_6 = Integer.parseInt("111111",2);
	public static final int	BIT_MASK_5 = Integer.parseInt("11111",2);
	public static final int	BIT_MASK_4 = Integer.parseInt("1111",2);
	
	
	private int ROWS;		//tentatively maximum 32
	private int COLS;		//tentatively maximum 32
	private int currSide;
	private int[][] state;	//[0][]: max player [1][]: min player
	private int[] colMask;	//for extracting bit at specific column of a row
	private int[][][][] value;//SIDE,ROW,COL,DIRECTION
	private Stack<Point> moves;

	public Stack<Point> getMoves() {
		return moves;
	}
	public Stack<Point> copyStack(Stack<Point> src){
		Stack<Point> dest = new Stack<Point>();
		for(Point p: src){
			dest.push(p);
		}
		return dest;
	}
	public void setMoves(Stack<Point> moves) {
		this.moves = copyStack(moves);
	}

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
	public int[][][][] getValue() {
		return value;
	}

	public void setValue(int[][][][] v) {
		value = new int[2][ROWS][COLS][5]; 
		for(int k=0; k<2; k++){
			for(int i=0; i<ROWS; i++){
				for(int j=0; j<COLS; j++){
					System.arraycopy(v[k][i][j], 0, value[k][i][j], 0, 5);
				}
			}
		}
	}
	public void initValue(){
		value = new int[2][ROWS][COLS][5];
	}
	public void initMoves(){
		moves = new Stack<Point>();
	}

	public GameState(int r, int c, int side){
		setRows(r);
		setCols(c);
		setCurrSide(side);
		initState();
		initMoves();
		//initValue();
		

	}
	public GameState(int r, int c, int side, int[][] state, Stack<Point> moves){//, int[][][][] value){
		setRows(r);
		setCols(c);
		setCurrSide(side);
		setGameState(state);
		setMoves(moves);
		//setValue (value);

	}
	
	public boolean addPiece(int x, int y){
		if(y>=ROWS || x>= COLS || x<0 || y<0) return false;
		
		int bRow = state[MAX_PLAYER][y];
		int wRow = state[MIN_PLAYER][y];	
		int bitMap = 1<<COLS-x-1;
		int wBit = wRow & bitMap;
		int bBit = bRow & bitMap;
		
		//already occupied
		if(wBit!=0 || bBit!=0) return false;
		state[currSide][y] |= bitMap;
		moves.push(new Point(x,y));

		//updateValue(y,x);
		setCurrSide(1-currSide);
		return true;
	}
	public boolean revertOneMove(){
		
		if(moves.isEmpty()) return false;
		
		Point lastMove = moves.pop();
		int x = (int) lastMove.getX();
		int y = (int) lastMove.getY();
		int bitMask = ~ colMask[x];
		state[1-currSide][y] = state[1-currSide][y] & bitMask;
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
	public List<Point> nextPossibleMoves(){
		List<Point> nexts = new ArrayList<Point>();
		if(isGameOver()!=-1) return nexts;
		
		int occupied[] = new int[ROWS];
		for(int i=0; i<ROWS; i++){
			occupied[i]=state[MAX_PLAYER][i] | state[MIN_PLAYER][i];
		}
		
		//prioritize the latestMove first, maximum 8 possibilities
		Point latestMove = moves.peek();
		int latestR = (int) latestMove.getY();
		int latestC = (int) latestMove.getX();
		
		for(int i=-1; i<=1; i++){
			if(latestR+i<0 || latestR+i>= ROWS) continue;
			int row = occupied[latestR+i];
			for(int j=-1; j<=1; j++){
				if(i==0 && j==0) continue;
				if(latestC+j<0 || latestC+j>=COLS) continue;
				int newRow = row | colMask[latestC+j];
				if(newRow != row){
					nexts.add(new Point(latestC+j, latestR+i));
				}
			}
		}
		
		for(int i=0; i<ROWS; i++){
			int row = occupied[i];
			for(int j=0; j<COLS; j++){
				if(Math.abs(latestR-i)<2 && Math.abs(latestC-j)<2) continue;
				int newRow = row | colMask[j];
				if (newRow!=row && !isTooFar(occupied,i,j)){
					nexts.add(new Point(j, i));
				}
	 		}
		}
		
		return nexts;
		
	}
	public List<GameState> nextPossibleStates(){
		List<GameState> nexts = new ArrayList<GameState>();
		if(isGameOver()!=-1) return nexts;
		
		int occupied[] = new int[ROWS];
		for(int i=0; i<ROWS; i++){
			occupied[i]=state[0][i] | state[1][i];
		}

		
		//prioritize the latestMove first, maximum 8 possibilities
		Point latestMove = moves.peek();
		int latestR = (int) latestMove.getY();
		int latestC = (int) latestMove.getX();
		//System.out.println("latest R = "+latestR+" latest C = "+latestC );

		for(int i=-1; i<=1; i++){
			if(latestR+i<0 || latestR+i>= ROWS) continue;
			int row = occupied[latestR+i];
			for(int j=-1; j<=1; j++){
				if(latestC+j<0 || latestC+j>=COLS) continue;
				if(i==0 && j==0) continue;
				int newRow = row | colMask[latestC+j];
				if(newRow != row){
					GameState s = new GameState(ROWS, COLS, currSide, state, moves);//, value);
					s.addPiece(latestC+j,latestR+i); //addPiece uses grid coordinates, so j,i
					nexts.add(s);
				}
			}
		}
		
		for(int i=0; i<ROWS; i++){
			int row = occupied[i];
			for(int j=0; j<COLS; j++){
				if(Math.abs(latestR-i)<2 && Math.abs(latestC-j)<2) continue;
				int newRow = row | colMask[j];
				if (newRow!=row && !isTooFar(occupied,i,j)){
					GameState s = new GameState(ROWS, COLS, currSide, state, moves);//, value);
					s.addPiece(j,i); //addPiece uses grid coordinates, so j,i
					nexts.add(s);
				}
	 		}
		}
		return nexts;
	}

	public int evaluate(){
		
		if(checkConnect(MAX_PLAYER,WIN_CONNECT)) return MAX_STATE_VALUE;
		if(checkConnect(MIN_PLAYER,WIN_CONNECT)) return MIN_STATE_VALUE;
		
		int maxplayerTotal = 0;
		int minplayerTotal = 0;
		
		
		for(int i=0; i<ROWS; i++){
			for(int j=0; j<COLS; j++){
				
				
				//only need to calculate for non-zero pos
				if((state[MAX_PLAYER][i] & colMask[j])!=0){
					int maxplayerValue=evaluatePos(i,j,MAX_PLAYER);
					maxplayerTotal+=(maxplayerValue+1);
				}
				else if((state[MIN_PLAYER][i] & colMask[j])!=0){
					int minplayerValue=0-evaluatePos(i,j,MIN_PLAYER);
					minplayerTotal+=(minplayerValue-1);
				}
				
				//maxplayerTotal += value[MAX_PLAYER][i][j][0];
				//minplayerTotal -= value[MIN_PLAYER][i][j][0];
				
			}
		}
		
		return maxplayerTotal+minplayerTotal;//maxplayerTotal/5*2+minplayerTotal/5*3;

	}
	
	//TODO: except for the pos newly added, the rest only need to update 1 direction and reevaluate pattern.
	public int updateValue(int r, int c){
		
		//evaluate the newly added pos
		int newPosValue = evaluatePos(r,c,currSide);
		
		//update horizontally affected pos
		for(int i=0; i<COLS; i++){
			if(i!=c){
				if((state[MAX_PLAYER][r] & colMask[i])!=0){
					evaluatePos(r,i, MAX_PLAYER);
				}
				else if((state[MIN_PLAYER][r] & colMask[i])!=0){
					evaluatePos(r,i, MIN_PLAYER);
				}
				
			}
		}
		
		//update vertically affected pos
		for(int i=0; i<ROWS; i++){
			if(i!=r){
				if((state[MAX_PLAYER][i] & colMask[c])!=0){
					evaluatePos(i,c, MAX_PLAYER);
				}
				else if((state[MIN_PLAYER][i] & colMask[c])!=0){
					evaluatePos(i,c, MIN_PLAYER);
				}
			}	
		}
		
		//update diagonal \
		for(int i=1; i<ROWS; i++){
			if(r-i<0 || c-i<0){
				break;
			}
			int row = r-i;
			int col = c-i;
			if((state[MAX_PLAYER][row] & colMask[col])!=0){
				evaluatePos(row,col, MAX_PLAYER);
			}
			else if((state[MIN_PLAYER][row] & colMask[col])!=0){
				evaluatePos(row,col, MIN_PLAYER);
			}
		}
		for(int i=1; i<ROWS; i++){
			if(r+i>=ROWS || c+i>=COLS){
				break;
			}
			int row = r+i;
			int col = c+i;
			if((state[MAX_PLAYER][row] & colMask[col])!=0){
				evaluatePos(row,col, MAX_PLAYER);
			}
			else if((state[MIN_PLAYER][row] & colMask[col])!=0){
				evaluatePos(row,col, MIN_PLAYER);
			}
		}
		
		//update diagonal /
		for(int i=1; i<ROWS; i++){
			if(r-i<0 || c+i>=COLS){
				break;
			}
			int row = r-i;
			int col = c+i;
			if((state[MAX_PLAYER][row] & colMask[col])!=0){
				evaluatePos(row,col, MAX_PLAYER);
			}
			else if((state[MIN_PLAYER][row] & colMask[col])!=0){
				evaluatePos(row,col, MIN_PLAYER);
			}
		}
		for(int i=1; i<ROWS; i++){
			if(r+i>=ROWS || c-i<0){
				break;
			}
			int row = r+i;
			int col = c-i;
			if((state[MAX_PLAYER][row] & colMask[col])!=0){
				evaluatePos(row,col, MAX_PLAYER);
			}
			else if((state[MIN_PLAYER][row] & colMask[col])!=0){
				evaluatePos(row,col, MIN_PLAYER);
			}
		}
		
		return 0;
	}

	public int evaluatePos(int r, int c, int side){
		int[] values = new int[4];
		
		//check horizontal
		int thisSideBits = state[side][r];
		int otherSideBits = state[1-side][r];
		values[0] = evaluatePattern(thisSideBits, otherSideBits, COLS-1-c);
		
		//check vertical
		thisSideBits = 0;
		otherSideBits = 0;
		for(int i=0; i<ROWS; i++){
			if(i>0){
				thisSideBits =  thisSideBits << 1;
				otherSideBits =  otherSideBits << 1;
			}
			int bitThisSide = state[side][i] & colMask[c];
			int bitOtherSide = state[1-side][i] & colMask[c];
			if(bitThisSide != 0){
				thisSideBits |= 1;
			}else if(bitOtherSide != 0){
				otherSideBits |= 1;
			}
		}
		values[1] = evaluatePattern(thisSideBits, otherSideBits, ROWS-1-r);
		
		//check diagonal \
		thisSideBits = 0;
		otherSideBits = 0;
		int count = 0;
		for(int i=0; i<ROWS; i++){
			if(r-i<0 || c-i<0){
				break;
			}
			if(i>0){
				thisSideBits =  thisSideBits << 1;
				otherSideBits =  otherSideBits << 1;
			}
			if((state[side][r-i] & colMask[c-i])!=0){
				thisSideBits |= 1;
			}else if((state[1-side][r-i] & colMask[c-i])!=0){
				otherSideBits |= 1;
			}
			count = i;
		}
		int idx = count-1;
		for(int i=1; i<ROWS; i++){
			if(r+i>=ROWS || c+i>=COLS){
				break;
			}
			thisSideBits =  thisSideBits << 1;
			otherSideBits =  otherSideBits << 1;
			if((state[side][r+i] & colMask[c+i])!=0){
				thisSideBits |= 1;
			}else if((state[1-side][r+i] & colMask[c+i])!=0){
				otherSideBits |= 1;
			}
		}
		values[2] = evaluatePattern(thisSideBits, otherSideBits, ROWS-1-idx);
		
		//check diagonal /
		thisSideBits = 0;
		otherSideBits = 0;
		count = 0;
		for(int i=0; i<ROWS; i++){
			if(r-i<0 || c+i>=COLS){
				break;
			}
			if(i>0){
				thisSideBits =  thisSideBits << 1;
				otherSideBits =  otherSideBits << 1;
			}
			if((state[side][r-i] & colMask[c+i])!=0){
				thisSideBits |= 1;
			}else if((state[1-side][r-i] & colMask[c+i])!=0){
				otherSideBits |= 1;
			}
			count = i;
		}
		idx = count-1;
		for(int i=1; i<ROWS; i++){
			if(r+i>=ROWS || c-i<0){
				break;
			}
			thisSideBits =  thisSideBits << 1;
			otherSideBits =  otherSideBits << 1;
			if((state[side][r+i] & colMask[c-i])!=0){
				thisSideBits |= 1;
			}else if((state[1-side][r+i] & colMask[c-i])!=0){
				otherSideBits |= 1;
			}
		}
		values[3] = evaluatePattern(thisSideBits, otherSideBits, ROWS-1-idx);
		int overall = evaluateValues(values);
		
//		value[side][r][c][0]= overall+1;
//		value[side][r][c][1]=values[0];
//		value[side][r][c][2]=values[1];
//		value[side][r][c][3]=values[2];
//		value[side][r][c][4]=values[3];
		
		return overall;
	}
	public int evaluatePattern(int thisSidePattern, int otherSidePattern, int idx ){
		
		for(int i=0; i<6; i++){
			int mask = (BIT_MASK_6 << idx) >> i;
			int blocked = otherSidePattern & mask;
			if (blocked!=0) continue;
			int checkRegion = thisSidePattern & mask;
			
			int live4 = (BIT_LIVE_4 << idx) >> i;
			int result = checkRegion ^ live4;
			if(result==0) return LIVE_4;
		}
		for(int i=0; i<5; i++){
			int mask = (BIT_MASK_5 << idx) >> i;
			int blocked = otherSidePattern & mask;
			if (blocked!=0) continue;
			int checkRegion = thisSidePattern & mask;
			
			int dead4_1 = (BIT_DEAD_4_1 << idx) >> i;
			int result = checkRegion ^ dead4_1;
			if(result==0) return DEAD_4;
			int dead4_2 = (BIT_DEAD_4_2 << idx) >> i;
			result = checkRegion ^ dead4_2;
			if(result==0) return DEAD_4;
			int dead4_3 = (BIT_DEAD_4_3 << idx) >> i;
			result = checkRegion ^ dead4_3;
			if(result==0) return DEAD_4;
			int dead4_4 = (BIT_DEAD_4_4 << idx) >> i;
			result = checkRegion ^ dead4_4;
			if(result==0) return DEAD_4;
			int dead4_5 = (BIT_DEAD_4_5 << idx) >> i;
			result = checkRegion ^ dead4_5;
			if(result==0) return DEAD_4;
		}
		
		for(int i=0; i<7; i++){
			int mask = (BIT_MASK_7 << idx) >> i;
			int blocked = otherSidePattern & mask;
			if (blocked!=0) continue;
			int checkRegion = thisSidePattern & mask;
			
			int live3 = (BIT_LIVE_3 << idx) >> i;
			int result = checkRegion ^ live3;
			if(result==0) return LIVE_3;
		}
		
		for(int i=0; i<5; i++){
			int mask = (BIT_MASK_5 << idx) >> i;
			int blocked = otherSidePattern & mask;
			if (blocked!=0) continue;
			int checkRegion = thisSidePattern & mask;
			
			int dead3_1 = (BIT_DEAD_3_1 << idx) >> i;
			int result = checkRegion ^ dead3_1;
			if(result==0) return DEAD_3;
			int dead3_2 = (BIT_DEAD_3_2 << idx) >> i;
			result = checkRegion ^ dead3_2;
			if(result==0) return DEAD_3;
			int dead3_3 = (BIT_DEAD_3_3 << idx) >> i;
			result = checkRegion ^ dead3_3;
			if(result==0) return DEAD_3;
			int dead3_4 = (BIT_DEAD_3_4 << idx) >> i;
			result = checkRegion ^ dead3_4;
			if(result==0) return DEAD_3;
			int dead3_5 = (BIT_DEAD_3_5 << idx) >> i;
			result = checkRegion ^ dead3_5;
			if(result==0) return DEAD_3;
			int dead3_6 = (BIT_DEAD_3_6 << idx) >> i;
			result = checkRegion ^ dead3_6;
			if(result==0) return DEAD_3;
		}
		
		for(int i=0; i<6; i++){
			int mask = (BIT_MASK_6 << idx) >> i;
			int blocked = otherSidePattern & mask;
			if (blocked!=0) continue;
			int checkRegion = thisSidePattern & mask;
			
			int dead3_7 = (BIT_DEAD_3_7 << idx) >> i;
			int result = checkRegion ^ dead3_7;
			if(result==0) return DEAD_3;
			int dead3_8 = (BIT_DEAD_3_8 << idx) >> i;
			result = checkRegion ^ dead3_8;
			if(result==0) return DEAD_3;
		}
		
		for(int i=0; i<8; i++){
			int mask = (BIT_MASK_8 << idx) >> i;
			int blocked = otherSidePattern & mask;
			if (blocked!=0) continue;
			int checkRegion = thisSidePattern & mask;
			
			int live2 = (BIT_LIVE_2 << idx) >> i;
			int result = checkRegion ^ live2;
			if(result==0) return LIVE_2;
		}
		
		for(int i=0; i<5; i++){
			int mask = (BIT_MASK_5 << idx) >> i;
			int blocked = otherSidePattern & mask;
			if (blocked!=0) continue;
			int checkRegion = thisSidePattern & mask;
			
			int dead2_1 = (BIT_DEAD_2_1 << idx) >> i;
			int result = checkRegion ^ dead2_1;
			if(result==0) return DEAD_2;
			int dead2_2 = (BIT_DEAD_2_2 << idx) >> i;
			result = checkRegion ^ dead2_2;
			if(result==0) return DEAD_2;
			int dead2_3 = (BIT_DEAD_2_3 << idx) >> i;
			result = checkRegion ^ dead2_3;
			if(result==0) return DEAD_2;
			int dead2_4 = (BIT_DEAD_2_4 << idx) >> i;
			result = checkRegion ^ dead2_4;
			if(result==0) return DEAD_2;
		}
		
		for(int i=0; i<6; i++){
			int mask = (BIT_MASK_6 << idx) >> i;
			int blocked = otherSidePattern & mask;
			if (blocked!=0) continue;
			int checkRegion = thisSidePattern & mask;
			
			int dead2_5 = (BIT_DEAD_2_5 << idx) >> i;
			int result = checkRegion ^ dead2_5;
			if(result==0) return DEAD_2;
		}
		
		for(int i=0; i<7; i++){
			int mask = (BIT_MASK_7 << idx) >> i;
			int blocked = otherSidePattern & mask;
			if (blocked!=0) continue;
			int checkRegion = thisSidePattern & mask;
			
			int dead2_6 = (BIT_DEAD_2_6 << idx) >> i;
			int result = checkRegion ^ dead2_6;
			if(result==0) return DEAD_2;
		}
		
		return 0;		
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
	
	public int evaluateValues (int values[]){
		int max = 0;
		int countDead4 = 0;
		int countLive3 = 0;
		int countDead3 = 0;
		int countLive2 = 0;
		for(int i=0; i<values.length; i++){
			max = Math.max(max, values[i]);
			switch(values[i]){
			case DEAD_4:
				countDead4++;
				break;
			case LIVE_3:
				countLive3++;
				break;
			case DEAD_3:
				countDead3++;
				break;
			case LIVE_2:
				countLive2++;
				break;
			default:
				break;
			}
		}
		if(countDead4>1){
			return Math.max(max,DOUBLE_DEAD_4);
		}
		if(countDead4>0 && countLive3>0){
			return Math.max(max,DEAD_4_LIVE_3);
		}
		if(countLive3>1){
			return Math.max(max,DOUBLE_LIVE_3);
		}
		if(countLive3>0 && countDead3>0){
			return Math.max(max,DEAD_3_LIVE_3);
		}
		if(countDead3>1){
			return Math.max(max,DOUBLE_DEAD_3);
		}
		if(countLive2>1){
			return Math.max(max,DOUBLE_LIVE_2);
		}
		return max;
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
