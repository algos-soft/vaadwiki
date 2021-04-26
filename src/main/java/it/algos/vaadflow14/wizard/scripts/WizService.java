package it.algos.vaadflow14.wizard.scripts;

import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.backend.application.*;
import static it.algos.vaadflow14.backend.application.FlowCost.SLASH;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.backend.wrapper.*;
import it.algos.vaadflow14.wizard.enumeration.*;
import static it.algos.vaadflow14.wizard.scripts.WizCost.*;
import org.apache.commons.io.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import java.io.*;
import java.time.*;
import java.util.*;


/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: lun, 20-apr-2020
 * Time: 14:39
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class WizService {


    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ATextService text;


    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AArrayService array;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ALogService logger;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ADateService date;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con @Autowired <br>
     * Disponibile al termine del costruttore di questa classe <br>
     */
    @Autowired
    public AFileService file;


    /**
     * Regolazioni iniziali indipendenti dal dialogo di input <br>
     * Chiamato da Wizard.initView() <br>
     * Esegue un reset di ogni enumeration <br>
     * Elabora le varie enumeration nell'ordine (obbligatorio) <br>
     */
    public void fixVariabiliIniziali() {
        //--reset di tutte le enumeration
        reset();
        fixAEWizCost();
        //        AEModulo.fixValues(AEWizCost.pathTargetProjectModulo.get(), AEWizCost.projectCurrent.get());
        //        fixAEFlag();
        fixAEDir();
    }

    /**
     * Regolazioni iniziali indipendenti dal dialogo di input <br>
     * Chiamato da Wizard.initView() <br>
     */
    public void fixAEWizCost() {
        //--directory di lavoro
        String pathCurrent = System.getProperty("user.dir") + SLASH;
        AEWizCost.pathCurrent.setValue(pathCurrent);

        //--programmatore (magari serve)
        String user = pathCurrent.substring(1);
        user = text.levaTestoPrimaDi(user, SLASH);
        user = user.substring(0, user.indexOf(SLASH));
        AEWizCost.nameUser.setValue(user);

        //--progetto in esecuzione
        String projectCurrent = file.estraeDirectoryFinaleSenzaSlash(pathCurrent);
        projectCurrent = text.primaMaiuscola(projectCurrent);
        AEWizCost.nameProjectCurrentUpper.setValue(projectCurrent);

        //--differenziazione tra progetto base (vaadflow14) e progetti derivati
        AEFlag.isBaseFlow.set(projectCurrent.toLowerCase().equals(AEWizCost.nameVaadFlow14.get().toLowerCase()));

        //--regola tutti i valori automatici, dopo aver inserito quelli fondamentali
        AEWizCost.fixValoriDerivati();
    }

    /**
     * Regolazioni iniziali indipendenti dal dialogo di input <br>
     * Chiamato da Wizard.initView() <br>
     */
    public void fixAEDir() {
        String pathCurrent;
        String projectName;

        //--regola i valori elaborabili di AEDir
        pathCurrent = System.getProperty("user.dir") + SLASH;
        AEDir.elaboraAll(pathCurrent);

        //--se è un progetto specifico, ne conosco il nome e lo inserisco nelle enumeration AEDir modificabili
        if (AEFlag.isBaseFlow.is()) {
            projectName = file.estraeDirectoryFinale(pathCurrent);
            if (projectName.endsWith(SLASH)) {
                projectName = text.levaCoda(projectName, SLASH);
            }

            AEDir.modificaProjectAll(projectName);
        }
        else {
            projectName = file.estraeDirectoryFinale(pathCurrent);
            if (projectName.endsWith(SLASH)) {
                projectName = text.levaCoda(projectName, SLASH);
            }

            AEDir.modificaProjectAll(projectName);
        }
    }

    /**
     * Reset delle enumeration <br>
     */
    public void reset() {
        AEToken.reset();
        AEFlag.reset();
        AECheck.reset();
    }

    /**
     * Visualizzazione iniziale di controllo <br>
     */
    public void printInfo(String message) {
        AEDir.printInfo(message);
        AEFlag.printInfo(message);
        AECheck.printInfo(message);
        AEPackage.printInfo(message);
    }

    /**
     * Visualizzazione finale di controllo <br>
     */
    public void printInfoCompleto(String message) {
        printInfo(message);
        AEToken.printInfo(message);
    }

    /**
     * Copia una cartella da VaadFlow al progetto <br>
     *
     * @param copyWiz   modalità di comportamento se esiste la directory di destinazione
     * @param srcPath   nome completo della directory sorgente
     * @param destPath  nome completo della directory destinazione
     * @param directory da cui iniziare il pathBreve del messaggio
     */
    public AIResult copyDir(final AECopyWiz copyWiz, final String srcPath, final String destPath, final String directory) {
        AIResult result = AResult.errato();
        String message = VUOTA;
        File srcDir = new File(srcPath);
        File destDir = new File(destPath);
        String dirPath = text.isValid(directory) ? directory : AEWizCost.nameProjectCurrentUpper.get().toLowerCase();
        String pathBreve = file.findPathBreveDa(destPath, dirPath);
        String type = text.setTonde(copyWiz.name());

        switch (copyWiz) {
            case dirDeletingAll:
                message = String.format("la directory %s %s non esisteva ma è stata creata.", pathBreve, type);
                if (file.isEsisteDirectory(destPath)) {
                    delete(destDir);
                    message = String.format("la directory %s %s esisteva già ed è stata cancellata e completamente sostituita.", pathBreve, type);
                }
                copy(srcDir, destDir);
                message = String.format("la directory %s %s è stata creata.", pathBreve, type);
                result = AResult.valido(message);
                break;
            case dirAddingOnly:
                message = String.format("la directory %s %s esiste già ed è stata integrata.", pathBreve, type);
                if (file.isNotEsisteDirectory(destPath)) {
                    message = String.format("la directory %s %s non esisteva ma è stata creata.", pathBreve, type);
                }
                copy(srcDir, destDir);
                result = AResult.valido(message);
                break;
            case dirSoloSeNonEsiste:
                message = String.format("la directory %s %s esiste già e non è stata modificata.", pathBreve, type);
                if (file.isNotEsisteDirectory(destPath)) {
                    copy(srcDir, destDir);
                    message = String.format("la directory %s %s non esisteva ma è stata creata.", pathBreve, type);
                }
                break;
            default:
                logger.warn("Switch - caso non definito", this.getClass(), "copyDir");
                break;
        }

        return result;
    }

    private boolean delete(final File destDir) {
        try {
            FileUtils.deleteDirectory(destDir);
            return true;
        } catch (Exception unErrore) {
            logger.error(unErrore, this.getClass(), "delete");
            return false;
        }
    }

    private boolean copy(final File srcDir, final File destDir) {
        try {
            FileUtils.copyDirectory(srcDir, destDir);
            return true;
        } catch (Exception unErrore) {
            logger.error(unErrore, this.getClass(), "copy");
            return false;
        }
    }

    /**
     * Copia un file <br>
     * <p>
     * Controlla che siano validi i path di riferimento <br>
     * Controlla che esista il path del file sorgente  <br>
     * Se manca il file sorgente, non fa nulla <br>
     * Se esiste il file di destinazione ed è AECopyFile.soloSeNonEsiste, non fa nulla <br>
     * Se esiste il file di destinazione ed è AECopyDir.sovrascriveSempreAncheSeEsiste, lo sovrascrive <br>
     * Se esiste il file di destinazione ed è AECopyFile.checkFlagSeEsiste, controlla il flag sovraScrivibile <br>
     * Nei messaggi di avviso, accorcia il destPath eliminando i livelli precedenti alla directory indicata <br>
     *
     * @param copyWiz  modalità di comportamento se esiste il file di destinazione
     * @param srcPath  nome completo di suffisso del file sorgente
     * @param destPath nome completo di suffisso del file da creare
     * @param firstDir prima directory per troncare il path nel messaggio di avviso
     */
    public void copyFile(final AECopyWiz copyWiz, final String srcPath, final String destPath, final String firstDir) {
        String sourceTextElaborato;
        String type = text.setTonde(copyWiz.name());

        sourceTextElaborato = file.leggeFile(srcPath);
        copy(copyWiz, destPath, sourceTextElaborato, firstDir, type);
    }

    /**
     * Crea un nuovo file leggendo il testo da wizard.sources di VaadFlow14 ed elaborandolo <br>
     * <p>
     * Controlla che sia valido il path di riferimento <br>
     * Controlla che nella directory wizard.sources di VaadFlow14 esista il file sorgente da copiare <br>
     * Se manca il file sorgente, non fa nulla <br>
     * Se non esiste la directory di destinazione, la crea <br>
     * Se esiste il file di destinazione ed è AECopyFile.soloSeNonEsiste, non fa nulla <br>
     * Se esiste la directory di destinazione ed è AECopyDir.deletingAll, la cancella e poi la copia <br>
     * Se esiste la directory di destinazione ed è AECopyFile.checkFlagSeEsiste, controlla il flag sovraScrivibile <br>
     * Nei messaggi di avviso, accorcia il pathFileToBeWritten eliminando i primi 4 livelli (/Users/gac/Documents/IdeaProjects) <br>
     * Elabora il testo sostituendo i 'tokens' coi valori attuali <br>
     * Scrive il file col path e suffisso indicati <br>
     *
     * @param typeCopy            modalità di comportamento se esiste il file di destinazione
     * @param nameSourceText      nome del file di testo presente nella directory wizard.sources di VaadFlow14
     * @param pathFileToBeWritten nome completo di suffisso del file da creare
     */
    public AIResult creaFilePackage(AECopyWiz typeCopy, String nameSourceText, String pathFileToBeWritten) {
        return creaFile(typeCopy, nameSourceText, pathFileToBeWritten, FlowCost.DIR_PACKAGES);
    }

    /**
     * Crea un nuovo file leggendo il testo da wizard.sources di VaadFlow14 ed elaborandolo <br>
     * <p>
     * Controlla che sia valido il path di riferimento <br>
     * Controlla che nella directory wizard.sources di VaadFlow14 esista il file sorgente da copiare <br>
     * Se manca il file sorgente, non fa nulla <br>
     * Se non esiste la directory di destinazione, la crea <br>
     * Se esiste il file di destinazione ed è AECopyFile.soloSeNonEsiste, non fa nulla <br>
     * Se esiste la directory di destinazione ed è AECopyDir.deletingAll, la cancella e poi la copia <br>
     * Se esiste la directory di destinazione ed è AECopyFile.checkFlagSeEsiste, controlla il flag sovraScrivibile <br>
     * Nei messaggi di avviso, accorcia il pathFileToBeWritten eliminando i primi 4 livelli (/Users/gac/Documents/IdeaProjects) <br>
     * Elabora il testo sostituendo i 'tokens' coi valori attuali <br>
     * Scrive il file col path e suffisso indicati <br>
     *
     * @param copyWiz             modalità di comportamento se esiste il file di destinazione
     * @param nameSourceText      nome del file di testo presente nella directory wizard.sources di VaadFlow14
     * @param pathFileToBeWritten nome completo di suffisso del file da creare
     * @param firstDir            prima directory per troncare il path nel messaggio di avviso
     */
    public AIResult creaFile(final AECopyWiz copyWiz, final String nameSourceText, final String pathFileToBeWritten, final String firstDir) {
        AIResult result;
        String message;
        String sourceTextGrezzo;
        String sourceTextElaborato;
        String fileName = nameSourceText;
        String pathSource;
        String tagToken = "@.+@";
        String type = text.setTonde(copyWiz.name());

        if (!fileName.endsWith(FlowCost.TXT_SUFFIX)) {
            fileName += FlowCost.TXT_SUFFIX;
        }

        pathSource = file.findPathBreve(AEWizCost.pathVaadFlow14WizSources.get() + fileName, AEWizCost.nameVaadFlow14.get().toLowerCase());
        if (!file.isEsisteFile(AEWizCost.pathVaadFlow14WizSources.get(), fileName)) {
            message = String.format("Non sono riuscito a trovare il file sorgente %s %s", pathSource, type);
            logger.log(AETypeLog.wizard, message);
            return AResult.errato(message);
        }

        sourceTextGrezzo = leggeFile(fileName);
        if (text.isEmpty(sourceTextGrezzo)) {
            message = String.format("Non sono riuscito ad elaborare il file sorgente %s %s", pathSource, type);
            logger.log(AETypeLog.wizard, message);
            return AResult.errato(message);
        }

        sourceTextElaborato = elaboraFileCreatoDaSource(sourceTextGrezzo);
        if (sourceTextElaborato.matches(tagToken)) {
            message = String.format("Non sono riuscito ad elaborare i tokens del file sorgente %s %s", pathSource, type);
            logger.log(AETypeLog.wizard, message);
            return AResult.errato(message);
        }

        result = copy(copyWiz, pathFileToBeWritten, sourceTextElaborato, firstDir, type);
        return result;
    }

    private AIResult copy(final AECopyWiz copyWiz, final String pathFileToBeWritten, final String sourceTextElaborato, final String firstDir, String type) {
        AIResult result = AResult.errato();
        AIResult resultCheck = AResult.errato();
        String message = VUOTA;
        boolean esisteFileDest = false;
        String dirPath = text.isValid(firstDir) ? firstDir : AEWizCost.nameProjectCurrentUpper.get().toLowerCase();
        String pathBreve = file.findPathBreveDa(pathFileToBeWritten, dirPath);

        esisteFileDest = file.isEsisteFile(pathFileToBeWritten);
        switch (copyWiz) {
            case fileSovrascriveSempreAncheSeEsiste:
            case sourceSovrascriveSempreAncheSeEsiste:
                if (esisteFileDest) {
                    message = String.format("il file %s %s esisteva già ma è stato riscritto", pathBreve, type);
                    result.setMessage(message);
                }
                else {
                    message = String.format("il file %s %s non esisteva ed è stato creato", pathBreve, type);
                    result.setMessage(message);
                }
                file.scriveFile(pathFileToBeWritten, sourceTextElaborato, true, firstDir);
                break;
            case fileSoloSeNonEsiste:
            case sourceSoloSeNonEsiste:
                if (esisteFileDest) {
                    message = String.format("il file %s %s esisteva già e non è stato modificato", pathBreve, type);
                    result.setMessage(message);
                }
                else {
                    file.scriveFile(pathFileToBeWritten, sourceTextElaborato, true, firstDir);
                    message = String.format("il file %s %s non esisteva ed è stato creato.", pathBreve, type);
                    result.setMessage(message);
                }
                break;
            case fileCheckFlagSeEsiste:
            case sourceCheckFlagSeEsiste:
                if (esisteFileDest) {
                    resultCheck = checkFileCanBeModified(pathFileToBeWritten, pathBreve);
                    if (resultCheck.isValido()) {
                        file.scriveFile(pathFileToBeWritten, sourceTextElaborato, true, firstDir);
                    }
                    result = resultCheck;
                }
                else {
                    result = file.scriveFile(pathFileToBeWritten, sourceTextElaborato, true, firstDir);
                    message = String.format("il file %s %s non esisteva ed è stato creato.", pathBreve, type);
                    result.setValidationMessage(message);
                }
                break;
            default:
                logger.warn("Switch - caso non definito", this.getClass(), "creaFile");
                break;
        }

        return result;
    }

    /**
     * Sostituisce l'header di un file leggendo il testo da wizard.sources di VaadFlow14 ed elaborandolo <br>
     * <p>
     * Modifica il testo esistente dal termine dei dati cronologici fino al tag @AIScript(... <br>
     * Controlla che esista il file destinazione da modificare <br>
     * Controlla che nella directory wizard.sources di VaadFlow14 esista il file sorgente da copiare <br>
     * Nei messaggi di avviso, accorcia il pathFileToBeWritten eliminando i primi 4 livelli (/Users/gac/Documents/IdeaProjects) <br>
     * Elabora il testo sostituendo i 'tokens' coi valori attuali <br>
     * Modifica il file col path e suffisso indicati <br>
     *
     * @param packageName          nome della directory per il package in esame
     * @param nameSourceText       nome del file di testo presente nella directory wizard.sources di VaadFlow14
     * @param suffisso             del file da modificare
     * @param pathFileDaModificare nome completo del file da modificare
     * @param inizioFile           per la modifica dell'header
     */
    public AIResult fixDocFile(String packageName, String nameSourceText, String suffisso, String pathFileDaModificare, boolean inizioFile) {
        AIResult risultato = AResult.errato();
        String message = VUOTA;
        String tagIni = inizioFile ? "package" : "* <p>";
        String tagEnd = "@AIScript(";
        String oldHeader;
        String newHeader;
        String realText = file.leggeFile(pathFileDaModificare);
        String sourceText = leggeFile(nameSourceText);
        String path = file.findPathBreve(pathFileDaModificare, FlowCost.DIR_PACKAGES);
        String fileName = file.estraeClasseFinaleSenzaJava(pathFileDaModificare);

        if (text.isEmpty(sourceText)) {
            logger.warn("Non sono riuscito a trovare il file " + nameSourceText + " nella directory wizard.sources di VaadFlow14", this.getClass(), "fixDocFile");
            return risultato;
        }
        sourceText = elaboraFileCreatoDaSource(sourceText);
        if (text.isEmpty(sourceText)) {
            logger.warn("Non sono riuscito a elaborare i tokens del file " + path, this.getClass(), "fixDocFile");
            return risultato;
        }

        if (!file.isEsisteFile(pathFileDaModificare)) {
            logger.warn("Non esiste il file " + path, this.getClass(), "fixDocFile");
            return risultato;
        }

        if (realText.contains(tagIni) && realText.contains(tagEnd)) {
            if (realText.indexOf(tagIni) >= realText.indexOf(tagEnd)) {
                message = String.format("Il file originale %s ha qualche problema. Probabilmente manca il tag %s.", path, tagIni);
                logger.log(AETypeLog.wizardDoc, message);
                risultato = AResult.errato(message);
                return risultato;
            }
            oldHeader = realText.substring(realText.indexOf(tagIni), realText.indexOf(tagEnd));
            newHeader = sourceText.substring(sourceText.indexOf(tagIni), sourceText.indexOf(tagEnd));
            if (text.isValid(oldHeader) && text.isValid(newHeader)) {
                if (newHeader.trim().equals(oldHeader.trim())) {
                    message = String.format("Nel package %s non è stato modificato il file %s", packageName, fileName);
                    logger.log(AETypeLog.wizardDoc, message); //@todo PROVVISORIO
                    risultato = AResult.errato(message);
                }
                else {
                    realText = text.sostituisce(realText, oldHeader, newHeader);
                    risultato = file.scriveFile(pathFileDaModificare, realText, true, FlowCost.DIR_PACKAGES);
                    if (risultato.isValido()) {
                        message = String.format("Nel package %s è stato modificato il file %s", packageName, fileName);
                        risultato = AResult.valido(message);
                    }
                }
            }
            else {
                message = String.format("Nel package %s non sono riuscito a elaborare il file %s", packageName, path);
                logger.log(AETypeLog.wizardDoc, message);
            }
        }
        else {
            message = String.format("Nel package %s manca il tag @AIScript nel file %s che non è stato modificato", packageName, path);
            logger.log(AETypeLog.wizardDoc, message);
        }

        return risultato;
    }

    public String leggeFile(String nomeFileTextSorgente) {
        String nomeFileTxt = nomeFileTextSorgente;

        if (!nomeFileTxt.endsWith(FlowCost.TXT_SUFFIX)) {
            nomeFileTxt += FlowCost.TXT_SUFFIX;
        }

        return file.leggeFile(AEWizCost.pathVaadFlow14WizSources.get() + nomeFileTxt);
    }

    /**
     * Elabora un nuovo file <br>
     * <p>
     * Elabora il testo sostituendo i 'tag' coi valori attuali <br>
     *
     * @param testoGrezzoDaElaborare proveniente da wiz.sources
     *
     * @return testo elaborato
     */
    protected String elaboraFileCreatoDaSource(String testoGrezzoDaElaborare) {
        String testoFinaleElaborato = testoGrezzoDaElaborare;

        for (AEToken token : AEToken.values()) {
            try {
                testoFinaleElaborato = AEToken.replace(token, testoFinaleElaborato, token.getValue());
            } catch (Exception unErrore) {
                logger.error(token.getTokenTag(), this.getClass(), "elaboraFileCreatoDaSource");
                testoFinaleElaborato = AEToken.replace(token, testoFinaleElaborato, token.getValue());
            }
        }

        return testoFinaleElaborato;
    }

    /**
     * Prepara la data attuale in forma 'text' per la sostituzione <br>
     *
     * @return testo nella forma 'LocalDate.of(2020, 10, 19)'
     */
    public String fixVersionDate() {
        String testoData = VUOTA;
        LocalDate dataAttuale = LocalDate.now();

        testoData = "LocalDate.of(";
        testoData += dataAttuale.getYear();
        testoData += FlowCost.VIRGOLA_SPAZIO;
        testoData += dataAttuale.getMonthValue();
        testoData += FlowCost.VIRGOLA_SPAZIO;
        testoData += dataAttuale.getDayOfMonth();
        testoData += ")";

        return testoData;
    }

    /**
     * Esamina un file per controllare lo stato del flag 'sovraScrivibile' (se esiste) <br>
     * Di default è uguale a true <br>
     *
     * @param pathFileToBeChecked nome completo del file da controllare
     *
     * @return true se il flag non esiste o è sovraScrivibile=true
     * .       false se il flag esiste ed è sovraScrivibile=false
     * <p>
     * .       0 non esiste il flag sovraScrivibile
     * .       1 il flag sovraScrivibile esiste ma non è completo
     * .       2 il flag sovraScrivibile esiste ed è sovrascrivibile=true
     * .       3 il flag sovraScrivibile esiste ed è sovrascrivibile=false
     * .       4 il flag sovraScrivibile esiste ma non è ne true ne false
     */
    public AIResult checkFileCanBeModified(String pathFileToBeChecked, String pathBreve) {
        AIResult result = AResult.errato();
        String message;
        int risultato = 0;
        String oldText = VUOTA;

        if (!file.isEsisteFile(pathFileToBeChecked)) {
            result = AResult.errato();
        }

        oldText = file.leggeFile(pathFileToBeChecked);
        if (text.isValid(oldText)) {
            risultato = checkFlagSovrascrivibile(oldText);
            switch (risultato) {
                case 0:
                    message = String.format("il file %s esisteva ma è stato modificato perché mancava il flag sovraScrivibile.", pathBreve);
                    result = AResult.valido(message);
                    break;
                case 1:
                    message = String.format("il file %s esiste già col flag sovraScrivibile incompleto e non accetta modifiche.", pathBreve);
                    result = AResult.errato(message);
                    break;
                case 2:
                    message = String.format("il file %s esisteva col flag sovraScrivibile=true ed è stato modificato.", pathBreve);
                    result = AResult.valido(message);
                    break;
                case 3:
                    message = String.format("il file %s esiste già col flag sovraScrivibile=false e non accetta modifiche.", pathBreve);
                    result = AResult.errato(message);
                    break;
                case 4:
                    message = String.format("il file %s esiste già col flag sovraScrivibile ne true ne false e non accetta modifiche.", pathBreve);
                    result = AResult.errato(message);
                    break;
                default:
                    logger.warn("Switch - caso non definito", this.getClass(), "checkFileCanBeModified");
                    break;
            }
        }
        else {
            result = AResult.errato("Non esiste il file");
        }

        return result;
    }

    /**
     * Controlla lo stato del flag 'sovraScrivibile' (se esiste) <br>
     *
     * @param oldFileText testo completo del file da controllare
     *
     * @return 0 non esiste il flag sovraScrivibile
     * .       1 il flag sovraScrivibile esiste ma non è completo
     * .       2 il flag sovraScrivibile esiste ed è sovrascrivibile=true
     * .       3 il flag sovraScrivibile esiste ed è sovrascrivibile=false
     * .       4 il flag sovraScrivibile esiste ma non è ne true ne false
     */
    public int checkFlagSovrascrivibile(final String oldFileText) {
        int risultato = 0;
        String tag1 = "@AIScript(sovra";
        String tag2 = "@AIScript(Sovra";
        List<String> tagsTrue = new ArrayList<>();
        List<String> tagsFalse = new ArrayList<>();
        List<String> tagsNullo = new ArrayList<>();

        if (oldFileText.contains(tag1) || oldFileText.contains(tag2)) {
            risultato = 1;
        }
        else {
            return risultato;
        }

        tagsTrue.add("@AIScript(sovrascrivibile = true)");
        tagsTrue.add("@AIScript(sovrascrivibile=true)");
        tagsTrue.add("@AIScript(sovrascrivibile= true)");
        tagsTrue.add("@AIScript(sovrascrivibile =true)");
        tagsTrue.add("@AIScript(sovraScrivibile = true)");
        tagsTrue.add("@AIScript(sovraScrivibile=true)");
        tagsTrue.add("@AIScript(sovraScrivibile= true)");
        tagsTrue.add("@AIScript(sovraScrivibile =true)");

        tagsFalse.add("@AIScript(sovrascrivibile = false)");
        tagsFalse.add("@AIScript(sovrascrivibile=false)");
        tagsFalse.add("@AIScript(sovrascrivibile= false)");
        tagsFalse.add("@AIScript(sovrascrivibile =false)");
        tagsFalse.add("@AIScript(sovraScrivibile = false)");
        tagsFalse.add("@AIScript(sovraScrivibile=false)");
        tagsFalse.add("@AIScript(sovraScrivibile= false)");
        tagsFalse.add("@AIScript(sovraScrivibile =false)");

        tagsNullo.add("@AIScript(sovrascrivibile = )");
        tagsNullo.add("@AIScript(sovrascrivibile=)");
        tagsNullo.add("@AIScript(sovrascrivibile= )");
        tagsNullo.add("@AIScript(sovrascrivibile =)");
        tagsNullo.add("@AIScript(sovraScrivibile = )");
        tagsNullo.add("@AIScript(sovraScrivibile=)");
        tagsNullo.add("@AIScript(sovraScrivibile= )");
        tagsNullo.add("@AIScript(sovraScrivibile =)");

        if (text.isContiene(oldFileText, tagsTrue)) {
            risultato = 2;
        }
        if (text.isContiene(oldFileText, tagsFalse)) {
            risultato = 3;
        }
        if (text.isContiene(oldFileText, tagsNullo)) {
            risultato = 4;
        }

        return risultato;
    }

    /**
     * Lista dei packages esistenti nel target project <br>
     * Controlla che non sia una directory di raggruppamento. Nel caso entra nelle sub-directories <br>
     * Prende il nome della directory di livello più interno <br>
     */
    public List<String> getPackages() {
        List<String> packages = new ArrayList<>();
        String path = AEWizCost.pathTargetProjectPackages.get();

        if (text.isValid(path)) {
            packages = getLastSubDirNames(path);
        }

        if (array.isEmpty(packages)) {
            logger.log(AETypeLog.wizard, "Non ci sono packages");
        }

        return packages;
    }

    /**
     * Estrae l'ultimo livello di sub-directories da una directory <br>
     *
     * @param pathDirectoryToBeScanned nome completo della directory
     */
    public List<String> getLastSubDirNames(String pathDirectoryToBeScanned) {
        List<String> pathSubDirName = new ArrayList<>();
        List<File> pathSubDir = file.getSubDirectories(pathDirectoryToBeScanned);
        List<File> pathSubSub;
        List<String> pathSubSubName = new ArrayList<>();

        if (pathSubDir != null) {
            for (File subDir : pathSubDir) {
                pathSubSub = file.getSubDirectories(subDir.getAbsolutePath());
                if (pathSubSub != null && pathSubSub.size() > 0) {
                    pathSubSubName = getLastSubDirNames(subDir.getAbsolutePath());
                    for (String nome : pathSubSubName) {
                        pathSubDirName.add(subDir.getName() + SLASH + nome);
                    }
                }
                else {
                    pathSubDirName.add(subDir.getName());
                }
            }
        }

        return pathSubDirName;
    }

    /**
     * Regola alcuni valori della Enumeration AEWizCost relativi al package selezionato <br>
     */
    public void regolaPackages(final String packName) {
        String fileName;

        AEWizCost.nameTargetPackagePunto.setValue(text.fixSlashToPunto(packName));
        AEWizCost.nameTargetPackage.setValue(text.fixPuntoToSlash(packName));
        fileName = text.levaTestoPrimaDi(text.fixPuntoToSlash(packName), SLASH);
        AEWizCost.nameTargetFileUpper.setValue(text.primaMaiuscola(fileName));
        AEWizCost.pathTargetPackageSlash.setValue(AEWizCost.pathTargetProjectPackages.get() + AEWizCost.nameTargetPackage.get() + SLASH);
    }

    /**
     * Regola alcuni valori della Enumeration EAToken che saranno usati da: <br>
     * WizElaboraNewPackage e WizElaboraUpdatePackage <br>
     *
     * @param nameTargetProjectUpper  Nome del progetto target. Può essere diverso dal valore di nameTargetProjectModulo (Es. vaadwiki e Wiki)
     * @param nameTargetProjectModulo Nome della directory e del modulo del progetto target. Può essere diverso dal valore di nameTargetProjectUpper (Es. vaadwiki e Wiki)
     * @param packageName             Nome del package da creare/modificare. Eventualmente con sub-directory (separatore slash)
     * @param fileName                Nome maiuscolo del file da creare con suffisso. Singolo file. Senza sub-directory.
     */
    public boolean regolaAEToken(String nameTargetProjectUpper, String nameTargetProjectModulo, String packageName, String fileName) {
        boolean status = true;
        boolean usaCompany = AECheck.company.is();
        String tagEntity = "AEntity";
        String tagCompany = "AECompany";
        AEToken.reset();
        String codeName = text.primaMinuscola(AEPackage.code.getFieldName());
        String ordineName = text.primaMinuscola(AEPackage.ordine.getFieldName());
        String descrizioneName = text.primaMinuscola(AEPackage.description.getFieldName());
        String validoName = text.primaMinuscola(AEPackage.valido.getFieldName());

        if (text.isEmpty(nameTargetProjectUpper) && text.isEmpty(packageName)) {
            logger.warn("Manca sia nameTargetProjectUpper che packageName.", this.getClass(), "regolaAEToken");
            return false;
        }
        if (AEFlag.isProject.is() && text.isEmpty(nameTargetProjectUpper)) {
            logger.warn("Stiamo modificando un progetto e manca nameTargetProjectUpper.", this.getClass(), "regolaAEToken");
            return false;
        }
        if (AEFlag.isProject.is() && text.isEmpty(nameTargetProjectModulo)) {
            logger.warn("Stiamo modificando un progetto e manca nameTargetProjectModulo.", this.getClass(), "regolaAEToken");
            return false;
        }
        if (AEFlag.isPackage.is() && !AECheck.docFile.is() && text.isEmpty(packageName)) {
            logger.warn("Stiamo modificando un package e manca packageName.", this.getClass(), "regolaAEToken");
            return false;
        }

        if (text.isValid(packageName)) {
            AEToken.first.setValue(packageName.substring(0, 1).toUpperCase());
            AEToken.packageNamePunti.setValue(text.fixSlashToPunto(packageName));
            AEToken.packageNameSlash.setValue(text.fixPuntoToSlash(packageName));
            AEToken.packageNameLower.setValue(text.levaTestoPrimaDi(AEToken.packageNameSlash.getValue(), SLASH));
            AEToken.packageNameUpper.setValue(text.primaMaiuscola(AEToken.packageNameLower.getValue()));
        }
        if (text.isValid(fileName)) {
            AEToken.entityLower.setValue(fileName.toLowerCase(Locale.ROOT));
            AEToken.entityUpper.setValue(text.primaMaiuscola(fileName));
        }
        AEToken.nameTargetProject.setValue(nameTargetProjectUpper);
        AEToken.nameTargetProjectLower.setValue(nameTargetProjectUpper.toLowerCase());
        AEToken.projectNameUpper.setValue(nameTargetProjectUpper.toUpperCase());
        AEToken.moduleNameMinuscolo.setValue(nameTargetProjectModulo.toLowerCase());
        AEToken.moduleNameMaiuscolo.setValue(text.primaMaiuscola(nameTargetProjectModulo));
        AEToken.first.setValue(nameTargetProjectUpper.substring(0, 1).toUpperCase());
        AEToken.user.setValue(AEWizCost.nameUser.get());
        AEToken.today.setValue(date.getCompletaShort(LocalDate.now()));
        AEToken.todayAnno.setValue(String.valueOf(LocalDate.now().getYear()));
        AEToken.todayMese.setValue(String.valueOf(LocalDate.now().getMonthValue()));
        AEToken.todayGiorno.setValue(String.valueOf(LocalDate.now().getDayOfMonth()));
        AEToken.time.setValue(date.getOrario());
        AEToken.versionDate.setValue(fixVersion());
        AEToken.usaCompany.setValue(usaCompany ? "true" : "false");
        AEToken.superClassEntity.setValue(usaCompany ? tagCompany : tagEntity);
        AEToken.usaSecurity.setValue(AECheck.security.is() ? ")" : ", exclude = {SecurityAutoConfiguration.class}");
        AEToken.keyProperty.setValue(AEPackage.code.is() ? codeName : VUOTA);
        AEToken.searchProperty.setValue(AEPackage.code.is() ? codeName : VUOTA);
        AEToken.sortProperty.setValue(AEPackage.ordine.is() ? ordineName : AEPackage.code.is() ? codeName : VUOTA);
        AEToken.rowIndex.setValue(AEPackage.rowIndex.is() ? "true" : "false");
        AEToken.properties.setValue(fixProperties());
        AEToken.propertyOrdineName.setValue(ordineName);
        AEToken.propertyOrdine.setValue(fixProperty(AEPackage.ordine));
        AEToken.propertyCodeName.setValue(codeName);
        AEToken.propertyCode.setValue(fixProperty(AEPackage.code));
        AEToken.propertyDescrizioneName.setValue(descrizioneName);
        AEToken.propertyDescrizione.setValue(fixProperty(AEPackage.description));
        AEToken.propertyValidoName.setValue(validoName);
        AEToken.propertyValido.setValue(fixProperty(AEPackage.valido));
        AEToken.propertiesRinvio.setValue(fixPropertiesRinvio());
        AEToken.propertiesDoc.setValue(fixPropertiesDoc());
        AEToken.propertiesParams.setValue(fixPropertiesParams());
        AEToken.propertiesBuild.setValue(fixPropertiesBuild());
        AEToken.creaIfNotExist.setValue(fixCreaIfNotExist());
        AEToken.codeDoc.setValue(fixCodeDoc());
        AEToken.codeParams.setValue(fixCodeParams());
        AEToken.codeRinvio.setValue(fixCodeRinvio());
        AEToken.newEntityKeyUnica.setValue(fixNewEntityUnica());
        AEToken.toString.setValue(fixString());
        printProgetto();

        System.out.println(VUOTA);
        System.out.println(AEToken.packageNamePunti.getTokenTag()+SEP+AEToken.packageNamePunti.getValue());
        System.out.println(AEToken.packageNameSlash.getTokenTag()+SEP+AEToken.packageNameSlash.getValue());
        System.out.println(AEToken.packageNameLower.getTokenTag()+SEP+AEToken.packageNameLower.getValue());
        System.out.println(AEToken.packageNameUpper.getTokenTag()+SEP+AEToken.packageNameUpper.getValue());
        System.out.println(AEToken.entityLower.getTokenTag()+SEP+AEToken.entityLower.getValue());
        System.out.println(AEToken.entityUpper.getTokenTag()+SEP+AEToken.entityUpper.getValue());

        return status;
    }

    protected String fixProperty(AEPackage pack) {
        String testo = VUOTA;
        String sourceText = VUOTA;
        String tagSources = pack.getSourcesName();

        if (pack.is()) {
            sourceText = this.leggeFile(tagSources);
            testo = this.elaboraFileCreatoDaSource(sourceText);
        }

        return testo;
    }

    protected String fixProperties() {
        String testo = VUOTA;

        if (AEPackage.ordine.is()) {
            testo += AEPackage.ordine.getFieldName() + FlowCost.VIRGOLA;
        }

        if (AEPackage.code.is()) {
            testo += AEPackage.code.getFieldName() + FlowCost.VIRGOLA;
        }

        if (AEPackage.description.is()) {
            testo += AEPackage.description.getFieldName() + FlowCost.VIRGOLA;
        }

        if (AEPackage.valido.is()) {
            testo += AEPackage.valido.getFieldName() + FlowCost.VIRGOLA;
        }

        testo = text.levaCoda(testo, FlowCost.VIRGOLA);
        return text.setApici(testo).trim();
    }

    protected String fixPropertiesRinvio() {
        String testo = VUOTA;

        if (AEPackage.ordine.is()) {
            testo += "0" + FlowCost.VIRGOLA_SPAZIO;
        }

        if (AEPackage.code.is()) {
            testo += "VUOTA" + FlowCost.VIRGOLA_SPAZIO;
        }

        if (AEPackage.description.is()) {
            testo += "VUOTA" + FlowCost.VIRGOLA_SPAZIO;
        }

        if (AEPackage.valido.is()) {
            testo += "false" + FlowCost.VIRGOLA_SPAZIO;
        }

        return text.levaCoda(testo, FlowCost.VIRGOLA_SPAZIO);
    }

    protected String fixPropertiesDoc() {
        String testo = VUOTA;
        String sep = FlowCost.A_CAPO + FlowCost.TAB + FlowCost.SPAZIO;

        if (AEPackage.ordine.is()) {
            testo += String.format("* @param %s (obbligatorio, unico)" + sep, AEPackage.ordine.getFieldName());
        }

        if (AEPackage.code.is()) {
            testo += String.format("* @param %s di riferimento (obbligatorio, unico)" + sep, AEPackage.code.getFieldName());
        }

        if (AEPackage.description.is()) {
            testo += String.format("* @param %s (facoltativo, non unico)" + sep, AEPackage.description.getFieldName());
        }

        if (AEPackage.valido.is()) {
            testo += String.format("* @param %s flag (facoltativo, di default false)" + sep, AEPackage.valido.getFieldName());
        }

        return testo.trim();
    }

    protected String fixPropertiesParams() {
        String testo = VUOTA;

        if (AEPackage.ordine.is()) {
            testo += String.format("final int %s" + FlowCost.VIRGOLA_SPAZIO, AEPackage.ordine.getFieldName());
        }

        if (AEPackage.code.is()) {
            testo += String.format("final String %s" + FlowCost.VIRGOLA_SPAZIO, AEPackage.code.getFieldName());
        }

        if (AEPackage.description.is()) {
            testo += String.format("final String %s" + FlowCost.VIRGOLA_SPAZIO, AEPackage.description.getFieldName());
        }

        if (AEPackage.valido.is()) {
            testo += String.format("final boolean %s" + FlowCost.VIRGOLA_SPAZIO, AEPackage.valido.getFieldName());
        }

        testo = text.levaCoda(testo, FlowCost.VIRGOLA_SPAZIO);
        return testo;
    }

    protected String fixPropertiesBuild() {
        String testo = VUOTA;
        String sep = FlowCost.A_CAPO + FlowCost.TAB + FlowCost.TAB + FlowCost.TAB + FlowCost.TAB;

        if (AEPackage.ordine.is()) {
            testo += String.format(".%1$s(%1$s > 0 ? %1$s : this.getNewOrdine())", AEPackage.ordine.getFieldName()) + sep;
        }

        if (AEPackage.code.is()) {
            testo += String.format(".%1$s(text.isValid(%1$s) ? %s : null)", AEPackage.code.getFieldName()) + sep;
        }

        if (AEPackage.description.is()) {
            testo += String.format(".%1$s(text.isValid(%1$s) ? %1$s : null)", AEPackage.description.getFieldName()) + sep;
        }

        if (AEPackage.valido.is()) {
            testo += String.format(".%s(%s)", AECheck.valido.getFieldName(), AEPackage.valido.getFieldName()) + sep;
        }

        return testo.trim();
    }

    protected String fixCreaIfNotExist() {
        String testo = VUOTA;
        String tagSources = "MethodCreaIfNotExist";
        String sourceText = VUOTA;

        if (AEPackage.code.is()) {
            sourceText = this.leggeFile(tagSources);
            testo = this.elaboraFileCreatoDaSource(sourceText);
        }

        return testo;
    }

    protected String fixCodeDoc() {
        String testo = VUOTA;

        if (AEPackage.code.is()) {
            testo += String.format("* @param %s di riferimento (obbligatorio, unico)", AEPackage.code.getFieldName());
        }

        return testo.trim();
    }

    protected String fixCodeParams() {
        String testo = VUOTA;

        if (AEPackage.code.is()) {
            testo += String.format("final String %s", AEPackage.code.getFieldName());
        }

        return testo.trim();
    }

    protected String fixCodeRinvio() {
        String testo = VUOTA;

        if (AEPackage.ordine.is()) {
            testo += "0" + FlowCost.VIRGOLA_SPAZIO;
        }

        if (AEPackage.code.is()) {
            testo += AEPackage.code.getFieldName() + FlowCost.VIRGOLA_SPAZIO;
        }

        if (AEPackage.description.is()) {
            testo += "VUOTA" + FlowCost.VIRGOLA_SPAZIO;
        }

        if (AEPackage.valido.is()) {
            testo += "false" + FlowCost.VIRGOLA_SPAZIO;
        }

        return text.levaCoda(testo, FlowCost.VIRGOLA_SPAZIO);
    }

    protected String fixNewEntityUnica() {
        String testo = VUOTA;
        String tagSources = "MethodNewEntityKeyUnica";
        String sourceText = VUOTA;

        if (AEPackage.code.is() && (AEPackage.ordine.is() || AEPackage.description.is() || AEPackage.valido.is())) {
            sourceText = this.leggeFile(tagSources);
            testo = this.elaboraFileCreatoDaSource(sourceText);
        }

        return testo;
    }

    protected String fixString() {
        String toString = "VUOTA";

        if (AEPackage.code.is()) {
            toString = AEPackage.code.getFieldName();
        }
        else {
            if (AEPackage.description.is()) {
                toString = AEPackage.description.getFieldName();
            }
        }

        return toString;
    }

    protected String fixVersion() {
        String versione = VUOTA;
        String tag = "LocalDate.of(";
        String anno;
        String mese;
        String giorno;
        LocalDate localDate = LocalDate.now();

        anno = localDate.getYear() + VUOTA;
        mese = localDate.getMonth().getValue() + VUOTA;
        giorno = localDate.getDayOfMonth() + VUOTA;
        versione = tag + anno + FlowCost.VIRGOLA + mese + FlowCost.VIRGOLA + giorno + ")";

        return versione;
    }

    public List<AEWizCost> getAll() {
        List<AEWizCost> listaWizCost = new ArrayList<>();

        for (AEWizCost aeWizCost : AEWizCost.values()) {
            listaWizCost.add(aeWizCost);
        }

        return listaWizCost;
    }

    @Deprecated
    private List<AEWizCost> getAllTypeWiz(final List<AEWizValue> listaType) {
        List<AEWizCost> listaWizCost = new ArrayList<>();

        for (AEWizCost aeWizCost : AEWizCost.values()) {
            for (AEWizValue type : listaType) {
                if (aeWizCost.getWizValue() == type) {
                    listaWizCost.add(aeWizCost);
                }
            }
        }

        return listaWizCost;
    }

    private List<AEWizCost> getAllWizValue(final AEWizValue type) {
        List<AEWizCost> listaWizCost = new ArrayList<>();

        for (AEWizCost aeWizCost : AEWizCost.values()) {
            if (aeWizCost.getWizValue() == type) {
                listaWizCost.add(aeWizCost);
            }
        }

        return listaWizCost;
    }

    public List<AEWizCost> getWizValueCostanti() {
        return getAllWizValue(AEWizValue.costante);
    }

    public List<AEWizCost> getWizValueCalcolati() {
        return getAllWizValue(AEWizValue.calcolato);
    }

    public List<AEWizCost> getWizValueInseriti() {
        return getAllWizValue(AEWizValue.inserito);
    }

    public List<AEWizCost> getWizValueDerivati() {
        return getAllWizValue(AEWizValue.derivato);
    }

    private List<AEWizCost> getAllWizUso(final AEWizUso type) {
        List<AEWizCost> listaWizCost = new ArrayList<>();

        for (AEWizCost aeWizCost : AEWizCost.values()) {
            if (aeWizCost.getWizUso() == type) {
                listaWizCost.add(aeWizCost);
            }
        }

        return listaWizCost;
    }

    public List<AEWizCost> getWizUsoNullo() {
        return getAllWizUso(AEWizUso.nullo);
    }

    public List<AEWizCost> getWizUsoProject() {
        return getAllWizUso(AEWizUso.flagProject);
    }

    public List<AEWizCost> getWizUsoPackage() {
        return getAllWizUso(AEWizUso.flagPackages);
    }

    public List<AEWizCost> getNecessitanoInserimentoValore() {
        List<AEWizValue> listaType = new ArrayList<>();
        listaType.add(AEWizValue.calcolato);
        //        listaType.add(AETypeWiz.necessarioEntrambi);
        listaType.add(AEWizValue.inserito);
        listaType.add(AEWizValue.derivato);

        return getAllTypeWiz(listaType);
    }

    private List<AEWizCost> getAllTypeFile(final AEWizCopy type) {
        List<AEWizCost> listaWizCost = new ArrayList<>();

        for (AEWizCost aeWizCost : AEWizCost.values()) {
            if (aeWizCost.getTypeFile() == type) {
                listaWizCost.add(aeWizCost);
            }
        }

        return listaWizCost;
    }

    //    public List<AEWizCost> getFile() {
    //        return getAllTypeFile(AEWizCopy.file);
    //    }
    //
    //    public List<AEWizCost> getSource() {
    //        return getAllTypeFile(AEWizCopy.source);
    //    }
    //
    //    public List<AEWizCost> getDir() {
    //        return getAllTypeFile(AEWizCopy.dir);
    //    }

    public List<AEWizCost> getNome() {
        return getAllTypeFile(AEWizCopy.nome);
    }

    //    public List<AEWizCost> getNewProject() {
    //        List<AEWizCost> listaWizCost = new ArrayList<>();
    //
    //        for (AEWizCost aeWizCost : AEWizCost.values()) {
    //            if (aeWizCost.isNewProject()) {
    //                listaWizCost.add(aeWizCost);
    //            }
    //        }
    //
    //        return listaWizCost;
    //    }
    //
    //    public List<AEWizCost> getUpdateProject() {
    //        List<AEWizCost> listaWizCost = new ArrayList<>();
    //
    //        for (AEWizCost aeWizCost : AEWizCost.values()) {
    //            if (aeWizCost.isUpdateProject()) {
    //                listaWizCost.add(aeWizCost);
    //            }
    //        }
    //
    //        return listaWizCost;
    //    }

    //    public List<AEWizCost> getNewUpdateProject() {
    //        List<AEWizCost> listaWizCost;
    //        if (AEFlag.isBaseFlow.is()) {
    //            listaWizCost = getNewProject();
    //        }
    //        else {
    //            listaWizCost = getUpdateProject();
    //        }
    //
    //        return listaWizCost;
    //    }

    public List<AEWizCost> getPath() {
        return getAllTypeFile(AEWizCopy.path);
    }

    public List<AEWizCost> getInseritoValore() {
        List<AEWizCost> listaWizCost = new ArrayList<>();

        for (AEWizCost aeWizCost : getNecessitanoInserimentoValore()) {
            if (aeWizCost.get() != null && aeWizCost.get().length() > 0 && !aeWizCost.get().startsWith(VALORE_MANCANTE)) {
                listaWizCost.add(aeWizCost);
            }
        }

        return listaWizCost;
    }

    public List<AEWizCost> getVuote() {
        List<AEWizCost> listaWizCost = new ArrayList<>();

        for (AEWizCost aeWizCost : getNecessitanoInserimentoValore()) {
            if (aeWizCost.get() == null || aeWizCost.get().length() < 1 || aeWizCost.get().startsWith(VALORE_MANCANTE)) {
                listaWizCost.add(aeWizCost);
            }
        }

        return listaWizCost;
    }

    public List<AEWizCost> getVuoteProgetto() {
        List<AEWizCost> listaWizCost = new ArrayList<>();

        //        for (AEWizCost aeWizCost : getNecessarioProgetto()) {
        //            if (aeWizCost.getValue() == null || aeWizCost.getValue().length() < 1 || aeWizCost.getValue().startsWith(VALORE_MANCANTE)) {
        //                listaWizCost.add(aeWizCost);
        //            }
        //        }

        return listaWizCost;
    }

    public List<AEWizCost> getHannoValore() {
        List<AEWizCost> listaWizCost = new ArrayList<>();

        for (AEWizCost aeWizCost : AEWizCost.values()) {
            if (aeWizCost.getWizValue().isServeInserireValore()) {
                listaWizCost.add(aeWizCost);
            }
        }

        return listaWizCost;
    }

    public List<AEWizCost> getHannoValoreVuoto() {
        List<AEWizCost> listaWizCost = new ArrayList<>();

        for (AEWizCost aeWizCost : getHannoValore()) {
            if (aeWizCost.get() == null || aeWizCost.get().length() < 1 || aeWizCost.get().startsWith(VALORE_MANCANTE)) {
                listaWizCost.add(aeWizCost);
            }
        }

        return listaWizCost;
    }


    public List<AEWizCost> getHannoValoreValido() {
        List<AEWizCost> listaWizCost = new ArrayList<>();

        for (AEWizCost aeWizCost : getHannoValore()) {
            if (aeWizCost.get() != null && aeWizCost.get().length() > 0 && !aeWizCost.get().startsWith(VALORE_MANCANTE)) {
                if (aeWizCost.isValida()) {
                    listaWizCost.add(aeWizCost);
                }
                else {
                    logger.adminLogger.info("Manca il flag 'valida' a " + aeWizCost.name());
                }
            }
        }

        return listaWizCost;
    }


    /**
     * Recupera il nome dell'applicazione principale (main) di un progetto <br>
     * Può coincider col nome della directory/modulo oppure essere diverso <br>
     * Cerco tutti i file nella directory/modulo che è l'ultima del path <br>
     * Dovrebbe essercene solo uno che termina con 'xxxApplication' <br>
     * Se ce n'è più di uno, prende il primo. <br>
     * Estraggo il testo prima del tag 'Application' <br>
     *
     * @return nome maiuscolo del progetto, indipendente dalla directory/modulo
     */
    public String estraeProjectFromApplication() {
        String projectNameUpper = VUOTA;
        String tag = "Application";
        String pathModulo = System.getProperty("user.dir") + SLASH;
        String nameModulo = file.estraeDirectoryFinaleSenzaSlash(pathModulo);
        pathModulo = pathModulo + AEWizCost.dirModulo.get() + nameModulo + FlowCost.SLASH;
        List<String> lista = file.getFilesNames(pathModulo);

        for (String nome : lista) {
            if (nome.endsWith(tag)) {
                projectNameUpper = text.levaCoda(nome, tag);
                break;
            }
        }

        return projectNameUpper;
    }

    //    public List<AEWizCost> getVuoteProject() {
    //        List<AEWizCost> listaWizCost = new ArrayList<>();
    //
    //        for (AEWizCost aeWizCost : getVuote()) {
    //            if (aeWizCost.isNewProject() || aeWizCost.isUpdateProject()) {
    //                listaWizCost.add(aeWizCost);
    //            }
    //        }
    //
    //        return listaWizCost;
    //    }

    public List<AEWizCost> printProgetto() {
        List<AEWizCost> listaWiz = null;
        List<AEWizValue> listaType = new ArrayList<>();
        listaType.add(AEWizValue.inserito);
        listaType.add(AEWizValue.derivato);

        listaWiz = getAllTypeWiz(listaType);
        printInfoBase(listaWiz, "Valori correnti per un nuovo progetto");

        return listaWiz;

        //        if (array.isEmpty(vuoteProgetto)) {
        //            printInfoBase(vuoteProgetto, "Costanti del progetto a cui manca ancora un valore indispensabile");
        //        }
        //        else {
        //            printInfoBase(getHannoValoreValido(), "Costanti del progetto con i valori utilizzabili");
        //        }
    }

    //--metodo statico invocato da Wizard.initView()
    public void printInfoBase(List<AEWizCost> lista, String titolo) {
        System.out.println(VUOTA);
        System.out.println("********************");
        System.out.println(titolo + " (" + lista.size() + ")");
        System.out.println("********************");
        for (AEWizCost aeWizCost : lista) {
            System.out.print("AEWizCost." + aeWizCost.name() + ": \"" + aeWizCost.getDescrizione() + "\" " + FlowCost.UGUALE_SPAZIATO + aeWizCost.get());
            //            System.out.print(FlowCost.FORWARD + "AECopyWiz." + aeWizCost.getCopyWiz().name());
            System.out.println(VUOTA);
        }
        System.out.println(VUOTA);
    }

    //--metodo statico invocato da Wizard.initView()
    public void printInfoStart() {
        printInfoBase(getNecessitanoInserimentoValore(), "Tutte le costanti a cui bisogna inserire il valore in runtime.");
        printInfoBase(getInseritoValore(), "Costanti a cui è già stato inserito un valore in runtime.");
        printInfoBase(getVuote(), "Costanti a cui manca ancora l'inserimento del valore.");
        printInfoBase(getHannoValoreVuoto(), "Costanti a cui manca ancora un valore o inserito o calcolato");
        printInfoBase(getHannoValoreValido(), "Costanti con un valore valido o inserito o calcolato");

        //        printInfoBase(getDirectory(), "Directory di percorso. Valori statici ed immutabili");
        //        printInfoBase(getSistema(), "Variabili di sistema. Dipende dal programma in uso");
        //        printInfoBase(getNomeFile(), "Nome e file di percorso. Dipende dal progetto selezionato");
        //        printInfoBase(getPath(), "Path di percorso. Dipende dal progetto selezionato");
        //        printInfoBase(getPackages(), "Variabili del package. Dipende dal package selezionato");
        //
        //        System.out.println(FlowCost.VUOTA);
        //        System.out.println("********************");
        //        System.out.println("Costanti statiche indipendenti dal progetto che sta girando");
        //        System.out.println("********************");
        //        for (AEWizCost aeWizCost : AEWizCost.values()) {
        //            System.out.print("AEWizCost." + aeWizCost.name() + ": \"" + aeWizCost.descrizione + "\" " + FlowCost.UGUALE + aeWizCost.value);
        //            if (aeWizCost.isNewProject() || aeWizCost.isUpdateProject()) {
        //                System.out.print(FlowCost.FORWARD + "AECopyWiz." + aeWizCost.copyWiz.name());
        //            }
        //            System.out.println(FlowCost.VUOTA);
        //        }
        //        System.out.println(FlowCost.VUOTA);
    }


    //--metodo statico invocato da Wizard.initView()
    public void printInfoPackage() {
        //        printInfoBase(getPackages(), "Variabili del package. Dipende dal package selezionato");
    }

    //--metodo statico
    public void printVuote() {
        System.out.println(VUOTA);
        System.out.println("********************");
        System.out.println("Costanti statiche a cui manca ancora il valore");
        System.out.println("********************");
        for (AEWizCost aeWizCost : getNecessitanoInserimentoValore()) {
            System.out.println("AEWizCost." + aeWizCost.name() + ": " + aeWizCost.getDescrizione());
        }
        System.out.println(VUOTA);
    }

}
