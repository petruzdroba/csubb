#pragma once

#include "Disciplina.h"
typedef Disciplina Element;

class Iterator;

template <typename Element>
class VectorDinamic {
	friend class Iterator;
private:
	int lungime;
	int capacitate;
	Element* elemente;

	void capacityRegulator() {
		if (lungime < capacitate)
			return;

		capacitate *= 2;

		Element* copyElemente = new Element[capacitate]; //default needed 

		for (int index = 0; index < lungime; index++)
		{
			copyElemente[index] = elemente[index];
		}

		delete[] elemente;

		elemente = copyElemente;
	}
public:
	VectorDinamic() {
		capacitate = 10;
		elemente = new Element[capacitate];
		lungime = 0;
	}

	~VectorDinamic() {
		if(elemente)
			delete[] elemente;
	}

	void push_back(const Element& disciplina) {
		capacityRegulator();

		elemente[lungime] = disciplina;
		lungime++;
	}

	void erase(const int pozitie) {
		if (pozitie < 0 || pozitie > lungime)
			return;

		for (int index = pozitie; index < lungime - 1; index++)
		{
			elemente[index] = elemente[index + 1];
		}
		lungime--;
	}

	void set(const int pozitie, const Element& disciplina)
	{
		elemente[pozitie] = disciplina;
	}

	int size() const noexcept {
		return this->lungime;
	}

	const VectorDinamic<Element>& getAll() const noexcept {
		return *this;  // Const version
	}


	VectorDinamic<Element>& getAll() noexcept {
		return *this;
	}

	// Copy constructor
	VectorDinamic(const VectorDinamic& other) {
		capacitate = other.capacitate;
		lungime = other.lungime;
		elemente = new Element[capacitate];
		for (int i = 0; i < lungime; ++i) {
			elemente[i] = other.elemente[i];
		}
	}

	// Copy assignment
	VectorDinamic& operator=(const VectorDinamic& other) {
		if (this == &other) return *this;

		delete[] elemente;

		capacitate = other.capacitate;
		lungime = other.lungime;
		elemente = new Element[capacitate];
		for (int i = 0; i < lungime; ++i) {
			elemente[i] = other.elemente[i];
		}

		return *this;
	}

	Element* begin() noexcept { return elemente; }
	Element* end() noexcept { return elemente + lungime; }

	const Element* begin() const noexcept { return elemente; }
	const Element* end() const noexcept { return elemente + lungime; }


	// Non-const version of operator[]
	Element& operator[](int index) {
		return elemente[index];
	}

	// Const version of operator[]
	const Element& operator[](int index) const noexcept {
		return elemente[index];
	}

	int getLungime()
	{
		return this->lungime;
	}
};

class Iterator
{
private:
	const VectorDinamic<Element>& v;
	int pozitie = 0;
public:
	explicit Iterator(const VectorDinamic<Element>& v) noexcept : v{ v } {};

	Iterator(const VectorDinamic<Element>& v, int pozitie)noexcept : v{ v }, pozitie{ pozitie } {};

	bool valid()const noexcept {

		return pozitie < v.lungime;
	};

	Element& element() const noexcept {

		return v.elemente[pozitie];
	};

	void next() noexcept {

		pozitie++;
	};

	Element& operator*() const noexcept {

		return element();
	};

	Iterator& operator++() noexcept {

		next();

		return *this;
	};


};