bits 32
global start

extern exit, scanf, printf
import exit msvcrt.dll
import scanf msvcrt.dll
import printf msvcrt.dll

segment data use32 class=data
    formatc db '%d', 0
    n dd 0
    s times 100 dd 0
    
; Cititi de la tastatura si afisati un sir de numere intregi.
; (citirea se termina atunci cand utilizatorul introduce numarul ZERO)
segment code use32 class=code
    start:
        mov edi, s
        cld
        
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
        repeta2:
            lodsd
            cmp eax, 0
            je skip2
            
            push eax
            push dword formatc
            
            call[printf]
            add esp, 4*2
            
            jmp repeta2
           
        skip2:
            
        push dword 0        ; push the parameter for exit onto the stack
        call [exit]         ; call exit to terminate the program
