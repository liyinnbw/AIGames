#include "wuziqi.h"
#include "GameTree.h"
#include <QPainter>
#include <QDebug>
#include <QPushButton>
#include <QMessageBox>
#include <ctime>

int XO=50;
int YO=50;
int UNIT = 60;
int GRID_ROWS = 15;	//vertices, not squares
int GRID_COLS = 15;	//vertices, not squares
int SEARCH_TIME = 3; //seconds

Wuziqi::Wuziqi(QWidget *parent, Qt::WFlags flags)
	: QMainWindow(parent, flags)
{
	
	gameState = NULL;
	message = new QMessageBox();
	message ->setGeometry(XO+3*UNIT,YO+4*UNIT,2*UNIT,UNIT);
	message ->setText("Game Over");
	message ->addButton(tr("Restart"),QMessageBox::AcceptRole);
	if(!connect(message, SIGNAL(rejected()), this, SLOT(newGame()))){
		qDebug()<<"WARNING:Cannot connect signal slot!";
	}
	newGame();
	
}

Wuziqi::~Wuziqi()
{
	delete gameState;
}
void Wuziqi::newGame(){
	qDebug()<<"====new game====";
	if(gameState!=NULL){
		delete gameState;
	}
	gameState	= new GameState(GRID_ROWS,GRID_COLS, GameState::MAX_PLAYER);
	if(agent!=NULL){
		delete agent;
	}
	agent		= new GameTree (gameState, SEARCH_TIME);
	setFixedSize(XO*2+GRID_COLS*UNIT-UNIT,YO*2+GRID_ROWS*UNIT-UNIT);
	//qDebug()<<(gameState->toString()).c_str();
	qDebug()<<"game score ="<<gameState->evaluate();
}
void Wuziqi::agentMove(){
	if(gameState->getCurrSide()==GameState::MIN_PLAYER){
		clock_t start = clock();
		Point nextBestMove = agent->nextMove();
		clock_t end = clock();
		qDebug()<<"cleared outdated hash="<<agent->outdatedCount;
		qDebug()<<"Reuse saved nodes="<<agent->hmQuerySuccessfulCount<<"/"<<agent->hm.size()<<" max depth reached="<<agent->maxDepthReached<<" average branching="<<agent->branchesCount/agent->nodesVisitedCount;
		qDebug()<<"ai move=("<<nextBestMove.x<<","<<nextBestMove.y<<") move calculation time="<<(end-start)*1.0/CLOCKS_PER_SEC<<"s (sorting ="<<agent->totalSortingTime*1.0/CLOCKS_PER_SEC<<"s)";;
		gameState->addPiece(nextBestMove.x, nextBestMove.y);
	}
}
void Wuziqi::mousePressEvent (QMouseEvent * e){
	if(gameState->getCurrSide()==GameState::MAX_PLAYER){
		QPoint gridPos = posOnGrid(e->pos());
		qDebug()<<"mouse press on board"<<gridPos;
		gameState->addPiece(gridPos.x(),gridPos.y());
		update();
		
		if(gameState->isGameOver()!=-1){
			message ->show();
		}
		else{
			agentMove();
			if(gameState->isGameOver()!=-1){
				message ->show();
			}
		}
	}
}
QPoint Wuziqi::posOnGrid(QPoint p){
	QPoint pos;
	pos.setX(int((p.x()-XO+UNIT/2.0)/UNIT));
	pos.setY(int((p.y()-YO+UNIT/2.0)/UNIT));
	return pos;
}
QPoint Wuziqi::posAbsolute(QPoint p){
	QPoint pos;
	pos.setX(p.x()*UNIT+XO);
	pos.setY(p.y()*UNIT+YO);
	return pos;
}
void Wuziqi::paintEvent(QPaintEvent *e)
{
	Q_UNUSED(e);
	QPainter qp(this);
	drawGrid(&qp);
}
void Wuziqi::drawGrid(QPainter *qp)
{
	//draw grid
	QPen gridPen(Qt::black, 4, Qt::SolidLine);  
	qp->setPen(gridPen);
	for(int i=0; i<GRID_ROWS-1; i++){
		for(int j=0; j<GRID_COLS-1; j++){
			int x = XO+j*UNIT;
			int y = YO+i*UNIT;
			qp->drawRect(x,y,UNIT,UNIT);
		}
	}

	int *state = gameState->getGameState();
	int *colMask = (int*) malloc (GRID_COLS*sizeof(int));
	for(int i=0; i<GRID_COLS; i++){
		colMask[i]=1<<GRID_COLS-1-i;
	}

	//draw maxplayer pieces
	QBrush blackBrush(Qt::black, Qt::SolidPattern);  
	qp->setBrush(blackBrush);
	for(int i=0; i<GRID_ROWS; i++){
		int row = state[i];
		for(int j=0; j<GRID_COLS; j++){
			int bit = row & colMask[j];
			if(bit!=0){
				qp->drawEllipse(posAbsolute(QPoint(j,i)),int(0.4*UNIT),int(0.4*UNIT));
			}
		}
	}

	//draw minplayer pieces
	QBrush whiteBrush(Qt::white, Qt::SolidPattern);  
	qp->setBrush(whiteBrush);
	for(int i=0; i<GRID_ROWS; i++){
		int row = state[GRID_ROWS+i];
		for(int j=0; j<GRID_COLS; j++){
			int bit = row & colMask[j];
			if(bit!=0){
				qp->drawEllipse(posAbsolute(QPoint(j,i)),int(0.4*UNIT),int(0.4*UNIT));
			}
		}
	}

	//draw latest move
	if(!gameState->getMoves().empty()){
		QBrush redBrush(Qt::red, Qt::SolidPattern);  
		qp->setBrush(redBrush);
		int d = (int) (UNIT*0.1);
		Point lastMove = gameState->getMoves().back();
		QPoint ap = posAbsolute(QPoint(lastMove.x,lastMove.y));
		qp->drawEllipse(ap,d,d);
	}

	//qDebug()<<(gameState->toString()).c_str();
	
}