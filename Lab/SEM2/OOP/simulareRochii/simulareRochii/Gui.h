#pragma once

#include <QtWidgets>
#include "service.h"

using namespace std;

class Gui : public QWidget
{
private:
	Service service;
	vector<Rochie> currentList;
	string sorted = "nesortat";

	QHBoxLayout* mainLayout = new QHBoxLayout;
	QListWidget* lst = new QListWidget{};

	QPushButton* btnExit = new QPushButton{ "&Exit" };

	QLabel* curInfo = new QLabel;
	QPushButton* btnInch = new QPushButton{ "&Inchiriere" };

	QPushButton* btnSortMarime = new QPushButton{ "&Sort Marime" };
	QPushButton* btnSortatPret = new QPushButton{ "&Sort Pret" };
	QPushButton* btnNesortat = new QPushButton{ "&Nesortat" };


	void initGui()
	{
		mainLayout->addWidget(lst);

		auto rightLayout = new QVBoxLayout{};

		auto formLy = new QHBoxLayout;
		formLy->addWidget(curInfo);
		formLy->addWidget(btnInch);

		auto sortLy = new QHBoxLayout;
		sortLy->addWidget(btnSortMarime);
		sortLy->addWidget(btnSortatPret);
		sortLy->addWidget(btnNesortat);

		rightLayout->addLayout(formLy);
		rightLayout->addLayout(sortLy);
		rightLayout->addWidget(btnExit);
		mainLayout->addLayout(rightLayout);
		setLayout(mainLayout);
	};

	void initConnect()
	{

		QObject::connect(lst, &QListWidget::currentRowChanged, this, [=](int index) {
			if (index >= 0 && index < service.lungime())
				curInfo->setText(QString::fromStdString(currentList[index].getDenumire()));
			else
				curInfo->setText(QString::fromStdString("None selected"));

			});

		QObject::connect(btnInch, &QPushButton::clicked, this, [&]() {
			int index = lst->currentRow();

			if (index < 0 || index >= service.lungime())
				return;

			bool response;
			if (sorted == "nesortat")
				response = service.inchiriaza(service.all()[index]);
			else if(sorted == "marime")
				response = service.inchiriaza(service.sortareMarime()[index]);
			else if (sorted == "pret")
				response = service.inchiriaza(service.sortarePret()[index]);
			else
				response = service.inchiriaza(service.all()[index]);

			if (!response)
			{
				QMessageBox::information(this, QString::fromStdString("Rochie deja ichiriata"), QString::fromStdString(service.all()[index].getDenumire() + " este deja inchiriata"));
			}

			if (sorted == "nesortat")
				loadData(service.all());
			else if (sorted == "marime")
				loadData(service.sortareMarime());
			else
				loadData(service.sortarePret());
			});

		QObject::connect(btnExit, &QPushButton::clicked, this, [&]() {
			close();
			});

		QObject::connect(btnSortMarime, &QPushButton::clicked, this, [&]() {
			sorted = "marime";
			loadData(service.sortareMarime());
			});

		QObject::connect(btnSortatPret, &QPushButton::clicked, this, [&]() {
			sorted = "pret";
			loadData(service.sortarePret());
			});

		QObject::connect(btnNesortat, &QPushButton::clicked, this, [&]() {
			sorted = "nesortat";
			loadData(service.all());
			});
	};

	void loadData(vector<Rochie> rochii)
	{
		lst->clear();
		for (const Rochie& r : rochii)
		{
			auto item = new QListWidgetItem(QString::fromStdString(r.getDenumire() + "	|" + r.getMarime() + "	|$" + to_string(r.getPret())));
			if (r.getDispoibilitate())
				item->setBackground(QBrush(Qt::green));
			else
				item->setBackground(QBrush(Qt::red));

			lst->addItem(item);
		}
		currentList = rochii;
	};

public:
	Gui(Service& s) : service(s)
	{
		initGui();
		loadData(service.all());
		initConnect();
	}
};