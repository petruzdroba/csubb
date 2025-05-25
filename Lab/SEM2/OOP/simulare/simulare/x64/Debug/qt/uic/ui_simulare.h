/********************************************************************************
** Form generated from reading UI file 'simulare.ui'
**
** Created by: Qt User Interface Compiler version 6.9.0
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_SIMULARE_H
#define UI_SIMULARE_H

#include <QtCore/QVariant>
#include <QtWidgets/QApplication>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QMenuBar>
#include <QtWidgets/QStatusBar>
#include <QtWidgets/QToolBar>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_simulareClass
{
public:
    QMenuBar *menuBar;
    QToolBar *mainToolBar;
    QWidget *centralWidget;
    QStatusBar *statusBar;

    void setupUi(QMainWindow *simulareClass)
    {
        if (simulareClass->objectName().isEmpty())
            simulareClass->setObjectName("simulareClass");
        simulareClass->resize(600, 400);
        menuBar = new QMenuBar(simulareClass);
        menuBar->setObjectName("menuBar");
        simulareClass->setMenuBar(menuBar);
        mainToolBar = new QToolBar(simulareClass);
        mainToolBar->setObjectName("mainToolBar");
        simulareClass->addToolBar(mainToolBar);
        centralWidget = new QWidget(simulareClass);
        centralWidget->setObjectName("centralWidget");
        simulareClass->setCentralWidget(centralWidget);
        statusBar = new QStatusBar(simulareClass);
        statusBar->setObjectName("statusBar");
        simulareClass->setStatusBar(statusBar);

        retranslateUi(simulareClass);

        QMetaObject::connectSlotsByName(simulareClass);
    } // setupUi

    void retranslateUi(QMainWindow *simulareClass)
    {
        simulareClass->setWindowTitle(QCoreApplication::translate("simulareClass", "simulare", nullptr));
    } // retranslateUi

};

namespace Ui {
    class simulareClass: public Ui_simulareClass {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_SIMULARE_H
