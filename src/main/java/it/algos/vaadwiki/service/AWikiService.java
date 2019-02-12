package it.algos.vaadwiki.service;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.service.AArrayService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadwiki.application.WikiCost;
import it.algos.wiki.Page;
import it.algos.wiki.PagePar;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.HashMap;

import static it.algos.wiki.Cost.QUERY;
import static it.algos.wiki.LibWiki.PAGES;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mar, 12-feb-2019
 * Time: 15:48
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class AWikiService {

    @Autowired
    private ATextService text;

    @Autowired
    private AArrayService array;


    /**
     * Recupera una lista di pagine (valori Page) dal testo JSON di una response
     *
     * @param contenutoCompletoPaginaWebInFormatoJSON in ingresso
     *
     * @return lista di pagine (valori Page)
     */
    public ArrayList<Page> getListaPages(String contenutoCompletoPaginaWebInFormatoJSON) {
        ArrayList<Page> listaPages = null;
        JSONArray arrayPagine = null;
        JSONObject objectQuery = getObjectQuery(contenutoCompletoPaginaWebInFormatoJSON);
        HashMap mappa;
        Page page;

        if (text.isValid(contenutoCompletoPaginaWebInFormatoJSON)) {
            if (objectQuery.get(PAGES) != null && objectQuery.get(PAGES) instanceof JSONArray) {
                arrayPagine = (JSONArray) objectQuery.get(PAGES);
            }// fine del blocco if
        }// end of if cycle

        if (array.isValid(arrayPagine)) {
            listaPages = new ArrayList<>();
            for (Object pagina : arrayPagine) {
                mappa = getMappaSingolaPagina((JSONObject) pagina);
                page = new Page(mappa);
                listaPages.add(page);
            }// end of for cycle
        }// end of if cycle

        return listaPages;
    } // fine del metodo


    /**
     * Recupera la mappa dei valori dal testo JSON di una singola pagina
     * 21 parametri
     * 10 generali
     * 8 revisions
     * 3 slots/main
     *
     * @param paginaTextJSON in ingresso
     *
     * @return mappa parametri di una pagina
     */
    public HashMap<String, Object> getMappaSingolaPagina(JSONObject paginaTextJSON) {
        HashMap<String, Object> mappaOut = new HashMap<String, Object>();
        String key;
        Object value;
        JSONArray arrayRevisions;
        JSONObject objectRevisions = null;
        JSONObject objectSlots;
        JSONObject objectMain = null;

        if (paginaTextJSON == null) {
            return null;
        }// end of if cycle
        mappaOut = new HashMap<String, Object>();

        //--parametri revisions
        if (paginaTextJSON.get(WikiCost.REVISIONS) != null && paginaTextJSON.get(WikiCost.REVISIONS) instanceof JSONArray) {
            arrayRevisions = (JSONArray) paginaTextJSON.get(WikiCost.REVISIONS);
            if (arrayRevisions != null && arrayRevisions.size() > 0 && arrayRevisions.get(0) instanceof JSONObject) {
                objectRevisions = (JSONObject) arrayRevisions.get(0);
            }// end of if cycle
        }// end of if cycle

        //--parametri slots/main -> content
        if (objectRevisions.get(WikiCost.SLOTS) != null && objectRevisions.get(WikiCost.SLOTS) instanceof JSONObject) {
            objectSlots = (JSONObject) objectRevisions.get(WikiCost.SLOTS);
            if (objectSlots.get(WikiCost.MAIN) != null && objectSlots.get(WikiCost.MAIN) instanceof JSONObject) {
                objectMain = (JSONObject) objectSlots.get(WikiCost.MAIN);
            }// end of if cycle
        }// end of if cycle

        //--ciclo per tutti i parametri
        for (PagePar par : PagePar.getRead()) {
            value = null;
            key = par.name();

            if (paginaTextJSON.get(key) != null) {
                value = paginaTextJSON.get(key);
            }// fine del blocco if

            if (objectRevisions.get(key) != null) {
                value = objectRevisions.get(key);
            }// fine del blocco if

            if (objectMain.get(key) != null) {
                value = objectMain.get(key);
            }// fine del blocco if

            mappaOut.put(key, value);
        } // fine del ciclo for-each

        return mappaOut;
    } // fine del metodo


    /**
     * Recupera l'oggetto pagina dal testo JSON di una pagina action=query
     *
     * @param contenutoCompletoPaginaWebInFormatoJSON in ingresso
     *
     * @return parametri pages
     */
    public JSONObject getObjectQuery(String contenutoCompletoPaginaWebInFormatoJSON) {
        JSONObject objectQuery = null;
        JSONObject objectAll = (JSONObject) JSONValue.parse(contenutoCompletoPaginaWebInFormatoJSON);

        //--recupera i valori dei parametri pages
        if (objectAll != null && objectAll.get(QUERY) != null && objectAll.get(QUERY) instanceof JSONObject) {
            objectQuery = (JSONObject) objectAll.get(QUERY);
        }// fine del blocco if

        return objectQuery;
    } // fine del metodo


    /**
     * Recupera un array di pagine dal testo JSON di una pagina action=query
     *
     * @param contenutoCompletoPaginaWebInFormatoJSON in ingresso
     *
     * @return parametri pages
     */
    public JSONArray getArrayPagine(String contenutoCompletoPaginaWebInFormatoJSON) {
        JSONArray arrayQuery = null;
        JSONObject objectQuery = getObjectQuery(contenutoCompletoPaginaWebInFormatoJSON);

        //--recupera i valori dei parametri pages
        if (objectQuery != null && objectQuery.get(PAGES) != null && objectQuery.get(PAGES) instanceof JSONArray) {
            arrayQuery = (JSONArray) objectQuery.get(PAGES);
        }// fine del blocco if

        return arrayQuery;
    } // fine del metodo


    /**
     * Recupera la mappa dei valori dal testo JSON di una singola pagina
     * 21 parametri
     * 10 generali
     * 8 revisions
     * 3 slots/main
     *
     * @param singolaPaginaTextJSON in ingresso
     *
     * @return mappa parametri di una pagina
     */
    public HashMap<String, Object> getMappaPagina(String singolaPaginaTextJSON) {
        HashMap<String, Object> mappa = null;
        JSONArray arrayPagine = this.getArrayPagine(singolaPaginaTextJSON);

        if (arrayPagine != null) {
            mappa = this.getMappaSingolaPagina((JSONObject) arrayPagine.get(0));
        }// end of if cycle

        return mappa;
    } // fine del metodo

}// end of class
