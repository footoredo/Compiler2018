//考察点：section 9 函数，包括函数定义，内建函数
//算法：斜堆
//样例输入：
//5 5
//ABCDE
//1 2 3 4 5
//样例输出：
//5 E
//10
int N;
int M;
string ch;

int[] l;
int[] r;
int[] w;

int merge(int x,int y)
{
	if (0 == x) return y;
	if (0 == y) return x;
	if (w[x] < w[y]) {
		int e = x;
		x = y;
		y = e;
	}
	r[x] = merge(r[x],y);
	int e = l[x];
	l[x] = r[x];
	r[x] = e;
	return x;
}

int main()
{
	N = getInt();
	M = getInt();
	ch = getString();
	l = new int[N+M+5];
	r = new int[N+M+5];
	w = new int[N+M+5];
	int i;
	for (i=1;i <= N;i++) {
		w[i] = getInt();
		l[i] = 0;
   		r[i] = 0;
	}
	// println(ch);
	for (i=1;i <= M;i++) {
		w[i + N] = ch.ord(i-1);
		l[i + N] = 0;
		r[i + N] = 0;
	}
	int rt0 = 1;
	int rt1 = N + 1;
	for (i = 2;i <= N;i++)
		rt0 = merge(rt0,i);
	for (i = N+2;i<= N+M;i++)
		rt1 = merge(rt1,i);
	print(toString(w[rt0]));
	print(" ");
	print(ch.substring(rt1-N-1,rt1-N-1));
	print("\n");
	println(toString(merge(rt0,rt1)));
	return 0;
}



/*!! metadata:
=== comment ===
function_test-huyuncong.mx
=== input ===
5
5
ABCDE
1
2
3
4
5
=== assert ===
output
=== timeout ===
0.1
=== output ===
5 E
10
=== phase ===
codegen pretest
=== is_public ===
True

!!*/
