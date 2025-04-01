#include "validator.h"

bool validatorDisciplina(const string& denumire, const int& ore, const string& tip, const string& profesor)
{
	return (denumire != "" && ore > 0 && tip != "" && profesor != "");
}
