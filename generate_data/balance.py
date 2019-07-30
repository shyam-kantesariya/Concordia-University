ac=dict()
with open("transactions.csv") as f:
  for line in f:
    data = line.split(",")
    if ac.get(data[0]) == None:
      ac[data[0]] = int(data[1])
#      print ac.get(data[0])
    else:
      ac[data[0]] = ac.get(data[0]) + int(data[1])
for key in ac.keys():
  print str(key) + "," + str(ac.get(key))
