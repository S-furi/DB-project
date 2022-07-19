from codicefiscale import codicefiscale
import random
import datetime
import json

def getDate():
    start_date = datetime.date(1965, 1, 1)
    end_date = datetime.date(2000, 2, 1)
    time_between_dates = end_date - start_date
    days_between_dates = time_between_dates.days
    random_number_of_days = random.randrange(days_between_dates)
    random_date = start_date + datetime.timedelta(days=random_number_of_days)
    
    okdate = str(random_date.day) + "/" + str(random_date.month) + "/" + str(random_date.year)
    return okdate

def normalize_name(name:str) -> str:
    name = list(name)
    if not name[0].isupper():
        letter = name[0]
        name[0] = letter.upper()
    return "".join(name)



names = open("nomi.txt", "r").read().split()
surnames = open("cognomi.txt", "r").read().split()
cities = open("citta.txt", 'r').read().split()

w = open("./selected_names.json", "w")

name_num = 9118
surn_num = 726

w.write("[")
for j in range(50):
    
    i = random.randint(0, name_num-1)
    k = random.randint(0, surn_num-1)
    c = random.randint(0, 19)

    name : str = normalize_name(names[i])
    surname = normalize_name(surnames[k])
    city = cities[c]
    date = getDate()

    sex = "M" if name.endswith("o") else "F"
    cf = codicefiscale.encode(surname=surname, name=name, sex=sex, birthdate=date, birthplace=city)

    chunk = {
        "name" : name,
        "surname" : surname,
        "birhdate" : date,
        "CF" : cf
    }

    json.dump(chunk, w)
    w.write(",\n")
w.write("]")
w.close()

