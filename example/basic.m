int main()
{
	int a;
	int b;
	int c;
	int d;
	int e;
	int f;
	int g;
	int n = getInt();
	int ans = 0;
	for(a = 0; a < n; ++a)
		for(b = 0; b < n; ++b)
			for(c = 0; c < n; ++c)
				for(d = 0; d < n; ++d)
					for(e = 0; e < n; ++e)
						for(f = 0; f < n; ++f)
							for(g = 0; g < n; ++g)
							{
								int t1 = (((a ^ b) & c - d) | (d ^ e & f - d & g - d) | (a ^ g) | (d - d & f - d));
								int t2 = (((a ^ b) & c - d) | (d ^ e & f - d & g - d) | (a ^ g) | (d - d & f - d));
								int t3 = (((a ^ b) & c - d) | (d ^ e & f - d & g - d) | (a ^ g) | (d - d & f - d));
								int t4 = (((a ^ b) & c - d) | (d ^ e & f - d & g - d) | (a ^ g) | (d - d & f - d));
								int t5 = (((a ^ b) & c - d) | (d ^ e & f - d & g - d) | (a ^ g) | (d - d & f - d));
								int t6 = (((a ^ b) & c - d) | (d ^ e & f - d & g - d) | (a ^ g) | (d - d & f - d));
								if(t1 > 0)
									ans++;
								if(t2 > 0)
									ans++;
								if(t3 > 0)
									ans++;
								if(t4 > 0)
									ans++;
								if(t5 > 0)
									ans++;
								if(t6 > 0)
									ans++;
							}
	print(toString(ans));
	return 0;
}



/*!! metadata:
=== comment ===
5140309234-xietiancheng Common Expression Elimination
=== is_public ===
True
=== assert ===
output
=== timeout ===
5.0
=== input ===
14
=== phase ===
optim extended
=== output ===
559355322
=== exitcode ===


!!*/
