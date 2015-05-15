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
	}
	private GameState currState;	//the current game state
	private int depthLim;			//number of moves to look ahead
	public HashMap<Integer, TreeNode> hm;
	public int hmQuerySuccessfulCount;
	public HashMap<Integer, TreeNode> stateValue;
	public int stateValueQuerySuccessfulCount;
	private HashMap<Integer, GameState> stateLibrary;	//key: currstate hash value, value: next state
	private ZobristHash hasher;
	private static boolean ENABLE_HASH = true;
	private static boolean ENABLE_STATELIB = false;

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
		stateLibrary = new HashMap<Integer, GameState>();
		String filename = "stateLib.txt";
		BufferedReader br = null;
		try {
			System.out.println("===== reading state library =====");
			br = new BufferedReader(new FileReader(filename));
			String line;
			int[][] state = new int[2][currState.getRows()];
			int side =0;
			int row =0;
			int hashValue = 0;
			boolean isCurrentState = true;
			
			while ((line = br.readLine()) != null) {
				if(line.equals("maxplayer:")){
					state = new int[2][currState.getRows()];
					side = currState.MAX_PLAYER;
					row = 0;
				}else if(line.equals("minplayer:")){
					side = currState.MIN_PLAYER;
					row = 0;
				}else{
					state[side][row]=Integer.parseInt(line,2);
					row++;
					if(row>=currState.getRows() && side == currState.MIN_PLAYER){
						GameState s = new GameState(currState.getRows(), currState.getCols(), 0 , state, currState.getMoves());//, currState.getValue());
						if(isCurrentState){
							System.out.println("is current state");
							hashValue = hasher.hash(s);
							isCurrentState = !isCurrentState;
						}else{
							System.out.println("is next state");
							stateLibrary.put(hashValue, s);
							isCurrentState = !isCurrentState;
						}
					}
				}
			}
			System.out.println(stateLibrary.size());
			for (HashMap.Entry<Integer, GameState> entry : stateLibrary.entrySet()){
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
		if(ENABLE_STATELIB){
			setStateLibrary();
		}
	}
	
	public Point nextMove(){
		//if state lib is used, query it first
		if(ENABLE_STATELIB){
			GameState libState = stateLibrary.get(hasher.hash(currState));
			if (libState!=null){
				System.out.println("find next move in library");
				return new Point(-1,-1); //TODO: state lib need to return move instead of state
			}
		}
		
		//clean outdated hash
		int outdatedCount = 0;
		int currTotalMoveCount = currState.getMoves().size();
		Iterator<HashMap.Entry<Integer, TreeNode>> it = stateValue.entrySet().iterator();
		while(it.hasNext()){
			HashMap.Entry<Integer, TreeNode> entry = it.next();
			if(entry.getValue().moveCount<currTotalMoveCount){
				it.remove();
				outdatedCount++;
			}
		}
		System.out.println("cleared outdated hash = "+outdatedCount+" current hash size = "+stateValue.size());
		
		hm = new HashMap<Integer, TreeNode>(); //old hash table useless since tree depth changed
		hmQuerySuccessfulCount = 0;
		stateValueQuerySuccessfulCount = 0;
		TreeNode bestNext = minMaxAlphaBeta(currState,1,-1*Integer.MAX_VALUE, Integer.MAX_VALUE);
		
		return bestNext.nextMove;
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
	public TreeNode minMaxAlphaBeta(GameState curr, int depth, int alpha, int beta){
		
		//query hash table
		if(ENABLE_HASH){	
			TreeNode queryRoot = hm.get(hasher.hash(curr));
			if(queryRoot!=null) {
				hmQuerySuccessfulCount++;
				return queryRoot;
			}
		}

		List<Point> nextPossibleMoves = curr.nextPossibleMoves();
		
		//if(depth >= depthLim-4){
		//	nextPossibleMoves = reduceConsideration(curr, nextPossibleMoves, curr.getCurrSide());
		//}

		
		TreeNode root = new TreeNode();	//the node to return
		Point selectedMove = null;		//the state selected
		if(nextPossibleMoves.size()==0){
			root.nextMove = null;
			root.moveCount = curr.getMoves().size();
			root.v=curr.evaluate();
			
			//int stateHash = hasher.hash(curr);
			//TreeNode hashedNode = stateValue.get(stateHash);
			//if(hashedNode!=null){
			//	root.v = hashedNode.v;
			//	stateValueQuerySuccessfulCount++;
			//}else{
			//	root.v=curr.evaluate();
			//	stateValue.put(stateHash, root);
			//}
		}
		else if(curr.getCurrSide() == GameState.MAX_PLAYER){
			int max = 0;
			boolean maxNotInit = true;
			for(Point p: nextPossibleMoves){
				curr.addPiece((int)p.getX(), (int)p.getY());
				int value = 0;
				if(depth==depthLim){
					value = curr.evaluate();
					
					//int stateHash = hasher.hash(curr);
					//TreeNode hashedNode = stateValue.get(stateHash);
					//if(hashedNode!=null){
					//	value = hashedNode.v;
					//	stateValueQuerySuccessfulCount++;
					//}else{
					//	value = curr.evaluate();
					//	TreeNode tn = new TreeNode();
					//	tn.nextMove = p;
					//	tn.moveCount = curr.getMoves().size();
					//	tn.v = value;
					//	stateValue.put(stateHash, tn);
					//}
					
				}else{
					TreeNode bestNext = minMaxAlphaBeta(curr, depth+1, alpha, beta);
					value = bestNext.v;
				}
				curr.revertOneMove();
				
				if(maxNotInit){
					max = value;
					selectedMove = p; 
					maxNotInit = false;
				}else if(value>max){
					max = value;
					selectedMove = p; 
				}
				
				if(max>=beta || max==GameState.MAX_STATE_VALUE){
					break;
				}else{
					if(max>alpha){
						alpha = max;
					}
				}
			}
			root.nextMove = selectedMove;
			root.moveCount = curr.getMoves().size()+1;
			root.v = max;
		}
		else{
			int min = 0;
			boolean minNotInit = true;
			for(Point p: nextPossibleMoves){
				curr.addPiece((int)p.getX(), (int)p.getY());
				int value = 0;
				
				if(depth==depthLim){
					value = curr.evaluate();
					//int stateHash = hasher.hash(curr);
					//TreeNode hashedNode = stateValue.get(stateHash);
					//if(hashedNode!=null){
					//	value = hashedNode.v;
					//	stateValueQuerySuccessfulCount++;
					//}else{
					//	value = curr.evaluate();
					//	TreeNode tn = new TreeNode();
					//	tn.nextMove = p;
					//	tn.moveCount = curr.getMoves().size();
					//	tn.v = value;
					//	stateValue.put(stateHash, tn);
					//}	
				}else{
					TreeNode bestNext = minMaxAlphaBeta(curr, depth+1, alpha, beta);
					value = bestNext.v;
				}
				curr.revertOneMove();
				
				if(minNotInit){
					min = value;
					selectedMove = p; 
					minNotInit = false;
				}else if(value<min){
					min = value;
					selectedMove = p; 
				}
				
				if(min<=alpha || min==GameState.MIN_STATE_VALUE){
					break;
				}else{
					if(min<beta){
						beta = min;
					}
				}
			}
			root.nextMove = selectedMove;
			root.moveCount = curr.getMoves().size()+1;
			root.v = min;
		}
		if(ENABLE_HASH){
			if(root.nextMove!=null){
				curr.addPiece((int)root.nextMove.getX(), (int)root.nextMove.getY());
				hm.put(hasher.hash(curr), root);
				curr.revertOneMove();
			}else{
				hm.put(hasher.hash(curr), root);
			}
		}
		return root;
	}
}
