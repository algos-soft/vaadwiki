package it.algos.vaadflow14.ui.footer;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow14.backend.application.FlowVar;
import it.algos.vaadflow14.backend.enumeration.AEPreferenza;
import it.algos.vaadflow14.backend.service.DateService;
import it.algos.vaadflow14.backend.service.TextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

import static it.algos.vaadflow14.backend.application.FlowCost.*;
import static it.algos.vaadflow14.backend.application.FlowVar.*;


/**
 * Created by gac on 12/06/17
 * <p>
 * Barra inferiore di messaggi per l'utilizzatore.
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
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public DateService date;


    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public TextService text;

    //    /**
    //     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
    //     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
    //     */
    //    @Autowired
    //    public ADateService date;
    //
    //    /**
    //     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
    //     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
    //     * Disponibile SOLO DOPO @PostConstruct <br>
    //     */
    //    @Autowired
    //    protected AVaadinService vaadinService;

    //    /**
    //     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
    //     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
    //     * Disponibile SOLO DOPO @PostConstruct <br>
    //     */
    //    @Autowired
    //    private PreferenzaService pref;


    /**
     * Questa classe viene costruita partendo da @Route e non da SprinBoot <br>
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * <p>
     * Prima viene chiamato il costruttore <br>
     * Prima viene chiamato init(); <br>
     * Viene chiamato @PostConstruct (con qualsiasi firma) <br>
     * Dopo viene chiamato setParameter(); <br>
     * Dopo viene chiamato beforeEnter(); <br>
     * <p>
     * Le preferenze vengono (eventualmente) lette da mongo e (eventualmente) sovrascritte nella sottoclasse
     * Creazione e posizionamento dei componenti UI <br>
     * Possono essere sovrascritti nelle sottoclassi <br>
     */
    @PostConstruct
    protected void postConstruct() {
        this.setMargin(false);
        this.setSpacing(false);
        this.setPadding(false);

        //--Context e login della sessione
        //--Recuperato dalla sessione, quando la @route fa partire la UI. <br>
        //--Viene regolato nel service specifico (AVaadinService) <br>
        //        AContext context = vaadinService.getSessionContext();
        //        ALogin login = context.getLogin();

        String message = VUOTA;
        Label label;
        Label labelDebug;
        String tag = "all companies";
        //        String companyCode = login.getCompany() != null ? login.getCompany().getCode() : "";
        //        String companyName = login.getCompany() != null ? login.getCompany().getDescrizione() : "";
        //        String userName = login.getUtente() != null ? login.getUtente().getUsername() : "";
        this.removeAll();

        //        if (usaCompany) {
        //            if (text.isValid(companyCode)) {
        //                message += " - " + companyCode;
        //            } else {
        //                message += " - " + tag;
        //            }// end of if/else cycle
        //        }// end of if cycle

        //        if (login.isDeveloper()) {
        //            message += " (dev)";
        //        } else {
        //            if (login.isAdmin()) {
        //                message += " (admin)";
        //            } else {
        //                message += " (buttonUser)";
        //            }// end of if/else cycle
        //        }// end of if/else cycle

        message = DEVELOPER_COMPANY + SEP + projectNameUpper;
        message += SPAZIO;
        message += FlowVar.projectVersion;
        message += " del ";
        message += date.get(versionDate);
        if (AEPreferenza.usaDebug.is() && text.isValid(projectNote)) {
            message += SPAZIO;
            message += " " + projectNote;
        }

        //        if (text.isValid(companyName)) {
        //            message += sep;
        //            message += companyName;
        //        }// end of if cycle
        //        if (text.isValid(userName)) {
        //            message += sep;
        //            message += "loggato come " + userName;
        //        }// end of if cycle

        //        label = new Label(message);
        //
        //        label.getStyle().set("font-size", "small");
        //        label.getStyle().set("font-weight", "bold");
        //        if (label != null) {
        //            if (login.getRoleType() != null) {
        //                switch (login.getRoleType()) {
        //                    case user:
        //                        label.getElement().getStyle().set("color", "green");
        //                        break;
        //                    case admin:
        //                        label.getElement().getStyle().set("color", "blue");
        //                        break;
        //                    case developer:
        //                        label.getElement().getStyle().set("color", "red");
        //                        break;
        //                    default:
        //                        break;
        //                } // end of switch statement
        //            }// end of if cycle
        //        }// end of if cycle
        //
        //        this.add(label);
        //        if (pref.isBool(USA_DEBUG)) {
        //            labelDebug= new Label("Sei in modalità DEBUG");
        //            labelDebug.getStyle().set("font-size", "small");
        //            labelDebug.getStyle().set("font-weight", "bold");
        //            labelDebug.getElement().getStyle().set("color", "red");
        //            this.getElement().getStyle().set("background-color", EAColor.lime.getEsadecimale());
        //            this.add(labelDebug);
        //        }// end of if cycle
        //

        if (FlowVar.usaSecurity) {
            this.add(text.getLabelUserSmall(message));//@todo Funzionalità ancora da implementare
        } else {
            this.add(text.getLabelUserSmall(message));
        }
    }


}
