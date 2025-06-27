#include <QtWidgets/QApplication>
#include "DisciplinaGUI.h"
#include "ContractGUI.h"
#include "MainGUI.h"
#include "teste.h"

#include "service.h"
#include "repo.h"
#include "contract.h"

int main(int argc, char* argv[])
{
	testAll();
	QApplication a(argc, argv);

	Repository repo("data.txt");
	Contract contract;

	Service service{ repo,contract };

	ContractGUI contractGui{ service };
	DisciplinaGui disciplinaGui{ service };
	ContractReadOnlyGui contractReadGui{ service };

	MainGUI mainGUI{disciplinaGui, contractGui, contractReadGui};
	mainGUI.show();


	return a.exec();
}
