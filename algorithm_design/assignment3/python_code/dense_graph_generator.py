import sys;
import random;
size=10
fopen = open("D:\\gitrepo\\concordia\\algorithm_design\\algo_asgnmnt3\\input\\links.txt", 'w')
i=0
j=0
while(j<size):
    while(i<size):
        #y=random.randint(0,4)
        if(i!=j):
            fopen.write(str(j)+","+ str(i) + "\n")
        i += 1
    i=0
    print (str(j))
    j += 1

fopen.close()