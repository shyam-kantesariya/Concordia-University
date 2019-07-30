/**********************************************************************
* Student ID: 40042715
* Student Name: SHYAM KANTESARIYA
/**********************************************************************

/**********************************************************************
* REFERENCES:
* https://computing.llnl.gov/tutorials/mpi/samples/C/mpi_pi_reduce.c
* http://www.geeksforgeeks.org/how-to-measure-time-taken-by-a-program-in-c/
* https://stackoverflow.com/questions/14731751/mpi-comm-spawn-and-mpi-reduce
* Usage: mpirun -np 1 dynamic_child_master <number-of-workers>
* http://etutorials.org/Linux+systems/cluster+computing+with+linux/Part+II+Parallel+Programming/Chapter+9+Advanced+Topics+in+MPI+Programming/
**********************************************************************/

#include "mpi.h"
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>

#define ROUNDS 100      /* number of times "darts" is iterated */
#define DARTS 5000000
int main (int argc, char *argv[])
{
    double	homepi=0,
            pisum,	        /* sum of tasks' pi values */
            pi,	        /* average of pi after "darts" is thrown */
            avepi;	        /* average pi value for all iterations */
    int numtasks, i, rc;
    clock_t start_time,
            end_time;

    MPI_Status status;
    MPI_Comm child_processes;

    /* Note down the start time */
    start_time = clock();

    /* Obtain number of tasks and task ID */
    MPI_Init(&argc,&argv);
    printf ("Parent has started with process id: %d \n",getpid() );

    if ( argc != 2 )
        printf( "usage: %s <number of workers>\n", argv[0] );
    else
        numtasks = atoi( argv[1] );

    avepi = 0;

    MPI_Comm_spawn( "dynamic_child_worker", argv, numtasks, MPI_INFO_NULL, 0, MPI_COMM_SELF, &child_processes, MPI_ERRCODES_IGNORE );

    for (i = 0; i < ROUNDS; i++) {
        /* Only child processes will participate in Reduce operation */
        rc = MPI_Reduce(&homepi, &pisum, 1, MPI_DOUBLE, MPI_SUM, MPI_ROOT, child_processes);
        pi = pisum/numtasks;
        avepi = ((avepi * i) + pi)/(i + 1); 
        printf("   After %8d throws, average value of pi = %10.8f\n", (DARTS * (i + 1)),avepi);
    } 

    printf ("\nReal value of PI: 3.1415926535897 \n");

    MPI_Finalize();
    end_time = clock();
    printf("Master %d is exiting \n", getpid());
    printf("Total time elapsed is %f seconds\n", ((double)(end_time - start_time))/CLOCKS_PER_SEC);
    return 0;
}
