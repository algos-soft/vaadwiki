package it.algos.vaadwiki.didascalia;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.wiki.LibWiki;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import static it.algos.vaadflow.application.FlowCost.SPAZIO;
import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadwiki.application.WikiCost.TAG_SEPARATORE;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Sun, 09-Jun-2019
 * Time: 14:28
 * <p>
 * Didascalia specializzata per le liste di nome, cognomi, attività e nazionalità. <br>
 * Può essere recuperata da DidascaliaService (Singleton). <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class DidascaliaListe extends Didascalia {


    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso il parametro Bio del costruttore usato <br>
     */
    public DidascaliaListe() {
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(DidascaliaListe.class, bio) <br>
     *
     * @param bio di cui costruire la didascalia
     */
    public DidascaliaListe(Bio bio) {
        super(bio, EADidascalia.liste);
    }// end of constructor


    /**
     * Costruisce il blocco finale (potrebbe non esserci)
     * Sovrascritto
     *
     * @return testo
     */
    protected String getBloccoFinale() {
        String text = VUOTA;
        String tagParIni = SPAZIO + "(";
        String tagParEnd = ")";
        String textNascita = getBloccoFinaleNascita();
        String textMorte = getBloccoFinaleMorte();
        boolean isEsisteNascita = !textNascita.equals(VUOTA);
        boolean isEsisteMorte = !textMorte.equals(VUOTA);

        if (isEsisteNascita || isEsisteMorte) {
        } else {
            return VUOTA;
        }// end of if/else cycle

        // costruisce il blocco finale (potrebbe non esserci)
        text += tagParIni;
        if (isEsisteNascita) {
            text += textNascita;
        }// fine del blocco if

        if (isEsisteNascita && isEsisteMorte) {
            text += TAG_SEPARATORE;
        }// end of if cycle

        if (isEsisteMorte) {
            text += textMorte;
        }// fine del blocco if

        text += tagParEnd;
        return text;
    }// end of method


    /**
     * Parte nascita del blocco finale (potrebbe non esserci)
     *
     * @return testo
     */
    private String getBloccoFinaleNascita() {
        String testo = VUOTA;
        boolean isEsisteLocalita = !luogoNato.equals(VUOTA);
        boolean isEsisteNascita = !annoNato.equals(VUOTA);

        if (isEsisteLocalita) {
            if (!isEsisteNascita) {
                testo += TAG_NATO;
            }// end of if cycle
            if (luogoNato.endsWith(")")) {
                testo += LibWiki.setQuadre(luogoNato + "|");
            } else {
                testo += LibWiki.setQuadre(luogoNato);
            }// end of if/else cycle
        }// fine del blocco if

        if (isEsisteLocalita && isEsisteNascita) {
            testo += TAG_VIRGOLA;
        }// end of if cycle

        if (isEsisteNascita) {
            testo += TAG_NATO;
            testo += LibWiki.setQuadre(annoNato);
        }// fine del blocco if

        return testo;
    }// end of method


    /**
     * Parte morte del blocco finale (potrebbe non esserci)
     *
     * @return testo
     */
    private String getBloccoFinaleMorte() {
        String testo = VUOTA;
        boolean isEsisteLocalita = !luogoMorto.equals(VUOTA);
        boolean isEsisteMorte = !annoMorto.equals(VUOTA);

        if (isEsisteLocalita) {
            if (!isEsisteMorte) {
                testo += TAG_MORTO;
            }// end of if cycle
            if (luogoMorto.endsWith(")")) {
                testo += LibWiki.setQuadre(luogoMorto + "|");
            } else {
                testo += LibWiki.setQuadre(luogoMorto);
            }// end of if/else cycle
        }// fine del blocco if

        if (isEsisteLocalita && isEsisteMorte) {
            testo += TAG_VIRGOLA;
        }// fine del blocco if

        if (isEsisteMorte) {
            testo += TAG_MORTO;
            testo += LibWiki.setQuadre(annoMorto);
        }// fine del blocco if

        return testo;
    }// end of method

}// end of class
