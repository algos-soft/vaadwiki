package it.algos.vaadflow14.backend.service;

import it.algos.vaadflow14.backend.application.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.wrapper.*;
import org.apache.commons.io.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;


/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: dom, 28-giu-2020
 * Time: 15:10
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti SpringBoot non la costruisce <br>
 * Estende la classe astratta AAbstractService che mantiene i riferimenti agli altri services <br>
 * L'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(AAnnotationService.class); <br>
 * 3) @Autowired private AArrayService annotation; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AFileService extends AAbstractService {

    public static final String PARAMETRO_NULLO = "Il parametro in ingresso è nullo";

    public static final String PATH_NULLO = "Il path in ingresso è nullo o vuoto";

    public static final String PATH_NOT_ABSOLUTE = "Il primo carattere del path NON è uno '/' (slash)";

    public static final String NON_ESISTE_FILE = "Il file non esiste";

    public static final String PATH_SENZA_SUFFIX = "Manca il 'suffix' terminale";

    public static final String PATH_FILE_ESISTENTE = "Esiste già il file";

    public static final String NON_E_FILE = "Non è un file";

    public static final String NON_CREATO_FILE = "Il file non è stato creato";

    public static final String NON_COPIATO_FILE = "Il file non è stato copiato";

    public static final String NON_CANCELLATO_FILE = "Il file non è stato cancellato";

    public static final String NON_CANCELLATA_DIRECTORY = "La directory non è stata cancellata";

    public static final String NON_ESISTE_DIRECTORY = "La directory non esiste";

    public static final String NON_CREATA_DIRECTORY = "La directory non è stata creata";

    public static final String NON_E_DIRECTORY = "Non è una directory";

    public static final String DIR_PROGETTO = "/src/";

    public static final String DIR_PROGETTO_VUOTO = "/src/main/java/";

    /**
     * versione della classe per la serializzazione
     */
    private static final long serialVersionUID = 1L;


    /**
     * Controlla l'esistenza di una directory
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed inziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     * Una volta costruita la directory, getPath() e getAbsolutePath() devono essere uguali
     *
     * @param directoryToBeChecked con path completo che DEVE cominciare con '/' SLASH
     *
     * @return true se la directory esiste, false se non sono rispettate le condizioni della richiesta
     */
    public boolean isEsisteDirectory(File directoryToBeChecked) {
        return isEsisteDirectoryStr(directoryToBeChecked).equals(FlowCost.VUOTA);
    }


    /**
     * Controlla l'esistenza di una directory
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed iniziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     * Una volta costruita la directory, getPath() e getAbsolutePath() devono essere uguali
     *
     * @param directoryToBeChecked con path completo che DEVE cominciare con '/' SLASH
     *
     * @return testo di errore, vuoto se il file esiste
     */
    public String isEsisteDirectoryStr(File directoryToBeChecked) {
        if (directoryToBeChecked == null) {
            return PARAMETRO_NULLO;
        }

        if (text.isEmpty(directoryToBeChecked.getName())) {
            return PATH_NULLO;
        }

        if (!directoryToBeChecked.getPath().equals(directoryToBeChecked.getAbsolutePath())) {
            return PATH_NOT_ABSOLUTE;
        }

        if (directoryToBeChecked.exists()) {
            if (directoryToBeChecked.isDirectory()) {
                return FlowCost.VUOTA;
            }
            else {
                return NON_E_DIRECTORY;
            }
        }
        else {
            return NON_ESISTE_DIRECTORY;
        }
    }


    /**
     * Creazioni di una directory 'parent' <br>
     * Se manca il path completo alla creazione di un file, creo la directory 'parent' di quel file <br>
     * Riprovo la creazione del file <br>
     */
    public String creaDirectoryParentAndFile(File unFile) {
        String risposta = NON_CREATO_FILE;
        String parentDirectoryName;
        File parentDirectoryFile = null;
        boolean parentDirectoryCreata = false;

        if (unFile != null) {
            parentDirectoryName = unFile.getParent();
            parentDirectoryFile = new File(parentDirectoryName);
            parentDirectoryCreata = parentDirectoryFile.mkdirs();
        }

        if (parentDirectoryCreata) {
            try { // prova ad eseguire il codice
                unFile.createNewFile();
                risposta = FlowCost.VUOTA;
            } catch (Exception unErrore) { // intercetta l'errore
                System.out.println("Errore nel path per la creazione di un file");
            }
        }

        return risposta;
    }


    /**
     * Controlla l'esistenza di una directory <br>
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed iniziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * Controlla che getPath() e getAbsolutePath() siano uguali <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     * Una volta costruita la directory, getPath() e getAbsolutePath() devono essere uguali
     *
     * @param absolutePathDirectoryToBeChecked path completo della directory che DEVE cominciare con '/' SLASH
     *
     * @return true se la directory esiste, false se non sono rispettate le condizioni della richiesta
     */
    public boolean isNotEsisteDirectory(String absolutePathDirectoryToBeChecked) {
        return !isEsisteDirectoryStr(absolutePathDirectoryToBeChecked).equals(FlowCost.VUOTA);
    }


    /**
     * Controlla l'esistenza di una directory <br>
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed iniziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * Controlla che getPath() e getAbsolutePath() siano uguali <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     * Una volta costruita la directory, getPath() e getAbsolutePath() devono essere uguali
     *
     * @param absolutePathDirectoryToBeChecked path completo della directory che DEVE cominciare con '/' SLASH
     *
     * @return true se la directory esiste, false se non sono rispettate le condizioni della richiesta
     */
    public boolean isEsisteDirectory(String absolutePathDirectoryToBeChecked) {
        return isEsisteDirectoryStr(absolutePathDirectoryToBeChecked).equals(FlowCost.VUOTA);
    }


    /**
     * Controlla l'esistenza di una directory <br>
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed iniziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * Controlla che getPath() e getAbsolutePath() siano uguali <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     * Una volta costruita la directory, getPath() e getAbsolutePath() devono essere uguali
     *
     * @param absolutePathDirectoryToBeChecked path completo della directory che DEVE cominciare con '/' SLASH
     *
     * @return testo di errore, vuoto se la directory esiste
     */
    public String isEsisteDirectoryStr(String absolutePathDirectoryToBeChecked) {
        if (text.isEmpty(absolutePathDirectoryToBeChecked)) {
            return PATH_NULLO;
        }

        if (this.isNotSlash(absolutePathDirectoryToBeChecked)) {
            return PATH_NOT_ABSOLUTE;
        }

        return isEsisteDirectoryStr(new File(absolutePathDirectoryToBeChecked));
    }


    /**
     * Controlla l'esistenza di un file <br>
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed iniziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * Controlla che getPath() e getAbsolutePath() siano uguali <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     *
     * @param fileToBeChecked con path completo che DEVE cominciare con '/' SLASH
     *
     * @return true se il file esiste, false se non sono rispettate le condizioni della richiesta
     */
    public boolean isEsisteFile(File fileToBeChecked) {
        return isEsisteFileStr(fileToBeChecked).equals(FlowCost.VUOTA);
    }


    /**
     * Controlla l'esistenza di un file <br>
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed iniziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     *
     * @param pathDirectoryToBeChecked path completo della directory che DEVE cominciare con '/' SLASH
     * @param fileName                 da controllare
     *
     * @return true se il file esiste, false se non sono rispettate le condizioni della richiesta
     */
    public boolean isEsisteFile(String pathDirectoryToBeChecked, String fileName) {
        return isEsisteFile(pathDirectoryToBeChecked + FlowCost.SLASH + fileName);
    }


    /**
     * Controlla l'esistenza di un file <br>
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed iniziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     *
     * @param absolutePathFileWithSuffixToBeChecked path completo del file che DEVE cominciare con '/' SLASH
     *
     * @return true se il file esiste, false se non sono rispettate le condizioni della richiesta
     */
    public boolean isEsisteFile(String absolutePathFileWithSuffixToBeChecked) {
        return isEsisteFileStr(absolutePathFileWithSuffixToBeChecked).equals(FlowCost.VUOTA);
    }


    /**
     * Controlla l'esistenza di un file <br>
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed iniziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     *
     * @param absolutePathFileWithSuffixToBeChecked path completo del file che DEVE cominciare con '/' SLASH
     *
     * @return testo di errore, vuoto se il file esiste
     */
    public String isEsisteFileStr(String absolutePathFileWithSuffixToBeChecked) {
        String risposta = FlowCost.VUOTA;

        if (text.isEmpty(absolutePathFileWithSuffixToBeChecked)) {
            return PATH_NULLO;
        }

        if (this.isNotSlash(absolutePathFileWithSuffixToBeChecked)) {
            return PATH_NOT_ABSOLUTE;
        }

        risposta = isEsisteFileStr(new File(absolutePathFileWithSuffixToBeChecked));
        if (!risposta.equals(FlowCost.VUOTA)) {
            if (isEsisteDirectory(new File(absolutePathFileWithSuffixToBeChecked))) {
                return NON_E_FILE;
            }

            if (this.isNotSuffix(absolutePathFileWithSuffixToBeChecked)) {
                return PATH_SENZA_SUFFIX;
            }
        }

        return risposta;
    }


    /**
     * Controlla l'esistenza di un file <br>
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed iniziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * Controlla che getPath() e getAbsolutePath() siano uguali <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     *
     * @param fileToBeChecked con path completo che DEVE cominciare con '/' SLASH
     *
     * @return testo di errore, vuoto se il file esiste
     */
    public String isEsisteFileStr(File fileToBeChecked) {
        if (fileToBeChecked == null) {
            return PARAMETRO_NULLO;
        }

        if (text.isEmpty(fileToBeChecked.getName())) {
            return PATH_NULLO;
        }

        if (!fileToBeChecked.getPath().equals(fileToBeChecked.getAbsolutePath())) {
            return PATH_NOT_ABSOLUTE;
        }

        if (fileToBeChecked.exists()) {
            if (fileToBeChecked.isFile()) {
                return FlowCost.VUOTA;
            }
            else {
                return NON_E_FILE;
            }
        }
        else {
            if (this.isNotSuffix(fileToBeChecked.getAbsolutePath())) {
                return PATH_SENZA_SUFFIX;
            }

            if (!fileToBeChecked.exists()) {
                return NON_ESISTE_FILE;
            }

            return FlowCost.VUOTA;
        }
    }


    /**
     * Crea un nuovo file
     * <p>
     * Il file DEVE essere costruita col path completo, altrimenti assume che sia nella directory in uso corrente
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed iniziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     * Se manca la directory, viene creata dal System <br>
     *
     * @param absolutePathFileWithSuffixToBeCreated path completo del file che DEVE cominciare con '/' SLASH e compreso il suffisso
     *
     * @return true se il file è stato creato, false se non sono rispettate le condizioni della richiesta
     */
    public boolean creaFile(String absolutePathFileWithSuffixToBeCreated) {
        return creaFileStr(absolutePathFileWithSuffixToBeCreated).equals(FlowCost.VUOTA);
    }


    /**
     * Crea un nuovo file
     * <p>
     * Il file DEVE essere costruita col path completo, altrimenti assume che sia nella directory in uso corrente
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed iniziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     * Se manca la directory, viene creata dal System <br>
     *
     * @param absolutePathFileWithSuffixToBeCreated path completo del file che DEVE cominciare con '/' SLASH e compreso il suffisso
     *
     * @return testo di errore, vuoto se il file è stato creato
     */
    public String creaFileStr(String absolutePathFileWithSuffixToBeCreated) {

        if (text.isEmpty(absolutePathFileWithSuffixToBeCreated)) {
            return PATH_NULLO;
        }

        return creaFileStr(new File(absolutePathFileWithSuffixToBeCreated));
    }


    /**
     * Crea un nuovo file
     * <p>
     * Il file DEVE essere costruita col path completo, altrimenti assume che sia nella directory in uso corrente
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed iniziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     * Se manca la directory, viene creata dal System <br>
     *
     * @param fileToBeCreated con path completo che DEVE cominciare con '/' SLASH
     *
     * @return true se il file è stato creato, false se non sono rispettate le condizioni della richiesta
     */
    public boolean creaFile(File fileToBeCreated) {
        return creaFileStr(fileToBeCreated).equals(FlowCost.VUOTA);
    }


    /**
     * Crea un nuovo file
     * <p>
     * Il file DEVE essere costruita col path completo, altrimenti assume che sia nella directory in uso corrente
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed iniziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     * Se manca la directory, viene creata dal System <br>
     *
     * @param fileToBeCreated con path completo che DEVE cominciare con '/' SLASH
     *
     * @return testo di errore, vuoto se il file è stato creato
     */
    public String creaFileStr(File fileToBeCreated) {
        if (fileToBeCreated == null) {
            return PARAMETRO_NULLO;
        }

        if (text.isEmpty(fileToBeCreated.getName())) {
            return PATH_NULLO;
        }

        if (!fileToBeCreated.getPath().equals(fileToBeCreated.getAbsolutePath())) {
            return PATH_NOT_ABSOLUTE;
        }

        if (this.isNotSuffix(fileToBeCreated.getAbsolutePath())) {
            return PATH_SENZA_SUFFIX;
        }

        try {
            fileToBeCreated.createNewFile();
        } catch (Exception unErrore) {
            return creaDirectoryParentAndFile(fileToBeCreated);
        }

        return fileToBeCreated.exists() ? FlowCost.VUOTA : NON_CREATO_FILE;
    }


    /**
     * Crea una nuova directory
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed iniziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     *
     * @param absolutePathDirectoryToBeCreated path completo della directory che DEVE cominciare con '/' SLASH
     *
     * @return true se la directory è stata creata, false se non sono rispettate le condizioni della richiesta
     */
    public boolean creaDirectory(String absolutePathDirectoryToBeCreated) {
        return creaDirectoryStr(absolutePathDirectoryToBeCreated).equals(FlowCost.VUOTA);
    }


    /**
     * Crea una nuova directory
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed iniziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     *
     * @param absolutePathDirectoryToBeCreated path completo della directory che DEVE cominciare con '/' SLASH
     *
     * @return testo di errore, vuoto se il file è stato creato
     */
    public String creaDirectoryStr(String absolutePathDirectoryToBeCreated) {
        if (text.isEmpty(absolutePathDirectoryToBeCreated)) {
            return PATH_NULLO;
        }

        return creaDirectoryStr(new File(absolutePathDirectoryToBeCreated));
    }


    /**
     * Crea una nuova directory
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed iniziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     *
     * @param directoryToBeCreated con path completo che DEVE cominciare con '/' SLASH
     *
     * @return true se la directory è stata creata, false se non sono rispettate le condizioni della richiesta
     */
    public boolean creaDirectory(File directoryToBeCreated) {
        return creaDirectoryStr(directoryToBeCreated).equals(FlowCost.VUOTA);
    }


    /**
     * Crea una nuova directory
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed iniziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     *
     * @param directoryToBeCreated con path completo che DEVE cominciare con '/' SLASH
     *
     * @return testo di errore, vuoto se il file è stato creato
     */
    public String creaDirectoryStr(File directoryToBeCreated) {
        if (directoryToBeCreated == null) {
            return PARAMETRO_NULLO;
        }

        if (text.isEmpty(directoryToBeCreated.getName())) {
            return PATH_NULLO;
        }

        if (!directoryToBeCreated.getPath().equals(directoryToBeCreated.getAbsolutePath())) {
            return PATH_NOT_ABSOLUTE;
        }

        if (!this.isNotSuffix(directoryToBeCreated.getAbsolutePath())) {
            return NON_E_DIRECTORY;
        }

        try { // prova ad eseguire il codice
            directoryToBeCreated.mkdirs();
        } catch (Exception unErrore) { // intercetta l'errore
            return NON_CREATA_DIRECTORY;
        }

        return directoryToBeCreated.exists() ? FlowCost.VUOTA : NON_CREATA_DIRECTORY;
    }


    /**
     * Cancella un file
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed iniziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     *
     * @param absolutePathFileWithSuffixToBeCanceled path completo del file che DEVE cominciare con '/' SLASH e compreso il suffisso
     *
     * @return true se il file è stato cancellato oppure non esisteva
     */
    public boolean deleteFile(String absolutePathFileWithSuffixToBeCanceled) {
        return deleteFileStr(absolutePathFileWithSuffixToBeCanceled).equals(FlowCost.VUOTA);
    }


    /**
     * Cancella un file
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed iniziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     *
     * @param absolutePathFileWithSuffixToBeCanceled path completo del file che DEVE cominciare con '/' SLASH e compreso il suffisso
     *
     * @return testo di errore, vuoto se il file è stato cancellato
     */
    public String deleteFileStr(String absolutePathFileWithSuffixToBeCanceled) {
        if (text.isEmpty(absolutePathFileWithSuffixToBeCanceled)) {
            return PATH_NULLO;
        }

        return deleteFileStr(new File(absolutePathFileWithSuffixToBeCanceled));
    }


    /**
     * Cancella un file
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed iniziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     *
     * @param fileToBeDeleted con path completo che DEVE cominciare con '/' SLASH
     *
     * @return true se il file è stato cancellato oppure non esisteva
     */
    public boolean deleteFile(File fileToBeDeleted) {
        return deleteFileStr(fileToBeDeleted).equals(FlowCost.VUOTA);
    }


    /**
     * Cancella un file
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed iniziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     *
     * @param fileToBeDeleted con path completo che DEVE cominciare con '/' SLASH
     *
     * @return testo di errore, vuoto se il file è stato creato
     */
    public String deleteFileStr(File fileToBeDeleted) {

        if (fileToBeDeleted == null) {
            return PARAMETRO_NULLO;
        }

        if (text.isEmpty(fileToBeDeleted.getName())) {
            return PATH_NULLO;
        }

        if (!fileToBeDeleted.getPath().equals(fileToBeDeleted.getAbsolutePath())) {
            return PATH_NOT_ABSOLUTE;
        }

        if (this.isNotSuffix(fileToBeDeleted.getAbsolutePath())) {
            return PATH_SENZA_SUFFIX;
        }

        if (!fileToBeDeleted.exists()) {
            return NON_ESISTE_FILE;
        }

        if (fileToBeDeleted.delete()) {
            return FlowCost.VUOTA;
        }
        else {
            return NON_CANCELLATO_FILE;
        }

    }


    /**
     * Cancella una directory
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed iniziare con uno 'slash' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     *
     * @param absolutePathDirectoryToBeDeleted path completo della directory che DEVE cominciare con '/' SLASH
     *
     * @return true se la directory è stato cancellato oppure non esisteva
     */
    public boolean deleteDirectory(String absolutePathDirectoryToBeDeleted) {
        return deleteDirectoryStr(absolutePathDirectoryToBeDeleted).equals(FlowCost.VUOTA);
    }


    /**
     * Cancella una directory
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed iniziare con uno 'slash' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     *
     * @param absolutePathDirectoryToBeDeleted path completo della directory che DEVE cominciare con '/' SLASH
     *
     * @return testo di errore, vuoto se la directory è stata cancellata
     */
    public String deleteDirectoryStr(String absolutePathDirectoryToBeDeleted) {
        if (text.isEmpty(absolutePathDirectoryToBeDeleted)) {
            return PATH_NULLO;
        }

        return deleteDirectoryStr(new File(absolutePathDirectoryToBeDeleted));
    }


    /**
     * Cancella una directory
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed iniziare con uno 'slash' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     *
     * @param directoryToBeDeleted con path completo che DEVE cominciare con '/' SLASH
     *
     * @return true se la directory è stata cancellata oppure non esisteva
     */
    public boolean deleteDirectory(File directoryToBeDeleted) {
        return deleteDirectoryStr(directoryToBeDeleted).equals(FlowCost.VUOTA);
    }


    /**
     * Cancella una directory
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed iniziare con uno 'slash' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     *
     * @param directoryToBeDeleted con path completo che DEVE cominciare con '/' SLASH
     *
     * @return testo di errore, vuoto se la directory è stata cancellata
     */
    public String deleteDirectoryStr(File directoryToBeDeleted) {
        if (directoryToBeDeleted == null) {
            return PARAMETRO_NULLO;
        }

        if (text.isEmpty(directoryToBeDeleted.getName())) {
            return PATH_NULLO;
        }

        if (!directoryToBeDeleted.getPath().equals(directoryToBeDeleted.getAbsolutePath())) {
            return PATH_NOT_ABSOLUTE;
        }

        if (!directoryToBeDeleted.exists()) {
            return NON_ESISTE_DIRECTORY;
        }

        if (directoryToBeDeleted.delete()) {
            return FlowCost.VUOTA;
        }
        else {
            try {
                FileUtils.deleteDirectory(directoryToBeDeleted);
                return FlowCost.VUOTA;
            } catch (Exception unErrore) {
                logger.error(unErrore, this.getClass(), "deleteDirectoryStr");
                return NON_CANCELLATA_DIRECTORY;
            }
        }
    }


    /**
     * Copia un file
     * <p>
     * Se manca il file sorgente, non fa nulla <br>
     * Se manca la directory di destinazione, la crea <br>
     * Se esiste il file destinazione, non fa nulla <br>
     *
     * @param srcPath  nome completo del file sorgente
     * @param destPath nome completo del file destinazione
     *
     * @return true se il file è stato copiato
     */
    @Deprecated
    public boolean copyFile(String srcPath, String destPath) {
        return copyFileStr(srcPath, destPath) == FlowCost.VUOTA;
    }


    /**
     * Copia un file sovrascrivendolo se già esistente
     * <p>
     * Se manca il file sorgente, non fa nulla <br>
     * Se esiste il file destinazione, lo cancella prima di copiarlo <br>
     *
     * @param srcPath  nome completo del file sorgente
     * @param destPath nome completo del file destinazione
     *
     * @return true se il file è stato copiato
     */
    public boolean copyFileDeletingAll(String srcPath, String destPath) {
        if (!isEsisteFile(srcPath)) {
            return false;
        }

        if (isEsisteFile(destPath)) {
            deleteFile(destPath);
        }

        return copyFileStr(srcPath, destPath) == FlowCost.VUOTA;
    }


    /**
     * Copia un file solo se non già esistente
     * <p>
     * Se manca il file sorgente, non fa nulla <br>
     * Se esiste il file destinazione, non fa nulla <br>
     *
     * @param srcPath  nome completo del file sorgente
     * @param destPath nome completo del file destinazione
     *
     * @return true se il file è stato copiato
     */
    public boolean copyFileOnlyNotExisting(String srcPath, String destPath) {
        return copyFileStr(srcPath, destPath) == FlowCost.VUOTA;
    }


    /**
     * Copia un file
     * <p>
     * Se manca il file sorgente, non fa nulla <br>
     * Se manca la directory di destinazione, la crea <br>
     * Se esiste il file destinazione, non fa nulla <br>
     *
     * @param srcPath  nome completo del file sorgente
     * @param destPath nome completo del file destinazione
     *
     * @return testo di errore, vuoto se il file è stato copiato
     */
    public String copyFileStr(String srcPath, String destPath) {
        String risposta = FlowCost.VUOTA;
        File srcFile = new File(srcPath);
        File destFile = new File(destPath);

        if (!isEsisteFile(srcPath)) {
            return NON_ESISTE_FILE;
        }

        if (isEsisteFile(destPath)) {
            return PATH_FILE_ESISTENTE;
        }

        try { // prova ad eseguire il codice
            FileUtils.copyFile(srcFile, destFile);
        } catch (IOException e) {
            return NON_COPIATO_FILE;
        }

        return risposta;
    }


    /**
     * Copia una directory <br>
     * <p>
     * Controlla che siano validi i path di riferimento <br>
     * Controlla che esista la directory sorgente da copiare <br>
     * Se manca la directory sorgente, non fa nulla <br>
     * Se non esiste la directory di destinazione, la crea <br>
     * Se esiste la directory di destinazione ed è AECopyDir.soloSeNonEsiste, non fa nulla <br>
     * Se esiste la directory di destinazione ed è AECopyDir.deletingAll, la cancella e poi la copia <br>
     * Se esiste la directory di destinazione ed è AECopyDir.addingOnly, la integra aggiungendo file/cartelle <br>
     * Nei messaggi di avviso, accorcia il destPath eliminando i livelli precedenti alla directory indicata <br>
     *
     * @param typeCopy modalità di comportamento se esiste la directory di destinazione
     * @param srcPath  nome completo della directory sorgente
     * @param destPath nome completo della directory destinazione
     *
     * @return true se la directory  è stata copiata
     */
    public boolean copyDirectory(AECopy typeCopy, String srcPath, String destPath) {
        return copyDirectory(typeCopy, srcPath, destPath, FlowCost.VUOTA);
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
     * @param typeCopy       modalità di comportamento se esiste il file di destinazione
     * @param srcPath        nome completo di suffisso del file sorgente
     * @param destPath       nome completo di suffisso del file da creare
     * @param firstDirectory da cui iniziare il path per il messaggio di avviso
     */
    public void copyFile(AECopy typeCopy, String srcPath, String destPath, String firstDirectory) {
        boolean esisteFileDest;
        String message;
        String path = this.findPathBreve(destPath, firstDirectory);

        if (!this.isEsisteFile(srcPath)) {
            logger.warn("Non sono riuscito a trovare il file " + srcPath + " nella directory indicata", this.getClass(), "copyFile");
            return;
        }

        esisteFileDest = this.isEsisteFile(destPath);
        switch (typeCopy) {
            case fileSovrascriveSempreAncheSeEsiste:
                if (esisteFileDest) {
                    message = "Il file: " + path + " esisteva già ed è stato modificato.";
                    logger.info(message, this.getClass(), "copyFile");
                }
                else {
                    message = "Il file: " + path + " non esisteva ed è stato copiato.";
                    logger.info(message, this.getClass(), "copyFile");
                }
                this.copyFileDeletingAll(srcPath, destPath);
                break;
            case fileSoloSeNonEsiste:
                if (esisteFileDest) {
                    message = "Il file: " + path + " esisteva già e non è stato modificato.";
                    logger.info(message, this.getClass(), "copyFile");
                }
                else {
                    this.copyFileDeletingAll(srcPath, destPath);
                    message = "Il file: " + path + " non esisteva ed è stato copiato.";
                    logger.info(message, this.getClass(), "copyFile");
                }
                break;
            default:
                logger.warn("Switch - caso non definito", this.getClass(), "copyFile");
                break;
        }
    }

    /**
     * Copia una directory <br>
     * <p>
     * Controlla che siano validi i path di riferimento <br>
     * Controlla che esista la directory sorgente da copiare <br>
     * Se manca la directory sorgente, non fa nulla <br>
     * Se non esiste la directory di destinazione, la crea <br>
     * Se esiste la directory di destinazione ed è AECopyDir.soloSeNonEsiste, non fa nulla <br>
     * Se esiste la directory di destinazione ed è AECopyDir.deletingAll, la cancella e poi la copia <br>
     * Se esiste la directory di destinazione ed è AECopyDir.addingOnly, la integra aggiungendo file/cartelle <br>
     * Nei messaggi di avviso, accorcia il destPath eliminando i livelli precedenti alla directory indicata <br>
     *
     * @param typeCopy       modalità di comportamento se esiste la directory di destinazione
     * @param srcPath        nome completo della directory sorgente
     * @param destPath       nome completo della directory destinazione
     * @param firstDirectory da cui iniziare il path per il messaggio di avviso
     *
     * @return true se la directory  è stata copiata
     */
    public boolean copyDirectory(AECopy typeCopy, String srcPath, String destPath, String firstDirectory) {
        return copyDirectory(typeCopy, srcPath, destPath, firstDirectory, false);
    }

    /**
     * Copia una directory <br>
     * <p>
     * Controlla che siano validi i path di riferimento <br>
     * Controlla che esista la directory sorgente da copiare <br>
     * Se manca la directory sorgente, non fa nulla <br>
     * Se non esiste la directory di destinazione, la crea <br>
     * Se esiste la directory di destinazione ed è AECopyDir.soloSeNonEsiste, non fa nulla <br>
     * Se esiste la directory di destinazione ed è AECopyDir.deletingAll, la cancella e poi la copia <br>
     * Se esiste la directory di destinazione ed è AECopyDir.addingOnly, la integra aggiungendo file/cartelle <br>
     * Nei messaggi di avviso, accorcia il destPath eliminando i livelli precedenti alla directory indicata <br>
     *
     * @param typeCopy       modalità di comportamento se esiste la directory di destinazione
     * @param srcPath        nome completo della directory sorgente
     * @param destPath       nome completo della directory destinazione
     * @param firstDirectory da cui iniziare il path per il messaggio di avviso
     * @param stampaInfo     flag per usare il logger
     *
     * @return true se la directory  è stata copiata
     */
    public boolean copyDirectory(AECopy typeCopy, String srcPath, String destPath, String firstDirectory, boolean stampaInfo) {
        boolean copiata = false;
        boolean esisteDest;
        String message = FlowCost.VUOTA;
        String tag;
        String path;

        if (text.isEmpty(srcPath) || text.isEmpty(destPath)) {
            tag = text.isEmpty(srcPath) ? "srcPath" : "destPath";
            message = "Manca il " + tag + " della directory da copiare.";
        }
        else {
            path = this.findPathBreve(destPath, firstDirectory);
            if (isEsisteDirectory(srcPath)) {
                switch (typeCopy) {
                    case dirSoloSeNonEsiste:
                        copiata = copyDirectoryOnlyNotExisting(srcPath, destPath);
                        if (copiata) {
                            message = "La directory: " + path + " non esisteva ed è stata copiata.";
                        }
                        else {
                            message = "La directory: " + path + " esisteva già e non è stata toccata.";
                        }
                        if (stampaInfo) {
                            logger.info(message, this.getClass(), "copyDirectory");
                        }
                        message = FlowCost.VUOTA;
                        break;
                    case dirDeletingAll:
                        esisteDest = isEsisteDirectory(destPath);
                        copiata = copyDirectoryDeletingAll(srcPath, destPath);
                        if (copiata) {
                            if (esisteDest) {
                                message = "La directory: " + path + " esisteva già ma è stata sostituita.";
                            }
                            else {
                                message = "La directory: " + path + " non esisteva ed è stata creata.";
                            }
                            if (stampaInfo) {
                                logger.info(message, this.getClass(), "copyDirectory");
                            }
                            message = FlowCost.VUOTA;
                        }
                        else {
                            if (esisteDest) {
                                message = "Non sono riuscito a sostituire " + path;
                            }
                            else {
                                message = "Non sono riuscito a creare " + path;
                            }
                        }
                        break;
                    case dirAddingOnly:
                        esisteDest = isEsisteDirectory(destPath);
                        copiata = copyDirectoryAddingOnly(srcPath, destPath);
                        if (copiata) {
                            if (esisteDest) {
                                message = "La directory: " + path + " esisteva già ma è stata integrata.";
                            }
                            else {
                                message = "La directory: " + path + " non esisteva ed è stata creata.";
                            }
                            if (stampaInfo) {
                                logger.info(message, this.getClass(), "copyDirectory");
                            }
                        }
                        else {
                            if (esisteDest) {
                                message = "Non sono riuscito ad integrare la directory: " + path;
                            }
                            else {
                                message = "Non sono riuscito a creare " + path;
                            }
                        }
                        break;
                    default:
                        copiata = copyDirectoryAddingOnly(srcPath, destPath);
                        logger.warn("Switch - caso non definito", this.getClass(), "copyDirectory");
                        break;
                }
            }
            else {
                message = "Manca la directory " + srcPath + " da copiare";
            }
        }

        if (!copiata && text.isValid(message)) {
            logger.error(message, this.getClass(), "copyDirectory");
        }

        return copiata;
    }


    /**
     * Copia una directory
     * <p>
     * Se manca la directory sorgente, non fa nulla <br>
     * Se manca la directory di destinazione, la crea <br>
     * Se esiste la directory di destinazione, non fa nulla <br>
     *
     * @param srcPath  nome completo della directory sorgente
     * @param destPath nome completo della directory destinazione
     *
     * @return true se la directory  è stata copiata
     */
    @Deprecated
    public boolean copyDirectory(String srcPath, String destPath) {
        return copyDirectoryAddingOnly(srcPath, destPath);
    }


    /**
     * Copia una directory sostituendo integralmente quella eventualmente esistente <br>
     * <p>
     * Se manca la directory sorgente, non fa nulla <br>
     * Se manca la directory di destinazione, la crea <br>
     * Se esiste la directory di destinazione, la cancella prima di ricopiarla <br>
     * Tutte i files e le subdirectories originali vengono cancellata <br>
     *
     * @param srcPath  nome parziale del path sorgente
     * @param destPath nome parziale del path destinazione
     * @param dirName  nome della directory da copiare
     *
     * @return true se la directory  è stata copiata
     */
    public boolean copyDirectoryDeletingAll(String srcPath, String destPath, String dirName) {
        return copyDirectoryDeletingAll(srcPath + dirName, destPath + dirName);
    }


    /**
     * Copia una directory sostituendo integralmente quella eventualmente esistente <br>
     * <p>
     * Se manca la directory sorgente, non fa nulla <br>
     * Se manca la directory di destinazione, la crea <br>
     * Se esiste la directory di destinazione, la cancella prima di ricopiarla <br>
     * Tutte i files e le subdirectories originali vengono cancellata <br>
     *
     * @param srcPath  nome completo della directory sorgente
     * @param destPath nome completo della directory destinazione
     *
     * @return true se la directory  è stata copiata
     */
    public boolean copyDirectoryDeletingAll(String srcPath, String destPath) {
        File srcDir = new File(srcPath);
        File destDir = new File(destPath);

        if (!isEsisteDirectory(srcPath)) {
            return false;
        }

        if (isEsisteDirectory(destPath)) {
            try {
                FileUtils.forceDelete(new File(destPath));
            } catch (Exception unErrore) {
            }
        }

        if (isEsisteDirectory(destPath)) {
            return false;
        }
        else {
            try {
                FileUtils.copyDirectory(srcDir, destDir);
                return true;
            } catch (Exception unErrore) {
            }
        }

        return false;
    }


    /**
     * Copia una directory solo se non esisteva <br>
     * <p>
     * Se manca la directory sorgente, non fa nulla <br>
     * Se manca la directory di destinazione, la crea <br>
     * Se esiste la directory di destinazione, non fa nulla <br>
     *
     * @param srcPath  nome completo della directory sorgente
     * @param destPath nome completo della directory destinazione
     *
     * @return true se la directory  è stata copiata
     */
    public boolean copyDirectoryOnlyNotExisting(String srcPath, String destPath) {
        if (!isEsisteDirectory(srcPath)) {
            return false;
        }

        if (isEsisteDirectory(destPath)) {
            return false;
        }

        return copyDirectoryDeletingAll(srcPath, destPath);
    }


    /**
     * Copia una directory aggiungendo files e subdirectories a quelli eventualmente esistenti <br>
     * Lascia inalterate subdirectories e files già esistenti <br>
     * <p>
     * Se manca la directory sorgente, non fa nulla <br>
     * Se manca la directory di destinazione, la crea <br>
     * Se esiste la directory destinazione, aggiunge files e subdirectories <br>
     * Tutti i files e le subdirectories esistenti vengono mantenuti <br>
     * Tutte le aggiunte sono ricorsive nelle subdirectories <br>
     *
     * @param srcPath  nome parziale del path sorgente
     * @param destPath nome parziale del path destinazione
     * @param dirName  nome della directory da copiare
     *
     * @return true se la directory  è stata copiata
     */
    public boolean copyDirectoryAddingOnly(String srcPath, String destPath, String dirName) {
        return copyDirectoryAddingOnly(srcPath + dirName, destPath + dirName);
    }


    /**
     * Copia una directory aggiungendo files e subdirectories a quelli eventualmente esistenti <br>
     * Lascia inalterate subdirectories e files già esistenti <br>
     * <p>
     * Se manca la directory sorgente, non fa nulla <br>
     * Se manca la directory di destinazione, la crea <br>
     * Se esiste la directory destinazione, aggiunge files e subdirectories <br>
     * Tutti i files e le subdirectories esistenti vengono mantenuti <br>
     * Tutte le aggiunte sono ricorsive nelle subdirectories <br>
     *
     * @param srcPath  nome completo della directory sorgente
     * @param destPath nome completo della directory destinazione
     *
     * @return true se la directory  è stata copiata
     */
    public boolean copyDirectoryAddingOnly(String srcPath, String destPath) {
        boolean copiata = false;
        File srcDir = new File(srcPath);
        File destDir = new File(destPath);

        if (!isEsisteDirectory(srcPath)) {
            return false;
        }

        try {
            FileUtils.copyDirectory(srcDir, destDir);
            copiata = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return copiata;
    }


    /**
     * Scrive un file
     * Se non esiste, non fa nulla
     *
     * @param pathFileToBeWritten nome completo del file
     * @param text                contenuto del file
     */
    public AIResult scriveNewFile(String pathFileToBeWritten, String text) {
        return scriveFile(pathFileToBeWritten, text, false);
    }


    /**
     * Scrive un file
     * Se non esiste, lo crea
     *
     * @param pathFileToBeWritten nome completo del file
     * @param testo               contenuto del file
     * @param sovrascrive         anche se esiste già
     */
    public AIResult scriveFile(String pathFileToBeWritten, String testo, boolean sovrascrive) {
        return scriveFile(pathFileToBeWritten, testo, sovrascrive, FlowCost.VUOTA);
    }

    /**
     * Scrive un file
     * Se non esiste, lo crea
     *
     * @param pathFileToBeWritten nome completo del file
     * @param testo               contenuto del file
     * @param sovrascrive         anche se esiste già
     * @param directory           da cui iniziare il path per il messaggio di avviso
     */
    public AIResult scriveFile(String pathFileToBeWritten, String testo, boolean sovrascrive, String directory) {
        return scriveFile(pathFileToBeWritten, testo, sovrascrive, directory, false);
    }

    /**
     * Scrive un file
     * Se non esiste, lo crea
     *
     * @param pathFileToBeWritten nome completo del file
     * @param testo               contenuto del file
     * @param sovrascrive         anche se esiste già
     * @param directory           da cui iniziare il path per il messaggio di avviso
     * @param stampaInfo          flag per usare il logger
     */
    public AIResult scriveFile(String pathFileToBeWritten, String testo, boolean sovrascrive, String directory, boolean stampaInfo) {
        AIResult result = AResult.errato();
        String message = FlowCost.VUOTA;
        File fileToBeWritten;
        FileWriter fileWriter;
        String path = this.findPathBreve(pathFileToBeWritten, directory);

        if (isEsisteFile(pathFileToBeWritten)) {
            if (sovrascrive) {
                sovraScriveFile(pathFileToBeWritten, testo);
                message = "Il file: " + path + " esisteva già ed è stato aggiornato";
                if (stampaInfo) {
                    logger.info(message, this.getClass(), "scriveFile");
                }
                result = AResult.valido(message);
            }
            else {
                message = "Il file: " + path + " esisteva già e non è stato modificato";
                if (stampaInfo) {
                    logger.info(message, this.getClass(), "scriveFile");
                }
                result = AResult.errato(message);
            }
        }
        else {
            message = creaFileStr(pathFileToBeWritten);
            if (text.isEmpty(message)) {
                sovraScriveFile(pathFileToBeWritten, testo);
                message = "Il file: " + path + " non esisteva ed è stato creato";
                result = AResult.valido(message);
            }
            else {
                //                logger.warn("Il file: " + path + " non è stato scritto perché " + message, this.getClass(), "scriveFile");
                //                result = AResult.errato(message);
            }
        }

        return result;
    }


    /**
     * Sovrascrive un file
     *
     * @param pathFileToBeWritten nome completo del file
     * @param text                contenuto del file
     */
    public boolean sovraScriveFile(String pathFileToBeWritten, String text) {
        boolean status = false;
        File fileToBeWritten;
        FileWriter fileWriter = null;

        if (isEsisteFile(pathFileToBeWritten)) {
            fileToBeWritten = new File(pathFileToBeWritten);
            try {
                fileWriter = new FileWriter(fileToBeWritten);
                fileWriter.write(text);
                fileWriter.flush();
                status = true;
            } catch (Exception unErrore) {
            } finally {
                try {
                    if (fileWriter != null) {
                        fileWriter.close();
                    }
                } catch (Exception unErrore) {
                }
            }
        }
        else {
            System.out.println("Il file " + pathFileToBeWritten + " non esiste e non è stato creato");
        }

        return status;
    }


    /**
     * Legge un file
     *
     * @param pathFileToBeRead nome completo del file
     */
    public String leggeFile(String pathFileToBeRead) {
        String testo = FlowCost.VUOTA;
        String aCapo = FlowCost.A_CAPO;
        String currentLine;

        //-- non va, perché se arriva it/algos/Alfa.java becca anche il .java
        //        nameFileToBeRead=  nameFileToBeRead.replaceAll("\\.","/");

        try (BufferedReader br = new BufferedReader(new FileReader(pathFileToBeRead))) {
            while ((currentLine = br.readLine()) != null) {
                testo += currentLine;
                testo += "\n";
            }

            testo = text.levaCoda(testo, aCapo);
        } catch (Exception unErrore) {
            logger.error(unErrore, this.getClass(), "leggeFile");
        }

        return testo;
    }


    /**
     * Legge un file CSV <br>
     * Prima lista (prima riga): titoli
     * Liste successive (righe successive): valori
     *
     * @param pathFileToBeRead nome completo del file
     *
     * @return lista di liste di valori, senza titoli
     */
    public List<List<String>> leggeListaCSV(String pathFileToBeRead) {
        return leggeListaCSV(pathFileToBeRead, FlowCost.VIRGOLA, FlowCost.A_CAPO);
    }


    /**
     * Legge un file CSV <br>
     * Prima lista (prima riga): titoli
     * Liste successive (righe successive): valori
     *
     * @param pathFileToBeRead nome completo del file
     * @param sepColonna       normalmente una virgola
     * @param sepRiga          normalmente un \n
     *
     * @return lista di liste di valori, senza titoli
     */
    public List<List<String>> leggeListaCSV(String pathFileToBeRead, String sepColonna, String sepRiga) {
        List<List<String>> lista = new ArrayList<>();
        List<String> riga = null;
        String[] righe;
        String[] colonne;

        String testo = leggeFile(pathFileToBeRead);

        if (text.isValid(testo)) {
            righe = testo.split(sepRiga);
            if (righe != null && righe.length > 0) {
                for (String rigaTxt : righe) {
                    riga = null;
                    colonne = rigaTxt.split(sepColonna);
                    if (colonne != null && colonne.length > 0) {
                        riga = new ArrayList<>();
                        for (String colonna : colonne) {
                            riga.add(colonna);
                        }
                    }
                    if (riga != null) {
                        lista.add(riga);
                    }
                }
            }
        }

        return array.isAllValid(lista) ? lista.subList(1, lista.size()) : lista;
    }


    /**
     * Legge un file CSV <br>
     * Prima lista (prima riga): titoli
     * Liste successive (righe successive): valori
     *
     * @param pathFileToBeRead nome completo del file
     *
     * @return lista di mappe di valori
     */
    public List<LinkedHashMap<String, String>> leggeMappaCSV(String pathFileToBeRead) {
        return leggeMappaCSV(pathFileToBeRead, FlowCost.VIRGOLA, FlowCost.A_CAPO);
    }


    /**
     * Legge un file CSV <br>
     * Prima lista (prima riga): titoli
     * Liste successive (righe successive): valori
     *
     * @param pathFileToBeRead nome completo del file
     * @param sepColonna       normalmente una virgola
     * @param sepRiga          normalmente un \n
     *
     * @return lista di mappe di valori
     */
    public List<LinkedHashMap<String, String>> leggeMappaCSV(String pathFileToBeRead, String sepColonna, String sepRiga) {
        List<LinkedHashMap<String, String>> lista = new ArrayList<>();
        LinkedHashMap<String, String> mappa = null;
        String[] righe;
        String[] titoli;
        String[] colonne;

        String testo = leggeFile(pathFileToBeRead);
        if (text.isValid(testo)) {
            righe = testo.split(sepRiga);
            titoli = righe[0].split(sepColonna);

            if (righe != null && righe.length > 0) {
                for (int k = 1; k < righe.length; k++) {
                    mappa = null;
                    colonne = righe[k].split(sepColonna);
                    if (colonne != null && colonne.length > 0) {
                        mappa = new LinkedHashMap<>();
                        for (int j = 0; j < colonne.length; j++) {
                            if (j < colonne.length) {
                                mappa.put(titoli[j], colonne[j]);
                            }
                        }
                    }
                    if (mappa != null) {
                        lista.add(mappa);
                    }
                }
            }
        }

        return lista;
    }


    /**
     * Estrae le sub-directories da una directory <br>
     *
     * @param pathDirectoryToBeScanned nome completo della directory
     */
    public List<String> getSubDirectoriesAbsolutePathName(String pathDirectoryToBeScanned) {
        List<String> subDirectoryName = new ArrayList<>();
        List<File> subDirectory = getSubDirectories(pathDirectoryToBeScanned);

        if (subDirectory != null) {
            for (File file : subDirectory) {
                subDirectoryName.add(file.getAbsolutePath());
            }
        }

        return subDirectoryName;
    }


    /**
     * Estrae le sub-directories da una directory <br>
     *
     * @param pathDirectoryToBeScanned nome completo della directory
     */
    public List<String> getSubDirectoriesName(String pathDirectoryToBeScanned) {
        List<String> subDirectoryName = new ArrayList<>();
        List<File> subDirectory = getSubDirectories(pathDirectoryToBeScanned);

        if (subDirectory != null) {
            for (File file : subDirectory) {
                subDirectoryName.add(file.getName());
            }
        }

        return subDirectoryName;
    }


    /**
     * Estrae le sub-directories da una directory <br>
     *
     * @param pathDirectoryToBeScanned nome completo della directory
     */
    public List<String> getSubDirectoriesName(File fileSorgente) {
        List<String> subDirectoryName = new ArrayList<>();
        List<File> subDirectory = getSubDirectories(fileSorgente);

        if (subDirectory != null) {
            for (File file : subDirectory) {
                subDirectoryName.add(file.getName());
            }
        }

        return subDirectoryName;
    }


    /**
     * Estrae le sub-directories da una directory <br>
     *
     * @param pathDirectoryToBeScanned nome completo della directory
     */
    public List<File> getSubDirectories(String pathDirectoryToBeScanned) {
        return getSubDirectories(new File(pathDirectoryToBeScanned));
    }


    /**
     * Estrae le sub-directories da una directory <br>
     *
     * @param directoryToBeScanned della directory
     *
     * @return lista di sub-directory SENZA files
     */
    public List<File> getSubDirectories(File directoryToBeScanned) {
        List<File> subDirectory = new ArrayList<>();
        File[] allFiles = null;

        if (directoryToBeScanned != null) {
            allFiles = directoryToBeScanned.listFiles();
        }

        if (allFiles != null) {
            subDirectory = new ArrayList<>();
            for (File file : allFiles) {
                if (file.isDirectory()) {
                    subDirectory.add(file);
                }
            }
        }

        return subDirectory;
    }


    /**
     * Estrae le sub-directories da un sotto-livello di una directory <br>
     * La dirInterna non è, ovviamente, al primo livello della directory altrimenti chiamerei getSubDirectories <br>
     *
     * @param pathDirectoryToBeScanned della directory
     * @param dirInterna               da scandagliare
     *
     * @return lista di sub-directory SENZA files
     */
    public List<File> getSubSubDirectories(String pathDirectoryToBeScanned, String dirInterna) {
        return getSubSubDirectories(new File(pathDirectoryToBeScanned), dirInterna);
    }


    /**
     * Estrae le sub-directories da un sotto-livello di una directory <br>
     *
     * @param directoryToBeScanned della directory
     * @param dirInterna           da scandagliare
     *
     * @return lista di sub-directory SENZA files
     */
    public List<File> getSubSubDirectories(File directoryToBeScanned, String dirInterna) {
        String subDir = directoryToBeScanned.getAbsolutePath();

        if (subDir.endsWith(FlowCost.SLASH)) {
            subDir = text.levaCoda(subDir, FlowCost.SLASH);
        }

        if (dirInterna.startsWith(FlowCost.SLASH)) {
            dirInterna = text.levaTesta(dirInterna, FlowCost.SLASH);
        }

        String newPath = subDir + FlowCost.SLASH + dirInterna;
        File subFile = new File(newPath);

        return getSubDirectories(subFile);
    }


    /**
     * Controlla se una sotto-directory esiste <br>
     *
     * @param directoryToBeScanned della directory
     * @param dirInterna           da scandagliare
     *
     * @return true se esiste
     */
    public boolean isEsisteSubDirectory(File directoryToBeScanned, String dirInterna) {
        return isEsisteDirectory(directoryToBeScanned.getAbsolutePath() + FlowCost.SLASH + dirInterna);
    }


    /**
     * Controlla se una sotto-directory è piena <br>
     *
     * @param directoryToBeScanned della directory
     * @param dirInterna           da scandagliare
     *
     * @return true se è piena
     */
    public boolean isPienaSubDirectory(File directoryToBeScanned, String dirInterna) {
        return array.isAllValid(getSubSubDirectories(directoryToBeScanned, dirInterna));
    }


    /**
     * Controlla se una sotto-directory è vuota <br>
     *
     * @param directoryToBeScanned della directory
     * @param dirInterna           da scandagliare
     *
     * @return true se è vuota
     */
    public boolean isVuotaSubDirectory(File directoryToBeScanned, String dirInterna) {
        return array.isEmpty(getSubSubDirectories(directoryToBeScanned, dirInterna));
    }


    /**
     * Elimina l'ultima directory da un path <br>
     * <p>
     * Esegue solo se il path è valido <br>
     * Elimina spazi vuoti iniziali e finali <br>
     *
     * @param pathIn in ingresso
     *
     * @return path ridotto in uscita
     */
    public String levaDirectoryFinale(final String pathIn) {
        String pathOut = pathIn.trim();

        if (text.isValid(pathOut) && pathOut.endsWith(FlowCost.SLASH)) {
            pathOut = text.levaCoda(pathOut, FlowCost.SLASH);
            pathOut = text.levaCodaDa(pathOut, FlowCost.SLASH) + FlowCost.SLASH;
        }

        return pathOut.trim();
    }

    /**
     * Recupera l'ultima directory da un path <br>
     * <p>
     * Esegue solo se il path è valido <br>
     * Elimina spazi vuoti iniziali e finali <br>
     *
     * @param pathIn in ingresso
     *
     * @return directory finale del path
     */
    public String estraeDirectoryFinaleSenzaSlash(final String pathIn) {
        String pathOut = pathIn.trim();

        if (text.isValid(pathOut) && pathOut.endsWith(FlowCost.SLASH)) {
            pathOut = text.levaCoda(pathOut, FlowCost.SLASH);
            pathOut = pathOut.substring(pathOut.lastIndexOf(FlowCost.SLASH) + 1);
        }

        return pathOut.trim();
    }

    /**
     * Recupera l'ultima directory da un path <br>
     * <p>
     * Esegue solo se il path è valido <br>
     * Elimina spazi vuoti iniziali e finali <br>
     *
     * @param pathIn in ingresso
     *
     * @return directory finale del path, comprensiva di SLASH
     */
    public String estraeDirectoryFinale(final String pathIn) {
        String pathOut = pathIn.trim();

        if (text.isValid(pathOut) && pathOut.endsWith(FlowCost.SLASH)) {
            pathOut = text.levaCoda(pathOut, FlowCost.SLASH);
            pathOut = pathOut.substring(pathOut.lastIndexOf(FlowCost.SLASH) + 1) + FlowCost.SLASH;
        }

        return pathOut.trim();
    }

    /**
     * Recupera l'ultima classe da un path <br>
     * <p>
     * Elimina spazi vuoti iniziali e finali <br>
     *
     * @param pathIn in ingresso
     *
     * @return classe finale del path
     */
    public String estraeClasseFinale(final String pathIn) {
        String pathOut = pathIn.trim();

        if (text.isValid(pathOut)) {
            pathOut = text.levaCoda(pathOut, FlowCost.SLASH);
            if (pathOut.contains(FlowCost.SLASH)) {
                pathOut = pathOut.substring(pathOut.lastIndexOf(FlowCost.SLASH) + FlowCost.SLASH.length());
            }
            if (pathOut.contains(FlowCost.PUNTO)) {
                pathOut = pathOut.substring(pathOut.lastIndexOf(FlowCost.PUNTO) + FlowCost.PUNTO.length());
            }
        }

        return pathOut.trim();
    }

    /**
     * Recupera l'ultima classe da un path <br>
     * <p>
     * Elimina spazi vuoti iniziali e finali <br>
     *
     * @param pathIn in ingresso
     *
     * @return classe finale del path
     */
    public String estraeClasseFinaleSenzaJava(final String pathIn) {
        String pathOut = text.levaCoda(pathIn, JAVA_SUFFIX);
        return estraeClasseFinale(pathOut);
    }

    /**
     * Estrae il path che contiene la directory indicata <br>
     * <p>
     * Esegue solo se il path è valido
     * Se la directory indicata è vuota, restituisce il path completo <br>
     * Se la directory indicata non esiste nel path, restituisce una stringa vuota <br>
     * Elimina spazi vuoti iniziali e finali
     *
     * @param pathIn            in ingresso
     * @param directoryFindPath di cui ricercare il path che la contiene
     *
     * @return path completo fino alla directory selezionata
     */
    public String findPathDirectory(String pathIn, String directoryFindPath) {
        String pathOut = pathIn.trim();

        if (text.isEmpty(pathIn)) {
            logger.warn("Nullo il path in ingresso", this.getClass(), "findPathDirectory");
            return pathOut;
        }

        if (text.isEmpty(directoryFindPath)) {
            logger.info("Nulla la directory in ingresso", this.getClass(), "findPathDirectory");
            return pathOut;
        }

        if (pathOut.contains(directoryFindPath)) {
            pathOut = pathOut.substring(0, pathOut.indexOf(directoryFindPath));
        }
        else {
            pathOut = FlowCost.VUOTA;
            logger.warn("Non esiste la directory indicata nel path indicato", this.getClass(), "findPathDirectory");
        }

        return pathOut.trim();
    }


    /**
     * Estrae il path parziale da una directory indicata, escludendo il percorso iniziale <br>
     * <p>
     * La directory indicata è la prima con quel nome <br>
     * Esegue solo se il path è valido
     * Se la directory indicata non esiste nel path, restituisce tutto il path completo <br>
     * Elimina spazi vuoti iniziali e finali
     *
     * @param pathIn    in ingresso
     * @param directory da cui iniziare il path
     *
     * @return path parziale da una directory
     */
    public String findPathBreve(String pathIn, String directory) {
        String pathBreve = pathIn;
        String pathCanonical = FlowCost.VUOTA;
        String prefix = "../";

        if (text.isEmpty(directory)) {
            return pathIn;
        }

        pathCanonical = findPathCanonical(pathIn, directory);
        if (text.isValid(pathCanonical)) {
            pathBreve = prefix + pathCanonical;
            pathBreve = text.levaCoda(pathBreve, FlowCost.SLASH);
            pathBreve = text.levaCoda(pathBreve, JAVA_SUFFIX);
        }

        return pathBreve;
    }

    /**
     * Estrae il path parziale da una directory indicata, escludendo il percorso iniziale <br>
     * <p>
     * La directory indicata è l'ultima con quel nome <br>
     * Esegue solo se il path è valido
     * Se la directory indicata non esiste nel path, restituisce tutto il path completo <br>
     * Elimina spazi vuoti iniziali e finali
     *
     * @param pathIn    in ingresso
     * @param directory da cui iniziare il path
     *
     * @return path parziale da una directory
     */
    public String findPathBreveDa(String pathIn, String directory) {
        String pathBreve = pathIn;
        String pathCanonical = FlowCost.VUOTA;
        String prefix = "../";

        if (text.isEmpty(directory)) {
            return pathIn;
        }

        pathCanonical = findPathCanonicalDa(pathIn, directory);
        if (text.isValid(pathCanonical)) {
            pathBreve = prefix + pathCanonical;
            pathBreve = text.levaCoda(pathBreve, FlowCost.SLASH);
            pathBreve = text.levaCoda(pathBreve, JAVA_SUFFIX);
        }

        return pathBreve;
    }

    /**
     * Estrae il path canonico da una directory indicata <br>
     * <p>
     * La directory indicata è l'ultima con quel nome <br>
     * Esegue solo se il path è valido
     * Se la directory indicata non esiste nel path, restituisce tutto il path completo <br>
     * Elimina spazi vuoti iniziali e finali
     *
     * @param pathIn    in ingresso
     * @param directory da cui iniziare il path
     *
     * @return path parziale da una directory
     */
    public String findPathCanonical(String pathIn, String directory) {
        String path = FlowCost.VUOTA;

        if (text.isEmpty(pathIn) || text.isEmpty(directory)) {
            return pathIn;
        }

        if (pathIn.contains(directory)) {
            path = pathIn.substring(pathIn.lastIndexOf(directory));
            if (path.startsWith(FlowCost.SLASH)) {
                path = path.substring(1);
            }
        }

        return path;
    }


    /**
     * Estrae il path canonico da una directory indicata <br>
     * <p>
     * La directory indicata è l'ultima con quel nome <br>
     * Esegue solo se il path è valido
     * Se la directory indicata non esiste nel path, restituisce tutto il path completo <br>
     * Elimina spazi vuoti iniziali e finali
     *
     * @param pathIn    in ingresso
     * @param directory da cui iniziare il path
     *
     * @return path parziale da una directory
     */
    public String findPathCanonicalDa(String pathIn, String directory) {
        String path = FlowCost.VUOTA;

        if (text.isEmpty(pathIn) || text.isEmpty(directory)) {
            return pathIn;
        }

        if (pathIn.contains(directory)) {
            path = pathIn.substring(pathIn.lastIndexOf(directory) + directory.length());
            if (path.startsWith(FlowCost.SLASH)) {
                path = path.substring(1);
            }
        }

        return path;
    }

    //    /**
    //     * Elabora un path eliminando i livelli iniziali indicati. <br>
    //     * <p>
    //     * Esegue solo se il testo è valido <br>
    //     * Elimina spazi vuoti iniziali e finali <br>
    //     * Aggiunge un prefisso indicativo -> '../' <br>
    //     *
    //     * @param testoIn    ingresso
    //     * @param numLivelli iniziali da eliminare nel path
    //     *
    //     * @return path semplificato
    //     */
    //    @Deprecated
    //    public String pathBreve(final String testoIn, int numLivelli) {
    //        String path = text.testoDopoTagRipetuto(testoIn, SLASH, numLivelli);
    //        String prefix = "../";
    //
    //        if (!path.equals(testoIn)) {
    //            path = text.isValid(path) ? prefix + path : path;
    //        }
    //
    //        return path;
    //    }


    /**
     * Recupera i progetti da una directory <br>
     * Controlla che la sotto-directory sia di un project e quindi contenga la cartella 'src' e questa non sia vuota <br>
     *
     * @param pathDirectoryToBeScanned nome completo della directory
     */
    public List<File> getAllProjects(String pathDirectoryToBeScanned) {
        List<File> listaProjects = null;
        List<File> listaDirectory = getSubDirectories(new File(pathDirectoryToBeScanned));

        if (listaDirectory != null) {
            listaProjects = new ArrayList<>();

            for (File file : listaDirectory) {
                if (isEsisteSubDirectory(file, DIR_PROGETTO)) {
                    listaProjects.add(file);
                }
            }
        }

        return listaProjects;
    }


    /**
     * Recupera i progetti vuoti da una directory <br>
     * Controlla che la sotto-directory sia di un project e quindi contenga la cartella 'src.main.java' <br>
     * Controlla che il progetto sia vuoto; deve essere vuota la cartella 'src.main.java' <br>
     *
     * @param pathDirectoryToBeScanned nome completo della directory
     */
    public List<File> getEmptyProjects(String pathDirectoryToBeScanned) {
        List<File> listaEmptyProjects = null;
        List<File> listaProjects = getAllProjects(pathDirectoryToBeScanned);

        if (listaProjects != null) {
            listaEmptyProjects = new ArrayList<>();
            for (File file : listaProjects) {
                if (isVuotaSubDirectory(file, DIR_PROGETTO_VUOTO)) {
                    listaEmptyProjects.add(file);
                }
            }
        }

        return listaEmptyProjects;
    }

    /**
     * Crea una lista di tutte le Entity esistenti nel modulo indicato <br>
     */
    public List<String> getModuleSubFilesEntity(String moduleName) {
        String tagIniziale = "src/main/java/it/algos/";
        String tagFinale = "/backend/packages";

        return getAllSubFilesEntity(tagIniziale + moduleName + tagFinale);
    }

    /**
     * Crea una lista di tutte le Entity esistenti nella directory packages <br>
     */
    public List<String> getAllSubFilesEntity(String path) {
        List<String> listaCanonicalNamesOnlyEntity = new ArrayList<>();
        List<String> listaNamesOnlyFilesJava = getAllSubFilesJava(path);
        String simpleName;

        if (array.isAllValid(listaNamesOnlyFilesJava)) {
            for (String canonicalName : listaNamesOnlyFilesJava) {
                //--estrae la parte significativa
                simpleName = canonicalName.substring(canonicalName.lastIndexOf(FlowCost.PUNTO) + FlowCost.PUNTO.length());

                //--scarta Enumeration
                if (simpleName.startsWith("AE")) {
                    continue;
                }

                //--scarta 'logic', 'form', 'service', 'view', 'grid'
                if (simpleName.endsWith("Logic") || simpleName.endsWith("Form") || simpleName.endsWith("Service") || simpleName.endsWith("View") || simpleName.endsWith("Grid")) {
                    continue;
                }

                listaCanonicalNamesOnlyEntity.add(canonicalName);
            }
        }

        return listaCanonicalNamesOnlyEntity;
    }

    /**
     * Crea una lista di soli files java ricorsiva nelle sub-directory <br>
     *
     * @return canonicalName con i PUNTI di separazione e NON lo SLASH
     */
    public List<String> getAllSubFilesJava(String path) {
        List<String> listaCanonicalNamesOnlyFilesJava = new ArrayList<>();
        List<String> listaPathNamesOnlyFiles = getAllSubPathFiles(path);
        String tag = ".it.";
        String canonicalName;

        if (array.isAllValid(listaPathNamesOnlyFiles)) {
            for (String pathName : listaPathNamesOnlyFiles) {
                if (pathName.endsWith(JAVA_SUFFIX)) {
                    canonicalName = text.levaCoda(pathName, JAVA_SUFFIX);
                    canonicalName = canonicalName.replaceAll(FlowCost.SLASH, FlowCost.PUNTO);
                    canonicalName = findPathCanonical(canonicalName, tag);
                    canonicalName = canonicalName.substring(1);
                    listaCanonicalNamesOnlyFilesJava.add(canonicalName);
                }
            }
        }

        return listaCanonicalNamesOnlyFilesJava;
    }

    /**
     * Crea una lista di soli files ricorsiva nelle sub-directory <br>
     *
     * @return path name completo
     */
    public List<String> getAllSubPathFiles(String path) {
        List<String> listaPathNamesOnlyFiles = new ArrayList<>();
        List<String> listaAllPathNames = null;
        File unaDirectory = new File(path);
        Path start = Paths.get(unaDirectory.getAbsolutePath());

        try {
            listaAllPathNames = recursionSubPathNames(start);
        } catch (Exception unErrore) {
            logger.error(unErrore, this.getClass(), "getAllEntityClass");
        }

        if (array.isAllValid(listaAllPathNames)) {
            for (String pathName : listaAllPathNames) {
                if (isEsisteFile(pathName)) {
                    listaPathNamesOnlyFiles.add(pathName);
                }
            }
        }

        return listaPathNamesOnlyFiles;
    }

    /**
     * Crea una lista di files/directory ricorsiva nelle sub-directory <br>
     *
     * @return path name completo
     */
    public List<String> recursionSubPathNames(Path start) throws IOException {
        List<String> collect;

        try (Stream<Path> stream = Files.walk(start, Integer.MAX_VALUE)) {
            collect = stream
                    .map(String::valueOf)
                    .sorted()
                    .collect(Collectors.toList());
        }

        return collect;
    }

    /**
     * Crea una lista di tutte le Entity esistenti nella directory packages <br>
     * Considera sia il modulo vaadflow14 sia il progetto corrente <br>
     */
    public List<Class> getAllEntityClass() {
        List<Class> lista = new ArrayList<>();

        return lista;
    }

    /**
     * Sposta un file da una directory ad un'altra <br>
     * Esegue solo se il path sorgente esiste <br>
     * Esegue solo se il path destinazione NON esiste <br>
     * Viene cancellato il file sorgente <br>
     *
     * @param pathFileToBeRead  posizione iniziale del file da spostare
     * @param pathFileToBeWrite posizione iniziale del file da spostare
     *
     * @return testo di errore, vuoto se il file è stato spostato
     */
    public boolean spostaFile(String pathFileToBeRead, String pathFileToBeWrite) {
        return spostaFileStr(pathFileToBeRead, pathFileToBeWrite) == FlowCost.VUOTA;
    }


    /**
     * Sposta un file da una directoy ad un'altra <br>
     * Esegue solo se il path sorgente esiste <br>
     * Esegue solo se il path destinazione NON esiste <br>
     * Viene cancellato il file sorgente <br>
     *
     * @param pathFileToBeRead  posizione iniziale del file da spostare
     * @param pathFileToBeWrite posizione iniziale del file da spostare
     *
     * @return testo di errore, vuoto se il file è stato spostato
     */
    public String spostaFileStr(String pathFileToBeRead, String pathFileToBeWrite) {
        String status = FlowCost.VUOTA;

        if (text.isValid(pathFileToBeRead) && text.isValid(pathFileToBeWrite)) {
            status = copyFileStr(pathFileToBeRead, pathFileToBeWrite);
        }
        else {
            return PATH_NULLO;
        }

        if (status.equals(FlowCost.VUOTA)) {
            status = deleteFileStr(pathFileToBeRead);
        }

        return status;
    }


    /**
     * Controlla se il primo carattere della stringa passata come parametro è uno 'slash' <br>
     *
     * @param testoIngresso          da elaborare
     * @param primoCarattereExpected da controllare
     *
     * @return true se il primo carattere NON è uno quello previsto
     */
    public boolean isNotCarattere(String testoIngresso, String primoCarattereExpected) {
        boolean status = true;
        String primoCarattereEffettivo;

        if (text.isValid(testoIngresso)) {
            primoCarattereEffettivo = testoIngresso.substring(0, 1);
            if (primoCarattereEffettivo.equals(primoCarattereExpected)) {
                status = false;
            }
        }

        return status;
    }


    /**
     * Controlla se il primo carattere della stringa passata come parametro è uno 'slash' <br>
     *
     * @param testoIngresso da elaborare
     *
     * @return true se NON è uno 'slash'
     */
    public boolean isNotSlash(String testoIngresso) {
        return isNotCarattere(testoIngresso, FlowCost.SLASH);
    }


    /**
     * Controlla la stringa passata come parametro termina con un 'suffix' (3 caratteri terminali dopo un punto) <br>
     *
     * @param testoIngresso da elaborare
     *
     * @return true se MANCA il 'suffix'
     */
    public boolean isNotSuffix(String testoIngresso) {
        boolean status = true;
        String quartultimoCarattere;
        int gap = 4;
        int max;
        String tagPatchProperties = ".properties";
        String tagPatchGitIgnore = ".gitignore";
        String tagPatchJava = ".java";

        if (text.isValid(testoIngresso)) {
            max = testoIngresso.length();
            quartultimoCarattere = testoIngresso.substring(max - gap, max - gap + 1);
            if (quartultimoCarattere.equals(FlowCost.PUNTO)) {
                status = false;
            }
        }

        if (testoIngresso.endsWith(tagPatchProperties)) {
            status = false;
        }

        if (testoIngresso.endsWith(tagPatchGitIgnore)) {
            status = false;
        }

        if (testoIngresso.endsWith(tagPatchJava)) {
            status = false;
        }

        return status;
    }

    /**
     * Recupera una lista di files (sub-directory escluse) dalla directory <br>
     *
     * @param pathDirectory da spazzolare
     *
     * @return lista di files
     */
    public List<File> getFiles(String pathDirectory) {
        List<File> lista = new ArrayList();
        File unaDirectory = new File(pathDirectory);
        File[] array = unaDirectory.listFiles();

        for (File unFile : array) {
            if (unFile.isFile()) {
                lista.add(unFile);
            }
        }

        return lista;
    }


    /**
     * Recupera una lista di nomi di files (sub-directory escluse) dalla directory <br>
     * Elimina il suffisso '.java' finale <br>
     *
     * @param pathDirectory da spazzolare
     *
     * @return lista di files
     */
    public List<String> getFilesNames(String pathDirectory) {
        List<String> lista = new ArrayList();
        List<File> listaFiles = getFiles(pathDirectory);
        String nome;

        for (File unFile : listaFiles) {
            if (unFile.isFile()) {
                nome = unFile.getName();
                nome = text.levaCoda(nome, JAVA_SUFFIX);
                lista.add(nome);
            }
        }

        return lista;
    }

}