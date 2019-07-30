import random
alpha='abcdefghijklmnopqrstuvwxyz'
i=0
alphindex = dict();
while(i<26):
  alphindex[i] = alpha[i];
  i += 1

fopen = open("D:\\algorithm_design\\algo_asgnmnt1\\input_files\\vocab.txt", 'w')

for x in range(400000):
    word_length = random.randint(5,29)
    i = 0
    word = ""
    while(i<word_length):
        y=random.randint(0,25)
        word += str(alphindex.get(y))
        i += 1
    fopen.write(word + "\n")
fopen.close()