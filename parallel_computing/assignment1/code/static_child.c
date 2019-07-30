/**********************************************************************
* Student ID: 40042715
* Student Name: SHYAM KANTESARIYA
/**********************************************************************

/**********************************************************************
* REFERENCES:
* http://www.geeksforgeeks.org/how-to-measure-time-taken-by-a-program-in-c/
* https://computing.llnl.gov/tutorials/mpi/samples/C/mpi_pi_reduce.c
**********************************************************************/

#include "mpi.h"
#include <stdio.h>
#include <stdlib.h>
#include <time.h>

void srandom (unsigned seed);
double dboard (int darts);
#define DARTS 5000000   /* number of throws at dartboard */
#define ROUNDS 100      /* number of times "darts" is iterated */
#define MASTER 0        /* task ID of master task */

int main (int argc, char *argv[])
{
        double	homepi,         /* value of pi calculated by current task */
                pisum,	        /* sum of tasks' pi values */
                pi,	        /* average of pi after "darts" is thrown */
                avepi;	        /* average pi value for all iterations */

        int	taskid,	        /* task ID - also used as seed number */
                numtasks,       /* number of tasks */
                rc,             /* return code */
                i,
                darts_per_process, /* each process will devide the total no of darts in equal amount */
                remainder_darts_to_proc;

        clock_t start_time, 
                end_time;

        MPI_Status status;

        // Measure the start time of program
        start_time = clock();

        /* Obtain number of tasks and task ID */
        MPI_Init(&argc,&argv);
        MPI_Comm_size(MPI_COMM_WORLD,&numtasks);
        MPI_Comm_rank(MPI_COMM_WORLD,&taskid);
        printf ("MPI task %d has started...\n", taskid);

        /* Set seed for random number generator equal to task ID */
        srandom (taskid);

        avepi = 0;
        darts_per_process = (int)DARTS/numtasks;
        remainder_darts_to_proc = DARTS % numtasks;

        for (i = 0; i < ROUNDS; i++) {
                /* All tasks calculate pi using dartboard algorithm */
                if (taskid != MASTER){
                        homepi = dboard(darts_per_process + remainder_darts_to_proc);
                }
                else {
                        homepi = dboard(darts_per_process);
                }

                /* Use MPI_Reduce to sum values of homepi across all tasks 
                * Master will store the accumulated value in pisum 
                * - homepi is the send buffer
                * - pisum is the receive buffer (used by the receiving task only)
                * - the size of the message is sizeof(double)
                * - MASTER is the task that will receive the result of the reduction
                *   operation
                * - MPI_SUM is a pre-defined reduction function (double-precision
                *   floating-point vector addition).  Must be declared extern.
                * - MPI_COMM_WORLD is the group of tasks that will participate.
                */

                rc = MPI_Reduce(&homepi, &pisum, 1, MPI_DOUBLE, MPI_SUM, MASTER, MPI_COMM_WORLD);

                /* Master computes average for this iteration and all iterations */
                if (taskid == MASTER) {
                        pi = pisum/numtasks;
                        avepi = ((avepi * i) + pi)/(i + 1); 
                        printf("   After %8d throws, average value of pi = %10.8f\n", (DARTS * (i + 1)),avepi);
                }    

        } 
        if (taskid == MASTER) 
        printf ("\nReal value of PI: 3.1415926535897 \n");

        MPI_Finalize();

        if (taskid == MASTER) {
                end_time = clock();
                printf("Time taken is %f seconds \n", ((double)(end_time - start_time))/CLOCKS_PER_SEC);
        }
        return 0;
}

// subroutine dboard

double dboard(int darts)
{
        #define sqr(x)	((x)*(x))
        long random(void);
        double x_coord, y_coord, pi, r; 
        int score, n, proc_id;
        unsigned int cconst;  /* must be 4-bytes in size */
        /*************************************************************************
        * The cconst variable must be 4 bytes. We check this and bail if it is
        * not the right size
        ************************************************************************/
        if (sizeof(cconst) != 4) {
                printf("Wrong data size for cconst variable in dboard routine!\n");
                printf("See comments in source file. Quitting.\n");
                exit(1);
        }
        /* 2 bit shifted to MAX_RAND later used to scale random number between 0 and 1 */
        cconst = 2 << (31 - 1);
        score = 0;

        /* "throw darts at board" */
        for (n = 1; n <= darts; n++)  {
                /* generate random numbers for x and y coordinates */
                r = (double)random()/cconst;
                x_coord = (2.0 * r) - 1.0;
                r = (double)random()/cconst;
                y_coord = (2.0 * r) - 1.0;

                /* if dart lands in circle, increment score */
                if ((sqr(x_coord) + sqr(y_coord)) <= 1.0)
                        score++;
        }

        /* calculate pi */
        pi = 4.0 * (double)score/(double)darts;      
        return(pi);
} 
