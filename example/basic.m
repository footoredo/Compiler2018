int n;
int[] a;

void exchange(int x,int y)
{
    int t=a[x];
    a[x]=a[y];
    a[y]=t;
}

int makeHeap() {
    int i;
    int t;
    int j;
    i = (n - 1) / 2;
    t = 0;
	j = 0;
    while (i >= 0) {
        j = i * 2;
        if (i*2+1<n && a[i*2+1]<a[i*2]) j = i*2+1;
        if (a[i] > a[j]) {
            exchange(i,j);
        }
        i = i - 1;
    }
    return 0;
}

int adjustHeap(int n) {
    int i;
    int j;
    int t;
    i = 0;
	j = 0;
	t = 0;
    while (i * 2 < n) {
        j = i*2;
        if (i*2+1<n && a[i*2+1] < a[i*2]) j = i*2+1;
        if (a[i] > a[j]) {
            int t = a[i];
            a[i] = a[j];
            a[j] = t;
            i = j;
        }
        else break;
    }
    return 0;
}

int heapSort() {
    int t;
    int k;
    t = 0;
    for (k = 0; k < n; k = k + 1) {
        t = a[0];
        a[0] = a[n-k-1];
        a[n-k-1] = t;
        adjustHeap(n-k-1);
    }
    return 0;
}

int main() {
    int i;
	n = getString().parseInt();
	a = new int[n];

    for (i = 0; i < a.size(); i = i + 1)
		a[i] = i;
    makeHeap();
    heapSort();
    for (i = 0; i < a.size(); i = i + 39997)
        print(toString(a[i]) + " ");
	print("\n");
    return 0;
}



/*!! metadata:
=== comment ===
heapsort-5100379110-daibo.mx
=== is_public ===
True
=== assert ===
output
=== timeout ===
2.5
=== input ===
3289347
=== phase ===
optim pretest
=== output ===
3289346 3249349 3209352 3169355 3129358 3089361 3049364 3009367 2969370 2929373 2889376 2849379 2809382 2769385 2729388 2689391 2649394 2609397 2569400 2529403 2489406 2449409 2409412 2369415 2329418 2289421 2249424 2209427 2169430 2129433 2089436 2049439 2009442 1969445 1929448 1889451 1849454 1809457 1769460 1729463 1689466 1649469 1609472 1569475 1529478 1489481 1449484 1409487 1369490 1329493 1289496 1249499 1209502 1169505 1129508 1089511 1049514 1009517 969520 929523 889526 849529 809532 769535 729538 689541 649544 609547 569550 529553 489556 449559 409562 369565 329568 289571 249574 209577 169580 129583 89586 49589 9592
=== exitcode ===


!!*/
