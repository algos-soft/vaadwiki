package it.algos.vaadflow.footer;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.backend.login.ALogin;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.ATextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.time.LocalDate;

import static it.algos.vaadflow.application.FlowCost.*;

/**
 * Created by gac on 12/06/17
 * VerticalLayout has 100% width and undefined height by default.
 * Barra inferiore di messaggi all'utente.
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

    private final static String DEVELOPER_COMPANY = "Algos® ";

    //    @Autowired
//    public AHtmlService html;
    private final static LocalDate DATA = LocalDate.now();
    private final static String DEV_TAG = "Dev: ";
    private final static String ADMIN_TAG = "Admin: ";
    private final static String USER_TAG = "User: ";
    /**
     * Inietta da Spring come 'session'
     */
    @Autowired
    @Lazy
    public ALogin login;
    /**
     * Inietta da Spring come 'session'
     */
    @Autowired
    public ATextService text;
    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private PreferenzaService pref;
    private String message = "";
    private Label label;

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

        if (FlowCost.DEBUG) {// @TODO costante provvisoria da sostituire con preferenzeService
//            this.addStyleName("greenBg");
        }// end of if cycle

        this.start();
    }// end of method

    public Label setAppMessage(String message) {
        this.message = message;
        return this.start();
    }// end of method


    public Label start() {
        String message = "";
        String sep = " - ";
        String spazio = " ";
        String tag = "all companies";
        String companyCode = login.getCompany() != null ? login.getCompany().getCode() : "";
        String companyName = login.getCompany() != null ? login.getCompany().getDescrizione() : "";
        String userName = login.getUtente() != null ? login.getUtente().getUserName() : "";
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

//        label = new LabelRosso(DEVELOPER_NAME + message);

        String data;
        message = DEVELOPER_COMPANY + sep + PROJECT_NAME;
        message += spazio;
        message += PROJECT_VERSION;
        message += " del ";
        message += DATA.toString();
        if (text.isValid(companyName)) {
            message += sep;
            message += companyName;
        }// end of if cycle
        if (text.isValid(userName)) {
            message += sep;
            message += "loggato come " + userName;
//            switch (login.getTypeLogged()) {
//                case user:
//                    message += sep;
//                    message += USER_TAG + userName;
//                    break;
//                case admin:
//                    message += sep;
//                    message += ADMIN_TAG + userName;
//                    break;
//                case developer:
//                    message += sep;
//                    message += DEV_TAG + userName;
//                    break;
//                default:
//                    break;
//            } // end of switch statement
        }// end of if cycle
        label = new Label(message);
        this.add(label);

        return label;
    }// end of method

}// end of class
