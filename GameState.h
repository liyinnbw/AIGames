/*
* Different game implement this class differently
*/
#pragma once
#include "State.h"

typedef struct{
	int x;
	int y;
	int side;
}myPiece;

class GameState : public State
{
public:
	GameState(int r, int c, int side);
	GameState(int r, int c, int side, int* state);
	~GameState(void);
	void setGridSize(int r, int c){rows=r; cols=c;}
	void setSide(int side){currSide = side;}
	void setGameState(int *s);
	void addPiece(int x, int y);
	std::vector<myPiece> getPieces();
	int* getGameState(){return gameState;}
	int getCurrSide(){return currSide;}
	const static int WHITE_SIDE = -1;
	const static int BLACK_SIDE = 1;
	const static int WIN_CONNECT = 5;

	bool isGameOver();
	double	evaluate();
	std::vector<GameState> getNexts();
	void next();
	std::string toString();

private:
	void initState();
	double getScore(int side);
	bool checkWin(int side);
	int rows;
	int cols;
	int currSide;
	int *gameState;
};

