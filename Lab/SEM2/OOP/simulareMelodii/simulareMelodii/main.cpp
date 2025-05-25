#include <QtWidgets/QApplication>
#include "repo.h"
#include "service.h"
#include "gui.h"


int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    Repo r("melodii.txt");
    Service s(r);
    Gui gui(s);
    gui.showMaximized();
    return a.exec();
}
