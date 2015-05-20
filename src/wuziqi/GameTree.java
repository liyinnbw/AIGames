package wuziqi;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class GameTree {
	public static final int BEST_SIZE = 10;
	private class TreeNode{
		public Point nextMove;
		public int moveCount;
		public int v;
		public int searchDepth;
	}
	private GameState currState;	//the current game state
	private int depthLim;			//number of moves to look ahead
	public HashMap<Integer, TreeNode> hm;
	public int hmQuerySuccessfulCount;
	public HashMap<Integer, TreeNode> stateValue;
	public int stateValueQuerySuccessfulCount;
	private HashMap<Integer, Point> moveLib;	//key: currstate hash value, value: next state
	private ZobristHash hasher;
	private static boolean ENABLE_HASH = true;
	private static boolean ENABLE_MOVELIB = false;
	private static final int NULLMOVE_R = 2;
	private long startTime;
	private long timeLim;
	public int maxDepthReached;

	//debug flag
	boolean debug;

	public int getDepthLim() {
		return depthLim;
	}

	public void setDepthLim(int depthLim) {
		this.depthLim = depthLim;
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
			for (HashMap.Entry<Integer, Point> entry : moveLib.entrySet()){
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
	public GameTree(GameState curr, int dpLim){
		System.out.println("agent created");
		setCurrState(curr);
		setDepthLim(dpLim);
		hasher = new ZobristHash(curr.getRows()*curr.getCols(),3);
		stateValue = new HashMap<Integer, TreeNode>();
		hm = new HashMap<Integer, TreeNode>();
		hmQuerySuccessfulCount = 0;
		stateValueQuerySuccessfulCount = 0;
		if(ENABLE_MOVELIB){
			setStateLibrary();
		}
		timeLim = Long.MAX_VALUE;
		maxDepthReached = 0;
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
		Iterator<HashMap.Entry<Integer, TreeNode>> it = hm.entrySet().iterator();
		while(it.hasNext()){
			HashMap.Entry<Integer, TreeNode> entry = it.next();
			if(entry.getValue().moveCount<currTotalMoveCount){
				it.remove();
				outdatedCount++;
			}
		}
		System.out.println("cleared outdated hash = "+outdatedCount+" current hash size = "+hm.size());
		
		//hm = new HashMap<Integer, TreeNode>(); //old hash table useless since tree depth changed
		hmQuerySuccessfulCount = 0;
		stateValueQuerySuccessfulCount = 0;
		maxDepthReached = 0;
		
		TreeNode bestNext = null;
		timeLim = 30000; //30s
		startTime = System.currentTimeMillis();
		for(int depth = 1; ; depth++){
			bestNext = minMaxAlphaBeta(currState,depth-1,0-Integer.MAX_VALUE, Integer.MAX_VALUE, false);
			maxDepthReached = depth;
			long currTime = System.currentTimeMillis();
			if((currTime - startTime > timeLim) || bestNext.v==GameState.MAX_STATE_VALUE || bestNext.v==GameState.MIN_STATE_VALUE) break;
		}

		
		
		//it = hm.entrySet().iterator();
		//while(it.hasNext()){
		//	HashMap.Entry<Integer, TreeNode> entry = it.next();
		//	if(entry.getValue().moveCount==2 && entry.getValue().nextMove.equals(new Point(5,6))){
		//		System.out.println("verified saved node value = "+entry.getValue().v);
		//	}
		//}
		
		
		System.out.println("Successfully queried saved nodes = "+hmQuerySuccessfulCount+"/"+hm.size()+" max depth reached = "+maxDepthReached);
		
		return bestNext.nextMove;
	}
	public List<Point> sortMoves(GameState curr, List<Point> nextPossibleMoves, int side){
		List<TreeNode> sortable = new ArrayList<TreeNode>();
		
		//evaluate all moves
		for(Point p : nextPossibleMoves){
			curr.addPiece((int)p.getX(), (int)p.getY());
			TreeNode t = new TreeNode();
			t.nextMove = p;
			t.v = curr.evaluate();
			curr.revertOneMove();
			sortable.add(t);
		}
		
		//sort moves by value
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

		List<Point> sortedMoves = new ArrayList<Point>();
		for(int i=0; i<sortable.size(); i++){
			sortedMoves.add(sortable.get(i).nextMove);
		}
		return sortedMoves;
	}
	public List<Point> reduceConsideration (GameState curr, List<Point> nextPossibleMoves, int side){
		if(nextPossibleMoves.size()<BEST_SIZE) return nextPossibleMoves;
		
		List<TreeNode> sortable = new ArrayList<TreeNode>();
		
		//evaluate all moves
		for(Point p : nextPossibleMoves){
			curr.addPiece((int)p.getX(), (int)p.getY());
			TreeNode t = new TreeNode();
			t.nextMove = p;
			t.v = curr.evaluate();
			curr.revertOneMove();
			/*
			int stateHash = hasher.hash(s);
			TreeNode hashedNode = stateValue.get(stateHash);
			if(hashedNode!=null){
				t.v=hashedNode.v;
			}else{
				t.v=s.evaluate();
				stateValue.put(stateHash, t);
			}*/
			sortable.add(t);
		}
		
		//sort moves by value
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
		
		//for(TreeNode t: sortable){
		//	System.out.print(t.v+" ");
		//}
		//System.out.println();
		
		int reducedSize = 0;
		for(int i=0; i<sortable.size(); i++){
			if(i>=BEST_SIZE && (sortable.get(BEST_SIZE-1).v!=sortable.get(i).v)){
				break;
			}
			reducedSize++;
		}
		
		List<Point> reduced = new ArrayList<Point>();
		for(int i=0; i<reducedSize; i++){
			reduced.add(sortable.get(i).nextMove);
		}
		
		//for(int i=0; i<reduced.size(); i++){
		//	System.out.print(sortable.get(i).v+" ");
		//}
		//System.out.println();
		
		return reduced;
	}

	public TreeNode minMaxAlphaBeta(GameState curr, int depth, int alpha, int beta, boolean useNullMove){
		
		Point previousBest = null;
		//query hash table
		if(ENABLE_HASH){	
			TreeNode queryRoot = hm.get(hasher.hash(curr));
			if(queryRoot!=null){
				if(queryRoot.searchDepth>=depth) {
					hmQuerySuccessfulCount++;
					return queryRoot;
				}else{
					previousBest = queryRoot.nextMove;
				}
			}
		}

		List<Point> nextPossibleMoves = curr.nextPossibleMoves();
		
		//order search by resultant state value
		nextPossibleMoves = sortMoves(curr, nextPossibleMoves, curr.getCurrSide());
		
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
		else if(curr.getCurrSide() == GameState.MAX_PLAYER){
			int max = 0;
			boolean maxNotInit = true;
			
			if(depth>0 && useNullMove){
				//make null move
				curr.makeNullMove();
				TreeNode nullMoveBestNext = minMaxAlphaBeta(curr, depth-1-NULLMOVE_R, alpha, beta, false);
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
					return root;
				}
			}
			
			
			
			for(Point p: nextPossibleMoves){
				curr.addPiece((int)p.getX(), (int)p.getY());
				int value = 0;
				int searchDepth = depth;
				if(depth<=0){
					value = curr.evaluate();		
				}else{
					if(depth == depthLim - 1 && p.equals(new Point(10,7))){
						//debug = true;
					}
					TreeNode bestNext = minMaxAlphaBeta(curr, depth-1, alpha, beta, false);
					if(depth == depthLim - 1 && p.equals(new Point(10,7))){
						debug = false;
					}
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
			
			if(depth>0 && useNullMove){
				//make null move
				curr.makeNullMove();
				TreeNode nullMoveBestNext = minMaxAlphaBeta(curr, depth-1-NULLMOVE_R, alpha, beta, false);
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
					return root;
				}
			}
			
			
			for(Point p: nextPossibleMoves){
				curr.addPiece((int)p.getX(), (int)p.getY());
				int value = 0;
				int searchDepth = depth;
				if(depth<=0){
					value = curr.evaluate();
				}else{
					if(depth == depthLim - 1 && p.equals(new Point(10,7))){
						//debug = true;
					}
					TreeNode bestNext = minMaxAlphaBeta(curr, depth-1, alpha, beta,false);
					if(depth == depthLim - 1 && p.equals(new Point(10,7))){
						debug = false;
					}
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
			if(root.nextMove!=null){
				//curr.addPiece((int)root.nextMove.getX(), (int)root.nextMove.getY());
				hm.put(hasher.hash(curr), root);
				//curr.revertOneMove();
			}else{
				hm.put(hasher.hash(curr), root);
			}
		}
		if(debug == true){
			System.out.println(root.nextMove+" value = "+root.v+" depth = "+root.searchDepth+" movecount = "+root.moveCount);
		}
		return root;
	}
}
