package it.algos.vaadwiki.download;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadwiki.service.ABioService;
import it.algos.wiki.DownloadResult;
import it.algos.wiki.web.AQueryCat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

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
 * Esegue un ciclo (ELABORA) di elaborazione delle informazioni grezze (eventuale)
 * Esegue un ciclo (UPLOAD) di costruzione delle liste (eventuale)
 * <p>
 * Il ciclo viene chiamato da DaemonBio (con frequenza giornaliera)
 * Il ciclo può essere invocato dal bottone 'Ciclo' nella tavola Bio
 * Il ciclo necessita del login come bot per il funzionamento normale
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class CicloService extends ABioService {

    @Autowired
    protected ApplicationContext appContext;

    /**
     * Aggiorna le ATTIVITA, con un download del modulo attività
     * Aggiorna le NAZIONALITA, con un download del modulo nazionalità
     * Aggiorna le PROFESSIONI, con un download del modulo professioni
     * Aggiorna le CATEGORIE, con un download di tutte le voci della categoria BioBot
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
        ArrayList<String> listaTitlesCategoria;
        ArrayList<String> listaTitlesMongoBio;
        ArrayList<Long> listaTitlesEccedenti;
        ArrayList<Long> listaTitlesMancanti;
        long inizio;

        //--Il ciclo necessita del login valido come bot per il funzionamento normale
        //--oppure del flag USA_CICLI_ANCHE_SENZA_BOT per un funzionamento ridotto
        if (wikiLogin != null && wikiLogin.isValido() && wikiLogin.isBot()) {
        } else {
            return null;
        }// end of if/else cycle

        //--download del modulo attività
        attivitaService.download();

        //--download del modulo nazionalità
        nazionalitaService.download();

        //--download del modulo professione
        professioneService.download();

        //--recupera la lista dei pageids dalla collezione Categoria
        listaTitlesCategoria  = appContext.getBean(AQueryCat.class, pref.getStr(CAT_BIO)).urlRequestTitle();

        //--recupera la lista dei pageids dalla collezione Bio
        listaTitlesMongoBio = bioService.findAllTitles();

        //--elabora le liste delle differenze per la sincronizzazione
        inizio = System.currentTimeMillis();
        if (pref.isBool(FlowCost.USA_DEBUG)) {
            log.info("Debug - Inizio a calcolare le voci in eccedenza. Circa sette minuti");
        }// end of if cycle
        listaTitlesEccedenti = array.differenza(listaTitlesMongoBio, listaTitlesCategoria);
        if (pref.isBool(FlowCost.USA_DEBUG)) {
            log.info("Calcolate " + text.format(listaTitlesEccedenti.size()) + " listaPageidsEccedenti in " + date.deltaText(inizio));
            logger.debug("Calcolate " + text.format(listaTitlesEccedenti.size()) + " listaPageidsEccedenti in " + date.deltaText(inizio));
        }// end of if cycle

        //--Cancella dal mongoDB tutte le entities non più presenti nella categoria
        deleteService.esegue(listaTitlesEccedenti);

        //--elabora le liste delle differenze per la sincronizzazione
        inizio = System.currentTimeMillis();
        if (pref.isBool(FlowCost.USA_DEBUG)) {
            log.info("Debug - Inizio a calcolare le voci mancanti. Circa sette minuti");
        }// end of if cycle
        listaTitlesMancanti = array.differenza(listaTitlesCategoria, listaTitlesMongoBio);
        if (pref.isBool(FlowCost.USA_DEBUG)) {
            log.info("Calcolate " + text.format(listaTitlesMancanti.size()) + " listaPageidsMancanti in " + date.deltaText(inizio));
            logger.debug("Calcolate " + text.format(listaTitlesMancanti.size()) + " listaPageidsMancanti in " + date.deltaText(inizio));
        }// end of if cycle

        //--Scarica dal server la lista di voci mancanti e crea le nuove entities sul mongoDB Bio
        newService.esegue(listaTitlesMancanti);

        //--aggiorna tutte le entities mongoDB Bio che sono stati modificate sul server wiki DOPO l'ultima lettura
        result = updateService.esegue();

//        --elabora i nuovi records
//        elaboraService.esegue(listaVociMancanti);

        //--elabora tutti i records che sono stati modificati sul server wiki DOPO l'ultima lettura
//        elaboraService.esegueAll();

        return result;
    }// end of method


}// end of class
