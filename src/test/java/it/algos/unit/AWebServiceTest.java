package it.algos.unit;

import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import static it.algos.vaadflow14.backend.service.AWebService.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.*;

import java.util.*;

/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: gio, 07-mag-2020
 * Time: 07:56
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("testAllValido")
@DisplayName("Test di controllo per i collegamenti base del web.")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AWebServiceTest extends ATest {

    public static final String URL_ERRATO = "htp://www.altos.it/hellogac.html";

    public static final String URL_WEB_GAC = "http://www.algos.it/hellogac.html";

    private static String URL_WIKI_GENERICO = "https://it.wikipedia.org/wiki/ISO_3166-2:IT";

    private static String PAGINA = "ISO 3166-2:IT";


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
    }


    @Test
    @Order(1)
    @DisplayName("1 - Legge un indirizzo URL errato (inesistente)")
    public void leggeErrato() {
        sorgente = URL_ERRATO;

        ottenutoRisultato = web.legge(sorgente);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isErrato());
        assertTrue(ottenutoRisultato.getErrorMessage().equals(UNKNOWN_HOST));

        System.out.println(String.format("Non ha trovato il domain %s richiesto", sorgente));
        System.out.println("Genera un messaggio di errore:");
        System.out.println(ottenutoRisultato.getErrorMessage());

        ottenuto = web.leggeWebTxt(sorgente);
        assertNotNull(ottenuto);
        assertFalse(text.isValid(ottenuto));
    }

    @Test
    @Order(2)
    @DisplayName("2 - Legge un indirizzo URL generico")
    public void leggeGac() {
        sorgente = URL_WEB_GAC;
        previsto = "<!DOCTYPE html><html><body><h1>Telefoni</h1><p style=\"font-family:verdana;font-size:60px\">Gac: 338 9235040</p>";

        ottenutoRisultato = web.legge(sorgente);
        assertTrue(ottenutoRisultato.isValido());

        ottenuto = web.leggeWebTxt(sorgente);
        assertNotNull(ottenuto);
        assertTrue(text.isValid(ottenuto));
        assertTrue(ottenuto.equals(ottenutoRisultato.getText()));
        assertTrue(ottenuto.startsWith(previsto));

        System.out.println(String.format("2 - Legge il testo grezzo di una pagina web"));
        System.out.println(String.format("La pagina web è: %s", ottenutoRisultato.getMessage()));
        System.out.println("Risultato restituito in formato html");
        System.out.println(String.format("Tempo impiegato per leggere %d pagine: %s", cicli, getTime()));
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

        ottenutoRisultato = web.leggeBodyWeb(sorgente);
        assertTrue(ottenutoRisultato.isValido());

        ottenuto = web.leggeBodyWebTxt(sorgente);
        assertNotNull(ottenuto);
        assertTrue(text.isValid(ottenuto));
        assertTrue(ottenuto.equals(ottenutoRisultato.getText()));
        assertTrue(ottenuto.startsWith(previsto));

        System.out.println(String.format("3 - Legge il body di una pagina web"));
        System.out.println(String.format("La pagina web è: %s", ottenutoRisultato.getMessage()));
        System.out.println("Risultato restituito in formato html");
        System.out.println(String.format("Tempo impiegato per leggere %d pagine: %s", cicli, getTime()));
        System.out.println("Faccio vedere solo l'inizio, perché troppo lungo");

        System.out.println(VUOTA);
        System.out.println(ottenuto.substring(0, previsto.length()));
        System.out.println(ottenuto.substring(ottenuto.length() - previsto2.length()));
    }

    @Test
    @Order(4)
    @DisplayName("4 - Legge un indirizzo wiki in formato html")
    public void legge() {
        sorgente = URL_WIKI_GENERICO;

        ottenutoRisultato = web.legge(sorgente);
        assertTrue(ottenutoRisultato.isValido());

        ottenuto = web.leggeWebTxt(sorgente);
        assertNotNull(ottenuto);
        assertTrue(text.isValid(ottenuto));
        assertTrue(ottenuto.equals(ottenutoRisultato.getText()));

        System.out.println(String.format("4 - Legge il sorgente di una pagina wiki"));
        System.out.println(String.format("La pagina wiki è: %s", ottenutoRisultato.getMessage()));
        System.out.println("Risultato restituito in formato html");
        System.out.println(String.format("Tempo impiegato per leggere %d pagine: %s", cicli, getTime()));
        System.out.println("Faccio vedere solo l'inizio, perché troppo lungo");

        System.out.println(VUOTA);
        System.out.println(ottenuto.substring(0, WIDTH));
    }


    @Test
    @Order(5)
    @DisplayName("5 - Legge una pagina wiki")
    public void leggeWiki() {
        sorgente = PAGINA;

        ottenutoRisultato = web.leggeWiki(sorgente);
        assertTrue(ottenutoRisultato.isValido());

        ottenuto = web.leggeWikiTxt(sorgente);
        assertNotNull(ottenuto);
        assertTrue(text.isValid(ottenuto));
        assertTrue(ottenuto.equals(ottenutoRisultato.getText()));

        System.out.println(String.format("5 - Legge il sorgente di una pagina wiki"));
        System.out.println(String.format("La pagina wiki è: %s", ottenutoRisultato.getMessage()));
        System.out.println("Risultato restituito in formato html");
        System.out.println(String.format("Tempo impiegato per leggere %d pagine: %s", cicli, getTime()));
        System.out.println("Faccio vedere solo l'inizio, perché troppo lungo");

        System.out.println(VUOTA);
        System.out.println(ottenuto.substring(0, WIDTH));
    }

    @Test
    @Order(6)
    @DisplayName("6 - Legge una pagina wiki (inesistente)")
    public void leggeWikiMancante() {
        sorgente = "Pagina inesistente";

        ottenutoRisultato = web.leggeWiki(sorgente);
        assertTrue(ottenutoRisultato.isErrato());

        System.out.println(String.format("6 - Cerca di leggere la pagina wiki: %s in formato html", ottenutoRisultato.getMessage()));
        System.out.println(VUOTA);
        System.out.println("Genera un messaggio di errore:");
        System.out.println(ottenutoRisultato.getErrorMessage());

        ottenuto = web.leggeWikiTxt(sorgente);
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
        ottenuto = web.costruisceTagTitoliTable(null);
        assertNotNull(ottenuto);
        assertFalse(text.isValid(ottenuto));

        titoli = new String[]{"Codice"};
        previsto = VUOTA;
        previsto += "<table class=\"wikitable sortable\">";
        previsto += "<tbody><tr>";
        previsto += "<th>";
        previsto += "Codice";

        ottenuto = web.costruisceTagTitoliTable(titoli);
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

        ottenuto = web.costruisceTagTitoliTable(titoli);
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

        ottenuto = web.costruisceTagTitoliTable(titoli);
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
        sorgente = web.leggeWikiTxt(PAGINA);

        String[] titoli = new String[]{"Codice", "Città metropolitane", "Nella regione"};

        ottenuto = web.estraeTableWiki(sorgente, titoli);
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
        sorgente = web.leggeWikiTxt(PAGINA);
        String[] titoli = new String[]{"Codice", "Regioni"};

        ottenuto = web.estraeTableWiki(sorgente, titoli);
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

        lista = web.getRigheTableWiki(PAGINA, titoli);
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

        mappaTable = web.getMappaTableWiki(PAGINA, titoli);
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

        matriceTable = web.getMatriceTableWiki(PAGINA, titoli);
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

        matriceTable = web.getMatriceTableWiki("Comuni del Molise", titoli);
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