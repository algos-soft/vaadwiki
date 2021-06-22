package it.algos.vaadflow14.wiki;

import com.vaadin.flow.component.*;
import it.algos.vaadflow14.backend.application.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.backend.wrapper.*;
import org.json.simple.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

import java.net.*;
import java.sql.*;
import java.util.Date;
import java.util.*;
import java.util.regex.*;


/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: gio, 07-mag-2020
 * Time: 07:49
 * <p>
 * Importazione di pagine da Wikipedia con API che NON richiedono login come Bot <br>
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti SpringBoot non la costruisce <br>
 * L'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(AAnnotationService.class); <br>
 * 3) @Autowired private AWikiService wiki; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AWikiApiService extends AAbstractService {


    public static final String PAGINA_ISO_1 = "ISO 3166-1";

    public static final String PAGINA_ISO_2 = "ISO 3166-2:IT";

    public static final String PAGINA_PROVINCE = "Regioni_d'Italia";

    public static final String PAGINA_ISO_1_NUMERICO = "ISO 3166-1 numerico";

    public static final String PAGES = "pages";

    public static final String QUERY = "query";

    public static final String REVISIONS = "revisions";

    public static final String SLOTS = "slots";

    public static final String MAIN = "main";

    public static final String CONTENT = "content";

    public static final String WIKI_QUERY = "https://it.wikipedia.org/w/api.php?&format=json&formatversion=2&action=query&rvslots=main&prop=info|revisions&rvprop=content|ids|flags|timestamp|user|userid|comment|size&titles=";

    public static final String WIKI_PARSE = "https://it.wikipedia.org/w/api.php?action=parse&prop=wikitext&formatversion=2&format=json&page=";

    public static final String WIKI_QUERY_TITLES = "https://it.wikipedia.org/w/api.php?&format=json&formatversion=2&action=query&rvslots=main&prop=revisions&rvprop=content|ids|timestamp&titles=";

    public static final String WIKI_QUERY_PAGEIDS = "https://it.wikipedia.org/w/api.php?&format=json&formatversion=2&action=query&rvslots=main&prop=revisions&rvprop=content|ids|timestamp&pageids=";

    /**
     * Converte il valore stringa nel tipo previsto dal parametro PagePar
     *
     * @param key     del parametro PagePar in ingresso
     * @param valueIn in ingresso
     *
     * @return valore della classe corretta
     */
    private Object fixValueMap(String key, Object valueIn) {
        return fixValueMap(PagePar.getPar(key), valueIn);
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
     * Legge il testo di un template da una voce wiki <br>
     * Esamina il PRIMO template che trova <br>
     * Gli estremi sono COMPRESI <br>
     * <p>
     * Recupera il tag iniziale con o senza ''Template''
     * Recupera il tag iniziale con o senza primo carattere maiuscolo
     * Recupera il tag finale di chiusura con o senza ritorno a capo precedente
     * Controlla che non esistano doppie graffe dispari all'interno del template
     *
     * @param wikiTitle della pagina wiki
     *
     * @return template completo di doppie graffe iniziali e finali
     */
    public String leggeTmpl(final String wikiTitle, final String tag) {
        return estraeTmpl(leggeQueryTxt(wikiTitle), tag);
    }

    /**
     * Estrae il testo di un template dal testo completo di una voce wiki <br>
     * Esamina il PRIMO template che trova <br>
     * Gli estremi sono COMPRESI <br>
     * <p>
     * Recupera il tag iniziale con o senza ''Template''
     * Recupera il tag iniziale con o senza primo carattere maiuscolo
     * Recupera il tag finale di chiusura con o senza ritorno a capo precedente
     * Controlla che non esistano doppie graffe dispari all'interno del template
     *
     * @return template completo di doppie graffe iniziali e finali
     */
    public String estraeTmpl(final String testoPagina, String tag) {
        String templateTxt = VUOTA;
        boolean continua = false;
        String patternTxt = "";
        Pattern patttern = null;
        Matcher matcher = null;
        int posIni;
        int posEnd;
        String tagIniTemplate = VUOTA;

        // controllo di congruità
        if (text.isValid(testoPagina) && text.isValid(tag)) {
            // patch per nome template minuscolo o maiuscolo
            // deve terminare con 'aCapo' oppure 'return' oppure 'tab' oppure '|'(pipe) oppure 'spazio'(u0020)
            if (tag.equals("Bio")) {
                tag = "[Bb]io[\n\r\t\\|\u0020(grafia)]";
            }

            // Create a Pattern text
            patternTxt = "\\{\\{(Template:)?" + tag;

            // Create a Pattern object
            patttern = Pattern.compile(patternTxt);

            // Now create matcher object.
            matcher = patttern.matcher(testoPagina);
            if (matcher.find() && matcher.groupCount() > 0) {
                tagIniTemplate = matcher.group(0);
            }

            // controlla se esiste una doppia graffa di chiusura
            // non si sa mai
            if (!tagIniTemplate.equals("")) {
                posIni = testoPagina.indexOf(tagIniTemplate);
                posEnd = testoPagina.indexOf(DOPPIE_GRAFFE_END, posIni);
                templateTxt = testoPagina.substring(posIni);
                if (posEnd != -1) {
                    continua = true;
                }
            }

            // cerco la prima doppia graffa che abbia all'interno
            // lo stesso numero di aperture e chiusure
            // spazzola il testo fino a pareggiare le graffe
            if (continua) {
                templateTxt = chiudeTmpl(templateTxt);
            }
        }

        return templateTxt;
    }

    /**
     * Chiude il template <br>
     * <p>
     * Il testo inizia col template, ma prosegue (forse) anche oltre <br>
     * Cerco la prima doppia graffa che abbia all'interno lo stesso numero di aperture e chiusure <br>
     * Spazzola il testo fino a pareggiare le graffe <br>
     * Se non riesce a pareggiare le graffe, ritorna una stringa nulla <br>
     *
     * @param templateTxt da spazzolare
     *
     * @return template completo
     */
    public String chiudeTmpl(String templateTxt) {
        String templateOut;
        int posIni = 0;
        int posEnd = 0;
        boolean pari = false;

        templateOut = templateTxt.substring(posIni, posEnd + DOPPIE_GRAFFE_END.length()).trim();

        while (!pari) {
            posEnd = templateTxt.indexOf(DOPPIE_GRAFFE_END, posEnd + DOPPIE_GRAFFE_END.length());
            if (posEnd != -1) {
                templateOut = templateTxt.substring(posIni, posEnd + DOPPIE_GRAFFE_END.length()).trim();
                pari = html.isPariTag(templateOut, DOPPIE_GRAFFE_INI, DOPPIE_GRAFFE_END);
            }
            else {
                break;
            }
        }

        if (!pari) {
            templateOut = VUOTA;
        }

        return templateOut;
    }

    /**
     * Restituisce una colonna estratta da una wiki table <br>
     *
     * @param wikiTitle    della pagina wiki
     * @param posTabella   della wikitable nella pagina se ce ne sono più di una
     * @param rigaIniziale da cui estrarre le righe, scartando la testa della table
     * @param numColonna   da cui estrarre la cella
     *
     * @return lista di coppia di valori: sigla e nome
     */
    public List<String> getColonna(String wikiTitle, int posTabella, int rigaIniziale, int numColonna) {
        List<String> colonna = null;
        List<List<String>> lista = null;
        String cella = VUOTA;
        String[] parti = null;

        if (text.isEmpty(wikiTitle)) {
            return null;
        }

        try {
            lista = getTable(wikiTitle, posTabella);
        } catch (Exception unErrore) {
        }

        if (array.isAllValid(lista)) {
            colonna = new ArrayList<>();
            for (List<String> riga : lista) {
                if (riga.size() == 1 || (riga.size() == 2 && !riga.get(0).startsWith(GRAFFA_END))) {
                    parti = riga.get(0).split(DOPPIO_PIPE_REGEX);
                    if (parti != null && parti.length >= numColonna) {
                        cella = parti[numColonna - 1];
                    }
                }
                else {
                    if (!riga.get(0).startsWith(ESCLAMATIVO)) {
                        cella = riga.get(numColonna - 1);
                    }
                }
                if (text.isValid(cella)) {
                    cella = cella.trim();
                    cella = text.setNoDoppieGraffe(cella);
                    cella = fixCode(cella);
                    colonna.add(cella);
                }
            }
        }

        return colonna;
    }

    /**
     * Restituisce due colonne sincronizzate da una wiki table <br>
     * Esclude il testo di eventuali note <br>
     *
     * @param wikiTitle     della pagina wiki
     * @param posTabella    della wikitable nella pagina se ce ne sono più di una
     * @param rigaIniziale  da cui estrarre le righe, scartando la testa della table
     * @param numColonnaUno da cui estrarre la cella
     * @param numColonnaDue da cui estrarre la cella
     *
     * @return lista di coppia di valori: sigla e nome
     */
    @Deprecated
    public List<WrapDueStringhe> getDueColonne(String wikiTitle, int posTabella, int rigaIniziale, int numColonnaUno, int numColonnaDue) {
        List<WrapDueStringhe> listaWrap = null;
        WrapDueStringhe wrap;
        List<String> colonna = null;
        List<List<String>> lista = null;
        String cella = VUOTA;
        String[] parti = null;
        String prima;
        String seconda;

        if (text.isEmpty(wikiTitle)) {
            return null;
        }

        try {
            lista = getTable(wikiTitle, posTabella);
        } catch (Exception unErrore) {
        }

        if (array.isAllValid(lista)) {
            listaWrap = new ArrayList<>();
            for (List<String> riga : lista) {
                wrap = null;
                prima = VUOTA;
                seconda = VUOTA;
                if (riga.size() < 2 || (riga.size() == 2 && riga.get(1).startsWith(GRAFFA_END))) {
                    cella = riga.get(0);
                    parti = cella.split(DOPPIO_PIPE_REGEX);
                    if (parti != null && parti.length >= numColonnaDue) {
                        prima = parti[numColonnaUno - 1];
                        seconda = parti[numColonnaDue - 1];
                    }

                }
                else {
                    if (!riga.get(0).startsWith(ESCLAMATIVO)) {
                        prima = riga.get(numColonnaUno - 1);
                        seconda = riga.get(numColonnaDue - 1);
                    }
                }
                if (text.isValid(prima) && text.isValid(seconda)) {
                    prima = text.levaCodaDa(prima, REF);
                    seconda = text.levaCodaDa(seconda, REF);
                    prima = text.setNoDoppieGraffe(prima);
                    seconda = text.setNoDoppieGraffe(seconda);
                    prima = text.setNoQuadre(prima);
                    seconda = text.setNoQuadre(seconda);
                    prima = fixCode(prima);
                    seconda = fixCode(seconda);
                    prima = html.setNoHtmlTag(prima, "kbd");
                    if (prima.contains(PIPE)) {
                        if (prima.contains(DOPPIE_GRAFFE_INI) && prima.contains(DOPPIE_GRAFFE_END)) {
                        }
                        else {
                            prima = text.levaTestoPrimaDi(prima, PIPE);
                        }
                    }
                    if (seconda.contains(DOPPIE_QUADRE_INI) && seconda.contains(DOPPIE_QUADRE_END)) {
                        seconda = text.estrae(seconda, DOPPIE_QUADRE_INI, DOPPIE_QUADRE_END);
                    }
                    if (seconda.contains(PIPE)) {
                        if (seconda.contains(DOPPIE_GRAFFE_INI) && seconda.contains(DOPPIE_GRAFFE_END)) {
                        }
                        else {
                            seconda = text.levaTestoPrimaDi(seconda, PIPE);
                        }
                    }
                    wrap = new WrapDueStringhe(prima, seconda);
                }
                if (wrap != null) {
                    listaWrap.add(wrap);
                }
            }
        }

        return listaWrap;
    }

    /**
     * Estrae una wikitable da una pagina wiki <br>
     * Restituisce una lista dei valori per ogni riga, esclusa la prima coi titoli <br>
     *
     * @param wikiTitle della pagina wiki
     *
     * @return lista di valori per ogni riga significativa della wikitable
     */
    public List<List<String>> getTable(String wikiTitle) {
        return getTable(wikiTitle, 1);
    }

    /**
     * Estrae una wikitable da una pagina wiki <br>
     * Restituisce una lista di valori per ogni riga valida <br>
     * Restituisce anche la prima lista di titoli <br>
     * Esclude il testo che precede il primo punto ESCLAMATIVO, da scartare <br>
     * Poi estrae i titoli e poi esegue lo spilt per separare le righe valide <br>
     *
     * @param wikiTitle  della pagina wiki
     * @param posTabella della wikitable nella pagina se ce ne sono più di una
     *
     * @return lista di valori per ogni riga significativa della wikitable
     */
    public List<List<String>> getTable(String wikiTitle, int posTabella) {
        List<List<String>> listaTable = new ArrayList<>();
        List<String> listaRiga;
        String[] righeTable = null;
        String testoRigaSingola;
        String[] partiRiga = null;
        String tagTable = "\\|-";
        String testoTable = VUOTA;

        testoTable = leggeTable(wikiTitle, posTabella);

        //--elimina la testa di apertura della table per evitare fuffa
        if (text.isValid(testoTable)) {
            testoTable = text.levaTestoPrimaDi(testoTable, ESCLAMATIVO);
            testoTable = testoTable.trim();
        }

        //--elimina la coda di chiusura della table per evitare che la suddivisione in righe contenga anche la chiusura della table
        if (text.isValid(testoTable)) {
            if (testoTable.endsWith(GRAFFA_END)) {
                testoTable = text.levaCodaDa(testoTable, GRAFFA_END);
            }
            testoTable = testoTable.trim();
            if (testoTable.endsWith(PIPE)) {
                testoTable = text.levaCodaDa(testoTable, PIPE);
            }
            testoTable = testoTable.trim();
        }

        //--dopo aver eliminato la testa della tabella, la coda della tabella ed i titoli, individua le righe valide
        righeTable = testoTable.split(tagTable);

        if (righeTable != null && righeTable.length > 2) {
            for (int k = 0; k < righeTable.length; k++) {
                testoRigaSingola = righeTable[k].trim();
                if (testoRigaSingola.startsWith(ESCLAMATIVO)) {
                    continue;
                }
                partiRiga = getParti(testoRigaSingola);
                if (partiRiga != null && partiRiga.length > 0) {
                    listaRiga = new ArrayList<>();
                    for (String value : partiRiga) {
                        if (text.isValid(value)) {
                            value = value.trim();
                            if (value.startsWith(PIPE)) {
                                value = text.levaTesta(value, PIPE);
                            }
                            value = value.trim();
                            listaRiga.add(value);
                        }
                    }
                    if (!listaRiga.get(0).equals(ESCLAMATIVO)) {
                        listaTable.add(listaRiga);
                    }
                }
            }
        }

        return listaTable;
    }

    private String[] getParti(String testoRigaSingola) {
        String[] partiRiga = null;
        String tagUno = A_CAPO;
        String tagDue = DOPPIO_PIPE_REGEX;
        String tagTre = DOPPIO_ESCLAMATIVO;

        //--primo tentativo
        partiRiga = testoRigaSingola.split(tagUno);

        //--secondo tentativo
        if (partiRiga != null && partiRiga.length == 1) {
            partiRiga = testoRigaSingola.split(tagDue);
        }

        //--terzo tentativo
        if (partiRiga != null && partiRiga.length == 1) {
            partiRiga = testoRigaSingola.split(tagTre);
        }

        return partiRiga;
    }

    /**
     * Legge una wikitable da una pagina wiki <br>
     *
     * @param wikiTitle della pagina wiki
     *
     * @return testo completo della wikitable
     */
    public String leggeTable(final String wikiTitle) {
        try {
            return leggeTable(wikiTitle, 1);
        } catch (Exception unErrore) {
        }
        return VUOTA;
    }

    /**
     * Legge una wikitable da una pagina wiki, selezionandola se ce n'è più di una <br>
     *
     * @param wikiTitle della pagina wiki
     * @param pos       della wikitable nella pagina se ce ne sono più di una
     *
     * @return testo completo della wikitable alla posizione indicata
     */
    public String leggeTable(String wikiTitle, int pos) {
        String testoTable = VUOTA;
        String tag1 = "{|class=\"wikitable";
        String tag2 = "{| class=\"wikitable";
        String tag3 = "{|  class=\"wikitable";
        String tag4 = "{|class=\"sortable wikitable";
        String tag5 = "{| class=\"sortable wikitable";
        String tag6 = "{|  class=\"sortable wikitable";
        String tagEnd = "|}\n";
        int posIni = 0;
        int posEnd = 0;
        String testoPagina = leggeQueryTxt(wikiTitle);

        if (text.isValid(testoPagina)) {
            if (testoPagina.contains(tag1) || testoPagina.contains(tag2) || testoPagina.contains(tag3) || testoPagina.contains(tag4) || testoPagina.contains(tag5) || testoPagina.contains(tag6)) {
                if (testoPagina.contains(tag1)) {
                    for (int k = 1; k <= pos; k++) {
                        posIni = testoPagina.indexOf(tag1, posIni + tag1.length());
                    }
                }
                if (testoPagina.contains(tag2)) {
                    for (int k = 1; k <= pos; k++) {
                        posIni = testoPagina.indexOf(tag2, posIni + tag2.length());
                    }
                }
                if (testoPagina.contains(tag3)) {
                    for (int k = 1; k <= pos; k++) {
                        posIni = testoPagina.indexOf(tag3, posIni + tag3.length());
                    }
                }
                if (testoPagina.contains(tag4)) {
                    for (int k = 1; k <= pos; k++) {
                        posIni = testoPagina.indexOf(tag4, posIni + tag4.length());
                    }
                }
                posEnd = testoPagina.indexOf(tagEnd, posIni) + tagEnd.length();
                testoTable = testoPagina.substring(posIni, posEnd);
            }
            else {
                //                throw new Exception("La pagina wiki " + wikiTitle + " non contiene nessuna wikitable. AWikiService.leggeTable()");
                //                logger.warn("La pagina wiki " + wikiTitle + " non contiene nessuna wikitable", this.getClass(), "leggeTable");
            }
        }

        return text.isValid(testoTable) ? testoTable.trim() : VUOTA;
    }

    /**
     * Legge un modulo di una pagina wiki <br>
     *
     * @param wikiTitle della pagina wiki
     *
     * @return testo completo del modulo, comprensivo delle graffe iniziale e finale
     */
    public String leggeModulo(final String wikiTitle) {
        String testoModulo = VUOTA;
        String testoPagina = leggeQueryTxt(wikiTitle);
        String tag = "return";

        if (text.isValid(testoPagina)) {
            if (testoPagina.contains(tag)) {
                testoModulo = text.levaTestoPrimaDi(testoPagina, tag);
            }
        }

        return text.isValid(testoModulo) ? testoModulo.trim() : VUOTA;
    }

    /**
     * Legge la mappa dei valori di modulo di una pagina wiki <br>
     *
     * @param wikiTitle della pagina wiki
     *
     * @return mappa chiave=valore del modulo
     */
    public Map<String, String> leggeMappaModulo(final String wikiTitle) {
        Map<String, String> mappa = null;
        String tagRighe = VIRGOLA_CAPO;
        String tagSezioni = UGUALE_SEMPLICE;
        String[] righe = null;
        String[] sezioni = null;
        String key;
        String value;
        String testoModulo = leggeModulo(wikiTitle);

        if (text.isValid(testoModulo)) {
            testoModulo = text.setNoGraffe(testoModulo);
            righe = testoModulo.split(tagRighe);
        }

        if (righe != null && righe.length > 0) {
            mappa = new LinkedHashMap<>();
            for (String riga : righe) {

                sezioni = riga.split(tagSezioni);
                if (sezioni != null && sezioni.length == 2) {
                    key = sezioni[0];

                    key = text.setNoQuadre(key);
                    key = text.setNoDoppiApici(key);
                    value = sezioni[1];
                    value = text.setNoDoppiApici(value);
                    value = text.setNoGraffe(value);
                    mappa.put(key, value);
                }
            }
        }

        return mappa;
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
        JSONObject object = getObjectPage(rispostaDellaQuery);

        if (object != null) {
            testoPagina = this.getContent(object);
        }

        return testoPagina;
    }


    /**
     * Recupera i parametri fondamentali di una singola pagina con action=parse <br>
     * 3 parametri:
     * title
     * pageid
     * wikitext
     *
     * @param wikiTitleGrezzo della pagina wiki
     *
     * @return mappa dei parametri
     */
    public Map<String, Object> leggeMappaParse(final String wikiTitleGrezzo) {
        Map<String, Object> mappa = new HashMap<>();
        String webUrl = webUrlParse(wikiTitleGrezzo);
        String rispostaAPI = web.legge(webUrl).getText();
        JSONObject jsonRisposta = (JSONObject) JSONValue.parse(rispostaAPI);
        JSONObject jsonParse = (JSONObject) jsonRisposta.get(KEY_MAPPA_PARSE);

        mappa.put(KEY_MAPPA_DOMAIN, webUrl);
        mappa.put(KEY_MAPPA_TITLE, jsonParse.get(KEY_MAPPA_TITLE));
        mappa.put(KEY_MAPPA_PAGEID, jsonParse.get(KEY_MAPPA_PAGEID));
        mappa.put(KEY_MAPPA_TEXT, jsonParse.get(KEY_MAPPA_TEXT));

        return mappa;
    }

    /**
     * Legge (come user) una pagina dal server wiki <br>
     * Usa una API con action=parse SENZA bisogno di loggarsi <br>
     * Recupera dalla urlRequest title, pageid e wikitext <br>
     * Estrae il wikitext in linguaggio wiki visibile <br>
     * Elaborazione della urlRequest leggermente meno complessa di leggeQuery <br>
     * Tempo di download leggermente più lungo di leggeQuery <br>
     *
     * @param wikiTitle della pagina wiki
     *
     * @return testo completo (visibile) della pagina wiki
     */
    public String leggeParseText(final String wikiTitle) {
        Map mappa = leggeMappaParse(wikiTitle);
        return (String) mappa.get(KEY_MAPPA_TEXT);
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

        JSONArray jsonPages = getArrayPagine(rispostaAPI);
        if (jsonPages != null) {
            wraps = new ArrayList<>();
            for (Object obj : jsonPages) {
                wraps.add(creaPage(webUrl, (JSONObject) obj, tagTemplate));
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


    private WrapPage creaPage(final String webUrl, final JSONObject jsonPage, String tagTemplate) {
        long pageid;
        String title;
        String stringTimestamp;
        String content;

        pageid = (long) jsonPage.get(KEY_JSON_PAGE_ID);
        title = (String) jsonPage.get(KEY_JSON_TITLE);
        JSONArray jsonRevisions = (JSONArray) jsonPage.get(KEY_JSON_REVISIONS);
        JSONObject jsonRevZero = (JSONObject) jsonRevisions.get(0);
        stringTimestamp = (String) jsonRevZero.get(KEY_JSON_TIMESTAMP);
        JSONObject jsonSlots = (JSONObject) jsonRevZero.get(KEY_JSON_SLOTS);
        JSONObject jsonMain = (JSONObject) jsonSlots.get(KEY_JSON_MAIN);
        content = (String) jsonMain.get(KEY_JSON_CONTENT);

        if (text.isValid(tagTemplate)) {
            content = estraeTmpl(content, tagTemplate);
        }

        return new WrapPage(webUrl, pageid, title, content, stringTimestamp, text.isValid(tagTemplate));
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

    /**
     * Converte i tipi di una mappa secondo i parametri PagePar
     *
     * @param mappaIn standard (valori String) in ingresso
     *
     * @return mappa tipizzata secondo PagePar
     */
    public HashMap<String, Object> converteMappa(HashMap mappaIn) {
        HashMap<String, Object> mappaOut = new HashMap<String, Object>();
        String key = "";
        String valueTxt;
        Object valueObj = null;

        if (mappaIn != null) {
            for (Object obj : mappaIn.keySet()) {
                if (obj instanceof String) {
                    key = (String) obj;
                    valueObj = mappaIn.get(key);
                    valueObj = fixValueMap(key, valueObj);
                    mappaOut.put(key, valueObj);
                }
            }
        }

        return mappaOut;
    }

    /**
     * Converte il valore stringa nel tipo previsto dal parametro PagePar
     *
     * @param par     parametro PagePar in ingresso
     * @param valueIn in ingresso
     *
     * @return valore della classe corretta
     */
    private Object fixValueMap(PagePar par, Object valueIn) {
        Object valueOut = null;
        PagePar.TypeField typo = par.getType();

        if (typo == PagePar.TypeField.string) {
            valueOut = valueIn;
        }

        if (typo == PagePar.TypeField.booleano) {
            valueOut = valueIn;
        }

        if (typo == PagePar.TypeField.longzero || typo == PagePar.TypeField.longnotzero) {
            if (valueIn instanceof String) {
                try {
                    valueOut = Long.decode((String) valueIn);
                } catch (Exception unErrore) { // intercetta l'errore
                }
            }
            if (valueIn instanceof Integer) {
                try {
                    valueOut = new Long(valueIn.toString());
                } catch (Exception unErrore) { // intercetta l'errore
                }
            }
            if (valueIn instanceof Long) {
                valueOut = valueIn;
            }
        }

        if (typo == PagePar.TypeField.date) {
            if (valueIn instanceof String) {
                try {
                    valueOut = date.convertTxtData((String) valueIn);
                } catch (Exception unErrore) { // intercetta l'errore
                    valueOut = DATA_NULLA; //@todo mettere LocalDate
                }
            }
            if (valueIn instanceof Date) {
                valueOut = valueIn;
            }
        }

        if (typo == PagePar.TypeField.timestamp) {
            if (valueIn instanceof String) {
                try {
                    valueOut = date.convertTxtTime((String) valueIn);
                } catch (Exception unErrore) { // intercetta l'errore
                    valueOut = DATA_NULLA;
                }
            }
            if (valueIn instanceof Timestamp) {
                valueOut = valueIn;
            }
        }

        return valueOut;
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

    public WikiPage getWikiPageFromMappa(Map<String, Object> mappa) {
        WikiPage wiki = new WikiPage();
        fixMappaWiki(wiki, mappa);

        return wiki;
    }

    public WikiPage getWikiPageFromTitle(String wikiTitle) {
        String rispostaDellaQuery = leggeJsonTxt(wikiTitle);
        JSONObject objectPage = getObjectPage(rispostaDellaQuery);
        Map<String, Object> mappa = getMappaJSON(objectPage);

        return getWikiPageFromMappa(mappa);
    }

    public String getContent(String wikiTitle) {
        String textContent = VUOTA;
        WikiPage wikiPage = getWikiPageFromTitle(wikiTitle);

        if (wikiPage != null) {
            textContent = wikiPage.getContent();
        }

        return textContent;
    }

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


    /**
     * Sorgente completo di una pagina wiki <br>
     * Testo 'grezzo' html <br>
     * Invoca il corrispondente metodo di AWebService <br>
     * Non usa le API di Mediawiki <br>
     *
     * @param wikiTitle della pagina wiki
     *
     * @return testo sorgente completo della pagina web in formato html
     */
    public String leggeHtml(final String wikiTitle) {
        return web.leggeWiki(wikiTitle).getText();
    }


    /**
     * Legge una porzione di testo incolonnato dalla pagina wikipedia <br>
     *
     * @param wikiTitle della pagina wiki
     *
     * @return testo contenuto nelle colonne
     */
    public String leggeColonne(String wikiTitle) {
        String testoIncolonnato = VUOTA;
        String tagIni = "{{Colonne}}";
        String tagEnd = "{{Colonne fine}}";
        int posIni = 0;
        int posEnd = 0;
        String testoPagina = leggeQueryTxt(wikiTitle);

        if (text.isValid(testoPagina)) {
            if (testoPagina.contains(tagIni)) {
                posIni = testoPagina.indexOf(tagIni);
                posEnd = testoPagina.indexOf(tagEnd, posIni);
                testoIncolonnato = testoPagina.substring(posIni, posEnd);
            }
        }

        return testoIncolonnato;
    }


    /**
     * Import da una pagina di wikipedia <br>
     *
     * @return lista di wrapper con due stringhe ognuno
     */
    @Deprecated
    public List<WrapDueStringhe> estraeListaDue(String pagina, String titoli, int posUno, int posDue) {
        List<WrapDueStringhe> listaWrap = null;
        List<List<String>> matriceTable = null;
        String[] titoliTable = text.getMatrice(titoli);
        WrapDueStringhe wrapGrezzo;

        matriceTable = web.getMatriceTableWiki(pagina, titoliTable);
        if (matriceTable != null && matriceTable.size() > 0) {
            listaWrap = new ArrayList<>();
            for (List<String> riga : matriceTable) {
                wrapGrezzo = new WrapDueStringhe(riga.get(posUno - 1), posDue > 0 ? riga.get(posDue - 1) : VUOTA);
                listaWrap.add(wrapGrezzo);
            }
        }
        return listaWrap;
    }


    /**
     * Import da una pagina di wikipedia <br>
     *
     * @return lista di wrapper con tre stringhe ognuno
     */
    @Deprecated
    public List<WrapTreStringhe> estraeListaTre(String pagina, String titoli) {
        List<WrapTreStringhe> listaWrap = null;
        LinkedHashMap<String, LinkedHashMap<String, String>> mappaGenerale = null;
        LinkedHashMap<String, String> mappa;
        String[] titoliTable = text.getMatrice(titoli);
        String tagUno = titoliTable[0];
        String tagDue = titoliTable[1];
        String tagTre = titoliTable[2];
        WrapTreStringhe wrapGrezzo;

        mappaGenerale = web.getMappaTableWiki(pagina, titoliTable);
        if (mappaGenerale != null && mappaGenerale.size() > 0) {
            listaWrap = new ArrayList<>();
            for (String elemento : mappaGenerale.keySet()) {
                mappa = mappaGenerale.get(elemento);
                wrapGrezzo = new WrapTreStringhe(mappa.get(tagUno), mappa.get(tagDue), mappa.get(tagTre));
                listaWrap.add(wrapGrezzo);
            }
        }
        return listaWrap;
    }


    public String fixCode(String testoGrezzo) {
        String testoValido = VUOTA;
        String tagIni = "<code>";
        String tagEnd = "</code>";

        if (text.isEmpty(testoGrezzo)) {
            return VUOTA;
        }

        if (!testoGrezzo.contains(tagIni) || !testoGrezzo.contains(tagEnd)) {
            return testoGrezzo;
        }

        testoValido = testoGrezzo.trim();
        if (testoValido.startsWith(tagIni)) {
            testoValido = text.levaTesta(testoValido, tagIni);
            testoValido = text.levaCoda(testoValido, tagEnd);
        }
        else {
            testoValido = text.estrae(testoValido, tagIni, tagEnd);
        }

        return testoValido.trim();
    }


    public String estraeGraffaCon(String testoCompleto) {
        String testoValido = VUOTA;
        int posIni;
        int posEnd;

        if (testoCompleto.contains(DOPPIE_GRAFFE_INI) && testoCompleto.contains(DOPPIE_GRAFFE_END)) {
            posIni = testoCompleto.indexOf(GRAFFA_INI);
            posEnd = testoCompleto.indexOf(DOPPIE_GRAFFE_END) + DOPPIE_GRAFFE_END.length();
            if (posIni >= 0 && posEnd > posIni) {
                testoValido = testoCompleto.substring(posIni, posEnd);
            }
        }

        return testoValido;
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

    public String estraeGraffa(String testoCompleto) {
        return text.setNoDoppieGraffe(estraeGraffaCon(testoCompleto));
    }

    public void openWikiPage(String wikiTitle) {
        String link = "\"" + FlowCost.PATH_WIKI + wikiTitle + "\"";
        UI.getCurrent().getPage().executeJavaScript("window.open(" + link + ");");
    }

}