#ifndef WUZIQI_H
#define WUZIQI_H
#include "GameState.h"
#include "State.h"
#include <QtGui/QMainWindow>
#include <QMouseEvent>
class QPushButton;
class QMessageBox;
class Wuziqi : public QMainWindow
{
	Q_OBJECT


public:
	Wuziqi(QWidget *parent = 0, Qt::WFlags flags = 0);
	~Wuziqi();
	void paintEvent(QPaintEvent *event);
	void drawGrid(QPainter *qp);
	void mousePressEvent (QMouseEvent * e);

public slots:
	void newGame();
private:
	QPoint posOnGrid(QPoint);
	QPoint posAbsolute(QPoint);
	void aiMove();
	GameState *gameState;
	QMessageBox *message;
};

#endif // WUZIQI_H
