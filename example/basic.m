int fuck (int x) {
    if (x == 0) return 1;
    else return x * fuck (x - 1);
}

int main () {
    println (toString(fuck(5)));
    return 0;
}