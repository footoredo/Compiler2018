print:
        push    rbp
        mov     rbp, rsp
        sub     rsp, 16
        mov     qword [rbp-8H], rdi
print#L_001:  mov     rax, qword [rbp-8H]
        movzx   eax, byte [rax]
        test    al, al
        jz      print#L_002
        mov     rax, qword [rbp-8H]
        movzx   eax, byte [rax]
        movsx   eax, al
        mov     edi, eax
        call    putchar
        add     qword [rbp-8H], 1
        jmp     print#L_001

print#L_002:  nop
        leave
        ret