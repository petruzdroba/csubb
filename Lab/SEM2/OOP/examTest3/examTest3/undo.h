#pragma once

#include "repo.h"
#include "redo.h"

#include "Student.h"

class Undo {
public:
	virtual Redo* doUndo() = 0;
};

class UndoDelete: public Undo {
private:
	Repo& r;
	vector<Student> stud;
public:
	UndoDelete(Repo& r, vector<Student> s) : r(r), stud(s) {};

	Redo* doUndo() override {
		for (auto& el : stud)
			r.adauga(el);

		return new RedoDelete{ r, stud };
	}
};

class UndoAgeUp : public Undo {
private:
	Repo& r;
public:
	UndoAgeUp(Repo& r) : r(r) {};

	Redo*  doUndo() override {
		r.intinerire();

		return new RedoAgeUp{ r };
	}
};

class UndoAgeDown : public Undo {
private:
	Repo& r;
public:
	UndoAgeDown(Repo& r) : r(r) {};

	Redo* doUndo() override {
		r.imbatranire();

		return new RedoAgeDown{ r };
	}
};