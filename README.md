# DB-project
Database Project for Database course, University of Bologna
- [DB-project](#db-project)
    - [**CODING**](#coding)
    - [**SCHEMA**](#schema)
    - [**REPORT**](#report)
  - [**Istruzioni**](#istruzioni)

Robe da far subito:
- [ ] Decidere come e con cosa disegnare per il [Report](#report) schemi e tabelle
- [x] Tradurre tutto il codice python in java (? serve ?)
### **CODING**
- [x] Implement pathfind for railways  
- [ ] Choose number/which stations to put into JSONs
- [x] Clean **local code** for calculating time and routes:
- [x] Make DB code more robust and generic as possible
- [x] Model all **Tables** that will be needed:
  - [x] Logically (Progettazione Logica, teoricamente)
  - [x] Pratically (Java Tables implementation)
  - [x] Check them into DB
- [x] **GUI**:
- [ ] **Queries** design:
  - Write their purpose down.
  - Check complexity
  - Write *SQL* query
  - [x] Implement a JDBC handler for reducing Boilerplate code, and make queries simpler to compute
---
### **SCHEMA**
Tutto quello che è qui è da fare concorrentemente alla stesura del report
- [x] Rifinire **ER** (specialmente i nomi)
  - [x] Check dei vincoli (dopo averli definiti)
  - [x] Check *1NF, 2NF, 3NF* (magari *3NF* anche no dai).
- [ ] Progettazione logica:
  - [x] Verificare che la traduzione non faccia tralasciare dei vincoli espressi
  - [x] Rendere la progettazione di query più semplice possibile 

---
### **REPORT**
Take **daily** notes for **REPORT** (at least two or three times a week work on the report).
- [x] Intervista
  - [x] Analisi dei *requisiti*
- [x] Rilevamento delle ambiguità e correzioni proposte
- [x] Definizione delle specifche in linguaggio naturale (analisi) ed estrazione dei **Progettazione Concettuale**
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

## **Istruzioni**
Per generare il database, nella home del progetto digitare:
```bash
./gradlew createDB
```
L'ultima riga dei messaggi che verranno visualizzati a schermo determinerà se l'operazione è andata a buon fine. (da implementare check di altri exit status e fare i comandi anche per WINDOWS con gradlew.bat ...).

Se si vuole elimare il database:
```bash
./gradlew dropDB
```
Per lanciare l'applicazione sarà sufficiente l'ulteriore task di gradle:
```bash
./gradlew run
```
Fine :)
