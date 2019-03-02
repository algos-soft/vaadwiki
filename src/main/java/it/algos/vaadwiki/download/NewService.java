package it.algos.vaadwiki.download;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadwiki.service.ABioService;
import it.algos.wiki.DownloadResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.time.LocalDateTime;

import static it.algos.vaadwiki.application.WikiCost.LAST_DOWNLOAD_BIO;

/**
 * Project vaadbio2
 * Created by Algos
 * User: gac
 * Date: sab, 11-ago-2018
 * Time: 17:48
 * <p>
 * Esegue un ciclo (NEW) di controllo e creazione di nuovi records esistenti sul server e mancanti nel database
 * Scarica la lista di voci mancanti dal server e crea i nuovi records di Bio
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class NewService extends ABioService {


    /**
     * Esegue un ciclo (NEW) di creazione di nuovi records esistenti sul server e mancanti nel database
     * Scarica dal server tutte le voci indicate e crea le nuove entities sul mongoDB Bio
     * <p>
     * Esegue una serie di AQueryPages a blocchi di WIKI_PAGE_LIMIT (250) per volta <br>
     * Per ogni page crea la entity (Bio) corrispondente <br>
     *
     * @param result wrapper di dati risultanti
     *
     * @return wrapper di dati risultanti
     */
    public DownloadResult esegue(DownloadResult result) {
        long inizio = System.currentTimeMillis();

        if (array.isValid(result.getVociDaCreare())) {
            result = pageService.downloadPagine(result);
            pref.saveValue(LAST_DOWNLOAD_BIO, LocalDateTime.now());

            if (result.getNumVociCreate() > 0) {
                logger.info("NEW - download di nuove voci e creazione in mongoDB Bio (" + text.format(result.getNumVociCreate()) + " voci) in " + date.deltaText(inizio));
            }// end of if cycle
        } else {
            logger.info("NEW - nessuna nuova voce da scaricare");
        }// end of if/else cycle

        return result;
    }// end of method



}// end of class
