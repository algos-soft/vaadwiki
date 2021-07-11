package it.algos.unit;

import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.wrapper.*;
import it.algos.vaadflow14.wiki.*;
import org.json.simple.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.*;

import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: sab, 12-set-2020
 * Time: 20:25
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("testAllValido")
@DisplayName("Test di controllo per i collegamenti base di wikipedia.")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WikiApiServiceTest extends ATest {

    public static final String PAGINA_PIOZZANO = "Piozzano";

    public static final String PAGINA_TEST = "Utente:Gac/T17";

    public static final String PAGINA_NO_ASCI = "Roman Protasevič";

    public static final String PAGINA_INESISTENTE = "Roman Protellino";

    public static final String PAGINA_DISAMBIGUA = "Rossi";

    public static final String PAGINA_REDIRECT = "Regno di Napoli (1805-1815)";

    public static final String TEMPL_BIO = "Bio";

    public static final long PAGINA_TEST_PAGEID = 8956310;

    public static final String CAT_INESISTENTE = "Nati nel 3435";

    public static final String CAT_1435 = "Nati nel 1435";

    public static final String CAT_1935 = "Nati nel 1935";

    public static final String CAT_ROMA = "Nati a Roma";

    public static final int TOT_CAT_1935 = 1985;

    public static final String CAT_ROMANI = "Personaggi della storia romana";

    /**
     * Classe principale di riferimento <br>
     */
    @InjectMocks
    AWikiApiService service;


    private List<List<String>> listaGrezza;

    private List<WrapDueStringhe> listaWrap;

    private List<WrapTreStringhe> listaWrapTre;

    private List<WrapQuattro> listaWrapQuattro;

    private WrapDueStringhe dueStringhe;


    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpAll() {
        super.setUpStartUp();

        MockitoAnnotations.initMocks(this);
        MockitoAnnotations.initMocks(service);
        Assertions.assertNotNull(service);
        service.text = text;
        service.array = array;
        service.web = web;
        service.html = html;
    }


    /**
     * Qui passa ad ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    void setUpEach() {
        super.setUp();

        listaGrezza = null;
        listaWrap = null;
        listaWrapTre = null;
        dueStringhe = null;
    }


    @Test
    @Order(1)
    @DisplayName("1 - legge il testo grezzo in formato html")
    public void leggeHtml() {
        sorgente = PAGINA_PIOZZANO;
        previsto = "<!DOCTYPE html><html class=\"client-nojs\" lang=\"it\" dir=\"ltr\"><head><meta charset=\"UTF-8\"/><title>Piozzano - Wikipedia</title>";
        previsto2 = ";});</script></body></html>";

        for (int k = 0; k < cicli; k++) {
            ottenuto = service.leggeHtml(sorgente);
        }
        assertTrue(text.isValid(ottenuto));
        assertTrue(ottenuto.startsWith(previsto));
        assertTrue(ottenuto.endsWith(previsto2));

        System.out.println("1 - legge il testo grezzo in formato html");
        System.out.println("Rimanda a un metodo di AWebService");
        System.out.println(String.format("Legge il sorgente di una pagina wiki"));
        System.out.println(String.format("La pagina wiki è: %s", sorgente));
        System.out.println("Risultato restituito in formato html");
        System.out.println(String.format("Tempo impiegato per leggere %d pagine: %s", cicli, getTime()));
        System.out.println("Faccio vedere solo l'inizio e la fine, perché tutto sarebbe troppo lungo");

        System.out.println(VUOTA);
        System.out.println("Inizio");
        System.out.println(ottenuto.substring(0, previsto.length()));
        System.out.println(VUOTA);
        System.out.println("Fine");
        System.out.println(ottenuto.substring(ottenuto.length() - previsto2.length()));
    }


    @Test
    @Order(2)
    @DisplayName("2 - legge in formato JSON con una API query di Mediawiki")
    public void leggeJson() {
        sorgente = PAGINA_TEST;
        previsto = "{\"batchcomplete\":true,\"query\":{\"pages\":[{\"pageid\":8956310,\"ns\":2,\"title\":\"Utente:Gac/T17\"";

        ottenutoRisultato = service.leggeJson(sorgente);
        assertTrue(ottenutoRisultato.isValido());

        ottenuto = service.leggeJsonTxt(sorgente);
        assertNotNull(ottenuto);
        assertTrue(text.isValid(ottenuto));
        assertTrue(ottenuto.equals(ottenutoRisultato.getText()));

        System.out.println("2 - legge in formato JSON con una API query di Mediawiki");
        System.out.println("Legge una pagina wiki con una query API Mediawiki");
        System.out.println(String.format("La pagina wiki è: %s", sorgente));
        System.out.println("Risultato restituito in formato JSON");
        System.out.println(String.format("Tempo impiegato per leggere %d pagine: %s", cicli, getTime()));

        System.out.println(VUOTA);
        System.out.println("Query");
        System.out.println(ottenutoRisultato.getValidationMessage());
        System.out.println(VUOTA);
        System.out.println("Risultato completo");
        System.out.println(ottenutoRisultato.getText());
    }


    @Test
    @Order(3)
    @DisplayName("3 - legge in formato visibile con una API action=query di Mediawiki")
    public void leggeQuery() {
        System.out.println("3 - legge in formato visibile con una API action=query di Mediawiki");
        System.out.println("Legge (come user) una pagina dal server wiki");
        System.out.println("Usa una API con action=query SENZA bisogno di loggarsi");
        System.out.println("Recupera dalla urlRequest tutti i dati della pagina");
        System.out.println("Estrae il testo in linguaggio wiki visibile/leggibile");
        System.out.println("Elaborazione della urlRequest leggermente più complessa di leggeParse");
        System.out.println("Tempo di download leggermente più corto di leggeParse");

        sorgente = PAGINA_TEST;
        previsto = "Solo test";

        ottenutoRisultato = service.leggeQuery(sorgente);
        assertTrue(ottenutoRisultato.isValido());

        ottenuto = service.leggeQueryTxt(sorgente);
        assertNotNull(ottenuto);
        assertTrue(text.isValid(ottenuto));
        assertTrue(ottenuto.equals(ottenutoRisultato.getText()));
        assertEquals(previsto, ottenutoRisultato.getText());

        System.out.println(VUOTA);
        System.out.println(String.format("La pagina wiki è: %s", sorgente));
        System.out.println(String.format("La query è: %s", ottenutoRisultato.getMessage()));
        System.out.println(String.format("Tempo impiegato per leggere %d pagine: %s", cicli, getTime()));
        System.out.println(ottenutoRisultato.getText());

        sorgente = PAGINA_NO_ASCI;
        previsto = "{{In corso|biografie}}\n{{P|la voce include fonti poco accessibili";

        for (int k = 0; k < cicli; k++) {
            ottenutoRisultato = service.leggeQuery(sorgente);
        }
        assertTrue(ottenutoRisultato.isValido());
        assertTrue(ottenutoRisultato.getText().startsWith(previsto));

        System.out.println(VUOTA);
        System.out.println(String.format("La pagina wiki è: %s", sorgente));
        System.out.println(String.format("La query è: %s", ottenutoRisultato.getMessage()));
        System.out.println("Faccio vedere solo l'inizio, perché troppo lungo");

        System.out.println(VUOTA);
        System.out.println("Query");
        System.out.println(ottenutoRisultato.getValidationMessage());
        System.out.println(VUOTA);
        System.out.println("Inizio");
        System.out.println(ottenutoRisultato.getText().substring(0, previsto.length()));
    }

    @Test
    @Order(4)
    @DisplayName("4 - estrae una mappa (title,pageid,text) da action=parse di Mediawiki")
    public void leggeMappaParse() {
        sorgente = PAGINA_NO_ASCI;
        previstoIntero = 4;
        Map mappa = service.leggeMappaParse(sorgente);

        assertNotNull(mappa);
        assertEquals(previstoIntero, mappa.size());

        System.out.println("4 - Legge (come user) una mappa (title,pageid,text) dal server wiki");
        System.out.println("Legge (come user) una mappa (title,pageid,text) dal server wiki");
        System.out.println(String.format("La pagina wiki è: %s", sorgente));
        System.out.println("Usa una API con action=parse SENZA bisogno di loggarsi");
        System.out.println("Recupera dalla urlRequest title, pageid e wikitext");
        System.out.println("Estrae il testo in linguaggio wiki visibile/leggibile");
        System.out.println(String.format("La mappa contiene %s elementi", mappa.size()));
        System.out.println("Elaborazione della urlRequest leggermente meno complessa di leggeQuery");
        System.out.println("Tempo di download leggermente più lungo di leggeQuery");
        System.out.println(String.format("Tempo impiegato per leggere %d pagine: %s", cicli, getTime()));
        System.out.println("Faccio vedere solo l'inizio del wikitext, perché troppo lungo");

        System.out.println(VUOTA);
        System.out.println(KEY_MAPPA_DOMAIN + SEP + mappa.get(KEY_MAPPA_DOMAIN));
        System.out.println(KEY_MAPPA_TITLE + SEP + mappa.get(KEY_MAPPA_TITLE));
        System.out.println(KEY_MAPPA_PAGEID + SEP + mappa.get(KEY_MAPPA_PAGEID));
        System.out.println(KEY_MAPPA_TEXT + SEP + ((String) mappa.get(KEY_MAPPA_TEXT)).substring(0, WIDTH));
        System.out.println(VUOTA);
    }

    @Test
    @Order(5)
    @DisplayName("5 - legge in formato visibile con una API action=parse di Mediawiki")
    public void leggeParseText() {
        sorgente = PAGINA_PIOZZANO;
        previsto = "{{Divisione amministrativa\n" + "|Nome=Piozzano";
        previsto2 = "[[Categoria:Piozzano| ]]";

        for (int k = 0; k < cicli; k++) {
            ottenuto = service.leggeParseText(sorgente);
        }
        assertNotNull(ottenuto);
        assertTrue(text.isValid(ottenuto));
        assertTrue(ottenuto.startsWith(previsto));
        assertTrue(ottenuto.endsWith(previsto2));

        System.out.println("5 - legge in formato visibile con una API action=parse di Mediawiki");
        System.out.println("Legge (come user) una pagina dal server wiki");
        System.out.println("Usa una API con action=parse SENZA bisogno di loggarsi");
        System.out.println("Estrae il testo in linguaggio wiki visibile/leggibile");
        System.out.println("Elaborazione della urlRequest leggermente meno complessa di leggeQuery");
        System.out.println("Tempo di download leggermente più lungo di leggeQuery");
        System.out.println("Sorgente restituito in formato visibile/leggibile");
        System.out.println(String.format("Tempo impiegato per leggere %d pagine: %s", cicli, getTime()));
        System.out.println("Faccio vedere solo l'inizio, perché troppo lungo");

        System.out.println(VUOTA);
        System.out.println("Inizio");
        System.out.println(ottenuto.substring(0, previsto.length()));
        System.out.println(VUOTA);
        System.out.println("Fine");
        System.out.println(ottenuto.substring(ottenuto.length() - previsto2.length()));
    }

    @Test
    @Order(6)
    @DisplayName("6 - legge un wrapper di dati con una API action=query di Mediawiki")
    public void leggePage() {
        WrapPage wrap;
        System.out.println("6 - legge un wrapper di dati con una API action=query di Mediawiki");
        System.out.println("Legge (come user) una SINGOLA pagina dal server wiki");
        System.out.println("La pagina viene richiesta dal TITLE");
        System.out.println("Usa una API con action=query SENZA bisogno di loggarsi");
        System.out.println("Estrae il testo in linguaggio wiki visibile/leggibile");
        System.out.println("Sorgente restituito in formato visibile/leggibile");

        sorgente = PAGINA_INESISTENTE;
        wrap = service.leggePage(sorgente);
        assertNotNull(wrap);
        assertEquals(AETypePage.nonEsiste, wrap.getType());
        assertFalse(wrap.isValida());
        this.printWrap(wrap);

        sorgente = PAGINA_DISAMBIGUA;
        wrap = service.leggePage(sorgente);
        assertNotNull(wrap);
        assertEquals(AETypePage.disambigua, wrap.getType());
        assertFalse(wrap.isValida());
        this.printWrap(wrap);

        sorgente = PAGINA_REDIRECT;
        wrap = service.leggePage(sorgente);
        assertNotNull(wrap);
        assertEquals(AETypePage.redirect, wrap.getType());
        assertFalse(wrap.isValida());
        this.printWrap(wrap);
    }

    @Test
    @Order(7)
    @DisplayName("7 - legge un wrapper di dati con una API action=query di Mediawiki")
    public void leggePageID() {
        WrapPage wrap;
        System.out.println("7 - legge un wrapper di dati con una API action=query di Mediawiki");
        System.out.println("Legge (come user) una SINGOLA pagina dal server wiki");
        System.out.println("La pagina viene richiesta dal TITLE");
        System.out.println("Usa una API con action=query SENZA bisogno di loggarsi");
        System.out.println("Estrae il testo in linguaggio wiki visibile/leggibile");
        System.out.println("Sorgente restituito in formato visibile/leggibile");

        sorgente = PAGINA_NO_ASCI;
        wrap = service.leggePage(sorgente, TEMPL_BIO);
        assertNotNull(wrap);
        assertEquals(AETypePage.testoConTmpl, wrap.getType());
        assertTrue(wrap.isValida());
        this.printWrap(wrap);

        System.out.println(String.format("Tempo impiegato per leggere %d pagine: %s", cicli, getTime()));
        this.printWrap(wrap);
    }

    @Test
    @Order(8)
    @DisplayName("8 - legge un wrapper di dati con una API action=query di Mediawiki")
    public void creaPage() {
        WrapPage wrap;
        System.out.println("8 - legge un wrapper di dati con una API action=query di Mediawiki");
        System.out.println("Legge (come user) una SINGOLA pagina dal server wiki");
        System.out.println("La pagina viene richiesta dal TITLE");
        System.out.println("Usa una API con action=query SENZA bisogno di loggarsi");
        System.out.println("Estrae il testo in linguaggio wiki visibile/leggibile");
        System.out.println("Sorgente restituito in formato visibile/leggibile");

        sorgente = PAGINA_INESISTENTE;
        wrap = service.leggePage(sorgente, TEMPL_BIO);
        assertNotNull(wrap);
        assertFalse(wrap.isValida());
        //        assertFalse(wrap.isTemplate());

        System.out.println(String.format("Tempo impiegato per leggere %d pagine: %s", cicli, getTime()));
        this.printWrap(wrap);
    }

    @Test
    @Order(9)
    @DisplayName("9 - legge una serie di wrapper di dati con una API action=query di Mediawiki")
    public void leggePages() {
        sorgente = "8956310|132555|134246|133958|8978579";
        List<WrapPage> wrapLista;
        previstoIntero = 4;

        wrapLista = service.leggePages(sorgente, "Bio");
        assertNotNull(wrapLista);
        assertEquals(previstoIntero, wrapLista.size());

        System.out.println("9 - legge un wrapper di dati con una API action=query di Mediawiki");
        System.out.println("Legge (come user) una SERIE di pagine dal server wiki");
        System.out.println("Le pagine vengono richiesta dal PAGEIDs");
        System.out.println(String.format("Le pagine wiki sono: %s", sorgente));
        System.out.println("Usa una API con action=query SENZA bisogno di loggarsi");
        System.out.println(String.format("Tempo impiegato per leggere %d pagine: %s", previstoIntero, getTime()));

        System.out.println(VUOTA);
        System.out.println("Pagine recuperate:");
        for (WrapPage wrap : wrapLista) {
            this.printWrap(wrap);
        }
    }


    @Test
    @Order(10)
    @DisplayName("10 - estrae un array di 'pages' da una query")
    public void getArrayPagine() {
        sorgente = PAGINA_TEST;
        JSONArray jsonArray = null;
        previsto = "{\"batchcomplete\":true,\"query\":{\"pages\":[{\"pageid\":8956310,\"ns\":2,\"title\":\"Utente:Gac/T17\",\"revisions\":[{\"revid\":120519028,\"parentid\":0,\"timestamp\":\"2021-05-08T16:44:17Z\",\"slots\":{\"main\":{\"contentmodel\":\"wikitext\",\"contentformat\":\"text/x-wiki\",\"content\":\"Solo test\"}}}]}]}}";
        previstoIntero = 1;

        ottenutoRisultato = service.leggeJson(sorgente);
        assertTrue(ottenutoRisultato.isValido());
        ottenuto = ottenutoRisultato.getText();
        assertTrue(ottenuto.equals(previsto));

        System.out.println("10 - estrae un array di 'pages' da una query");
        System.out.println(VUOTA);
        System.out.println("Query");
        System.out.println(ottenutoRisultato.getValidationMessage());
        System.out.println(VUOTA);
        System.out.println("Risposta json");
        System.out.println(ottenutoRisultato.getText());

        jsonArray = service.getArrayPagine(ottenuto);
        assertNotNull(jsonArray);
        assertEquals(previstoIntero, jsonArray.size());
        System.out.println("Legge il contenuto in formato JSON di una pagina wiki");
        System.out.println(String.format("Tempo impiegato per leggere %d pagine: %s", cicli, getTime()));
        System.out.println(VUOTA);
        System.out.println(ottenuto);
    }


    @Test
    @Order(11)
    @DisplayName("11 - estrae un singolo oggetto JSON da una query")
    public void getObjectPage() {
        sorgente = PAGINA_TEST;
        JSONObject jsonObject = null;
        previstoIntero = 1;

        ottenuto = service.leggeJsonTxt(sorgente);
        assertTrue(text.isValid(ottenuto));
        jsonObject = service.getObjectPage(ottenuto);
        assertNotNull(jsonObject);
        System.out.println("estrae un singolo oggetto JSON da una query");
        System.out.println(VUOTA);
        System.out.println(jsonObject);
    }

    @Test
    @Order(12)
    @DisplayName("12 - crea una mappa da un singolo oggetto JSON")
    public void getMappaJSON() {
        sorgente = PAGINA_TEST;
        JSONObject jsonObject = null;
        previstoIntero = 7;
        sorgente2 = "content";

        ottenuto = service.leggeJsonTxt(sorgente);
        jsonObject = service.getObjectPage(ottenuto);
        mappa = service.getMappaJSON(jsonObject);
        assertNotNull(mappa);
        assertEquals(previstoIntero, mappa.size());
        ottenuto = (String) mappa.get(sorgente2);
        assertTrue(text.isValid(ottenuto));
        printMappaPar(mappa);
    }

    @Test
    @Order(13)
    @DisplayName("13 - crea una wikiPage da una mappa")
    public void getWikiPageFromMappa() {
        sorgente = PAGINA_TEST;
        JSONObject jsonObject = null;
        WikiPage wikiPage;
        sorgente2 = "content";

        ottenuto = service.leggeJsonTxt(sorgente);
        jsonObject = service.getObjectPage(ottenuto);
        mappa = service.getMappaJSON(jsonObject);
        assertNotNull(mappa);
        wikiPage = service.getWikiPageFromMappa(mappa);
        assertNotNull(wikiPage);
        ottenuto = wikiPage.getContent();
        assertTrue(text.isValid(ottenuto));
        this.printWikiPage(wikiPage);
    }


    @Test
    @Order(14)
    @DisplayName("14 - crea una wikiPage in risposta ad una query")
    public void getWikiPageFromTitle() {
        sorgente = PAGINA_TEST;
        WikiPage wikiPage;
        sorgente2 = "content";

        wikiPage = service.getWikiPageFromTitle(sorgente);
        assertNotNull(wikiPage);
        ottenuto = wikiPage.getContent();
        assertTrue(text.isValid(ottenuto));
        this.printWikiPage(wikiPage);
    }

    @Test
    @Order(15)
    @DisplayName("15 - Legge il testo wiki della pagina wiki passando da Page")
    public void getContent() {
        sorgente = PAGINA_TEST;

        ottenuto = service.getContent(sorgente);
        assertTrue(text.isValid(ottenuto));
        System.out.println("Legge il testo wiki della pagina wiki.");
        System.out.println("Usa le API base SENZA loggarsi.");
        System.out.println("Sorgente restituito in formato visibile/leggibile");
        System.out.println(String.format("Tempo impiegato per leggere %d pagine: %s", cicli, getTime()));
        System.out.println(VUOTA);
        System.out.println(ottenuto);
    }

    @Test
    @Order(16)
    @DisplayName("16 - legge (come user) una table wiki")
    public void leggeTable() {
        sorgente = "ISO 3166-2:IT";
        previsto = "{| class=\"wikitable sortable\"";
        previsto2 = "| <code>IT-34</code>\n| {{bandiera|Veneto|nome}}\n|}";

        //--regione
        ottenuto = service.leggeTable(sorgente);
        assertTrue(text.isValid(ottenuto));
        assertTrue(ottenuto.startsWith(previsto));
        assertTrue(ottenuto.endsWith(previsto2));
        System.out.println("Legge una tabella wiki completa");
        System.out.println(VUOTA);
        System.out.println(ottenuto);

        //--provincia
        previsto2 = "| <code>IT-VE</code>\n| {{IT-VE}}\n| [[Veneto]] (<code>34</code>)\n|}";
        try {
            ottenuto = service.leggeTable(sorgente, 2);
        } catch (Exception unErrore) {
        }

        assertTrue(text.isValid(ottenuto));
        assertTrue(ottenuto.startsWith(previsto));
        assertTrue(ottenuto.endsWith(previsto2));
        System.out.println(VUOTA);
        System.out.println("Legge una tabella wiki completa");
        System.out.println(VUOTA);
        System.out.println(ottenuto);
    }

    @Test
    @Order(17)
    @DisplayName("17 - legge (come user) un modulo")
    public void leggeModulo() {
        sorgente = "Modulo:Bio/Plurale_attività";
        previsto = "{\n[\"abate\"] =";
        previsto2 = "[\"zoologo\"] = \"zoologi\"\n}";

        ottenuto = service.leggeModulo(sorgente);
        assertTrue(text.isValid(ottenuto));
        assertTrue(ottenuto.startsWith(previsto));
        assertTrue(ottenuto.endsWith(previsto2));
        System.out.println("Legge un modulo wiki. Non lo faccio vedere perché troppo lungo");
    }


    @Test
    @Order(18)
    @DisplayName("18 - legge (come user) la mappa del modulo Attività")
    public void leggeMappaModulo() {
        sorgente = "Modulo:Bio/Plurale_attività";
        sorgente2 = "abate";
        sorgente3 = "badessa";
        previsto = "abati e badesse";

        mappaOttenuta = service.leggeMappaModulo(sorgente);
        assertNotNull(mappaOttenuta);
        ottenuto = mappaOttenuta.get(sorgente2);
        assertTrue(text.isValid(ottenuto));
        assertEquals(ottenuto, previsto);
        ottenuto = mappaOttenuta.get(sorgente3);
        assertTrue(text.isValid(ottenuto));
        assertEquals(ottenuto, previsto);
        System.out.println("Legge la mappa di un modulo wiki. Non lo faccio vedere perché troppo lungo");
    }


    @Test
    @Order(19)
    @DisplayName("19 - legge (come user) la mappa del modulo Attività/Genere")
    public void leggeMappaModulo2() {
        sorgente = "Modulo:Bio/Plurale_attività_genere";
        sorgente2 = "abate";
        previsto = "\"abati\",\"M\"";

        mappaOttenuta = service.leggeMappaModulo(sorgente);
        assertNotNull(mappaOttenuta);
        ottenuto = mappaOttenuta.get(sorgente2);
        assertTrue(text.isValid(ottenuto));
        assertEquals(ottenuto, previsto);
        System.out.println("Legge la mappa di un modulo wiki. Non lo faccio vedere perché troppo lungo");
    }

    @Test
    @Order(20)
    @DisplayName("20 - legge (come user) un template")
    public void leggeTmpl() {
        sorgente = PAGINA_PIOZZANO;
        previsto = VUOTA;
        sorgente2 = "Divisione amministrativa";

        ottenuto = service.leggeTmpl(VUOTA, VUOTA);
        assertTrue(text.isEmpty(ottenuto));
        assertEquals(ottenuto, previsto);

        ottenuto = service.leggeTmpl(sorgente, VUOTA);
        assertTrue(text.isEmpty(ottenuto));
        assertEquals(ottenuto, previsto);

        previsto = "{{Divisione amministrativa";
        ottenuto = service.leggeTmpl(sorgente, sorgente2);
        assertTrue(text.isValid(ottenuto));
        assertTrue(ottenuto.startsWith(previsto));
        System.out.println("Legge un template amministrativo");
        System.out.println(VUOTA);
        System.out.println(ottenuto);

        sorgente = "Guido Rossi";
        sorgente2 = "Bio";
        previsto = "{{Bio";
        ottenuto = service.leggeTmpl(sorgente, sorgente2);
        assertTrue(text.isValid(ottenuto));
        assertTrue(ottenuto.startsWith(previsto));
        System.out.println(VUOTA);
        System.out.println(VUOTA);
        System.out.println("Legge un template bio");
        System.out.println(VUOTA);
        System.out.println(ottenuto);
    }

    @Test
    @Order(21)
    @DisplayName("21 - legge una lista di WrapCat (come user) di una categoria")
    public void leggeCategoria() {
        List<WrapCat> lista;
        System.out.println("21 - legge una lista di WrapCat (come user) di una categoria");

        sorgente = CAT_INESISTENTE;
        previstoIntero = 0;
        lista = service.getWrapCat(sorgente);
        assertNotNull(lista);
        assertEquals(lista.size(), previstoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("Categoria: %s", sorgente));
        System.out.println(String.format("Nessuna pagina"));

        sorgente = CAT_1435;
        previstoIntero = 33;
        inizio = System.currentTimeMillis();
        lista = service.getWrapCat(sorgente);
        assertNotNull(lista);
        assertEquals(lista.size(), previstoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("Categoria: %s", sorgente));
        System.out.println(String.format("Ce ne sono %s", lista.size()));
        System.out.println(String.format("Tempo impiegato per leggere la categoria: %s", getTime()));
        printCat(lista);

        sorgente = CAT_1935;
        previstoIntero = TOT_CAT_1935;
        inizio = System.currentTimeMillis();
        lista = service.getWrapCat(sorgente);
        assertNotNull(lista);
        //        assertEquals(lista.size(), previstoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("Categoria: %s", sorgente));
        System.out.println(String.format("Ce ne sono %s", lista.size()));
        System.out.println(String.format("Tempo impiegato per leggere la categoria: %s", getTime()));
        System.out.println("Non faccio vedere le pagine perché sono troppe");
    }


    @Test
    @Order(22)
    @DisplayName("22 - WrapCat (come user) pages/subcat/files di una categoria")
    public void leggeCategoria2() {
        List<WrapCat> lista;
        AECatType typeCat;
        System.out.println("22 - legge (come user) pages/subcat/files di una categoria");

        sorgente = CAT_ROMANI;

        typeCat = AECatType.file;
        previstoIntero = 0;
        inizio = System.currentTimeMillis();
        lista = service.getWrapCat(sorgente, typeCat);
        assertNotNull(lista);
        assertEquals(lista.size(), previstoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("Categoria: %s Ricerca di: %s", sorgente, typeCat.getTag()));
        System.out.println(String.format("Non ce ne sono"));

        typeCat = AECatType.subcat;
        previstoIntero = 60;
        inizio = System.currentTimeMillis();
        lista = service.getWrapCat(sorgente, typeCat);
        assertNotNull(lista);
        assertEquals(lista.size(), previstoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("Categoria: %s Ricerca di: %s", sorgente, typeCat.getTag()));
        System.out.println(String.format("Ce ne sono %s", lista.size()));
        System.out.println(String.format("Tempo impiegato per leggere la categoria: %s", getTime()));

        typeCat = AECatType.page;
        previstoIntero = 78;
        inizio = System.currentTimeMillis();
        lista = service.getWrapCat(sorgente, typeCat);
        assertNotNull(lista);
        assertEquals(lista.size(), previstoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("Categoria: %s Ricerca di: %s", sorgente, typeCat.getTag()));
        System.out.println(String.format("Ce ne sono %s", lista.size()));
        System.out.println(String.format("Tempo impiegato per leggere la categoria: %s", getTime()));

        previstoIntero = 78;
        inizio = System.currentTimeMillis();
        lista = service.getWrapCat(sorgente);
        assertNotNull(lista);
        assertEquals(lista.size(), previstoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("Categoria: %s Ricerca generica che automaticamente cerca solo %s", sorgente, typeCat.getTag()));
        System.out.println(String.format("Ce ne sono %s", lista.size()));
        System.out.println(String.format("Tempo impiegato per leggere la categoria: %s", getTime()));

        typeCat = AECatType.all;
        previstoIntero = 138;
        inizio = System.currentTimeMillis();
        lista = service.getWrapCat(sorgente, typeCat);
        assertNotNull(lista);
        assertEquals(lista.size(), previstoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("Categoria: %s Ricerca di: %s", sorgente, typeCat.getTag()));
        System.out.println(String.format("Ce ne sono %s", lista.size()));
        System.out.println(String.format("Tempo impiegato per leggere la categoria: %s", getTime()));
    }

    @Test
    @Order(23)
    @DisplayName("23 - legge una lista di pageid (come user) di una categoria")
    public void getLongCat() {
        List<Long> lista;
        System.out.println("23 - legge una lista di pageid (come user) di una categoria");

        sorgente = CAT_INESISTENTE;
        previstoIntero = 0;
        lista = service.getLongCat(sorgente);
        assertNotNull(lista);
        assertEquals(lista.size(), previstoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("Categoria: %s", sorgente));
        System.out.println(String.format("Nessuna pagina"));

        sorgente = CAT_1435;
        previstoIntero = 33;
        inizio = System.currentTimeMillis();
        lista = service.getLongCat(sorgente);
        assertNotNull(lista);
        assertEquals(previstoIntero, lista.size());
        System.out.println(VUOTA);
        System.out.println(String.format("Categoria: %s", sorgente));
        System.out.println(String.format("Ce ne sono %s", lista.size()));
        System.out.println(String.format("Tempo impiegato per leggere la categoria: %s", getTime()));
        printLong(lista);

        sorgente = CAT_1935;
        previstoIntero = TOT_CAT_1935;
        inizio = System.currentTimeMillis();
        lista = service.getLongCat(sorgente);
        assertNotNull(lista);
        //        assertEquals(previstoIntero, lista.size());
        System.out.println(VUOTA);
        System.out.println(String.format("Categoria: %s", sorgente));
        System.out.println(String.format("Ce ne sono %s", lista.size()));
        System.out.println(String.format("Tempo impiegato per leggere la categoria: %s", getTime()));
        System.out.println("Non faccio vedere le pagine perché sono troppe");

        sorgente = CAT_ROMA;
        inizio = System.currentTimeMillis();
        lista = service.getLongCat(sorgente);
        assertNotNull(lista);
        System.out.println(VUOTA);
        System.out.println(String.format("Categoria: %s", sorgente));
        System.out.println(String.format("Ce ne sono %s", lista.size()));
        System.out.println(String.format("Tempo impiegato per leggere la categoria: %s", getTime()));
        System.out.println("Non faccio vedere le pagine perché sono troppe");
    }


    @Test
    @Order(24)
    @DisplayName("24 - legge una stringa di pageid (come user) di una categoria")
    public void getLongCat2() {
        String striscia;
        System.out.println("24 - legge una stringa di pageid (come user) di una categoria");

        sorgente = CAT_INESISTENTE;
        previsto = VUOTA;
        striscia = service.getPageidsCat(sorgente);
        assertFalse(text.isValid(striscia));
        assertEquals(previsto, ottenuto);
        System.out.println(VUOTA);
        System.out.println(String.format("Categoria: %s", sorgente));
        System.out.println(String.format("Nessuna pagina"));

        sorgente = CAT_1435;
        inizio = System.currentTimeMillis();
        striscia = service.getPageidsCat(sorgente);
        assertTrue(text.isValid(striscia));
        System.out.println(VUOTA);
        System.out.println(String.format("Categoria: %s", sorgente));
        System.out.println(String.format("Ce ne sono alcune"));
        System.out.println(String.format("Tempo impiegato per leggere la categoria: %s", getTime()));
        System.out.println(striscia);

        //        sorgente = CAT_1935;
        //        previstoIntero = 1987;
        //        inizio = System.currentTimeMillis();
        //        lista = service.getLongCat(sorgente);
        //        assertNotNull(lista);
        //        assertEquals(lista.size(), previstoIntero);
        //        System.out.println(VUOTA);
        //        System.out.println(String.format("Categoria: %s", sorgente));
        //        System.out.println(String.format("Ce ne sono %s", lista.size()));
        //        System.out.println(String.format("Tempo impiegato per leggere la categoria: %s", getTime()));
        //        System.out.println("Non faccio vedere le pagine perché sono troppe");
    }

    @Test
    @Order(25)
    @DisplayName("25 - legge titles (come user) una categoria")
    public void getTitleCat() {
        List<String> lista;
        System.out.println("25 - legge titles (come user) una categoria");

        sorgente = CAT_INESISTENTE;
        previstoIntero = 0;
        lista = service.getTitleCat(sorgente);
        assertNotNull(lista);
        assertEquals(lista.size(), previstoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("Categoria: %s", sorgente));
        System.out.println(String.format("Nessuna pagina"));

        sorgente = CAT_1435;
        previstoIntero = 33;
        inizio = System.currentTimeMillis();
        lista = service.getTitleCat(sorgente);
        assertNotNull(lista);
        assertEquals(lista.size(), previstoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("Categoria: %s", sorgente));
        System.out.println(String.format("Ce ne sono %s", lista.size()));
        System.out.println(String.format("Tempo impiegato per leggere la categoria: %s", getTime()));
        printTitle(lista);

        sorgente = CAT_1935;
        previstoIntero = TOT_CAT_1935;
        inizio = System.currentTimeMillis();
        lista = service.getTitleCat(sorgente);
        assertNotNull(lista);
        //        assertEquals(lista.size(), previstoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("Categoria: %s", sorgente));
        System.out.println(String.format("Ce ne sono %s", lista.size()));
        System.out.println(String.format("Tempo impiegato per leggere la categoria: %s", getTime()));
        System.out.println("Non faccio vedere le pagine perché sono troppe");
    }

    @Test
    @Order(26)
    @DisplayName("26 - legge il numero totale di pagine di una categoria")
    public void getTotaleCategoria() {
        System.out.println("26 - legge il numero totale di pagine di una categoria");

        sorgente = CAT_INESISTENTE;
        previstoIntero = 0;
        ottenutoIntero = service.getTotaleCategoria(sorgente);
        assertEquals(ottenutoIntero, previstoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("La categoria: '%s' non contiene nessuna pagina", sorgente));

        sorgente = CAT_1435;
        previstoIntero = 33;
        ottenutoIntero = service.getTotaleCategoria(sorgente);
        //        assertEquals(ottenutoIntero, previstoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("La categoria: '%s' contiene %d pagine", sorgente, ottenutoIntero));

        sorgente = CAT_1935;
        previstoIntero = TOT_CAT_1935;
        ottenutoIntero = service.getTotaleCategoria(sorgente);
        //        assertEquals(ottenutoIntero, previstoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("La categoria: '%s' contiene %d pagine", sorgente, ottenutoIntero));
    }

    //    @Test
    @Order(5)
    @DisplayName("5 - legge (come user) un template")
    public void getTemplateBandierina() {
        sorgente = "ES-AN";
        dueStringhe = geografic.getTemplateBandierina(sorgente);
        assertNotNull(dueStringhe);
        System.out.println(VUOTA);
        System.out.println(sorgente + FORWARD + dueStringhe.getPrima() + SEP + dueStringhe.getSeconda());

        sorgente = "{{ES-CB}}";
        dueStringhe = geografic.getTemplateBandierina(sorgente);
        assertNotNull(dueStringhe);
        System.out.println(VUOTA);
        System.out.println(sorgente + FORWARD + dueStringhe.getPrima() + SEP + dueStringhe.getSeconda());
    }


    //    @Test
    @Order(5)
    @DisplayName("5 - legge (come user) una colonna")
    public void getColonna() {
        sorgente = "ISO_3166-2:ES";
        listaStr = service.getColonna(sorgente, 1, 2, 2);
        assertNotNull(listaStr);
        System.out.println(VUOTA);
        System.out.println("5 - Template Spagna: " + listaStr.size());
        System.out.println(VUOTA);
        printColonna(listaStr);

        sorgente = "ISO_3166-2:IT";
        listaStr = service.getColonna(sorgente, 2, 2, 2);
        assertNotNull(listaStr);
        System.out.println(VUOTA);
        System.out.println("5 - province: " + listaStr.size());
        System.out.println(VUOTA);
        printColonna(listaStr);
    }


    //    @Test
    @Order(6)
    @DisplayName("6 - legge una lista di template")
    public void getTemplateBandierine() {
        sorgente = "ISO_3166-2:ES";
        previstoIntero = 18;
        listaStr = service.getColonna(sorgente, 1, 2, 2);
        assertNotNull(listaStr);
        assertEquals(previstoIntero, listaStr.size());
        listaWrap = geografic.getTemplateList(listaStr);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("6 - Spagna: " + listaWrap.size());
        printWrap(listaWrap);
    }


    //    @Test
    @Order(7)
    @DisplayName("7 - legge una coppia di colonne da una table")
    public void getDueColonne() {
        sorgente = "ISO_3166-2:ES";
        previstoIntero = 18;
        listaWrap = service.getDueColonne(sorgente, 1, 2, 2, 3);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println("7 - Spagna: " + listaWrap.size());
        printWrap(listaWrap);

        sorgente = "ISO_3166-2:IT";
        previstoIntero = 15;
        listaWrap = service.getDueColonne(sorgente, 2, 2, 1, 3);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("7 - Province (capoluogo): " + listaWrap.size());
        printWrap(listaWrap);
    }


    //    @Test
    @Order(8)
    @DisplayName("8 - legge le righe delle regioni italiane")
    public void getTableRegioni() {
        sorgente = "ISO 3166-2:IT";

        //--regioni
        previstoIntero = 20;
        previsto = "<code>IT-65</code>";
        previsto2 = "{{bandiera|Abruzzo|nome}}";
        try {
            listaGrezza = service.getTable(sorgente);
        } catch (Exception unErrore) {
        }
        assertNotNull(listaGrezza);
        assertEquals(previstoIntero, listaGrezza.size());
        assertEquals(previsto, listaGrezza.get(0).get(0));
        assertEquals(previsto2, listaGrezza.get(0).get(1));
        System.out.println("Legge le righe di una tabella wiki");
        System.out.println(VUOTA);
        System.out.println("8 - Regioni: " + listaGrezza.size());
        System.out.println("*******");
        printList(listaGrezza);
    }


    //        @Test
    @Order(9)
    @DisplayName("9 - legge le righe delle province")
    public void getTableProvince() {
        sorgente = "ISO 3166-2:IT";

        //--province
        previstoIntero = 14;
        listaWrapTre = geografic.getTemplateList(sorgente, 2, 3, 1, 3);
        assertNotNull(listaWrapTre);
        assertEquals(previstoIntero, listaWrapTre.size());
        System.out.println(VUOTA);
        System.out.println(VUOTA);
        System.out.println(VUOTA);
        System.out.println("9 - Province: " + listaWrapTre.size());
        printWrapTre(listaWrapTre);
    }


    //    @Test
    @Order(10)
    @DisplayName("10 - legge le regioni della Francia")
    public void getTableFrancia() {
        sorgente = "ISO_3166-2:FR";
        previstoIntero = 13;
        listaWrap = geografic.getTemplateList(sorgente, 1, 2, 2);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("10 - Francia: " + listaWrap.size());
        printWrap(listaWrap);

        previstoIntero = 3;
        listaWrap = service.getDueColonne(sorgente, 3, 2, 2, 4);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("10 - Francia: " + listaWrap.size());
        printWrap(listaWrap);

        previstoIntero = 9;
        listaWrap = service.getDueColonne(sorgente, 4, 2, 1, 3);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("10 - Francia: " + listaWrap.size());
        printWrap(listaWrap);
    }


    //    @Test
    @Order(11)
    @DisplayName("11 - legge i cantoni della Svizzera")
    public void getTableSvizzera() {
        sorgente = "ISO_3166-2:CH";
        previstoIntero = 26;
        listaWrap = geografic.getTemplateList(sorgente, 1, 2, 2);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("11 - Svizzera: " + listaWrap.size());
        printWrap(listaWrap);
    }


    //    @Test
    @Order(12)
    @DisplayName("12 - legge i lander della Austria")
    public void getTableAustria() {
        sorgente = "ISO_3166-2:AT";
        previstoIntero = 9;
        listaWrap = geografic.getTemplateList(sorgente, 1, 2, 2);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("12 - Austria: " + listaWrap.size());
        printWrap(listaWrap);
    }


    //    @Test
    @Order(13)
    @DisplayName("13 - legge i lander della Germania")
    public void getTableGermania() {
        sorgente = "ISO_3166-2:DE";
        previstoIntero = 16;
        listaWrap = geografic.getTemplateList(sorgente, 1, 2, 2);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("13 - Germania: " + listaWrap.size());
        printWrap(listaWrap);
    }


    //    @Test
    @Order(14)
    @DisplayName("14 - legge le comunità della Spagna")
    public void getTableSpagna() {
        sorgente = "ISO_3166-2:ES";
        previstoIntero = 17;
        listaWrap = geografic.getTemplateList(sorgente, 1, 2, 2);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("14 - Spagna: " + listaWrap.size());
        printWrap(listaWrap);
    }


    //    @Test
    @Order(15)
    @DisplayName("15 - legge i distretti del Portogallo")
    public void getTablePortogallo() {
        sorgente = "ISO_3166-2:PT";
        previstoIntero = 18;
        listaWrap = service.getDueColonne(sorgente, 1, 2, 2, 3);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("15 - Portogallo: " + listaWrap.size());
        printWrap(listaWrap);
    }


    //        @Test
    @Order(16)
    @DisplayName("16 - legge i comuni della Slovenia")
    public void getTableSlovenia() {
        sorgente = "ISO_3166-2:SI";
        previstoIntero = 211;
        listaWrap = service.getDueColonne(sorgente, 1, 2, 1, 2);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("16 - Slovenia: " + listaWrap.size());
        printWrap(listaWrap);
    }


    //    @Test
    @Order(17)
    @DisplayName("17 - legge i comuni del Belgio")
    public void getTableBelgio() {
        sorgente = "ISO_3166-2:BE";
        previstoIntero = 3;
        listaWrap = geografic.getTemplateList(sorgente, 1, 2, 2);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("17 - Belgio: " + listaWrap.size());
        printWrap(listaWrap);
    }


    //    @Test
    @Order(18)
    @DisplayName("18 - legge le province dell'Olanda")
    public void getTableOlanda() {
        sorgente = "ISO_3166-2:NL";
        previstoIntero = 12;
        listaWrap = geografic.getTemplateList(sorgente, 1, 2, 3);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("18 - Olanda: " + listaWrap.size());
        printWrap(listaWrap);
    }


    //    @Test
    @Order(19)
    @DisplayName("19 - legge le province della Croazia")
    public void getTableCroazia() {
        sorgente = "ISO_3166-2:HR";
        previstoIntero = 21;
        listaWrap = service.getDueColonne(sorgente, 1, 2, 1, 2);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("19 - Croazia: " + listaWrap.size());
        printWrap(listaWrap);
    }


    //    @Test
    @Order(20)
    @DisplayName("20 - legge i distretti della Albania")
    public void getTableAlbania() {
        sorgente = "ISO_3166-2:AL";
        previstoIntero = 36;
        listaWrap = service.getDueColonne(sorgente, 1, 1, 1, 2);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("20 - Albania: " + listaWrap.size());
        printWrap(listaWrap);
    }


    //    @Test
    @Order(21)
    @DisplayName("21 - legge i distretti della Grecia")
    public void getTableGrecia() {
        sorgente = "ISO_3166-2:GR";

        //--periferie
        previstoIntero = 13;
        listaWrap = service.getDueColonne(sorgente, 1, 2, 1, 2);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("21 - Grecia: " + listaWrap.size());
        printWrap(listaWrap);

        //--prefetture
        previstoIntero = 52;
        listaWrap = service.getDueColonne(sorgente, 2, 2, 2, 3);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("21 - Grecia: " + listaWrap.size());
        printWrap(listaWrap);
    }


    //    @Test
    @Order(22)
    @DisplayName("22 - legge le regioni della Cechia")
    public void getTableCechia() {
        sorgente = "ISO_3166-2:CZ";
        previstoIntero = 14;
        listaWrap = geografic.getTemplateList(sorgente, 1, 2, 3);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("22 - Cechia: " + listaWrap.size());
        printWrap(listaWrap);
    }


    //    @Test
    @Order(23)
    @DisplayName("23 - legge le regioni della Slovacchia")
    public void getTableSlovacchia() {
        sorgente = "ISO_3166-2:SK";
        previstoIntero = 8;
        listaWrap = service.getDueColonne(sorgente, 1, 2, 1, 2);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("23 - Slovacchia: " + listaWrap.size());
        printWrap(listaWrap);
    }


    //    @Test
    @Order(24)
    @DisplayName("24 - legge le province della Ungheria")
    public void getTableUngheria() {
        sorgente = "ISO_3166-2:HU";
        previstoIntero = 19;
        listaWrap = geografic.getTemplateList(sorgente, 1, 2, 2);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("24 - Ungheria: " + listaWrap.size());
        printWrap(listaWrap);
    }


    //    @Test
    @Order(25)
    @DisplayName("25 - legge i distretti della Romania")
    public void getTableRomania() {
        sorgente = "ISO_3166-2:RO";

        //--distretti
        previstoIntero = 41;
        listaWrap = service.getDueColonne(sorgente, 1, 2, 2, 3);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("25 - Romania: " + listaWrap.size());
        printWrap(listaWrap);

        //--capitale
        previstoIntero = 1;
        listaWrap = service.getDueColonne(sorgente, 2, 2, 2, 3);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("25 - Romania: " + listaWrap.size());
        printWrap(listaWrap);
    }


    //    @Test
    @Order(26)
    @DisplayName("26 - legge i distretti della Bulgaria")
    public void getTableBulgaria() {
        sorgente = "ISO_3166-2:BG";
        previstoIntero = 28;
        listaWrap = service.getDueColonne(sorgente, 1, 2, 2, 3);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("26 - Bulgaria: " + listaWrap.size());
        printWrap(listaWrap);
    }


    //    @Test
    @Order(27)
    @DisplayName("27 - legge i voivodati della Polonia")
    public void getTablePolonia() {
        sorgente = "ISO_3166-2:PL";
        previstoIntero = 16;
        listaWrap = service.getDueColonne(sorgente, 1, 2, 2, 3);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("27 - Polonia: " + listaWrap.size());
        printWrap(listaWrap);
    }


    //    @Test
    @Order(28)
    @DisplayName("28 - legge le regioni della Danimarca")
    public void getTableDanimarca() {
        sorgente = "ISO_3166-2:DK";
        previstoIntero = 5;
        listaWrap = service.getDueColonne(sorgente, 1, 2, 1, 2);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("28 - Danimarca: " + listaWrap.size());
        printWrap(listaWrap);
    }


    //    @Test
    @Order(29)
    @DisplayName("29 - legge i distretti di Finlandia")
    public void getTableFinlandia() {
        sorgente = "ISO_3166-2:FI";
        previstoIntero = 19 + 1;
        try {
            listaWrap = geografic.getRegioni(sorgente);
        } catch (Exception unErrore) {
        }
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("29 - Finlandia: " + (listaWrap.size() - 1) + " + titolo");
        printWrap(listaWrap);
    }

    //    @Test
    @Order(30)
    @DisplayName("30 - legge i distretti di Azerbaigian")
    public void getTableAzerbaigian() {
        sorgente = "ISO_3166-2:AZ";
        previstoIntero = 77 + 1;
        try {
            listaWrap = geografic.getRegioni(sorgente);
        } catch (Exception unErrore) {
        }
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("30 - Azerbaigian: " + (listaWrap.size() - 1) + " + titolo");
        printWrap(listaWrap);
    }

    //    @Test
    @Order(31)
    @DisplayName("31 - legge i distretti di Belize")
    public void getTableBelize() {
        sorgente = "ISO_3166-2:BZ";
        previstoIntero = 6 + 1;
        try {
            listaWrap = geografic.getRegioni(sorgente);
        } catch (Exception unErrore) {
        }
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("31 - Belize: " + (listaWrap.size() - 1) + " + titolo");
        printWrap(listaWrap);
    }

    //    @Test
    @Order(32)
    @DisplayName("32 - legge i distretti di Guatemala")
    public void getTableGuatemala() {
        sorgente = "ISO_3166-2:GT";
        previstoIntero = 22 + 1;
        try {
            listaWrap = geografic.getRegioni(sorgente);
        } catch (Exception unErrore) {
        }
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("32 - Guatemala: " + (listaWrap.size() - 1) + " + titolo");
        printWrap(listaWrap);
    }

    //    @Test
    @Order(33)
    @DisplayName("33 - legge i distretti di Guinea Bissau")
    public void getTableGuinea() {
        sorgente = "ISO_3166-2:GW";
        previstoIntero = 9 + 1;
        try {
            listaWrap = geografic.getRegioni(sorgente);
        } catch (Exception unErrore) {
        }
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("33 - Guinea Bissau: " + (listaWrap.size() - 1) + " + titolo");
        printWrap(listaWrap);
    }

    //    @Test
    @Order(34)
    @DisplayName("34 - legge i distretti di Slovenia")
    public void getTableSlovenia2() {
        sorgente = "ISO_3166-2:SI";
        previstoIntero = 211 + 1;
        try {
            listaWrap = geografic.getRegioni(sorgente);
        } catch (Exception unErrore) {
        }
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("33 - Guinea Bissau: " + (listaWrap.size() - 1) + " + titolo");
        printWrap(listaWrap);
    }

    //    @Test
    @Order(35)
    @DisplayName("35 - legge i distretti di Kirghizistan")
    public void getTableKirghizistan() {
        sorgente = "ISO_3166-2:KG";
        previstoIntero = 8 + 1;
        try {
            listaWrap = geografic.getRegioni(sorgente);
        } catch (Exception unErrore) {
        }
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("33 - Guinea Bissau: " + (listaWrap.size() - 1) + " + titolo");
        printWrap(listaWrap);
    }

    //    @Test
    @Order(40)
    @DisplayName("40 - legge le regioni dei primi 50 stati")
    public void readStati1() {
        List<List<String>> listaStati = geografic.getStati();
        assertNotNull(listaStati);
        listaStati = listaStati.subList(0, 50);
        readStati(listaStati);
    }

    //    @Test
    @Order(41)
    @DisplayName("41 - legge le regioni degli stati 50-100")
    public void readStati2() {
        List<List<String>> listaStati = geografic.getStati();
        assertNotNull(listaStati);
        listaStati = listaStati.subList(50, 100);
        readStati(listaStati);
    }

    //    @Test
    @Order(42)
    @DisplayName("42 - legge le regioni degli stati 100-150")
    public void readStati3() {
        List<List<String>> listaStati = geografic.getStati();
        assertNotNull(listaStati);
        listaStati = listaStati.subList(100, 150);
        readStati(listaStati);
    }

    //    @Test
    @Order(43)
    @DisplayName("43 - legge le regioni degli stati 150-200")
    public void readStati4() {
        List<List<String>> listaStati = geografic.getStati();
        assertNotNull(listaStati);
        listaStati = listaStati.subList(150, 200);
        readStati(listaStati);
    }

    //    @Test
    @Order(44)
    @DisplayName("44 - legge le regioni degli stati 200-250")
    public void readStati5() {
        List<List<String>> listaStati = geografic.getStati();
        assertNotNull(listaStati);
        listaStati = listaStati.subList(200, listaStati.size() - 1);
        readStati(listaStati);
    }

    private void readStati(List<List<String>> listaStati) {
        String nome;
        String tag = "ISO 3166-2:";
        List<String> valide = new ArrayList<>();
        List<String> errate = new ArrayList<>();

        System.out.println("Legge le regioni di " + listaStati.size() + " stati (" + AWikiApiService.PAGINA_ISO_1 + ")");
        System.out.println(VUOTA);
        System.out.println("Valide                    Errate");
        System.out.println("********************************");
        for (List<String> lista : listaStati) {
            nome = lista.get(0);
            sorgente = tag + lista.get(3);
            try {
                listaWrap = geografic.getRegioni(sorgente);
                if (listaWrap != null && listaWrap.size() > 0) {
                    valide.add(nome);
                    System.out.println(nome);
                }
            } catch (Exception unErrore) {
                errate.add(nome);
                System.out.println("                          " + nome);
            }
        }
        System.out.println(VUOTA);
        System.out.println("Stati con regioni valide: " + valide.size());
        System.out.println("Stati con regioni errate: " + errate.size());
    }

    //    @Test
    @Order(45)
    @DisplayName("45 - legge le province italiane")
    public void getTableProvinceItaliane() {
        previstoIntero = 107;

        listaWrapQuattro = geografic.getProvince();

        assertNotNull(listaWrapQuattro);
        assertEquals(previstoIntero, listaWrapQuattro.size());
        System.out.println(VUOTA);
        System.out.println(VUOTA);
        System.out.println(VUOTA);
        System.out.println("45 - Province: " + listaWrapQuattro.size());
        printWrapQuattro(listaWrapQuattro);
    }

    private void printColonna(List<String> listaColonna) {
        if (array.isAllValid(listaColonna)) {
            for (String stringa : listaColonna) {
                System.out.println(stringa);
            }
        }
    }


    private void printWrap(List<WrapDueStringhe> listaWrap) {
        System.out.println("********");
        if (array.isAllValid(listaWrap)) {
            for (WrapDueStringhe wrap : listaWrap) {
                System.out.println(wrap.getPrima() + SEP + wrap.getSeconda());
            }
        }
    }


    private void printWrapTre(List<WrapTreStringhe> listaWrap) {
        System.out.println("********");
        if (array.isAllValid(listaWrap)) {
            for (WrapTreStringhe wrap : listaWrap) {
                System.out.println(wrap.getPrima() + SEP + wrap.getSeconda() + SEP + wrap.getTerza());
            }
        }
    }

    private void printWrapQuattro(List<WrapQuattro> listaWrap) {
        System.out.println("********");
        if (array.isAllValid(listaWrap)) {
            for (WrapQuattro wrap : listaWrap) {
                System.out.println(wrap.getPrima() + SEP + wrap.getSeconda() + SEP + wrap.getTerza());
            }
        }
    }

    private void printMappaPar(Map<String, Object> mappa) {
        System.out.println("11 - crea una mappa da un singolo oggetto BJSON");
        System.out.println(VUOTA);
        for (String key : mappa.keySet()) {
            System.out.print(key);
            System.out.print(SEP);
            System.out.println(mappa.get(key));
        }
    }


    private void printWikiPage(WikiPage wikiPage) {
        System.out.println("WikiPage");
        System.out.println(VUOTA);
        System.out.println("pageid" + SEP + wikiPage.getPageid());
        System.out.println("ns" + SEP + wikiPage.getNs());
        System.out.println("title" + SEP + wikiPage.getTitle());
        System.out.println("pagelanguage" + SEP + wikiPage.getPagelanguage());
        System.out.println("pagelanguagehtmlcode" + SEP + wikiPage.getPagelanguagehtmlcode());
        System.out.println("pagelanguagedir" + SEP + wikiPage.getPagelanguagedir());
        System.out.println("touched" + SEP + wikiPage.getTouched());
        System.out.println("length" + SEP + wikiPage.getLength());
        System.out.println("revid" + SEP + wikiPage.getRevid());
        System.out.println("parentid" + SEP + wikiPage.getParentid());
        System.out.println("user" + SEP + wikiPage.getUser());
        System.out.println("userid" + SEP + wikiPage.getUserid());
        System.out.println("timestamp" + SEP + wikiPage.getTimestamp());
        System.out.println("size" + SEP + wikiPage.getSize());
        System.out.println("comment" + SEP + wikiPage.getComment());
        System.out.println("content" + SEP + wikiPage.getContent());
    }

    private void printCat(List<WrapCat> lista) {
        for (WrapCat wrap : lista) {
            System.out.print(wrap.getPageid());
            System.out.print(SEP);
            System.out.println(wrap.getTitle());
        }
    }

    private void printLong(List<Long> lista) {
        for (Long pageid : lista) {
            System.out.println(pageid);
        }
    }

    private void printTitle(List<String> lista) {
        for (String title : lista) {
            System.out.println(title);
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
    @AfterEach
    void tearDownAll() {
    }

}