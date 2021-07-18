package it.algos.unit;

import it.algos.test.*;
import it.algos.vaadflow14.backend.application.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.enumeration.*;
import static it.algos.vaadflow14.backend.service.FileService.*;
import org.apache.tomcat.util.http.fileupload.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: dom, 28-giu-2020
 * Time: 15:11
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

    private static String PATH_DIRECTORY_NON_ESISTENTE = PATH_DIRECTORY_TEST + "Genova/";

    private static String PATH_DIRECTORY_DA_COPIARE = PATH_DIRECTORY_TEST + "NuovaDirectory/";

    private static String PATH_DIRECTORY_MANCANTE = PATH_DIRECTORY_TEST + "CartellaCopiata/";

    private static String PATH_FILE_UNO = PATH_DIRECTORY_TEST + "Pluto.rtf";

    private static String PATH_FILE_DUE = PATH_DIRECTORY_TEST + "Secondo.rtf";

    private static String PATH_FILE_TRE = PATH_DIRECTORY_TRE + "/Topolino.txt";

    private static String PATH_FILE_QUATTRO = PATH_DIRECTORY_TRE + "/Paperino.txt";

    private static String PATH_FILE_ESISTENTE_CON_MAIUSCOLA_SBAGLIATA = "/Users/gac/Desktop/test/pluto.rtf";

    private static String PATH_FILE_NO_SUFFIX = PATH_DIRECTORY_TEST + "Topolino";

    private static String PATH_FILE_NON_ESISTENTE = PATH_DIRECTORY_TEST + "Topolino.txt";

    private static String PATH_FILE_AGGIUNTO = PATH_DIRECTORY_MANCANTE + "TerzaPossibilita.htm";

    private static String PATH_FILE_AGGIUNTO_TRE = PATH_DIRECTORY_TRE + "FileSorgenteAggiunto.ccs";

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
        if (array.isAllValid(listaDirectory)) {
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
        if (array.isAllValid(listaFile)) {
            for (File unFile : listaFile) {
                try { // prova ad eseguire il codice
                    unFile.createNewFile();
                } catch (Exception unErrore) { // intercetta l'errore
                    if (file.creaDirectoryParentAndFile(unFile).equals(VUOTA)) {
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
        ottenutoDaNome = file.isEsisteFileStr((String) null);
        assertFalse(file.isEsisteFile((String) null));
        assertEquals(PATH_NULLO, ottenutoDaNome);
        System.out.println("Risposta" + ESISTE_FILE + "ottenutoDaNome: " + nomeCompletoFile + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        ottenutoDaFile = file.isEsisteFileStr((File) null);
        assertFalse(file.isEsisteFile((File) null));
        assertEquals(PARAMETRO_NULLO, ottenutoDaFile);
        System.out.println("Risposta" + ESISTE_FILE + "ottenutoDaFile: " + nomeCompletoFile + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoFile = VUOTA;
        ottenutoDaNome = file.isEsisteFileStr(nomeCompletoFile);
        assertFalse(file.isEsisteFile(nomeCompletoFile));
        assertEquals(PATH_NULLO, ottenutoDaNome);
        System.out.println("Risposta" + ESISTE_FILE + "ottenutoDaNome: " + nomeCompletoFile + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unFile = new File(nomeCompletoFile);
        assertNotNull(unFile);
        ottenutoDaFile = file.isEsisteFileStr(unFile);
        assertFalse(file.isEsisteFile(unFile));
        assertEquals(PATH_NULLO, ottenutoDaFile);
        System.out.println("Risposta" + ESISTE_FILE + "ottenutoDaFile: " + nomeCompletoFile + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoFile = "nonEsiste";
        ottenutoDaNome = file.isEsisteFileStr(nomeCompletoFile);
        assertFalse(file.isEsisteFile(nomeCompletoFile));
        assertEquals(PATH_NOT_ABSOLUTE, ottenutoDaNome);
        System.out.println("Risposta" + ESISTE_FILE + "ottenutoDaNome: " + nomeCompletoFile + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unFile = new File(nomeCompletoFile);
        assertNotNull(unFile);
        ottenutoDaFile = file.isEsisteFileStr(unFile);
        assertFalse(file.isEsisteFile(unFile));
        assertEquals(PATH_NOT_ABSOLUTE, ottenutoDaFile);
        System.out.println("Risposta" + ESISTE_FILE + "ottenutoDaFile: " + nomeCompletoFile + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoFile = PATH_FILE_NO_SUFFIX;
        ottenutoDaNome = file.isEsisteFileStr(nomeCompletoFile);
        assertFalse(file.isEsisteFile(nomeCompletoFile));
        assertEquals(PATH_SENZA_SUFFIX, ottenutoDaNome);
        System.out.println("Risposta" + ESISTE_FILE + "ottenutoDaNome: " + nomeCompletoFile + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unFile = new File(nomeCompletoFile);
        assertNotNull(unFile);
        ottenutoDaFile = file.isEsisteFileStr(unFile);
        assertFalse(file.isEsisteFile(unFile));
        assertEquals(PATH_SENZA_SUFFIX, ottenutoDaFile);
        System.out.println("Risposta" + ESISTE_FILE + "ottenutoDaFile: " + nomeCompletoFile + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoFile = PATH_FILE_NON_ESISTENTE;
        ottenutoDaNome = file.isEsisteFileStr(nomeCompletoFile);
        assertFalse(file.isEsisteFile(nomeCompletoFile));
        assertEquals(NON_ESISTE_FILE, ottenutoDaNome);
        System.out.println("Risposta" + ESISTE_FILE + "ottenutoDaNome: " + nomeCompletoFile + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unFile = new File(nomeCompletoFile);
        assertNotNull(unFile);
        ottenutoDaFile = file.isEsisteFileStr(unFile);
        assertFalse(file.isEsisteFile(unFile));
        assertEquals(NON_ESISTE_FILE, ottenutoDaFile);
        System.out.println("Risposta" + ESISTE_FILE + "ottenutoDaFile: " + nomeCompletoFile + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoFile = PATH_FILE_NO_PATH;
        ottenutoDaNome = file.isEsisteFileStr(nomeCompletoFile);
        assertFalse(file.isEsisteFile(nomeCompletoFile));
        assertEquals(PATH_NOT_ABSOLUTE, ottenutoDaNome);
        System.out.println("Risposta" + ESISTE_FILE + "ottenutoDaNome: " + nomeCompletoFile + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unFile = new File(nomeCompletoFile);
        assertNotNull(unFile);
        ottenutoDaFile = file.isEsisteFileStr(unFile);
        assertFalse(file.isEsisteFile(unFile));
        assertEquals(PATH_NOT_ABSOLUTE, ottenutoDaFile);
        System.out.println("Risposta" + ESISTE_FILE + "ottenutoDaFile: " + nomeCompletoFile + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoFile = PATH_DIRECTORY_UNO;
        ottenutoDaNome = file.isEsisteFileStr(nomeCompletoFile);
        assertFalse(file.isEsisteFile(nomeCompletoFile));
        assertEquals(NON_E_FILE, ottenutoDaNome);
        System.out.println("Risposta" + ESISTE_FILE + "ottenutoDaNome: " + nomeCompletoFile + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unFile = new File(nomeCompletoFile);
        assertNotNull(unFile);
        ottenutoDaFile = file.isEsisteFileStr(unFile);
        assertFalse(file.isEsisteFile(unFile));
        assertEquals(NON_E_FILE, ottenutoDaFile);
        System.out.println("Risposta" + ESISTE_FILE + "ottenutoDaFile: " + nomeCompletoFile + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoFile = PATH_FILE_UNO;
        ottenutoDaNome = file.isEsisteFileStr(nomeCompletoFile);
        assertTrue(file.isEsisteFile(nomeCompletoFile));
        assertEquals(VUOTA, ottenutoDaNome);
        System.out.println("Risposta" + ESISTE_FILE + "ottenutoDaNome: " + nomeCompletoFile + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unFile = new File(nomeCompletoFile);
        assertNotNull(unFile);
        ottenutoDaFile = file.isEsisteFileStr(unFile);
        assertTrue(file.isEsisteFile(unFile));
        assertEquals(VUOTA, ottenutoDaFile);
        System.out.println("Risposta" + ESISTE_FILE + "ottenutoDaFile: " + nomeCompletoFile + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoFile = PATH_FILE_ESISTENTE_CON_MAIUSCOLA_SBAGLIATA;
        ottenutoDaNome = file.isEsisteFileStr(nomeCompletoFile);
        assertTrue(file.isEsisteFile(nomeCompletoFile));
        assertEquals(VUOTA, ottenutoDaNome);
        System.out.println("Risposta" + ESISTE_FILE + "ottenutoDaNome: " + nomeCompletoFile + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unFile = new File(nomeCompletoFile);
        assertNotNull(unFile);
        ottenutoDaFile = file.isEsisteFileStr(unFile);
        assertTrue(file.isEsisteFile(unFile));
        assertEquals(VUOTA, ottenutoDaFile);
        System.out.println("Risposta" + ESISTE_FILE + "ottenutoDaFile: " + nomeCompletoFile + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));
    }


    @Test
    @Order(3)
    @DisplayName("3 - isEsisteDirectory")
    public void isEsisteDirectory() {
        nomeCompletoDirectory = "null";
        ottenutoDaNome = file.isEsisteDirectoryStr((String) null);
        assertFalse(file.isEsisteDirectory((String) null));
        assertEquals(PATH_NULLO, ottenutoDaNome);
        System.out.println("Risposta" + ESISTE_DIRECTORY + "ottenutoDaNome: " + nomeCompletoDirectory + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        ottenutoDaFile = file.isEsisteDirectoryStr((File) null);
        assertFalse(file.isEsisteDirectory((File) null));
        assertEquals(PARAMETRO_NULLO, ottenutoDaFile);
        System.out.println("Risposta" + ESISTE_DIRECTORY + "ottenutoDaFile: " + nomeCompletoDirectory + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoDirectory = VUOTA;
        ottenutoDaNome = file.isEsisteDirectoryStr(nomeCompletoDirectory);
        assertFalse(file.isEsisteDirectory(nomeCompletoDirectory));
        assertEquals(PATH_NULLO, ottenutoDaNome);
        System.out.println("Risposta" + ESISTE_DIRECTORY + "ottenutoDaNome: " + nomeCompletoDirectory + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unaDirectory = new File(nomeCompletoDirectory);
        assertNotNull(unaDirectory);
        ottenutoDaFile = file.isEsisteDirectoryStr((File) null);
        assertFalse(file.isEsisteDirectory((File) null));
        assertEquals(PARAMETRO_NULLO, ottenutoDaFile);
        System.out.println("Risposta" + ESISTE_DIRECTORY + "ottenutoDaFile: " + nomeCompletoDirectory + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoDirectory = "nonEsiste";
        ottenutoDaNome = file.isEsisteDirectoryStr(nomeCompletoDirectory);
        assertFalse(file.isEsisteDirectory(nomeCompletoDirectory));
        assertEquals(PATH_NOT_ABSOLUTE, ottenutoDaNome);
        System.out.println("Risposta" + ESISTE_DIRECTORY + "ottenutoDaNome: " + nomeCompletoDirectory + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unaDirectory = new File(nomeCompletoDirectory);
        assertNotNull(unaDirectory);
        ottenutoDaFile = file.isEsisteDirectoryStr(unaDirectory);
        assertFalse(file.isEsisteDirectory(unaDirectory));
        assertEquals(PATH_NOT_ABSOLUTE, ottenutoDaFile);
        System.out.println("Risposta" + ESISTE_DIRECTORY + "ottenutoDaFile: " + nomeCompletoDirectory + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoDirectory = PATH_DIRECTORY_NON_ESISTENTE;
        ottenutoDaNome = file.isEsisteDirectoryStr(nomeCompletoDirectory);
        assertFalse(file.isEsisteDirectory(nomeCompletoDirectory));
        assertEquals(NON_ESISTE_DIRECTORY, ottenutoDaNome);
        System.out.println("Risposta" + ESISTE_DIRECTORY + "ottenutoDaNome: " + nomeCompletoDirectory + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unaDirectory = new File(nomeCompletoDirectory);
        assertNotNull(unaDirectory);
        ottenutoDaFile = file.isEsisteDirectoryStr(unaDirectory);
        assertFalse(file.isEsisteDirectory(unaDirectory));
        assertEquals(NON_ESISTE_DIRECTORY, ottenutoDaFile);
        System.out.println("Risposta" + ESISTE_DIRECTORY + "ottenutoDaFile: " + nomeCompletoDirectory + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoDirectory = PATH_FILE_UNO;
        ottenutoDaNome = file.isEsisteDirectoryStr(nomeCompletoDirectory);
        assertFalse(file.isEsisteDirectory(nomeCompletoDirectory));
        assertEquals(NON_E_DIRECTORY, ottenutoDaNome);
        System.out.println("Risposta" + ESISTE_DIRECTORY + "ottenutoDaNome: " + nomeCompletoDirectory + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unaDirectory = new File(nomeCompletoDirectory);
        assertNotNull(unaDirectory);
        ottenutoDaFile = file.isEsisteDirectoryStr(unaDirectory);
        assertFalse(file.isEsisteDirectory(unaDirectory));
        assertEquals(NON_E_DIRECTORY, ottenutoDaFile);
        System.out.println("Risposta" + ESISTE_DIRECTORY + "ottenutoDaFile: " + nomeCompletoDirectory + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoDirectory = PATH_DIRECTORY_NO_PATH;
        ottenutoDaNome = file.isEsisteDirectoryStr(nomeCompletoDirectory);
        assertFalse(file.isEsisteDirectory(nomeCompletoDirectory));
        assertEquals(PATH_NOT_ABSOLUTE, ottenutoDaNome);
        System.out.println("Risposta" + ESISTE_DIRECTORY + "ottenutoDaNome: " + nomeCompletoDirectory + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unaDirectory = new File(nomeCompletoDirectory);
        assertNotNull(unaDirectory);
        ottenutoDaFile = file.isEsisteDirectoryStr(unaDirectory);
        assertFalse(file.isEsisteDirectory(unaDirectory));
        assertEquals(PATH_NOT_ABSOLUTE, ottenutoDaFile);
        System.out.println("Risposta" + ESISTE_DIRECTORY + "ottenutoDaFile: " + nomeCompletoDirectory + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoDirectory = PATH_DIRECTORY_UNO;
        ottenutoDaNome = file.isEsisteDirectoryStr(nomeCompletoDirectory);
        assertTrue(file.isEsisteDirectory(nomeCompletoDirectory));
        assertEquals(VUOTA, ottenutoDaNome);
        System.out.println("Risposta" + ESISTE_DIRECTORY + "ottenutoDaNome: " + nomeCompletoDirectory + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unaDirectory = new File(nomeCompletoDirectory);
        assertNotNull(unaDirectory);
        ottenutoDaFile = file.isEsisteDirectoryStr(unaDirectory);
        assertTrue(file.isEsisteDirectory(unaDirectory));
        assertEquals(VUOTA, ottenutoDaFile);
        System.out.println("Risposta" + ESISTE_DIRECTORY + "ottenutoDaFile: " + nomeCompletoDirectory + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoDirectory = PATH_DIRECTORY_ESISTENTE_CON_MAIUSCOLA_SBAGLIATA;
        ottenutoDaNome = file.isEsisteDirectoryStr(nomeCompletoDirectory);
        assertTrue(file.isEsisteDirectory(nomeCompletoDirectory));
        assertEquals(VUOTA, ottenutoDaNome);
        System.out.println("Risposta" + ESISTE_DIRECTORY + "ottenutoDaNome: " + nomeCompletoDirectory + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unaDirectory = new File(nomeCompletoDirectory);
        assertNotNull(unaDirectory);
        ottenutoDaFile = file.isEsisteDirectoryStr(unaDirectory);
        assertTrue(file.isEsisteDirectory(unaDirectory));
        assertEquals(VUOTA, ottenutoDaFile);
        System.out.println("Risposta" + ESISTE_DIRECTORY + "ottenutoDaFile: " + nomeCompletoDirectory + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));
    }


    @Test
    @Order(4)
    @DisplayName("4 - creaFile")
    public void creaFile() {
        nomeCompletoFile = "null";
        ottenutoDaNome = file.creaFileStr((String) null);
        assertFalse(file.creaFile((String) null));
        assertEquals(PATH_NULLO, ottenutoDaNome);
        System.out.println("Risposta" + CREA_FILE + "ottenutoDaNome: " + nomeCompletoFile + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        ottenutoDaFile = file.creaFileStr((File) null);
        assertFalse(file.creaFile((File) null));
        assertEquals(PARAMETRO_NULLO, ottenutoDaFile);
        System.out.println("Risposta" + CREA_FILE + "ottenutoDaFile: " + nomeCompletoFile + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoFile = VUOTA;
        ottenutoDaNome = file.creaFileStr(nomeCompletoFile);
        assertFalse(file.creaFile(nomeCompletoFile));
        assertEquals(PATH_NULLO, ottenutoDaNome);
        System.out.println("Risposta" + CREA_FILE + "ottenutoDaNome: " + nomeCompletoFile + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unFile = new File(nomeCompletoFile);
        assertNotNull(unFile);
        ottenutoDaFile = file.creaFileStr(unFile);
        assertFalse(file.creaFile(unFile));
        assertEquals(PATH_NULLO, ottenutoDaFile);
        System.out.println("Risposta" + CREA_FILE + "ottenutoDaFile: " + nomeCompletoFile + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoFile = PATH_FILE_NO_SUFFIX;
        ottenutoDaNome = file.creaFileStr(nomeCompletoFile);
        assertFalse(file.creaFile(nomeCompletoFile));
        assertEquals(PATH_SENZA_SUFFIX, ottenutoDaNome);
        System.out.println("Risposta" + CREA_FILE + "ottenutoDaNome: " + nomeCompletoFile + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unFile = new File(nomeCompletoFile);
        assertNotNull(unFile);
        ottenutoDaFile = file.creaFileStr(unFile);
        assertFalse(file.creaFile(unFile));
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
        ottenutoDaNome = file.creaDirectoryStr((String) null);
        assertFalse(file.creaDirectory((String) null));
        assertEquals(PATH_NULLO, ottenutoDaNome);
        System.out.println("Risposta" + CREA_DIRECTORY + "ottenutoDaNome: " + nomeCompletoDirectory + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        ottenutoDaFile = file.creaDirectoryStr((File) null);
        assertFalse(file.creaDirectory((File) null));
        assertEquals(PARAMETRO_NULLO, ottenutoDaFile);
        System.out.println("Risposta" + CREA_DIRECTORY + "ottenutoDaFile: " + nomeCompletoDirectory + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoDirectory = VUOTA;
        ottenutoDaNome = file.creaDirectoryStr(nomeCompletoDirectory);
        assertFalse(file.creaDirectory(nomeCompletoDirectory));
        assertEquals(PATH_NULLO, ottenutoDaNome);
        System.out.println("Risposta" + CREA_DIRECTORY + "ottenutoDaNome: " + nomeCompletoDirectory + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unaDirectory = new File(nomeCompletoDirectory);
        assertNotNull(unaDirectory);
        ottenutoDaFile = file.creaDirectoryStr(unaDirectory);
        assertFalse(file.creaDirectory(unaDirectory));
        assertEquals(PATH_NULLO, ottenutoDaFile);
        System.out.println("Risposta" + CREA_DIRECTORY + "ottenutoDaFile: " + nomeCompletoDirectory + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoDirectory = "nonEsiste";
        ottenutoDaNome = file.creaDirectoryStr(nomeCompletoDirectory);
        assertFalse(file.creaDirectory(nomeCompletoDirectory));
        assertEquals(PATH_NOT_ABSOLUTE, ottenutoDaNome);
        System.out.println("Risposta" + CREA_DIRECTORY + "ottenutoDaNome: " + nomeCompletoDirectory + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unaDirectory = new File(nomeCompletoDirectory);
        assertNotNull(unaDirectory);
        ottenutoDaFile = file.creaDirectoryStr(unaDirectory);
        assertFalse(file.creaDirectory(unaDirectory));
        assertEquals(PATH_NOT_ABSOLUTE, ottenutoDaFile);
        System.out.println("Risposta" + CREA_DIRECTORY + "ottenutoDaFile: " + nomeCompletoDirectory + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoDirectory = "/Users/gac/Desktop/test/Paperino/Topolino.abc";
        ottenutoDaNome = file.creaDirectoryStr(nomeCompletoDirectory);
        assertFalse(file.creaDirectory(nomeCompletoDirectory));
        assertEquals(NON_E_DIRECTORY, ottenutoDaNome);
        System.out.println("Risposta" + CREA_DIRECTORY + "ottenutoDaNome: " + nomeCompletoDirectory + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unaDirectory = new File(nomeCompletoDirectory);
        assertNotNull(unaDirectory);
        ottenutoDaFile = file.creaDirectoryStr(unaDirectory);
        assertFalse(file.creaDirectory(unaDirectory));
        assertEquals(NON_E_DIRECTORY, ottenutoDaFile);
        System.out.println("Risposta" + CREA_DIRECTORY + "ottenutoDaFile: " + nomeCompletoDirectory + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        creaDirectoryValida(PATH_DIRECTORY_TRE);
    }


    @Test
    @Order(6)
    @DisplayName("6 - deleteFile")
    public void deleteFile() {
        nomeCompletoFile = "null";
        ottenutoDaNome = file.deleteFileStr((String) null);
        assertFalse(file.deleteFile((String) null));
        assertEquals(PATH_NULLO, ottenutoDaNome);
        System.out.println("Risposta" + DELETE_FILE + "ottenutoDaNome: " + nomeCompletoFile + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        ottenutoDaFile = file.deleteFileStr((File) null);
        assertFalse(file.deleteFile((File) null));
        assertEquals(PARAMETRO_NULLO, ottenutoDaFile);
        System.out.println("Risposta" + DELETE_FILE + "ottenutoDaFile: " + nomeCompletoFile + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoFile = VUOTA;
        ottenutoDaNome = file.deleteFileStr(nomeCompletoFile);
        assertFalse(file.deleteFile(nomeCompletoFile));
        assertEquals(PATH_NULLO, ottenutoDaNome);
        System.out.println("Risposta" + DELETE_FILE + "ottenutoDaNome: " + nomeCompletoFile + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unFile = new File(nomeCompletoFile);
        assertNotNull(unFile);
        ottenutoDaFile = file.deleteFileStr(unFile);
        assertFalse(file.deleteFile(unFile));
        assertEquals(PATH_NULLO, ottenutoDaFile);
        System.out.println("Risposta" + DELETE_FILE + "ottenutoDaFile: " + nomeCompletoFile + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoFile = "Pippo";
        ottenutoDaNome = file.deleteFileStr(nomeCompletoFile);
        assertFalse(file.deleteFile(nomeCompletoFile));
        assertEquals(PATH_NOT_ABSOLUTE, ottenutoDaNome);
        System.out.println("Risposta" + DELETE_FILE + "ottenutoDaNome: " + nomeCompletoFile + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unFile = new File(nomeCompletoFile);
        assertNotNull(unFile);
        ottenutoDaFile = file.deleteFileStr(unFile);
        assertFalse(file.deleteFile(unFile));
        assertEquals(PATH_NOT_ABSOLUTE, ottenutoDaFile);
        System.out.println("Risposta" + DELETE_FILE + "ottenutoDaFile: " + nomeCompletoFile + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoFile = PATH_FILE_NON_ESISTENTE;
        ottenutoDaNome = file.deleteFileStr(nomeCompletoFile);
        assertFalse(file.deleteFile(nomeCompletoFile));
        assertEquals(NON_ESISTE_FILE, ottenutoDaNome);
        System.out.println("Risposta" + DELETE_FILE + "ottenutoDaNome: " + nomeCompletoFile + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unFile = new File(nomeCompletoFile);
        assertNotNull(unFile);
        ottenutoDaFile = file.deleteFileStr(unFile);
        assertFalse(file.deleteFile(unFile));
        assertEquals(NON_ESISTE_FILE, ottenutoDaFile);
        System.out.println("Risposta" + DELETE_FILE + "ottenutoDaFile: " + nomeCompletoFile + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        deleFileValido(PATH_FILE_DELETE);
    }


    @Test
    @Order(7)
    @DisplayName("7 - deleteDirectory")
    public void deleteDirectory() {
        nomeCompletoDirectory = "null";
        ottenutoDaNome = file.deleteDirectoryStr((String) null);
        assertFalse(file.deleteDirectory((String) null));
        assertEquals(PATH_NULLO, ottenutoDaNome);
        System.out.println("Risposta" + DELETE_DIRECTORY + "ottenutoDaNome: " + nomeCompletoDirectory + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        ottenutoDaFile = file.deleteDirectoryStr((File) null);
        assertFalse(file.deleteDirectory((File) null));
        assertEquals(PARAMETRO_NULLO, ottenutoDaFile);
        System.out.println("Risposta" + DELETE_DIRECTORY + "ottenutoDaFile: " + nomeCompletoDirectory + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoDirectory = VUOTA;
        ottenutoDaNome = file.deleteDirectoryStr(nomeCompletoDirectory);
        assertFalse(file.deleteDirectory(nomeCompletoDirectory));
        assertEquals(PATH_NULLO, ottenutoDaNome);
        System.out.println("Risposta" + DELETE_DIRECTORY + "ottenutoDaNome: " + nomeCompletoDirectory + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unaDirectory = new File(nomeCompletoDirectory);
        assertNotNull(unaDirectory);
        ottenutoDaFile = file.deleteDirectoryStr(unaDirectory);
        assertFalse(file.deleteDirectory(unaDirectory));
        assertEquals(PATH_NULLO, ottenutoDaFile);
        System.out.println("Risposta" + DELETE_DIRECTORY + "ottenutoDaFile: " + nomeCompletoDirectory + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoDirectory = "Pippo";
        ottenutoDaNome = file.deleteDirectoryStr(nomeCompletoDirectory);
        assertFalse(file.deleteDirectory(nomeCompletoDirectory));
        assertEquals(PATH_NOT_ABSOLUTE, ottenutoDaNome);
        System.out.println("Risposta" + DELETE_DIRECTORY + "ottenutoDaNome: " + nomeCompletoDirectory + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unaDirectory = new File(nomeCompletoDirectory);
        assertNotNull(unaDirectory);
        ottenutoDaFile = file.deleteDirectoryStr(unaDirectory);
        assertFalse(file.deleteDirectory(unaDirectory));
        assertEquals(PATH_NOT_ABSOLUTE, ottenutoDaFile);
        System.out.println("Risposta" + DELETE_DIRECTORY + "ottenutoDaFile: " + nomeCompletoDirectory + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        nomeCompletoDirectory = PATH_DIRECTORY_NON_ESISTENTE;
        ottenutoDaNome = file.deleteDirectoryStr(nomeCompletoDirectory);
        assertFalse(file.deleteDirectory(nomeCompletoDirectory));
        assertEquals(NON_ESISTE_DIRECTORY, ottenutoDaNome);
        System.out.println("Risposta" + DELETE_DIRECTORY + "ottenutoDaNome: " + nomeCompletoDirectory + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unaDirectory = new File(nomeCompletoDirectory);
        assertNotNull(unaDirectory);
        ottenutoDaFile = file.deleteDirectoryStr(unaDirectory);
        assertFalse(file.deleteDirectory(unaDirectory));
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
        ottenutoBooleano = file.copyFile(srcPathNonEsistente, destPath);
        assertFalse(ottenutoBooleano);
        ottenuto = file.copyFileStr(srcPathNonEsistente, destPath);
        assertEquals(NON_ESISTE_FILE, ottenuto);

        //--esegue con destinazione GIA esistente
        ottenutoBooleano = file.copyFile(srcPath, destPathEsistente);
        assertFalse(ottenutoBooleano);
        ottenuto = file.copyFileStr(srcPathNonEsistente, destPath);
        assertEquals(NON_ESISTE_FILE, ottenuto);

        //--controllo condizioni iniziali
        assertTrue(file.isEsisteFile(srcPath));
        assertFalse(file.isEsisteFile(destPath));

        //--esegue
        ottenutoBooleano = file.copyFile(srcPath, destPath);
        assertTrue(ottenutoBooleano);

        //--controllo condizioni finali
        assertTrue(file.isEsisteFile(srcPath));
        assertTrue(file.isEsisteFile(destPath));

        //--ripristina condizioni iniziali
        assertTrue(file.deleteFile(destPath));
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
        assertFalse(file.isEsisteDirectory(srcPathNonEsistente));
        assertFalse(file.isEsisteDirectory(destPathDaSovrascrivere));
        ottenutoBooleano = file.copyDirectoryDeletingAll(srcPathNonEsistente, destPathDaSovrascrivere);
        assertFalse(ottenutoBooleano);
        assertFalse(file.isEsisteDirectory(destPathDaSovrascrivere));

        //--esegue con destinazione NON esistente
        assertTrue(file.isEsisteDirectory(srcPathValida));
        assertFalse(file.isEsisteDirectory(destPathDaSovrascrivere));
        ottenutoBooleano = file.copyDirectoryDeletingAll(srcPathValida, destPathDaSovrascrivere);
        assertTrue(ottenutoBooleano);
        assertTrue(file.isEsisteDirectory(destPathDaSovrascrivere));

        //--aggiunge nella cartella di destinazione un file per controllare che venga (come previsto) cancellato
        assertTrue(file.creaFile(destFileAggiunto));

        //--esegue con destinazione esistente
        assertTrue(file.isEsisteDirectory(destPathDaSovrascrivere));
        ottenutoBooleano = file.copyDirectoryDeletingAll(srcPathValida, destPathDaSovrascrivere);
        assertTrue(ottenutoBooleano);

        try {
            FileUtils.forceDelete(new File(destPathDaSovrascrivere));
        } catch (Exception unErrore) {
        }

        assertFalse(file.isEsisteDirectory(destPathDaSovrascrivere));
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
        assertFalse(file.isEsisteDirectory(srcPathNonEsistente));
        assertFalse(file.isEsisteDirectory(destPathDaSovrascrivere));
        ottenutoBooleano = file.copyDirectoryOnlyNotExisting(srcPathNonEsistente, destPathDaSovrascrivere);
        assertFalse(ottenutoBooleano);
        assertFalse(file.isEsisteDirectory(destPathDaSovrascrivere));

        //--esegue con destinazione NON esistente
        assertTrue(file.isEsisteDirectory(srcPathValida));
        assertFalse(file.isEsisteDirectory(destPathDaSovrascrivere));
        ottenutoBooleano = file.copyDirectoryOnlyNotExisting(srcPathValida, destPathDaSovrascrivere);
        assertTrue(ottenutoBooleano);
        assertTrue(file.isEsisteDirectory(destPathDaSovrascrivere));

        //--aggiunge nella cartella sorgente un file per controllare che NON venga aggiunto (come previsto)
        assertTrue(file.creaFile(srcFileAggiunto));

        //--esegue con destinazione esistente
        assertTrue(file.isEsisteDirectory(destPathDaSovrascrivere));
        assertTrue(file.isEsisteDirectory(destPathDaSovrascrivere));
        ottenutoBooleano = file.copyDirectoryOnlyNotExisting(srcPathValida, destPathDaSovrascrivere);
        assertFalse(ottenutoBooleano);

        try {
            FileUtils.forceDelete(new File(destPathDaSovrascrivere));
        } catch (Exception unErrore) {
        }

        assertFalse(file.isEsisteDirectory(destPathDaSovrascrivere));
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

        ottenutoBooleano = file.copyDirectoryAddingOnly(sorgente, destinazione);
        assertTrue(ottenutoBooleano);

        assertTrue(file.isEsisteDirectory(destinazione + "Rimane"));
        assertTrue(file.isEsisteDirectory(destinazione + "VieneCopiata"));

        assertTrue(file.isEsisteFile(destinazione + dirUno + "TerzoFileVariabile.txx"));
        assertTrue(file.isEsisteFile(destinazione + dirUno + "QuartoIncerto.txx"));

        assertTrue(file.isEsisteFile(destinazione + dirDue + "TerzoFileVariabile.txx"));
        assertTrue(file.isEsisteFile(destinazione + dirDue + "QuartoIncerto.txx"));

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
        assertFalse(file.isEsisteDirectory(srcPathNonEsistente));
        assertFalse(file.isEsisteDirectory(destPathDaSovrascrivere));
        ottenutoBooleano = file.copyDirectoryAddingOnly(srcPathNonEsistente, destPathDaSovrascrivere);
        assertFalse(ottenutoBooleano);
        assertFalse(file.isEsisteDirectory(destPathDaSovrascrivere));

        //--esegue con destinazione NON esistente
        assertTrue(file.isEsisteDirectory(srcPathValida));
        assertFalse(file.isEsisteDirectory(destPathDaSovrascrivere));
        ottenutoBooleano = file.copyDirectoryAddingOnly(srcPathValida, destPathDaSovrascrivere);
        assertTrue(ottenutoBooleano);
        assertTrue(file.isEsisteDirectory(destPathDaSovrascrivere));

        //--aggiunge nella cartella sorgente un file per controllare che venga (come previsto) copiato
        assertTrue(file.creaFile(srcFileAggiunto));

        //--aggiunge nella cartella di destinazione un file per controllare che venga (come previsto) mantenuto
        assertTrue(file.creaFile(destFileAggiunto));

        //--esegue con destinazione esistente
        assertTrue(file.isEsisteDirectory(destPathDaSovrascrivere));
        assertTrue(file.isEsisteDirectory(destPathDaSovrascrivere));
        ottenutoBooleano = file.copyDirectoryAddingOnly(srcPathValida, destPathDaSovrascrivere);
        assertTrue(ottenutoBooleano);

        try {
            FileUtils.forceDelete(new File(destPathDaSovrascrivere));
        } catch (Exception unErrore) {
        }

        assertFalse(file.isEsisteDirectory(destPathDaSovrascrivere));
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
        assertFalse(file.isEsisteDirectory(srcPathNonEsistente));
        assertFalse(file.isEsisteDirectory(destPathDaSovrascrivere));
        ottenutoBooleano = file.copyDirectory(AECopy.dirAddingOnly, srcPathNonEsistente, destPathDaSovrascrivere);
        assertFalse(ottenutoBooleano);
        assertFalse(file.isEsisteDirectory(destPathDaSovrascrivere));

        //--esegue con sorgente NON esistente
        //--messaggio di errore
        assertFalse(file.isEsisteDirectory(srcPathNonEsistente));
        assertFalse(file.isEsisteDirectory(destPathDaSovrascrivere));
        ottenutoBooleano = file.copyDirectory(AECopy.dirDeletingAll, srcPathNonEsistente, destPathDaSovrascrivere);
        assertFalse(ottenutoBooleano);
        assertFalse(file.isEsisteDirectory(destPathDaSovrascrivere));

        //--esegue con sorgente NON esistente
        //--messaggio di errore
        assertFalse(file.isEsisteDirectory(srcPathNonEsistente));
        assertFalse(file.isEsisteDirectory(destPathDaSovrascrivere));
        ottenutoBooleano = file.copyDirectory(AECopy.dirSoloSeNonEsiste, srcPathNonEsistente, destPathDaSovrascrivere);
        assertFalse(ottenutoBooleano);
        assertFalse(file.isEsisteDirectory(destPathDaSovrascrivere));

        //--esegue con sorgente NON esistente
        //--messaggio di errore
        assertFalse(file.isEsisteDirectory(srcPathNonEsistente));
        assertFalse(file.isEsisteDirectory(destPathDaSovrascrivere));
        ottenutoBooleano = file.copyDirectory((AECopy) null, srcPathNonEsistente, destPathDaSovrascrivere);
        assertFalse(ottenutoBooleano);
        assertFalse(file.isEsisteDirectory(destPathDaSovrascrivere));

        //--esegue con destinazione NON esistente
        assertTrue(file.isEsisteDirectory(srcPathValida));
        assertFalse(file.isEsisteDirectory(destPathDaSovrascrivere));
        ottenutoBooleano = file.copyDirectory(AECopy.dirSoloSeNonEsiste, srcPathValida, destPathDaSovrascrivere);
        assertTrue(ottenutoBooleano);
        assertTrue(file.isEsisteDirectory(destPathDaSovrascrivere));

        //--esegue con destinazione esistente
        assertTrue(file.isEsisteDirectory(srcPathValida));
        assertTrue(file.isEsisteDirectory(destPathDaSovrascrivere));
        ottenutoBooleano = file.copyDirectory(AECopy.dirDeletingAll, srcPathValida, destPathDaSovrascrivere);
        assertTrue(ottenutoBooleano);
        assertTrue(file.isEsisteDirectory(destPathDaSovrascrivere));

        //--esegue con destinazione esistente
        assertTrue(file.isEsisteDirectory(srcPathValida));
        assertTrue(file.isEsisteDirectory(destPathDaSovrascrivere));
        ottenutoBooleano = file.copyDirectory(AECopy.dirAddingOnly, srcPathValida, destPathDaSovrascrivere);
        assertTrue(ottenutoBooleano);
        assertTrue(file.isEsisteDirectory(destPathDaSovrascrivere));

        //--esegue con destinazione esistente
        assertTrue(file.isEsisteDirectory(srcPathValida));
        assertTrue(file.isEsisteDirectory(destPathDaSovrascrivere));
        ottenutoBooleano = file.copyDirectory(AECopy.dirSoloSeNonEsiste, srcPathValida, destPathDaSovrascrivere);
        assertFalse(ottenutoBooleano);
        assertTrue(file.isEsisteDirectory(destPathDaSovrascrivere));

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
        ottenuto = file.leggeFile(sorgente);
        System.out.println(ottenuto);
    }


    @Test
    @Order(15)
    @DisplayName("15 - levaDirectoryFinale")
    public void levaDirectoryFinale() {
        sorgente = "/Users/gac/Documents/IdeaProjects/operativi/vaadflow/";
        previsto = "/Users/gac/Documents/IdeaProjects/operativi/";
        ottenuto = file.levaDirectoryFinale(sorgente);
        assertEquals(previsto, ottenuto);
    }


    @Test
    @Order(16)
    @DisplayName("16 - getSubdiretories")
    public void getSubdiretories() {
        sorgente = DIRECTORY_IDEA;
        listaDirectory = file.getSubDirectories(sorgente);
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
        listaDirectory = file.getSubDirectories(fileSorgente);
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
        List<String> ottenuto = file.getSubDirectoriesName(sorgente);
        System.out.println("Tramite path");
        System.out.println("************");
        if (ottenuto != null) {
            for (String directory : ottenuto) {
                System.out.println(directory);
            }
        }

        File fileSorgente = new File(DIRECTORY_IDEA);
        ottenuto = file.getSubDirectoriesName(fileSorgente);
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
        List<String> ottenuto = file.getSubDirectoriesAbsolutePathName(sorgente);
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
        listaDirectory = file.getSubSubDirectories(sorgente, dirInterna);
        assertEquals(listaDirectory.size(), 3);
        if (listaDirectory != null) {
            for (File file : listaDirectory) {
                System.out.println(file.getName());
            }
        }

        sorgente = "/Users/gac/Documents/IdeaProjects/operativi/vaadflow/";
        dirInterna = "/src/main";
        listaDirectory = file.getSubSubDirectories(sorgente, dirInterna);
        assertEquals(listaDirectory.size(), 3);

        sorgente = "/Users/gac/Documents/IdeaProjects/operativi/vaadflow";
        dirInterna = "/src/main";
        listaDirectory = file.getSubSubDirectories(sorgente, dirInterna);
        assertEquals(listaDirectory.size(), 3);

        sorgente = "/Users/gac/Documents/IdeaProjects/operativi/vaadflow/";
        dirInterna = "src/main";
        listaDirectory = file.getSubSubDirectories(sorgente, dirInterna);
        assertEquals(listaDirectory.size(), 3);

    }


    @Test
    @Order(21)
    @DisplayName("21 - isEsisteSubDirectory")
    public void isEsisteSubDirectory() {
        sorgente = "/Users/gac/Documents/IdeaProjects/operativi/vaadflow";
        String dirInterna = "src/main";
        ottenutoBooleano = file.isPienaSubDirectory(new File(sorgente), dirInterna);
        assertTrue(ottenutoBooleano);
    }


    @Test
    @Order(22)
    @DisplayName("22 - isVuotaSubDirectory")
    public void isVuotaSubDirectory() {
        sorgente = "/Users/gac/Documents/IdeaProjects/tutorial";
        String dirInterna = "src/main";
        ottenutoBooleano = file.isVuotaSubDirectory(new File(sorgente), dirInterna);
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
        lista = file.leggeListaCSV(sorgente);
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
        lista = file.leggeListaCSV(sorgente);
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
        lista = file.leggeMappaCSV(sorgente);
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
        lista = file.leggeMappaCSV(sorgente);
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
        assertFalse(file.isEsisteFile(pathFileUno));
        assertFalse(file.isEsisteFile(pathFileDue));

        //--provo con condizioni NON valide
        ottenuto = file.spostaFileStr(VUOTA, pathFileDue);
        assertEquals(PATH_NULLO, ottenuto);

        //--provo con condizioni NON valide
        ottenuto = file.spostaFileStr(pathFileUno, pathFileDue);
        assertEquals(NON_ESISTE_FILE, ottenuto);

        //--creo un file e controllo la situazione al momento
        file.creaFile(pathFileUno);
        assertTrue(file.isEsisteFile(pathFileUno));
        assertFalse(file.isEsisteFile(pathFileDue));

        //--eseguo lo spostamento
        ottenuto = file.spostaFileStr(pathFileUno, pathFileDue);
        assertEquals(VUOTA, ottenuto);

        //--controllo la situazione dopo lo spostamento
        assertFalse(file.isEsisteFile(pathFileUno));
        assertTrue(file.isEsisteFile(pathFileDue));

        //--cancello il file temporaneo
        file.deleteFile(pathFileDue);

        //--situazione finale vuota
        assertFalse(file.isEsisteFile(pathFileUno));
        assertFalse(file.isEsisteFile(pathFileDue));
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
        listaDirectory = file.getAllProjects(sorgente);
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
        listaDirectory = file.getEmptyProjects(sorgente);
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
        ottenuto = file.findPathDirectory(VUOTA, VUOTA);
        assertEquals(previsto, ottenuto);
        printPath(VUOTA, VUOTA, ottenuto);

        previsto = "/Users/gac/Documents/IdeaProjects/operativi/vaadflow14/";
        ottenuto = file.findPathDirectory(sorgente, VUOTA);
        assertEquals(previsto, ottenuto);
        printPath(sorgente, sorgente2, ottenuto);

        sorgente2 = "sconosciuta";
        previsto = VUOTA;
        ottenuto = file.findPathDirectory(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);
        printPath(sorgente, sorgente2, ottenuto);

        sorgente2 = "Documents";
        previsto = "/Users/gac/";
        ottenuto = file.findPathDirectory(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);
        printPath(sorgente, sorgente2, ottenuto);

        sorgente2 = "IdeaProjects";
        previsto = "/Users/gac/Documents/";
        ottenuto = file.findPathDirectory(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);
        printPath(sorgente, sorgente2, ottenuto);

        sorgente2 = "operativi";
        previsto = "/Users/gac/Documents/IdeaProjects/";
        ottenuto = file.findPathDirectory(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);
        printPath(sorgente, sorgente2, ottenuto);

        sorgente2 = "vaadflow14";
        previsto = "/Users/gac/Documents/IdeaProjects/operativi/";
        ottenuto = file.findPathDirectory(sorgente, sorgente2);
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
        ottenuto = file.findPathBreve(VUOTA, VUOTA);
        assertEquals(previsto, ottenuto);
        printPath(VUOTA, VUOTA, ottenuto);

        previsto = sorgente;
        ottenuto = file.findPathBreve(sorgente, VUOTA);
        assertEquals(previsto, ottenuto);
        printPath(sorgente, VUOTA, ottenuto);

        sorgente2 = "Documents";
        previsto = VUOTA;
        ottenuto = file.findPathBreve(VUOTA, sorgente2);
        assertEquals(previsto, ottenuto);
        printPath(VUOTA, sorgente2, ottenuto);

        sorgente2 = "tutorial";
        previsto = sorgente;
        ottenuto = file.findPathBreve(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);
        printPath(sorgente, sorgente2, ottenuto);

        sorgente2 = "IdeaProjects";
        previsto = "../IdeaProjects/operativi/vaadflow14/src/main/java/it/algos/vaadflow14/backend/packages/crono/giorno";
        ottenuto = file.findPathBreve(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);
        printPath(sorgente, sorgente2, ottenuto);

        sorgente2 = "operativi";
        previsto = "../operativi/vaadflow14/src/main/java/it/algos/vaadflow14/backend/packages/crono/giorno";
        ottenuto = file.findPathBreve(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);
        printPath(sorgente, sorgente2, ottenuto);

        sorgente2 = "vaadflow14";
        previsto = "../vaadflow14/backend/packages/crono/giorno";
        ottenuto = file.findPathBreve(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);
        printPath(sorgente, sorgente2, ottenuto);

        sorgente2 = "packages";
        previsto = "../packages/crono/giorno";
        ottenuto = file.findPathBreve(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);
        printPath(sorgente, sorgente2, ottenuto);
    }

    @Test
    @Order(34)
    @DisplayName("34 - estraeDirectoryFinale")
    public void estraeDirectoryFinale() {
        sorgente = "/Users/gac/Documents/IdeaProjects/operativi/vaadflow/";
        previsto = "vaadflow/";
        ottenuto = file.estraeDirectoryFinale(sorgente);
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
            lista = file.recursionSubPathNames(path);
        } catch (Exception unErrore) {
            logger.warn(unErrore, this.getClass(), "recursionSubPathNames");
        }

        assertTrue(array.isAllValid(lista));
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

        lista = file.getAllSubPathFiles(pathName);

        assertTrue(array.isAllValid(lista));
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

        lista = file.getAllSubFilesJava(pathName);

        assertTrue(array.isAllValid(lista));
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
        lista = file.getAllSubFilesEntity(pathName);

        assertTrue(array.isAllValid(lista));
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
        ottenuto = file.estraeClasseFinale(sorgente);
        assertEquals(previsto, ottenuto);

        sorgente = "it.algos.vaadflow14.backend.packages.geografica.stato.Stato";
        previsto = "Stato";
        ottenuto = file.estraeClasseFinale(sorgente);
        assertEquals(previsto, ottenuto);

        sorgente = "/Users/gac/Documents/IdeaProjects/untitled/";
        previsto = "untitled";
        ottenuto = file.estraeClasseFinale(sorgente);
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
        ottenuto = file.findPathBreve(sorgente, sorgente2);
        assertTrue(text.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        System.out.println(VUOTA);
        System.out.println(sorgente);
        System.out.println(sorgente2);
        System.out.println(ottenuto);

        sorgente2 = "operativi";
        previsto = "../operativi/vaadflow14/src/main/java/it/algos/vaadflow14/wizard";
        ottenuto = file.findPathBreve(sorgente, sorgente2);
        assertTrue(text.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        System.out.println(VUOTA);
        System.out.println(sorgente);
        System.out.println(sorgente2);
        System.out.println(ottenuto);

        sorgente2 = "it";
        previsto = "../it/algos/vaadflow14/wizard";
        ottenuto = file.findPathBreve(sorgente, sorgente2);
        assertTrue(text.isValid(ottenuto));
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
        ottenuto = file.findPathBreveDa(sorgente, sorgente2);
        assertTrue(text.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        System.out.println(VUOTA);
        System.out.println(sorgente);
        System.out.println(sorgente2);
        System.out.println(ottenuto);

        sorgente2 = "operativi";
        previsto = "../vaadflow14/src/main/java/it/algos/vaadflow14/wizard";
        ottenuto = file.findPathBreveDa(sorgente, sorgente2);
        assertTrue(text.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        System.out.println(VUOTA);
        System.out.println(sorgente);
        System.out.println(sorgente2);
        System.out.println(ottenuto);

        sorgente2 = "it";
        previsto = "../algos/vaadflow14/wizard";
        ottenuto = file.findPathBreveDa(sorgente, sorgente2);
        assertTrue(text.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        System.out.println(VUOTA);
        System.out.println(sorgente);
        System.out.println(sorgente2);
        System.out.println(ottenuto);
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
        file.deleteFile(nomeCompletoFile);

        //--creazione da testare
        ottenutoDaNome = file.creaFileStr(nomeCompletoFile);
        assertTrue(file.creaFile(nomeCompletoFile));
        assertEquals(VUOTA, ottenutoDaNome);
        System.out.println("Risposta" + CREA_FILE + "ottenutoDaNome: " + nomeCompletoFile + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unFile = new File(nomeCompletoFile);
        assertNotNull(unFile);
        ottenutoDaFile = file.creaFileStr(unFile);
        assertTrue(file.creaFile(unFile));
        assertEquals(VUOTA, ottenutoDaFile);
        System.out.println("Risposta" + CREA_FILE + "ottenutoDaFile: " + nomeCompletoFile + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        //--controlla dopo averlo creato - Esiste
        assertTrue(file.isEsisteFile(nomeCompletoFile));

        //--se è stato creato il file che qui viene cancellato,
        //--devo aggiungere alla lista la directory parent per poterla cancellare alla fine del test
        if (file.isEsisteFile(nomeCompletoFile)) {
            listaDirectory.add(new File(unFile.getParent()));
        }

        //--cancellazione
        file.deleteFile(nomeCompletoFile);

        //--controlla dopo averlo cancellato - Non esiste
        assertFalse(file.isEsisteFile(nomeCompletoFile));
    }


    /**
     * Crea al volo una directory (probabilmente) valida e la cancella subito dopo
     */
    private void creaDirectoryValida(String nomeCompletoDirectory) {
        System.out.println("");

        //--creazione da testare
        ottenutoDaNome = file.creaDirectoryStr(nomeCompletoDirectory);
        assertTrue(file.creaDirectory(nomeCompletoDirectory));
        assertEquals(VUOTA, ottenutoDaNome);
        System.out.println("Risposta" + CREA_DIRECTORY + "ottenutoDaNome: " + nomeCompletoDirectory + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        unaDirectory = new File(nomeCompletoDirectory);
        assertNotNull(unaDirectory);
        ottenutoDaFile = file.creaDirectoryStr(unaDirectory);
        assertTrue(file.creaDirectory(unaDirectory));
        assertEquals(VUOTA, ottenutoDaFile);
        System.out.println("Risposta" + CREA_DIRECTORY + "ottenutoDaFile: " + nomeCompletoDirectory + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        //--controlla dopo averlo creato - Esiste
        assertTrue(file.isEsisteDirectory(nomeCompletoDirectory));

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
        file.creaFile(nomeCompletoFile);

        //--cancellazione da testare
        ottenutoDaNome = file.deleteFileStr(nomeCompletoFile);
        file.creaFile(nomeCompletoFile);
        assertTrue(file.deleteFile(nomeCompletoFile));
        assertEquals(VUOTA, ottenutoDaNome);
        System.out.println("Risposta" + DELETE_FILE + "ottenutoDaNome: " + nomeCompletoFile + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        file.creaFile(nomeCompletoFile);
        unFile = new File(nomeCompletoFile);
        assertNotNull(unFile);
        ottenutoDaFile = file.deleteFileStr(unFile);
        file.creaFile(nomeCompletoFile);
        assertTrue(file.deleteFile(unFile));
        assertEquals(VUOTA, ottenutoDaFile);
        System.out.println("Risposta" + DELETE_FILE + "ottenutoDaFile: " + nomeCompletoFile + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        //--controlla dopo averlo cancellato - Non esiste
        assertFalse(file.isEsisteFile(nomeCompletoFile));
    }


    /**
     * Cancello una directory e la ricreo subito dopo
     * Se non esiste, la creo al volo PRIMA di cancellarla
     */
    private void deleteDirectoryValida(String nomeCompletoDirectory) {
        System.out.println("");

        //--controlla che esista prima di cancellarla
        file.creaDirectory(nomeCompletoDirectory);

        //--cancellazione da testare
        ottenutoDaNome = file.deleteDirectoryStr(nomeCompletoDirectory);
        file.creaDirectory(nomeCompletoDirectory);
        assertTrue(file.deleteDirectory(nomeCompletoDirectory));
        assertEquals(VUOTA, ottenutoDaNome);
        System.out.println("Risposta" + DELETE_DIRECTORY + "ottenutoDaNome: " + nomeCompletoDirectory + " = " + (ottenutoDaNome.equals(VUOTA) ? VALIDO : ottenutoDaNome));
        file.creaDirectory(nomeCompletoDirectory);
        unFile = new File(nomeCompletoDirectory);
        assertNotNull(unFile);
        ottenutoDaFile = file.deleteDirectoryStr(unFile);
        file.creaDirectory(nomeCompletoDirectory);
        assertTrue(file.deleteDirectory(unFile));
        assertEquals(VUOTA, ottenutoDaFile);
        System.out.println("Risposta" + DELETE_DIRECTORY + "ottenutoDaFile: " + nomeCompletoDirectory + " = " + (ottenutoDaFile.equals(VUOTA) ? VALIDO : ottenutoDaFile));

        //--controlla dopo averlo cancellato - Non esiste
        assertFalse(file.isEsisteDirectory(nomeCompletoDirectory));
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

        assertTrue(file.creaDirectory(emptyDirCopiata));
        assertTrue(file.creaFile(srcDirUnoFileUno));
        assertTrue(file.creaFile(srcDirUnoFileDue));
        assertTrue(file.creaFile(srcDirUnoFileTre));
        assertTrue(file.creaFile(srcDirDueFileUno));
        assertTrue(file.creaFile(srcDirDueFileDue));
        assertTrue(file.creaFile(srcDirDueFileQuattro));

        assertTrue(file.creaDirectory(emptyDirRimane));
        assertTrue(file.creaFile(destDirUnoFileUno));
        assertTrue(file.creaFile(destDirUnoFileDue));
        assertTrue(file.creaFile(destDirUnoFileQuattro));
        assertTrue(file.creaFile(destDirDueFileUno));
        assertTrue(file.creaFile(destDirDueFileDue));
        assertTrue(file.creaFile(destDirDueFileTre));

        //--aggiunge nella directory sorgente un file di testo contenete effettivamente del testo
        assertTrue(file.creaFile(srcPath + dirUno + fileConTesto));
        file.sovraScriveFile(srcPath + dirUno + fileConTesto, "Questo testo verrà copiato");
        assertTrue(file.creaFile(destPath + dirUno + fileConTesto));
        file.sovraScriveFile(destPath + dirUno + fileConTesto, "Questo testo verrà cancellato");

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