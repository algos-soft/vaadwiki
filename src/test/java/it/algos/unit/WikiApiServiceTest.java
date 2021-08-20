package it.algos.unit;

import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.wiki.*;
import static it.algos.vaadflow14.wiki.AWikiApiService.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: sab, 12-set-2020
 * Time: 20:25
 * <p>
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("WikiApiService")
@DisplayName("WikiApiService - Collegamenti base di wikipedia.")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WikiApiServiceTest extends ATest {


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
     * Gia 'costruita' nella superclasse <br>
     */
    private AWikiApiService service;


    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpIniziale() {
        super.setUpStartUp();

        //--reindirizzo l'istanza della superclasse
        service = wikiApiService;
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

        ottenuto = service.leggeHtml(sorgente);
        assertTrue(textService.isValid(ottenuto));
        assertTrue(ottenuto.startsWith(previsto));
        assertTrue(ottenuto.endsWith(previsto2));

        System.out.println("1 - legge il testo grezzo in formato html");
        System.out.println("Rimanda a un metodo di AWebService");
        System.out.println(String.format("Legge il sorgente di una pagina wiki"));
        System.out.println(String.format("La pagina wiki è: %s", sorgente));
        System.out.println("Risultato restituito in formato html");
        System.out.println(String.format("Tempo impiegato per leggere la pagina: %s", getTime()));
        System.out.println("Faccio vedere solo l'inizio e la fine, perché tutto sarebbe troppo lungo");

        System.out.println(VUOTA);
        System.out.print("Inizio");
        System.out.print(DUE_PUNTI_SPAZIO);
        System.out.println(ottenuto.substring(0, previsto.length()));
        System.out.println(VUOTA);
        System.out.print("Fine");
        System.out.print(DUE_PUNTI_SPAZIO);
        System.out.println(ottenuto.substring(ottenuto.length() - previsto2.length()));
    }

    @Test
    @Order(2)
    @DisplayName("2 - legge una pagina in formato JSON con una API action=parse di Mediawiki")
    public void leggeJSONParse() {
        sorgente = PAGINA_TEST;
        previsto = "{\"parse\":{\"title\":\"Utente:Gac/T17\",\"pageid\":8956310,\"wikitext\":\"Solo test\"}}";

        ottenuto = service.leggeJSONParse(sorgente);
        assertNotNull(ottenuto);
        assertTrue(textService.isValid(ottenuto));
        assertEquals(previsto, ottenuto);

        System.out.println("2 - legge una pagina in formato JSON con una API action=parse di Mediawiki");
        System.out.println("Legge una pagina wiki con una query API Mediawiki");
        System.out.println("Legge title, pageid e wikitext");
        System.out.println(String.format("La pagina wiki è: %s", sorgente));
        System.out.println("Risultato restituito in formato JSON");
        System.out.println(String.format("Tempo impiegato per leggere la pagina: %s", getTime()));

        System.out.println(VUOTA);
        System.out.print(KEY_MAPPA_DOMAIN + DUE_PUNTI_SPAZIO);
        System.out.println(WIKI_PARSE + sorgente);
        System.out.println(VUOTA);
        System.out.print("Risultato completo");
        System.out.print(DUE_PUNTI_SPAZIO);
        System.out.println(ottenuto);
    }

    @Test
    @Order(3)
    @DisplayName("3 - legge una mappa in formato JSON con una API action=parse di Mediawiki")
    public void leggeMappaParse() {
        Map<String, Object> mappa;
        sorgente = PAGINA_TEST;
        previsto = "Utente:Gac/T17";
        long previstoLungo = 8956310;
        previsto3 = "Solo test";

        mappa = service.leggeMappaParse(sorgente);
        assertNotNull(mappa);
        assertEquals(previsto, mappa.get(KEY_MAPPA_TITLE));
        assertEquals(previstoLungo, (long) mappa.get(KEY_MAPPA_PAGEID));
        assertEquals(previsto3, mappa.get(KEY_MAPPA_TEXT));

        System.out.println("3 - legge una mappa in formato JSON con una API action=parse di Mediawiki");
        System.out.println("Legge una mappa wiki con una query API Mediawiki");
        System.out.println("Legge title, pageid e wikitext");
        System.out.println(String.format("La pagina wiki è: %s", sorgente));
        System.out.println("Risultato restituito in formato JSON");
        System.out.println(String.format("Tempo impiegato per leggere la pagina: %s", getTime()));

        System.out.println(VUOTA);
        System.out.print(KEY_MAPPA_DOMAIN + DUE_PUNTI_SPAZIO);
        System.out.println(mappa.get(KEY_MAPPA_DOMAIN));
        System.out.println(VUOTA);
        System.out.print(KEY_MAPPA_TITLE + DUE_PUNTI_SPAZIO);
        System.out.println(mappa.get(KEY_MAPPA_TITLE));
        System.out.println(VUOTA);
        System.out.print(KEY_MAPPA_PAGEID + DUE_PUNTI_SPAZIO);
        System.out.println(mappa.get(KEY_MAPPA_PAGEID));
        System.out.println(VUOTA);
        System.out.print(KEY_MAPPA_TEXT + DUE_PUNTI_SPAZIO);
        System.out.println(mappa.get(KEY_MAPPA_TEXT));
    }


    @Test
    @Order(4)
    @DisplayName("4 - legge il testo in formato visibile con una API action=parse di Mediawiki")
    public void legge() {
        System.out.println("4 - legge il testo in formato visibile con una API action=parse di Mediawiki");
        System.out.println("Legge (come user) una pagina dal server wiki");
        System.out.println("Usa una API con action=parse SENZA bisogno di loggarsi");
        System.out.println("Estrae il testo in linguaggio wiki visibile/leggibile");

        sorgente = PAGINA_TEST;
        previsto = "Solo test";

        ottenuto = service.legge(sorgente);
        assertNotNull(ottenuto);
        assertTrue(textService.isValid(ottenuto));
        assertEquals(previsto, ottenuto);

        System.out.println(VUOTA);
        System.out.println(String.format("La pagina wiki è: %s", sorgente));
        System.out.println(String.format("Tempo impiegato per leggere %d pagine: %s", cicli, getTime()));
        System.out.print(KEY_MAPPA_DOMAIN + DUE_PUNTI_SPAZIO);
        System.out.println(WIKI_PARSE + sorgente);
        System.out.print(KEY_MAPPA_TEXT + DUE_PUNTI_SPAZIO);
        System.out.println(ottenuto);

        sorgente = PAGINA_NO_ASCI;
        previsto = "{{In corso|biografie}}";

        inizio = System.currentTimeMillis();
        ottenuto = service.legge(sorgente);
        assertNotNull(ottenuto);
        assertTrue(textService.isValid(ottenuto));
        assertTrue(ottenuto.startsWith(previsto));

        System.out.println(VUOTA);
        System.out.println(String.format("La pagina wiki è: %s", sorgente));
        System.out.println(String.format("Tempo impiegato per leggere %d pagine: %s", cicli, getTime()));
        System.out.println("Faccio vedere solo l'inizio, perché troppo lungo");

        System.out.print(KEY_MAPPA_DOMAIN + DUE_PUNTI_SPAZIO);
        System.out.println(WIKI_PARSE + service.fixWikiTitle(sorgente));
        System.out.print("Inizio");
        System.out.print(DUE_PUNTI_SPAZIO);
        System.out.println(ottenuto.substring(0, previsto.length()));
    }

    @Test
    @Order(5)
    @DisplayName("5 - legge una mappa in formato JSON con una API action=parse di Mediawiki")
    public void leggeMappaParse2() {
        sorgente = PAGINA_NO_ASCI;
        previstoIntero = 4;
        Map mappa = service.leggeMappaParse(sorgente);

        assertNotNull(mappa);
        assertEquals(previstoIntero, mappa.size());

        System.out.println("5 - legge una mappa in formato JSON con una API action=parse di Mediawiki");
        System.out.println("Legge (come user) una mappa (title,pageid,text) dal server wiki");
        System.out.println(String.format("La pagina wiki è: %s", sorgente));
        System.out.println("Usa una API con action=parse SENZA bisogno di loggarsi");
        System.out.println("Recupera dalla urlRequest title, pageid e wikitext");
        System.out.println("Estrae il testo in linguaggio wiki visibile/leggibile");
        System.out.println(String.format("La mappa contiene %s elementi", mappa.size()));
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
    @Order(6)
    @DisplayName("6 - legge (come anonymous) una table wiki")
    public void leggeTable() {
        sorgente = "ISO 3166-2:IT";
        previsto = "{| class=\"wikitable sortable\"";
        previsto2 = "| <code>IT-34</code>\n| {{bandiera|Veneto|nome}}\n|}";

        //--regione
        ottenuto = service.leggeTable(sorgente);
        assertTrue(textService.isValid(ottenuto));
        assertTrue(ottenuto.startsWith(previsto));
        assertTrue(ottenuto.endsWith(previsto2));
        System.out.println("Legge una tabella wiki completa");
        System.out.println(String.format("La pagina wiki è: %s", sorgente));
        System.out.println("REGIONE");
        System.out.println(String.format("Tempo impiegato per leggere la pagina: %s", getTime()));
        System.out.println(VUOTA);
        System.out.println(ottenuto);

        //--provincia
        previsto2 = "| <code>IT-VE</code>\n| {{IT-VE}}\n| [[Veneto]] (<code>34</code>)\n|}";
        inizio = System.currentTimeMillis();
        ottenuto = service.leggeTable(sorgente, 2);

        assertTrue(textService.isValid(ottenuto));
        assertTrue(ottenuto.startsWith(previsto));
        assertTrue(ottenuto.endsWith(previsto2));
        System.out.println(VUOTA);
        System.out.println(VUOTA);
        System.out.println("Legge una tabella wiki completa");
        System.out.println(String.format("La pagina wiki è: %s", sorgente));
        System.out.println("PROVINCIA");
        System.out.println(String.format("Tempo impiegato per leggere la pagina: %s", getTime()));
        System.out.println("Faccio vedere solo l'inizio, perché troppo lungo");
        System.out.println(VUOTA);
        System.out.print("Inizio");
        System.out.print(DUE_PUNTI_SPAZIO);
        System.out.println(ottenuto.substring(0, previsto.length()));
    }

    @Test
    @Order(7)
    @DisplayName("7 - legge (come anonymous) un modulo wiki")
    public void leggeModulo() {
        sorgente = "Modulo:Bio/Plurale_attività";
        previsto = "{\n[\"abate\"] =";
        previsto2 = "[\"zoologo\"] = \"zoologi\"\n}";
        int ini = 0;
        int end;

        ottenuto = service.leggeModulo(sorgente);
        assertTrue(textService.isValid(ottenuto));
        assertTrue(ottenuto.startsWith(previsto));
        assertTrue(ottenuto.endsWith(previsto2));
        System.out.println("Legge un modulo wiki completo");
        System.out.println(String.format("La pagina wiki è: %s", sorgente));
        System.out.println(String.format("Tempo impiegato per leggere la pagina: %s", getTime()));
        System.out.println("Faccio vedere solo l'inizio e la fine, perché troppo lungo");
        System.out.println(VUOTA);
        System.out.print("Inizio");
        System.out.print(DUE_PUNTI_SPAZIO);
        end = previsto.length();
        System.out.println(ottenuto.substring(ini, end));
        System.out.println(VUOTA);
        System.out.print("Fine");
        System.out.print(DUE_PUNTI_SPAZIO);
        ini = ottenuto.length() - previsto2.length();
        end = ottenuto.length();
        System.out.println(ottenuto.substring(ini, end));
    }


    @Test
    @Order(8)
    @DisplayName("8 - legge (come anonymous) la mappa del modulo Attività")
    public void leggeMappaModulo() {
        sorgente = "Modulo:Bio/Plurale attività";
        sorgente2 = "abate";
        sorgente3 = "badessa";
        previsto = "abati e badesse";

        mappaOttenuta = service.leggeMappaModulo(sorgente);
        assertNotNull(mappaOttenuta);
        ottenuto = mappaOttenuta.get(sorgente2);
        assertTrue(textService.isValid(ottenuto));
        assertEquals(ottenuto, previsto);
        ottenuto = mappaOttenuta.get(sorgente3);
        assertTrue(textService.isValid(ottenuto));
        assertEquals(ottenuto, previsto);
        System.out.println("Legge la mappa di un modulo wiki");
        System.out.println(String.format("La pagina wiki è: %s", sorgente));
        System.out.println(String.format("Tempo impiegato per leggere la pagina: %s", getTime()));
        System.out.println("Faccio vedere solo l'inizio, perché troppo lungo");
        System.out.println(VUOTA);
        System.out.println("Mappa");
        System.out.print(sorgente2 + FORWARD);
        System.out.println(mappaOttenuta.get(sorgente2));
        System.out.print(sorgente3 + FORWARD);
        System.out.println(mappaOttenuta.get(sorgente3));
        System.out.print("allevatore" + FORWARD);
        System.out.println(mappaOttenuta.get("allevatore"));
        System.out.print("allevatrice" + FORWARD);
        System.out.println(mappaOttenuta.get("allevatrice"));

    }


    @Test
    @Order(9)
    @DisplayName("9 - legge (come anonymous) la mappa del modulo Attività/Genere")
    public void leggeMappaModulo2() {
        sorgente = "Modulo:Bio/Plurale attività genere";
        sorgente2 = "abate";
        sorgente3 = "badessa";
        previsto = "\"abati\",\"M\"";

        mappaOttenuta = service.leggeMappaModulo(sorgente);
        assertNotNull(mappaOttenuta);
        ottenuto = mappaOttenuta.get(sorgente2);
        assertTrue(textService.isValid(ottenuto));
        assertEquals(ottenuto, previsto);
        System.out.println("Legge la mappa di un modulo wiki");
        System.out.println(String.format("La pagina wiki è: %s", sorgente));
        System.out.println(String.format("Tempo impiegato per leggere la pagina: %s", getTime()));
        System.out.println("Faccio vedere solo l'inizio, perché troppo lungo");
        System.out.println(VUOTA);
        System.out.println("Mappa");
        System.out.print(sorgente2 + FORWARD);
        System.out.println(mappaOttenuta.get(sorgente2));
        System.out.print(sorgente3 + FORWARD);
        System.out.println(mappaOttenuta.get(sorgente3));
        System.out.print("accademica" + FORWARD);
        System.out.println(mappaOttenuta.get("accademica"));
        System.out.print("accademico" + FORWARD);
        System.out.println(mappaOttenuta.get("accademico"));
        System.out.print("alchimista" + FORWARD);
        System.out.println(mappaOttenuta.get("alchimista"));
        System.out.print("alpinista" + FORWARD);
        System.out.println(mappaOttenuta.get("alpinista"));
    }

    @Test
    @Order(10)
    @DisplayName("10 - legge (come anonymous) un template amministrativo")
    public void leggeTmpl() {
        sorgente = PAGINA_PIOZZANO;
        previsto = "{{Divisione amministrativa";
        sorgente2 = "Divisione amministrativa";

        ottenuto = service.leggeTmpl(VUOTA, VUOTA);
        assertTrue(textService.isEmpty(ottenuto));

        ottenuto = service.leggeTmpl(sorgente, VUOTA);
        assertTrue(textService.isEmpty(ottenuto));

        ottenuto = service.leggeTmpl(sorgente, sorgente2);
        assertTrue(textService.isValid(ottenuto));
        assertTrue(ottenuto.startsWith(previsto));
        System.out.println("Legge un template amministrativo");
        System.out.println(String.format("La pagina wiki è: %s", sorgente));
        System.out.println(String.format("Tempo impiegato per leggere la pagina: %s", getTime()));
        System.out.println(VUOTA);
        System.out.println(ottenuto);
    }


    @Test
    @Order(11)
    @DisplayName("11 - legge (come anonymous) un template bio")
    public void leggeTmpl2() {
        sorgente = "Guido Rossi";
        previsto = "{{Bio";
        sorgente2 = "Bio";

        ottenuto = service.leggeTmpl(VUOTA, VUOTA);
        assertTrue(textService.isEmpty(ottenuto));

        ottenuto = service.leggeTmpl(sorgente, VUOTA);
        assertTrue(textService.isEmpty(ottenuto));

        ottenuto = service.leggeTmpl(sorgente, sorgente2);
        assertTrue(textService.isValid(ottenuto));
        assertTrue(ottenuto.startsWith(previsto));
        System.out.println("Legge un template bio");
        System.out.println(String.format("La pagina wiki è: %s", sorgente));
        System.out.println(String.format("Tempo impiegato per leggere la pagina: %s", getTime()));
        System.out.println(VUOTA);
        System.out.println(ottenuto);
    }


    @Test
    @Order(12)
    @DisplayName("12 - legge (come anonymous) una colonna")
    public void getColonna() {
        sorgente = "ISO_3166-2:ES";
        listaStr = service.getColonna(sorgente, 1, 2, 2);
        assertNotNull(listaStr);
        System.out.println(String.format("La pagina wiki è: %s", sorgente));
        System.out.println("Template Spagna: " + listaStr.size());
        System.out.println(String.format("Tempo impiegato per leggere la pagina: %s", getTime()));
        System.out.println(VUOTA);
        printColonna(listaStr);

        sorgente = "ISO_3166-2:IT";
        inizio = System.currentTimeMillis();
        listaStr = service.getColonna(sorgente, 2, 2, 2);
        assertNotNull(listaStr);
        System.out.println(VUOTA);
        System.out.println(VUOTA);
        System.out.println(String.format("La pagina wiki è: %s", sorgente));
        System.out.println("Template province: " + listaStr.size());
        System.out.println(String.format("Tempo impiegato per leggere la pagina: %s", getTime()));
        System.out.println(VUOTA);
        printColonna(listaStr);
    }


    @Test
    @Order(13)
    @DisplayName("13 - legge (come anonymous) una coppia di colonne da una table")
    public void getDueColonne() {
        sorgente = "ISO_3166-2:ES";
        previstoIntero = 18;
        listaWrap = service.getDueColonne(sorgente, 1, 2, 2, 3);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(String.format("La pagina wiki è: %s", sorgente));
        System.out.println("Template Spagna: " + listaWrap.size());
        System.out.println(String.format("Tempo impiegato per leggere la pagina: %s", getTime()));
        System.out.println(VUOTA);
        printWrap(listaWrap);

        sorgente = "ISO_3166-2:IT";
        previstoIntero = 15;
        inizio = System.currentTimeMillis();
        listaWrap = service.getDueColonne(sorgente, 2, 2, 1, 3);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println(VUOTA);
        System.out.println(String.format("La pagina wiki è: %s", sorgente));
        System.out.println("Template Province (capoluogo): " + listaWrap.size());
        System.out.println(String.format("Tempo impiegato per leggere la pagina: %s", getTime()));
        System.out.println(VUOTA);
        printWrap(listaWrap);
    }

    @Test
    @Order(14)
    @DisplayName("14 - controlla se un text inizia con un #redirect")
    public void isRedirect() {
        System.out.println("14 - controlla se un text inizia con un #redirect");

        sorgente = "Pippoz";
        ottenutoBooleano = service.isRedirect(sorgente);
        assertFalse(ottenutoBooleano);
        System.out.println(String.format("Il testo %s non contiene #redirect", sorgente));

        sorgente = "#REDIRECT";
        ottenutoBooleano = service.isRedirect(sorgente);
        assertTrue(ottenutoBooleano);
        System.out.println(String.format("Il testo: '%s' inizia con un #redirect valido", sorgente));

        sorgente = "#redirect";
        ottenutoBooleano = service.isRedirect(sorgente);
        assertTrue(ottenutoBooleano);
        System.out.println(String.format("Il testo: '%s' inizia con un #redirect valido", sorgente));

        sorgente = " #REDIRECT";
        ottenutoBooleano = service.isRedirect(sorgente);
        assertTrue(ottenutoBooleano);
        System.out.println(String.format("Il testo: '%s' inizia con un #redirect valido", sorgente));

        sorgente = "# REDIRECT";
        ottenutoBooleano = service.isRedirect(sorgente);
        assertTrue(ottenutoBooleano);
        System.out.println(String.format("Il testo: '%s' inizia con un #redirect valido", sorgente));

        sorgente = " # REDIRECT";
        ottenutoBooleano = service.isRedirect(sorgente);
        assertTrue(ottenutoBooleano);
        System.out.println(String.format("Il testo: '%s' inizia con un #redirect valido", sorgente));

        sorgente = "#REDIRECT[[1º maggio]]";
        ottenutoBooleano = service.isRedirect(sorgente);
        assertTrue(ottenutoBooleano);
        System.out.println(String.format("Il testo: '%s' inizia con un #redirect valido", sorgente));

        sorgente = "#REDIRECT [[1º maggio]]";
        ottenutoBooleano = service.isRedirect(sorgente);
        assertTrue(ottenutoBooleano);
        System.out.println(String.format("Il testo: '%s' inizia con un #redirect valido", sorgente));

        sorgente = "#RINVIA";
        ottenutoBooleano = service.isRedirect(sorgente);
        assertTrue(ottenutoBooleano);
        System.out.println(String.format("Il testo: '%s' inizia con un #redirect valido", sorgente));

        sorgente = "#rinvia";
        ottenutoBooleano = service.isRedirect(sorgente);
        assertTrue(ottenutoBooleano);
        System.out.println(String.format("Il testo: '%s' inizia con un #redirect valido", sorgente));

        sorgente = " #RINVIA";
        ottenutoBooleano = service.isRedirect(sorgente);
        assertTrue(ottenutoBooleano);
        System.out.println(String.format("Il testo: '%s' inizia con un #redirect valido", sorgente));

        sorgente = "# RINVIA";
        ottenutoBooleano = service.isRedirect(sorgente);
        assertTrue(ottenutoBooleano);
        System.out.println(String.format("Il testo: '%s' inizia con un #redirect valido", sorgente));

        sorgente = " # RINVIA";
        ottenutoBooleano = service.isRedirect(sorgente);
        assertTrue(ottenutoBooleano);
        System.out.println(String.format("Il testo: '%s' inizia con un #redirect valido", sorgente));

        sorgente = "#RINVIA[[1º maggio]]";
        ottenutoBooleano = service.isRedirect(sorgente);
        assertTrue(ottenutoBooleano);
        System.out.println(String.format("Il testo: '%s' inizia con un #redirect valido", sorgente));

        sorgente = "#RINVIA [[1º maggio]]";
        ottenutoBooleano = service.isRedirect(sorgente);
        assertTrue(ottenutoBooleano);
        System.out.println(String.format("Il testo: '%s' inizia con un #redirect valido", sorgente));
    }

    @Test
    @Order(15)
    @DisplayName("15 - valore del #redirect per leggere la pagina")
    public void getRedirect() {
        sorgente = "#REDIRECT [[1º maggio]]\n" +
                "\n" +
                "[[Categoria:Redirect da mantenere orfani]]";
        previsto = "1º maggio";
        ottenuto = service.getRedirect(sorgente);
        assertNotNull(ottenuto);
        assertTrue(textService.isValid(ottenuto));
        assertTrue(ottenuto.startsWith(previsto));
        sorgente = sorgente.substring(0, sorgente.indexOf("\n"));
        System.out.println(String.format("Il testo che inizia con: '%s...' rinvia alla pagina %s", sorgente, ottenuto));

        sorgente = "#REDIRECT [[6 dicembre]]\n" +
                "[[Categoria:Redirect da mantenere orfani]]";
        previsto = "6 dicembre";
        ottenuto = service.getRedirect(sorgente);
        assertNotNull(ottenuto);
        assertTrue(textService.isValid(ottenuto));
        assertTrue(ottenuto.startsWith(previsto));
        sorgente = sorgente.substring(0, sorgente.indexOf("\n"));
        System.out.println(String.format("Il testo: '%s...' rinvia alla pagina %s", sorgente, ottenuto));
    }

    @Test
    @Order(16)
    @DisplayName("16 - legge un #redirect in formato visibile con una API action=parse di Mediawiki")
    public void leggeRedirect() {
        System.out.println("16 - legge un #redirect in formato visibile con una API action=parse di Mediawiki");
        System.out.println("Legge (come user) una pagina di #redirect dal server wiki");
        System.out.println("Usa una API con action=parse SENZA bisogno di loggarsi");
        System.out.println("Ripete la lettura usando il #redirect trovato");
        System.out.println("Estrae il testo in linguaggio wiki visibile/leggibile");

        sorgente = "1 maggio";
        previsto = "{{nota disambigua||Primo maggio}}{{maggio}}";

        ottenuto = service.legge(sorgente);
        assertNotNull(ottenuto);
        assertTrue(textService.isValid(ottenuto));
        assertTrue(ottenuto.startsWith(previsto));

        System.out.println(VUOTA);
        System.out.println(String.format("La pagina wiki è: %s", sorgente));
        System.out.println(String.format("Tempo impiegato per leggere %d pagine: %s", cicli, getTime()));
        System.out.println(String.format("La pagina '%s' è un #redirect ad un altra pagina che è stata letta", sorgente));
        System.out.println("Inizio pagina effettiva: " + previsto);

        sorgente = "6 Dicembre";
        previsto = "{{dicembre}}\nIl '''6 dicembre''' è il 340º giorno";

        ottenuto = service.legge(sorgente);
        assertNotNull(ottenuto);
        assertTrue(textService.isValid(ottenuto));
        assertTrue(ottenuto.startsWith(previsto));

        System.out.println(VUOTA);
        System.out.println(String.format("La pagina wiki è: %s", sorgente));
        System.out.println(String.format("Tempo impiegato per leggere %d pagine: %s", cicli, getTime()));
        System.out.println(String.format("La pagina '%s' è un #redirect ad un altra pagina che è stata letta", sorgente));
        System.out.println("Inizio pagina effettiva: " + previsto);
    }

    private void printColonna(List<String> listaColonna) {
        if (arrayService.isAllValid(listaColonna)) {
            for (String stringa : listaColonna) {
                System.out.println(stringa);
            }
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