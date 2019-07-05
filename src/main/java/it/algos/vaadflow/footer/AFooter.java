package it.algos.vaadflow.footer;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.backend.login.ALogin;
import it.algos.vaadflow.enumeration.EAColor;
import it.algos.vaadflow.enumeration.EATime;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadflow.service.AVaadinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

import static it.algos.vaadflow.application.FlowCost.*;

/**
 * Created by gac on 12/06/17
 * <p>
 * Barra inferiore di messaggi all'utente.
 * <p>
 * VerticalLayout has 100% width and undefined height by default.
 * Può essere visibile o nascosta a seconda del flag booleano KEY_DISPLAY_FOOTER_INFO
 * La visibilità viene gestita da AlgosUI
 * Tipicamente dovrebbe mostrare:
 * Copyright di Algos
 * Nome dell'applicazione
 * Versione dell'applicazione
 * Livello di accesso dell'utente loggato (developer, admin, utente) eventualmente oscurato per l'utente semplice
 * Company selezionata (nel caso di applicazione multiCompany)
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AFooter extends VerticalLayout {


    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public ATextService text = ATextService.getInstance();

    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public ADateService date = ADateService.getInstance();

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile SOLO DOPO @PostConstruct <br>
     */
    @Autowired
    protected AVaadinService vaadinService;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile SOLO DOPO @PostConstruct <br>
     */
    @Autowired
    private PreferenzaService pref;


    /**
     * Metodo invocato subito DOPO il costruttore
     * <p>
     * Performing the initialization in a constructor is not suggested
     * as the state of the UI is not properly set up when the constructor is invoked.
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti,
     * ma l'ordine con cui vengono chiamati NON è garantito
     */
    @PostConstruct
    protected void inizia() {
        this.setMargin(false);
        this.setSpacing(false);
        this.setPadding(false);

        //--Context e login della sessione
        //--Recuperato dalla sessione, quando la @route fa partire la UI. <br>
        //--Viene regolato nel service specifico (AVaadinService) <br>
        AContext context = vaadinService.fixLoginAndContext();
        ALogin login = context.getLogin();

        String message = "";
        Label label;
        Label labelDebug;
        String sep = " - ";
        String spazio = " ";
        String tag = "all companies";
        String companyCode = login.getCompany() != null ? login.getCompany().getCode() : "";
        String companyName = login.getCompany() != null ? login.getCompany().getDescrizione() : "";
        String userName = login.getUtente() != null ? login.getUtente().getUsername() : "";
        this.removeAll();

        if (pref.isBool(USA_COMPANY)) {
            if (text.isValid(companyCode)) {
                message += " - " + companyCode;
            } else {
                message += " - " + tag;
            }// end of if/else cycle
        }// end of if cycle

        if (login.isDeveloper()) {
            message += " (dev)";
        } else {
            if (login.isAdmin()) {
                message += " (admin)";
            } else {
                message += " (buttonUser)";
            }// end of if/else cycle
        }// end of if/else cycle

        message = DEVELOPER_COMPANY + sep + PROJECT_NAME;
        message += spazio;
        message += PROJECT_VERSION;
        message += " del ";
        message += date.get(PROJECT_DATE, EATime.normal);
        if (text.isValid(PROJECT_NOTE)) {
            message += " " + PROJECT_NOTE;
        }// end of if cycle
        if (text.isValid(companyName)) {
            message += sep;
            message += companyName;
        }// end of if cycle
        if (text.isValid(userName)) {
            message += sep;
            message += "loggato come " + userName;
        }// end of if cycle
        label = new Label(message);
        this.add(label);

        label.getStyle().set("font-size", "small");
        label.getStyle().set("font-weight", "bold");
        if (label != null) {
            if (login.getRoleType() != null) {
                switch (login.getRoleType()) {
                    case user:
                        label.getElement().getStyle().set("color", "green");
                        break;
                    case admin:
                        label.getElement().getStyle().set("color", "blue");
                        break;
                    case developer:
                        label.getElement().getStyle().set("color", "red");
                        break;
                    default:
                        break;
                } // end of switch statement
            }// end of if cycle
        }// end of if cycle

        this.add(label);
        if (pref.isBool(USA_DEBUG)) {
            labelDebug= new Label("Sei in modalità DEBUG");
            labelDebug.getStyle().set("font-size", "small");
            labelDebug.getStyle().set("font-weight", "bold");
            labelDebug.getElement().getStyle().set("color", "red");
            this.getElement().getStyle().set("background-color", EAColor.lime.getEsadecimale());
            this.add(labelDebug);
        }// end of if cycle

    }// end of method



}// end of class
