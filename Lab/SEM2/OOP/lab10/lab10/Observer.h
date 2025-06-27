#pragma once

#include <vector>

using namespace std;

class Observer
{
public:
	virtual void updateData() = 0;
};

class Observerable
{
private:
	vector<Observer*> observers;
public:
	void addObservable(Observer* o)
	{
		observers.push_back(o);
	}

	void removeObservable(Observer* o)
	{
		observers.erase(remove(begin(observers), end(observers), o), observers.end());
	}

protected:

	void notify()
	{
		for (auto o : observers)
			o->updateData();
	}
};