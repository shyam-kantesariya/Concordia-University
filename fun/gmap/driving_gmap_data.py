#pip install -U googlemaps
#https://github.com/googlemaps/google-maps-services-python

import googlemaps
import pandas as pd
import sys

reload(sys)
sys.setdefaultencoding('utf8')
sheet = 'DDO'
input_file = pd.ExcelFile("/home/s_kante/Downloads/BAPS_Montreal_Database.xlsx")
#print(input_file.sheet_names)
cust_df_org = input_file.parse(sheet)
cust_df = cust_df_org [["Sr. No.", "First Name", "Last Name", "Street Address", "City", "Postal Code", "Telephone (Home)"]]
print(cust_df.head())

#sys.exit(0)#sorted_cust_df = cust_df2.sort_values(by=['Prefered Finish Time'])
#cust_df["Address"] = cust_df["Street Address"] + "," + cust_df["Postal Code"] + "," + cust_df["City"]

gmaps = googlemaps.Client(key='')

fopen = open("output.csv",'w')

for index, record in cust_df.iterrows():
	address = record["Street Address"].rstrip() + "," + record["Postal Code"] + "," + record["City"]
	print address
        try:
	  directions_result = gmaps.directions('36 Cardiff avenue, pointe-Claire, QC H9R 5S8', address, mode='driving')
	except:
	  final_str = str(record["Sr. No."]).replace(","," ") + "," + str(record["First Name"]).replace(","," ") + " " + str(record["Last Name"]).replace(","," ") + "," + address.replace(",", " ")
	  fopen.write(str(final_str) + "\n")
          continue

	try:
	  data = directions_result[0]
	  data2 = data.get('legs')[0]

	  distance = str(data2.get('distance').get('text'))

	  duration = data2.get('duration').get('text')

	  instructions=""
	  for stop in data2.get('steps'):
	    instr = stop.get('html_instructions')
	    dist = stop.get('distance').get('text')
	    mode = stop.get('travel_mode')
	    instructions = instructions + "   " + mode + ":" + str(dist) + "  " + instr + "."
	  instructions=instructions.replace("<b>"," ").replace("</b>"," ").replace("</div>"," ").replace('<div style="font-size:0.9em\">'," ")

	  final_str = str(record["Sr. No."]).replace(","," ") + "," + str(record["First Name"]).replace(","," ") + " " + str(record["Last Name"]).replace(","," ") + "," + str(address).replace(","," ") + "," + str(distance).replace(","," ") + "," + str(duration).replace(","," ") + "," + instructions.replace(","," ")
	  fopen.write((final_str) + "\n")

	except:
	  final_str = str(record["Sr. No."]).replace(","," ") + "," + str(record["First Name"]).replace(","," ") + " " + str(record["Last Name"]).replace(","," ") + "," + address.replace(","," ")
          fopen.write(str(final_str) + "\n")

	#print final_str
	#sys.exit(0)


fopen.close()