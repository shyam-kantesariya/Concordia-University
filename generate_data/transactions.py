import sys,os
import random as rand
for x in range(5):
  for tx in range(100):
    amt = rand.randint(-10000,10000)
    print str(tx) + "," + str(amt)
