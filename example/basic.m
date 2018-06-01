int[] b = new int[500005];
int[] now = new int[500005];
int[] t = new int[500005];
int[] a = new int[200005];
int n;
int m;
int p;
int op;
int L = 1;
int[] flag = new int[500005];
int[][] s = new int[500005][80];
int[] sum = new int[500005];
int ans = 0;

int square(int x) {
    return (x % p) * (x % p) ;
}

int gcd(int x, int y)
{
    if(y == 0) return x;
    if(x < y)
        return gcd(y,x);
    else
        return gcd(y, x % y);
}

int lcm(int a, int b) {
    return a / gcd(a, b) * b;
}

int aa=13131;
int bb=5353;
int MOD=(2<<14) - 7;
int no=1;

int Rand()
{
    int i = 1;
    for(i=1;i<3;i++)
        no=(no * aa + bb) % MOD;
    if(no < 0){
        no = -no;
    }
    return no;
}

int RandRange(int low, int high)
{
    return low + Rand() % (high - low + 1) + 1;
}


void init() {
    int[] c = new int[140005];
    int i = 0;
    int j = 0;
    for(i = 2; i < p; ++i) c[i] = i;
    for(i = 2; i < p; ++i)
        for(j = 1; j <= 15; ++j)
            c[i] = square(c[i]) % p;
    for(i = 2; i < p; ++i) {
        int j;
        int x = c[i];
        for(j = 1; ;++j) {
            x = square(x) % p;
            b[x] = 1;
            if(x == c[i]) break;
        }
        L = lcm(L, j);
    }
    b[0] = 1;
	b[1] = 1;
}

void build(int o, int l, int r) {
    int i = 0;
    if(l == r) {
        sum[o] = a[l];
        if(a[l] < p && a[l] >= 0  && b[a[l] % p] > 0) {
            flag[o] = 1;
            s[o][0] = a[l];
            for(i = 1; i < L; ++i)
                s[o][i] = square(s[o][i - 1]) % p;
        }
        now[o] = 0;
    } else {
        int lc = o * 2;
        int rc = o * 2 + 1;
        int mid = (l + r) / 2;
        build(lc, l, mid);
        build(rc, mid + 1, r);
        sum[o] = sum[lc] + sum[rc];
        flag[o] = flag[lc] & flag[rc];
        if(flag[o]>0){
            for(i = 0; i < L; ++i)
                s[o][i] = s[lc][i] + s[rc][i];
            now[0] = 0;
        }
    }
}

void pushdown(int o) {
    if(t[o]>0) {
        int lc = o * 2;
        int rc = o * 2 + 1;
        now[lc] = (now[lc] + t[o]) % L;
        sum[lc] = s[lc][now[lc]];
        t[lc] = (t[lc] + t[o]) % L;
        now[rc] = (now[rc] + t[o]) % L;
        sum[rc] = s[rc][now[rc]];
        t[rc] = (t[rc] + t[o]) % L;
        t[o] = 0;
    }
}

int pl = 0;
int pr = 0;
//
void update(int o, int l, int r) {
    if (pl <= l && pr >= r && flag[o]>0) {
        now[o] = (now[o] + 1) % L;
        sum[o] = s[o][now[o]];
        t[o] = (t[o] + 1) % L;
        return;
    }
    if(l == r) {
        sum[o] = square(sum[o]) % p;
        if(b[sum[o]]>0) {
            flag[o] = 1;
            s[o][0] = sum[o];
            int i = 0;
            for(i = 1; i < L; ++i)
                s[o][i] = square(s[o][i - 1]) % p;
        }
        return;
    }
    if(t[o]>0) pushdown(o);
    int lc = o * 2;
    int rc = o * 2 + 1;
    int mid = (l + r) / 2;
    if(pl <= mid) update(lc, l, mid);
    if(pr >= mid + 1) update(rc, mid + 1, r);
    sum[o] = sum[lc] + sum[rc];
    flag[o] = flag[lc] & flag[rc];
    if(flag[o]>0){
        int i = 0;
        for(i = 0; i < L; ++i)
            s[o][i] = s[lc][(i + now[lc]) % L] + s[rc][(i + now[rc]) % L];
        now[o] = 0;
    }
}

int query(int o, int l, int r) {
    if (pl <= l && pr >= r) return sum[o];
    int lc = o * 2;
    int rc = o * 2 + 1;
    int mid = (l + r) / 2;
    int ss = 0;
    if(t[o]>0) pushdown(o);
    if(pl <= mid) ss = (ss + query(lc, l, mid)) % MOD;
    if(pr >= mid + 1) ss = (ss + query(rc, mid + 1, r)) % MOD;
    return ss;
}

int main() {
    n = getInt();
    m = getInt();
    p = getInt();
    int i = 1;
    for(i = 1; i <= n; ++i)
        a[i] = RandRange(0, p);
    init();
    build(1, 1, n);
    while(m>0) {
        op = Rand() % 2;
        pl = RandRange(1, n);
        pr = RandRange(1, n);
        while(pr <= pl)
            pr = RandRange(1, n);
        if(op == 0) update(1, 1, n);
        if(op == 1) ans = (ans + query(1, 1, n))%MOD;
        m --;
    }
    print(toString(ans));
    return 0;
}



/*!! metadata:
=== comment ===
segtree-515030910592-lijinning.txt
=== is_public ===
True
=== assert ===
output
=== timeout ===
1.5
=== input ===
40000
40000
9977
=== phase ===
optim pretest
=== output ===
21523
=== exitcode ===


!!*/
