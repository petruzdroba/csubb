#include <QtWidgets/QApplication>
#include "teste.h"
#include "repo.h"
#include "service.h"
#include "gui.h"


using namespace std;

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);

    runTests();
    Repository repo{ "produse.txt" };
    Service service{ repo };
    Gui gui{ service };

    gui.show();

    return a.exec();
}
