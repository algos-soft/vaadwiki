package it.algos.vaadflow.modules.utente;

import it.algos.vaadflow.modules.company.EACompany;
import it.algos.vaadflow.modules.role.EARole;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: ven, 14-set-2018
 * Time: 15:39
 */
public enum EAUtente {
    uno(EACompany.algos, "gac", "fulvia", EARole.developer, "gac@algos.it"),
    due(EACompany.algos, "alex", "axel01", EARole.developer, "alex@algos.it"),
    tre(EACompany.demo, "admin", "admin", EARole.admin, "info@algos.it"),
    quattro((EACompany) null, "anonymous", "anonymous", EARole.user, ""),
    cinque(EACompany.test, "Addabbo Andrea", "addabbo123", EARole.user, "");

    public String userName;

    public String passwordInChiaro;

    public EARole ruolo;

    public String mail;

    public EACompany company;


    EAUtente(EACompany company, String userName, String passwordInChiaro, EARole ruolo, String mail) {
        this.setCompany(company);
        this.setUserName(userName);
        this.setPasswordInChiaro(passwordInChiaro);
        this.setRuolo(ruolo);
        this.setMail(mail);
    }// fine del costruttore


    public EACompany getCompany() {
        return company;
    }// end of method


    public void setCompany(EACompany company) {
        this.company = company;
    }// end of method


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
