# DB-project
Database Project for Database course, University of Bologna
- [DB-project](#db-project)
    - [**CODING**](#coding)
    - [**SCHEMA**](#schema)
    - [**REPORT**](#report)

Robe da far subito:
- [ ] Decidere come e con cosa disegnare per il [Report](#report) schemi e tabelle
- [x] Tradurre tutto il codice python in java (? serve ?)
### **CODING**
- [x] Implement pathfind for railways  
- [ ] Choose number/which stations to put into JSONs
- [ ] Clean **local code** for calculating time and routes:
  - [ ] Merge all various operation in one single file in order to accomplish these requirements:
  - running the executable with a name of two stations does:
     - Taken two station  names as parameter:
     - Retreive stations id's from trenitalia's APIs (if they're not already into `/railway/route_data/statID.json`)
     - Find the shortest route from Trenitalia's solutions
     - Add the new entries into routes file (`/railway/route_data/routes_distances_diff.json`).
     - (*optional*): use Google APIs to get stations' distances.
     - Save and push changes (maybe a github action for all of this?)
     - Make it easy to use and interact
- [ ] Make DB code more robust and generic as possible
- [ ] Model all **Tables** that will be needed:
  - [ ] Logically (Progettazione Logica, teoricamente)
  - [ ] Pratically (Java Tables implementation)
  - [ ] Check them into DB
- [ ] **GUI**:
  - [ ] Scratch a GUI design
  - [ ] Decide which operation will be needed in the GUI and how to do/design them
  - [ ] Generate *FXML* with *sceneBuilder*
  - [ ] Implement a robust controller
- [ ] **Queries** design:
  - Write their purpose down.
  - Check complexity
  - Write *SQL* query
  - [ ] Implement a function that can easy compute a new query (MODEL PART) given a query as a String.

---
### **SCHEMA**
Tutto quello che è qui è da fare concorrentemente alla stesura del report
- [ ] Rifinire **ER** (specialmente i nomi)
  - [ ] Check dei vincoli (dopo averli definiti)
  - [ ] Check *1NF, 2NF, 3NF* (magari *3NF* anche no dai).
- [ ] Progettazione logica:
  - [ ] Verificare che la traduzione non faccia tralasciare dei vincoli espressi
  - [ ] Rendere la progettazione di query più semplice possibile 

---
### **REPORT**
Take **daily** notes for **REPORT** (at least two or three times a week work on the report).
- [ ] Intervista
  - [ ] Analisi dei *requisiti*
- [ ] Rilevamento delle ambiguità e correzioni proposte
- [ ] Definizione delle specifche in linguaggio naturale (analisi) ed estrazione dei **Progettazione Concettuale**
- [ ] **Schema Scheletro** (progettazione concettuale)
- [ ] Raffinamenti proposti
- [ ] Schema conettuale finale (Solo dopo aver finito tutti i punti di [Schema](#schema))
**Progettazione logica**
- [ ] Stima dei volumi dei dati
- [ ] Descrizione delle operazioni principali e stima della loro frequenza
- [ ] Schemi di navigazione e tabelle degli acessi (DECIDERE CON COSA FARLE)
- [ ] Raffinamento dello schema (eliminazione di identificatori esterni, attributi composti e gerarchie, **scelta delle chiavi**)
- [ ] Analisi delle ridondanze 
- [ ] Traduzione di entità e associazioni in relazioni
- [ ] Schema **Relazionale** finale.
- [ ] Traduzione delle operazioni in query SQL
**Progettazione** dell'**[Applicazione](#coding)**
- [ ] Descrizione dell'architettura dell'applicazione realizzata (**screenshots**)
