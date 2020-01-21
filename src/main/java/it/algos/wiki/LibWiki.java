package it.algos.wiki;

/**
 * Created by gac on 04 ago 2015.
 * .
 */

import it.algos.vaadflow.service.ATextService;
import it.algos.wiki.entities.wiki.Wiki;
import it.algos.wiki.request.QueryCat;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static it.algos.wiki.mediawiki.ReadLogin.FIRST_NEW_TOKEN;
import static it.algos.wiki.mediawiki.ReadLogin.LOGIN_TOKEN;
import static it.algos.wiki.mediawiki.ReadWiki.SESSION_TOKEN;

/**
 * Libreria
 */
public abstract class LibWiki {

    public static final String NOME_BOT = "Biobot";

    //--preferenza
    public static final String DEBUG = "debug";

    public static final String NUM_RECORDS_INDEX_BIO = "numRecordsIndexBio";

    public static final String USA_FLASH_TRUE_DOWNLOAD = "usaFlashTrueDownload";

    public static final String SEND_MAIL_ERROR = "sendMailError";

    public static final String SEND_MAIL_WARN = "sendMailWarn";

    public static final String SEND_MAIL_INFO = "sendMailInfo";

    public static final String LOG_ERROR = "logError";

    public static final String LOG_WARN = "logWarn";

    public static final String LOG_INFO = "logInfo";

    public static final String NEW_BIO = "NewBio";

    public static final String REF = "<ref>";

    public static final String NOTE = "<!--";


    public static final String PARAGRAFO = "==";

    public static final String TONDA_INI = "(";

    public static final String TONDA_END = ")";

    public static final String NO_WIKI_INI = "<nowiki>";

    public static final String NO_WIKI_END = "</nowiki>";

    public static final String QUADRA_INI = "[";

    public static final String QUADRE_INI = QUADRA_INI + QUADRA_INI;

    public static final String QUADRA_END = "]";

    public static final String QUADRE_END = QUADRA_END + QUADRA_END;

    public static final String GRAFFA_INI = "{";

    public static final String GRAFFE_INI = GRAFFA_INI + GRAFFA_INI;

    public static final String GRAFFA_END = "}";

    public static final String GRAFFE_END = GRAFFA_END + GRAFFA_END;

    public static final String REF_INI = "<ref>";

    public static final String REF_END = "</ref>";

    public static final String COL_INI = "Div col";

    public static final String COL_END = "Div col end";

    public static final String BOLD = "'''";

    public static final String BIO = Cost.TAG_WIKI_BIO;

    // tag per la stringa vuota

    public static final String VUOTA = "";

    // tag per il valore falso per una posizione
    public static final int INT_NULLO = -1;

    // key to store objects in a HashMap
    public static final String KEY_PAGINE_VALIDE = "pagineValide";

    public static final String KEY_PAGINE_MANCANTI = "pagineMancanti";

    public static final String TOKEN = "csrftoken";

    public static final String EDIT = "edit";

    public static final String RESULT = "result";

    public static final String SUCCESS = "Success";

    public static final String NOCHANGE = "nochange";

    public static final String OLD_REV_ID = "oldrevid";

    public static final String NEW_REV_ID = "newrevid";

    public static final String NEW_TIME_STAMP = "newtimestamp";

    public static final String CONTENT_MODEL = "contentmodel";

    public static final String NS = "ns";

    public static final String PAGEID = "pageid";

    public static final String TITLE = "title";

    public static final String BATCH = "batchcomplete";

    public static final String QUERY = "query";

    public static final String CODE = "code";

    public static final String DOCREF = "docref";

    public static final String INFO = "info";

    public static final String MAIN = "main";

    public static final String CONTENT = "content";

    public static final String MUST_BE_POSTED = "mustbeposted";

    public static final String KEY_VOCE_PAGEID = "keyvocepageid";

    public static final String KEY_VOCE_TITLE = "keyvocetitle";

    public static final String KEY_CAT_PAGEID = "keycatpageid";

    public static final String KEY_CAT_TITLE = "keycattitle";

    public static final String WARNINGS = "warnings";

    public static final String ERROR = "error";

    public static final String CATEGORY_MEMBERS = "categorymembers";

    public static final String CONTINUE = "continue";

    public static final String CMCONTINUE = "cmcontinue";

    public static final String CM_CONTINUE = "cmcontinue";

    public static final String PAGES = "pages";

    public static final String REVISIONS = "revisions";

    private static final String TOKENS = "tokens";

    private static final String TIMESTAMP = "timestamp";

    private static final String BACK_LINKS = "backlinks";

    private static final String QUERY_CONTINUE = "query-continue"; // deprecated

    private static final String LOGIN = "login";

    private static final String BATCHCOMPLETE = "batchcomplete";

    private static final String MISSING = "missing";

    private static final String VIR = ",";

    private static final String APICI = "\"";

    private static final String PUNTI = ":";

    private static final String SPAZIO = " ";

    private static final String PIPE = "|";

    private static final String A_CAPO = "\n";

    public static Date DATA_NULLA = new Date(70, 0, 1);

    /* caratteri finali per la stringa di edittoken da rinviare al server */
    public static String END_TOKEN = "%2B%5C";

    // patch per la key del parametro testo
    private static String PATCH_OLD = "*";

    private static String PATCH_NEW = "text";

    private static String GRIGIO_SCURO = "style=\"background-color:#EFEFEF;\"";

    private static String GRIGIO_MEDIO = "style=\"background-color:#CCC;\"";

    private static String TXT_ALIGN = "style=\"text-align: right;\"" + SPAZIO;

    @Autowired
    private ATextService text;

//    /**
//     * Converte il valore stringa nel tipo previsto dal parametro PagePar
//     *
//     * @param mappa standard (valori String) in ingresso
//     * @return mappa typizzata secondo PagePar
//     */
//    private static fixValueMap(String key, String valueTxt) {
//        def valueObj = null
//        PagePar.TypeField typo = PagePar.getParField(key)
//
//        switch (typo) {
//            case PagePar.TypeField.string:
//                valueObj = valueTxt
//                break;
//            case PagePar.TypeField.longzero:     //--conversione degli interi
//            case PagePar.TypeField.integernotzero:  //--conversione degli interi
//                try { // prova ad eseguire il codice
//                    valueObj = Integer.decode(valueTxt)
//                } catch (Exception unErrore) { // intercetta l'errore
//                    unErrore = null
//                    valueObj = 0
//                }// fine del blocco try-catch


//                break;
//            case PagePar.TypeField.date:            //--conversione delle date
//                try { // prova ad eseguire il codice
//                    valueObj = convertTxtData(valueTxt)
//                } catch (Exception unErrore) { // intercetta l'errore
//                    unErrore = null
//                    valueObj = DATA_NULLA
//                }// fine del blocco try-catch
//                break;
//            default: // caso non definito
//                break;
//        } // fine del blocco switch
//
//        return valueObj
//    } // fine del metodo


    /**
     * Restituisce il numero di occorrenze di un tag nel testo.
     * Il tag non viene trimmato ed è sensibile agli spazi prima e dopo
     *
     * @param testo da spazzolare
     * @param tag   da cercare
     *
     * @return numero di occorrenze - zero se non ce ne sono
     */
    public static int getNumTag(String testo, String tag) {
        int numTag = 0;
        int pos;

        // controllo di congruità
        if (testo != null && tag != null) {
            if (testo.contains(tag)) {
                pos = testo.indexOf(tag);
                while (pos != -1) {
                    pos = testo.indexOf(tag, pos + tag.length());
                    numTag++;
                }// fine di while
            } else {
                numTag = 0;
            }// fine del blocco if-else
        }// fine del blocco if

        return numTag;
    } // fine del metodo


    /**
     * Restituisce il numero di occorrenze di una coppia di graffe iniziali nel testo.
     *
     * @param testo da spazzolare
     *
     * @return numero di occorrenze
     * zero se non ce ne sono
     */
    public static int getNumGraffeIni(String testo) {
        return getNumTag(testo, GRAFFE_INI);
    } // fine del metodo


    /**
     * Restituisce il numero di occorrenze di una coppia di graffe finali nel testo.
     *
     * @param testo da spazzolare
     *
     * @return numero di occorrenze
     * zero se non ce ne sono
     */
    public static int getNumGraffeEnd(String testo) {
        return getNumTag(testo, GRAFFE_END);
    } // fine del metodo


    /**
     * Controlla che le occorrenze del tag iniziale e di quello finale si pareggino all'interno del testo.
     * Ordine ed annidamento NON considerato
     *
     * @param testo  da spazzolare
     * @param tagIni tag iniziale
     * @param tagEnd tag finale
     *
     * @return vero se il numero di tagIni è uguale al numero di tagEnd
     */
    public static boolean isPariTag(String testo, String tagIni, String tagEnd) {
        boolean pari = false;
        int numIni;
        int numEnd;

        // controllo di congruità
        if (testo != null && tagIni != null && tagEnd != null) {
            numIni = getNumTag(testo, tagIni);
            numEnd = getNumTag(testo, tagEnd);
            pari = (numIni == numEnd);
        }// fine del blocco if

        return pari;
    } // fine del metodo


    /**
     * Controlla che le occorrenze delle graffe iniziali e finali si pareggino all'interno del testo.
     * Ordine ed annidamento NON considerato
     *
     * @param testo da spazzolare
     *
     * @return vero se il numero di GRAFFE_INI è uguale al numero di GRAFFE_END
     */
    public static boolean isPariGraffe(String testo) {
        return isPariTag(testo, GRAFFE_INI, GRAFFE_END);
    } // fine del metodo


    /**
     * Elimina la testa iniziale della stringa, se esiste. <br>
     * <p>
     * Esegue solo se la stringa è valida. <br>
     * Se manca la testa, restituisce la stringa. <br>
     * Elimina spazi vuoti iniziali e finali. <br>
     *
     * @param entrata stringa in ingresso
     * @param testa   da eliminare
     *
     * @return uscita stringa convertita
     */
    public static String levaTesta(String entrata, String testa) {
        String uscita = entrata;

        if (entrata != null) {
            uscita = entrata.trim();
            if (testa != null) {
                testa = testa.trim();
                if (uscita.startsWith(testa)) {
                    uscita = uscita.substring(testa.length());
                    uscita = uscita.trim();
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        return uscita;
    } // fine del metodo


    /**
     * Elimina la coda terminale della stringa, se esiste.
     * <p>
     * Esegue solo se la stringa è valida. <br>
     * Se manca la coda, restituisce la stringa. <br>
     * Elimina spazi vuoti iniziali e finali. <br>
     *
     * @param entrata stringa in ingresso
     * @param coda    da eliminare
     *
     * @return uscita stringa convertita
     */
    public static String levaCoda(String entrata, String coda) {
        String uscita = entrata;

        if (entrata != null) {
            uscita = entrata.trim();
            if (coda != null) {
                coda = coda.trim();
                if (uscita.endsWith(coda)) {
                    uscita = uscita.substring(0, uscita.length() - coda.length());
                    uscita = uscita.trim();
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        return uscita;
    } // fine del metodo


    /**
     * Sostituisce tutte le occorrenze.
     * Esegue solo se il testo è valido
     * Se arriva un oggetto non stringa, restituisce l'oggetto
     *
     * @param testoIn    in ingresso
     * @param oldStringa da eliminare
     * @param newStringa da sostituire
     *
     * @return testoOut convertito
     */
    public static String sostituisce(String testoIn, String oldStringa, String newStringa) {
        String testoOut = testoIn;
        int pos = 0;
        String prima = VUOTA;

        if (testoIn != null && oldStringa != null && newStringa != null) {
            testoOut = testoIn.trim();
            if (testoIn.contains(oldStringa)) {

                while (pos != INT_NULLO) {
                    pos = testoIn.indexOf(oldStringa);
                    if (pos != INT_NULLO) {
                        prima += testoIn.substring(0, pos);
                        prima += newStringa;
                        pos += oldStringa.length();
                        testoIn = testoIn.substring(pos);
                    }// fine del blocco if
                }// fine di while

                testoOut = prima + testoIn;
            }// fine del blocco if
        }// fine del blocco if

        return testoOut;
    } // fine del metodo


    /**
     * Chiude il template
     * <p>
     * Il testo inizia col template, ma prosegue (forse) anche oltre
     * Cerco la prima doppia graffa che abbia all'interno lo stesso numero di aperture e chiusure
     * Spazzola il testo fino a pareggiare le graffe
     * Se non riesce a pareggiare le graffe, ritorna una stringa nulla
     *
     * @param templateIn da spazzolare
     *
     * @return template
     */
    public static String chiudeTmpl(String templateIn) {
        String templateOut;
        int posIni = 0;
        int posEnd = 0;
        boolean pari = false;

        templateOut = templateIn.substring(posIni, posEnd + GRAFFE_END.length()).trim();

        while (!pari) {
            posEnd = templateIn.indexOf(GRAFFE_END, posEnd + GRAFFE_END.length());
            if (posEnd != -1) {
                templateOut = templateIn.substring(posIni, posEnd + GRAFFE_END.length()).trim();
                pari = isPariGraffe(templateOut);
            } else {
                break;
            }// fine del blocco if-else
        } //fine del ciclo while

        if (!pari) {
            templateOut = VUOTA;
        }// fine del blocco if

        return templateOut;
    } // fine del metodo


    /**
     * Estrae il testo di un template dal testo completo della voce
     * Esamina il PRIMO template che trova
     * Gli estremi sono COMPRESI
     * <p>
     * Recupera il tag iniziale con o senza ''Template''
     * Recupera il tag iniziale con o senza primo carattere maiuscolo
     * Recupera il tag finale di chiusura con o senza ritorno a capo precedente
     * Controlla che non esistano doppie graffe dispari all'interno del template
     * <p>
     * Prova anche col tag minuscolo
     */
    public static String estraeTmplCompresi(String testo, String tag) {
        String template = estraeTmplCompresiBase(testo, tag);

        // forza il primo carattere a maiuscolo
        if (template.equals("")) {
//            template = estraeTmplCompresiBase(testo, text.primaMaiuscola(tag));
        }// fine del blocco if

        // forza il primo carattere a minuscolo
        if (template.equals("")) {
//            template = estraeTmplCompresiBase(testo, text.primaMinuscola(tag));
        }// fine del blocco if

        return template;
    } // fine del metodo


    /**
     * Estrae il testo di un template dal testo completo della voce
     * Esamina il PRIMO template che trova
     * Gli estremi sono COMPRESI
     * <p>
     * Recupera il tag iniziale con o senza ''Template''
     * Recupera il tag iniziale con o senza primo carattere maiuscolo
     * Recupera il tag finale di chiusura con o senza ritorno a capo precedente
     * Controlla che non esistano doppie graffe dispari all'interno del template
     */
    private static String estraeTmplCompresiBase(String testo, String tag) {
        String template = VUOTA;
        boolean continua = false;
        String patternTxt = "";
        Pattern patttern = null;
        Matcher matcher = null;
        int posIni;
        int posEnd;
        String tagIniTemplate = VUOTA;

        // controllo di congruita
        if (testo != null && tag != null) {
            // patch per nome template minuscolo o maiuscolo
            // deve terminare con 'aCapo' oppure 'return' oppure 'tab' oppure '|'(pipe) oppure 'spazio'(u0020)
            if (tag.equals("Bio")) {
                tag = "[Bb]io[\n\r\t\\|\u0020(grafia)]";
            }// end of if cycle

            // Create a Pattern text
            patternTxt = "\\{\\{(Template:)?" + tag;

            // Create a Pattern object
            patttern = Pattern.compile(patternTxt);

            // Now create matcher object.
            matcher = patttern.matcher(testo);
            if (matcher.find() && matcher.groupCount() > 0) {
                tagIniTemplate = matcher.group(0);
            }// fine del blocco if-else

            // controlla se esiste una doppia graffa di chiusura
            // non si sa mai
            if (!tagIniTemplate.equals("")) {
                posIni = testo.indexOf(tagIniTemplate);
                posEnd = testo.indexOf(GRAFFE_END, posIni);
                template = testo.substring(posIni);
                if (posEnd != -1) {
                    continua = true;
                }// fine del blocco if
            }// fine del blocco if

            // cerco la prima doppia graffa che abbia all'interno
            // lo stesso numero di aperture e chiusure
            // spazzola il testo fino a pareggiare le graffe
            if (continua) {
                template = chiudeTmpl(template);
            }// fine del blocco if
        }// fine del blocco if

        return template;
    }// fine del metodo


    /**
     * Estrae il testo di un template dal testo completo della voce
     * Esamina il PRIMO template che trova
     * Gli estremi sono ESCLUSI
     * <p>
     * Recupera il tag iniziale con o senza ''Template''
     * Recupera il tag finale di chiusura con o senza ritorno a capo precedente
     * Controlla che non esistano doppie graffe dispari all'interno del template
     */
    public static String estraeTmplEsclusi(String testo, String tag) {
        String template = estraeTmplCompresi(testo, tag);
        template = setNoGraffe(template);
        return template.trim();
    }// fine del metodo


    /**
     * Estrae il testo di un template BIO dal testo completo della voce
     * Esamina il PRIMO template che trova (ce ne dovrebbe essere solo uno)
     * Gli estremi sono COMPRESI
     * <p>
     * Recupera il tag iniziale con o senza ''Template''
     * Recupera il tag finale di chiusura con o senza ritorno a capo precedente
     * Controlla che non esistano doppie graffe dispari all'interno del template
     */
    public static String estraeTmplBioCompresi(String testo) {
        return estraeTmplCompresi(testo, BIO);
    }// fine del metodo


    /**
     * Estrae il testo di un template BIO dal testo completo della voce
     * Esamina il PRIMO template che trova
     * Gli estremi sono ESCLUSI
     * <p>
     * Recupera il tag iniziale con o senza ''Template''
     * Recupera il tag finale di chiusura con o senza ritorno a capo precedente
     * Controlla che non esistano doppie graffe dispari all'interno del template
     */
    public static String estraeTmplBioEsclusi(String testo) {
        return estraeTmplEsclusi(testo, BIO);
    }// fine del metodo


    /**
     * Crea una mappa standard (valori reali) dal testo JSON di una pagina action=query
     *
     * @param textJSON in ingresso
     *
     * @return mappa query (valori reali)
     */
    public static HashMap<String, Object> creaMappaQuery(String textJSON) {
        return creaMappaQuery(textJSON, 0);
    }// fine del metodo


    /**
     * Crea una mappa standard (valori reali) dal testo JSON di una pagina action=query
     *
     * @param textJSON in ingresso
     *
     * @return mappa query (valori reali)
     */
    public static HashMap<String, Object> creaMappaQuery(String textJSON, int pos) {
        HashMap<String, Object> mappa = null;
        JSONObject objectAll = null;
        boolean batchcomplete = false;
        JSONObject objectQuery = null;
        JSONObject objectToken = null;
        JSONArray arrayPages = null;
        JSONObject objectRev = null;
        JSONArray arrayRev = null;
        String token = "";

        //--recupera i due oggetti al livello root del testo (batchcomplete e query)
        objectAll = (JSONObject) JSONValue.parse(textJSON);

        //--controllo
        if (objectAll == null) {
            return null;
        }// fine del blocco if

        //--recupera il valore del parametro di controllo per la gestione dell'ultima versione di mediawiki
        if (objectAll.get(BATCH) != null && objectAll.get(BATCH) instanceof Boolean) {
            batchcomplete = (Boolean) objectAll.get(BATCH);
        }// fine del blocco if

        //--recupera i valori dei parametri pages
        if (objectAll.get(QUERY) != null && objectAll.get(QUERY) instanceof JSONObject) {
            objectQuery = (JSONObject) objectAll.get(QUERY);
            if (objectQuery.get(PAGES) != null && objectQuery.get(PAGES) instanceof JSONArray) {
                arrayPages = (JSONArray) objectQuery.get(PAGES);
            }// fine del blocco if
            if (objectQuery.get(TOKENS) != null && objectQuery.get(TOKENS) instanceof JSONObject) {
                objectToken = (JSONObject) objectQuery.get(TOKENS);
                if (objectToken.get(TOKEN) != null && objectToken.get(TOKEN) instanceof String) {
                    token = (String) objectToken.get(TOKEN);
                }// end of if cycle
            }// fine del blocco if
        }// fine del blocco if

        //--recupera i valori dei parametri revisions
        if (arrayPages != null && arrayPages.get(pos) != null && arrayPages.get(pos) instanceof JSONObject) {
            objectRev = (JSONObject) arrayPages.get(pos);
            if (objectRev != null) {
                arrayRev = (JSONArray) objectRev.get(REVISIONS);
            }// fine del blocco if
        }// fine del blocco if

        //--crea la mappa
        mappa = mixJSON(batchcomplete, arrayPages, arrayRev, token);

        return patchMappa(mappa);
    } // fine del metodo


    /**
     * Crea una mappa standard (valori reali) dal testo JSON di una pagina action=edit
     *
     * @param textJSON in ingresso
     *
     * @return mappa edit (valori reali)
     */
    public static HashMap<String, Object> creaMappaEdit(String textJSON) {
        HashMap<String, Object> mappa = null;
        JSONObject objectAll;
        JSONObject objectEdit = null;
        String result;
        String contentmodel;
        long pageid;
        String title;
        boolean noChange = false;
        long oldrevid;
        long newrevid;
        String newtimestamp;

        //--recupera i due oggetti al livello root del testo (batchcomplete e query)
        objectAll = (JSONObject) JSONValue.parse(textJSON);

        //--controllo
        if (objectAll == null) {
            return null;
        }// fine del blocco if

        if (objectAll.get(EDIT) != null && objectAll.get(EDIT) instanceof JSONObject) {
            objectEdit = (JSONObject) objectAll.get(EDIT);
        }// fine del blocco if

        if (objectEdit != null) {
            mappa = new HashMap<String, Object>();

            if (objectEdit.get(RESULT) != null && objectEdit.get(RESULT) instanceof String) {
                result = (String) objectEdit.get(RESULT);
                mappa.put(RESULT, result);
            }// end of if cycle

            if (objectEdit.get(CONTENT_MODEL) != null && objectEdit.get(CONTENT_MODEL) instanceof String) {
                contentmodel = (String) objectEdit.get(CONTENT_MODEL);
                mappa.put(CONTENT_MODEL, contentmodel);
            }// end of if cycle

            if (objectEdit.get(PAGEID) != null && objectEdit.get(PAGEID) instanceof Long) {
                pageid = (Long) objectEdit.get(PAGEID);
                mappa.put(PAGEID, pageid);
            }// end of if cycle

            if (objectEdit.get(TITLE) != null && objectEdit.get(TITLE) instanceof String) {
                title = (String) objectEdit.get(TITLE);
                mappa.put(TITLE, title);
            }// end of if cycle

            if (objectEdit.get(NOCHANGE) != null && objectEdit.get(NOCHANGE) instanceof Boolean) {
                noChange = (Boolean) objectEdit.get(NOCHANGE);
                mappa.put(NOCHANGE, noChange);
            }// end of if cycle

            if (!noChange) {
                if (objectEdit.get(OLD_REV_ID) != null && objectEdit.get(OLD_REV_ID) instanceof Long) {
                    oldrevid = (Long) objectEdit.get(OLD_REV_ID);
                    mappa.put(OLD_REV_ID, oldrevid);
                }// end of if cycle

                if (objectEdit.get(NEW_REV_ID) != null && objectEdit.get(NEW_REV_ID) instanceof Long) {
                    newrevid = (Long) objectEdit.get(NEW_REV_ID);
                    mappa.put(NEW_REV_ID, newrevid);
                }// end of if cycle

                if (objectEdit.get(NEW_TIME_STAMP) != null && objectEdit.get(NEW_TIME_STAMP) instanceof String) {
                    newtimestamp = (String) objectEdit.get(NEW_TIME_STAMP);
                    mappa.put(NEW_TIME_STAMP, newtimestamp);
                }// end of if cycle
            }// end of if cycle
        }// fine del blocco if

        return mappa;
    } // fine del metodo


    /**
     * Crea una mappa categoria (valori String) dal testo JSON di una pagina di login
     *
     * @param textJSON in ingresso
     *
     * @return mappa categoria (valori String)
     */
    public static HashMap<String, Object> creaMappaCat(String textJSON) {
        HashMap<String, Object> mappa = null;
        JSONObject objectAll;
        boolean batchcomplete = false;
        JSONObject objContinue = null;
        String cmContinue = null;
        JSONObject objQuery = null;
        JSONArray listaCat;

        objectAll = (JSONObject) JSONValue.parse(textJSON);

        // controllo
        if (objectAll == null) {
            return null;
        }// fine del blocco if
        mappa = new HashMap<String, Object>();

        if (objectAll.get(BATCHCOMPLETE) != null && objectAll.get(BATCHCOMPLETE) instanceof Boolean) {
            batchcomplete = (boolean) objectAll.get(BATCHCOMPLETE);
            mappa.put(BATCHCOMPLETE, batchcomplete);
        }// fine del blocco if

        if (objectAll.get(CONTINUE) != null && objectAll.get(CONTINUE) instanceof JSONObject) {
            objContinue = (JSONObject) objectAll.get(CONTINUE);
            if (objContinue.get(CMCONTINUE) != null && objContinue.get(CMCONTINUE) instanceof String) {
                cmContinue = (String) objContinue.get(CMCONTINUE);
                mappa.put(CMCONTINUE, cmContinue);
            }// fine del blocco if
        }// fine del blocco if

        if (objectAll.get(QUERY) != null && objectAll.get(QUERY) instanceof JSONObject) {
            objQuery = (JSONObject) objectAll.get(QUERY);
            if (objQuery.get(CATEGORY_MEMBERS) != null && objQuery.get(CATEGORY_MEMBERS) instanceof JSONArray) {
                listaCat = (JSONArray) objQuery.get(CATEGORY_MEMBERS);
                mappa.put(CATEGORY_MEMBERS, listaCat);
            }// fine del blocco if
        }// fine del blocco if


        return mappa;
    } // fine del metodo


    /**
     * Crea una mappa standard (valori reali) dal testo JSON di una pagina action=move
     *
     * @param textJSON in ingresso
     *
     * @return mappa edit (valori reali)
     */
    public static HashMap<String, Object> creaMappaMove(String textJSON) {
        HashMap<String, Object> mappa = null;
        JSONObject objectAll;
        JSONObject objectError = null;
        String code;
        String docref;
        String info;

        //--recupera i due oggetti al livello root del testo (batchcomplete e query)
        objectAll = (JSONObject) JSONValue.parse(textJSON);

        //--controllo
        if (objectAll == null) {
            return null;
        }// fine del blocco if

        if (objectAll.get(ERROR) != null && objectAll.get(ERROR) instanceof JSONObject) {
            objectError = (JSONObject) objectAll.get(ERROR);
        }// fine del blocco if

        if (objectError != null) {
            mappa = new HashMap<String, Object>();

            if (objectError.get(CODE) != null && objectError.get(CODE) instanceof String) {
                code = (String) objectError.get(CODE);
                mappa.put(CODE, code);
            }// end of if cycle

            if (objectError.get(DOCREF) != null && objectError.get(DOCREF) instanceof String) {
                docref = (String) objectError.get(DOCREF);
                mappa.put(DOCREF, docref);
            }// end of if cycle

            if (objectError.get(INFO) != null && objectError.get(INFO) instanceof String) {
                info = (String) objectError.get(INFO);
                mappa.put(INFO, info);
            }// end of if cycle
        }// fine del blocco if

        return mappa;
    } // fine del metodo


    /**
     * Crea una mappa login (valori String) dal testo JSON di una pagina di login
     *
     * @param textJSON in ingresso
     *
     * @return mappa standard (valori String)
     */
    public static HashMap<String, Object> creaMappaLogin(String textJSON) {
        HashMap<String, Object> mappa = null;
        JSONObject objectAll;
        JSONObject objectQuery = null;

        // recupera i due oggetti al livello root del testo (batchcomplete e query)
        objectAll = (JSONObject) JSONValue.parse(textJSON);

        // controllo
        if (objectAll == null) {
            return null;
        }// fine del blocco if

        //recupera i valori dei parametri di login
        if (objectAll.get(LOGIN) != null && objectAll.get(LOGIN) instanceof JSONObject) {
            objectQuery = (JSONObject) objectAll.get(LOGIN);
        }// fine del blocco if

        //recupera i valori dei parametri di login
        if (objectAll.get(QUERY) != null && objectAll.get(QUERY) instanceof JSONObject) {
            objectQuery = (JSONObject) objectAll.get(QUERY);
        }// fine del blocco if

        if (objectQuery != null && objectQuery.size() > 0) {
            mappa = new HashMap<String, Object>();

            for (Object key : objectQuery.keySet()) {
                mappa.put((String) key, objectQuery.get((String) key));
            } // fine del ciclo for-each
        }// end of if cycle

        return mappa;
    } // fine del metodo


    /**
     * Crea una mappa errrori (valori String) dal testo JSON di una response
     *
     * @param textJSON in ingresso
     *
     * @return mappa standard (valori String)
     */
    public static HashMap<String, Object> creaMappaError(String textJSON) {
        HashMap<String, Object> mappa = null;
        JSONObject objectAll;
        JSONObject objectError = null;

        // recupera i due oggetti al livello root del testo (error e servedby)
        objectAll = (JSONObject) JSONValue.parse(textJSON);

        // controllo
        if (objectAll == null) {
            return null;
        }// fine del blocco if

        //recupera i valori dei parametri di error
        if (objectAll.get(ERROR) != null && objectAll.get(ERROR) instanceof JSONObject) {
            objectError = (JSONObject) objectAll.get(ERROR);
        }// fine del blocco if

        if (objectError != null && objectError.size() > 0) {
            mappa = new HashMap<String, Object>();

            for (Object key : objectError.keySet()) {
                mappa.put((String) key, objectError.get((String) key));
            } // fine del ciclo for-each
        }// end of if cycle

        return mappa;
    } // fine del metodo


    /**
     * Restituisce il logintoken dalla mappa (se esiste)
     *
     * @param mappa standard (valori String)
     *
     * @return logintoken
     */
    public static String getLoginToken(HashMap<String, Object> mappa) {
        String loginToken = "";
        JSONObject obj;

        if (mappa.get(FIRST_NEW_TOKEN) != null && mappa.get(FIRST_NEW_TOKEN) instanceof JSONObject) {
            obj = (JSONObject) mappa.get(FIRST_NEW_TOKEN);
            if (obj.get(LOGIN_TOKEN) != null && obj.get(LOGIN_TOKEN) instanceof String) {
                loginToken = (String) obj.get(LOGIN_TOKEN);
            }// fine del blocco if
        }// fine del blocco if

        return loginToken;
    } // fine del metodo


    /**
     * Restituisce il itWikiSession dalla mappa (se esiste)
     *
     * @param mappa standard (valori String)
     *
     * @return sessionToken
     */
    public static String getSessionToken(HashMap<String, Object> mappa) {
        return getToken(mappa, SESSION_TOKEN);
    } // fine del metodo


    /**
     * Restituisce un valore dalla mappa per la chiave indicata (se esiste)
     *
     * @param mappa  standard (valori String)
     * @param keyTag per individuare il valore della mappa
     *
     * @return sessionToken
     */
    public static String getToken(HashMap<String, Object> mappa, String keyTag) {
        String sessionToken = "";

        if (mappa.get(keyTag) != null && mappa.get(keyTag) instanceof String) {
            sessionToken = (String) mappa.get(keyTag);
        }// fine del blocco if

        return sessionToken;
    } // fine del metodo


    /**
     * Crea una mappa token (valori String) dal testo JSON di una preliminary request
     *
     * @param textJSON in ingresso
     *
     * @return mappa standard (valori String)
     */
    public static HashMap<String, Object> creaMappaToken(String textJSON) {
        HashMap<String, Object> mappa = null;
        JSONObject objectAll;
        boolean batchcomplete = false;
        JSONObject objectQuery = null;
        JSONObject objectToken = null;
        String token = "";

        // recupera i due oggetti al livello root del testo (batchcomplete e query)
        objectAll = (JSONObject) JSONValue.parse(textJSON);

        // controllo
        if (objectAll == null) {
            return null;
        }// fine del blocco if

        //--recupera il valore del parametro di controllo per la gestione dell'ultima versione di mediawiki
        if (objectAll.get(BATCH) != null && objectAll.get(BATCH) instanceof Boolean) {
            batchcomplete = (Boolean) objectAll.get(BATCH);
        }// fine del blocco if

        //--recupera i valori dei parametri pages
        if (objectAll.get(QUERY) != null && objectAll.get(QUERY) instanceof JSONObject) {
            objectQuery = (JSONObject) objectAll.get(QUERY);
            if (objectQuery.get(TOKENS) != null && objectQuery.get(TOKENS) instanceof JSONObject) {
                objectToken = (JSONObject) objectQuery.get(TOKENS);
                if (objectToken.get(TOKEN) != null && objectToken.get(TOKEN) instanceof String) {
                    token = (String) objectToken.get(TOKEN);
                }// end of if cycle
            }// fine del blocco if
        }// fine del blocco if

        //--crea la mappa
        mappa = mixJSON(batchcomplete, token);

        return mappa;
    } // fine del metodo


    /**
     * Recupera una lista delle chiavi di una mappa
     *
     * @param mappa in ingresso
     *
     * @return lista delle chiavi
     */
    public static ArrayList getKeyFromMap(HashMap mappa) {
        ArrayList listaKeys = null;

        if (mappa != null) {
            listaKeys = new ArrayList();
            for (Object obj : mappa.keySet()) {
                listaKeys.add(obj);
            }// end of for cycle
        }// end of if cycle

        return listaKeys;
    }// end of static method


    /**
     * Crea una mappa standard (valori reali) dal testo JSON di una pagina
     *
     * @param textJSON in ingresso
     *
     * @return mappa standard (valori reali)
     */
    public static HashMap<String, Object> creaMappa(String textJSON) {
        HashMap<String, Object> mappa = null;
        JSONObject objectAll;
        ArrayList<String> listaKeys = null;

        //--recupera gli oggetti al livello root del testo
        objectAll = (JSONObject) JSONValue.parse(textJSON);

        //--controllo
        if (objectAll == null) {
            return null;
        }// fine del blocco if

        listaKeys = getKeyFromMap(objectAll);

        if (listaKeys != null) {
            mappa = new HashMap<String, Object>();
            for (String key : listaKeys) {
                mappa.put(key, objectAll.get(key));
            }// end of for cycle
        }// end of if cycle

        return mappa;
    } // fine del metodo


    /**
     * Crea una mappa per leggere solo i timestamps dal testo JSON di una pagina
     *
     * @param textJSON in ingresso
     *
     * @return mappa con due array: timestamps validi e timestamps non validi
     */
    public static HashMap<String, ArrayList<WrapTime>> creaArrayWrapTime(String textJSON) {
        HashMap<String, ArrayList<WrapTime>> mappa = null;
        ArrayList<WrapTime> listaPagineValide = null;
        ArrayList<WrapTime> listaPagineMancanti = null;
        WrapTime wrap;
        JSONObject objectAll;
        boolean batchcomplete;
        JSONObject objectQuery;
        JSONArray arrayPages = null;
        JSONObject singlePage;
        long pageid;
        String wikiTitle;
        JSONArray singleRev;
        JSONObject timeObj;
        String timeStr;

        // recupera i due oggetti al livello root del testo (batchcomplete e query)
        objectAll = (JSONObject) JSONValue.parse(textJSON);

        // controllo
        if (objectAll == null) {
            return null;
        }// fine del blocco if

        //recupera il valore del parametro di controllo per la gestione dell'ultima versione di mediawiki
        if (objectAll.get(BATCH) != null && objectAll.get(BATCH) instanceof Boolean) {
            batchcomplete = (Boolean) objectAll.get(BATCH);
        }// fine del blocco if

        //recupera i valori dei parametri
        if (objectAll.get(QUERY) != null && objectAll.get(QUERY) instanceof JSONObject) {
            objectQuery = (JSONObject) objectAll.get(QUERY);
            if (objectQuery.get(PAGES) != null && objectQuery.get(PAGES) instanceof JSONArray) {
                arrayPages = (JSONArray) objectQuery.get(PAGES);
            }// fine del blocco if
        }// fine del blocco if

        // crea la mappa
        if (arrayPages != null && arrayPages.get(0) != null && arrayPages.get(0) instanceof JSONObject) {
            listaPagineValide = new ArrayList<WrapTime>();
            listaPagineMancanti = new ArrayList<WrapTime>();
            for (Object obj : arrayPages) {
                if (obj instanceof JSONObject) {
                    singlePage = (JSONObject) obj;
                    pageid = (long) singlePage.get(PAGEID);
                    wikiTitle = (String) singlePage.get(TITLE);

                    if (singlePage.get(MISSING) == null) {
                        singleRev = (JSONArray) singlePage.get(REVISIONS);
                        timeObj = (JSONObject) singleRev.get(0);
                        timeStr = (String) timeObj.get(TIMESTAMP);
                        wrap = new WrapTime(pageid, wikiTitle, timeStr, true);
                        listaPagineValide.add(wrap);
                    } else {
                        wrap = new WrapTime(pageid, wikiTitle, null, false);
                        listaPagineMancanti.add(wrap);
                    }// end of if/else cycle

                }// fine del blocco if
            } // fine del ciclo for-each
        }// fine del blocco if

        if (listaPagineValide.size() == 0 && listaPagineMancanti.size() == 1) {
        } else {
            mappa = new HashMap<String, ArrayList<WrapTime>>();
            mappa.put(KEY_PAGINE_VALIDE, listaPagineValide);
            mappa.put(KEY_PAGINE_MANCANTI, listaPagineMancanti);
        }// end of if/else cycle

        return mappa;
    } // fine del metodo


    /**
     * Correzioni/aggiunte per eventuali patch
     * Il parametro ''anon'' è presente nel ritorno della Request SOLO se l'ultimo utente è un IP
     *
     * @param mappa in ingresso
     *
     * @return mappa in uscita
     */
    public static HashMap<String, Object> patchMappa(HashMap<String, Object> mappa) {

        if (mappa != null) {
            if (!mappa.containsKey(PagePar.anon.toString())) {
                mappa.put(PagePar.anon.toString(), false);
            }// fine del blocco if
            if (!mappa.containsKey(PagePar.missing.toString())) {
                mappa.put(PagePar.missing.toString(), false);
            }// fine del blocco if
        }// end of if cycle

        return mappa;
    } // fine del metodo


    /**
     * Crea una mappa standard (valori String) dalle mappe JSON parziali
     *
     * @param batchcomplete flag di controllo
     *
     * @return mappa standard (valori String)
     */
    public static HashMap<String, Object> mixJSON(boolean batchcomplete, String token) {
        return mixJSON(batchcomplete, null, null, token);
    } // fine del metodo


    /**
     * Crea una mappa standard (valori String) dalle mappe JSON parziali
     *
     * @param batchcomplete flag di controllo
     * @param arrayPages    parametri base (3) ed info (1)
     * @param arrayRev      parametri revisions (12)
     *
     * @return mappa standard (valori String)
     */
    public static HashMap<String, Object> mixJSON(boolean batchcomplete, JSONArray arrayPages, JSONArray arrayRev, String token) {
        return mixJSON(batchcomplete, arrayPages, arrayRev, token, 0);
    } // fine del metodo


    /**
     * Crea una mappa standard (valori String) dalle mappe JSON parziali
     *
     * @param batchcomplete flag di controllo
     * @param arrayPages    parametri base (3) ed info (1)
     * @param arrayRev      parametri revisions (12)
     *
     * @return mappa standard (valori String)
     */
    public static HashMap<String, Object> mixJSON(boolean batchcomplete, JSONArray arrayPages, JSONArray arrayRev, String token, int pos) {
        HashMap<String, Object> mappa = new HashMap<String, Object>();
        HashMap<String, Object> mappaPages = null;
        HashMap<String, Object> mappaRev = null;
        JSONObject slots;
        JSONObject main;
        Object value;
        String contenuto;

        //--recupera il valore del parametro di controllo per la gestione dell'ultima versione di mediawiki
        mappa.put(BATCH, batchcomplete);

        //--recupera i valori dei parametri info
        if (arrayPages != null) {
            mappaPages = estraeMappaJsonPar(arrayPages, pos);
            for (String key : mappaPages.keySet()) {
                value = mappaPages.get(key);
                mappa.put(key, value);
            } // fine del ciclo for-each
        }// end of if cycle

        //--recupera i valori dei parametri revisions
        if (arrayRev != null) {
            mappaRev = estraeMappaJsonPar(arrayRev, pos);
            for (String key : mappaRev.keySet()) {
                value = mappaRev.get(key);
                mappa.put(key, value);
            } // fine del ciclo for-each
        }// end of if cycle

        //--recupera i valori dei parametro di controllo token
        if (token != null && !token.equals("")) {
            token = fixToken(token);
            mappa.put(TOKEN, token);
        }// end of if cycle

//        //--patch per una nuova gestione da parete di mediawiki API
//        if (mappa.get(PagePar.slots.name()) != null && mappa.get(PagePar.slots.name()) instanceof JSONObject) {
//            slots = (JSONObject) mappa.get(PagePar.slots.name());
//            if (slots != null && slots.get(MAIN) != null && slots.get(MAIN) instanceof JSONObject) {
//                main = (JSONObject) slots.get(MAIN);
//                if (main != null && main.get(CONTENT) != null && main.get(CONTENT) instanceof String) {
//                    contenuto = (String) main.get(CONTENT);
//                    if (!contenuto.equals("")) {
//                        mappa.put(CONTENT, contenuto);
//                        mappa.remove(PagePar.slots.name());
//                    }// end of if cycle
//                }// end of if cycle
//            }// end of if cycle
//        }// end of if cycle

        return mappa;
    } // fine del metodo


    /**
     * Estrae un (eventuale) messaggio di errore dal testo JSON di una pagina
     *
     * @param textJSON in ingresso
     *
     * @return messaggio di errore
     */
    public static String getWarning(String textJSON) {
        String errore = "";
        HashMap<String, Object> mappa;
        JSONObject avviso;
        JSONObject sottoAvviso;

        if (textJSON != null && !textJSON.equals("")) {
            mappa = creaMappa(textJSON);

            if (mappa != null) {
                if (mappa.get(LibWiki.WARNINGS) != null && mappa.get(LibWiki.WARNINGS) instanceof JSONObject) {
                    avviso = (JSONObject) mappa.get(LibWiki.WARNINGS);
                    if (avviso.get(LibWiki.CATEGORY_MEMBERS) != null && avviso.get(LibWiki.CATEGORY_MEMBERS) instanceof JSONObject) {
                        sottoAvviso = (JSONObject) avviso.get(LibWiki.CATEGORY_MEMBERS);
                        if (sottoAvviso.get(LibWiki.WARNINGS) != null && sottoAvviso.get(LibWiki.WARNINGS) instanceof String) {
                            errore = (String) sottoAvviso.get(LibWiki.WARNINGS);
                        }// end of if cycle
                    }// end of if cycle
                } // fine del metodo
            }// end of if cycle

        }// end of if cycle

        return errore;
    } // fine del metodo


    /**
     * Estrae un (eventuale) messaggio di errore dal testo JSON di un login
     *
     * @param textJSON in ingresso
     *
     * @return messaggio di errore
     */
    public static String getError(String textJSON) {
        String errore = "";
        HashMap<String, Object> mappa;
        JSONObject avviso;

        if (textJSON != null && !textJSON.equals("")) {
            mappa = creaMappa(textJSON);

            if (mappa != null) {
                if (mappa.get(LibWiki.ERROR) != null && mappa.get(LibWiki.ERROR) instanceof JSONObject) {
                    avviso = (JSONObject) mappa.get(LibWiki.ERROR);
                    if (avviso.get(LibWiki.CODE) != null && avviso.get(LibWiki.CODE) instanceof String) {
                        errore = (String) avviso.get(LibWiki.CODE);
                    }// end of if cycle
                } // fine del metodo
            }// end of if cycle
        }// end of if cycle

        return errore;
    } // fine del metodo


    /**
     * Estrae un (eventuale) token testo JSON di una preliminaryRequest
     *
     * @param textJSON in ingresso
     *
     * @return csrfToken
     */
    public static String getToken(String textJSON) {
        String token = "";
        HashMap<String, Object> mappa;

        if (textJSON != null && !textJSON.equals("")) {
            mappa = creaMappaToken(textJSON);

            if (mappa != null) {
                if (mappa.get(LibWiki.TOKEN) != null && mappa.get(LibWiki.TOKEN) instanceof String) {
                    token = (String) mappa.get(LibWiki.TOKEN);
                } // fine del metodo
            }// end of if cycle
        }// end of if cycle

        return token;
    } // fine del metodo


    private static String fixToken(String edittokenIn) {
        String edittokenOut = edittokenIn;

        if (edittokenIn != null && !edittokenIn.equals("")) {
            edittokenOut = edittokenIn.substring(0, edittokenIn.length() - 2);
            edittokenOut += END_TOKEN;
        }// fine del blocco if

        return edittokenOut;
    } // fine del metodo


    /**
     * Crea una mappa standard (valori String) dal testo JSON di una pagina
     *
     * @param textJSON in ingresso
     *
     * @return mappa standard (valori String)
     *
     * @deprecated
     */
    public static LinkedHashMap creaMappaOld(String textJSON) {
        LinkedHashMap mappa = null;

        JSONObject allObj = (JSONObject) JSONValue.parse(textJSON);
        JSONObject queryObj = (JSONObject) allObj.get(QUERY);
        Object pagesObj = queryObj.get(PAGES);

        if (pagesObj instanceof JSONArray) {
            mappa = fixMappa((JSONArray) pagesObj);
        }// fine del blocco if

//        if (pagesObj instanceof JSONArray) {
//            mappa = estraeMappaJson((JSONArray) pagesObj);
//        }// fine del blocco if

        return mappa;
    } // fine del metodo


    /**
     * Crea una mappa standard (valori String) da una mappa JSON di una pagina
     * <p>
     * Prima i parametri delle info
     * Poi, se ci sono, i parametri della revisione
     *
     * @param mappaJson JSONObject in ingresso
     *
     * @return mappa standard (valori String)
     *
     * @deprecated
     */
    private static LinkedHashMap fixMappa(JSONArray mappaJson) {
        LinkedHashMap<String, Object> mappaOut = new LinkedHashMap<String, Object>();
        HashMap<String, Object> mappaValoriInfo = estraeMappaJson(mappaJson);
        HashMap<String, Object> mappaValoriRev = null;
        String key;
        Object value = null;

        //--valori dei parametri ricavati dalle info della pagina
        for (PagePar par : PagePar.getInf()) {
            key = par.toString();
            value = mappaValoriInfo.get(key);
            if (value != null) {
                mappaOut.put(key, value);
            }// fine del blocco if
        } // fine del ciclo for-each

        //--controlla che esistano i valori della revisione
        if (mappaValoriInfo.containsKey(REVISIONS) && mappaValoriInfo.get(REVISIONS) instanceof JSONArray) {
            mappaValoriRev = estraeMappaJson((JSONArray) mappaValoriInfo.get(REVISIONS));
        }// fine del blocco if

        //--valori dei parametri ricavati dall'ultima revisione della pagina
        if (mappaValoriRev != null) {
            for (String keyRev : mappaValoriRev.keySet()) {
                value = mappaValoriRev.get(keyRev);
                mappaOut.put(keyRev, value);
            } // fine del ciclo for-each
        }// fine del blocco if

        return mappaOut;
    } // fine del metodo


    /**
     * Estrae una mappa standard da un JSONObject
     *
     * @param mappaJson JSONObject in ingresso
     *
     * @return mappa standard (valori String)
     */
    private static HashMap<String, Object> estraeMappaJson(JSONObject mappaJson) {
        HashMap<String, Object> mappaOut = new HashMap<String, Object>();
        Set setJson = mappaJson.keySet();
        String key;
        Object value = null;
        JSONObject mappaGrezza = null;

        if (setJson.size() == 1) {
            for (Object obj : setJson) {
                if (obj instanceof String) {
                    mappaGrezza = (JSONObject) mappaJson.get((String) obj);
                }// fine del blocco if
            } // fine del ciclo for-each
        } else {
            mappaGrezza = mappaJson;
        }// fine del blocco if-else

        if (mappaGrezza != null) {
            for (Object elemento : mappaGrezza.keySet()) {
                if (elemento instanceof String) {
                    key = (String) elemento;
                    value = mappaGrezza.get(key);
                    mappaOut.put(key, value);
                }// fine del blocco if
            } // fine del ciclo for-each
        }// fine del blocco if

        return mappaOut;
    } // fine del metodo


    /**
     * Estrae una mappa standard da un JSONArray
     * Considera SOLO i valori della Enumeration PagePar
     *
     * @param arrayJson JSONArray in ingresso
     *
     * @return mappa standard (valori String)
     */
    private static HashMap<String, Object> estraeMappaJsonPar(JSONArray arrayJson) {
        return estraeMappaJsonPar(arrayJson, 0);
    } // fine del metodo


    /**
     * Estrae una mappa standard da un JSONArray
     * Considera SOLO i valori della Enumeration PagePar
     *
     * @param arrayJson JSONArray in ingresso
     * @param pos       elemento da estrarre
     *
     * @return mappa standard (valori String)
     */
    public static HashMap<String, Object> estraeMappaJsonPar(JSONArray arrayJson, int pos) {
        HashMap<String, Object> mappaOut = new HashMap<String, Object>();
        JSONObject mappaJSON = null;
        String key;
        Object value;

        if (arrayJson != null && arrayJson.size() > pos) {
            if (arrayJson.get(pos) != null && arrayJson.get(pos) instanceof JSONObject) {
                mappaJSON = (JSONObject) arrayJson.get(pos);
            }// fine del blocco if
        }// fine del blocco if

        if (mappaJSON != null) {
            for (PagePar par : PagePar.getRead()) {
                key = par.toString();
                if (mappaJSON.get(key) != null) {
                    value = mappaJSON.get(key);
                    mappaOut.put(key, value);
                }// fine del blocco if
            } // fine del ciclo for-each
        }// fine del blocco if

        return mappaOut;
    } // fine del metodo


    /**
     * Estrae una mappa standard da un JSONArray
     *
     * @param mappaJson JSONArray in ingresso
     *
     * @return mappa standard (valori String)
     */
    private static HashMap<String, Object> estraeMappaJson(JSONArray mappaJson) {
        HashMap<String, Object> mappaOut = new HashMap<String, Object>();

        if (mappaJson.size() == 1) {
            for (Object obj : mappaJson) {
                if (obj instanceof JSONObject) {
                    mappaOut = estraeMappaJson((JSONObject) obj);
                }// fine del blocco if
            } // fine del ciclo for-each
        }// fine del blocco if

        return mappaOut;
    } // fine del metodo


    /**
     * Converte i typi di una mappa secondo i parametri PagePar
     *
     * @param mappaIn standard (valori String) in ingresso
     *
     * @return mappa typizzata secondo PagePar
     */
    public static HashMap<String, Object> converteMappa(HashMap mappaIn) {
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
                }// fine del blocco if
            } // fine del ciclo for-each
        }// fine del blocco if

        return mappaOut;
    } // fine del metodo


    /**
     * Crea una lista di pagine (valori pageids) dal titolo di una categoria
     */
    @Deprecated
    public static ArrayList<Long> creaListaCat(String title) {
        ArrayList<Long> lista = null;
        QueryCat query = new QueryCat(title);

        lista = query.getListaPageids();

        return lista;
    }// end of method


    /**
     * Crea un array delle categorie dal testo JSON di una pagina
     *
     * @param textJSON in ingresso
     *
     * @return array di oggetti Json
     */
    private static JSONArray creaArrayCatJson(String textJSON) {
        JSONArray catObj = null;

        JSONObject allObj = (JSONObject) JSONValue.parse(textJSON);
        JSONObject queryObj = (JSONObject) allObj.get(QUERY);
        catObj = (JSONArray) queryObj.get(CATEGORY_MEMBERS);

        return catObj;
    } // fine del metodo


    /**
     * Crea un array delle pagine back dal testo JSON di una pagina
     *
     * @param textJSON in ingresso
     *
     * @return arry di oggetti Json
     */
    private static JSONArray creaArrayBackJson(String textJSON) {
        JSONArray backObj = null;

        JSONObject allObj = (JSONObject) JSONValue.parse(textJSON);
        JSONObject queryObj = (JSONObject) allObj.get(QUERY);
        backObj = (JSONArray) queryObj.get(BACK_LINKS);

        return backObj;
    } // fine del metodo


    /**
     * Crea una lista di pagine (valori pageids) da un array di oggetti Json
     *
     * @param objArray array di oggetti Json
     *
     * @return lista pageid (valori Long)
     */
    private static ArrayList<Long> creaListaBaseLongJson(JSONArray objArray) {
        return creaListaBaseLongJson(objArray, false);
    } // fine del metodo


    /**
     * Crea una lista di pagine (valori pageids) da un array di oggetti Json
     *
     * @param objArray          array di oggetti Json
     * @param nameSpaceBaseOnly flag per selezionare solo il namespace=0
     *
     * @return lista pageid (valori Long)
     */
    private static ArrayList<Long> creaListaBaseLongJson(JSONArray objArray, boolean nameSpaceBaseOnly) {
        ArrayList<Long> lista = null;
        JSONObject jsonObject = null;
        Object longPageid = null;
        long pageid = 0;
        Object longNs = null;
        long ns = 0;

        if (objArray != null && objArray.size() > 0) {
            lista = new ArrayList<Long>();
            for (Object obj : objArray) {
                if (obj instanceof JSONObject) {
                    jsonObject = (JSONObject) obj;

                    longPageid = jsonObject.get(PAGEID);
                    if (longPageid instanceof Long) {
                        pageid = ((Long) longPageid).intValue();
                    }// fine del blocco if

                    if (nameSpaceBaseOnly) {
                        longNs = jsonObject.get(NS);
                        if (longNs instanceof Long) {
                            ns = ((Long) longNs).intValue();
                            if (ns == 0) {
                                if (pageid > 0) {
                                    lista.add(pageid);
                                }// end of if cycle
                            }// end of if cycle
                        }// fine del blocco if
                    } else {
                        if (pageid > 0) {
                            lista.add(pageid);
                        }// end of if cycle
                    }// end of if/else cycle

                }// fine del blocco if
            } // fine del ciclo for-each
        }// fine del blocco if

        return lista;
    } // fine del metodo


    /**
     * Crea una lista di pagine (valori title) da un array di oggetti Json
     *
     * @param objArray array di oggetti Json
     *
     * @return lista title (valori String)
     */
    private static ArrayList<String> creaListaBaseTxtJson(JSONArray objArray) {
        return creaListaBaseTxtJson(objArray, false);
    } // fine del metodo


    /**
     * Crea una lista di pagine (valori title) da un array di oggetti Json
     *
     * @param objArray          array di oggetti Json
     * @param nameSpaceBaseOnly flag per selezionare solo il namespace=0
     *
     * @return lista title (valori String)
     */
    private static ArrayList<String> creaListaBaseTxtJson(JSONArray objArray, boolean nameSpaceBaseOnly) {
        ArrayList<String> lista = null;
        JSONObject jsonObject = null;
        Object stringTitle = null;
        String title = "";
        Object longNs = null;
        long ns = 0;

        if (objArray != null && objArray.size() > 0) {
            lista = new ArrayList<String>();
            for (Object obj : objArray) {
                if (obj instanceof JSONObject) {
                    jsonObject = (JSONObject) obj;

                    stringTitle = jsonObject.get(TITLE);
                    if (stringTitle instanceof String) {
                        title = stringTitle.toString();
                    }// fine del blocco if

                    if (nameSpaceBaseOnly) {
                        longNs = jsonObject.get(NS);
                        if (longNs instanceof Long) {
                            ns = ((Long) longNs).intValue();
                            if (ns == 0) {
                                if (!title.equals("")) {
                                    lista.add(title);
                                }// end of if cycle
                            }// end of if cycle
                        }// fine del blocco if
                    } else {
                        if (!title.equals("")) {
                            lista.add(title);
                        }// end of if cycle
                    }// end of if/else cycle

                }// fine del blocco if
            } // fine del ciclo for-each
        }// fine del blocco if

        return lista;
    } // fine del metodo


    /**
     * Crea una lista di pagine (valori pageids) dal testo JSON di una categoria
     *
     * @param textJSON in ingresso
     *
     * @return lista pageid (valori Long)
     *
     * @deprecated
     */
    public static ArrayList<Long> creaListaCatJson(String textJSON) {
        ArrayList<Long> lista = null;
        JSONArray objArray = creaArrayCatJson(textJSON);

        if (objArray != null) {
            lista = creaListaBaseLongJson(objArray);
        }// end of if cycle

        return lista;
    } // fine del metodo


    /**
     * Crea una lista di pagine (valori title) dal testo JSON di una categoria
     *
     * @param textJSON in ingresso
     *
     * @return lista title (valori String)
     *
     * @deprecated
     */
    public static ArrayList<String> creaListaCatTxtJson(String textJSON) {
        ArrayList<String> lista = null;
        JSONArray objArray = creaArrayCatJson(textJSON);

        if (objArray != null) {
            lista = creaListaBaseTxtJson(objArray);
        }// end of if cycle

        return lista;
    } // fine del metodo


    /**
     * Crea una lista di pagine back (valori pageids) dal testo JSON di una pagina puntata
     *
     * @param textJSON in ingresso
     *
     * @return lista pageid (valori Long)
     */
    public static ArrayList<Long> creaListaBackLongJson(String textJSON) {
        ArrayList<Long> lista = null;
        JSONArray objArray = creaArrayBackJson(textJSON);

        if (objArray != null) {
            lista = creaListaBaseLongJson(objArray);
        }// end of if cycle

        if (lista != null && lista.size() > 0) {
            return lista;
        } else {
            return null;
        }// end of if/else cycle
    } // fine del metodo


    /**
     * Crea una lista di pagine back (valori pageids) dal testo JSON di una pagina puntata
     *
     * @param textJSON in ingresso
     *
     * @return lista pageid (valori Long) solo delle pagine (namespace=0)
     */
    public static ArrayList<Long> creaListaBackLongVociJson(String textJSON) {
        ArrayList<Long> lista = null;
        JSONArray objArray = creaArrayBackJson(textJSON);

        if (objArray != null) {
            lista = creaListaBaseLongJson(objArray, true);
        }// end of if cycle

        if (lista != null && lista.size() > 0) {
            return lista;
        } else {
            return null;
        }// end of if/else cycle
    } // fine del metodo


    /**
     * Crea una lista di pagine back (valori title) dal testo JSON di una pagina puntata
     *
     * @param textJSON in ingresso
     *
     * @return lista title (valori String)
     */
    public static ArrayList<String> creaListaBackTxtJson(String textJSON) {
        ArrayList<String> lista = null;
        JSONArray objArray = creaArrayBackJson(textJSON);

        if (objArray != null) {
            lista = creaListaBaseTxtJson(objArray);
        }// end of if cycle

        if (lista != null && lista.size() > 0) {
            return lista;
        } else {
            return null;
        }// end of if/else cycle
    } // fine del metodo


    /**
     * Crea una lista di pagine back (valori title) dal testo JSON di una pagina puntata
     *
     * @param textJSON in ingresso
     *
     * @return lista title (valori String) solo delle pagine (namespace=0)
     */
    public static ArrayList<String> creaListaBackTxtVociJson(String textJSON) {
        ArrayList<String> lista = null;
        JSONArray objArray = creaArrayBackJson(textJSON);

        if (objArray != null) {
            lista = creaListaBaseTxtJson(objArray, true);
        }// end of if cycle

        if (lista != null && lista.size() > 0) {
            return lista;
        } else {
            return null;
        }// end of if/else cycle
    } // fine del metodo


    /**
     * Crea una lista di wrapper dal testo JSON di una pagina per le categorie
     *
     * @param textJSON in ingresso
     *
     * @return lista wrapper
     */
    public static ArrayList<WrapCat> creaListaWrapJson(String textJSON) {

        JSONObject allObj = (JSONObject) JSONValue.parse(textJSON);
        JSONObject queryObj = (JSONObject) allObj.get(QUERY);
        JSONArray catObj = (JSONArray) queryObj.get(CATEGORY_MEMBERS);

        return creaListaWrapJson(catObj);
    } // fine del metodo


    /**
     * Crea una lista di wrapper da un JSONArray per le categorie
     *
     * @param objArray in ingresso
     *
     * @return lista wrapper
     */
    private static ArrayList<WrapCat> creaListaWrapJson(JSONArray objArray) {
        ArrayList<WrapCat> lista = null;
        JSONObject jsonObject = null;
        long ns = 0;
        long pageid = 0L;
        String title = "";
        WrapCat wrap;

        if (objArray != null && objArray.size() > 0) {
            lista = new ArrayList<WrapCat>();
            for (Object obj : objArray) {
                if (obj instanceof JSONObject) {
                    jsonObject = (JSONObject) obj;

                    if (jsonObject.get(NS) != null && jsonObject.get(NS) instanceof Long) {
                        ns = (Long) jsonObject.get(NS);
                    }// fine del blocco if

                    if (jsonObject.get(PAGEID) != null && jsonObject.get(PAGEID) instanceof Long) {
                        pageid = (Long) jsonObject.get(PAGEID);
                    }// fine del blocco if

                    if (jsonObject.get(TITLE) != null && jsonObject.get(TITLE) instanceof String) {
                        title = (String) jsonObject.get(TITLE);
                    }// fine del blocco if

                    wrap = new WrapCat(ns, pageid, title);
                    lista.add(wrap);
                }// fine del blocco if
            } // fine del ciclo for-each
        }// fine del blocco if

        return lista;
    } // fine del metodo


    /**
     * Suddivide la lista di wrapper in quattro liste differerenziate
     * <p>
     * Dall'unica lista in ingresso, costruisce 4 liste divise per:
     * namespace 0/14 e title/pageid
     *
     * @param lista in ingresso
     *
     * @return mappa con le quattro liste
     */
    public static HashMap<String, ArrayList> getMappaWrap(ArrayList<WrapCat> lista) {
        HashMap<String, ArrayList> mappa = null;
        ArrayList<Long> listaVociPageid;
        ArrayList<String> listaVociTitle;
        ArrayList<Long> listaCatPageid;
        ArrayList<String> listaCatTitle;

        if (lista != null && lista.size() > 0) {
            mappa = new HashMap<String, ArrayList>();
            listaVociPageid = new ArrayList<Long>();
            listaVociTitle = new ArrayList<String>();
            listaCatPageid = new ArrayList<Long>();
            listaCatTitle = new ArrayList<String>();

            for (WrapCat wrap : lista) {
                if (wrap.getNs() == 0) {
                    listaVociPageid.add(wrap.getPageid());
                    listaVociTitle.add(wrap.getTitle());
                }// end of if cycle
                if (wrap.getNs() == 14) {
                    listaCatPageid.add(wrap.getPageid());
                    listaCatTitle.add(wrap.getTitle());
                }// end of if cycle
            }// end of for cycle

            mappa.put(KEY_VOCE_PAGEID, listaVociPageid);
            mappa.put(KEY_VOCE_TITLE, listaVociTitle);
            mappa.put(KEY_CAT_PAGEID, listaCatPageid);
            mappa.put(KEY_CAT_TITLE, listaCatTitle);
        }// end of if cycle

        return mappa;
    } // fine del metodo


    /**
     * Crea una lista di wrapper dal testo JSON di una pagina per le categorie
     *
     * @param textJSON in ingresso
     *
     * @return lista di wrapper categorie
     */
    public static ArrayList<WrapCat> getListaWrapCat(String textJSON) {
        ArrayList<WrapCat> lista = null;

        if (textJSON != null && !textJSON.equals("")) {
            lista = creaListaWrapJson(textJSON);
        }// end of if cycle

        return lista;
    } // fine del metodo


    /**
     * Crea una lista di wrapper dal testo JSON di una pagina per le categorie
     * <p>
     * Dal testo della pagina in ingresso, costruisce 4 liste divise per:
     * namespace 0/14 e title/pageid
     *
     * @param textJSON in ingresso
     *
     * @return mappa con le quattro liste
     */
    public static HashMap<String, ArrayList> getMappaWrap(String textJSON) {
        HashMap<String, ArrayList> mappa = null;
        ArrayList<WrapCat> lista = getListaWrapCat(textJSON);

        if (lista != null && lista.size() > 0) {
            mappa = getMappaWrap(lista);
        }// end of if cycle

        return mappa;
    } // fine del metodo


    /**
     * Crea una mappa del testo JSON
     *
     * @param textJSON in ingresso
     *
     * @return mappa base JSON
     */
    public static HashMap getMappaJSON(String textJSON) {
        HashMap mappaJSON = null;
        JSONObject objectAll;

        //--recupera i tre oggetti al livello root del testo (batchcomplete e warnings e query)
        objectAll = (JSONObject) JSONValue.parse(textJSON);

        //--controllo
        if (objectAll != null) {
            mappaJSON = new HashMap();
            for (Object key : objectAll.keySet()) {
                mappaJSON.put(key, objectAll.get(key));
            }// end of for cycle
        }// fine del blocco if

        return mappaJSON;
    } // fine del metodo


    /**
     * Estrae un componente dalla mappa del testo JSON
     *
     * @param textJSON in ingresso
     *
     * @return elemento della mappa JSON
     */
    public static HashMap getWarnings(String textJSON) {
        HashMap mappa = new HashMap();
        JSONObject jsonObject = null;
        HashMap mappaJSON = getMappaJSON(textJSON);
        JSONObject obj;
        Object value;

        if (mappaJSON != null) {
            if (mappaJSON.get(TipoJSON.warnings.name()) != null) {
                jsonObject = (JSONObject) mappaJSON.get(TipoJSON.warnings.name());
            }// end of if cycle
        }// end of if cycle

        if (jsonObject != null) {
            for (Object key : jsonObject.keySet()) {
                obj = (JSONObject) jsonObject.get(key);
                value = obj.get(TipoJSON.warnings.name());
                mappa.put(key, value);
            }// end of for cycle
        }// end of if cycle

        return mappa;
    } // fine del metodo


    /**
     * Restituisce il testo del warnings di tipo 'result' (se esiste)
     *
     * @param textJSON in ingresso
     *
     * @return testo del warnings
     */
    public static String getWarningResult(String textJSON) {
        String text = "";
        String tag = "result";
        HashMap mappa = getWarnings(textJSON);

        if (mappa != null && mappa.get(tag) != null) {
            text = (String) mappa.get(tag);
        }// end of if cycle

        return text;
    } // fine del metodo


    /**
     * Crea un array delle pagine wikimedia dal testo JSON di una risposta multiPagine action=query
     *
     * @param textJSON in ingresso
     *
     * @return array delle singole pagine
     */
    public static JSONArray getArrayPagesJSON(String textJSON) {
        JSONArray arrayPages = null;
        JSONObject objectAll;
        JSONObject objectQuery;

        //--recupera i due oggetti al livello root del testo (batchcomplete e query)
        objectAll = (JSONObject) JSONValue.parse(textJSON);

        //--controllo
        if (objectAll == null) {
            return null;
        }// fine del blocco if

        //--recupera i valori dei parametri pages
        if (objectAll.get(QUERY) != null && objectAll.get(QUERY) instanceof JSONObject) {
            objectQuery = (JSONObject) objectAll.get(QUERY);
            if (objectQuery.get(PAGES) != null && objectQuery.get(PAGES) instanceof JSONArray) {
                arrayPages = (JSONArray) objectQuery.get(PAGES);
            }// fine del blocco if
        }// fine del blocco if

        return arrayPages;
    } // fine del metodo


    /**
     * Crea una mappa standard (valori reali) da una singola page JSON di una multi-pagina action=query
     *
     * @param paginaJSON in ingresso
     *
     * @return mappa query (valori reali)
     */
    public static HashMap<String, Object> creaMappaJSON(JSONObject paginaJSON) {
        HashMap<String, Object> mappa = new HashMap<String, Object>();
        HashMap<String, Object> mappaRev;
        JSONArray arrayRev;
        String keyPage;
        Object value;

        if (paginaJSON == null) {
            return null;
        }// fine del blocco if

        //--recupera i valori dei parametri info
        for (PagePar par : PagePar.getRead()) {
            keyPage = par.toString();
            if (paginaJSON.get(keyPage) != null) {
                value = paginaJSON.get(keyPage);
                mappa.put(keyPage, value);
            }// fine del blocco if
        } // fine del ciclo for-each

        //--recupera i valori dei parametri revisions
        arrayRev = (JSONArray) paginaJSON.get(REVISIONS);
        if (arrayRev != null) {
            mappaRev = estraeMappaJsonPar(arrayRev);
            for (String key : mappaRev.keySet()) {
                value = mappaRev.get(key);
                mappa.put(key, value);
            } // fine del ciclo for-each
        }// end of if cycle

        return mappa;
    } // fine del metodo


    /**
     * Controlla se esiste un warnings nella risposta del server
     *
     * @param textJSON in ingresso
     *
     * @return true se il testo in ingresso contiene un warning
     */

    public static boolean isWarnings(String textJSON) {
        boolean status = false;

        JSONObject allObj = (JSONObject) JSONValue.parse(textJSON);
        JSONObject warningsObj = (JSONObject) allObj.get(WARNINGS);

        if (warningsObj != null) {
            status = true;
        }// fine del blocco if

        return status;
    } // fine del metodo


    /**
     * Estrae il valore del parametro continue dal testo JSON di una pagina
     *
     * @param textJSON in ingresso
     *
     * @return parametro continue
     */
    public static String creaCatContinue(String textJSON) {
        String textContinue = VUOTA;
        JSONObject allObj;
        JSONObject queryObj = null;

        allObj = (JSONObject) JSONValue.parse(textJSON);
        if (allObj != null && allObj.get(CONTINUE) instanceof JSONObject) {
            queryObj = (JSONObject) allObj.get(CONTINUE);
        }// fine del blocco if
        if (queryObj != null && queryObj.get(CM_CONTINUE) instanceof String) {
            textContinue = (String) queryObj.get(CM_CONTINUE);
        }// fine del blocco if

        return textContinue;
    } // fine del metodo

//    /**
//     * Estrae il valore del parametro continue dalla mappa
//     *
//     * @param text in ingresso
//     * @return parametro continue
//     */
//    private static String estraeContinue(LazyMap lazyMap) {
//        String valore=VUOTA;
//        String textContinue = VUOTA;
//        Object mappaValori;
//        String sep = "\\|";
//        Object parti;
//
//        mappaValori = lazyMap.values();
//        valore = mappaValori[0];
//        parti = valore.split(sep);
//        textContinue = parti[1];
//
//        //@todo rimetto il valore intero (e non le parti) perché così adesso funziona
//        return valore;
//    } // fine del metodo


    /**
     * Converte i typi di una mappa secondo i parametri PagePar
     * <p>
     * La mappa in ingresso contiene ns, pageid e title
     * Utilizzo solo il pageid (Integer)
     *
     * @param listaIn standard (valori String) in ingresso
     *
     * @return mappa typizzata secondo PagePar
     */
    private static ArrayList<Integer> converteListaCat(ArrayList listaIn) {
        ArrayList<Integer> lista = new ArrayList<Integer>();
        int value;

//        listaIn ?.each {
//            value = (int) it[PAGEID];
//            lista.add(value);
//        } // fine del ciclo each

        return lista;
    } // fine del metodo


    /**
     * Crea una stringa di testo, con tutti i valori della lista, separati dal pipe
     *
     * @param lista (valori Integer) in ingresso
     *
     * @return stringa di valori
     */
    public static String creaListaPageids(ArrayList<Long> lista) {
        String testo = VUOTA;
        String sep = "|";

        for (Long lungo : lista) {
            testo += lungo;
            testo += sep;
        } // fine del ciclo for-each
        testo = levaCoda(testo, sep);

        return testo;
    } // fine del metodo


    /**
     * Converte il valore stringa del timestamp in un timestamp
     * Formato: 2015-06-30T10:18:05Z
     *
     * @param timestampText in ingresso
     *
     * @return data in uscita
     */
    public static Timestamp convertTxtTime(String timestampText) {
        return new Timestamp(convertTxtData(timestampText).getTime());
    } // fine del metodo


    /**
     * Converte il valore stringa della data in una data
     * Formato: 2015-06-30T10:18:05Z
     *
     * @param dataTxt in ingresso
     *
     * @return data in uscita
     */
    public static Date convertTxtData(String dataTxt) {
        String zero = "0";
        String annoStr;
        String meseStr;
        String giornoStr;
        String oraStr;
        String minutoStr;
        String secondoStr;
        int anno = 0;
        int mese = 0;
        int giorno = 0;
        int ora = 0;
        int minuto = 0;
        int secondo = 0;

        annoStr = dataTxt.substring(0, 4);
        if (dataTxt.substring(5, 6).equals(zero)) {
            meseStr = dataTxt.substring(6, 7);
        } else {
            meseStr = dataTxt.substring(5, 7);
        }// fine del blocco if-else

        if (dataTxt.substring(8, 9).equals(zero)) {
            giornoStr = dataTxt.substring(9, 10);
        } else {
            giornoStr = dataTxt.substring(8, 10);
        }// fine del blocco if-else

        if (dataTxt.substring(11, 12).equals(zero)) {
            oraStr = dataTxt.substring(12, 13);
        } else {
            oraStr = dataTxt.substring(11, 13);
        }// fine del blocco if-else

        if (dataTxt.substring(14, 15).equals(zero)) {
            minutoStr = dataTxt.substring(15, 16);
        } else {
            minutoStr = dataTxt.substring(14, 16);
        }// fine del blocco if-else

        if (dataTxt.substring(17, 18).equals(zero)) {
            secondoStr = dataTxt.substring(18, 19);
        } else {
            secondoStr = dataTxt.substring(17, 19);
        }// fine del blocco if-else

        try { // prova ad eseguire il codice
            anno = Integer.decode(annoStr);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch    anno = Integer.decode(annoStr);
        try { // prova ad eseguire il codice
            mese = Integer.decode(meseStr);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch    anno = Integer.decode(annoStr);
        try { // prova ad eseguire il codice
            giorno = Integer.decode(giornoStr);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch    anno = Integer.decode(annoStr);
        try { // prova ad eseguire il codice
            ora = Integer.decode(oraStr);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        try { // prova ad eseguire il codice
            minuto = Integer.decode(minutoStr);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch    anno = Integer.decode(annoStr);
        try { // prova ad eseguire il codice
            secondo = Integer.decode(secondoStr);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch    anno = Integer.decode(annoStr);

        //--patch
        anno = anno - 1900;
        mese = mese - 1;

        return new Date(anno, mese, giorno, ora, minuto, secondo);
    }// fine del metodo


    private static String apici(String entrata) {
        return APICI + entrata + APICI;
    }// fine del metodo


    private static String graffe(String entrata) {
        return GRAFFA_INI + entrata + GRAFFA_END;
    }// fine del metodo


    /**
     * Converte il valore stringa nel tipo previsto dal parametro PagePar
     *
     * @param par     parametro PagePar in ingresso
     * @param valueIn in ingresso
     *
     * @return valore della classe corretta
     */
    private static Object fixValueMap(PagePar par, Object valueIn) {
        Object valueOut = null;
        PagePar.TypeField typo = par.getType();

        if (typo == PagePar.TypeField.string) {
            valueOut = valueIn;
        }// fine del blocco if

        if (typo == PagePar.TypeField.booleano) {
            valueOut = valueIn;
        }// fine del blocco if

        if (typo == PagePar.TypeField.longzero || typo == PagePar.TypeField.longnotzero) {
            if (valueIn instanceof String) {
                try { // prova ad eseguire il codice
                    valueOut = Long.decode((String) valueIn);
                } catch (Exception unErrore) { // intercetta l'errore
                }// fine del blocco try-catch
            }// fine del blocco if
            if (valueIn instanceof Integer) {
                try { // prova ad eseguire il codice
                    valueOut = new Long(valueIn.toString());
                } catch (Exception unErrore) { // intercetta l'errore
                }// fine del blocco try-catch
            }// fine del blocco if
            if (valueIn instanceof Long) {
                valueOut = valueIn;
            }// fine del blocco if
        }// fine del blocco if

        if (typo == PagePar.TypeField.date) {
            if (valueIn instanceof String) {
                try { // prova ad eseguire il codice
                    valueOut = convertTxtData((String) valueIn);
                } catch (Exception unErrore) { // intercetta l'errore
                    valueOut = DATA_NULLA;
                }// fine del blocco try-catch
            }// fine del blocco if
            if (valueIn instanceof Date) {
                valueOut = valueIn;
            }// fine del blocco if
        }// fine del blocco if

        if (typo == PagePar.TypeField.timestamp) {
            if (valueIn instanceof String) {
                try { // prova ad eseguire il codice
                    valueOut = convertTxtTime((String) valueIn);
                } catch (Exception unErrore) { // intercetta l'errore
                    valueOut = DATA_NULLA;
                }// fine del blocco try-catch
            }// fine del blocco if
            if (valueIn instanceof Timestamp) {
                valueOut = valueIn;
            }// fine del blocco if
        }// fine del blocco if

        return valueOut;
    } // fine del metodo


    /**
     * Converte il valore stringa nel tipo previsto dal parametro PagePar
     *
     * @param key     del parametro PagePar in ingresso
     * @param valueIn in ingresso
     *
     * @return valore della classe corretta
     */
    private static Object fixValueMap(String key, Object valueIn) {
        return fixValueMap(PagePar.getPar(key), valueIn);
    } // fine del metodo


    /**
     * Differenza tra due array
     *
     * @param primo   array
     * @param secondo array
     *
     * @return differenza
     */
    public static ArrayList delta(ArrayList primo, ArrayList secondo) {
        ArrayList differenza = null;

        if (primo != null && secondo != null) {
            differenza = new ArrayList();
            for (Object value : primo) {
                if (!secondo.contains(value)) {
                    differenza.add(value);
                }// fine del blocco if
            } // fine del ciclo for-each
        }// fine del blocco if

        return differenza;
    } // fine del metodo


    /**
     * Regola i parametri della tavola in base alla mappa letta dal server
     * Aggiunge le date di riferimento lettura/scrittura
     */
    public static Wiki fixMappa(Wiki wiki, HashMap mappa) {
        List<PagePar> lista = PagePar.getDB();
        String key;
        Object value;

        for (PagePar par : lista) {
            key = par.toString();
            value = null;

            if (mappa.get(key) != null) {
                value = mappa.get(key);
            }// fine del blocco if

            //--controllo dei LONG che POSSONO essere anche zero
            if (par.getType() == PagePar.TypeField.longzero) {
                if (value == null) {
                    value = 0;
                }// fine del blocco if
            }// fine del blocco if

            //--patch
            if (par == PagePar.comment) {
                if (value instanceof String) {
                    if (((String) value).startsWith("[[WP:OA|←]]")) {
                        value = "Nuova pagina";
                    }// fine del blocco if
                }// fine del blocco if
            }// fine del blocco if

            par.setWiki(wiki, value);
        } // fine del ciclo for-each

        return wiki;
    }// end of method


    /**
     * Legge dal server wiki
     * Registra la tavola WikiBio
     * <p>
     *
     * @param title della pagina
     */
    public static void download(String title) {
        Page pagina = Api.leggePage(title);
        download(pagina);
    }// end of method


    /**
     * Legge dal server wiki
     * Registra la tavola  WikiBio
     * <p>
     *
     * @param pagina dal server
     */
    public static void download(Page pagina) {
        HashMap mappa;
        Wiki wiki;
        String testoVoce;
        String tmplBio;

        testoVoce = pagina.getText();
        tmplBio = Api.estraeTmplBio(testoVoce);

        if (tmplBio != null) {
            wiki = new Wiki();
            mappa = pagina.getMappaDB();
            wiki = fixMappa(wiki, mappa);

            try { // prova ad eseguire il codice
//                wiki.save();
            } catch (Exception unErrore) { // intercetta l'errore
                //@todo inserire log e logger
//                (new Notification("ATTENZIONE", "La pagina " + pagina.getTitle() + " non è stata registrata", Notification.TYPE_WARNING_MESSAGE, true)).show(com.vaadin.server.Page.getCurrent());
            }// fine del blocco try-catch
        }// fine del blocco if
    }// end of method


    /**
     * Restituisce un Timestamp attuale
     */
    public static Timestamp getTime() {
        return new Timestamp(new Date().getTime());
    }// end of method


    /**
     * Elimina (eventuali) doppie graffe in testa e coda della stringa.
     * Funziona solo se le graffe sono esattamente in TESTA ed in CODA alla stringa
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     *
     * @param stringaIn in ingresso
     *
     * @return stringa con doppie graffe eliminate
     */
    public static String setNoGraffe(String stringaIn) {
        String stringaOut = stringaIn;

        if (stringaIn != null && stringaIn.length() > 0) {
            stringaOut = stringaIn.trim();
            stringaOut = levaTesta(stringaOut, GRAFFA_INI);
            stringaOut = levaTesta(stringaOut, GRAFFA_INI);
            stringaOut = levaCoda(stringaOut, GRAFFA_END);
            stringaOut = levaCoda(stringaOut, GRAFFA_END);
        }// fine del blocco if

        return stringaOut.trim();
    } // fine del metodo


    /**
     * Elimina (eventuali) doppie quadre in testa e coda della stringa.
     * Funziona solo se le quadre sono esattamente in TESTA ed in CODA alla stringa
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     *
     * @param stringaIn in ingresso
     *
     * @return stringa con doppie quadre eliminate
     */
    public static String setNoQuadre(String stringaIn) {
        String stringaOut = stringaIn;

        if (stringaIn != null && stringaIn.length() > 0) {
            stringaOut = stringaIn.trim();
            stringaOut = levaTesta(stringaOut, QUADRA_INI);
            stringaOut = levaTesta(stringaOut, QUADRA_INI);
            stringaOut = levaCoda(stringaOut, QUADRA_END);
            stringaOut = levaCoda(stringaOut, QUADRA_END);
        }// fine del blocco if

        return stringaOut.trim();
    } // fine del metodo


    /**
     * Elimina (eventuali) tripli apici (grassetto) in testa e coda della stringa.
     * Funziona solo se gli apici sono esattamente in TESTA ed in CODA alla stringa
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     *
     * @param stringaIn in ingresso
     *
     * @return stringa con tripli apici eliminati
     */
    public static String setNoBold(String stringaIn) {
        String stringaOut = stringaIn;

        if (stringaIn != null && stringaIn.length() > 0) {
            stringaOut = stringaIn.trim();
            stringaOut = levaTesta(stringaOut, BOLD);
            stringaOut = levaCoda(stringaOut, BOLD);
        }// fine del blocco if

        return stringaOut.trim();
    } // fine del metodo


    /**
     * Elimina (eventuali) virgolette in testa e coda della stringa.
     * Elimina spazi vuoti iniziali e finali
     *
     * @param stringaIn in ingresso
     *
     * @return stringa con virgolette eliminate
     */
    public static String setNoVirgolette(String stringaIn) {
        String stringaOut = stringaIn;
        String tag = "\"";

        if (stringaIn != null && stringaIn.length() > 0) {
            stringaOut = stringaIn.trim();
            stringaOut = levaTesta(stringaOut, tag);
            stringaOut = levaCoda(stringaOut, tag);
        }// fine del blocco if

        return stringaOut.trim();
    } // fine del metodo


    /**
     * Elimina (eventuali) doppi uguali (paragrafo) in testa e coda della stringa.
     * Funziona solo se i doppi uguali sono esattamente in TESTA ed in CODA alla stringa
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     *
     * @param stringaIn in ingresso
     *
     * @return stringa con doppi uguali eliminati
     */
    public static String setNoParagrafo(String stringaIn) {
        String stringaOut = stringaIn;

        if (stringaIn != null && stringaIn.length() > 0) {
            stringaOut = stringaIn.trim();
            stringaOut = levaTesta(stringaOut, PARAGRAFO);
            stringaOut = levaCoda(stringaOut, PARAGRAFO);
        }// fine del blocco if

        return stringaOut.trim();
    } // fine del metodo


    /**
     * Aggiunge doppie graffe in testa e coda alla stringa.
     * Aggiunge SOLO se gia non esistono (ne doppie, ne singole)
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     * Elimina eventuali graffe già presenti, per evitare di metterle doppi
     *
     * @param stringaIn in ingresso
     *
     * @return stringa con doppie graffe aggiunte
     */
    public static String setGraffe(String stringaIn) {
        String stringaOut = stringaIn;
        String stringaPulita;

        if (stringaIn != null && stringaIn.length() > 0) {
            stringaPulita = setNoGraffe(stringaIn);
            if (!stringaPulita.equals("")) {
                stringaOut = GRAFFE_INI + stringaPulita + GRAFFE_END;
            }// fine del blocco if-else
        }// fine del blocco if

        return stringaOut.trim();
    } // fine del metodo


    /**
     * Aggiunge doppie quadre in testa e coda alla stringa.
     * Aggiunge SOLO se gia non esistono (ne doppie, ne singole)
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     * Elimina eventuali quadre già presenti, per evitare di metterle doppi
     *
     * @param stringaIn in ingresso
     *
     * @return stringa con doppie quadre aggiunte
     */
    public static String setQuadre(String stringaIn) {
        String stringaOut = stringaIn;
        String stringaPulita;

        if (stringaIn != null && stringaIn.length() > 0) {
            stringaPulita = setNoQuadre(stringaIn);
            if (!stringaPulita.equals("")) {
                stringaOut = QUADRE_INI + stringaPulita + QUADRE_END;
            }// fine del blocco if-else
        }// fine del blocco if

        return stringaOut.trim();
    } // fine del metodo


    /**
     * Aggiunge doppie quadre in testa e coda alla stringa.
     * Aggiunge SOLO se gia non esistono (ne doppie, ne singole)
     * Se arriva una stringa vuota, restituisce una stringa vuota
     *
     * @param stringaIn in ingresso
     *
     * @return stringa con doppie quadre aggiunte
     */
    public static String setQuadreSpazio(String stringaIn) {
        String stringaOut = stringaIn;

        if (stringaIn != null && stringaIn.length() > 0) {
            if (!stringaOut.equals("")) {
                stringaOut = QUADRE_INI + stringaOut + QUADRE_END;
            }// fine del blocco if-else
        }// fine del blocco if

        return stringaOut;
    } // fine del metodo


    /**
     * Aggiunge singola parentesi tonda in testa e coda alla stringa.
     *
     * @param stringaIn in ingresso
     *
     * @return stringa con parentesi tonde aggiunte
     */
    public static String setTonde(String stringaIn) {
        String stringaOut = stringaIn;

        if (stringaIn != null && stringaIn.length() > 0) {
            stringaOut = TONDA_INI + stringaIn + TONDA_END;
        }// fine del blocco if

        return stringaOut.trim();
    } // fine del metodo


    /**
     * Aggiunge tag 'nowiki' in testa e coda alla stringa.
     *
     * @param stringaIn in ingresso
     *
     * @return stringa coi tag aggiunti
     */
    public static String setNowiki(String stringaIn) {
        String stringaOut = "";

        if (stringaIn != null && stringaIn.length() > 0) {
            stringaOut = NO_WIKI_INI + stringaIn.trim() + NO_WIKI_END;
        }// fine del blocco if

        return stringaOut.trim();
    } // fine del metodo


    /**
     * Aggiunge doppie quadre in testa e coda alla stringa.
     * Aggiunge SOLO se gia non esistono (ne doppie, ne singole)
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     * Elimina eventuali quadre già presenti, per evitare di metterle doppi
     *
     * @param paginaWiki da linkare
     *
     * @return stringa con doppie quadre aggiunte
     */
    public static String setLink(String paginaWiki) {
        return setQuadre(paginaWiki);
    } // fine del metodo


    /**
     * Aggiunge il PIPE intermedio e le doppie quadre in testa ed in coda alla stringa. <br>
     * Se è nullo 'paginaWiki', non aggiunge il PIPE e restituisce solo nomeVisibile <br>
     *
     * @param paginaWiki   da linkare
     * @param nomeVisibile da mostrare
     *
     * @return stringa con PIPE intermedio e doppie quadre aggiunte
     */
    public static String setLink(String paginaWiki, String nomeVisibile) {
        if (paginaWiki != null && paginaWiki.length() > 0) {
            return setQuadre(paginaWiki + PIPE + nomeVisibile);
        } else {
            return setQuadre(nomeVisibile);
        }// end of if/else cycle
    } // fine del metodo


    /**
     * Aggiunge tripli apici (grassetto) in testa ed in coda alla stringa.
     * Aggiunge SOLO se gia non esistono
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     * Elimina eventuali apici già presenti, per evitare di metterli doppi
     *
     * @param stringaIn in ingresso
     *
     * @return stringa con tripli apici aggiunte
     */
    public static String setBold(String stringaIn) {
        String stringaOut = stringaIn;
        String stringaPulita;

        if (stringaIn != null && stringaIn.length() > 0) {
            stringaPulita = setNoBold(stringaIn);
            if (!stringaPulita.equals("")) {
                stringaOut = BOLD + stringaPulita + BOLD;
            }// fine del blocco if-else
        }// fine del blocco if

        return stringaOut.trim();
    } // fine del metodo


    /**
     * Aggiunge tripli apici (grassetto) in testa ed in coda alla stringa.
     * Aggiunge doppie quadre in testa e coda alla stringa.
     *
     * @param stringaIn in ingresso
     *
     * @return stringa con tripli apici aggiunte
     */
    public static String setQuadreBold(String stringaIn) {
        String stringaOut = stringaIn;

        if (stringaIn != null && stringaIn.length() > 0) {
            stringaOut = setQuadre(stringaOut);
            stringaOut = setBold(stringaOut);
        }// fine del blocco if

        return stringaOut.trim();
    } // fine del metodo


    /**
     * Aggiunge tripli apici (grassetto) in testa ed in coda al numero.
     *
     * @param numero in ingresso
     *
     * @return stringa con tripli apici aggiunte
     */
    public static String setBold(Number numero) {
        return setBold(numero + "");
    } // fine del metodo


    /**
     * Aggiunge doppi uguali in testa e coda alla stringa per creare un paragrafo.
     * Aggiunge SOLO se gia non esistono (ne doppie, ne singole)
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     * Elimina eventuali uguali già presenti, per evitare di metterle doppi
     *
     * @param stringaIn in ingresso
     *
     * @return stringa con doppi uguali aggiunti
     */
    public static String setParagrafo(String stringaIn) {
        return setParagrafo(stringaIn, 0);
    } // fine del metodo


    /**
     * Aggiunge doppi uguali in testa e coda alla stringa per creare un paragrafo.
     * Aggiunge SOLO se gia non esistono (ne doppie, ne singole)
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     * Elimina eventuali uguali già presenti, per evitare di metterle doppi
     *
     * @param stringaIn in ingresso
     * @param numVoci   nel paragrafo
     *
     * @return stringa con doppi uguali aggiunti
     */
    public static String setParagrafo(String stringaIn, int numVoci) {
        String stringaOut = "";
        String stringaPulita;

        if (stringaIn != null && stringaIn.length() > 0) {
            stringaPulita = setNoParagrafo(stringaIn);
            if (!stringaPulita.equals("")) {
                if (numVoci > 0) {
                    stringaPulita += " <small><small>(" + numVoci + ")</small></small>";
                }// end of if cycle
                stringaOut = PARAGRAFO + stringaPulita + PARAGRAFO;
            }// fine del blocco if-else
        }// fine del blocco if

        return stringaOut.trim();
    } // fine del metodo


    /**
     * Aggiunge il tag ref in testa ed in coda alla stringa
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     *
     * @param stringaIn in ingresso
     *
     * @return stringa con i tag ref iniziale e finale
     */
    public static String setRef(String stringaIn) {
        String stringaOut = stringaIn;

        if (stringaIn != null && stringaIn.length() > 0) {
            stringaOut = REF_INI + stringaOut + REF_END;
        }// fine del blocco if

        return stringaOut.trim();
    } // fine del metodo


    /**
     * Aggiunge il 'col' in testa ed in coda alla stringa
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     *
     * @param stringaIn in ingresso
     *
     * @return stringa con i tag col iniziale e finale
     */
    public static String setColonne(String stringaIn) {
        String stringaOut = stringaIn;

        if (stringaIn != null && stringaIn.length() > 0) {
            stringaOut = setGraffe(COL_INI) + A_CAPO + stringaOut + A_CAPO + setGraffe(COL_END);
        }// fine del blocco if

        return stringaOut.trim();
    } // fine del metodo


    /**
     * Costruisce il tag del portale
     *
     * @param nomePortale in ingresso
     *
     * @return stringa con la categoria
     */
    public static String setPortale(String nomePortale) {
        String testo = VUOTA;
        String tag = "Portale";

        testo += tag;
        testo += PIPE;
        testo += nomePortale;
        testo = setGraffe(testo);
        testo += A_CAPO;

        return testo;
    } // fine del metodo


    /**
     * Costruisce il tag della categoria
     *
     * @param nomeCategoria in ingresso
     *
     * @return stringa con la categoria
     */
    public static String setCat(String nomeCategoria) {
        return setCat(nomeCategoria, "");
    } // fine del metodo


    /**
     * Costruisce il tag della categoria
     *
     * @param nomeCategoria in ingresso
     *
     * @return stringa con la categoria
     */
    public static String setCat(String nomeCategoria, String ordine) {
        return setCat(nomeCategoria, ordine, false);
    } // fine del metodo


    /**
     * Costruisce il tag della categoria
     *
     * @param nomeCategoria in ingresso
     * @param ordine        di presentazione della categoria
     * @param nascosta
     *
     * @return stringa con la categoria
     */
    public static String setCat(String nomeCategoria, String ordine, boolean nascosta) {
        String testo = VUOTA;
        String tagNascosta = nascosta ? ":" : "";
        String tag = tagNascosta + "Categoria:";

        testo += tag;
        testo += nomeCategoria;
        if (ordine != null && ordine.length() > 0) {
            testo += PIPE;
            testo += ordine;
        }// end of if cycle
        testo = setQuadreSpazio(testo);
        testo += A_CAPO;

        return testo;
    } // fine del metodo


    /**
     * Crea una lista standard (valori String) dewlle chiavi del testo JSON di una pagina
     *
     * @param textJSON in ingresso
     *
     * @return lista standard di chiavi (valori String)
     */
    public static ArrayList<String> creaListaKeys(String textJSON) {
        ArrayList<String> listaKeys = null;
        JSONObject objectAll = null;

        //--recupera gli oggetti al livello root del testo
        if (textJSON != null && !textJSON.equals("")) {
            objectAll = (JSONObject) JSONValue.parse(textJSON);
        }// end of if cycle

        if (objectAll != null) {
//            listaKeys = LibArray.getKeyFromMap(objectAll);
        }// end of if cycle

        return listaKeys;
    } // fine del metodo


    /**
     * Sommario standard in funzione della versione
     *
     * @return stringa oggetto della modifica
     */
    public static String getSummary() {
        String summary = "";
        String name = "";
//        WikiLogin wikiLogin = (WikiLogin) LibSession.getAttribute(WikiLogin.WIKI_LOGIN_KEY_IN_SESSION);

//        if (wikiLogin == null) {
//            wikiLogin = VaadApp.WIKI_LOGIN;
//        }// end of if cycle

//        if (wikiLogin != null) {
//            name = wikiLogin.getLgusername();
//        }// end of if cycle

        if (name == null || name.equals("")) {
            name = NOME_BOT;
        }// end of if cycle

        summary = "[[Utente:" + name + "|" + name + "]]";
        return summary;
    } // fine del metodo


    /**
     * Sommario standard in funzione della versione
     *
     * @return stringa oggetto della modifica
     */
    public static String getSummary(String dettaglio) {
        return getSummary() + " " + dettaglio;
    } // fine del metodo


    /**
     * Legge il modulo dal testo della pagina
     *
     * @param testo da cui recuperare il modulo
     *
     * @return testo del modulo
     */
    public static String estraeTestoModulo(String testo) {
        String testoModulo = "";
        String tagIni = "return {";
        String tagEnd = "}";
        int posIni = 0;
        int posEnd = 0;

        if (!testo.equals("")) {
            posIni = testo.indexOf(tagIni);
            posIni += tagIni.length();
            posEnd = testo.lastIndexOf(tagEnd);
        }// fine del blocco if

        if ((posIni != -1 && posEnd != -1)) {
            testoModulo = testo.substring(posIni, posEnd);
        }// fine del blocco if

        return testoModulo.trim();
    } // fine del metodo


    /**
     * Legge il modulo dalla pagina
     *
     * @param titolo della pagina da cui recuperare il modulo
     *
     * @return testo del modulo
     */
    public static String leggeModulo(String titolo) {
        String testoModulo = "";
        String testoPagina;

        if (!titolo.equals("")) {
            testoPagina = Api.leggeVoce(titolo);
            if (!testoPagina.equals("")) {
                testoModulo = estraeTestoModulo(testoPagina);
            }// fine del blocco if
        }// fine del blocco if

        return testoModulo.trim();
    } // fine del metodo


    /**
     * Legge la mappa del modulo della pagina
     *
     * @param titolo della pagina da cui recuperare la mappa
     *
     * @return mappa chiave/valore dei campi singolare/plurale
     */
    public static LinkedHashMap<String, Object> leggeMappaModulo(String titolo) {
        LinkedHashMap<String, Object> mappa = null;
        String testoModulo = "";
        String[] righe;
        String chiave;
        String valore;
        String tagUgu = "=";
        String[] partiDellaRiga;
        String[] partiDelValore;
        ArrayList arrayValori;

        if (!titolo.equals("")) {
            testoModulo = leggeModulo(titolo);
        }// fine del blocco if

        if (!testoModulo.equals("")) {
            mappa = new LinkedHashMap<String, Object>();
            righe = testoModulo.split("\n");
            for (String riga : righe) {
                partiDellaRiga = riga.split(tagUgu);
                if (partiDellaRiga.length == 2) {
                    chiave = partiDellaRiga[0].trim();
//                    chiave = LibText.levaTesta(chiave, "[");
//                    chiave = LibText.levaCoda(chiave, "]");
//                    chiave = LibText.levaTesta(chiave, "\"");
//                    chiave = LibText.levaCoda(chiave, "\"");
//                    valore = partiDellaRiga[1];
//                    valore = LibText.levaCoda(valore, ",");
//                    if (valore.contains(",")) {
//                        arrayValori = new ArrayList();
//                        partiDelValore = valore.split(",");
//                        for (String stringa : partiDelValore) {
//                            stringa = LibText.levaTesta(stringa, "{");
//                            stringa = LibText.levaCoda(stringa, "}");
//                            stringa = LibText.levaTesta(stringa, "\"");
//                            stringa = LibText.levaCoda(stringa, "\"");
//                            arrayValori.add(stringa);
//                        }// end of for cycle
//                        mappa.put(chiave, arrayValori);
//                    } else {
//                        valore = LibText.levaTesta(valore, "\"");
//                        valore = LibText.levaCoda(valore, "\"");
//                        mappa.put(chiave, valore);
//                    }// end of if/else cycle
                }// fine del blocco if
            }// end of for cycle
        }// fine del blocco if

        return mappa;
    } // fine del metodo


    /**
     * Suddivide la lista in due colonne.
     *
     * @param testoIn in ingresso
     *
     * @return listaOut in uscita
     */
    public static String listaDueColonne(String testoIn) {
        String testoOut = "";

        if (!testoIn.equals("")) {
            testoOut = "{{Div col}}";
            testoOut += A_CAPO;
            testoOut += testoIn.trim();
            testoOut += A_CAPO;
            testoOut += "{{Div col end}}";
        }// fine del blocco if

        return testoOut;
    }// fine della closure


    /**
     * Crea una table di classe wikitable
     * <p>
     * default:
     * width=50%
     * align=center
     * text-align=right
     * font-size=100%
     * background:#FFF
     * bgcolor="#EFEFEF"
     * <p>
     * Facoltativi:
     * Cost.KEY_MAPPA_LISTA_SORTABLE
     * Cost.KEY_MAPPA_CAPTION
     * Cost.KEY_MAPPA_TITOLI_SCURI
     * <p>
     * I numeri sono sempre allineati a destra
     * Sono formattati se la colonna NON è sortable
     *
     * @param mappa con i vari parametri e valori
     *
     * @return testo
     */
    public static String creaTable(HashMap mappa) {
        String testo = VUOTA;
        String header;
        String titoli = VUOTA;
        String body = VUOTA;
        String bottom;

        // controllo esistenza della mappa
        if (mappa == null) {
            return "";
        }// fine del blocco if

        if (mappa.size() < 1) {
            return "";
        }// fine del blocco if

        // controllo congruità base della mappa
        if (!isTableValida(mappa)) {
            return testo;
        }// fine del blocco if

        // inizio tabella
        header = creaTableHeader(mappa);
        header += A_CAPO;

        // titoli
        titoli = creaTableTitoli(mappa);
        titoli += A_CAPO;

        // body
        body = creaTableBody(mappa);
        body += A_CAPO;

        // chiusura
        bottom = "|}";

        // impacchetta tutto
        testo = header + titoli + body + bottom;

        return testo.trim();
    }// fine del metodo


    /**
     * Controllo della mappa per la costruzione della table
     * <p>
     * Obbligatori:
     * Cost.KEY_MAPPA_TITOLI
     * Cost.KEY_MAPPA_LISTA
     *
     * @param mappa con i vari parametri e valori
     *
     * @return testo
     */
    @SuppressWarnings("all")
    private static boolean isTableValida(HashMap mappa) {
        ArrayList<String> listaTitoli = getTitoli(mappa);
        ArrayList<ArrayList> listaRighe = getRighe(mappa);
        int numColonneTitoli = 0;
        int numColonneRighe = 0;
        ArrayList<String> primaRiga = null;

        // controllo parametri essenziali della mappa
        if (listaTitoli != null && listaTitoli.size() > 0) {
            numColonneTitoli = listaTitoli.size();
        } else {
            return false;
        }// end of if/else cycle

        // controllo congruità base della mappa
        if (listaRighe != null && listaRighe.size() > 0) {
            if (listaRighe.size() > 0) {
                if (listaRighe.get(0) instanceof ArrayList) {
                    numColonneRighe = listaRighe.get(0).size();
                } else {
                    numColonneRighe = 1;
                }// end of if/else cycle
            }// fine del blocco if
        } else {
            return true;
        }// end of if/else cycle

        if (numColonneTitoli != numColonneRighe) {
            return false;
        }// fine del blocco if

        return true;
    }// fine del metodo


    private static String creaTableHeader(HashMap mappa) {
        String caption = VUOTA;
        String header;
        boolean sortable = getTableSortable(mappa);
        boolean coloriScuri = true;
        String tagTitolo = "";
        String bgColorBody = GRIGIO_SCURO;

        // controllo parametri della mappa
        if (mappa.get(Cost.KEY_MAPPA_CAPTION) instanceof String) {
            caption = (String) mappa.get(Cost.KEY_MAPPA_CAPTION);
        }// fine del blocco if
        if (mappa.get(Cost.KEY_MAPPA_TITOLI_SCURI) instanceof Boolean) {
            coloriScuri = (Boolean) mappa.get(Cost.KEY_MAPPA_TITOLI_SCURI);
        }// fine del blocco if

        // inizio tabella
        if (sortable) {
            header = "{|class=\"wikitable sortable\"";
        } else {
            header = "{|class=\"wikitable\"";
        }// fine del blocco if-else

        if (coloriScuri) {
            header += SPAZIO;
            header += bgColorBody;
        }// fine del blocco if

        if (!caption.equals("")) {
            header += A_CAPO;
            header += tagTitolo + caption;
        }// fine del blocco if

        return header;
    }// fine del metodo


    //    @SuppressWarnings("all")
    private static String creaTableTitoli(HashMap mappa) {
        String titoli = VUOTA;
        ArrayList<String> listaTitoli = getTitoli(mappa);
        ArrayList<Boolean> listaColonneSort = getColonneSort(mappa);
        boolean numerazioneProgressiva = getNumerazioneProgressiva(mappa);
        String titoloColonnaProgressivo = "#";
        boolean coloriScuri = true;

//        if (mappa[MAPPA_NUMERAZIONE_PROGRESSIVA] in Boolean){
//            numerazioneProgressiva = mappa[MAPPA_NUMERAZIONE_PROGRESSIVA]
//        }// fine del blocco if
//
//        if (mappa[MAPPA_TITOLI_SCURI] in Boolean){
//            coloriScuri = mappa[MAPPA_TITOLI_SCURI]
//        }// fine del blocco if

        if (numerazioneProgressiva) {
            titoli += creaTableSingoloTitolo(mappa, titoloColonnaProgressivo, getTableSortable(mappa), coloriScuri);
        }// fine del blocco if

        for (int k = 0; k < listaTitoli.size(); k++) {
            if (listaColonneSort != null && listaColonneSort.size() > 0) {
                titoli += creaTableSingoloTitolo(mappa, listaTitoli.get(k), listaColonneSort.get(k), coloriScuri);
            }// end of if cycle
        }// end of for cycle

//        for (String titolo : listaTitoli) {
//            titoli += creaTableSingoloTitolo(mappa, titolo, true, coloriScuri);
//        }// end of for cycle

        return titoli.trim();
    }// fine del metodo


    @SuppressWarnings("all")
    private static ArrayList<String> getTitoli(HashMap mappa) {
        ArrayList<String> listaTitoli = null;

        if (mappa.get(Cost.KEY_MAPPA_TITOLI) != null) {
            if (mappa.get(Cost.KEY_MAPPA_TITOLI) instanceof ArrayList) {
                listaTitoli = (ArrayList) mappa.get(Cost.KEY_MAPPA_TITOLI);
            }// fine del blocco if
            if (mappa.get(Cost.KEY_MAPPA_TITOLI) instanceof String) {
//                listaTitoli = LibArray.fromStringaVirgola((String) mappa.get(Cost.KEY_MAPPA_TITOLI));
            }// fine del blocco if
        }// fine del blocco if

        return listaTitoli;
    }// fine del metodo


    @SuppressWarnings("all")
    private static ArrayList<ArrayList> getRighe(HashMap mappa) {
        ArrayList<ArrayList> listaRighe = null;
        ArrayList listaTmp = null;
        ArrayList listaSingolaRiga = null;
        Object obj = null;

        if (mappa.get(Cost.KEY_MAPPA_RIGHE_LISTA) != null) {
            if (mappa.get(Cost.KEY_MAPPA_RIGHE_LISTA) instanceof ArrayList) {
                listaTmp = (ArrayList) mappa.get(Cost.KEY_MAPPA_RIGHE_LISTA);
            }// fine del blocco if
        }// fine del blocco if

        if (listaTmp != null && listaTmp.size() > 0) {
            if (!(listaTmp.get(0) instanceof ArrayList)) {
                listaRighe = new ArrayList();
                for (int k = 0; k < listaTmp.size(); k++) {
                    obj = listaTmp.get(k);
                    listaSingolaRiga = new ArrayList();
                    listaSingolaRiga.add(obj);
                    listaRighe.add(listaSingolaRiga);
                }// end of for cycle
            } else {
                listaRighe = listaTmp;
            }// end of if/else cycle
        }// end of if cycle

        return listaRighe;
    }// fine del metodo


    @SuppressWarnings("all")
    private static boolean getNumerazioneProgressiva(HashMap mappa) {
        boolean numerazioneProgressiva = true;

        if (mappa.get(Cost.KEY_MAPPA_NUMERAZIONE_PROGRESSIVA) != null) {
            if (mappa.get(Cost.KEY_MAPPA_NUMERAZIONE_PROGRESSIVA) instanceof Boolean) {
                numerazioneProgressiva = (Boolean) mappa.get(Cost.KEY_MAPPA_NUMERAZIONE_PROGRESSIVA);
            }// fine del blocco if
        }// fine del blocco if

        return numerazioneProgressiva;
    }// fine del metodo


    @SuppressWarnings("all")
    private static boolean getTableSortable(HashMap mappa) {
        boolean tableSortable = false;

        if (mappa.get(Cost.KEY_MAPPA_SORTABLE_BOOLEAN) != null) {
            if (mappa.get(Cost.KEY_MAPPA_SORTABLE_BOOLEAN) instanceof Boolean) {
                tableSortable = (Boolean) mappa.get(Cost.KEY_MAPPA_SORTABLE_BOOLEAN);
            }// fine del blocco if
        } else {
            if (mappa.get(Cost.KEY_MAPPA_SORTABLE_LISTA) != null) {
                tableSortable = true;
            }// end of if cycle
        }// end of if/else cycle

        return tableSortable;
    }// fine del metodo


    @SuppressWarnings("all")
    private static ArrayList<Boolean> getColonneSort(HashMap mappa) {
        ArrayList<Boolean> listaColonneSort = null;
        boolean tableSortable = getTableSortable(mappa);

        if (mappa.get(Cost.KEY_MAPPA_SORTABLE_LISTA) != null) {
            if (mappa.get(Cost.KEY_MAPPA_SORTABLE_LISTA) instanceof ArrayList) {
                listaColonneSort = (ArrayList) mappa.get(Cost.KEY_MAPPA_SORTABLE_LISTA);
            }// fine del blocco if
        } else {
            listaColonneSort = new ArrayList<Boolean>();
            for (String stringa : getTitoli(mappa)) {
                listaColonneSort.add(tableSortable);
            }// end of for cycle
        }// end of if/else cycle

        return listaColonneSort;
    }// fine del metodo


    @SuppressWarnings("all")
    private static ArrayList<Boolean> getColonneDestra(HashMap mappa) {
        ArrayList<Boolean> listaColonneDestra = null;

        if (mappa.get(Cost.KEY_MAPPA_DESTRA_LISTA) != null) {
            if (mappa.get(Cost.KEY_MAPPA_DESTRA_LISTA) instanceof ArrayList) {
                listaColonneDestra = (ArrayList) mappa.get(Cost.KEY_MAPPA_DESTRA_LISTA);
            }// fine del blocco if
        }// end of if cycle

        return listaColonneDestra;
    }// fine del metodo


    private static String creaTableSingoloTitolo(HashMap mappa, String nome, boolean colonnaSortable, boolean coloriScuri) {
        String titolo = VUOTA;
        String tagNormale = PIPE;
        String tagSortable = "!";
        String tag;
        String tagColonnaUnSortable = "class=\"unsortable\"";
        boolean tableSortable = getTableSortable(mappa);
        boolean creaSpazi = false;
        String parametri = "";
        String bgColorTitle = GRIGIO_MEDIO;

        // controllo parametri della mappa
        if (tableSortable) {
            tag = tagSortable;
        } else {
            tag = tagNormale;
        }// fine del blocco if-else

        titolo += tag;
        if (tableSortable && !colonnaSortable) {
            parametri += SPAZIO + tagColonnaUnSortable;
        }// fine del blocco if
        if (coloriScuri) {
            parametri += SPAZIO + bgColorTitle;
        }// fine del blocco if
        if (!parametri.equals("")) {
            titolo += parametri + SPAZIO + tagNormale;
        }// fine del blocco if

        // spazio per leggere il codice sorgente
        titolo += SPAZIO;

        // spazio visibile a video
        if (creaSpazi) {
            titolo += SPAZIO;
        }// fine del blocco if
        titolo += LibWiki.setBold(nome);
        if (creaSpazi) {
            titolo += SPAZIO;
        }// fine del blocco if
        titolo += A_CAPO;

        return titolo;
    }// fine del metodo


    private static String creaTableBody(HashMap mappa) {
        String body = VUOTA;
        ArrayList<ArrayList> listaRighe = getRighe(mappa);
        ArrayList<Boolean> listaColonneDestra = getColonneDestra(mappa);
        String tagRiga = "|-";
        String tagCampo = PIPE;
        int pos = 0;
        boolean numProg = getNumerazioneProgressiva(mappa);
        ArrayList<Boolean> listaColonneSort = getColonneSort(mappa);

        if (listaRighe != null && listaRighe.size() > 0) {
            if (listaRighe.get(0) instanceof ArrayList) {
                for (ArrayList singolaRiga : (ArrayList<ArrayList>) listaRighe) {
                    pos++;
                    body += tagRiga;
                    body += A_CAPO;
                    if (numProg) {
                        body += rigaProg(pos, tagCampo);
                    }// fine del blocco if
                    body += creaTableRiga(singolaRiga, pos, listaColonneSort, listaColonneDestra);
//                    body = LibText.levaCoda(body, tagCampo);
                    body += A_CAPO;
                }// end of for cycle
            }// end of if cycle
        }// fine del blocco if

        return body.trim();
    }// fine del metodo


    private static String rigaProg(int pos, String tagCampo) {
        String body = VUOTA;

        if (pos > 0) {
            body += tagCampo;
            body += TXT_ALIGN;
            body += tagCampo;
            body += SPAZIO;
            body += pos;
            body += SPAZIO;
            body += SPAZIO;
            body += tagCampo;
        }// fine del blocco if

        return body.trim();
    }// fine del metodo


    /**
     * I numeri sono sempre allineati a destra
     * Sono formattati se la colonna NON è sortable
     */
    private static String creaTableRiga(ArrayList singolaRiga, int pos, ArrayList<Boolean> listaColonneSort, ArrayList<Boolean> listaColonneDestra) {
        String body = VUOTA;
        String tagCampo = PIPE;
        String value = "";
        boolean numero = false;
        boolean numeriFormattati = true;
        boolean allineatoADestra = false;
        boolean colonnaSortable = false;
        Object cella;
        boolean destraObbligatoria = false;

        for (int k = 0; k < singolaRiga.size(); k++) {
            cella = singolaRiga.get(k);
            colonnaSortable = listaColonneSort.get(k);
            allineatoADestra = false;
            if (listaColonneDestra != null && listaColonneDestra.size() == singolaRiga.size()) {
                destraObbligatoria = listaColonneDestra.get(k);
            }// end of if cycle

            if (cella instanceof Number) {
                numero = true;
                allineatoADestra = true;
                if (colonnaSortable) {
                    value = cella + VUOTA;
                } else {
//                    value = LibNum.format(cella);
                }// end of if/else cycle
            }// end of if cycle

            if (cella instanceof String) {
                numero = false;
                value = (String) cella;
            }// end of if cycle

            if (allineatoADestra || destraObbligatoria) {
                body += tagCampo;
                body += TXT_ALIGN;
            }// fine del blocco if
            body += tagCampo;
            body += SPAZIO;
            body += value;
            body += SPAZIO;
            body += tagCampo;

        }// end of for cycle


        if (value.equals(SPAZIO)) {
            body += tagCampo;
        } else {
//            body = LibText.levaCoda(body, tagCampo);
        }// end of if/else cycle
        body += A_CAPO;

        return body.trim();
    }// fine del metodo


    /**
     * Sostituzione ragionata
     * Viene esaminata ogni singola evenienze e la regolazione è specifica
     * Non uso regex perché non ci riesco
     */
    public static String modificaLink(String testoIn, String oldTitle, String newTitle) {
        String testoOut = "";
        String tagStx = "[[";
        String pipe = "|";
        String spazio = " ";
        String oldLink = tagStx + oldTitle;
        String newLink = tagStx + newTitle;
        String newSub = "";
        int posIni = 0;
        int posEnd = 0;
        String nextCar;
        String nextNextCar;
        String prima;
        String estratto;
        String dopo = testoIn;
        int k = 0;

        posIni = dopo.indexOf(oldLink);
        while (posIni != -1) {
            k++;
            posEnd = posIni + oldLink.length();
            prima = dopo.substring(0, posIni);
            estratto = dopo.substring(posIni, posEnd);
            nextCar = dopo.substring(posEnd, posEnd + 1);
            if (nextCar.equals(pipe)) {
                newSub = newLink;
            } else {
                nextNextCar = dopo.substring(posEnd, posEnd + spazio.length());
                if (nextNextCar.equals(spazio)) {
                    newSub = estratto;
                } else {
                    newSub = tagStx + modificaLink(newTitle);
                }// end of if/else cycle
            }// end of if/else cycle
            dopo = dopo.substring(posEnd);
            testoOut += prima + newSub;
            posIni = dopo.indexOf(oldLink);
        }// end of for cycle
        testoOut += dopo;

        return testoOut;
    }// end of method


    public static String modificaLink(String newLinkIn) {
        String newLinkInOut = newLinkIn;
        String tagParIni = "(";
        String tagParEnd = ")";
        String tagPipe = "|";
        int pos;
        String prima;

        if (newLinkIn == null || newLinkIn.equals("")) {
            return "";
        }// end of if cycle

        newLinkIn = newLinkIn.trim();
        if (newLinkIn.endsWith(tagParEnd)) {
            pos = newLinkIn.indexOf(tagParIni);
            prima = newLinkIn.substring(0, pos).trim();
            newLinkInOut = newLinkIn + tagPipe + prima;
        }// end of if cycle

        return newLinkInOut;
    }// end of method


    /**
     * Costruisce la stringa dei cookies
     *
     * @param cookies mappa dei cookies
     */
    public static String creaCookiesText(HashMap<String, Object> cookies) {
        String cookiesTxt = "";
        String sep = "=";
        Object valObj = null;
        String key;

        if (cookies != null && cookies.size() > 0) {
            for (Object obj : cookies.keySet()) {
                if (obj instanceof String) {
                    key = (String) obj;
                    valObj = cookies.get(key);
                    cookiesTxt += key;
                    cookiesTxt += sep;
                    cookiesTxt += valObj;
                    cookiesTxt += ";";
                }// end of if cycle
            }// end of for cycle
        }// fine del blocco if

        return cookiesTxt;
    } // fine del metodo


    /**
     * Allega i cookies alla request (upload)
     *
     * @param urlConn connessione attiva
     * @param cookies mappa dei cookies
     */
    public static void uploadCookies(URLConnection urlConn, HashMap<String, Object> cookies) {
        String cookiesTxt = creaCookiesText(cookies);

        if (urlConn != null && !cookiesTxt.equals("")) {
            urlConn.setRequestProperty("Cookie", cookiesTxt);
        }// fine del blocco if
    } // fine del metodo


    /**
     * Prepara una riga di una lista
     * Prepone un asterisco
     * Racchiude tra parentesi quadre
     * Pospone un ritorno a capo
     *
     * @param rigaIn ingresso
     */
    public static String setRigaQuadre(String rigaIn) {
        String rigaOut = "*";

        rigaOut += setQuadre(rigaIn);
        rigaOut += A_CAPO;

        return rigaOut;
    } // fine del metodo


    /**
     * Prepara una riga di categoria
     * Racchiude tra parentesi quadre
     * Pospone un ritorno a capo
     *
     * @param rigaIn ingresso
     */
    public static String setRigaCat(String rigaIn) {
        String rigaOut = "";
        String tag = "Categoria:";

        rigaOut = QUADRE_INI + tag + rigaIn + QUADRE_END;
        rigaOut += A_CAPO;

        return rigaOut;
    } // fine del metodo


    /**
     * Aggiunge il tag html small in testa ed in coda alla stringa
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     *
     * @param stringaIn in ingresso
     *
     * @return stringa con i tag ref iniziale e finale
     */
    public static String setSmall(String stringaIn) {
        String stringaOut = stringaIn;

        if (stringaIn != null && stringaIn.length() > 0) {
            stringaOut = "<small>" + stringaOut + "</small>";
        }// fine del blocco if

        return stringaOut.trim();
    } // fine del metodo


    /**
     * Aggiunge il tag html italic in testa ed in coda alla stringa
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     *
     * @param stringaIn in ingresso
     *
     * @return stringa con i tag ref iniziale e finale
     */
    public static String setItalic(String stringaIn) {
        String stringaOut = stringaIn;

        if (stringaIn != null && stringaIn.length() > 0) {
            stringaOut = "<i>" + stringaOut + "</i>";
        }// fine del blocco if

        return stringaOut.trim();
    } // fine del metodo


    /**
     * Aggiunge il tag html strong in testa ed in coda alla stringa
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     *
     * @param stringaIn in ingresso
     *
     * @return stringa con i tag ref iniziale e finale
     */
    public static String setStrong(String stringaIn) {
        String stringaOut = stringaIn;

        if (stringaIn != null && stringaIn.length() > 0) {
            stringaOut = "<b>" + stringaOut + "</b>";
        }// fine del blocco if

        return stringaOut.trim();
    } // fine del metodo


    /**
     * Estrae un valore da una mappa se esiste la chiave
     *
     * @param mappa in ingresso
     * @param key   richiesta
     *
     * @return valore della singola chiave
     */
    public static Object getValue(HashMap<String, Object> mappa, String key) {
        Object value = null;

        if (mappa != null && key != null && key.length() > 0) {
            if (mappa.containsKey(key)) {
                if (mappa.get(key) != null) {
                    value = mappa.get(key);
                }// end of if cycle
            }// end of if cycle
        }// fine del blocco if

        return value;
    } // fine del metodo


    /**
     * Estrae un valore da una mappa se esiste la chiave
     *
     * @param mappa in ingresso
     * @param key   richiesta
     *
     * @return valore stringa della singola chiave
     */
    public static String getValueStr(HashMap<String, Object> mappa, String key) {
        String value = "";

        if (mappa != null && key != null && key.length() > 0) {
            if (mappa.containsKey(key)) {
                if (mappa.get(key) != null && mappa.get(key) instanceof String) {
                    value = (String) mappa.get(key);
                }// end of if cycle
            }// end of if cycle
        }// fine del blocco if

        return value;
    } // fine del metodo


    /**
     * Estrae un valore da una mappa se esiste la chiave
     *
     * @param mappa in ingresso
     * @param key   richiesta
     *
     * @return valore long della singola chiave
     */
    public static long getValueLong(HashMap<String, Object> mappa, String key) {
        long value = 0L;

        if (mappa != null && key != null && key.length() > 0) {
            if (mappa.containsKey(key)) {
                if (mappa.get(key) != null && mappa.get(key) instanceof Long) {
                    value = (long) mappa.get(key);
                }// end of if cycle
            }// end of if cycle
        }// fine del blocco if

        return value;
    } // fine del metodo


    /**
     * Controlla se la response di un login contiene la conferma
     *
     * @param urlResponse in ingresso
     *
     * @return true se il login è confermato
     */
    public static boolean isLoginValid(String urlResponse) {
        boolean loginValido = false;
        String key = "result";
        String previsto = "Success";
        HashMap<String, Object> mappa = creaMappaLogin(urlResponse);
        String ottenuto = getValueStr(mappa, key);

        if (ottenuto.equals(previsto)) {
            loginValido = true;
        }// end of if cycle

        return loginValido;
    } // fine del metodo


    /**
     * Controlla se la urlResponse di una query generica contiene la conferma
     *
     * @param textJSON in ingresso
     *
     * @return true se la urlResponse è valida
     */
    public static boolean isResponseValid(String textJSON) {
        boolean responseValida = false;
        String tag = BATCHCOMPLETE;
        HashMap<String, Object> mappa = getMappaJSON(textJSON);

        if (textJSON != null && textJSON.length() > 0) {
            if (mappa != null && mappa.get(tag) != null && mappa.get(tag) instanceof Boolean) {
                responseValida = (boolean) mappa.get(tag);
            }// fine del blocco if
        }// end of if cycle

        return responseValida;
    } // fine del metodo


} // fine della classe astratta
