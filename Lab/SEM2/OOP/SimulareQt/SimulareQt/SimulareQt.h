#pragma once

#include <QtWidgets/QMainWindow>
#include "ui_SimulareQt.h"

class SimulareQt : public QMainWindow
{
    Q_OBJECT

public:
    SimulareQt(QWidget *parent = nullptr);
    ~SimulareQt();

private:
    Ui::SimulareQtClass ui;
};
