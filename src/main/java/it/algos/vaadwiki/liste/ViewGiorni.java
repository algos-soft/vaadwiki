package it.algos.vaadwiki.liste;

import com.vaadin.flow.router.BeforeEvent;
import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadflow.modules.giorno.GiornoService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Tue, 28-May-2019
 * Time: 05:38
 * <p>
 * Classe astratta per la visualizzazione di una lista di prova di biografie di un particolare giorno <br>
 * Viene invocata da WikiGiornoViewList <br>
 * Eliminato header e footer della pagina definitiva su wiki <br>
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


    protected Giorno giorno;


    /**
     * Recupera il giorno arrivato come parametro nella chiamata del browser effettuata da @Route <br>
     *
     * @param giornoIdKey per recuperare l'istanza di Giorno
     */
    @Override
    public void setParameter(BeforeEvent event, String giornoIdKey) {
        this.giorno = giornoService.findById(giornoIdKey);
        this.inizia();
    }// end of method

}// end of class
