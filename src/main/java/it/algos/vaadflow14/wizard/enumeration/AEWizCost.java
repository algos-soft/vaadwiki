package it.algos.vaadflow14.wizard.enumeration;

import it.algos.vaadflow14.backend.application.*;
import static it.algos.vaadflow14.backend.application.FlowCost.SLASH;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
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

    /**
     * Root iniziale. Hardcoded su di un singolo computer. <br>
     * Valore standard che verrà controllato in funzione di AEDIR.pathCurrent effettivo <br>
     * Potrebbe essere diverso <br>
     * Tutte le enums il cui nome inizia con 'path', iniziano e finiscono con uno SLASH <br>
     */
    pathRoot(AEWizValue.costante, AEWizUso.nullo, AEWizCopy.path, "Root iniziale del computer utilizzato", "/Users/gac/Documents/"),

    /**
     * Cartella base dei progetti. <br>
     * Tutte le enums il cui nome inizia con 'dir', finiscono con uno SLASH <br>
     */
    dirProjects(AEWizValue.costante, AEWizUso.nullo, AEWizCopy.dir, "Cartella di partenza dei projects Idea", "IdeaProjects/"),

    /**
     * Percorso base dei progetti. Nei Documents di Gac <br>
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
    nameVaadFlow14(AEWizValue.costante, AEWizUso.nullo, AEWizCopy.nome, "Nome del progetto base vaadflow14", "Vaadflow14"),

    /**
     * Cartella del progetto base vaadflow14. <br>
     * Tutte le enums il cui nome inizia con 'dir', finiscono con uno SLASH <br>
     */
    dirVaadFlow14(AEWizValue.costante, AEWizUso.nullo, AEWizCopy.dir, "Cartella del progetto base vaadflow14", nameVaadFlow14.value.toLowerCase() + SLASH),

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
    dirRootConfig(AEWizValue.costante, AEWizUso.flagProject, AEWizCopy.dir, String.format("Directory root CONFIG di risorse on-line esterne al JAR (da %s)", nameVaadFlow14.value), "config/", AECopyWiz.dirAddingOnly, "", ""),

    /**
     * Cartella a livello di root. <br>
     * Tutte le enums il cui nome inizia con 'dir', finiscono con uno SLASH <br>
     */
    dirRootDoc(AEWizValue.costante, AEWizUso.flagProject, AEWizCopy.dir, String.format("Directory root DOC di documentazione (da %s)", nameVaadFlow14.value), "doc/", AECopyWiz.dirAddingOnly),

    /**
     * Cartella a livello di root. <br>
     * contiene images/ (di solito)
     * contiene src/ (di solito)
     * contiene styles/ (sempre)
     * Tutte le enums il cui nome inizia con 'dir', finiscono con uno SLASH <br>
     */
    dirRootFrontend(AEWizValue.costante, AEWizUso.flagProject, AEWizCopy.dir, String.format("Directory root FRONTEND per il Client (da %s)", nameVaadFlow14.value), "frontend/", AECopyWiz.dirAddingOnly),

    /**
     * Cartella a livello di root. <br>
     * Tutte le enums il cui nome inizia con 'dir', finiscono con uno SLASH <br>
     */
    dirRootLinks(AEWizValue.costante, AEWizUso.flagProject, AEWizCopy.dir, String.format("Directory root LINKS a siti web utili (da %s)", nameVaadFlow14.value), "links/", AECopyWiz.dirAddingOnly),

    /**
     * Cartella a livello di root. <br>
     * Tutte le enums il cui nome inizia con 'dir', finiscono con uno SLASH <br>
     */
    dirRootSnippets(AEWizValue.costante, AEWizUso.flagProject, AEWizCopy.dir, String.format("Directory root SNIPPETS di codice suggerito (da %s)", nameVaadFlow14.value), "snippets/", AECopyWiz.dirAddingOnly),

    /**
     * Cartella a livello di modulo. <br>
     * Tutte le enums il cui nome inizia con 'dir', finiscono con uno SLASH <br>
     */
    dirModuloVaadFlow14(AEWizValue.costante, AEWizUso.flagProject, AEWizCopy.dir, String.format("Directory modulo BASE %s (da %s, Wizard compreso)", nameVaadFlow14.value, nameVaadFlow14.value), dirModulo.value + dirVaadFlow14.value, AECopyWiz.dirDeletingAll, VUOTA, VUOTA),

    /**
     * Modulo del progetto target. <br>
     * Tutte le enums il cui nome inizia con 'path', iniziano e finiscono con uno SLASH <br>
     */
    pathTargetProjectModulo(AEWizValue.derivato, AEWizUso.flagProject, AEWizCopy.path, "Directory MODULO del progetto", VALORE_MANCANTE, AECopyWiz.dirAddingOnly) {
        @Override
        public void fixValue() {
            nameTargetProjectModulo.fixValue();
            if (nameTargetProjectModulo.valida) {
                this.value = pathTargetProjectRoot.get() + AEWizCost.dirModulo.get() + nameTargetProjectModulo.get() + FlowCost.SLASH;
                this.setValida(true);
            }
        }
    },

    /**
     * Percorso del modulo base vaadflow14. Nei Documents di Gac <br>
     * Tutte le enums il cui nome inizia con 'path', iniziano e finiscono con uno SLASH <br>
     */
    pathVaadFlow14Modulo(AEWizValue.costante, AEWizUso.nullo, AEWizCopy.path, "Path del modulo base vaadflow14", pathVaadFlow14Root.value + dirModuloVaadFlow14.value),

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
    dirRootWizardEnumeration(AEWizValue.costante, AEWizUso.nullo, AEWizCopy.dir, "Directory wizard.enumeration", dirRootWizard.value + "enumeration/"),

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
    dirEnumeration(AEWizValue.costante, AEWizUso.nullo, AEWizCopy.dir, "Nome della directory enumeration del modulo target", dirBackend.get() + "enumeration/"),


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
    dirMetaInf(AEWizValue.costante, AEWizUso.flagProject, AEWizCopy.dir, String.format("Directory META-INF (da %s)", nameVaadFlow14.value), dirResources.value + "META-INF", AECopyWiz.dirAddingOnly, "", "resources"),

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
     * File a livello di root. <br>
     * Tutte le enums il cui nome NON inizia con 'path' sono nomi o files o sub-directory, non path completi <br>
     */
    fileRootTEST(AEWizValue.costante, AEWizUso.flagProject, AEWizCopy.file, String.format("File root/test ATEST (da %s)", nameVaadFlow14.value), "src/test/java/it/algos/test/ATest.java", AECopyWiz.fileCheckFlagSeEsiste),

    /////////////////

    /**
     * Regolata inizialmente dal system, indipendentemente dall'apertura di un dialogo. <br>
     * Tutte le enums il cui nome inizia con 'path', iniziano e finiscono con uno SLASH <br>
     */
    pathCurrent(AEWizValue.calcolato, AEWizUso.nullo, AEWizCopy.path, "Directory dove gira il programma in uso. Recuperata dal System", VALORE_MANCANTE),

    /**
     * Regolata inizialmente dal system, indipendentemente dall'apertura di un dialogo. <br>
     * Tutte le enums il cui nome NON inizia con 'path' sono nomi o files o sub-directory, non path completi <br>
     */
    nameUser(AEWizValue.calcolato, AEWizUso.nullo, AEWizCopy.nome, "Programmatore. Ricavato dal path della directory corrente.", VALORE_MANCANTE),

    /**
     * Regolata inizialmente dal system, indipendentemente dall'apertura di un dialogo. <br>
     * Tutte le enums il cui nome NON inizia con 'path' sono nomi o files o sub-directory, non path completi <br>
     */
    nameProjectCurrentUpper(AEWizValue.inserito
            , AEWizUso.nullo, AEWizCopy.nome, "Nome maiuscolo del programma in uso. Ricavato dal path della directory corrente", VALORE_MANCANTE) {
    },

    /**
     * Regolata inizialmente dal system, indipendentemente dall'apertura di un dialogo. <br>
     * Tutte le enums il cui nome NON inizia con 'path' sono nomi o files o sub-directory, non path completi <br>
     */
    nameProjectCurrentLower(AEWizValue.calcolato
            , AEWizUso.nullo, AEWizCopy.nome, "Nome minuscolo del programma in uso. Ricavato dal path della directory corrente", VALORE_MANCANTE) {
        @Override
        public void fixValue() {
            if (nameProjectCurrentUpper.valida) {
                this.value = nameProjectCurrentUpper.get().toLowerCase();
                this.setValida(true);
            }
        }
    },

    /////////////////

    /**
     * Root del progetto target. <br>
     * Tutte le enums il cui nome inizia con 'path', iniziano e finiscono con uno SLASH <br>
     */
    pathTargetProjectRoot(AEWizValue.inserito, AEWizUso.nullo, AEWizCopy.path, "Path root del progetto target", VALORE_MANCANTE),

    /**
     * Nome della directory e del modulo del progetto target. Può essere diverso dal valore di nameTargetProjectUpper (Es. vaadwiki e Wiki) <br>
     * Tutte le enums il cui nome NON inizia con 'path' sono nomi o files o sub-directory, non path completi <br>
     */
    nameTargetProjectModulo(AEWizValue.derivato, AEWizUso.nullo, AEWizCopy.nome, "Nome minuscolo della directory e del modulo target", VALORE_MANCANTE) {
        @Override
        public void fixValue() {
            if (pathTargetProjectRoot.valida) {
                this.value = file.estraeClasseFinale(pathTargetProjectRoot.get()).toLowerCase(Locale.ROOT);
                this.setValida(true);
            }
        }
    },

    /**
     * Nome del progetto target. Di norma è uguale a nameTargetProjectModulo <br>
     * Tutte le enums il cui nome NON inizia con 'path' sono nomi o files o sub-directory, non path completi <br>
     */
    nameTargetProjectUpper(AEWizValue.inserito, AEWizUso.nullo, AEWizCopy.nome, "Nome maiuscolo del progetto target", VALORE_MANCANTE),

    /**
     * Nome minuscolo del progetto target. Di norma ( e sempre per i nuovi progetti) è uguale a nameTargetProjectModulo. <br>
     * Può differire nella enumeration AEProgetto <br>
     * Tutte le enums il cui nome NON inizia con 'path' sono nomi o files o sub-directory, non path completi <br>
     */
    nameTargetProjectLower(AEWizValue.derivato, AEWizUso.nullo, AEWizCopy.nome, "Nome minuscolo del progetto target", VALORE_MANCANTE) {
        @Override
        public void fixValue() {
            nameTargetProjectUpper.fixValue();
            if (nameTargetProjectUpper.valida) {
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
     * Nome del package da creare. Eventualmente con sub-directory (separatore punto) <br>
     * Tutte le enums il cui nome NON inizia con 'path' sono nomi o files o sub-directory, non path completi <br>
     */
    nameTargetPackagePunto(AEWizValue.inserito, AEWizUso.nullo, AEWizCopy.nome, "Nome del package da creare/modificare usando punto", VALORE_MANCANTE),

    /**
     * Nome del package da creare. Eventualmente con sub-directory (separatore slash) <br>
     * Tutte le enums il cui nome NON inizia con 'path' sono nomi o files o sub-directory, non path completi <br>
     */
    nameTargetPackage(AEWizValue.derivato, AEWizUso.nullo, AEWizCopy.nome, "Nome del package da creare/modificare usando slash", VALORE_MANCANTE) {
        @Override
        public void fixValue() {
            if (nameTargetPackagePunto.valida) {
                this.value = text.fixPuntoToSlash(nameTargetPackagePunto.get());
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

    protected AFileService file;

    protected ATextService text;

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
     * Visualizzazione di controllo <br>
     */
    public static void print(String titolo) {
        System.out.println(VUOTA);
        System.out.println("********************");
        System.out.println("AEWizCost  - " + titolo);
        System.out.println("********************");
        for (AEWizCost wiz : AEWizCost.values()) {
            System.out.println("AEDir." + wiz.name() + " \"" + wiz.getDescrizione() + "\" = " + wiz.get());
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

    public void setFile(AFileService file) {
        this.file = file;
    }

    public void setText(ATextService text) {
        this.text = text;
    }

    @Component
    public static class WizCostServiceInjector {

        @Autowired
        private AFileService file;

        @Autowired
        private ATextService text;


        @PostConstruct
        public void postConstruct() {
            for (AEWizCost aeWizCost : AEWizCost.values()) {
                aeWizCost.setFile(file);
                aeWizCost.setText(text);
            }
        }

    }

}
