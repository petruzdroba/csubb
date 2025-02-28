bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit, printf              ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
import printf msvcrt.dll    

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    ;citire
    n db 120
    format_a db "n = %d", 13,10, 0   ;in C sirurile se termina cu zero, 13 si 10 reprezinta enter in windows
    
    c db 'H'
    format_c db "caracterul este %c",13,10, 0
    
    n1 dd 111
    n2 dd 222
    n3 dd 333
    
    format_b db "%d %d %d",13,10, 0
    
    sir db "ana are mere ", 0
    format_s db "%s", 13,10,0
    
; our code starts here
segment code use32 class=code
    start:
        ; afisez un intreg
        ; printf("n = %d", n) asta este in C
        xor eax, eax
        mov al, [n] ; se pune si n pe stiva
        push eax    ;deoarece nu pot pune un octet pe stiva, minim cuvant
        
        push dword format_a   ;punem offsetul pe stiva
        
        call [printf]
        add esp, 4*2    ;mu indicatorul de stiva mai jos , "eliberarea argumentelor"
        
        ;printf("%d %d %d", n1,n2,n3)
        mov eax, n3
        push eax
        mov eax, n2
        push eax
        mov eax , n1
        push eax
        
        push dword format_b
        
        call [printf]
        add esp, 4*4    ; 4 pentru ca avem patru argumente si 4 pentru ca sunt dublu cuvinte
        
        
        ;adisez un caracer
        ;printf("%c", c)
        
        xor eax, eax
        mov al, [c]     ;EAX = c
        push eax
        
        push dword format_c
        
        call[printf]
        
        add esp, 4*2    ;mu indicatorul de stiva mai jos , "eliberarea argumentelor"
        
        ;afisex un si de caractere
        ;printf("%s", sir)
        
        push dword sir
        push dword format_s
        
        call[printf]
        
        add esp, 2*4
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
