package it.algos.vaadflow.modules.utente;

import it.algos.vaadflow.modules.role.EARole;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: ven, 14-set-2018
 * Time: 15:39
 */
public enum EAUtente {
    uno("gac", "fulvia", EARole.developer, "gac@algos.it"),
    due("alex", "axel01", EARole.developer, "alex@algos.it"),
    tre("admin", "admin", EARole.admin, "info@algos.it"),
    quattro("anonymous", "anonymous", EARole.user, ""),
    cinque("Addabbo Andrea", "addabbo123", EARole.user, "");

    public String userName;
    public String passwordInChiaro;
    public EARole ruolo;
    public String mail;

    EAUtente(String userName, String passwordInChiaro, EARole ruolo, String mail) {
        this.setUserName(userName);
        this.setPasswordInChiaro(passwordInChiaro);
        this.setRuolo(ruolo);
        this.setMail(mail);
    }// fine del costruttore

    public String getUserName() {
        return userName;
    }// end of method

    public void setUserName(String userName) {
        this.userName = userName;
    }// end of method

    public String getPasswordInChiaro() {
        return passwordInChiaro;
    }// end of method

    public void setPasswordInChiaro(String passwordInChiaro) {
        this.passwordInChiaro = passwordInChiaro;
    }// end of method

    public EARole getRuolo() {
        return ruolo;
    }// end of method

    public void setRuolo(EARole ruolo) {
        this.ruolo = ruolo;
    }// end of method

    public String getMail() {
        return mail;
    }// end of method

    public void setMail(String mail) {
        this.mail = mail;
    }// end of method

}// end of enumeration class
