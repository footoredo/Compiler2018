    section .text
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

println:
        push    rbp
        mov     rbp, rsp
        sub     rsp, 16
        mov     qword [rbp-8H], rdi
        mov     rax, qword [rbp-8H]
        mov     rdi, rax
        call    puts
        nop
        leave
        ret

getString:
        push    rbp
        mov     rbp, rsp
        sub     rsp, 16
        mov     edi, 256
        call    _Znam
        mov     qword [rbp-8H], rax
        mov     rax, qword [rbp-8H]
        mov     rdi, rax
        call    gets
        leave
        ret


getInt:
        push    rbp
        mov     rbp, rsp
        sub     rsp, 16


        mov     rax, qword [fs:abs 28H]
        mov     qword [rbp-8H], rax
        xor     eax, eax
        lea     rax, [rbp-10H]
        mov     rsi, rax
        mov     edi, getInt#L_002
        mov     eax, 0
        call    scanf
        mov     rax, qword [rbp-10H]
        mov     rdx, qword [rbp-8H]


        xor     rdx, qword [fs:abs 28H]
        leave
        ret

SECTION .rodata

getInt#L_002:
        db 25H, 6CH, 64H, 00H

_strcmp:
        push    rbp
        mov     rbp, rsp
        sub     rsp, 16
        mov     qword [rbp-8H], rdi
        mov     qword [rbp-10H], rsi
        mov     rdx, qword [rbp-10H]
        mov     rax, qword [rbp-8H]
        mov     rsi, rdx
        mov     rdi, rax
        call    strcmp
        cdqe
        leave
        ret

toString:
        push    rbp
        mov     rbp, rsp
        sub     rsp, 32
        mov     qword [rbp-18H], rdi
        mov     edi, 20
        call    _Znam
        mov     qword [rbp-8H], rax
        mov     rdx, qword [rbp-18H]
        mov     rax, qword [rbp-8H]
        mov     esi, toString#L_002
        mov     rdi, rax
        mov     eax, 0
        call    sprintf
        mov     rax, qword [rbp-8H]
        leave
        ret
      SECTION .rodata
toString#L_002:
        db 25H, 6CH, 64H, 00H


_strcat:
        push    rbp
        mov     rbp, rsp
        sub     rsp, 32
        mov     qword [rbp-18H], rdi
        mov     qword [rbp-20H], rsi
        mov     rax, qword [rbp-18H]
        mov     rdi, rax
        call    strlen
        mov     qword [rbp-10H], rax
        mov     rax, qword [rbp-20H]
        mov     rdi, rax
        call    strlen
        mov     rdx, rax
        mov     rax, qword [rbp-10H]
        add     rax, rdx
        add     rax, 1
        mov     rdi, rax
        call    _Znam
        mov     qword [rbp-8H], rax
        mov     rdx, qword [rbp-18H]
        mov     rax, qword [rbp-8H]
        mov     rsi, rdx
        mov     rdi, rax
        call    strcpy
        mov     rdx, qword [rbp-10H]
        mov     rax, qword [rbp-8H]
        add     rdx, rax
        mov     rax, qword [rbp-20H]
        mov     rsi, rax
        mov     rdi, rdx
        call    strcpy
        mov     rax, qword [rbp-8H]
        leave
        ret

