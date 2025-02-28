bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit,scanf, fopen, fprintf, fclose          
import exit msvcrt.dll
import fopen msvcrt.dll
import scanf msvcrt.dll
import fprintf msvcrt.dll
import fclose msvcrt.dll  

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    
    nume_fisier db 'lab12_ex2.txt', 0
    mod_acces db 'a', 0
    descriptor dd -1
    
    cuvant times 5 dd 0
    format db '%s', 0

; our code starts here
segment code use32 class=code
    start:
        push dword mod_acces
        push dword nume_fisier
        call [fopen]
        
        mov [descriptor], eax
        cmp eax, 0
        je final
        
        repeta:
            push dword cuvant
            push dword format
            call [scanf]
            add esp, 4*2
            
            mov eax, [cuvant]
            cmp al, '$'
            je skip
            
            
            push dword cuvant
            push dword format
            push dword [descriptor]
            call[fprintf]                                                                                                                   
            add esp, 4*3
            
        jmp repeta
        skip:
            push dword [descriptor]
            call [fclose]
            add esp, 4
        final:
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
