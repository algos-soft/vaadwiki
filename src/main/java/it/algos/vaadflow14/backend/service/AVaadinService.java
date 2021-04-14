package it.algos.vaadflow14.backend.service;

import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WebBrowser;
import com.vaadin.flow.server.WrappedSession;
import it.algos.vaadflow14.backend.application.FlowVar;
import it.algos.vaadflow14.backend.login.ALogin;
import it.algos.vaadflow14.backend.packages.company.Company;
import it.algos.vaadflow14.backend.packages.security.utente.Utente;
import it.algos.vaadflow14.backend.packages.security.utente.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import static it.algos.vaadflow14.backend.application.FlowCost.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: dom, 23-ago-2020
 * Time: 08:04
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti SpringBoot non la costruisce <br>
 * Estende la classe astratta AAbstractService che mantiene i riferimenti agli altri services <br>
 * L'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(AAnnotationService.class); <br>
 * 3) @Autowired public AArrayService annotation; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AVaadinService extends AAbstractService {

    /**
     * versione della classe per la serializzazione
     */
    private static final long serialVersionUID = 1L;


    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public UtenteService utenteService;

//    /**
//     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
//     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
//     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
//     */
//    @Autowired
//    public CompanyLogic companyLogic;


    /**
     *
     */
    public void fixLogin() {
        VaadinSession vaadSession = VaadinSession.getCurrent();
        String username;
        Utente utente;
        Company company;
        ALogin login = null;

        //--potrebbe non esiste ancora la sessione
        if (vaadSession == null) {
            return;
        }

        //--l' applicazione NON usa Spring Security
        //--potrebbe essere arrivato qui (per errore di programmazione) senza aver controllato il flag
        if (!FlowVar.usaSecurity) {
            return;
        }

        //--indipendentemente dal login, controlla il browser che si è collegato
        //--l' informazione viene mentenuta nella sessione
        //--vale anche per un applicazione con usaCompany=false oppure con usaSecurity=false
        fixBrowser();

        if (isNotLogin()) {
            username = getLoggedUsername();
            utente = utenteService.findByUser(username);

            if (utente != null) {
                if (FlowVar.usaCompany) {
                    company = utente.company;
                    if (company != null) {
                        vaadSession.setAttribute(KEY_SESSION_LOGIN, new ALogin(utente, company));
                    } else {
                        logger.warn("Manca la company dell' utente", this.getClass(), "fixLogin");
                    }
                } else {
                    vaadSession.setAttribute(KEY_SESSION_LOGIN, new ALogin(utente));
                }
            } else {
                logger.warn("Manca l' utente loggato", this.getClass(), "fixLogin");
            }
        }
    }


    /**
     * Restituisce l' username dell' utente loggato <br>
     *
     * @return username utente loggato
     */
    public String getLoggedUsername() {
        String username = VUOTA;
        VaadinSession session = VaadinSession.getCurrent();
        WrappedSession wrappedSession = session.getSession();
        SecurityContext securityContext = (SecurityContext) wrappedSession.getAttribute(KEY_SECURITY_CONTEXT);

        if (securityContext != null) {
            User springUser = (User) securityContext.getAuthentication().getPrincipal();
            username = springUser.getUsername();
        }

        return username;
    }


    /**
     * Restituisce il login, se esiste la sessione e se il login è registrato <br>
     *
     * @return istanza di login
     */
    public ALogin getLogin() {
        VaadinSession vaadSession = VaadinSession.getCurrent();
        return vaadSession != null ? (ALogin) vaadSession.getAttribute(KEY_SESSION_LOGIN) : null;
    }


    /**
     * Controlla se esiste un login registrato nella sessione <br>
     *
     * @return true se esiste un login della sessione
     */
    public boolean isLogin() {
        return getLogin() != null;
    }


    /**
     * Controlla se non esiste un login registrato nella sessione <br>
     *
     * @return true se non esiste un login della sessione
     */
    public boolean isNotLogin() {
        return getLogin() == null;
    }


    /**
     * Restituisce l' utente, se esiste la sessione, se il login è registrato e se l' utente è valido <br>
     *
     * @return utente loggato
     */
    public Utente getUtente() {
        ALogin login = getLogin();
        return login != null ? login.getUtente() : null;
    }


    /**
     * Restituisce la company, se esiste la sessione, se il login è registrato e se la company è valida <br>
     *
     * @return company loggato
     */
    public Company getCompany() {
        ALogin login = getLogin();
        return login != null ? login.getCompany() : null;
    }


    public boolean isDeveloper() {
        ALogin login = getLogin();
        return login != null && login.isDeveloper();
    }


    public boolean isAdmin() {
        ALogin login = getLogin();
        return login != null && login.isAdmin();
    }


    public boolean isAdminOrDeveloper() {
        return isAdmin() || isDeveloper();
    }


    /**
     * indipendentemente dal login, controlla il browser che si è collegato <br>
     * l' informazione viene mentenuta nella sessione <br>
     * vale anche per un applicazione con usaCompany=false oppure con usaSecurity=false <br>
     */
    public void fixBrowser() {
        VaadinSession vaadSession = VaadinSession.getCurrent();
        WebBrowser webBrowser;
        boolean isMobile = false;

        if (vaadSession == null) {
            return;
        }

        webBrowser = vaadSession.getBrowser();
        if (webBrowser != null) {
            if (webBrowser.isAndroid() || webBrowser.isIPhone() || webBrowser.isWindowsPhone()) {
                isMobile = true;
            }
        }
        vaadSession.setAttribute(KEY_SESSION_MOBILE, isMobile);
    }

}