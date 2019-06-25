package it.algos.vaadwiki.upload;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadwiki.liste.ListaNomi;
import it.algos.vaadwiki.modules.cognome.Cognome;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Fri, 14-Jun-2019
 * Time: 17:07
 * <p>
 * Classe specializzata per caricare (upload) le liste sul server wiki. <br>
 * <p>
 * Viene chiamato da Scheduler (con frequenza giornaliera ?) <br>
 * Può essere invocato dal bottone 'Upload all' della classe CognomeViewList <br>
 * Necessita del login come bot <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class UploadCognome extends Upload {

    //--property
    protected Cognome cognome;


    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso il parametro Bio del costruttore usato <br>
     */
    public UploadCognome() {
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(UploadCognome.class, cognome) <br>
     *
     * @param cognome di cui costruire la pagina sul server wiki
     */
    public UploadCognome(Cognome cognome) {
        this.cognome = cognome;
    }// end of constructor

//    /**
//     * Metodo invocato subito DOPO il costruttore
//     * <p>
//     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() del costruttore <br>
//     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
//     * <p>
//     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti, <br>
//     * ma l'ordine con cui vengono chiamati (nella stessa classe) NON è garantito <br>
//     * Se ci sono superclassi e sottoclassi, chiama prima @PostConstruct della superclasse <br>
//     */
//    @PostConstruct
//    protected void inizia() {
//        super.esegue();
//    }// end of method
//


    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();
        usaSuddivisioneParagrafi = false;
    }// fine del metodo


//    /**
//     * Titolo della pagina da creare/caricare su wikipedia
//     * Sovrascritto
//     */
//    @Override
//    protected void elaboraTitolo() {
//        super.elaboraTitolo();
//    }// fine del metodo


//    /**
//     * Costruisce una mappa di liste di didascalie che hanno una valore valido per la pagina specifica <br>
//     * La mappa è composta da una chiave (ordinata) e da un ArrayList di didascalie (testo) <br>
//     * Sovrascritto nella sottoclasse concreta <br>
//     * DOPO invoca il metodo della superclasse per calcolare la dimensione della mappa <br>
//     */
//    @Override
//    protected void elaboraMappaDidascalie() {
////        ListaCognomi listaCognomi;
////        listaCognomi = appContext.getBean(ListaCognomi.class, cognome);
////        mappaDidascalie = listaNomi.mappa;
////        super.elaboraMappaDidascalie();
//    }// fine del metodo


}// end of class
