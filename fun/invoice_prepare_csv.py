import sys

fname = sys.argv[1] 

def clean_tpls(tpls):
  final_str = ""
  item =  " "
  try:
    item_num = int(tpls[0].split(" ")[0])
    if len(item_num) == 6:
      for ele in tpls[0].split(" ")[1:]:
        item += " " + ele
    else:
      item = tpls[0]
  except:
    item = tpls[0]
  final_str = item.strip(" ") + "," + tpls[1].replace("UN","").strip(" ") + "," + tpls[2].strip(" ") \
              + "," + tpls[3].strip(" ") + "," + tpls[4].strip(" ") + "," + tpls[5].strip(" ")
  print final_str
with open(fname) as f:
  for line in f:
    if " UN " in line:
      tpls = line.strip("\n").split("  ")
      #print str(line.strip("\n").split("  "))
      cntr=0
      rc = []
      tpls_len = len(tpls)
      while (cntr < tpls_len):
        if tpls[cntr] not in ("","*"):
          rc.append(tpls[cntr])
        cntr += 1
      print str(rc)
#      clean_tpls(rc)
