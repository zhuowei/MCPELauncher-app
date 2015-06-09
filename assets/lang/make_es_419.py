import os

dictionary = dict()

def parseLine(line):
	splitIdx = line.find('=')

	key = line[:splitIdx]
	value = line[splitIdx+1:]

	if not key in dictionary:
		dictionary[key] = list()

	dictionary[key].append(value)

def parseFile(path):
	with open(path, encoding="utf8") as f:
		for line in f:
			parseLine(line)

root = "pc-base/latin_america/"

for filename in os.listdir(root):
	parseFile(root+filename)

#now count the most used ones and stitch them together through voting
f = open("pc-base/es_419.lang", encoding="utf8", mode='w')
for key, value in dictionary.items():
	if len(value) == 1:
		f.write(key + "=" + value[0])
		pass
	else:
		d = dict()
		for v in value:
			if not v in d:
				d[v] = 1
			else:
				d[v] = d[v] + 1

		sortedd = ((k, d[k]) for k in sorted(d, key=d.get, reverse=True))
		for k, v in sortedd:
			f.write(key + "=" + k)
			break



