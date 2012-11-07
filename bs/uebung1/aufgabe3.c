#include <unistd.h>
#include <wait.h>
#include <stdio.h>
#include <string.h>

#define MAX_PARAM 	4096
#define MAX_PID		10

pid_t childs[MAX_PID] = {0};
unsigned int childsPos = 0;

int main(int argc, char** argv)
{
	const char* file = NULL;
	unsigned int i = 1;

	while (argv[i])
	{
		if (!strcmp(argv[i], "-f"))
			file = argv[i + 1];
		++i;
	}

	FILE* src = stdin;

	if (file)
	{
		src = fopen(file, "r");
		
		if (!src)
		{
			printf("Could not open file specified by -f switch\n");
			return 0;
		}
	}

	int c;	

	while ((c = fgetc(src)) != EOF && c != 'q')
	{
		char param[MAX_PARAM];

		switch (c)
		{
			case '\n':
				break;
			case 'x':
			case 'b':
			{
				char* result = fgets(param, sizeof(param), src);

				if (!result)
				{
					printf("Error reading parameter of command %c\n", c);
					fclose(src);
					return 0;
				}

				unsigned int len = strlen(result);
				
				if (result[len - 1] == '\n')
					result[len - 1] = '\0';

				++result;

				pid_t PID = fork();

				if (PID == -1)
				{
					printf("Error forking process\n", c);
					fclose(src);
 					return 0;
				}
				else if (!PID)
				{
					char* args[] = {result, NULL};
					execv(result, args);
					printf("execv error in child running %s\n", result);
					return 0;
				}
				else
				{
					if (c == 'x')
						waitpid(PID, NULL, 0);
					else
					{
						if (childsPos == MAX_PID)
							childsPos = 0;
						childs[childsPos] = PID;
						++childsPos;
					}
				}

				break;
			}
			case 'p':
			{
				unsigned int i;
				
				for (i = 0; i < MAX_PID; ++i)
				{
					if (childs[i])
						printf("PID: %d     still running: %s\n", childs[i], waitpid(childs[i], NULL, WNOHANG) ? "no" : "yes");
				}
				break;
			}

			default:
				printf("Unrecognized command %c\n", c);
		}
	}

	fclose(src);

	return 0;
}
