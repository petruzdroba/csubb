bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    ;   ex 19
    ;   Se dau 2 siruri de octeti A si B. Sa se construiasca
    ;   sirul R care sa contina doar elementele pare
    ;   si negative din cele 2 siruri.
    
    a db 2, 1, 3, -3, -4, 2, -6
    b db 4, 5, -5, 7, -6, -2, 1
    len equ $ - b
    r times len db 0

; our code starts here
segment code use32 class=code
    start:
        mov ecx, len
        jecxz final
        
        mov esi, 0
        mov edi, 0
                    
        repeta:
            mov al, [a + esi]
            
            cmp al, 0
            jge next    ;jump greater or equal  
            
            test al, 1  ;testez paritatea elementului curent
            jz e_par
            ; e impar
            jmp next
            e_par:
                mov [r + edi], al
                inc edi
            next:
                inc esi
        loop repeta
        
        mov ecx, len
        jecxz final
        
        mov esi, 0
        
        repeta_1:
            mov al, [b + esi]
            
            cmp al, 0
            jge next_1
            
            test al, 1
            jz e_par_1
            ; e impar
            jmp next_1
            e_par_1:
                mov [r + edi], al
                inc edi
            next_1:
                inc esi
        loop repeta_1
        
        final:
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
