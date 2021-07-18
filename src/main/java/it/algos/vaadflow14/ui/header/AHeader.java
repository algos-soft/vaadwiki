package it.algos.vaadflow14.ui.header;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import it.algos.vaadflow14.backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: dom, 02-ago-2020
 * Time: 11:10
 */
@Deprecated
public abstract class AHeader extends VerticalLayout {

    protected AlertWrap alertWrap;

    protected List<String> alertBlack;

    protected List<String> alertGreen;

    protected List<String> alertBlue;

    protected List<String> alertRed;

    protected List<String> alertUser;

    protected List<String> alertAdmin;

    protected List<String> alertDev;

    protected List<String> alertDevAll;

    protected List<String> alertParticolare;

    protected List<String> alertHtml;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    protected ArrayService array;


    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    protected TextService text;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    protected HtmlService html;


    protected void initView() {
        this.fixProperties();
        this.fixAlertWrap();
        this.fixView();
    }


    /**
     * Regola alcune properties (grafiche e non grafiche) <br>
     * Regola la business logic di questa classe <br>
     */
    protected void fixProperties() {
        this.setMargin(false);
        this.setSpacing(false);
        this.setPadding(false);
    }


    public void fixAlertWrap() {
        alertBlack = new ArrayList<>();
        alertGreen = new ArrayList<>();
        alertBlue = new ArrayList<>();
        alertRed = new ArrayList<>();

        alertUser = new ArrayList<>();
        alertAdmin = new ArrayList<>();
        alertDev = new ArrayList<>();
        alertDevAll = new ArrayList<>();
        alertParticolare = new ArrayList<>();

        if (alertWrap != null) {
            alertBlack = alertWrap.getAlertBlack();
            alertGreen = alertWrap.getAlertGreen();
            alertBlue = alertWrap.getAlertBlue();
            alertRed = alertWrap.getAlertRed();

            alertUser = alertWrap.getAlertUser();
            alertAdmin = alertWrap.getAlertAdmin();
            alertDev = alertWrap.getAlertDev();
            alertDevAll = alertWrap.getAlertDevAll();
            alertParticolare = alertWrap.getAlertParticolare();
        }
    }


    /**
     * Costruisce graficamente la view <br>
     */
    protected void fixView() {
    }

}
