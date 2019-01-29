package it.algos.wiki.web;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.wiki.LibWiki;
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
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class AQueryBio extends AQueryVoce {

    private String titoloWiki;


    public AQueryBio() {
    }


    public AQueryBio(String titoloWiki) {
        this.titoloWiki = titoloWiki;
    }


    /**
     * Request principale
     * Quella base usa solo il GET
     * In alcune request (non tutte) si aggiunge anche il POST
     *
     * @return urlResponse
     */
    public String urlResponse() {
        return urlRequest(titoloWiki);
    }// end of method


    /**
     * Request principale
     * Quella base usa solo il GET
     * In alcune request (non tutte) si aggiunge anche il POST
     *
     * @param titoloWiki della pagina
     */
    @Override
    public String urlRequest(String titoloWiki) {
        String templateBio = "";
        String contenutoVoce = super.urlRequest(titoloWiki);

        if (text.isValid(contenutoVoce)) {
            templateBio = LibWiki.estraeTmplBioCompresi(contenutoVoce);
        }// end of if cycle

        return templateBio;
    }// end of method


}// end of class
