#pragma once

#include <QAbstractTableModel>
#include <service.h>

class TableModel : public QAbstractTableModel
{
private:
	vector<Disciplina> disciplinaList;
public:
	TableModel(QObject* parent, vector<Disciplina> d) : QAbstractTableModel(parent), disciplinaList(d) {};

    int rowCount(const QModelIndex& parent = QModelIndex()) const override {
        return (int)disciplinaList.size();
    }

    int columnCount(const QModelIndex& parent = QModelIndex()) const override {
        return 4;
    }

    QVariant data(const QModelIndex& index, int role = Qt::DisplayRole) const override {
        if (!index.isValid() || index.row() >= disciplinaList.size() || index.column() >= 4)
            return QVariant();

        const Disciplina& disciplina = disciplinaList.at(index.row());
        if (role == Qt::DisplayRole) {
            switch (index.column()) {
            case 0:
                return QString::fromStdString(disciplina.getDenumire());
            case 1:
                return disciplina.getOre();
            case 2:
                return QString::fromStdString(disciplina.getTip());
            case 3:
                return QString::fromStdString(disciplina.getProfesor());
            default: return QVariant();
            }
        }
        return QVariant();
    }
};