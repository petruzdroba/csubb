#pragma once

#include <QtWidgets/qwidget.h>
#include <QtWidgets/QHBoxLayout>
#include <QtWidgets/QListWidget>
#include <QtWidgets/QFormLayout>
#include <QtWidgets/QLineEdit>
#include <QtWidgets/QPushButton>
#include <QtWidgets/QLabel>
#include <QtWidgets/QStackedWidget>
#include <QString>
#include <QtWidgets/QMessageBox>
#include <QtWidgets/QTableWidget>

#include <qdebug.h>

#include<vector>
#include <string>

#include "service.h"
#include "exception.h"

using namespace std;

class DisciplinaGui : public QWidget
{
private:
	Service& service;
	QHBoxLayout* layoutMain = new QHBoxLayout{};
	QHBoxLayout* layoutTypes = new QHBoxLayout{};
	QPushButton* btnType;

	QTableWidget* table = new QTableWidget{};

	QListWidget* lstSorted = new QListWidget;
	QListWidget* lstFiltered = new QListWidget;

	QPushButton* btnExit = new QPushButton{ "&Exit" };
	QPushButton* btnAdauga = new QPushButton{ "&Adauga" };
	QPushButton* btnSterge = new QPushButton{ "&Sterge" };
	QPushButton* btnModifica = new QPushButton{ "&Modifica" };
	QPushButton* btnUndo = new QPushButton{ "&Undo" };

	QPushButton* btnDenumire = new QPushButton{ "&Denumire" };
	QPushButton* btnOre = new QPushButton{ "&Ore" };
	QPushButton* btnTip = new QPushButton{ "&Tip" };
	QPushButton* btnProfesor = new QPushButton{ "&Profesor" };

	QPushButton* btnFilterOre = new QPushButton{ "&Filter Ore" };
	QPushButton* btnFilterNume = new QPushButton{ "&Filter Prof" };

	QLineEdit* txtDenumire = new QLineEdit;
	QLineEdit* txtOre = new QLineEdit;
	QLineEdit* txtTip = new QLineEdit;
	QLineEdit* txtProfesor = new QLineEdit;

	QLineEdit* filterNrOre = new QLineEdit;
	QLineEdit* filterNumeP = new QLineEdit;

	QStackedWidget* stackedWidget = new QStackedWidget;

	QWidget* mainPage = new QWidget;
	QWidget* secondPage = new QWidget;

	QPushButton* btnGoToSecondPage = new QPushButton{ "Go to Second Page" };
	QPushButton* btnBackToMain = new QPushButton{ "Back to Main Page" };


	void initConnect()
	{
		QObject::connect(btnExit, &QPushButton::clicked, [&]() {
			close();
			});

		QObject::connect(btnAdauga, &QPushButton::clicked, [&]() {
			auto denumire = txtDenumire->text();
			auto ore = txtOre->text();
			auto tip = txtTip->text();
			auto profesor = txtProfesor->text();

			bool okOre;
			int nrOre = ore.toInt(&okOre);
			if (!okOre) {
				qDebug() << "Numar ore invalid!";
				txtOre->clear();
			}
			else
			{
				try
				{
					service.adaugaService(denumire.toStdString(), stoi(ore.toStdString()), tip.toStdString(), profesor.toStdString());
				}
				catch (const MyException& e)
				{
					qDebug() << e.getMessage();
				}

				txtDenumire->clear();
				txtOre->clear();
				txtTip->clear();
				txtProfesor->clear();
			}

			loadData();
			});

		QObject::connect(btnSterge, &QPushButton::clicked, [&]() {
			int index =table->currentRow();
			if (index != -1)
			{
				try
				{
					service.stergeService(index);
				}
				catch (const MyException& e)
				{
					qDebug() << e.getMessage();
				}
				loadData();
			}
			});

		QObject::connect(btnModifica, &QPushButton::clicked, [&]() {
			int index = table->currentRow();
			if (index != -1)
			{
				auto denumire = txtDenumire->text();
				auto ore = txtOre->text();
				auto tip = txtTip->text();
				auto profesor = txtProfesor->text();

				bool okOre;
				int nrOre = ore.toInt(&okOre);
				if (!okOre) {
					qDebug() << "Numar ore invalid!";
					txtOre->clear();
				}

				try
				{
					service.modificaService(index, denumire.toStdString(), stoi(ore.toStdString()), tip.toStdString(), profesor.toStdString());
				}
				catch (const MyException& e)
				{
					qDebug() << e.getMessage();
				}
				txtDenumire->clear();
				txtOre->clear();
				txtTip->clear();
				txtProfesor->clear();
				loadData();
			}
			});

		QObject::connect(btnUndo, &QPushButton::clicked, [&]() {
			try
			{
				service.undo();
			}
			catch (const MyException& e)
			{
				qDebug() << e.getMessage();
			}
			loadData();
			});

		QObject::connect(btnGoToSecondPage, &QPushButton::clicked, [&]() {
			stackedWidget->setCurrentIndex(1);
			});

		QObject::connect(btnBackToMain, &QPushButton::clicked, [&]() {
			stackedWidget->setCurrentIndex(0);
			});

		QObject::connect(btnDenumire, &QPushButton::clicked, [&]() {
			loadSorted(service.sortareDenumire());
			});

		QObject::connect(btnOre, &QPushButton::clicked, [&]() {
			loadSorted(service.sortareOre());

			});

		QObject::connect(btnTip, &QPushButton::clicked, [&]() {
			loadSorted(service.sortareTip());

			});

		QObject::connect(btnProfesor, &QPushButton::clicked, [&]() {
			loadSorted(service.sortareProfesor());

			});

		QObject::connect(btnFilterOre, &QPushButton::clicked, [&]() {
			auto ore = filterNrOre->text();
			bool okOre;
			int nrOre = ore.toInt(&okOre);
			if (!okOre) {
				qDebug() << "Numar ore invalid!";
				txtOre->clear();
			}
			else {
				loadFiltered(service.filtrareOre(stoi(ore.toStdString())));
			}
			});

		QObject::connect(btnFilterNume, &QPushButton::clicked, [&]() {
			auto nume = filterNumeP->text();

			if (nume.toStdString() != "")
			{
				loadFiltered(service.filtrareProfesor(nume.toStdString()));
			}
			});

		QObject::connect(table, &QTableWidget::currentCellChanged, this, [=](int index) {
			if (index >= 0 && index < service.getAllService().size()) {
				table->clear();
				table->setRowCount(service.getLungimeService());
				table->setColumnCount(4);
				int count = 0;
				for (const Disciplina& d : service.getAllService())
				{
					QTableWidgetItem* item1 = new QTableWidgetItem(QString::fromStdString(d.getDenumire()));
					if (count == index)
						item1->setBackground(QBrush(Qt::gray));
					table->setItem(count, 0, item1);
					item1 = new QTableWidgetItem(QString::fromStdString(to_string(d.getOre())));
					if (count == index)
						item1->setBackground(QBrush(Qt::gray));
					table->setItem(count, 1, item1);

					item1 = new QTableWidgetItem(QString::fromStdString(d.getTip()));
					if (count == index)
						item1->setBackground(QBrush(Qt::gray));
					table->setItem(count, 2, item1);
					item1 = new QTableWidgetItem(QString::fromStdString(d.getProfesor()));
					if (count == index)
						item1->setBackground(QBrush(Qt::gray));
					table->setItem(count, 3, item1);
					count++;
				}

				const Disciplina& d = service.getAllService()[index];

				txtDenumire->setText(QString::fromStdString(d.getDenumire()));
				txtOre->setText(QString::number(d.getOre()));
				txtTip->setText(QString::fromStdString(d.getTip()));
				txtProfesor->setText(QString::fromStdString(d.getProfesor()));
			}
			else {
				txtDenumire->clear();
				txtOre->clear();
				txtTip->clear();
				txtProfesor->clear();
			}
			});

	}

	void clearLayout(QLayout* layout) {
		while (QLayoutItem* item = layout->takeAt(0)) {
			if (QWidget* widget = item->widget()) {
				widget->setParent(nullptr);
				widget->deleteLater();
			}
			if (QLayout* childLayout = item->layout()) {
				clearLayout(childLayout);
			}
			delete item;
		}
	}

	void loadTypeButtons()
	{
		clearLayout(layoutTypes);
		vector<pair<string, int>> types = service.getTypes();

		for (const pair<string, int>& pair : types)
		{
			btnType = new QPushButton{ QString::fromStdString("&" + pair.first) };
			connect(btnType, &QPushButton::clicked, this, [=]() {
				QMessageBox::information(this, QString::fromStdString(pair.first), "Exista " + QString::fromStdString(to_string(pair.second)));
				});
			layoutTypes->addWidget(btnType);
		}

		layoutMain->addLayout(layoutTypes);
	}

	void initGui()
	{
		mainPage->setLayout(layoutMain);

		auto layoutLeft = new QVBoxLayout;
		QLabel* lblHeader = new QLabel("Denumire | NrOre | Tip | Profesor");
		QFont font;
		font.setBold(true);
		lblHeader->setFont(font);
		layoutLeft->addWidget(lblHeader);
		//layoutLeft->addWidget(lst);
		layoutLeft->addWidget(table);

		layoutMain->addLayout(layoutLeft);

		auto leftLayout = new QVBoxLayout;
		auto formLayout = new QFormLayout;
		formLayout->addRow("Denumire", txtDenumire);
		formLayout->addRow("Numar Ore", txtOre);
		formLayout->addRow("Tip", txtTip);
		formLayout->addRow("Profesor", txtProfesor);
		leftLayout->addLayout(formLayout);

		auto layoutMainButtons = new QHBoxLayout{};
		layoutMainButtons->addWidget(btnAdauga);
		layoutMainButtons->addWidget(btnSterge);
		layoutMainButtons->addWidget(btnModifica);
		layoutMainButtons->addWidget(btnExit);

		auto layoutSecondaryButtons = new QHBoxLayout{};
		layoutSecondaryButtons->addWidget(btnUndo);
		layoutSecondaryButtons->addWidget(btnGoToSecondPage);

		leftLayout->addLayout(layoutMainButtons);
		leftLayout->addLayout(layoutSecondaryButtons);


		leftLayout->addLayout(layoutMainButtons);
		leftLayout->addLayout(layoutSecondaryButtons);

		layoutMain->addLayout(leftLayout);

		QHBoxLayout* secondLayout = new QHBoxLayout{ secondPage };

		auto layoutLeft2 = new QVBoxLayout;
		layoutLeft2->addWidget(lstSorted);

		auto layoutMainButtons2 = new QHBoxLayout{};
		layoutMainButtons2->addWidget(btnDenumire);
		layoutMainButtons2->addWidget(btnOre);
		layoutMainButtons2->addWidget(btnTip);
		layoutMainButtons2->addWidget(btnProfesor);

		layoutLeft2->addLayout(layoutMainButtons2);

		auto layoutRight = new QVBoxLayout;
		layoutRight->addWidget(lstFiltered);

		auto filterLayout = new QFormLayout;


		filterLayout->addRow("Numar de Ore", filterNrOre);
		filterLayout->addRow("Nume Profesor", filterNumeP);
		layoutRight->addLayout(filterLayout);

		auto layoutMainButtons3 = new QHBoxLayout{};
		layoutMainButtons3->addWidget(btnFilterOre);
		layoutMainButtons3->addWidget(btnFilterNume);
		layoutRight->addLayout(layoutMainButtons3);


		secondLayout->addLayout(layoutLeft2);
		secondLayout->addLayout(layoutRight);
		secondLayout->addWidget(btnBackToMain);

		stackedWidget->addWidget(mainPage);
		stackedWidget->addWidget(secondPage);

		QVBoxLayout* wrapperLayout = new QVBoxLayout;
		wrapperLayout->addWidget(stackedWidget);
		setLayout(wrapperLayout);
	}

	void loadData()
	{
		//lst->clear();
		table->clear();
		table->setRowCount(service.getLungimeService());
		table->setColumnCount(4);
		int count = 0;
		for (const Disciplina& d : service.getAllService())
		{
			/*QString itemText = QString::fromStdString(
				d.getDenumire() + " | " +
				std::to_string(d.getOre()) + " | " +
				d.getTip() + " | " +
				d.getProfesor()
			);
			lst->addItem(itemText);*/
			QTableWidgetItem* item1 = new QTableWidgetItem(QString::fromStdString(d.getDenumire()));
			table->setItem(count, 0, item1);
			item1 = new QTableWidgetItem(QString::fromStdString(to_string(d.getOre())));
			table->setItem(count, 1, item1);

			item1 = new QTableWidgetItem(QString::fromStdString(d.getTip()));
			table->setItem(count, 2, item1);
			item1 = new QTableWidgetItem(QString::fromStdString(d.getProfesor()));
			table->setItem(count, 3, item1);
			count++;

		}
		loadTypeButtons();

	}

	void loadSorted(const vector<Disciplina>& discipline)
	{
		lstSorted->clear();
		for (const Disciplina& d : discipline)
		{
			QString itemText = QString::fromStdString(
				d.getDenumire() + " | " +
				std::to_string(d.getOre()) + " | " +
				d.getTip() + " | " +
				d.getProfesor()
			);
			lstSorted->addItem(itemText);
		}
	}

	void loadFiltered(const vector<Disciplina>& discipline)
	{
		lstFiltered->clear();
		for (const Disciplina& d : discipline)
		{
			QString itemText = QString::fromStdString(
				d.getDenumire() + " | " +
				std::to_string(d.getOre()) + " | " +
				d.getTip() + " | " +
				d.getProfesor()
			);
			lstFiltered->addItem(itemText);
		}
	}
public:
	DisciplinaGui(Service& s) :service{ s } {
		initGui();
		loadData();
		initConnect();
	}
};