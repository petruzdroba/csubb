/********************************************************************************
** Form generated from reading UI file 'exam.ui'
**
** Created by: Qt User Interface Compiler version 6.9.0
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_EXAM_H
#define UI_EXAM_H

#include <QtCore/QVariant>
#include <QtWidgets/QApplication>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QMenuBar>
#include <QtWidgets/QStatusBar>
#include <QtWidgets/QToolBar>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_examClass
{
public:
    QMenuBar *menuBar;
    QToolBar *mainToolBar;
    QWidget *centralWidget;
    QStatusBar *statusBar;

    void setupUi(QMainWindow *examClass)
    {
        if (examClass->objectName().isEmpty())
            examClass->setObjectName("examClass");
        examClass->resize(600, 400);
        menuBar = new QMenuBar(examClass);
        menuBar->setObjectName("menuBar");
        examClass->setMenuBar(menuBar);
        mainToolBar = new QToolBar(examClass);
        mainToolBar->setObjectName("mainToolBar");
        examClass->addToolBar(mainToolBar);
        centralWidget = new QWidget(examClass);
        centralWidget->setObjectName("centralWidget");
        examClass->setCentralWidget(centralWidget);
        statusBar = new QStatusBar(examClass);
        statusBar->setObjectName("statusBar");
        examClass->setStatusBar(statusBar);

        retranslateUi(examClass);

        QMetaObject::connectSlotsByName(examClass);
    } // setupUi

    void retranslateUi(QMainWindow *examClass)
    {
        examClass->setWindowTitle(QCoreApplication::translate("examClass", "exam", nullptr));
    } // retranslateUi

};

namespace Ui {
    class examClass: public Ui_examClass {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_EXAM_H
