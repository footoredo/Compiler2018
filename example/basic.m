int main() {
    int a = 5;
    int b;
    int c;
    c = a++;
    b = c;
    return b+c+a;
}



/*!! metadata:
=== comment ===
simple7.mx
=== assert ===
exitcode
=== timeout ===
0.1
=== input ===

=== phase ===
codegen pretest
=== is_public ===
True
=== exitcode ===
16

!!*/
