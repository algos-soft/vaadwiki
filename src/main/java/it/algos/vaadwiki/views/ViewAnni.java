package it.algos.vaadwiki.views;

import com.vaadin.flow.router.BeforeEvent;
import it.algos.vaadflow.modules.anno.Anno;
import it.algos.vaadflow.modules.anno.AnnoService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Wed, 29-May-2019
 * Time: 22:13
 * <p>
 * Classe astratta per la visualizzazione di una lista di prova di biografie di un particolare anno <br>
 * Viene invocata da WikiAnnoViewList <br>
 * Due sottoclassi (concrete) per i Nati e per i Morti <br>
 */
public abstract class ViewAnni extends ViewListe {


    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected AnnoService annoService;


    //--property
    protected Anno anno;


    /**
     * Recupera il giorno arrivato come parametro nella chiamata del browser effettuata da @Route <br>
     *
     * @param event       con la Location, segments, target, source, ecc
     * @param annoIdKey per recuperare l'istanza di Anno
     */
    @Override
    public void setParameter(BeforeEvent event, String annoIdKey) {
        this.anno = annoService.findById(annoIdKey);
        this.inizia();
    }// end of method

}// end of class
