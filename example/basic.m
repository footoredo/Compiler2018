int main() {
    string s1 = "aaa";
    string s2 = "bbbbb";
    string s3 = s1 + s2;
    return s3.length() + s3.ord(5);
}



/*!! metadata:
=== comment ===
string1.mx
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
106

!!*/
