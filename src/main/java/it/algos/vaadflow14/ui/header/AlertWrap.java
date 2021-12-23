package it.algos.vaadflow14.ui.header;

import java.util.*;

/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: dom, 03-mag-2020
 * Time: 18:03
 * Wrapper per incapsulare le liste di avvisi e passarle dal Service a AHeader <br>
 */
public class AlertWrap {

    private List<String> alertBlack;

    private List<String> alertGreen;

    private List<String> alertBlue;

    private List<String> alertRed;

    private boolean usaSecurity;

    private List<String> alertUser;

    private List<String> alertAdmin;

    private List<String> alertDev;

    private List<String> alertDevAll;

    private List<String> alertParticolare;


    public AlertWrap(String singoloAlert) {
        this(new ArrayList(Arrays.asList(singoloAlert)));
    }

    public AlertWrap(List<String> alertUser) {
        this.alertUser = alertUser;
    }


    public AlertWrap(List<String> prima, List<String> seconda, List<String> terza, boolean usaSecurity) {
        if (usaSecurity) {
            this.alertUser = prima;
            this.alertAdmin = seconda;
            this.alertDev = terza;
        }
        else {
            this.alertGreen = prima;
            this.alertBlue = seconda;
            this.alertRed = terza;
        }
    }

    //    public AlertWrap(List<String> alertUser, List<String> alertAdmin, List<String> alertDev) {
    //        this.alertUser = alertUser;
    //        this.alertAdmin = alertAdmin;
    //        this.alertDev = alertDev;
    //    }


    public AlertWrap(List<String> alertUser, List<String> alertAdmin, List<String> alertDev, List<String> alertDevAll, List<String> alertParticolare) {
        this.alertUser = alertUser;
        this.alertAdmin = alertAdmin;
        this.alertDev = alertDev;
        this.alertDevAll = alertDevAll;
        this.alertParticolare = alertParticolare;
    }


    public List<String> getAlertBlack() {
        return alertBlack;
    }


    public List<String> getAlertGreen() {
        return alertGreen;
    }


    public List<String> getAlertBlue() {
        return alertBlue;
    }


    public List<String> getAlertRed() {
        return alertRed;
    }


    public List<String> getAlertUser() {
        return alertUser;
    }


    public List<String> getAlertAdmin() {
        return alertAdmin;
    }


    public List<String> getAlertDev() {
        return alertDev;
    }


    public List<String> getAlertDevAll() {
        return alertDevAll;
    }


    public List<String> getAlertParticolare() {
        return alertParticolare;
    }


    public void setAlertParticolare(List<String> alertParticolare) {
        this.alertParticolare = alertParticolare;
    }

}
