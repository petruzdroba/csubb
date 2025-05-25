#include "Repo.h"

vector<Jucator>& Repo::getAll()
{
	return jucatori;
}

int Repo::getLen()
{
	return (int)jucatori.size();
}

void Repo::adauga(const Jucator& j)
{
	jucatori.push_back(j);
}
