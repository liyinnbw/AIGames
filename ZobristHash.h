#pragma once
#include "GameState.h"

class ZobristHash
{
public:
	//constructor & destructor
	ZobristHash(int rows, int cols, int statesPerSquare);
	~ZobristHash(void);

	//setters
	void setRows(int r){ROWS=r;}
	void setCols(int c){COLS=c;}
	void setStatesPerSquare(int statesPerSquare){STATES=statesPerSquare;}

	//getters
	int getRows(){return ROWS;}
	int getCols(){return COLS;}

	int hash(GameState *s);

private:
	void initTable();
	int ROWS;
	int COLS;
	int STATES;
	int *hashTable;
};

