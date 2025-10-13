# Metagenomic_Comparison_Tool

Made by Ivar Lottman  
Teachers: Michiel Noback and Marcel Kempenaar  

## Introduction  
MCT is een tool voor het vergelijken van microbiomen door te kijken naar de verschillen in populatie tussen de (groupen) samples doormiddel van het inlezen van metagenomic sequencing data.  

De hoofd functionaliteit van deze tool is het vergelijken tussen de sample (groepen) op het gebied van de gemiddelde abundancie verandering in ruw getal en percentage.  

De hoofd argumenten zijn het filteren op taxon en min abundance waarde. En in het geval van sample groepen keuze uit een gemiddelde of meadiaan om de groepen te vergelijken.  

De input van deze tool gebruikt kreports uit de kraken2 tool  
dit is tab limited data met als eerste tab het percentage fragmenten van de root van het raport  
het 2de tab is de hoeveelheid fragmenten op de Clade(organisme groep), de 3de tab is hoeveel fragmenten direct aan het taxon word toegewezen.  
De 4de en 5de tab zijn de NCBI identifier en de wetenschapelijke naam van het taxon.  


## Instalation guide   
java 24
In het geval van het gebruik van een IDE open de MCT folder en niet de repo

## User guide  
jar (configfile1) (configfile2) (optie ...)(optie..)
in de deze config files moeten de filepaths naar de krepport samples staan 


## Explanation Repostory  
MCT folder
hieren staat het java project

datamanagement  
hierien staan de data handeling classes  
io   
hierien staan de file handeling classes  

main  

Recources folder  
In deze folder staat de orginele uml ontwerpen, accession en commands textfiles voor het downloaden van de test files.   
Verder zitten er 3 test kreports in en 2 test config files

## Programmer guide  

### Known bugs  

## Honerary mentions  
Michiel Noback  
Marcel Kempenaar    