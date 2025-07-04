#include "IteratorMDO.h"
#include "MDO.h"
#include <iostream>
#include <vector>

#include <exception>
using namespace std;

MDO::MDO(Relatie r) {
	this->R = r;
	this->size = 0;
	this->prim = nullptr;
}


void MDO::adauga(TCheie c, TValoare v) {
	Node* newNode = new Node;
	newNode->e = make_pair(c, v);
	newNode->next = nullptr;

	if (prim == nullptr)//no elements in the multi set
	{
		prim = newNode;
	}
	else {
		Node* curent = prim;
		Node* prev = nullptr;

		while (curent != nullptr && R(curent->e.first, c)) {//find its corresponding position based on key
			prev = curent;
			curent = curent->next;
		}

		if (prev == nullptr)
		{//inserted at the beginning
			newNode->next = prim;
			prim = newNode;
		}
		else {
			prev->next = newNode;
			newNode->next = curent;
		}
	}

	size++;
}

vector<TValoare> MDO::cauta(TCheie c) const {
	vector<TValoare> elemente;
	Node* curent = prim;

	while (curent != nullptr && R(curent->e.first, c)) {
		if (curent->e.first == c)
			elemente.push_back(curent->e.second);
		curent = curent->next;
	}

	return elemente;
}

bool MDO::sterge(TCheie c, TValoare v) {
	Node* curent = prim;
	Node* prev = nullptr;

	while (curent != nullptr && R(curent->e.first, c)) {
		if (curent->e.first == c && curent->e.second == v)
		{
			if (prev == nullptr)//elementul e primul
			{
				prim = prim->next;
			}
			else {
				prev->next = curent->next;
			}

			delete curent;
			size--;
			return true;
		}

		prev = curent;
		curent = curent->next;
	}

	return false;
}

int MDO::dim() const {
	return size;
}

bool MDO::vid() const {
	return size == 0;
}

IteratorMDO MDO::iterator() const {
	return IteratorMDO(*this);
}

MDO::~MDO() {
	Node* curent = prim;
	Node* prev = nullptr;

	while (curent != nullptr)
	{
		prev = curent;
		curent = curent->next;
		delete prev;
	}
}
