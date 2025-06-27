#pragma once
#include <QtWidgets>
#include <service.h>
#include <Parcare.h>
#include <ob.h>



class guiParcare : public QWidget, Observer{
private:
	Parcare& p;
	Service& s;

	QGridLayout* mainLy = new QGridLayout;
	QGridLayout* grid = new QGridLayout;


	void init() {
		mainLy->addLayout(grid,0,0);
		setLayout(mainLy);
	}

	void loadData(string stare) {
		int i = 0, j = 0;
		for (char c : stare)
		{
			QPushButton* button;
			if (c == 'X')
				button = new QPushButton{ "&X" };
			else
				button = new QPushButton{ "&-" };



			if (j == p.getColoanre())
			{
				j = 0;
				i++;
			}

			//QObject::connect(button, &QPushButton::clicked, this, [=]() {

			//	//int n = p.getLinii(), m = p.getColoanre();
			//	int ii = 0, jj = 0;
			//	char fuck[100][100];

			//	string temp2 = p.getStare();
			//	for (char c : temp2)
			//	{
			//		fuck[ii][jj] = c;
			//		if (jj = p.getColoanre())
			//		{
			//			jj = 0;
			//			ii++;
			//		}
			//		jj++;
			//	}

			//	fuck[i][j] = fuck[i][j] == 'X' ? '-' : 'X';
			//	string newS = "";
			//	for (ii = 0; ii < p.getLinii(); ii++)
			//	{
			//		for (jj = 0; jj < p.getColoanre(); jj++)
			//			newS += fuck[ii][jj];
			//	}

			//	QMessageBox::warning(NULL, QString::fromStdString(newS), QString::fromStdString(to_string(j)));
			//	});
				grid->addWidget(button, i, j);

			j++;

		}
	}

public:


	guiParcare(Parcare& p, Service& s)
		: p(p), s(s)
	{
		s.addObserver(this);
		init();
		loadData(p.getStare());
	}

	void update() override { loadData(p.getStare()); }
};
