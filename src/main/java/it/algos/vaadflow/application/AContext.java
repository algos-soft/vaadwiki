package it.algos.vaadflow.application;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayoutMenu;
import it.algos.vaadflow.backend.login.ALogin;
import it.algos.vaadflow.modules.company.Company;
import it.algos.vaadflow.ui.MainLayout;

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

    private MainLayout mainLayout;

    private AppLayout appLayout;

    private AppLayoutMenu appMenu;


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


    public MainLayout getMainLayout() {
        return mainLayout;
    }// end of method


    public void setMainLayout(MainLayout mainLayout) {
        this.mainLayout = mainLayout;
    }// end of method


    public AppLayout getAppLayout() {
        return appLayout;
    }// end of method


    public void setAppLayout(AppLayout appLayout) {
        this.appLayout = appLayout;
    }// end of method


    public AppLayoutMenu getAppMenu() {
        return appMenu;
    }// end of method


    public void setAppMenu(AppLayoutMenu appMenu) {
        this.appMenu = appMenu;
    }// end of method


    public boolean isDev() {
        return login.isDeveloper();
    }// end of method


    public boolean isAdmin() {
        return login.isAdmin();
    }// end of method

}// end of class
