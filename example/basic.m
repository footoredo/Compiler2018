int main() {
    int [] a = new int [2];
    int i;
    int [][] c = new int [1][];
    c[0] = a;
    for (i = 0; i < 2; ++ i) {
        c[0][i] = 1;
    }
    return a[1];
}