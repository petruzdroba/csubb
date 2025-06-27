#pragma once
#include <QtWidgets>
#include "Produs.h"
#include <service.h>


class GuiType : public QWidget, Observer {
private:
	string nume;
	int ap;
	Service& s;
	QVBoxLayout* mainLy = new QVBoxLayout;
	QLabel* label = new QLabel;

	void init() {
		label->setText(QString::fromStdString(to_string(ap)));
		mainLy->addWidget(label);
		setWindowTitle(QString::fromStdString(nume));
		setLayout(mainLy);
	};
	void loadData() {
		for(auto a: s.getTypes())
			if(nume == a.first)
				label->setText(QString::fromStdString(to_string(a.second)));

	};
public:
	GuiType(string n, int a, Service& s) : nume(n), ap(a), s(s) { init(); s.addObserver(this); };

	void update() override { loadData(); };
};