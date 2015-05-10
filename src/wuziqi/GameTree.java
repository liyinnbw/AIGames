package wuziqi;

import java.util.List;

public class GameTree {
	
	private class TreeNode{
		public GameState s;
		public int v;
	}
	private GameState currState;	//the current game state
	private int depthLim;			//number of moves to look ahead

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

	public GameTree(GameState curr, int dpLim){
		setCurrState(curr);
		setDepthLim(dpLim);
	}
	
	public GameState nextMove(){
		TreeNode bestNext = minMaxAlphaBeta(currState,1,-1*Integer.MAX_VALUE, Integer.MAX_VALUE);
		return bestNext.s;
	}

	public TreeNode minMaxAlphaBeta(GameState curr, int depth, int alpha, int beta){
		
		List<GameState> nextPossibleStates = curr.nextPossibleStates();
		TreeNode root = new TreeNode();	//the node to return
		GameState selection = null;		//the state selected
		if(nextPossibleStates.size()==0){
			root.s=curr;
			root.v=curr.evaluate();
		}
		else if(curr.getCurrSide() == GameState.MAX_PLAYER){
			int max = 0;
			boolean maxNotInit = true;
			for(GameState s: nextPossibleStates){
				int value = 0;
				if(depth==depthLim){
					value = s.evaluate();	
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
					value = s.evaluate();	
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
		
		return root;
	}
}
