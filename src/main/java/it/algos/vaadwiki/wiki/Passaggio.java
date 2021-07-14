package it.algos.vaadwiki.wiki;

import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.backend.wrapper.*;
import it.algos.vaadflow14.wiki.*;
import static it.algos.vaadflow14.wiki.AWikiApiService.*;
import org.json.simple.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import java.net.*;
import java.util.*;
import java.util.regex.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: lun, 12-lug-2021
 * Time: 10:47
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class Passaggio {

    public static final String WIKI = "https://it.wikipedia.org/w/api.php?&format=json&formatversion=2&action=query";

    public static final String WIKI_QUERY = WIKI + "&rvslots=main&prop=info|revisions&rvprop=content|ids|flags|timestamp|user|userid|comment|size&titles=";

    public static final String WIKI_PARSE = "https://it.wikipedia.org/w/api.php?action=parse&prop=wikitext&formatversion=2&format=json&page=";

    public static final String WIKI_QUERY_TITLES = WIKI + "&rvslots=main&prop=revisions&rvprop=content|ids|timestamp&titles=";

    public static final String WIKI_QUERY_PAGEIDS = WIKI + "&rvslots=main&prop=revisions&rvprop=content|ids|timestamp&pageids=";

    public static final String WIKI_QUERY_TIMESTAMP = WIKI + "&prop=revisions&rvprop=ids|timestamp&limit=" + LIMIT_USER + "&pageids=";

    public static final String WIKI_QUERY_CATEGORY = WIKI + "&list=categorymembers&cmtitle=Categoria:";

    public static final String WIKI_QUERY_CAT_LIMIT_USER = "&cmlimit=500";

    public static final String WIKI_QUERY_CAT_LIMIT_BOT = "&cmlimit=5000";

    public static final String WIKI_QUERY_CAT_CONTINUE = "&cmcontinue=";

    public static final String WIKI_QUERY_CAT_TYPE = "&cmtype=";

    public static final String WIKI_QUERY_CAT_PROP = "&cmprop=";

    public static final String WIKI_QUERY_CAT_TOTALE = WIKI + "&prop=categoryinfo&titles=Categoria:";

    public static final String WIKI_QUERY_USER = "&assert=user";

    public static final String WIKI_QUERY_BOT = "&assert=bot";

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ATextService text;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AWebService web;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AHtmlService html;
    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AArrayService array;

    /**
     * Legge (come user) una pagina dal server wiki <br>
     * Usa una API con action=query SENZA bisogno di loggarsi <br>
     * Recupera dalla urlRequest tutti i dati della pagina <br>
     * Estrae il testo in linguaggio wiki visibile <br>
     * Elaborazione della urlRequest leggermente più complessa di leggeParse <br>
     * Tempo di download leggermente più corto di leggeParse <br>
     * Metodo base per tutte le API in semplice lettura <br>
     *
     * @param wikiTitleGrezzo della pagina wiki
     *
     * @return risultato col testo completo (visibile) della pagina wiki
     */
    public AIResult leggeQuery(final String wikiTitleGrezzo) {
        AIResult result;
        String webUrl;
        String rispostaDellaQuery;
        String testoValido;

        if (text.isEmpty(wikiTitleGrezzo)) {
            return AResult.errato("Manca il wikiTitle");
        }

        webUrl = webUrlQuery(wikiTitleGrezzo);
        if (text.isValid(webUrl)) {
            result = web.legge(webUrl);
            rispostaDellaQuery = result.getText();
            testoValido = estraeTestoPaginaWiki(rispostaDellaQuery);
            result.setText(testoValido);
            return result;
        }
        else {
            return AResult.errato("Manca il domain");
        }
    }

    /**
     * Regola l'url per interrogare una pagina wiki <br>
     * Recupera spazio e caratteri strani nel titolo <br>
     * Aggiunge in testa il prefisso della API Mediawiki <br>
     *
     * @param wikiTitleGrezzo della pagina wiki
     *
     * @return webUrl completo
     */
    public String webUrlQuery(final String wikiTitleGrezzo) {
        return WIKI_QUERY + fixWikiTitle(wikiTitleGrezzo);
    }

    /**
     * Regola l'url per interrogare una pagina wiki <br>
     * Recupera spazio e caratteri strani nel titolo <br>
     * Aggiunge in testa il prefisso della API Mediawiki <br>
     *
     * @param wikiTitleGrezzo della pagina wiki
     *
     * @return webUrl completo
     */
    public String webUrlQueryTitles(final String wikiTitleGrezzo) {
        return WIKI_QUERY_TITLES + fixWikiTitle(wikiTitleGrezzo);
    }

    /**
     * Regola l'url per interrogare una pagina wiki <br>
     * Recupera spazio e caratteri strani nel titolo <br>
     * Aggiunge in testa il prefisso della API Mediawiki <br>
     *
     * @param wikiTitleGrezzo della pagina wiki
     *
     * @return webUrl completo
     */
    public String webUrlParse(final String wikiTitleGrezzo) {
        return WIKI_PARSE + fixWikiTitle(wikiTitleGrezzo);
    }

    /**
     * Legge una lista di pageid di una categoria wiki <br>
     * Se non si mette 'cmlimit' restituisce 10 pagine <br>
     * Valore massimo di 'cmlimit' (come user) 500 pagine <br>
     * Il valore massimo (come user) di 'cmlimit' è 20 <br>
     * La query restituisce SOLO pageid <br>
     *
     * @param catTitle da recuperare
     *
     * @return lista di pageid
     */
    public List<Long> getLongCat(final String catTitle) {
        List<Long> lista = new ArrayList<>();
        String catType = AECatType.page.getTag();
        String urlDomain;
        String propType = AECatProp.pageid.getTag();
        AIResult result;
        String continueParam = VUOTA;

        do {
            urlDomain = fixUrlCat(catTitle, catType, propType, continueParam,true);
            result = web.legge(urlDomain);
            lista.addAll(getListaLongCategoria(result.getText()));
            continueParam = getContinuaCategoria(result.getText());
        }
        while (text.isValid(continueParam));

        return lista;
    }

    /**
     * Legge una lista di titles di una categoria wiki <br>
     * Se non si mette 'cmlimit' restituisce 10 pagine <br>
     * Valore massimo di 'cmlimit' (come user) 500 pagine <br>
     * Il valore massimo (come user) di 'cmlimit' è 20 <br>
     * La query restituisce SOLO pageid <br>
     *
     * @param catTitle da recuperare
     *
     * @return lista di titles
     */
    public List<String> getTitleCat(final String catTitle) {
        List<String> lista = new ArrayList<>();
        String catType = AECatType.page.getTag();
        String urlDomain;
        String propType = AECatProp.title.getTag();
        AIResult result;
        String continueParam = VUOTA;

        do {
            urlDomain = fixUrlCat(catTitle, catType, propType, continueParam,true);
            result = web.legge(urlDomain);
            lista.addAll(getListaTitleCategoria(result.getText()));
            continueParam = getContinuaCategoria(result.getText());
        }
        while (text.isValid(continueParam));

        return lista;
    }

    /**
     * Costruisce l'url <br>
     *
     * @param catTitle      da recuperare
     * @param catType       per la selezione
     * @param continueParam per la successiva query
     *
     * @return testo dell'url
     */
    private String fixUrlCat(final String catTitle, final String catType, final String propType, final String continueParam, final boolean bot) {
        String query = WIKI_QUERY_CATEGORY + fixWikiTitle(catTitle);
        String type = WIKI_QUERY_CAT_TYPE + catType;
        String prop = WIKI_QUERY_CAT_PROP + propType;
        String limit = bot ? WIKI_QUERY_CAT_LIMIT_BOT : WIKI_QUERY_CAT_LIMIT_USER;
        String user = bot ? WIKI_QUERY_BOT : WIKI_QUERY_USER;
        String continua = WIKI_QUERY_CAT_CONTINUE + continueParam;

        return  String.format("%s%s%s%s%s%s", query, type, prop, limit, user, continua);
    }

    /**
     * Recupera un lista di 'pageid'' dal testo JSON di risposta ad una query <br>
     *
     * @param rispostaDellaQuery in ingresso
     *
     * @return array di pageid
     */
    private List<Long> getListaLongCategoria(String rispostaDellaQuery) {
        JSONArray jsonPagine = getJsonPagine(rispostaDellaQuery);
        return getListaLongCategoria(jsonPagine);
    }

    /**
     * Recupera un lista di 'pageid'' dal testo JSON di risposta ad una query <br>
     *
     * @param jsonPagine in ingresso
     *
     * @return array di 'pageid'
     */
    private List<Long> getListaLongCategoria(JSONArray jsonPagine) {
        List<Long> lista = new ArrayList<>();
        long pageid;

        if (jsonPagine != null && jsonPagine.size() > 0) {
            for (Object obj : jsonPagine) {
                pageid = (long) ((JSONObject) obj).get(PAGE_ID);
                lista.add(pageid);
            }
        }

        return lista;
    }

    /**
     * Recupera un lista di 'pageid'' dal testo JSON di risposta ad una query <br>
     *
     * @param rispostaDellaQuery in ingresso
     *
     * @return array di pageid
     */
    private List<String> getListaTitleCategoria(String rispostaDellaQuery) {
        JSONArray jsonPagine = getJsonPagine(rispostaDellaQuery);
        return getListaTitleCategoria(jsonPagine);
    }

    /**
     * Recupera un lista di 'title'' dal testo JSON di risposta ad una query <br>
     *
     * @param jsonPagine in ingresso
     *
     * @return array di 'title'
     */
    private List<String> getListaTitleCategoria(JSONArray jsonPagine) {
        List<String> lista = new ArrayList<>();
        String title;

        if (jsonPagine != null && jsonPagine.size() > 0) {
            for (Object obj : jsonPagine) {
                title = (String) ((JSONObject) obj).get(TITLE);
                lista.add(title);
            }
        }

        return lista;
    }

    /**
     * Recupera spazio e caratteri strani nel titolo <br>
     *
     * @param wikiTitleGrezzo della pagina wiki
     *
     * @return titolo 'spedibile' al server
     */
    public String fixWikiTitle(final String wikiTitleGrezzo) {
        String wikiTitle = wikiTitleGrezzo.replaceAll(SPAZIO, UNDERSCORE);
        try {
            wikiTitle = URLEncoder.encode(wikiTitle, ENCODE);

        } catch (Exception unErrore) {

        }

        return wikiTitle;
    }

    /**
     * Recupera il testo di una singola pagina dalla risposta alla query <br>
     * La query è la richiesta di una sola singola pagina <br>
     * <p>
     * 21 parametri
     * 10 generali
     * 8 revisions
     * 3 slots/main
     *
     * @param rispostaDellaQuery in ingresso
     *
     * @return testo della prima pagina
     */
    public String estraeTestoPaginaWiki(final String rispostaDellaQuery) {
        String testoPagina = VUOTA;
        JSONObject jsonPageZero = getObjectPage(rispostaDellaQuery);

        if (jsonPageZero != null) {
            testoPagina = this.getContent(jsonPageZero);
        }

        return testoPagina;
    }


    /**
     * Recupera una singola page dal testo JSON di risposta ad una query <br>
     *
     * @param rispostaDellaQuery in ingresso
     *
     * @return singola page (la prima)
     */
    public JSONObject getObjectPage(String rispostaDellaQuery) {
        JSONObject objectPage = null;
        JSONArray arrayPagine = this.getArrayPagine(rispostaDellaQuery);

        if (arrayPagine != null) {
            objectPage = (JSONObject) arrayPagine.get(0);
        }

        return objectPage;
    }


    /**
     * Recupera un array di pages dal testo JSON di risposta ad una query <br>
     *
     * @param rispostaDellaQuery in ingresso
     *
     * @return array di pages
     */
    public JSONArray getArrayPagine(String rispostaDellaQuery) {
        JSONArray arrayQuery = null;
        JSONObject objectQuery = getPages(rispostaDellaQuery);

        //--recupera i valori dei parametri pages
        if (objectQuery != null && objectQuery.get(PAGES) != null && objectQuery.get(PAGES) instanceof JSONArray) {
            arrayQuery = (JSONArray) objectQuery.get(PAGES);
        }

        return arrayQuery;
    }

    /**
     * Recupera l'oggetto 'pages'' dal testo JSON di una pagina action=query
     *
     * @param rispostaDellaQuery in ingresso
     *
     * @return oggetto 'pages'
     */
    public JSONObject getPages(String rispostaDellaQuery) {
        JSONObject objectQuery = null;
        JSONObject objectAll = (JSONObject) JSONValue.parse(rispostaDellaQuery);

        //--recupera i valori dei parametri pages
        if (objectAll != null && objectAll.get(QUERY) != null && objectAll.get(QUERY) instanceof JSONObject) {
            objectQuery = (JSONObject) objectAll.get(QUERY);
        }

        return objectQuery;
    }

    public String getContent(String wikiTitle) {
        String textContent = VUOTA;
        WikiPage wikiPage = getWikiPageFromTitle(wikiTitle);

        if (wikiPage != null) {
            textContent = wikiPage.getContent();
        }

        return textContent;
    }

    public WikiPage getWikiPageFromTitle(String wikiTitle) {
        String rispostaDellaQuery = leggeJsonTxt(wikiTitle);
        JSONObject jsonPageZero = getObjectPage(rispostaDellaQuery);
        Map<String, Object> mappa = getMappaJSON(jsonPageZero);

        return getWikiPageFromMappa(mappa);
    }

    /**
     * Legge la risposta in formato JSON ad una query su API Mediawiki <br>
     * Usa le API base SENZA loggarsi <br>
     * Testo in linguaggio JSON non leggibile <br>
     *
     * @param wikiTitle della pagina wiki
     *
     * @return risultato col testo completo in formato JSON della pagina wiki, che può contenere più 'pages'
     */
    public String leggeJsonTxt(final String wikiTitle) {
        return leggeJson(wikiTitle).getText();
    }

    /**
     * Legge la risposta in formato JSON ad una query su API Mediawiki <br>
     * Usa le API base SENZA loggarsi <br>
     * Testo in linguaggio JSON non leggibile <br>
     *
     * @param wikiTitle della pagina wiki
     *
     * @return risultato col testo completo in formato JSON della pagina wiki, che può contenere più 'pages'
     */
    public AIResult leggeJson(final String wikiTitle) {
        if (text.isEmpty(wikiTitle)) {
            return AResult.errato("Manca il wikiTitle");
        }

        return web.legge(WIKI_QUERY_TITLES + wikiTitle);
    }

    /**
     * Crea una mappa standard (valori reali) da una singola page JSON di una multi-pagina action=query <br>
     *
     * @param paginaJSON in ingresso
     *
     * @return mappa query (valori reali)
     */
    public HashMap<String, Object> getMappaJSON(JSONObject paginaJSON) {
        HashMap<String, Object> mappa = new HashMap<String, Object>();
        HashMap<String, Object> mappaRev;
        JSONArray arrayRev;
        String keyPage;
        Object value;

        if (paginaJSON == null) {
            return null;
        }

        //--recupera i valori dei parametri info
        for (PagePar par : PagePar.getRead()) {
            keyPage = par.toString();
            if (paginaJSON.get(keyPage) != null) {
                value = paginaJSON.get(keyPage);
                mappa.put(keyPage, value);
            }
        }

        //--recupera i valori dei parametri revisions
        arrayRev = (JSONArray) paginaJSON.get(REVISIONS);
        if (arrayRev != null) {
            mappaRev = estraeMappaJsonPar(arrayRev);
            for (String key : mappaRev.keySet()) {
                value = mappaRev.get(key);
                mappa.put(key, value);
            }
        }

        return mappa;
    }

    /**
     * Estrae una mappa standard da un JSONArray
     * Considera SOLO i valori della Enumeration PagePar
     *
     * @param arrayJson JSONArray in ingresso
     *
     * @return mappa standard (valori String)
     */
    private HashMap<String, Object> estraeMappaJsonPar(JSONArray arrayJson) {
        return estraeMappaJsonPar(arrayJson, 0);
    }

    /**
     * Estrae una mappa standard da un JSONArray
     * Considera SOLO i valori della Enumeration PagePar
     *
     * @param arrayJson JSONArray in ingresso
     * @param pos       elemento da estrarre
     *
     * @return mappa standard (valori String)
     */
    public HashMap<String, Object> estraeMappaJsonPar(JSONArray arrayJson, int pos) {
        HashMap<String, Object> mappaOut = new HashMap<String, Object>();
        JSONObject mappaJSON = null;
        String key;
        Object value;
        String slots = "slots";
        String main = "main";
        String contentName = "content";
        String content = VUOTA;
        JSONObject slotsObject = null;
        JSONObject mainObject = null;
        JSONObject contentObject = null;

        if (arrayJson != null && arrayJson.size() > pos) {
            if (arrayJson.get(pos) != null && arrayJson.get(pos) instanceof JSONObject) {
                mappaJSON = (JSONObject) arrayJson.get(pos);
            }
        }

        if (mappaJSON != null) {
            for (PagePar par : PagePar.getRead()) {
                key = par.toString();
                if (mappaJSON.get(key) != null) {
                    value = mappaJSON.get(key);
                    mappaOut.put(key, value);
                }
            }
        }

        //--content
        if (mappaJSON.get(slots) != null) {
            slotsObject = (JSONObject) mappaJSON.get(slots);
            mainObject = (JSONObject) slotsObject.get(main);
            mappaOut.put(contentName, mainObject.get(contentName));
        }

        return mappaOut;
    }

    public WikiPage getWikiPageFromMappa(Map<String, Object> mappa) {
        WikiPage wiki = new WikiPage();
        fixMappaWiki(wiki, mappa);

        return wiki;
    }

    /**
     * Regola i parametri della tavola in base alla mappa letta dal server
     * Aggiunge le date di riferimento lettura/scrittura
     */
    public WikiPage fixMappaWiki(WikiPage wiki, Map mappa) {
        List<PagePar> lista = PagePar.getDB();
        String key;
        Object value;

        for (PagePar par : lista) {
            key = par.toString();
            value = null;

            if (mappa.get(key) != null) {
                value = mappa.get(key);
            }

            //--controllo dei LONG che POSSONO essere anche zero
            if (par.getType() == PagePar.TypeField.longzero) {
                if (value == null) {
                    value = 0;
                }
            }

            //--patch
            if (par == PagePar.comment) {
                if (value instanceof String) {
                    if (((String) value).startsWith("[[WP:OA|←]]")) {
                        value = "Nuova pagina";
                    }
                }
            }

            par.setWiki(wiki, value);
        }

        return wiki;
    }

    /**
     * Legge (come user) una pagina dal server wiki <br>
     * Usa una API con action=query SENZA bisogno di loggarsi <br>
     * Recupera dalla urlRequest tutti i dati della pagina <br>
     * Estrae il testo in linguaggio wiki visibile <br>
     * Elaborazione della urlRequest leggermente più complessa di leggeParse <br>
     * Tempo di download leggermente più corto di leggeParse <br>
     * Metodo base per tutte le API in semplice lettura <br>
     *
     * @param wikiTitle della pagina wiki
     *
     * @return risultato col testo completo (visibile) della pagina wiki
     */
    public String leggeQueryTxt(final String wikiTitle) {
        return leggeQuery(wikiTitle).getText();
    }


    /**
     * Legge il numero di pagine di una categoria wiki <br>
     *
     * @param categoryTitle da recuperare
     *
     * @return numero di pagine (subcategorie escluse)
     */
    public int getTotaleCategoria(final String categoryTitle) {
        int totale = 0;
        String webUrl = WIKI_QUERY_CAT_TOTALE + categoryTitle;
        String rispostaDellaQuery = web.legge(webUrl).getText();

        JSONObject jsonPageZero = this.getObjectPage(rispostaDellaQuery);
        if (isMissing(jsonPageZero)) {
            return totale;
        }

        JSONObject categoryInfo = (JSONObject) jsonPageZero.get(CATEGORY_INFO);
        if (categoryInfo != null && categoryInfo.get(CATEGORY_PAGES) != null) {
            totale = ((Long) categoryInfo.get(CATEGORY_PAGES)).intValue();
        }

        return totale;
    }

    private boolean isMissing(final JSONObject jsonPage) {
        return jsonPage.get(KEY_JSON_MISSING) != null && (boolean) jsonPage.get(KEY_JSON_MISSING);
    }

    private WrapPage creaPage(final String webUrl, final JSONObject jsonPage, String tagTemplate) {
        long pageid;
        String title;
        String stringTimestamp;
        String content;
        String tmpl;

        title = (String) jsonPage.get(KEY_JSON_TITLE);

        if (isMissing(jsonPage)) {
            return new WrapPage(webUrl, title, AETypePage.nonEsiste);
        }

        pageid = (long) jsonPage.get(KEY_JSON_PAGE_ID);
        JSONArray jsonRevisions = (JSONArray) jsonPage.get(KEY_JSON_REVISIONS);
        JSONObject jsonRevZero = (JSONObject) jsonRevisions.get(0);
        stringTimestamp = (String) jsonRevZero.get(KEY_JSON_TIMESTAMP);
        JSONObject jsonSlots = (JSONObject) jsonRevZero.get(KEY_JSON_SLOTS);
        JSONObject jsonMain = (JSONObject) jsonSlots.get(KEY_JSON_MAIN);
        content = (String) jsonMain.get(KEY_JSON_CONTENT);

        //--la pagina esiste ma il content no
        if (text.isEmpty(content)) {
            return new WrapPage(webUrl, pageid, title, VUOTA, stringTimestamp, AETypePage.testoVuoto);
        }

        //--contenuto inizia col tag della disambigua
        if (content.startsWith(TAG_DISAMBIGUA_UNO) || content.startsWith(TAG_DISAMBIGUA_DUE)) {
            return new WrapPage(webUrl, title, AETypePage.disambigua);
        }

        //--contenuto inizia col tag del redirect
        if (content.startsWith(TAG_REDIRECT_UNO) || content.startsWith(TAG_REDIRECT_DUE) || content.startsWith(TAG_REDIRECT_TRE) || content.startsWith(TAG_REDIRECT_QUATTRO)) {
            return new WrapPage(webUrl, title, AETypePage.redirect);
        }

        //--flag per la ricerca o meno del template
        if (text.isValid(tagTemplate)) {
            //--prova ad estrarre il template
//            tmpl = wikiApi.estraeTmpl(content, tagTemplate);//@todo RIMETTERE
            tmpl="pippoz";
            if (text.isValid(tmpl)) {
                return new WrapPage(webUrl, pageid, title, tmpl, stringTimestamp, AETypePage.testoConTmpl);
            }
            else {
                return new WrapPage(webUrl, pageid, title, content, stringTimestamp, AETypePage.mancaTmpl);
            }
        }
        else {
            return new WrapPage(webUrl, pageid, title, content, stringTimestamp, AETypePage.testoSenzaTmpl);
        }
    }



    /**
     * Recupera le 'pagine' dal testo JSON di risposta ad una query <br>
     *
     * @param rispostaDellaQuery in ingresso
     *
     * @return array di 'pagine''
     */
    public JSONArray getJsonPagine(String rispostaDellaQuery) {
        JSONArray jsonPagine = null;
        JSONObject objectQuery = getPages(rispostaDellaQuery);

        //--recupera i valori dei parametri
        if (objectQuery != null && objectQuery.get(CATEGORY) != null && objectQuery.get(CATEGORY) instanceof JSONArray) {
            jsonPagine = (JSONArray) objectQuery.get(CATEGORY);
        }

        return jsonPagine;
    }

    /**
     * Legge una lista di pageid di una categoria wiki <br>
     * Se non si mette 'cmlimit' restituisce 10 pagine <br>
     * Valore massimo di 'cmlimit' (come user) 500 pagine <br>
     * Il valore massimo (come user) di 'cmlimit' è 20 <br>
     * La query restituisce SOLO pageid <br>
     *
     * @param categoryTitle da recuperare
     *
     * @return lista di pageid
     */
    public String getPageidsCat(final String categoryTitle) {
        String striscia = VUOTA;
        List<Long> lista = getLongCat(categoryTitle);

        striscia = array.toStringaPipe(lista);

        return striscia;
    }

    /**
     * Legge una lista di WrapCat di una categoria wiki <br>
     * Se non si mette 'cmlimit' restituisce 10 pagine <br>
     * Valore massimo di 'cmlimit' (come user) 500 pagine <br>
     * Il valore massimo (come user) di 'cmlimit' è 20 <br>
     * La query restituisce sia pageid che title <br>
     *
     * @param titleWikiCategoria da recuperare
     *
     * @return lista di WrapCat
     */
    public List<WrapCat> getWrapCat(final String titleWikiCategoria) {
        return getWrapCat(titleWikiCategoria, AECatType.page);
    }

    /**
     * Legge una lista di WrapCat di pagine/files/subcategorie di una categoria wiki <br>
     * Se non si mette 'cmlimit' restituisce 10 pagine <br>
     * Valore massimo di 'cmlimit' (come user) 500 pagine <br>
     * Il valore massimo (come user) di 'cmlimit' è 20 <br>
     * La query restituisce sia pageid che title <br>
     *
     * @param catTitle  da recuperare
     * @param aeCatType per la selezione
     *
     * @return lista di WrapCat
     */
    public List<WrapCat> getWrapCat(final String catTitle, final AECatType aeCatType) {
        List<WrapCat> lista = new ArrayList<>();
        String catType = aeCatType.getTag();
        String urlDomain;
        AIResult result;
        String propType = AECatProp.all.getTag();
        String continueParam = VUOTA;

        do {
            urlDomain = fixUrlCat(catTitle, catType, propType, continueParam,true);
            result = web.legge(urlDomain);
            if (result.isValido()) {
                lista.addAll(getListaWrapCategoria(result.getText()));
                continueParam = getContinuaCategoria(result.getText());
            }
            else {
                int a=87;
            }
        }
        while (text.isValid(continueParam));

        return lista;
    }

    /**
     * Recupera un lista di WrapCat dal testo JSON di risposta ad una query <br>
     *
     * @param rispostaDellaQuery in ingresso
     *
     * @return array di WrapCat
     */
    private List<WrapCat> getListaWrapCategoria(String rispostaDellaQuery) {
        JSONArray jsonPagine = getJsonPagine(rispostaDellaQuery);
        return getListaWrapCategoria(jsonPagine);
    }

    /**
     * Recupera il tag per le categorie successive <br>
     * Nella forma 'page|token|pageid' <br>
     *
     * @param rispostaDellaQuery in ingresso
     *
     * @return token e prossima pagina
     */
    private String getContinuaCategoria(String rispostaDellaQuery) {
        String continua = VUOTA;
        JSONObject objectAll = (JSONObject) JSONValue.parse(rispostaDellaQuery);
        JSONObject jsonContinue;

        //--recupera il valore del parametro
        if (objectAll != null && objectAll.get(CONTINUE) != null && objectAll.get(CONTINUE) instanceof JSONObject) {
            jsonContinue = (JSONObject) objectAll.get(CONTINUE);
            continua = (String) jsonContinue.get(CONTINUE_CM);
        }

        return continua;
    }

    /**
     * Legge (come user) una serie pagina dal server wiki <br>
     * Usa una API con action=query SENZA bisogno di loggarsi <br>
     * Recupera dalla urlRequest title, pageid, timestamp e wikitext <br>
     * Estrae il wikitext in linguaggio wiki visibile <br>
     *
     * @param pageIds della pagina wiki
     *
     * @return wrapper con testo completo (visibile) della pagina wiki
     */
    public List<WrapPage> leggePages(String pageIds, String tagTemplate) {
        List<WrapPage> wraps = null;
        pageIds = fixWikiTitle(pageIds);
        String webUrl = WIKI_QUERY_PAGEIDS + pageIds;
        String rispostaAPI = web.legge(webUrl).getText();
        WrapPage wrap = null;

        JSONArray jsonPages = getArrayPagine(rispostaAPI);
        if (jsonPages != null) {
            wraps = new ArrayList<>();
            for (Object obj : jsonPages) {
                wrap = creaPage(webUrl, (JSONObject) obj, tagTemplate);
                if (wrap.isValida()) {
                    wraps.add(wrap);
                }
            }
        }

        return wraps;
    }

    /**
     * Legge (come user) una pagina dal server wiki <br>
     * Usa una API con action=query SENZA bisogno di loggarsi <br>
     * Recupera dalla urlRequest title, pageid, timestamp e wikitext <br>
     * Estrae il wikitext in linguaggio wiki visibile <br>
     *
     * @param pageId della pagina wiki
     *
     * @return wrapper con testo completo (visibile) della pagina wiki
     */
    public WrapPage leggePage(final long pageId) {
        String webUrl = WIKI_QUERY_PAGEIDS + pageId;

        return creaPage(webUrl, VUOTA);
    }

    /**
     * Legge (come user) una pagina dal server wiki <br>
     * Usa una API con action=query SENZA bisogno di loggarsi <br>
     * Recupera dalla urlRequest title, pageid, timestamp e wikitext <br>
     * Estrae il wikitext in linguaggio wiki visibile <br>
     *
     * @param wikiTitleGrezzo della pagina wiki
     *
     * @return wrapper con testo completo (visibile) della pagina wiki
     */
    public WrapPage leggePage(final String wikiTitleGrezzo) {
        String webUrl = webUrlQueryTitles(wikiTitleGrezzo);
        return creaPage(webUrl, VUOTA);
    }

    /**
     * Legge (come user) una pagina dal server wiki <br>
     * Usa una API con action=query SENZA bisogno di loggarsi <br>
     * Recupera dalla urlRequest title, pageid, timestamp e tmpl <br>
     * Estrae il wikitext in linguaggio wiki visibile <br>
     *
     * @param wikiTitleGrezzo della pagina wiki
     *
     * @return wrapper con template (visibile) della pagina wiki
     */
    public WrapPage leggePage(final String wikiTitleGrezzo, String tagTemplate) {
        String webUrl = webUrlQueryTitles(wikiTitleGrezzo);
        return creaPage(webUrl, tagTemplate);
    }

    /**
     * Legge (come user) una pagina dal server wiki <br>
     * Usa una API con action=query SENZA bisogno di loggarsi <br>
     * Recupera dalla urlRequest title, pageid, timestamp e wikitext <br>
     * Estrae il wikitext in linguaggio wiki visibile <br>
     *
     * @param webUrl completo
     *
     * @return wrapper con testo completo (visibile) della pagina wiki
     */
    private WrapPage creaPage(final String webUrl, String tagTemplate) {
        String rispostaAPI = web.legge(webUrl).getText();
        JSONObject jsonPageZero = getObjectPage(rispostaAPI);
        return creaPage(webUrl, jsonPageZero, tagTemplate);
    }

//    /**
//     * Legge (come user) una pagina dal server wiki <br>
//     * Usa una API con action=parse SENZA bisogno di loggarsi <br>
//     * Recupera dalla urlRequest title, pageid e wikitext <br>
//     * Estrae il wikitext in linguaggio wiki visibile <br>
//     * Elaborazione della urlRequest leggermente meno complessa di leggeQuery <br>
//     * Tempo di download leggermente più lungo di leggeQuery <br>
//     *
//     * @param wikiTitle della pagina wiki
//     *
//     * @return testo completo (visibile) della pagina wiki
//     */
//    public String leggeParseText(final String wikiTitle) {
//        Map mappa = leggeMappaParse(wikiTitle);
//        return (String) mappa.get(KEY_MAPPA_TEXT);
//    }

//    /**
//     * Recupera i parametri fondamentali di una singola pagina con action=parse <br>
//     * 3 parametri:
//     * title
//     * pageid
//     * wikitext
//     *
//     * @param wikiTitleGrezzo della pagina wiki
//     *
//     * @return mappa dei parametri
//     */
//    public Map<String, Object> leggeMappaParse(final String wikiTitleGrezzo) {
//        Map<String, Object> mappa = new HashMap<>();
//        String webUrl = webUrlParse(wikiTitleGrezzo);
//        String rispostaAPI = web.legge(webUrl).getText();
//        JSONObject jsonRisposta = (JSONObject) JSONValue.parse(rispostaAPI);
//        JSONObject jsonParse = (JSONObject) jsonRisposta.get(KEY_MAPPA_PARSE);
//
//        mappa.put(KEY_MAPPA_DOMAIN, webUrl);
//        mappa.put(KEY_MAPPA_TITLE, jsonParse.get(KEY_MAPPA_TITLE));
//        mappa.put(KEY_MAPPA_PAGEID, jsonParse.get(KEY_MAPPA_PAGEID));
//        mappa.put(KEY_MAPPA_TEXT, jsonParse.get(KEY_MAPPA_TEXT));
//
//        return mappa;
//    }

    /**
     * Recupera il contenuto testuale dal testo JSON di una singola pagina <br>
     * 21 parametri
     * 10 generali
     * 8 revisions
     * 3 slots/main
     *
     * @param paginaTextJSON in ingresso
     *
     * @return testo della pagina wiki
     */
    public String getContent(JSONObject paginaTextJSON) {
        String textContent = VUOTA;
        JSONArray arrayRevisions;
        JSONObject objectRevisions = null;
        JSONObject objectSlots;
        JSONObject objectMain = null;

        if (paginaTextJSON == null) {
            return null;
        }

        //--parametri revisions
        if (paginaTextJSON.get(REVISIONS) != null && paginaTextJSON.get(REVISIONS) instanceof JSONArray) {
            arrayRevisions = (JSONArray) paginaTextJSON.get(REVISIONS);
            if (arrayRevisions != null && arrayRevisions.size() > 0 && arrayRevisions.get(0) instanceof JSONObject) {
                objectRevisions = (JSONObject) arrayRevisions.get(0);
            }
        }

        //--parametri slots/main -> content
        if (objectRevisions != null && objectRevisions.get(SLOTS) != null && objectRevisions.get(SLOTS) instanceof JSONObject) {
            objectSlots = (JSONObject) objectRevisions.get(SLOTS);
            if (objectSlots.get(MAIN) != null && objectSlots.get(MAIN) instanceof JSONObject) {
                objectMain = (JSONObject) objectSlots.get(MAIN);
            }
        }

        if (objectMain != null && objectMain.get(CONTENT) != null) {
            textContent = (String) objectMain.get(CONTENT);
        }

        return textContent;
    }

    /**
     * Recupera un lista di WrapCat dal testo JSON di risposta ad una query <br>
     *
     * @param jsonPagine in ingresso
     *
     * @return array di WrapCat
     */
    private List<WrapCat> getListaWrapCategoria(JSONArray jsonPagine) {
        List<WrapCat> lista = new ArrayList<>();
        WrapCat wrap;
        long pageid;
        String title;

        if (jsonPagine != null && jsonPagine.size() > 0) {
            for (Object obj : jsonPagine) {
                pageid = (long) ((JSONObject) obj).get(PAGE_ID);
                title = (String) ((JSONObject) obj).get(TITLE);
                wrap = new WrapCat(pageid, title);
                lista.add(wrap);
            }
        }

        return lista;
    }

}
