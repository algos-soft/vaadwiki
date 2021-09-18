package it.algos.vaadflow14.wizard.enumeration;

import it.algos.vaadflow14.backend.application.*;
import it.algos.vaadflow14.backend.service.*;
import static it.algos.vaadflow14.wizard.scripts.WizCost.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import javax.annotation.*;
import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: mer, 04-nov-2020
 * Time: 19:40
 */
public enum AEDir {

    //--regolata inizialmente dal system, indipendentemente dall'apertura di un dialogo
    //--tutte le property il cui nome inizia con 'path' iniziano e finiscono con uno SLASH e sono path completi
    pathCurrent(true, false, false, "Directory dove gira il programma in uso. Recuperata dal System") {
        @Override
        public void elabora(String pathCurrent) {
            this.setValue(pathCurrent);
        }
    },// end of single enumeration

    //--regolata inizialmente dal system, indipendentemente dall'apertura di un dialogo
    //--tutte le property il cui nome NON inizia con 'path' sono nomi o files o sub-directory, non path completi
    nameUser(true, false, false, "Programmatore. Ricavato dal path della directory corrente.") {
        @Override
        public void elabora(String pathCurrent) {
            String user = pathCurrent;
            user = user.substring(1);
            user = text.levaTestoPrimaDi(user, SLASH);
            user = user.substring(0, user.indexOf(SLASH));
            this.setValue(user);
        }
    },// end of single enumeration

    //--elaborata inizialmente partendo dal pathCurrent fornito dal system
    //--percorso base di IdeaProjects
    //--tutte le property il cui nome inizia con 'path' iniziano e finiscono con uno SLASH e sono path completi
    pathRoot(true, false, false, "Directory base in cui si trova la directory IdeaProjects") {
        @Override
        public void elabora(String pathCurrent) {
            String tagDirectory = DIR_PROJECTS;
            String path = file.findPathDirectory(pathCurrent, tagDirectory);
            this.setValue(path);
        }
    },// end of single enumeration

    //--elaborata inizialmente partendo dal pathRoot
    //--percorso per arrivare alla directory IdeaProjects dove (di norma) vengono creati i nuovi progetti
    //--posso spostarla (è successo) senza che cambi nulla. Occorre modificare la creazione in questa enumeration.
    //--tutte le property il cui nome inizia con 'path' iniziano e finiscono con uno SLASH e sono path completi
    pathIdeaProjects(true, false, false, "Directory che contiene i nuovi programmi appena creati da Idea") {
        @Override
        public void elabora(String pathCurrent) {
            String path = pathRoot.get();
            path += DIR_PROJECTS;
            this.setValue(path);
        }
    },// end of single enumeration

    //--elaborata inizialmente partendo dal pathIdeaProjects
    //--occorre che la enumeration AEFlag sia già stata elaborata PRIMA di passare di qui
    //--se siamo in AEFlag.isBaseFlow(), usa pathCurrent altrimenti cerca nel fileSystem la directory vaadFlow14
    //--posso spostarla (è successo) senza che cambi nulla. Occorre modificare la creazione in questa enumeration.
    //--tutte le property il cui nome inizia con 'path' iniziano e finiscono con uno SLASH e sono path completi
    pathVaadFlowRoot(true, false, false, "Directory del programma vaadFlow14 a livello di root") {
        @Override
        public void elabora(String pathCurrent) {
            if (AEFlag.isBaseFlow.is()) {
                String path = pathIdeaProjects.get();
                path += DIR_OPERATIVI + DIR_VAADFLOW;
                this.setValue(path);
            }
            else {//@todo Funzionalità ancora da implementare
                String path = pathIdeaProjects.get();
                path += DIR_OPERATIVI + DIR_VAADFLOW;
                this.setValue(path);
            }
        }
    },// end of single enumeration

    //--elaborata inizialmente partendo dal pathVaadFlowRoot
    //--tutte le property il cui nome inizia con 'path' iniziano e finiscono con uno SLASH e sono path completi
    pathVaadFlowResources(true, false, false, "Directory delle risorse di vaadFlow14") {
        @Override
        public void elabora(String pathCurrent) {
            String path = pathVaadFlowRoot.get();
            path += ROOT_DIR_MAIN + DIR_RESOURCES;
            this.setValue(path);
        }
    },// end of single enumeration

    //--elaborata inizialmente partendo dal pathVaadFlowResources
    //--tutte le property il cui nome inizia con 'path' iniziano e finiscono con uno SLASH e sono path completi
    pathVaadFlowMetaInf(true, false, false, "Directory della cartella META-INF di vaadFlow14") {
        @Override
        public void elabora(String pathCurrent) {
            String path = pathVaadFlowResources.get();
            path += DIR_META;
            this.setValue(path);
        }
    },// end of single enumeration

    //--elaborata inizialmente partendo dal pathVaadFlowRoot
    //--contiene i moduli, di solito due (vaadFlow14 e xxx)
    //--tutte le property il cui nome inizia con 'path' iniziano e finiscono con uno SLASH e sono path completi
    pathVaadFlowAlgos(true, false, false, "Directory che contiene il modulo vaadFlow14") {
        @Override
        public void elabora(String pathCurrent) {
            String path = pathVaadFlowRoot.get();
            path += ROOT_DIR_MAIN + DIR_ALGOS;
            this.setValue(path);
        }
    },// end of single enumeration

    //--elaborata inizialmente partendo dal pathVaadFlowAlgos
    //--tutte le property il cui nome inizia con 'path' iniziano e finiscono con uno SLASH e sono path completi
    pathVaadFlowSources(true, false, false, "Directory che contiene i sorgenti testuali di vaadFlow14 da elaborare") {
        @Override
        public void elabora(String pathCurrent) {
            String path = pathVaadFlowAlgos.get();
            path += DIR_VAADFLOW + DIR_WIZARD + DIR_SOURCES;
            this.setValue(path);
        }
    },// end of single enumeration

    //--regolata DOPO essere passati da un dialogo (WizDialogNewProject, WizDialogUpdateProject, WizDialogNewPackage, WizDialogUpdatePackage)
    //--può essere un new project oppure un update di un progetto esistente
    //--se non siamo in AEFlag.isBaseFlow(), il nome del progetto corrente
    //--tutte le property il cui nome NON inizia con 'path' sono nomi o files o sub-directory, non path completi
    nameTargetProject(false, true, false, "Nome breve new/update project da Vaadflow14 e update da altro progetto") {
        @Override
        public boolean modificaProject(String projectName) {
            this.setValue(projectName);
            return text.isValid(projectName);
        }
    },// end of single enumeration

    //--regolata DOPO essere passati da un dialogo (WizDialogNewProject, WizDialogUpdateProject, WizDialogNewPackage, WizDialogUpdatePackage)
    //--può essere un new project oppure un update di un progetto esistente
    //--se non siamo in AEFlag.isBaseFlow(), il nome del progetto corrente
    //--tutte le property il cui nome NON inizia con 'path' sono nomi o files o sub-directory, non path completi
    nameTargetProjectUpper(false, true, false, "Nome breve new/update project da Vaadflow14 e update da altro progetto") {
        @Override
        public boolean modificaProject(String projectName) {
            this.setValue(text.primaMaiuscola(projectName));
            return text.isValid(projectName);
        }
    },// end of single enumeration

    //--regolata DOPO essere passati da un dialogo (WizDialogNewProject, WizDialogUpdateProject, WizDialogNewPackage, WizDialogUpdatePackage)
    //--può essere un new project oppure un update di un progetto esistente
    //--se siamo in AEFlag.isBaseFlow(), costruisce il path da pathIdeaProjects + nameTargetProject
    //--se siamo in AEFlag.isBaseFlow() ed il path risulta errato, prova col path di AEProgetto selezionato
    //--se non siamo in AEFlag.isBaseFlow(), il path della directory corrente
    //--tutte le property il cui nome inizia con 'path' iniziano e finiscono con uno SLASH
    pathTargetRoot(false, true, false, "Path completo new/update project da Vaadflow14 e update da altro progetto") {
        @Override
        public boolean modificaProject(String projectName) {
            boolean status = true;
            String path = pathCurrent.get();

            if (AEFlag.isBaseFlow.is()) {
                if (AEFlag.isNewProject.is()) {
                    path = pathIdeaProjects.get() + nameTargetProject.get() + SLASH;
                }
                if (AEFlag.isUpdateProject.is()) {
                    path = pathIdeaProjects.get() + DIR_OPERATIVI + nameTargetProject.get() + SLASH;
                }
//                if (text.isEmpty(path)) {
//                    return false;
//                }

                if (!file.isEsisteDirectory(path)) {
                    path = AEProgetto.getProgettoByName(projectName).getPathCompleto();
                    if (text.isValid(path)) {
                        if (!file.isEsisteDirectory(path)) {
                            logger.error("Errato il path del progetto selezionato: " + projectName);
                            status = false;
                        }
                    }
                    else {
                        logger.error("Manca il path del progetto selezionato: " + projectName);
                        status = false;
                    }
                }
            }
            else {
                path += pathCurrent.get();
            }

            this.setValue(path);
            return status;
        }
    },// end of single enumeration

    //--regolata DOPO essere passati da un dialogo (WizDialogNewProject, WizDialogUpdateProject, WizDialogNewPackage, WizDialogUpdatePackage)
    //--tutte le property il cui nome inizia con 'path' iniziano e finiscono con uno SLASH
    pathTargetResources(false, true, false, "Directory delle risorse del progetto target") {
        @Override
        public boolean modificaProject(String projectName) {
            String path = pathTargetRoot.get();
            path += ROOT_DIR_MAIN + DIR_RESOURCES;
            this.setValue(path);
            return text.isValid(path);
        }
    },// end of single enumeration

    //--regolata DOPO essere passati da un dialogo (WizDialogNewProject, WizDialogUpdateProject, WizDialogNewPackage, WizDialogUpdatePackage)
    //--tutte le property il cui nome inizia con 'path' iniziano e finiscono con uno SLASH
    pathTargetMetaInf(false, true, false, "Directory della cartella META-INF del progetto target") {
        @Override
        public boolean modificaProject(String projectName) {
            String path = pathTargetResources.get();
            path += DIR_META;
            this.setValue(path);
            return text.isValid(path);
        }
    },// end of single enumeration

    //--regolata DOPO essere passati da un dialogo (WizDialogNewProject, WizDialogUpdateProject, WizDialogNewPackage, WizDialogUpdatePackage)
    ////parte dal livello di root del progetto
    //--contiene i moduli, di solito due (vaadFlow14 e xxx)
    //--tutte le property il cui nome inizia con 'path' iniziano e finiscono con uno SLASH
    pathTargetAlgos(false, true, false, "Directory che contiene sia il modulo vaadFlow14 sia modulo progetto target") {
        @Override
        public boolean modificaProject(String projectName) {
            String path = pathTargetRoot.get();
            path += ROOT_DIR_MAIN + DIR_ALGOS;
            this.setValue(path);
            return text.isValid(path);
        }
    },// end of single enumeration

    //--regolata DOPO essere passati da un dialogo (WizDialogNewProject, WizDialogUpdateProject, WizDialogNewPackage, WizDialogUpdatePackage)
    //--tutte le property il cui nome inizia con 'path' iniziano e finiscono con uno SLASH
    pathTargetSources(false, true, false, "Directory che contiene i sorgenti wizard di vaadFlow14 da cancellare nel progetto target") {
        @Override
        public boolean modificaProject(String projectName) {
            String path = pathTargetAlgos.get();
            path += DIR_VAADFLOW + DIR_WIZARD + DIR_SOURCES;
            this.setValue(path);
            return text.isValid(path);
        }
    },// end of single enumeration

    //--regolata DOPO essere passati da un dialogo (WizDialogNewProject, WizDialogUpdateProject, WizDialogNewPackage, WizDialogUpdatePackage)
    //parte dal livello di root del progetto
    //--contiene i moduli, di solito due (vaadFlow14 e xxx)
    //--tutte le property il cui nome inizia con 'path' iniziano e finiscono con uno SLASH
    pathTargetModulo(false, true, false, "Directory del modulo del progetto target") {
        @Override
        public boolean modificaProject(String projectName) {
            String path = pathTargetRoot.get();
            path += ROOT_DIR_ALGOS + nameTargetProject.get() + SLASH;
            this.setValue(path);
            return text.isValid(path);
        }
    },// end of single enumeration

    //--regolata DOPO essere passati da un dialogo (WizDialogNewProject, WizDialogUpdateProject, WizDialogNewPackage, WizDialogUpdatePackage)
    //parte dal livello di root del progetto
    //--contiene i moduli, di solito due (vaadFlow14 e xxx)
    //--tutte le property il cui nome inizia con 'path' iniziano e finiscono con uno SLASH
    pathTargetAllPackages(false, true, false, "Directory dei packages del progetto target") {
        @Override
        public boolean modificaProject(String projectName) {
            String path = pathTargetModulo.get();
            path += DIR_BACKEND + DIR_PACKAGES;
            this.setValue(path);
            return text.isValid(path);
        }
    },// end of single enumeration

    //--regolata DOPO essere passati da un dialogo (WizDialogNewProject, WizDialogUpdateProject, WizDialogNewPackage, WizDialogUpdatePackage)
    //--tutte le property il cui nome inizia con 'path' iniziano e finiscono con uno SLASH
    pathTargetBoot(false, true, false, "Directory di boot") {
        @Override
        public boolean modificaProject(String projectName) {
            String path = pathTargetModulo.get();
            path += DIR_BACKEND + DIR_BOOT;
            this.setValue(path);
            return text.isValid(path);
        }
    },// end of single enumeration

    //--regolata DOPO essere passati da un dialogo (WizDialogNewProject, WizDialogUpdateProject, WizDialogNewPackage, WizDialogUpdatePackage)
    //--tutte le property il cui nome inizia con 'file' iniziano con uno SLASH e finiscono col suffix .java
    fileTargetBoot(false, true, false, "File di boot") {
        @Override
        public boolean modificaProject(String projectName) {
            String filePath = pathTargetBoot.get();

            filePath += AEDir.nameTargetProjectUpper.get();
            filePath += FILE_BOOT;
            filePath += JAVA_SUFFIX;

            this.setValue(filePath);
            return text.isValid(filePath);
        }
    },// end of single enumeration

    //--regolata DOPO essere passati da un dialogo (WizDialogNewProject, WizDialogUpdateProject, WizDialogNewPackage, WizDialogUpdatePackage)
    //--tutte le property il cui nome inizia con 'path' iniziano e finiscono con uno SLASH
    pathTargetData(false, true, false, "Directory di data") {
        @Override
        public boolean modificaProject(String projectName) {
            String path = pathTargetModulo.get();
            path += DIR_BACKEND + DIR_DATA;
            this.setValue(path);
            return text.isValid(path);
        }
    },// end of single enumeration

    //--regolata DOPO essere passati da un dialogo (WizDialogNewProject, WizDialogUpdateProject, WizDialogNewPackage, WizDialogUpdatePackage)
    //--tutte le property il cui nome inizia con 'file' iniziano con uno SLASH e finiscono col suffix .java
    fileTargetData(false, true, false, "File di data") {
        @Override
        public boolean modificaProject(String projectName) {
            String filePath = pathTargetData.get();

            filePath += AEDir.nameTargetProjectUpper.get();
            filePath += FILE_DATA;
            filePath += JAVA_SUFFIX;

            this.setValue(filePath);
            return text.isValid(filePath);
        }
    },// end of single enumeration

    //--regolata DOPO essere passati da un dialogo (WizDialogNewProject, WizDialogUpdateProject, WizDialogNewPackage, WizDialogUpdatePackage)
    //--tutte le property il cui nome inizia con 'path' iniziano e finiscono con uno SLASH
    pathTargetApplication(false, true, false, "Directory application") {
        @Override
        public boolean modificaProject(String projectName) {
            String path = pathTargetModulo.get();
            path += DIR_BACKEND + DIR_APPLICATION;
            this.setValue(path);
            return text.isValid(path);
        }
    },// end of single enumeration

    //--regolata DOPO essere passati da un dialogo (WizDialogNewProject, WizDialogUpdateProject, WizDialogNewPackage, WizDialogUpdatePackage)
    //--tutte le property il cui nome inizia con 'file' iniziano con uno SLASH e finiscono col suffix .java
    fileTargetCost(false, true, false, "File di boot") {
        @Override
        public boolean modificaProject(String projectName) {
            String filePath = pathTargetApplication.get();

            filePath += AEDir.nameTargetProjectUpper.get();
            filePath += FILE_COST;
            filePath += JAVA_SUFFIX;

            this.setValue(filePath);
            return text.isValid(filePath);
        }
    },// end of single enumeration

    //--regolata DOPO essere passati da un dialogo (WizDialogNewProject, WizDialogUpdateProject, WizDialogNewPackage, WizDialogUpdatePackage)
    //--tutte le property il cui nome inizia con 'path' iniziano e finiscono con uno SLASH
    pathTargetPackage(false, false, true, "Directory del package corrente") {
        @Override
        public boolean modificaPackage(String packageName) {
            String path = pathTargetAllPackages.get();
            path += packageName + SLASH;
            this.setValue(path);
            return text.isValid(path);
        }
    },// end of single enumeration

    //--regolata DOPO essere passati da un dialogo (WizDialogNewProject, WizDialogUpdateProject, WizDialogNewPackage, WizDialogUpdatePackage)
    //--tutte le property il cui nome inizia con 'file' iniziano con uno SLASH e finiscono col suffix .java
    fileTargetEntity(false, false, true, "File di Entity") {
        @Override
        public boolean modificaPackage(String packageName) {
            String filePath = pathTargetPackage.get();

            filePath += text.primaMaiuscola(packageName);
            filePath += JAVA_SUFFIX;

            this.setValue(filePath);
            return text.isValid(filePath);
        }
    },// end of single enumeration

    //--regolata DOPO essere passati da un dialogo (WizDialogNewProject, WizDialogUpdateProject, WizDialogNewPackage, WizDialogUpdatePackage)
    //--tutte le property il cui nome inizia con 'file' iniziano con uno SLASH e finiscono col suffix .java
    fileTargetService(false, false, true, "File di xxxService") {
        @Override
        public boolean modificaPackage(String packageName) {
            String filePath = pathTargetPackage.get();

            filePath += text.primaMaiuscola(packageName);
            filePath += FlowCost.SUFFIX_SERVICE;
            filePath += JAVA_SUFFIX;

            this.setValue(filePath);
            return text.isValid(filePath);
        }
    },// end of single enumeration

    //--regolata DOPO essere passati da un dialogo (WizDialogNewProject, WizDialogUpdateProject, WizDialogNewPackage, WizDialogUpdatePackage)
    //--tutte le property il cui nome inizia con 'file' iniziano con uno SLASH e finiscono col suffix .java
    fileTargetLogic(false, false, true, "File di xxxLogic") {
        @Override
        public boolean modificaPackage(String packageName) {
            String filePath = pathTargetPackage.get();

            filePath += text.primaMaiuscola(packageName);
            filePath += FlowCost.SUFFIX_SERVICE;
            filePath += JAVA_SUFFIX;

            this.setValue(filePath);
            return text.isValid(filePath);
        }
    },// end of single enumeration

    //--regolata DOPO essere passati da un dialogo (WizDialogNewProject, WizDialogUpdateProject, WizDialogNewPackage, WizDialogUpdatePackage)
    //--può essere un new package oppure un update di un package esistente
    //--tutte le property il cui nome NON inizia con 'path' sono nomi o files o sub-directory, non path completi
    nameTargetPackage(false, false, true, "Nome del package") {
        @Override
        public boolean modificaPackage(String packageName) {
            this.setValue(packageName);
            return text.isValid(packageName);
        }
    },// end of single enumeration

    //--regolata DOPO essere passati da un dialogo (WizDialogNewProject, WizDialogUpdateProject, WizDialogNewPackage, WizDialogUpdatePackage)
    //--può essere un new package oppure un update di un package esistente
    //--tutte le property il cui nome NON inizia con 'path' sono nomi o files o sub-directory, non path completi
    nameTargetPackageUpper(false, false, true, "Nome del package con iniziale maiuscola") {
        @Override
        public boolean modificaPackage(String packageName) {
            this.setValue(text.primaMaiuscola(packageName));
            return text.isValid(packageName);
        }
    },// end of single enumeration

    ;// end of all ENUMERATIONS

    //--riferimento iniettato nella classe statico ServiceInjector
    protected FileService file;

    //--riferimento iniettato nella classe statico ServiceInjector
    protected TextService text;

    //--riferimento iniettato nella classe statico ServiceInjector
    protected ALogService logger;

    //--elaborazione SOLO all'apertura della view Wizard. Poi i valori restano statici (non modificabili)
    private boolean elaborabile;

    //--valori modificabili da ogni dialog
    private boolean modificabileProject;

    //--valori modificabili da ogni dialog
    private boolean modificabilePackage;

    private String descrizione;

    private String value;


    /**
     * Costruttore completo <br>
     */
    AEDir(boolean elaborabile, boolean modificabileProject, boolean modificabilePackage, String descrizione) {
        this.setElaborabile(elaborabile);
        this.setModificabileProject(modificabileProject);
        this.setModificabilePackage(modificabilePackage);
        this.setDescrizione(descrizione);
    }


    /**
     * Elaborazione iniziale di ogni enumeration 'elaborabile', partendo da pathCurrent <br>
     * pathCurrent è stato ricavato da System.getProperty("user.dir") <br>
     * Si applica alle enumeration che hanno il flag elaborabile=true <br>
     * Elabora tutti i valori della Enumeration AEDir dipendenti dal pathCurrent <br>
     * Verranno usati dai dialoghi: <br>
     * WizDialogNewProject, WizDialogUpdateProject, WizDialogNewPackage, WizDialogUpdatePackage <br>
     *
     * @param pathCurrent directory dove gira il programma in uso. Recuperata dal System.
     */
    public static void elaboraAll(String pathCurrent) {
        for (AEDir aeDir : AEDir.getElaborabili()) {
            aeDir.elabora(pathCurrent);
        }
    }


    /**
     * Elaborazione di ogni enumeration 'modificabile', partendo da projectName <br>
     * Chiamato (di solito) alla dismissione di un dialogo: <br>
     * WizDialogNewProject, WizDialogUpdateProject <br>
     * Elabora tutti i valori della Enumeration AEDir dipendenti dal nome del progetto <br>
     * Verranno usati dagli scripts: <br>
     * WizElaboraNewProject, WizElaboraUpdateProject <br>
     *
     * @param projectName usato per regolare le istanze modificabili della enumeration. Recuperato dal dialogo.
     */
    public static boolean modificaProjectAll(String projectName) {
        boolean status = true;

        for (AEDir aeDir : AEDir.getModificabiliProject()) {
            aeDir.modificaProject(projectName);
        }

        return status;
    }


    /**
     * Elaborazione di ogni enumeration 'modificabile', partendo da packageName <br>
     * Chiamato (di solito) alla dismissione di un dialogo: <br>
     * WizDialogNewPackage, WizDialogUpdatePackage <br>
     * Elabora tutti i valori della Enumeration AEDir dipendenti dal nome del package <br>
     * Verranno usati dagli scripts: <br>
     * WizElaboraNewPackage, WizElaboraUpdatePackage <br>
     *
     * @param packageName usato per regolare le istanze modificabili della enumeration. Recuperato dal dialogo.
     */
    public static boolean modificaPackageAll(String packageName) {
        boolean status = true;

        for (AEDir aeDir : AEDir.getModificabiliPackage()) {
            status = status && aeDir.modificaPackage(packageName);
        }

        return status;
    }


    public static List<AEDir> geValues() {
        List<AEDir> listaDirs = new ArrayList<>();

        for (AEDir aeDir : AEDir.values()) {
            listaDirs.add(aeDir);
        }

        return listaDirs;
    }


    /**
     * Lista di enumerations che vengono regolate SOLO inizialmente alla creazione della view Wizard <br>
     * Partono dal valore di System.getProperty("user.dir") <br>
     */
    public static List<AEDir> getElaborabili() {
        List<AEDir> listaDirs = new ArrayList<>();

        for (AEDir aeDir : AEDir.values()) {
            if (aeDir.isElaborabile()) {
                listaDirs.add(aeDir);
            }
        }

        return listaDirs;
    }


    /**
     * Lista di enumerations che vengono regolate all'uscita di un dialogo: <br>
     * WizDialogNewProject, WizDialogUpdateProject <br>
     */
    public static List<AEDir> getModificabiliProject() {
        List<AEDir> listaDirs = new ArrayList<>();

        for (AEDir aeDir : AEDir.values()) {
            if (aeDir.isModificabileProject()) {
                listaDirs.add(aeDir);
            }
        }

        return listaDirs;
    }


    /**
     * Lista di enumerations che vengono regolate all'uscita di un dialogo: <br>
     * WizDialogNewPackage, WizDialogUpdatePackage <br>
     */
    public static List<AEDir> getModificabiliPackage() {
        List<AEDir> listaDirs = new ArrayList<>();

        for (AEDir aeDir : AEDir.values()) {
            if (aeDir.isModificabilePackage()) {
                listaDirs.add(aeDir);
            }
        }

        return listaDirs;
    }


    /**
     * Visualizzazione di controllo <br>
     */
    public static void printInfo(String posizione) {
//        if (FLAG_DEBUG_WIZ) {
//            System.out.println("********************");
//            System.out.println("AEDir  - " + posizione);
//            System.out.println("********************");
//            for (AEDir aeDir : AEDir.values()) {
//                System.out.println("AEDir." + aeDir.name() + " \"" + aeDir.getDescrizione() + "\" = " + aeDir.get());
//            }
//            System.out.println("");
//        }
    }


    /**
     * Elaborazione iniziale di una enumeration 'elaborabile', partendo dal pathCurrent <br>
     * Sovrascritto nella enumeration specifica <br>
     *
     * @param pathCurrent directory dove gira il programma in uso. Recuperata dal System.
     */
    public void elabora(String pathCurrent) {
    }


    /**
     * Elaborazione di ogni enumeration 'modificabile', partendo da projectName <br>
     * Chiamato (di solito) alla dismissione di un dialogo: <br>
     * Sovrascritto nella enumeration specifica <br>
     *
     * @param projectName usato per regolare le istanze modificabili della enumeration. Recuperato dal dialogo.
     */
    public boolean modificaProject(String projectName) {
        return true;
    }


    /**
     * Elaborazione di ogni enumeration 'modificabile', partendo da packageName <br>
     * Chiamato (di solito) alla dismissione di un dialogo: <br>
     * Sovrascritto nella enumeration specifica <br>
     *
     * @param packageName usato per regolare le istanze modificabili della enumeration. Recuperato dal dialogo.
     */
    public boolean modificaPackage(String packageName) {
        return true;
    }


    public void setFile(FileService file) {
        this.file = file;
    }


    public void setText(TextService text) {
        this.text = text;
    }


    public void setLogger(ALogService logger) {
        this.logger = logger;
    }


    public boolean isElaborabile() {
        return elaborabile;
    }


    public void setElaborabile(boolean elaborabile) {
        this.elaborabile = elaborabile;
    }


    public boolean isModificabileProject() {
        return modificabileProject;
    }


    public void setModificabileProject(boolean modificabileProject) {
        this.modificabileProject = modificabileProject;
    }


    public boolean isModificabilePackage() {
        return modificabilePackage;
    }


    public void setModificabilePackage(boolean modificabilePackage) {
        this.modificabilePackage = modificabilePackage;
    }


    public String getDescrizione() {
        return descrizione;
    }


    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }


    public String get() {
        return value;
    }


    public void setValue(String value) {
        this.value = value;
    }


    @Component
    public static class ServiceInjector {

        @Autowired
        private TextService text;

        @Autowired
        private FileService file;

        @Autowired
        private ALogService logger;


        @PostConstruct
        public void postConstruct() {
            for (AEDir aeDir : AEDir.values()) {
                aeDir.setText(text);
                aeDir.setFile(file);
                aeDir.setLogger(logger);
            }
        }

    }
}
