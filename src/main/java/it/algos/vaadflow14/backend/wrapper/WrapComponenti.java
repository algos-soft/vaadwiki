package it.algos.vaadflow14.backend.wrapper;

import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.backend.logic.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: mer, 26-mag-2021
 * Time: 18:57
 * Wrap di informazioni usato da ALogic per la creazione di ATopLayout e ABottomLayout <br>
 * La ALogic mantiene lo stato ed elabora informazioni che verranno usate da ATopLayout e ABottomLayout <br>
 * Obbligatoria la AILogic con cui regolare i listener per l'evento/azione da eseguire <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WrapComponenti {


    /**
     * La AILogic con cui regolare i listener per l'evento/azione da eseguire
     */
    private AILogic entityLogic;

    /**
     * Mappa di componenti di selezione e filtro <br>
     */
    private Map<String, Object> mappaComponenti;

    /**
     * Property per il numero di bottoni nella prima riga sopra la Grid (facoltativa)
     */
    private int maxNumeroBottoniPrimaRiga;


    public WrapComponenti(final AILogic entityLogic, final Map<String, Object> mappaComponenti) {
        this(entityLogic, mappaComponenti, 0);
    }


    public WrapComponenti(final AILogic entityLogic, final Map<String, Object> mappaComponenti, int maxNumeroBottoniPrimaRiga) {
        this.entityLogic = entityLogic;
        this.mappaComponenti = mappaComponenti;
        this.maxNumeroBottoniPrimaRiga = maxNumeroBottoniPrimaRiga;
    }


    public AILogic getEntityLogic() {
        return entityLogic;
    }


    public int getMaxNumeroBottoniPrimaRiga() {
        return maxNumeroBottoniPrimaRiga;
    }


    public Map<String, Object> getMappaComponenti() {
        return mappaComponenti;
    }

}
