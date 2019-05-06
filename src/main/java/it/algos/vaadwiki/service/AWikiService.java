package it.algos.vaadwiki.service;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.service.AArrayService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadwiki.application.WikiCost;
import it.algos.wiki.Page;
import it.algos.wiki.PagePar;
import it.algos.wiki.web.AQuery;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import static it.algos.vaadwiki.application.WikiCost.CATEGORY_INFO;
import static it.algos.vaadwiki.application.WikiCost.ENCODE;
import static it.algos.wiki.Cost.QUERY;
import static it.algos.wiki.LibWiki.PAGES;
import static it.algos.wiki.web.AQuery.CSRF_TOKEN;

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
     * Crea una mappa per il token (valori String) dal testo JSON di una pagina di GET preliminary
     *
     * @param textJSON in ingresso
     *
     * @return mappa standard (valori String)
     */
    public HashMap<String, Object> getMappaToken(String textJSON) {
        HashMap<String, Object> mappa = null;
        JSONObject query = getObjectQuery(textJSON);
        JSONObject tokens = getObjectQuery(textJSON);

        if (query.get(AQuery.TOKENS) != null && query.get(AQuery.TOKENS) instanceof JSONObject) {
            tokens = (JSONObject) query.get(AQuery.TOKENS);
            if (tokens.get(CSRF_TOKEN) != null && tokens.get(CSRF_TOKEN) instanceof String) {
                mappa = new HashMap();
                mappa.put(CSRF_TOKEN, tokens.get(CSRF_TOKEN));
            }// fine del blocco if
        }// fine del blocco if

        return mappa;
    }// end of method


    /**
     * Restituisce il token dal testo JSON di una pagina di GET preliminary
     *
     * @param textJSON in ingresso
     *
     * @return logintoken
     */
    public String getToken(String textJSON) {
        String csrfToken = "";
        HashMap<String, Object> mappa = getMappaToken(textJSON);

        if (mappa != null && mappa.get(CSRF_TOKEN) != null && mappa.get(CSRF_TOKEN) instanceof String) {
            csrfToken = (String) mappa.get(CSRF_TOKEN);
        }// fine del blocco if

        return csrfToken;
    } // fine del metodo


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
    public HashMap<String, Object> getMappaCategoryInfo(String contenutoCompletoPaginaWebInFormatoJSON) {
        HashMap<String, Object> mappa = null;
        JSONArray arrayPagine = this.getArrayPagine(contenutoCompletoPaginaWebInFormatoJSON);
        JSONObject object;

        if (arrayPagine != null) {
            if (arrayPagine != null && arrayPagine.size() == 1 && arrayPagine.get(0) != null && arrayPagine.get(0) instanceof JSONObject) {
                object = (JSONObject) arrayPagine.get(0);
                mappa = (HashMap<String, Object>) object.get(CATEGORY_INFO);
            }// fine del blocco if
        }// end of if cycle

        return mappa;
    } // fine del metodo


    /**
     * Numero di pagine della categoria.
     *
     * @param contenutoCompletoPaginaWebInFormatoJSON in ingresso
     *
     * @return dimensioni della categoria
     */
    public int getNumVociCategory(String contenutoCompletoPaginaWebInFormatoJSON) {
        int numVoci = 0;
        Long numVociLong = 0L;
        HashMap<String, Object> mappa = getMappaCategoryInfo(contenutoCompletoPaginaWebInFormatoJSON);

        if (mappa != null) {
            if (mappa != null && mappa.get(WikiCost.PAGES) != null) {
                numVociLong = (Long) mappa.get(WikiCost.PAGES);
                numVoci = numVociLong.intValue();
            }// fine del blocco if
        }// end of if cycle

        return numVoci;
    } // fine del metodo


    /**
     * Costruisce una stringa con i singoli valori divisi da un pipe
     * Ogni singolo valore viene 'encode' come UTF-8, PRIMA di applicare il separatore
     * <p>
     *
     * @param array lista di valori
     *
     * @return stringa con i singoli valori divisi dal separatore pipe e codificati UTF-8
     */
    public String multiPages(ArrayList<String> array) {
        String urlDomain;
        String sep = "|";
        StringBuilder textBuffer = new StringBuilder();

        for (String titolo : array) {
            try { // prova ad eseguire il codice
                titolo = URLEncoder.encode(titolo, ENCODE);
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
            }// fine del blocco try-catch
            textBuffer.append(titolo);
            textBuffer.append(sep);
        } // fine del ciclo for-each
        urlDomain = textBuffer.toString();
        urlDomain = text.levaCoda(urlDomain, sep);

        return urlDomain;
    }// end of method


//    /**
//     * Scrive il testo indicato nella pagina indicata
//     *
//     * @param titoloPagina da modificare
//     * @param testo        da utilizzare per la sostituzione
//     *
//     * @return stringa con i singoli valori divisi dal separatore pipe e codificati UTF-8
//     */
//    public void write(String titoloPagina, String testo) {
//    }// end of method


}// end of class
