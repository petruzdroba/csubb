#pragma once
#include "DisciplinaGUI.h"
#include "ContractGUI.h"

#include <QtWidgets/QHBoxLayout>
#include <QtWidgets/QListWidget>
#include <QtWidgets/Qlabel>


class MainGUI :public QWidget
{
private:
	DisciplinaGui& dGui;
	ContractGUI& cGui;
	ContractReadOnlyGui& cRGui;
	QVBoxLayout* layoutMain = new QVBoxLayout{};
	QPushButton* btnExit = new QPushButton{ "&Exit" };
	QPushButton* btnCrud = new QPushButton{ "&CRUD Menu" };
	QPushButton* btnContract = new QPushButton{ "&Contract Menu" };
	QPushButton* btnReadOnly = new QPushButton{ "&Contract ReadOnly" };


	void initConnect()
	{
		QObject::connect(btnExit, &QPushButton::clicked, [&]() {
			dGui.close();
			cGui.close();
			cRGui.close();
			close();
			});

		QObject::connect(btnCrud, &QPushButton::clicked, [&]() {
			dGui.showNormal();
			});

		QObject::connect(btnContract, &QPushButton::clicked, [&]() {
			cGui.showNormal();
			});

		QObject::connect(btnReadOnly, &QPushButton::clicked, [&]() {
			cRGui.showNormal();
			});
	}

	void initGUI()
	{
		QLabel* lblTitle = new QLabel("Main Menu");
		layoutMain->addWidget(lblTitle);
		QHBoxLayout* layoutV = new QHBoxLayout{};
		layoutV->addWidget(btnCrud);
		layoutV->addWidget(btnContract);
		layoutV->addWidget(btnReadOnly);
		layoutV->addWidget(btnExit);
		layoutMain->addLayout(layoutV);
		setLayout(layoutMain);
	}

public:
	MainGUI(DisciplinaGui& d, ContractGUI& c, ContractReadOnlyGui& cR): dGui(d), cGui(c), cRGui(cR){
		initGUI();
		initConnect();
	}
};