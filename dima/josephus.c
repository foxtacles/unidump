/*	
	Calculates f(n) of the "josephus problem"
	date: 14th november 2011
	author: recycler@brickster.net (Christian Semmler)
*/

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int main(int argc, const char* argv[])
{
	if (argc > 1)
	{
		int n = atoi(argv[1]);

		if (n > 0)
		{
			int q = (log(n) / log(2)); // calculate log2(n), floating-point gets truncated
			q = (n - pow(2, q)); // n - 2powQ
			q *= 2; // q * 2
			q++; // q + 1
			printf("josephus f(%d) : %d\n", n, q);
		}		
	}

	return 0;
} 


