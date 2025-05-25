#pragma once

#include <QtWidgets>
#include "service.h"
#include "Melodie.h"


class Gui : public QWidget
{
private:
	Service& service;

	QVBoxLayout* mainLy = new QVBoxLayout;
	QTableWidget* tbl = new QTableWidget;
	QTableWidget* rankTable = new QTableWidget;


	QLineEdit* editRank = new QLineEdit{};
	QLabel* showRank = new QLabel;
	QSlider* slider = new QSlider;
	QPushButton* btnRank = new QPushButton{ "&Rank!!" };
	QPushButton* btnDelete = new QPushButton{ "&Delete" };
	QPushButton* btnExit = new QPushButton{ "&Exit" };



	void init()
	{
		auto topLy = new QHBoxLayout;
		auto rankLy = new QHBoxLayout;
		slider->setRange(0, 10);
		showRank->setText(QString::number(slider->value()));
		auto rankLy2 = new QVBoxLayout;
		rankLy->addWidget(showRank);
		rankLy2->addWidget(editRank);

		auto btnLy = new QHBoxLayout;
		btnLy->addWidget(btnRank);
		btnLy->addWidget(btnDelete);
		btnLy->addWidget(btnExit);

		rankLy2->addLayout(btnLy);
		rankLy->addLayout(rankLy2);
		rankLy->addWidget(slider);


		topLy->addWidget(tbl);
		topLy->addLayout(rankLy);
		mainLy->addLayout(topLy);

		rankTable->setMaximumHeight(500);
		rankTable->setMaximumWidth(1100);

		mainLy->addWidget(rankTable);

		setLayout(mainLy);
	}

	void loadData(vector<Melodie> melodii)
	{
		tbl->clear();
		tbl->setHorizontalHeaderLabels({ "Id", "Titlu", "Artist", "Rank", "Same rank" });
		tbl->setRowCount((int)melodii.size());
		tbl->setColumnCount(5);

		int count = 0;
		for (Melodie& m : melodii)
		{
			QTableWidgetItem* item = new QTableWidgetItem(QString::fromStdString(to_string(m.getId())));
			tbl->setItem(count, 0, item);

			item = new QTableWidgetItem(QString::fromStdString(m.getTitlu()));
			tbl->setItem(count, 1, item);

			item = new QTableWidgetItem(QString::fromStdString(m.getArtist()));
			tbl->setItem(count, 2, item);

			item = new QTableWidgetItem(QString::fromStdString(to_string(m.getRank())));
			tbl->setItem(count, 3, item);

			item = new QTableWidgetItem(QString::fromStdString(to_string(service.getSameRank(m.getRank()))));
			tbl->setItem(count, 4, item);

			count++;
		}
		editRank->setText(QString::fromStdString("none"));

		rankTable->clear();
		rankTable->setColumnCount(11);
		rankTable->setRowCount(service.len());
		for (int i = 0; i <= 10; ++i)
		{
			int count = service.getSameRank(i);

			for (int j = 0; j < count; ++j)
			{
				QTableWidgetItem* item = new QTableWidgetItem(QString::fromStdString(to_string(i)));
				item->setBackground(QBrush(Qt::green));
				rankTable->setItem(service.len()- 1 - j,i, item);
			}
		}
		for (int i = 0; i < rankTable->columnCount(); ++i) {
			rankTable->resizeColumnToContents(i);
		}

		rankTable->scrollToBottom();

	}

	void connect()
	{
		QObject::connect(slider, &QSlider::valueChanged, this, [&](int value) {
			showRank->setText(QString::number(slider->value()));
			});

		QObject::connect(btnRank, &QPushButton::clicked, this, [&]() {
			int index = tbl->currentRow();
			if (index >= 0 && index < service.len())
			{
				int newRank = slider->value();
				string newName = editRank->text().toStdString();

				service.modifica(index, newRank, newName);
				loadData(service.all());
			}
			});

		QObject::connect(btnDelete, &QPushButton::clicked, this, [&]() {
			int index = tbl->currentRow();
			if (index >= 0 && index < service.len())
			{
				if (service.getByArtist(tbl->item(index, 2)->text().toStdString()) == 1)
				{
					QMessageBox::information(this, QString::fromStdString("OOPS!"), QString::fromStdString("Aceasta melodie este ultima si nu se poate sterge"));
				}
				else
				{
					service.sterge(index);
					loadData(service.all());
				}
			}
			});

		QObject::connect(btnExit, &QPushButton::clicked, this, [&]() {
			close();
			});

		QObject::connect(tbl, &QTableWidget::currentCellChanged, this, [&](int row, int column, int prow, int pcol) {
			if (row >= 0 && row <= service.len())
			{
				editRank->setText(tbl->item(row,1)->text());
				showRank->setText(tbl->item(row, 3)->text());
				slider->setValue(showRank->text().toInt());
			}
			});
	}

public:
	Gui(Service& s) : service(s)
	{
		init();
		loadData(s.all());
		connect();
	}
};