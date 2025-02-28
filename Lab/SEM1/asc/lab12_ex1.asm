bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit, printf, fopen, fscanf, fclose          
import exit msvcrt.dll
import printf msvcrt.dll 
import fopen msvcrt.dll
import fscanf msvcrt.dll
import fclose msvcrt.dll   
                          

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    ; Se citesc din fisier un sir de numere
    ; Sa se afiseze suma numerelor negative si pare
    
    nume_fisier db 'lab12.txt', 0
    mod_acces db 'r', 0
    
    descriptor dd -1
    
    formatc db '%d', 0
    
    n dd 0
    ;sir times 100 dd 0
    
; our code starts here
segment code use32 class=code
    start:
        push dword mod_acces
        push dword nume_fisier
        call [fopen]
        add esp, 4*2
        
        mov [descriptor], eax
        cmp eax, 0
        je final
        
        mov edi, sir
        cld
        
        xor ebx, ebx
        
        citire:
            push dword n
            push dword formatc
            push dword [descriptor]
            call [fscanf]
            add esp, 4*3
            
            cmp eax, 1
            jne close
            
            mov eax, [n]
            cmp eax, 0
            jge pozitiv
            
            ;e_negativ: verific ca e par
                test eax, 1
                jnz pozitiv
                ;e_par:
                   add ebx, eax
            pozitiv:
        jmp citire
        close:
            push dword [descriptor]
            call [fclose]
            add esp, 4
            
        final:
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
