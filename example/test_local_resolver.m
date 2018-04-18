int v;

int fuck (int u) {
    u = u + 1;
    v = u + 2;
    int c = v;
    fuck (c);
}