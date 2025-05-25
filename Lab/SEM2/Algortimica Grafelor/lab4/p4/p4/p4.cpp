#include <iostream>
#include <fstream>
#include <unordered_map>
#include <string>
#include <queue>
#include <vector>

using namespace std;

class Node
{
public:
	char ch;
	int frecv;
	Node* left;
	Node* right;

	Node(char c = 0, int f = 0) : ch(c), frecv(f), left(nullptr), right(nullptr) {};
};

bool nodeCompare(Node* a, Node* b)
{
	if (a->frecv == b->frecv)
		return a->ch > b->ch;
	return a->frecv > b->frecv;
}

priority_queue<Node*, vector<Node*>, bool(*)(Node* a, Node* b)> createTree(int n,unordered_map<char,int> alpha)
{
	priority_queue<Node*, vector<Node*>, bool(*)(Node* a, Node* b)> tree(nodeCompare);
	for (pair<char, int> pair : alpha)
	{
		Node* newNode = new Node();
		newNode->ch = pair.first;
		newNode->frecv = pair.second;
		tree.push(newNode);
	}

	for (int i = 1; i <= n - 1; ++i)
	{
		Node* z = new Node();
		Node* x = tree.top(); tree.pop();
		Node* y = tree.top(); tree.pop();

		z->left = x;
		z->right = y;
		z->frecv = x->frecv + y->frecv;
		tree.push(z);
	}

	return tree;
}

string decodeHuffman(int n, unordered_map<char, int> alpha, string message)
{
	priority_queue<Node*, vector<Node*>, bool(*)(Node* a, Node* b)> tree = createTree(n, alpha);

	Node* root = tree.top();

	string decoded = "";
	Node* curent = root;
	for (char c : message)
	{
		if (c == '0')
		{
			curent = curent->left;
		}
		else if (c == '1')
		{
			curent = curent->right;
		}
		
		if (curent->left == nullptr && curent->right == nullptr)//frunza
		{
			decoded += curent->ch;
			curent = root;
		}
	}

	return decoded;
}

int main(int argc, char** argv)
{
	ifstream in(argv[1]);
	ofstream out(argv[2]);

	int n;
	in >> n;

	unordered_map<char, int> alpha;
	for (int i = 0; i < n; ++i)
	{
		char ch; int ap;
		in >> ch >> ap;
		alpha[ch] = ap;
	}

	string coded;
	in.get();
	getline(in, coded);

	out << decodeHuffman(n,alpha,coded);
	in.close();
	out.close();
}