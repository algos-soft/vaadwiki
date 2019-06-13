package it.algos.vaadwiki.upload;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.modules.anno.Anno;
import it.algos.vaadwiki.liste.ListaAnnoNato;
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
 * Time: 17:22
 * <p>
 * Classe specializzata per caricare (upload) le liste sul server wiki. <br>
 * <p>
 * Viene chiamato da Scheduler (con frequenza giornaliera ?) <br>
 * Può essere invocato dal bottone 'Upload all' della classe WikiAnnoViewList <br>
 * Necessita del login come bot <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class UploadAnnoNato extends UploadAnni {


    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso il parametro Bio del costruttore usato <br>
     */
    public UploadAnnoNato() {
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(UploadAnnoNato.class, anno) <br>
     *
     * @param anno di cui costruire la pagina sul server wiki
     */
    public UploadAnnoNato(Anno anno) {
        super(anno);
    }// end of constructor


    /**
     * Titolo della pagina da creare/caricare su wikipedia
     * Sovrascritto
     */
    @Override
    protected void elaboraTitolo() {
        if (anno != null) {
            titoloPagina = getTitoloPagina(anno);
        }// fine del blocco if
    }// fine del metodo


    /**
     * Titolo della pagina Nati/Morti da creare/caricare su wikipedia
     * Sovrascritto
     */
    public String getTitoloPagina(Anno anno) {
        return libBio.getTitoloAnnoNato(anno);
    }// fine del metodo


    /**
     * Costruisce una mappa di liste di didascalie che hanno una valore valido per la pagina specifica <br>
     * La mappa è composta da una chiave (ordinata) e da un ArrayList di didascalie (testo) <br>
     * Sovrascritto nella sottoclasse concreta <br>
     * DOPO invoca il metodo della superclasse per calcolare la dimensione della mappa <br>
     */
    @Override
    protected void elaboraMappaDidascalie() {
        ListaAnnoNato listaAnnoNato = appContext.getBean(ListaAnnoNato.class, anno);
        mappaDidascalie = listaAnnoNato.mappa;
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
        cat = LibWiki.setCat("Liste di nati per anno", SPAZIO + anno.ordine);
        cat = nascosta ? LibWiki.setNowiki(cat) : cat;
        testo += cat;
        cat = LibWiki.setCat(titoloPagina, SPAZIO);
        cat = nascosta ? LibWiki.setNowiki(cat) : cat;
        testo += cat;
        testo = LibBio.setNoIncludeMultiRiga(testo);

        return testo;
    }// fine del metodo

}// end of class
