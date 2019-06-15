package it.algos.vaadwiki.views;

import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.Route;
import it.algos.vaadwiki.modules.nome.Nome;
import it.algos.vaadwiki.modules.nome.NomeService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static it.algos.vaadflow.application.FlowCost.A_CAPO;
import static it.algos.vaadwiki.application.WikiCost.ROUTE_VIEW_NOMI;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Fri, 07-Jun-2019
 * Time: 19:00
 * <p>
 * Classe per la visualizzazione di una lista di prova di biografie di un particolare nome <br>
 * Viene invocata da NomeViewList <br>
 * Eliminato header e footer della pagina definitiva su wiki <br>
 * Lista delle biografie di un Nome <br>
 */
@Route(value = ROUTE_VIEW_NOMI)
public class ViewNome extends ViewListe {

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected NomeService nomeService;


    //--property
    protected Nome nome;


    /**
     * Recupera il nome arrivato come parametro nella chiamata del browser effettuata da @Route <br>
     *
     * @param nomeIdKey per recuperare l'istanza di Nome
     */
    @Override
    public void setParameter(BeforeEvent event, String nomeIdKey) {
        this.nome = nomeService.findById(nomeIdKey);
        this.inizia();
    }// end of method


    /**
     * Costruisce una mappa di tutte le didascalie relative al nome considerato <br>
     * Presenta le righe secondo uno dei possibili metodi di raggruppamento <br>
     * Deve essere sovrascritto nella sottoclassse concreta <br>
     * Dopo deve invocare il metodo della superclasse <br>
     */
    public void inizia() {
        LinkedHashMap<String, ArrayList<String>> mappa;

        mappa = listaService.getMappaNomi(nome);
        super.testo = listaService.righeSemplici(mappa);

        super.numVoci = listaService.getMappaSize(mappa);
        super.inizia();
    }// end of method


    /**
     * Costruisce il titolo della pagina <br>
     * Sovrascritto <br>
     */
    protected void addTitolo() {
        String titolo = "Lista biografie di " + text.format(numVoci) + " persone di nome " + nome.getNome() + A_CAPO + A_CAPO;
        super.testo = titolo + super.testo;
    }// end of method

}// end of class
