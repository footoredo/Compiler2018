int main()
{
    int k;
    k = getInt();
    print("p cnf ");
    println(toString(k * 2 + 1) + " " + toString(k));
    int i;
    int last;
    last = 1;
    string t;
    for(i = 0; i < k; ++i)
    {
        t = toString(last) + " " + toString(last + 1) + " " + toString(-(last + 2));
        if(i % 100000 == 0)
        {
            println(t);
        }
        last = last + 2;
    }
    return 0;
}



/*!! metadata:
=== comment ===
print_cnf-5140309234-xietiancheng.txtprint a 3-cnf, fast built in function
=== is_public ===
True
=== assert ===
output
=== timeout ===
1.5
=== input ===
1000000
=== phase ===
optim pretest
=== output ===
p cnf 2000001 1000000
1 2 -3
200001 200002 -200003
400001 400002 -400003
600001 600002 -600003
800001 800002 -800003
1000001 1000002 -1000003
1200001 1200002 -1200003
1400001 1400002 -1400003
1600001 1600002 -1600003
1800001 1800002 -1800003
=== exitcode ===


!!*/
