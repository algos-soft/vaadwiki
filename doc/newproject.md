#Documentazione per creare un nuovo progetto

IDEA, creazione di un nuovo progetto di base con VaadinFlow e SpringBoot<br>
Usa Vaadin 14.0.1 e IntelliJ Idea 2019.3
   	

###Crea struttura base project Idea

1. File -> New -> Project...

2. Seleziona **Maven** a sinistra (la seconda opzione del secondo gruppo)

3. Controlla che Project SDK sia selezionato. Se manca, crealo -> _New_

4. Non selezionare 'Create from archetype'.
   <br>Confermare -> _Next_

5. Controlla 'Name' del progetto (minuscolo) e 'Location' d'installazione.

6. Apri 'Artifact Coordinates' e regola: 
   <br>GroupId: it.algos
   <br>ArtifactId: nomeNuovoProgetto
   <br>Version: 1.0-SNAPSHOT
   <br> Confermare -> _Finish_
   
7. Chiudi il progetto appena creato

8. Il progetto base è stato creato in /Users/gac/Documents/IdeaProjects/...

9. Se vuoi cambiare directory di creazione di Idea vai a: Preferences->System Settings a destra Project->Default project directory

10. Se lo crei in una directory diversa devi spostarlo in /Users/gac/Documents/IdeaProjects/ prima di usare Wizard


###Crea nuovo progetto

1. Apri 'vaadflow14' e lancialo

2. Clicca sul meu **Wizard** e sul bottone 'New project'

3. Seleziona il project appena creato nel popup di progetti vuoti esistenti nella directory IdeaProjects
   <br>Se il progetto non esiste/non appare, torna alla sezione precedente
   <br>Clicca il bottone _Conferma_



###Struttura base 'vaadflow'

1. Viene creata la struttura base di un progetto, copiando i sorgenti del progetto 'it.algos.vaadflow' 

2. Viene creata in 'src/main/java', una directory 'it.algos.vaadflow'



###Struttura specifica 'nomeNuovoProgetto'

1. Viene creata la struttura specifica di un progetto, utilizzando i sorgenti situati in wizard/sources 

2. Viene creata in 'src/main/java', una directory 'it.algos.nomeNuovoProgetto'

3. Viene creata in 'src/main/java/nomeNuovoProgetto', una directory 'application' con alcuni files obbligatori

4. Viene creata in 'src/main/java/nomeNuovoProgetto', una directory vuota 'modules' per le collections da creare

5. Viene creato in 'src/main/java/nomeNuovoProgetto', un file xxxApplication.java col metodo 'main'

6. Viene creato un file Maven 'pom.xml'

7. Viene creata in 'src/main', una directory 'webapp'

8. Viene creata in 'src/main', una directory 'resources'


###Regolazioni iniziali 'nomeNuovoProgetto'

1. **Maven** project need to be imported  ->  Enable Auto-Import

2. Edit Configuration
   <br>Cliccare **+** in alto a sinistra
   <br>Selezionare Spring Boot
   <br>Click the **+** button to create a new **Spring Boot Application** based on default settings
   <br>Selezionare la main class   
   <br>Confermare -> _OK_

3. Regolare il vcs su GitHub


###Installazione JAR
1. Da Maven Projects, lanciare Lifecycle->install
2. (locale) Da terminale java -jar 'nomefile'
3. (server) Da terminale scp /Users/gac/Documents/IdeaProjects/vaadwam/target/vaadwam-1.jar root@54.37.64.27:/root/springboot/vaadwam.jar

