/**********************************************************************
* Student ID: 40042715
* Student Name: SHYAM KANTESARIYA
/**********************************************************************

/**********************************************************************
* REFERENCES:
* http://www.geeksforgeeks.org/how-to-measure-time-taken-by-a-program-in-c/
* https://computing.llnl.gov/tutorials/mpi/samples/C/mpi_pi_reduce.c
**********************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include <time.h>

void srandom (unsigned seed);
double dboard (int darts);
#define DARTS 5000000     /* number of throws at dartboard */
#define ROUNDS 100      /* number of times "darts" is iterated */

int main (int argc, char *argv[])
{
      clock_t t;
      t = clock();
      double pi,        /* average of pi after "darts" is thrown */
            avepi;     /* average pi value for all iterations */
      int	 i;

      /* Set seed for random number generator equal to task ID */
      srandom (1);

      avepi = 0;
      for (i = 0; i < ROUNDS; i++) {
            /* All tasks calculate pi using dartboard algorithm */
            pi = dboard(DARTS);
            avepi = ((avepi * i) + pi)/(i + 1); 
            printf("   After %8d throws, average value of pi = %10.8f\n",
            (DARTS * (i + 1)),avepi);  
      } 

      printf ("\nReal value of PI: 3.1415926535897 \n");
      t = clock() - t;
      printf("Time taken is %f seconds \n", ((double)t)/CLOCKS_PER_SEC);
      return 0;
}



/**************************************************************************
* subroutine dboard
* DESCRIPTION:
*   Used in pi calculation example codes. 
*   See mpi_pi_send.c and mpi_pi_reduce.c  
*   Throw darts at board.  Done by generating random numbers 
*   between 0 and 1 and converting them to values for x and y 
*   coordinates and then testing to see if they "land" in 
*   the circle."  If so, score is incremented.  After throwing the 
*   specified number of darts, pi is calculated.  The computed value 
*   of pi is returned as the value of this function, dboard. 
*
*   Explanation of constants and variables used in this function:
*   darts       = number of throws at dartboard
*   score       = number of darts that hit circle
*   n           = index variable
*   r           = random number scaled between 0 and 1
*   x_coord     = x coordinate, between -1 and 1
*   x_sqr       = square of x coordinate
*   y_coord     = y coordinate, between -1 and 1
*   y_sqr       = square of y coordinate
*   pi          = computed value of pi
****************************************************************************/

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
