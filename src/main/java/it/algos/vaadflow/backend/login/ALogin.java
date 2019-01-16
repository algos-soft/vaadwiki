package it.algos.vaadflow.backend.login;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import it.algos.vaadflow.modules.company.Company;
import it.algos.vaadflow.modules.utente.Utente;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: gio, 10-mag-2018
 * Time: 16:23
 */
@SpringComponent
//@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@VaadinSessionScope
public class ALogin {

    private Utente utente;

    private Company company;

    private boolean developer = false;

    private boolean admin = false;

//    @Autowired
//    private RoleService roleService;


    public Utente getUtente() {
        return utente;
    }// end of method


    public void setUtente(Utente utente) {
        this.setUtenteAndCompany(utente, (Company) null);
    }// end of method


    public void setUtenteAndCompany(Utente utente, Company company) {
        this.utente = utente;
        this.company = company;
        this.developer = false;
        this.admin = false;

        if (utente != null) {
            this.admin = utente.isAdmin();
            this.developer = utente.isDev();
        }// end of if cycle
    }// end of method


    public Company getCompany() {
        return company;
    }// end of method


    public void setCompany(Company company) {
        this.company = company;
    }// end of method


    public boolean isDeveloper() {
        return developer;
    }// end of method


    public void setDeveloper(boolean developer) {
        this.developer = developer;
    }// end of method


    public boolean isAdmin() {
        return admin;
    }// end of method


    public void setAdmin(boolean admin) {
        this.admin = admin;
    }// end of method


    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }// end of method


    /**
     * Checks if the user is logged in.
     *
     * @return true if the user is logged in. False otherwise.
     */
    public boolean isUserLoggedIn() {
        Authentication authentication = getAuthentication();

        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken);
    }// end of method

}// end of class
