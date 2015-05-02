#include "GameState.h"
#include "GameTree.h"
#include <iostream>
#include <sstream>

GameState::GameState(int r, int c, int side)
{
	setGridSize(r,c);
	setSide(side);
	gameState = (int*) malloc(rows*cols*sizeof(int));
	initState();
}
void GameState::initState(){
	for(int i=0; i<rows*cols; i++){
			gameState[i]=0;
	}
}
GameState::GameState(int r, int c, int side, int* state){
	setGridSize(r,c);
	setSide(side);
	gameState = (int*) malloc(rows*cols*sizeof(int));
	setGameState(state);
}
GameState::~GameState(void)
{
	//free(gameState);
}
void GameState::setGameState(int *state){
	for(int i=0; i<rows*cols; i++){
		gameState[i]=state[i];
	}
}
void GameState::addPiece(int x, int y){
	if(x>=rows || y>= cols || x<0 || y<0 || gameState[y*cols+x]!=0) return;
	gameState[y*cols+x]=currSide;
	currSide*=-1;
}

bool GameState::isGameOver(){
	bool boardFull = false;
	for(int i=0; i<rows*cols; i++){
		if(gameState[i]==0) break;
	}
	if(boardFull) return true;

	if(checkWin(WHITE_SIDE) || checkWin(BLACK_SIDE)) return true;
	
	return false;
}
bool GameState::checkWin(int side){
	//check horizontal
	for(int i=0; i<rows; i++){
		int count=0;
		for(int j=0; j<cols; j++){
			if(gameState[i*cols+j]==side){
				count++;
				if(count==WIN_CONNECT) return true;
			}else{
				count=0;
			}
		}
	}
	//check vertical
	for(int j=0; j<cols; j++){
		int count=0;
		for(int i=0; i<rows; i++){
			if(gameState[i*cols+j]==side){
				count++;
				if(count==WIN_CONNECT) return true;
			}else{
				count=0;
			}
		}
	}
	return false;
}
double GameState::evaluate(){
	if(checkWin(WHITE_SIDE)) return -1*std::numeric_limits<double>::max();
	if(checkWin(BLACK_SIDE)) return std::numeric_limits<double>::max();
	double whiteScore = getScore(WHITE_SIDE);
	double blackScore = getScore(BLACK_SIDE);

	return whiteScore+blackScore;
}
double GameState::getScore(int side){
	int totalLen = 0;
	int totalFilled = 0;

	//check horizontal
	for(int i=0; i<rows; i++){
		int countLen =0;		//length of unblocked segment
		int countFilled=0;		//spots taken by this side in the unblocked segment
		for(int j=0; j<cols; j++){
			if(gameState[i*cols+j]==-1*side){
				if(countLen>=WIN_CONNECT){
					totalLen+=countLen;
					totalFilled+=countFilled;
				}
				countLen =0;
				countFilled=0;
			}else{
				countLen++;
				countFilled++;
				if(j==cols-1){
					if(countLen>=WIN_CONNECT){
						totalLen+=countLen;
						totalFilled+=countFilled;
					}
				}
			}
		}
	}
	//check vertical
	for(int j=0; j<cols; j++){
		int countLen =0;		//length of unblocked segment
		int countFilled=0;		//spots taken by this side in the unblocked segment
		for(int i=0; i<rows; i++){
			if(gameState[i*cols+j]==-1*side){
				if(countLen>=WIN_CONNECT){
					totalLen+=countLen;
					totalFilled+=countFilled;
				}
				countLen =0;
				countFilled=0;
			}else{
				countLen++;
				countFilled++;
				if(i==rows-1){
					if(countLen>=WIN_CONNECT){
						totalLen+=countLen;
						totalFilled+=countFilled;
					}
				}
			}
		}
	}

	return side*(totalLen*0.4+totalFilled*0.6);

}
std::vector<GameState> GameState::getNexts(){
	std::vector<GameState> nexts;
	for(int i=0; i<rows; i++){
		for(int j=0; j<cols; j++){
			int gst = gameState[i*cols+j];
			if(gst==0){
				int *gs = getGameState();
				GameState s(rows, cols, currSide, gs);
				s.addPiece(j,i); //addPiece uses grid coordinates, so j,i
				nexts.push_back(s);
			}
 		}
	}
	return nexts;
}

void GameState::next(){
	/*
	GameState s(rows, cols, currSide, getGameState());
	GameTree gt(3,s,GameTree::MIN_PLAYER);
	GameState nextBest = gt.next();
	setGameState(nextBest.getGameState());
	*/
}

std::vector<myPiece> GameState::getPieces(){

	std::vector<myPiece> pieces;
	for(int i=0; i<rows; i++){
		for(int j=0; j<cols; j++){
			int p = gameState[i*cols+j];
			if (p!=0){
				myPiece m;
				m.x=j;
				m.y=i;
				m.side = p;
				pieces.push_back(m);
			}
		}
	}
	
	return pieces;

}

std::string GameState::toString(){
	std::string s;
	s+="====gameState====\n";
	std::stringstream ss;
	ss << rows<<"x"<<cols<<"\n";
	s+="game board = "+ss.str();
	ss.clear();
	for(int i=0; i<rows; i++){
		for(int j=0; j<cols; j++){
			ss<<gameState[i*cols+j]<<"\t";
		}
		ss<<"\n";
	}
	s+=ss.str();
	return s;
}