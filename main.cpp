#include "wuziqi.h"
#include <QtGui/QApplication>

int main(int argc, char *argv[])
{
	QApplication a(argc, argv);
	Wuziqi w;
	w.show();
	return a.exec();
}
