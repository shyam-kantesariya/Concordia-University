import sys;
import random;
size=1000000
fopen = open("D:\\gitrepo\\concordia\\algorithm_design\\algo_asgnmnt3\\input\\links.txt", 'w')
i=0
while(i<size):
    fopen.write(str(i)+","+ str(i+1) + "\n")
    i += 1
for x in range(size):
    y=random.randint(1,size)
    m=y=random.randint(y,size)
    fopen.write(str(y) + "," + str(m) + "\n")
fopen.close()