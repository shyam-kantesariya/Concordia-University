fopen = open("D:\\shyam\\evaluation.txt")
marks=dict()
for line in fopen:
    tokens = line.strip("\n").split("\t")
    marks[tokens[0]] = tokens[1]
fopen.close()

fopen = open("D:\\shyam\\students.txt")
for line in fopen:
    line = line.strip("\n")
    print(line + "\t" + str(marks.get(line,0)))
fopen.close()