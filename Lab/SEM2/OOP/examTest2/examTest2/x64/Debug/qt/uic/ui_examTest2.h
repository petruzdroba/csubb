/********************************************************************************
** Form generated from reading UI file 'examTest2.ui'
**
** Created by: Qt User Interface Compiler version 6.9.0
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_EXAMTEST2_H
#define UI_EXAMTEST2_H

#include <QtCore/QVariant>
#include <QtWidgets/QApplication>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QMenuBar>
#include <QtWidgets/QStatusBar>
#include <QtWidgets/QToolBar>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_examTest2Class
{
public:
    QMenuBar *menuBar;
    QToolBar *mainToolBar;
    QWidget *centralWidget;
    QStatusBar *statusBar;

    void setupUi(QMainWindow *examTest2Class)
    {
        if (examTest2Class->objectName().isEmpty())
            examTest2Class->setObjectName("examTest2Class");
        examTest2Class->resize(600, 400);
        menuBar = new QMenuBar(examTest2Class);
        menuBar->setObjectName("menuBar");
        examTest2Class->setMenuBar(menuBar);
        mainToolBar = new QToolBar(examTest2Class);
        mainToolBar->setObjectName("mainToolBar");
        examTest2Class->addToolBar(mainToolBar);
        centralWidget = new QWidget(examTest2Class);
        centralWidget->setObjectName("centralWidget");
        examTest2Class->setCentralWidget(centralWidget);
        statusBar = new QStatusBar(examTest2Class);
        statusBar->setObjectName("statusBar");
        examTest2Class->setStatusBar(statusBar);

        retranslateUi(examTest2Class);

        QMetaObject::connectSlotsByName(examTest2Class);
    } // setupUi

    void retranslateUi(QMainWindow *examTest2Class)
    {
        examTest2Class->setWindowTitle(QCoreApplication::translate("examTest2Class", "examTest2", nullptr));
    } // retranslateUi

};

namespace Ui {
    class examTest2Class: public Ui_examTest2Class {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_EXAMTEST2_H
