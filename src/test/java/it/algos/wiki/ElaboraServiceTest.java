package it.algos.wiki;

import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
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
@Tag("ElaboraServiceTest")
@DisplayName("ElaboraService - Elaborazione delle biografie.")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ElaboraServiceTest extends WTest {

    protected static final String NOME_UNO = "testo errato";

    protected static final String NOME_DUE = "Marcello <ref>Da levare</ref>";

    protected static final String NOME_TRE = "Antonio [html:pippoz]";

    protected static final String NOME_QUATTRO = "Roberto Marco Maria";

    protected static final String NOME_CINQUE = "Colin Campbell (generale)";

    protected static final String NOME_SEI = "Edwin Hall";

    protected static final String NOME_SETTE = "Louis Winslow Austin";


    protected Giorno previstoGiorno;

    protected Giorno ottenutoGiorno;

    protected Anno previstoAnno;

    protected Anno ottenutoAnno;

    public static String[] NOMI() {
        return new String[]{NOME_UNO, NOME_DUE, NOME_TRE, NOME_QUATTRO, NOME_CINQUE, NOME_SEI, NOME_SETTE};
    }

    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    private ElaboraService service;


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
    }


    /**
     * Qui passa a ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    void setUpEach() {
        super.setUp();
    }


    @ParameterizedTest
    @MethodSource(value = "NOMI")
    @Order(1)
    @DisplayName("1 - fixNome")
    void testWithStringParameter(String nome) {
        ottenuto = service.fixNomeValido(nome);
        assertTrue(textService.isValid(ottenuto));
        printNome(nome, ottenuto);
    }

    @Test
    @Order(2)
    @DisplayName("2 - fixNome")
    void fixNome() {
        System.out.println("2 - fixNome");

        sorgente = "testo errato";
        ottenuto = service.fixNomeValido(sorgente);
        assertTrue(textService.isValid(ottenuto));
        printNome(sorgente, ottenuto);
    }


    @Test
    @Order(3)
    @DisplayName("3 - fixGiorno")
    void fixGiorno() {
        previstoGiorno = null;

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
        previstoGiorno = giornoService.findByKey(sorgente);
        ottenutoGiorno = service.fixGiorno(sorgente);
        assertNotNull(ottenutoGiorno);
        assertEquals(previstoGiorno, ottenutoGiorno);
        System.out.println(ottenutoGiorno);
    }


    @Test
    @Order(4)
    @DisplayName("4 - fixAnno")
    void fixAnno() {
        previstoGiorno = null;

        sorgente = "testo errato";
        ottenutoAnno = service.fixAnno(sorgente);
        assertNull(ottenutoAnno);

        sorgente = "3145";
        ottenutoAnno = service.fixAnno(sorgente);
        assertNull(ottenutoAnno);

        sorgente = "1874";
        previstoAnno = annoService.findByKey(sorgente);
        ottenutoAnno = service.fixAnno(sorgente);
        assertNotNull(ottenutoAnno);
        assertEquals(previstoAnno, ottenutoAnno);
        System.out.println(ottenutoAnno);
    }

    private void printNome(final String grezzo, final String elaborato) {
        System.out.println(VUOTA);
        System.out.println(String.format("'%s' -> [%s]", grezzo, elaborato));
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