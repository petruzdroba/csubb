bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit,scanf, fopen,fprintf, fclose              ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
import scanf msvcrt.dll
import fopen msvcrt.dll  
import fprintf msvcrt.dll
import fclose msvcrt.dll

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    ;17
    ;Se da un nume de fisier (definit in segmentul de date).
    ;Sa se creeze un fisier cu numele dat, apoi sa se citeasca de la tastatura numere si sa se scrie din valorile citite in fisier numerele divizibile cu 7, pana cand se citeste de la tastatura valoarea 0.
    
    nume_fisier db 'result.txt', 0
    mod_acces db 'a', 0     ;append deoarece vom trece prin sir, si vom adauga la finalul fisierului fiecare numar
    descriptor dd -1
    
    formatc db '%d', 0  ;formatul de citire
    formats db '%d ',0  ;format de afisare
    n dd 0              ; numarul care este citit
    s times 100 dd 0    ;sirul unde numerele vor fi salvate daca sunt divizibile cu 7

; our code starts here
segment code use32 class=code
    start:
        mov edi, s  ;sirul destinatie
        cld
        
        repeta:
            push dword n
            push dword formatc
            call [scanf]
            
            add esp, 4*2
            
            mov eax, [n]    ;punem pe n in eax, pentru a utiliza stiva
            
            push eax        ; transformam cu ajutorul stivei eax in dx:ax
            pop ax
            pop dx
            
            mov bx, 7
            div bx          ; impartim DX:AX la BX, cat - ax si rest - dx
            
            cmp dx, 0       ; comparam restul cu 0
            jne skip        ; daca ZF nu e zero, se sare peste numar
            
            mov eax,[n]     ; punem din nou in eax, val lui n, ca sa poate sa fie pusa in sir
            stosd          
            
            cmp eax, 0      ;comparam val curenta a lui n cu 0 (aceasta a fost deja adaugata in sir, pt a fi folosita ca 
            je skip2        ;conditie de stop)
            
            skip:
            jmp repeta
        skip2:
        
        ;creem si deshidem fisierul destinatie
        push dword mod_acces
        push dword nume_fisier
        call [fopen]
        
        add esp, 4*2
        
        mov [descriptor], eax   ;salvam descriptor fisier
        cmp eax, 0  ; verificam daca functia fopen a creat cu succes fisierul (daca EAX != 0)
        je final
        
        
        ; adaugam/scriem textul in fisierul deschis folosind functia fprintf
        ; fprintf(descriptor_fis, text)
        mov esi, s  ;sirul sursa
        
        repeta2:
            lodsd
            
            cmp eax, 0
            je skip3
           
            push eax
            push formats
            push dword [descriptor]
            call[fprintf]
            
            add esp, 4*3
            
            jmp repeta2
        skip3:
        
        ;apelam functia fclose pentru a inchide fisierul
        ; fclose(descriptor_fis)
        push dword [descriptor]
        call [fclose]
        add esp, 4
        
        final:
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
