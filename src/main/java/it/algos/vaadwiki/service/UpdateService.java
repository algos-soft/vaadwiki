package it.algos.vaadwiki.service;

import com.google.common.collect.Lists;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.wiki.DownloadResult;
import it.algos.wiki.WrapTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import static it.algos.vaadwiki.application.WikiCost.PAGE_LIMIT;

/**
 * Project vaadbio2
 * Created by Algos
 * User: gac
 * Date: sab, 11-ago-2018
 * Time: 17:46
 * <p>
 * Esegue un ciclo (UPDATE) di controllo e aggiornamento di tutti i records esistenti nel database
 * Ricontrolla (eventualmente) le due liste di pageid che devono essere uguali
 * Spazzola tutte le voci a blocchi di 'pageLimit' per volta per recuperare il 'timestamp' delle pagine
 * Trova tutte le pagine (del blocco) modificate sul server DOPO l'ultima lettura
 * Aggiorna i records che sono stati modificati sul servere wiki DOPO l'ultima lettura
 * <p>
 * Esegue un ciclo (ELABORA) di elaborazione delle informazioni grezze (eventuale)
 * Esegue un ciclo (UPLOAD) di costruzione delle liste (eventuale)
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class UpdateService extends ABioService {


    private ArrayList<Long> listaPageidMongoCategoria;

    private ArrayList<Long> listaPageidMongoBio;


    /**
     * Esegue un ciclo (UPDATE) di controllo e aggiornamento di tutte le entities esistenti nel mongoDB Bio
     * Aggiorna tutte le entities mongoDB Bio, che sono stati modificate sul server wiki DOPO l'ultima lettura
     */
    public void esegue() {
        if (checkListePageids()) {
            esegueCiclo();
            log.info("Algos - Ciclo UPDATE");
        } else {
            mail.send("UpdateService", "Le liste di pageid tra Categoria server e mongoDB, sono diverse");
            log.info("Algos - Ciclo UPDATE -  No buono");
        }// end of if/else cycle
    }// end of method


    /**
     * Ricontrolla (eventualmente) le due liste di pageid che devono essere uguali
     * oppure
     * Controlla che il numero di entities della due collezioni (Categoria e Bio) sia lo stesso
     * <p>
     * Recupera comunque la lista dei records della collezione categoria, perché serve sempre
     * Recupera la lista delle voci dalla categoria sul server wiki, solo se necessario
     */
    public boolean checkListePageids() {
        boolean status = true;
//        //--recupera la lista delle entities dalla collezione Bio
//        listaPageidMongoBio = bioService.findAllPageid();
//
//        if (pref.isBool(USA_CHECK_LISTE_PAGEID)) {
//            //--recupera la lista delle entities dalla collezione Categoria
//            listaPageidMongoCategoria = categoriaService.findAllPageid();
//
//            status = listaPageidMongoCategoria.size() == listaPageidMongoBio.size();
//        }// end of if cycle
//
//        return status;

        return categoriaService.count() == bioService.count();
    }// end of method


    /**
     * Esegue il ciclo per TUTTE le voci biografiche esistenti nella collezione mongoDB Bio
     * <p>
     * Esegue dei cicli di elaborazione per ogni 500 voci (od altro numero)
     * Per ogni ciclo:
     * 1) recupera dalla collezione Bio una lista di 'WrapTime'
     * 2) recupara dal server wiki una lista di 'WrapTime' delle voci corrispondenti
     * 3) confronta le due liste ed estra una lista delle voci modificate dall'ultima lettura
     * 4) rilegge solo le voci modificate
     * 5) cancella le entities di mongoDB Bio che sono state modificate
     * 6) inserisce (bulk) le voci modifcate nella collazione Bio
     */
    public void esegueCiclo() {
        DownloadResult result = null;
        int numVociModificate=0;
        long inizio = System.currentTimeMillis();
        ArrayList<WrapTime> listaWrapTime = null;
        int recNum = bioService.count();
        int pageLimit = pref.getInt(PAGE_LIMIT);
        int numCicliLetturaPagine = (recNum / pageLimit) + 1;
        Sort sort = new Sort(Sort.Direction.ASC, "pageid");

        for (int k = 0; k < numCicliLetturaPagine; k++) {
//            listaWrapTime = bioService.findAllWrapTime(k, pageLimit, sort);
//            esegueSingoloBlocco(listaWrapTime);

            LinkedHashMap<Long, Timestamp> mappa = bioService.findTimestampMap(k, pageLimit, sort);
            result = esegueSingoloBlocco(mappa);
            numVociModificate=result.getNumVociRegistrate();
        }// end of for cycle

        log.info("Algos - Ciclo UPDATE - letti i timestamp della wiki.categoria.BioBot e modificati in mongoDB.Bio (" + text.format(numVociModificate) + " voci) in " + date.deltaText(inizio));
    }// end of method


//    /**
//     * Esegue un singolo ciclo di elaborazione:
//     * 1) recupera dalla lista di 'WrapTime' una lista di solo 'pageid'
//     * 2) recupara dal server wiki una lista di 'WrapTime' delle voci corrispondenti
//     * 3) confronta le due liste ed estra una lista delle voci modificate dall'ultima lettura
//     * 4) rilegge solo le voci modificate
//     * 5) cancella le entities di mongoDB Bio che sono state modificate
//     * 6) inserisce (bulk) le voci modifcate nella collazione Bio
//     */
//    public void esegueSingoloBlocco(ArrayList<WrapTime> listaWrapTimeMongo) {
//        ArrayList<WrapTime> listaWrapTimeServer = null;
//        ArrayList<Long> listaPageidAll = null;
//        ArrayList<Long> listaPageidModificateDaRileggere = null;
//        Timestamp timestampLocalMongo;
//        Timestamp timestampServerWiki;
//
//        if (array.isValid(listaWrapTimeMongo)) {
//            listaPageidAll = new ArrayList<>();
//            for (WrapTime wrap : listaWrapTimeMongo) {
//                listaPageidAll.add(wrap.getPageid());
//            }// end of for cycle
//        }// end of if cycle
//
//        if (array.isValid(listaPageidAll)) {
//            listaWrapTimeServer = api.leggeTimestamp(listaPageidAll);
//        }// end of if cycle
//
//        if (array.isValid(listaWrapTimeServer)) {
//            listaPageidModificateDaRileggere = new ArrayList<>();
//            for (WrapTime wrap : listaWrapTimeMongo) {
//                timestampLocalMongo = wrap.getTimestamp();
//                timestampServerWiki = WrapTime.getTimestamp(listaWrapTimeServer, wrap.getPageid());
//                if (timestampLocalMongo.getTime() < timestampServerWiki.getTime()) {
//                    listaPageidModificateDaRileggere.add(wrap.getPageid());
//                }// end of if cycle
//            }// end of for cycle
//        }// end of if cycle
//
//    }// end of method


    /**
     * Esegue un singolo ciclo di elaborazione:
     * 1) recupera dalla lista di 'WrapTime' una lista di solo 'pageid'
     * 2) recupara dal server wiki una lista di 'WrapTime' delle voci corrispondenti
     * 3) confronta le due liste ed estra una lista delle voci modificate dall'ultima lettura
     * 4) rilegge solo le voci modificate
     * 5) cancella le entities di mongoDB Bio che sono state modificate
     * 6) inserisce (bulk) le voci modifcate nella collazione Bio
     */
    public DownloadResult esegueSingoloBlocco(LinkedHashMap<Long, Timestamp> mappa) {
        DownloadResult result = null;
        ArrayList<WrapTime> listaWrapTimeServer = null;
        ArrayList<Long> listaPageidAll = null;
        ArrayList<Long> listaPageidModificateDaRileggere = null;
        Timestamp timestampLocalMongo;
        Timestamp timestampServerWiki;
        Long pageid;

        if (mappa != null) {
            listaPageidAll = Lists.newArrayList(mappa.keySet());
        }// end of if cycle

        if (listaPageidAll != null) {
            listaWrapTimeServer = api.leggeTimestamp(listaPageidAll);
        }// end of if cycle

        if (array.isValid(listaWrapTimeServer)) {
            listaPageidModificateDaRileggere = new ArrayList<>();
            for (WrapTime wrap : listaWrapTimeServer) {
                timestampServerWiki = wrap.getTimestamp();
                pageid = wrap.getPageid();
                timestampLocalMongo = mappa.get(pageid);
                if (timestampLocalMongo.getTime() < timestampServerWiki.getTime()) {
                    listaPageidModificateDaRileggere.add(wrap.getPageid());
                }// end of if cycle
            }// end of for cycle

            result = pageService.downloadNewPagine(listaPageidModificateDaRileggere);
            if (result.isValid()) {
                log.info("Algos - Ciclo UPDATE blocco - modificate (" + text.format(result.getNumVociRegistrate()) + " voci)");
            } else {
                log.warn("Algos - Ciclo UPDATE blocco - qualcosa non funzione");
            }// end of if/else cycle
        } else {
            log.info("Algos - Ciclo UPDATE blocco - nessuna nuova voce da scaricare");
        }// end of if/else cycle

        return result;
    }// end of method


//    /**
//     * Legge i timestamp del blocco di voci (probabilmente 500)
//     * Confronta, per tutta la lista di (500) voci, i timestamp di ogni singola voce con il valore dell'ultima lettura della voce stessa
//     * Crea una lista di voci modificate dalla data dell'ultima lettura
//     * Scarica, elabora e registra tutte le voci modificate che sono state individuate nel blocco di voci
//     */
//    public void leggeTimestampBlocco(ArrayList<Long> bloccoVoci) {
//        long inizio = System.currentTimeMillis();
//        ArrayList<WrapTime> lista;
//        Bio entity;
//        LocalDateTime time = null;
//
//        lista = api.leggeTimestamp(bloccoVoci);
//        if (array.isValid(lista)) {
//            for (WrapTime wrap : lista) {
//                time = wrap.getDate();
//
//                if (time != null) {
//                    entity = bioService.findByKeyUnica(wrap.getPageid());
//                    entity.lastModifica = time;
//                    if (!entity.sporca) { //--se era sporca, rimane sporca
//                        entity.sporca = time.isAfter(entity.lastLettura);
//                    }// end of if cycle
//                    entity.lastLettura = LocalDateTime.now();
//                    bioService.save(entity);
//                }// end of if cycle
//
//            }// end of for cycle
//        }// end of if cycle
//        log.info("Algos - Ciclo PIPPO - letti i timestamp della categoria BioBot e registrati in mongoDB Bio (" + text.format(bloccoVoci.size()) + " elementi) in " + date.deltaText(inizio));
//    }// end of method


//    /**
//     * Scarica dal server wiki le SOLE voci 'sporche' (con ultimaModifica successiva a ultimaLettura)
//     * Recupera dal mongoDB Bio le sole voci 'sporche'
//     * Scarica le pagine a blocchi di 500 e le registra nel mongoDB Bio
//     */
//    public void leggeVociRecentementeModificate() {
//        DownloadResult result;
//        long inizio = System.currentTimeMillis();
//        int numVociRegistrate = 0;
//        ArrayList<Long> lista = bioService.findSporcaPageId();
//
//        if (array.isValid(lista)) {
//            result = pageService.downloadUpdatePagine(lista);
//            log.info("Algos - Ciclo UPDATE - download delle voci modificate successivamente all'ultima lettura (" + text.format(numVociRegistrate) + " elementi) in " + date.deltaText(inizio));
//        } else {
//            log.info("Algos - Ciclo UPDATE - nessuna voce modificata successivamente all'ultima lettura");
//        }// end of if/else cycle
//
//    }// end of method
//
}// end of class
