class older{
	int age;
	void solveAge(){
		int i;
		int j;
		int k;
		for(i = 1; i < 100; i++)
			for(j = i + 1; j < 100; j++)
				for(k = j + 1; k < 100; k++)
					if((i - i + i + j - j + j + k - k + k) * (100 * i + 10 * j + k) % 1926 == 0) age = (i - i + i + j - j + j + k - k + k) * (100 * i + 10 * j + k);
	}
}
int main(){
	older mrJiang = new older;
	mrJiang.solveAge();
	if(mrJiang.age > 10000)println("eternal!");
    return 0;
}
