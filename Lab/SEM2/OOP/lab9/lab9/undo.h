#pragma once
#include "repo.h"
#include "Disciplina.h"


class UndoAction
{
public:
	virtual void doUndo() = 0;
	virtual ~UndoAction() {};
};

class UndoAdauga : public UndoAction
{
private:
	RepositoryAbs &repo;
	Disciplina addedDisciplina;
public:
	UndoAdauga(RepositoryAbs& repo, const Disciplina& d) : repo{ repo }, addedDisciplina{ d } {};

	void doUndo() override
	{
		if (repo.getLungime())
		{
			for (int index = 0; index < repo.getLungime(); ++index)
			{
				if (repo.getAll()[index] == addedDisciplina)
				{
					repo.sterge(index);
					break;
				}
			}
		}
	}
};

class UndoSterge : public UndoAction
{
private:
	RepositoryAbs &repo;
	Disciplina deletedDisciplina;
public:
	UndoSterge(RepositoryAbs& repo, const Disciplina& d) : repo{ repo }, deletedDisciplina{ d } {};

	void doUndo() override
	{
		repo.adauga(deletedDisciplina);
	}
};

class UndoModifica : public UndoAction
{
private:
	RepositoryAbs &repo;
	Disciplina oldState;
	Disciplina newState;
public:
	UndoModifica(RepositoryAbs& repo, const Disciplina& o, const Disciplina& n) : repo{ repo }, newState{ n }, oldState{ o } {};

	void doUndo() override
	{
		if (repo.getLungime() > 0)
		{
			for (int index = 0; index < repo.getLungime(); ++index)
			{
				if (repo.getAll()[index] == newState)
				{
					repo.modifica(index, oldState);
					break;
				}
			}
		}
	}
};