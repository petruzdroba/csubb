bits 32
global start

extern exit
import exit msvcrt.dll

segment data use32 class=data
    s db 'abcdef'
    len equ $-s
    d times len db 0
    
; Se da un sir de caractere s.
; Se cere sirul de caractere d obtinut prin copierea sirului s.
segment code use32 class=code
    start:
        mov ecx, len
        ;cmp ecx, 0
        ;je final
        
        jecxz final
        ;times 128 nop
        
        mov esi, 0
        mov edi, 0
        
        ;cld                ;DF = 0 - se incrementeaza sirul
        
        repeta:
            ; movsb = lodsb + sotsb
        
            ;lodsb          ;al = s[i] + inc ESI
        
            mov al, [s + esi]
            mov [d + edi], al
            
            ;sotsb          ; d[i] = al + inc EDI
            
            inc esi
            inc edi
        loop repeta
        
        final:
        ; exit(0)
        push dword 0        ; push the parameter for exit onto the stack
        call [exit]         ; call exit to terminate the program
