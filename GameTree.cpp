#include "GameTree.h"

GameTree::GameTree(int depthLim, GameState *s, int side){
	depthLimit=(depthLim<0)?0:depthLim;
	setRootState(s);
	rootSide = side;
}

GameTree::~GameTree(void)
{
}

GameState GameTree::next(){
	TreeNode best = minMax(rootState, 1, rootSide);
	return *(best.state);
}

TreeNode GameTree::minMax(GameState *root, int depth, int turn){
	std::vector<GameState> nextPossibleStates = root->getNexts();
	TreeNode bestCurrent;

	if(depth==depthLimit){
		GameState *selection;
		if(turn == MAX_PLAYER){
			double max = -1*std::numeric_limits<double>::max();
			for(int i=0; i<nextPossibleStates.size(); i++){
				//IMPORTANT: copy to new instance avoid the object gets destroyed when
				//the function exits and nextPossibleStates gets destroyed.
				GameState s = nextPossibleStates.at(i);	 
				double value = s.evaluate();	//only evaluate gamestate at leaves
				if(value>max){
					max = value;
					selection = &s; //s will not be destroyed when function exits since there's a pointer
				}
			}
		}else{
			double min = std::numeric_limits<double>::max();
			for(int i=0; i<nextPossibleStates.size(); i++){
				GameState s = nextPossibleStates.at(i);
				double value = s.evaluate();	//only evaluate gamestate at leaves
				if(value<min){
					min = value;
					selection = &s;
				}
			}
		}
		bestCurrent.state = selection;
		bestCurrent.value = selection->evaluate();
		return bestCurrent;
	}
	
	if(turn == MAX_PLAYER){
		double max = std::numeric_limits<double>::min();
		for(int i=0; i<nextPossibleStates.size(); i++){
			GameState s = nextPossibleStates.at(i);
			TreeNode bestNext = minMax(&s,depth+1, -1*turn);
			double value = bestNext.value;
			if(value>max){
				max = value;
				bestCurrent.state=&s;
				bestCurrent.value=value;
			}
		}
	}else{
		double min = std::numeric_limits<double>::max();
		for(int i=0; i<nextPossibleStates.size(); i++){
			GameState s = nextPossibleStates.at(i);
			TreeNode bestNext = minMax(&s,depth+1, -1*turn);
			double value = bestNext.value;
			if(value<min){
				min = value;
				bestCurrent.state=&s;
				bestCurrent.value=value;
			}
		}
	}

	return bestCurrent;

}