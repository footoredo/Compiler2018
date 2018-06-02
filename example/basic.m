int f (int x) {
    return x;
}

int g (int x) {
    return f(x);
}

int main () {
    return g(1);
}