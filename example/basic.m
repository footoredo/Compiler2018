int main() {
    int la = 5;
    int[] a = new int[la];

    int cnt = 0;
    int i;
    for (i = 0; i < la; ++i)
        a[i] = ++cnt;

    int sum = 0;
    for (i = 0; i < la; ++i) sum = sum + a[i];

    return sum;
}




/*!! metadata:
=== comment ===
array3.mx
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
15

!!*/
