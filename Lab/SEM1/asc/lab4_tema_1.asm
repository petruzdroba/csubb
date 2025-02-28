bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    ; ex 4
    
    ;Se da octetul A. Sa se obtina numarul intreg n reprezentat de bitii 2-4 ai lui A. Sa se obtina apoi in B octetul rezultat prin rotirea spre dreapta a lui A cu n pozitii. Sa se ;obtina dublucuvantul C:
    ;bitii 8-15 ai lui C sunt 0
    ;bitii 16-23 ai lui C coincid cu bitii lui B
    ;bitii 24-31 ai lui C coincid cu bitii lui A
    ;bitii 0-7 ai lui C sunt 1
    
    ; a - byte, b - byte, c - dword
    a db 9fh
    n resb 1
    b resb 1
    c resd 1
    
; our code starts here
segment code use32 class=code
    start:
        ; se obtine n , nr intreg care este reprezentat de bitii 2-4 ai lui
        
        mov al,[a]
        and al, 0001_1100b
        shr al, 2   ; se elimina cei doi biti extra de pe poz 0-1
        mov [n],al 
        
        
        ; sa se obt in b rotirea spre dreapta a lui a cu n pozitii
        ;folosim registrul bl pt aceasta operatie
        
        mov bl, [a]
        mov cl, [n]
        ror bl, cl  ;rotim a spre dreapta cu n = CL biti
        mov [b], bl
        
        
        ; utilizez stiva ca sa formez dword C in registrul EAX
        ;forma AAAA_AAAA_BBBB_BBBB_0000_0000_1111_1111b
        
        ;formez DX - partea high in DX:AX
        ; DH - bitii lui [a]  DL - bitii lui [b]
        mov dh, [a]
        mov dl, [b]
        
        ;formez AX - partea low in DX:AX
        ; AH - zerorizata , AL - numai 1
        mov ah, 0
        mov al, 0ffh
        
        ;utilizez stiva
        push dx
        push ax
        pop eax
        
        mov [c], eax
        
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
