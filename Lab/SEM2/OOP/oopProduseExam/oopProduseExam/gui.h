#pragma once
#include <QtWidgets>
#include "service.h"

using namespace std;

class Gui : public QWidget {
private:
	Service& service;
public:
	Gui(Service& s) : service(s) {

	}
};