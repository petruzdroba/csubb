#pragma once
#include <QtWidgets/QHBoxLayout>
#include <QtWidgets/QListWidget>
#include <QtWidgets/QFormLayout>
#include <QtWidgets/QLineEdit>

#include "service.h"

class ContractGUI : public QWidget
{
private:
	Service& service;
	QHBoxLayout* layoutMain = new QHBoxLayout{};
	QListWidget* lst = new QListWidget;
	QListWidget* lstContracte = new QListWidget;

	QLineEdit* txtDenumire = new QLineEdit;
	QLineEdit* txtExport = new QLineEdit;
	QLineEdit* txtRandom = new QLineEdit;

	QPushButton* btnAdd = new QPushButton{"&Add"};
	QPushButton* btnDelete = new QPushButton{ "&Delete all" };
	QPushButton* btnRandom = new QPushButton{ "&Random populate" };
	QPushButton* btnExport = new QPushButton{ "&Export" };
	QPushButton* btnExit = new QPushButton{ "&Exit" };

	void initGui()
	{
		layoutMain->addWidget(lst);
		QVBoxLayout* middleLayout = new QVBoxLayout{};

		QHBoxLayout* addLayout = new QHBoxLayout{};
		addLayout->addWidget(txtDenumire);
		addLayout->addWidget(btnAdd);
		middleLayout->addLayout(addLayout);

		QHBoxLayout* randomLayout = new QHBoxLayout{};
		randomLayout->addWidget(txtRandom);
		randomLayout->addWidget(btnRandom);
		middleLayout->addLayout(randomLayout);

		QHBoxLayout* exportLayout = new QHBoxLayout{};
		exportLayout->addWidget(txtExport);
		exportLayout->addWidget(btnExport);
		middleLayout->addLayout(exportLayout);

		QHBoxLayout* extraBtns = new QHBoxLayout{};
		extraBtns->addWidget(btnDelete);
		extraBtns->addWidget(btnExit);
		middleLayout->addLayout(extraBtns);

		layoutMain->addLayout(middleLayout);
		layoutMain->addWidget(lstContracte);

		setLayout(layoutMain);
	}

	void initConnect()
	{
		QObject::connect(lst, &QListWidget::currentRowChanged, this, [=](int index) {
			if (index >= 0 && index < service.getAllService().size()) {
				const Disciplina& d = service.getAllService()[index];

				txtDenumire->setText(QString::fromStdString(d.getDenumire()));
			}
			else {
				txtDenumire->clear();
			}
			});

		QObject::connect(btnExit, &QPushButton::clicked, [&]() {
			close();
			});

		QObject::connect(btnAdd, &QPushButton::clicked, [&]() {
			auto denumire = txtDenumire->text();
			if (denumire.toStdString() != "")
			{
				service.adaugaContractService(denumire.toStdString());
				loadContract();
			}
			});

		QObject::connect(btnDelete, &QPushButton::clicked, [&]() {
			service.deleteAllContract();
			loadContract();
			});

		QObject::connect(btnRandom, &QPushButton::clicked, [&]() {
			auto nr = txtRandom->text();
			bool okR;
			int nrR = nr.toInt(&okR);
			if (!okR) {
				qDebug() << "Numar ore invalid!";
				txtRandom->clear();
			}
			else
			{
				service.randomPopulateContract(stoi(nr.toStdString()));
				loadContract();
			}

			QObject::connect(btnExport, &QPushButton::clicked, [&]() {
				auto path = txtExport->text();
				if (path.toStdString() != "")
				{
					service.exportContract(path.toStdString());
					txtExport->clear();
				}
				});

			});
	}

	void loadData()
	{
		lst->clear();
		for (const Disciplina& d : service.getAllService())
		{
			QString itemText = QString::fromStdString(
				d.getDenumire() + " | " +
				std::to_string(d.getOre()) + " | " +
				d.getTip() + " | " +
				d.getProfesor()
			);
			lst->addItem(itemText);
		}

		lst->setCurrentRow(-1);
	}

	void loadContract()
	{
		lstContracte->clear();
		for (const Disciplina& d : service.getAllContract())
		{
			QString itemText = QString::fromStdString(
				d.getDenumire() + " | " +
				std::to_string(d.getOre()) + " | " +
				d.getTip() + " | " +
				d.getProfesor()
			);
			lstContracte->addItem(itemText);
		}

		lstContracte->setCurrentRow(-1);
	}

public:
	ContractGUI(Service& s) : service(s)
	{
		initGui();
		loadData();
		initConnect();
	}
};