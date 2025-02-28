a = int(input("Dati un numar"))
check = False
i = a+1
while not check:
    sum = 1
    for d in range(2, i//2+1):
        if i%d == 0:
            sum+=d
    if sum == i:
        print(i)
        check = True
    i+=1