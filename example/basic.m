//input: 1 2 3 4

int[] a = new int[4];
int main()
{
    int[] b = new int[a.size()];
	int i;
    for (i = 0; i < a.size(); i++)
	{
		a[i] = 0;
		b[i] = getInt();
	}
	for (i = 0; i < a.size(); i++)
	{
		print(toString(a[i]));
	}
	println("");
	a=b;
	for (i = 0; i < a.size(); i++)
	{
		print(toString(a[i]));
	}
    return 0;
}



/*!! metadata:
=== comment ===
array_test1-mahaojun.mx
=== input ===
1
2
3
4
=== assert ===
output
=== timeout ===
0.1
=== output ===
0000
1234
=== phase ===
codegen pretest
=== is_public ===
True

!!*/
