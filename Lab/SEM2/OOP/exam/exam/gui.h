#pragma once
#include <QtWidgets>
#include <service.h>
#include <guiParcare.h>
#include <ob.h>
#include <cstdlib>
#include <time.h>


class Gui : public QWidget, Observer {
private:
	Service& service;
	QHBoxLayout* mainLy = new QHBoxLayout;

	QTableWidget* tbl = new QTableWidget;
	QLineEdit* edAdresa = new QLineEdit;
	QLineEdit* edLinii = new QLineEdit;
	QLineEdit* edColoana = new QLineEdit;
	QLineEdit* edStare = new QLineEdit;

	QPushButton* btnSumbit = new QPushButton{ "&Sumbit" };
	QPushButton* btnModifica = new QPushButton{ "&Modifica" };

	QPushButton* btnCompleteStare = new QPushButton{ "&Stare Complete" };
	QPushButton* btnOpen = new QPushButton{ "&Open Disponibilitate" };


	void init() {
		mainLy->addWidget(tbl);

		auto formLy = new QFormLayout;
		auto formLbl = new QLabel;
		formLbl->setText("Adauga/Modifica");
		formLy->addWidget(formLbl);
		edAdresa->setPlaceholderText("Adresa");
		formLy->addWidget(edAdresa);
		edLinii->setPlaceholderText("Linii");
		formLy->addWidget(edLinii);
		edColoana->setPlaceholderText("Coloana");
		formLy->addWidget(edColoana);
		edStare->setPlaceholderText("Stare");
		formLy->addWidget(edStare);
		formLy->addWidget(btnSumbit);
		formLy->addWidget(btnModifica);
		formLy->addWidget(btnCompleteStare);


		mainLy->addLayout(formLy);
		mainLy->addWidget(btnOpen);


		setLayout(mainLy);
	}

	void loadData(vector<Parcare>& s) {
		tbl->setColumnCount(4);
		tbl->setRowCount((int)s.size());

		int count = 0;
		for (auto& a : s)
		{
			QTableWidgetItem* item = new QTableWidgetItem(QString::fromStdString(a.getAdresa()));
			tbl->setItem(count, 0, item);

			item = new QTableWidgetItem(QString::fromStdString(to_string(a.getLinii())));
			tbl->setItem(count, 1, item);

			item = new QTableWidgetItem(QString::fromStdString(to_string(a.getColoanre())));
			tbl->setItem(count, 2, item);

			item = new QTableWidgetItem(QString::fromStdString(a.getStare()));
			tbl->setItem(count, 3, item);

			count++;
		}
	}

	void connect() {
		QObject::connect(btnSumbit, &QPushButton::clicked, this, [=]() {
			string adresa = edAdresa->text().toStdString();
			int linii = edLinii->text().toInt();
			int coloane = edColoana->text().toInt();
			string stare = edStare->text().toStdString();

			try {
				service.adaugaS(adresa, linii, coloane, stare);
				loadData(service.getAllS());
				edAdresa->clear();
				edLinii->clear();
				edColoana->clear();
				edStare->clear();
			}
			catch (invalid_argument& e) {
				QMessageBox::warning(NULL, QString::fromStdString("OOPS"), QString::fromStdString(string(e.what())));
				edAdresa->clear();
				edLinii->clear();
				edColoana->clear();
				edStare->clear();
			}

			});

		QObject::connect(btnModifica, &QPushButton::clicked, this, [=]() {
			string adresa = edAdresa->text().toStdString();
			int linii = edLinii->text().toInt();
			int coloane = edColoana->text().toInt();
			string stare = edStare->text().toStdString();

			try {
				service.modificaS(adresa, linii, coloane, stare);
				loadData(service.getAllS());
				edAdresa->clear();
				edLinii->clear();
				edColoana->clear();
				edStare->clear();
			}
			catch (invalid_argument& e) {
				QMessageBox::warning(NULL, QString::fromStdString("OOPS"), QString::fromStdString(string(e.what())));
				edAdresa->clear();
				edLinii->clear();
				edColoana->clear();
				edStare->clear();
			}

			});

		QObject::connect(btnCompleteStare, &QPushButton::clicked, this, [=]() {
			if (edLinii && edColoana)
			{
				int linii = edLinii->text().toInt();
				int coloane = edColoana->text().toInt();
				int total = linii * coloane; //string size


				srand(time(NULL));
				string stare = "";

				while ((int)stare.size() < total)
				{
					int r = rand() % 10;
					if (r % 2)
						stare += 'X';
					else
						stare += '-';
				}

				edStare->clear();
				edStare->setText(QString::fromStdString(stare));
			}

			});

		QObject::connect(btnOpen, &QPushButton::clicked, this, [=]() {
			string adresa = edAdresa->text().toStdString();
			int linii = edLinii->text().toInt();
			int coloane = edColoana->text().toInt();
			string stare = edStare->text().toStdString();

			Parcare p("1",1,1,"X");

			for(auto& c: service.getAllS())
				if (adresa == c.getAdresa())
				{
					p = c;
					break;
				}

			guiParcare* g1 = new guiParcare{ p,service };
			guiParcare* g2 = new guiParcare{p,service};

			g1->show();
			g2->show();
			});

		QObject::connect(tbl, &QTableWidget::currentCellChanged, this, [=](int row, int column) {

			edAdresa->setText(tbl->item(row, 0)->text());
			edLinii->setText(tbl->item(row, 1)->text());
			edColoana->setText(tbl->item(row, 2)->text());
			edStare->setText(tbl->item(row, 3)->text());
			});
	}
public:
	Gui(Service& s) :service(s) {
		service.addObserver(this);
		init();
		loadData(s.getAllS());
		connect();
	}

	void update() override { loadData(service.getAllS()); }
};