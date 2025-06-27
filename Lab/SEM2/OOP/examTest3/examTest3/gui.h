#pragma once

#include <QtWidgets>
#include <service.h>

class Gui : public QWidget {
private:
	Service& service;
	QVBoxLayout* mainLy = new QVBoxLayout;
	QTableWidget* tbl = new QTableWidget;

	QVBoxLayout* ageLy = new QVBoxLayout;

	QPushButton* ageUp = new QPushButton{"&Imbatranire"};
	QPushButton* ageDown = new QPushButton{"&Intinerire"};

	QPushButton* btnDelete = new QPushButton{ "&Delete" };
	QPushButton* btnUndo = new QPushButton{ "&Undo" };
	QPushButton* btnRedo= new QPushButton{ "&Redo" };



	void init() {
		mainLy->addWidget(tbl);
	
		ageLy->addWidget(ageDown);
		ageLy->addWidget(ageUp);
		mainLy->addWidget(btnDelete);
		mainLy->addWidget(btnUndo);
		mainLy->addWidget(btnRedo);

		mainLy->addLayout(ageLy);
		setLayout(mainLy);
	}

	void loadData(vector<Student>& lista) {
		tbl->setColumnCount(4);
		tbl->setRowCount((int)lista.size());

		int count = 0;

		for (auto &el : lista)
		{
			auto culoare = Qt::red;
			if (el.getFacultate() == "mate")
				culoare = Qt::blue;
			else if (el.getFacultate() == "info")
				culoare = Qt::green;
			else if (el.getFacultate() == "ai")
				culoare = Qt::yellow;

			QTableWidgetItem* item = new QTableWidgetItem(QString::fromStdString(to_string(el.getNr())));
			item->setBackground(QBrush(culoare));
			tbl->setItem(count, 0, item);

			item = new QTableWidgetItem(QString::fromStdString(el.getNume()));
			item->setBackground(QBrush(culoare));
			tbl->setItem(count, 1, item);

			item = new QTableWidgetItem(QString::fromStdString(el.getFacultate()));
			item->setBackground(QBrush(culoare));
			tbl->setItem(count, 2, item);

			item = new QTableWidgetItem(QString::fromStdString(to_string(el.getVarsta())));
			item->setBackground(QBrush(culoare));
			tbl->setItem(count, 3, item);

			count++;
		}
	}

	void connect() {
		QObject::connect(ageUp, &QPushButton::clicked, this, [=]() {
			service.imbatranire();
			loadData(service.getAllS());
			});

		QObject::connect(ageDown, &QPushButton::clicked, this, [=]() {
			service.intinerire();
			loadData(service.getAllS());
			});

		QObject::connect(btnDelete, &QPushButton::clicked, this, [=]() {
			vector<int> ids;
			for (auto& item : tbl->selectedRanges())
			{
				for (int row = item.topRow(); row <= item.bottomRow(); ++row) {
					ids.push_back(tbl->item(row, 0)->text().toInt());
				}
			}

			service.sterge(ids);
			loadData(service.getAllS());
			
			});

		QObject::connect(btnUndo, &QPushButton::clicked, this, [=]() {
			service.undoThis();
			loadData(service.getAllS());
			});

		QObject::connect(btnRedo, &QPushButton::clicked, this, [=]() {
			service.redoThis();
			loadData(service.getAllS());
			});

	}

public:
	Gui(Service& s) : service(s) {
		init();
		loadData(s.getAllS());
		connect();
	}
};