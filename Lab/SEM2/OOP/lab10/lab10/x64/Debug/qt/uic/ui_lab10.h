/********************************************************************************
** Form generated from reading UI file 'lab10.ui'
**
** Created by: Qt User Interface Compiler version 6.9.0
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_LAB10_H
#define UI_LAB10_H

#include <QtCore/QVariant>
#include <QtWidgets/QApplication>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QMenuBar>
#include <QtWidgets/QStatusBar>
#include <QtWidgets/QToolBar>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_lab10Class
{
public:
    QMenuBar *menuBar;
    QToolBar *mainToolBar;
    QWidget *centralWidget;
    QStatusBar *statusBar;

    void setupUi(QMainWindow *lab10Class)
    {
        if (lab10Class->objectName().isEmpty())
            lab10Class->setObjectName("lab10Class");
        lab10Class->resize(600, 400);
        menuBar = new QMenuBar(lab10Class);
        menuBar->setObjectName("menuBar");
        lab10Class->setMenuBar(menuBar);
        mainToolBar = new QToolBar(lab10Class);
        mainToolBar->setObjectName("mainToolBar");
        lab10Class->addToolBar(mainToolBar);
        centralWidget = new QWidget(lab10Class);
        centralWidget->setObjectName("centralWidget");
        lab10Class->setCentralWidget(centralWidget);
        statusBar = new QStatusBar(lab10Class);
        statusBar->setObjectName("statusBar");
        lab10Class->setStatusBar(statusBar);

        retranslateUi(lab10Class);

        QMetaObject::connectSlotsByName(lab10Class);
    } // setupUi

    void retranslateUi(QMainWindow *lab10Class)
    {
        lab10Class->setWindowTitle(QCoreApplication::translate("lab10Class", "lab10", nullptr));
    } // retranslateUi

};

namespace Ui {
    class lab10Class: public Ui_lab10Class {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_LAB10_H
