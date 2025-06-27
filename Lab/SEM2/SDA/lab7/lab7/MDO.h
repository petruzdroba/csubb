#pragma once

#include <vector>

typedef int TCheie;
typedef int TValoare;

#include <utility>
typedef std::pair<TCheie, TValoare> TElem;

using namespace std;

class IteratorMDO;

typedef bool(*Relatie)(TCheie, TCheie);

typedef bool(*Conditie)(TValoare);

class Node
{
public:
	TCheie cheie;
	vector<TValoare> elems;
	int left;
	int right;
	int parinte;
	int nextFree;

	Node() : cheie(0), left(-1), right(-1), parinte(-1), nextFree(-1) {}
};

class MDO {
	friend class IteratorMDO;
    private:
	/* aici e reprezentarea */
		int prim, primLiber;
		Node* elemente;
		int cp;
		int len;
		Relatie comp;

		int aloca();
		int tree_min(int node)const ;
		int succesor(int index) const;
    public:

	// constructorul implicit al MultiDictionarului Ordonat
	MDO(Relatie r);

	// adauga o pereche (cheie, valoare) in MDO
	void adauga(TCheie c, TValoare v);

	//cauta o cheie si returneaza vectorul de valori asociate
	vector<TValoare> cauta(TCheie c) const;

	//sterge o cheie si o valoare 
	//returneaza adevarat daca s-a gasit cheia si valoarea de sters
	bool sterge(TCheie c, TValoare v);

	//returneaza numarul de perechi (cheie, valoare) din MDO 
	int dim() const;

	//verifica daca MultiDictionarul Ordonat e vid 
	bool vid() const;

	// se returneaza iterator pe MDO
	// iteratorul va returna perechile in ordine in raport cu relatia de ordine
	IteratorMDO iterator() const;

	// destructorul 	
	~MDO();

	void filtreaza(Conditie cond);
};
