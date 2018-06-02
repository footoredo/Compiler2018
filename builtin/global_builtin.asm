    section .text
print:
        push    rbp
        mov     rbp, rsp
        sub     rsp, 16
        mov     rax, rdi
        mov     qword [rbp-8H], rax
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
        mov     rax, rdi
        mov     qword [rbp-8H], rax
        mov     rax, qword [rbp-8H]
        mov     rdi, rax
        call    puts
        ;nop
        leave
        ret

getString:
        push    rbp
        mov     rbp, rsp
        sub     rsp, 16
        mov     edi, 256
        call    malloc
        mov     qword [rbp-8H], rax
        mov     rax, qword [rbp-8H]
        mov     rsi, rax
        mov     edi, getString#L_005
        mov     eax, 0
        call    scanf
        mov     rax, qword [rbp-8H]
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
        mov     edi, getInt#L_004
        mov     eax, 0
        call    scanf
        mov     rax, qword [rbp-10H]
        mov     rdx, qword [rbp-8H]


        xor     rdx, qword [fs:abs 28H]
        jz      getInt#L_001
        call    __stack_chk_fail
getInt#L_001:  leave
        ret

SECTION .rodata

getInt#L_004:
        db 25H, 6CH, 64H, 00H



getString#L_005:
        db 25H, 73H, 00H

_strcmp:
        push    rbp
        mov     rbp, rsp
        sub     rsp, 16
        mov     rax, rdi
        mov     qword [rbp-8H], rax
        mov     rax, qword [rbp+16]
        mov     qword [rbp-10H], rax
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
        mov     rax, rdi
        mov     qword [rbp-18H], rax
        mov     rdi, 20
        call    malloc
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
        mov     rsi, qword [rbp + 16]
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
        call    malloc
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

__array#size:
        push    rbp
        mov     rbp, rsp
        mov     rax, qword [rel @_@thisPointer]
        ;mov     rax, qword [rax]
        mov     rax, qword [rax]
        pop     rbp
        ret

string#length:
        push    rbp
        mov     rbp, rsp
        mov     rax, qword [rel @_@thisPointer]
        ;mov     rax, qword [rax]
        mov     rdi, rax
        call    strlen
        pop     rbp
        ret

string#substring:
        push    rbp
        mov     rbp, rsp
        sub     rsp, 32
        mov     rax, rdi
                mov     qword [rbp-18H], rax
                mov     rax, qword [rbp+16]
                mov     qword [rbp-20H], rax
        mov     rax, qword [rbp-20H]
        sub     rax, qword [rbp-18H]
        add     rax, 2
        mov     rdi, rax
        call    malloc
        mov     qword [rbp-8H], rax
        mov     rax, qword [rbp-20H]
        sub     rax, qword [rbp-18H]
        add     rax, 1
        mov     rsi, rax
        mov     rdx, qword [rel @_@thisPointer]
        ;mov     rdx, qword [rax]
        mov     rax, qword [rbp-18H]
        lea     rcx, [rdx+rax]
        mov     rax, qword [rbp-8H]
        mov     rdx, rsi
        mov     rsi, rcx
        mov     rdi, rax
        call    strncpy
        mov     rax, qword [rbp-20H]
        sub     rax, qword [rbp-18H]
        lea     rdx, [rax+1H]
        mov     rax, qword [rbp-8H]
        add     rax, rdx
        mov     byte [rax], 0
        mov     rax, qword [rbp-8H]
        leave
        ret

string#parseInt:
        push    rbp
        mov     rbp, rsp
        mov     qword [rbp-10H], 0
        mov     rax, qword [rel @_@thisPointer]
        ;mov     rax, qword [rax]
        mov     qword [rbp-8H], rax
string#parseInt#L_002:  mov     rax, qword [rbp-8H]
        movzx   eax, byte [rax]
        cmp     al, 47
        jle     string#parseInt#L_003
        mov     rax, qword [rbp-8H]
        movzx   eax, byte [rax]
        cmp     al, 57
        jg      string#parseInt#L_003
        mov     rdx, qword [rbp-10H]
        mov     rax, rdx
        shl     rax, 2
        add     rax, rdx
        add     rax, rax
        mov     rdx, rax
        mov     rax, qword [rbp-8H]
        movzx   eax, byte [rax]
        movsx   rax, al
        add     rax, rdx
        sub     rax, 48
        mov     qword [rbp-10H], rax
        add     qword [rbp-8H], 1
        jmp     string#parseInt#L_002

string#parseInt#L_003:  mov     rax, qword [rbp-10H]
        pop     rbp
        ret

string#ord:
        push    rbp
        mov     rbp, rsp
        mov     eax, edi
        mov     dword [rbp-4H], eax
        mov     rdx, qword [rel @_@thisPointer]
        ;mov     rdx, qword [rax]
        mov     eax, dword [rbp-4H]
        cdqe
        add     rax, rdx
        movzx   eax, byte [rax]
        movsx   rax, al
        pop     rbp
        ret