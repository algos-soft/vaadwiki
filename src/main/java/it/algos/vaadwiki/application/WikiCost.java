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

    // da aumentare ogni tanto per essere sicuri che sia adeguato alle dimensioni del database
    public final static int BIO_NEEDED_MINUMUM_SIZE = 381000;

    public final static String TAG_DOP = "doppinomi";

    public final static String TAG_GEN = "genere";

    public final static String TAG_COG = "cognome";

    public final static String TAG_NOM = "nome";

    //	public final static String TAG_SEC = "secolo";
//	public final static String TAG_MES = "mese";
//	public final static String TAG_ANN = "anno";
//	public final static String TAG_GIO = "giorno";
    public final static String ROUTE_VIEW_GIORNO_NATI = "viewgiornonati";

    public final static String ROUTE_VIEW_GIORNO_MORTI = "viewgiornomorti";

    public final static String ROUTE_VIEW_ANNO_NATI = "viewannonati";

    public final static String ROUTE_VIEW_ANNO_MORTI = "viewannomorti";

    public final static String ROUTE_VIEW_NOMI = "viewnomi";

    public final static String ROUTE_VIEW_COGNOMI = "viewcognomi";

    public final static String ROUTE_VIEW_ATTIVITA = "viewattivita";

    public final static String ROUTE_VIEW_NAZIONALITA = "viewnazionalita";

    public final static String ROUTE_DIDASCALIE = "didascalie";

    public final static String TAG_PRO = "professione";

    public final static String TAG_NAZ = "nazionalita";

    public final static String TAG_ATT = "attivita";

    public final static String TAG_CAT = "categoria";

    public final static String TAG_BIO = "bio";

    public final static String TAG_UTI = "utility";

    public final static String TAG_WGIO = "wikigiorno";

    public final static String TAG_WANN = "wikianno";

    public final static String TAG_SEP = " - ";

    public static final String AST = "*";

    // daemons
    public final static String BIO = "Bio";

    public final static String ATT = "Attività";

    public final static String NAZ = "Nazionalità";

    public final static String PROF = "Professione";

    public final static String CAT = "Categoria:";

    public final static String GEN = "Genere:";

    public final static String GIORNI = "Giorni";

    public final static String ANNI = "Anni";

    public final static String NOME = "Nome:";

    public final static String COGNOME = "Cognome:";

    public final static String CODE_DOWNLOAD = "lastDownload";

    public final static String CODE_UPDATE = "lastUpdate";

    public final static String CODE_UPLOAD = "lastUpload";

    public final static String STATISTICHE = "Statistiche";

    public final static String CODE_STATISTICHE = CODE_UPLOAD + STATISTICHE;

    public final static String DURATA = "durataDownload";

    public final static String DURATA_ELABORA = "durataElabora";

    public final static String DURATA_UPLOAD = "durataUpload";

    public final static String DURATA_UPLOAD_STATISTICHE = DURATA_UPLOAD + STATISTICHE;

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

    public final static String USA_DAEMON_GENERE = DAEMON + GEN;

    public final static String USA_DAEMON_GIORNI = DAEMON + GIORNI;

    public final static String USA_DAEMON_ANNI = DAEMON + ANNI;

    public final static String USA_DAEMON_NOMI = DAEMON + "Nomi";

    public final static String USA_DAEMON_COGNOMI = DAEMON + "Cognomi";

    public final static String USA_DAEMON_LOCALITA = DAEMON + "Localita";

    //--localdatetime dell'ultimo download
    public final static String LAST_DOWNLOAD_ATTIVITA = CODE_DOWNLOAD + ATT;

    public final static String LAST_DOWNLOAD_NAZIONALITA = CODE_DOWNLOAD + NAZ;

    public final static String LAST_DOWNLOAD_PROFESSIONE = CODE_DOWNLOAD + PROF;

    public final static String LAST_DOWNLOAD_CATEGORIA = CODE_DOWNLOAD + CAT;

    public final static String LAST_DOWNLOAD_GENERE = CODE_DOWNLOAD + GEN;

    public final static String LAST_UPDATE_BIO = CODE_UPDATE + BIO;

    public final static String LAST_DOWNLOAD_DOPPI_NOMI = CODE_DOWNLOAD + "DoppiNomi";

    public final static String LAST_ELABORA_NOME = CODE_DOWNLOAD + NOME;

    public final static String LAST_ELABORA_COGNOME = CODE_DOWNLOAD + COGNOME;

    public final static String LAST_UPLOAD_GIORNI = CODE_UPLOAD + GIORNI;

    public final static String LAST_UPLOAD_ANNI = CODE_UPLOAD + ANNI;

    public final static String LAST_UPLOAD_NOMI = CODE_UPLOAD + NOME;

    public final static String LAST_UPLOAD_COGNOMI = CODE_UPLOAD + COGNOME;

    public final static String LAST_UPLOAD_STATISTICHE_GIORNI = CODE_STATISTICHE + GIORNI;

    public final static String LAST_UPLOAD_STATISTICHE_ANNI = CODE_STATISTICHE + ANNI;

    public final static String LAST_UPLOAD_STATISTICHE_ATTIVITA = CODE_STATISTICHE + ATT;

    public final static String LAST_UPLOAD_STATISTICHE_NAZIONALITA = CODE_STATISTICHE + NAZ;

    public final static String LAST_UPLOAD_STATISTICHE_NOMI = CODE_STATISTICHE + NOME;

    public final static String LAST_UPLOAD_STATISTICHE_COGNOMI = CODE_STATISTICHE + COGNOME;

    //--tempo impiegato per l'ultimo download, in secondi
    public final static String DURATA_DOWNLOAD_ATTIVITA = DURATA + ATT;

    public final static String DURATA_DOWNLOAD_NAZIONALITA = DURATA + NAZ;

    public final static String DURATA_DOWNLOAD_PROFESSIONE = DURATA + PROF;

    public final static String DURATA_DOWNLOAD_CATEGORIA = DURATA + CAT;

    public final static String DURATA_DOWNLOAD_GENERE = DURATA + GEN;

    public final static String DURATA_DOWNLOAD_BIO = DURATA + BIO;

    public final static String DURATA_DOWNLOAD_DOPPI_NOMI = DURATA + "DoppiNomi";

    public final static String DURATA_UPLOAD_GIORNI = DURATA + GIORNI;

    public final static String DURATA_UPLOAD_ANNI = DURATA + ANNI;

    public final static String DURATA_UPLOAD_NOMI = DURATA + NOME;

    public final static String DURATA_UPLOAD_COGNOMI = DURATA + COGNOME;

    public final static String DURATA_ELABORA_NOMI = DURATA_ELABORA + NOME;

    public final static String DURATA_ELABORA_COGNOMI = DURATA_ELABORA + NOME;

    public final static String DURATA_UPLOAD_STATISTICHE_GIORNI = DURATA_UPLOAD + GIORNI;

    public final static String DURATA_UPLOAD_STATISTICHE_ANNI = DURATA_UPLOAD + ANNI;

    public final static String DURATA_UPLOAD_STATISTICHE_ATTIVITA = DURATA_UPLOAD_STATISTICHE + ATT;

    public final static String DURATA_UPLOAD_STATISTICHE_NAZIONALITA = DURATA_UPLOAD_STATISTICHE + NAZ;

    public final static String DURATA_UPLOAD_STATISTICHE_NOMI = DURATA_UPLOAD + NOME;

    public final static String DURATA_UPLOAD_STATISTICHE_COGNOMI = DURATA_UPLOAD + COGNOME;

    //--path di varie pagine wiki
    public final static String PATH_WIKI = "https://it.wikipedia.org/wiki/";

    public final static String PATH_WIKI_EDIT_ANTE = "https://it.wikipedia.org/w/index.php?title=";

    public final static String PATH_WIKI_EDIT_POST = "&action=edit";

    public final static String CAT_BIO = "catBio";

    public final static String WIKI_PAGE_LIMIT = "wikiPageLimit";

    public final static String MIN_NATI_MORTI_PER_ANNO = "minNatiMortiAnno";

    public final static String TAG_SEPARATORE = SPAZIO + "-" + SPAZIO;

    public final static String TASK_DOW = "taskDownload";

    public final static String TASK_UPD = "taskUpdate";

    public final static String TASK_CRO = "taskCrono";

    public final static String TASK_GIO = "taskGiorni";

    public final static String TASK_ANN = "taskAnni";

    public final static String TASK_ATT = "taskAttivita";

    public final static String TASK_NAZ = "taskNazionalita";

    public final static String TASK_NOM = "taskNomi";

    public final static String TASK_COG = "taskCognomi";

    public final static String SEND_MAIL_CICLO = "sendMailCiclo";

    public final static String SEND_MAIL_RESTART = "sendMailRestart";

//    public final static String USA_UPLOAD_DURANTE_DOWNLOAD = "usaUploadDuranteDownload";

    public static final String PAGES = "pages";

    public static final String REVISIONS = "revisions";

    public static final String SLOTS = "slots";

    public static final String MAIN = "main";

    public static final String CATEGORY_INFO = "categoryinfo";

    public final static String ENCODE = "UTF-8";

    public final static String SOGLIA_NOMI_MONGO = "sogliaNomiMongo";

    public final static String SOGLIA_NOMI_PAGINA_WIKI = "sogliaNomiPaginaWiki";

    public final static String SOGLIA_COGNOMI_MONGO = "sogliaCognomiMongo";

    public final static String SOGLIA_COGNOMI_PAGINA_WIKI = "sogliaCognomiPaginaWiki";

    public final static String KEY_MAP_PARAGRAFO_TITOLO = "keyMapTitolo";

    public final static String KEY_MAP_PARAGRAFO_LINK = "keyMapLink";

    public final static String KEY_MAP_SESSO = "keyMapSesso";

    public final static String KEY_MAP_LISTA = "keyMapLista";

    public final static String KEY_MAP_VOCI = "keyMapVoci";

    public final static String KEY_MAP_ORDINE_ANNO_NATO = "keyMapOrdineAnnoNato";

    public final static String KEY_MAP_ORDINE_ANNO_MORTO = "keyMapOrdineAnnoMorto";

    public final static String KEY_MAP_ORDINE_GIORNO_NATO = "keyMapOrdineGiornoNato";

    public final static String KEY_MAP_ORDINE_GIORNO_MORTO = "keyMapOrdineGiornoMorto";

    public final static String USA_REGISTRA_SEMPRE_CRONO = "salvaSempreCrono";


    public final static String USA_PARAGRAFI_GIORNI = "usaParagrafiGiorni";

    public final static String USA_RIGHE_RAGGRUPPATE_GIORNI = "usaRigheRaggruppateGiorni";

    public final static String TAG_PARAGRAFO_VUOTO_GIORNI_NASCITA = "tagParagrafoVuotoGiorniNascita";

    public final static String TAG_PARAGRAFO_VUOTO_GIORNI_MORTE = "tagParagrafoVuotoGiorniMorte";

    public final static String IS_PARAGRAFO_VUOTO_GIORNI_IN_CODA = "isParagrafoVuotoGiorniInCoda";

    public final static String USA_FORCETOC_GIORNI = "usaForcetocGiorni";

    public final static String USA_PARAGRAFI_ANNI = "usaParagrafiAnni";

    public final static String TAG_PARAGRAFO_VUOTO_ANNI_NASCITA = "tagParagrafoVuotoAnniNascita";

    public final static String TAG_PARAGRAFO_VUOTO_ANNI_MORTE = "tagParagrafoVuotoAnniMorte";

    public final static String IS_PARAGRAFO_VUOTO_ANNI_IN_CODA = "isParagrafoVuotoAnniInCoda";

    public final static String USA_FORCETOC_ANNI = "usaForcetocAnni";

    public final static String USA_RIGHE_RAGGRUPPATE_ANNI = "usaRigheRaggruppateAnni";

    public final static String SOGLIA_SOTTOPAGINA_GIORNI_ANNI = "sogliaSottopaginaGiorniAnni";

    public final static String SOGLIA_SOTTOPAGINA_NOMI_COGNOMI = "sogliaSottopaginaNomiCognomi";

    public final static String USA_SOTTOPAGINE_GIORNI_ANNI = "usaSottopagineGiorniAnni";

    public final static String USA_SOTTOPAGINE_NOMI_COGNOMI = "usaSottopagineNomiCognomi";

    public final static String TAG_PARAGRAFO_VUOTO_NOMI_COGNOMI = "tagParagrafoVuotoNomi";

    public final static String TAG_SOTTOPAGINA_VUOTA_NOMI_COGNOMI = "tagSottoPaginaVuotaNomi";

    public final static String USA_FORCETOC_NOMI = "usaForcetocNomi";

    public final static String USA_FORCETOC_COGNOMI = "usaForcetocCognomi";

    public final static String IS_PARAGRAFO_VUOTO_NOMI_IN_CODA = "isParagrafoVuotoNomiInCoda";

    public final static String IS_PARAGRAFO_VUOTO_COGNOMI_IN_CODA = "isParagrafoVuotoCognomiInCoda";

    public final static String IS_PARAGRAFO_VUOTO_ATTIVITA_IN_CODA = "isParagrafoVuotoAttivitaInCoda";

    public final static String IS_PARAGRAFO_VUOTO_NAZIONALITA_IN_CODA = "isParagrafoVuotoNazionalitaInCoda";

    public final static String USA_LINK_ATTIVITA = "usaLinkAttivita";

    public final static String USA_LINK_PROFESSIONE = "usaLinkProfessione";

    public final static String USA_PARAGRAFO_SIZE_GIORNI = "usaParagrafoSizeGiorni";

    public final static String USA_PARAGRAFO_SIZE_ANNI = "usaParagrafoSizeAnni";

    public final static String USA_PARAGRAFO_SIZE_NOMI = "usaParagrafoSizeNomi";

    public final static String USA_PARAGRAFO_SIZE_COGNOMI = "usaParagrafoSizeCognomi";

    public final static String USA_PARAGRAFO_SIZE_ATTIVITA = "usaParagrafoSizeAttivita";

    public final static String USA_PARAGRAFO_SIZE_NAZIONALITA = "usaParagrafoSizeNazionalita";

    public static WikiLoginOld WIKI_LOGIN = null;

}// end of static class