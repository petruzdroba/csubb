bits 32
global start

extern exit
import exit msvcrt.dll

segment data use32 class=data
    s dd 11234456h, 22345567h, 33456678h
    ls equ ($-s)/4
    d times ls dw 0

; Se da un sir de dublucuvinte s.
; Se cere sirul d ale carui elemente sunt cuvinte obtinute in interpretarea cu semn, astfel:
; - octetul inferior din d[i] este suma octetilor inferiori ai cuvintelor din fiecare dublucuvant al sirului s
; - octetul superior din d[i] este suma octetilor superiori ai cuvintelor din fiecare dublucuvant al sirului s
segment code use32 class=code
    start:
        mov ecx, ls
        jecxz final
        
        cld
        
        mov esi, s
        mov edi ,d 
        
        repeta:
            lodsw   ; incarcam cuvinte ca sa accesam partea inferiaoara mai usor
            
            mov bl, al
            mov bh, ah
            
            lodsw   ; incarc cuvantul superior acum
            
            add al, bl  ; facem suma fara transport
            add ah, bh
            
            stosw       ;<ES:EDI> <- suma in ax si in edi += 2
        
        final:
        push dword 0
        call [exit]
