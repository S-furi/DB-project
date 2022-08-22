-- *********************************************
-- * SQL MySQL generation                      
-- *--------------------------------------------
-- * DB-MAIN version: 11.0.2              
-- * Generator date: Sep 14 2021              
-- * Generation date: Mon Aug 22 13:26:30 2022 
-- * LUN file: Y:\DBproject\schemas\Railway.lun 
-- * Schema: Ferrovia/6 
-- ********************************************* 


-- Database Section
-- ________________ 

create database Ferrovia;
use Ferrovia;


-- Tables Section
-- _____________ 

create table AMMINISTRATORE (
     adminID varchar(5) not null,
     annoContratto date not null,
     nome varchar(40) not null,
     cognome varchar(40) not null,
     telefono varchar(10) not null,
     email varchar(50) not null,
     residenza varchar(40) not null,
     constraint ID_AMMINISTRATORE_ID primary key (adminID));

create table BIGLIETTO (
     codiceBiglietto varchar(5) not null,
     regionaleVeloce char,
     prezzo float(10) not null,
     codPasseggero varchar(5) not null,
     codPercorso varchar(5) not null,
     codTreno varchar(5) not null,
     data date not null,
     constraint ID_BIGLIETTO_ID primary key (codiceBiglietto));

create table CARROZZA (
     numClasse varchar(1) not null,
     codTreno varchar(5) not null,
     numeroCarrozza int not null,
     maxPosti int not null,
     bagno char,
     constraint ID_CARROZZA_ID primary key (numClasse, codTreno, numeroCarrozza));

create table CITTA (
     nome varchar(40) not null,
     regione varchar(40) not null,
     provincia varchar(2) not null,
     constraint ID_CITTA_ID primary key (nome));

create table CLASSE (
     numClasse varchar(1) not null,
     postiDisponibili int not null,
     constraint ID_CLASSE_ID primary key (numClasse));

create table LOYALTY_CARD (
     codCarta varchar(5) not null,
     punti int not null,
     percentualeSconto int not null,
     constraint ID_LOYALTY_CARD_ID primary key (codCarta));

create table MACCHINISTA (
     numeroPatente varchar(5) not null,
     annoContratto date not null,
     nome varchar(40) not null,
     cognome varchar(40) not null,
     telefono varchar(10) not null,
     email varchar(50) not null,
     residenza varchar(40) not null,
     constraint ID_MACCHINISTA_ID primary key (numeroPatente));

create table PASSEGGERO (
     codPasseggero varchar(5) not null,
     nome varchar(40) not null,
     cognome varchar(40) not null,
     telefono varchar(10) not null,
     email varchar(50) not null,
     residenza varchar(40) not null,
     constraint ID_PASSEGGERO_ID primary key (codPasseggero));

create table PERCORRENZA (
     codPercorso varchar(5) not null,
     codTreno varchar(5) not null,
     data date not null,
     tempoEffettivo varchar(10) not null,
     postiDisponibili int not null,
     constraint ID_PERCORRENZA_ID primary key (codPercorso, codTreno, data));

create table PERCORSO (
     codPercorso varchar(5) not null,
     tempoTotale varchar(5) not null,
     numFermate int not null,
     adminID varchar(5) not null,
     constraint ID_PERCORSO_ID primary key (codPercorso));

create table POSTO (
     numClasse varchar(1) not null,
     codTreno varchar(5) not null,
     numeroCarrozza int not null,
     numeroPosto int not null,
     constraint ID_POSTO_ID primary key (numClasse, codTreno, numeroCarrozza, numeroPosto));

create table DETTAGLIO_BIGLIETTO (
     numClasse varchar(1) not null,
     codTreno varchar(5) not null,
     numeroCarrozza int not null,
     numeroPosto int not null,
     dataViaggio date not null,
     codiceBiglietto varchar(5) not null,
     constraint ID_Prenotazione_ID primary key (numClasse, codTreno, numeroCarrozza, numeroPosto, dataViaggio),
     constraint FKRiseva_ID unique (codiceBiglietto));

create table RESPONSABILE_STAZIONE (
     codResponsabile varchar(5) not null,
     annoContratto date not null,
     nome varchar(40) not null,
     cognome varchar(40) not null,
     telefono varchar(10) not null,
     email varchar(50) not null,
     residenza varchar(40) not null,
     constraint ID_RESPONSABILE_STAZIONE_ID primary key (codResponsabile));

create table Sottoscrizione (
     codPasseggero varchar(5) not null,
     codCarta varchar(5) not null,
     dataSottoscrizione date not null,
     constraint FKRiferimento_Pas_ID primary key (codPasseggero),
     constraint FKRiferimento_Card_ID unique (codCarta));

create table STAZIONE (
     codStazione varchar(10) not null,
     nome varchar(20) not null,
     numBinari int not null,
     locazione varchar(40) not null,
     codResponsabile varchar(5) not null,
     constraint ID_STAZIONE_ID primary key (codStazione));

create table DETTAGLIO_PERCORSO (
     codPercorso varchar(5) not null,
     codTratta varchar(5) not null,
     ordine varchar(10) not null,
     constraint ID_Strutturazione_ID primary key (codPercorso, codTratta, ordine));

create table TRATTA (
     codTratta varchar(5) not null,
     distanza int not null,
     codStazionePartenza varchar(10) not null,
     codStazioneArrivo varchar(10) not null,
     constraint ID_TRATTA_ID primary key (codTratta));

create table TRENO (
     codTreno varchar(5) not null,
     capienza int not null,
     regionaleVeloce char,
     codMacchinista varchar(5) not null,
     constraint ID_TRENO_ID primary key (codTreno));


-- Constraints Section
-- ___________________ 

-- Not implemented
-- alter table AMMINISTRATORE add constraint ID_AMMINISTRATORE_CHK
--     check(exists(select * from PERCORSO
--                  where PERCORSO.adminID = adminID)); 

alter table AMMINISTRATORE add constraint FKResidenza_Adm_FK
     foreign key (residenza)
     references CITTA (nome);

-- Not implemented
-- alter table BIGLIETTO add constraint ID_BIGLIETTO_CHK
--     check(exists(select * from DETTAGLIO_BIGLIETTO
--                  where DETTAGLIO_BIGLIETTO.codiceBiglietto = codiceBiglietto)); 

alter table BIGLIETTO add constraint FKAcquisto_FK
     foreign key (codPasseggero)
     references PASSEGGERO (codPasseggero);

alter table BIGLIETTO add constraint FKRiferimento_FK
     foreign key (codPercorso, codTreno, data)
     references PERCORRENZA (codPercorso, codTreno, data);

alter table CARROZZA add constraint FKComposizione_FK
     foreign key (codTreno)
     references TRENO (codTreno);

alter table CARROZZA add constraint FKAppartenenza
     foreign key (numClasse)
     references CLASSE (numClasse);

-- Not implemented
-- alter table LOYALTY_CARD add constraint ID_LOYALTY_CARD_CHK
--     check(exists(select * from Sottoscrizione
--                  where Sottoscrizione.codCarta = codCarta)); 

alter table MACCHINISTA add constraint FKResidenza_Mac_FK
     foreign key (residenza)
     references CITTA (nome);

alter table PASSEGGERO add constraint FKResidenza_Pas_FK
     foreign key (residenza)
     references CITTA (nome);

alter table PERCORRENZA add constraint FKServizio_FK
     foreign key (codTreno)
     references TRENO (codTreno);

alter table PERCORRENZA add constraint FKAttivazione
     foreign key (codPercorso)
     references PERCORSO (codPercorso);

alter table PERCORSO add constraint FKAmministrazione_FK
     foreign key (adminID)
     references AMMINISTRATORE (adminID);

alter table POSTO add constraint FKSuddivisione
     foreign key (numClasse, codTreno, numeroCarrozza)
     references CARROZZA (numClasse, codTreno, numeroCarrozza);

alter table DETTAGLIO_BIGLIETTO add constraint FKRiseva_FK
     foreign key (codiceBiglietto)
     references BIGLIETTO (codiceBiglietto);

alter table DETTAGLIO_BIGLIETTO add constraint FKPer
     foreign key (numClasse, codTreno, numeroCarrozza, numeroPosto)
     references POSTO (numClasse, codTreno, numeroCarrozza, numeroPosto);

-- Not implemented
-- alter table RESPONSABILE_STAZIONE add constraint ID_RESPONSABILE_STAZIONE_CHK
--     check(exists(select * from STAZIONE
--                  where STAZIONE.codResponsabile = codResponsabile)); 

alter table RESPONSABILE_STAZIONE add constraint FKResidenza_Resp_FK
     foreign key (residenza)
     references CITTA (nome);

alter table Sottoscrizione add constraint FKRiferimento_Pas_FK
     foreign key (codPasseggero)
     references PASSEGGERO (codPasseggero);

alter table Sottoscrizione add constraint FKRiferimento_Card_FK
     foreign key (codCarta)
     references LOYALTY_CARD (codCarta);

alter table STAZIONE add constraint FKLocazione_FK
     foreign key (locazione)
     references CITTA (nome);

alter table STAZIONE add constraint FKGestione_FK
     foreign key (codResponsabile)
     references RESPONSABILE_STAZIONE (codResponsabile);

alter table DETTAGLIO_PERCORSO add constraint FKStr_TRA_FK
     foreign key (codTratta)
     references TRATTA (codTratta);

alter table DETTAGLIO_PERCORSO add constraint FKStr_PER
     foreign key (codPercorso)
     references PERCORSO (codPercorso);

alter table TRATTA add constraint FKPartenza_FK
     foreign key (codStazionePartenza)
     references STAZIONE (codStazione);

alter table TRATTA add constraint FKArrivo_FK
     foreign key (codStazioneArrivo)
     references STAZIONE (codStazione);

alter table TRENO add constraint FKPilota_FK
     foreign key (codMacchinista)
     references MACCHINISTA (numeroPatente);


-- Index Section
-- _____________ 

create unique index ID_AMMINISTRATORE_IND
     on AMMINISTRATORE (adminID);

create index FKResidenza_Adm_IND
     on AMMINISTRATORE (residenza);

create unique index ID_BIGLIETTO_IND
     on BIGLIETTO (codiceBiglietto);

create index FKAcquisto_IND
     on BIGLIETTO (codPasseggero);

create index FKRiferimento_IND
     on BIGLIETTO (codPercorso, codTreno, data);

create unique index ID_CARROZZA_IND
     on CARROZZA (numClasse, codTreno, numeroCarrozza);

create index FKComposizione_IND
     on CARROZZA (codTreno);

create unique index ID_CITTA_IND
     on CITTA (nome);

create unique index ID_CLASSE_IND
     on CLASSE (numClasse);

create unique index ID_LOYALTY_CARD_IND
     on LOYALTY_CARD (codCarta);

create unique index ID_MACCHINISTA_IND
     on MACCHINISTA (numeroPatente);

create index FKResidenza_Mac_IND
     on MACCHINISTA (residenza);

create unique index ID_PASSEGGERO_IND
     on PASSEGGERO (codPasseggero);

create index FKResidenza_Pas_IND
     on PASSEGGERO (residenza);

create unique index ID_PERCORRENZA_IND
     on PERCORRENZA (codPercorso, codTreno, data);

create index FKServizio_IND
     on PERCORRENZA (codTreno);

create unique index ID_PERCORSO_IND
     on PERCORSO (codPercorso);

create index FKAmministrazione_IND
     on PERCORSO (adminID);

create unique index ID_POSTO_IND
     on POSTO (numClasse, codTreno, numeroCarrozza, numeroPosto);

create unique index ID_Prenotazione_IND
     on DETTAGLIO_BIGLIETTO (numClasse, codTreno, numeroCarrozza, numeroPosto, dataViaggio);

create unique index FKRiseva_IND
     on DETTAGLIO_BIGLIETTO (codiceBiglietto);

create unique index ID_RESPONSABILE_STAZIONE_IND
     on RESPONSABILE_STAZIONE (codResponsabile);

create index FKResidenza_Resp_IND
     on RESPONSABILE_STAZIONE (residenza);

create unique index FKRiferimento_Pas_IND
     on Sottoscrizione (codPasseggero);

create unique index FKRiferimento_Card_IND
     on Sottoscrizione (codCarta);

create unique index ID_STAZIONE_IND
     on STAZIONE (codStazione);

create index FKLocazione_IND
     on STAZIONE (locazione);

create index FKGestione_IND
     on STAZIONE (codResponsabile);

create unique index ID_Strutturazione_IND
     on DETTAGLIO_PERCORSO (codPercorso, codTratta, ordine);

create index FKStr_TRA_IND
     on DETTAGLIO_PERCORSO (codTratta);

create unique index ID_TRATTA_IND
     on TRATTA (codTratta);

create index FKPartenza_IND
     on TRATTA (codStazionePartenza);

create index FKArrivo_IND
     on TRATTA (codStazioneArrivo);

create unique index ID_TRENO_IND
     on TRENO (codTreno);

create index FKPilota_IND
     on TRENO (codMacchinista);

