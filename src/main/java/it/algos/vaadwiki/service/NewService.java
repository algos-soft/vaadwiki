package it.algos.vaadwiki.service;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadwiki.modules.categoria.Categoria;
import it.algos.wiki.DownloadResult;
import it.algos.wiki.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static it.algos.vaadwiki.application.WikiCost.LAST_DOWNLOAD_BIO;

/**
 * Project vaadbio2
 * Created by Algos
 * User: gac
 * Date: sab, 11-ago-2018
 * Time: 17:48
 * <p>
 * Esegue un ciclo (NEW) di controllo e creazione di nuovi records esistenti sul server e mancanti nel database
 * Scarica la lista di voci mancanti dal server e crea i nuovi records di Bio
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class NewService extends ABioService {


    /**
     * Scarica dal server tutte le voci indicate e crea i nuovi records di Bio
     * <p>
     * Controlla il flag USA_LIMITE_DOWNLOAD
     * Usa il numero massimo (MAX_DOWNLOAD) di voci da scaricare in totale (se USA_LIMITE_DOWNLOAD=true)
     * Esegue una serie di RequestWikiReadMultiPages a blocchi di PAGES_PER_REQUEST per volta
     * Per ogni page crea un record bio
     *
     * @param listaIdsMancanti elenco (ids=pageids) delle pagine mancanti da scaricare e registrare
     */
    public void esegue(ArrayList<Long> listaIdsMancanti) {
        long inizio = System.currentTimeMillis();
        DownloadResult result;

        if (array.isValid(listaIdsMancanti)) {
            result = pageService.downloadNewPagine(listaIdsMancanti);
            pref.saveValue(LAST_DOWNLOAD_BIO, LocalDateTime.now());

            if (result.getNumVociRegistrate() > 0) {
                log.info("Algos - Ciclo NEW - download di nuove voci e creazione in mongoDB Bio (" + text.format(result.getNumVociRegistrate()) + " voci) in " + date.deltaText(inizio));
            }// end of if cycle

            if (result.getNumVociNonRegistrate() > 0) {
                fixError(result.vociNonRegistrate);
            }// end of if cycle
        } else {
            log.info("Algos - Ciclo NEW - nessuna nuova voce da scaricare");
        }// end of if/else cycle

    }// end of method


    /**
     * Alcune voci non sono state registrate.
     * Probabilmente non è stata creata la entity Bio perché mancava il template nella pagina wiki
     * Primio controllo: se esiste la corrispondente entiy in Categoria, la elimino
     *
     * @param listaIdsNonRegistrate elenco (ids=pageids) delle voci non registrate
     */
    public void fixError(ArrayList<Long> listaIdsNonRegistrate) {
        for (Long pageid : listaIdsNonRegistrate) {
            checkCategoria(pageid);
        }// end of for cycle
    }// end of method


    /**
     * Controlla una singola entity di Categoria.
     * <p>
     * Controlla che esista la pagina su wiki. Se non esiste, cancella la entity Categoria
     * Controlla che la pagina contenga il template Bio. Se non lo contiene, cancella la entity Categoria
     *
     * @param pageid di una entity di categoria da controllare
     */
    public void checkCategoria(Long pageid) {
        Categoria categoria;
        Page page;
        String title;

        if (!api.isVoceBio(pageid)) {
            categoria = categoriaService.findByKeyUnica(pageid);
            if (categoria != null) {
                categoriaService.delete(categoria);
                page = api.leggePage(pageid);
                title = page.getTitle();
                log.info("Algos - Ciclo NEW - cancellata da mongoDB.Categoria la entity '" + title + "', perché la pagina sul server non contiene il tmpl Bio");
            } else {
                log.warn("Algos - Ciclo NEW - con find() non trovo la entity '" + pageid + "' di mongoDB.Categoria che risulta invece nella lista");
            }// end of if/else cycle
        }// end of if cycle
    }// end of method

}// end of class
