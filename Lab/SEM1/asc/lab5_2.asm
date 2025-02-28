bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    ;61 62 63 64 65
    a db 'abcde'    ;sir de caractere    
    len equ $-a     ;constanta, nr de biti generat precedent, trb facut dupa sir init

; our code starts here
segment code use32 class=code
    start:
        mov ecx, len    ;o constanta nu este stoca ta in segmentul de date
        mov ebx, 0  ;index
        
        jecxz final
        repeta:
            mov al, [a + ebx]
            sub al, 'a' - 'A'   ;transform in litera mare
            mov [a + ebx], al
            inc ebx
            
            loop repeta
        
        final:
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
