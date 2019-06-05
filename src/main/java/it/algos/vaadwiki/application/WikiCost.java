package it.algos.vaadwiki.application;

import it.algos.vaadflow.annotation.AIScript;
import it.algos.wiki.WikiLoginOld;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import static it.algos.vaadflow.application.FlowCost.SPAZIO;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: ven, 8-mag-2018
 * <p>
 * Completa la classe BaseCost con le costanti statiche specifiche di questa applicazione <br>
 * <p>
 * Not annotated with @SpringComponent (inutile) <br>
 * Not annotated with @Scope (inutile) <br>
 * Annotated with @AIScript (facoltativo) per controllare la ri-creazione di questo file nello script di algos <br>
 */
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@AIScript(sovrascrivibile = false)
public class WikiCost {

    public final static String TAG_NOM = "wikinome";

    //	public final static String TAG_SEC = "secolo";
//	public final static String TAG_MES = "mese";
//	public final static String TAG_ANN = "anno";
//	public final static String TAG_GIO = "giorno";
    public final static String ROUTE_VIEW_GIORNO_NATI = "viewgiornonati";

    public final static String ROUTE_VIEW_GIORNO_MORTI = "viewgiornomorti";

    public final static String ROUTE_VIEW_ANNO_NATI = "viewannonati";

    public final static String ROUTE_VIEW_ANNO_MORTI = "viewannomorti";

    public final static String TAG_PRO = "professione";

    public final static String TAG_NAZ = "nazionalita";

    public final static String TAG_ATT = "attivita";

    public final static String TAG_CAT = "categoria";

    public final static String TAG_BIO = "bio";

    public final static String TAG_UTI = "utility";

    public final static String TAG_WGIO = "wikigiorno";

    public final static String TAG_WANN = "wikianno";

    // daemons
    public final static String BIO = "Bio";

    public final static String ATT = "Attività";

    public final static String NAZ = "Nazionalità";

    public final static String PROF = "Professione";

    public final static String CAT = "Categoria:";

    public final static String CODE = "lastDownload";

    public final static String DURATA = "durataDownload";

    public final static String PATH_PROGETTO = "Progetto:Biografie/";

    public final static String PATH_MODULO = "Modulo:Bio/";

    public final static String PATH_MODULO_LINK = PATH_MODULO + "Link_";

    public final static String PATH_MODULO_PLURALE = PATH_MODULO + "Plurale_";

    //--flag per attivare o meno le tasks
    public final static String DAEMON = "usaDaemon";

    public final static String USA_DAEMON_BIO = DAEMON + BIO;

    public final static String USA_DAEMON_ATTIVITA = DAEMON + ATT;

    public final static String USA_DAEMON_NAZIONALITA = DAEMON + NAZ;

    public final static String USA_DAEMON_PROFESSIONE = DAEMON + PROF;

    public final static String USA_DAEMON_CATEGORIA = DAEMON + CAT;

    public final static String USA_DAEMON_NOMI = DAEMON + "Nomi";

    public final static String USA_DAEMON_COGNOMI = DAEMON + "Cognomi";

    public final static String USA_DAEMON_LOCALITA = DAEMON + "Localita";

    //--localdatetime dell'ultimo download
    public final static String LAST_DOWNLOAD_ATTIVITA = CODE + ATT;

    public final static String LAST_DOWNLOAD_NAZIONALITA = CODE + NAZ;

    public final static String LAST_DOWNLOAD_PROFESSIONE = CODE + PROF;

    public final static String LAST_DOWNLOAD_CATEGORIA = CODE + CAT;

    public final static String LAST_DOWNLOAD_BIO = CODE + BIO;

    //--tempo impiegato per l'ultimo download, in secondi
    public final static String DURATA_DOWNLOAD_ATTIVITA = DURATA + ATT;

    public final static String DURATA_DOWNLOAD_NAZIONALITA = DURATA + NAZ;

    public final static String DURATA_DOWNLOAD_PROFESSIONE = DURATA + PROF;

    public final static String DURATA_DOWNLOAD_CATEGORIA = DURATA + CAT;

    public final static String DURATA_DOWNLOAD_BIO = DURATA + BIO;

    //--path di varie pagine wiki
    public final static String PATH_WIKI = "https://it.wikipedia.org/wiki/";

    public final static String PATH_WIKI_EDIT_ANTE = "https://it.wikipedia.org/w/index.php?title=";

    public final static String PATH_WIKI_EDIT_POST = "&action=edit";

    public final static String CAT_BIO = "catBio";

    public final static String WIKI_PAGE_LIMIT = "wikiPageLimit";

    public final static String USA_CHECK_LISTE_PAGEID = "usaCheckListe";

    public final static String TAG_SEPARATORE = SPAZIO + "-" + SPAZIO;

    public final static String TASK_DOW = "taskDownload";

    public final static String TASK_UPD = "taskUpdate";

    public final static String TASK_CRO = "taskCrono";

    public final static String TASK_ATT = "taskAttivita";

    public final static String TASK_NAZ = "taskNazionalita";

    public final static String TASK_COG = "taskCognomi";

    public final static String TASK_NOM = "taskNomi";

    public final static String SEND_MAIL_CICLO = "sendMailCiclo";

    public final static String USA_UPLOAD_DURANTE_DOWNLOAD = "usaUploadDuranteDownload";

    public static final String PAGES = "pages";

    public static final String REVISIONS = "revisions";

    public static final String SLOTS = "slots";

    public static final String MAIN = "main";

    public static final String CATEGORY_INFO = "categoryinfo";

    public final static String ENCODE = "UTF-8";

    public static WikiLoginOld WIKI_LOGIN = null;

}// end of static class