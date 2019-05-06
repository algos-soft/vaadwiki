package it.algos.vaadwiki;

import it.algos.vaadwiki.application.WikiCost;
import it.algos.vaadwiki.service.AWikiService;
import it.algos.wiki.web.AQuery;
import name.falgout.jeffrey.testing.junit5.MockitoExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mer, 13-feb-2019
 * Time: 12:27
 */
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("wikiservice")
@DisplayName("Test per le request al server wiki")
public class AWikiServiceTest extends ATest {


    private final static String CAT_INFO = "{\"batchcomplete\":true,\"query\":{\"normalized\":[{\"fromencoded\":false,\"from\":\"Category:Nati nel 1945\",\"to\":\"Categoria:Nati nel 1945\"}],\"pages\":[{\"pageid\":2117062,\"ns\":14,\"title\":\"Categoria:Nati nel 1945\",\"categoryinfo\":{\"size\":2196,\"pages\":2196,\"files\":0,\"subcats\":0,\"hidden\":false}}]}}";

    private final static String TOKEN = "{\"batchcomplete\":true,\"query\":{\"tokens\":{\"csrftoken\":\"29f310ed2871f29d565b2fe4b4ab25bb5c6663e1+\\\\\"}}}";

    @InjectMocks
    protected AWikiService wikiService;


    @BeforeAll
    public void setUp() {
        super.setUpTest();
        MockitoAnnotations.initMocks(this);
        MockitoAnnotations.initMocks(wikiService);
    }// end of method


    @Test
    /**
     * Recupera la mappa di informazioni su una categoria.
     * <p>
     * Dovrebbero essere:
     * pages (long)
     * size (long)
     * hidden (boolean)
     * files (long)
     * subcats (long)
     *
     * @param contenutoCompletoPaginaWebInFormatoJSON in ingresso
     *
     * @return mappa parametri delle informazioni su una categoria
     */
    public void getMappaCategoryInfo() {
        previstoIntero = 5;
        HashMap<String, Object> mappa = wikiService.getMappaCategoryInfo(CAT_INFO);

        assertNotNull(mappa);
        assertNotNull(mappa.get(WikiCost.PAGES));
        assertEquals(previstoIntero, mappa.size());
    }// end of single test


    @Test
    /**
     * Numero di pagine della categoria.
     *
     * @param contenutoCompletoPaginaWebInFormatoJSON in ingresso
     *
     * @return dimensioni della categoria
     */
    public void getNumVociCategory() {
        previstoIntero = 2196;
        ottenutoIntero = wikiService.getNumVociCategory(CAT_INFO);

        assertEquals(previstoIntero, ottenutoIntero);
    }// end of single test


    @Test
    /**
     * Crea una mappa per il token (valori String) dal testo JSON di una pagina di GET preliminary
     *
     * @param textJSON in ingresso
     *
     * @return mappa standard (valori String)
     */
    public void creaMappaToken() {
        HashMap<String, Object> mappa = wikiService.getMappaToken(TOKEN);
        assertNotNull(mappa);
        assertNotNull(mappa.get(AQuery.CSRF_TOKEN));
    }// end of single test



    @Test
    /**
      * Restituisce il token dalla mappa (se esiste)
      *
      * @param mappa standard (valori String)
      *
      * @return logintoken
      */
    public void getToken() {
        ottenuto=wikiService.getToken(TOKEN);
        assertNotNull(ottenuto);
    }// end of single test

//    @Test
//    /**
//     * Recupera l'oggetto pagina dal testo JSON di una pagina action=query
//     *
//     * @param contenutoCompletoPaginaWebInFormatoJSON in ingresso
//     *
//     * @return parametri pages
//     */
//    public void getObjectQuery() {
//        sorgente = "{\"batchcomplete\":true,\"query\":{\"normalized\":[{\"fromencoded\":false,\"from\":\"Category:Nati nel 1945\",\"to\":\"Categoria:Nati nel 1945\"}],\"pages\":[{\"pageid\":2117062,\"ns\":14,\"title\":\"Categoria:Nati nel 1945\",\"categoryinfo\":{\"size\":2196,\"pages\":2196,\"files\":0,\"subcats\":0,\"hidden\":false}}]}}";
//
//        Object alfa=  wikiService.getObjectQuery(sorgente);
//        Object beta=  wikiService.getArrayPagine(sorgente);
//        Object delta=  wikiService.getMappaPagina(sorgente);
//        Object gamma=  wikiService.getListaPages(sorgente);
//        int a=87;
//    }// end of single test

}// end of class
