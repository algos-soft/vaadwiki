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
 * Esegue un ciclo (NEW) di controllo e creazione di nuovi records esistenti nella categoria sul server e mancanti nel database
 * Esegue un ciclo (UPDATE) di controllo e aggiornamento di tutti i records esistenti nel database
 * Esegue un ciclo (DELETE) di cancellazione di records esistenti nel database e mancanti nella categoria
 * Esegue un ciclo (ELABORA) di elaborazione delle informazioni grezze
 * Esegue un ciclo (UPLOAD) di costruzione delle liste
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
     * Legge la collezione Categorie dal mongoDB
     * Legge le voci Bio esistenti
     * <p>
     * Trova la differenza positiva (records eccedenti)
     * Esegue un ciclo (DELETE) di cancellazione di records esistenti nel database e mancanti nella categoria
     * Cancella tutti i records non più presenti nella categoria
     * <p>
     * Trova la differenza negativa (records mancanti)
     * Esegue un ciclo (NEW) di controllo e creazione di nuovi records esistenti sul server e mancanti nel database
     * Scarica la lista di voci mancanti dal server e crea i nuovi records di Bio
     * <p>
     * Trova tutte le pagine modificate sul server DOPO l'ultima lettura
     * Esegue un ciclo (UPDATE) di controllo e aggiornamento di tutti i records esistenti nel database
     * Aggiorna tutti i records che sono stati modificati sul servere wiki DOPO l'ultima lettura
     * <p>
     * <p>
     * Esegue un ciclo (ELABORA) di elaborazione delle informazioni grezze
     * Esegue un ciclo (UPLOAD) di costruzione delle liste
     */
    @SuppressWarnings("unchecked")
    public void esegue() {
        ArrayList<Long> listaVociCategoriaDelServer;
        ArrayList<Long> listaEsistentiSulDataBaseLocale;
        ArrayList<Long> listaVociMancanti;
        ArrayList<Long> listaVociEccedenti;

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
        categoriaService.download();

        //--recupera la lista delle voci dalla collezione categoria
        listaVociCategoriaDelServer = categoriaService.findPageids();

        //--recupera la lista delle voci dalla collezione categoria
        listaEsistentiSulDataBaseLocale = bioService.findPageids();

        //--elabora le liste delle differenze per la sincronizzazione
        listaVociMancanti = LibWiki.delta(listaVociCategoriaDelServer, listaEsistentiSulDataBaseLocale);

        //--elabora le liste delle differenze per la sincronizzazione
        listaVociEccedenti = LibWiki.delta(listaEsistentiSulDataBaseLocale, listaVociCategoriaDelServer);

        //--Cancella tutti i records non più presenti nella categoria
        deleteService.esegue(listaVociEccedenti);

        //--Scarica la lista di voci mancanti dal server e crea i nuovi records di Bio
        newService.esegue(listaVociMancanti);

//        --elabora i nuovi records
//        elaboraService.esegue(listaVociMancanti);

        //--aggiorna tutti i records che sono stati modificati sul server wiki DOPO l'ultima lettura
        updateService.esegue();

        //--elabora tutti i records che sono stati modificati sul server wiki DOPO l'ultima lettura
//        elaboraService.esegueAll();
    }// end of method


}// end of class
