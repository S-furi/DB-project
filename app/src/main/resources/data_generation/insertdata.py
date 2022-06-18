import json
import random

def generate_mail(name : str, surname : str) -> str:
    domains = ["gmail.com", "libero.it", "outlook.com", "alice.it", "aruba.com", "icloud.com", "live.it", "hotmail.it", "virgilio.it", "yahoo.com"]
    i = random.randint(0, len(domains)-1)
    j = random.randint(1,9)
    return name + "." + surname + str(j) + "@" + domains[i]
def generate_tel():
    return random.randint(3110000000, 3999999999)

w = open("db_data.json", 'w')
w.write("[")
with open("selected_names.json", 'r') as f:
    people = json.load(f)
    for i in range(len(people)):
        person = people[i]
        person["email"] = generate_mail(person.get('name'), person.get('surname'))
        person["telphone"] = generate_tel()
        w.write(json.dumps(person))
        w.write(',\n')
        
w.write("]")
w.close()