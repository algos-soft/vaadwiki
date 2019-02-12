package it.algos.vaadwiki.download;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadwiki.service.ABioService;
import it.algos.wiki.DownloadResult;
import it.algos.wiki.web.AQueryCat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static it.algos.vaadwiki.application.WikiCost.CAT_BIO;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mer, 06-feb-2019
 * Time: 17:12
 * Esegue un ciclo completo di creazione delle entities della collezione Bio
 * <p>
 * Esegue un ciclo (NEW) di controllo e creazione di nuovi records esistenti nella categoria sul server e mancanti nel database
 * <p>
 * Il ciclo viene chiamato da DaemonBio (con frequenza bimensile)
 * Il ciclo può essere invocato dal bottone 'Ciclo' nella tavola Bio
 * Il ciclo necessita del login come bot per il funzionamento
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class CicloDownload extends ABioService {

    /**
     * Aggiorna le ATTIVITA, con un download del modulo attività
     * Aggiorna le NAZIONALITA, con un download del modulo nazionalità
     * Aggiorna le PROFESSIONI, con un download del modulo professioni
     * Recupera la lista delle voci della categoria dal server wiki
     * <p>
     * Esegue un ciclo (NEW) di controllo e creazione di nuovi records esistenti sul server e mancanti nel database
     * Scarica la lista di voci mancanti dal server e crea i nuovi records di Bio
     */
    @SuppressWarnings("unchecked")
    public DownloadResult esegue() {
        DownloadResult result = new DownloadResult();
        String nomeCategoria = pref.getStr(CAT_BIO);
        ArrayList<String> listaVociCategoria;
        long inizio = System.currentTimeMillis();
        result.setInizio(inizio);
        result.setNomeCategoria(nomeCategoria);
        log.info("" );
        log.info("Inizio task di download: " + date.getTime(LocalDateTime.now()));

        //--Download del modulo attività
        attivitaService.download();

        //--Download del modulo nazionalità
        nazionalitaService.download();

        //--Download del modulo professione
        professioneService.download();

        //--Recupera la lista delle voci della categoria dal server wiki
        listaVociCategoria = appContext.getBean(AQueryCat.class, nomeCategoria).urlRequestTitle();

        //--Scarica dal server tutte le voci mancanti e crea le nuove entities sul mongoDB Bio
        newService.esegue(listaVociCategoria);

        log.info("Download - Ciclo totale attività, nazionalità, professione, categoria, nuove voci in " + date.deltaText(result.getInizio()));
        log.info("Fine task di download: " + date.getTime(LocalDateTime.now()));
        log.info("" );

        return result;
    }// end of method

}// end of class
