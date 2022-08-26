# Progetto corso *Basi di Dati*
## **Istruzioni**
Prima di effettuare qualunque operazione, ispezionare le classi
`DbGenerator` e `RegistrationController` e modificare 
i campi statici *username* e *password* in base alle proprie credenziali
locali di MySQL.


Per generare il database, nella home del progetto digitare:
```bash
./gradlew createDB
```
In caso di errore, `BUILD FAILED` verrà mostrato a video, e può essere 
utile visionare lo standard output dell'operazione poco sopra, oppure
visitare la pagina html autogenerata in 
`app/build/reports/tests/test/index.html` per ottenere maggiori 
informazioni su cosa/dove/quando il programma ha fallito.

Se si vuole elimare il database:
```bash
./gradlew dropDB
```
Per lanciare l'applicazione sarà sufficiente l'ulteriore task di gradle:
```bash
./gradlew run
```
