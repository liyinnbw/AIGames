#include "GameState.h"
#include <iostream>
#include <sstream>
#include <algorithm>

GameState::GameState(int r, int c, int side)
{
	setRows(r);
	setCols(c);
	setCurrSide(side);
	initState();
	initMoves();
	initNextMap();
}
void GameState::initState(){
	state = (int*) malloc(2*ROWS*sizeof(int));
	for(int i=0; i<2*ROWS; i++){
			state[i]=0;
	}
}
void GameState::initMoves(){
	if(!moves.empty()){
		moves.clear();
	}
}
void GameState::initNextMap(){
	nextMap = (int*) malloc(ROWS*COLS*sizeof(int));
	for(int i=0; i<ROWS*COLS; i++){
		nextMap[i] = 0;
	}
}
/*
GameState::GameState(int r, int c, int side, int* state){
	setGridSize(r,c);
	setSide(side);
	gameState = (int*) malloc(rows*cols*sizeof(int));
	setGameState(state);
}
*/
GameState::~GameState(void)
{
	free(state);
	free(nextMap);
	free(colMask);
}
/*
void GameState::setGameState(int *state){
	for(int i=0; i<rows*cols; i++){
		gameState[i]=state[i];
	}
}
*/
std::vector<Point> GameState::getUnoccupiedNeighbours(){
	std::vector<Point> neighbours;
	
	int *occupied = (int *)malloc(ROWS*sizeof(int));
	int maxPlayerOffset = MAX_PLAYER*ROWS;
	int minPlayerOffset = MIN_PLAYER*ROWS;
	for(int i=0; i<ROWS; i++){
		occupied[i]=state[maxPlayerOffset+i] | state[minPlayerOffset+i];
	}

	//init all values to 0
	for(int i=0; i<ROWS*COLS; i++){
		nextMap[i] = 0;
	}

	for(int i=0; i<ROWS; i++){
		int row = occupied[i];
		if(row == 0) continue;
		for(int j=0; j<COLS; j++){
			if((row & colMask[j])!=0){
				//if is 1, set 0 neighbour pos =1, set this pos =-1
				int idx = i*COLS+j;		//this pos
				nextMap[idx]=-1;
				int nidx = idx-COLS;	//north
				if(i>0 && nextMap[nidx]==0)					nextMap[nidx]=1;
				nidx = idx-COLS-1;		//north-west
				if(i>0 && j>0 && nextMap[nidx]==0)			nextMap[nidx]=1;
				nidx = idx-COLS+1;		//north-east
				if(i>0 && j<COLS-1 && nextMap[nidx]==0)		nextMap[nidx]=1;

				nidx = idx+COLS;		//south
				if(i<ROWS-1 && nextMap[nidx]==0)			nextMap[nidx]=1;
				nidx = idx+COLS-1;		//south-west
				if(i<ROWS-1 && j>0 && nextMap[nidx]==0)		nextMap[nidx]=1;
				nidx = idx+COLS+1;		//south-east
				if(i<ROWS-1 && j<COLS-1 && nextMap[nidx]==0)nextMap[nidx]=1;

				nidx = idx-1;			//west
				if(j>0 && nextMap[nidx]==0)					nextMap[nidx]=1;
				nidx = idx+1;			//east
				if(j<COLS-1 && nextMap[nidx]==0)			nextMap[nidx]=1;
			}
		}
	}

	free(occupied);

	for(int i=0; i<ROWS; i++){
		for(int j=0; j<COLS; j++){
			if(nextMap[i*COLS+j]==1){ 
				Point p;
				p.x=j;
				p.y=i;
				neighbours.push_back(p);
			}
		}
	}

	return neighbours;
	
}
std::vector<Point> GameState::nextPossibleMoves(){
	std::vector<Point> nexts;
	if(isGameOver()!=-1) return nexts;

	nexts = getUnoccupiedNeighbours();
	nexts = checkMustMoves(nexts);

	return nexts;
}
bool GameState::isTooFar(int *occupied, int r, int c){
	int *newPieceExpandedMap = (int *)malloc(ROWS*sizeof(int));
	for(int i=0; i<ROWS; i++){
		newPieceExpandedMap[i]=0;
	}
	for(int i=0; i<ROWS; i++){
		for(int j=0; j<COLS; j++){
			if(((i-r)<2 && (i-r)>-2) && ((j-c)<2 && (j-c)>-2)){
				newPieceExpandedMap[i] |= colMask[j];
			}
		}
	}
	for(int i=0; i<ROWS; i++){
		if((occupied[i] & newPieceExpandedMap[i])!=0){
			free(newPieceExpandedMap);
			return false;
		}
	}
	
	free(newPieceExpandedMap);
	return true;
}
std::vector<Point> GameState::checkMustMoves(std::vector<Point> nexts){
	std::vector<Point> winMoves;
	std::vector<Point> blockWinMoves;
	std::vector<Point> threatMoves; 
	std::vector<Point> blockThreatMoves;
		
	for(int i=0; i<nexts.size(); i++){
		Point p = nexts.at(i);
		int valueThisSide = evaluatePos(p.x, p.x, currSide, ALL_DIRECTION);
		int valueOtherSide = evaluatePos(p.y, p.x, 1-currSide, ALL_DIRECTION);
			
		if(valueThisSide == DEAD_4 || valueThisSide == LIVE_4){
			winMoves.push_back(p);
			return winMoves;
		}else if(valueOtherSide == DEAD_4 || valueOtherSide == LIVE_4){
			blockWinMoves.push_back(p);
		}
		else if(valueOtherSide >= LIVE_3){
			blockThreatMoves.push_back(p);
		}else if(valueThisSide >= DEAD_3){
			threatMoves.push_back(p);
		}
	}
		
	if(blockWinMoves.size()>0){
		return blockWinMoves;
	}
		
	if(blockThreatMoves.size()>0){
		for(int i=0; i<threatMoves.size(); i++){
			Point p = threatMoves.at(i);
			blockThreatMoves.push_back(p);
		}
		return blockThreatMoves;
	}
	return nexts;
}

bool GameState::addPiece(int x, int y){
	if(y>=ROWS || x>= COLS || x<0 || y<0) return false;
		
	int bRow = state[MAX_PLAYER*ROWS+y];
	int wRow = state[MIN_PLAYER*ROWS+y];	
	int bitMap = 1<<COLS-x-1;
	int wBit = wRow & bitMap;
	int bBit = bRow & bitMap;
		
	//already occupied
	if(wBit!=0 || bBit!=0) return false;

	state[currSide*ROWS+y] |= bitMap;
	Point p;
	p.x = x;
	p.y = y;
	moves.push_back(p);
	setCurrSide(1-currSide);
	return true;
}
bool GameState::revertOneMove(){
		
	if(moves.empty()) return false;
		
	Point lastMove = moves.back();
	int x = lastMove.x;
	int y = lastMove.y;
	moves.pop_back();
	int bitMask = ~ colMask[x];
	int rowIdx = (1-currSide)*ROWS+y;
	state[rowIdx] = state[rowIdx] & bitMask;
	setCurrSide(1-currSide);
	return true;
}
bool GameState::makeNullMove(){
	setCurrSide(1-currSide);
	return true;
}
bool GameState::revertNullMove(){
	setCurrSide(1-currSide);
	return true;
}

//TODO: consider the situation when both side have MAX/MIN value
int GameState::evaluate(){
	if(checkConnect(MAX_PLAYER,WIN_CONNECT)) return MAX_STATE_VALUE;
	if(checkConnect(MIN_PLAYER,WIN_CONNECT)) return MIN_STATE_VALUE;
		
	int maxplayerTotal = 0;
	int minplayerTotal = 0;
		
	int maxplayerOffset = MAX_PLAYER*ROWS;
	int minplayerOffset = MIN_PLAYER*ROWS;
	for(int i=0; i<ROWS; i++){
		for(int j=0; j<COLS; j++){
				
			//only need to calculate for non-zero pos
			if((state[maxplayerOffset+i] & colMask[j])!=0){
				int maxplayerValue=evaluatePos(i,j,MAX_PLAYER, ALL_DIRECTION);
				if (maxplayerValue == MAX_STATE_VALUE) return MAX_STATE_VALUE;
				maxplayerTotal+=(maxplayerValue+1);
			}
			else if((state[minplayerOffset+i] & colMask[j])!=0){
				int minplayerValue=0-evaluatePos(i,j,MIN_PLAYER, ALL_DIRECTION);
				if (minplayerValue == MIN_STATE_VALUE) return MIN_STATE_VALUE;
				minplayerTotal+=(minplayerValue-1);
			}
		}
	}
		
	return maxplayerTotal+minplayerTotal;

}

//TODO: consider the situation when there is MAX/MIN value
int GameState::updateValue(int r, int c){
	int maxplayerTotal = 0;
	int minplayerTotal = 0;

	int maxplayerOffset = MAX_PLAYER*ROWS;
	int minplayerOffset = MIN_PLAYER*ROWS;
		
	//evaluate the newly changed pos
	if((state[maxplayerOffset+r] & colMask[c])!=0){
		maxplayerTotal += evaluatePos(r,c,MAX_PLAYER, ALL_DIRECTION);
	}else if((state[minplayerOffset+r] & colMask[c])!=0){
		minplayerTotal -= evaluatePos(r,c,MIN_PLAYER, ALL_DIRECTION);
	}
		
	//update horizontally affected pos
	for(int i=0; i<COLS; i++){
		if(i!=c){
			if((state[maxplayerOffset+r] & colMask[i])!=0){
				maxplayerTotal +=evaluatePos(r,i, MAX_PLAYER, H_DIRECTION);
			}
			else if((state[minplayerOffset+r] & colMask[i])!=0){
				minplayerTotal -=evaluatePos(r,i, MIN_PLAYER, H_DIRECTION);
			}
				
		}
	}
		
	//update vertically affected pos
	for(int i=0; i<ROWS; i++){
		if(i!=r){
			if((state[maxplayerOffset+i] & colMask[c])!=0){
				maxplayerTotal +=evaluatePos(i,c, MAX_PLAYER, V_DIRECTION);
			}
			else if((state[minplayerOffset+i] & colMask[c])!=0){
				minplayerTotal -=evaluatePos(i,c, MIN_PLAYER, V_DIRECTION);
			}
		}	
	}
		
	//update diagonal '\'
	for(int i=1; i<ROWS; i++){
		if(r-i<0 || c-i<0){
			break;
		}
		int row = r-i;
		int col = c-i;
		if((state[maxplayerOffset+row] & colMask[col])!=0){
			maxplayerTotal +=evaluatePos(row,col, MAX_PLAYER, D1_DIRECTION );
		}
		else if((state[minplayerOffset+row] & colMask[col])!=0){
			minplayerTotal -=evaluatePos(row,col, MIN_PLAYER, D1_DIRECTION );
		}
	}
	for(int i=1; i<ROWS; i++){
		if(r+i>=ROWS || c+i>=COLS){
			break;
		}
		int row = r+i;
		int col = c+i;
		if((state[maxplayerOffset+row] & colMask[col])!=0){
			maxplayerTotal +=evaluatePos(row,col, MAX_PLAYER, D1_DIRECTION );
		}
		else if((state[minplayerOffset+row] & colMask[col])!=0){
			minplayerTotal -=evaluatePos(row,col, MIN_PLAYER, D1_DIRECTION );
		}
	}
		
	//update diagonal '/'
	for(int i=1; i<ROWS; i++){
		if(r-i<0 || c+i>=COLS){
			break;
		}
		int row = r-i;
		int col = c+i;
		if((state[maxplayerOffset+row] & colMask[col])!=0){
			maxplayerTotal +=evaluatePos(row,col, MAX_PLAYER, D2_DIRECTION );
		}
		else if((state[minplayerOffset+row] & colMask[col])!=0){
			minplayerTotal -=evaluatePos(row,col, MIN_PLAYER, D2_DIRECTION);
		}
	}
	for(int i=1; i<ROWS; i++){
		if(r+i>=ROWS || c-i<0){
			break;
		}
		int row = r+i;
		int col = c-i;
		if((state[maxplayerOffset+row] & colMask[col])!=0){
			maxplayerTotal +=evaluatePos(row,col, MAX_PLAYER, D2_DIRECTION);
		}
		else if((state[minplayerOffset+row] & colMask[col])!=0){
			minplayerTotal -=evaluatePos(row,col, MIN_PLAYER, D2_DIRECTION);
		}
	}
		
	return maxplayerTotal+minplayerTotal;
}
int GameState::evaluatePos(int r, int c, int side, int direction){
	int thisSideOffSet = side*ROWS;
	int otherSideOffSet = (1-side)*ROWS;
	int thisSideBits = 0;
	int otherSideBits = 0;
	int values[4]={0,0,0,0};
	if(direction== H_DIRECTION  || direction == ALL_DIRECTION ){
		//check horizontal
		thisSideBits = state[thisSideOffSet+r];
		otherSideBits = state[otherSideOffSet+r];
		values[0] = evaluatePattern(thisSideBits, otherSideBits, COLS-1-c, COLS);
		if (values[0] == MAX_STATE_VALUE) return MAX_STATE_VALUE;
	}
		
	if(direction== V_DIRECTION  || direction == ALL_DIRECTION ){
		//check vertical
		thisSideBits = 0;
		otherSideBits = 0;
		for(int i=0; i<ROWS; i++){
			if(i>0){
				thisSideBits =  thisSideBits << 1;
				otherSideBits =  otherSideBits << 1;
			}
			int bitThisSide = state[thisSideOffSet+i] & colMask[c];
			int bitOtherSide = state[otherSideOffSet+i] & colMask[c];
			if(bitThisSide != 0){
				thisSideBits |= 1;
			}else if(bitOtherSide != 0){
				otherSideBits |= 1;
			}
		}
		values[1] = evaluatePattern(thisSideBits, otherSideBits, ROWS-1-r, ROWS);
		if (values[1] == MAX_STATE_VALUE) return MAX_STATE_VALUE;
	}
		
	if(direction== D1_DIRECTION  || direction == ALL_DIRECTION ){
		//check diagonal '\'
		thisSideBits = 0;
		otherSideBits = 0;
		bool isFirst = true;
		int len = 0;
		for(int i=ROWS-1; i>=0; i--){
			if(r-i<0 || c-i<0){
				continue;
			}
			if(isFirst){
				isFirst=false;
			}else{
				thisSideBits =  thisSideBits << 1;
				otherSideBits =  otherSideBits << 1;
			}
			if((state[thisSideOffSet+r-i] & colMask[c-i])!=0){
				thisSideBits |= 1;
			}else if((state[otherSideOffSet+r-i] & colMask[c-i])!=0){
				otherSideBits |= 1;
			}
			len++;
		}
		int count = 0;
		for(int i=1; i<ROWS; i++){
			if(r+i>=ROWS || c+i>=COLS){
				break;
			}
			thisSideBits =  thisSideBits << 1;
			otherSideBits =  otherSideBits << 1;
			if((state[thisSideOffSet+r+i] & colMask[c+i])!=0){
				thisSideBits |= 1;
			}else if((state[otherSideOffSet+r+i] & colMask[c+i])!=0){
				otherSideBits |= 1;
			}
			count++;
			len++;
		}
		values[2] = evaluatePattern(thisSideBits, otherSideBits, count, len);
		if (values[2] == MAX_STATE_VALUE) return MAX_STATE_VALUE;
	}
		
	if(direction== D2_DIRECTION  || direction == ALL_DIRECTION ){
		//check diagonal /
		thisSideBits = 0;
		otherSideBits = 0;
		bool isFirst = true;
		int len = 0;
		for(int i=ROWS-1; i>=0; i--){
			if(r-i<0 || c+i>=COLS){
				continue;
			}
			if(isFirst){
				isFirst=false;
			}else{
				thisSideBits =  thisSideBits << 1;
				otherSideBits =  otherSideBits << 1;
			}
			if((state[thisSideOffSet+r-i] & colMask[c+i])!=0){
				thisSideBits |= 1;
			}else if((state[otherSideOffSet+r-i] & colMask[c+i])!=0){
				otherSideBits |= 1;
			}
			len++;
		}
		int count = 0;
		for(int i=1; i<ROWS; i++){
			if(r+i>=ROWS || c-i<0){
				break;
			}
			thisSideBits =  thisSideBits << 1;
			otherSideBits =  otherSideBits << 1;
			if((state[thisSideOffSet+r+i] & colMask[c-i])!=0){
				thisSideBits |= 1;
			}else if((state[otherSideOffSet+r+i] & colMask[c-i])!=0){
				otherSideBits |= 1;
			}
			count++;
			len++;
		}
		values[3] = evaluatePattern(thisSideBits, otherSideBits, count, len);
		if (values[3] == MAX_STATE_VALUE) return MAX_STATE_VALUE;
	}

	int overall = evaluateValues(values);
	return overall;
}
int GameState::evaluatePattern(int thisSidePattern, int otherSidePattern, int pos, int len){
	int maskLen = 6;
	for(int i=0; i<maskLen; i++){
		if(i>pos) break;
		if(pos-i+maskLen>len) continue;
		int mask = (BIT_MASK_6 << pos) >> i;
		int blocked = otherSidePattern & mask;
		if (blocked!=0) continue;
		int checkRegion = thisSidePattern & mask;
			
		int live4 = (BIT_LIVE_4 << pos) >> i;
		int result = checkRegion ^ live4;
		if(result==0) return LIVE_4;
	}
	maskLen = 5;
	for(int i=0; i<maskLen; i++){
		if(i>pos) break;
		if(pos-i+maskLen>len) continue;
		int mask = (BIT_MASK_5 << pos) >> i;
		int blocked = otherSidePattern & mask;
		if (blocked!=0) continue;
		int checkRegion = thisSidePattern & mask;
			
		int dead4_1 = (BIT_DEAD_4_1 << pos) >> i;
		int result = checkRegion ^ dead4_1;
		if(result==0) return DEAD_4;
		int dead4_2 = (BIT_DEAD_4_2 << pos) >> i;
		result = checkRegion ^ dead4_2;
		if(result==0) return DEAD_4;
		int dead4_3 = (BIT_DEAD_4_3 << pos) >> i;
		result = checkRegion ^ dead4_3;
		if(result==0) return DEAD_4;
		int dead4_4 = (BIT_DEAD_4_4 << pos) >> i;
		result = checkRegion ^ dead4_4;
		if(result==0) return DEAD_4;
		int dead4_5 = (BIT_DEAD_4_5 << pos) >> i;
		result = checkRegion ^ dead4_5;
		if(result==0) return DEAD_4;
	}
		
	maskLen = 5;
	for(int i=0; i<maskLen; i++){
		if(i>pos) break;
		if(pos-i+maskLen> len) continue;
		int mask = (BIT_MASK_5 << pos) >> i;
		int blocked = otherSidePattern & mask;
		if (blocked!=0) continue;
		int checkRegion = thisSidePattern & mask;
			
		int live3_2 = (BIT_LIVE_3_2 << pos) >> i;
		int result = checkRegion ^ live3_2;
		if(result==0) return LIVE_3;
	}
		
	maskLen = 6;
	for(int i=0; i<maskLen; i++){
		if(i>pos) break;
		if(pos-i+maskLen> len) continue;
		int mask = (BIT_MASK_6 << pos) >> i;
		int blocked = otherSidePattern & mask;
		if (blocked!=0) continue;
		int checkRegion = thisSidePattern & mask;
			
		int live3_3 = (BIT_LIVE_3_3 << pos) >> i;
		int result = checkRegion ^ live3_3;
		if(result==0) return LIVE_3;
		int live3_4 = (BIT_LIVE_3_4 << pos) >> i;
		result = checkRegion ^ live3_4;
		if(result==0) return LIVE_3;
	}
		
	maskLen = 5;
	for(int i=0; i<maskLen; i++){
		if(i>pos) break;
		if(pos-i+maskLen> len) continue;
		int mask = (BIT_MASK_5 << pos) >> i;
		int blocked = otherSidePattern & mask;
		if (blocked!=0) continue;
		int checkRegion = thisSidePattern & mask;
			
		int dead3_1 = (BIT_DEAD_3_1 << pos) >> i;
		int result = checkRegion ^ dead3_1;
		if(result==0) return DEAD_3;
		int dead3_2 = (BIT_DEAD_3_2 << pos) >> i;
		result = checkRegion ^ dead3_2;
		if(result==0) return DEAD_3;
		int dead3_3 = (BIT_DEAD_3_3 << pos) >> i;
		result = checkRegion ^ dead3_3;
		if(result==0) return DEAD_3;
		int dead3_4 = (BIT_DEAD_3_4 << pos) >> i;
		result = checkRegion ^ dead3_4;
		if(result==0) return DEAD_3;
		int dead3_5 = (BIT_DEAD_3_5 << pos) >> i;
		result = checkRegion ^ dead3_5;
		if(result==0) return DEAD_3;
		int dead3_6 = (BIT_DEAD_3_6 << pos) >> i;
		result = checkRegion ^ dead3_6;
		if(result==0) return DEAD_3;
		int dead3_7 = (BIT_DEAD_3_7 << pos) >> i;
		result = checkRegion ^ dead3_7;
		if(result==0) return DEAD_3;
		int dead3_8 = (BIT_DEAD_3_8 << pos) >> i;
		result = checkRegion ^ dead3_8;
		if(result==0) return DEAD_3;
		int dead3_9 = (BIT_DEAD_3_9 << pos) >> i;
		result = checkRegion ^ dead3_9;
		if(result==0) return DEAD_3;
		int dead3_10= (BIT_DEAD_3_10<< pos) >> i;
		result = checkRegion ^ dead3_10;
		if(result==0) return DEAD_3;
	}
		
	maskLen = 6;
	for(int i=0; i<maskLen; i++){
		if(i>pos) break;
		if(pos-i+maskLen> len) continue;
		int mask = (BIT_MASK_6 << pos) >> i;
		int blocked = otherSidePattern & mask;
		if (blocked!=0) continue;
		int checkRegion = thisSidePattern & mask;
			
		int live2 = (BIT_LIVE_2 << pos) >> i;
		int result = checkRegion ^ live2;
		if(result==0) return LIVE_2;
	}
		
	maskLen = 5;
	for(int i=0; i<maskLen; i++){
		if(i>pos) break;
		if(pos-i+maskLen> len) continue;
		int mask = (BIT_MASK_5 << pos) >> i;
		int blocked = otherSidePattern & mask;
		if (blocked!=0) continue;
		int checkRegion = thisSidePattern & mask;
			
		int dead2_1 = (BIT_DEAD_2_1 << pos) >> i;
		int result = checkRegion ^ dead2_1;
		if(result==0) return DEAD_2;
		int dead2_2 = (BIT_DEAD_2_2 << pos) >> i;
		result = checkRegion ^ dead2_2;
		if(result==0) return DEAD_2;
		int dead2_3 = (BIT_DEAD_2_3 << pos) >> i;
		result = checkRegion ^ dead2_3;
		if(result==0) return DEAD_2;
		int dead2_4 = (BIT_DEAD_2_4 << pos) >> i;
		result = checkRegion ^ dead2_4;
		if(result==0) return DEAD_2;
	}
		
	maskLen = 6;
	for(int i=0; i<maskLen; i++){
		if(i>pos) break;
		if(pos-i+maskLen> len) continue;
		int mask = (BIT_MASK_6 << pos) >> i;
		int blocked = otherSidePattern & mask;
		if (blocked!=0) continue;
		int checkRegion = thisSidePattern & mask;
			
		int dead2_5 = (BIT_DEAD_2_5 << pos) >> i;
		int result = checkRegion ^ dead2_5;
		if(result==0) return DEAD_2;
	}
	maskLen = 7;
	for(int i=0; i<maskLen; i++){
		if(i>pos) break;
		if(pos-i+maskLen> len) continue;
		int mask = (BIT_MASK_7 << pos) >> i;
		int blocked = otherSidePattern & mask;
		if (blocked!=0) continue;
		int checkRegion = thisSidePattern & mask;
			
		int dead2_6 = (BIT_DEAD_2_6 << pos) >> i;
		int result = checkRegion ^ dead2_6;
		if(result==0) return DEAD_2;
	}
		
	return 0;		
}
//precon: values[] length = 4
int GameState::evaluateValues(int values[]){
	int max = 0;
	int countDead4 = 0;
	int countLive3 = 0;
	int countDead3 = 0;
	int countLive2 = 0;
	for(int i=0; i<4; i++){
		max = std::max(max, values[i]);
		switch(values[i]){
		case DEAD_4:
			countDead4++;
			break;
		case LIVE_3:
			countLive3++;
			break;
		case DEAD_3:
			countDead3++;
			break;
		case LIVE_2:
			countLive2++;
			break;
		default:
			break;
		}
	}
	if(countDead4>1){
		return std::max(max,DOUBLE_DEAD_4);
	}
	if(countDead4>0 && countLive3>0){
		return std::max(max,DEAD_4_LIVE_3);
	}
	if(countLive3>1){
		return std::max(max,DOUBLE_LIVE_3);
	}
	if(countLive3>0 && countDead3>0){
		return std::max(max,DEAD_3_LIVE_3);
	}
	if(countDead3>1){
		return std::max(max,DOUBLE_DEAD_3);
	}
	if(countLive2>0 && countDead3>0){
		return std::max(max,DEAD_3_LIVE_2);
	}
	if(countLive2>1){
		return std::max(max,DOUBLE_LIVE_2);
	}
	return max;
}
int GameState::isGameOver(){
	if(checkConnect(MAX_PLAYER,WIN_CONNECT)) return MAX_PLAYER;	//0
	if(checkConnect(MIN_PLAYER,WIN_CONNECT)) return MIN_PLAYER;	//1
	if(checkFull()) return TIE;									//2
		
	return -1;
}
bool GameState::checkFull(){
	int *occupied = (int *)malloc(ROWS*sizeof(int));
	int maxPlayerOffset = MAX_PLAYER*ROWS;
	int minPlayerOffset = MIN_PLAYER*ROWS;
	for(int i=0; i<ROWS; i++){
		occupied[i]=state[maxPlayerOffset+i] | state[minPlayerOffset+i];
	}
	int full = INT_MAX & ((1<<COLS)-1);
	for(int i=0; i<ROWS; i++){
		if(occupied[i]!=full){
			free(occupied);
			return false;
		}
	}
	
	free(occupied);
	return true;
}
bool GameState::checkConnect(int side, int connect){
	int *s = state+side*ROWS;

	//check vertical
	if(checkVertical(s,connect)) return true;
		
	//check horizontal
	int *tpState = transpose(s);
	bool checkResult = checkVertical(tpState,connect);
	free(tpState);
	if(checkResult) return true;
		
	//check diagonal '\'
	int *sflState = diagonalShiftL(s);
	checkResult = checkVertical(sflState,connect);
	free(sflState);
	if(checkResult) return true;
		
	//check diagonal '/'
	int *sfrState = diagonalShiftR(s);
	checkResult = checkVertical(sfrState,connect);
	free(sfrState);
	if(checkResult) return true;
		
	return false;
}

bool GameState::checkVertical(int *state, int connect){
	int end = ROWS-connect;
	for(int i=0; i<=end; i++){
		int result=state[i];
		for(int j=1; j<connect; j++){
			result &= state[i+j];
		}
		if (result!=0) return true;
	}
	return false;
}
int * GameState::transpose(int *state){
	int *tpState = (int *)malloc(COLS*sizeof(int));
	for(int i=0; i<COLS; i++){
		int col = 0;
		for(int j=0; j<ROWS; j++){
			int bit = (state[j] & colMask[i])<<i;
			bit = bit>>j;
			col |= col | bit;
		}
		tpState[i]= col;
	}
	return tpState;
}
int * GameState::diagonalShiftL(int *state){
	int *sfState =(int *)malloc(ROWS*sizeof(int));
	for(int i=0; i<ROWS; i++){
		sfState[i]= state[i]<<i;
	}
	return sfState;
}
int * GameState::diagonalShiftR(int *state){
	int *sfState =(int *)malloc(ROWS*sizeof(int));
	for(int i=0; i<ROWS; i++){
		sfState[i] = state[i]<<(ROWS-i-1);
	}
	return sfState;
}
std::string GameState::intToRow(int r){
	return NULL;
}
std::string GameState::toString(){
	std::string s;
	s+="====gameState====\n";
	std::stringstream ss;
	ss <<evaluate()<<"\n";
	s+="game value = "+ss.str();
	//ss.clear();
	//for(int i=0; i<ROWS; i++){
	//	for(int j=0; j<cols; j++){
	//		ss<<gameState[i*cols+j]<<"\t";
	//	}
	//	ss<<"\n";
	//}
	//s+=ss.str();
	return s;
}