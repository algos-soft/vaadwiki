package it.algos.unit;

import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.service.*;
import static it.algos.vaadflow14.backend.service.WebService.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.mockito.*;

import java.util.*;

/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: gio, 07-mag-2020
 * Time: 07:56
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("testAllValido")
@DisplayName("WebService - Collegamenti base del web.")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WebServiceTest extends ATest {

    public static final String URL_ERRATO = "htp://www.altos.it/hellogac.html";

    public static final String URL_WEB_GAC = "http://www.algos.it/hellogac.html";

    private static String URL_WIKI_GENERICO = "https://it.wikipedia.org/wiki/ISO_3166-2:IT";

    private static String PAGINA = "ISO 3166-2:IT";

    /**
     * Classe principale di riferimento <br>
     */
    @InjectMocks
    protected WebService service;

    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpAll() {
        super.setUpStartUp();
    }


    /**
     * Qui passa ad ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    void setUpEach() {
        super.setUp();

        MockitoAnnotations.initMocks(this);
        MockitoAnnotations.initMocks(service);
        Assertions.assertNotNull(service);
        service.text = text;
    }


    @Test
    @Order(1)
    @DisplayName("1 - Legge un indirizzo URL errato (inesistente)")
    public void leggeErrato() {
        sorgente = URL_ERRATO;

        ottenutoRisultato = service.legge(sorgente);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isErrato());
        assertTrue(text.isEmpty(ottenutoRisultato.getWikiTitle()));
        assertEquals(sorgente, ottenutoRisultato.getUrlRequest());
        assertTrue(ottenutoRisultato.getErrorCode().equals(UNKNOWN_HOST));
        assertTrue(ottenutoRisultato.getErrorMessage().equals(UNKNOWN_HOST));
        assertTrue(text.isEmpty(ottenutoRisultato.getValidMessage()));
        assertTrue(text.isEmpty(ottenutoRisultato.getResponse()));
        assertTrue(ottenutoRisultato.getValue() == 0);

        System.out.println(String.format("Non ha trovato il domain '%s' richiesto", ottenutoRisultato.getUrlRequest()));
        System.out.println("Genera un messaggio di errore:");
        System.out.println(ottenutoRisultato.getErrorMessage());

        ottenuto = service.leggeWebTxt(sorgente);
        assertNotNull(ottenuto);
        assertTrue(text.isEmpty(ottenuto));
    }

    @Test
    @Order(2)
    @DisplayName("2 - Legge un indirizzo URL generico")
    public void leggeGac() {
        sorgente = URL_WEB_GAC;
        previsto = "<!DOCTYPE html><html><body><h1>Telefoni</h1><p style=\"font-family:verdana;font-size:60px\">Gac: 338 9235040</p>";

        ottenutoRisultato = service.legge(sorgente);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());
        assertTrue(text.isEmpty(ottenutoRisultato.getWikiTitle()));
        assertEquals(sorgente, ottenutoRisultato.getUrlRequest());
        assertTrue(text.isEmpty(ottenutoRisultato.getErrorCode()));
        assertTrue(text.isEmpty(ottenutoRisultato.getErrorMessage()));
        assertEquals(JSON_SUCCESS, ottenutoRisultato.getValidMessage());
        assertTrue(text.isValid(ottenutoRisultato.getResponse()));
        assertTrue(ottenutoRisultato.getResponse().startsWith(previsto));
        assertTrue(ottenutoRisultato.getValue() == 0);

        ottenuto = service.leggeWebTxt(sorgente);
        assertNotNull(ottenuto);
        assertTrue(text.isValid(ottenuto));
        assertTrue(ottenuto.equals(ottenutoRisultato.getResponse()));
        assertTrue(ottenuto.startsWith(previsto));

        System.out.println(String.format("2 - Legge il testo grezzo di una pagina web"));
        System.out.println(String.format("La pagina web è: %s", ottenutoRisultato.getUrlRequest()));
        System.out.println("Risultato restituito in formato html");
        System.out.println(String.format("Tempo impiegato per leggere la pagina: %s", getTime()));
        System.out.println("Faccio vedere solo l'inizio, perché troppo lungo");

        System.out.println(VUOTA);
        System.out.println(ottenuto.substring(0, previsto.length()));
    }

    @Test
    @Order(3)
    @DisplayName("3 - Legge un body di un URL generico")
    public void leggeBodyGac() {
        sorgente = URL_WEB_GAC;
        previsto = "<h1>Telefoni</h1><p style=\"font-family:verdana;font-size:60px\">Gac: 338 9235040</p>";
        previsto2 = "<p style=\"font-family:verdana;font-size:60px\">2NT-3F: No</p>";

        ottenutoRisultato = service.leggeBodyWeb(sorgente);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());
        assertTrue(text.isEmpty(ottenutoRisultato.getWikiTitle()));
        assertEquals(sorgente, ottenutoRisultato.getUrlRequest());
        assertTrue(text.isEmpty(ottenutoRisultato.getErrorCode()));
        assertTrue(text.isEmpty(ottenutoRisultato.getErrorMessage()));
        assertEquals(JSON_SUCCESS, ottenutoRisultato.getValidMessage());
        assertTrue(text.isValid(ottenutoRisultato.getResponse()));
        assertTrue(ottenutoRisultato.getResponse().startsWith(previsto));
        assertTrue(ottenutoRisultato.getResponse().endsWith(previsto2));
        assertTrue(ottenutoRisultato.getValue() == 0);

        ottenuto = service.leggeBodyWebTxt(sorgente);
        assertNotNull(ottenuto);
        assertTrue(text.isValid(ottenuto));
        assertTrue(ottenuto.equals(ottenutoRisultato.getResponse()));
        assertTrue(ottenuto.startsWith(previsto));
        assertTrue(ottenuto.endsWith(previsto2));

        System.out.println(String.format("3 - Legge il body di una pagina web"));
        System.out.println(String.format("La pagina web è: %s", ottenutoRisultato.getUrlRequest()));
        System.out.println("Risultato restituito in formato html");
        System.out.println(String.format("Tempo impiegato per leggere la pagina: %s", getTime()));
        System.out.println("Faccio vedere solo l'inizio e la fine, perché troppo lungo");

        System.out.println(VUOTA);
        System.out.println(ottenuto.substring(0, previsto.length()));
        System.out.println(ottenuto.substring(ottenuto.length() - previsto2.length()));
    }

    @Test
    @Order(4)
    @DisplayName("4 - Legge un indirizzo wiki in formato html")
    public void legge() {
        sorgente = URL_WIKI_GENERICO;

        ottenutoRisultato = service.legge(sorgente);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());
        assertTrue(text.isEmpty(ottenutoRisultato.getWikiTitle()));
        assertEquals(JSON_SUCCESS, ottenutoRisultato.getValidMessage());
        assertEquals(sorgente, ottenutoRisultato.getUrlRequest());
        assertTrue(text.isEmpty(ottenutoRisultato.getErrorCode()));
        assertTrue(text.isEmpty(ottenutoRisultato.getErrorMessage()));
        assertEquals(JSON_SUCCESS, ottenutoRisultato.getValidMessage());
        assertTrue(text.isValid(ottenutoRisultato.getResponse()));
        assertTrue(ottenutoRisultato.getValue() == 0);

        ottenuto = service.leggeWebTxt(sorgente);
        assertNotNull(ottenuto);
        assertTrue(text.isValid(ottenuto));
        assertTrue(ottenuto.equals(ottenutoRisultato.getResponse()));

        System.out.println(String.format("4 - Legge il sorgente di una pagina wiki letta come url e non come titolo"));
        System.out.println(String.format("La pagina web è: %s", ottenutoRisultato.getUrlRequest()));
        System.out.println("Risultato restituito in formato html");
        System.out.println(String.format("Tempo impiegato per leggere la pagina: %s", getTime()));
        System.out.println("Faccio vedere solo l'inizio, perché troppo lungo");

        System.out.println(VUOTA);
        System.out.println(ottenuto.substring(0, WIDTH));
    }


    @Test
    @Order(5)
    @DisplayName("5 - Legge una pagina wiki")
    public void leggeWiki() {
        sorgente = PAGINA;
        previsto = "<!DOCTYPE html><html class=\"client-nojs\" lang=\"it\" dir=\"ltr\"><head><meta charset=\"UTF-8\"/><title>ISO 3166-2:IT - Wikipedia";

        ottenutoRisultato = service.leggeWiki(sorgente);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());
        assertTrue(text.isValid(ottenutoRisultato.getWikiTitle()));
        assertEquals(sorgente, ottenutoRisultato.getWikiTitle());
        assertTrue(text.isValid(ottenutoRisultato.getUrlRequest()));
        assertEquals(TAG_WIKI + sorgente, ottenutoRisultato.getUrlRequest());
        assertTrue(text.isEmpty(ottenutoRisultato.getErrorCode()));
        assertTrue(text.isEmpty(ottenutoRisultato.getErrorMessage()));
        assertEquals(JSON_SUCCESS, ottenutoRisultato.getValidMessage());
        assertTrue(text.isValid(ottenutoRisultato.getResponse()));
        assertTrue(ottenutoRisultato.getResponse().startsWith(previsto));
        assertTrue(ottenutoRisultato.getValue() == 0);

        ottenuto = service.leggeWikiTxt(sorgente);
        assertNotNull(ottenuto);
        assertTrue(text.isValid(ottenuto));
        assertTrue(ottenuto.equals(ottenutoRisultato.getResponse()));

        System.out.println(String.format("5 - Legge il sorgente di una pagina wiki"));
        System.out.println(String.format("La pagina wiki è: %s", ottenutoRisultato.getWikiTitle()));
        System.out.println("Risultato restituito in formato html");
        System.out.println(String.format("Tempo impiegato per leggere la pagina: %s", getTime()));
        System.out.println("Faccio vedere solo l'inizio, perché troppo lungo");

        System.out.println(VUOTA);
        System.out.println(ottenuto.substring(0, WIDTH));
    }


    @Test
    @Order(6)
    @DisplayName("6 - Legge una pagina wiki (inesistente)")
    public void leggeWikiMancante() {
        sorgente = "Pagina inesistente";

        ottenutoRisultato = service.leggeWiki(sorgente);
        assertNotNull(ottenutoRisultato);
        assertFalse(ottenutoRisultato.isValido());
        assertTrue(ottenutoRisultato.isErrato());
        assertTrue(text.isValid(ottenutoRisultato.getWikiTitle()));
        assertEquals(sorgente, ottenutoRisultato.getWikiTitle());
        assertTrue(text.isValid(ottenutoRisultato.getUrlRequest()));
        assertEquals(TAG_WIKI + sorgente, ottenutoRisultato.getUrlRequest());
        assertEquals(ERROR_FILE_WIKI + sorgente.replaceAll(SPAZIO, UNDERSCORE), ottenutoRisultato.getErrorCode());
        assertEquals(ERROR_FILE_WIKI + sorgente.replaceAll(SPAZIO, UNDERSCORE), ottenutoRisultato.getErrorMessage());
        assertTrue(text.isEmpty(ottenutoRisultato.getValidMessage()));
        assertTrue(text.isEmpty(ottenutoRisultato.getResponse()));
        assertTrue(ottenutoRisultato.getValue() == 0);

        System.out.println(String.format("6 - Cerca di leggere la pagina wiki: %s in formato html", ottenutoRisultato.getMessage()));
        System.out.println(VUOTA);
        System.out.println("Genera un messaggio di errore:");
        System.out.println(ottenutoRisultato.getErrorMessage());

        ottenuto = service.leggeWikiTxt(sorgente);
        assertNotNull(ottenuto);
        assertFalse(text.isValid(ottenuto));
    }


    @Test
    @Order(7)
    @DisplayName("7 - Titoli tabella")
    public void costruisceTagTitoliTable() {
        String[] titoli;
        System.out.println("7 - Costruisce una stringa di testo coi titoli della Table per individuarla nel sorgente della pagina");

        previsto = VUOTA;
        ottenuto = service.costruisceTagTitoliTable(null);
        assertNotNull(ottenuto);
        assertFalse(text.isValid(ottenuto));

        titoli = new String[]{"Codice"};
        previsto = VUOTA;
        previsto += "<table class=\"wikitable sortable\">";
        previsto += "<tbody><tr>";
        previsto += "<th>";
        previsto += "Codice";

        ottenuto = service.costruisceTagTitoliTable(titoli);
        assertNotNull(ottenuto);
        assertEquals(previsto, ottenuto);
        System.out.println(VUOTA);
        System.out.println("Titoli costruiti: " + Arrays.asList(titoli));
        System.out.println(ottenuto);

        titoli = new String[]{"Codice", "Province"};
        previsto = VUOTA;
        previsto += "<table class=\"wikitable sortable\">";
        previsto += "<tbody><tr>";
        previsto += "<th>";
        previsto += "Codice";
        previsto += "</th>";
        previsto += "<th>";
        previsto += "Province";

        ottenuto = service.costruisceTagTitoliTable(titoli);
        assertNotNull(ottenuto);
        assertEquals(previsto, ottenuto);
        System.out.println(VUOTA);
        System.out.println("Titoli costruiti: " + Arrays.asList(titoli));
        System.out.println(ottenuto);

        titoli = new String[]{"Codice", "Province", "Nella regione"};
        previsto = VUOTA;
        previsto += "<table class=\"wikitable sortable\">";
        previsto += "<tbody><tr>";
        previsto += "<th>";
        previsto += "Codice";
        previsto += "</th>";
        previsto += "<th>";
        previsto += "Province";
        previsto += "</th>";
        previsto += "<th>";
        previsto += "Nella regione";

        ottenuto = service.costruisceTagTitoliTable(titoli);
        assertNotNull(ottenuto);
        assertEquals(previsto, ottenuto);

        System.out.println(VUOTA);
        System.out.println("Titoli costruiti: " + Arrays.asList(titoli));
        System.out.println(ottenuto);
    }


    @Test
    @Order(8)
    @DisplayName("8 - Estrae una tavola")
    public void estraeTableWiki() {
        sorgente = service.leggeWikiTxt(PAGINA);

        String[] titoli = new String[]{"Codice", "Città metropolitane", "Nella regione"};

        ottenuto = service.estraeTableWiki(sorgente, titoli);
        assertNotNull(ottenuto);
        assertTrue(text.isValid(ottenuto));

        System.out.println(String.format("8 - Estrae una table"));
        System.out.println(String.format("La pagina wiki è: %s", PAGINA));
        System.out.println("Risultato restituito in formato html");
        System.out.println(String.format("Tempo impiegato per leggere %d pagine: %s", cicli, getTime()));
        System.out.println("Faccio vedere solo l'inizio, perché troppo lungo");

        System.out.println(VUOTA);
        System.out.println("Titoli per selezionare la table: " + Arrays.asList(titoli));
        System.out.println(ottenuto.substring(0, WIDTH));
    }


    @Test
    @Order(9)
    @DisplayName("9 - Estrae un'altra tavola")
    public void estraeTableWiki2() {
        sorgente = service.leggeWikiTxt(PAGINA);
        String[] titoli = new String[]{"Codice", "Regioni"};

        ottenuto = service.estraeTableWiki(sorgente, titoli);
        assertNotNull(ottenuto);
        assertTrue(text.isValid(ottenuto));

        System.out.println(String.format("9 - Estrae un'altra table"));
        System.out.println(String.format("La pagina wiki è: %s", PAGINA));
        System.out.println("Risultato restituito in formato html");
        System.out.println(String.format("Tempo impiegato per leggere %d pagine: %s", cicli, getTime()));
        System.out.println("Faccio vedere solo l'inizio, perché troppo lungo");

        System.out.println(VUOTA);
        System.out.println("Titoli per selezionare la table: " + Arrays.asList(titoli));
        System.out.println(ottenuto.substring(0, WIDTH));
    }


    @Test
    @Order(10)
    @DisplayName("10 - Estrae le righe")
    public void getRigheTableWiki() {
        List<String> lista;
        String[] titoli = new String[]{"Codice", "Regioni"};
        int previstoIntero = 20;

        lista = service.getRigheTableWiki(PAGINA, titoli);
        assertNotNull(lista);
        assertEquals(previstoIntero, lista.size());

        System.out.println(String.format("10 - Estrae le singole righe di una table"));
        System.out.println(String.format("La pagina wiki è: %s", PAGINA));
        System.out.println(String.format("Le righe sono: %s", lista.size()));
        System.out.println("Risultato restituito in formato html");
        System.out.println(String.format("Tempo impiegato per leggere %d pagine: %s", cicli, getTime()));

        System.out.println(VUOTA);
        System.out.println("Titoli per selezionare la table: " + Arrays.asList(titoli));
        for (String riga : lista) {
            System.out.println(riga);
        }
    }


    @Test
    @Order(11)
    @DisplayName("11 - Estrae la mappa")
    public void getMappaTableWiki() {
        LinkedHashMap<String, LinkedHashMap<String, String>> mappaTable = null;
        String[] titoli = new String[]{"Codice", "Regioni"};
        int previstoIntero = 20;

        mappaTable = service.getMappaTableWiki(PAGINA, titoli);
        assertNotNull(mappaTable);
        assertEquals(previstoIntero, mappaTable.size());

        System.out.println(String.format("11 - Estrae una mappa da una pagina wiki"));
        System.out.println(String.format("La pagina wiki è: %s", PAGINA));
        System.out.println(String.format("Le righe della mappa sono: %s", mappaTable.size()));
        System.out.println("Risultato restituito in formato html");
        System.out.println(String.format("Tempo impiegato per leggere %d pagine: %s", cicli, getTime()));

        System.out.println(VUOTA);
        System.out.println("Titoli per selezionare la table: " + Arrays.asList(titoli));
        for (String key : mappaTable.keySet()) {
            for (String key2 : mappaTable.get(key).keySet()) {
                System.out.println(mappaTable.get(key).get(key2));
            }
            System.out.println(VUOTA);
        }
    }


    @Test
    @Order(12)
    @DisplayName("12 - Estrae la matrice")
    public void getMatriceTableWiki() {
        List<List<String>> matriceTable = null;
        String[] titoli = new String[]{"Codice", "Regioni"};
        int previstoIntero = 20;

        matriceTable = service.getMatriceTableWiki(PAGINA, titoli);
        assertNotNull(matriceTable);
        assertEquals(previstoIntero, matriceTable.size());

        System.out.println(String.format("12 - Estrae una matrice da una pagina wiki"));
        System.out.println(String.format("La pagina wiki è: %s", PAGINA));
        System.out.println(String.format("Le righe della matrice sono: %s", matriceTable.size()));
        System.out.println("Risultato restituito in formato html");
        System.out.println(String.format("Tempo impiegato per leggere %d pagine: %s", cicli, getTime()));

        System.out.println(VUOTA);
        System.out.println("Titoli per selezionare la table: " + Arrays.asList(titoli));
        for (List<String> riga : matriceTable) {
            for (String key : riga) {
                System.out.println(key);
            }
            System.out.println(VUOTA);
        }
    }


    @Test
    @Order(13)
    @DisplayName("13 - Estrae un'altra matrice")
    public void getMatriceTableWiki2() {
        List<List<String>> matriceTable = null;
        String[] titoli = new String[]{"pos.", "comune"};

        int previstoIntero = 136;

        matriceTable = service.getMatriceTableWiki("Comuni del Molise", titoli);
        assertNotNull(matriceTable);
        assertEquals(previstoIntero, matriceTable.size());

        System.out.println(String.format("13 - Estrae una matrice da una pagina wiki"));
        System.out.println(String.format("La pagina wiki è: %s", PAGINA));
        System.out.println(String.format("Le righe della matrice sono: %s", matriceTable.size()));
        System.out.println("Risultato restituito in formato html");
        System.out.println(String.format("Tempo impiegato per leggere %d pagine: %s", cicli, getTime()));

        System.out.println(VUOTA);
        System.out.println("Titoli per selezionare la table: " + Arrays.asList(titoli));
        for (List<String> riga : matriceTable) {
            System.out.println(VUOTA);
            for (String key : riga) {
                System.out.println(key);
            }
        }
    }

}