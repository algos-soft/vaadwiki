package it.algos.vaadwiki.liste;

import com.vaadin.flow.component.UI;
import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadflow.modules.giorno.GiornoService;
import it.algos.vaadwiki.modules.wiki.WikiGiornoViewList;
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
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected GiornoService giornoService;


    protected Giorno giorno;


}// end of class
