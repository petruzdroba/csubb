/********************************************************************************
** Form generated from reading UI file 'examTest3.ui'
**
** Created by: Qt User Interface Compiler version 6.9.0
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_EXAMTEST3_H
#define UI_EXAMTEST3_H

#include <QtCore/QVariant>
#include <QtWidgets/QApplication>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QMenuBar>
#include <QtWidgets/QStatusBar>
#include <QtWidgets/QToolBar>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_examTest3Class
{
public:
    QMenuBar *menuBar;
    QToolBar *mainToolBar;
    QWidget *centralWidget;
    QStatusBar *statusBar;

    void setupUi(QMainWindow *examTest3Class)
    {
        if (examTest3Class->objectName().isEmpty())
            examTest3Class->setObjectName("examTest3Class");
        examTest3Class->resize(600, 400);
        menuBar = new QMenuBar(examTest3Class);
        menuBar->setObjectName("menuBar");
        examTest3Class->setMenuBar(menuBar);
        mainToolBar = new QToolBar(examTest3Class);
        mainToolBar->setObjectName("mainToolBar");
        examTest3Class->addToolBar(mainToolBar);
        centralWidget = new QWidget(examTest3Class);
        centralWidget->setObjectName("centralWidget");
        examTest3Class->setCentralWidget(centralWidget);
        statusBar = new QStatusBar(examTest3Class);
        statusBar->setObjectName("statusBar");
        examTest3Class->setStatusBar(statusBar);

        retranslateUi(examTest3Class);

        QMetaObject::connectSlotsByName(examTest3Class);
    } // setupUi

    void retranslateUi(QMainWindow *examTest3Class)
    {
        examTest3Class->setWindowTitle(QCoreApplication::translate("examTest3Class", "examTest3", nullptr));
    } // retranslateUi

};

namespace Ui {
    class examTest3Class: public Ui_examTest3Class {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_EXAMTEST3_H
