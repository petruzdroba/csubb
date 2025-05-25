#pragma once
#include <QtWidgets>
#include <iostream>
#include <string>

using namespace std;

class GUI : public QWidget
{
private:
	int deathScore = 0;

	QVBoxLayout* mainLayout = new QVBoxLayout;
	QPushButton* btnExit = new QPushButton{ "&exit" };

	QLabel* userSex = new QLabel{ "None selected" };
	QRadioButton* btnMale = new QRadioButton{ "male" };
	QRadioButton* btnFemale = new QRadioButton{ "female" };
	QRadioButton* btnNone = new QRadioButton{ "none" };

	QCheckBox* btnAlcohol = new QCheckBox{ "Alcohol" };
	QCheckBox* btnSmoke = new QCheckBox{ "Smoke" };
	QCheckBox* btnMedication = new QCheckBox{ "Medication" };

	QComboBox* comboBox = new QComboBox{};


	void initGui()
	{
		mainLayout->addWidget(userSex);

		auto btnGroupSex = new QButtonGroup{};
		btnGroupSex->addButton(btnFemale);
		btnGroupSex->addButton(btnMale);
		btnGroupSex->addButton(btnNone);
		btnNone->toggle();
		btnGroupSex->setExclusive(true);

		auto btnLySex = new QHBoxLayout;
		btnLySex->addWidget(btnFemale);
		btnLySex->addWidget(btnMale);
		btnLySex->addWidget(btnNone);

		mainLayout->addLayout(btnLySex);


		auto btnLyVices = new QHBoxLayout;
		btnLyVices->addWidget(btnAlcohol);
		btnLyVices->addWidget(btnSmoke);
		btnLyVices->addWidget(btnMedication);

		
		comboBox->addItem("123");
		comboBox->addItem("19847654");
		comboBox->addItem("2345768123");

		mainLayout->addWidget(comboBox);
		mainLayout->addLayout(btnLyVices);

		mainLayout->addWidget(btnExit);
		setLayout(mainLayout);
	}

	void initConnect()
	{
		QObject::connect(btnExit, &QPushButton::clicked, this, [=]() {
			close();
			});

		QObject::connect(btnMale, &QRadioButton::toggled, this, [=]() {
			userSex->setText("User is Male");
			});

		QObject::connect(btnFemale, &QRadioButton::toggled, this, [=]() {
			userSex->setText("User is Female");
			});

		QObject::connect(btnNone, &QRadioButton::toggled, this, [=]() {
			userSex->setText("None selected");
			});

		QObject::connect(btnAlcohol, &QCheckBox::toggled, this, [=](bool checked) {
			
			});
	}
public:
	GUI() {
		initGui();
		initConnect();
	}
};