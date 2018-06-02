
int main() {
	int a = 3100;
	int b = 0;
	int c = 1;
	for (b = 0; b < 100000000; ++b)
		c = c * 2 - c;
    println(toString(a));

	return 0;
}



/*!! metadata:
=== comment ===
code_elimination_recursion-515030910639-yingsihao.txt
=== is_public ===
True
=== assert ===
output
=== timeout ===
1.0
=== input ===

=== phase ===
optim extended
=== output ===
3100
3100
7053
1035
7035
=== exitcode ===


!!*/
