/**********************************************************************
* Student ID: 40042715
* Student Name: SHYAM KANTESARIYA
/**********************************************************************

/**********************************************************************
* REFERENCES:
* https://computing.llnl.gov/tutorials/mpi/samples/C/mpi_pi_reduce.c
* http://www.geeksforgeeks.org/how-to-measure-time-taken-by-a-program-in-c/
* https://stackoverflow.com/questions/14731751/mpi-comm-spawn-and-mpi-reduce
* http://etutorials.org/Linux+systems/cluster+computing+with+linux/Part+II+Parallel+Programming/Chapter+9+Advanced+Topics+in+MPI+Programming/
**********************************************************************/

#include "mpi.h"
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

void srandom (unsigned seed);
double dboard (int darts);
#define DARTS 5000000     /* number of throws at dartboard */
#define ROUNDS 100      /* number of times "darts" is iterated */

int main (int argc, char *argv[])
{
    double	homepi,         /* value of pi calculated by current task */
            pisum;
    int	taskid,	        /* task ID - also used as seed number */
        numtasks,       /* number of tasks */
        rc,             /* return code */
        i,
        darts_per_process, /* each process will devide the total no of darts in equal amount */
        remainder_darts_to_proc;

    MPI_Status status;
    MPI_Comm parentcomm;

    /* Obtain number of tasks and task ID */
    MPI_Init(&argc,&argv);

    MPI_Comm_get_parent(&parentcomm); /*Get comm value of Parent process*/
    MPI_Comm_rank( MPI_COMM_WORLD, &taskid );
    printf ("Child task %d has started with process id: %d \n", taskid, getpid());

    /* Set seed for random number generator equal to task ID */
    srandom (taskid);

    /* Get the num of task from argument to devide work among all child processes*/
    numtasks = atoi( argv[2]);
    darts_per_process = ((int)DARTS/numtasks) + (DARTS % numtasks);

    for (i = 0; i < ROUNDS; i++) {
        /* All tasks calculate pi using dartboard algorithm */
        homepi = dboard(darts_per_process);   
        /* Send the result back to Master process */
        rc = MPI_Reduce(&homepi, &pisum, 1, MPI_DOUBLE, MPI_SUM, 0, parentcomm);
    } 
    MPI_Finalize();
    printf("Child id %d exiting \n", getpid());
    return 0;
}

// subroutine dboard

double dboard(int darts)
{
    #define sqr(x)	((x)*(x))
    long random(void);
    double x_coord, y_coord, pi, r; 
    int score, n;
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
