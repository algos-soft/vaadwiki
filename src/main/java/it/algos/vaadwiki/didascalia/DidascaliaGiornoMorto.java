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
 * Date: lun, 21-gen-2019
 * Time: 17:59
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class DidascaliaGiornoMorto extends Didascalia {


    public String esegue(Bio bio) {
        return super.esegue(bio);
    }// end of method


    public String esegueSenza(Bio bio) {
        return super.esegue(bio, false);
    }// end of method


    /**
     * Costruisce il testo della didascalia
     * Sovrascritto
     */
    protected void regolaDidascalia(boolean usaChiave) {
        testo = VUOTA;
        String natoTxt;

        // giorno di morte (deve esserci)
        if (text.isEmpty(giornoMorto)) {
            return;
        }// end of if cycle

        // anno di morte (potrebbe non esserci)
        if (text.isValid(annoMorto) && usaChiave) {
            testo += LibWiki.setQuadre(annoMorto);
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
