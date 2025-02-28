bits 32
segment code use32 public code
global _asmCompara

_asmCompara:
    mov eax, [esp + 4]
    mov ebx, [esp + 8]
    
    cmp eax, ebx
    jle .done
    mov eax,ebx
    
    .done:
        ret