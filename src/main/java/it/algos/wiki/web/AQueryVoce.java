package it.algos.wiki.web;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.wiki.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 28-gen-2019
 * Time: 14:37
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class AQueryVoce extends AQueryPage {


    /**
     * Request principale
     * Quella base usa solo il GET
     * In alcune request (non tutte) si aggiunge anche il POST
     *
     * @param titoloWiki della pagina
     */
    @Override
    public String urlRequest(String titoloWiki) {
        String contenutoTesto = "";
        Page page = super.crea(titoloWiki);

        if (page != null) {
            contenutoTesto = page.getText();
        }// end of if cycle

        return contenutoTesto;
    }// end of method



}// end of class
