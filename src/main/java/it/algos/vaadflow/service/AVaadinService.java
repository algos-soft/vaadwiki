package it.algos.vaadflow.service;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.backend.login.ALogin;
import it.algos.vaadflow.modules.company.Company;
import it.algos.vaadflow.modules.utente.Utente;
import it.algos.vaadflow.modules.utente.UtenteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

import static it.algos.vaadflow.application.FlowCost.KEY_CONTEXT;
import static it.algos.vaadflow.application.FlowCost.KEY_SECURITY_CONTEXT;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: mer, 31-ott-2018
 * Time: 09:16
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class AVaadinService {


    /**
     * L'istanza viene  dichiarata nel costruttore @Autowired <br>
     */
    private UtenteService utenteService;


    /**
     * Costruttore @Autowired <br>
     */
    public AVaadinService(UtenteService utenteService) {
        this.utenteService = utenteService;
    }// end of Spring constructor


    /**
     * Crea il login ed il context <br>
     * Controlla che non esista già il context nella vaadSession
     * Invocato quando la @route fa partire la AViewList. <br>
     * (non è chiaro se passa prima da MainLayout o da AViewList o da AViewDialog) <br>
     * <p>
     * Recupera l'user dall'attributo della sessione HttpSession al termine della security <br>
     * Crea il login <br>
     * Crea il context <br>
     * Inserisce il context come attributo nella vaadSession <br>
     */
    public AContext fixLoginAndContext() {
        return fixLoginAndContext((ALogin) null);
    }// end of method


    /**
     * Crea il login ed il context <br>
     * Controlla che non esista già il context nella vaadSession
     * Invocato quando la @route fa partire la AViewList. <br>
     * (non è chiaro se passa prima da MainLayout o da AViewList o da AViewDialog) <br>
     * <p>
     * Recupera l'user dall'attributo della sessione HttpSession al termine della security <br>
     * Crea il login <br>
     * Crea il context <br>
     * Inserisce il context come attributo nella vaadSession <br>
     */
    public AContext fixLoginAndContext(ALogin login) {
        AContext context;
        String uniqueUserName = "";
        Utente utente;
        Company company;
        VaadinSession vaadSession = UI.getCurrent().getSession();

        context = (AContext) vaadSession.getAttribute(KEY_CONTEXT);
        if (context == null) {
            if (login == null) {
                login = new ALogin();
            }// end of if cycle

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession httpSession = attr.getRequest().getSession(true);
            SecurityContext securityContext = (SecurityContext) httpSession.getAttribute(KEY_SECURITY_CONTEXT);
            User springUser = (User) securityContext.getAuthentication().getPrincipal();
            uniqueUserName = springUser.getUsername();
            utente = utenteService.findByKeyUnica(uniqueUserName);

            login.setUtente(utente);
            company = utente.company;
            context = new AContext(login, company);
            vaadSession.setAttribute(KEY_CONTEXT, context);
        }// end of if cycle

        return context;
    }// end of method

}// end of class
