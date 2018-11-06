package it.algos.vaadwiki.service;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadwiki.modules.bio.Bio;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;

/**
 * Project vaadbio2
 * Created by Algos
 * User: gac
 * Date: sab, 11-ago-2018
 * Time: 21:45
 * <p>
 * Esegue un ciclo (DELETE) di cancellazione di records esistenti nel database e mancanti nella categoria
 * Cancella tutti i records non più presenti nella categoria
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class DeleteService extends ABioService {

    /**
     * Cancella dal mongoDB la lista di voci non più presenti nella categoria BioBot sul server wikipedia
     *
     * @param listaVociEccedenti elenco (pageids) delle pagine eccedenti da cancellare
     */
    public void esegue(ArrayList<Long> listaVociEccedenti) {
        long inizio = System.currentTimeMillis();
        Bio bio;

        if (array.isValid(listaVociEccedenti)) {
            for (Long pageid : listaVociEccedenti) {
                bio = bioService.findByKeyUnica(pageid);
//                bioService.delete(bio);
//                bioService.d
            }// end of for cycle @todo rimettere
            log.info("Algos - Ciclo DELETE - eliminate le voci da mongoDB Bio (" + listaVociEccedenti.size() + " elementi) in " + date.deltaText(inizio));
        } else {
            log.info("Algos - Ciclo DELETE - nessuna voce deprecata da eliminare");
        }// end of if/else cycle

    }// end of method

}// end of class
