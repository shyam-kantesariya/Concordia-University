/**********************************************************************
* Student ID: 40042715
* Student Name: SHYAM KANTESARIYA
***********************************************************************

**********************************************************************
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
#include <math.h>
#include <time.h>

#define GRANULARITY 4
#define INITIAL_DATA 0
#define PIVOT 1
#define INTERMEDIATE_BLOCK_SIZE 2
#define INTERMEDIATE_SMALLER_BLOCK 3
#define INTERMEDIATE_LARGER_BLOCK 4

void srandom (unsigned seed);
void get_random_elements(int taskid, int* elements);
int * int_to_dec(int x, int dim);
int *exchange(int *elements, int ele_size, int pivot, int myid, int partnerid, int indexbit);
int get_leader(int *binary_taskid, int dimension, int iteration);
int get_pivot(int taskid, int group_leader, int *binary_taskid, int iteration, int *elements, int ele_size);
int exit_routine(int exit_code, clock_t start_time);

int main (int argc, char *argv[])
{
    int dimension, numtasks, i, j, data_chunk[GRANULARITY], taskid, *elements, group_leader, *binary_taskid, 
    ele_size=GRANULARITY, pivot, ele_iter, indexbitoffset=0;
    clock_t start_time;
    MPI_Status status;
    MPI_Comm child_processes;

    /* Note down the start time */
    start_time = clock();

    /* Obtain number of tasks and task ID */
    MPI_Init(&argc,&argv);
    //printf ("Parent has started with process id: %d \n",getpid() );
    MPI_Comm_rank( MPI_COMM_WORLD, &taskid );
    
    if ( argc != 2 ){
        printf( "usage: %s <d-dimension of hypercube>\n", argv[0] );
        return exit_routine(1,start_time);
    } else {
        dimension = atoi( argv[1] );
        numtasks = pow(2,dimension);
    }
    
    elements = malloc(GRANULARITY * sizeof(int));
    get_random_elements(taskid, elements);
    
    //Get binary format ID
    binary_taskid = int_to_dec(taskid, dimension);
    printf ("Task %d has started with process id %d and Hypercube coordinate ", taskid, getpid());
    for(i=0; i<dimension; i++)
    {
        printf("%d",*(binary_taskid+i));
    }
    printf(" and elements ");
    for(i=0; i<GRANULARITY; i++)
        printf("%d  ", *(elements + i));
    printf("\n");

	
    for (i=dimension-1; i>=0; i--){
        group_leader = get_leader(binary_taskid, dimension, i);
        pivot = get_pivot(taskid, group_leader, binary_taskid, i, elements, ele_size);
        int partnerid=0;
        for(j=0; j<dimension; j++){
            if (i==(dimension-1-j))
                partnerid = partnerid + (((1 + *(binary_taskid + j))%2) * pow(2,dimension-1-j));
            else 
                partnerid = partnerid + (*(binary_taskid + j) * pow(2,dimension-1-j));
        }
if(taskid==4)
  printf(" I am 4 and my elements val is %d \n", elements);
         elements = exchange(elements, ele_size, pivot,taskid, partnerid, *(binary_taskid + indexbitoffset++));
		 ele_size = *elements;
		 elements = &elements[1];
if(taskid==4)
  printf(" I am 4 and my elements val is %d \n", elements);

printf("During round %d task %d has exchanged elements with %d as pivot was %d. Currently task %d is holding elements: ", i, taskid, partnerid, pivot, taskid);
		for(ele_iter=0; ele_iter<ele_size; ele_iter++)
			printf("%d  ", *(elements+ele_iter));
        printf("\n");
 MPI_Finalize();
exit(0);
    }
    return exit_routine(0, start_time);
}

int get_pivot(int taskid, int group_leader, int *binary_taskid, int iteration, int *elements, int ele_size){
    int pivot, i, j, maxid=taskid;
    if(taskid == group_leader){
        srandom (taskid);
        if(ele_size>0)
            pivot = *(elements + (random()%ele_size));
        else
            pivot = random()%71;
        for(i=iteration; i>=0; i--){
            maxid = maxid + pow(2,i);
        }
        for(i=taskid+1; i<=maxid; i++){
            MPI_Send(&pivot, 1, MPI_INT, i, PIVOT, MPI_COMM_WORLD);
        }
    } else {
        MPI_Recv(&pivot, 1, MPI_INT, group_leader, PIVOT, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
    }
    return pivot;
}

int get_leader(int *binary_taskid, int dimension, int iteration){
    int leaderid = 0, i, j=dimension-iteration;
    for(i=iteration+1; i<dimension; i++){
        leaderid = *(binary_taskid + --j) * pow(2,i);
    }
    return leaderid;
}

void get_random_elements(int taskid, int* elements){
    int i;
    srandom (taskid);
    for(i=0; i<GRANULARITY; i++){
        elements[i] = random()%71;
    }
}

int *exchange(int *elements, int ele_size, int pivot, int myid, int partnerid, int indexbit){
    int smaller=0, larger=0, iter=0, i=0, j=0, received_larger=0, received_smaller=0, *received_elements;
    for(i=0; i<ele_size; i++){
        if (*(elements+i) > pivot)
            larger++;
        else
            smaller++;
    }
    
    int smaller_ele[smaller], larger_ele[larger];
    i=0;
    j=0;
    for(iter=0; iter<ele_size; iter++){
        if (*(elements+iter) > pivot)
	    larger_ele[i++] = *(elements+iter);
        else
            smaller_ele[j++] = *(elements+iter);
    }
    free(elements);
    if(indexbit==1){
if(myid==4)
{
	printf("i am %d and talking to %d sending %d smaller elements \n", myid, partnerid, smaller);
int x;
for(x=0;x<smaller;x++)
  printf("task 4: %d ", smaller_ele[x]);
printf("\n");
}

        MPI_Send(&smaller, 1, MPI_INT, partnerid, INTERMEDIATE_BLOCK_SIZE, MPI_COMM_WORLD);
        if(smaller>0)
            MPI_Send(smaller_ele, smaller, MPI_INT, partnerid, INTERMEDIATE_SMALLER_BLOCK, MPI_COMM_WORLD);
        MPI_Recv(&received_larger, 1, MPI_INT, partnerid, INTERMEDIATE_BLOCK_SIZE, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        if(received_larger>0){
            received_elements = malloc(received_larger * sizeof(int));
            MPI_Recv(received_elements, received_larger, MPI_INT, partnerid, INTERMEDIATE_LARGER_BLOCK, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
            ele_size = received_larger + larger;
            elements = malloc((ele_size+1) * sizeof(int));
			elements[0] = ele_size;
            for(i=1;i<=larger;i++)
                elements[i] = larger_ele[i];
            j=0;
            for(i=larger+1;i<=ele_size;i++)
                elements[i] = *(received_elements + j++);
if(myid==4){
printf("task %d elements: ", myid);
int ele_iter;
                for(ele_iter=0; ele_iter<ele_size; ele_iter++)
                        printf("%d  ", *(elements+ele_iter));
        printf("\n");
}

        }
    } else {
        MPI_Recv(&received_smaller, 1, MPI_INT, partnerid, INTERMEDIATE_BLOCK_SIZE, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        if (received_smaller>0){
            received_elements = malloc(received_smaller * sizeof(int));
            MPI_Recv(received_elements, received_smaller, MPI_INT, partnerid, INTERMEDIATE_SMALLER_BLOCK, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
            ele_size = received_smaller + smaller;
            elements = malloc((ele_size+1) * sizeof(int));
			elements[0] = ele_size;
            for(i=1;i<=smaller;i++)
                elements[i] = smaller_ele[i];
            j=0;
            for(i=smaller+1;i<=ele_size;i++)
                elements[i] = *(received_elements + j++);
if(myid==0){
printf("task %d elements: ", myid);
int ele_iter;
                for(ele_iter=0; ele_iter<ele_size; ele_iter++)
                        printf("%d  ", *(elements+ele_iter));
        printf("\n");
}
  
        }
        MPI_Send(&larger, 1, MPI_INT, partnerid, INTERMEDIATE_BLOCK_SIZE, MPI_COMM_WORLD);
        if(larger>0)
            MPI_Send(larger_ele, larger, MPI_INT, partnerid, INTERMEDIATE_LARGER_BLOCK, MPI_COMM_WORLD);
    }

if(myid==4)
  printf(" I am 4 and my elements val is %d \n", elements);

    return elements;
}

int * int_to_dec(int x, int dim){
    int *result = malloc(dim * sizeof(*result));
    int i=dim-1;
    while(x>0){
        result[i] = x%2;
        x=(int)(x/2);
        i--;
    }
    while(i>=0){
        result[i] = 0+0;
        i--;
    }
    return result;
}

int exit_routine(int exit_code, clock_t start_time){
    MPI_Finalize();
    clock_t end_time = clock();
    printf("Master %d is exiting \n", getpid());
    printf("Total time elapsed is %f seconds\n", ((double)(end_time - start_time))/CLOCKS_PER_SEC);
    return exit_code;
}
