package it.algos.vaadflow14.backend.packages.geografica.stato;

import com.vaadin.flow.router.*;
import it.algos.vaadflow14.backend.logic.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.ui.*;
import it.algos.vaadflow14.ui.enumeration.*;
import it.algos.vaadflow14.ui.interfaces.*;
import org.springframework.beans.factory.annotation.*;

import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: lun, 15-mar-2021
 * Time: 17:41
 * <p>
 */
@Route(value = "statoForm", layout = MainLayout.class)
public class StatoLogicForm extends LogicForm {


    /**
     * Versione della classe per la serializzazione
     */
    private static final long serialVersionUID = 1L;


    /**
     * Costruttore senza parametri <br>
     * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
     */
    public StatoLogicForm(@Qualifier("StatoService") AIService entityService) {
        super.entityClazz = Stato.class;
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

        super.wikiPageTitle = ((Stato)entityBean).stato;
    }

    /**
     * Costruisce una lista di bottoni (enumeration) <br>
     * Di default costruisce (come da flag) i bottoni 'delete' e 'reset' <br>
     * Può essere sovrascritto. Invocare PRIMA il metodo della superclasse <br>
     */
    protected List<AIButton> getListaAEBottoniTop() {
        return Collections.singletonList(AEButton.wiki);
    }


}// end of Route class



