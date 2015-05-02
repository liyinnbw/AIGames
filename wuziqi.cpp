#include "wuziqi.h"
#include "GameTree.h"
#include <QPainter>
#include <QDebug>
#include <QPushButton>
#include <QMessageBox>

int XO=50;
int YO=50;
int UNIT = 60;
int GRID_ROWS = 6;	//vertices, not squares
int GRID_COLS = 6;	//vertices, not squares

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
	gameState = new GameState(GRID_ROWS,GRID_COLS, GameState::BLACK_SIDE);
	setFixedSize(XO*2+GRID_COLS*UNIT-UNIT,YO*2+GRID_ROWS*UNIT-UNIT);
	qDebug()<<(gameState->toString()).c_str();
	qDebug()<<"game score ="<<gameState->evaluate();
}
void Wuziqi::aiMove(){
	qDebug()<<"====ai move====";
	if(gameState->getCurrSide()==GameState::WHITE_SIDE){
		//qDebug()<<(gameState->toString()).c_str();
		GameTree gt(3, gameState, gameState->getCurrSide());
		//qDebug()<<(gt.getRootState()->toString()).c_str();

		GameState nextBest = gt.next();
		gameState->setGameState(nextBest.getGameState());
		qDebug()<<"game score ="<<gameState->evaluate();
		qDebug()<<(gameState->toString()).c_str();
		update();
		
		if(gameState->isGameOver()){
			message ->show();
		}else{
			gameState->setSide(gameState->getCurrSide()*-1);
		}
	}
}
void Wuziqi::mousePressEvent (QMouseEvent * e){
	if(gameState->getCurrSide()==GameState::BLACK_SIDE){
		QPoint gridPos = posOnGrid(e->pos());
		qDebug()<<"mouse press on board"<<gridPos;
		gameState->addPiece(gridPos.x(),gridPos.y());
		qDebug()<<"game score ="<<gameState->evaluate();
		qDebug()<<(gameState->toString()).c_str();
		update();
		
		if(gameState->isGameOver()){
			message ->show();
		}else{
			aiMove();
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

	std::vector<myPiece> pieces = gameState->getPieces();
	for(int i=0; i<pieces.size(); i++){
		myPiece p = pieces.at(i);
		if(p.side== GameState::WHITE_SIDE){
			QBrush whiteBrush(Qt::white, Qt::SolidPattern);  
			qp->setBrush(whiteBrush);
		}else{
			QBrush blackBrush(Qt::black, Qt::SolidPattern);  
			qp->setBrush(blackBrush);
		}
		qp->drawEllipse(posAbsolute(QPoint(p.x,p.y)),int(0.4*UNIT),int(0.4*UNIT));
	}
	
}