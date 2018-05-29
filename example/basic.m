string A;
string B;
string C;
int N;

string calc(string A)
{
	int len = A.length();
	if (1 == len) return A;
	int mid = len/2;
	string L = calc(A.substring(0,mid-1));
	string R = calc(A.substring(mid,len-1));
	if (L < R) return L + R;
	else if (L == R) {
		int l = L.ord(0);
		int r = R.ord(0);
		if (l < r) return L + R;
		return R + L;
	}
	else if (L > R) return R + L;
	println("Never Ever!");
}

int main()
{
	A = getString();
	B = getString();
	N = B.parseInt();
	if (A.length() < N) {
		println("length error!");
		return 0;
	}
	C = calc(A.substring(0,N-1));
	println(C);
	return 0;
}