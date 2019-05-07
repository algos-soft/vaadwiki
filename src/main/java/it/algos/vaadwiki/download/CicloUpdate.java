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
import java.util.ArrayList;

import static it.algos.vaadwiki.application.WikiCost.CAT_BIO;
import static it.algos.vaadwiki.application.WikiCost.SEND_MAIL_CICLO;

/**
 * Project vaadbio2
 * Created by Algos
 * User: gac
 * Date: sab, 11-ago-2018
 * Time: 15:44
 * Esegue un ciclo completo di sincronizzazione tra le pagine della categoria TAG_BIO e le entities della collezione Bio
 * <p>
 * Esegue un ciclo (DELETE) di cancellazione di records esistenti nel database e mancanti nella categoria
 * Esegue un ciclo (NEW) di controllo e creazione di nuovi records esistenti nella categoria sul server e mancanti nel database
 * Esegue un ciclo (UPDATE) di controllo e aggiornamento di tutti i records esistenti nel database
 * Esegue un ciclo (ELABORA) di elaborazione delle informazioni grezze (eventuale - not for now)
 * Esegue un ciclo (UPLOAD) di costruzione delle liste (eventuale - not for now)
 * <p>
 * Il ciclo viene chiamato da DaemonBio (con frequenza giornaliera)
 * Il ciclo può essere invocato dal bottone 'Ciclo' nella tavola Bio
 * Il ciclo necessita del login come bot per il funzionamento
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class CicloUpdate extends ABioService {


    /**
     * Aggiorna le ATTIVITA, con un download del modulo attività
     * Aggiorna le NAZIONALITA, con un download del modulo nazionalità
     * Aggiorna le PROFESSIONI, con un download del modulo professioni
     * <p>
     * Recupera dal server wiki la lista delle pagine della categoria
     * Recupera dal server wiki il totale delle pagine della categoria per un controllo
     * <p>
     * Crea una lista di titles dalla collezione Bio esistente sul mongoDB
     * <p>
     * Trova la differenza positiva (records eccedenti)
     * Esegue un ciclo (DELETE) di cancellazione di records esistenti nel database e mancanti nella categoria
     * Cancella tutti i records non più presenti nella categoria
     * <p>
     * Trova la differenza negativa (records mancanti)
     * Esegue un ciclo (NEW) di creazione di nuovi records esistenti sul server e mancanti nel database
     * Scarica la lista di pagine mancanti dal server e crea i nuovi records di Bio
     * <p>
     * Esegue un ciclo (UPDATE) di controllo e aggiornamento di tutti i records esistenti nel database
     * Spazzola tutte le pagine a blocchi di 'pageLimit' per volta per recuperare il 'timestamp' delle pagine
     * Trova tutte le pagine (del blocco) modificate sul server DOPO l'ultima lettura
     * Aggiorna i records che sono stati modificati sul servere wiki DOPO l'ultima lettura
     * <p>
     * Manda una mail di conferma (se previsto)
     */
    @SuppressWarnings("unchecked")
    public DownloadResult esegue() {
        DownloadResult result = new DownloadResult(pref.getStr(CAT_BIO));
        long inizio;
        String message = "";
        ArrayList<String> vociBio;

        if (pref.isBool(FlowCost.USA_DEBUG)) {
            log.info("");
            log.info("Inizio task di update: " + date.getTime(result.getInizio()));
        }// end of if cycle

        //--download del modulo attività
        attivitaService.download();

        //--download del modulo nazionalità
        nazionalitaService.download();

        //--download del modulo professione
        professioneService.download();

        //--Recupera la lista delle pagine della categoria dal server wiki
        result.setNumVociCategoria(appContext.getBean(AQueryCatInfo.class, result.getNomeCategoria()).numVoci());
        result.setVociDaCreare(appContext.getBean(AQueryCat.class, result.getNomeCategoria()).lista);
        if (pref.isBool(FlowCost.USA_DEBUG)) {
            if (result.getNumVociCategoria() == 0) {
                message = "Numero errato di pagine sul server";
                log.warn(message);
                logger.warning("Download - " + message);
            }// end of if cycle
            if (result.getVociDaCreare() == null) {
                message = "Non riesco a leggere le pagine dal server. Forse non sono loggato come bot";
                log.warn(message);
                logger.warning("Download - " + message);
            }// end of if cycle
            if (result.getNumVociCategoria() != result.getVociDaCreare().size()) {
                message = "Le pagine della categoria non coincidono: sul server ce ne sono " + text.format(result.getNumVociCategoria()) + " e ne ha recuperate " + text.format(result.getVociDaCreare().size());
                log.warn(message);
                logger.warning("Download - " + message);
            }// end of if cycle
        }// end of if cycle

        //--recupera la lista dei titles dalla collezione Bio
        vociBio = bioService.findAllTitles();

        //--elabora le liste delle differenze per la sincronizzazione
        inizio = System.currentTimeMillis();
        if (pref.isBool(FlowCost.USA_DEBUG)) {
            log.info("Debug - Inizio a calcolare le pagine in eccedenza. Circa dodici minuti");
        }// end of if cycle
        result.setVociDaCancellare(array.differenza(vociBio, result.getVociDaCreare()));
        if (pref.isBool(FlowCost.USA_DEBUG)) {
            log.info("Calcolate " + text.format(result.getVociDaCancellare().size()) + " vociDaCancellare in " + date.deltaText(inizio));
            logger.debug("Calcolate " + text.format(result.getVociDaCancellare().size()) + " vociDaCancellare in " + date.deltaText(inizio));
        }// end of if cycle

        //--Cancella dal mongoDB tutte le entities non più presenti nella categoria
        deleteService.esegue(result.getVociDaCancellare());

        //--elabora le liste delle differenze per la sincronizzazione
        inizio = System.currentTimeMillis();
        if (pref.isBool(FlowCost.USA_DEBUG)) {
            log.info("Debug - Inizio a calcolare le pagine mancanti. Circa dodici minuti");
        }// end of if cycle
        result.setVociDaCreare(array.differenza(result.getVociDaCreare(), vociBio));
        if (pref.isBool(FlowCost.USA_DEBUG)) {
            log.info("Calcolate " + text.format(result.getVociDaCreare().size()) + " listaPageidsMancanti in " + date.deltaText(inizio));
            logger.debug("Calcolate " + text.format(result.getVociDaCreare().size()) + " listaPageidsMancanti in " + date.deltaText(inizio));
        }// end of if cycle

        //--Scarica dal server la lista di pagine mancanti e crea le nuove entities sul mongoDB Bio
        result = newService.esegue(result);

        //--aggiorna tutte le entities mongoDB Bio che sono stati modificate sul server wiki DOPO l'ultima lettura
        result = updateService.esegue(result);

        if (pref.isBool(SEND_MAIL_CICLO)) {
            libBio.sendUpdate(result);
        }// end of if cycle

        if (pref.isBool(FlowCost.USA_DEBUG)) {
            log.info("Update - Ciclo totale attività, nazionalità, professione, categoria, nuove pagine in " + date.deltaText(result.getInizioLong()));
            log.info("Fine task di update: " + date.getTime(LocalDateTime.now()));
            log.info("");
        }// end of if cycle

        return result;
    }// end of method


}// end of class
