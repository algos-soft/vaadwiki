package it.algos.vaadwiki.enumeration;

import it.algos.vaadflow.enumeration.EASchedule;

import static it.algos.vaadflow.application.FlowCost.A_CAPO;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: sab, 02-mar-2019
 * Time: 10:39
 */
public enum EACicloType {

    download(EASchedule.biMensile.getNota()) {
        @Override
        public String getEsegue() {
            String testo = "";
            testo += A_CAPO;
            testo += A_CAPO;
            testo += "Cancella tutte le voci biografiche e ricarica completamente il db";
            testo += A_CAPO;
            return testo;
        }// end of method
    },
    update(EASchedule.oreQuattro.getNota()) {
        @Override
        public String getEsegue() {
            String testo = "";
            testo += A_CAPO;
            testo += A_CAPO;
            testo += "Aggiunge le voci biografiche mancanti,";
            testo += A_CAPO;
            testo += " cancella quelle eccedenti ";
            testo += A_CAPO;
            testo += " e ricarica tutte quelle esistenti che sono state modificate dall'ultima lettura";
            testo += A_CAPO;
            return testo;
        }// end of method
    },
    ;

    private final String schedule;

    private String esegue;


    EACicloType(String schedule) {
        this.schedule = schedule;
    }//end of constructor


    public String getSchedule() {
        return schedule;
    }// end of method


    public String getEsegue() {
        return esegue;
    }// end of method


    public void setEsegue(String esegue) {
        this.esegue = esegue;
    }// end of method


}// end of enumeration
