#pragma once
#include <QtWidgets>
#include "service.h"
#include "Observator.h"

#include <guiType.h>

using namespace std;

class Gui : public QWidget, Observer {
private:
	Service& service;

	QHBoxLayout* mainLayout = new QHBoxLayout;
	QTableWidget* tbl = new QTableWidget;

	QPushButton* btnSumbit = new QPushButton{ "&Sumbit" };
	QLineEdit* id = new QLineEdit;
	QLineEdit* name = new QLineEdit;
	QLineEdit* tip = new QLineEdit;
	QLineEdit* pret = new QLineEdit;

	QSlider* slider = new QSlider;

	void init() {
		mainLayout->addWidget(tbl);

		auto formLayout = new QFormLayout;
		id->setPlaceholderText("id");
		formLayout->addWidget(id);
		name->setPlaceholderText("name");
		formLayout->addWidget(name);
		tip->setPlaceholderText("tip");
		formLayout->addWidget(tip);
		pret->setPlaceholderText("pret");
		formLayout->addWidget(pret);
		formLayout->addWidget(btnSumbit);

		mainLayout->addLayout(formLayout);

		slider->setRange(1, 100);
		slider->setValue(50);
		mainLayout->addWidget(slider);

		setLayout(mainLayout);
	}

	void loadData(vector<Produs>& produse) {
		tbl->setRowCount((int)produse.size());
		tbl->setColumnCount(5);

		int count = 0;

		for (auto p : produse) {
			bool red = false;
			if (p.getPret() < slider->value())
				red = true;


			QTableWidgetItem* item = new QTableWidgetItem(QString::fromStdString(to_string(p.getId())));
			if (red) { item->setBackground(QBrush(Qt::red)); }

			tbl->setItem(count, 0, item);

			item = new QTableWidgetItem(QString::fromStdString(p.getNume()));
			if (red) { item->setBackground(QBrush(Qt::red)); }


			tbl->setItem(count, 1, item);

			item = new QTableWidgetItem(QString::fromStdString(p.getTip()));
			if (red) { item->setBackground(QBrush(Qt::red)); }


			tbl->setItem(count, 2, item);

			item = new QTableWidgetItem(QString::fromStdString(to_string(p.getPret())));
			if (red) { item->setBackground(QBrush(Qt::red)); }


			tbl->setItem(count, 3, item);

			item = new QTableWidgetItem(QString::fromStdString(to_string(service.nrVocale(p))));
			if (red) { item->setBackground(QBrush(Qt::red)); }

			tbl->setItem(count, 4, item);

			count++;

		}
	}

	void connect() {
		QObject::connect(btnSumbit, &QPushButton::clicked, this, [=]() {
			int n = id->text().toInt();
			string nume = name->text().toStdString();
			string type = tip->text().toStdString();
			double price = pret->text().toDouble();
			try {
				service.add(n, nume, type, price);
				loadData(service.getAllS());

				id->clear();
				name->clear();
				tip->clear();
				pret->clear();
			}
			catch (invalid_argument& e) {
				QMessageBox::information(NULL,QString::fromStdString("Oops!"), QString::fromStdString(string(e.what())));
			}
			}
		);

		QObject::connect(slider, &QSlider::valueChanged, this, [=]() {loadData(service.getAllS());  });
	}


	void windowType() {
		auto all = service.getTypes();
		for (auto& tip : all)
		{
			GuiType *g = new GuiType(tip.first, tip.second, service);
			g->show();
		}
	}
public:
	Gui(Service& s) : service(s) {
		init();
		loadData(s.getAllS());
		connect();
		windowType();
		s.addObserver(this);
	}

	void update() override {
		loadData(service.getAllS());
	}
};