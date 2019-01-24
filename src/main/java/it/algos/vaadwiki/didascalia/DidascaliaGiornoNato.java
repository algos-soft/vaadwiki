package it.algos.vaadwiki.didascalia;

import com.vaadin.flow.spring.annotation.SpringComponent;
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
 * Date: ven, 18-gen-2019
 * Time: 19:05
 * Didascalia specializzata per le liste di nati nel giorno.
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class DidascaliaGiornoNato extends Didascalia {


    public String esegue(Bio bio) {
        return super.esegue(bio);
    }// end of method


    /**
     * Costruisce il testo della didascalia
     * Sovrascritto
     */
    protected void regolaDidascalia() {
        testo = VUOTA;
        String mortoTxt;

        // giorno di nascita (deve esserci)
        if (text.isEmpty(giornoNato)) {
            return;
        }// end of if cycle

        // anno di nascita (potrebbe non esserci)
        if (text.isValid(annoNato)) {
            testo += LibWiki.setQuadre(annoNato);
            testo += TAG_SEP;
        }// end of if cycle

        // titolo e nome (obbligatori)
        testo += getNomeCognome();

        // attivitaNazionalita (potrebbe non esserci)
        testo += getAttNaz();

        // anno di morte (potrebbe non esserci)
        if (text.isValid(annoMorto)) {
            testo += SPAZIO;
            mortoTxt = TAG_MORTO;
            mortoTxt += SPAZIO;
            mortoTxt += LibWiki.setQuadre(annoMorto);
            testo += LibWiki.setTonde(mortoTxt);
        }// end of if cycle

    }// end of method

}// end of class
