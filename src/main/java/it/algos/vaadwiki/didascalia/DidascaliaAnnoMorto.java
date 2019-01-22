package it.algos.vaadwiki.didascalia;

import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.wiki.LibWiki;
import lombok.extern.slf4j.Slf4j;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import static it.algos.vaadflow.application.FlowCost.SPAZIO;
import static it.algos.vaadflow.application.FlowCost.VUOTA;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 21-gen-2019
 * Time: 17:59
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class DidascaliaAnnoMorto extends Didascalia{


    public String esegue(Bio bio) {
        return super.esegue(bio);
    }// end of method


    /**
     * Costruisce il testo della didascalia
     * Sovrascritto
     */
    protected void regolaDidascalia() {
        testo = VUOTA;
        String natoTxt;

        // anno di morte (deve esserci)
        if (text.isEmpty(annoMorto)) {
            return;
        }// end of if cycle

        // giorno di morte (potrebbe non esserci)
        if (text.isValid(giornoMorto)) {
            testo += LibWiki.setQuadre(giornoMorto);
            testo += TAG_SEP;
        }// end of if cycle

        // titolo e nome (obbligatori)
        testo += getNomeCognome();

        // attivitaNazionalita (potrebbe non esserci)
        testo += getAttNaz();

        // anno di nascita (potrebbe non esserci)
        if (text.isValid(annoNato)) {
            testo += SPAZIO;
            natoTxt = TAG_NATO;
            natoTxt += SPAZIO;
            natoTxt += LibWiki.setQuadre(annoNato);
            testo += LibWiki.setTonde(natoTxt);
        }// end of if cycle

    }// end of method

}// end of class
