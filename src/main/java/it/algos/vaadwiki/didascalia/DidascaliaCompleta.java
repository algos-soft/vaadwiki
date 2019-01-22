package it.algos.vaadwiki.didascalia;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadwiki.modules.bio.Bio;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

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
public class DidascaliaCompleta extends Didascalia {

    /**
     * Costruisce il testo della didascalia
     * Sovrascritto
     */
    protected void regolaDidascalia() {
        testo = VUOTA;
        String natoTxt;

        // titolo e nome (obbligatori)
        testo += getNomeCognome();

        // blocco cronologico e luoghi (potrebbe non esserci)
        testo += getBloccoFinale();

        // attivitaNazionalita (potrebbe non esserci)
        testo += getAttNaz();


    }// end of method

}// end of class
