package it.algos.vaadwiki;


import it.algos.wiki.Api;
import it.algos.wiki.LibWiki;
import name.falgout.jeffrey.testing.junit5.MockitoExtension;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: gio, 07-dic-2017
 * Time: 14:23
 */
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("libwiki")
@DisplayName("Test per la libreria LibWiki")
public class LibWikiTest extends ATest {


    @InjectMocks
    public Api api;

    private String wikiTitle = "Pietro Ponzo";

    private String textPage;


    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        MockitoAnnotations.initMocks(api);
        textPage = api.legge(wikiTitle);
    }// end of method


    /**
     * Estrae il testo di un template BIO dal testo completo della voce
     * Esamina il PRIMO template che trova (ce ne dovrebbe essere solo uno)
     * Gli estremi sono COMPRESI
     * <p>
     * Recupera il tag iniziale con o senza ''Template''
     * Recupera il tag finale di chiusura con o senza ritorno a capo precedente
     * Controlla che non esistano doppie graffe dispari all'interno del template
     */
    @Test
    public void estraeTmplBioCompresi() {
        sorgente = "Questa prova di {{bio forse funziona}}, ma non è detto";
        previsto = "{{bio forse funziona}}";

        ottenuto = LibWiki.estraeTmplBioCompresi(sorgente);
        assertEquals(previsto, ottenuto);

        sorgente = "Questa prova di {{bio\nforse funziona}}, ma non è detto";
        previsto = "{{bio\nforse funziona}}";
        ottenuto = LibWiki.estraeTmplBioCompresi(sorgente);
        assertEquals(previsto, ottenuto);

        sorgente = "Questa prova di {{bio\tforse funziona}}, ma non è detto";
        previsto = "{{bio\tforse funziona}}";
        ottenuto = LibWiki.estraeTmplBioCompresi(sorgente);
        assertEquals(previsto, ottenuto);

        sorgente = "Questa prova di {{bio\rforse funziona}}, ma non è detto";
        previsto = "{{bio\rforse funziona}}";
        ottenuto = LibWiki.estraeTmplBioCompresi(sorgente);
        assertEquals(previsto, ottenuto);

        sorgente = "Questa prova di {{biox forse funziona}}, ma non è detto";
        previsto = "";
        ottenuto = LibWiki.estraeTmplBioCompresi(sorgente);
        assertEquals(previsto, ottenuto);

        sorgente = "Questa prova di {{biografia forse funziona}}, ma non è detto";
        previsto = "{{biografia forse funziona}}";
        ottenuto = LibWiki.estraeTmplBioCompresi(sorgente);
        assertEquals(previsto, ottenuto);
    }// end of single test


    /**
     * Crea un array delle pagine wikimedia dal testo JSON di una risposta multiPagine action=query
     *
     * @param textJSON in ingresso
     *
     * @return array delle singole pagine
     */
    @Test
    public void getArrayPagesJSON() {
        HashMap mappaJSON = null;
        String[] previstoMatrice = {"batchcomplete", "warnings", "query"};
        List previstoLista = Arrays.asList(previstoMatrice);
        List ottenutoLista;

        mappaJSON = LibWiki.getMappaJSON(textPage);
        assertNotNull(mappaJSON);
        assertEquals(3, mappaJSON.size());
        ottenutoLista = Arrays.asList(mappaJSON.keySet().toArray());
        assertEquals(previstoLista, ottenutoLista);
    }// end of single test


    /**
     * Estrae un componente dalla mappa del testo JSON
     *
     * @param textJSON in ingresso
     *
     * @return elemento della mappa JSON
     */
    @Test
    public void getWarnings() {
        HashMap mappaWarnings = LibWiki.getWarnings(textPage);
        ;

        assertNotNull(mappaWarnings);
        assertEquals(2, mappaWarnings.size());
    }// end of single test


    /**
     * Restituisce il testo del warnings di tipo 'result' (se esiste)
     *
     * @param textJSON in ingresso
     *
     * @return testo del warnings
     */
    @Test
    public void getWarningResult() {
        previsto = "";
        ottenuto = LibWiki.getWarningResult(textPage);
        assertEquals(previsto, ottenuto);
    }// end of single test


    @Test
    public void jSonArray() {
        JSONObject objectAll;
        JSONArray arrayPages;
        HashMap<String, Object> mappa = null;

        sorgente = "<?xml version=\"1.0\"?><api><login result=\"Success\" lguserid=\"124123\" lgusername=\"Biobot\" /></api>";
        sorgente = "{\"login\":{\"result\":\"Success\",\"lguserid\":124123,\"lgusername\":\"Biobot\"}}";


        mappa = LibWiki.creaMappaLogin(sorgente);

        ottenuto = LibWiki.getValueStr(mappa, "result");
        Long ottenutoLong = LibWiki.getValueLong(mappa, "lguserid");
        ottenuto = LibWiki.getValueStr(mappa, "lgusername");
        ottenutoBooleano = LibWiki.isLoginValid(sorgente);

        sorgente = "{\"batchcomplete\":true,\"continue\":{\"cmcontinue\":\"page|2f31353f390429432f293f4504062f3929432903062f39294329042f31353f390429432f293f450125418a01c0dcbfdcc1dcbbdc0a|3878354\",\"continue\":\"-||\"},\"query\":{\"categorymembers\":[{\"pageid\":1302888,\"ns\":0,\"title\":\"Nati nel 1201\"},{\"pageid\":775573,\"ns\":0,\"title\":\"Nasir al-Din al-Tusi\"},{\"pageid\":2114785,\"ns\":0,\"title\":\"Richard de Fournival\"}]}}";
        boolean responseValida = LibWiki.isResponseValid(sorgente);
        assertTrue(responseValida);

        sorgente = "{\"batchcomplete\":true,\"continue\":{\"cmcontinue\":\"page|2f31353f390429432f293f4504062f3929432903062f39294329042f31353f390429432f293f450125418a01c0dcbfdcc1dcbbdc0a|3878354\",\"continue\":\"-||\"},\"query\":{\"categorymembers\":[{\"pageid\":1302888,\"ns\":0,\"title\":\"Nati nel 1201\"},{\"pageid\":775573,\"ns\":0,\"title\":\"Nasir al-Din al-Tusi\"},{\"pageid\":2114785,\"ns\":0,\"title\":\"Richard de Fournival\"}]}}";
        mappa = LibWiki.creaMappaCat(sorgente);
        ottenuto = LibWiki.getValueStr(mappa, "query");
        int a = 87;


        sorgente="{\"error\":{\"code\":\"assertbotfailed\",\"info\":\"Assertion that the user has the \\\"bot\\\" right failed.\",\"docref\":\"See https://it.wikipedia.org/w/api.php for API usage. Subscribe to the mediawiki-api-announce mailing list at &lt;https://lists.wikimedia.org/mailman/listinfo/mediawiki-api-announce&gt; for notice of API deprecations and breaking changes.\"},\"servedby\":\"mw1233\"}";
        mappa = LibWiki.creaMappaError(sorgente);
        ottenuto = LibWiki.getValueStr(mappa, "query");
    }// end of single test


}// end of class
