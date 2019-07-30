import googlemaps
import pandas as pd
import sys

reload(sys)
sys.setdefaultencoding('utf8')
input_file = pd.ExcelFile("/home/s_kante/Downloads/BAPS_Montreal_Database.xlsx")
#print(input_file.sheet_names)
cust_df_org = input_file.parse("Master")
cust_df = cust_df_org [["Sr. No.", "First Name", "Last Name", "Street Address", "City", "Postal Code", "Telephone (Home)"]]
cc_nu_ghar = "36 Cardiff avenue, pointe-Claire, QC H9R 5S8"
sabha_hall = "1345 Rue Lapointe, Saint-Laurent, QC H4L 1K5"
print(cust_df.head())

#sys.exit(0)#sorted_cust_df = cust_df2.sort_values(by=['Prefered Finish Time'])
#cust_df["Address"] = cust_df["Street Address"] + "," + cust_df["Postal Code"] + "," + cust_df["City"]

gmaps = googlemaps.Client(key='')

#fopen = open("output.csv",'w')

with open("output.csv",'w') as fopen:
  for index, record in cust_df.iterrows():
    address = record["Street Address"].rstrip() + "," + record["Postal Code"] + "," + record["City"]
    print(address)
    try:
      directions_result = gmaps.distance_matrix(sabha_hall, address, mode='driving')
    except:
      final_str = str(record["Sr. No."]) + "," + str(record["First Name"]).replace(","," ") + " " + str(record["Last Name"]).replace(","," ") + "," + address.replace(",", " ")
      fopen.write(str(final_str) + "\n")
      #print("Add didn't find " + str(final_str))
      #sys.exit(0)
      continue
    try:
      #print("dir result "+ str(directions_result))
      data = directions_result.get('rows')[0]
      data2 = data.get('elements')[0]
      #print(str(data2))
      distance = str(data2.get('distance').get('text'))
      duration = data2.get('duration').get('text')

      instructions=""
#      for stop in data2.get('steps'):
#        instr = stop.get('html_instructions')
#        dist = stop.get('distance').get('text')
#        mode = stop.get('travel_mode')
#        instructions = instructions + "   " + mode + ":" + str(dist) + "  " + instr + "."
#      instructions=instructions.replace("<b>"," ").replace("</b>"," ").replace("</div>"," ").replace('<div style="font-size:0.9em\">'," ")

#      final_str = str(record["Sr. No."]).replace(","," ") + "," + str(record["First Name"]).replace(","," ") + " " + str(record["Last Name"]).replace(","," ") + "," + str(address).replace(","," ") + "," + str(distance).replace(","," ") + "," + str(duration).replace(","," ") + "," + instructions.replace(","," ")
      final_str = str(record["Sr. No."]).replace(","," ") + "," + str(record["First Name"]).replace(","," ") + " " + str(record["Last Name"]).replace(","," ") + "," + str(address).replace(","," ") + "," + str(distance).replace(","," ") + "," + str(duration).replace(","," ")
      fopen.write(str(final_str) + "\n")
      #print(final_str)
    except:
      final_str = str(record["Sr. No."]).replace(","," ") + "," + str(record["First Name"]).replace(","," ") + " " + str(record["Last Name"]).replace(","," ") + "," + address.replace(","," ")
      print str(final_str)
      print("Add found but parsing failed " + final_str)
    #print final_str
    #sys.exit(0)
    #sys.exit(0)
#fopen.close()