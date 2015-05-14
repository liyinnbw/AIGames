package wuziqi;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GameTree {
	
	private class TreeNode{
		public GameState s;
		public int v;
	}
	private GameState currState;	//the current game state
	private int depthLim;			//number of moves to look ahead
	public HashMap<Integer, TreeNode> hm;
	public int hmQuerySuccessfulCount;
	public HashMap<Integer, Integer> stateValue;
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
						GameState s = new GameState(currState.getRows(), currState.getCols(), 0 , state);//, currState.getValue());
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
		stateValue = new HashMap<Integer, Integer>();
		hm = new HashMap<Integer, TreeNode>();
		hmQuerySuccessfulCount = 0;
		stateValueQuerySuccessfulCount = 0;
		if(ENABLE_STATELIB){
			setStateLibrary();
		}
	}
	
	public GameState nextMove(){
		if(ENABLE_STATELIB){
			GameState libState = stateLibrary.get(hasher.hash(currState));
			if (libState!=null){
				System.out.println("find next move in library");
				libState.setCurrSide(1-currState.getCurrSide());
				return libState;
			}else{
				hm = new HashMap<Integer, TreeNode>(); //old hash table useless since tree depth changed
				TreeNode bestNext = minMaxAlphaBeta(currState,1,-1*Integer.MAX_VALUE, Integer.MAX_VALUE);
				return bestNext.s;
			}
		}else{
			hm = new HashMap<Integer, TreeNode>(); //old hash table useless since tree depth changed
			hmQuerySuccessfulCount = 0;
			stateValueQuerySuccessfulCount = 0;
			TreeNode bestNext = minMaxAlphaBeta(currState,1,-1*Integer.MAX_VALUE, Integer.MAX_VALUE);
			
			return bestNext.s;
		}
	}

	public TreeNode minMaxAlphaBeta(GameState curr, int depth, int alpha, int beta){
		
		if(ENABLE_HASH){
			//query hash table
			TreeNode queryRoot = hm.get(hasher.hash(curr));
			if(queryRoot!=null) {
				//System.out.println("query result=\n"+queryRoot.s);
				hmQuerySuccessfulCount++;
				return queryRoot;
			}
		}
		List<GameState> nextPossibleStates = curr.nextPossibleStates();
		//System.out.println("DEPTH = "+depth+ " nextPossibleStates = "+nextPossibleStates.size());
		TreeNode root = new TreeNode();	//the node to return
		GameState selection = null;		//the state selected
		if(nextPossibleStates.size()==0){
			root.s=curr;
			int stateHash = hasher.hash(curr);
			Integer v = stateValue.get(stateHash);
			if(v!=null){
				root.v = v;
				stateValueQuerySuccessfulCount++;
			}else{
				root.v=curr.evaluate();
				stateValue.put(stateHash, root.v);
			}
		}
		else if(curr.getCurrSide() == GameState.MAX_PLAYER){
			int max = 0;
			boolean maxNotInit = true;
			for(GameState s: nextPossibleStates){
				int value = 0;
				if(depth==depthLim){
					
					int stateHash = hasher.hash(s);
					Integer v = stateValue.get(stateHash);
					if(v!=null){
						value = v;
						stateValueQuerySuccessfulCount++;
					}else{
						value = s.evaluate();
						stateValue.put(stateHash, value);
					}		
						
				}else{
					TreeNode bestNext = minMaxAlphaBeta(s, depth+1, alpha, beta);
					value = bestNext.v;
				}
				if(maxNotInit){
					max = value;
					selection = s; 
					maxNotInit = false;
				}
				if(value>max){
					max = value;
					selection = s; 
				}
				if(max>=beta || max==GameState.MAX_STATE_VALUE){
					root.s=selection;
					root.v=max;
					//hashing
					if(ENABLE_HASH){hm.put(hasher.hash(root.s), root);}
					return root;
				}else{
					if(max>alpha){
						alpha = max;
					}
				}
			}
			root.s = selection;
			root.v = max;
		}
		else{
			int min = 0;
			boolean minNotInit = true;
			for(GameState s: nextPossibleStates){
				int value = 0;
				if(depth==depthLim){
					int stateHash = hasher.hash(s);
					Integer v = stateValue.get(stateHash);
					if(v!=null){
						value = v;
						stateValueQuerySuccessfulCount++;
					}else{
						value = s.evaluate();
						stateValue.put(stateHash, value);
					}	
				}else{
					TreeNode bestNext = minMaxAlphaBeta(s, depth+1, alpha, beta);
					value = bestNext.v;
				}
				if(minNotInit){
					min = value;
					selection = s; 
					minNotInit = false;
				}
				if(value<min){
					min = value;
					selection = s; 
				}
				if(min<=alpha || min==GameState.MIN_STATE_VALUE){
					root.s=selection;
					root.v=min;
					//hashing
					if(ENABLE_HASH){hm.put(hasher.hash(root.s), root);}
					return root;
				}else{
					if(min<beta){
						beta = min;
					}
				}
			}
			root.s = selection;
			root.v = min;
		}
		//hashing
		if(ENABLE_HASH){hm.put(hasher.hash(root.s), root);}
		return root;
	}
}
