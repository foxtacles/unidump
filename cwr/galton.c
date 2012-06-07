/*	
	Simulates "Galtonsches Nagelbrett"
	date: 24th december 2011
	author: recycler@brickster.net (Christian Semmler)
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

int* galton(int depth, int rounds, float leftChance);
const int BIRTHDAY_N = 14;

int main(int argc, const char* argv[])
{
	if (argc > 3)
	{
		// get input: depth of tree, number of rounds, left probability of an element
		int n = atoi(argv[1]);
		int j = atoi(argv[2]);
		float k = atof(argv[3]);

		int* result = galton(n, j, k); // simulate galton

		if (result == NULL)
			printf("You suck.\n"); // wrong input
		else
		{
			for (int i = 0; i < n + 1; i++) // output the result
				printf("We have %d cakes in box #%d\n", result[i], i + 1);

			free(result);
		}
	}

	return 0;
}

int* galton(int depth, int rounds, float leftChance)
{
	if (depth > 0 && rounds > 0 && leftChance >= 0.0 && leftChance <= 1.0)
	{
		int* result = calloc(depth + 1, 4); // result boxes
		char dots[(int) ((float) (0.5 * depth) * (depth + 1))]; // nodes
		srand(time(NULL)); // initialize random

		for (int i = 0; i < rounds; i++)
		{
			int pos = 0;
			memset(dots, 0, sizeof(dots));
			dots[0] = 1;

			for (int j = 0; j < depth; j++)
			{
				if (depth - BIRTHDAY_N == j) // 100% left if we are in depth - BIRTHDAY_N row
				{
					pos += j + 1;

					if (j != depth - 1)
						dots[pos] = 1;
					else
						result[pos - sizeof(dots)]++;

					continue;
				}

				int r = rand();
				int leftHalf = RAND_MAX * leftChance; // left part
				int rightHalf = RAND_MAX - leftHalf; // right part
			
				if (r < leftHalf)
					pos += j + 1; // left event
				else
					pos += j + 2; // right event
				
				if (j != depth - 1)
					dots[pos] = 1; // mark the dot as passed (can be used for visual representations)
				else 
					result[pos - sizeof(dots)]++; // element in result box
			}
		}

		return result;
	}

	return NULL;
}


