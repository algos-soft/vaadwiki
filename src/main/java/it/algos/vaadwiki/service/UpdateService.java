package it.algos.vaadwiki.service;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.wiki.WrapTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Project vaadbio2
 * Created by Algos
 * User: gac
 * Date: sab, 11-ago-2018
 * Time: 17:46
 * Esegue un ciclo (UPDATE) di controllo e aggiornamento di tutti i records esistenti nel database
 * Aggiorna tutti i records che sono stati modificati sul server wiki DOPO l'ultima lettura
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class UpdateService extends ABioService {


    /**
     * Legge i timestamp di TUTTE le voci (biografiche) a blocchi di 500 e li registra nella collezione Bio
     */
    public void esegue() {
        ArrayList<Long> listaVociCategoriaSuServer;

        //--recupera la lista delle voci dal mongoDB Categoria
        listaVociCategoriaSuServer = categoriaService.findPageids();

        //--legge e registra i timestamp di TUTTE le voci biografiche
        leggeTimestampAll(listaVociCategoriaSuServer);

        //--scarica dal server wiki le SOLE voci 'sporche' (con ultimaModifica successiva a ultimaLettura)
        leggeVociRecentementeModificate();
    }// end of method


    /**
     * Legge i timestamp di TUTTE le voci (biografiche)
     * Legge e registra a blocchi di 500
     */
    public void leggeTimestampAll(ArrayList<Long> listaVociCategoriaSuServer) {
        long inizio = System.currentTimeMillis();
        List<Long> listaBlocco500Voci;
        int dimBloccoLettura = PageService.BLOCCO_PAGES;
        int numCicliLetturaPagine;

        if (listaVociCategoriaSuServer != null && listaVociCategoriaSuServer.size() > 0) {
            numCicliLetturaPagine = array.numCicli(listaVociCategoriaSuServer.size(), dimBloccoLettura);
            for (int k = 0; k < numCicliLetturaPagine; k++) {
                listaBlocco500Voci = array.estraeSublistaLong(listaVociCategoriaSuServer, dimBloccoLettura, k + 1);
                if (array.isValid(listaBlocco500Voci)) {
                    leggeTimestampBlocco(listaBlocco500Voci);
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        log.info("Algos - Ciclo UPDATE - letti i timestamp della categoria BioBot e registrati in mongoDB Bio (" + text.format(listaVociCategoriaSuServer.size()) + " elementi) in " + date.deltaText(inizio));
    }// end of method


    /**
     * Legge i timestamp del blocco di 500 voci
     * Legge e registra il blocco di 500
     */
    public void leggeTimestampBlocco(List<Long> listaBlocco500Voci) {
        ArrayList<WrapTime> lista;
        Bio entity;
        LocalDateTime time = null;

        lista = api.leggeTimestamp(listaBlocco500Voci);
        if (array.isValid(lista)) {
            for (WrapTime wrap : lista) {
                time = wrap.getDate();

                if (time != null) {
                    entity = bioService.findByKeyUnica(wrap.getPageid());
                    entity.lastModifica = time;
                    if (!entity.sporca) { //--se era sporca, rimane sporca
                        entity.sporca = time.isAfter(entity.lastLettura);
                    }// end of if cycle
                    entity.lastLettura = LocalDateTime.now();
                    bioService.save(entity);
                }// end of if cycle

            }// end of for cycle
        }// end of if cycle
    }// end of method


    /**
     * Scarica dal server wiki le SOLE voci 'sporche' (con ultimaModifica successiva a ultimaLettura)
     * Recupera dal mongoDB Bio le sole voci 'sporche'
     * Scarica le pagine a blocchi di 500 e le registra nel mongoDB Bio
     */
    public void leggeVociRecentementeModificate() {
        long inizio = System.currentTimeMillis();
        int numVociRegistrate = 0;
        ArrayList<Long> lista = bioService.findSporcaPageId();

        if (array.isValid(lista)) {
            numVociRegistrate = pageService.downloadUpdatePagine(lista);
            log.info("Algos - Ciclo UPDATE - download delle voci modificate successivamente all'ultima lettura (" + text.format(numVociRegistrate) + " elementi) in " + date.deltaText(inizio));
        } else {
            log.info("Algos - Ciclo UPDATE - nessuna voce modificata successivamente all'ultima lettura");
        }// end of if/else cycle

    }// end of method

}// end of class
