import glob
submissions_path='D:\shyam\COMP5461-DD-4'
evaluation_path='D:\shyam\evaluation.txt'
submissions = glob.glob('D:\shyam\COMP5461-DD-4\*\*\*')
fopen=open(evaluation_path)
marks=dict()
for result in fopen:
    tokens=result.split("\t")
    marks[tokens[0]] = tokens[1]
fopen.close()
id_dir = dict()
for submission in submissions:
    tokens = submission.split("\\")
    id=tokens[3].split("-")[0]
    id_dir[id] = submission

for key in id_dir.keys():
    resultfile = open(id_dir.get(key)+"\\mark.txt",'w')
    resultfile.write(marks.get(key, "Not evaluated, Contact TA"))
    resultfile.close()