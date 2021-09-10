package it.algos.wiki;

import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.application.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.packages.crono.anno.*;
import it.algos.vaadflow14.backend.packages.crono.giorno.*;
import it.algos.vaadwiki.backend.service.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: sab, 31-lug-2021
 * Time: 17:09
 * <p>
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("testAllValidoWiki")
@DisplayName("ElaboraService - Elaborazione delle biografie.")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ElaboraServiceTest extends WTest {

    protected static final String NOME_UNO = "";

    protected static final String NOME_DUE = "Marcello <ref>Da levare</ref>";

    protected static final String NOME_TRE = "Antonio [html:pippoz]";

    protected static final String NOME_QUATTRO = "Roberto Marco Maria";

    protected static final String NOME_CINQUE = "Colin Campbell (generale)";

    protected static final String NOME_SEI = "Giovan Battista";

    protected static final String NOME_SETTE = "Anna Maria";

    protected static final String NOME_OTTO = "testo errato";

    protected static final String COGNOME_UNO = "";

    protected static final String COGNOME_DUE = "Brambilla <ref>Da levare</ref>";

    protected static final String COGNOME_TRE = "Rossi [html:pippoz]";

    protected static final String COGNOME_QUATTRO = "Bayley";

    protected static final String COGNOME_CINQUE = "Mora Porras";

    protected static final String COGNOME_SEI = "Ørsted";

    protected static final String COGNOME_SETTE = "de Bruillard";

    protected static final String COGNOME_OTTO = "testo errato";

    protected static final String GIORNO_UNO = "";

    protected static final String GIORNO_DUE = "31 febbraio";

    protected static final String GIORNO_TRE = "4 termidoro";

    protected static final String GIORNO_QUATTRO = "17 marzo";

    protected static final String GIORNO_CINQUE = "testo errato";

    protected static final String GIORNO_SEI = "12 luglio <ref>Da levare</ref>";

    protected static final String GIORNO_SETTE = "24aprile";

    protected static final String GIORNO_OTTO = "2 Novembre";

    protected static final String GIORNO_NOVE = "2Novembre";

    protected static final String GIORNO_DIECI = "?";

    protected static final String GIORNO_UNDICI = "3 dicembre?";

    protected static final String GIORNO_DODICI = "3 dicembre circa";

    protected static final String ANNO_UNO = "";

    protected static final String ANNO_DUE = "3145";

    protected static final String ANNO_TRE = "1874";

    protected static final String ANNO_QUATTRO = "testo errato";

    protected static final String ANNO_CINQUE = "";

    protected static final String ANNO_SEI = "";

    protected static final String ANNO_SETTE = "";


    protected Giorno previstoGiorno;

    protected Giorno ottenutoGiorno;

    protected Anno previstoAnno;

    protected Anno ottenutoAnno;

    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    private ElaboraService service;

    public static String[] NOMI() {
        return new String[]{NOME_UNO, NOME_DUE, NOME_TRE, NOME_QUATTRO, NOME_CINQUE, NOME_SEI, NOME_SETTE, NOME_OTTO};
    }

    public static String[] COGNOMI() {
        return new String[]{COGNOME_UNO, COGNOME_DUE, COGNOME_TRE, COGNOME_QUATTRO, COGNOME_CINQUE, COGNOME_SEI, COGNOME_SETTE, COGNOME_OTTO};
    }

    public static String[] GIORNI() {
        return new String[]{GIORNO_UNO, GIORNO_DUE, GIORNO_TRE, GIORNO_QUATTRO, GIORNO_CINQUE, GIORNO_SEI, GIORNO_SETTE, GIORNO_OTTO, GIORNO_NOVE, GIORNO_DIECI, GIORNO_UNDICI, GIORNO_DODICI};
    }

    public static String[] ANNI() {
        return new String[]{ANNO_UNO, ANNO_DUE, ANNO_TRE, ANNO_QUATTRO, ANNO_CINQUE, ANNO_SEI, ANNO_SETTE};
    }

    public static String[] ATTIVITA() {
        return new String[]{"errata", "medico", "dottoressa", "Medico", "ex ciclista"};
    }


    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpIniziale() {
        super.setUpStartUp();

        //--reindirizzo l'istanza della superclasse
        service = elaboraService;

        service.mongo = mongoService;
        service.text = textService;
        service.annotation = annotationService;
        service.wikiBotService = wikiBotService;
    }


    /**
     * Qui passa a ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    void setUpEach() {
        super.setUp();

        previstoGiorno = null;
        previstoAnno = null;
    }


    @ParameterizedTest
    @MethodSource(value = "NOMI")
    @Order(1)
    @DisplayName("1 - fixNome (come stringa)")
    void testWithStringParameterNome(String nome) {
        System.out.println("1 - fixNome (come stringa)");
        FlowVar.typeSerializing = AETypeSerializing.gson;

        sorgente = nome;
        ottenuto = service.fixNome(sorgente);
        printNome(sorgente, ottenuto);
    }


    @ParameterizedTest
    @MethodSource(value = "COGNOMI")
    @Order(2)
    @DisplayName("2 - fixCognome (come stringa)")
    void testWithStringParameterCognome(String cognome) {
        System.out.println("2 - fixCognome (come stringa)");

        sorgente = cognome;
        ottenuto = service.fixCognome(sorgente);
        printNome(sorgente, ottenuto);
    }

    @Test
    @Order(3)
    @DisplayName("3 - fixSesso (come stringa)")
    void fixSesso() {
        System.out.println("3 - fixSesso (come stringa)");
        System.out.println(VUOTA);

        System.out.println("**********");
        for (String tagInput : ElaboraService.MASCHI) {
            ottenuto = service.fixSesso(tagInput);
            printSesso(tagInput, ottenuto);
        }

        System.out.println("**********");
        for (String tagInput : ElaboraService.FEMMINE) {
            ottenuto = service.fixSesso(tagInput);
            printSesso(tagInput, ottenuto);
        }

        System.out.println("**********");
        for (String tagInput : ElaboraService.TRANS) {
            ottenuto = service.fixSesso(tagInput);
            printSesso(tagInput, ottenuto);
        }
    }


    @ParameterizedTest
    @MethodSource(value = "GIORNI")
    @Order(4)
    @DisplayName("4 - fixGiorno (come stringa)")
    void testWithStringParameterGiorno(String giorno) {
        System.out.println("4 - fixGiorno (come stringa)");

        sorgente = giorno;
        ottenuto = service.fixGiorno(sorgente);
        printNome(sorgente, ottenuto);
    }


    @ParameterizedTest
    @MethodSource(value = "GIORNI")
    @Order(5)
    @DisplayName("5 - fixGiornoLink (come Giorno esistente)")
    void testWithStringParameterGiornoLink(String giorno) {
        System.out.println("5 - fixGiornoLink (come Giorno esistente)");
        FlowVar.typeSerializing = AETypeSerializing.gson;

        sorgente = giorno;
        ottenutoGiorno = null;
        try {
            ottenutoGiorno = service.fixGiornoLink(sorgente);
        } catch (Exception unErrore) {
        }
        printGiorno(sorgente, ottenutoGiorno);
    }

    @ParameterizedTest
    @MethodSource(value = "GIORNI")
    @Order(6)
    @DisplayName("6 - fixGiornoValido (come stringa)")
    void testWithStringParameterGiornoValido(String giorno) {
        System.out.println("6 - fixGiornoValido (come stringa)");

        sorgente = giorno;
        ottenuto = service.fixGiornoValido(sorgente);
        printNome(sorgente, ottenuto);
    }


    @ParameterizedTest
    @MethodSource(value = "ANNI")
    @Order(7)
    @DisplayName("7 - fixAnno (come stringa)")
    void testWithStringParameterAnno(String anno) {
        System.out.println("7 - fixAnno (come stringa)");

        sorgente = anno;
        ottenuto = service.fixAnno(sorgente);
        printNome(sorgente, ottenuto);

    }


    @ParameterizedTest
    @MethodSource(value = "ANNI")
    @Order(8)
    @DisplayName("8 - fixAnno (come Anno esistente)")
    void testWithStringParameterAnnoLink(String anno) {
        System.out.println("8 - fixAnno (come Anno esistente)");
        FlowVar.typeSerializing = AETypeSerializing.gson;

        sorgente = anno;
        ottenutoAnno = null;
        try {
            ottenutoAnno = service.fixAnnoLink(sorgente);
        } catch (Exception unErrore) {
        }
        printAnno(sorgente, ottenutoAnno);
    }

    //    @Test
    //    @Order(6)
    //    @DisplayName("6 - fixAnno")
    //    void fixAnno() {
    //        System.out.println("6 - fixAnno");
    //        FlowVar.typeSerializing = AETypeSerializing.gson;
    //
    //        sorgente = "testo errato";
    //        try {
    //            ottenutoAnno = service.fixAnnoLink(sorgente);
    //        } catch (Exception unErrore) {
    //        }
    //
    //        assertNull(ottenutoAnno);
    //
    //        sorgente = "3145";
    //        try {
    //            ottenutoAnno = service.fixAnnoLink(sorgente);
    //        } catch (Exception unErrore) {
    //        }
    //        assertNull(ottenutoAnno);
    //
    //        sorgente = "1874";
    //        try {
    //            ottenutoAnno = annoService.findByKey(sorgente);
    //        } catch (AMongoException unErrore) {
    //        }
    //        assertNotNull(ottenutoAnno);
    //        System.out.println(ottenutoAnno);
    //    }


    @ParameterizedTest
    @MethodSource(value = "ANNI")
    @Order(9)
    @DisplayName("9 - fixAnno (come stringa)")
    void testWithStringParameterAnnoValido(String anno) {
        System.out.println("9 - fixAnno (come stringa)");

        sorgente = anno;
        ottenuto = service.fixAnno(sorgente);
        printNome(sorgente, ottenuto);

    }

    @ParameterizedTest
    @MethodSource(value = "ATTIVITA")
    @Order(10)
    @DisplayName("10 - fixAttivita")
    void testWithStringParameterAttivita(String attivita) {
        System.out.println("10 - fixAttivita");

        //        sorgente = attivita;
        //        ottenuto = service.fixAttivitaValida(sorgente);
        //        assertTrue(textService.isValid(ottenuto));
        //        printNome(sorgente, ottenuto);
    }

    @Test
    @Order(15)
    @DisplayName("15 - fixNazionalita")
    void fixNazionalita() {
    }

    @Test
    @Order(20)
    @DisplayName("20 - fixxxx")
    void fixNazionxxxalita() {
    }


    private void printNome(final String tagInput, final String tagValido) {
        System.out.println(String.format("'%s' -> [%s]", tagInput, tagValido));
        System.out.println(VUOTA);
    }

    private void printSesso(final String tagInput, final String tagValido) {
        System.out.println(String.format("'%s' -> [%s]", tagInput, tagValido));
        System.out.println(VUOTA);
    }

    private void printGiorno(final String tagInput, final Giorno giornoValido) {
        if (giornoValido != null) {
            System.out.println(String.format("'%s' -> [%s]", tagInput, giornoValido.titolo));
            System.out.println(VUOTA);
        }
        else {
            System.out.println(String.format("Non esiste un giorno corrispondente a '%s'", tagInput));
            System.out.println(VUOTA);
        }
    }

    private void printAnno(final String tagInput, final Anno annoValido) {
        if (annoValido != null) {
            System.out.println(String.format("'%s' -> [%s]", tagInput, annoValido.titolo));
            System.out.println(VUOTA);
        }
        else {
            System.out.println(String.format("Non esiste un anno corrispondente a '%s'", tagInput));
            System.out.println(VUOTA);
        }
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
        FlowVar.typeSerializing = AETypeSerializing.spring;
    }

}