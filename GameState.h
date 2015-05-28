/*
* Different game implement this class differently
*/
#pragma once
#include <vector>
#include <string>
#include <limits.h>
#include <stdlib.h>

typedef struct{
	int x;
	int y;
}Point;

class GameState
{
public:
	const static int MAX_PLAYER = 0;
	const static int MIN_PLAYER = 1;
	const static int MIN_STATE_VALUE = 0-INT_MAX;
	const static int MAX_STATE_VALUE = INT_MAX;
	const static int TIE = 2;
	const static int WIN_CONNECT = 5;
	const static int H_DIRECTION = 1;
	const static int V_DIRECTION = 2;
	const static int D1_DIRECTION = 3;
	const static int D2_DIRECTION = 4;
	const static int ALL_DIRECTION = 5;
	const static int LIVE_4 = MAX_STATE_VALUE;
	const static int DOUBLE_DEAD_4 = MAX_STATE_VALUE;
	const static int DEAD_4_LIVE_3 = MAX_STATE_VALUE;
	const static int DOUBLE_LIVE_3 = 80;
	const static int DEAD_3_LIVE_3 = 70;
	const static int DEAD_4 = 60;
	const static int LIVE_3 = 50;
	const static int DOUBLE_DEAD_3 = 40;
	const static int DEAD_3_LIVE_2 = 40;
	const static int DEAD_3 = 30;
	const static int DOUBLE_LIVE_2 = 30;
	const static int LIVE_2 = 20;
	const static int DEAD_2 = 10;
	const static int BIT_LIVE_4   = 30;	//011110
	const static int BIT_DEAD_4_1 = 30; //11110
	const static int BIT_DEAD_4_2 = 29; //11101
	const static int BIT_DEAD_4_3 = 27; //11011
	const static int BIT_DEAD_4_4 = 23; //10111
	const static int BIT_DEAD_4_5 = 15; //01111
	const static int BIT_LIVE_3_2 = 14; //01110
	const static int BIT_LIVE_3_3 = 22; //010110
	const static int BIT_LIVE_3_4 = 26; //011010
	const static int BIT_DEAD_3_1 = 7;  //00111
	const static int BIT_DEAD_3_2 = 19; //10011
	const static int BIT_DEAD_3_3 = 25; //11001
	const static int BIT_DEAD_3_4 = 28; //11100
	const static int BIT_DEAD_3_5 = 21; //10101
	const static int BIT_DEAD_3_6 = 14; //01110
	const static int BIT_DEAD_3_7 = 22; //10110
	const static int BIT_DEAD_3_8 = 11; //01011
	const static int BIT_DEAD_3_9 = 13; //01101
	const static int BIT_DEAD_3_10= 26; //11010
	const static int BIT_LIVE_2   = 12; //001100
	const static int BIT_DEAD_2_1 = 3;  //00011
	const static int BIT_DEAD_2_2 = 6;  //00110
	const static int BIT_DEAD_2_3 = 12; //01100
	const static int BIT_DEAD_2_4 = 24; //11000
	const static int BIT_DEAD_2_5 = 18; //010010
	const static int BIT_DEAD_2_6 = 20; //0010100
	const static int BIT_MASK_8 = 255;  //11111111
	const static int BIT_MASK_7 = 127;  //1111111
	const static int BIT_MASK_6 = 63;   //111111
	const static int BIT_MASK_5 = 31;   //11111
	const static int BIT_MASK_4 = 15;   //1111

	//constructors & destructor
	GameState(int r, int c, int side);
	//GameState(int r, int c, int side, int* state);
	~GameState(void);

	//setters
	void setRows(int r){ROWS =r;}
	void setCols(int c){
		COLS =c;
		colMask = (int*) malloc (COLS*sizeof(int));
		for(int i=0; i<COLS; i++){
			colMask[i]=(1<<(COLS-1-i));
		}
	}
	void setCurrSide(int side){currSide = side;}
	void setGameState(int *s);
	
	//getters
	int *getGameState(){return state;}
	int getRows(){return ROWS;}
	int getCols(){return COLS;}
	int getCurrSide(){return currSide;}
	std::vector<Point> getMoves(){return moves;}
	int getBit(int *s, int r, int c){return (s[r] & colMask[c]);}

	bool addPiece(int x, int y);
	bool revertOneMove();
	bool makeNullMove();
	bool revertNullMove();
	int isGameOver();
	int evaluate();
	int updateValue(int r, int c);
	std::vector<Point> nextPossibleMoves();
	std::string toString();



	bool checkConnect(int side, int connect);
	bool checkFull();
	bool checkVertical(int *state, int connect);
	int *transpose(int *state);
	int *diagonalShiftL(int *state);
	int *diagonalShiftR(int *state);
	bool isTooFar(int *occupied, int r, int c);
	std::vector<Point> getUnoccupiedNeighbours(); 
	std::vector<Point> checkMustMoves(std::vector<Point> nexts);

	int evaluatePos(int r, int c, int side, int direction);
	int evaluatePattern(int thisSidePattern, int otherSidePattern, int pos, int len);
	int evaluateValues(int values[]);


private:
	void initState();
	void initMoves();
	void initNextMap();
	std::string intToRow(int r);


	

	int ROWS;		//tentatively maximum 32
	int COLS;		//tentatively maximum 32
	int currSide;
	int *state;		//[0][]: max player [1][]: min player
	int *colMask;	//for extracting bit at specific column of a row
	int *nextMap;	//for identifying possible next moves
	std::vector<Point> moves;
};

