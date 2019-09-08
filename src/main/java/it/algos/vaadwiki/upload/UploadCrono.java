package it.algos.vaadwiki.upload;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadwiki.service.LibBio;
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
 * Date: sab, 07-set-2019
 * Time: 07:46
 */
public abstract class UploadCrono extends Upload {

    /**
     * Piede della pagina
     * Sovrascritto
     */
    protected String elaboraFooter() {
        String testo = super.elaboraFooter();
        boolean nascosta = pref.isBool(FlowCost.USA_DEBUG);
        String cat;

        cat = LibWiki.setCat(titoloPagina, SPAZIO);
        cat = nascosta ? LibWiki.setNowiki(cat) : cat;
        testo += cat;
        testo = LibBio.setNoIncludeMultiRiga(testo);

        return testo;
    }// fine del metodo

}// end of class
