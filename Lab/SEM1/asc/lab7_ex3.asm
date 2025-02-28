bits 32
global start

extern exit
import exit msvcrt.dll

segment data use32 class=data
    s1 dw 4h, 6, 89cdh
    ls equ ($-s1)/2
    s2 dw 2345h, 5678h, 4567h
    d times ls db 0

; Se dau doua siruri de cuvinte s1 si s2.
; Se cere sirul de cuvinte d obtinut in interpretarea cu semn, astfel:
; - d[i] = s1[i], daca s1[i] > s2[i]
; - d[i] = s2[i], altfel.
segment code use32 class=code
    start:
        mov ecx, ls
        jecxz final
        
        mov esi, s1
        mov edi, d
        
        cld
        
        rep movsw
        
        mov ecx, ls
        jecxz final
        
        mov esi, s2
        mov edi, d
        
        repeta:
            lodsw
            
            scasw
            jle next
            
            sub edi,2
            stosw
            
            next:
        loop repeta
        
        
        final:
        push dword 0
        call [exit]
