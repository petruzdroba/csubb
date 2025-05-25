/********************************************************************************
** Form generated from reading UI file 'simulareRochii.ui'
**
** Created by: Qt User Interface Compiler version 6.9.0
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_SIMULAREROCHII_H
#define UI_SIMULAREROCHII_H

#include <QtCore/QVariant>
#include <QtWidgets/QApplication>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QMenuBar>
#include <QtWidgets/QStatusBar>
#include <QtWidgets/QToolBar>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_simulareRochiiClass
{
public:
    QMenuBar *menuBar;
    QToolBar *mainToolBar;
    QWidget *centralWidget;
    QStatusBar *statusBar;

    void setupUi(QMainWindow *simulareRochiiClass)
    {
        if (simulareRochiiClass->objectName().isEmpty())
            simulareRochiiClass->setObjectName("simulareRochiiClass");
        simulareRochiiClass->resize(600, 400);
        menuBar = new QMenuBar(simulareRochiiClass);
        menuBar->setObjectName("menuBar");
        simulareRochiiClass->setMenuBar(menuBar);
        mainToolBar = new QToolBar(simulareRochiiClass);
        mainToolBar->setObjectName("mainToolBar");
        simulareRochiiClass->addToolBar(mainToolBar);
        centralWidget = new QWidget(simulareRochiiClass);
        centralWidget->setObjectName("centralWidget");
        simulareRochiiClass->setCentralWidget(centralWidget);
        statusBar = new QStatusBar(simulareRochiiClass);
        statusBar->setObjectName("statusBar");
        simulareRochiiClass->setStatusBar(statusBar);

        retranslateUi(simulareRochiiClass);

        QMetaObject::connectSlotsByName(simulareRochiiClass);
    } // setupUi

    void retranslateUi(QMainWindow *simulareRochiiClass)
    {
        simulareRochiiClass->setWindowTitle(QCoreApplication::translate("simulareRochiiClass", "simulareRochii", nullptr));
    } // retranslateUi

};

namespace Ui {
    class simulareRochiiClass: public Ui_simulareRochiiClass {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_SIMULAREROCHII_H
