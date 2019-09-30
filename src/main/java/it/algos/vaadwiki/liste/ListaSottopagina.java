package it.algos.vaadwiki.liste;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: ven, 27-set-2019
 * Time: 08:07
 * Wrapper di passaggio tra Lista e Upload <br>
 * Contiene il testo visibile ed una mappa delle sottopagine da creare <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ListaSottopagina {

    //--property
    private String testo;

    //--property
    private LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappa;


    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso il parametro Bio del costruttore usato <br>
     */
    public ListaSottopagina() {
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(ListaSottopagina.class, testo, mappa) <br>
     *
     * @param testo della pagina wiki da comporre
     * @param mappa delle sottopagine da creare
     */
    public ListaSottopagina(String testo, LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappa) {
        this.testo = testo;
        this.mappa = mappa;
    }// end of constructor


    public String getTesto() {
        return testo;
    }// end of method


    public LinkedHashMap<String, LinkedHashMap<String, List<String>>> getMappa() {
        return mappa;
    }// end of method

}// end of class
