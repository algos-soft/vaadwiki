package it.algos.vaadwiki.service;

import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static it.algos.vaadwiki.application.VaadwikiCost.LAST_DOWNLOAD_BIO;

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
     * Scarica dal server tutte le voci indicate e crea i nuovi records di Bio
     * <p>
     * Controlla il flag USA_LIMITE_DOWNLOAD
     * Usa il numero massimo (MAX_DOWNLOAD) di voci da scaricare in totale (se USA_LIMITE_DOWNLOAD=true)
     * Esegue una serie di RequestWikiReadMultiPages a blocchi di PAGES_PER_REQUEST per volta
     * Per ogni page crea un record bio
     *
     * @param listaVociMancanti elenco (pageids) delle pagine mancanti da scaricare
     */
    public void esegue(ArrayList<Long> listaVociMancanti) {
        long inizio = System.currentTimeMillis();
        int numVociRegistrate = 0;

        if (array.isValid(listaVociMancanti)) {
            numVociRegistrate = pageService.downloadNewPagine(listaVociMancanti);
            pref.setDate(LAST_DOWNLOAD_BIO, LocalDateTime.now());
            log.info("Algos - Ciclo NEW - download di nuove voci e creazione in mongoDB Bio (" + text.format(numVociRegistrate) + " elementi) in " + date.deltaText(inizio));
        } else {
            log.info("Algos - Ciclo NEW - nessuna nuova voce da scaricare");
        }// end of if/else cycle

    }// end of method


}// end of class
