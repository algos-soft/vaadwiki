package it.algos.vaadwiki.views;

import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.Route;
import it.algos.vaadwiki.liste.ListaGiornoNato;
import it.algos.vaadwiki.liste.ListaNomi;
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
     * Punto di ingresso dopo la chiamata navigate() effettuata da com.vaadin.flow.router.Router verso questa view <br>
     *
     * @param event       con la Location, segments, target, source, ecc
     * @param nomeIdKey per recuperare l'istanza di Nome
     */
    @Override
    public void setParameter(BeforeEvent event, String nomeIdKey) {
        this.nome = nomeService.findById(nomeIdKey);
        this.inizia();
    }// end of method


    /**
     * Costruisce il testo con tutte le didascalie relative al giorno considerato <br>
     * Presenta le righe secondo uno dei possibili metodi di raggruppamento <br>
     * Deve essere sovrascritto nella sottoclassse concreta <br>
     * Dopo DEVE invocare il metodo della superclasse <br>
     */
    @Override
    public void inizia() {
        lista = appContext.getBean(ListaNomi.class, nome);
        super.inizia();
    }// end of method


    /**
     * Costruisce il titolo della pagina <br>
     * Sovrascritto <br>
     */
    @Override
    protected String addTitolo() {
        return "Lista biografie di " + text.format(numVoci) + " persone di nome " + nome.getNome() + A_CAPO;
    }// end of method

}// end of class
