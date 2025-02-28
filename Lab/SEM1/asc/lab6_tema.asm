bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    ;ex 14
    ;Se da un sir S de dublucuvinte.
    ;Sa se obtina sirul D format din octetii dublucuvintelor din sirul D 
    ;sortati in ordine crescatoare in interpretarea fara semn.
    
    s DD 12345607h, 1A2B3C15h
    ls equ $-s
    d times ls db 0

; our code starts here
segment code use32 class=code
    start:
        ; traversam s pe octeti si ii punem in d, dupa sortam d
        
        ; copierea octetilor din s in d
        mov ecx, ls
        jecxz final
        
        cld
        
        mov esi, s
        mov edi, d
        
        rep movsb
        
        ; sortarea octetilor din d in ordine crescatoare
        ;utilizand metoda sortarii in loc
        
        mov ecx, ls
        dec ecx     ;decrementam 0 <= index < lungime -1
        jecxz final
        
        mov esi, d ;Sirul sursa va fi d, care contine octetii din s
        
        repeta_1:
            push ecx    ;punem ECX = ls pe stiva, pentru a utiliza ecx in urmatoarea struct repetitiva
            
            mov al, [esi]   ;valoarea curenta din d
            
            mov ebx, 1      ;index2 = index1 + 1
            repeta_2:
                mov dl, [esi + ebx]     ;valoarile care urmeaza dupa index1
                
                cmp al,dl       ;comparam val de pe index1 cu restul valorilor de dupa
                jb skip_swap    ;jump if below
                
                ;swap
                mov [esi + ebx], al
                mov [esi], dl
                mov al, dl  ;se mai poate folosi xchg
                
                skip_swap:
                    inc ebx     ;sarim la urmatorul element
                    cmp ebx, ls    ;verificam index2 <= ls
                    je next
            loop repeta_2
            
            next:
                inc esi     ;index1 trece la urmatorul element
                pop ecx     ; reinitializam ecx
        loop repeta_1
        
        final:
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
