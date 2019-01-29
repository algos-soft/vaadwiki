package it.algos.wiki.mediawiki;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.wiki.Page;
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
 * Legge il contenuto visibile di una pagina wiki.
 * Solo lettura. Request di tipo GET. Niente Bot. Niente cookies.
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class ReadVoce extends ReadPage {

    /**
     * Request di tipo GET
     * Accetta SOLO un domain (indirizzo) di pagine wiki
     *
     * @param titoloWiki
     *
     * @return contenuto visibile della voce sul server wiki
     */
    public String legge(String titoloWiki) {
        String contenutoTesto = "";
        Page page = super.crea(titoloWiki);

        if (page != null) {
            contenutoTesto = page.getText();
        }// end of if cycle

        return contenutoTesto;
    } // fine del metodo

}// end of class
