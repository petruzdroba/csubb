bits 32
segment code use32 public code
global compara

compara:
    cmp ebx, eax
    jle .done
    mov ebx, eax
    .done:
        ret