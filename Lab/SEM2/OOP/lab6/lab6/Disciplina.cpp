#include "Disciplina.h"


string Disciplina::getDenumire() const{
	return denumire;
}

int Disciplina::getOre() const {
	return ore;
}

string Disciplina::getTip()const {
	return tip;
}

string Disciplina::getProfesor() const{
	return profesor;
}

void Disciplina::setDenumire(const string& newDenumire)
{
	denumire = newDenumire;
}

void Disciplina::setOre(const int& newOre)
{
	ore = newOre;
}

void Disciplina::setTip(const string& newTip)
{
	tip = newTip;
}

void Disciplina::setProfesor(const string& newProfesor)
{
	profesor = newProfesor;
}
