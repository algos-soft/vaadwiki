package it.algos.vaadwiki.service;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.wiki.Page;
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
 * Time: 17:54
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class PageService extends ABioService {


    public final static int BLOCCO_PAGES = 500;


    /**
     * Scarica una lista di nuove pagine (long) dal server wiki e le memorizza nel mongoDB <br>
     * <p>
     * Esegue una serie di RequestWikiReadPages a blocchi di BLOCCO_PAGES (500) per volta <br>
     * Per ogni page crea la entity (Bio) corrispondente <br>
     * Esegue un inserimento di tipo BULK nel mongoDB Bio <br>
     *
     * @param listaVociDaScaricare elenco (pageids) delle pagine nuove, da scaricare
     */
    public int downloadNewPagine(List<Long> listaVociDaScaricare) {
        return downloadPagine(listaVociDaScaricare, true);
    }// end of method


    /**
     * Scarica una lista di pagine modificate (long) dal server wiki e memorizza le modifiche nel mongoDB <br>
     * <p>
     * Esegue una serie di RequestWikiReadPages a blocchi di BLOCCO_PAGES (500) per volta <br>
     * Per ogni page modifica la entity (Bio) corrispondente <br>
     *
     * @param listaVociDaScaricare elenco (pageids) delle pagine modificate, da scaricare
     */
    public int downloadUpdatePagine(ArrayList<Long> listaVociDaScaricare) {
        return downloadPagine(listaVociDaScaricare, false);
    }// end of method


    /**
     * Scarica una lista di pagine (long) dal server wiki e le memorizza nel mongoDB <br>
     * <p>
     * Esegue una serie di RequestWikiReadPages a blocchi di BLOCCO_PAGES (500) per volta <br>
     * Per ogni page crea o modifica il records corrispondente con lo stesso pageid <br>
     *
     * @param listaVociDaScaricare elenco (pageids) delle pagine mancanti o modificate, da scaricare
     * @param bulk                 flag per inserimento 'massiccio' delle nuove pagine
     */
    private int downloadPagine(List<Long> listaVociDaScaricare, boolean bulk) {
        long inizio = System.currentTimeMillis();
        int numVociRegistrate = 0;
        List<Long> bloccoPageids;
        int dimBloccoLettura = BLOCCO_PAGES;
        int numCicliLetturaPagine;

        if (listaVociDaScaricare != null && listaVociDaScaricare.size() > 0) {
            numCicliLetturaPagine = array.numCicli(listaVociDaScaricare.size(), dimBloccoLettura);
            for (int k = 0; k < numCicliLetturaPagine; k++) {
                bloccoPageids = array.estraeSublistaLong(listaVociDaScaricare, dimBloccoLettura, k + 1);
                numVociRegistrate += downloadSingoloBlocco(bloccoPageids, bulk);
            }// end of for cycle
        }// end of if cycle

        return numVociRegistrate;
    }// end of method


    /**
     * @param bloccoPageids lista (pageids) di pagine da scaricare dal server wiki
     * @param bulk          flag per inserimento 'massiccio' delle nuove pagine
     */
    private int downloadSingoloBlocco(List<Long> bloccoPageids, boolean bulk) {
        int numVociRegistrate = 0;
        ArrayList<Page> pages; // di norma 500

        if (bloccoPageids == null || bloccoPageids.size() > 500) {
            return 0;
        }// end of if cycle

        pages = api.leggePages(bloccoPageids);
        if (bulk) {
            numVociRegistrate = registraBulk(pages);
        } else {
            numVociRegistrate = registraSingoloBlocco(pages);
        }// end of if/else cycle

        return numVociRegistrate;
    }// end of method


    private int registraBulk(ArrayList<Page> pages) {
        int numVociRegistrate = 0;
        Bio entity;
        ArrayList<Bio> listaBio = new ArrayList<Bio>();

        for (Page page : pages) {
            entity = creaBio(page);
            listaBio.add(entity);
        }// end of for cycle

        if (listaBio.size() > 0) {
            try { // prova ad eseguire il codice
                mongo.insert(listaBio, "bio");
                numVociRegistrate = listaBio.size();
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
            }// fine del blocco try-catch
        }// end of if cycle

        return numVociRegistrate;
    }// end of method


    public Bio creaBio(Page page) {
        Bio entity = null;
        long pageid = 0;
        String wikiTitle = "";
        String template = "";

        if (page != null) {
            pageid = page.getPageid();
            wikiTitle = page.getTitle();
            template = api.estraeTmplBio(page);
        }// end of if cycle

        if (pageid > 0 && text.isValid(wikiTitle) && text.isValid(template)) {
            entity = bioService.newEntity(pageid, wikiTitle, template, LocalDateTime.now());
            elaboraService.esegueNoSave(entity);
        } else {
            if (text.isEmpty(template)) {
                log.warn("Manca il template alla voce " + wikiTitle);
            }// end of if cycle
        }// end of if/else cycle

        return entity;
    }// end of method


    private int registraSingoloBlocco(ArrayList<Page> pages) {
        for (Page page : pages) {
            registraPagina(page);

//            pageid = page.getPageid();
//            wikiTitle = page.getTitle();
//            template = api.estraeTmplBio(page);
//
//            entity = bioService.findByKeyUnica(pageid);
//            if (entity != null) {
//                entity.setWikiTitle(wikiTitle);
//                entity.setTmplBioServer(template);
//                entity.setLastLettura(LocalDateTime.now());
//                entity.setSporca(false);
//                elaboraService.esegueNoSave(entity);
//                bioService.save(entity);
//            } else {
//                log.warn("Algos - Ciclo UPDATE - non esiste una voce che si dovrebbe modificare: " + wikiTitle);
//            }// end of if/else cycle

        }// end of for cycle

        return pages.size();
    }// end of method


    public void registraPagina(long pageid) {
        if (pageid > 0) {
            registraPagina(api.leggePage(pageid));
        }// end of if cycle
    }// end of method


    public void registraPagina(String wikiTitle) {
        if (text.isValid(wikiTitle)) {
            registraPagina(api.leggePage(wikiTitle));
        }// end of if cycle
    }// end of method


    public void registraPagina(Page page) {
        long pageid = 0;
        String wikiTitle = "";
        String template = "";
        Bio entity;

        pageid = page.getPageid();
        wikiTitle = page.getTitle();
        template = api.estraeTmplBio(page);

        entity = bioService.findByKeyUnica(pageid);
        if (entity != null) {
            entity.setWikiTitle(wikiTitle);
            entity.setTmplBioServer(template);
            entity.setLastLettura(LocalDateTime.now());
            entity.setSporca(false);
            elaboraService.esegueNoSave(entity);
            bioService.save(entity);
        } else {
            log.warn("Algos - Ciclo UPDATE - non esiste una voce che si dovrebbe modificare: " + wikiTitle);
        }// end of if/else cycle

    }// end of method


//    /**
//     * @param bloccoPageids lista (pageids) di pagine da scaricare dal server wiki
//     */
//    private void doInitSenzaCommit(ArrayList<Long> bloccoPageids) {
//        long inizio = 0;
//        long fine = 0;
//        WrapBio wrap;
//
//        if (bloccoPageids == null) {
//            return;
//        }// end of if cycle
//
//        inizio = System.currentTimeMillis();
//        pages = Api.leggePages(bloccoPageids);
////        Log.setDebug("test", "lettura di " + pages.size() + " pages in " + LibTime.difText(inizio));
//
//        if (pages != null && pages.size() > 0) {
//            wraps = new ArrayList<WrapBio>();
//            inizio = System.currentTimeMillis();
//
//            for (Page page : pages) {
//                wrap = new WrapBio(page, null);
//                if (wrap.isRegistrata()) {
//                    numVociRegistrate++;
//                }// end of if cycle
//                wraps.add(wrap);
//            }// end of for cycle
//
//            fine = System.currentTimeMillis();
//            Log.debug("test", "save singolarmente senza commit " + LibNum.format(numVociRegistrate) + " nuove voci in " + LibNum.format(fine - inizio));
//        }// end of if cycle
//    }// end of method

}// end of class
