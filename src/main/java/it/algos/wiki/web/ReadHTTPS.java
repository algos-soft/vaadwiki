package it.algos.wiki.web;

import com.vaadin.flow.spring.annotation.SpringComponent;
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
 * Legge una pagina internet di tipo HTTPS. Non adatta per pagine HTTP.
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class ReadHTTPS extends Read {

    private final static String TAG_INIZIALE = "https://";


    public String getTagIniziale() {
        return TAG_INIZIALE;
    } // fine del metodo

}// end of class
