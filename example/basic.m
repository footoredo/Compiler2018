//考察点：section 8 语句，包括if,while,for,break,continue,return等
//算法：线性筛法求欧拉函数
//样例输入：10
//样例输出：
//1
//2
//2
//4
//2
//6
//4
//6
//4

int N;
int M = 0;
bool[] check;

int main() {
    N = getInt();
	check = new bool[N+5];
	int i = 0;
	while ( i <= N ) check[i++] = true;
	int[] phi = new int[N+5];
	int[] P = new int[N+5];
	phi[1] = 1;
	for (i = 2; ; ++i ) {
		if ( i > N ) break;
		if ( check[i] ) {
			P[++M] = i;
			phi[i] = i - 1;
		}
		int k = i;
		int i;
		for (i = 1; i <= M && (k * P[i] <= N); i++) {
			int tmp = k * P[i];
			if ( tmp > N ) continue;
			check[tmp] = false;
			if ( k % P[i] == 0) {
				phi[tmp] = phi[k] * P[i];
				break;
			}
			else {
				phi[k * P[i]] = phi[k] * (P[i] - 1);
			}
		}
		println(toString(phi[k]));
	}
    return 0;
}



/*!! metadata:
=== comment ===
statement_test-huyuncong.mx
=== input ===
10
=== assert ===
output
=== timeout ===
0.1
=== output ===
1
2
2
4
2
6
4
6
4
=== phase ===
codegen pretest
=== is_public ===
True

!!*/
