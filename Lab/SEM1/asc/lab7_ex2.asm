bits 32
global start

extern exit
import exit msvcrt.dll

segment data use32 class=data
    s db 'a', 'b', 'c', 'd', 'e', 'f'
    ls equ $-s
    d times ls db 0
    
; Se da un sir de caractere s.
; Se cere sirul de caractere d obtinut prin inversarea sirului s, folosind instructiuni pe siruri.
segment code use32 class=code
    start:
        mov ecx, ls
        jecxz final
        
        std
        
        mov esi, s
        mov edi, d
        
        repeta:
            cld
            movsb
            std
        loop repeta
        
        
        final:
        push dword 0
        call [exit]
