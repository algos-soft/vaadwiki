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
public class AQueryPost extends AQueryWiki {

    /**
     * Costruttore base senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(AQueryxxx.class) <br>
     */
    public AQueryPost() {
        super();
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(AQueryxxx.class, titoloWiki).urlResponse() <br>
     *
     * @param titoloWiki della pagina (necessita di codifica) usato nella urlRequest
     */
    public AQueryPost(String titoloWiki) {
        super(titoloWiki);
    }// end of constructor

}// end of class
