bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit, printf, scanf           
import exit msvcrt.dll    
import printf msvcrt.dll    
import scanf msvcrt.dll    
                          

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    mesaj db "dati un numar:    ", 0
    
    n dd 0  ; definesc ca dublu cavant pt ca fucntiile ret in eax
    format_n db "%d", 0
    
    format_2 db "Am citit %d",0
; our code starts here
segment code use32 class=code
    start:
        push dword mesaj
        call [printf]
        add esp, 1*4
        
        ; cititrea unui intreg de la tastatura
        ;scanf("%d", &n)    ;adresa lui n
        
        push dword n    ;punem pe stiva dresa lui n
        push dword format_n
        
        call [scanf]
        
        add esp, 2*4
        
        ;afisam intregul citit
        ;printf("%d", n)
        
        push dword [n]  ;punem valoarea de la adresa n !!!DIFERIT DE ADRESA
        push dword format_2
        
        call[printf]
        add esp, 2*4
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
