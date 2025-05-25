/********************************************************************************
** Form generated from reading UI file 'simulareMelodii.ui'
**
** Created by: Qt User Interface Compiler version 6.9.0
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_SIMULAREMELODII_H
#define UI_SIMULAREMELODII_H

#include <QtCore/QVariant>
#include <QtWidgets/QApplication>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QMenuBar>
#include <QtWidgets/QStatusBar>
#include <QtWidgets/QToolBar>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_simulareMelodiiClass
{
public:
    QMenuBar *menuBar;
    QToolBar *mainToolBar;
    QWidget *centralWidget;
    QStatusBar *statusBar;

    void setupUi(QMainWindow *simulareMelodiiClass)
    {
        if (simulareMelodiiClass->objectName().isEmpty())
            simulareMelodiiClass->setObjectName("simulareMelodiiClass");
        simulareMelodiiClass->resize(600, 400);
        menuBar = new QMenuBar(simulareMelodiiClass);
        menuBar->setObjectName("menuBar");
        simulareMelodiiClass->setMenuBar(menuBar);
        mainToolBar = new QToolBar(simulareMelodiiClass);
        mainToolBar->setObjectName("mainToolBar");
        simulareMelodiiClass->addToolBar(mainToolBar);
        centralWidget = new QWidget(simulareMelodiiClass);
        centralWidget->setObjectName("centralWidget");
        simulareMelodiiClass->setCentralWidget(centralWidget);
        statusBar = new QStatusBar(simulareMelodiiClass);
        statusBar->setObjectName("statusBar");
        simulareMelodiiClass->setStatusBar(statusBar);

        retranslateUi(simulareMelodiiClass);

        QMetaObject::connectSlotsByName(simulareMelodiiClass);
    } // setupUi

    void retranslateUi(QMainWindow *simulareMelodiiClass)
    {
        simulareMelodiiClass->setWindowTitle(QCoreApplication::translate("simulareMelodiiClass", "simulareMelodii", nullptr));
    } // retranslateUi

};

namespace Ui {
    class simulareMelodiiClass: public Ui_simulareMelodiiClass {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_SIMULAREMELODII_H
