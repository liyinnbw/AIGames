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
	public static final int H_DIRECTION = 1;
	public static final int V_DIRECTION = 2;
	public static final int D1_DIRECTION = 3;
	public static final int D2_DIRECTION = 4;
	public static final int ALL_DIRECTION = 5;
	public static final int LIVE_4 = 90;
	public static final int DOUBLE_DEAD_4 = 90;
	public static final int DEAD_4_LIVE_3 = 90;
	public static final int DOUBLE_LIVE_3 = 80;
	public static final int DEAD_3_LIVE_3 = 70;
	public static final int DEAD_4 = 60;
	public static final int LIVE_3 = 50;
	public static final int DOUBLE_DEAD_3 = 40;
	public static final int DEAD_3_LIVE_2 = 40;
	public static final int DEAD_3 = 30;
	public static final int DOUBLE_LIVE_2 = 30;
	public static final int LIVE_2 = 20;
	public static final int DEAD_2 = 10;
	public static final String REGEX_LIVE_4   = "011110";
	public static final String REGEX_DEAD_4_1 = "11110";
	public static final String REGEX_DEAD_4_2 = "11101";
	public static final String REGEX_DEAD_4_3 = "11011";
	public static final String REGEX_DEAD_4_4 = "10111";
	public static final String REGEX_DEAD_4_5 = "01111";
	public static final String REGEX_LIVE_3_1 = "011100";
	public static final String REGEX_LIVE_3_2 = "001110";
	public static final String REGEX_LIVE_3_3 = "010110";
	public static final String REGEX_LIVE_3_4 = "011010";
	public static final String REGEX_DEAD_3_1 = "00111";
	public static final String REGEX_DEAD_3_2 = "10011";
	public static final String REGEX_DEAD_3_3 = "11001";
	public static final String REGEX_DEAD_3_4 = "11100";
	public static final String REGEX_DEAD_3_5 = "10101";
	public static final String REGEX_DEAD_3_6 = "01110";
	public static final String REGEX_DEAD_3_7 = "10110";
	public static final String REGEX_DEAD_3_8 = "01011";
	public static final String REGEX_DEAD_3_9 = "01101";
	public static final String REGEX_DEAD_3_10= "11010";
	public static final String REGEX_LIVE_2_1 = "001100";
	public static final String REGEX_LIVE_2_2 = "001010";
	public static final String REGEX_LIVE_2_3 = "010100";
	public static final String REGEX_LIVE_2_4 = "010010";
	public static final String REGEX_LIVE_2_5 = "011000";
	public static final String REGEX_LIVE_2_6 = "000110";
	public static final String REGEX_DEAD_2_1 = "00011";
	public static final String REGEX_DEAD_2_2 = "00110";
	public static final String REGEX_DEAD_2_3 = "01100";
	public static final String REGEX_DEAD_2_4 = "11000";
	public static final String REGEX_DEAD_2_5 = "01001";
	public static final String REGEX_DEAD_2_6 = "10010";
	public static final String REGEX_DEAD_2_7 = "00101";
	public static final String REGEX_DEAD_2_8 = "01010";
	public static final String REGEX_DEAD_2_9 = "10100";
	public static final String REGEX_DEAD_2_10= "10001";
	public static final int	BIT_LIVE_4   = Integer.parseInt(REGEX_LIVE_4,2);
	public static final int BIT_DEAD_4_1 = Integer.parseInt(REGEX_DEAD_4_1,2); 
	public static final int BIT_DEAD_4_2 = Integer.parseInt(REGEX_DEAD_4_2,2); 
	public static final int BIT_DEAD_4_3 = Integer.parseInt(REGEX_DEAD_4_3,2); 
	public static final int BIT_DEAD_4_4 = Integer.parseInt(REGEX_DEAD_4_4,2); 
	public static final int BIT_DEAD_4_5 = Integer.parseInt(REGEX_DEAD_4_5,2);
	public static final int	BIT_LIVE_3_1 = Integer.parseInt(REGEX_LIVE_3_1,2);
	public static final int	BIT_LIVE_3_2 = Integer.parseInt(REGEX_LIVE_3_2,2);
	public static final int	BIT_LIVE_3_3 = Integer.parseInt(REGEX_LIVE_3_3,2);
	public static final int	BIT_LIVE_3_4 = Integer.parseInt(REGEX_LIVE_3_4,2);
	public static final int	BIT_DEAD_3_1 = Integer.parseInt(REGEX_DEAD_3_1,2);
	public static final int	BIT_DEAD_3_2 = Integer.parseInt(REGEX_DEAD_3_2,2);
	public static final int	BIT_DEAD_3_3 = Integer.parseInt(REGEX_DEAD_3_3,2);
	public static final int	BIT_DEAD_3_4 = Integer.parseInt(REGEX_DEAD_3_4,2);
	public static final int	BIT_DEAD_3_5 = Integer.parseInt(REGEX_DEAD_3_5,2);
	public static final int	BIT_DEAD_3_6 = Integer.parseInt(REGEX_DEAD_3_6,2);
	public static final int	BIT_DEAD_3_7 = Integer.parseInt(REGEX_DEAD_3_7,2);
	public static final int	BIT_DEAD_3_8 = Integer.parseInt(REGEX_DEAD_3_8,2);
	public static final int	BIT_DEAD_3_9 = Integer.parseInt(REGEX_DEAD_3_9,2);
	public static final int	BIT_DEAD_3_10= Integer.parseInt(REGEX_DEAD_3_10,2);
	public static final int	BIT_LIVE_2_1 = Integer.parseInt(REGEX_LIVE_2_1,2);
	public static final int	BIT_LIVE_2_2 = Integer.parseInt(REGEX_LIVE_2_2,2);
	public static final int	BIT_LIVE_2_3 = Integer.parseInt(REGEX_LIVE_2_3,2);
	public static final int	BIT_LIVE_2_4 = Integer.parseInt(REGEX_LIVE_2_4,2);
	public static final int	BIT_LIVE_2_5 = Integer.parseInt(REGEX_LIVE_2_5,2);
	public static final int	BIT_LIVE_2_6 = Integer.parseInt(REGEX_LIVE_2_6,2);
	public static final int	BIT_DEAD_2_1 = Integer.parseInt(REGEX_DEAD_2_1,2);
	public static final int	BIT_DEAD_2_2 = Integer.parseInt(REGEX_DEAD_2_2,2);
	public static final int	BIT_DEAD_2_3 = Integer.parseInt(REGEX_DEAD_2_3,2);
	public static final int	BIT_DEAD_2_4 = Integer.parseInt(REGEX_DEAD_2_4,2);
	public static final int	BIT_DEAD_2_5 = Integer.parseInt(REGEX_DEAD_2_5,2);
	public static final int	BIT_DEAD_2_6 = Integer.parseInt(REGEX_DEAD_2_6,2);
	public static final int	BIT_DEAD_2_7 = Integer.parseInt(REGEX_DEAD_2_7,2);
	public static final int	BIT_DEAD_2_8 = Integer.parseInt(REGEX_DEAD_2_8,2);
	public static final int	BIT_DEAD_2_9 = Integer.parseInt(REGEX_DEAD_2_9,2);
	public static final int	BIT_DEAD_2_10= Integer.parseInt(REGEX_DEAD_2_10,2);
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
		initValue();
		

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
		//updateValue(y,x,currSide);
		setCurrSide(1-currSide);
		return true;
	}
	public boolean makeNullMove(){
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
		//updateValue(y,x,currSide);
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
	public List<Point> checkMustMoves(List<Point> nexts){
		List<Point> winMoves = new ArrayList<Point>();
		List<Point> blockWinMoves = new ArrayList<Point>();
		List<Point> threatMoves = new ArrayList<Point>();
		List<Point> blockThreatMoves = new ArrayList<Point>();
		
		for(Point p: nexts){
			int valueThisSide = evaluatePos((int) p.getY(), (int) p.getX(), currSide, ALL_DIRECTION);
			int valueOtherSide = evaluatePos((int) p.getY(), (int) p.getX(), 1-currSide, ALL_DIRECTION);
			
			if(valueThisSide == DEAD_4 || valueThisSide == LIVE_4){
				winMoves.add(p);
				return winMoves;
			}else if(valueOtherSide == DEAD_4 || valueOtherSide == LIVE_4){
				blockWinMoves.add(p);
			}
			else if(valueOtherSide >= LIVE_3){
				blockThreatMoves.add(p);
			}else if(valueThisSide >= DEAD_3){
				threatMoves.add(p);
			}
		}
		
		if(blockWinMoves.size()>0){
			return blockWinMoves;
		}
		
		if(blockThreatMoves.size()>0){
			for(Point p: threatMoves){
				blockThreatMoves.add(p);
			}
			return blockThreatMoves;
		}
		return nexts;
	}
	public List<Point> nextPossibleMoves(){
		List<Point> nexts = new ArrayList<Point>();
		if(isGameOver()!=-1) return nexts;
		
		int occupied[] = new int[ROWS];
		for(int i=0; i<ROWS; i++){
			occupied[i]=state[MAX_PLAYER][i] | state[MIN_PLAYER][i];
		}
		
		/*
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
		*/
		for(int i=0; i<ROWS; i++){
			int row = occupied[i];
			for(int j=0; j<COLS; j++){
				//if(Math.abs(latestR-i)<2 && Math.abs(latestC-j)<2) continue;
				int newRow = row | colMask[j];
				if (newRow!=row && !isTooFar(occupied,i,j)){
					nexts.add(new Point(j, i));
				}
	 		}
		}
		
		nexts = checkMustMoves(nexts);
		
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
					int maxplayerValue=evaluatePos(i,j,MAX_PLAYER, ALL_DIRECTION);
					maxplayerTotal+=(maxplayerValue+1);
				}
				else if((state[MIN_PLAYER][i] & colMask[j])!=0){
					int minplayerValue=0-evaluatePos(i,j,MIN_PLAYER, ALL_DIRECTION);
					minplayerTotal+=(minplayerValue-1);
				}
				
				//maxplayerTotal += value[MAX_PLAYER][i][j][0];
				//minplayerTotal -= value[MIN_PLAYER][i][j][0];
				
			}
		}
		
		return maxplayerTotal+minplayerTotal;//maxplayerTotal/5*2+minplayerTotal/5*3;

	}
	
	//TODO: except for the pos newly added, the rest only need to update 1 direction and reevaluate pattern.
	public int updateValue(int r, int c, int side){
		
		//evaluate the newly changed pos
		if((state[side][r] & colMask[c])==0){
			for(int i=0; i<5; i++){
				value[side][r][c][i]=0;
			}
		}else{
			int newPosValue = evaluatePos(r,c,side, ALL_DIRECTION);
		}
		
		//update horizontally affected pos
		for(int i=0; i<COLS; i++){
			if(i!=c){
				if((state[MAX_PLAYER][r] & colMask[i])!=0){
					evaluatePos(r,i, MAX_PLAYER, H_DIRECTION);
				}
				else if((state[MIN_PLAYER][r] & colMask[i])!=0){
					evaluatePos(r,i, MIN_PLAYER, H_DIRECTION);
				}
				
			}
		}
		
		//update vertically affected pos
		for(int i=0; i<ROWS; i++){
			if(i!=r){
				if((state[MAX_PLAYER][i] & colMask[c])!=0){
					evaluatePos(i,c, MAX_PLAYER, V_DIRECTION);
				}
				else if((state[MIN_PLAYER][i] & colMask[c])!=0){
					evaluatePos(i,c, MIN_PLAYER, V_DIRECTION);
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
				evaluatePos(row,col, MAX_PLAYER, D1_DIRECTION );
			}
			else if((state[MIN_PLAYER][row] & colMask[col])!=0){
				evaluatePos(row,col, MIN_PLAYER, D1_DIRECTION );
			}
		}
		for(int i=1; i<ROWS; i++){
			if(r+i>=ROWS || c+i>=COLS){
				break;
			}
			int row = r+i;
			int col = c+i;
			if((state[MAX_PLAYER][row] & colMask[col])!=0){
				evaluatePos(row,col, MAX_PLAYER, D1_DIRECTION );
			}
			else if((state[MIN_PLAYER][row] & colMask[col])!=0){
				evaluatePos(row,col, MIN_PLAYER, D1_DIRECTION );
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
				evaluatePos(row,col, MAX_PLAYER, D2_DIRECTION );
			}
			else if((state[MIN_PLAYER][row] & colMask[col])!=0){
				evaluatePos(row,col, MIN_PLAYER, D2_DIRECTION);
			}
		}
		for(int i=1; i<ROWS; i++){
			if(r+i>=ROWS || c-i<0){
				break;
			}
			int row = r+i;
			int col = c-i;
			if((state[MAX_PLAYER][row] & colMask[col])!=0){
				evaluatePos(row,col, MAX_PLAYER, D2_DIRECTION);
			}
			else if((state[MIN_PLAYER][row] & colMask[col])!=0){
				evaluatePos(row,col, MIN_PLAYER, D2_DIRECTION);
			}
		}
		
		return 0;
	}
	public int testDiagonal(int r, int c, int side){
		//check diagonal /
		int thisSideBits = 0;
		int otherSideBits = 0;
		boolean isFirst = true;
		int len = 0;
		for(int i=ROWS-1; i>=0; i--){
			if(r-i<0 || c+i>=COLS){
				continue;
			}
			if(isFirst){
				isFirst = false;
			}else{
				thisSideBits =  thisSideBits << 1;
				otherSideBits =  otherSideBits << 1;
			}
			if((state[side][r-i] & colMask[c+i])!=0){
				thisSideBits |= 1;
			}else if((state[1-side][r-i] & colMask[c+i])!=0){
				otherSideBits |= 1;
			}
			len++;
		}
		int count = 0;
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
			count++;
			len++;
		}
		System.out.println("this side bit = "+intToRow(thisSideBits)+" pos from right = "+count);
		System.out.println("other side bit = "+intToRow(otherSideBits));
		return evaluatePattern(thisSideBits, otherSideBits, count,len);
	}
	public int evaluatePos(int r, int c, int side, int direction){
		int thisSideBits = 0;
		int otherSideBits = 0;
		if(direction== H_DIRECTION  || direction == ALL_DIRECTION ){
			//check horizontal
			thisSideBits = state[side][r];
			otherSideBits = state[1-side][r];
			value[side][r][c][1] = evaluatePattern(thisSideBits, otherSideBits, COLS-1-c, COLS);
		}
		
		if(direction== V_DIRECTION  || direction == ALL_DIRECTION ){
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
			value[side][r][c][2] = evaluatePattern(thisSideBits, otherSideBits, ROWS-1-r, ROWS);
		}
		
		if(direction== D1_DIRECTION  || direction == ALL_DIRECTION ){
			//check diagonal \
			thisSideBits = 0;
			otherSideBits = 0;
			boolean isFirst = true;
			int len = 0;
			for(int i=ROWS-1; i>=0; i--){
				if(r-i<0 || c-i<0){
					continue;
				}
				if(isFirst){
					isFirst=false;
				}else{
					thisSideBits =  thisSideBits << 1;
					otherSideBits =  otherSideBits << 1;
				}
				if((state[side][r-i] & colMask[c-i])!=0){
					thisSideBits |= 1;
				}else if((state[1-side][r-i] & colMask[c-i])!=0){
					otherSideBits |= 1;
				}
				len++;
			}
			int count = 0;
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
				count++;
				len++;
			}
			value[side][r][c][3] = evaluatePattern(thisSideBits, otherSideBits, count, len);
		}
		
		if(direction== D2_DIRECTION  || direction == ALL_DIRECTION ){
			//check diagonal /
			thisSideBits = 0;
			otherSideBits = 0;
			boolean isFirst = true;
			int len = 0;
			for(int i=ROWS-1; i>=0; i--){
				if(r-i<0 || c+i>=COLS){
					continue;
				}
				if(isFirst){
					isFirst=false;
				}else{
					thisSideBits =  thisSideBits << 1;
					otherSideBits =  otherSideBits << 1;
				}
				if((state[side][r-i] & colMask[c+i])!=0){
					thisSideBits |= 1;
				}else if((state[1-side][r-i] & colMask[c+i])!=0){
					otherSideBits |= 1;
				}
				len++;
			}
			int count = 0;
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
				count++;
				len++;
			}
			value[side][r][c][4] = evaluatePattern(thisSideBits, otherSideBits, count, len);
		}
		
		int values[] = new int[4];
		values[0] = value[side][r][c][1];
		values[1] = value[side][r][c][2];
		values[2] = value[side][r][c][3];
		values[3] = value[side][r][c][4];
		//for(int i=0; i<values.length; i++){
		//	System.out.println("direction "+i+": "+values[i]);
		//}
		int overall = evaluateValues(values);
		
		value[side][r][c][0]= overall+1;
		
		return overall;
	}
	public int evaluatePattern(int thisSidePattern, int otherSidePattern, int pos, int len){
		int maskLen = 6;
		for(int i=0; i<maskLen; i++){
			if(i>pos) break;
			if(pos-i+maskLen>len) continue;
			int mask = (BIT_MASK_6 << pos) >> i;
			int blocked = otherSidePattern & mask;
			if (blocked!=0) continue;
			int checkRegion = thisSidePattern & mask;
			
			int live4 = (BIT_LIVE_4 << pos) >> i;
			int result = checkRegion ^ live4;
			if(result==0) return LIVE_4;
		}
		maskLen = 5;
		for(int i=0; i<maskLen; i++){
			if(i>pos) break;
			if(pos-i+maskLen>len) continue;
			int mask = (BIT_MASK_5 << pos) >> i;
			int blocked = otherSidePattern & mask;
			if (blocked!=0) continue;
			int checkRegion = thisSidePattern & mask;
			
			int dead4_1 = (BIT_DEAD_4_1 << pos) >> i;
			int result = checkRegion ^ dead4_1;
			if(result==0) return DEAD_4;
			int dead4_2 = (BIT_DEAD_4_2 << pos) >> i;
			result = checkRegion ^ dead4_2;
			if(result==0) return DEAD_4;
			int dead4_3 = (BIT_DEAD_4_3 << pos) >> i;
			result = checkRegion ^ dead4_3;
			if(result==0) return DEAD_4;
			int dead4_4 = (BIT_DEAD_4_4 << pos) >> i;
			result = checkRegion ^ dead4_4;
			if(result==0) return DEAD_4;
			int dead4_5 = (BIT_DEAD_4_5 << pos) >> i;
			result = checkRegion ^ dead4_5;
			if(result==0) return DEAD_4;
		}
		/*
		maskLen = 5;
		for(int i=0; i<maskLen; i++){
			if(i>pos) break;
			if(pos-i+maskLen> len) continue;
			int mask = (BIT_MASK_5 << pos) >> i;
			int blocked = otherSidePattern & mask;
			if (blocked!=0) continue;
			int checkRegion = thisSidePattern & mask;
			
			int live3_2 = (BIT_LIVE_3_2 << pos) >> i;
			int result = checkRegion ^ live3_2;
			if(result==0) return LIVE_3;
		}
		*/
		maskLen = 6;
		for(int i=0; i<maskLen; i++){
			if(i>pos) break;
			if(pos-i+maskLen> len) continue;
			int mask = (BIT_MASK_6 << pos) >> i;
			int blocked = otherSidePattern & mask;
			if (blocked!=0) continue;
			int checkRegion = thisSidePattern & mask;
			
			int live3_1 = (BIT_LIVE_3_1 << pos) >> i;
			int result = checkRegion ^ live3_1;
			if(result==0) return LIVE_3;
			int live3_2 = (BIT_LIVE_3_2 << pos) >> i;
			result = checkRegion ^ live3_2;
			if(result==0) return LIVE_3;
			int live3_3 = (BIT_LIVE_3_3 << pos) >> i;
			result = checkRegion ^ live3_3;
			if(result==0) return LIVE_3;
			int live3_4 = (BIT_LIVE_3_4 << pos) >> i;
			result = checkRegion ^ live3_4;
			if(result==0) return LIVE_3;
		}
		
		maskLen = 5;
		for(int i=0; i<maskLen; i++){
			if(i>pos) break;
			if(pos-i+maskLen> len) continue;
			int mask = (BIT_MASK_5 << pos) >> i;
			int blocked = otherSidePattern & mask;
			if (blocked!=0) continue;
			int checkRegion = thisSidePattern & mask;
			
			int dead3_1 = (BIT_DEAD_3_1 << pos) >> i;
			int result = checkRegion ^ dead3_1;
			if(result==0) return DEAD_3;
			int dead3_2 = (BIT_DEAD_3_2 << pos) >> i;
			result = checkRegion ^ dead3_2;
			if(result==0) return DEAD_3;
			int dead3_3 = (BIT_DEAD_3_3 << pos) >> i;
			result = checkRegion ^ dead3_3;
			if(result==0) return DEAD_3;
			int dead3_4 = (BIT_DEAD_3_4 << pos) >> i;
			result = checkRegion ^ dead3_4;
			if(result==0) return DEAD_3;
			int dead3_5 = (BIT_DEAD_3_5 << pos) >> i;
			result = checkRegion ^ dead3_5;
			if(result==0) return DEAD_3;
			int dead3_6 = (BIT_DEAD_3_6 << pos) >> i;
			result = checkRegion ^ dead3_6;
			if(result==0) return DEAD_3;
			int dead3_7 = (BIT_DEAD_3_7 << pos) >> i;
			result = checkRegion ^ dead3_7;
			if(result==0) return DEAD_3;
			int dead3_8 = (BIT_DEAD_3_8 << pos) >> i;
			result = checkRegion ^ dead3_8;
			if(result==0) return DEAD_3;
			int dead3_9 = (BIT_DEAD_3_9 << pos) >> i;
			result = checkRegion ^ dead3_9;
			if(result==0) return DEAD_3;
			int dead3_10= (BIT_DEAD_3_10<< pos) >> i;
			result = checkRegion ^ dead3_10;
			if(result==0) return DEAD_3;
		}
		
		maskLen = 6;
		for(int i=0; i<maskLen; i++){
			if(i>pos) break;
			if(pos-i+maskLen> len) continue;
			int mask = (BIT_MASK_6 << pos) >> i;
			int blocked = otherSidePattern & mask;
			if (blocked!=0) continue;
			int checkRegion = thisSidePattern & mask;
			
			int live2_1 = (BIT_LIVE_2_1 << pos) >> i;
			int result = checkRegion ^ live2_1;
			if(result==0) return LIVE_2;
			int live2_2 = (BIT_LIVE_2_2 << pos) >> i;
			result = checkRegion ^ live2_2;
			if(result==0) return LIVE_2;
			int live2_3 = (BIT_LIVE_2_3 << pos) >> i;
			result = checkRegion ^ live2_3;
			if(result==0) return LIVE_2;
			int live2_4 = (BIT_LIVE_2_4 << pos) >> i;
			result = checkRegion ^ live2_4;
			if(result==0) return LIVE_2;
			int live2_5 = (BIT_LIVE_2_5 << pos) >> i;
			result = checkRegion ^ live2_5;
			if(result==0) return LIVE_2;
			int live2_6 = (BIT_LIVE_2_6 << pos) >> i;
			result = checkRegion ^ live2_6;
			if(result==0) return LIVE_2;
		}
		
		maskLen = 5;
		for(int i=0; i<maskLen; i++){
			if(i>pos) break;
			if(pos-i+maskLen> len) continue;
			int mask = (BIT_MASK_5 << pos) >> i;
			int blocked = otherSidePattern & mask;
			if (blocked!=0) continue;
			int checkRegion = thisSidePattern & mask;
			
			int dead2_1 = (BIT_DEAD_2_1 << pos) >> i;
			int result = checkRegion ^ dead2_1;
			if(result==0) return DEAD_2;
			int dead2_2 = (BIT_DEAD_2_2 << pos) >> i;
			result = checkRegion ^ dead2_2;
			if(result==0) return DEAD_2;
			int dead2_3 = (BIT_DEAD_2_3 << pos) >> i;
			result = checkRegion ^ dead2_3;
			if(result==0) return DEAD_2;
			int dead2_4 = (BIT_DEAD_2_4 << pos) >> i;
			result = checkRegion ^ dead2_4;
			if(result==0) return DEAD_2;
			int dead2_5 = (BIT_DEAD_2_5 << pos) >> i;
			result = checkRegion ^ dead2_5;
			if(result==0) return DEAD_2;
			int dead2_6 = (BIT_DEAD_2_6 << pos) >> i;
			result = checkRegion ^ dead2_6;
			if(result==0) return DEAD_2;
			int dead2_7 = (BIT_DEAD_2_7 << pos) >> i;
			result = checkRegion ^ dead2_7;
			if(result==0) return DEAD_2;
			int dead2_8 = (BIT_DEAD_2_8 << pos) >> i;
			result = checkRegion ^ dead2_8;
			if(result==0) return DEAD_2;
			int dead2_9 = (BIT_DEAD_2_9 << pos) >> i;
			result = checkRegion ^ dead2_9;
			if(result==0) return DEAD_2;
			int dead2_10 = (BIT_DEAD_2_10 << pos) >> i;
			result = checkRegion ^ dead2_10;
			if(result==0) return DEAD_2;
		}
		
		return 0;		
	}
	
	public int dead2_5test (int thisSidePattern, int otherSidePattern, int pos){
		for(int i=0; i<6; i++){
			if(i>pos) break;
			System.out.println("i = "+i);
			int mask = (BIT_MASK_6 << pos) >> i;
			int blocked = otherSidePattern & mask;
			System.out.println("mask   = "+intToRow(mask));
			System.out.println("bocked = "+intToRow(blocked));
			if (blocked!=0) continue;
			int checkRegion = thisSidePattern & mask;
			System.out.println("check  = "+intToRow(checkRegion));
			int dead2_5 = (BIT_DEAD_2_5 << pos) >> i;
			System.out.println("dead2_5= "+intToRow(dead2_5));
			int result = checkRegion ^ dead2_5;
			System.out.println("result = "+intToRow(result));
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
		if(countLive2>0 && countDead3>0){
			return Math.max(max,DEAD_3_LIVE_2);
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
