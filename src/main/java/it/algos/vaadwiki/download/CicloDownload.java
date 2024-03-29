package it.algos.vaadwiki.download;

import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow.application.*;
import static it.algos.vaadwiki.application.WikiCost.*;
import it.algos.vaadwiki.service.*;
import it.algos.wiki.*;
import it.algos.wiki.web.*;
import lombok.extern.slf4j.*;
import org.slf4j.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import javax.annotation.*;
import java.time.*;

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
     * Recupera dal server wiki la lista delle pagine della categoria
     * Recupera dal server wiki il totale delle pagine della categoria per un controllo
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
            logger.info("");
            logger.info("Inizio task di download: " + date.getTime(result.getInizio()));
        }// end of if cycle

        //--Download del modulo attività
        attivitaService.download();

        //--Download del modulo nazionalità
        nazionalitaService.download();

        //--Download del modulo professione
        professioneService.download();

        //--download del modulo genere
        genereService.download();

        //--Recupera la lista delle pagine della categoria dal server wiki
        result.setNumVociCategoria(appContext.getBean(AQueryCatInfo.class, result.getNomeCategoria()).numVoci());
        result.setVociDaCreare(appContext.getBean(AQueryCatPaginePageid.class, result.getNomeCategoria()).listaPageid);

        if (pref.isBool(FlowCost.USA_DEBUG)) {
            if (result.getNumVociCategoria() == 0) {
                message = "Numero errato di pagine sul server";
                logger.warn(message);
                logger.warn("Download - " + message);
            }// end of if cycle
            if (result.getVociDaCreare() == null) {
                message = "Non riesco a leggere le pagine dal server. Forse non sono loggato come bot";
                logger.warn(message);
                logger.warn("Download - " + message);
            }// end of if cycle
            if (result.getNumVociCategoria() != result.getVociDaCreare().size()) {
                message = "Le pagine della categoria non coincidono: sul server ce ne sono " + text.format(result.getNumVociCategoria()) + " e ne ha recuperate " + text.format(result.getVociDaCreare().size());
                logger.warn(message);
                logger.warn("Download - " + message);
            }// end of if cycle
        }// end of if cycle

        //--Scarica dal server tutte le pagine mancanti e crea le nuove entities sul mongoDB Bio
        result = newService.esegue(result);

        if (pref.isBool(SEND_MAIL_CICLO)) {
            libBio.sendDownload(result);
        }// end of if cycle

        if (pref.isBool(FlowCost.USA_DEBUG)) {
            logger.info("Download - Ciclo totale attività, nazionalità, professione, categoria, nuove pagine in " + date.deltaText(result.getInizioLong()));
            logger.info("Fine task di download: " + date.getTime(LocalDateTime.now()));
            logger.info("");
        }// end of if cycle

        return result;
    }// end of method

}// end of class
