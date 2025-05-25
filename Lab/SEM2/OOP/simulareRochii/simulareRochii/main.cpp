#include <QtWidgets/QApplication>

#include "Gui.h"
#include "teste.h"
#include "repo.h"
#include "service.h"


int main(int argc, char *argv[])
{
    runAll();

    QApplication a(argc, argv);
    Repo r("rochii.txt");
    Service s(r);
    Gui g(s);
    g.show();

    return a.exec();
}
