#include <iostream>
#include <fstream>
#include <string>
#include <unordered_map>
#include <queue>

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

bool nodeCompare(Node* a, Node* b) {
	if (a->frecv == b->frecv)
		return a->ch > b->ch;
	return a->frecv > b->frecv;
}

unordered_map<char, int> createSet(string text)
{
	unordered_map<char, int> alpha;
	for (char ch : text)
	{
		alpha[ch]++;
	}
	return alpha;
}

bool dfs(Node* node, char target,const string& path, string& outCode) {
	if (!node)
		return false;
	if (!node->left && !node->right) {
		if (node->ch == target) {
			outCode = path;
			return true;
		}
		return false;
	}
	if (dfs(node->left, target, path + "0", outCode)) 
		return true;
	if (dfs(node->right, target, path + "1", outCode)) 
		return true;
	return false;
}

string printCodes(Node* node, char c)
{
	string code;
	dfs(node, c,"", code);
	return code;
}

string Huffman(string text)
{
	unordered_map<char, int> C = createSet(text);
	int n = (int)C.size();
	priority_queue<Node*, vector<Node*>, bool(*)(Node*, Node*)> Q(nodeCompare);

	for (pair<char, int> pair : C)
	{
		Node* newNode = new Node();
		newNode->ch = pair.first;
		newNode->frecv = pair.second;
		Q.push(newNode);
	}

	for (int i = 1; i <= n - 1; ++i)
	{
		Node* x = Q.top();
		Q.pop();
		Node* y = Q.top();
		Q.pop();
		Node* z = new Node();
		z->frecv = x->frecv + y->frecv;
		z->left = x;
		z->right = y;
		Q.push(z);
	}

	Node* root = Q.top();
	string code = "";
	for (char c : text) {
		code += printCodes(root, c);
	}
	return code;
}

int main(int argc, char** argv)
{
	ifstream in(argv[1]);
	ofstream out(argv[2]);

	string message;
	getline(in, message);
	string code = Huffman(message);

	unordered_map<char, int> C = createSet(message);
	out << C.size() << endl;;
	for (pair<char, int> set : C)
	{
		out << set.first << " " << set.second << endl;
	}

	out << code << "\n";
}
