package it.algos.vaadwiki.liste;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadwiki.modules.bio.Bio;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: ven, 18-gen-2019
 * Time: 07:56
 * <p>
 * Crea la lista dei nati nel giorno e la carica sul server wiki
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class ListaGiornoNato extends ListaGiorni {


    /**
     * Recupera una lista (array) di records Bio che usano questa istanza di Giorno nella property giornoNato
     * oppure
     * Recupera una lista (array) di records Bio che usano questa istanza di Giorno nella property giornoMorto
     *
     * @return lista delle istanze di Bio che usano questo istanza
     */
    public ArrayList<Bio> listaBio() {
        return bioService.findAllByGiornoNato(giorno.titolo);
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
        return bio.getAnnoNato();
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
        return didascaliaGiornoNato.esegue(bio);
    }// fine del metodo


}// end of class
