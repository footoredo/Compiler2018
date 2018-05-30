//Compute and Crack SHA-1
//by zzk

int hex2int(string x)
{
	int i;
	int result = 0;
	for(i=0;i<x.length();i++)
	{
		int digit = x.ord(i);
		if(digit >= 48 && digit <= 57)
			result = result * 16 + digit - 48;
		else if(digit >= 65 && digit <= 70)
			result = result * 16 + digit - 65 + 10;
		else if(digit >= 97 && digit <= 102)
			result = result * 16 + digit - 97 + 10;
		else
			return 0;
	}
	return result;
}

string asciiTable = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
string int2chr(int x)
{
	if(x >= 32 && x <= 126)
		return asciiTable.substring(x-32, x-32);
	return "";
}
string toStringHex(int x)
{
	string ret = "";
	int i;
	for(i=28;i>=0;i=i-4)
	{
		int digit = (x >> i) & 15;
		if(digit < 10)
			ret = ret + int2chr(48+digit);
		else
			ret = ret + int2chr(65+digit-10);
	}
	return ret;
}
int rotate_left(int x, int shift)
{
	if(shift == 1)
		return ((x & 2147483647) << 1) | ((x >> 31) & 1);
	if(shift == 31)
		return ((x & 1) << 31) | ((x >> 1) & 2147483647);
	return ((x & ((1 << (32-shift)) - 1)) << shift) | ((x >> (32-shift)) & ((1 << shift) - 1));
}
int add(int x, int y)	//to avoid possible undefined behaviour when overflow
{
	int low = (x & 65535) + (y & 65535);
	int high = (((x >> 16) & 65535) + ((y >> 16) & 65535) + (low >> 16)) & 65535;
	return (high << 16) | (low & 65535);
}
int lohi(int lo, int hi)
{
	return lo | (hi << 16);
}

int MAXCHUNK = 100;
int MAXLENGTH = (MAXCHUNK-1) * 64 - 16;
int[][] chunks = new int[MAXCHUNK][80];
int[] inputBuffer = new int[MAXLENGTH];
int[] outputBuffer = new int[5];
int[] sha1(int[] input, int length)
{
	int nChunk = (length + 64 - 56) / 64 + 1;
	if(nChunk > MAXCHUNK)
	{
		println("nChunk > MAXCHUNK!");
		return null;
	}
	int i;
	int j;
	for(i=0;i<nChunk;i++)
		for(j=0;j<80;j++)
			chunks[i][j] = 0;
	for(i=0;i<length;i++)
		chunks[i/64][i%64/4] = chunks[i/64][i%64/4] | (input[i] << ((3-i%4)*8));
	chunks[i/64][i%64/4] = chunks[i/64][i%64/4] | (128 << ((3-i%4)*8));
	chunks[nChunk-1][15] = length << 3;
	chunks[nChunk-1][14] = (length >> 29) & 7;

	int h0 = 1732584193;  //0x67452301
	int h1 = lohi(43913, 61389);  //0xEFCDAB89
	int h2 = lohi(56574, 39098); //0x98BADCFE
	int h3 = 271733878;   //0x10325476
	int h4 = lohi(57840, 50130); //0xC3D2E1F0
	for(i=0;i<nChunk;i++)
	{
		for(j=16;j<80;j++)
			chunks[i][j] = rotate_left(chunks[i][j-3] ^ chunks[i][j-8] ^ chunks[i][j-14] ^ chunks[i][j-16], 1);

		int a = h0;
		int b = h1;
		int c = h2;
		int d = h3;
		int e = h4;
		for(j=0;j<80;j++)
		{
			int f;
			int k;
			if(j<20)
			{
				f = (b & c) | ((~b) & d);
				k = 1518500249; //0x5A827999
			}
			else if(j<40)
			{
				f = b ^ c ^ d;
				k = 1859775393; //0x6ED9EBA1
			}
			else if(j<60)
			{
				f = (b & c) | (b & d) | (c & d);
				k = lohi(48348, 36635); //0x8F1BBCDC
			}
			else
			{
				f = b ^ c ^ d;
				k = lohi(49622, 51810); //0xCA62C1D6
			}
			int temp = add(add(add(rotate_left(a, 5), e), add(f, k)), chunks[i][j]);
			e = d;
			d = c;
			c = rotate_left(b, 30);
			b = a;
			a = temp;
		}
		h0 = add(h0, a);
		h1 = add(h1, b);
		h2 = add(h2, c);
		h3 = add(h3, d);
		h4 = add(h4, e);
	}
	outputBuffer[0] = h0;
	outputBuffer[1] = h1;
	outputBuffer[2] = h2;
	outputBuffer[3] = h3;
	outputBuffer[4] = h4;
	return outputBuffer;
}

void computeSHA1(string input)
{
	int i;
	for(i=0; i<input.length(); i++)
		inputBuffer[i] = input.ord(i);
	int[] result = sha1(inputBuffer, input.length());
	for(i=0; i<result.size(); i++)
		print(toStringHex(result[i]));
	println("");
}

int nextLetter(int now)
{
	if(now == 122) //'z'
		return -1;
	if(now == 90)  //'Z'
		return 97; //'a'
	if(now == 57)  //'9'
		return 65;
	return now + 1;
}

bool nextText(int[] now, int length)
{
	int i;
	for(i=length-1; i>=0; i--)
	{
		now[i] = nextLetter(now[i]);
		if(now[i] == -1)
			now[i] = 48;	//'0'
		else
			return true;
	}
	return false;
}

bool array_equal(int[] a, int[] b)
{
	if(a.size() != b.size())
		return false;
	int i;
	for(i=0; i<a.size(); i++)
		if(a[i] != b[i])
			return false;
	return true;
}

void crackSHA1(string input)
{
	int[] target = new int[5];
	if(input.length() != 40)
	{
		println("Invalid input");
		return;
	}
	int i;
	for(i=0;i<5;i++)
		target[i] = 0;
	for(i=0;i<40;i=i+4)
		target[i/8] = target[i/8] | (hex2int(input.substring(i, i+3)) << (1 - (i / 4) % 2) * 16);

	int MAXDIGIT = 4;
	int digit;
	for(digit=1; digit <= MAXDIGIT; digit++)
	{
		for(i=0;i<digit;i++)
			inputBuffer[i] = 48;
		while(true)
		{
			int[] out = sha1(inputBuffer, digit);
			if(array_equal(out, target))
			{
				for(i=0;i<digit;i++)
					print(int2chr(inputBuffer[i]));
				println("");
				return;
			}
			if(!nextText(inputBuffer, digit))
				break;
		}
	}
	println("Not Found!");
}

int main()
{
	int op;
	string input;
	while(true)
	{
		op = getInt();
		if(op == 0)
			break;
		if(op == 1)
		{
			input = getString();
			computeSHA1(input);
		}
		else if(op == 2)
		{
			input = getString();
			crackSHA1(input);
		}
	}
	return 0;
}

/*!! metadata:
=== comment ===
sha-515030910628-zhangzhekai.txt Compute and Crack SHA-1
=== is_public ===
True
=== assert ===
output
=== timeout ===
4.5
=== input ===
1
ACM
1
acm2015
1
compiler2017
1
optim-extended
1
SJTU
1
2017
2
ABC64A57029F21F165A96BDB59F0351C7C7D1769
2
04E8696E6424C21D717E46008780505D598EB59A
2
52fdb9f68c503e11d168fe52035901864c0a4861
2
cd3f0c85b158c08a2b113464991810cf2cdfc387
0
=== phase ===
optim pretest
=== output ===
ABC64A57029F21F165A96BDB59F0351C7C7D1769
5B38674EB4BD02CEC1D41C8DE3CC14A9872A2656
082178164865B8E6425C5955DEB97DDC21DEC6DC
E77812CE4574964BC144E3DB97953235B5A149DA
BD1B591F0B3E8FCC2035F9FB2043FAB18E0DBDDA
04E8696E6424C21D717E46008780505D598EB59A
ACM
2017
233
666
=== exitcode ===
0

!!*/