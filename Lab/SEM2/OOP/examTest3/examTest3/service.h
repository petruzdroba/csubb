#pragma once
#include "repo.h"
#include "undo.h"
#include <algorithm>

#include <vector>

class Service {
private:
	Repo& repo;
	vector<Undo*> undo;
	vector<Redo* >redo;
public:
	Service(Repo& r) : repo(r) {};

	vector<Student>& getAllS() {
		auto& studs = repo.getAll();

		sort(studs.begin(), studs.end(), [](const Student& a, const Student& b) {return a.getVarsta() < b.getVarsta(); });

		return studs;
	}

	void intinerire() { repo.intinerire(); undo.push_back(new UndoAgeDown{ repo }); };
	void imbatranire() { repo.imbatranire(); undo.push_back(new UndoAgeUp{ repo }); };

	void sterge(vector<int> ids) {
		vector<Student> s;
		for (int id : ids)
		{
			s.push_back(repo.getStudentById(id));
			repo.stergeR(id);
		}

		undo.push_back(new UndoDelete{repo, s});
	}

	void undoThis() {
		if (!undo.empty())
		{
			redo.push_back(undo.back()->doUndo());
			undo.pop_back();
		}
	}

	void redoThis() {
		if (!redo.empty())
		{
			redo.back()->doRedo();
			redo.pop_back();
		}
	}

	~Service() {
		for (auto& u : undo)
			delete u;
		for (auto& r : redo)
			delete r;
	}
};