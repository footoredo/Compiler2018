int Wallace = 1<<10;
class sometimes {
    int naive;
    string haha;
}
class ArrayWrapper {
    sometimes[][] arr;
}
int get(int n) {
    if (n == 0) return 0;
    return 1 + get(n-1);
}
int main() {
    sometimes keep = new sometimes;
    int n = 3;
    keep.naive = 2 + get(n) * 4 / 5;
    bool fun = (keep.naive - 5 * 3) != (666 ^ 7);
    while (getInt() < Wallace) {
        keep.naive++;
        ++n;
    }
    return 0;
}


/*!! metadata:
=== comment ===
language_manual.mx
=== assert ===
success_compile
=== phase ===
semantic pretest
=== is_public ===
True

!!*/
