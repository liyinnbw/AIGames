#pragma once
#include "GameState.h"
#include "ZobristHash.h"
#include <ctime>
typedef struct{
	//GameState *state;	//must use pointer since state contains dynamic size array
						//struct size cannot vary so pointer (always 4 byte for 32bit
						// and 8 byte for 64-bit) is good
	Point nextMove;
	int moveCount;
	int v;
	int searchDepth;
}TreeNode;

class HashMap
{
public:
	HashMap(void);
	~HashMap(void);

	int size(){return hashList.size();}
	int get(int key);
	TreeNode at(int idx){return hashList.at(idx).second;}
	bool put(int key, TreeNode node);
	std::vector<std::pair<int, TreeNode> > *getHashList(){return &hashList;}
private:
	std::vector<std::pair<int, TreeNode> > hashList;
};

class GameTree
{
public:
	const static int NULLMOVE_R = 2;
	//constructor & destructor
	GameTree(GameState *s, int tLim);
	~GameTree(void);
	
	//setter
	void setCurrState(GameState *curr){currState = curr;}
	void setTimeLim(int tLim){timeLim = tLim*CLOCKS_PER_SEC;}

	//getter
	GameState *getCurrState(){return currState;}

	Point nextMove();
	TreeNode minMaxAlphaBeta(GameState *curr, int depth, int alpha, int beta, bool useNullMove);

	//stats for print
	int hmQuerySuccessfulCount;
	int maxDepthReached;
	int nodesVisitedCount;
	int branchesCount;
	int totalSortingTime;
	int outdatedCount;

	HashMap hm; 

private:
	std::vector<Point> sortMoves(GameState *curr, std::vector<Point> nextPossibleMoves, int side);
	GameState *currState;
	ZobristHash *hasher;
	

	clock_t timeLim;
	clock_t startTime;
	int depthLim;
	bool endSearch;
	
	
};

