package wuziqi;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameState {
	public static final int MIN_PLAYER = 1;
	public static final int MAX_PLAYER = 0;
	public static final int MIN_STATE_VALUE = -1*Integer.MAX_VALUE;
	public static final int MAX_STATE_VALUE = Integer.MAX_VALUE;
	public static final int TIE = 2;
	public static int WIN_CONNECT = 5;
	public static final int LIVE_4 = 90;//Integer.MAX_VALUE;
	public static final int DOUBLE_DEAD_4 = 90;//Integer.MAX_VALUE;
	public static final int DEAD_4_LIVE_3 = 90;//Integer.MAX_VALUE;
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
	public static final String REGEX_DEAD_4 = "11110|11101|11011|10111|01111";
	public static final String REGEX_DEAD_4_1 = "11110";
	public static final String REGEX_DEAD_4_2 = "11101";
	public static final String REGEX_DEAD_4_3 = "11011";
	public static final String REGEX_DEAD_4_4 = "10111";
	public static final String REGEX_DEAD_4_5 = "01111";
	public static final String REGEX_LIVE_3 = "0011100";
	public static final String REGEX_DEAD_3 = "00111|10011|11001|11100|010110|011010|10101";
	public static final String REGEX_DEAD_3_1 = "00111";
	public static final String REGEX_DEAD_3_2 = "10011";
	public static final String REGEX_DEAD_3_3 = "11001";
	public static final String REGEX_DEAD_3_4 = "11100";
	public static final String REGEX_DEAD_3_5 = "10101";
	public static final String REGEX_DEAD_3_6 = "010110";
	public static final String REGEX_DEAD_3_7 = "011010";
	public static final String REGEX_LIVE_2 = "00011000";
	public static final String REGEX_DEAD_2 = "00011|11000|0010100|010010";
	public static final String REGEX_DEAD_2_1 = "00011";
	public static final String REGEX_DEAD_2_2 = "11000";
	public static final String REGEX_DEAD_2_3 = "010010";
	public static final String REGEX_DEAD_2_4 = "0010100";
	public static final Pattern PATTERN_LIVE_4 = Pattern.compile(REGEX_LIVE_4);
	public static final Pattern PATTERN_DEAD_4 = Pattern.compile(REGEX_DEAD_4);
	public static final Pattern PATTERN_LIVE_3 = Pattern.compile(REGEX_LIVE_3);
	public static final Pattern PATTERN_DEAD_3 = Pattern.compile(REGEX_DEAD_3);
	public static final Pattern PATTERN_LIVE_2 = Pattern.compile(REGEX_LIVE_2);
	public static final Pattern PATTERN_DEAD_2 = Pattern.compile(REGEX_DEAD_2);
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
	public static final int	BIT_LIVE_2   = Integer.parseInt(REGEX_LIVE_2,2);
	public static final int	BIT_DEAD_2_1 = Integer.parseInt(REGEX_DEAD_2_1,2);
	public static final int	BIT_DEAD_2_2 = Integer.parseInt(REGEX_DEAD_2_1,2);
	public static final int	BIT_DEAD_2_3 = Integer.parseInt(REGEX_DEAD_2_1,2);
	public static final int	BIT_DEAD_2_4 = Integer.parseInt(REGEX_DEAD_2_1,2);
	public static final int	BIT_MASK_8 = Integer.parseInt("11111111",2);
	public static final int	BIT_MASK_7 = Integer.parseInt("1111111",2);
	public static final int	BIT_MASK_6 = Integer.parseInt("111111",2);
	public static final int	BIT_MASK_5 = Integer.parseInt("11111",2);
	public static final int	BIT_MASK_4 = Integer.parseInt("1111",2);
	
	private int ROWS;		//tentatively maximum 32
	private int COLS;		//tentatively maximum 32
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
		
		if(checkConnect(MAX_PLAYER,WIN_CONNECT)) return MAX_STATE_VALUE;
		if(checkConnect(MIN_PLAYER,WIN_CONNECT)) return MIN_STATE_VALUE;
		
		int maxplayerTotal = 0;
		int minplayerTotal = 0;
		
		for(int i=0; i<ROWS; i++){
			for(int j=0; j<COLS; j++){
				int maxplayerValue=evaluatePos3(i,j,MAX_PLAYER);
				if(maxplayerValue==MAX_STATE_VALUE) return maxplayerValue;
				int minplayerValue=-1*evaluatePos3(i,j,MIN_PLAYER);
				if(minplayerValue==MIN_STATE_VALUE) return minplayerValue;
				
				maxplayerTotal+=maxplayerValue+1;
				minplayerTotal+=minplayerValue-1;
			}
		}
		
		return maxplayerTotal+minplayerTotal;//maxplayerTotal/5*2+minplayerTotal/5*3;
		
		//return 0;
	}
	public int evaluateAll(int side){
		int posValues[][][] = new int[ROWS][COLS][4];
		
		//check horizontal
		for(int i=0; i<ROWS; i++){
			String s = combineBitStrings(intToRow(state[side][i]),intToRow(state[1-side][i]));
			evaluatePatternHorizontal(s, i, 0, posValues);
		}
		/*
		for(int i=0; i<ROWS; i++){
			for(int j=0; j<COLS; j++){
				System.out.print(posValues[i][j][0]+"\t");
			}
			System.out.println();
		}
		*/
		
		int total = 0;
		for(int i=0; i<ROWS; i++){
			for(int j=0; j<COLS; j++){
				total+=evaluateValues(posValues[i][j])+1;
			}
		}
		
		return total;
	}
	public void evaluatePatternHorizontal(String s, int r, int type, int posValues[][][]){
		Matcher matcher = PATTERN_LIVE_4.matcher(s);
		while(matcher.find()){
			int start = matcher.start();
			int end = matcher.end();
			for(int i=start; i<end; i++){
				if(s.charAt(i)=='1'){
					posValues[r][i][type]=LIVE_4;
				}
			}
		}
		matcher = PATTERN_DEAD_4.matcher(s);
		while(matcher.find()){
			int start = matcher.start();
			int end = matcher.end();
			for(int i=start; i<end; i++){
				if(s.charAt(i)=='1'){
					posValues[r][i][type]=DEAD_4;
				}
			}
		}
		matcher = PATTERN_LIVE_3.matcher(s);
		while(matcher.find()){
			int start = matcher.start();
			int end = matcher.end();
			for(int i=start; i<end; i++){
				if(s.charAt(i)=='1'){
					posValues[r][i][type]=LIVE_3;
				}
			}
		}
		
		matcher = PATTERN_DEAD_3.matcher(s);
		while(matcher.find()){
			int start = matcher.start();
			int end = matcher.end();
			for(int i=start; i<end; i++){
				if(s.charAt(i)=='1'){
					posValues[r][i][type]=DEAD_4;
				}
			}
		}
		
		matcher = PATTERN_LIVE_2.matcher(s);
		while(matcher.find()){
			int start = matcher.start();
			int end = matcher.end();
			for(int i=start; i<end; i++){
				if(s.charAt(i)=='1'){
					posValues[r][i][type]=LIVE_2;
					System.out.println("live 2 at pos"+" r="+r+" col="+i);
				}
			}
		}
		
		matcher = PATTERN_DEAD_2.matcher(s);
		while(matcher.find()){
			System.out.println("find dead 2");
			int start = matcher.start();
			int end = matcher.end();
			for(int i=start; i<end; i++){
				if(s.charAt(i)=='1'){
					posValues[r][i][type]=DEAD_2;
					System.out.println("dead 2 at pos"+" r="+r+" col="+i);
				}
			}
		}
	}
	public int evaluatePos3(int r, int c, int side){
		int[] values = new int[4];
		
		//check horizontal
		int thisSideBits = state[side][r];
		int otherSideBits = state[1-side][r];
		values[0] = evaluatePattern3(thisSideBits, otherSideBits, COLS-1-c);
		
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
		values[1] = evaluatePattern3(thisSideBits, otherSideBits, ROWS-1-r);
		
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
		values[2] = evaluatePattern3(thisSideBits, otherSideBits, ROWS-1-idx);
		
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
		values[3] = evaluatePattern3(thisSideBits, otherSideBits, ROWS-1-idx);

		return evaluateValues(values);
	}
	public int evaluatePattern3(int thisSidePattern, int otherSidePattern, int idx ){
		
		//System.out.println("this side = "+ intToRow(thisSidePattern));
		//System.out.println("other side= "+ intToRow(otherSidePattern));
		//System.out.println("idx       = "+ idx);
		for(int i=0; i<6; i++){
			int mask = (BIT_MASK_6 << idx) >> i;
			int blocked = otherSidePattern & mask;
			if (blocked!=0) continue;
			int checkRegion = thisSidePattern & mask;
			
			int live4 = (BIT_LIVE_4 << idx) >> i;
			int result = checkRegion ^ live4;
			if(result==0) return LIVE_4;
			//System.out.println("mask = "+ intToRow(pattern)+" result = "+intToRow(result)+ " other side = "+intToRow(influence));
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
			//System.out.println("mask = "+ intToRow(pattern)+" result = "+intToRow(result)+ " other side = "+intToRow(influence));
		}
		
		for(int i=0; i<7; i++){
			int mask = (BIT_MASK_7 << idx) >> i;
			int blocked = otherSidePattern & mask;
			if (blocked!=0) continue;
			int checkRegion = thisSidePattern & mask;
			
			int live3 = (BIT_LIVE_3 << idx) >> i;
			int result = checkRegion ^ live3;
			if(result==0) return LIVE_3;
			//System.out.println("mask = "+ intToRow(pattern)+" result = "+intToRow(result)+ " other side = "+intToRow(influence));
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
			//System.out.println("mask = "+ intToRow(pattern)+" result = "+intToRow(result)+ " other side = "+intToRow(influence));
		}
		
		for(int i=0; i<6; i++){
			int mask = (BIT_MASK_6 << idx) >> i;
			int blocked = otherSidePattern & mask;
			if (blocked!=0) continue;
			int checkRegion = thisSidePattern & mask;
			
			int dead3_6 = (BIT_DEAD_3_6 << idx) >> i;
			int result = checkRegion ^ dead3_6;
			if(result==0) return DEAD_3;
			int dead3_7 = (BIT_DEAD_3_7 << idx) >> i;
			result = checkRegion ^ dead3_7;
			if(result==0) return DEAD_3;
			//System.out.println("mask = "+ intToRow(pattern)+" result = "+intToRow(result)+ " other side = "+intToRow(influence));
		}
		
		for(int i=0; i<8; i++){
			int mask = (BIT_MASK_8 << idx) >> i;
			int blocked = otherSidePattern & mask;
			if (blocked!=0) continue;
			int checkRegion = thisSidePattern & mask;
			
			int live2 = (BIT_LIVE_2 << idx) >> i;
			int result = checkRegion ^ live2;
			if(result==0) return LIVE_2;
			//System.out.println("mask = "+ intToRow(pattern)+" result = "+intToRow(result)+ " other side = "+intToRow(influence));
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
			//System.out.println("mask = "+ intToRow(pattern)+" result = "+intToRow(result)+ " other side = "+intToRow(influence));
		}
		
		for(int i=0; i<6; i++){
			int mask = (BIT_MASK_6 << idx) >> i;
			int blocked = otherSidePattern & mask;
			if (blocked!=0) continue;
			int checkRegion = thisSidePattern & mask;
			
			int dead2_3 = (BIT_DEAD_2_3 << idx) >> i;
			int result = checkRegion ^ dead2_3;
			if(result==0) return DEAD_2;
			//System.out.println("mask = "+ intToRow(pattern)+" result = "+intToRow(result)+ " other side = "+intToRow(influence));
		}
		
		for(int i=0; i<7; i++){
			int mask = (BIT_MASK_7 << idx) >> i;
			int blocked = otherSidePattern & mask;
			if (blocked!=0) continue;
			int checkRegion = thisSidePattern & mask;
			
			int dead2_4 = (BIT_DEAD_2_4 << idx) >> i;
			int result = checkRegion ^ dead2_4;
			if(result==0) return DEAD_2;
			//System.out.println("mask = "+ intToRow(pattern)+" result = "+intToRow(result)+ " other side = "+intToRow(influence));
		}
		
		return 0;
			
	}
	public int evaluatePos2(int r, int c, int side){
		int[] values = new int[4];
		
		//check horizontal
		String s = combineBitStrings(intToRow(state[side][r]),intToRow(state[1-side][r]));
//		System.out.println("row = "+s +" idx = "+c);
		values[0] = evaluatePattern2(s,c);
		
		//check vertical
		char[] col = new char[ROWS];
		for(int i=0; i<ROWS; i++){
			int bitThisSide = state[side][i] & colMask[c];
			int bitOtherSide = state[1-side][i] & colMask[c];
			if(bitThisSide != 0){
				col[i] = '1';
			}else if(bitOtherSide != 0){
				col[i] = '2';
			}else{
				col[i] = '0';
			}
		}
		s = String.valueOf(col);
//		System.out.println("col = "+s +" idx = "+r);
		values[1] = evaluatePattern2(s,r);
		
		//check diagonal \
		s="";
		for(int i=0; i<ROWS; i++){
			if(r-i<0 || c-i<0){
				break;
			}
			if((state[side][r-i] & colMask[c-i])!=0){
				s="1"+s;
			}else if((state[1-side][r-i] & colMask[c-i])!=0){
				s="2"+s;
			}else{
				s="0"+s;
			}
		}
		int idx = s.length()-1;
		for(int i=1; i<ROWS; i++){
			if(r+i>=ROWS || c+i>=COLS){
				break;
			}
			if((state[side][r+i] & colMask[c+i])!=0){
				s+="1";
			}else if((state[1-side][r+i] & colMask[c+i])!=0){
				s+="2";
			}else{
				s+="0";
			}
		}
//		System.out.println("diagonal \\ = "+s +" idx = "+idx);
		values[2] = evaluatePattern2(s,idx);
		
		//check diagonal /
		s="";
		for(int i=0; i<ROWS; i++){
			if(r-i<0 || c+i>=COLS){
				break;
			}
			if((state[side][r-i] & colMask[c+i])!=0){
				s="1"+s;
			}else if((state[1-side][r-i] & colMask[c+i])!=0){
				s="2"+s;
			}else{
				s="0"+s;
			}
		}
		idx = s.length()-1;
		for(int i=1; i<ROWS; i++){
			if(r+i>=ROWS || c-i<0){
				break;
			}
			if((state[side][r+i] & colMask[c-i])!=0){
				s+="1";
			}else if((state[1-side][r+i] & colMask[c-i])!=0){
				s+="2";
			}else{
				s+="0";
			}
		}
//		System.out.println("diagonal / = "+s +" idx = "+idx);
		values[3] = evaluatePattern2(s,idx);

		return evaluateValues(values);
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
	public int evaluatePattern2(String s, int posIdx){
		
		for(int i=0; i<REGEX_LIVE_4.length(); i++){
			if(s.indexOf(REGEX_LIVE_4, posIdx-i)!=-1){
				return LIVE_4;
			}
		}
		/*
		for(int i=0; i<REGEX_LIVE_3.length(); i++){
			if(s.indexOf(REGEX_LIVE_3, posIdx-i)!=-1){
				return LIVE_3;
			}
		}
		for(int i=0; i<REGEX_LIVE_2.length(); i++){
			if(s.indexOf(REGEX_LIVE_2, posIdx-i)!=-1){
				return LIVE_2;
			}
		}
		*/
	/*	
		Matcher matcher = PATTERN_LIVE_4.matcher(s);
		while(matcher.find()){
			int start = matcher.start();
			int end = matcher.end();
			//System.out.print("Start index: " + matcher.start());
	        //System.out.print(" End index: " + matcher.end());
	        //System.out.println(" Found: " + matcher.group());
	        if(posIdx>=start && posIdx<end) return LIVE_4;
		}
		matcher = PATTERN_DEAD_4.matcher(s);
		while(matcher.find()){
			int start = matcher.start();
			int end = matcher.end();
	        if(posIdx>=start && posIdx<end) return DEAD_4;
		}
		matcher = PATTERN_LIVE_3.matcher(s);
		while(matcher.find()){
			int start = matcher.start();
			int end = matcher.end();
	        if(posIdx>=start && posIdx<end) return LIVE_3;
		}
		
		matcher = PATTERN_DEAD_3.matcher(s);
		while(matcher.find()){
			int start = matcher.start();
			int end = matcher.end();
	        if(posIdx>=start && posIdx<end) return DEAD_3;
		}
		
		matcher = PATTERN_LIVE_2.matcher(s);
		while(matcher.find()){
			int start = matcher.start();
			int end = matcher.end();
	        if(posIdx>=start && posIdx<end) return LIVE_2;
		}
		
		matcher = PATTERN_DEAD_2.matcher(s);
		while(matcher.find()){
			int start = matcher.start();
			int end = matcher.end();
	        if(posIdx>=start && posIdx<end) return DEAD_2;
		}*/
		return 0;
	}
	public int evaluatePos(int r, int c, int side){

		int[] values = new int[4];

		//check vertical
		int connected=0;
		int sideA =0;
		int sideB =0;
		for(int i=r; i>=0; i--){
			if((state[side][i] & colMask[c])!=0){
				connected++;
			}else if((state[1-side][i] & colMask[c])!=0){
				break;
			}else{
				sideA++;
			}
		}
		for(int i=r+1; i<ROWS; i++){
			if((state[side][i] & colMask[c])!=0){
				connected++;
			}else if((state[1-side][i] & colMask[c])!=0){
				break;
			}else{
				sideB++;
			}
		}
		values[0] = evaluatePattern(connected, sideA, sideB);
		if(values[0] ==MAX_STATE_VALUE) return values[0];	//early return
		
		//check horizontal
		connected=0;
		sideA =0;
		sideB =0;
		for(int i=c; i>=0; i--){
			if((state[side][r] & colMask[i])!=0){
				connected++;
			}else if((state[1-side][r] & colMask[i])!=0){
				break;
			}else{
				sideA++;
			}
		}
		for(int i=c+1; i<COLS; i++){
			if((state[side][r] & colMask[i])!=0){
				connected++;
			}else if((state[1-side][r] & colMask[i])!=0){
				break;
			}else{
				sideB++;
			}
		}
		values[1] = evaluatePattern(connected, sideA, sideB);
		if(values[1]==MAX_STATE_VALUE) return values[1];	//early return
		
		//check diagonal \
		connected=0;
		sideA =0;
		sideB =0;
		for(int i=0; i<ROWS; i++){
			if(r-i<0 || c-i<0){
				break;
			}
			if((state[side][r-i] & colMask[c-i])!=0){
				connected++;
			}else if((state[1-side][r-i] & colMask[c-i])!=0){
				break;
			}else{
				sideA++;
			}
		}
		for(int i=1; i<ROWS; i++){
			if(r+i>=ROWS || c+i>=COLS){
				break;
			}
			if((state[side][r+i] & colMask[c+i])!=0){
				connected++;
			}else if((state[1-side][r+i] & colMask[c+i])!=0){
				break;
			}else{
				sideB++;
			}
		}
		values[2] = evaluatePattern(connected, sideA, sideB);
		if(values[2]==MAX_STATE_VALUE) return values[2];	//early return
		
		//check diagonal /
		connected=0;
		sideA =0;
		sideB =0;
		for(int i=0; i<ROWS; i++){
			if(r-i<0 || c+i>=COLS){
				break;
			}
			if((state[side][r-i] & colMask[c+i])!=0){
				connected++;
			}else if((state[1-side][r-i] & colMask[c+i])!=0){
				break;
			}else{
				sideA++;
			}
		}
		for(int i=1; i<ROWS; i++){
			if(r+i>=ROWS || c-i<0){
				break;
			}
			if((state[side][r+i] & colMask[c-i])!=0){
				connected++;
			}else if((state[1-side][r+i] & colMask[c-i])!=0){
				break;
			}else{
				sideB++;
			}
		}
		values[3] = evaluatePattern(connected, sideA, sideB);
		if(values[3]==MAX_STATE_VALUE) return values[3];	//early return
		
		return evaluateValues(values);
	}
	public int evaluatePattern(int connectedCount, int sideA, int sideB){

		if(connectedCount==4){
			if(sideA>0 && sideB>0){
				return LIVE_4;
			}
			if(sideA>0 || sideB>0){
				return DEAD_4;
			}
			return 0;
		}
		if(connectedCount==3){
			if(sideA>0 && sideB>0){
				return LIVE_3;
			}
			if(sideA>1 || sideB>1){
				return DEAD_3;
			}
			return 0;
		}
		if(connectedCount==2){
			if(sideA>0 && sideB>0 && sideA+sideB>2){
				return LIVE_2;
			}
			if(sideA>2 || sideB>2){
				return DEAD_2;
			}
			return 0;
		}
		return 0;	
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
