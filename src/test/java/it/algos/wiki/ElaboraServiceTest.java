package it.algos.wiki;

import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.application.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.exceptions.*;
import it.algos.vaadflow14.backend.packages.crono.anno.*;
import it.algos.vaadflow14.backend.packages.crono.giorno.*;
import it.algos.vaadwiki.backend.service.*;
import static org.junit.Assert.*;
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

    protected static final String NOME_UNO = "testo errato";

    protected static final String NOME_DUE = "Marcello <ref>Da levare</ref>";

    protected static final String NOME_TRE = "Antonio [html:pippoz]";

    protected static final String NOME_QUATTRO = "Roberto Marco Maria";

    protected static final String NOME_CINQUE = "Colin Campbell (generale)";

    protected static final String NOME_SEI = "Giovan Battista";

    protected static final String NOME_SETTE = "Anna Maria";

    protected static final String COGNOME_UNO = "testo errato";

    protected static final String COGNOME_DUE = "Brambilla <ref>Da levare</ref>";

    protected static final String COGNOME_TRE = "Rossi [html:pippoz]";

    protected static final String COGNOME_QUATTRO = "Bayley";

    protected static final String COGNOME_CINQUE = "Mora Porras";

    protected static final String COGNOME_SEI = "Ørsted";

    protected static final String COGNOME_SETTE = "de Bruillard";

    protected static final String GIORNO_UNO = "testo errato";

    protected static final String GIORNO_DUE = "31 febbraio";

    protected static final String GIORNO_TRE = "4 termidoro";

    protected static final String GIORNO_QUATTRO = "17 marzo";

    protected static final String GIORNO_CINQUE = "";

    protected static final String GIORNO_SEI = "";

    protected static final String GIORNO_SETTE = "";

    protected static final String ANNO_UNO = "testo errato";

    protected static final String ANNO_DUE = "3145";

    protected static final String ANNO_TRE = "1874";

    protected static final String ANNO_QUATTRO = "";

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
        return new String[]{NOME_UNO, NOME_DUE, NOME_TRE, NOME_QUATTRO, NOME_CINQUE, NOME_SEI, NOME_SETTE};
    }

    public static String[] COGNOMI() {
        return new String[]{COGNOME_UNO, COGNOME_DUE, COGNOME_TRE, COGNOME_QUATTRO, COGNOME_CINQUE, COGNOME_SEI, COGNOME_SETTE};
    }

    public static String[] GIORNI() {
        return new String[]{GIORNO_UNO, GIORNO_DUE, GIORNO_TRE, GIORNO_QUATTRO, GIORNO_CINQUE, GIORNO_SEI, GIORNO_SETTE};
    }

    public static String[] ANNI() {
        return new String[]{ANNO_UNO, ANNO_DUE, ANNO_TRE, ANNO_QUATTRO, ANNO_CINQUE, ANNO_SEI, ANNO_SETTE};
    }

//    public static String[] MASCHI() {
//        return new String[]{"M", "m", "Maschio", "maschio", "Uomo", "uomo"};
//    }
//
//    public static String[] FEMMINE() {
//        return new String[]{"F", "f", "Femmina", "femmina", "Donna", "donna"};
//    }
//
//    public static String[] TRANS() {
//        return new String[]{"trans", "incerto", "non si sa", "dubbio", "?"};
//    }

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
    @DisplayName("1 - fixNome")
    void testWithStringParameterNome(String nome) {
        System.out.println("1 - fixNome");
        FlowVar.typeSerializing = AETypeSerializing.gson;

        sorgente = nome;
        ottenuto = service.fixNome(sorgente);
        assertTrue(textService.isValid(ottenuto));
        printNome(sorgente, ottenuto);
    }


    @ParameterizedTest
    @MethodSource(value = "COGNOMI")
    @Order(2)
    @DisplayName("2 - fixCognome")
    void testWithStringParameterCognome(String cognome) {
        System.out.println("2 - fixCognome");

        sorgente = cognome;
        ottenuto = service.fixCognome(sorgente);
        assertTrue(textService.isValid(ottenuto));
        printNome(sorgente, ottenuto);
    }

    @Test
    @Order(3)
    @DisplayName("3 - fixSesso")
    void fixSesso() {
        System.out.println("3 - fixSesso");
        System.out.println(VUOTA);

        System.out.println("**********");
        for (String tagInput : ElaboraService.MASCHI) {
            ottenuto = service.fixSesso(tagInput);
            printSesso(tagInput,ottenuto);
        }

        System.out.println("**********");
        for (String tagInput : ElaboraService.FEMMINE) {
            ottenuto = service.fixSesso(tagInput);
            printSesso(tagInput,ottenuto);
        }

        System.out.println("**********");
        for (String tagInput : ElaboraService.TRANS) {
            ottenuto = service.fixSesso(tagInput);
            printSesso(tagInput,ottenuto);
        }
    }

    @Test
    @Order(4)
    @DisplayName("4 - fixGiorno")
    void fixGiorno() {
        System.out.println("4 - fixGiorno");
        FlowVar.typeSerializing = AETypeSerializing.gson;

        sorgente = "testo errato";
        ottenutoGiorno = service.fixGiorno(sorgente);
        assertNull(ottenutoGiorno);

        sorgente = "31 febbraio";
        ottenutoGiorno = service.fixGiorno(sorgente);
        assertNull(ottenutoGiorno);

        sorgente = "4 termidoro";
        ottenutoGiorno = service.fixGiorno(sorgente);
        assertNull(ottenutoGiorno);

        sorgente = "17 marzo";
        try {
            previstoGiorno = giornoService.findByKey(sorgente);
        } catch (AMongoException unErrore) {
        }
        ottenutoGiorno = service.fixGiorno(sorgente);
        assertNotNull(ottenutoGiorno);
        assertEquals(previstoGiorno, ottenutoGiorno);
        System.out.println(ottenutoGiorno);
    }


    @Test
    @Order(5)
    @DisplayName("5 - fixAnno")
    void fixAnno() {
        System.out.println("5 - fixAnno");
        FlowVar.typeSerializing = AETypeSerializing.gson;

        sorgente = "testo errato";
        try {
            ottenutoAnno = service.fixAnno(sorgente);
        } catch (AlgosException unErrore) {
        }

        assertNull(ottenutoAnno);

        sorgente = "3145";
        try {
            ottenutoAnno = service.fixAnno(sorgente);
        } catch (AlgosException unErrore) {
        }
        assertNull(ottenutoAnno);

        sorgente = "1874";
        try {
            ottenutoAnno = annoService.findByKey(sorgente);
        } catch (AMongoException unErrore) {
        }
        assertNotNull(ottenutoAnno);
        System.out.println(ottenutoAnno);
    }

    @Test
    @Order(6)
    @DisplayName("6 - fixAttivita")
    void fixAttivita() {

        sorgente = "testo errato";
        //        try {
        //            ottenuto = service.fixAttivitaValida(sorgente);
        //        } catch (AlgosException unErrore) {
        //        }

        assertNull(ottenutoAnno);

        sorgente = "3145";
        //        try {
        //            ottenuto = service.fixAttivitaValida(sorgente);
        //        } catch (AlgosException unErrore) {
        //        }
        assertNull(ottenutoAnno);

        sorgente = "1874";
        //        try {
        //            ottenuto = service.fixAttivitaValida(sorgente);
        //        } catch (AMongoException unErrore) {
        //        }
        assertNotNull(ottenutoAnno);
        System.out.println(ottenutoAnno);
    }

    @Test
    @Order(7)
    @DisplayName("7 - fixNazionalita")
    void fixNazionalita() {
    }

    @Test
    @Order(10)
    @DisplayName("10 - fixxxx")
    void fixNazionxxxalita() {
    }


    private void printNome(final String grezzo, final String elaborato) {
        System.out.println(String.format("'%s' -> [%s]", grezzo, elaborato));
        System.out.println(VUOTA);
    }

    private void printSesso(final String tagInput, final String tagValido) {
        System.out.println(String.format("'%s' -> [%s]", tagInput, tagValido));
        System.out.println(VUOTA);
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
    @AfterEach
    void tearDownAll() {
    }

}