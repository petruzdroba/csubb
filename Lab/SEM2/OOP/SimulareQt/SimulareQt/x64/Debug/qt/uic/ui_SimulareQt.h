/********************************************************************************
** Form generated from reading UI file 'SimulareQt.ui'
**
** Created by: Qt User Interface Compiler version 6.9.0
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_SIMULAREQT_H
#define UI_SIMULAREQT_H

#include <QtCore/QVariant>
#include <QtWidgets/QApplication>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QMenuBar>
#include <QtWidgets/QStatusBar>
#include <QtWidgets/QToolBar>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_SimulareQtClass
{
public:
    QMenuBar *menuBar;
    QToolBar *mainToolBar;
    QWidget *centralWidget;
    QStatusBar *statusBar;

    void setupUi(QMainWindow *SimulareQtClass)
    {
        if (SimulareQtClass->objectName().isEmpty())
            SimulareQtClass->setObjectName("SimulareQtClass");
        SimulareQtClass->resize(600, 400);
        menuBar = new QMenuBar(SimulareQtClass);
        menuBar->setObjectName("menuBar");
        SimulareQtClass->setMenuBar(menuBar);
        mainToolBar = new QToolBar(SimulareQtClass);
        mainToolBar->setObjectName("mainToolBar");
        SimulareQtClass->addToolBar(mainToolBar);
        centralWidget = new QWidget(SimulareQtClass);
        centralWidget->setObjectName("centralWidget");
        SimulareQtClass->setCentralWidget(centralWidget);
        statusBar = new QStatusBar(SimulareQtClass);
        statusBar->setObjectName("statusBar");
        SimulareQtClass->setStatusBar(statusBar);

        retranslateUi(SimulareQtClass);

        QMetaObject::connectSlotsByName(SimulareQtClass);
    } // setupUi

    void retranslateUi(QMainWindow *SimulareQtClass)
    {
        SimulareQtClass->setWindowTitle(QCoreApplication::translate("SimulareQtClass", "SimulareQt", nullptr));
    } // retranslateUi

};

namespace Ui {
    class SimulareQtClass: public Ui_SimulareQtClass {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_SIMULAREQT_H
