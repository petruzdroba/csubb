bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit,scanf, fopen,fprintf, fclose, compara
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
import scanf msvcrt.dll
import fopen msvcrt.dll  
import fprintf msvcrt.dll
import fclose msvcrt.dll

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    ;26
    ;Se citeste de la tastatura un sir de numere in baza 10, cu semn, pana la valoarea 0.
    ;Sa se determine valoarea minima din sir si sa se afiseze in fisierul min.txt (fisierul va fi creat)
    ;valoarea minima, in baza 16.
    
    nume_fisier db 'min.txt', 0
    mod_acces db 'w',0
    descriptor dd -1
    
    formatc db '%d', 0
    formats db '%x', 0
    
    n dd 0
    s times 100 dd 0

; our code starts here
segment code use32 class=code
    start:
        mov edi , s
        cld
        ;citirea sirului
        repeta:
            push dword n
            push dword formatc
            call [scanf]
            add esp, 4*2
            
            mov eax, [n]
            stosd
            
            cmp eax, 0
            je skip
            
        jmp repeta
        skip:
            mov esi, s
            mov ebx, [s]    ;punem in EBX primul numar din sir
            
            repeta2:
                lodsd
                cmp eax, 0
                je skip2
                
                call compara    ; in ebx va fi returnat numarul mai mic
            jmp repeta2
            skip2:
                ;in EBX se afla acum cel mai mic numar din sir
                
                ;creem si deshidem fisierul destinatie
                push dword mod_acces
                push dword nume_fisier
                call [fopen]
                
                add esp, 4*2
                
                mov [descriptor], eax
                cmp eax, 0
                je final
                
                ;scriem in fisier rezultatul din ebx
                
                push ebx
                push formats
                push dword [descriptor]
                call[fprintf]
            
                add esp, 4*3
                
                final:
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
