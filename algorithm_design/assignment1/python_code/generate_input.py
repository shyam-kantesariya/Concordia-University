import random
alpha='abcdefghijklmnopqrstuvwxyz'
i=0
alphindex = dict();
while(i<26):
  alphindex[i] = alpha[i];
  i += 1

fopen = open("D:\\algorithm_design\\algo_asgnmnt1\\input_files\\sentence.txt", 'w')
sentence = ""
for x in range(20):
    i=0
    word = ""
    while(i<10):
        y=random.randint(0,25)
        word += str(alphindex.get(y))
        i += 1
    sentence += word + " "
fopen.write(sentence)
fopen.close()