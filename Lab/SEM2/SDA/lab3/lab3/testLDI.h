#pragma once

#include <iostream>
#include <cassert>
#include "LDI.h" // Include your LDI header with the Node and LDI classes

bool rel3(TPrioritate p1, TPrioritate p2) {
    if (p1 <= p2) {
        return true;
    }
    else {
        return false;
    }
}

void testAddAndSize() {
    LDI list{rel3};
    assert(list.size() == 0);

    list.add({ 10, 1 });
    list.add({ 20, 2 });
    list.add({ 30, 3 });

    assert(list.size() == 3);
}

void testEraseHead() {
    LDI list{rel3};
    list.add({ 10, 1 });
    list.add({ 20, 2 });
    list.add({ 30, 3 });

    Node* head = list.begin();
    list.erase(head);

    assert(list.size() == 2);
    assert(list.begin()->data.first == 20);
}

void testEraseTail() {
    LDI list{rel3};
    list.add({ 10, 1 });
    list.add({ 20, 2 });
    list.add({ 30, 3 });

    // Navigate to the tail
    Node* tail = list.begin();
    while (tail->next != nullptr)
        tail = tail->next;

    list.erase(tail);

    assert(list.size() == 2);
    Node* last = list.begin();
    while (last->next != nullptr)
        last = last->next;
    assert(last->data.first == 20);
}

void testEraseMiddle() {
    LDI list{rel3};
    list.add({ 10, 1 });
    list.add({ 20, 2 });
    list.add({ 30, 3 });

    Node* mid = list.begin()->next; // Second element (20, 2)
    list.erase(mid);

    assert(list.size() == 2);
    assert(list.begin()->next->data.first == 30);
}

void testEraseNull() {
    LDI list{rel3};
    list.erase(nullptr);  // Should not crash
    assert(list.size() == 0);
}

void testLDI() {
    testAddAndSize();
    testEraseHead();
    testEraseTail();
    testEraseMiddle();
    testEraseNull();

}
