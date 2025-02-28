bits 32
global start

extern exit
import exit msvcrt.dll

segment data use32 class=data
    
    
; Cititi de la tastatura si afisati o propozitie
; (mai multe siruri de caractere separate prin SPATII si care se termina cu caracterul '.')
segment code use32 class=code
    start:
        
    

        push dword 0        ; push the parameter for exit onto the stack
        call [exit]         ; call exit to terminate the program
