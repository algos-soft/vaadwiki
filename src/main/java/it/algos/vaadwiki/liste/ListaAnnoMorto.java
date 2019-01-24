package it.algos.vaadwiki.liste;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadwiki.didascalia.DidascaliaAnnoMorto;
import it.algos.vaadwiki.modules.bio.Bio;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: gio, 24-gen-2019
 * Time: 10:33
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class ListaAnnoMorto extends ListaAnni {


    @Autowired
    protected DidascaliaAnnoMorto didascaliaAnnoMorto;


    /**
     * Recupera una lista (array) di records Bio che usano questa istanza di Giorno nella property giornoNato
     * oppure
     * Recupera una lista (array) di records Bio che usano questa istanza di Giorno nella property giornoMorto
     * oppure
     * Recupera una lista (array) di records Bio che usano questa istanza di Anno nella property annoNato
     * oppure
     * Recupera una lista (array) di records Bio che usano questa istanza di Anno nella property annoMorto
     * <p>
     * Sovrascritto nella sottoclasse concreta
     *
     * @return lista delle istanze di Bio che usano questo istanza nella property appropriata
     */
    public ArrayList<Bio> listaBio() {
        return bioService.findAllByAnnoMorto(anno.titolo);
    }// fine del metodo


    /**
     * Recupera dalla entity Bio, la property giornoNato
     * oppure
     * Recupera dalla entity Bio, la property giornoMorto
     *
     * @param bio entity
     *
     * @return property Bio richiesta
     */
    public String getKeyText(Bio bio) {
        return bio.getGiornoMorto();
    }// fine del metodo


    /**
     * Recupera dalla entity Bio, la didascalia giornoNato
     * oppure
     * Recupera dalla entity Bio, la didascalia giornoMorto
     *
     * @param bio entity
     *
     * @return didascalia Bio richiesta
     */
    public String getDidascalia(Bio bio) {
        return didascaliaAnnoMorto.esegue(bio);
    }// fine del metodo

}// end of class
