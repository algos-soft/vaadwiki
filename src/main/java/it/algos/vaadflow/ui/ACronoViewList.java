package it.algos.vaadflow.ui;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import it.algos.vaadflow.presenter.IAPresenter;
import it.algos.vaadflow.ui.dialog.IADialog;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: dom, 14-ott-2018
 * Time: 16:28
 */
//@SpringComponent
//@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
//@Slf4j
public abstract class ACronoViewList extends AViewList {

    /**
     * Costruttore @Autowired (nella sottoclasse concreta) <br>
     * La sottoclasse usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * La sottoclasse usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     */
    public ACronoViewList(IAPresenter presenter, IADialog dialog) {
        super(presenter, dialog);
    }// end of Spring constructor

    /**
     * Le preferenze sovrascritte nella sottoclasse
     */
    @Override
    protected void fixPreferenzeSpecifiche() {
        super.usaSearchTextField = false;
        super.usaSearchBottoneNew = false;
        super.usaBottoneDeleteAll = true;
        super.usaBottoneReset = true;
        super.isEntityDeveloper = true;
    }// end of method

    /**
     * Costruisce un (eventuale) layout per informazioni aggiuntive alla grid ed alla lista di elementi
     * Normalmente ad uso esclusivo del developer
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    @Override
    protected VerticalLayout creaTopAlert() {
        VerticalLayout layout = super.creaTopAlert();
        layout.add(new Label("Serve per costruire liste cronologiche"));
        return layout;
    }// end of method

}// end of class
