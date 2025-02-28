bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit, scanf, printf               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll 
import scanf msvcrt.dll 
import printf msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    ;ex 14
    ;Sa se citeasca de la tastatura doua numere a si b (in baza 16) si sa se calculeze a+b.
    ;Sa se afiseze rezultatul adunarii in baza 10.
    a dd 0
    b dd 0
    formath db "%x", 0  ;formatul din C pentru hexa
    formatd db "%d", 0

; our code starts here
segment code use32 class=code
    start:
        ;punem pe stiva argumentele de la dreapta la stanga
        push dword a
        push dword formath
        call [scanf]
        
        add esp, 4*2    ; curatam stiva, inainte de urmatorul apel
        
        push dword b
        push dword formath
        call [scanf]
        
        add esp, 4*2
        
        
        mov eax,[a]
        add eax,[b]
        
        push eax
        push dword formatd
        call[printf]
        
        add esp, 4*2
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
