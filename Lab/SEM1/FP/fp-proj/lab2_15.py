def f(x):
    print("fffff",x)


a = int(input("Dati un numar"))
z=False
for i in range(a-1,1,-1):
    b = True
    for d in range(2, i//2):
        if i%d==0:
            b=False
            break
    if b:
        print(i)
        z=True
        break
if z==False:
    print("Nu exista")
