package it.algos.vaadflow14.wizard.enumeration;

import it.algos.vaadflow14.backend.application.*;
import static it.algos.vaadflow14.backend.application.FlowCost.SLASH;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.service.*;
import static it.algos.vaadflow14.wizard.scripts.WizCost.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import javax.annotation.*;
import java.util.*;

/**
 * Project vaadwiki14
 * Created by Algos
 * User: gac
 * Date: lun, 18-gen-2021
 * Time: 07:09
 * <p>
 * Enumeration di costanti statiche praticamente fisse <br>
 * Possono essere (eventualmente) modificate se modifico la struttura delle directory che uso <br>
 * <p>
 * Enumeration di property variabili in funzione del target di project o di package <br>
 * <p>
 * Gestite come Enumeration per tenerle sotto controllo, spazzolarle e stamparle tutte insieme <br>
 */
public enum AEWizCost {

    //==========================================================================
    // Valori costanti. Hardcoded su di un singolo computer.
    //==========================================================================
    /**
     * Root iniziale. Hardcoded su di un singolo computer. Nei Documents di Gac. <br>
     * Valore standard <br>
     * Tutte le enums il cui nome inizia con 'path', iniziano e finiscono con uno SLASH <br>
     */
    pathRoot(AEWizValue.costante, AEWizUso.nullo, AEWizCopy.path, "Root iniziale del computer utilizzato", "/Users/gac/Documents/"),

    /**
     * Cartella base dei progetti. Hardcoded su di un singolo computer. Nei Documents di Gac. <br>
     * Tutte le enums il cui nome inizia con 'dir', finiscono con uno SLASH <br>
     */
    dirProjects(AEWizValue.costante, AEWizUso.nullo, AEWizCopy.dir, "Cartella di partenza dei projects Idea", "IdeaProjects/"),

    /**
     * Percorso base dei progetti. Nei Documents di Gac. <br>
     * Tutte le enums il cui nome inizia con 'path', iniziano e finiscono con uno SLASH <br>
     */
    pathProjectsDirStandard(AEWizValue.costante, AEWizUso.nullo, AEWizCopy.path, "Path base dei projects Idea", pathRoot.value + dirProjects.value),

    /**
     * Cartella dei progetti operativi. <br>
     * Tutte le enums il cui nome inizia con 'dir', finiscono con uno SLASH <br>
     */
    dirOperativi(AEWizValue.costante, AEWizUso.nullo, AEWizCopy.dir, "Cartella dei projects operativi", "operativi/"),

    /**
     * Percorso dei progetti operativi. Nei Documents di Gac <br>
     * Tutte le enums il cui nome inizia con 'path', iniziano e finiscono con uno SLASH <br>
     */
    pathOperativiDirStandard(AEWizValue.costante, AEWizUso.nullo, AEWizCopy.path, "Path dei projects operativi", pathProjectsDirStandard.value + dirOperativi.value),

    /**
     * Nome del progetto base vaadflow14. <br>
     * Tutte le enums il cui nome NON inizia con 'path' sono nomi o files o sub-directory, non path completi <br>
     */
    nameVaadFlow14Upper(AEWizValue.costante, AEWizUso.nullo, AEWizCopy.nome, "Nome del progetto base vaadflow14", "Vaadflow14"),

    /**
     * Nome del progetto base vaadflow14. <br>
     * Tutte le enums il cui nome NON inizia con 'path' sono nomi o files o sub-directory, non path completi <br>
     */
    nameVaadFlow14Lower(AEWizValue.costante, AEWizUso.nullo, AEWizCopy.nome, "Nome del progetto base vaadflow14", "vaadflow14"),

    /**
     * Cartella del progetto base vaadflow14. <br>
     * Tutte le enums il cui nome inizia con 'dir', finiscono con uno SLASH <br>
     */
    dirVaadFlow14(AEWizValue.costante, AEWizUso.nullo, AEWizCopy.dir, "Cartella del progetto base vaadflow14", nameVaadFlow14Upper.value.toLowerCase() + SLASH),

    /**
     * Percorso del progetto base vaadflow14. Nei Documents di Gac <br>
     * Tutte le enums il cui nome inizia con 'path', iniziano e finiscono con uno SLASH <br>
     */
    pathVaadFlow14Root(AEWizValue.costante, AEWizUso.nullo, AEWizCopy.path, "Path root del progetto base vaadflow14", pathOperativiDirStandard.value + dirVaadFlow14.value),

    /**
     * Cartella a livello di root. <br>
     * Tutte le enums il cui nome inizia con 'dir', finiscono con uno SLASH <br>
     */
    dirMain(AEWizValue.costante, AEWizUso.nullo, AEWizCopy.dir, "Directory files main", "src/main/"),

    /**
     * Cartella a livello di modulo. <br>
     * Tutte le enums il cui nome inizia con 'dir', finiscono con uno SLASH <br>
     */
    dirModulo(AEWizValue.costante, AEWizUso.nullo, AEWizCopy.dir, "Directory files modulo", dirMain.value + "java/it/algos/"),

    /**
     * Cartella a livello di resources. <br>
     * Tutte le enums il cui nome inizia con 'dir', finiscono con uno SLASH <br>
     */
    dirResources(AEWizValue.costante, AEWizUso.nullo, AEWizCopy.dir, "Directory resources", dirMain.value + "resources/"),

    /**
     * Cartella a livello di root. <br>
     * Tutte le enums il cui nome inizia con 'dir', finiscono con uno SLASH <br>
     */
    dirRootConfig(AEWizValue.costante, AEWizUso.flagProject, AEWizCopy.dir, String.format("Directory root CONFIG di risorse on-line esterne al JAR (da %s)", nameVaadFlow14Upper.value), "config/", AECopyWiz.dirAddingOnly, "", ""),

    /**
     * Cartella a livello di root. <br>
     * Tutte le enums il cui nome inizia con 'dir', finiscono con uno SLASH <br>
     */
    dirRootDoc(AEWizValue.costante, AEWizUso.flagProject, AEWizCopy.dir, String.format("Directory root DOC di documentazione (da %s)", nameVaadFlow14Upper.value), "doc/", AECopyWiz.dirAddingOnly),

    /**
     * Cartella a livello di root. <br>
     * contiene images/ (di solito)
     * contiene src/ (di solito)
     * contiene styles/ (sempre)
     * Tutte le enums il cui nome inizia con 'dir', finiscono con uno SLASH <br>
     */
    dirRootFrontend(AEWizValue.costante, AEWizUso.flagProject, AEWizCopy.dir, String.format("Directory root FRONTEND per il Client (da %s)", nameVaadFlow14Upper.value), "frontend/", AECopyWiz.dirAddingOnly),

    /**
     * Cartella a livello di root. <br>
     * Tutte le enums il cui nome inizia con 'dir', finiscono con uno SLASH <br>
     */
    dirRootLinks(AEWizValue.costante, AEWizUso.flagProject, AEWizCopy.dir, String.format("Directory root LINKS a siti web utili (da %s)", nameVaadFlow14Upper.value), "links/", AECopyWiz.dirAddingOnly),

    /**
     * Cartella a livello di root. <br>
     * Tutte le enums il cui nome inizia con 'dir', finiscono con uno SLASH <br>
     */
    dirRootSnippets(AEWizValue.costante, AEWizUso.flagProject, AEWizCopy.dir, String.format("Directory root SNIPPETS di codice suggerito (da %s)", nameVaadFlow14Upper.value), "snippets/", AECopyWiz.dirAddingOnly),

    /**
     * Cartella a livello di modulo. <br>
     * Tutte le enums il cui nome inizia con 'dir', finiscono con uno SLASH <br>
     */
    dirModuloVaadFlow14(AEWizValue.costante, AEWizUso.flagProject, AEWizCopy.dir, String.format("Directory modulo BASE %s (da %s, Wizard compreso)", nameVaadFlow14Upper.value, nameVaadFlow14Upper.value), dirModulo.value + dirVaadFlow14.value, AECopyWiz.dirDeletingAll, VUOTA, VUOTA),

    /**
     * Percorso del modulo base vaadflow14. Nei Documents di Gac <br>
     * Tutte le enums il cui nome inizia con 'path', iniziano e finiscono con uno SLASH <br>
     */
    pathVaadFlow14Modulo(AEWizValue.costante, AEWizUso.nullo, AEWizCopy.path, "Path del modulo base vaadflow14", pathVaadFlow14Root.value + dirModuloVaadFlow14.value),

    /**
     * Cartella. <br>
     * Tutte le enums il cui nome inizia con 'dir', finiscono con uno SLASH <br>
     */
    dirEnum(AEWizValue.costante, AEWizUso.nullo, AEWizCopy.dir, "Nome della directory backend", "enumeration/"),

    /**
     * Cartella. <br>
     * Tutte le enums il cui nome inizia con 'dir', finiscono con uno SLASH <br>
     */
    dirRootWizard(AEWizValue.costante, AEWizUso.nullo, AEWizCopy.dir, "Directory wizard", dirModuloVaadFlow14.value + "wizard/"),

    /**
     * Percorso della directory wizard sources di vaadflow14. Nei Documents di Gac <br>
     * Tutte le enums il cui nome inizia con 'path', iniziano e finiscono con uno SLASH <br>
     */
    pathVaadFlow14Wizard(AEWizValue.costante, AEWizUso.nullo, AEWizCopy.path, "Directory vaadflow14.wizard", pathVaadFlow14Root.value + dirRootWizard.value),

    /**
     * Cartella. <br>
     * Tutte le enums il cui nome inizia con 'dir', finiscono con uno SLASH <br>
     */
    dirRootWizardEnumeration(AEWizValue.costante, AEWizUso.nullo, AEWizCopy.dir, "Directory wizard.enumeration", dirRootWizard.value + dirEnum.value),

    /**
     * Cartella. <br>
     * Tutte le enums il cui nome inizia con 'dir', finiscono con uno SLASH <br>
     */
    dirRootWizardScripts(AEWizValue.costante, AEWizUso.nullo, AEWizCopy.dir, "Directory wizard.scripts", dirRootWizard.value + "scripts/"),

    /**
     * Cartella. <br>
     * Tutte le enums il cui nome inizia con 'dir', finiscono con uno SLASH <br>
     */
    dirVaadFlow14WizardSources(AEWizValue.costante, AEWizUso.nullo, AEWizCopy.dir, "Directory sources", dirRootWizard.value + "sources/"),

    /**
     * Cartella. <br>
     * Tutte le enums il cui nome inizia con 'dir', finiscono con uno SLASH <br>
     */
    dirWizardSources(AEWizValue.costante, AEWizUso.nullo, AEWizCopy.dir, "Directory wizard/sources", "wizard/sources/"),

    /**
     * Percorso della directory wizard sources di vaadflow14. Nei Documents di Gac <br>
     * Tutte le enums il cui nome inizia con 'path', iniziano e finiscono con uno SLASH <br>
     */
    pathVaadFlow14WizSources(AEWizValue.costante, AEWizUso.nullo, AEWizCopy.path, "Directory vaadflow14.wizard.sources", pathVaadFlow14Root.value + dirVaadFlow14WizardSources.value),

    /**
     * Cartella. <br>
     * Tutte le enums il cui nome inizia con 'dir', finiscono con uno SLASH <br>
     */
    dirBackend(AEWizValue.costante, AEWizUso.nullo, AEWizCopy.dir, "Nome della directory backend", "backend/"),

    /**
     * Cartella. <br>
     * Tutte le enums il cui nome inizia con 'dir', finiscono con uno SLASH <br>
     */
    dirApplication(AEWizValue.costante, AEWizUso.nullo, AEWizCopy.path, "Nome della directory application del modulo target", dirBackend.get() + "application/"),

    /**
     * Cartella. <br>
     * Tutte le enums il cui nome inizia con 'dir', finiscono con uno SLASH <br>
     */
    dirBoot(AEWizValue.costante, AEWizUso.nullo, AEWizCopy.dir, "Nome della directory boot", dirBackend.get() + "boot/"),

    /**
     * Cartella. <br>
     * Tutte le enums il cui nome inizia con 'dir', finiscono con uno SLASH <br>
     */
    dirData(AEWizValue.costante, AEWizUso.nullo, AEWizCopy.dir, "Nome della directory data del modulo target", dirBackend.get() + "data/"),

    /**
     * Cartella. <br>
     * Tutte le enums il cui nome inizia con 'dir', finiscono con uno SLASH <br>
     */
    dirBackEnum(AEWizValue.costante, AEWizUso.nullo, AEWizCopy.dir, "Nome della directory backend/enumeration del modulo target", dirBackend.get() + dirEnum.value),

    /**
     * Cartella. <br>
     * Tutte le enums il cui nome inizia con 'dir', finiscono con uno SLASH <br>
     */
    dirPackages(AEWizValue.costante, AEWizUso.nullo, AEWizCopy.dir, "Nome della directory packages del modulo target", dirBackend.get() + "packages/"),

    /**
     * Cartella. <br>
     * Tutte le enums il cui nome inizia con 'dir', finiscono con uno SLASH <br>
     */
    dirUI(AEWizValue.costante, AEWizUso.nullo, AEWizCopy.dir, "Nome della directory UI del modulo target", "ui/"),

    /**
     * Cartella. <br>
     * Tutte le enums il cui nome inizia con 'dir', finiscono con uno SLASH <br>
     */
    dirUiEnum(AEWizValue.costante, AEWizUso.nullo, AEWizCopy.dir, "Nome della directory ui/enumeration del modulo target", dirUI.get() + dirEnum.value),

    /**
     * Nome della directory sources. <br>
     * Tutte le enums il cui nome NON inizia con 'path' sono nomi o files o sub-directory, non path completi <br>
     */
    dirSources(AEWizValue.costante, AEWizUso.nullo, AEWizCopy.dir, "Nome della directory sources", "../vaadflow14/wizard/sources"),

    /**
     * Nome del file wizard. <br>
     * Tutte le enums il cui nome NON inizia con 'path' sono nomi o files o sub-directory, non path completi <br>
     */
    fileWizard(AEWizValue.costante, AEWizUso.nullo, AEWizCopy.file, "Nome del file wizard", "Wizard.java"),

    /**
     * Cartella. <br>
     * Tutte le enums il cui nome inizia con 'dir', finiscono con uno SLASH <br>
     */
    dirMetaInf(AEWizValue.costante, AEWizUso.flagProject, AEWizCopy.dir, String.format("Directory META-INF (da %s)", nameVaadFlow14Upper.value), dirResources.value + "META-INF", AECopyWiz.dirAddingOnly, "", "resources"),

    /**
     * File. <br>
     * Tutte le enums il cui nome NON inizia con 'path' sono nomi o files o sub-directory, non path completi <br>
     */
    sourceProperties(AEWizValue.costante, AEWizUso.flagProject, AEWizCopy.source, String.format("File application.properties (da %s)", dirSources.value), dirResources.value + "application.properties", AECopyWiz.sourceCheckFlagSeEsiste, "properties", dirMain.value),

    /**
     * File. <br>
     * Tutte le enums il cui nome NON inizia con 'path' sono nomi o files o sub-directory, non path completi <br>
     */
    sourceBanner(AEWizValue.costante, AEWizUso.flagProject, AEWizCopy.source, String.format("File banner (da %s)", dirSources.value), dirResources.value + "banner.txt", AECopyWiz.sourceSoloSeNonEsiste, "banner.txt"),

    /**
     * File a livello di root. <br>
     * Tutte le enums il cui nome NON inizia con 'path' sono nomi o files o sub-directory, non path completi <br>
     */
    sourceRootGit(AEWizValue.costante, AEWizUso.flagProject, AEWizCopy.source, String.format("File root GIT di esclusione (da %s)", dirSources.value), ".gitignore", AECopyWiz.sourceSovrascriveSempreAncheSeEsiste),

    /**
     * File a livello di root. <br>
     * Tutte le enums il cui nome NON inizia con 'path' sono nomi o files o sub-directory, non path completi <br>
     */
    sourceRootPOM(AEWizValue.costante, AEWizUso.flagProject, AEWizCopy.source, String.format("File root POM.xml di Maven (da %s)", dirSources.value), "pom.xml", AECopyWiz.sourceCheckFlagSeEsiste, "pom"),

    /**
     * File a livello di root. <br>
     * Tutte le enums il cui nome NON inizia con 'path' sono nomi o files o sub-directory, non path completi <br>
     */
    sourceRootREAD(AEWizValue.costante, AEWizUso.flagProject, AEWizCopy.source, String.format("File root README con note di testo (da %s)", dirSources.value), "README.txt", AECopyWiz.sourceCheckFlagSeEsiste),

    /**
     * File di test. <br>
     * Tutte le enums il cui nome NON inizia con 'path' sono nomi o files o sub-directory, non path completi <br>
     */
    fileRootTEST(AEWizValue.costante, AEWizUso.flagProject, AEWizCopy.file, String.format("File root/test/ATEST (da %s)", nameVaadFlow14Upper.value), "src/test/java/it/algos/test/ATest.java", AECopyWiz.fileSovrascriveSempreAncheSeEsiste),

    /**
     * Cartella. <br>
     * Tutte le enums il cui nome inizia con 'dir', finiscono con uno SLASH <br>
     */
    dirTestUnit(AEWizValue.costante, AEWizUso.flagProject, AEWizCopy.dir, String.format("Directory root/test/UNIT (da %s)", nameVaadFlow14Upper.value), "src/test/java/it/algos/unit", AECopyWiz.dirAddingOnly, VUOTA, "test"),

    //==========================================================================
    // Valori calcolati automaticamente dal programma alla partenza del Wizard.
    //==========================================================================
    /**
     * Regolata inizialmente dal system, indipendentemente dall'apertura di un dialogo. <br>
     * Programmatore (magari serve) <br>
     * Tutte le enums il cui nome NON inizia con 'path' sono nomi o files o sub-directory, non path completi <br>
     */
    nameUser(AEWizValue.calcolato, AEWizUso.nullo, AEWizCopy.nome, "Programmatore. Ricavato dal path della directory corrente.", VALORE_MANCANTE) {
        @Override
        public void fixValue() {
            String pathCurrent = System.getProperty("user.dir") + SLASH;
            String user = pathCurrent.substring(1);
            user = text.levaTestoPrimaDi(user, SLASH);
            this.value = user.substring(0, user.indexOf(SLASH));
            this.setValida(true);
        }
    },

    /**
     * Regolata inizialmente dal system, indipendentemente dall'apertura di un dialogo. <br>
     * Tutte le enums il cui nome inizia con 'path', iniziano e finiscono con uno SLASH <br>
     */
    pathCurrentProjectRoot(AEWizValue.calcolato, AEWizUso.nullo, AEWizCopy.path, "Path dove gira il programma in uso. Recuperata dal System", VALORE_MANCANTE) {
        @Override
        public void fixValue() {
            this.value = System.getProperty("user.dir") + SLASH;
            this.setValida(true);
        }
    },

    /**
     * Regolata inizialmente dal system, indipendentemente dall'apertura di un dialogo. <br>
     * Ricavato da backend.boot.xxxBoot.fixVariabili() <br>
     * Confrontato col valore estratto da pathCurrentProject che deve essere uguale <br>
     * In caso contrario genera un errore <br>
     * Tutte le enums il cui nome NON inizia con 'path' sono nomi o files o sub-directory, non path completi <br>
     */
    nameCurrentProjectDirectoryIdea(AEWizValue.calcolato
            , AEWizUso.nullo, AEWizCopy.nome, "Directory del progetto Idea. Ricavato da backend.boot.xxxBoot.fixVariabili()", VALORE_MANCANTE) {
        @Override
        public void fixValue() {
            String valueSystem;
            String message;
            this.value = FlowVar.projectNameDirectoryIdea;
            this.setValida(true);

            pathCurrentProjectRoot.fixValue();
            if (pathCurrentProjectRoot.valida) {
                valueSystem = file.estraeDirectoryFinaleSenzaSlash(pathCurrentProjectRoot.get()).toLowerCase();
                if (text.isValid(this.value)) {
                    if (!valueSystem.equals(this.value)) {
                        message = String.format("FlowVar.projectNameDirectoryIdea=%s mentre il programma gira in %s", this.value, valueSystem);
                        logger.log(AETypeLog.wizard, message);
                        this.value = ERRORE;
                    }
                }
                else {
                    message = String.format("Il programma gira in %s ma manca il valore di FlowVar.projectNameDirectoryIdea come controllo", valueSystem);
                    logger.log(AETypeLog.wizard, message);
                    this.value = ERRORE;
                }
            }
        }
    },

    /**
     * Regolata inizialmente dal system, indipendentemente dall'apertura di un dialogo. <br>
     * Tutte le enums il cui nome NON inizia con 'path' sono nomi o files o sub-directory, non path completi <br>
     */
    nameCurrentProjectModulo(AEWizValue.calcolato
            , AEWizUso.nullo, AEWizCopy.nome, "Nome del modulo del progetto. Ricavato da backend.boot.xxxBoot.fixVariabili()", VALORE_MANCANTE) {
        @Override
        public void fixValue() {
            String valueSystem;
            String message;
            this.value = FlowVar.projectNameModulo;
            this.setValida(true);

            pathCurrentProjectRoot.fixValue();
            if (pathCurrentProjectRoot.valida) {
                valueSystem = file.estraeDirectoryFinaleSenzaSlash(pathCurrentProjectRoot.get()).toLowerCase();
                if (text.isValid(this.value)) {
                    //                    if (!valueSystem.equals(this.value)) {
                    //                        message = String.format("FlowVar.projectNameModulo=%s mentre il programma gira in %s", this.value, valueSystem);
                    //                        logger.log(AETypeLog.wizard, message);
                    //                        this.value = ERRORE;
                    //                    }
                }
                else {
                    message = String.format("Il programma gira in %s ma manca il valore di FlowVar.projectNameModulo come controllo", valueSystem);
                    logger.log(AETypeLog.wizard, message);
                    this.value = ERRORE;
                }
            }
        }
    },

    /**
     * Regolata inizialmente dal system, indipendentemente dall'apertura di un dialogo. <br>
     * Tutte le enums il cui nome inizia con 'path', iniziano e finiscono con uno SLASH <br>
     */
    pathCurrentProjectModulo(AEWizValue.calcolato, AEWizUso.nullo, AEWizCopy.path, "Path dove gira il programma in uso. Recuperata dal System", VALORE_MANCANTE) {
        @Override
        public void fixValue() {
            pathCurrentProjectRoot.fixValue();
            nameCurrentProjectModulo.fixValue();
            if (pathCurrentProjectRoot.valida && nameCurrentProjectModulo.valida) {
                this.value = pathCurrentProjectRoot.get() + AEWizCost.dirModulo.get() + nameCurrentProjectModulo.get() + SLASH;
                this.setValida(true);
            }
        }
    },

    /**
     * Regolata inizialmente dal system, indipendentemente dall'apertura di un dialogo. <br>
     * Tutte le enums il cui nome NON inizia con 'path' sono nomi o files o sub-directory, non path completi <br>
     */
    nameCurrentProjectUpper(AEWizValue.calcolato, AEWizUso.nullo, AEWizCopy.nome, "Nome maiuscolo dell' applicazione. Ricavato da backend.boot.xxxBoot.fixVariabili()", VALORE_MANCANTE) {
        @Override
        public void fixValue() {
            if (text.isValid(FlowVar.projectNameUpper)) {
                this.value = FlowVar.projectNameUpper;
                this.setValida(true);
            }
            else {
                logger.log(AETypeLog.wizard, "Manca il nome del progetto in FlowVar.projectNameUpper");
                this.value = ERRORE;
                this.setValida(false);
            }
        }
    },

    //==========================================================================
    // Valori inseriti dall'utente nei dialoghi.
    //==========================================================================
    /**
     * Root del progetto target. <br>
     * Può essere diverso dal valore di nameTargetProjectUpper (Es. ../vaadFlow14 e ../simple, ../company, ../security) <br>
     * Tutte le enums il cui nome inizia con 'path', iniziano e finiscono con uno SLASH <br>
     */
    pathTargetProjectRoot(AEWizValue.inserito, AEWizUso.nullo, AEWizCopy.path, "Path root del progetto target", VALORE_MANCANTE),

    /**
     * Nome della directory e del modulo del progetto target. <br>
     * Può essere diverso dal valore di nameTargetProjectUpper (Es. vaadwiki e Wiki) <br>
     * Tutte le enums il cui nome NON inizia con 'path' sono nomi o files o sub-directory, non path completi <br>
     */
    nameTargetProjectModulo(AEWizValue.inserito, AEWizUso.nullo, AEWizCopy.nome, "Nome minuscolo del modulo target", VALORE_MANCANTE),

    /**
     * Nome del progetto target. <br>
     * Può essere diverso dal valore di nameTargetProjectModulo (Es. vaadwiki e Wiki)  <br>
     * Tutte le enums il cui nome NON inizia con 'path' sono nomi o files o sub-directory, non path completi <br>
     */
    nameTargetProjectUpper(AEWizValue.inserito, AEWizUso.nullo, AEWizCopy.nome, "Nome maiuscolo del progetto target", VALORE_MANCANTE),

    /**
     * Nome del package da creare. Eventualmente con sub-directory (separatore punto) <br>
     * Tutte le enums il cui nome NON inizia con 'path' sono nomi o files o sub-directory, non path completi <br>
     */
    nameTargetPackagePunto(AEWizValue.inserito, AEWizUso.nullo, AEWizCopy.nome, "Nome del package da creare/modificare usando punto", VALORE_MANCANTE),

    //==========================================================================
    // Valori derivati in automatico dai valori inseriti dall'utente.
    //==========================================================================
    /**
     * Modulo del progetto target. <br>
     * Tutte le enums il cui nome inizia con 'path', iniziano e finiscono con uno SLASH <br>
     */
    pathTargetProjectModulo(AEWizValue.derivato, AEWizUso.nullo, AEWizCopy.path, "Directory MODULO del progetto", VALORE_MANCANTE, AECopyWiz.dirAddingOnly) {
        @Override
        public void fixValue() {
            if (text.isValid(pathTargetProjectRoot.get()) && text.isValid(nameTargetProjectModulo.get())) {
                this.value = pathTargetProjectRoot.get() + AEWizCost.dirModulo.get() + nameTargetProjectModulo.get() + FlowCost.SLASH;
                this.setValida(true);
            }
        }
    },

    /**
     * Nome minuscolo del progetto target. Di norma ( e sempre per i nuovi progetti) è uguale a nameTargetProjectModulo. <br>
     * Può differire nella enumeration AEProgetto <br>
     * Tutte le enums il cui nome NON inizia con 'path' sono nomi o files o sub-directory, non path completi <br>
     */
    nameTargetProjectLower(AEWizValue.derivato, AEWizUso.nullo, AEWizCopy.nome, "Nome minuscolo del progetto target", VALORE_MANCANTE) {
        @Override
        public void fixValue() {
            if (text.isValid(nameTargetProjectUpper.get())) {
                this.value = text.primaMinuscola(nameTargetProjectUpper.get());
                this.setValida(true);
            }
        }
    },

    /**
     * Percorso della directory boot. <br>
     * Tutte le enums il cui nome inizia con 'path', iniziano e finiscono con uno SLASH <br>
     */
    pathTargetProjectBoot(AEWizValue.derivato, AEWizUso.nullo, AEWizCopy.path, "Directory target boot", pathTargetProjectModulo.value + dirBoot.value) {
        @Override
        public void fixValue() {
            pathTargetProjectModulo.fixValue();
            if (pathTargetProjectModulo.valida) {
                this.value = pathTargetProjectModulo.get() + AEWizCost.dirBoot.get();
                this.setValida(true);
            }
        }
    },

    /**
     * Percorso della directory packages. <br>
     * Tutte le enums il cui nome inizia con 'path', iniziano e finiscono con uno SLASH <br>
     */
    pathTargetProjectPackages(AEWizValue.derivato, AEWizUso.nullo, AEWizCopy.path, "Directory target packages", pathTargetProjectModulo.value + dirPackages.value) {
        @Override
        public void fixValue() {
            pathTargetProjectModulo.fixValue();
            if (pathTargetProjectModulo.valida) {
                this.value = pathTargetProjectModulo.get() + AEWizCost.dirPackages.get();
                this.setValida(true);
            }
        }
    },

    /**
     * Percorso della directory target sources. <br>
     * Tutte le enums il cui nome inizia con 'path', iniziano e finiscono con uno SLASH <br>
     */
    pathTargetProjectSources(AEWizValue.derivato, AEWizUso.nullo, AEWizCopy.path, "Directory target sources (da cancellare)", pathTargetProjectModulo.value + "wizard/sources/") {
        @Override
        public void fixValue() {
            pathTargetProjectRoot.fixValue();
            if (pathTargetProjectRoot.valida) {
                this.value = pathTargetProjectRoot.get() + AEWizCost.dirVaadFlow14WizardSources.get();
                this.setValida(true);
            }
        }
    },

    /**
     * Nome del package da creare. Eventualmente con sub-directory (separatore slash) <br>
     * Tutte le enums il cui nome NON inizia con 'path' sono nomi o files o sub-directory, non path completi <br>
     */
    nameTargetPackage(AEWizValue.derivato, AEWizUso.nullo, AEWizCopy.nome, "Nome del package da creare/modificare usando slash", VALORE_MANCANTE) {
        @Override
        public void fixValue() {
            nameTargetPackagePunto.fixValue();
            if (nameTargetPackagePunto.valida) {
                this.value = text.fixPuntoToSlash(nameTargetPackagePunto.get()).toLowerCase();
                this.setValida(true);
            }
        }
    },

    /**
     * Nome del file da creare. Singolo file. Senza sub-directory. <br>
     * Tutte le enums il cui nome NON inizia con 'path' sono nomi o files o sub-directory, non path completi <br>
     */
    nameTargetFileUpper(AEWizValue.derivato, AEWizUso.nullo, AEWizCopy.nome, "Nome del file da creare/modificare con iniziale maiuscola", VALORE_MANCANTE) {
        @Override
        public void fixValue() {
            String fileName;

            nameTargetPackage.fixValue();
            if (nameTargetPackage.valida) {
                fileName = text.levaTestoPrimaDi(nameTargetPackage.get(), SLASH);
                this.value = text.primaMaiuscola(fileName);
                this.setValida(true);
            }
        }
    },

    /**
     * Percorso del package. <br>
     * Tutte le enums il cui nome inizia con 'path', iniziano e finiscono con uno SLASH <br>
     */
    pathTargetPackageSlash(AEWizValue.derivato, AEWizUso.nullo, AEWizCopy.path, "Directory del package da creare/modificare", VALORE_MANCANTE) {
        @Override
        public void fixValue() {
            pathTargetProjectPackages.fixValue();
            nameTargetPackage.fixValue();
            if (pathTargetProjectPackages.valida && nameTargetPackage.valida) {
                this.value = pathTargetProjectPackages.get() + AEWizCost.nameTargetPackage.get() + SLASH;
                this.setValida(true);
            }
        }
    },

    ;


    protected String value;

    protected String valoreIniziale;

    protected FileService file;

    protected TextService text;

    protected ALogService logger;

    private AEWizValue wizValue;

    private AEWizCopy typeFile;

    private AEWizUso wizUso;

    private String descrizione;

    private String sourcesName;

    private String pathBreve;

    private boolean acceso;

    private AECopyWiz copyWiz;

    private boolean valida;

    /**
     * Costruttore parziale <br>
     */
    AEWizCost(AEWizValue wizValue, AEWizUso wizUso, AEWizCopy typeFile, String descrizione, String value) {
        this(wizValue, wizUso, typeFile, descrizione, value, (AECopyWiz) null, VUOTA, VUOTA);
    }


    /**
     * Costruttore parziale <br>
     */
    AEWizCost(AEWizValue wizValue, AEWizUso wizUso, AEWizCopy typeFile, String descrizione, String value, AECopyWiz copyWiz) {
        this(wizValue, wizUso, typeFile, descrizione, value, copyWiz, value, VUOTA);
    }

    /**
     * Costruttore parziale <br>
     */
    AEWizCost(AEWizValue wizValue, AEWizUso wizUso, AEWizCopy typeFile, String descrizione, String value, AECopyWiz copyWiz, String sourcesNameSeDiversoDaValue) {
        this(wizValue, wizUso, typeFile, descrizione, value, copyWiz, sourcesNameSeDiversoDaValue, VUOTA);
    }


    /**
     * Costruttore completo <br>
     */
    AEWizCost(AEWizValue wizValue, AEWizUso wizUso, AEWizCopy typeFile, String descrizione, String value, AECopyWiz copyWiz, String sourcesNameSeDiversoDaValue, String pathBreve) {
        this.wizValue = wizValue;
        this.wizUso = wizUso;
        this.typeFile = typeFile;
        this.descrizione = descrizione;
        this.value = value;
        this.valoreIniziale = value;
        this.copyWiz = copyWiz;
        this.sourcesName = sourcesNameSeDiversoDaValue;
        this.pathBreve = pathBreve;
        //        this.newProject = newProject;
        //        this.updateProject = updateProject;
        //        this.accesoInizialmenteNew = accesoInizialmenteNew;
        ////        this.accesoInizialmenteNew = false;
        //        this.accesoInizialmenteUpdate = accesoInizialmenteUpdate;
        this.valida = !value.startsWith(VALORE_MANCANTE);
    }


    public static void fixValoriDerivati() {
        for (AEWizCost aeWizCost : AEWizCost.values()) {
            aeWizCost.fixValue();
        }
    }

    public static void reset() {
        for (AEWizCost aeWizCost : AEWizCost.values()) {
            aeWizCost.value = aeWizCost.valoreIniziale;
            aeWizCost.valida = false;
        }
    }


    /**
     * Estrae tutte le enumeration di un gruppo di valore <br>
     */
    public static List<AEWizCost> getWizCostByValue(AEWizValue wizValue) {
        List<AEWizCost> listaWiz = new ArrayList<>();

        for (AEWizCost wizCost : AEWizCost.values()) {
            if (wizCost.getWizValue() == wizValue) {
                listaWiz.add(wizCost);
            }
        }

        return listaWiz;
    }

    /**
     * Stampa tutte le enumeration del gruppo di valore <br>
     */
    public static void print(AEWizValue aeWizValue) {
        String message;
        List<AEWizCost> listaWiz = getWizCostByValue(aeWizValue);
        System.out.println(VUOTA);
        System.out.println("********************");
        System.out.println(aeWizValue.getDescrizione() + " (" + listaWiz.size() + ")");
        System.out.println("********************");
        for (AEWizCost aeWizCost : listaWiz) {
            message = String.format("AEWizCost.%s: \"%s\"%s%s", aeWizCost.name(), aeWizCost.getDescrizione(), FlowCost.UGUALE_SPAZIATO, aeWizCost.get());
            System.out.print(message);
            System.out.println(VUOTA);
        }
        System.out.println(VUOTA);
    }


    public void fixValue() {
    }

    public AEWizValue getWizValue() {
        return wizValue;
    }

    public AEWizUso getWizUso() {
        return wizUso;
    }

    public AEWizCopy getTypeFile() {
        return typeFile;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        this.valida = true;
    }

    public String getSourcesName() {
        return sourcesName;
    }

    public String getPathBreve() {
        return pathBreve;
    }

    public boolean isAcceso() {
        return acceso;
    }

    public void setAcceso(boolean acceso) {
        this.acceso = acceso;
    }

    public AECopyWiz getCopyWiz() {
        return copyWiz;
    }

    public boolean isValida() {
        return valida;
    }

    public void setValida(boolean valida) {
        this.valida = valida;
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

    @Component
    public static class WizCostServiceInjector {

        @Autowired
        private FileService file;

        @Autowired
        private TextService text;

        @Autowired
        private ALogService logger;


        @PostConstruct
        public void postConstruct() {
            for (AEWizCost aeWizCost : AEWizCost.values()) {
                aeWizCost.setFile(file);
                aeWizCost.setText(text);
                aeWizCost.setLogger(logger);
            }
        }

    }

}
