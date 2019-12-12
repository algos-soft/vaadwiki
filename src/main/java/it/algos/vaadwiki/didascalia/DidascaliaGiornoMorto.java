package it.algos.vaadwiki.didascalia;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadwiki.enumeration.EADidascalia;
import it.algos.vaadwiki.modules.bio.Bio;
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
 * Date: lun, 21-gen-2019
 * Time: 17:59
 * <p>
 * Didascalia specializzata per le liste di morti nel giorno. <br>
 * Può essere recuperata da DidascaliaService (Singleton). <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class DidascaliaGiornoMorto extends Didascalia {


    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso il parametro Bio del costruttore usato <br>
     */
    public DidascaliaGiornoMorto() {
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(DidascaliaGiornoMorto.class, bio) <br>
     *
     * @param bio di cui costruire la didascalia
     */
    public DidascaliaGiornoMorto(Bio bio) {
        super(bio, EADidascalia.giornoMorto);
    }// end of constructor


    /**
     * Costruisce il testo della didascalia <br>
     * Sovrascritto <br>
     */
    protected void regolaDidascalia() {
        testoSenza = VUOTA;
        testoCon = VUOTA;
        String natoTxt;

        // giorno di morte (deve esserci)
        if (text.isEmpty(giornoMorto)) {
            return;
        }// end of if cycle

        // titolo e nome (obbligatori)
        testoSenza += getNomeCognome();

        // attivitaNazionalita (potrebbe non esserci)
        testoSenza += getAttNaz();

        // anno di nascita (potrebbe non esserci)
        if (text.isValid(annoNato)) {
            testoSenza += SPAZIO;
            natoTxt = TAG_NATO;
            natoTxt += SPAZIO;
            natoTxt += LibWiki.setQuadre(annoNato);
            testoSenza += LibWiki.setTonde(natoTxt);
        }// end of if cycle

        //--chiave per il paragrafo
        //--anno di morte (potrebbe non esserci)
        //--inserito all'inizio del testo
        if (text.isValid(annoMorto)) {
            testoCon += LibWiki.setQuadre(annoMorto);
            testoCon += TAG_SEP;
            testoCon += testoSenza;
        } else {
            testoCon = testoSenza;
        }// end of if/else cycle

    }// end of method


}// end of class
