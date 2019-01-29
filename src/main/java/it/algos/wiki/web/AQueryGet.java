package it.algos.wiki.web;

import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 28-gen-2019
 * Time: 14:36
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class AQueryGet extends AQueryWiki {


    /**
     * Controlla la stringa della request
     * Inserisce un tag specifico
     * Codifica i caratteri
     *
     * @param titoloWiki della pagina
     *
     * @return stringa della request modificata
     */
    @Override
    public String fixUrlDomain(String titoloWiki) {
        return titoloWiki.startsWith(TAG_WIKI) ? titoloWiki : TAG_WIKI + titoloWiki;
    } // fine del metodo


}// end of class
