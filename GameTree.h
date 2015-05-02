#pragma once
#include "GameState.h"
typedef struct{
	GameState *state;	//must use pointer since state contains dynamic size array
						//struct size cannot vary so pointer (always 4 byte for 32bit
						// and 8 byte for 64-bit) is good
	double value;
}TreeNode;
class GameTree
{
public:
	GameTree(int depthLim, GameState *s, int side);
	~GameTree(void);
	const static int MAX_PLAYER = 1;
	const static int MIN_PLAYER = -1;
	
	void setDepthLimit(int d){depthLimit=d;}
	void setRootState(GameState *s){rootState = s;}
	GameState *getRootState(){return rootState;}

	GameState next();
	TreeNode minMax(GameState *root, int depth, int turn);
private:
	GameState *rootState;
	int rootSide;
	int depthLimit;
	
};

