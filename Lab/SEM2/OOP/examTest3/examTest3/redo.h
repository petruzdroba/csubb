#pragma once

#include "repo.h"
#include "Student.h"

class Redo {
public:
	virtual void doRedo() = 0;
};

class RedoDelete : public Redo {
private:
	Repo& r;
	vector<Student> s;
public:
	RedoDelete(Repo& r, vector<Student> s) : r(r), s(s) {};

	void doRedo() override {
		for (auto& el : s)
			r.stergeR(el.getNr());
	}
};

class RedoAgeUp : public Redo {
private:
	Repo& r;
public:
	RedoAgeUp(Repo& r) : r(r) {};

	void doRedo() override { r.imbatranire(); }
};

class RedoAgeDown : public Redo {
private:
	Repo& r;
public:
	RedoAgeDown(Repo& r) : r(r) {};

	void doRedo() override { r.intinerire(); }
};