package it.algos.vaadwiki.upload;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.wiki.LibWiki;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mer, 06-nov-2019
 * Time: 15:46
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class UploadSottoPaginaNomi  {

    protected final static String PREFIX_TITOLO = "Persone di nome ";


    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso il parametro Bio del costruttore usato <br>
     */
    public UploadSottoPaginaNomi() {
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(UploadSottoPagina.class, titoloBrevePaginaPrincipale, titoloCompletoSottoPagina, mappaAlfabetica) <br>
     *
     * @param titoloBrevePaginaPrincipale solo il nome/cognome per la costruzione del titolo e per le categorie
     * @param titoloBreveSottoPagina      attività della sottopagina per l'incipit (eventuale) e per le categorie
     * @param mappaAlfabetica             mappa delle didascalie suddivise per iniziale alfabetica del cognome
     */
    public UploadSottoPaginaNomi(String titoloBrevePaginaPrincipale, String titoloBreveSottoPagina, LinkedHashMap<String, List<String>> mappaAlfabetica) {
//        super(titoloBrevePaginaPrincipale, titoloBreveSottoPagina, mappaAlfabetica);
    }// end of constructor


//    /**
//     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse <br>
//     * Può essere sovrascritto, per aggiungere informazioni <br>
//     * Invocare PRIMA il metodo della superclasse <br>
//     */
//    @Override
//    protected void fixPreferenze() {
//        super.fixPreferenze();
//
//        super.prefixTitolo = PREFIX_TITOLO;
//        super.titoloPagina = prefixTitolo + " " + titoloBrevePaginaPrincipale + "/" + titoloBreveSottoPagina;
//        String nomeCat = titoloBrevePaginaPrincipale + "/" + titoloBreveSottoPagina;
//        super.tagCategoria = LibWiki.setCat("Liste di persone per nome", nomeCat);
//    }// end of method

}// end of class
