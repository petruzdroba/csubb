#pragma once

#include <vector>

class Observer {
public:
	virtual void update() = 0;
};

class Observable {
private:
	std::vector<Observer*> observers;
public:
	void addObserver(Observer* o) {
		observers.push_back(o);
	}

protected:
	void notify()
	{
		for (auto o : observers)
			o->update();
	}
};10