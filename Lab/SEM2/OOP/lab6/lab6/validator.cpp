#include "validator.h"
#include <stdexcept>

using namespace std;

void validatorDisciplina(const string& denumire, const int& ore, const string& tip, const string& profesor)
{
    if (denumire.empty()) {
        throw invalid_argument("Validator: denumire nu poate fi un sir gol");
    }

    if (ore <= 0) {
        throw invalid_argument("Validator: valoare ore negativa");
    }

    if (tip.empty()) {
        throw invalid_argument("Validator: tip nu poate fi un sir gol");
    }

    if (profesor.empty()) {
        throw invalid_argument("Validator: profesor nu poate fi un sir gol");
    }
}
