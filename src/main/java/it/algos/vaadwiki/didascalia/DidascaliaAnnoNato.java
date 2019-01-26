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
public class DidascaliaAnnoNato extends Didascalia {


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
        String mortoTxt;

        // anno di nascita (deve esserci)
        if (text.isEmpty(annoNato)) {
            return;
        }// end of if cycle

        // giorno di nascita (potrebbe non esserci)
        if (text.isValid(giornoNato) && usaChiave) {
            testo += LibWiki.setQuadre(giornoNato);
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
