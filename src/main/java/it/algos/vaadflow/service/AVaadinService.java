package it.algos.vaadflow.service;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.backend.login.ALogin;
import it.algos.vaadflow.modules.company.Company;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.modules.utente.Utente;
import it.algos.vaadflow.modules.utente.UtenteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

import static it.algos.vaadflow.application.FlowCost.*;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: mer, 31-ott-2018
 * Time: 09:16
 * Implementa il 'pattern' SINGLETON; l'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(AVaadinService.class); <br>
 * 2) AVaadinService.getInstance(); <br>
 * 3) @Autowired private AVaadinService vaadinService; <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class AVaadinService {


    /**
     * Private final property
     */
    private static final AVaadinService INSTANCE = new AVaadinService();


    @Autowired
    ApplicationContext appContext;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile SOLO DOPO il costruttore <br>
     */
    @Autowired
    private UtenteService utenteService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    public PreferenzaService pref;

    /**
     * Private constructor to avoid client applications to use constructor
     */
    private AVaadinService() {
    }// end of constructor


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
        AContext context;
        ALogin login;
        String uniqueUserName = "";
        Utente utente;
        Company company = null;
        VaadinSession vaadSession = UI.getCurrent().getSession();
        User springUser;
        boolean secured = false;

        context = (AContext) vaadSession.getAttribute(KEY_CONTEXT);
        if (context == null) {
            login = appContext.getBean(ALogin.class);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession httpSession = attr.getRequest().getSession(true);
            SecurityContext securityContext = (SecurityContext) httpSession.getAttribute(KEY_SECURITY_CONTEXT);

            if (pref.isBool(USA_SECURITY)) {
                try { // prova ad eseguire il codice
                    springUser = (User) securityContext.getAuthentication().getPrincipal();
                    uniqueUserName = springUser.getUsername();

                    utente = utenteService.findByKeyUnica(uniqueUserName);
                    if (utente != null) {
                        login.setUtenteAndCompany(utente, utente.company);
                        login.setRoleType(utenteService.getRole(utente));
                    }// end of if cycle

                    secured = true;
                } catch (Exception unErrore) { // intercetta l'errore
                    log.error(unErrore.toString());
                }// fine del blocco try-catch
            }// end of if cycle

            context = new AContext(login, company);
            context.setSecured(secured);
            vaadSession.setAttribute(KEY_CONTEXT, context);
        }// end of if cycle

        return context;
    }// end of method


    /**
     * Gets the unique instance of this Singleton.
     *
     * @return the unique instance of this Singleton
     */
    public static AVaadinService getInstance() {
        return INSTANCE;
    }// end of static method

}// end of class
