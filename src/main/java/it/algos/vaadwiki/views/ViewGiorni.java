package it.algos.vaadwiki.views;

import com.vaadin.flow.router.BeforeEvent;
import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadflow.modules.giorno.GiornoService;
import org.springframework.beans.factory.annotation.Autowired;

import static it.algos.vaadwiki.application.WikiCost.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Tue, 28-May-2019
 * Time: 05:38
 * <p>
 * Classe astratta per la visualizzazione di una lista di prova di biografie di un particolare giorno <br>
 * Viene invocata da WikiGiornoViewList <br>
 * Due sottoclassi (concrete) per i Nati e per i Morti <br>
 */
public abstract class ViewGiorni extends ViewListe {


    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected GiornoService giornoService;


    //--property
    protected Giorno giorno;


    /**
     * Punto di ingresso dopo la chiamata navigate() effettuata da com.vaadin.flow.router.Router verso questa view <br>
     *
     * @param event       con la Location, segments, target, source, ecc
     * @param giornoIdKey per recuperare l'istanza di Giorno
     */
    public void setParameter(BeforeEvent event, String giornoIdKey) {
        this.giorno = giornoService.findById(giornoIdKey);
        this.inizia();
    }// end of method


}// end of class
