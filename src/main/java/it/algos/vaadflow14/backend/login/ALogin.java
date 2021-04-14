package it.algos.vaadflow14.backend.login;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import it.algos.vaadflow14.backend.enumeration.AERole;
import it.algos.vaadflow14.backend.packages.company.Company;
import it.algos.vaadflow14.backend.packages.security.utente.Utente;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static it.algos.vaadflow14.backend.application.FlowCost.TAG_LOGIN;


/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: gio, 10-mag-2018
 * Time: 16:23
 * Viene creata un' istanza specifica di ogni sessione <br>
 * L' istanza viene creata in AVaadinService.fixLogin() <br>
 * L' istanza viene resa disponibile in AVaadinService.getLogin() <br>
 * <p>
 * Le property sono 'final' perché nella sessione non è possibile cambiare utente loggato o company <br>
 * Ha senso solo se FlowVar.usaSecurity=true <br>
 * Mantiene un flag se manca la company quando FlowVar.usaCompany=false <br>
 */
@SpringComponent
@VaadinSessionScope
@Qualifier(TAG_LOGIN)
public class ALogin {

    final private Utente utente;

    final private Company company;

    //    private EARoleType roleType;


    /**
     * Costruttore con parametri <br>
     *
     * @param utente obbligatorio
     */
    public ALogin(final Utente utente) {
        this(utente, null);
    } // end of SpringBoot constructor


    /**
     * Costruttore con parametri <br>
     *
     * @param utente  obbligatorio
     * @param company obbligatoria solo se FlowVar.usaCompany=true
     */
    public ALogin(final Utente utente, final Company company) {
        this.utente = utente;
        this.company = company;
    } // end of SpringBoot constructor


    public Utente getUtente() {
        return utente;
    }


    public Company getCompany() {
        return company;
    }


    //    public EARoleType getRoleType() {
    //        return roleType;
    //    }// end of method
    //
    //
    //    public void setRoleType(EARoleType roleType) {
    //        this.roleType = roleType;
    //    }// end of method


    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }


    //    public boolean isDeveloper() {
    //        return roleType == null || roleType == EARoleType.developer;
    //    }// end of method
    //
    //
    //    public boolean isAdmin() {
    //        return roleType == EARoleType.admin;
    //    }// end of method


    public boolean isDeveloper() {
        return utente.role == AERole.developer;
    }


    public boolean isAdmin() {
        return utente.role == AERole.admin;
    }


    public boolean isUser() {
        return utente.role == AERole.user;
    }


    /**
     * Checks if the user is logged in.
     *
     * @return true if the user is logged in. False otherwise.
     */
    public boolean isUserLoggedIn() {
        Authentication authentication = getAuthentication();
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken);
    }

}
