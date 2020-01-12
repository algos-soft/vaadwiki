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
        super.titoloPagina = TITOLO_PAGINA_WIKI;
    }// end of Spring constructor


//    /**
//     * Questa classe viene tipicamente costruita con appContext.getBean(StatisticheParametri.class) <br>
//     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() <br>
//     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired di questa classe <br>
//     */
//    @PostConstruct
//    protected void initIstanzaDopoInitDiSpringBoot() {
////        super.inizia();
//    }// end of method


    /**
     * Costruisce la pagina <br>
     * Registra la pagina sul server wiki <br>
     */
    protected void inizia() {
        super.inizia();
        appContext.getBean(StatisticheSingoloParametro.class, ParBio.sesso.name());
    }// end of method


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

        //--prima tabella (15 parametri)
        testo += A_CAPO;
        testo += tabellaParametriBase();

        //--seconda tabella
        testo += tabellaAltriParametri();

        testoPagina += testo.trim();
    }// fine del metodo


    protected String tabellaParametriBase() {
        String testo = VUOTA;

        //--tabella
        testo += A_CAPO;
        testo += testoTabellaBase();
        testo += inizioTabella();
        testo += colonneTabella(true);
        testo += corpoTabella(true);
        testo += fineTabella();
        testo += A_CAPO;

        return testo;
    }// fine del metodo


    protected String tabellaAltriParametri() {
        String testo = VUOTA;

        //--tabella
        testo += A_CAPO;
        testo += testoTabellaAltri();
        testo += inizioTabella();
        testo += colonneTabella(false);
        testo += corpoTabella(false);
        testo += fineTabella();
        testo += A_CAPO;

        return testo;
    }// fine del metodo


    /*
     * testo descrittivo <br>
     */
    protected String testoTabellaBase() {
        String testo = VUOTA;

        testo += A_CAPO;
        testo += "==Parametri base==";
        testo += A_CAPO;
        testo += "Il '''[[template:Bio|template Bio]]''' prevede ";
        testo += LibWiki.setBold(ParBio.values().length);
        testo += " parametri; alcuni obbligatori ed altri facoltativi.";
        testo += LibWiki.setRef("Se nella voce biografica manca un parametro ''facoltativo'', ad esempio ''nome'', la relativa persona non verrà inserita nelle liste di ''persone di nome...''.");
        testo += " Per la gestione delle ''liste'' effettuata dal '''[[Utente:Biobot|<span style=\"color:green;\">bot</span>]]''', sono utilizzati i seguenti ";
        testo += LibWiki.setBold(15);
        testo += " parametri.";
        testo += LibWiki.setRef("Il parametro ''sesso'' è indispensabile per inserire correttamente le desinenze maschili e femminili nelle liste di attività e nazionalità; tutte le voci biografiche ''dovrebbero'' averlo. ");

        return testo;
    }// fine del metodo


    protected String colonneTabella(boolean estesa) {
        String testo = "";
        String color = "! style=\"background-color:#CCC;\" |";

        testo += color;
        testo += LibWiki.setBold("#");
        testo += A_CAPO;

        testo += color;
        testo += LibWiki.setBold("Parametro");
        testo += A_CAPO;

        if (estesa) {
            testo += color;
            testo += LibWiki.setBold("Voci che non lo usano");
            testo += A_CAPO;

            testo += color;
            testo += LibWiki.setBold("Voci che lo usano");
            testo += A_CAPO;

            testo += color;
            testo += LibWiki.setBold("Perc. di utilizzo");
            testo += A_CAPO;
        }// end of if cycle

        return testo;
    }// fine del metodo


    protected String corpoTabella(boolean visibile) {
        StringBuilder testo = new StringBuilder(VUOTA);
        int k = 1;
        int totVoci = bioService.count();

        for (ParBio par : ParBio.values()) {
            if (visibile) {
                if (par.isVisibileLista()) {
                    testo.append(riga(totVoci, par, k++));
                    testo.append(A_CAPO);
                }// end of if cycle
            } else {
                if (!par.isVisibileLista()) {
                    testo.append(riga(par, k++));
                    testo.append(A_CAPO);
                }// end of if cycle
            }// end of if/else cycle
        }// end of for cycle

        return testo.toString();
    }// fine del metodo


    /*
     * Riga parametri delle liste <br>
     */
    protected String riga(int totVoci, ParBio par, int pos) {
        String testo = VUOTA;
        int usati = usati(par);
        int nonUsati = totVoci - usati;

        testo += SEP_INI;
        testo += A_CAPO;

        testo += SEP;
        testo += pos;

        testo += SEP_DOPPIO;
        testo += SINISTRA;
        testo += SEP;
        if (par == ParBio.sesso) {
            testo += LibWiki.setQuadreBold(TITOLO_PAGINA_WIKI + "/" + par.getTag() + "|" + par.getTag());
        } else {
            testo += LibWiki.setBold(par.getTag());
        }// end of if/else cycle

        testo += SEP_DOPPIO;
        testo += text.format(nonUsati);

        testo += SEP_DOPPIO;
        testo += text.format(usati);

        testo += SEP_DOPPIO;
        testo += LibWiki.setBold(math.percentualeDueDecimali(usati, totVoci));

        return testo;
    }// fine del metodo


    protected int usati(ParBio par) {
        long numVoci = 0;

        Query query = new Query();
        query.addCriteria(Criteria.where(par.getDbName()).exists(true));
        numVoci = mongo.mongoOp.count(query, Bio.class);

        return (int) numVoci;
    }// fine del metodo


    /*
     * Riga parametri non utilizzati nelle liste e non presenti nel mongoDB <br>
     */
    protected String riga(ParBio par, int pos) {
        String testo = VUOTA;

        testo += SEP_INI;
        testo += A_CAPO;

        testo += SEP;
        testo += pos;

        testo += SEP_DOPPIO;
        testo += SINISTRA;
        testo += SEP;
        testo += LibWiki.setBold(par.getTag());

        return testo;
    }// fine del metodo


    /*
     * testo descrittivo <br>
     */
    protected String testoTabellaAltri() {
        String testo = VUOTA;

        testo += A_CAPO;
        testo += "==Altri parametri==";
        testo += A_CAPO;
        testo += "Oltre ai 15 parametri utilizzati nelle liste, il '''[[template:Bio|template Bio]]''' prevede ";
        testo += "altri 26 parametri, tutti facoltativi.";

        return testo;
    }// fine del metodo

}// end of class
