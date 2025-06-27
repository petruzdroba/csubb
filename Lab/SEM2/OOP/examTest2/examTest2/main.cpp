#include <QtWidgets/QApplication>
#include "teste.h"
#include "service.h"
#include "repo.h"
#include "gui.h"


int main(int argc, char *argv[])
{
    QApplication a(argc, argv);

    runTests();

    Repository r{ "produse.txt" };
    Service s{ r };
    Gui g{ s };

    g.show();

    return a.exec();
}
