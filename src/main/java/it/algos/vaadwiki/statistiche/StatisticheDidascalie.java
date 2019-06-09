package it.algos.vaadwiki.statistiche;

import it.algos.vaadwiki.didascalia.Didascalia;
import it.algos.vaadwiki.didascalia.EADidascalia;
import it.algos.vaadwiki.modules.bio.Bio;
import lombok.extern.slf4j.Slf4j;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Sun, 09-Jun-2019
 * Time: 18:31
 * <p>
 * Statistica specializzata per costruire una pagina di esempio di didascalie. <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class StatisticheDidascalie extends Statistiche {

    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso il parametro Bio del costruttore usato <br>
     */
    public StatisticheDidascalie() {
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(DidascaliaGiornoNato.class, bio) <br>
     *
     * @param bio di cui costruire la didascalia
     */
    public StatisticheDidascalie(Bio bio) {
//        super(bio, EADidascalia.giornoNato);
    }// end of constructor

}// end of class
