#pragma once
#include <QtWidgets>
#include <random>
#include <stdexcept>


#include "service.h"

class Gui : public QWidget {
private:
	Service& service;

	QHBoxLayout* mainLy = new QHBoxLayout;
	QTableWidget* tbl = new QTableWidget;

	QPushButton* btnAdd = new QPushButton{ "&Add" };
	QPushButton* btnExit = new QPushButton{ "&Exit" };

	QLineEdit* lineNume = new QLineEdit;
	QLineEdit* lineTara = new QLineEdit;

	QSlider* slider = new QSlider;
	QComboBox* cmbBox = new QComboBox{};
	QLineEdit* path = new QLineEdit;
	QPushButton* btnExport = new QPushButton{ "&Export" };

	void init()
	{
		mainLy->addWidget(tbl);

		auto actionLy = new QVBoxLayout;

		auto addLy = new QFormLayout;
		addLy->addWidget(btnAdd);
		addLy->addWidget(lineNume);
		addLy->addWidget(lineTara);

		auto exportLy = new QFormLayout;

		cmbBox->addItem(QString::fromStdString("Puncte"));
		cmbBox->addItem(QString::fromStdString("Rebounds"));
		cmbBox->addItem(QString::fromStdString("Assist"));
		slider->setRange(1, 100);

		exportLy->addWidget(slider);
		exportLy->addWidget(cmbBox);
		exportLy->addWidget(path);
		exportLy->addWidget(btnExport);

		actionLy->addLayout(addLy);
		actionLy->addLayout(exportLy);
		actionLy->addWidget(btnExit);
		mainLy->addLayout(actionLy);
		setLayout(mainLy);
	}

	void loadData(vector<Jucator> jucatori)
	{
		tbl->clear();
		tbl->setRowCount(service.len());
		tbl->setColumnCount(6);

		int count = 0;
		for (Jucator& j : jucatori)
		{
			bool isHim = service.westbrook(j);
			QTableWidgetItem* item = new QTableWidgetItem(QString::fromStdString(j.getNume()));
			if (isHim)
				item->setBackground(QBrush(Qt::green));
			tbl->setItem(count, 0, item);

			item = new QTableWidgetItem(QString::fromStdString(j.getTara())); if (isHim)
				item->setBackground(QBrush(Qt::green));
			tbl->setItem(count, 1, item);

			item = new QTableWidgetItem(QString::fromStdString(to_string(j.getMeciuri())));
			if (isHim)
				item->setBackground(QBrush(Qt::green));
			tbl->setItem(count, 2, item);

			item = new QTableWidgetItem(QString::fromStdString(to_string(j.getPuncte())));
			if (isHim)
				item->setBackground(QBrush(Qt::green));
			tbl->setItem(count, 3, item);

			item = new QTableWidgetItem(QString::fromStdString(to_string(j.getRebounds())));
			if (isHim)
				item->setBackground(QBrush(Qt::green));
			tbl->setItem(count, 4, item);

			item = new QTableWidgetItem(QString::fromStdString(to_string(j.getAssisturi())));
			if (isHim)
				item->setBackground(QBrush(Qt::green));
			tbl->setItem(count, 5, item);
			count++;
		}
	}

	void connect()
	{
		QObject::connect(btnAdd, &QPushButton::clicked, this, [&]() {
			try {
				mt19937 mt{ random_device{}() };
				uniform_int_distribution<> dist1(0, 100);
				int meciuri = dist1(mt);
				uniform_int_distribution<> dist2(10, 1000);
				int pcte = dist2(mt);
				uniform_int_distribution<> dist3(10, 500);
				int rebo = dist3(mt);
				uniform_int_distribution<> dist4(10, 500);
				int ass = dist4(mt);

				service.adauga(lineNume->text().toStdString(), lineTara->text().toStdString(), meciuri, pcte, rebo, ass);
				lineNume->clear();
				lineTara->clear();
				loadData(service.all());
			}
			catch (const invalid_argument& e)
			{
				QMessageBox::information(this, QString::fromStdString("Validare"), QString::fromStdString((string)e.what()));
			}
			});

		QObject::connect(btnExport, &QPushButton::clicked, this, [&]() {
			string path2 = path->text().toStdString();
			int proc = slider->value();
			string opt = cmbBox->currentText().toStdString();
			if(path2 == "")
				QMessageBox::information(this, QString::fromStdString("Path empty"), QString::fromStdString("Path Empty"));
			else
			{
				service.exportProcentage(proc, opt, path2);
				path->clear();
			}
			});

		QObject::connect(btnExit, &QPushButton::clicked, this, [&]() {
			close();
			});
	}
public:
	Gui(Service& s) : service(s)
	{
		init();
		loadData(s.all());
		connect();
	};
};