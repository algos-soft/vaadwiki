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
 * Time: 14:39
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class AQueryHTTPS extends AQueryWeb {

    /**
     * Tag aggiunto prima del titolo (leggibile) della pagina per costruire il 'urlDomain' completo
     */
    private final static String TAG_INIZIALE = "https://";


    /**
     * Controlla la stringa della request
     * Inserisce un tag specifico
     * Codifica i caratteri
     *
     * @param urlDomain stringa della request originale
     *
     * @return stringa della request modificata
     */
    @Override
    public String fixUrlDomain(String urlDomain) {
        return urlDomain.startsWith(TAG_INIZIALE) ? urlDomain : TAG_INIZIALE + urlDomain;
    } // fine del metodo

}// end of class
