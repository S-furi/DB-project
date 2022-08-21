import json



f = open('comuni.txt', 'r')


righe = f.readlines()
w = open("./cities.json", "w")

w.write("[")
for i in range(len(righe)):
    riga = righe[i].split(" ")

    #print(riga[0:-2])
    
    #print(riga[-1][0:-1])

    chunk = {
        "nome" : riga[0:-2],
        "provincia" : riga[-2],
        "regione" : riga[-1][0:-1]
    }

    json.dump(chunk, w);
    w.write(",\n")
w.write("]")
w.close()