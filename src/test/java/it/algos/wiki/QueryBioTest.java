package it.algos.wiki;

import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadwiki.backend.wrapper.*;
import it.algos.vaadwiki.wiki.*;
import it.algos.vaadwiki.wiki.query.*;
import static org.junit.Assert.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mar, 10-ago-2021
 * Time: 09:30
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("testAllValidoWiki")
@DisplayName("QueryBio - Istanza per recuperare una bio.")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QueryBioTest extends WTest {

    public static final String PAGINA_ESISTENTE_MAIUSCOLA = "Pippoz";

    public static final String PAGINA_INESISTENTE_MINUSCOLA = "pippoz";

    public static final String PAGINA_INESISTENTE_CON_SPAZI = "Questa pagina non esiste";

    public static final String PAGINA_DISAMBIGUA = "Costa (disambigua)";

    public static final String PAGINA_REDIRECT = "Alberto Gines Lopez";

    public static final String PAGINA_SENZA_TMPL_BIO = "Costa (Conegliano)";

    public static final String PAGINA_BIOGRAFICA = "Cesare Costa";

    //    @InjectMocks
    //    public AWikiApiService wikiApi;

    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    private QueryBio istanza;

    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpIniziale() {
        super.setUpStartUp();

        //--reindirizzo l'istanza della superclasse
        istanza = queryBio;

        //--titolo della query
        queryType = istanza.getClass().getSimpleName();

        //--abilita il bot
        queryLogin.urlRequest();
    }


    /**
     * Qui passa ad ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    void setUpEach() {
        super.setUp();
    }

    @Test
    @Order(1)
    @DisplayName("1 - Cerca di leggere una pagina inesistente")
    void urlRequest() {
        sorgente = PAGINA_INESISTENTE_MINUSCOLA;
        ottenutoRisultato = istanza.urlRequest(sorgente);
        assertNotNull(ottenutoRisultato);
        assertFalse(ottenutoRisultato.isValido());
        System.out.println("Titolo errato senza spazi");
        System.out.println(VUOTA);
        printRisultato(ottenutoRisultato);
        System.out.println(VUOTA);
        printWrapBio(((WResult) ottenutoRisultato).getWrap());
    }

    @Test
    @Order(2)
    @DisplayName("2 - Cerca di leggere una pagina inesistente")
    void urlRequest2() {
        sorgente = PAGINA_INESISTENTE_CON_SPAZI;
        ottenutoRisultato = istanza.urlRequest(sorgente);
        assertNotNull(ottenutoRisultato);
        assertFalse(ottenutoRisultato.isValido());
        System.out.println("Titolo errato con spazi");
        System.out.println(VUOTA);
        printRisultato(ottenutoRisultato);
        System.out.println(VUOTA);
        printWrapBio(((WResult) ottenutoRisultato).getWrap());
    }


    @Test
    @Order(3)
    @DisplayName("3 - Legge una pagina di disambigua")
    void urlRequest3() {
        sorgente = PAGINA_DISAMBIGUA;
        ottenutoRisultato = istanza.urlRequest(sorgente);
        assertNotNull(ottenutoRisultato);
        assertFalse(ottenutoRisultato.isValido());
        System.out.println("Pagina di disambigua");
        System.out.println(VUOTA);
        printRisultato(ottenutoRisultato);
        System.out.println(VUOTA);
        printWrapBio(((WResult) ottenutoRisultato).getWrap());
    }

    @Test
    @Order(4)
    @DisplayName("4 - Legge una pagina di redirect")
    void urlRequest4() {
        sorgente = PAGINA_REDIRECT;
        ottenutoRisultato = istanza.urlRequest(sorgente);
        assertNotNull(ottenutoRisultato);
        assertFalse(ottenutoRisultato.isValido());
        System.out.println("Pagina di redirect");
        System.out.println(VUOTA);
        printRisultato(ottenutoRisultato);
        System.out.println(VUOTA);
        printWrapBio(((WResult) ottenutoRisultato).getWrap());
    }

    @Test
    @Order(5)
    @DisplayName("5 - Legge una pagina SENZA tmpl Bio")
    void urlRequest5() {
        sorgente = PAGINA_SENZA_TMPL_BIO;
        ottenutoRisultato = istanza.urlRequest(sorgente);
        assertNotNull(ottenutoRisultato);
        assertFalse(ottenutoRisultato.isValido());
        System.out.println("Pagina SENZA tmpl Bio");
        System.out.println(VUOTA);
        printRisultato(ottenutoRisultato);
        System.out.println(VUOTA);
        printWrapBio(((WResult) ottenutoRisultato).getWrap());
    }

    @Test
    @Order(6)
    @DisplayName("6 - Legge una pagina con tmpl Bio")
    void urlRequest6() {
        sorgente = PAGINA_BIOGRAFICA;
        ottenutoRisultato = istanza.urlRequest(sorgente);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());
        System.out.println("Pagina biografica con tmpl Bio");
        System.out.println(VUOTA);
        printRisultato(ottenutoRisultato);
        System.out.println(VUOTA);
        printWrapBio(((WResult) ottenutoRisultato).getWrap());

    }

    @Test
    @Order(7)
    @DisplayName("7 - Recupera un wrapBio")
    void urlRequest7() {
        WrapBio wrap;
        sorgente = PAGINA_BIOGRAFICA;
        wrap = istanza.urlRequest(sorgente).getWrap();
        assertNotNull(wrap);
        assertTrue(wrap.isValido());
        printWrapBio(wrap);
    }


    protected void printWrapBio(WrapBio wrap) {

        System.out.println("WrapBio");
        System.out.println(VUOTA);
        System.out.println("Wrap valido: " + wrap.isValido());
        System.out.println("Titolo:" + SPAZIO + wrap.getTitle());
        System.out.println("PageId:" + SPAZIO + wrap.getPageid());
        System.out.println("Type:" + SPAZIO + wrap.getType());
        System.out.println("Timestamp:" + SPAZIO + wrap.getTime());
        System.out.println("Template:" + SPAZIO + wrap.getTemplBio());
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
    }

}