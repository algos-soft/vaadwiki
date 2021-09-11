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

    protected static final String GIORNO_1 = "";

    protected static final String GIORNO_2 = "31 febbraio";

    protected static final String GIORNO_3 = "4 termidoro";

    protected static final String GIORNO_4 = "17 marzo";

    protected static final String GIORNO_5 = "testo errato";

    protected static final String GIORNO_6 = "12 [[Luglio]] <ref>Da levare</ref>";

    protected static final String GIORNO_7 = "24aprile";

    protected static final String GIORNO_8 = "2 Novembre";

    protected static final String GIORNO_9 = "2Novembre";

    protected static final String GIORNO_10 = "?";

    protected static final String GIORNO_11 = "3 dicembre?";

    protected static final String GIORNO_12 = "3 dicembre circa";

    protected static final String GIORNO_13 = "[[8 agosto]]";

    protected static final String GIORNO_14 = "21[Maggio]";

    protected static final String GIORNO_15 = "[4 febbraio]";

    protected static final String GIORNO_16 = "settembre 5";

    protected static final String GIORNO_17 = "27 ottobre <!--eh eh eh-->";

    protected static final String GIORNO_18 = "29 giugno <nowiki> levare";

    protected static final String GIORNO_19 = "dicembre";

    protected static final String GIORNO_20 = "12/5";

    protected static final String GIORNO_21 = "12-5";

    protected static final String ANNO_1 = "";

    protected static final String ANNO_2 = "3145";

    protected static final String ANNO_3 = "1874";

    protected static final String ANNO_4 = "testo errato";

    protected static final String ANNO_5 = "[[1954]]";

    protected static final String ANNO_6 = "1512?";

    protected static final String ANNO_7 = "?";

    protected static final String ANNO_8 = "1649 circa";

    protected static final String ANNO_9 = "1649 <ref>Da levare</ref>";

    protected static final String ANNO_10 = "754 a.C.";

    protected static final String ANNO_11 = "754 a.c.";

    protected static final String ANNO_12 = "754a.c.";

    protected static final String ANNO_13 = "754a.C.";

    protected static final String ANNO_14 = "754 A.C.";

    protected static final String ANNO_15 = "754 AC";

    protected static final String ANNO_16 = "754 ac";

    protected static final String ANNO_17 = "novecento";

    protected static final String ANNO_18 = "3 secolo";

    protected static final String ANNO_19 = "1532/1537";

    protected static final String ANNO_20 = "754 a.C. circa";

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
        return new String[]{
                GIORNO_1, GIORNO_2, GIORNO_3, GIORNO_4, GIORNO_5, GIORNO_6, GIORNO_7,
                GIORNO_8, GIORNO_9, GIORNO_10, GIORNO_11, GIORNO_12, GIORNO_13, GIORNO_14,
                GIORNO_15, GIORNO_16, GIORNO_17, GIORNO_18, GIORNO_19, GIORNO_20, GIORNO_21};
    }

    public static String[] ANNI() {
        return new String[]{
                ANNO_1, ANNO_2, ANNO_3, ANNO_4, ANNO_5, ANNO_6, ANNO_7, ANNO_8, ANNO_9, ANNO_10,
                ANNO_11, ANNO_12, ANNO_13, ANNO_14, ANNO_15, ANNO_16, ANNO_17, ANNO_18, ANNO_19, ANNO_20};
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
        System.out.println(String.format("'%s' -> '%s'", tagInput, tagValido));
        System.out.println(VUOTA);
    }

    private void printSesso(final String tagInput, final String tagValido) {
        System.out.println(String.format("'%s' -> '%s'", tagInput, tagValido));
        System.out.println(VUOTA);
    }

    private void printGiorno(final String tagInput, final Giorno giornoValido) {
        if (giornoValido != null) {
            System.out.println(String.format("'%s' -> '%s'", tagInput, giornoValido.titolo));
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