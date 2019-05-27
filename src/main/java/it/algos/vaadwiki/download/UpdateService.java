package it.algos.vaadwiki.download;

import com.google.common.collect.Lists;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadwiki.service.ABioService;
import it.algos.wiki.DownloadResult;
import it.algos.wiki.WrapTime;
import it.algos.wiki.web.AQueryTimestamp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import static it.algos.vaadwiki.application.WikiCost.WIKI_PAGE_LIMIT;

/**
 * Project vaadbio2
 * Created by Algos
 * User: gac
 * Date: sab, 11-ago-2018
 * Time: 17:46
 * <p>
 * Esegue un ciclo (UPDATE) di controllo e aggiornamento di tutti i records esistenti nel database
 * Ricontrolla (eventualmente) le due liste di pageid che devono essere uguali
 * Spazzola tutte le pagine a blocchi di 'pageLimit' per volta per recuperare il 'timestamp' delle pagine
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
    public DownloadResult esegue() {
        return esegue(new DownloadResult(""));
    }// end of method


    /**
     * Esegue un ciclo (UPDATE) di controllo e aggiornamento di tutte le entities esistenti nel mongoDB Bio
     * Aggiorna tutte le entities mongoDB Bio, che sono stati modificate sul server wiki DOPO l'ultima lettura
     */
    public DownloadResult esegue(DownloadResult result) {
        return esegueCiclo(result);
    }// end of method


//    /**
//     * Ricontrolla (eventualmente) le due liste di pageid che devono essere uguali
//     * oppure
//     * Controlla che il numero di entities della due collezioni (Categoria e Bio) sia lo stesso
//     * <p>
//     * Recupera comunque la lista dei records della collezione categoria, perché serve sempre
//     * Recupera la lista delle pagine dalla categoria sul server wiki, solo se necessario
//     */
//    public boolean checkListePageids() {
//        int catSize = categoriaService.count();
//        int bioSize = bioService.count();
//        return bioSize == catSize;
//    }// end of method


    /**
     * Esegue il ciclo per TUTTE le pagine biografiche esistenti nella collezione mongoDB Bio
     * <p>
     * Esegue dei cicli di elaborazione per ogni 250 pagine (od altro numero)
     * Per ogni ciclo:
     * 1) recupera dalla collezione Bio una lista di 'WrapTime'
     * 2) recupara dal server wiki una lista di 'WrapTime' delle pagine corrispondenti
     * 3) confronta le due liste ed estrae una lista delle pagine modificate dall'ultima lettura
     * 4) rilegge solo le pagine modificate
     * 5) cancella le entities di mongoDB Bio che sono state modificate
     * 6) inserisce (bulk) le pagine modifcate nella collezione Bio
     */
    public DownloadResult esegueCiclo(DownloadResult result) {
        int soglia = 50000;
        int cont = 0;
        long inizio = System.currentTimeMillis();
        int recNum = bioService.count();
        int pageLimit = pref.getInt(WIKI_PAGE_LIMIT);
        int numCicliLetturaPagine = (recNum / pageLimit) + 1;
        Sort sort = new Sort(Sort.Direction.ASC, "pageid");
        String message = "";

        if (pref.isBool(FlowCost.USA_DEBUG)) {
            message = "UPDATE - inizio ";
            log.info(message);
        }// end of if cycle

        for (int k = 0; k < numCicliLetturaPagine; k++) {
            LinkedHashMap<Long, Timestamp> mappa = bioService.findTimestampMap(k, pageLimit, sort);
            esegueSingoloBlocco(mappa, result);

            cont = cont + pageLimit;
            if (cont >= soglia) {
                message = "UPDATE - controllate " + text.format(pageLimit + pageLimit * k) + " pagine wiki e modificati in mongoDB.Bio " + text.format(result.getNumVociCreate()) + " elementi in " + date.deltaText(inizio);
                cont = 0;
                log.info(message);
            }// end of if cycle
        }// end of for cycle

        message = "UPDATE - controllate " + text.format(pageLimit + pageLimit * numCicliLetturaPagine) + " pagine wiki e modificati in mongoDB.Bio " + text.format(result.getNumVociCreate()) + " elementi in " + date.deltaText(inizio);
        log.info(message);
        message = "UPDATE - fine ";
        log.info(message);

        return result;
    }// end of method


    /**
     * Esegue un singolo ciclo di elaborazione:
     * 1) recupera dalla lista di 'WrapTime' una lista di solo 'pageid'
     * 2) recupara dal server wiki una lista di 'WrapTime' delle pagine corrispondenti
     * 3) confronta le due liste ed estrae una lista delle pagine modificate dall'ultima lettura
     * 4) rilegge solo le pagine modificate
     * 5) cancella le entities di mongoDB Bio che sono state modificate
     * 6) inserisce (bulk) le pagine modifcate nella collezione Bio
     */
    public DownloadResult esegueSingoloBlocco(LinkedHashMap<Long, Timestamp> mappa, DownloadResult result) {
        ArrayList<WrapTime> listaWrapTimeServer = null;
        ArrayList<Long> listaVoci = null;
        ArrayList<Long> vociModificateDaRileggere = null;
        Timestamp timestampLocalMongoItalianTime;
        long pageid;
        long deltaOraLegale = 3600000 * 24;//l'ora legale cambia; metto 24H così sono sicuro di beccare le ultime modifiche
        long timeServerOraGMT;
        long timeServerOraItaliana;
        long timeMongo;

        if (mappa != null) {
            listaVoci = Lists.newArrayList(mappa.keySet());
        }// end of if cycle

        if (listaVoci != null) {
            listaWrapTimeServer = ((AQueryTimestamp) appContext.getBean("AQueryTimestamp", listaVoci)).timestampResponse();
        }// end of if cycle

//        if (pref.isBool(FlowCost.USA_DEBUG)) {
//            delta = delta * 200;
//        }// end of if cycle

        if (array.isValid(listaWrapTimeServer)) {
            vociModificateDaRileggere = new ArrayList<>();
            for (WrapTime wrap : listaWrapTimeServer) {
                pageid = wrap.getPageid();
                timeServerOraGMT = wrap.getTimestamp().getTime();
                timeServerOraItaliana = timeServerOraGMT + deltaOraLegale; //ora legale
                timestampLocalMongoItalianTime = mappa.get(pageid);
                if (timestampLocalMongoItalianTime != null) {
                    timeMongo = timestampLocalMongoItalianTime.getTime();
                    if (timeMongo < timeServerOraItaliana) {
                        vociModificateDaRileggere.add(wrap.getPageid());
                        result.addVoceDaAggiornare();
                    }// end of if cycle
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        if (array.isValid(vociModificateDaRileggere)) {
            return pageService.updateSingoloBlocco(result, vociModificateDaRileggere);
        } else {
            return result;
        }// end of if/else cycle
    }// end of method


}// end of class
