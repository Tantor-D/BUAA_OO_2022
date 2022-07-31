#include<stdio.h>
#include<stdlib.h>
#include<time.h>
int main()
{
	FILE *out;
	out=fopen("statistic5.in","w");
	int N,n;
	srand(time(0));
	N=4+rand()%147;
//	N=4;
	fprintf(out,"%d\n",N);
	for(int i=1;i<=N/3;i++){
		fprintf(out,"A %d\n",rand()%101);
	}
	
	for(int i=1;i<=N/3;i++){
		fprintf(out,"B %d\n",rand()%101);
	}

	for(int i=1;i<=N-N/3*2;i++){
		fprintf(out,"C %d\n",rand()%101);
	}
	fclose(out);
	return 0;
}
