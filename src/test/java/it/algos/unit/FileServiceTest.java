package it.algos.unit;

import it.algos.test.*;
import it.algos.vaadflow14.backend.application.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.exceptions.*;
import it.algos.vaadflow14.backend.service.*;
import static it.algos.vaadflow14.backend.service.FileService.*;
import org.apache.tomcat.util.http.fileupload.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: dom, 28-giu-2020
 * Time: 15:11
 * <p>
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("testAllValido")
@DisplayName("FileService - Gestione dei files.")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FileServiceTest extends ATest {

    static boolean FLAG_CREAZIONE_INIZIALE = true;

    static boolean FLAG_CANCELLAZIONE_FINALE = true;

    private static String DIRECTORY_IDEA = "/Users/gac/Documents/IdeaProjects/";

    private static String PATH_DIRECTORY_TEST = "/Users/gac/Desktop/test/";

    private static String PATH_DIRECTORY_UNO = PATH_DIRECTORY_TEST + "Pippo/";

    private static String PATH_DIRECTORY_DUE = PATH_DIRECTORY_TEST + "Possibile/";

    private static String PATH_DIRECTORY_TRE = PATH_DIRECTORY_TEST + "Mantova/";

    private static String PATH_FILE_TRE = PATH_DIRECTORY_TRE + "/Topolino.txt";

    private static String PATH_FILE_QUATTRO = PATH_DIRECTORY_TRE + "/Paperino.txt";

    private static String PATH_FILE_AGGIUNTO_TRE = PATH_DIRECTORY_TRE + "FileSorgenteAggiunto.ccs";

    private static String PATH_DIRECTORY_NON_ESISTENTE = PATH_DIRECTORY_TEST + "Genova/";

    private static String PATH_DIRECTORY_DA_COPIARE = PATH_DIRECTORY_TEST + "NuovaDirectory/";

    private static String PATH_DIRECTORY_MANCANTE = PATH_DIRECTORY_TEST + "CartellaCopiata/";

    private static String PATH_FILE_AGGIUNTO = PATH_DIRECTORY_MANCANTE + "TerzaPossibilita.htm";

    private static String PATH_FILE_UNO = PATH_DIRECTORY_TEST + "Pluto.rtf";

    private static String PATH_FILE_DUE = PATH_DIRECTORY_TEST + "Secondo.rtf";

    private static String PATH_FILE_ESISTENTE_CON_MAIUSCOLA_SBAGLIATA = "/Users/gac/Desktop/test/pluto.rtf";

    private static String PATH_FILE_NO_SUFFIX = PATH_DIRECTORY_TEST + "Topolino";

    private static String PATH_FILE_NON_ESISTENTE = PATH_DIRECTORY_TEST + "Topolino.txt";

    private static String PATH_FILE_NO_PATH = "Users/gac/Desktop/test/Pluto.rtf";

    private static String PATH_DIRECTORY_NO_PATH = "Users/gac/Desktop/test/Mantova/";

    private static String PATH_FILE_NO_GOOD = "/Users/gac/Desktop/test/Pa perino/Topolino.abc";

    private static String PATH_FILE_ANOMALO = PATH_DIRECTORY_TEST + "Pluto.properties";

    private static String PATH_DIRECTORY_ESISTENTE_CON_MAIUSCOLA_SBAGLIATA = "/Users/gac/desktop/test/Pippo/";

    private static String PATH_FILE_DELETE = "/Users/gac/Desktop/test/NonEsiste/Minni.txt";

    private static String PATH_MODULO_PROVA = "vaadtest/";

    private static String VALIDO = "TROVATO";

    private static String ESISTE_FILE = " isEsisteFile() ";

    private static String ESISTE_DIRECTORY = " isEsisteDirectory() ";

    private static String CREA_FILE = " creaFile() ";

    private static String CREA_DIRECTORY = " creaDirectory() ";

    private static String DELETE_FILE = " deleteFile() ";

    private static String DELETE_DIRECTORY = " deleteDirectory() ";

    //    @InjectMocks
    //    public WizService wizService;

    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    private FileService service;

    private File unFile;

    private File unaDirectory;

    private String ottenutoDaNome;

    private String ottenutoDaFile;

    private String nomeFile;

    private String nomeCompletoFile;

    private String nomeDirectory;

    private String nomeCompletoDirectory;

    private List<File> listaDirectory;

    private List<File> listaFile;

    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpIniziale() {
        super.setUpStartUp();

        //--reindirizzo l'istanza della superclasse
        service = fileService;

        //        MockitoAnnotations.initMocks(wizService);
        //        Assertions.assertNotNull(wizService);
        //        wizService.text = textService;
        //        wizService.file = fileService;
        //
        //        for (AEWizCost cost : AEWizCost.values()) {
        //            cost.setText(textService);
        //            cost.setFile(fileService);
        //            cost.setLogger(loggerService);
        //        }
        //        for (AEDir aeDir : AEDir.values()) {
        //            aeDir.setText(textService);
        //            aeDir.setFile(fileService);
        //            aeDir.setLogger(loggerService);
        //        }

        if (FLAG_CREAZIONE_INIZIALE) {
            creazioneListe();
            creazioneDirectory();
            creazioneFiles();
        }
    }


    /**
     * Qui passa ad ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    void setUpEach() {
        super.setUp();

        nomeFile = VUOTA;
        unFile = null;
        unaDirectory = null;
        nomeCompletoDirectory = PATH_DIRECTORY_TEST;
    }


    /**
     * Qui passa al termine di ogni singolo test <br>
     */
    @AfterEach
    void tearDown() {
    }


    /**
     * Qui passa una volta sola, chiamato alla fine di tutti i tests <br>
     */
    @AfterAll
    void tearDownAll() {
        if (FLAG_CANCELLAZIONE_FINALE) {
            fine();
        }
    }


    /**
     * Creazioni di servizio per essere sicuri che ci siano tutti i files/directories richiesti <br>
     */
    private void creazioneListe() {
        listaDirectory = new ArrayList<>();
        listaDirectory.add(new File(PATH_DIRECTORY_TEST));
        listaDirectory.add(new File(PATH_DIRECTORY_UNO));
        listaDirectory.add(new File(PATH_DIRECTORY_DUE));

        listaFile = new ArrayList<>();
        listaFile.add(new File(PATH_FILE_UNO));
        listaFile.add(new File(PATH_FILE_DUE));
        listaFile.add(new File(PATH_FILE_TRE));
        listaFile.add(new File(PATH_FILE_QUATTRO));
        listaFile.add(new File(PATH_FILE_ESISTENTE_CON_MAIUSCOLA_SBAGLIATA));
    }


    /**
     * Creazioni di servizio per essere sicuri che ci siano tutti i files/directories richiesti <br>
     * Alla fine verranno cancellati tutti <br>
     */
    private void creazioneDirectory() {
        if (arrayService.isAllValid(listaDirectory)) {
            for (File directory : listaDirectory) {
                directory.mkdirs();
            }
        }
    }


    /**
     * Creazioni di servizio per essere sicuri che ci siano tutti i files/directories richiesti <br>
     * Alla fine verranno cancellati tutti <br>
     */
    private void creazioneFiles() {
        if (arrayService.isAllValid(listaFile)) {
            for (File unFile : listaFile) {
                try { // prova ad eseguire il codice
                    unFile.createNewFile();
                } catch (Exception unErrore) { // intercetta l'errore
                    if (service.creaDirectoryParentAndFile(unFile).equals(VUOTA)) {
                        listaDirectory.add(new File(unFile.getParent()));
                    }
                }
            }
        }
    }


    @Test
    @Order(1)
    @DisplayName("1 - isEsisteFileZero")
    public void isEsisteFileZero() {
        nomeFile = "nonEsiste";
        unFile = new File(nomeFile);
        assertNotNull(unFile);
        System.out.println(" ");
        System.out.println("file.getName() = " + unFile.getName());
        System.out.println("file.getPath() = " + unFile.getPath());
        System.out.println("file.getAbsolutePath() = " + unFile.getAbsolutePath());
        try {
            System.out.println("file.getCanonicalPath() = " + unFile.getCanonicalPath());
        } catch (Exception unErrore) {
            System.out.println("Errore");
        }

        nomeFile = "Maiuscola";
        unFile = new File(nomeFile);
        assertNotNull(unFile);
        System.out.println(" ");
        System.out.println("file.getName() = " + unFile.getName());
        System.out.println("file.getPath() = " + unFile.getPath());
        System.out.println("file.getAbsolutePath() = " + unFile.getAbsolutePath());
        try {
            System.out.println("file.getCanonicalPath() = " + unFile.getCanonicalPath());
        } catch (Exception unErrore) {
            System.out.println("Errore");
        }

        nomeFile = "/User/pippoz";
        unFile = new File(nomeFile);
        assertNotNull(unFile);
        System.out.println(" ");
        System.out.println("file.getName() = " + unFile.getName());
        System.out.println("file.getPath() = " + unFile.getPath());
        System.out.println("file.getAbsolutePath() = " + unFile.getAbsolutePath());

        try {
            System.out.println("file.getCanonicalPath() = " + unFile.getCanonicalPath());
        } catch (Exception unErrore) {
            System.out.println("Errore");
        }

        nomeFile = "/User/pippo/Pluto.rtf";
        unFile = new File(nomeFile);
        assertNotNull(unFile);
        System.out.println(" ");
        System.out.println("file.getName() = " + unFile.getName());
        System.out.println("file.getPath() = " + unFile.getPath());
        System.out.println("file.getAbsolutePath() = " + unFile.getAbsolutePath());

        try {
            System.out.println("file.getCanonicalPath() = " + unFile.getCanonicalPath());
        } catch (Exception unErrore) {
            System.out.println("Errore");
        }
    }


    @Test
    @Order(2)
    @DisplayName("2 - isEsisteFile")
    public void isEsisteFile() {
        nomeCompletoFile = "null";
        ottenutoDaNome = service.isEsisteFileStr((String) null);
        assertFalse(service.isEsisteFile((String) null));
        assertEquals(PATH_NULLO, ottenutoDaNome);
        System.out.println("Risposta" + ESISTE_FILE + "ottenutoDaNome: " + nomeCompletoFile + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        ottenutoDaFile = service.isEsisteFileStr((File) null);
        assertFalse(service.isEsisteFile((File) null));
        assertEquals(PARAMETRO_NULLO, ottenutoDaFile);
        System.out.println("Risposta" + ESISTE_FILE + "ottenutoDaFile: " + nomeCompletoFile + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoFile = VUOTA;
        ottenutoDaNome = service.isEsisteFileStr(nomeCompletoFile);
        assertFalse(service.isEsisteFile(nomeCompletoFile));
        assertEquals(PATH_NULLO, ottenutoDaNome);
        System.out.println("Risposta" + ESISTE_FILE + "ottenutoDaNome: " + nomeCompletoFile + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unFile = new File(nomeCompletoFile);
        assertNotNull(unFile);
        ottenutoDaFile = service.isEsisteFileStr(unFile);
        assertFalse(service.isEsisteFile(unFile));
        assertEquals(PATH_NULLO, ottenutoDaFile);
        System.out.println("Risposta" + ESISTE_FILE + "ottenutoDaFile: " + nomeCompletoFile + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoFile = "nonEsiste";
        ottenutoDaNome = service.isEsisteFileStr(nomeCompletoFile);
        assertFalse(service.isEsisteFile(nomeCompletoFile));
        assertEquals(PATH_NOT_ABSOLUTE, ottenutoDaNome);
        System.out.println("Risposta" + ESISTE_FILE + "ottenutoDaNome: " + nomeCompletoFile + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unFile = new File(nomeCompletoFile);
        assertNotNull(unFile);
        ottenutoDaFile = service.isEsisteFileStr(unFile);
        assertFalse(service.isEsisteFile(unFile));
        assertEquals(PATH_NOT_ABSOLUTE, ottenutoDaFile);
        System.out.println("Risposta" + ESISTE_FILE + "ottenutoDaFile: " + nomeCompletoFile + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoFile = PATH_FILE_NO_SUFFIX;
        ottenutoDaNome = service.isEsisteFileStr(nomeCompletoFile);
        assertFalse(service.isEsisteFile(nomeCompletoFile));
        assertEquals(PATH_SENZA_SUFFIX, ottenutoDaNome);
        System.out.println("Risposta" + ESISTE_FILE + "ottenutoDaNome: " + nomeCompletoFile + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unFile = new File(nomeCompletoFile);
        assertNotNull(unFile);
        ottenutoDaFile = service.isEsisteFileStr(unFile);
        assertFalse(service.isEsisteFile(unFile));
        assertEquals(PATH_SENZA_SUFFIX, ottenutoDaFile);
        System.out.println("Risposta" + ESISTE_FILE + "ottenutoDaFile: " + nomeCompletoFile + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoFile = PATH_FILE_NON_ESISTENTE;
        ottenutoDaNome = service.isEsisteFileStr(nomeCompletoFile);
        assertFalse(service.isEsisteFile(nomeCompletoFile));
        assertEquals(NON_ESISTE_FILE, ottenutoDaNome);
        System.out.println("Risposta" + ESISTE_FILE + "ottenutoDaNome: " + nomeCompletoFile + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unFile = new File(nomeCompletoFile);
        assertNotNull(unFile);
        ottenutoDaFile = service.isEsisteFileStr(unFile);
        assertFalse(service.isEsisteFile(unFile));
        assertEquals(NON_ESISTE_FILE, ottenutoDaFile);
        System.out.println("Risposta" + ESISTE_FILE + "ottenutoDaFile: " + nomeCompletoFile + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoFile = PATH_FILE_NO_PATH;
        ottenutoDaNome = service.isEsisteFileStr(nomeCompletoFile);
        assertFalse(service.isEsisteFile(nomeCompletoFile));
        assertEquals(PATH_NOT_ABSOLUTE, ottenutoDaNome);
        System.out.println("Risposta" + ESISTE_FILE + "ottenutoDaNome: " + nomeCompletoFile + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unFile = new File(nomeCompletoFile);
        assertNotNull(unFile);
        ottenutoDaFile = service.isEsisteFileStr(unFile);
        assertFalse(service.isEsisteFile(unFile));
        assertEquals(PATH_NOT_ABSOLUTE, ottenutoDaFile);
        System.out.println("Risposta" + ESISTE_FILE + "ottenutoDaFile: " + nomeCompletoFile + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoFile = PATH_DIRECTORY_UNO;
        ottenutoDaNome = service.isEsisteFileStr(nomeCompletoFile);
        assertFalse(service.isEsisteFile(nomeCompletoFile));
        assertEquals(NON_E_FILE, ottenutoDaNome);
        System.out.println("Risposta" + ESISTE_FILE + "ottenutoDaNome: " + nomeCompletoFile + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unFile = new File(nomeCompletoFile);
        assertNotNull(unFile);
        ottenutoDaFile = service.isEsisteFileStr(unFile);
        assertFalse(service.isEsisteFile(unFile));
        assertEquals(NON_E_FILE, ottenutoDaFile);
        System.out.println("Risposta" + ESISTE_FILE + "ottenutoDaFile: " + nomeCompletoFile + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoFile = PATH_FILE_UNO;
        ottenutoDaNome = service.isEsisteFileStr(nomeCompletoFile);
        assertTrue(service.isEsisteFile(nomeCompletoFile));
        assertEquals(VUOTA, ottenutoDaNome);
        System.out.println("Risposta" + ESISTE_FILE + "ottenutoDaNome: " + nomeCompletoFile + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unFile = new File(nomeCompletoFile);
        assertNotNull(unFile);
        ottenutoDaFile = service.isEsisteFileStr(unFile);
        assertTrue(service.isEsisteFile(unFile));
        assertEquals(VUOTA, ottenutoDaFile);
        System.out.println("Risposta" + ESISTE_FILE + "ottenutoDaFile: " + nomeCompletoFile + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoFile = PATH_FILE_ESISTENTE_CON_MAIUSCOLA_SBAGLIATA;
        ottenutoDaNome = service.isEsisteFileStr(nomeCompletoFile);
        assertTrue(service.isEsisteFile(nomeCompletoFile));
        assertEquals(VUOTA, ottenutoDaNome);
        System.out.println("Risposta" + ESISTE_FILE + "ottenutoDaNome: " + nomeCompletoFile + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unFile = new File(nomeCompletoFile);
        assertNotNull(unFile);
        ottenutoDaFile = service.isEsisteFileStr(unFile);
        assertTrue(service.isEsisteFile(unFile));
        assertEquals(VUOTA, ottenutoDaFile);
        System.out.println("Risposta" + ESISTE_FILE + "ottenutoDaFile: " + nomeCompletoFile + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));
    }


    @Test
    @Order(3)
    @DisplayName("3 - isEsisteDirectory")
    public void isEsisteDirectory() {
        nomeCompletoDirectory = "null";
        ottenutoDaNome = service.isEsisteDirectoryStr((String) null);
        assertFalse(service.isEsisteDirectory((String) null));
        assertEquals(PATH_NULLO, ottenutoDaNome);
        System.out.println("Risposta" + ESISTE_DIRECTORY + "ottenutoDaNome: " + nomeCompletoDirectory + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        ottenutoDaFile = service.isEsisteDirectoryStr((File) null);
        assertFalse(service.isEsisteDirectory((File) null));
        assertEquals(PARAMETRO_NULLO, ottenutoDaFile);
        System.out.println("Risposta" + ESISTE_DIRECTORY + "ottenutoDaFile: " + nomeCompletoDirectory + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoDirectory = VUOTA;
        ottenutoDaNome = service.isEsisteDirectoryStr(nomeCompletoDirectory);
        assertFalse(service.isEsisteDirectory(nomeCompletoDirectory));
        assertEquals(PATH_NULLO, ottenutoDaNome);
        System.out.println("Risposta" + ESISTE_DIRECTORY + "ottenutoDaNome: " + nomeCompletoDirectory + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unaDirectory = new File(nomeCompletoDirectory);
        assertNotNull(unaDirectory);
        ottenutoDaFile = service.isEsisteDirectoryStr((File) null);
        assertFalse(service.isEsisteDirectory((File) null));
        assertEquals(PARAMETRO_NULLO, ottenutoDaFile);
        System.out.println("Risposta" + ESISTE_DIRECTORY + "ottenutoDaFile: " + nomeCompletoDirectory + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoDirectory = "nonEsiste";
        ottenutoDaNome = service.isEsisteDirectoryStr(nomeCompletoDirectory);
        assertFalse(service.isEsisteDirectory(nomeCompletoDirectory));
        assertEquals(PATH_NOT_ABSOLUTE, ottenutoDaNome);
        System.out.println("Risposta" + ESISTE_DIRECTORY + "ottenutoDaNome: " + nomeCompletoDirectory + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unaDirectory = new File(nomeCompletoDirectory);
        assertNotNull(unaDirectory);
        ottenutoDaFile = service.isEsisteDirectoryStr(unaDirectory);
        assertFalse(service.isEsisteDirectory(unaDirectory));
        assertEquals(PATH_NOT_ABSOLUTE, ottenutoDaFile);
        System.out.println("Risposta" + ESISTE_DIRECTORY + "ottenutoDaFile: " + nomeCompletoDirectory + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoDirectory = PATH_DIRECTORY_NON_ESISTENTE;
        ottenutoDaNome = service.isEsisteDirectoryStr(nomeCompletoDirectory);
        assertFalse(service.isEsisteDirectory(nomeCompletoDirectory));
        assertEquals(NON_ESISTE_DIRECTORY, ottenutoDaNome);
        System.out.println("Risposta" + ESISTE_DIRECTORY + "ottenutoDaNome: " + nomeCompletoDirectory + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unaDirectory = new File(nomeCompletoDirectory);
        assertNotNull(unaDirectory);
        ottenutoDaFile = service.isEsisteDirectoryStr(unaDirectory);
        assertFalse(service.isEsisteDirectory(unaDirectory));
        assertEquals(NON_ESISTE_DIRECTORY, ottenutoDaFile);
        System.out.println("Risposta" + ESISTE_DIRECTORY + "ottenutoDaFile: " + nomeCompletoDirectory + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoDirectory = PATH_FILE_UNO;
        ottenutoDaNome = service.isEsisteDirectoryStr(nomeCompletoDirectory);
        assertFalse(service.isEsisteDirectory(nomeCompletoDirectory));
        assertEquals(NON_E_DIRECTORY, ottenutoDaNome);
        System.out.println("Risposta" + ESISTE_DIRECTORY + "ottenutoDaNome: " + nomeCompletoDirectory + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unaDirectory = new File(nomeCompletoDirectory);
        assertNotNull(unaDirectory);
        ottenutoDaFile = service.isEsisteDirectoryStr(unaDirectory);
        assertFalse(service.isEsisteDirectory(unaDirectory));
        assertEquals(NON_E_DIRECTORY, ottenutoDaFile);
        System.out.println("Risposta" + ESISTE_DIRECTORY + "ottenutoDaFile: " + nomeCompletoDirectory + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoDirectory = PATH_DIRECTORY_NO_PATH;
        ottenutoDaNome = service.isEsisteDirectoryStr(nomeCompletoDirectory);
        assertFalse(service.isEsisteDirectory(nomeCompletoDirectory));
        assertEquals(PATH_NOT_ABSOLUTE, ottenutoDaNome);
        System.out.println("Risposta" + ESISTE_DIRECTORY + "ottenutoDaNome: " + nomeCompletoDirectory + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unaDirectory = new File(nomeCompletoDirectory);
        assertNotNull(unaDirectory);
        ottenutoDaFile = service.isEsisteDirectoryStr(unaDirectory);
        assertFalse(service.isEsisteDirectory(unaDirectory));
        assertEquals(PATH_NOT_ABSOLUTE, ottenutoDaFile);
        System.out.println("Risposta" + ESISTE_DIRECTORY + "ottenutoDaFile: " + nomeCompletoDirectory + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoDirectory = PATH_DIRECTORY_UNO;
        ottenutoDaNome = service.isEsisteDirectoryStr(nomeCompletoDirectory);
        assertTrue(service.isEsisteDirectory(nomeCompletoDirectory));
        assertEquals(VUOTA, ottenutoDaNome);
        System.out.println("Risposta" + ESISTE_DIRECTORY + "ottenutoDaNome: " + nomeCompletoDirectory + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unaDirectory = new File(nomeCompletoDirectory);
        assertNotNull(unaDirectory);
        ottenutoDaFile = service.isEsisteDirectoryStr(unaDirectory);
        assertTrue(service.isEsisteDirectory(unaDirectory));
        assertEquals(VUOTA, ottenutoDaFile);
        System.out.println("Risposta" + ESISTE_DIRECTORY + "ottenutoDaFile: " + nomeCompletoDirectory + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoDirectory = PATH_DIRECTORY_ESISTENTE_CON_MAIUSCOLA_SBAGLIATA;
        ottenutoDaNome = service.isEsisteDirectoryStr(nomeCompletoDirectory);
        assertTrue(service.isEsisteDirectory(nomeCompletoDirectory));
        assertEquals(VUOTA, ottenutoDaNome);
        System.out.println("Risposta" + ESISTE_DIRECTORY + "ottenutoDaNome: " + nomeCompletoDirectory + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unaDirectory = new File(nomeCompletoDirectory);
        assertNotNull(unaDirectory);
        ottenutoDaFile = service.isEsisteDirectoryStr(unaDirectory);
        assertTrue(service.isEsisteDirectory(unaDirectory));
        assertEquals(VUOTA, ottenutoDaFile);
        System.out.println("Risposta" + ESISTE_DIRECTORY + "ottenutoDaFile: " + nomeCompletoDirectory + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));
    }


    @Test
    @Order(4)
    @DisplayName("4 - creaFile")
    public void creaFile() {
        nomeCompletoFile = "null";
        ottenutoDaNome = service.creaFileStr((String) null);
        assertFalse(service.creaFile((String) null));
        assertEquals(PATH_NULLO, ottenutoDaNome);
        System.out.println("Risposta" + CREA_FILE + "ottenutoDaNome: " + nomeCompletoFile + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        ottenutoDaFile = service.creaFileStr((File) null);
        assertFalse(service.creaFile((File) null));
        assertEquals(PARAMETRO_NULLO, ottenutoDaFile);
        System.out.println("Risposta" + CREA_FILE + "ottenutoDaFile: " + nomeCompletoFile + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoFile = VUOTA;
        ottenutoDaNome = service.creaFileStr(nomeCompletoFile);
        assertFalse(service.creaFile(nomeCompletoFile));
        assertEquals(PATH_NULLO, ottenutoDaNome);
        System.out.println("Risposta" + CREA_FILE + "ottenutoDaNome: " + nomeCompletoFile + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unFile = new File(nomeCompletoFile);
        assertNotNull(unFile);
        ottenutoDaFile = service.creaFileStr(unFile);
        assertFalse(service.creaFile(unFile));
        assertEquals(PATH_NULLO, ottenutoDaFile);
        System.out.println("Risposta" + CREA_FILE + "ottenutoDaFile: " + nomeCompletoFile + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoFile = PATH_FILE_NO_SUFFIX;
        ottenutoDaNome = service.creaFileStr(nomeCompletoFile);
        assertFalse(service.creaFile(nomeCompletoFile));
        assertEquals(PATH_SENZA_SUFFIX, ottenutoDaNome);
        System.out.println("Risposta" + CREA_FILE + "ottenutoDaNome: " + nomeCompletoFile + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unFile = new File(nomeCompletoFile);
        assertNotNull(unFile);
        ottenutoDaFile = service.creaFileStr(unFile);
        assertFalse(service.creaFile(unFile));
        assertEquals(PATH_SENZA_SUFFIX, ottenutoDaFile);
        System.out.println("Risposta" + CREA_FILE + "ottenutoDaFile: " + nomeCompletoFile + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        creaFileValido(PATH_FILE_DELETE);
        creaFileValido(PATH_FILE_ANOMALO);
    }


    @Test
    @Order(5)
    @DisplayName("5 - creaDirectory")
    public void creaDirectory() {
        nomeCompletoDirectory = "null";
        ottenutoDaNome = service.creaDirectoryStr((String) null);
        assertFalse(service.creaDirectory((String) null));
        assertEquals(PATH_NULLO, ottenutoDaNome);
        System.out.println("Risposta" + CREA_DIRECTORY + "ottenutoDaNome: " + nomeCompletoDirectory + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        ottenutoDaFile = service.creaDirectoryStr((File) null);
        assertFalse(service.creaDirectory((File) null));
        assertEquals(PARAMETRO_NULLO, ottenutoDaFile);
        System.out.println("Risposta" + CREA_DIRECTORY + "ottenutoDaFile: " + nomeCompletoDirectory + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoDirectory = VUOTA;
        ottenutoDaNome = service.creaDirectoryStr(nomeCompletoDirectory);
        assertFalse(service.creaDirectory(nomeCompletoDirectory));
        assertEquals(PATH_NULLO, ottenutoDaNome);
        System.out.println("Risposta" + CREA_DIRECTORY + "ottenutoDaNome: " + nomeCompletoDirectory + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unaDirectory = new File(nomeCompletoDirectory);
        assertNotNull(unaDirectory);
        ottenutoDaFile = service.creaDirectoryStr(unaDirectory);
        assertFalse(service.creaDirectory(unaDirectory));
        assertEquals(PATH_NULLO, ottenutoDaFile);
        System.out.println("Risposta" + CREA_DIRECTORY + "ottenutoDaFile: " + nomeCompletoDirectory + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoDirectory = "nonEsiste";
        ottenutoDaNome = service.creaDirectoryStr(nomeCompletoDirectory);
        assertFalse(service.creaDirectory(nomeCompletoDirectory));
        assertEquals(PATH_NOT_ABSOLUTE, ottenutoDaNome);
        System.out.println("Risposta" + CREA_DIRECTORY + "ottenutoDaNome: " + nomeCompletoDirectory + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unaDirectory = new File(nomeCompletoDirectory);
        assertNotNull(unaDirectory);
        ottenutoDaFile = service.creaDirectoryStr(unaDirectory);
        assertFalse(service.creaDirectory(unaDirectory));
        assertEquals(PATH_NOT_ABSOLUTE, ottenutoDaFile);
        System.out.println("Risposta" + CREA_DIRECTORY + "ottenutoDaFile: " + nomeCompletoDirectory + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoDirectory = "/Users/gac/Desktop/test/Paperino/Topolino.abc";
        ottenutoDaNome = service.creaDirectoryStr(nomeCompletoDirectory);
        assertFalse(service.creaDirectory(nomeCompletoDirectory));
        assertEquals(NON_E_DIRECTORY, ottenutoDaNome);
        System.out.println("Risposta" + CREA_DIRECTORY + "ottenutoDaNome: " + nomeCompletoDirectory + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unaDirectory = new File(nomeCompletoDirectory);
        assertNotNull(unaDirectory);
        ottenutoDaFile = service.creaDirectoryStr(unaDirectory);
        assertFalse(service.creaDirectory(unaDirectory));
        assertEquals(NON_E_DIRECTORY, ottenutoDaFile);
        System.out.println("Risposta" + CREA_DIRECTORY + "ottenutoDaFile: " + nomeCompletoDirectory + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        creaDirectoryValida(PATH_DIRECTORY_TRE);
    }


    @Test
    @Order(6)
    @DisplayName("6 - deleteFile")
    public void deleteFile() {
        nomeCompletoFile = "null";
        ottenutoDaNome = service.deleteFileStr((String) null);
        assertFalse(service.deleteFile((String) null));
        assertEquals(PATH_NULLO, ottenutoDaNome);
        System.out.println("Risposta" + DELETE_FILE + "ottenutoDaNome: " + nomeCompletoFile + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        ottenutoDaFile = service.deleteFileStr((File) null);
        assertFalse(service.deleteFile((File) null));
        assertEquals(PARAMETRO_NULLO, ottenutoDaFile);
        System.out.println("Risposta" + DELETE_FILE + "ottenutoDaFile: " + nomeCompletoFile + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoFile = VUOTA;
        ottenutoDaNome = service.deleteFileStr(nomeCompletoFile);
        assertFalse(service.deleteFile(nomeCompletoFile));
        assertEquals(PATH_NULLO, ottenutoDaNome);
        System.out.println("Risposta" + DELETE_FILE + "ottenutoDaNome: " + nomeCompletoFile + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unFile = new File(nomeCompletoFile);
        assertNotNull(unFile);
        ottenutoDaFile = service.deleteFileStr(unFile);
        assertFalse(service.deleteFile(unFile));
        assertEquals(PATH_NULLO, ottenutoDaFile);
        System.out.println("Risposta" + DELETE_FILE + "ottenutoDaFile: " + nomeCompletoFile + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoFile = "Pippo";
        ottenutoDaNome = service.deleteFileStr(nomeCompletoFile);
        assertFalse(service.deleteFile(nomeCompletoFile));
        assertEquals(PATH_NOT_ABSOLUTE, ottenutoDaNome);
        System.out.println("Risposta" + DELETE_FILE + "ottenutoDaNome: " + nomeCompletoFile + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unFile = new File(nomeCompletoFile);
        assertNotNull(unFile);
        ottenutoDaFile = service.deleteFileStr(unFile);
        assertFalse(service.deleteFile(unFile));
        assertEquals(PATH_NOT_ABSOLUTE, ottenutoDaFile);
        System.out.println("Risposta" + DELETE_FILE + "ottenutoDaFile: " + nomeCompletoFile + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoFile = PATH_FILE_NON_ESISTENTE;
        ottenutoDaNome = service.deleteFileStr(nomeCompletoFile);
        assertFalse(service.deleteFile(nomeCompletoFile));
        assertEquals(NON_ESISTE_FILE, ottenutoDaNome);
        System.out.println("Risposta" + DELETE_FILE + "ottenutoDaNome: " + nomeCompletoFile + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unFile = new File(nomeCompletoFile);
        assertNotNull(unFile);
        ottenutoDaFile = service.deleteFileStr(unFile);
        assertFalse(service.deleteFile(unFile));
        assertEquals(NON_ESISTE_FILE, ottenutoDaFile);
        System.out.println("Risposta" + DELETE_FILE + "ottenutoDaFile: " + nomeCompletoFile + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        deleFileValido(PATH_FILE_DELETE);
    }


    @Test
    @Order(7)
    @DisplayName("7 - deleteDirectory")
    public void deleteDirectory() {
        nomeCompletoDirectory = "null";
        ottenutoDaNome = service.deleteDirectoryStr((String) null);
        assertFalse(service.deleteDirectory((String) null));
        assertEquals(PATH_NULLO, ottenutoDaNome);
        System.out.println("Risposta" + DELETE_DIRECTORY + "ottenutoDaNome: " + nomeCompletoDirectory + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        ottenutoDaFile = service.deleteDirectoryStr((File) null);
        assertFalse(service.deleteDirectory((File) null));
        assertEquals(PARAMETRO_NULLO, ottenutoDaFile);
        System.out.println("Risposta" + DELETE_DIRECTORY + "ottenutoDaFile: " + nomeCompletoDirectory + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoDirectory = VUOTA;
        ottenutoDaNome = service.deleteDirectoryStr(nomeCompletoDirectory);
        assertFalse(service.deleteDirectory(nomeCompletoDirectory));
        assertEquals(PATH_NULLO, ottenutoDaNome);
        System.out.println("Risposta" + DELETE_DIRECTORY + "ottenutoDaNome: " + nomeCompletoDirectory + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unaDirectory = new File(nomeCompletoDirectory);
        assertNotNull(unaDirectory);
        ottenutoDaFile = service.deleteDirectoryStr(unaDirectory);
        assertFalse(service.deleteDirectory(unaDirectory));
        assertEquals(PATH_NULLO, ottenutoDaFile);
        System.out.println("Risposta" + DELETE_DIRECTORY + "ottenutoDaFile: " + nomeCompletoDirectory + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoDirectory = "Pippo";
        ottenutoDaNome = service.deleteDirectoryStr(nomeCompletoDirectory);
        assertFalse(service.deleteDirectory(nomeCompletoDirectory));
        assertEquals(PATH_NOT_ABSOLUTE, ottenutoDaNome);
        System.out.println("Risposta" + DELETE_DIRECTORY + "ottenutoDaNome: " + nomeCompletoDirectory + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unaDirectory = new File(nomeCompletoDirectory);
        assertNotNull(unaDirectory);
        ottenutoDaFile = service.deleteDirectoryStr(unaDirectory);
        assertFalse(service.deleteDirectory(unaDirectory));
        assertEquals(PATH_NOT_ABSOLUTE, ottenutoDaFile);
        System.out.println("Risposta" + DELETE_DIRECTORY + "ottenutoDaFile: " + nomeCompletoDirectory + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoDirectory = PATH_DIRECTORY_NON_ESISTENTE;
        ottenutoDaNome = service.deleteDirectoryStr(nomeCompletoDirectory);
        assertFalse(service.deleteDirectory(nomeCompletoDirectory));
        assertEquals(NON_ESISTE_DIRECTORY, ottenutoDaNome);
        System.out.println("Risposta" + DELETE_DIRECTORY + "ottenutoDaNome: " + nomeCompletoDirectory + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unaDirectory = new File(nomeCompletoDirectory);
        assertNotNull(unaDirectory);
        ottenutoDaFile = service.deleteDirectoryStr(unaDirectory);
        assertFalse(service.deleteDirectory(unaDirectory));
        assertEquals(NON_ESISTE_DIRECTORY, ottenutoDaFile);
        System.out.println("Risposta" + DELETE_DIRECTORY + "ottenutoDaFile: " + nomeCompletoDirectory + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        deleteDirectoryValida(PATH_DIRECTORY_NON_ESISTENTE);
    }


    @Test
    @Order(8)
    @DisplayName("8 - copyFile")
    public void copyFile() {
        String srcPathNonEsistente = PATH_FILE_NON_ESISTENTE;
        String srcPath = PATH_FILE_UNO;
        String destPath = PATH_DIRECTORY_TEST + "NuovoFile.rtf";
        String destPathEsistente = PATH_FILE_DUE;

        //--esegue con sorgente NON esistente
        ottenutoBooleano = service.copyFile(srcPathNonEsistente, destPath);
        assertFalse(ottenutoBooleano);
        ottenuto = service.copyFileStr(srcPathNonEsistente, destPath);
        assertEquals(NON_ESISTE_FILE, ottenuto);

        //--esegue con destinazione GIA esistente
        ottenutoBooleano = service.copyFile(srcPath, destPathEsistente);
        assertFalse(ottenutoBooleano);
        ottenuto = service.copyFileStr(srcPathNonEsistente, destPath);
        assertEquals(NON_ESISTE_FILE, ottenuto);

        //--controllo condizioni iniziali
        assertTrue(service.isEsisteFile(srcPath));
        assertFalse(service.isEsisteFile(destPath));

        //--esegue
        ottenutoBooleano = service.copyFile(srcPath, destPath);
        assertTrue(ottenutoBooleano);

        //--controllo condizioni finali
        assertTrue(service.isEsisteFile(srcPath));
        assertTrue(service.isEsisteFile(destPath));

        //--ripristina condizioni iniziali
        assertTrue(service.deleteFile(destPath));
    }


    @Test
    @Order(9)
    @DisplayName("9 - copyDirectoryDeletingAll")
    public void copyDirectoryDeletingAll() {
        String srcPathValida = PATH_DIRECTORY_TRE;
        String srcPathNonEsistente = PATH_DIRECTORY_NON_ESISTENTE;
        String destPathDaSovrascrivere = PATH_DIRECTORY_MANCANTE;
        String destFileAggiunto = PATH_FILE_AGGIUNTO;

        //--esegue con sorgente NON esistente
        assertFalse(service.isEsisteDirectory(srcPathNonEsistente));
        assertFalse(service.isEsisteDirectory(destPathDaSovrascrivere));
        ottenutoBooleano = service.copyDirectoryDeletingAll(srcPathNonEsistente, destPathDaSovrascrivere);
        assertFalse(ottenutoBooleano);
        assertFalse(service.isEsisteDirectory(destPathDaSovrascrivere));

        //--esegue con destinazione NON esistente
        assertTrue(service.isEsisteDirectory(srcPathValida));
        assertFalse(service.isEsisteDirectory(destPathDaSovrascrivere));
        ottenutoBooleano = service.copyDirectoryDeletingAll(srcPathValida, destPathDaSovrascrivere);
        assertTrue(ottenutoBooleano);
        assertTrue(service.isEsisteDirectory(destPathDaSovrascrivere));

        //--aggiunge nella cartella di destinazione un file per controllare che venga (come previsto) cancellato
        assertTrue(service.creaFile(destFileAggiunto));

        //--esegue con destinazione esistente
        assertTrue(service.isEsisteDirectory(destPathDaSovrascrivere));
        ottenutoBooleano = service.copyDirectoryDeletingAll(srcPathValida, destPathDaSovrascrivere);
        assertTrue(ottenutoBooleano);

        try {
            FileUtils.forceDelete(new File(destPathDaSovrascrivere));
        } catch (Exception unErrore) {
        }

        assertFalse(service.isEsisteDirectory(destPathDaSovrascrivere));
    }


    @Test
    @Order(10)
    @DisplayName("10 - copyDirectoryOnlyNotExisting")
    public void copyDirectoryOnlyNotExisting() {
        String srcPathValida = PATH_DIRECTORY_TRE;
        String srcPathNonEsistente = PATH_DIRECTORY_NON_ESISTENTE;
        String destPathDaSovrascrivere = PATH_DIRECTORY_MANCANTE;
        String srcFileAggiunto = PATH_FILE_AGGIUNTO_TRE;

        //--esegue con sorgente NON esistente
        assertFalse(service.isEsisteDirectory(srcPathNonEsistente));
        assertFalse(service.isEsisteDirectory(destPathDaSovrascrivere));
        ottenutoBooleano = service.copyDirectoryOnlyNotExisting(srcPathNonEsistente, destPathDaSovrascrivere);
        assertFalse(ottenutoBooleano);
        assertFalse(service.isEsisteDirectory(destPathDaSovrascrivere));

        //--esegue con destinazione NON esistente
        assertTrue(service.isEsisteDirectory(srcPathValida));
        assertFalse(service.isEsisteDirectory(destPathDaSovrascrivere));
        ottenutoBooleano = service.copyDirectoryOnlyNotExisting(srcPathValida, destPathDaSovrascrivere);
        assertTrue(ottenutoBooleano);
        assertTrue(service.isEsisteDirectory(destPathDaSovrascrivere));

        //--aggiunge nella cartella sorgente un file per controllare che NON venga aggiunto (come previsto)
        assertTrue(service.creaFile(srcFileAggiunto));

        //--esegue con destinazione esistente
        assertTrue(service.isEsisteDirectory(destPathDaSovrascrivere));
        assertTrue(service.isEsisteDirectory(destPathDaSovrascrivere));
        ottenutoBooleano = service.copyDirectoryOnlyNotExisting(srcPathValida, destPathDaSovrascrivere);
        assertFalse(ottenutoBooleano);

        try {
            FileUtils.forceDelete(new File(destPathDaSovrascrivere));
        } catch (Exception unErrore) {
        }

        assertFalse(service.isEsisteDirectory(destPathDaSovrascrivere));
    }


    @Test
    @Order(11)
    @DisplayName("11 - copyDirectoryOnlyNotExisting2")
    public void copyDirectoryOnlyNotExisting2() {
        String sorgente = PATH_DIRECTORY_TEST + "Sorgente/";
        String destinazione = PATH_DIRECTORY_TEST + "Destinazione/";
        String dirUno = "PrimaDirectory/";
        String dirDue = "SecondaDirectory/";
        creaCartelleTemporanee(sorgente, destinazione, dirUno, dirDue);

        ottenutoBooleano = service.copyDirectoryAddingOnly(sorgente, destinazione);
        assertTrue(ottenutoBooleano);

        assertTrue(service.isEsisteDirectory(destinazione + "Rimane"));
        assertTrue(service.isEsisteDirectory(destinazione + "VieneCopiata"));

        assertTrue(service.isEsisteFile(destinazione + dirUno + "TerzoFileVariabile.txx"));
        assertTrue(service.isEsisteFile(destinazione + dirUno + "QuartoIncerto.txx"));

        assertTrue(service.isEsisteFile(destinazione + dirDue + "TerzoFileVariabile.txx"));
        assertTrue(service.isEsisteFile(destinazione + dirDue + "QuartoIncerto.txx"));

        try {
            FileUtils.forceDelete(new File(destinazione));
        } catch (Exception unErrore) {
        }
    }


    @Test
    @Order(12)
    @DisplayName("12 - copyDirectoryAddingOnly")
    public void copyDirectoryAddingOnly() {
        String srcPathValida = PATH_DIRECTORY_TRE;
        String srcPathNonEsistente = PATH_DIRECTORY_NON_ESISTENTE;
        String destPathDaSovrascrivere = PATH_DIRECTORY_MANCANTE;
        String destFileAggiunto = PATH_FILE_AGGIUNTO;
        String srcFileAggiunto = PATH_FILE_AGGIUNTO_TRE;

        //--esegue con sorgente NON esistente
        assertFalse(service.isEsisteDirectory(srcPathNonEsistente));
        assertFalse(service.isEsisteDirectory(destPathDaSovrascrivere));
        ottenutoBooleano = service.copyDirectoryAddingOnly(srcPathNonEsistente, destPathDaSovrascrivere);
        assertFalse(ottenutoBooleano);
        assertFalse(service.isEsisteDirectory(destPathDaSovrascrivere));

        //--esegue con destinazione NON esistente
        assertTrue(service.isEsisteDirectory(srcPathValida));
        assertFalse(service.isEsisteDirectory(destPathDaSovrascrivere));
        ottenutoBooleano = service.copyDirectoryAddingOnly(srcPathValida, destPathDaSovrascrivere);
        assertTrue(ottenutoBooleano);
        assertTrue(service.isEsisteDirectory(destPathDaSovrascrivere));

        //--aggiunge nella cartella sorgente un file per controllare che venga (come previsto) copiato
        assertTrue(service.creaFile(srcFileAggiunto));

        //--aggiunge nella cartella di destinazione un file per controllare che venga (come previsto) mantenuto
        assertTrue(service.creaFile(destFileAggiunto));

        //--esegue con destinazione esistente
        assertTrue(service.isEsisteDirectory(destPathDaSovrascrivere));
        assertTrue(service.isEsisteDirectory(destPathDaSovrascrivere));
        ottenutoBooleano = service.copyDirectoryAddingOnly(srcPathValida, destPathDaSovrascrivere);
        assertTrue(ottenutoBooleano);

        try {
            FileUtils.forceDelete(new File(destPathDaSovrascrivere));
        } catch (Exception unErrore) {
        }

        assertFalse(service.isEsisteDirectory(destPathDaSovrascrivere));
    }


    @Test
    @Order(13)
    @DisplayName("13 - copyDirectory")
    public void copyDirectory() {
        String srcPathValida = PATH_DIRECTORY_TRE;
        String srcPathNonEsistente = PATH_DIRECTORY_NON_ESISTENTE;
        String destPathDaSovrascrivere = PATH_DIRECTORY_MANCANTE;
        String destFileAggiunto = PATH_FILE_AGGIUNTO;

        //--esegue con sorgente NON esistente
        //--messaggio di errore
        assertFalse(service.isEsisteDirectory(srcPathNonEsistente));
        assertFalse(service.isEsisteDirectory(destPathDaSovrascrivere));
        ottenutoBooleano = service.copyDirectory(AECopy.dirAddingOnly, srcPathNonEsistente, destPathDaSovrascrivere);
        assertFalse(ottenutoBooleano);
        assertFalse(service.isEsisteDirectory(destPathDaSovrascrivere));

        //--esegue con sorgente NON esistente
        //--messaggio di errore
        assertFalse(service.isEsisteDirectory(srcPathNonEsistente));
        assertFalse(service.isEsisteDirectory(destPathDaSovrascrivere));
        ottenutoBooleano = service.copyDirectory(AECopy.dirDeletingAll, srcPathNonEsistente, destPathDaSovrascrivere);
        assertFalse(ottenutoBooleano);
        assertFalse(service.isEsisteDirectory(destPathDaSovrascrivere));

        //--esegue con sorgente NON esistente
        //--messaggio di errore
        assertFalse(service.isEsisteDirectory(srcPathNonEsistente));
        assertFalse(service.isEsisteDirectory(destPathDaSovrascrivere));
        ottenutoBooleano = service.copyDirectory(AECopy.dirSoloSeNonEsiste, srcPathNonEsistente, destPathDaSovrascrivere);
        assertFalse(ottenutoBooleano);
        assertFalse(service.isEsisteDirectory(destPathDaSovrascrivere));

        //--esegue con sorgente NON esistente
        //--messaggio di errore
        assertFalse(service.isEsisteDirectory(srcPathNonEsistente));
        assertFalse(service.isEsisteDirectory(destPathDaSovrascrivere));
        ottenutoBooleano = service.copyDirectory((AECopy) null, srcPathNonEsistente, destPathDaSovrascrivere);
        assertFalse(ottenutoBooleano);
        assertFalse(service.isEsisteDirectory(destPathDaSovrascrivere));

        //--esegue con destinazione NON esistente
        assertTrue(service.isEsisteDirectory(srcPathValida));
        assertFalse(service.isEsisteDirectory(destPathDaSovrascrivere));
        ottenutoBooleano = service.copyDirectory(AECopy.dirSoloSeNonEsiste, srcPathValida, destPathDaSovrascrivere);
        assertTrue(ottenutoBooleano);
        assertTrue(service.isEsisteDirectory(destPathDaSovrascrivere));

        //--esegue con destinazione esistente
        assertTrue(service.isEsisteDirectory(srcPathValida));
        assertTrue(service.isEsisteDirectory(destPathDaSovrascrivere));
        ottenutoBooleano = service.copyDirectory(AECopy.dirDeletingAll, srcPathValida, destPathDaSovrascrivere);
        assertTrue(ottenutoBooleano);
        assertTrue(service.isEsisteDirectory(destPathDaSovrascrivere));

        //--esegue con destinazione esistente
        assertTrue(service.isEsisteDirectory(srcPathValida));
        assertTrue(service.isEsisteDirectory(destPathDaSovrascrivere));
        ottenutoBooleano = service.copyDirectory(AECopy.dirAddingOnly, srcPathValida, destPathDaSovrascrivere);
        assertTrue(ottenutoBooleano);
        assertTrue(service.isEsisteDirectory(destPathDaSovrascrivere));

        //--esegue con destinazione esistente
        assertTrue(service.isEsisteDirectory(srcPathValida));
        assertTrue(service.isEsisteDirectory(destPathDaSovrascrivere));
        ottenutoBooleano = service.copyDirectory(AECopy.dirSoloSeNonEsiste, srcPathValida, destPathDaSovrascrivere);
        assertFalse(ottenutoBooleano);
        assertTrue(service.isEsisteDirectory(destPathDaSovrascrivere));

        try {
            FileUtils.forceDelete(new File(destPathDaSovrascrivere));
        } catch (Exception unErrore) {
        }
    }


    @Test
    @Order(14)
    @DisplayName("14 - leggeFile")
    public void leggeFile() {
        String result = System.getenv("PATH");
        System.out.println(result);
        String path = "/Users/gac/Documents/IdeaProjects/operativi/vaadflow15/" + result;

        sorgente = "password.txt";
        sorgente = path + "/" + sorgente;
        ottenuto = service.leggeFile(sorgente);
        System.out.println(ottenuto);
    }


    @Test
    @Order(15)
    @DisplayName("15 - levaDirectoryFinale")
    public void levaDirectoryFinale() {
        sorgente = "/Users/gac/Documents/IdeaProjects/operativi/vaadflow/";
        previsto = "/Users/gac/Documents/IdeaProjects/operativi/";
        ottenuto = service.levaDirectoryFinale(sorgente);
        assertEquals(previsto, ottenuto);
    }


    @Test
    @Order(16)
    @DisplayName("16 - getSubdiretories")
    public void getSubdiretories() {
        sorgente = DIRECTORY_IDEA;
        listaDirectory = service.getSubDirectories(sorgente);
        if (listaDirectory != null) {
            for (File file : listaDirectory) {
                System.out.println(file.getName());
            }
        }
    }


    @Test
    @Order(17)
    @DisplayName("17 - getSubdiretories2")
    public void getSubdiretories2() {
        File fileSorgente = new File(DIRECTORY_IDEA);
        listaDirectory = service.getSubDirectories(fileSorgente);
        if (listaDirectory != null) {
            for (File file : listaDirectory) {
                System.out.println(file.getName());
            }
        }
    }


    @Test
    @Order(18)
    @DisplayName("18 - getSubDirectoriesName")
    public void getSubDirectoriesName() {
        sorgente = DIRECTORY_IDEA;
        List<String> ottenuto = service.getSubDirectoriesName(sorgente);
        System.out.println("Tramite path");
        System.out.println("************");
        if (ottenuto != null) {
            for (String directory : ottenuto) {
                System.out.println(directory);
            }
        }

        File fileSorgente = new File(DIRECTORY_IDEA);
        ottenuto = service.getSubDirectoriesName(fileSorgente);
        System.out.println("");
        System.out.println("Tramite file");
        System.out.println("************");
        if (ottenuto != null) {
            for (String directory : ottenuto) {
                System.out.println(directory);
            }
        }
    }


    @Test
    @Order(19)
    @DisplayName("19 - getSubDirectoriesAbsolutePathName")
    public void getSubDirectoriesAbsolutePathName() {
        sorgente = DIRECTORY_IDEA;
        List<String> ottenuto = service.getSubDirectoriesAbsolutePathName(sorgente);
        if (ottenuto != null) {
            for (String directory : ottenuto) {
                System.out.println(directory);
            }
        }
    }


    @Test
    @Order(20)
    @DisplayName("20 - getSubSubDirectories")
    public void getSubSubDirectories() {
        sorgente = "/Users/gac/Documents/IdeaProjects/operativi/vaadflow";
        String dirInterna = "src/main";
        listaDirectory = service.getSubSubDirectories(sorgente, dirInterna);
        assertEquals(listaDirectory.size(), 3);
        if (listaDirectory != null) {
            for (File file : listaDirectory) {
                System.out.println(file.getName());
            }
        }

        sorgente = "/Users/gac/Documents/IdeaProjects/operativi/vaadflow/";
        dirInterna = "/src/main";
        listaDirectory = service.getSubSubDirectories(sorgente, dirInterna);
        assertEquals(listaDirectory.size(), 3);

        sorgente = "/Users/gac/Documents/IdeaProjects/operativi/vaadflow";
        dirInterna = "/src/main";
        listaDirectory = service.getSubSubDirectories(sorgente, dirInterna);
        assertEquals(listaDirectory.size(), 3);

        sorgente = "/Users/gac/Documents/IdeaProjects/operativi/vaadflow/";
        dirInterna = "src/main";
        listaDirectory = service.getSubSubDirectories(sorgente, dirInterna);
        assertEquals(listaDirectory.size(), 3);

    }


    @Test
    @Order(21)
    @DisplayName("21 - isEsisteSubDirectory")
    public void isEsisteSubDirectory() {
        sorgente = "/Users/gac/Documents/IdeaProjects/operativi/vaadflow";
        String dirInterna = "src/main";
        ottenutoBooleano = service.isPienaSubDirectory(new File(sorgente), dirInterna);
        assertTrue(ottenutoBooleano);
    }


    @Test
    @Order(22)
    @DisplayName("22 - isVuotaSubDirectory")
    public void isVuotaSubDirectory() {
        sorgente = "/Users/gac/Documents/IdeaProjects/tutorial";
        String dirInterna = "src/main";
        ottenutoBooleano = service.isVuotaSubDirectory(new File(sorgente), dirInterna);
        assertTrue(ottenutoBooleano);
    }


    @Test
    @Order(23)
    @DisplayName("23 - leggeListaCSV")
    public void leggeListaCSV() {
        List<List<String>> lista;
        String result = System.getenv("PATH");
        String path = "/Users/gac/Documents/IdeaProjects/operativi/vaadflow15/" + result;

        sorgente = "regioni";
        sorgente = path + "/" + sorgente;
        lista = service.leggeListaCSV(sorgente);
        assertNotNull(lista);

        System.out.println("");
        System.out.println("Regioni");
        System.out.println("*******");
        for (List<String> riga : lista) {
            for (String colonna : riga) {
                System.out.print(colonna);
                System.out.print(SEP);
            }
            System.out.println("");
        }

        sorgente = "province";
        sorgente = path + "/" + sorgente;
        lista = service.leggeListaCSV(sorgente);
        assertNotNull(lista);

        System.out.println("");
        System.out.println("Province");
        System.out.println("*******");
        for (List<String> riga : lista) {
            for (String colonna : riga) {
                System.out.print(colonna);
                System.out.print(SEP);
            }
            System.out.println("");
        }
    }


    @Test
    @Order(24)
    @DisplayName("24 - leggeMappaCSV")
    public void leggeMappaCSV() {
        List<LinkedHashMap<String, String>> lista;
        String result = System.getenv("PATH");
        String path = "/Users/gac/Documents/IdeaProjects/operativi/vaadflow14/" + result;

        sorgente = "regioni";
        sorgente = path + "/" + sorgente;
        lista = service.leggeMappaCSV(sorgente);
        assertNotNull(lista);

        System.out.println("");
        System.out.println("Regioni");
        System.out.println("*******");
        for (Map<String, String> riga : lista) {
            for (String colonna : riga.keySet()) {
                System.out.print(riga.get(colonna));
                System.out.print(SEP);
            }
            System.out.println("");
        }

        System.out.println("");
        System.out.println("Regioni");
        System.out.println("*******");
        for (Map<String, String> riga : lista) {
            System.out.print(riga.get("ordine"));
            System.out.print(SEP);
            System.out.print(riga.get("nome"));
            System.out.print(SEP);
            System.out.print(riga.get("iso"));
            System.out.print(SEP);
            System.out.print(riga.get("sigla"));
            System.out.print(SEP);
            System.out.print(riga.get("tipo"));
            System.out.println("");
        }

        sorgente = "province";
        sorgente = path + "/" + sorgente;
        lista = service.leggeMappaCSV(sorgente);
        assertNotNull(lista);

        System.out.println("");
        System.out.println("Province");
        System.out.println("*******");
        for (Map<String, String> riga : lista) {
            for (String colonna : riga.keySet()) {
                System.out.print(riga.get(colonna));
                System.out.print(SEP);
            }
            System.out.println("");
        }

        System.out.println("");
        System.out.println("Province");
        System.out.println("*******");
        int num = 0;
        for (Map<String, String> riga : lista) {
            System.out.print(++num);
            System.out.print(SEP);
            System.out.print(riga.get("nome"));
            System.out.print(SEP);
            System.out.print(riga.get("regione"));
            System.out.print(SEP);
            System.out.print(riga.get("sigla"));
            System.out.print(SEP);
            System.out.print(riga.get("tipo"));
            System.out.println("");
        }
    }


    //    @Test
    @Order(24)
    @DisplayName("spostaFile-da rimettere")
    public void spostaFile() {
        nomeFile = "PerAdessoNonEsisto" + FlowCost.TXT_SUFFIX;
        String pathDirectoryUno = PATH_MODULO_PROVA + DIR_APPLICATION;
        String pathDirectoryDue = PATH_MODULO_PROVA + DIR_PACKAGES;
        String pathFileUno = pathDirectoryUno + nomeFile;
        String pathFileDue = pathDirectoryDue + nomeFile;

        //--situazione iniziale vuota
        assertFalse(service.isEsisteFile(pathFileUno));
        assertFalse(service.isEsisteFile(pathFileDue));

        //--provo con condizioni NON valide
        ottenuto = service.spostaFileStr(VUOTA, pathFileDue);
        assertEquals(PATH_NULLO, ottenuto);

        //--provo con condizioni NON valide
        ottenuto = service.spostaFileStr(pathFileUno, pathFileDue);
        assertEquals(NON_ESISTE_FILE, ottenuto);

        //--creo un file e controllo la situazione al momento
        service.creaFile(pathFileUno);
        assertTrue(service.isEsisteFile(pathFileUno));
        assertFalse(service.isEsisteFile(pathFileDue));

        //--eseguo lo spostamento
        ottenuto = service.spostaFileStr(pathFileUno, pathFileDue);
        assertEquals(VUOTA, ottenuto);

        //--controllo la situazione dopo lo spostamento
        assertFalse(service.isEsisteFile(pathFileUno));
        assertTrue(service.isEsisteFile(pathFileDue));

        //--cancello il file temporaneo
        service.deleteFile(pathFileDue);

        //--situazione finale vuota
        assertFalse(service.isEsisteFile(pathFileUno));
        assertFalse(service.isEsisteFile(pathFileDue));
    }


    @Test
    @Order(26)
    @DisplayName("26 - copyDirectoryLessFile")
    public void copyDirectoryLessFile() {
        String sorgente = PATH_DIRECTORY_TEST + "Alfetta/";
        String destinazione = PATH_DIRECTORY_TEST + "Beretta/";
        String dirUno = "PrimaDirectory/";
        String dirDue = "SecondaDirectory/";
        creaCartelleTemporanee(sorgente, destinazione, dirUno, dirDue);
    }


    @Test
    @Order(27)
    @DisplayName("27 - leggeFileConfig")
    public void leggeFileConfig() {
    }


    @Test
    @Order(28)
    @DisplayName("28 - Path progetto")
    public void pathProgetto() {
        String tag = "backend.application.FlowCost";
        String path = FlowCost.class.getCanonicalName();
        Package aPackage = FlowCost.class.getPackage();
        ClassLoader loader = FlowCost.class.getClassLoader();

        System.out.println(" ");
        System.out.println("pathProgettoGrezzo = " + path);
        System.out.println("aPackage = " + aPackage);
        System.out.println("ClassLoader = " + loader);
    }


    @Test
    @Order(29)
    @DisplayName("29 - getProjects")
    public void getProjects() {
        System.out.println("Recupera i progetti dalla directory IdeaProjects");
        System.out.println(" ");

        sorgente = DIRECTORY_IDEA;
        listaDirectory = service.getAllProjects(sorgente);
        if (listaDirectory != null) {
            for (File file : listaDirectory) {
                System.out.println(file.getName());
            }
        }
    }


    @Test
    @Order(30)
    @DisplayName("30 - getEmptyProjects")
    public void getEmptyProjects() {
        System.out.println("Recupera i progetti vuoti dalla directory IdeaProjects");
        System.out.println(" ");

        sorgente = DIRECTORY_IDEA;
        listaDirectory = service.getEmptyProjects(sorgente);
        if (listaDirectory != null) {
            for (File file : listaDirectory) {
                System.out.println(file.getName());
            }
        }
    }


    @Test
    @Order(31)
    @DisplayName("31 - daFare-scriveFile")
    public void scriveFile() {
    }


    @Test
    @Order(32)
    @DisplayName("32 - findPath che contiene una directory")
    public void findPath() {
        System.out.println("findPath che contiene una directory");
        System.out.println("");
        sorgente = "/Users/gac/Documents/IdeaProjects/operativi/vaadflow14/";

        previsto = VUOTA;
        ottenuto = service.findPathDirectory(VUOTA, VUOTA);
        assertEquals(previsto, ottenuto);
        printPath(VUOTA, VUOTA, ottenuto);

        previsto = "/Users/gac/Documents/IdeaProjects/operativi/vaadflow14/";
        ottenuto = service.findPathDirectory(sorgente, VUOTA);
        assertEquals(previsto, ottenuto);
        printPath(sorgente, sorgente2, ottenuto);

        sorgente2 = "sconosciuta";
        previsto = VUOTA;
        ottenuto = service.findPathDirectory(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);
        printPath(sorgente, sorgente2, ottenuto);

        sorgente2 = "Documents";
        previsto = "/Users/gac/";
        ottenuto = service.findPathDirectory(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);
        printPath(sorgente, sorgente2, ottenuto);

        sorgente2 = "IdeaProjects";
        previsto = "/Users/gac/Documents/";
        ottenuto = service.findPathDirectory(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);
        printPath(sorgente, sorgente2, ottenuto);

        sorgente2 = "operativi";
        previsto = "/Users/gac/Documents/IdeaProjects/";
        ottenuto = service.findPathDirectory(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);
        printPath(sorgente, sorgente2, ottenuto);

        sorgente2 = "vaadflow14";
        previsto = "/Users/gac/Documents/IdeaProjects/operativi/";
        ottenuto = service.findPathDirectory(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);
        printPath(sorgente, sorgente2, ottenuto);
    }

    @Test
    @Order(33)
    @DisplayName("33 - findPathBreve che inizia da una directory")
    public void findPathDopoDirectory() {
        System.out.println("pathBreve che inizia da ../ e una directory");
        System.out.println("");
        sorgente = "/Users/gac/Documents/IdeaProjects/operativi/vaadflow14/src/main/java/it/algos/vaadflow14/backend/packages/crono/giorno/";

        previsto = VUOTA;
        ottenuto = service.findPathBreve(VUOTA, VUOTA);
        assertEquals(previsto, ottenuto);
        printPath(VUOTA, VUOTA, ottenuto);

        previsto = sorgente;
        ottenuto = service.findPathBreve(sorgente, VUOTA);
        assertEquals(previsto, ottenuto);
        printPath(sorgente, VUOTA, ottenuto);

        sorgente2 = "Documents";
        previsto = VUOTA;
        ottenuto = service.findPathBreve(VUOTA, sorgente2);
        assertEquals(previsto, ottenuto);
        printPath(VUOTA, sorgente2, ottenuto);

        sorgente2 = "tutorial";
        previsto = sorgente;
        ottenuto = service.findPathBreve(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);
        printPath(sorgente, sorgente2, ottenuto);

        sorgente2 = "IdeaProjects";
        previsto = "../IdeaProjects/operativi/vaadflow14/src/main/java/it/algos/vaadflow14/backend/packages/crono/giorno";
        ottenuto = service.findPathBreve(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);
        printPath(sorgente, sorgente2, ottenuto);

        sorgente2 = "operativi";
        previsto = "../operativi/vaadflow14/src/main/java/it/algos/vaadflow14/backend/packages/crono/giorno";
        ottenuto = service.findPathBreve(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);
        printPath(sorgente, sorgente2, ottenuto);

        sorgente2 = "vaadflow14";
        previsto = "../vaadflow14/backend/packages/crono/giorno";
        ottenuto = service.findPathBreve(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);
        printPath(sorgente, sorgente2, ottenuto);

        sorgente2 = "packages";
        previsto = "../packages/crono/giorno";
        ottenuto = service.findPathBreve(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);
        printPath(sorgente, sorgente2, ottenuto);
    }

    @Test
    @Order(34)
    @DisplayName("34 - estraeDirectoryFinale")
    public void estraeDirectoryFinale() {
        sorgente = "/Users/gac/Documents/IdeaProjects/operativi/vaadflow/";
        previsto = "vaadflow/";
        ottenuto = service.estraeDirectoryFinale(sorgente);
        assertEquals(previsto, ottenuto);
    }

    @Test
    @Order(35)
    @DisplayName("35 - recursionSubPathNames")
    public void recursionSubPathNames() {
        List<String> lista = null;
        String tag = "src/main/java/it/algos/vaadflow14/backend/packages";
        File unaDirectory = new File(tag);
        String pathName = unaDirectory.getAbsolutePath();
        Path path = Paths.get(unaDirectory.getAbsolutePath());

        try {
            lista = service.recursionSubPathNames(path);
        } catch (Exception unErrore) {
            loggerService.warn(unErrore, this.getClass(), "recursionSubPathNames");
        }

        assertTrue(arrayService.isAllValid(lista));
        System.out.println(VUOTA);
        System.out.println("recursionSubPathNames");
        System.out.println("Ci sono " + lista.size() + " elementi misti files/directories");
        System.out.println(VUOTA);
        for (String name : lista) {
            System.out.println(name);
        }
    }

    @Test
    @Order(36)
    @DisplayName("36 - getAllSubPathFiles")
    public void getAllSubPathFiles() {
        List<String> lista = null;
        String tag = "src/main/java/it/algos/vaadflow14/backend/packages";
        File unaDirectory = new File(tag);
        String pathName = unaDirectory.getAbsolutePath();
        try {
            lista = service.getAllSubPathFiles(pathName);
        } catch (Exception unErrore) {
        }
        assertTrue(arrayService.isAllValid(lista));
        System.out.println(VUOTA);
        System.out.println("getAllSubPathFiles");
        System.out.println("Ci sono " + lista.size() + " files di vario tipo");
        System.out.println(VUOTA);
        for (String name : lista) {
            System.out.println(name);
        }
    }

    @Test
    @Order(37)
    @DisplayName("37 - getAllSubFilesJava")
    public void getAllSubFilesJava() {
        List<String> lista = null;
        String tag = "src/main/java/it/algos/vaadflow14/backend/packages";
        File unaDirectory = new File(tag);
        String pathName = unaDirectory.getAbsolutePath();

        try {
            lista = service.getAllSubFilesJava(pathName);
        } catch (Exception unErrore) {
        }

        assertTrue(arrayService.isAllValid(lista));
        System.out.println(VUOTA);
        System.out.println("getAllSubFilesJava");
        System.out.println("Ci sono " + lista.size() + " files di tipo Java");
        System.out.println(VUOTA);
        for (String name : lista) {
            System.out.println(name);
        }
    }

    @Test
    @Order(38)
    @DisplayName("38 - getAllSubFilesEntity")
    public void getAllSubFilesEntity() {
        List<String> lista = null;
        String tag = "src/main/java/it/algos/vaadflow14/backend/packages";
        File unaDirectory = new File(tag);
        String pathName = unaDirectory.getAbsolutePath();
        try {
            lista = service.getAllSubFilesEntity(pathName);
        } catch (Exception unErrore) {
        }

        assertTrue(arrayService.isAllValid(lista));
        System.out.println(VUOTA);
        System.out.println("getAllSubFilesEntity");
        System.out.println("Ci sono " + lista.size() + " files di tipo AEntity nel modulo Vaadflow14");
        System.out.println(VUOTA);
        for (String name : lista) {
            System.out.println(name);
        }
    }


    @Test
    @Order(39)
    @DisplayName("39 - estraeClasseFinale")
    public void estraeClasseFinale() {
        sorgente = "/Users/gac/Documents/IdeaProjects/operativi/vaadflow/";
        previsto = "vaadflow";
        ottenuto = service.estraeClasseFinale(sorgente);
        assertEquals(previsto, ottenuto);

        sorgente = "it.algos.vaadflow14.backend.packages.geografica.stato.Stato";
        previsto = "Stato";
        ottenuto = service.estraeClasseFinale(sorgente);
        assertEquals(previsto, ottenuto);

        sorgente = "/Users/gac/Documents/IdeaProjects/untitled/";
        previsto = "untitled";
        ottenuto = service.estraeClasseFinale(sorgente);
        assertEquals(previsto, ottenuto);
    }

    @Test
    @Order(40)
    @DisplayName("40 - findPathBreve")
    public void findPathBreve() {
        sorgente = PATH;
        System.out.println("findPathBreve con la directory COMPRESA");
        System.out.println(VUOTA);

        sorgente2 = "mario";
        previsto = sorgente;
        ottenuto = service.findPathBreve(sorgente, sorgente2);
        assertTrue(textService.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        System.out.println(VUOTA);
        System.out.println(sorgente);
        System.out.println(sorgente2);
        System.out.println(ottenuto);

        sorgente2 = "operativi";
        previsto = "../operativi/vaadflow14/src/main/java/it/algos/vaadflow14/wizard";
        ottenuto = service.findPathBreve(sorgente, sorgente2);
        assertTrue(textService.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        System.out.println(VUOTA);
        System.out.println(sorgente);
        System.out.println(sorgente2);
        System.out.println(ottenuto);

        sorgente2 = "it";
        previsto = "../it/algos/vaadflow14/wizard";
        ottenuto = service.findPathBreve(sorgente, sorgente2);
        assertTrue(textService.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        System.out.println(VUOTA);
        System.out.println(sorgente);
        System.out.println(sorgente2);
        System.out.println(ottenuto);
    }


    @Test
    @Order(41)
    @DisplayName("41 - findPathBreveDa")
    public void findPathBreveDa() {
        sorgente = PATH;
        System.out.println("findPathBreve con la directory ESCLUSA");
        System.out.println(VUOTA);

        sorgente2 = "mario";
        previsto = sorgente;
        ottenuto = service.findPathBreveDa(sorgente, sorgente2);
        assertTrue(textService.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        System.out.println(VUOTA);
        System.out.println(sorgente);
        System.out.println(sorgente2);
        System.out.println(ottenuto);

        sorgente2 = "operativi";
        previsto = "../vaadflow14/src/main/java/it/algos/vaadflow14/wizard";
        ottenuto = service.findPathBreveDa(sorgente, sorgente2);
        assertTrue(textService.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        System.out.println(VUOTA);
        System.out.println(sorgente);
        System.out.println(sorgente2);
        System.out.println(ottenuto);

        sorgente2 = "it";
        previsto = "../algos/vaadflow14/wizard";
        ottenuto = service.findPathBreveDa(sorgente, sorgente2);
        assertTrue(textService.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        System.out.println(VUOTA);
        System.out.println(sorgente);
        System.out.println(sorgente2);
        System.out.println(ottenuto);
    }


    @Test
    @Order(42)
    @DisplayName("42 - getPathModuloPackageFiles")
    public void getPathModuloPackageFiles() {
        System.out.println("42 - getPathModuloPackageFiles");
        System.out.println("path completo di ogni files (Entity, List, Form, Service) della directory 'package' e delle sue subdirectories");

        System.out.println(VUOTA);
        System.out.println("modulo base simple");
        try {
            listaStr = service.getPathModuloPackageFiles("simple");
        } catch (Exception unErrore) {
        }
        print(listaStr);

        System.out.println(VUOTA);
        System.out.println("modulo base vaadflow14");
        try {
            listaStr = service.getPathModuloPackageFiles("vaadflow14");
        } catch (Exception unErrore) {
        }
        print(listaStr);
    }


    @Test
    @Order(43)
    @DisplayName("43 - getPathBreveAllPackageFiles")
    public void getPathBreveAllPackageFiles() {
        System.out.println("43 - getPathBreveAllPackageFiles");
        System.out.println("path completo di ogni files (Entity, List, Form, Service) della directory 'package' e delle sue subdirectories");
        System.out.println("il progetto corrente viene simulato regolando (provvisoriamente) la property statica FlowVar.projectNameDirectoryIdea");
        System.out.println(VUOTA);

        try {
            listaStr = service.getPathBreveAllPackageFiles();
        } catch (Exception unErrore) {
        }
        print(listaStr);
    }


    @Test
    @Order(44)
    @DisplayName("44 - getPath")
    public void getPath() {
        System.out.println("44 - getPath");
        System.out.println("path completo di una singola classe esistente nella directory 'package' delle sue subdirectories");
        System.out.println(VUOTA);

        sorgente = "Via";
        try {
            ottenuto = service.getPath(sorgente);
        } catch (Exception unErrore) {
        }
        System.out.print(sorgente);
        System.out.print(FORWARD);
        System.out.println(ottenuto);

        sorgente = "Bolla";
        try {
            ottenuto = service.getPath(sorgente);
        } catch (Exception unErrore) {
        }
        System.out.print(sorgente);
        System.out.print(FORWARD);
        System.out.println(ottenuto);

        sorgente = "Mese";
        try {
            ottenuto = service.getPath(sorgente);
        } catch (Exception unErrore) {
        }
        System.out.print(sorgente);
        System.out.print(FORWARD);
        System.out.println(ottenuto);

        sorgente = "StatoLogicList";
        try {
            ottenuto = service.getPath(sorgente);
        } catch (Exception unErrore) {
        }
        System.out.print(sorgente);
        System.out.print(FORWARD);
        System.out.println(ottenuto);
    }


    @Test
    @Order(45)
    @DisplayName("45 - getCanonicalModuloPackageFiles")
    public void getCanonicalModuloPackageFiles() {
        System.out.println("45 - getCanonicalModuloPackageFiles");
        System.out.println("canonicalName di ogni files (Entity, List, Form, Service) della directory 'package' e delle sue subdirectories");

        System.out.println(VUOTA);
        System.out.println("modulo base simple");
        try {
            listaStr = service.getCanonicalModuloPackageFiles("simple");
        } catch (Exception unErrore) {
        }
        print(listaStr);

        System.out.println(VUOTA);
        System.out.println("modulo base vaadflow14");
        try {
            listaStr = service.getCanonicalModuloPackageFiles("vaadflow14");
        } catch (Exception unErrore) {
        }
        print(listaStr);
    }


    @Test
    @Order(46)
    @DisplayName("46 - getCanonicalAllPackageFiles")
    public void getCanonicalAllPackageFiles() {
        System.out.println("46 - getCanonicalAllPackageFiles");
        System.out.println("canonicalName di ogni files (Entity, List, Form, Service) della directory 'package' e delle sue subdirectories");
        System.out.println("il progetto corrente viene simulato regolando (provvisoriamente) la property statica FlowVar.projectNameDirectoryIdea");
        System.out.println(VUOTA);

        try {
            listaStr = service.getCanonicalAllPackageFiles();
        } catch (Exception unErrore) {
        }
        print(listaStr);
    }

    @ParameterizedTest
    @MethodSource(value = "SIMPLE")
    @EmptySource
    @Order(47)
    @DisplayName("47 - getCanonicalName from simpleName")
    /*
      47 - getCanonicalName from simpleName
      canonicalName di una singola classe esistente nella directory 'package' delle sue subdirectories
      ricerca partendo dal simpleName
     */
    void getCanonicalSimpleName(String simpleName) {
        sorgente = simpleName;
        try {
            ottenuto = service.getCanonicalName(sorgente);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        printOttenuto(sorgente, ottenuto);
        System.out.println(VUOTA);
    }

    @ParameterizedTest
    @MethodSource(value = "PATH")
    @EmptySource
    @Order(48)
    @DisplayName("48 - getCanonicalName from pathName")
    /*
      48 - getCanonicalName from pathname
      canonicalName di una singola classe esistente nella directory 'package' delle sue subdirectories
      ricerca partendo dal canonicalName
     */
    void getCanonicalPathName(String pathname) {
        sorgente = pathname;
        try {
            ottenuto = service.getCanonicalName(sorgente);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        printOttenuto(sorgente, ottenuto);
        System.out.println(VUOTA);
    }

    @ParameterizedTest
    @MethodSource(value = "CANONICAL")
    @EmptySource
    @Order(49)
    @DisplayName("49 - getCanonicalName from canonicalName")
    /*
      49 - getCanonicalName from canonicalName
      canonicalName di una singola classe esistente nella directory 'package' delle sue subdirectories
      ricerca partendo dal canonicalName
     */
    void getCanonicalName(String canonicalName) {
        sorgente = canonicalName;
        try {
            ottenuto = service.getCanonicalName(sorgente);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        printOttenuto(sorgente, ottenuto);
        System.out.println(VUOTA);
    }


    private void printPath(String path, String dir, String pathOttenuto) {
        System.out.println("Path completo: " + path);
        System.out.println("Directory interessata: " + dir);
        System.out.println("Path che la contiene:  " + pathOttenuto);
        System.out.println(VUOTA);
    }

    /**
     * Creo un file e lo cancello subito dopo
     * Se esiste già, lo cancello al volo PRIMA di crearlo
     */
    private void creaFileValido(String nomeCompletoFile) {
        System.out.println("");

        //--controlla che non esista prima di crearlo
        service.deleteFile(nomeCompletoFile);

        //--creazione da testare
        ottenutoDaNome = service.creaFileStr(nomeCompletoFile);
        assertTrue(service.creaFile(nomeCompletoFile));
        assertEquals(VUOTA, ottenutoDaNome);
        System.out.println("Risposta" + CREA_FILE + "ottenutoDaNome: " + nomeCompletoFile + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unFile = new File(nomeCompletoFile);
        assertNotNull(unFile);
        ottenutoDaFile = service.creaFileStr(unFile);
        assertTrue(service.creaFile(unFile));
        assertEquals(VUOTA, ottenutoDaFile);
        System.out.println("Risposta" + CREA_FILE + "ottenutoDaFile: " + nomeCompletoFile + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        //--controlla dopo averlo creato - Esiste
        assertTrue(service.isEsisteFile(nomeCompletoFile));

        //--se è stato creato il file che qui viene cancellato,
        //--devo aggiungere alla lista la directory parent per poterla cancellare alla fine del test
        if (service.isEsisteFile(nomeCompletoFile)) {
            listaDirectory.add(new File(unFile.getParent()));
        }

        //--cancellazione
        service.deleteFile(nomeCompletoFile);

        //--controlla dopo averlo cancellato - Non esiste
        assertFalse(service.isEsisteFile(nomeCompletoFile));
    }


    /**
     * Crea al volo una directory (probabilmente) valida e la cancella subito dopo
     */
    private void creaDirectoryValida(String nomeCompletoDirectory) {
        System.out.println(VUOTA);

        //--creazione da testare
        ottenutoDaNome = service.creaDirectoryStr(nomeCompletoDirectory);
        assertTrue(service.creaDirectory(nomeCompletoDirectory));
        assertEquals(VUOTA, ottenutoDaNome);
        System.out.println("Risposta" + CREA_DIRECTORY + "ottenutoDaNome: " + nomeCompletoDirectory + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unaDirectory = new File(nomeCompletoDirectory);
        assertNotNull(unaDirectory);
        ottenutoDaFile = service.creaDirectoryStr(unaDirectory);
        assertTrue(service.creaDirectory(unaDirectory));
        assertEquals(VUOTA, ottenutoDaFile);
        System.out.println("Risposta" + CREA_DIRECTORY + "ottenutoDaFile: " + nomeCompletoDirectory + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        //--controlla dopo averlo creato - Esiste
        assertTrue(service.isEsisteDirectory(nomeCompletoDirectory));

        //--se è stato creato la directory, la aggiungo alla lista in modo da poterla camncellare alla fine del test
        if (!listaDirectory.contains(unaDirectory)) {
            listaDirectory.add(unaDirectory);
        }
    }


    /**
     * Cancello un file e lo ricreo subito dopo
     * Se non esiste, lo creo al volo PRIMA di cancellarlo
     */
    private void deleFileValido(String nomeCompletoFile) {
        System.out.println("");

        //--controlla che esista prima di cancellarlo
        service.creaFile(nomeCompletoFile);

        //--cancellazione da testare
        ottenutoDaNome = service.deleteFileStr(nomeCompletoFile);
        service.creaFile(nomeCompletoFile);
        assertTrue(service.deleteFile(nomeCompletoFile));
        assertEquals(VUOTA, ottenutoDaNome);
        System.out.println("Risposta" + DELETE_FILE + "ottenutoDaNome: " + nomeCompletoFile + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        service.creaFile(nomeCompletoFile);
        unFile = new File(nomeCompletoFile);
        assertNotNull(unFile);
        ottenutoDaFile = service.deleteFileStr(unFile);
        service.creaFile(nomeCompletoFile);
        assertTrue(service.deleteFile(unFile));
        assertEquals(VUOTA, ottenutoDaFile);
        System.out.println("Risposta" + DELETE_FILE + "ottenutoDaFile: " + nomeCompletoFile + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        //--controlla dopo averlo cancellato - Non esiste
        assertFalse(service.isEsisteFile(nomeCompletoFile));
    }


    /**
     * Cancello una directory e la ricreo subito dopo
     * Se non esiste, la creo al volo PRIMA di cancellarla
     */
    private void deleteDirectoryValida(String nomeCompletoDirectory) {
        System.out.println("");

        //--controlla che esista prima di cancellarla
        service.creaDirectory(nomeCompletoDirectory);

        //--cancellazione da testare
        ottenutoDaNome = service.deleteDirectoryStr(nomeCompletoDirectory);
        service.creaDirectory(nomeCompletoDirectory);
        assertTrue(service.deleteDirectory(nomeCompletoDirectory));
        assertEquals(VUOTA, ottenutoDaNome);
        System.out.println("Risposta" + DELETE_DIRECTORY + "ottenutoDaNome: " + nomeCompletoDirectory + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        service.creaDirectory(nomeCompletoDirectory);
        unFile = new File(nomeCompletoDirectory);
        assertNotNull(unFile);
        ottenutoDaFile = service.deleteDirectoryStr(unFile);
        service.creaDirectory(nomeCompletoDirectory);
        assertTrue(service.deleteDirectory(unFile));
        assertEquals(VUOTA, ottenutoDaFile);
        System.out.println("Risposta" + DELETE_DIRECTORY + "ottenutoDaFile: " + nomeCompletoDirectory + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        //--controlla dopo averlo cancellato - Non esiste
        assertFalse(service.isEsisteDirectory(nomeCompletoDirectory));
    }


    private void creaCartelleTemporanee(String srcPath, String destPath, String dirUno, String dirDue) {
        String primo = "PrimoFileCheResta.txx";
        String secondo = "SecondoFileCheResta.txx";
        String terzo = "TerzoFileVariabile.txx";
        String quarto = "QuartoIncerto.txx";
        String fileConTesto = "FileConTesto.txt";
        String dirCopiata = "VieneCopiata";
        String dirRimane = "Rimane";
        String fileJava = "Entity.java";

        String emptyDirCopiata = srcPath + dirCopiata;
        String srcDirectoryUno = srcPath + dirUno;
        String srcDirUnoFileUno = srcDirectoryUno + primo;
        String srcDirUnoFileDue = srcDirectoryUno + secondo;
        String srcDirUnoFileTre = srcDirectoryUno + terzo;
        String srcDirectoryDue = srcPath + dirDue;
        String srcDirDueFileUno = srcDirectoryDue + primo;
        String srcDirDueFileDue = srcDirectoryDue + secondo;
        String srcDirDueFileQuattro = srcDirectoryDue + quarto;

        String emptyDirRimane = destPath + dirRimane;
        String destDirectoryUno = destPath + dirUno;
        String destDirUnoFileUno = destDirectoryUno + primo;
        String destDirUnoFileDue = destDirectoryUno + secondo;
        String destDirUnoFileQuattro = destDirectoryUno + quarto;
        String destDirectoryDue = destPath + dirDue;
        String destDirDueFileUno = destDirectoryDue + primo;
        String destDirDueFileDue = destDirectoryDue + secondo;
        String destDirDueFileTre = destDirectoryDue + terzo;

        assertTrue(service.creaDirectory(emptyDirCopiata));
        assertTrue(service.creaFile(srcDirUnoFileUno));
        assertTrue(service.creaFile(srcDirUnoFileDue));
        assertTrue(service.creaFile(srcDirUnoFileTre));
        assertTrue(service.creaFile(srcDirDueFileUno));
        assertTrue(service.creaFile(srcDirDueFileDue));
        assertTrue(service.creaFile(srcDirDueFileQuattro));

        assertTrue(service.creaDirectory(emptyDirRimane));
        assertTrue(service.creaFile(destDirUnoFileUno));
        assertTrue(service.creaFile(destDirUnoFileDue));
        assertTrue(service.creaFile(destDirUnoFileQuattro));
        assertTrue(service.creaFile(destDirDueFileUno));
        assertTrue(service.creaFile(destDirDueFileDue));
        assertTrue(service.creaFile(destDirDueFileTre));

        //--aggiunge nella directory sorgente un file di testo contenete effettivamente del testo
        assertTrue(service.creaFile(srcPath + dirUno + fileConTesto));
        service.sovraScriveFile(srcPath + dirUno + fileConTesto, "Questo testo verrà copiato");
        assertTrue(service.creaFile(destPath + dirUno + fileConTesto));
        service.sovraScriveFile(destPath + dirUno + fileConTesto, "Questo testo verrà cancellato");

    }


    /**
     * Cancellazione finale di tutti i files/directories creati in questo test <br>
     */
    private void fine() {
        try {
            FileUtils.forceDelete(new File(PATH_DIRECTORY_TEST));
        } catch (Exception unErrore) {
        }
    }

}