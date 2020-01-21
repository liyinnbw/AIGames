#include "GameTree.h"
#include <algorithm>
GameTree::GameTree(GameState *s, int tLim){
	setCurrState(s);
	setTimeLim(tLim);
	hasher = new ZobristHash(currState->getRows(),currState->getCols(),3);
}

GameTree::~GameTree(void)
{
}

Point GameTree::nextMove(){
	Point p;
		
	//clean outdated hash
	outdatedCount = 0;
	int currTotalMoveCount = currState->getMoves().size();
	std::vector<std::pair<int,TreeNode> > *hashList = hm.getHashList();
	std::vector<std::pair<int,TreeNode> >::iterator it;
	for( it = hashList->begin(); it!=hashList->end();){
		if((*it).second.moveCount<currTotalMoveCount){
			it = hashList->erase(it);
			outdatedCount++;
		}else{
			++it;
		}
	}
		

	hmQuerySuccessfulCount = 0;
	maxDepthReached = 0;
	nodesVisitedCount = 0;
	branchesCount = 0;
	totalSortingTime = 0;
	endSearch = false;
		
	TreeNode bestNext;
	startTime = clock();
	for(int depth = 4; ; depth++){
		depthLim = depth;
		bestNext = minMaxAlphaBeta(currState,depth-1,0-INT_MAX, INT_MAX, true);
		maxDepthReached = depth;
		clock_t currTime = clock();
		if((currTime - startTime > timeLim) || bestNext.v==GameState::MAX_STATE_VALUE || bestNext.v==GameState::MIN_STATE_VALUE || endSearch) break;
	}
	return bestNext.nextMove;
}
bool isAscending(const TreeNode& a, const TreeNode& b){return (a.v<b.v);}
bool isDescending(const TreeNode& a, const TreeNode& b){return (a.v>b.v);}
std::vector<Point> GameTree::sortMoves(GameState *curr, std::vector<Point> nextPossibleMoves, int side){
	std::vector<TreeNode> sortable;

	//evaluate all moves
	for(int i=0; i<nextPossibleMoves.size(); i++){
		Point p = nextPossibleMoves.at(i);
		int x = p.x;
		int y = p.y;
		int valueBeforeMove = curr->updateValue(y, x);
		curr->addPiece(x, y);
		TreeNode t; 
		t.nextMove = p;
		t.v = curr->updateValue(y,x) - valueBeforeMove;//evaluate();
		curr->revertOneMove();
		sortable.push_back(t);
	}
	//sort moves by value
	if(side == GameState::MAX_PLAYER){
		std::sort(sortable.begin(),sortable.end(),isDescending);
	}else{
		std::sort(sortable.begin(),sortable.end(),isAscending);
	}
	std::vector<Point> sortedMoves;
	for(int i=0; i<sortable.size(); i++){
		sortedMoves.push_back(sortable.at(i).nextMove);
	}
	return sortedMoves;
}

TreeNode GameTree::minMaxAlphaBeta(GameState *curr, int depth, int alpha, int beta, bool useNullMove){
	nodesVisitedCount++;
		
	Point previousBest;
	previousBest.x=-1;

	//query hash table
	int idx = hm.get(hasher->hash(curr));
	if(idx!=-1){
		TreeNode queryRoot = hm.at(idx);
		if(queryRoot.searchDepth>=depth && !(depthLim-depth == 1 && queryRoot.nextMove.x==-1)) {
			hmQuerySuccessfulCount++;
			return queryRoot;
		}else{
			previousBest = queryRoot.nextMove;
		}
	}


	std::vector<Point> nextPossibleMoves = curr->nextPossibleMoves();
		
	clock_t start = clock();
	//order search by resultant state value
	nextPossibleMoves = sortMoves(curr, nextPossibleMoves, curr->getCurrSide());
	clock_t end = clock();
	totalSortingTime += end-start;
		
	//if previous less deeper search result available, prioritize it as first search since it is likely still be the best
	if(previousBest.x!=-1){
		Point prev = nextPossibleMoves[0];
		for(int i=1; i<nextPossibleMoves.size(); i++){
			Point p = nextPossibleMoves[i];
			nextPossibleMoves[i] = prev;
			if(p.x == previousBest.x && p.y == previousBest.y){
				nextPossibleMoves[0] = p;
				break;
			}else{
				prev = p;
			}
		}
	}
		
	TreeNode root;			//the node to return
	Point selectedMove;		//the state selected
	selectedMove.x = -1;
	selectedMove.y = -1;
	int selectedSearchDepth = 0;
	if(nextPossibleMoves.size()==0){
		root.nextMove = selectedMove;
		root.moveCount = curr->getMoves().size();
		root.v=curr->evaluate();
		root.searchDepth = INT_MAX;
	}
	else if(nextPossibleMoves.size()==1 && (depthLim-depth)==1){
		root.nextMove = nextPossibleMoves[0];
		endSearch = true;
		return root;
	}
	else if(curr->getCurrSide() == GameState::MAX_PLAYER){
		int max = 0;
		bool maxNotInit = true;
			
		if(depth > 0 && (depthLim - depth>=3) && useNullMove){
			//make null move
			curr->makeNullMove();
			TreeNode nullMoveBestNext = minMaxAlphaBeta(curr, depth-1-NULLMOVE_R, alpha, beta, !useNullMove);
			curr->revertNullMove();
				
			if(nullMoveBestNext.v>=beta || nullMoveBestNext.v==GameState::MAX_STATE_VALUE){
				root.nextMove = selectedMove;
				root.moveCount = curr->getMoves().size()+1;
				root.v = nullMoveBestNext.v;
				root.searchDepth = std::max(depth, nullMoveBestNext.searchDepth);
				hm.put(hasher->hash(curr), root);
				branchesCount++;
				return root;
			}
		}
			
			
			
		for(int i=0; i<nextPossibleMoves.size(); i++){
			Point p = nextPossibleMoves[i];
			branchesCount++;
				
			curr->addPiece(p.x, p.y);
			int value = 0;
			int searchDepth = depth;
			if(depth<=0){
				value = curr->evaluate();		
			}else{
				TreeNode bestNext = minMaxAlphaBeta(curr, depth-1, alpha, beta, !useNullMove);
				value = bestNext.v;
				searchDepth = std::max(searchDepth, bestNext.searchDepth);
			}
			curr->revertOneMove();
				
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
				
			if(max>=beta || max==GameState::MAX_STATE_VALUE){
				break;
			}else{
				if(max>alpha){
					alpha = max;
				}
			}
				
			clock_t currTime = clock();
			if(currTime - startTime > timeLim) break;
		}
		root.nextMove = selectedMove;
		root.moveCount = curr->getMoves().size()+1;
		root.v = max;
		root.searchDepth = selectedSearchDepth;
			
	}
	else{
		int min = 0;
		bool minNotInit = true;
			
			
		if(depth>0 && (depthLim - depth>=3) && useNullMove){
			//make null move
			curr->makeNullMove();
			TreeNode nullMoveBestNext = minMaxAlphaBeta(curr, depth-1-NULLMOVE_R, alpha, beta, !useNullMove);
			curr->revertNullMove();
				
			if(nullMoveBestNext.v<=alpha || nullMoveBestNext.v==GameState::MIN_STATE_VALUE){
				root.nextMove = selectedMove;
				root.moveCount = curr->getMoves().size()+1;
				root.v = nullMoveBestNext.v;
				root.searchDepth = std::max(depth, nullMoveBestNext.searchDepth);
				hm.put(hasher->hash(curr), root);
				branchesCount++;
				return root;
			}
		}
			
			
		for(int i=0; i<nextPossibleMoves.size(); i++){
			Point p = nextPossibleMoves[i];
			branchesCount++;
				
			curr->addPiece(p.x, p.y);
			int value = 0;
			int searchDepth = depth;
			if(depth<=0){
				value = curr->evaluate();
			}else{
				TreeNode bestNext = minMaxAlphaBeta(curr, depth-1, alpha, beta, !useNullMove);
				value = bestNext.v;
				searchDepth = std::max(searchDepth, bestNext.searchDepth);
			}
			curr->revertOneMove();
				
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
				
			if(min<=alpha || min==GameState::MIN_STATE_VALUE){
				break;
			}else{
				if(min<beta){
					beta = min;
				}
			}
			clock_t currTime = clock();
			if(currTime - startTime > timeLim) break;
		}
		root.nextMove = selectedMove;
		root.moveCount = curr->getMoves().size()+1;
		root.v = min;
		root.searchDepth = selectedSearchDepth;
			
	}
	hm.put(hasher->hash(curr), root);
	return root;

}

HashMap::HashMap(){
	hashList.clear();
}
HashMap::~HashMap(){
	hashList.clear();
}
int HashMap::get(int key){
	int size = hashList.size();

	//TODO: convert to binary search
	int start = 0;
	int end = size-1;
	int mid = size>>1;	//faster than divide by 2
	while(start<=end){
		if(hashList[mid].first>key){
			end = mid-1;
		}else if(hashList[mid].first<key){
			start = mid+1;
		}else{
			return mid;
		}
		mid = (end+start)>>1;	//faster than divide by 2
	}
	/*
	for(int i=0; i<size; i++){
		if(key == hashList[i].first){
			return i;
		}
	}
	*/
	return -1;
}

bool HashMap::put(int key, TreeNode node){
	int idx = get(key);
	if(idx == -1){
		bool notInserted = true;
		std::vector<std::pair<int,TreeNode> >::iterator it;
		for(it = hashList.begin(); it!=hashList.end();){
			if((*it).first>key){
				it=hashList.insert(it,std::pair<int,TreeNode>(key,node));
				notInserted = false;
				break;
			}else{
				++it;
			}
		}
		if(notInserted){
			hashList.push_back(std::pair<int,TreeNode>(key,node));
		}

		//hashList.push_back(std::pair<int,TreeNode>(key,node));
	}else{
		hashList.at(idx).second = node; 
	}
	return true;
}
