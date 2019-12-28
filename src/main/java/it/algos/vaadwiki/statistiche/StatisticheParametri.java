package it.algos.vaadwiki.statistiche;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.service.ParBio;
import it.algos.wiki.LibWiki;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.annotation.PostConstruct;

import static it.algos.vaadflow.application.FlowCost.A_CAPO;
import static it.algos.vaadflow.application.FlowCost.VUOTA;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: ven, 27-dic-2019
 * Time: 19:27
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class StatisticheParametri extends Statistiche {

    private static String TITOLO_PAGINA_WIKI = "Progetto:Biografie/Parametri";


    /**
     * Costruttore base senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(StatisticheParametri.class) <br>
     */
    public StatisticheParametri() {
    }// end of Spring constructor


    /**
     * Questa classe viene tipicamente costruita con appContext.getBean(StatisticheParametri.class) <br>
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired di questa classe <br>
     */
    @PostConstruct
    protected void initIstanzaDopoInitDiSpringBoot() {
        super.titoloPagina = TITOLO_PAGINA_WIKI;
        super.inizia();
    }// end of method


    /**
     * Preferenze specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.usaTagIndice = false;
    }// fine del metodo


    /**
     * Corpo della pagina <br>
     */
    protected void elaboraBody() {
        String testo = VUOTA;

        //--tabella
        testo += A_CAPO;
        testo += inizioTabella();
        testo += colonneTabella();
        testo += corpoTabella();
        testo += fineTabella();
        testo += A_CAPO;

        testoPagina += testo.trim();
    }// fine del metodo


    protected String colonneTabella() {
        String testo = "";
        String color = "! style=\"background-color:#CCC;\" |";

        testo += color;
        testo += LibWiki.setBold("#");
        testo += A_CAPO;

        testo += color;
        testo += LibWiki.setBold("Parametro");
        testo += A_CAPO;

        testo += color;
        testo += LibWiki.setBold("Voci che non lo usano");
        testo += A_CAPO;

        testo += color;
        testo += LibWiki.setBold("Voci che lo usano");
        testo += A_CAPO;

        testo += color;
        testo += LibWiki.setBold("Perc. di utilizzo");
        testo += A_CAPO;


        return testo;
    }// fine del metodo


    protected String corpoTabella() {
        StringBuilder testo = new StringBuilder(VUOTA);
        int k = 1;
        for (ParBio par : ParBio.values()) {
            testo.append(riga(par, k++));
            testo.append(A_CAPO);
        }// end of for cycle

        return testo.toString();
    }// fine del metodo


    protected String riga(ParBio par, int pos) {
        String testo = VUOTA;
        int usati = usati(par);
        int nonUsati = 0;

        testo += SEP_INI;
        testo += A_CAPO;

        testo += SEP;
        testo += pos;

        testo += SEP_DOPPIO;
        testo += SINISTRA;
        testo += SEP;
        testo += LibWiki.setBold(par.getTag());

        testo += SEP_DOPPIO;
        testo += text.format(usati);

        testo += SEP_DOPPIO;
        testo += text.format(nonUsati);

        testo += SEP_DOPPIO;
        testo += text.format(usati - nonUsati);

        return testo;
    }// fine del metodo


    protected int usati(ParBio par) {
        long numVoci = 0;

        Query query = new Query();
        query.addCriteria(Criteria.where(par.getTag()).exists(true));
        numVoci = mongo.mongoOp.count(query, Bio.class);


        return (int)numVoci;
    }// fine del metodo

}// end of class
