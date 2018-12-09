package it.algos.vaadwiki.service;

import com.mongodb.client.result.DeleteResult;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.wiki.DownloadResult;
import it.algos.wiki.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.time.LocalDateTime;
import java.util.ArrayList;

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


    public final static int BLOCCO_PAGES = 200;


    /**
     * Scarica una lista di nuove pagine (long) dal server wiki e le memorizza nel mongoDB <br>
     * <p>
     * Esegue una serie di RequestWikiReadPages a blocchi di BLOCCO_PAGES (500) per volta <br>
     * Per ogni page crea la entity (Bio) corrispondente <br>
     * Esegue un inserimento di tipo BULK nel mongoDB Bio <br>
     *
     * @param listaVociDaScaricare elenco (pageids) delle pagine nuove, da scaricare
     */
    public DownloadResult downloadNewPagine(ArrayList<Long> listaVociDaScaricare) {
        return downloadPagine(listaVociDaScaricare);
    }// end of method


    /**
     * Scarica una lista di pagine modificate (long) dal server wiki e memorizza le modifiche nel mongoDB <br>
     * <p>
     * Esegue una serie di RequestWikiReadPages a blocchi di BLOCCO_PAGES (500) per volta <br>
     * Per ogni page modifica la entity (Bio) corrispondente <br>
     *
     * @param listaVociDaScaricare elenco (pageids) delle pagine modificate, da scaricare
     */
    public DownloadResult downloadUpdatePagine(ArrayList<Long> listaVociDaScaricare) {
        return downloadPagine(listaVociDaScaricare);
    }// end of method


    /**
     * Scarica una lista di pagine (long) dal server wiki e le memorizza nel mongoDB <br>
     * <p>
     * Esegue una serie di RequestWikiReadPages a blocchi di BLOCCO_PAGES (500) per volta <br>
     * Per ogni page crea o modifica il records corrispondente con lo stesso pageid <br>
     *
     * @param listaVociDaScaricare elenco (pageids) delle pagine mancanti o modificate, da scaricare
     */
    private DownloadResult downloadPagine(ArrayList<Long> listaVociDaScaricare) {
        DownloadResult result = new DownloadResult(listaVociDaScaricare);
        long inizioCiclo = System.currentTimeMillis();
        int numVociRegistrate = 0;
        ArrayList<Long> bloccoPageids;
        int dimBloccoLettura = BLOCCO_PAGES;
        int numCicliLetturaPagine;

        if (listaVociDaScaricare != null && listaVociDaScaricare.size() > 0) {
            numCicliLetturaPagine = array.numCicli(listaVociDaScaricare.size(), dimBloccoLettura);
            for (int k = 0; k < numCicliLetturaPagine; k++) {
                bloccoPageids = array.estraeSublistaLong(listaVociDaScaricare, dimBloccoLettura, k + 1);
                result = downloadSingoloBlocco(result, bloccoPageids);
            }// end of for cycle
        }// end of if cycle

        return result;
    }// end of method


    /**
     * @param bloccoPageids lista (pageids) di pagine da scaricare dal server wiki
     */
    private DownloadResult downloadSingoloBlocco(DownloadResult result, ArrayList<Long> bloccoPageids) {
        ArrayList<Page> pages; // di norma 500
        Bio entity;
        ArrayList<Long> vociRegistrateInQuestoBlocco = new ArrayList<>();
        ArrayList<Bio> listaBio = new ArrayList<Bio>();

        if (bloccoPageids == null || bloccoPageids.size() > BLOCCO_PAGES) {
            return result;
        }// end of if cycle

        pages = api.leggePages(bloccoPageids);
        for (Page page : pages) {
            entity = creaBio(page);
            if (entity != null) {
                listaBio.add(entity);
                vociRegistrateInQuestoBlocco.add(page.getPageid());
                result.addSi(page.getPageid());
            } else {
                result.addNo(page.getPageid());
            }// end of if/else cycle
        }// end of for cycle

        //--cancella le voci esistenti prima di inserire le nuove versioni con update()
        bioService.deleteBulkByPageid(vociRegistrateInQuestoBlocco);

        if (listaBio.size() > 0) {
            try { // prova ad eseguire il codice
                mongo.updateBulk(listaBio, Bio.class);
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
            }// fine del blocco try-catch
        }// end of if cycle

        return result;
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
                log.warn("Algos - Manca il template Bio alla voce " + wikiTitle);
            }// end of if cycle
        }// end of if/else cycle

        return entity;
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
