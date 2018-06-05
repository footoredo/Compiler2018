int f(int x, int y) {
if (x == 0) return 1;
return x * f(x - 1, y);
}

int main () {
    println(toString( f(10, 9)));
    return 0;
}