package it.algos.vaadwiki.enumeration;


import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.enumeration.EAPreferenza;
import it.algos.vaadflow.modules.preferenza.EAPrefType;
import it.algos.vaadflow.modules.preferenza.IAPreferenza;
import it.algos.vaadflow.modules.role.EARole;
import it.algos.vaadwiki.application.WikiCost;

import static it.algos.vaadwiki.application.WikiCost.*;
import static it.algos.vaadwiki.application.WikiCost.USA_DAEMON_COGNOMI;

/**
 * Project it.algos.vaadflow
 * Created by Algos
 * User: gac
 * Date: mer, 30-mag-2018
 * Time: 07:27
 */
public enum EAPreferenzaWiki implements IAPreferenza {

    usaDaemonBio(USA_DAEMON_BIO, "Crono per ciclo bio completo", EAPrefType.bool, true),
    usaDaemonAttivita(USA_DAEMON_ATTIVITA, "Crono per download attività, extra-ciclo", EAPrefType.bool, false),
    usaDaemonNazionalita(USA_DAEMON_NAZIONALITA, "Crono per download nazionalità, extra-ciclo", EAPrefType.bool, false),
    usaDaemonProfessione(USA_DAEMON_PROFESSIONE, "Crono per download professione, extra-ciclo", EAPrefType.bool, false),
    usaDaemonCategoria(USA_DAEMON_CATEGORIA, "Crono per download categoria, extra-ciclo", EAPrefType.bool, false),
    usaDaemonGenere(USA_DAEMON_GENERE, "Crono per download genere, extra-ciclo", EAPrefType.bool, false),
    usaDaemonGiorni(USA_DAEMON_GIORNI, "Crono per upload giorni, extra-ciclo", EAPrefType.bool, true),
    usaDaemonAnni(USA_DAEMON_ANNI, "Crono per upload anni, extra-ciclo", EAPrefType.bool, true),
    usaDaemonNomi(USA_DAEMON_NOMI, "Crono per upload nomi, extra-ciclo", EAPrefType.bool, false),
    usaDaemonCognomi(USA_DAEMON_COGNOMI, "Crono per upload cognomi, extra-ciclo", EAPrefType.bool, false),

    lastDownloadAttivita(LAST_DOWNLOAD_ATTIVITA, "Ultimo download del modulo attività", EAPrefType.date, null),
    lastDownloadNazionalita(LAST_DOWNLOAD_NAZIONALITA, "Ultimo download del modulo nazionalità", EAPrefType.date, null),
    lastDownloadProfessione(LAST_DOWNLOAD_PROFESSIONE, "Ultimo download del modulo professione", EAPrefType.date, null),
    lastDownloadGenere(LAST_DOWNLOAD_GENERE, "Ultimo download del modulo genere (plurali)", EAPrefType.date, null),
    lastDownloadCategoria(LAST_DOWNLOAD_CATEGORIA, "Ultimo controllo di tutte le pagine esistenti nella categoria BioBot", EAPrefType.date, null),
    lastDownloadBio(LAST_UPDATE_BIO, "Ultimo update delle pagine della categoria BioBot", EAPrefType.date, null),

    durataDownloadAttivita(DURATA_DOWNLOAD_ATTIVITA, "Durata in secondi dell'ultimo download del modulo attività", EAPrefType.integer, 0),
    durataDownloadNazionalita(DURATA_DOWNLOAD_NAZIONALITA, "Durata in secondi dell'ultimo download del modulo nazionalità", EAPrefType.integer, 0),
    durataDownloadProfessione(DURATA_DOWNLOAD_PROFESSIONE, "Durata in secondi dell'ultimo download del modulo professione", EAPrefType.integer, 0),
    durataDownloadCategoria(DURATA_DOWNLOAD_CATEGORIA, "Durata in secondi dell'ultimo download delle pagine della categoria BioBot", EAPrefType.integer, 0),
    durataDownloadGenere(DURATA_DOWNLOAD_GENERE, "Durata in secondi dell'ultimo download del modulo genere (plurali)", EAPrefType.integer, 0),
    durataDownloadBio(DURATA_DOWNLOAD_BIO, "Durata in minuti dell'ultimo update delle pagine della categoria BioBot", EAPrefType.integer, 0),

    categoriaAttiva(CAT_BIO, "Categoria attiva", EAPrefType.string, "BioBot"),
    wikiPageLimit(WIKI_PAGE_LIMIT, "Numero di pagine wiki da controllare nel blocco", EAPrefType.integer, 250),

    sendMailCiclo(SEND_MAIL_CICLO, "Mail di conferma e controllo del ciclo giornaliero", EAPrefType.bool, true),
    sendMailRestart(SEND_MAIL_RESTART, "Mail per il restart dell'applicazione", EAPrefType.bool, true),

    registraSempreCrono(USA_REGISTRA_SEMPRE_CRONO, "Registra sempre le pagine di giorni ed anni", EAPrefType.bool, false),

    usaParagrafiGiorni(USA_PARAGRAFI_GIORNI, "Paragrafi dei secoli nelle liste dei giorni", EAPrefType.bool, false),
    paragrafoVuotoGiorniNascita(TAG_PARAGRAFO_VUOTO_GIORNI_NASCITA, "Titolo del paragrafo per le biografie senza anno di nascita specificato", EAPrefType.string, "Senza anno di nascita"),
    paragrafoVuotoGiorniMorte(TAG_PARAGRAFO_VUOTO_GIORNI_MORTE, "Titolo del paragrafo per le biografie senza anno di morte specificato", EAPrefType.string, "Senza anno di morte"),
    usaForcetocGiorni(USA_FORCETOC_GIORNI, "Usa l'indice dei paragrafi nelle liste dei giorni", EAPrefType.bool, false),
    isParagrafoVuotoGiorniCoda(IS_PARAGRAFO_VUOTO_GIORNI_IN_CODA, "Posiziona come ultimo il paragrafo per le biografie senza anno specificato", EAPrefType.bool, true),
    usaParagrafoSizeGiorni(USA_PARAGRAFO_SIZE_GIORNI, "Dimensione del paragrafo nei titoli dei mesi per i giorni", EAPrefType.bool, true),
    usaRigheRaggruppateGiorni(USA_RIGHE_RAGGRUPPATE_GIORNI, "Usa righe raggruppate per anno nella liste dei giorni", EAPrefType.bool, true),
    lastUploadGiorni(LAST_UPLOAD_GIORNI, "Ultimo upload dei giorni", EAPrefType.date, null),
    durataUploadGiorni(DURATA_UPLOAD_GIORNI, "Durata in minuti dell'ultimo upload dei giorni", EAPrefType.integer, 0),

    usaParagrafiAnni(USA_PARAGRAFI_ANNI, "Paragrafi dei mesi nelle liste degli anni", EAPrefType.bool, false),
    paragrafoVuotoAnniNascita(TAG_PARAGRAFO_VUOTO_ANNI_NASCITA, "Titolo del paragrafo per le biografie senza giorno di nascita specificato", EAPrefType.string, "Senza giorno di nascita"),
    paragrafoVuotoAnniMorte(TAG_PARAGRAFO_VUOTO_ANNI_MORTE, "Titolo del paragrafo per le biografie senza giorno di morte specificato", EAPrefType.string, "Senza giorno di morte"),
    usaForcetocAnni(USA_FORCETOC_ANNI, "Usa l'indice dei paragrafi nelle liste degli anni", EAPrefType.bool, false),
    isParagrafoVuotoAnniCoda(IS_PARAGRAFO_VUOTO_ANNI_IN_CODA, "Posiziona come ultimo il paragrafo per le biografie senza giorno specificato", EAPrefType.bool, true),
    usaParagrafoSizeAnni(USA_PARAGRAFO_SIZE_ANNI, "Dimensione del paragrafo nei titoli dei secoli per gli anni", EAPrefType.bool, true),
    usaRigheRaggruppateAnni(USA_RIGHE_RAGGRUPPATE_ANNI, "Usa righe raggruppate per giorno nella liste degli anni", EAPrefType.bool, true),
    lastUploadAnni(LAST_UPLOAD_ANNI, "Ultimo upload degli anni", EAPrefType.date, null),
    durataUploadAnni(DURATA_UPLOAD_ANNI, "Durata in minuti dell'ultimo upload degli anni", EAPrefType.integer, 0),
    sogliaSottopaginaGiorniAnni(SOGLIA_SOTTOPAGINA_GIORNI_ANNI, "Soglia per sottopaginare i paragrafi di giorni ed anni", EAPrefType.integer, 50),
    usaSottopagineGiorniAnni(USA_SOTTOPAGINE_GIORNI_ANNI, "Usa sottopagine se i paragrafi nella liste dei giorni e degli anni superano SOGLIA_SOTTOPAGINA_GIORNI_ANNI", EAPrefType.bool, false),

    paragrafoVuotoNomiCognomi(TAG_PARAGRAFO_VUOTO_NOMI_COGNOMI, "Titolo del paragrafo per le biografie senza attività specificata", EAPrefType.string, "Senza attività specificata"),
    sottopaginaVuotaNomiCognomi(TAG_SOTTOPAGINA_VUOTA_NOMI_COGNOMI, "Titolo della sottopagina per le biografie senza attività specificata", EAPrefType.string, "Altre..."),
    sogliaSottopaginaNomiCognomi(SOGLIA_SOTTOPAGINA_NOMI_COGNOMI, "Soglia per sottopaginare i paragrafi di nomi e cognomi", EAPrefType.integer, 50),
    usaSottopagineNomiCognomi(USA_SOTTOPAGINE_NOMI_COGNOMI, "Usa sottopagine se i paragrafi nella liste di nomi e cognomi superano SOGLIA_SOTTOPAGINA_NOMI_COGNOMI", EAPrefType.bool, false),

    sogliaNomiMongo(SOGLIA_NOMI_MONGO, "Soglia minima per creare una entity nella collezione Nomi sul mongoDB", EAPrefType.integer, 30),
    sogliaNomiWiki(SOGLIA_NOMI_PAGINA_WIKI, "Soglia minima per creare la pagina di un nome sul server wiki", EAPrefType.integer, 50),
    usaForcetocNomi(USA_FORCETOC_NOMI, "Usa l'indice dei paragrafi nelle liste dei nomi", EAPrefType.bool, true),
    isParagrafoVuotoNomiCoda(IS_PARAGRAFO_VUOTO_NOMI_IN_CODA, "Posiziona come ultimo il paragrafo per le biografie senza attività specificata nelle liste dei nomi", EAPrefType.bool, true),
    usaParagrafoSizeNomi(USA_PARAGRAFO_SIZE_NOMI, "Numero delle voci contenute nel paragrafo per i nomi", EAPrefType.bool, true),
    lastElaboraNomi(LAST_ELABORA_NOME, "Ultima elaborazione dei nomi", EAPrefType.date, null),
    lastDownloadNomiDoppi(LAST_DOWNLOAD_DOPPI_NOMI, "Ultima elaborazione dei doppi nomi", EAPrefType.date, null),

    sogliaCognomiMongo(SOGLIA_COGNOMI_MONGO, "Soglia minima per creare una entity nella collezione Cognomi sul mongoDB", EAPrefType.integer, 30),
    sogliaCognomiWiki(SOGLIA_COGNOMI_PAGINA_WIKI, "Soglia minima per creare la pagina di un cognome sul server wiki", EAPrefType.integer, 50),
    usaForcetocCognomi(USA_FORCETOC_COGNOMI, "Usa l'indice dei paragrafi nelle liste dei cognomi", EAPrefType.bool, true),
    isParagrafoVuotoCognomiCoda(IS_PARAGRAFO_VUOTO_COGNOMI_IN_CODA, "Posiziona come ultimo il paragrafo per le biografie senza attività specificata nelle liste dei cognomi", EAPrefType.bool, true),
    usaParagrafoSizeCognomi(USA_PARAGRAFO_SIZE_COGNOMI, "Dimensione del paragrafo nel titolo per i cognomi", EAPrefType.bool, true),
    lastElaboraCognomi(LAST_ELABORA_COGNOME, "Ultima elaborazione dei cognomi", EAPrefType.date, null),
    ;



    private String code;

    private String desc;

    private EAPrefType type;

    private Object value;


    EAPreferenzaWiki(String code, String desc, EAPrefType type, Object value) {
        this.setCode(code);
        this.setDesc(desc);
        this.setType(type);
        this.setValue(value);
    }// fine del costruttore


    public String getCode() {
        return code;
    }// end of method


    public void setCode(String code) {
        this.code = code;
    }// end of method


    public String getDesc() {
        return desc;
    }// end of method


    public void setDesc(String desc) {
        this.desc = desc;
    }// end of method


    public EAPrefType getType() {
        return type;
    }// end of method


    public void setType(EAPrefType type) {
        this.type = type;
    }// end of method


    public Object getValue() {
        return value;
    }// end of method


    public void setValue(Object value) {
        this.value = value;
    }// end of method

    public EARole getShow() {
        return null;
    }// end of method


    @Override
    public boolean isCompanySpecifica() {
        return false;
    }// end of method

} // end of enumeration
