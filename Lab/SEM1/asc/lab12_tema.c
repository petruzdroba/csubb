/*Se citeste de la tastatura un sir de numere in baza 10, cu semn.
 Sa se determine valoarea minima din sir si sa se afiseze in fisierul min.txt (fisierul va fi creat)
 valoarea minima, in baza 16.
 */
 #include <stdio.h>
 
 void asmCompara(int element1, int element2);
 
 void citireSirC(char sir[]);
 
 int main(){
    int sir[100];
    citireSirC(sir);
    
    int min = sir[0];
    
    int index = 0;
    
    while(sir[index] != 0){
    }
    
    
    printf("%x", min);
 }
 
 void citireSirC(char sir[])
{
    scanf("%s", sir);
}