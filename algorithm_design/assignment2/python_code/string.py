import random
alpha='ABCDEFGHIJKLMNOPQRSTUVWXYZ'
i=0
alphindex = dict();
while(i<26):
  alphindex[i] = alpha[i];
  i += 1

fopen = open("D:\\gitrepo\\concordia\\algorithm_design\\algo_asgnmnt2\\input\\output.txt", 'w')
sentence = ""

word = ""
for x in range(400000):
    y=random.randint(0,25)
    word += str(alphindex.get(y))
fopen.write(word)
fopen.close()