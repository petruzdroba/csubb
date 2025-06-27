#pragma once

#include <vector>

using namespace std;

class Observer {
public:
	virtual void update() = 0;
};

class Observator {
private:
	vector<Observer*> obs;
public:
	void addObserver(Observer* o) { obs.push_back(o); }

	/*~Observator() {
		for (auto& o : obs)
			delete o;
	}*/

protected:
	void notify() {
		for (auto& o : obs)
			o->update();
	}
};