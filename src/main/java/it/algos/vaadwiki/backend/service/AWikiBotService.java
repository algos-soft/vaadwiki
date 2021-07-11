package it.algos.vaadwiki.backend.service;

import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.wiki.*;
import static it.algos.vaadflow14.wiki.AWikiApiService.*;
import static it.algos.vaadwiki.backend.application.WikiCost.*;
import it.algos.vaadwiki.backend.packages.bio.*;
import it.algos.vaadwiki.wiki.*;
import org.json.simple.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

import java.time.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 10-mag-2021
 * Time: 14:06
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti SpringBoot non la costruisce <br>
 * Estende la classe astratta AAbstractService che mantiene i riferimenti agli altri services <br>
 * L'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(AWikiBotService.class); <br>
 * 3) @Autowired public AWikiBotService annotation; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AWikiBotService extends AAbstractService {

    public static final String TAG_BIO = "Bio";

    private static final LocalDateTime MONGO_TIME_ORIGIN = LocalDateTime.of(2000, 1, 1, 0, 0);

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AWikiApiService wikiApi;


    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public BioUtility utility;

    /**
     * Vengono usati quelli che hanno un miniWrap.pageid senza corrispondente bio.pageid nel mongoDb <br>
     */
    protected Predicate<MiniWrap> checkNuovi = wrap -> mongo.isNotEsiste(Bio.class, wrap.getPageid());

    /**
     * Vengono usati quelli che hanno un miniWrap.pageid senza corrispondente bio.pageid nel mongoDb <br>
     */
    protected Predicate<MiniWrap> checkEsistenti = wrap -> mongo.isEsiste(Bio.class, wrap.getPageid());

    /**
     * Vengono usati quelli che hanno miniWrap.lastModifica maggiore di bio.lastModifica <br>
     */
    protected Predicate<MiniWrap> checkModificati = wrap -> {
        LocalDateTime wrapTime = wrap.getLastModifica();
        String key = wrap.getPageid() + VUOTA;
        Bio bio = (Bio) mongo.findById(Bio.class, key);
        LocalDateTime mongoTime = bio != null ? bio.getLastMongo() : MONGO_TIME_ORIGIN;

        return wrapTime.isAfter(mongoTime);
    };

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
    public WrapPage leggePage(final String wikiTitleGrezzo) {
        return wikiApi.leggePage(wikiTitleGrezzo, TAG_BIO);
    }

    /**
     * Legge (come user) una serie pagina dal server wiki <br>
     * Usa una API con action=query SENZA bisogno di loggarsi <br>
     * Recupera dalla urlRequest title, pageid, timestamp e wikitext <br>
     * Estrae il wikitext in linguaggio wiki visibile <br>
     *
     * @param listaPageIdsDaLeggere sul server
     *
     * @return lista di wrapper con testo completo (visibile) della pagina wiki
     */
    public List<WrapPage> leggePages(List<Long> listaPageIdsDaLeggere) {
        return leggePages(array.toStringaPipe(listaPageIdsDaLeggere));
    }

    /**
     * Legge (come user) una serie pagina dal server wiki <br>
     * Usa una API con action=query SENZA bisogno di loggarsi <br>
     * Recupera dalla urlRequest title, pageid, timestamp e wikitext <br>
     * Estrae il wikitext in linguaggio wiki visibile <br>
     *
     * @param pageIds stringa dei pageIds delle pagine wiki da leggere
     *
     * @return lista di wrapper con testo completo (visibile) della pagina wiki
     */
    public List<WrapPage> leggePages(String pageIds) {
        long inizio = System.currentTimeMillis();

        List<WrapPage> listaWrapPage = wikiApi.leggePages(pageIds, TAG_BIO);
        if (listaWrapPage != null) {
            if (listaWrapPage.size() > 0) {
                logger.info(AETypeLog.bio, String.format("Recupera una lista di %d wrapPage valide (con tmplBio) in %s", listaWrapPage.size(), date.deltaTextEsatto(inizio)));
            }
            else {
                logger.info(AETypeLog.bio, "Non ci sono wrapPages valide (con tmplBio) da leggere");
            }
        }
        return listaWrapPage;
    }

    /**
     * Recupera (come user) 'lastModifica' di una serie di pageid <br>
     * Usa una API con action=query SENZA bisogno di loggarsi <br>
     * Recupera dalla urlRequest  pageid e timestamp <br>
     *
     * @param categoryTitle da recuperare
     *
     * @return lista di MiniWrap con 'pageid' e 'lastModifica'
     */
    public List<MiniWrap> getMiniWrap(final String categoryTitle) {
        List<Long> listaPageids = wikiApi.getLongCat(categoryTitle);
        return getMiniWrap(listaPageids);
    }

    /**
     * Recupera (come user) 'lastModifica' di una serie di pageid <br>
     * Usa una API con action=query SENZA bisogno di loggarsi <br>
     * Recupera dalla urlRequest  pageid e timestamp <br>
     *
     * @param categoryTitle da recuperare
     *
     * @return lista di MiniWrap con 'pageid' e 'lastModifica'
     */
    public List<MiniWrap> getMiniWrap(final List<Long> listaPageids) {
        List<MiniWrap> wraps = new ArrayList<>();
        long inizio = System.currentTimeMillis();
        int limit = LIMIT_USER;
        int dimLista = listaPageids.size();
        int cicli = (dimLista / limit) + 1;
        String strisciaIds = VUOTA;
        int ini = 0;
        int end = 0;

        for (int k = 0; k < cicli; k++) {
            ini = k * limit;
            end = ((k + 1) * limit);
            end = Math.min(end, dimLista);
            strisciaIds = array.toStringaPipe(listaPageids.subList(ini, end));
            wraps.addAll(fixPages(strisciaIds));
        }

        logger.info(AETypeLog.bio, String.format("Recuperata una lista di %s miniWrap con pageId e lastModifica in %s", wraps.size(), date.deltaTextEsatto(inizio)));

        return wraps;
    }

    /**
     * Recupera (come user) 'lastModifica' di una serie di pageid <br>
     * Usa una API con action=query SENZA bisogno di loggarsi <br>
     * Recupera dalla urlRequest  pageid e timestamp <br>
     * Non devono arrivare più di 50 pageid <br>
     *
     * @param pageIds stringa dei pageIds delle pagine wiki da controllare
     *
     * @return lista di MiniWrap con 'pageid' e 'lastModifica'
     */
    public List<MiniWrap> fixPages(final String pageIds) {
        List<MiniWrap> wraps = null;
        String webUrl;
        AIResult result;
        String rispostaAPI;

        if (text.isEmpty(pageIds)) {
            return null;
        }

        if (pageIds.split(PIPE_REGEX).length > LIMIT_USER) {
            return null;
        }

        webUrl = WIKI_QUERY_TIMESTAMP + pageIds;
        result = web.legge(webUrl);
        rispostaAPI = result.getText();

        JSONArray jsonPages = wikiApi.getArrayPagine(rispostaAPI);
        if (jsonPages != null) {
            wraps = new ArrayList<>();
            for (Object obj : jsonPages) {
                wraps.add(creaPage((JSONObject) obj));
            }
        }
        return wraps;
    }

    public MiniWrap creaPage(final JSONObject jsonPage) {
        long pageid;
        String stringTimestamp;

        if (jsonPage.get(KEY_JSON_MISSING) != null && (boolean) jsonPage.get(KEY_JSON_MISSING)) {
            return null;
        }

        pageid = (long) jsonPage.get(KEY_JSON_PAGE_ID);
        JSONArray jsonRevisions = (JSONArray) jsonPage.get(KEY_JSON_REVISIONS);
        JSONObject jsonRevZero = (JSONObject) jsonRevisions.get(0);
        stringTimestamp = (String) jsonRevZero.get(KEY_JSON_TIMESTAMP);

        return new MiniWrap(pageid, stringTimestamp);
    }

    /**
     * Elabora la lista di MiniWrap e costruisce una lista di pageIds da leggere <br>
     * Vengono usati quelli che hanno un miniWrap.pageid senza corrispondente bio.pageid nel mongoDb <br>
     * Vengono usati quelli che hanno miniWrap.lastModifica maggiore di bio.lastModifica <br>
     *
     * @param listaMiniWrap da elaborare
     *
     * @return lista di pageId da leggere dal server
     */
    public List<Long> elaboraMiniWrap(final List<MiniWrap> listaMiniWrap) {
        List<Long> listaPageIdsDaLeggere = new ArrayList<>();
        List<MiniWrap> listaMiniWrapTotali = new ArrayList<>();
        long inizio = System.currentTimeMillis();
        int nuove;
        int modificate;
        int totali;
        String message = VUOTA;

        //--Vengono usati quelli che hanno un miniWrap.pageid senza corrispondente bio.pageid nel mongoDb
        List<MiniWrap> listaMiniWrapNuovi = listaMiniWrap
                .stream()
                .filter(checkNuovi)
                .sorted()
                .collect(Collectors.toList());
        nuove = listaMiniWrapNuovi.size();
        listaMiniWrapTotali.addAll(listaMiniWrapNuovi);

        //--Vengono usati quelli che hanno miniWrap.lastModifica maggiore di bio.lastModifica
        List<MiniWrap> listaMiniWrapModificati = listaMiniWrap
                .stream()
                .filter(checkEsistenti)
                .filter(checkModificati)
                .sorted()
                .collect(Collectors.toList());
        modificate = listaMiniWrapModificati.size();
        listaMiniWrapTotali.addAll(listaMiniWrapModificati);
        totali = nuove + modificate;

        for (MiniWrap wrap : listaMiniWrapTotali) {
            listaPageIdsDaLeggere.add(wrap.getPageid());
        }

        message = String.format("Elaborata una lista di miniWrap da leggere: %d nuove e %d modificate (%d totali) in %s", nuove, modificate, totali, date.deltaTextEsatto(inizio));

        logger.info(AETypeLog.bio, message);

        return listaPageIdsDaLeggere;
    }

    /**
     * Legge il testo del template Bio da una voce <br>
     * Esamina solo il PRIMO template BIO che trova <br>
     * Gli estremi sono COMPRESI <br>
     * <p>
     * Recupera il tag iniziale con o senza ''Template''
     * Recupera il tag iniziale con o senza primo carattere maiuscolo
     * Recupera il tag finale di chiusura con o senza ritorno a capo precedente
     * Controlla che non esistano doppie graffe dispari all'interno del template
     *
     * @param testoCompletoPagina in ingresso
     *
     * @return template completo di doppie graffe iniziali e finali
     */
    public String estraeTmpl(final String testoCompletoPagina) {
        return wikiApi.estraeTmpl(testoCompletoPagina, "Bio");
    }

    //    /**
    //     * Legge il testo del template Bio da una voce <br>
    //     * Esamina solo il PRIMO template BIO che trova <br>
    //     * Gli estremi sono COMPRESI <br>
    //     * <p>
    //     * Recupera il tag iniziale con o senza ''Template''
    //     * Recupera il tag iniziale con o senza primo carattere maiuscolo
    //     * Recupera il tag finale di chiusura con o senza ritorno a capo precedente
    //     * Controlla che non esistano doppie graffe dispari all'interno del template
    //     *
    //     * @param wikiTitle della pagina wiki
    //     *
    //     * @return template completo di doppie graffe iniziali e finali
    //     */
    //    public String leggeTmpl(final String wikiTitle) {
    //        return wikiApi.leggeTmpl(wikiTitle, "Bio");
    //    }

    //    /**
    //     * Legge una singola pagina da wiki <br>
    //     * Non serve essere loggato come Bot <br>
    //     *
    //     * @param wikiTitle della pagina wiki
    //     *
    //     * @return pagina wiki
    //     */
    //    public Pagina leggePagina(final String wikiTitle) {
    //        Pagina pagina = null;
    //
    //        return pagina;
    //    }

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
    //    public String leggeParse(final String wikiTitle) {
    //        Map mappa = getMappaParse(wikiTitle);
    //        return (String) mappa.get(KEY_MAPPA_TEXT);
    //    }

    //    /**
    //     * Recupera i parametri fondamentali di una singola pagina con action=parse <br>
    //     * 3 parametri:
    //     * title
    //     * pageid
    //     * wikitext
    //     *
    //     * @param wikiTitle della pagina wiki
    //     *
    //     * @return mappa dei parametri
    //     */
    //    public Map<String, Object> getMappaParse(final String wikiTitle) {
    //        AIResult result;
    //        Map<String, Object> mappa = new HashMap<>();
    //        String webUrl = WIKI_PARSE + wikiTitle;
    //        String rispostaDellaParse = web.leggeWikiTxt(webUrl);
    //
    //        if (false) {
    //            result=null;
    //        }
    //
    //        JSONObject jsonRisposta = (JSONObject) JSONValue.parse(rispostaDellaParse);
    //        JSONObject jsonParse = (JSONObject) jsonRisposta.get(KEY_MAPPA_PARSE);
    //
    //        mappa.put(KEY_MAPPA_TITLE, jsonParse.get(KEY_MAPPA_TITLE));
    //        mappa.put(KEY_MAPPA_PAGEID, jsonParse.get(KEY_MAPPA_PAGEID));
    //        mappa.put(KEY_MAPPA_TEXT, jsonParse.get(KEY_MAPPA_TEXT));
    //
    //        return mappa;
    //    }

    //    /**
    //     * Costruisce un wrapper di dati essenziali per una Biografia <br>
    //     *
    //     * @param wikiTitle della pagina wiki
    //     *
    //     * @return pagina wiki
    //     */
    //    public BioWrap getBioWrap(final String wikiTitle) {
    //        BioWrap wrap = null;
    //        Map mappa = getMappaParse(wikiTitle);
    //
    //        if (array.isAllValid(mappa)) {
    //            wrap = appContext.getBean(BioWrap.class, mappa);
    //        }
    //
    //        return wrap;
    //    }

    /**
     * Estrae una mappa chiave-valore per un fix di parametri, dal tmplBioServer di una biografia <br>
     * <p>
     * E impossibile sperare in uno schema fisso <br>
     * I parametri sono spesso scritti in ordine diverso da quello previsto <br>
     * Occorre considerare le {{ graffe annidate, i | (pipe) annidati ed i mancati ritorni a capo, ecc., ecc. <br>
     * <p>
     * Uso la lista dei parametri che può riconoscere <br>
     * (è meno flessibile, ma più sicuro) <br>
     * Cerco il primo parametro nel testo e poi spazzolo il testo per cercare <br>
     * il primo parametro noto e così via <br>
     *
     * @param tmplBioServer del template Bio
     *
     * @return mappa dei parametri esistenti nella enumeration e presenti nel testo
     */
    public LinkedHashMap<String, String> getMappaDownload(String tmplBioServer) {
        LinkedHashMap<String, String> mappa = null;
        LinkedHashMap<Integer, String> mappaTmp = new LinkedHashMap<Integer, String>();
        String chiave;
        String uguale = UGUALE_SEMPLICE;
        String valore = VUOTA;
        int pos = 0;
        int posUgu;
        int posEnd;

        if (tmplBioServer != null && !tmplBioServer.equals("")) {
            mappa = new LinkedHashMap();
            for (ParBio par : ParBio.values()) {
                if (par == ParBio.titolo) {
                    continue;
                }
                valore = par.getTag();

                try { // prova ad eseguire il codice
                    pos = text.getPosFirstTag(tmplBioServer, valore);
                } catch (Exception unErrore) { // intercetta l'errore
                    int a = 87;
                }
                if (pos > 0) {
                    mappaTmp.put(pos, valore);
                }
            }

            Object[] matrice = mappaTmp.keySet().toArray();
            Arrays.sort(matrice);
            ArrayList<Object> lista = new ArrayList<Object>();
            for (Object lungo : matrice) {
                lista.add(lungo);
            }

            for (int k = 1; k <= lista.size(); k++) {
                chiave = mappaTmp.get(lista.get(k - 1));

                try { // prova ad eseguire il codice
                    if (k < lista.size()) {
                        posEnd = (Integer) lista.get(k);
                    }
                    else {
                        posEnd = tmplBioServer.length();
                    }
                    valore = tmplBioServer.substring((Integer) lista.get(k - 1), posEnd);
                } catch (Exception unErrore) { // intercetta l'errore
                    int c = 76;
                }
                if (!valore.equals(VUOTA)) {
                    valore = valore.trim();
                    posUgu = valore.indexOf(uguale);
                    if (posUgu != -1) {
                        posUgu += uguale.length();
                        valore = valore.substring(posUgu).trim();
                    }
                    valore = regValore(valore);
                    if (!text.isPariTag(valore, DOPPIE_GRAFFE_INI, DOPPIE_GRAFFE_END)) {
                        valore = regGraffe(valore);
                    }
                    valore = regACapo(valore);
                    valore = regBreakSpace(valore);
                    valore = valore.trim();
                    mappa.put(chiave, valore);
                }
            }
        }

        return mappa;
    }

    /**
     * Mappa chiave-valore con i valori 'troncati' <br>
     * Valore grezzo troncato dopo alcuni tag chiave (<ref>, {{, ecc.) e senza la 'coda' risultante <br>
     *
     * @param mappaDownload coi valori originali provenienti dalla property tmplBioServer della entity Bio
     *
     * @return mappa con i valori 'troncati'
     */
    public LinkedHashMap<String, String> getMappaTroncata(LinkedHashMap<String, String> mappaDownload) {
        LinkedHashMap<String, String> mappa = null;
        ParBio par;
        String key;
        String value;

        if (mappaDownload != null) {
            mappa = new LinkedHashMap<>();
            for (Map.Entry<String, String> entry : mappaDownload.entrySet()) {
                key = entry.getKey();
                value = entry.getValue();
                par = ParBio.getType(key);
                value = par.estraeValoreInizialeGrezzo(value);
                mappa.put(entry.getKey(), value);
            }
        }

        return mappa;
    }

    /**
     * Mappa chiave-valore con i valori 'elaborati' <br>
     * Valore elaborato valido (minuscole, quadre, ecc.) <br>
     *
     * @param mappaTroncata dopo alcuni tag chiave (<ref>, {{, ecc.) e senza la 'coda' risultante
     *
     * @return mappa con i valori 'elaborati'
     */
    public LinkedHashMap<String, String> getMappaElaborata(LinkedHashMap<String, String> mappaTroncata) {
        LinkedHashMap<String, String> mappa = null;
        ParBio par;
        String key;
        String value;

        if (mappaTroncata != null) {
            mappa = new LinkedHashMap<>();
            for (Map.Entry<String, String> entry : mappaTroncata.entrySet()) {
                key = entry.getKey();
                value = entry.getValue();
                par = ParBio.getType(key);
                value = par.regolaValoreInizialeValido(value);
                mappa.put(entry.getKey(), value);
            }
        }

        return mappa;
    }


    /**
     * Elimina il testo se inizia con un pipe <br>
     */
    public String regValore(String valoreIn) {
        String valoreOut = valoreIn;

        if (valoreIn.startsWith(PIPE)) {
            valoreOut = VUOTA;
        }

        return valoreOut.trim();
    }


    /**
     * Elimina le graffe finali
     */
    public String regGraffe(String valoreIn) {
        String valoreOut = valoreIn;

        if (valoreIn.endsWith(DOPPIE_GRAFFE_END)) {
            valoreOut = valoreOut.substring(0, valoreOut.length() - DOPPIE_GRAFFE_END.length());
        }

        return valoreOut.trim();
    }


    /**
     * Controlla il primo aCapo che trova:
     * - se è all'interno di doppie graffe, non leva niente
     * - se non ci sono dopppie graffe, leva dopo l' aCapo
     */
    public String regACapo(String valoreIn) {
        String valoreOut = valoreIn;
        String doppioACapo = A_CAPO + A_CAPO;
        String pipeACapo = A_CAPO + PIPE;
        int pos;
        HashMap mappaGraffe;

        if (!valoreIn.equals(VUOTA) && valoreIn.contains(doppioACapo)) {
            valoreOut = valoreOut.replace(doppioACapo, A_CAPO);
        }

        if (!valoreIn.equals(VUOTA) && valoreIn.contains(pipeACapo)) {
            mappaGraffe = utility.checkGraffe(valoreIn);

            if (mappaGraffe.containsKey(KEY_MAP_GRAFFE_ESISTONO)) {
            }
            else {
                pos = valoreIn.indexOf(pipeACapo);
                valoreOut = valoreIn.substring(0, pos);
            }
        }

        return valoreOut.trim();
    }


    /**
     * Elimina un valore strano trovato (ed invisibile)
     * ATTENZIONE: non è uno spazio vuoto !
     * Trattasi del carattere: C2 A0 ovvero U+00A0 ovvero NO-BREAK SPACE
     * Non viene intercettato dal comando Java TRIM()
     */
    public String regBreakSpace(String valoreIn) {
        String valoreOut = valoreIn;
        String strano = " ";   //NON cancellare: sembra uno spazio, ma è un carattere invisibile

        if (valoreIn.startsWith(strano)) {
            valoreOut = valoreIn.substring(1);
        }

        if (valoreIn.endsWith(strano)) {
            valoreOut = valoreIn.substring(0, valoreIn.length() - 1);
        }

        return valoreOut.trim();
    }

    /**
     * Regola questo campo
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return testoValido regolato in uscita
     */
    public String fixNomeValido(String testoGrezzo) {
        return fixValoreGrezzo(testoGrezzo);
    }

    /**
     * Regola questo campo
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return testoValido regolato in uscita
     */
    public String fixCognomeValido(String testoGrezzo) {
        return fixValoreGrezzo(testoGrezzo);
    }

    /**
     * Elabora un valore GREZZO e restituisce un valore VALIDO <br>
     * NON controlla la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
     * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
     *
     * @param valoreGrezzo in entrata da elaborare
     *
     * @return valore finale valido del parametro
     */
    public String fixValoreGrezzo(String valoreGrezzo) {
        String valoreValido = valoreGrezzo.trim();

        if (text.isEmpty(valoreGrezzo)) {
            return VUOTA;
        }

        valoreValido = text.setNoQuadre(valoreValido);

        return valoreValido.trim();
    }

    /**
     * Elabora un valore GREZZO e restituisce un valore VALIDO <br>
     *
     * @param valoreGrezzo in entrata da elaborare
     *
     * @return valore finale valido del parametro
     */
    public String fixMaiuscola(String valoreGrezzo) {
        String valoreValido = fixValoreGrezzo(valoreGrezzo);

        if (text.isEmpty(valoreGrezzo)) {
            return VUOTA;
        }

        valoreValido = text.primaMaiuscola(valoreValido);

        return valoreValido.trim();
    }


    /**
     * Elabora un valore GREZZO e restituisce un valore VALIDO <br>
     *
     * @param valoreGrezzo in entrata da elaborare
     *
     * @return valore finale valido del parametro
     */
    public String fixMinuscola(String valoreGrezzo) {
        String valoreValido = fixValoreGrezzo(valoreGrezzo);

        if (text.isEmpty(valoreGrezzo)) {
            return VUOTA;
        }

        valoreValido = text.primaMinuscola(valoreValido);

        return valoreValido.trim();
    }


    /**
     * Restituisce un valore grezzo troncato dopo alcuni tag chiave <br>
     * <p>
     * ELIMINA gli eventuali contenuti IN CODA che non devono essere presi in considerazione <br>
     * Restituisce un valore GREZZO che deve essere ancora elaborato <br>
     * Eventuali parti terminali inutili vengono scartate ma devono essere conservate a parte per il template <br>
     * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
     *
     * @param valorePropertyTmplBioServer testo originale proveniente dalla property tmplBioServer della entity Bio
     *
     * @return valore grezzo troncato dopo alcuni tag chiave (<ref>, {{, ecc.) <br>
     */
    public String estraeValoreInizialeGrezzoPuntoAmmesso(String valorePropertyTmplBioServer) {
        return troncaDopoTag(valorePropertyTmplBioServer, true);
    }


    /**
     * Restituisce un valore grezzo troncato dopo alcuni tag chiave <br>
     * <p>
     * ELIMINA gli eventuali contenuti IN CODA che non devono essere presi in considerazione <br>
     * Restituisce un valore GREZZO che deve essere ancora elaborato <br>
     * Eventuali parti terminali inutili vengono scartate ma devono essere conservate a parte per il template <br>
     * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
     *
     * @param valorePropertyTmplBioServer testo originale proveniente dalla property tmplBioServer della entity Bio
     *
     * @return valore grezzo troncato dopo alcuni tag chiave (<ref>, {{, ecc.) <br>
     */
    public String estraeValoreInizialeGrezzoPuntoEscluso(String valorePropertyTmplBioServer) {
        return troncaDopoTag(valorePropertyTmplBioServer, false);
    }


    /**
     * Elimina gli eventuali contenuti IN CODA che non devono essere presi in considerazione <br>
     * Restituisce un valore GREZZO che deve essere ancora elaborato <br>
     * <p>
     * Tag chiave di troncatura sempre validi:
     * REF = "<ref"
     * NOTE = "<!--"
     * GRAFFE = "{{"
     * UGUALE = "="
     * CIRCA = "circa";
     * ECC = "ecc."
     * INTERROGATIVO = "?"
     * <p>
     * Tag chiave di troncatura opzionali a seconda del parametro:
     * PARENTESI = "("
     * VIRGOLA = ","
     *
     * @param valorePropertyTmplBioServer testo originale proveniente dalla property tmplBioServer della entity Bio
     *
     * @return valore grezzo troncato dopo alcuni tag chiave (<ref>, {{, ecc.) <br>
     */
    public String troncaDopoTag(String valorePropertyTmplBioServer, boolean puntoAmmesso) {
        String valoreGrezzo = valorePropertyTmplBioServer.trim();

        if (text.isEmpty(valorePropertyTmplBioServer)) {
            return VUOTA;
        }

        if (valorePropertyTmplBioServer.equals(PUNTO_INTERROGATIVO) && puntoAmmesso) {
            return PUNTO_INTERROGATIVO;
        }

        valoreGrezzo = text.levaDopoRef(valoreGrezzo);
        valoreGrezzo = text.levaDopoNote(valoreGrezzo);
        valoreGrezzo = text.levaDopoGraffe(valoreGrezzo);
        valoreGrezzo = text.levaDopoUguale(valoreGrezzo);
        valoreGrezzo = text.levaDopoCirca(valoreGrezzo);
        valoreGrezzo = text.levaDopoEccetera(valoreGrezzo);
        valoreGrezzo = text.levaDopoInterrogativo(valoreGrezzo);

        return valoreGrezzo.trim();
    }

}