## Specificarea limbajului de programare

Limbaj 'mama' folosit: C/C++

### Tipuri de date
- Integer
- String
- Double
- Struct

### Corp

```
- aritmethical ops: +, -, /, *, %, &&, ||
- assign op: =
- comparation: ==, !=, <, >, <=, >=
- misc: , .
- separators: ;, {}, ()
- reserved words: int, string, double,while, if, else, struct, in, out
```

### EBNF

```EBNF
<app> ::= [ <type_user_list> ] int main "(" ")" "{" [ <init_list> ] [ <instruction_list> ] "}"
<init_list> ::= <init> | <init> <init_list>
<init> ::= <type> VAR { "," VAR } ";"
<type> ::= int | double | string | VAR
<type_user_list> ::= <type_user> | <type_user> <type_user_list>
<type_user> ::= struct VAR "{" <init_list> "}" ";"

<instruction_list> ::= <instruction> | <instruction> <instruction_list>
<instruction> ::= <assign> | <read> | <write> | <return> | <cond_if> | <loop_while>

<ref> ::= VAR { "." VAR }
<assign> ::= <ref> "=" <expr> ";"
<expr> ::= <ref> | CONST | <expr> <op_arm> <expr> | <expr> <op_cmp> <expr>

<op_arm> ::= "+" | "-" | "/" | "*" | "%" | "&&" | "||"
<op_cmp> ::= "==" | "!=" | ">" | "<" | "<=" | ">="

<read> ::= cin ">>" <ref> ";"
<write> ::= cout "<<" <expr> ";"
<return> ::= return <expr> ";"

<cond_if> ::= if "(" <cond> ")" "{" <instruction_list> "}"
              [ else "{" <instruction_list> "}" ]
<cond> ::= <expr>

<loop_while> ::= while "(" <cond> ")" "{" <instruction_list> "}"

VAR       ::= letter ( letter | digit ){0,127}
CONST     ::= <c_int> | <c_double> | <c_string>
<c_int>   ::= digit+
<c_double> ::= digit+ "." digit+
<c_string>::= '"' [^"]* '"'
letter    ::= a | ... | z | A | B | ... | Z
digit     ::= 0 | ... | 9
```

### Programs using this language

```cpp
// perimeter and area of a circle

struct circle {
	double radius;
	double perimeter;
	double area;
};

int main() {
	circle c;
	double pi;
	
	pi = 3.14159;
	cin >> c.radius;
	c.perimeter = 2 * pi * c.radius;
	c.area = pi * c.radius * c.radius;
	
	return circle;
}
```

```C
// cmmdc 2 nr

int main() {
	int a, b, c;
	
	cin>>a;
	cin>>b;
	while(b != 0) {
		c = a % b;
		a = b;
		b =r;
	}
	
	return a;
}
```

```C
// sum of N numbers from in

int main() {
	int n, idx, x, sum;
	
	cin>>n;
	sum = 0;
	idx = 0;
	
	while (idx < n) {
		cin >> x;
		s = s+x;
		i = i+1;
	}
	
	return s;
}
```


### Errors

```C

#include <iostream>
using namespace std;

int main() {
    int a, b;
    cin >> a
    b = a * 2;
    while (b > 0 {
        b = b - 1;
    }
    cout << b;
    return 0;
}

// missing ";" after cin and missing ")" in while condition
// wrong here and in C++
```

```C

#include <iostream>
using namespace std;

int main() {
    int n = 5;
    int s;
    s = 0;
    for (int i = 1; i <= n; i++) {
        s = s + i;
    }
    cout << s;
    return 0;
}

// valid in C++, wrong here, for dosent exist here
// and int n = 5, not valid here
```