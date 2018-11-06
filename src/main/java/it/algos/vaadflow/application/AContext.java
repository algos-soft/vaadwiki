package it.algos.vaadflow.application;

import it.algos.vaadflow.backend.login.ALogin;
import it.algos.vaadflow.modules.company.Company;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: gio, 18-ott-2018
 * Time: 10:51
 */
public class AContext {
    private ALogin login;
    private Company company;

    public AContext() {
    } // end of constructor

    public AContext(ALogin login) {
        this(login, (Company) null);
    } // end of constructor

    public AContext(ALogin login, Company company) {
        this.login = login;
        this.company = company;
    } // end of constructor

    public ALogin getLogin() {
        return login;
    }// end of method

    public void setLogin(ALogin login) {
        this.login = login;
    }// end of method

    public Company getCompany() {
        return company;
    }// end of method

    public void setCompany(Company company) {
        this.company = company;
    }// end of method

}// end of class
