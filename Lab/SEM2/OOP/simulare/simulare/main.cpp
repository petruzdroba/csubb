#include <QtWidgets/QApplication>
#include "teste.h"
#include "gui.h"
#include "Repo.h"
#include "service.h"


using namespace std;

int main(int argc, char *argv[])
{
    runAll();
    QApplication a(argc, argv);
    
    Repo r;
    Service s(r);

    Gui g(s);
    g.show();
    return a.exec();
}
