-- *********************************************
-- * SQL MySQL generation                      
-- *--------------------------------------------
-- * DB-MAIN version: 11.0.2              
-- * Generator date: Sep 14 2021              
-- * Generation date: Mon Aug  1 15:38:53 2022 
-- * LUN file: C:\users\crossover\Desktop\My Mac Desktop\DB\Progetto\prj\DB-project\schemas\Railway.lun 
-- * Schema: Ferrovia/2-1 
-- ********************************************* 


-- Database Section
-- ________________ 

create database Ferrovia;
use Ferrovia;


-- Tables Section
-- _____________ 

create table AMMINISTRATORE (
     adminID char(1) not null,
     annoContratto char(1) not null,
     nome char(1) not null,
     cognome char(1) not null,
     telefono char(1) not null,
     email char(1) not null,
     residenza char(1) not null,
     constraint ID_AMMINISTRATORE_ID primary key (adminID));

create table BIGLIETTO (
     codiceBiglietto char(1) not null,
     regionaleVeloce char(1),
     codComitiva char(1),
     codPasseggero char(1) not null,
     codPercorso char(1) not null,
     codTreno char(1) not null,
     data char(1) not null,
     constraint ID_BIGLIETTO_ID primary key (codiceBiglietto));

create table CARROZZA (
     numClasse char(1) not null,
     codTreno char(1) not null,
     numeroCarrozza char(1), -- Compound attribute -- not null,
     maxPosti char(1) not null,
     bagno char(1),
     constraint ID_CARROZZA_ID primary key (numClasse, codTreno, numeroCarrozza -- Compound attribute --)
     ));

create table CITTA (
     nome char(1) not null,
     regione char(1) not null,
     provincia char(1) not null,
     constraint ID_CITTA_ID primary key (nome));

create table CLASSE (
     numClasse char(1) not null,
     postiDisponibili char(1) not null,
     constraint ID_CLASSE_ID primary key (numClasse));

create table COMITIVA (
     codComitiva char(1) not null,
     numPersone char(1) not null,
     constraint ID_COMITIVA_ID primary key (codComitiva));

create table LOYALTY_CARD (
     codCarta char(1) not null,
     dataSottoscrizione char(1) not null,
     punti char(1) not null,
     percentualeSconto char(1) not null,
     constraint ID_LOYALTY_CARD_ID primary key (codCarta));

create table MACCHINISTA (
     numeroPatente char(1) not null,
     annoContratto char(1) not null,
     nome char(1) not null,
     cognome char(1) not null,
     telefono char(1) not null,
     email char(1) not null,
     residenza char(1) not null,
     constraint ID_MACCHINISTA_ID primary key (numeroPatente));

create table PASSEGGERO (
     codPasseggero char(1) not null,
     nome char(1) not null,
     cognome char(1) not null,
     telefono char(1) not null,
     email char(1) not null,
     residenza char(1) not null,
     codComitiva char(1),
     constraint ID_PASSEGGERO_ID primary key (codPasseggero));

create table PERCORRENZA (
     codPercorso char(1) not null,
     codTreno char(1) not null,
     data char(1) not null,
     constraint ID_PERCORRENZA_ID primary key (codPercorso, codTreno, data));

create table PERCORSO (
     codPercorso char(1) not null,
     tempoTotale char(1) not null,
     numFermate char(1) not null,
     adminID char(1) not null,
     constraint ID_PERCORSO_ID primary key (codPercorso));

create table POSTO (
     numClasse char(1) not null,
     codTreno char(1) not null,
     numeroCarrozza char(1), -- Compound attribute -- not null,
     numeroPosto char(1) not null,
     constraint ID_POSTO_ID primary key (numClasse, codTreno, numeroCarrozza -- Compound attribute --, numeroPosto));
	));
    
create table DETTAGLIO_BIGLIETTO (
     codiceBiglietto char(1) not null,
     dataPrenotazione char(1) not null,
     numClasse char(1),
     codTreno char(1) not null,
     numeroCarrozza char(1), -- Compound attribute --,
     numeroPosto char(1),
     constraint ID_DETTA_BIGLI_ID primary key (codiceBiglietto));

create table RESPONSABILE_STAZIONE (
     codResponsabile char(1) not null,
     annoContratto char(1) not null,
     nome char(1) not null,
     cognome char(1) not null,
     telefono char(1) not null,
     email char(1) not null,
     residenza char(1) not null,
     constraint ID_RESPONSABILE_STAZIONE_ID primary key (codResponsabile));

create table SOTTOSCRIZIONE (
     codPasseggero char(1) not null,
     codCarta char(1) not null,
     dataSottoscrizione char(1) not null,
     constraint ID_SOTTO_PASSE_ID primary key (codPasseggero),
     constraint SID_SOTTO_LOYAL_ID unique (codCarta));

create table STAZIONE (
     codStazione char(1) not null,
     nome char(1) not null,
     numBinari char(1) not null,
     codResponsabile char(1) not null,
     constraint ID_STAZIONE_ID primary key (codStazione));

create table TRATTA (
     codStazioneArrivo char(1) not null,
     codStazionePartenza char(1) not null,
     codTratta char(1) not null,
     distanza char(1) not null,
     codPercorso char(1) not null,
     constraint ID_TRATTA_ID primary key (codStazioneArrivo, codStazionePartenza, codTratta));

create table TRENO (
     codTreno char(1) not null,
     numeroPatente char(1) not null,
     capienza char(1) not null,
     regionaleVeloce char(1),
     constraint ID_TRENO_ID primary key (codTreno),
     constraint SID_TRENO_MACCH_ID unique (numeroPatente));


-- Constraints Section
-- ___________________ 

-- Not implemented
-- alter table AMMINISTRATORE add constraint ID_AMMINISTRATORE_CHK
--     check(exists(select * from PERCORSO
--                  where PERCORSO.adminID = adminID)); 

alter table AMMINISTRATORE add constraint REF_AMMIN_CITTA_FK
     foreign key (residenza)
     references CITTA (nome);

-- Not implemented
-- alter table BIGLIETTO add constraint ID_BIGLIETTO_CHK
--     check(exists(select * from DETTAGLIO_BIGLIETTO
--                  where DETTAGLIO_BIGLIETTO.codiceBiglietto = codiceBiglietto)); 

alter table BIGLIETTO add constraint REF_BIGLI_COMIT_FK
     foreign key (codComitiva)
     references COMITIVA (codComitiva);

alter table BIGLIETTO add constraint REF_BIGLI_PASSE_FK
     foreign key (codPasseggero)
     references PASSEGGERO (codPasseggero);

alter table BIGLIETTO add constraint REF_BIGLI_PERCO_FK
     foreign key (codPercorso, codTreno, data)
     references PERCORRENZA (codPercorso, codTreno, data);

alter table CARROZZA add constraint REF_CARRO_TRENO_FK
     foreign key (codTreno)
     references TRENO (codTreno);

alter table CARROZZA add constraint REF_CARRO_CLASS
     foreign key (numClasse)
     references CLASSE (numClasse);

-- Not implemented
-- alter table LOYALTY_CARD add constraint ID_LOYALTY_CARD_CHK
--     check(exists(select * from SOTTOSCRIZIONE
--                  where SOTTOSCRIZIONE.codCarta = codCarta)); 

-- Not implemented
-- alter table MACCHINISTA add constraint ID_MACCHINISTA_CHK
--     check(exists(select * from TRENO
--                  where TRENO.numeroPatente = numeroPatente)); 

alter table MACCHINISTA add constraint REF_MACCH_CITTA_FK
     foreign key (residenza)
     references CITTA (nome);

alter table PASSEGGERO add constraint REF_PASSE_CITTA_FK
     foreign key (residenza)
     references CITTA (nome);

alter table PASSEGGERO add constraint REF_PASSE_COMIT_FK
     foreign key (codComitiva)
     references COMITIVA (codComitiva);

alter table PERCORRENZA add constraint REF_PERCO_TRENO_FK
     foreign key (codTreno)
     references TRENO (codTreno);

alter table PERCORRENZA add constraint REF_PERCO_PERCO
     foreign key (codPercorso)
     references PERCORSO (codPercorso);

alter table PERCORSO add constraint EQU_PERCO_AMMIN_FK
     foreign key (adminID)
     references AMMINISTRATORE (adminID);

alter table POSTO add constraint REF_POSTO_CARRO
     foreign key (numClasse, codTreno, numeroCarrozza )-- Compound attribute --)
     references CARROZZA (numClasse, codTreno, numeroCarrozza ); -- Compound attribute --);

alter table DETTAGLIO_BIGLIETTO add constraint ID_DETTA_BIGLI_FK
     foreign key (codiceBiglietto)
     references BIGLIETTO (codiceBiglietto);

alter table DETTAGLIO_BIGLIETTO add constraint REF_DETTA_POSTO_FK
     foreign key (numClasse, codTreno, numeroCarrozza ) -- Compound attribute --, numeroPosto)
     references POSTO (numClasse, codTreno, numeroCarrozza); -- Compound attribute --, numeroPosto);

-- Not implemented
-- alter table RESPONSABILE_STAZIONE add constraint ID_RESPONSABILE_STAZIONE_CHK
--     check(exists(select * from STAZIONE
--                  where STAZIONE.codResponsabile = codResponsabile)); 

alter table RESPONSABILE_STAZIONE add constraint REF_RESPO_CITTA_FK
     foreign key (residenza)
     references CITTA (nome);

alter table SOTTOSCRIZIONE add constraint ID_SOTTO_PASSE_FK
     foreign key (codPasseggero)
     references PASSEGGERO (codPasseggero);

alter table SOTTOSCRIZIONE add constraint SID_SOTTO_LOYAL_FK
     foreign key (codCarta)
     references LOYALTY_CARD (codCarta);

alter table STAZIONE add constraint EQU_STAZI_RESPO_FK
     foreign key (codResponsabile)
     references RESPONSABILE_STAZIONE (codResponsabile);

alter table TRATTA add constraint REF_TRATT_PERCO_FK
     foreign key (codPercorso)
     references PERCORSO (codPercorso);

alter table TRATTA add constraint REF_TRATT_STAZI_1_FK
     foreign key (codStazionePartenza)
     references STAZIONE (codStazione);

alter table TRATTA add constraint REF_TRATT_STAZI
     foreign key (codStazioneArrivo)
     references STAZIONE (codStazione);

alter table TRENO add constraint SID_TRENO_MACCH_FK
     foreign key (numeroPatente)
     references MACCHINISTA (numeroPatente);


-- Index Section
-- _____________ 

create unique index ID_AMMINISTRATORE_IND
     on AMMINISTRATORE (adminID);

create index REF_AMMIN_CITTA_IND
     on AMMINISTRATORE (residenza);

create unique index ID_BIGLIETTO_IND
     on BIGLIETTO (codiceBiglietto);

create index REF_BIGLI_COMIT_IND
     on BIGLIETTO (codComitiva);

create index REF_BIGLI_PASSE_IND
     on BIGLIETTO (codPasseggero);

create index REF_BIGLI_PERCO_IND
     on BIGLIETTO (codPercorso, codTreno, data);

create unique index ID_CARROZZA_IND
     on CARROZZA (numClasse, codTreno, numeroCarrozza); -- Compound attribute --);

create index REF_CARRO_TRENO_IND
     on CARROZZA (codTreno);

create unique index ID_CITTA_IND
     on CITTA (nome);

create unique index ID_CLASSE_IND
     on CLASSE (numClasse);

create unique index ID_COMITIVA_IND
     on COMITIVA (codComitiva);

create unique index ID_LOYALTY_CARD_IND
     on LOYALTY_CARD (codCarta);

create unique index ID_MACCHINISTA_IND
     on MACCHINISTA (numeroPatente);

create index REF_MACCH_CITTA_IND
     on MACCHINISTA (residenza);

create unique index ID_PASSEGGERO_IND
     on PASSEGGERO (codPasseggero);

create index REF_PASSE_CITTA_IND
     on PASSEGGERO (residenza);

create index REF_PASSE_COMIT_IND
     on PASSEGGERO (codComitiva);

create unique index ID_PERCORRENZA_IND
     on PERCORRENZA (codPercorso, codTreno, data);

create index REF_PERCO_TRENO_IND
     on PERCORRENZA (codTreno);

create unique index ID_PERCORSO_IND
     on PERCORSO (codPercorso);

create index EQU_PERCO_AMMIN_IND
     on PERCORSO (adminID);

create unique index ID_POSTO_IND
     on POSTO (numClasse, codTreno, numeroCarrozza); -- Compound attribute --, numeroPosto);

create unique index ID_DETTA_BIGLI_IND
     on DETTAGLIO_BIGLIETTO (codiceBiglietto);

create index REF_DETTA_POSTO_IND
     on DETTAGLIO_BIGLIETTO (numClasse, codTreno, numeroCarrozza); -- Compound attribute --, numeroPosto);

create unique index ID_RESPONSABILE_STAZIONE_IND
     on RESPONSABILE_STAZIONE (codResponsabile);

create index REF_RESPO_CITTA_IND
     on RESPONSABILE_STAZIONE (residenza);

create unique index ID_SOTTO_PASSE_IND
     on SOTTOSCRIZIONE (codPasseggero);

create unique index SID_SOTTO_LOYAL_IND
     on SOTTOSCRIZIONE (codCarta);

create unique index ID_STAZIONE_IND
     on STAZIONE (codStazione);

create index EQU_STAZI_RESPO_IND
     on STAZIONE (codResponsabile);

create unique index ID_TRATTA_IND
     on TRATTA (codStazioneArrivo, codStazionePartenza, codTratta);

create index REF_TRATT_PERCO_IND
     on TRATTA (codPercorso);

create index REF_TRATT_STAZI_1_IND
     on TRATTA (codStazionePartenza);

create unique index ID_TRENO_IND
     on TRENO (codTreno);

create unique index SID_TRENO_MACCH_IND
     on TRENO (numeroPatente);

