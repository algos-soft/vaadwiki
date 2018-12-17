package it.algos.vaadwiki.service;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.wiki.LibWiki;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;

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
    public void esegue() {
        ArrayList<Long> listaPageidsMongoCategoria;
        ArrayList<Long> listaPageidsMongoBio;
        ArrayList<Long> listaPageidsEccedenti;
        ArrayList<Long> listaPageidsMancanti;
        long inizio;

        //--Il ciclo necessita del login valido come bot per il funzionamento normale
        //--oppure del flag USA_CICLI_ANCHE_SENZA_BOT per un funzionamento ridotto
        if (wikiLogin != null && wikiLogin.isValido() && wikiLogin.isBot()) {
        } else {
            return;
        }// end of if/else cycle

        //--download del modulo attività
        attivitaService.download();

        //--download del modulo nazionalità
        nazionalitaService.download();

        //--download del modulo professione
        professioneService.download();

        //--download del modulo categoria
        //--crea una collezione Categoria, 'specchio' sul mongoDB di quella sul server wiki
        categoriaService.download();

        //--recupera la lista dei pageids dalla collezione Categoria
        listaPageidsMongoCategoria = categoriaService.findPageids();

        //--recupera la lista dei pageids dalla collezione Bio
        listaPageidsMongoBio = bioService.findPageids();

        //--elabora le liste delle differenze per la sincronizzazione
        inizio = System.currentTimeMillis();//@todo provvisorio
        listaPageidsEccedenti = array.differenza(listaPageidsMongoBio, listaPageidsMongoCategoria);
        logger.info("Calcolate " + text.format(listaPageidsEccedenti.size()) + " listaPageidsEccedenti in " + date.deltaText(inizio));

        //--Cancella dal mongoDB tutte le entities non più presenti nella categoria
        deleteService.esegue(listaPageidsEccedenti);

        //--elabora le liste delle differenze per la sincronizzazione
        inizio = System.currentTimeMillis();//@todo provvisorio
        listaPageidsMancanti = LibWiki.delta(listaPageidsMongoCategoria, listaPageidsMongoBio);
        logger.info("Calcolate " + text.format(listaPageidsMancanti.size()) + " listaPageidsMancanti in " + date.deltaText(inizio));


        //--Scarica dal server la lista di voci mancanti e crea le nuove entities sul mongoDB Bio
        newService.esegue(listaPageidsMancanti);

        //--aggiorna tutte le entities mongoDB Bio che sono stati modificate sul server wiki DOPO l'ultima lettura
//        updateService.esegue();

//        --elabora i nuovi records
//        elaboraService.esegue(listaVociMancanti);

        //--elabora tutti i records che sono stati modificati sul server wiki DOPO l'ultima lettura
//        elaboraService.esegueAll();
    }// end of method


}// end of class
