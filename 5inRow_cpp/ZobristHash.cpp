#include "ZobristHash.h"
#include <cstdlib>
//#include <stdlib.h>

ZobristHash::ZobristHash(int rows, int cols, int statesPerSquare)
{
	setRows(rows);
	setCols(cols);
	setStatesPerSquare(statesPerSquare);
	initTable();
}
ZobristHash::~ZobristHash(void)
{
	if(hashTable!=NULL){
		free (hashTable);
	}
}
void ZobristHash::initTable(){
	int tableSize = ROWS*COLS*STATES;
	hashTable = (int *)malloc(tableSize*sizeof(int));
	std::srand(NULL);
	for(int i=0; i<tableSize; i++){
		hashTable[i] = std::rand();
	}
}
int ZobristHash::hash(GameState *s){
	int hashValue = 0;
	int *state = s->getGameState();
	int *maxState = state;
	int *minState = state+ROWS;
		
	for(int r=0; r<ROWS; r++){
		for(int c=0; c<COLS; c++){
			int maxPlayerBit = s->getBit(maxState, r, c);
			int minPlayerBit = s->getBit(minState, r, c);
			if(maxPlayerBit==0 && minPlayerBit==0){
				hashValue ^= hashTable[(r*COLS+c)*STATES+2];
			}else if (maxPlayerBit!=0){
				hashValue ^= hashTable[(r*COLS+c)*STATES];
			}else{
				hashValue ^= hashTable[(r*COLS+c)*STATES+1];
			}
		}
	}
	return hashValue;
}
