#include "validator.h"
#include "exception.h"
#include <stdexcept>
#include <string>

using namespace std;

void validatorDisciplina(const string& denumire, const int& ore, const string& tip, const string& profesor)
{
	string error;
	if (denumire.empty()) {
		error = "Validator: denumire nu poate fi un sir gol";
		throw MyException(error);
	}

	if (ore <= 0) {
		error = "Validator: valoare ore negativa";
		throw MyException(error);
	}

	if (tip.empty()) {
		error = "Validator: tip nu poate fi un sir gol";
		throw MyException(error);
	}

	if (profesor.empty()) {
		error = "Validator: profesor nu poate fi un sir gol";
		throw MyException(error);
	}
}
