package chess;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GameTree {
	
	public static class MaxComparator implements Comparator<TreeNode>{
		@Override
		public int compare(TreeNode o1, TreeNode o2) {
			if(o1.v<o2.v) return 1;
			if(o1.v==o2.v) return 0;
			else return -1;
		}
	}
	public static class MinComparator implements Comparator<TreeNode>{
		@Override
		public int compare(TreeNode o1, TreeNode o2) {
			if(o1.v<o2.v) return -1;
			if(o1.v==o2.v) return 0;
			else return 1;
		}
	}
	private class TreeNode{
		public Point nextMove;
		public int moveCount;
		public int v;
		public int searchDepth;
	}
	private final static MaxComparator maxComparator = new MaxComparator();
	private final static MinComparator minComparator = new MinComparator();
	private GameState currState;	//the current game state
	private int depthLim;			//number of moves to look ahead
	private long timeLim;
	public HashMap<Integer, TreeNode> hm;
	private HashMap<Integer, Point> moveLib;	//key: currstate hash value, value: next state
	private ZobristHash hasher;
	private static boolean ENABLE_HASH = true;
	private static boolean ENABLE_MOVELIB = false;
	private static final int NULLMOVE_R = 2;	//must be multiples of 2
	private long startTime;
	private boolean endSearch;
	
	//stats for print
	public int hmQuerySuccessfulCount;
	public int maxDepthReached;
	public int nodesVisitedCount;
	public int branchesCount;
	public long totalSortingTime;
	//debug flag
	boolean debug;

	public long getTimeLim() {
		return timeLim;
	}

	public void setTimeLim(int t) {
		timeLim = (long)t;
	}

	public GameState getCurrState() {
		return currState;
	}

	public void setCurrState(GameState curr) {
		this.currState = curr;
	}
	
	public void setStateLibrary(){
		moveLib = new HashMap<Integer, Point>();
		String filename = "stateLib.txt";
		BufferedReader br = null;
		try {
			System.out.println("===== reading state library =====");
			br = new BufferedReader(new FileReader(filename));
			String line;
			while ((line = br.readLine()) != null) {
				//TODO: store moves to library
			}
			System.out.println(moveLib.size());
			for (Map.Entry<Integer, Point> entry : moveLib.entrySet()){
				System.out.println(entry.getValue());
			}
			
		}catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			System.out.println("===== finished reading lib =====");
		}
	}
	
	public GameTree(GameState curr, int tLim){
		System.out.println("agent created");
		setCurrState(curr);
		setTimeLim(tLim);
		hasher = new ZobristHash(curr.ROWS, curr.COLS,7);
		hm = new HashMap<Integer, TreeNode>();
		if(ENABLE_MOVELIB){
			setStateLibrary();
		}
		debug = false;
	}
	
	public Point nextMove(){
		//if state lib is used, query it first
		if(ENABLE_MOVELIB){
			Point libMove = moveLib.get(hasher.hash(currState));
			if (libMove!=null){
				System.out.println("find next move in library");
				return libMove;
			}
		}
		
		//clean outdated hash
		int outdatedCount = 0;
		int currTotalMoveCount = currState.getMoves().size();
		
		Iterator<Map.Entry<Integer, TreeNode>> it = hm.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<Integer, TreeNode> entry = it.next();
			if(entry.getValue().moveCount<currTotalMoveCount){
				it.remove();
				outdatedCount++;
			}
		}
		System.out.println("cleared outdated hash = "+outdatedCount+" current hash size = "+hm.size());
		

		hmQuerySuccessfulCount = 0;
		maxDepthReached = 0;
		nodesVisitedCount = 0;
		branchesCount = 0;
		totalSortingTime = 0;
		endSearch = false;
		
		TreeNode bestNext = null;
		startTime = System.currentTimeMillis();
		for(int depth = 4; ; depth++){
			depthLim = depth;
			bestNext = minMaxAlphaBeta(currState,depth-1,0-Integer.MAX_VALUE, Integer.MAX_VALUE, true);
			maxDepthReached = depth;
			long currTime = System.currentTimeMillis();
			if((currTime - startTime > timeLim) || bestNext.v==GameState.MAX_STATE_VALUE || bestNext.v==GameState.MIN_STATE_VALUE || endSearch) break;
		}

		
		
		//it = hm.entrySet().iterator();
		//while(it.hasNext()){
		//	HashMap.Entry<Integer, TreeNode> entry = it.next();
		//	if(entry.getValue().moveCount==2 && entry.getValue().nextMove.equals(new Point(5,6))){
		//		System.out.println("verified saved node value = "+entry.getValue().v);
		//	}
		//}
		
		
		System.out.println("Reuse saved nodes = "+hmQuerySuccessfulCount+"/"+hm.size()+" max depth reached = "+maxDepthReached+" average branching = "+branchesCount/nodesVisitedCount);
		System.out.println("sorting time = "+totalSortingTime/1000.0+" s");
		return bestNext.nextMove;
	}
	public List<Point> sortMoves(GameState curr, List<Point> nextPossibleMoves, int side){
		List<TreeNode> sortable = new ArrayList<TreeNode>();
		
		//evaluate all moves
		for(Point p : nextPossibleMoves){
			int x = (int)p.getX();
			int y = (int)p.getY();
			int valueBeforeMove = curr.updateValue(y, x);
			curr.makeMove(x, y);
			TreeNode t = new TreeNode();
			t.nextMove = p;
			t.v = curr.updateValue(y,x) - valueBeforeMove;//evaluate();
			curr.revertOneMove();
			sortable.add(t);
		}
		
		//sort moves by value
		if(side == GameState.MAX_PLAYER){
			Collections.sort(sortable,maxComparator);
		}else{
			Collections.sort(sortable,minComparator);
		}
		
		/*
		if(side == GameState.MAX_PLAYER){
			Collections.sort(sortable,new Comparator<TreeNode>(){
				@Override
				public int compare(TreeNode arg0, TreeNode arg1) {	
					if(arg0.v<arg1.v) return 1;
					if(arg0.v==arg1.v) return 0;
					else return -1;
					
				}		
			});
		}else{
			Collections.sort(sortable,new Comparator<TreeNode>(){
				@Override
				public int compare(TreeNode arg0, TreeNode arg1) {	
					if(arg0.v<arg1.v) return -1;
					if(arg0.v==arg1.v) return 0;
					else return 1;
				}		
			});
		}
		*/
		List<Point> sortedMoves = new ArrayList<Point>();
		for(int i=0; i<sortable.size(); i++){
			sortedMoves.add(sortable.get(i).nextMove);
		}
		return sortedMoves;
	}

	public TreeNode minMaxAlphaBeta(GameState curr, int depth, int alpha, int beta, boolean useNullMove){
		nodesVisitedCount++;
		
		Point previousBest = null;
		//query hash table
		if(ENABLE_HASH){	
			TreeNode queryRoot = hm.get(hasher.hash(curr));
			if(queryRoot!=null){
				if(queryRoot.searchDepth>=depth && !(depthLim-depth == 1 && queryRoot.nextMove==null)) {
					hmQuerySuccessfulCount++;
					return queryRoot;
				}else{
					previousBest = queryRoot.nextMove;
				}
			}
		}

		List<Point> nextPossibleMoves = curr.nextPossibleMoves();
		
		long start = System.currentTimeMillis();
		//order search by resultant state value
		nextPossibleMoves = sortMoves(curr, nextPossibleMoves, curr.getCurrSide());
		long end = System.currentTimeMillis();
		totalSortingTime += end-start;
		
		//if previous less deeper search result available, prioritize it as first search since it is likely still be the best
		if(previousBest!=null){
			if(nextPossibleMoves.remove(previousBest)){
				nextPossibleMoves.add(0, previousBest);
			}
		}
		
		TreeNode root = new TreeNode();	//the node to return
		Point selectedMove = null;		//the state selected
		int selectedSearchDepth = 0;
		if(nextPossibleMoves.size()==0){
			root.nextMove = null;
			root.moveCount = curr.getMoves().size();
			root.v=curr.evaluate();
			root.searchDepth = Integer.MAX_VALUE;
		}
		else if(nextPossibleMoves.size()==1 && (depthLim-depth)==1){
			root.nextMove = nextPossibleMoves.get(0);
			endSearch = true;
			return root;
		}
		else if(curr.getCurrSide() == GameState.MAX_PLAYER){
			int max = 0;
			boolean maxNotInit = true;
			
			if(depth > 0 && (depthLim - depth>=3) && useNullMove){
				//make null move
				curr.makeNullMove();
				TreeNode nullMoveBestNext = minMaxAlphaBeta(curr, depth-1-NULLMOVE_R, alpha, beta, !useNullMove);
				curr.revertNullMove();
				
				if(nullMoveBestNext.v>=beta || nullMoveBestNext.v==GameState.MAX_STATE_VALUE){
					//System.out.println("null move pruning successful at depth = "+depth);
					root.nextMove = null;
					root.moveCount = curr.getMoves().size()+1;
					root.v = nullMoveBestNext.v;
					root.searchDepth = Math.max(depth, nullMoveBestNext.searchDepth);
					if(ENABLE_HASH){
						hm.put(hasher.hash(curr), root);
					}
					branchesCount++;
					return root;
				}
			}
			
			
			
			for(Point p: nextPossibleMoves){
				branchesCount++;
				
				curr.makeMove((int)p.getX(), (int)p.getY());
				int value = 0;
				int searchDepth = depth;
				if(depth<=0){
					value = curr.evaluate();		
				}else{
					//if(depth == depthLim - 1 && p.equals(new Point(1,5))){
						//debug = true;
					//}
					TreeNode bestNext = minMaxAlphaBeta(curr, depth-1, alpha, beta, !useNullMove);
					//if(depth == depthLim - 1 &&( p.equals(new Point(1,5)) || p.equals(new Point(8,5)) || p.equals(new Point(6,3))) ){
						//System.out.println(p+" value = "+bestNext.v+ " depth = "+depth);
					//	debug = false;
					//}
					value = bestNext.v;
					searchDepth = Math.max(searchDepth, bestNext.searchDepth);
				}
				curr.revertOneMove();
				
				if(maxNotInit){
					max = value;
					selectedMove = p; 
					selectedSearchDepth = searchDepth;
					maxNotInit = false;
				}else if(value>max){
					max = value;
					selectedMove = p; 
					selectedSearchDepth = searchDepth;
				}
				
				if(max>=beta || max==GameState.MAX_STATE_VALUE){
					break;
				}else{
					if(max>alpha){
						alpha = max;
					}
				}
				
				long currTime = System.currentTimeMillis();
				if(currTime - startTime > timeLim) break;
			}
			root.nextMove = selectedMove;
			root.moveCount = curr.getMoves().size()+1;
			root.v = max;
			root.searchDepth = selectedSearchDepth;
			
		}
		else{
			int min = 0;
			boolean minNotInit = true;
			
			
			if(depth>0 && (depthLim - depth>=3) && useNullMove){
				//make null move
				curr.makeNullMove();
				TreeNode nullMoveBestNext = minMaxAlphaBeta(curr, depth-1-NULLMOVE_R, alpha, beta, !useNullMove);
				curr.revertNullMove();
				
				if(nullMoveBestNext.v<=alpha || nullMoveBestNext.v==GameState.MIN_STATE_VALUE){
					//System.out.println("null move pruning successful at depth = "+depth);
					root.nextMove = null;
					root.moveCount = curr.getMoves().size()+1;
					root.v = nullMoveBestNext.v;
					root.searchDepth = Math.max(depth, nullMoveBestNext.searchDepth);
					if(ENABLE_HASH){
						hm.put(hasher.hash(curr), root);
					}
					branchesCount++;
					return root;
				}
			}
			
			
			for(Point p: nextPossibleMoves){
				branchesCount++;
				
				curr.makeMove((int)p.getX(), (int)p.getY());
				int value = 0;
				int searchDepth = depth;
				if(depth<=0){
					value = curr.evaluate();
				}else{
					//if(depth == depthLim - 1 && p.equals(new Point(1,5))){
						//debug = true;
					//}
					TreeNode bestNext = minMaxAlphaBeta(curr, depth-1, alpha, beta, !useNullMove);
					//if(depth == depthLim - 1 &&( p.equals(new Point(1,5)) || p.equals(new Point(8,5)) || p.equals(new Point(6,3))) ){
						//System.out.println(p+" value = "+bestNext.v+ " depth = "+depth);
					//	debug = false;
					//}
					value = bestNext.v;
					searchDepth = Math.max(searchDepth, bestNext.searchDepth);
				}
				curr.revertOneMove();
				
				if(minNotInit){
					min = value;
					selectedMove = p; 
					selectedSearchDepth = searchDepth;
					minNotInit = false;
				}else if(value<min){
					min = value;
					selectedMove = p; 
					selectedSearchDepth = searchDepth;
				}
				
				if(min<=alpha || min==GameState.MIN_STATE_VALUE){
					break;
				}else{
					if(min<beta){
						beta = min;
					}
				}
				long currTime = System.currentTimeMillis();
				if(currTime - startTime > timeLim) break;
			}
			root.nextMove = selectedMove;
			root.moveCount = curr.getMoves().size()+1;
			root.v = min;
			root.searchDepth = selectedSearchDepth;
			
		}
		if(ENABLE_HASH){
			hm.put(hasher.hash(curr), root);
		}
		if(debug == true){
			System.out.println(root.nextMove+" value = "+root.v+" depth = "+root.searchDepth+" movecount = "+root.moveCount);
		}
		//if(depth == depthLim -1){
		//	System.out.println(root.nextMove+" value = "+root.v+" depth = "+root.searchDepth+" movecount = "+root.moveCount);
		//}
		return root;
	}
}
