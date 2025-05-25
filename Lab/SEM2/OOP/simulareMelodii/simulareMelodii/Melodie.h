#pragma once
#include <string>
using namespace std;

class Melodie {
private:
	int id;
	string titlu, artist;
	int rank;
public:


	Melodie(int id, const string& titlu, const string& artist, int rank)
		: id(id), titlu(titlu), artist(artist), rank(rank)
	{}

	int getId() {
		return id;
	}

	string getTitlu() {
		return titlu;
	}

	string getArtist() {
		return artist;
	}

	int getRank() {
		return rank;
	}

	const int getRank() const {
		return rank;
	}

	void setRank(int r)
	{
		rank = r;
	}

	void setName(string n)
	{
		titlu = n;
	}
};
