int main () {
    bool a = getInt() != 0;
    bool b = getInt() != 0;
    bool c = getInt() != 0;
    bool d = getInt() != 0;
    bool e = (a || b ) && (b && c) && (c || d || a);
    if (e)
        return 1;
    else
        return 0;
}