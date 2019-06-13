package it.algos.vaadwiki.upload;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadwiki.liste.ListaGiornoNato;
import it.algos.vaadwiki.service.LibBio;
import it.algos.wiki.LibWiki;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import static it.algos.vaadflow.application.FlowCost.SPAZIO;
import static it.algos.vaadflow.application.FlowCost.VUOTA;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: gio, 24-gen-2019
 * Time: 08:21
 * <p>
 * Classe specializzata per caricare (upload) le liste sul server wiki. <br>
 * <p>
 * Viene chiamato da Scheduler (con frequenza giornaliera ?) <br>
 * Può essere invocato dal bottone 'Upload all' della classe WikiGiornoViewList <br>
 * Necessita del login come bot <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class UploadGiornoNato extends UploadGiorni {


    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso il parametro Bio del costruttore usato <br>
     */
    public UploadGiornoNato() {
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(UploadGiornoNato.class, giorno) <br>
     *
     * @param giorno di cui costruire la pagina sul server wiki
     */
    public UploadGiornoNato(Giorno giorno) {
        super(giorno);
    }// end of constructor


    /**
     * Titolo della pagina da creare/caricare su wikipedia
     * Sovrascritto
     */
    @Override
    protected void elaboraTitolo() {
        if (giorno != null) {
            titoloPagina = getTitoloPagina(giorno);
        }// fine del blocco if
    }// fine del metodo


    /**
     * Titolo della pagina Nati/Morti da creare/caricare su wikipedia
     */
    public String getTitoloPagina(Giorno giorno) {
        return libBio.getTitoloGiornoNato(giorno);
    }// fine del metodo


    /**
     * Costruisce una mappa di liste di didascalie che hanno una valore valido per la pagina specifica <br>
     * La mappa è composta da una chiave (ordinata) e da un ArrayList di didascalie (testo) <br>
     * Sovrascritto nella sottoclasse concreta <br>
     * DOPO invoca il metodo della superclasse per calcolare la dimensione della mappa <br>
     */
    @Override
    protected void elaboraMappaDidascalie() {
        ListaGiornoNato listaGiornoNato = appContext.getBean(ListaGiornoNato.class, giorno);
        mappaDidascalie = listaGiornoNato.mappa;
        super.elaboraMappaDidascalie();
    }// fine del metodo


    /**
     * Piede della pagina
     * Sovrascritto
     */
    protected String elaboraFooter() {
        String testo = VUOTA;
        boolean nascosta = pref.isBool(FlowCost.USA_DEBUG);
        String cat;

        testo += LibWiki.setPortale(tagHeadTemplateProgetto);
        cat = LibWiki.setCat("Liste di nati per giorno", SPAZIO + giorno.ordine);
        cat = nascosta ? LibWiki.setNowiki(cat) : cat;
        testo += cat;
        cat = LibWiki.setCat(titoloPagina, SPAZIO);
        cat = nascosta ? LibWiki.setNowiki(cat) : cat;
        testo += cat;
        testo = LibBio.setNoIncludeMultiRiga(testo);

        return testo;
    }// fine del metodo

}// end of class
