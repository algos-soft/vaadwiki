package it.algos.wiki.mediawiki;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.wiki.LibWiki;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: dom, 27-gen-2019
 * Time: 18:09
 * <p>
 * Legge il contenuto del template Bio di una voce wiki.
 * Solo lettura. Request di tipo GET. Niente Bot. Niente cookies.
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class ReadBio extends ReadVoce {

    /**
     * Request di tipo GET
     * Accetta SOLO un domain (indirizzo) di pagine wiki
     *
     * @param titoloWiki
     *
     * @return template bio di una voce
     */
    public String legge(String titoloWiki) {
        String template = "";
        String contenutoTesto = super.legge(titoloWiki);

        if (text.isValid(contenutoTesto)) {
            template = LibWiki.estraeTmplBioCompresi(contenutoTesto);
        }// end of if cycle

        return template;
    } // fine del metodo

}// end of class
