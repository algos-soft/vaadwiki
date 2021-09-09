package it.algos.wiki;

import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadwiki.backend.enumeration.*;
import it.algos.vaadwiki.backend.service.*;
import static it.algos.vaadwiki.backend.service.WikiBotService.*;
import it.algos.vaadwiki.wiki.*;
import static org.junit.Assert.*;
import org.junit.jupiter.api.*;

import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 10-mag-2021
 * Time: 14:07
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("testAllValidoWiki")
@DisplayName("WikiBotService - Accesso alle pagine wiki.")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WikiBotServiceTest extends WTest {

    public static final String CAT_INESISTENTE = "Nati nel 3435";

    public static final String CAT_1167 = "Nati nel 1167";

    public static final String CAT_1435 = "Nati nel 1435";

    public static final String CAT_1591 = "Nati nel 1591";

    public static final String CAT_1935 = "Nati nel 1935";

    public static final int TOT_1935 = 1996;

    public static final String CAT_1713 = "Nati nel 1713";

    public static final String CAT_2020 = "Morti nel 2020";

    public static final int TOT_2020 = 2405;

    public static final String CAT_ROMANI = "Personaggi della storia romana";

    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    private WikiBotService service;


    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpIniziale() {
        super.setUpStartUp();

        //--reindirizzo l'istanza della superclasse
        service = wikiBotService;
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
    @DisplayName("1 - Controllo e risultato per una pagina")
    public void isEsisteResult() {
        System.out.println("1 - Controllo e risultato per una pagina e/o categoria.");
        System.out.println("Si collega come (come anonymous)");

        ottenutoRisultato = service.isEsisteResult(null);
        assertNotNull(ottenutoRisultato);
        assertFalse(ottenutoRisultato.isValido());
        assertTrue(ottenutoRisultato.isErrato());
        assertTrue(textService.isEmpty(ottenutoRisultato.getWikiTitle()));
        assertTrue(textService.isEmpty(ottenutoRisultato.getUrlRequest()));
        assertEquals(ERROR_WIKI_TITLE, ottenutoRisultato.getErrorCode());
        assertEquals(ERROR_WIKI_TITLE, ottenutoRisultato.getErrorMessage());
        assertTrue(textService.isEmpty(ottenutoRisultato.getValidMessage()));
        assertTrue(textService.isEmpty(ottenutoRisultato.getResponse()));
        assertTrue(ottenutoRisultato.getValue() == 0);
        printResult(ottenutoRisultato);

        sorgente = PAGINA_INESISTENTE;
        ottenutoRisultato = service.isEsisteResult(sorgente);
        assertNotNull(ottenutoRisultato);
        assertFalse(ottenutoRisultato.isValido());
        assertTrue(ottenutoRisultato.isErrato());
        assertEquals(sorgente, ottenutoRisultato.getWikiTitle());
        assertEquals(WIKI_PARSE + sorgente.replaceAll(SPAZIO, UNDERSCORE), ottenutoRisultato.getUrlRequest());
        assertEquals(ERROR_WIKI_PAGINA, ottenutoRisultato.getErrorCode());
        assertEquals(ERROR_WIKI_PAGINA, ottenutoRisultato.getErrorMessage());
        assertTrue(textService.isEmpty(ottenutoRisultato.getValidMessage()));
        assertTrue(textService.isValid(ottenutoRisultato.getResponse()));
        assertTrue(ottenutoRisultato.getValue() == 0);
        printResult(ottenutoRisultato);

        sorgente = CATEGORIA + CAT_INESISTENTE;
        ottenutoRisultato = service.isEsisteResult(sorgente);
        assertNotNull(ottenutoRisultato);
        assertFalse(ottenutoRisultato.isValido());
        assertTrue(ottenutoRisultato.isErrato());
        assertEquals(sorgente, ottenutoRisultato.getWikiTitle());
        assertEquals(WIKI_PARSE + sorgente.replaceAll(SPAZIO, UNDERSCORE), ottenutoRisultato.getUrlRequest());
        assertEquals(ERROR_WIKI_PAGINA, ottenutoRisultato.getErrorCode());
        assertEquals(ERROR_WIKI_PAGINA, ottenutoRisultato.getErrorMessage());
        assertTrue(textService.isEmpty(ottenutoRisultato.getValidMessage()));
        assertTrue(textService.isValid(ottenutoRisultato.getResponse()));
        assertTrue(ottenutoRisultato.getValue() == 0);
        printResult(ottenutoRisultato);

        sorgente = PAGINA_TEST;
        ottenutoRisultato = service.isEsisteResult(sorgente);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());
        assertFalse(ottenutoRisultato.isErrato());
        assertEquals(sorgente, ottenutoRisultato.getWikiTitle());
        assertEquals(WIKI_PARSE + sorgente.replaceAll(SPAZIO, UNDERSCORE), ottenutoRisultato.getUrlRequest());
        assertTrue(textService.isEmpty(ottenutoRisultato.getErrorCode()));
        assertTrue(textService.isEmpty(ottenutoRisultato.getErrorMessage()));
        assertEquals(JSON_SUCCESS, ottenutoRisultato.getValidMessage());
        assertTrue(textService.isValid(ottenutoRisultato.getResponse()));
        assertTrue(ottenutoRisultato.getValue() == 0);
        printResult(ottenutoRisultato);

        sorgente = CATEGORIA + CAT_1167;
        ottenutoRisultato = service.isEsisteResult(sorgente);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());
        assertFalse(ottenutoRisultato.isErrato());
        assertEquals(sorgente, ottenutoRisultato.getWikiTitle());
        assertEquals(WIKI_PARSE + sorgente.replaceAll(SPAZIO, UNDERSCORE), ottenutoRisultato.getUrlRequest());
        assertTrue(textService.isEmpty(ottenutoRisultato.getErrorCode()));
        assertTrue(textService.isEmpty(ottenutoRisultato.getErrorMessage()));
        assertEquals(JSON_SUCCESS, ottenutoRisultato.getValidMessage());
        assertTrue(textService.isValid(ottenutoRisultato.getResponse()));
        assertTrue(ottenutoRisultato.getValue() == 0);
        printResult(ottenutoRisultato);
    }


    @Test
    @Order(2)
    @DisplayName("2 - Esistenza di una pagina")
    public void isEsiste() {
        System.out.println("2 - Esistenza di una pagina e/o categoria.");
        System.out.println("Si collega come (come anonymous)");

        sorgente = PAGINA_INESISTENTE;
        ottenutoBooleano = service.isEsiste(sorgente);
        assertFalse(ottenutoBooleano);
        printEsiste(sorgente, ottenutoBooleano);

        sorgente = PAGINA_PIOZZANO;
        ottenutoBooleano = service.isEsiste(sorgente);
        assertTrue(ottenutoBooleano);
        printEsiste(sorgente, ottenutoBooleano);

        sorgente = CAT_INESISTENTE;
        ottenutoBooleano = service.isEsisteCat(sorgente);
        assertFalse(ottenutoBooleano);
        printEsiste(sorgente, ottenutoBooleano);

        sorgente = CAT_1167;
        ottenutoBooleano = service.isEsisteCat(sorgente);
        assertTrue(ottenutoBooleano);
        printEsiste(sorgente, ottenutoBooleano);

        sorgente = CATEGORIA + CAT_INESISTENTE;
        ottenutoBooleano = service.isEsiste(sorgente);
        assertFalse(ottenutoBooleano);
        printEsiste(sorgente, ottenutoBooleano);

        sorgente = CATEGORIA + CAT_1167;
        ottenutoBooleano = service.isEsiste(sorgente);
        assertTrue(ottenutoBooleano);
        printEsiste(sorgente, ottenutoBooleano);

        sorgente = CATEGORIA + CAT_INESISTENTE;
        ottenutoBooleano = service.isEsisteCat(sorgente);
        assertFalse(ottenutoBooleano);
        printEsiste(sorgente, ottenutoBooleano);

        sorgente = CATEGORIA + CAT_1167;
        ottenutoBooleano = service.isEsisteCat(sorgente);
        assertTrue(ottenutoBooleano);
        printEsiste(sorgente, ottenutoBooleano);
    }


    @Test
    @Order(3)
    @DisplayName("3 - Informazioni della categoria")
    public void getInfoCategoria() {
        System.out.println("3 - Informazioni della categoria.");
        System.out.println("Legge (come anonymous) le info della categoria");

        sorgente = CAT_INESISTENTE;
        previstoIntero = 0;
        inizio = System.currentTimeMillis();
        ottenutoRisultato = service.getInfoCategoria(sorgente);
        assertNotNull(ottenutoRisultato);
        assertFalse(ottenutoRisultato.isValido());
        assertTrue(ottenutoRisultato.isErrato());
        assertEquals(sorgente, ottenutoRisultato.getWikiTitle());
        assertEquals(WIKI_PARSE + CATEGORIA + sorgente.replaceAll(SPAZIO, UNDERSCORE), ottenutoRisultato.getUrlRequest());
        assertEquals(ERROR_WIKI_CATEGORIA, ottenutoRisultato.getErrorCode());
        assertEquals(ERROR_WIKI_CATEGORIA, ottenutoRisultato.getErrorMessage());
        assertTrue(textService.isEmpty(ottenutoRisultato.getValidMessage()));
        assertTrue(textService.isValid(ottenutoRisultato.getResponse()));
        assertTrue(ottenutoRisultato.getValue() == 0);
        printResult(ottenutoRisultato);

        sorgente = CAT_1167;
        previstoIntero = 6;
        inizio = System.currentTimeMillis();
        ottenutoRisultato = service.getInfoCategoria(sorgente);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());
        assertFalse(ottenutoRisultato.isErrato());
        assertEquals(sorgente, ottenutoRisultato.getWikiTitle());
        assertEquals(WIKI_QUERY_CAT_TOTALE + sorgente.replaceAll(SPAZIO, UNDERSCORE), ottenutoRisultato.getUrlRequest());
        assertTrue(textService.isEmpty(ottenutoRisultato.getErrorCode()));
        assertTrue(textService.isEmpty(ottenutoRisultato.getErrorMessage()));
        assertEquals(JSON_SUCCESS, ottenutoRisultato.getValidMessage());
        assertTrue(textService.isValid(ottenutoRisultato.getResponse()));
        assertEquals(previstoIntero, ottenutoRisultato.getValue());
        printResult(ottenutoRisultato);

        sorgente = CAT_1435;
        previstoIntero = 33;
        inizio = System.currentTimeMillis();
        ottenutoRisultato = service.getInfoCategoria(sorgente);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());
        assertFalse(ottenutoRisultato.isErrato());
        assertEquals(sorgente, ottenutoRisultato.getWikiTitle());
        assertEquals(WIKI_QUERY_CAT_TOTALE + sorgente.replaceAll(SPAZIO, UNDERSCORE), ottenutoRisultato.getUrlRequest());
        assertTrue(textService.isEmpty(ottenutoRisultato.getErrorCode()));
        assertTrue(textService.isEmpty(ottenutoRisultato.getErrorMessage()));
        assertEquals(JSON_SUCCESS, ottenutoRisultato.getValidMessage());
        assertTrue(textService.isValid(ottenutoRisultato.getResponse()));
        assertEquals(previstoIntero, ottenutoRisultato.getValue());
        printResult(ottenutoRisultato);

        sorgente = CAT_1935;
        previstoIntero = TOT_1935;
        inizio = System.currentTimeMillis();
        ottenutoRisultato = service.getInfoCategoria(sorgente);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());
        assertFalse(ottenutoRisultato.isErrato());
        assertEquals(sorgente, ottenutoRisultato.getWikiTitle());
        assertEquals(WIKI_QUERY_CAT_TOTALE + sorgente.replaceAll(SPAZIO, UNDERSCORE), ottenutoRisultato.getUrlRequest());
        assertTrue(textService.isEmpty(ottenutoRisultato.getErrorCode()));
        assertTrue(textService.isEmpty(ottenutoRisultato.getErrorMessage()));
        assertEquals(JSON_SUCCESS, ottenutoRisultato.getValidMessage());
        assertTrue(textService.isValid(ottenutoRisultato.getResponse()));
        assertEquals(previstoIntero, ottenutoRisultato.getValue());
        printResult(ottenutoRisultato);

        sorgente = CAT_ROMANI;
        previstoIntero = 78;
        inizio = System.currentTimeMillis();
        ottenutoRisultato = service.getInfoCategoria(sorgente);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());
        assertFalse(ottenutoRisultato.isErrato());
        assertEquals(sorgente, ottenutoRisultato.getWikiTitle());
        assertEquals(WIKI_QUERY_CAT_TOTALE + sorgente.replaceAll(SPAZIO, UNDERSCORE), ottenutoRisultato.getUrlRequest());
        assertTrue(textService.isEmpty(ottenutoRisultato.getErrorCode()));
        assertTrue(textService.isEmpty(ottenutoRisultato.getErrorMessage()));
        assertEquals(JSON_SUCCESS, ottenutoRisultato.getValidMessage());
        assertTrue(textService.isValid(ottenutoRisultato.getResponse()));
        assertEquals(previstoIntero, ottenutoRisultato.getValue());
        printResult(ottenutoRisultato);
    }


    @Test
    @Order(4)
    @DisplayName("4 - Numero di pagine della categoria")
    public void getTotaleCategoria() {
        System.out.println("4 - numero di pagine della categoria.");
        System.out.println("Legge (come anonymous) il numero di pagina della categoria");

        sorgente = CAT_INESISTENTE;
        previstoIntero = 0;
        inizio = System.currentTimeMillis();
        ottenutoIntero = service.getTotaleCategoria(sorgente);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("Nella categoria '%s' ci sono %d pagine", sorgente, ottenutoIntero));
        System.out.println(String.format("Tempo impiegato per leggere le info della categoria '%s': %s", sorgente, getTime()));

        sorgente = CAT_1167;
        previstoIntero = 6;
        inizio = System.currentTimeMillis();
        ottenutoIntero = service.getTotaleCategoria(sorgente);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("Nella categoria '%s' ci sono %d pagine", sorgente, ottenutoIntero));
        System.out.println(String.format("Tempo impiegato per leggere le info della categoria '%s': %s", sorgente, getTime()));

        sorgente = CAT_1435;
        previstoIntero = 33;
        inizio = System.currentTimeMillis();
        ottenutoIntero = service.getTotaleCategoria(sorgente);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("Nella categoria '%s' ci sono %d pagine", sorgente, ottenutoIntero));
        System.out.println(String.format("Tempo impiegato per leggere le info della categoria '%s': %s", sorgente, getTime()));

        sorgente = CAT_1935;
        previstoIntero = TOT_1935;
        inizio = System.currentTimeMillis();
        ottenutoIntero = service.getTotaleCategoria(sorgente);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("Nella categoria '%s' ci sono %d pagine", sorgente, ottenutoIntero));
        System.out.println(String.format("Tempo impiegato per leggere le info della categoria '%s': %s", sorgente, getTime()));

        sorgente = CAT_ROMANI;
        previstoIntero = 78;
        inizio = System.currentTimeMillis();
        ottenutoIntero = service.getTotaleCategoria(sorgente);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("Nella categoria '%s' ci sono %d pagine", sorgente, ottenutoIntero));
        System.out.println(String.format("Tempo impiegato per leggere le info della categoria '%s': %s", sorgente, getTime()));
    }

    @Test
    @Order(5)
    @DisplayName("5 - Accesso alla categoria")
    public void getTotaleCategoria2() {
        System.out.println("5 - Accesso alla categoria.");
        System.out.println(VUOTA);

        sorgente = CAT_INESISTENTE;
        System.out.println("Accesso come anonymous alla categoria");
        ottenutoRisultato = service.getResultCat(sorgente, AETypeUser.anonymous);
        //        assertFalse(ottenutoRisultato.isValido());
        //        assertTrue(ottenutoRisultato.getErrorMessage().startsWith(NO_CAT));
        //        System.out.println(ottenutoRisultato.getErrorMessage());
        printResult(ottenutoRisultato);

        sorgente = CAT_1167;
        ottenutoRisultato = service.getResultCat(sorgente, AETypeUser.anonymous);
        printResult(ottenutoRisultato);
    }

    @Test
    @Order(6)
    @DisplayName("6 - Legge una lista di pageid di una categoria wiki")
    public void getLongCat() {
        System.out.println("6 - Legge una lista di pageid di una categoria wiki");
        AETypeUser userType = null;
        sorgente = CAT_INESISTENTE;
        previstoIntero = 0;
        ottenutoArrayLong = service.getLongCat(sorgente);
        assertNotNull(ottenutoArrayLong);
        assertEquals(previstoIntero, ottenutoArrayLong.size());

        //--senza specificare il type di user, in automatico mette anonymous
        //--esegue internamente tutti i cicli necessari, ognuno di 500 pagine
        sorgente = CAT_1167;
        userType = null;
        previstoIntero = 6;
        ottenutoArrayLong = service.getLongCat(sorgente, userType);
        assertNotNull(ottenutoArrayLong);
        assertEquals(previstoIntero, ottenutoArrayLong.size());
        System.out.println(VUOTA);
        System.out.println(String.format("La categoria '%s' contiene %d pageIds recuperati (come %s) in %s", sorgente, ottenutoArrayLong.size(), userType, getTime()));

        //--type di user=anonymous
        //--esegue internamente tutti i cicli necessari, ognuno di 500 pagine
        sorgente = CAT_1167;
        userType = AETypeUser.anonymous;
        previstoIntero = 6;
        ottenutoArrayLong = service.getLongCat(sorgente, userType);
        assertNotNull(ottenutoArrayLong);
        assertEquals(previstoIntero, ottenutoArrayLong.size());
        System.out.println(VUOTA);
        System.out.println(String.format("La categoria '%s' contiene %d pageIds recuperati (come %s) in %s", sorgente, ottenutoArrayLong.size(), userType, getTime()));

        //--type di user=user
        //--non essendo collegato va in errore
        sorgente = CAT_1167;
        userType = AETypeUser.user;
        previstoIntero = 0;
        ottenutoArrayLong = service.getLongCat(sorgente, userType);
        assertNotNull(ottenutoArrayLong);
        assertEquals(previstoIntero, ottenutoArrayLong.size());
        System.out.println(VUOTA);
        System.out.println(String.format("La categoria '%s' contiene %d pageIds recuperati (come %s) in %s", sorgente, ottenutoArrayLong.size(), userType, getTime()));

        //--type di user=bot
        //--non essendo collegato va in errore
        sorgente = CAT_1167;
        userType = AETypeUser.bot;
        previstoIntero = 0;
        ottenutoArrayLong = service.getLongCat(sorgente, userType);
        assertNotNull(ottenutoArrayLong);
        assertEquals(previstoIntero, ottenutoArrayLong.size());
        System.out.println(VUOTA);
        System.out.println(String.format("La categoria '%s' contiene %d pageIds recuperati (come %s) in %s", sorgente, ottenutoArrayLong.size(), userType, getTime()));

        //--senza specificare il type di user, in automatico mette anonymous
        //--esegue internamente tutti i cicli necessari, ognuno di 500 pagine
        sorgente = CAT_1713;
        userType = AETypeUser.anonymous;
        previstoIntero = 104;
        ottenutoArrayLong = service.getLongCat(sorgente, userType);
        assertNotNull(ottenutoArrayLong);
        assertEquals(previstoIntero, ottenutoArrayLong.size());
        System.out.println(VUOTA);
        System.out.println(String.format("La categoria '%s' contiene %d pageIds recuperati (come %s) in %s", sorgente, ottenutoArrayLong.size(), userType, getTime()));

        //        //--senza specificare il type di user, in automatico mette anonymous
        //        //--esegue internamente tutti i cicli necessari, ognuno di 500 pagine
        //        sorgente = CAT_1935;
        //        userType = AETypeUser.anonymous;
        //        previstoIntero = TOT_1935;
        //        ottenutoArrayLong = service.getLongCat(sorgente, userType);
        //        assertNotNull(ottenutoArrayLong);
        //        assertEquals(previstoIntero, ottenutoArrayLong.size());
        //        System.out.println(VUOTA);
        //        System.out.println(String.format("La categoria '%s' contiene %d pageIds recuperati (come %s) in %s", sorgente, ottenutoArrayLong.size(), userType, getTime()));

        //        //--senza specificare il type di user, in automatico mette anonymous
        //        //--esegue internamente tutti i cicli necessari, ognuno di 500 pagine
        //        sorgente = CAT_2020;
        //        userType = AETypeUser.anonymous;
        //        previstoIntero = TOT_2020;
        //        ottenutoArrayLong = service.getLongCat(sorgente, userType);
        //        assertNotNull(ottenutoArrayLong);
        //        assertEquals(previstoIntero, ottenutoArrayLong.size());
        //        System.out.println(VUOTA);
        //        System.out.println(String.format("La categoria '%s' contiene %d pageIds recuperati (come %s) in %s", sorgente, ottenutoArrayLong.size(), userType, getTime()));
    }

    //    @Test
    @Order(1)
    @DisplayName("1 - legge (come user) un template")
    public void leggePage() {
        sorgente = "Guido Rossi";
        previsto = "{{Bio";
        WrapPage wrap;

        wrap = service.leggePage(sorgente);
        assertEquals(AETypePage.testoConTmpl, wrap.getType());
        assertTrue(wrap.getTmpl().startsWith(previsto));

        System.out.println("1 - Legge il template Bio della pagina.");
        System.out.println("Usa una API con action=query SENZA bisogno di loggarsi");
        System.out.println("Legge (come user) una SINGOLA pagina dal server wiki");
        System.out.println("La pagina viene richiesta dal TITLE");
        System.out.println(String.format("La pagina wiki è: %s", sorgente));
        System.out.println("Faccio vedere solo l'inizio, perché troppo lungo");
        System.out.println("Sorgente restituito in formato visibile/leggibile");

        System.out.println(String.format("Tempo impiegato per leggere %d pagine: %s", cicli, getTime()));
        //        this.printWrapCat(wrap); @todo RIMETTERE
    }

    //    @Test
    @Order(2)
    @DisplayName("2 - legge una serie di wrapper di dati con una API action=query di Mediawiki")
    public void leggePages() {
        sorgente = "8956310|132555|134246|133958|8978579";
        List<WrapPage> wrapLista;
        previstoIntero = 5;

        wrapLista = service.leggePages(sorgente);
        assertNotNull(wrapLista);
        assertEquals(previstoIntero, wrapLista.size());

        System.out.println("2 - legge un wrapper di dati con una API action=query di Mediawiki");
        System.out.println("Usa una API con action=query SENZA bisogno di loggarsi");
        System.out.println("Legge (come user) una SERIE di pagine dal server wiki");
        System.out.println("Le pagine vengono richiesta dal PAGEIDs");
        System.out.println(String.format("Le pagine wiki sono: %s", sorgente));
        System.out.println(String.format("Tempo impiegato per leggere %d pagine: %s", previstoIntero, getTime()));

        System.out.println(VUOTA);
        System.out.println("Pagine recuperate:");
        for (WrapPage wrap : wrapLista) {
            //            this.printWrap(wrap); @todo RIMETTERE
        }
    }

    //    @Test
    @Order(3)
    @DisplayName("3 - Recupera (come user) 'lastModifica' di una serie di pageid")
    public void fixPages() {
        sorgente = "8956310|132555|134246|133958|8978579";
        List<MiniWrap> wrapLista;
        previstoIntero = 5;

        wrapLista = service.fixPages(null);
        assertNull(wrapLista);

        wrapLista = service.fixPages(VUOTA);
        assertNull(wrapLista);

        wrapLista = service.fixPages(sorgente);
        assertNotNull(wrapLista);
        assertEquals(previstoIntero, wrapLista.size());

        System.out.println("3 - Recupera (come user) 'lastModifica' di una serie di pageid");
        System.out.println("Usa una API con action=query SENZA bisogno di loggarsi");
        System.out.println("Recupera dalla urlRequest  pageid e timestamp");
        System.out.println(String.format("Le pagine wiki sono: %s", sorgente));
        System.out.println(String.format("Tempo impiegato per leggere %d pagine: %s", previstoIntero, getTime()));

        System.out.println(VUOTA);
        System.out.println("Pagine recuperate:");
        for (MiniWrap wrap : wrapLista) {
            System.out.print(wrap.getPageid());
            System.out.print(SEP);
            System.out.println(wrap.getLastModifica());
        }
    }


    //    @Test
    @Order(4)
    @DisplayName("4 - Recupera (come user) 'lastModifica' di una categoria")
    public void fixPages2() {
        List<MiniWrap> wrapLista;

        sorgente = CAT_1435;
        previstoIntero = 33;
        sorgente2 = service.getPageidsCat(sorgente);
        assertTrue(textService.isValid(sorgente2));

        wrapLista = service.fixPages(sorgente2);
        assertNotNull(wrapLista);
        assertEquals(previstoIntero, wrapLista.size());

        System.out.println("4 - Recupera (come user) 'lastModifica' di una categoria");
        System.out.println("Usa una API con action=query SENZA bisogno di loggarsi");
        System.out.println("Recupera dalla urlRequest 'pageid' e 'timestamp'");
        System.out.println(String.format("Tempo impiegato per leggere la categoria: %s", getTime()));

        System.out.println(VUOTA);
        System.out.println(String.format("La categoria è: %s", sorgente));
        System.out.println(String.format("Le pagine wiki recuperate sono: %s", wrapLista.size()));
        for (MiniWrap wrap : wrapLista) {
            System.out.print(wrap.getPageid());
            System.out.print(SEP);
            System.out.println(wrap.getLastModifica());
        }

        sorgente = CAT_1435;
        previstoIntero = 33;
        wrapLista = service.getMiniWrap(sorgente);
        assertNotNull(wrapLista);
        assertEquals(previstoIntero, wrapLista.size());

        System.out.println(VUOTA);
        System.out.println(String.format("Le pagine wiki sono: %s", wrapLista.size()));
        System.out.println("Usa una API con action=query SENZA bisogno di loggarsi");
        System.out.println(String.format("Tempo impiegato per leggere la categoria '%s' e controllare il 'timestamp' di %d pagine: %s", sorgente, previstoIntero, getTime()));

        sorgente = CAT_1591;
        previstoIntero = 67;
        wrapLista = service.getMiniWrap(sorgente);
        assertNotNull(wrapLista);
        assertEquals(previstoIntero, wrapLista.size());

        System.out.println(VUOTA);
        System.out.println(String.format("Le pagine wiki sono: %s", wrapLista.size()));
        System.out.println("Usa una API con action=query SENZA bisogno di loggarsi");
        System.out.println(String.format("Tempo impiegato per leggere la categoria '%s' e controllare il 'timestamp' di %d pagine: %s", sorgente, previstoIntero, getTime()));

        sorgente = CAT_1713;
        previstoIntero = 104;
        wrapLista = service.getMiniWrap(sorgente);
        assertNotNull(wrapLista);
        assertEquals(previstoIntero, wrapLista.size());

        System.out.println(VUOTA);
        System.out.println(String.format("Le pagine wiki sono: %s", wrapLista.size()));
        System.out.println("Usa una API con action=query SENZA bisogno di loggarsi");
        System.out.println(String.format("Tempo impiegato per leggere la categoria '%s' e controllare il 'timestamp' di %d pagine: %s", sorgente, previstoIntero, getTime()));

        sorgente = CAT_1935;
        previstoIntero = 1987;
        wrapLista = service.getMiniWrap(sorgente);
        assertNotNull(wrapLista);
        assertEquals(previstoIntero, wrapLista.size());

        System.out.println(VUOTA);
        System.out.println(String.format("Le pagine wiki sono: %s", wrapLista.size()));
        System.out.println("Usa una API con action=query SENZA bisogno di loggarsi");
        System.out.println(String.format("Tempo impiegato per leggere la categoria '%s' e controllare il 'timestamp' di %d pagine: %s", sorgente, previstoIntero, getTime()));
    }

    //    @Test
    @Order(5)
    @DisplayName("5 - ciclo di download")
    public void leggePage2() {
        WrapPage wrap = null;
        int cont = 0;
        List<String> lista = service.getTitleCat(CAT_1167);
        assertNotNull(lista);

        System.out.println(String.format("Trovate %s pagine", lista.size()));
        for (String wikiTitle : lista) {
            wrap = service.leggePage(wikiTitle);
            cont++;
            if (wrap.isValida()) {
                System.out.println(String.format("%s%s%s è ok", cont, SEP, wrap.getTitle()));
            }
            else {
                System.out.println(String.format("%s%sAlla pagina '%s' manca il tmpl Bio", cont, SEP, wrap.getTitle()));
            }
        }
    }

    //    @Test
    @Order(6)
    @DisplayName("6 - ciclo di download")
    public void leggePage3() {
        List<WrapCat> listaWrapDiControlloDelPageid = null;
        List<Long> listaPageIdsCategoria = null;
        List<MiniWrap> listaMiniWrap = null;
        List<WrapPage> listaWrapPage = null;
        List<Long> listaPageIdsDaLeggere = null;
        String stringPageIds = VUOTA;

        //--solo per controllo del titolo nel test. Normalmente non serve
        listaWrapDiControlloDelPageid = service.getWrapCat(CAT_1167);
        assertNotNull(listaWrapDiControlloDelPageid);
        System.out.println(String.format("Lista di %d WrapCat con i pageIds per controllo", listaWrapDiControlloDelPageid.size()));
        System.out.println("Solo per controllo del titolo nel test. Normalmente non serve");
        System.out.println(VUOTA);
        printWrapCat(listaWrapDiControlloDelPageid);

        //--A - Parte dalla lista di tutti i (long)pageIds della categoria
        //--nel caso reale sono circa mezzo milione
        listaPageIdsCategoria = service.getLongCat(CAT_1167);
        assertNotNull(listaPageIdsCategoria);
        System.out.println(VUOTA);
        System.out.println(VUOTA);
        System.out.println("A - Parte dalla lista di tutti i (long) pageIds della categoria");
        System.out.println("Nel caso reale sono circa mezzo milione");
        System.out.println(String.format("Lista di %d pageIds (tutte quelle della categoria)", listaPageIdsCategoria.size()));
        printLong(listaPageIdsCategoria);

        //--B - Usa tutta la lista di pageIds e si recupera una lista (stessa lunghezza) di miniWrap
        listaMiniWrap = service.getMiniWrap(CAT_1167, listaPageIdsCategoria);
        assertNotNull(listaMiniWrap);
        System.out.println(VUOTA);
        System.out.println(VUOTA);
        System.out.println("B - Usa tutta la lista di pageIds e recupera una lista (stessa lunghezza) di miniWrap");
        System.out.println(String.format("Lista di %d miniWrap corrispondente alla list di %d long (l'ordine potrebbe essere diverso)", listaMiniWrap.size(), listaPageIdsCategoria.size()));
        printMiniWrap(listaMiniWrap);

        //--C - Elabora la lista di miniWrap e costruisce una lista di pageIds da leggere
        //--Vengono usati quelli che hanno un miniWrap.pageid senza corrispondente bio.pageid nel mongoDb
        //--Vengono usati quelli che hanno miniWrap.lastModifica maggiore di bio.lastModifica
        //--questi controlli vengono saltati in questa testUnit
        //--dalla lista risultante di MiniWrap, si costruisce una lista di pageIds da leggere
        //--si costruisce una lista di WrapPage valide
        listaPageIdsDaLeggere = service.elaboraMiniWrap(listaMiniWrap);
        listaWrapPage = service.leggePages(listaPageIdsDaLeggere);
        System.out.println(VUOTA);
        System.out.println(VUOTA);
        System.out.println("C - Elabora la lista di miniWrap e costruisce una lista di pageIds da leggere");
        System.out.println("Vengono usati quelli che hanno un miniWrap.pageid senza corrispondente bio.pageid nel mongoDb");
        System.out.println("Vengono usati quelli che hanno miniWrap.lastModifica maggiore di bio.lastModifica");
        System.out.println("questi controlli vengono saltati in questa testUnit");
        System.out.println("dalla elaborazione della lista di miniWrap, risulta una lista di pageIds da leggere");
        System.out.println(String.format("Lista originaria di %d miniWrap", listaMiniWrap.size()));
        System.out.println(String.format("Lista elaborata di %d pageIds", listaPageIdsDaLeggere.size()));
        System.out.println(String.format("Lista scaricata di %d wrapPage valide (con tmplBio)", listaWrapPage.size()));
        printWrapPage(listaWrapPage);
    }

    private void printEsiste(String wikiTitle, boolean esiste) {
        String status = esiste ? "esiste" : "non esiste";
        System.out.println(VUOTA);
        System.out.println(String.format("La pagina/categoria '%s'%s%s", sorgente, FORWARD, status));
    }


    private void printResult(AIResult result) {
        int max = 150;
        String risposta = result.getResponse();
        risposta = risposta.length() < max ? risposta : risposta.substring(0, Math.min(max, risposta.length()));
        System.out.println(VUOTA);
        System.out.println(String.format("Result di: %s", result.getWikiTitle()));
        System.out.println(String.format("Risultato: %s", result.isValido() ? "valido" : "errato"));
        System.out.println(String.format("WikiTitle: %s", result.getWikiTitle()));
        System.out.println(String.format("Url: %s", result.getUrlRequest()));
        System.out.println(String.format("ErrorCode: %s", result.getErrorCode()));
        System.out.println(String.format("ErrorMessage: %s", result.getErrorMessage()));
        System.out.println(String.format("ValidMessage: %s", result.getValidMessage()));
        System.out.println(String.format("Response: %s", risposta));
        System.out.println(String.format("Value: %d", result.getValue()));
        System.out.println(String.format("Tempo impiegato per leggere le info della categoria: %s", getTime()));
    }


    private void printWrapCat(List<WrapCat> wrapLista) {
        int pos = 0;
        for (WrapCat wrap : wrapLista) {
            pos++;
            System.out.print(pos);
            System.out.print(") ");
            System.out.print(wrap.getPageid());
            System.out.print(SEP);
            System.out.println(wrap.getTitle());
        }
    }

    private void printLong(List<Long> listaPageIds) {
        int pos = 0;
        for (long lungo : listaPageIds) {
            pos++;
            System.out.print(pos);
            System.out.print(") ");
            System.out.println(lungo);
        }
    }


    private void printMiniWrap(List<MiniWrap> listaMiniWrap) {
        int pos = 0;
        for (MiniWrap wrap : listaMiniWrap) {
            pos++;
            System.out.print(pos);
            System.out.print(") ");
            System.out.print(wrap.getPageid());
            System.out.print(SEP);
            System.out.println(dateService.get(wrap.getLastModifica()));
        }
    }

    private void printWrapPage(List<WrapPage> listaWrapPage) {
        int pos = 0;
        for (WrapPage wrap : listaWrapPage) {
            pos++;
            System.out.print(pos);
            System.out.print(") ");
            System.out.print(wrap.getPageid());
            System.out.print(SEP);
            System.out.println(wrap.getTitle());
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
    }

}