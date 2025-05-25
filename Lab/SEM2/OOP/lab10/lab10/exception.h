#pragma once

#include <iostream>
#include <exception>

using namespace std;

class MyException : public exception {

private:
    string message;

public:
    explicit MyException(const string& msg) : message{ msg } {};

    string getMessage() const {

        return message;
    }

};