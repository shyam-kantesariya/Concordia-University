from cStringIO import StringIO
from pdfminer.pdfinterp import PDFResourceManager, PDFPageInterpreter
from pdfminer.converter import TextConverter
from pdfminer.layout import LAParams
from pdfminer.pdfpage import PDFPage
import os
import sys, getopt
import textract as tx



fname=sys.argv[1]

#Process using PDFMiner

pagenums = set()

output = StringIO()
manager = PDFResourceManager()
converter = TextConverter(manager, output, laparams=LAParams())
interpreter = PDFPageInterpreter(manager, converter)

infile = file(fname, 'rb')
for page in PDFPage.get_pages(infile, pagenums):
  interpreter.process_page(page)

infile.close()
converter.close()
text = output.getvalue()
output.close

with open("pdfminer_output.txt","w") as f:
  f.write(text)

#Process using textract

text = tx.process("myfile.pdf")
with open("textract_output.txt","w") as f:
  f.write(text)
