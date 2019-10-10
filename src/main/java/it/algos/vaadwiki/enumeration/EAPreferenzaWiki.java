package it.algos.vaadwiki.enumeration;


import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.enumeration.EAPreferenza;
import it.algos.vaadflow.modules.preferenza.EAPrefType;
import it.algos.vaadflow.modules.preferenza.IAPreferenza;
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

} // end of enumeration
