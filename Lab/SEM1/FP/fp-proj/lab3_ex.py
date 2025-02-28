def readList(lista):
    nrOfElements = int(input("Nr of elements: "))
    for i in range(0, nrOfElements):
        lista.append(int(input(f"lista[{i}] = ")))

def cmmdc(a,b):
    while a != b:
        if a>b: 
            a-=b
        elif b>a:
            b-=a
    return a

def choiceTwo(lista):
    a = 0
    b = 1
    lungimeCurenta = 0
    lungimeMax = 0
    coord = 0
    while b< len(lista):
        if lungimeCurenta > lungimeMax:
            lungimeMax = lungimeCurenta
            coord = a
        if cmmdc(lista[a], lista[b]) == 1:
            lungimeCurenta += 1
        else:
            lungimeCurenta = 0
        a = b
        b += 1
        
    lista_printat_rezultat = []
    i = lungimeMax - 1
    while i >= -1:
        lista_printat_rezultat.append(lista[coord])
        i -= 1
        coord -=1
    lista_printat_rezultat.reverse()
    print(lista_printat_rezultat)
                
def choiceThree(lista):
    lista_max = []
    for a in range(0, len(lista)):
        lista_curenta = []
        while a < len(lista):
            if len(lista_curenta) > len(lista_max):
                lista_max = lista_curenta
            if lista[a] not in lista_curenta:
                lista_curenta.append(lista[a])
            else:
                lista_curenta = []
            a +=1 
    print(lista_max)  

def isPrime(number):
    if number < 2:
        return False
    if number == 2:
        return True
    if number % 2 ==0:
        return False
    for i in range(3,number//2):
        if number % i ==0:
            return False
    return True
           
def choiceFour(lista):
    a = 0
    b = 1
    lungimeCurenta = 0
    lungimeMax = 0
    coord = 0
    while b< len(lista):
        if lungimeCurenta > lungimeMax:
            lungimeMax = lungimeCurenta
            coord = a
        if isPrime(abs(lista[b] - lista[a])):
            lungimeCurenta += 1
        else:
            lungimeCurenta = 0
        a = b
        b += 1
        
    lista_printat_rezultat = []
    i = lungimeMax - 1
    while i >= -1:
        lista_printat_rezultat.append(lista[coord])
        i -= 1
        coord -=1
    lista_printat_rezultat.reverse()
    print(lista_printat_rezultat)


def consoleMenu():
    print("Menu")
    print("1. Read a list")
    print("2. Any random consecutive elements are prime in between them")
    print("3. All elements are unique")
    print("4. Any consecutive numbers difference is a prime number")
    print("5. Exit menu")
    choice = int(input("Pick an option from the menu "))
    if choice == 1:
        readList(lista)
        consoleMenu()
    elif choice == 2:
        choiceTwo(lista)
    elif choice == 3:
        choiceThree(lista)
    elif choice == 4:
        choiceFour(lista)
    else:
        return 0

lista=[]
consoleMenu()