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
	QVBoxLayout* layoutMain = new QVBoxLayout{};
	QPushButton* btnExit = new QPushButton{ "&Exit" };
	QPushButton* btnCrud = new QPushButton{ "&CRUD Menu" };
	QPushButton* btnContract = new QPushButton{ "&Contract Menu" };



	void initConnect()
	{
		QObject::connect(btnExit, &QPushButton::clicked, [&]() {
			dGui.close();
			cGui.close();
			close();
			});

		QObject::connect(btnCrud, &QPushButton::clicked, [&]() {
			dGui.showFullScreen();
			});

		QObject::connect(btnContract, &QPushButton::clicked, [&]() {
			cGui.showFullScreen();
			});
	}

	void initGUI()
	{
		QLabel* lblTitle = new QLabel("Main Menu");
		layoutMain->addWidget(lblTitle);
		QHBoxLayout* layoutV = new QHBoxLayout{};
		layoutV->addWidget(btnCrud);
		layoutV->addWidget(btnContract);
		layoutV->addWidget(btnExit);
		layoutMain->addLayout(layoutV);
		setLayout(layoutMain);
	}

public:
	MainGUI(DisciplinaGui& d, ContractGUI& c): dGui(d), cGui(c){
		initGUI();
		initConnect();
	}
};