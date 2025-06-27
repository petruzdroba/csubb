/********************************************************************************
** Form generated from reading UI file 'oopProduseExam.ui'
**
** Created by: Qt User Interface Compiler version 6.9.0
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_OOPPRODUSEEXAM_H
#define UI_OOPPRODUSEEXAM_H

#include <QtCore/QVariant>
#include <QtWidgets/QApplication>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QMenuBar>
#include <QtWidgets/QStatusBar>
#include <QtWidgets/QToolBar>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_oopProduseExamClass
{
public:
    QMenuBar *menuBar;
    QToolBar *mainToolBar;
    QWidget *centralWidget;
    QStatusBar *statusBar;

    void setupUi(QMainWindow *oopProduseExamClass)
    {
        if (oopProduseExamClass->objectName().isEmpty())
            oopProduseExamClass->setObjectName("oopProduseExamClass");
        oopProduseExamClass->resize(600, 400);
        menuBar = new QMenuBar(oopProduseExamClass);
        menuBar->setObjectName("menuBar");
        oopProduseExamClass->setMenuBar(menuBar);
        mainToolBar = new QToolBar(oopProduseExamClass);
        mainToolBar->setObjectName("mainToolBar");
        oopProduseExamClass->addToolBar(mainToolBar);
        centralWidget = new QWidget(oopProduseExamClass);
        centralWidget->setObjectName("centralWidget");
        oopProduseExamClass->setCentralWidget(centralWidget);
        statusBar = new QStatusBar(oopProduseExamClass);
        statusBar->setObjectName("statusBar");
        oopProduseExamClass->setStatusBar(statusBar);

        retranslateUi(oopProduseExamClass);

        QMetaObject::connectSlotsByName(oopProduseExamClass);
    } // setupUi

    void retranslateUi(QMainWindow *oopProduseExamClass)
    {
        oopProduseExamClass->setWindowTitle(QCoreApplication::translate("oopProduseExamClass", "oopProduseExam", nullptr));
    } // retranslateUi

};

namespace Ui {
    class oopProduseExamClass: public Ui_oopProduseExamClass {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_OOPPRODUSEEXAM_H
