package it.algos.vaadwiki.statistiche;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.wiki.LibWiki;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import static it.algos.vaadflow.application.FlowCost.*;
import static it.algos.vaadwiki.application.WikiCost.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mar, 24-dic-2019
 * Time: 13:52
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class StatisticheBiografie extends Statistiche {

    private static String TITOLO_PAGINA_WIKI = "Progetto:Biografie/Statistiche";

    private static String SEP_INI = "|-";

    private static String SEP = "|";

    private static String SEP_DOPPIO = "||";

    private static String SINISTRA = "style=\"text-align: left;\"";

    private boolean modificate;

    private String dataOld;

    private String dataNew;

    private int bioOld;

    private int bioNew;

    private int giorniOld;

    private int giorniNew;

    private int anniOld;

    private int anniNew;

    private int attivitaOld;

    private int attivitaNew;

    private int nazionalitaOld;

    private int nazionalitaNew;

    private int attesaOld;

    private int attesaNew;


    /**
     * Costruttore base senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(StatisticheAnni.class) <br>
     */
    public StatisticheBiografie() {
    }// end of Spring constructor


    /**
     * Costruisce la pagina <br>
     * Registra la pagina sul server wiki <br>
     */
    protected void inizia() {
        this.fixPreferenze();
        recuperaPrecedentiValori();
        recuperaValoriAttuali();
        elaboraPagina();
        registraPagina();
        registraValoriAttuali();
    }// end of method


    /**
     * Preferenze specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void fixPreferenze() {
        super.fixPreferenze();

        this.dataOld = "pippoz";
        this.dataNew = date.get();
        this.modificate = false;
        this.usaTagIndice = false;
        this.usaNote = true;
        this.usaCorrelate = false;
    }// fine del metodo

    /**
     * Recupera valori dalle preferenze <br>
     */
    protected void recuperaPrecedentiValori() {
        bioOld = pref.getInt(STATISTICHE_VOCI);

        if (bioOld > 0) {
            modificate = true;
            giorniOld = pref.getInt(STATISTICHE_GIORNI);
            anniOld = pref.getInt(STATISTICHE_ANNI);
            attivitaOld = pref.getInt(STATISTICHE_ATTIVITA);
            nazionalitaOld = pref.getInt(STATISTICHE_NAZIONALITA);
            attesaOld = pref.getInt(STATISTICHE_ATTESA);
        }// end of if cycle
    }// end of method


    /**
     * Registra nelle preferenze i valori attuali <br>
     */
    protected void recuperaValoriAttuali() {
        bioNew = bioService.count();
        giorniNew = giornoService.count();
        anniNew = annoService.countAnniUsati();
        anniNew = 2574;//@todo patch 'terrificante'
        attivitaNew = attivitaService.count();
        nazionalitaNew = nazionalitaService.count();
        attesaNew = 3;
    }// end of method


    /**
     * Registra nelle preferenze i valori attuali <br>
     */
    protected void registraValoriAttuali() {
        pref.saveValue(STATISTICHE_VOCI, anniNew-54);
        pref.saveValue(STATISTICHE_VOCI, anniNew-54);
        pref.saveValue(STATISTICHE_VOCI, anniNew-54);
    }// end of method




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
        String delta = "&nbsp;&nbsp;&nbsp;&nbsp;Δ";

        testo += color;
        testo += "'''#'''";
        testo += A_CAPO;

        testo += color;
        testo += "'''Statistiche'''";
        testo += A_CAPO;

        if (modificate) {
            testo += color;
            testo += dataOld;
            testo += A_CAPO;
        }// end of if cycle

        testo += color;
        testo += dataNew;
        testo += A_CAPO;

        if (modificate) {
            testo += color;
            testo += delta;
            testo += A_CAPO;
        }// end of if cycle

        return testo;
    }// fine del metodo


    protected String corpoTabella() {
        String testo = VUOTA;

        testo += rigaTemplate();
        testo += rigaGiorni();
        testo += rigaAnni();
        testo += rigaAttivita();
        testo += rigaNazionalita();
        testo += rigaAttesa();

        return testo;
    }// fine del metodo


    protected String rigaTemplate() {
        String testo = VUOTA;

        testo += SEP_INI;
        testo += A_CAPO;

        testo += SEP;
        testo += "1";

        testo += SEP_DOPPIO;
        testo += SINISTRA;
        testo += SEP;
        testo += LibWiki.setQuadreBold(":Categoria:BioBot|Template bio");
        testo += SPAZIO;
        testo += LibWiki.setRef("Una differenza di un centinaio di pagine tra la categoria e le voci gestite dal bot è '''fisiologica''' e dovuta a categorizzazioni diverse tra il software mediawiki ed il [[template:bio|template bio]]");

        if (modificate) {
            testo += SEP_DOPPIO;
            testo += text.format(bioOld);
        }// end of if cycle

        testo += SEP_DOPPIO;
        testo += text.format(bioNew);

        if (modificate) {
            testo += SEP_DOPPIO;
            testo += text.format(bioNew-bioOld);
        }// end of if cycle

        testo += A_CAPO;

        return testo;
    }// fine del metodo


    protected String rigaGiorni() {
        String testo = VUOTA;

        testo += SEP_INI;
        testo += A_CAPO;

        testo += SEP;
        testo += "2";

        testo += SEP_DOPPIO;
        testo += SINISTRA;
        testo += SEP;
        testo += LibWiki.setQuadreBold("Progetto:Biografie/Giorni|Giorni interessati");
        testo += SPAZIO;
        testo += LibWiki.setRef("Previsto il [[29 febbraio]] per gli [[Anno bisestile|anni bisestili]]");

        if (modificate) {
            testo += SEP_DOPPIO;
            testo += "2 bis";
        }// end of if cycle

        testo += SEP_DOPPIO;
        testo += text.format(giorniNew);

        if (modificate) {
            testo += SEP_DOPPIO;
            testo += "3 bis";
        }// end of if cycle

        testo += A_CAPO;

        return testo;
    }// fine del metodo


    protected String rigaAnni() {
        String testo = VUOTA;

        testo += SEP_INI;
        testo += A_CAPO;

        testo += SEP;
        testo += "3";

        testo += SEP_DOPPIO;
        testo += SINISTRA;
        testo += SEP;
        testo += LibWiki.setQuadreBold("Progetto:Biografie/Anni|Anni interessati");
        testo += SPAZIO;
        testo += LibWiki.setRef("Potenzialmente dal [[1000 a.C.]] al [[{{CURRENTYEAR}}]]");

        if (modificate) {
            testo += SEP_DOPPIO;
            testo += "2 bis";
        }// end of if cycle

        testo += SEP_DOPPIO;
        testo += text.format(anniNew);

        if (modificate) {
            testo += SEP_DOPPIO;
            testo += "3 bis";
        }// end of if cycle

        testo += A_CAPO;

        return testo;
    }// fine del metodo


    protected String rigaAttivita() {
        String testo = VUOTA;

        testo += SEP_INI;
        testo += A_CAPO;

        testo += SEP;
        testo += "4";

        testo += SEP_DOPPIO;
        testo += SINISTRA;
        testo += SEP;
        testo += LibWiki.setQuadreBold("Progetto:Biografie/Attività|Attività utilizzate");
        testo += SPAZIO;
        testo += LibWiki.setRef("Le attività sono quelle '''convenzionalmente''' previste dalla comunità ed inserite nell'elenco utilizzato dal template Bio");

        if (modificate) {
            testo += SEP_DOPPIO;
            testo += "2 bis";
        }// end of if cycle

        testo += SEP_DOPPIO;
        testo += text.format(attivitaNew);

        if (modificate) {
            testo += SEP_DOPPIO;
            testo += "3 bis";
        }// end of if cycle

        testo += A_CAPO;


        return testo;
    }// fine del metodo


    protected String rigaNazionalita() {
        String testo = VUOTA;

        testo += SEP_INI;
        testo += A_CAPO;

        testo += SEP;
        testo += "5";

        testo += SEP_DOPPIO;
        testo += SINISTRA;
        testo += SEP;
        testo += LibWiki.setQuadreBold("Progetto:Biografie/Nazionalità|Nazionalità utilizzate");
        testo += SPAZIO;
        testo += LibWiki.setRef("Le nazionalità sono quelle '''convenzionalmente''' previste dalla comunità ed inserite nell'elenco utilizzato dal template Bio");

        if (modificate) {
            testo += SEP_DOPPIO;
            testo += "2 bis";
        }// end of if cycle

        testo += SEP_DOPPIO;
        testo += text.format(nazionalitaNew);

        if (modificate) {
            testo += SEP_DOPPIO;
            testo += "3 bis";
        }// end of if cycle

        testo += A_CAPO;

        return testo;
    }// fine del metodo


    protected String rigaAttesa() {
        String testo = VUOTA;

        testo += SEP_INI;
        testo += A_CAPO;

        testo += SEP;
        testo += "6";

        testo += SEP_DOPPIO;
        testo += SINISTRA;
        testo += SEP;
        testo += LibWiki.setBold("Giorni di attesa");
        testo += SPAZIO;
        testo += LibWiki.setRef("Giorni di attesa '''indicativi''' prima che ogni singola voce venga ricontrollata per registrare eventuali modifiche intervenute nei parametri significativi");

        if (modificate) {
            testo += SEP_DOPPIO;
            testo += "2 bis";
        }// end of if cycle

        testo += SEP_DOPPIO;
        testo += text.format(attesaNew);

        if (modificate) {
            testo += SEP_DOPPIO;
            testo += "3 bis";
        }// end of if cycle

        testo += A_CAPO;


        return testo;
    }// fine del metodo

}// end of class
