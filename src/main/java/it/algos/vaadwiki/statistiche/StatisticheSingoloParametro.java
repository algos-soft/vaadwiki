package it.algos.vaadwiki.statistiche;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.service.LibBio;
import it.algos.vaadwiki.service.ParBio;
import it.algos.wiki.LibWiki;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

import static it.algos.vaadflow.application.FlowCost.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: sab, 28-dic-2019
 * Time: 09:09
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class StatisticheSingoloParametro extends Statistiche {

    protected static String TITOLO_PAGINA_WIKI = "Progetto:Biografie/Parametri/";

    protected static int MIN = 50;

    protected static int MAX = 100;

    protected static String DIRETTO = "Elenco diretto fino a " + MIN + " voci; oltre vengono cassettate.";

    protected static String CASSETTO = "Cassetto con un elenco di oltre " + MIN + " voci.";

    protected static String NUMERO = "Solo numero delle voci; la lista di oltre " + MAX + " voci sarebbe troppo lunga";

    protected ParBio par;

    protected List<Bio> lista;

    protected int usate;

    protected int nonUsate;


    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso il parametro Bio del costruttore usato <br>
     */
    public StatisticheSingoloParametro() {
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(StatisticheSingoloParametro.class, parametro) <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune property. <br>
     * La superclasse usa poi il metodo @PostConstruct inizia() per proseguire dopo l'init del costruttore <br>
     *
     * @param par      parametro di cui costruire la pagina sul server wiki
     * @param nonUsate voci che non usano il parametro
     * @param usate    voci che usano il parametro
     */
    public StatisticheSingoloParametro(ParBio par, int nonUsate, int usate) {
        this.par = par;
        this.nonUsate = nonUsate;
        this.usate = usate;
        super.titoloPagina = TITOLO_PAGINA_WIKI + par.getTag();
    }// end of constructor


    /**
     * Preferenze specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.usaTagIndice = false;
        super.usaNote = true;
    }// fine del metodo


    /**
     * Corpo della pagina <br>
     */
    protected void elaboraBody() {
        String testo = VUOTA;
//        lista = getListaVoci();

//        //--tabella
//        testo += A_CAPO;
//        testo += testoIniziale();
//        testo += elencoVoci();
//        testo += A_CAPO;

        testo += testoIniziale();
        testo += parametroVuoto();
        testo += parametroPieno();

        testoPagina += testo;
    }// fine del metodo


    /*
     * Testo descrittivo <br>
     */
    protected String testoIniziale() {
        String testo = VUOTA;

        testo += A_CAPO;
        testo += "Statistiche delle voci";
        testo += LibWiki.setRef("Per motivi tecnici, voci nuove o modificate negli ultimi giorni potrebbero non apparire.");
        testo += " biografiche che utilizzano il [[template:Bio|template Bio]] e usano il parametro ";
        testo += LibWiki.setBold(par.getTag());
        testo += A_CAPO;
        testo += A_CAPO;

        return testo;
    }// fine del metodo


    /**
     * Primo paragrafo/tabella/cassetto/lista <br>
     */
    protected String parametroVuoto() {
        String testo = VUOTA;

        testo += LibBio.setParagrafo("Parametro vuoto");
        testo += A_CAPO;
        testo += "Ci sono ";
        testo += LibWiki.setBold(text.format(nonUsate));
        testo += " voci";
        testo += selezioneRef(nonUsate);
        testo += " che non usano il parametro ";
        testo += LibWiki.setBold(par.getTag());
        testo += A_CAPO;

        testo += creaLista(false, nonUsate);

        return testo;
    }// fine del metodo


    /**
     * Secondo paragrafo/tabella/cassetto/lista <br>
     */
    protected String parametroPieno() {
        String testo = VUOTA;

        testo += LibBio.setParagrafo("Parametro pieno");
        testo += A_CAPO;
        testo += "Ci sono ";
        testo += LibWiki.setBold(text.format(usate));
        testo += " voci";
        testo += selezioneRef(usate);
        testo += " che  usano il parametro ";
        testo += LibWiki.setBold(par.getTag());

        testo += creaLista(true, usate);

        return testo;
    }// fine del metodo


    /**
     * Nota a seconda del numero di voci <br>
     */
    protected String selezioneRef(int numeroVoci) {
        String testo = VUOTA;

        if (numeroVoci < MIN) {
            testo = DIRETTO;
        } else {
            if (numeroVoci < MAX) {
                testo = CASSETTO;
            } else {
                testo = NUMERO;
            }// end of if/else cycle
        }// end of if/else cycle

        return LibWiki.setRef(testo);
    }// fine del metodo


    /**
     * Lista o cassetto <br>
     */
    protected String creaLista(boolean esistenti, int numeroVoci) {
        String testo = VUOTA;

        if (esistenti) {
        } else {
            if (numeroVoci < MIN) {
                testo = creaDiretto(esistenti);
            } else {
                if (numeroVoci < MAX) {
                    testo = creaCassetto(esistenti);
                }// end of if cycle
            }// end of if/else cycle
        }// end of if/else cycle

        return testo;
    }// fine del metodo


    /**
     * Lista <br>
     */
    protected String creaDiretto(boolean esistenti) {
        String testo = VUOTA;

        List<Bio> lista = getListaVoci(esistenti);

        if (array.isValid(lista)) {
            for (Bio bio : lista) {
                testo += ASTERISCO;
                testo += LibWiki.setQuadre(bio.getWikiTitle());
                testo += A_CAPO;
            }// end of for cycle
        }// end of if cycle

        return testo;
    }// fine del metodo


    /**
     * Cassetto <br>
     */
    protected String creaCassetto(boolean esistenti) {
        String testo = creaDiretto(esistenti);

        testo = LibWiki.listaDueColonne(testo);
        testo = libBio.setCassetto("Biografie", testo);
        return testo;
    }// fine del metodo


    //    /*
//     * testo descrittivo <br>
//     */
//    protected String testoIniziale() {
//        String testo = VUOTA;
//
//        testo += A_CAPO;
//        testo += "Elenco delle ";
//        testo += LibWiki.setBold(text.format(lista.size()));
//        testo += " voci ";
//        testo += LibWiki.setRef("Per motivi tecnici, voci nuove o modificate negli ultimi giorni potrebbero non apparire.");
//        testo += " biografiche che utilizzano il [[template:Bio|template Bio]] e '''non''' usano il parametro ";
//        testo += LibWiki.setBold(par.getTag().toLowerCase());
//        testo += SPAZIO;
//        testo += LibWiki.setRef("Il parametro ''sesso'' è indispensabile per inserire correttamente le desinenze maschili e femminili nelle liste di attività e nazionalità; tutte le voci biografiche ''dovrebbero'' averlo. ");
//        testo += ".";
//        testo += A_CAPO;
//
//        return testo;
//    }// fine del metodo
//
//
//    protected String elencoVoci() {
//        StringBuilder testoLista = new StringBuilder(VUOTA);
//
//        if (array.isValid(lista)) {
//            for (Bio bio : lista) {
//                testoLista.append(ASTERISCO);
//                testoLista.append(LibWiki.setQuadre(bio.wikiTitle));
//                testoLista.append(A_CAPO);
//            }// end of for cycle
//        }// end of if cycle
//
//        return LibWiki.setColonne(testoLista.toString());
//    }// fine del metodo
//
//
    protected List<Bio> getListaVoci(boolean esistenti) {
        List<Bio> lista = null;

        Query query = new Query();
        query.addCriteria(Criteria.where(par.getDbName()).exists(esistenti));
        lista = mongo.mongoOp.find(query, Bio.class);

        return lista;
    }// fine del metodo

}// end of class
