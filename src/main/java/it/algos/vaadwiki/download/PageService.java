package it.algos.vaadwiki.download;

import com.mongodb.client.result.*;
import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaadflow.application.FlowCost.*;
import static it.algos.vaadwiki.application.WikiCost.*;
import it.algos.vaadwiki.enumeration.*;
import it.algos.vaadwiki.modules.bio.*;
import it.algos.vaadwiki.service.*;
import it.algos.wiki.*;
import it.algos.wiki.web.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import java.time.*;
import java.util.*;

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


    /**
     * Scarica dal server la lista di pagine (title) indicate e crea le nuove entities sul mongoDB Bio
     * <p>
     * Esegue una serie di AQueryPages a blocchi di WIKI_PAGE_LIMIT (250) per volta <br>
     * Per ogni page crea la entity (Bio) corrispondente <br>
     * Esegue una cancellazione di tipo BULK nel mongoDB Bio <br>
     * Esegue un inserimento di tipo BULK nel mongoDB Bio <br>
     *
     * @param result wrapper di dati risultanti
     *
     * @return wrapper di dati risultanti
     */
    public DownloadResult downloadPagine(DownloadResult result) {
        ArrayList<Long> bloccoPage;
        int dimBloccoLettura = pref.getInt(WIKI_PAGE_LIMIT);
        int numCicliLetturaPagine;
        long inizio = System.currentTimeMillis();
        String message;
        int teorico;
        int effettivo;
        int totale;
        int delta;

        //        dimBloccoLettura = 50;//@todo patch provvisoria

        if (result.getVociDaCreare().size() > 0) {
            numCicliLetturaPagine = array.numCicli(result.getVociDaCreare().size(), dimBloccoLettura);
            for (int k = 0; k < numCicliLetturaPagine; k++) {
                bloccoPage = array.estraeSublista(result.getVociDaCreare(), dimBloccoLettura, k + 1);
                result = downloadSingoloBlocco(result, bloccoPage);
                if (pref.isBool(USA_DEBUG)) {
                    teorico = (k + 1) * dimBloccoLettura;
                    totale = result.getVociDaCreare().size();
                    teorico = Math.min(teorico, totale);
                    effettivo = result.getNumVociCreate();
                    delta = teorico - effettivo;
                    message = "New - aggiunte " + text.format(effettivo) + "/" + text.format(totale) + " (-" + delta + ") pagine totali a mongoDB.Bio in " + date.deltaText(inizio);
                    logger.info(message);
                    slf4jLogger.info(message);
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return result;
    }// end of method


    /**
     * Scarica dal server la lista di pagine (title) indicate e crea le nuove entities sul mongoDB Bio
     * <p>
     * Esegue una serie di AQueryPages a blocchi di WIKI_PAGE_LIMIT (250) per volta <br>
     * Per ogni page crea la entity (Bio) corrispondente <br>
     * Esegue una cancellazione di tipo BULK nel mongoDB Bio <br>
     * Esegue un inserimento di tipo BULK nel mongoDB Bio <br>
     *
     * @param result      wrapper di dati risultanti
     * @param arrayPageid lista (pageid) di pagine da scaricare dal server wiki
     *
     * @return wrapper di dati risultanti
     */
    public DownloadResult downloadSingoloBlocco(DownloadResult result, ArrayList<Long> arrayPageid) {
        return singoloBlocco(result, arrayPageid, EACicloType.download);
    }// end of method


    /**
     * Scarica dal server la lista di pagine (title) indicate e crea le nuove entities sul mongoDB Bio
     * <p>
     * Esegue una serie di AQueryPages a blocchi di WIKI_PAGE_LIMIT (250) per volta <br>
     * Per ogni page crea la entity (Bio) corrispondente <br>
     * Esegue una cancellazione di tipo BULK nel mongoDB Bio <br>
     * Esegue un inserimento di tipo BULK nel mongoDB Bio <br>
     *
     * @param result      wrapper di dati risultanti
     * @param arrayPageid lista (pageid) di pagine da scaricare dal server wiki
     *
     * @return wrapper di dati risultanti
     */
    public DownloadResult updateSingoloBlocco(DownloadResult result, ArrayList<Long> arrayPageid) {
        return singoloBlocco(result, arrayPageid, EACicloType.update);
    }// end of method


    /**
     * Scarica dal server la lista di pagine (title) indicate e crea le nuove entities sul mongoDB Bio
     * <p>
     * Esegue una serie di AQueryPages a blocchi di WIKI_PAGE_LIMIT (250) per volta <br>
     * Per ogni page crea la entity (Bio) corrispondente <br>
     * Esegue una cancellazione di tipo BULK nel mongoDB Bio <br>
     * Esegue un inserimento di tipo BULK nel mongoDB Bio <br>
     *
     * @param result      wrapper di dati risultanti
     * @param arrayPageid lista (pageid) di pagine da scaricare dal server wiki
     *
     * @return wrapper di dati risultanti
     */
    private DownloadResult singoloBlocco(DownloadResult result, ArrayList<Long> arrayPageid, EACicloType type) {
        DeleteResult deleteResult = null;
        ArrayList<Page> pages = null; // di norma 2505
        Bio entity = null;
        ArrayList<Long> vociDaCancellarePrimaDiReinserirle = new ArrayList<>();
        ArrayList<Bio> listaBio = new ArrayList<Bio>();
        boolean checkUpload = pref.isBool(USA_UPLOAD_SINGOLA_VOCE_ELABORATA);

        pages = ((AQueryPages) appContext.getBean("AQueryPages", arrayPageid)).pagesResponse();

        if (pages == null) {
            return result;
        }// end of if cycle

        //        if (true) {
        //            for (Page page : pages) {
        //                entity = creaBio(page, checkUpload);
        //                if (entity != null) {
        //                    if (bioService.save(entity) != null) {
        //                        logger.info("Registrata la entity " + entity.wikiTitle, PageService.class, "singoloBlocco");
        //                    } else {
        //                        logger.error("Non registrata la entity " + entity.wikiTitle, PageService.class, "singoloBlocco");
        //                    }// end of if/else cycle
        //                }// end of if cycle
        //            }// end of for cycle
        //
        //            return result;
        //        }// end of if cycle

        for (Page page : pages) {
            entity = creaBio(page, checkUpload);
            if (entity != null) {
                if (entity.pageid == page.getPageid()) {
                    listaBio.add(entity);
                    vociDaCancellarePrimaDiReinserirle.add(page.getPageid());
                    switch (type) {
                        case download:
                            result.addVoceCreata();
                            break;
                        case update:
                            result.addVoceAggiornata();
                            break;
                        default:
                            logger.warn("Switch - caso non definito");
                            break;
                    } // end of switch statement
                }
                else {
                    logger.error("I pageid sono differenti", PageService.class, "singoloBlocco");
                }// end of if/else cycle
            }
            else {
                result.addNo(page.getTitle());
            }// end of if/else cycle
        }// end of for cycle

        //--cancella le pagine esistenti prima di inserire le nuove versioni con update()
        if (type == EACicloType.update) {
            try { // prova ad eseguire il codice
                deleteResult = bioService.deleteBulkByPageid(vociDaCancellarePrimaDiReinserirle);
            } catch (Exception unErrore) { // intercetta l'errore
                logger.error(unErrore.toString());
            }// fine del blocco try-catch

            if (deleteResult != null && deleteResult.getDeletedCount() < vociDaCancellarePrimaDiReinserirle.size()) {
                logger.error(" ");
                logger.error("Non sono riuscito a cancellare: " + (vociDaCancellarePrimaDiReinserirle.size() - deleteResult.getDeletedCount()) + " voci prima di reinserirle per aggiornarle");
                logger.error(" ");
            }// end of if cycle
        }// end of if cycle

        if (listaBio.size() > 0) {
            try { // prova ad eseguire il codice
                mongo.insert(listaBio, Bio.class);
            } catch (Exception unErrore) { // intercetta l'errore
                slf4jLogger.info(String.format("Nel blocco di %s voci, ce n'è (almeno) una troppo lunga (index) quindi spazzolo 'singolarmente' il blocco", listaBio.size()));

                for (Bio bio : listaBio) {
                    if (bio != null) {
                        if (bioService.save(bio) != null) {
                            logger.info("Registrata la entity " + bio.wikiTitle, PageService.class, "singoloBlocco");
                            slf4jLogger.info(String.format("Registrata la entity %s", bio.wikiTitle));
                        }
                        else {
                            logger.error("Non registrata la entity " + bio.wikiTitle, PageService.class, "singoloBlocco");
                            slf4jLogger.info(String.format("Non registrata la entity %s", bio.wikiTitle));
                        }// end of if/else cycle
                    }// end of if cycle
                }// end of for cycle

                logger.error(unErrore, PageService.class, "singoloBlocco");
                //                log.error(" ");
                //                log.error(unErrore.toString());
                //                log.error("Numero pagine (bio) da registrare: " + listaBio.size());
                //                log.error(listaBio.toString());
                //                for (int k = 0; k < 10; k++) {
                //                    log.error(" ");
                //                    log.error(listaBio.get(k).wikiTitle + " " + listaBio.get(k).pageid);
                //                }// end of for cycle
                //                log.error(" ");

            }// fine del blocco try-catch
        }// end of if cycle

        return result;
    }// end of method


    /**
     * Crea una entity Bio partendo da una Page <br>
     * La entity NON viene salvata <br>
     *
     * @param page scaricata dal server wiki
     *
     * @return entity Bio
     */
    public Bio creaBio(Page page) {
        return creaBio(page, false);
    }// end of method


    public Bio creaBio(Page page, boolean checkUpload) {
        Bio entity = null;
        long pageid = 0;
        String wikiTitle = "";
        String template = "";
        LocalDateTime lastWikiModifica = null;

        if (page != null) {
            pageid = page.getPageid();
            wikiTitle = page.getTitle();
            template = api.estraeTmplBio(page);
            lastWikiModifica = page.getTimestamp().toLocalDateTime();
        }// end of if cycle

        // patch per il fuso orario del server wiki (Londra) col mio browser (Roma)
        if (lastWikiModifica != null) {
            lastWikiModifica = lastWikiModifica.plusHours(1);
        }// end of if cycle

        if (pageid > 0 && text.isValid(wikiTitle) && text.isValid(template)) {
            entity = bioService.newEntity(pageid, wikiTitle, template, lastWikiModifica);
            entity = elaboraService.esegueNoSave(entity);
        }
        else {
            if (text.isEmpty(template)) {
                logger.warn("Algos - Manca il template Bio alla voce " + wikiTitle);
            }// end of if cycle
        }// end of if/else cycle

        if (checkUpload) {
            uploadPagina(wikiTitle, template);
        }// end of if cycle

        return entity;
    }// end of method


    /**
     * Controlla se il tmpl arrivato dal server wiki è uguale a quello 'previsto' <br>
     * Se è diverso, lo modifica sul server <br>
     */
    public void uploadPagina(String wikiTitle, String templateOriginale) {
        String tmplOrdinato = elaboraService.ordinaNormaliNoLoss(templateOriginale);

        if (!tmplOrdinato.equals(templateOriginale)) {
            uploadService.uploadTmpl(wikiTitle, tmplOrdinato);

            logger.debug("Riordinata voce " + wikiTitle + " con un template non 'allineato'");
        }// end of if cycle

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

        entity = bioService.findByKeyUnica(wikiTitle);
        if (entity != null) {
            entity.wikiTitle = wikiTitle;
            entity.tmplBioServer = template;
            entity.lastLettura = LocalDateTime.now();
            entity = elaboraService.esegueNoSave(entity);
            bioService.save(entity);
        }
        else {
            logger.warn("Algos - Ciclo UPDATE - non esiste una voce che si dovrebbe modificare: " + wikiTitle);
        }// end of if/else cycle

    }// end of method

}// end of class
