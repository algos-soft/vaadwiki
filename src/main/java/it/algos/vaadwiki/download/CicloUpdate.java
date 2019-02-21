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
     * Crea una lista di pageid dalla collezione Categoria sul mongoDB
     * Crea una lista di pageid dalla collezione Bio esistente sul mongoDB
     * <p>
     * Trova la differenza positiva (records eccedenti)
     * Esegue un ciclo (DELETE) di cancellazione di records esistenti nel database e mancanti nella categoria
     * Cancella tutti i records non più presenti nella categoria
     * <p>
     * Trova la differenza negativa (records mancanti)
     * Esegue un ciclo (NEW) di controllo e creazione di nuovi records esistenti sul server e mancanti nel database
     * Scarica la lista di voci mancanti dal server e crea i nuovi records di Bio
     * <p>
     * Ricontrolla (eventualmente) le due liste di pageid che devono essere uguali
     * Esegue un ciclo (UPDATE) di controllo e aggiornamento di tutti i records esistenti nel database
     * Spazzola tutte le voci a blocchi di 'pageLimit' per volta per recuperare il 'timestamp' delle pagine
     * Trova tutte le pagine (del blocco) modificate sul server DOPO l'ultima lettura
     * Aggiorna i records che sono stati modificati sul servere wiki DOPO l'ultima lettura
     * <p>
     * Esegue un ciclo (ELABORA) di elaborazione delle informazioni grezze (eventuale)
     * Esegue un ciclo (UPLOAD) di costruzione delle liste (eventuale)
     */
    @SuppressWarnings("unchecked")
    public DownloadResult esegue() {
        DownloadResult result;
        String nomeCategoria = pref.getStr(CAT_BIO);
        int numVociCategoria;
        ArrayList<String> vociCategoria;
        ArrayList<String> vociBio;
        ArrayList<String> vociEccedenti;
        ArrayList<String> vociMancanti;
        long inizio;

        //--Il ciclo necessita del login valido come bot per il funzionamento normale
        //--oppure del flag USA_CICLI_ANCHE_SENZA_BOT per un funzionamento ridotto
        if (wikiLoginOld != null && wikiLoginOld.isValido() && wikiLoginOld.isBot()) {
        } else {
            return null;
        }// end of if/else cycle

        //--download del modulo attività
        attivitaService.download();

        //--download del modulo nazionalità
        nazionalitaService.download();

        //--download del modulo professione
        professioneService.download();

        //--Recupera la lista delle voci della categoria dal server wiki
        numVociCategoria = appContext.getBean(AQueryCatInfo.class, nomeCategoria).numVoci();
        vociCategoria = appContext.getBean(AQueryCat.class, nomeCategoria).urlRequestTitle();
        if (pref.isBool(FlowCost.USA_DEBUG)) {
            if (numVociCategoria == 0) {
                log.warn("Numero errato di voci sul server");
            }// end of if cycle
            if (vociCategoria == null) {
                log.warn("Non riesco a leggere le voci dal server. Forse non sono loggato come bot");
            }// end of if cycle
            if (numVociCategoria != vociCategoria.size()) {
                log.warn("Le voci della categoria non coincidono: sul server ce ne sono " + text.format(numVociCategoria) + " e ne ha recuperate " + text.format(vociCategoria.size()));
            }// end of if cycle
        }// end of if cycle

        //--recupera la lista dei pageids dalla collezione Bio
        vociBio = bioService.findAllTitles();

        //--elabora le liste delle differenze per la sincronizzazione
        inizio = System.currentTimeMillis();
        if (pref.isBool(FlowCost.USA_DEBUG)) {
            log.info("Debug - Inizio a calcolare le voci in eccedenza. Circa dodici minuti");
        }// end of if cycle
        vociEccedenti = array.differenza(vociBio, vociCategoria);
        if (pref.isBool(FlowCost.USA_DEBUG)) {
            log.info("Calcolate " + text.format(vociEccedenti.size()) + " listaPageidsEccedenti in " + date.deltaText(inizio));
            logger.debug("Calcolate " + text.format(vociEccedenti.size()) + " listaPageidsEccedenti in " + date.deltaText(inizio));
        }// end of if cycle

        //--Cancella dal mongoDB tutte le entities non più presenti nella categoria
        deleteService.esegue(vociEccedenti);

        //--elabora le liste delle differenze per la sincronizzazione
        inizio = System.currentTimeMillis();
        if (pref.isBool(FlowCost.USA_DEBUG)) {
            log.info("Debug - Inizio a calcolare le voci mancanti. Circa dodici minuti");
        }// end of if cycle
        vociMancanti = array.differenza(vociCategoria, vociBio);
        if (pref.isBool(FlowCost.USA_DEBUG)) {
            log.info("Calcolate " + text.format(vociMancanti.size()) + " listaPageidsMancanti in " + date.deltaText(inizio));
            logger.debug("Calcolate " + text.format(vociMancanti.size()) + " listaPageidsMancanti in " + date.deltaText(inizio));
        }// end of if cycle

        //--Scarica dal server la lista di voci mancanti e crea le nuove entities sul mongoDB Bio
        newService.esegue(vociMancanti);

        //--aggiorna tutte le entities mongoDB Bio che sono stati modificate sul server wiki DOPO l'ultima lettura
        result = updateService.esegue();

        log.info("Update - Ciclo totale attività, nazionalità, professione, categoria, nuove voci in " + date.deltaText(result.getInizio()));
        log.info("Fine task di update: " + date.getTime(LocalDateTime.now()));
        log.info("");

        return result;
    }// end of method


}// end of class
