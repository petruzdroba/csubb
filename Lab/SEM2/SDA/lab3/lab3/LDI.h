#pragma once

#include <utility> // For std::pair
#include <stdexcept>


typedef int TElem;       
typedef int TPrioritate; 

typedef std::pair<TElem, TPrioritate> Element;
typedef bool (*Relatie)(TPrioritate p1, TPrioritate p2);


class Node {
public:
	Element data;
	Node* next;
	Node* prev;


	Node(Element value) : data(value), next(nullptr), prev(nullptr) {}
};

class LDI 
{
private:
	Node* head;
	Node* tail;
	int length;
	Relatie relatie;

public:
	LDI(Relatie r) : head(nullptr), tail(nullptr), length(0), relatie(r) {}

	void add(Element el)
	{
		Node* newNode = new Node(el);

		if (head == nullptr) {
			head = tail = newNode;
		}
		else {
			Node* current = head;

			while (current != nullptr && relatie(current->data.second, el.second)) {
				current = current->next;
			}

			if (current == nullptr) {//e cel mai neprioritar
				tail->next = newNode;
				newNode->prev = tail;
				tail = newNode;
			}
			else if (current == head) {// e cel mai prioritar
				newNode->next = head;
				head->prev = newNode;
				head = newNode;
			}
			else {
				newNode->prev = current->prev;
				newNode->next = current;
				current->prev->next = newNode;
				current->prev = newNode;
			}
		}

		length++;
	}//O(n)

	Node* begin()const {
		return head;
	}//O(1)

	void erase(Node* node)
	{
		if (node == nullptr)
			return;
		Node* current = node;

		if (current == head)
		{
			head = current->next;
			if(head != nullptr)
				head->prev = nullptr; //primul nu are prev
		}
		else if (current == tail)
		{
			tail = current->prev;
			if(tail != nullptr)
				tail->next = nullptr; // ultimul nu are next
		}
		else {
			current->prev->next = current->next;  //practic mij.prev['next'] = mij.next
			current->next->prev = current->prev;  //practic mij.next['prev'] = mij.prev
			//scoatem orice aparitie a lui curent din running
		}
		
		delete current;
		length--;
	}//O(n)

	int size() const
	{
		return this->length;
	}//O(1)

};