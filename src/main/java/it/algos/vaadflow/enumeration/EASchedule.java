package it.algos.vaadflow.enumeration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Project it.algos.vaadflow
 * Created by Algos
 * User: gac
 * Date: gio, 12-lug-2018
 * Time: 17:38
 * <p>
 * Template di schedule preconfigurati, con nota esplicativa utilizzabile nelle info
 *
 * @see http://www.sauronsoftware.it/projects/cron4j/manual.php
 */
public enum EASchedule {

    giorno("0 0 * * *", "ogni giorno a mezzanotte"),
    giornoPrimoMinuto("1 0 * * *","ogni giorno 1 minuto dopo mezzanotte."),
    giornoSecondoMinuto("2 0 * * *","ogni giorno 2 minuti dopo mezzanotte."),
    giornoTerzoMinuto("3 0 * * *","ogni giorno 3 minuti dopo mezzanotte."),
    giornoQuartoMinuto("4 0 * * *","ogni giorno 4 minuti dopo mezzanotte."),
    giornoQuintoMinuto("5 0 * * *","ogni giorno 5 minuti dopo mezzanotte."),
    giornoSestoMinuto("6 0 * * *","ogni giorno 6 minuti dopo mezzanotte."),
    giornoSettimoMinuto("7 0 * * *","ogni giorno 7 minuti dopo mezzanotte."),
    giornoOttavoMinuto("8 0 * * *","ogni giorno 8 minuti dopo mezzanotte."),
    giornoNonoMinuto("9 0 * * *","ogni giorno 9 minuti dopo mezzanotte."),
    giornoDecimoMinuto("10 0 * * *","ogni giorno 10 minuti dopo mezzanotte."),
    oraSecondoMinuto("2 * * * *", "ogni ora, al minuto 2."),
    oraTerzoMinuto("3 * * * *", "ogni ora, al minuto 3."),
    oraQuartoMinuto("4 * * * *", "ogni ora, al minuto 4."),
    oraQuintoMinuto("5 * * * *", "ogni ora, al minuto 5."),
    minuto("* * * * *", "ogni minuto"),
    minutoUno("1/3 * * * *", "ogni minuto"),
    minutoDue("2/3 * * * *", "ogni minuto"),
    minutoTre("3/3 * * * *", "ogni minuto"),
    settimanaLunedi("0 0 * * 1", "ogni settimana nella notte tra domenica e lunedi.");


    private String tag;
    private String nota;

    EASchedule(String tag, String nota) {
        this.setTag(tag);
        this.setNota(nota);
    }// fine del costruttore

    public String getTag() {
        return tag;
    }// end of method

    public void setTag(String tag) {
        this.tag = tag;
    }// end of method

    public String getNota() {
        return nota;
    }// end of method

    public void setNota(String nota) {
        this.nota = nota;
    }// end of method

}// end of enum
