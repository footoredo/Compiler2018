int main() {
    int la = 5;
    int lb = 10;
    int[] a;
    int[] b;
    b = new int[lb];
    int[][] c = new int [2][];
    a = new int[la];
    c[0] = a;
    c[1] = b;

    int cnt = 0;
    int i;
    int j;
    for (i = 0; i < 2; ++i)
        for (j = 0; j < la; ++j)
            c[i][j] = ++cnt;
    for (j = la; j < lb; ++j)
        c[1][j] = ++cnt;

    int sum = 0;
    for (i = 0; i < la; ++i) sum = sum + a[i];
    for (i = 0; i < lb; ++i) sum = sum + b[i];

    return sum;
}