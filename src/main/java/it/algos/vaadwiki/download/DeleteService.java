package it.algos.vaadwiki.download;

import com.mongodb.client.result.DeleteResult;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.service.ABioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.List;

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
     * Cancella dal mongoDB la lista di pagine non più presenti nella categoria BioBot sul server wikipedia
     *
     * @param vociEccedenti elenco (title) delle pagine eccedenti da cancellare
     */
    public void esegue(List<Long> vociEccedenti) {
        long inizio = System.currentTimeMillis();
        DeleteResult result;
        Bio bio;
        String wikiTitle;

        if (array.isValid(vociEccedenti)) {
            result = mongo.deleteBulkByProperty(vociEccedenti, Bio.class, "pageid");
            if (result.getDeletedCount() < 1) {
                logger.error("DELETE - Non sono riuscito ad eliminare nessuna voce delle " + vociEccedenti.size() + " eccedenti");
                for (Long pageid : vociEccedenti) {
                    bio = bioService.downloadBio(pageid);
                    wikiTitle = bio != null ? bio.getWikiTitle() : "";
                    logger.error("DELETE - " + (text.isEmpty(wikiTitle) ? "Questa proprio non ci sta: "+pageid+" (pageid)" : "Non pervenuta"));
                }// end of for cycle

            } else {
                logger.info("DELETE - eliminate le pagine da mongoDB Bio (" + result.getDeletedCount() + " elementi) in " + date.deltaText(inizio));
            }// end of if/else cycle
        } else {
            if (pref.isBool(FlowCost.USA_DEBUG)) {
                logger.info("DELETE - nessuna voce deprecata da eliminare");
            }// end of if cycle
        }// end of if/else cycle

    }// end of method

}// end of class
