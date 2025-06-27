#include <QtWidgets/QApplication>
#include "teste.h"
#include "repo.h"
#include "service.h"
#include "gui.h"


int main(int argc, char *argv[])
{
    QApplication a(argc, argv);

    runTests();

    Repo r{ "studs.txt" };
    Service s{ r };
    Gui g{ s };

    g.show();

    return a.exec();
}
