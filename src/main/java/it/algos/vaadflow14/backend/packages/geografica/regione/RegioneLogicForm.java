package it.algos.vaadflow14.backend.packages.geografica.regione;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.*;
import it.algos.vaadflow14.backend.annotation.*;
import it.algos.vaadflow14.backend.logic.*;
import it.algos.vaadflow14.backend.packages.geografica.provincia.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.ui.*;
import it.algos.vaadflow14.ui.enumeration.*;
import it.algos.vaadflow14.ui.interfaces.*;
import it.algos.vaadflow14.wizard.enumeration.*;
import org.springframework.beans.factory.annotation.*;

import java.util.*;

import static it.algos.vaadflow14.backend.application.FlowCost.VUOTA;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import com.vaadin.flow.component.textfield.TextField;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: mar, 25-mag-2021
 * Time: 20:15
 * <p>
 */
//Vaadin flow
@Route(value = "regioneForm", layout = MainLayout.class)
//Algos
@AIScript(sovraScrivibile = false, doc = AEWizDoc.inizioRevisione)
public class RegioneLogicForm extends LogicForm {


    /**
     * Versione della classe per la serializzazione
     */
    private static final long serialVersionUID = 1L;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ProvinciaService provinciaService;

    /**
     * Costruttore senza parametri <br>
     * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
     */
    public RegioneLogicForm(@Qualifier("regioneService") AIService entityService) {
        super.entityClazz = Regione.class;
        super.entityService = entityService;
    }// end of Vaadin/@Route constructor


    /**
     * Preferenze usate da questa 'logica' <br>
     * Primo metodo chiamato dopo init() (implicito del costruttore) e postConstruct() (facoltativo) <br>
     * Puo essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.wikiPageTitle = ((Regione) entityBean).divisione;
    }


    /**
     * Costruisce una lista di bottoni (enumeration) <br>
     * Di default costruisce (come da flag) i bottoni 'delete' e 'reset' <br>
     * Può essere sovrascritto. Invocare PRIMA il metodo della superclasse <br>
     */
    protected List<AIButton> getListaAEBottoniTop() {
        List<AIButton> listaBottoni = new ArrayList<>();

        listaBottoni.add(AEButton.wiki);

        return listaBottoni;
    }


}// end of Route class



