package it.algos.vaadwiki.download;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadwiki.service.ABioService;
import it.algos.wiki.DownloadResult;
import it.algos.wiki.web.AQueryCat;
import it.algos.wiki.web.AQueryCatInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.time.LocalDateTime;

import static it.algos.vaadwiki.application.WikiCost.CAT_BIO;
import static it.algos.vaadwiki.application.WikiCost.SEND_MAIL_CICLO;

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
     * <p>
     * Recupera dal server wiki la lista delle voci della categoria
     * Recupera dal server wiki il totale delle voci della categoria per un controllo
     * <p>
     * Esegue un ciclo (NEW) di creazione di nuovi records esistenti sul server e mancanti nel database
     * <p>
     * Manda una mail di conferma (se previsto)
     *
     * @return wrapper di dati risultanti
     */
    @SuppressWarnings("unchecked")
    public DownloadResult esegue() {
        DownloadResult result = new DownloadResult(pref.getStr(CAT_BIO));
        String message = "";

        if (pref.isBool(FlowCost.USA_DEBUG)) {
            log.info("");
            log.info("Inizio task di download: " + date.getTime(result.getInizio()));
        }// end of if cycle

        //--Download del modulo attività
        attivitaService.download();

        //--Download del modulo nazionalità
        nazionalitaService.download();

        //--Download del modulo professione
        professioneService.download();

        //--Recupera la lista delle voci della categoria dal server wiki
        result.setNumVociCategoria(appContext.getBean(AQueryCatInfo.class, result.getNomeCategoria()).numVoci());
        result.setVociDaCreare(appContext.getBean(AQueryCat.class, result.getNomeCategoria()).urlRequestTitle());
        if (pref.isBool(FlowCost.USA_DEBUG)) {
            if (result.getNumVociCategoria() == 0) {
                message = "Numero errato di voci sul server";
                log.warn(message);
                logger.warning("Download - " + message);
            }// end of if cycle
            if (result.getVociDaCreare() == null) {
                message = "Non riesco a leggere le voci dal server. Forse non sono loggato come bot";
                log.warn(message);
                logger.warning("Download - " + message);
            }// end of if cycle
            if (result.getNumVociCategoria() != result.getVociDaCreare().size()) {
                message = "Le voci della categoria non coincidono: sul server ce ne sono " + text.format(result.getNumVociCategoria()) + " e ne ha recuperate " + text.format(result.getVociDaCreare().size());
                log.warn(message);
                logger.warning("Download - " + message);
            }// end of if cycle
        }// end of if cycle

        //--Scarica dal server tutte le voci mancanti e crea le nuove entities sul mongoDB Bio
        result = newService.esegue(result);

        if (pref.isBool(SEND_MAIL_CICLO)) {
            libBio.sendDownload(result);
        }// end of if cycle

        if (pref.isBool(FlowCost.USA_DEBUG)) {
            log.info("Download - Ciclo totale attività, nazionalità, professione, categoria, nuove voci in " + date.deltaText(result.getInizioLong()));
            log.info("Fine task di download: " + date.getTime(LocalDateTime.now()));
            log.info("");
        }// end of if cycle

        return result;
    }// end of method

}// end of class
