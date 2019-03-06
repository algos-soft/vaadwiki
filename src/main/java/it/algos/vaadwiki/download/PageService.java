package it.algos.vaadwiki.download;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadwiki.enumeration.EACicloType;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.service.ABioService;
import it.algos.wiki.DownloadResult;
import it.algos.wiki.Page;
import it.algos.wiki.web.AQueryPages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static it.algos.vaadwiki.application.WikiCost.WIKI_PAGE_LIMIT;

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
        ArrayList<String> bloccoPage;
        int dimBloccoLettura = pref.getInt(WIKI_PAGE_LIMIT);
        int numCicliLetturaPagine;
        long inizio = System.currentTimeMillis();
        String message;
        int teorico;
        int effettivo;
        int delta;

        if (result.getVociDaCreare().size() > 0) {
            numCicliLetturaPagine = array.numCicli(result.getVociDaCreare().size(), dimBloccoLettura);
            for (int k = 0; k < numCicliLetturaPagine; k++) {
                bloccoPage = array.estraeSublista(result.getVociDaCreare(), dimBloccoLettura, k + 1);
                result = downloadSingoloBlocco(result, bloccoPage);
                if (pref.isBool(FlowCost.USA_DEBUG)) {
                    teorico = (k + 1) * dimBloccoLettura;
                    teorico = Math.min(teorico, result.getVociDaCreare().size());
                    effettivo = result.getNumVociCreate();
                    delta = teorico - effettivo;
                    message = "New - aggiunte " + text.format(effettivo) + "/" + text.format(teorico) + " (-" + delta + ") voci totali a mongoDB.Bio in " + date.deltaText(inizio);
                    log.info(message);
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
     * @param arrayTitles lista (titles) di pagine da scaricare dal server wiki
     *
     * @return wrapper di dati risultanti
     */
    public DownloadResult downloadSingoloBlocco(DownloadResult result, ArrayList<String> arrayTitles) {
        return singoloBlocco(result, arrayTitles, EACicloType.download);
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
     * @param arrayTitles lista (titles) di pagine da scaricare dal server wiki
     *
     * @return wrapper di dati risultanti
     */
    public DownloadResult updateSingoloBlocco(DownloadResult result, ArrayList<String> arrayTitles) {
        return singoloBlocco(result, arrayTitles, EACicloType.update);
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
     * @param arrayTitles lista (titles) di pagine da scaricare dal server wiki
     *
     * @return wrapper di dati risultanti
     */
    private DownloadResult singoloBlocco(DownloadResult result, ArrayList<String> arrayTitles, EACicloType type) {
        ArrayList<Page> pages = null; // di norma 500
        Bio entity;
        ArrayList<Long> vociDaRegistrareInQuestoBlocco = new ArrayList<>();
        ArrayList<Bio> listaBio = new ArrayList<Bio>();

        pages = ((AQueryPages) appContext.getBean("AQueryPages", arrayTitles)).pagesResponse();

        if (pages == null) {
            return result;
        }// end of if cycle

        for (Page page : pages) {
            entity = creaBio(page);
            if (entity != null) {
                listaBio.add(entity);
                vociDaRegistrareInQuestoBlocco.add(page.getPageid());
                switch (type) {
                    case download:
                        result.addVoceCreata();
                        break;
                    case update:
                        result.addVoceAggiornata();
                        break;
                    default:
                        log.warn("Switch - caso non definito");
                        break;
                } // end of switch statement
            } else {
                result.addNo(page.getTitle());
            }// end of if/else cycle
        }// end of for cycle

        //--cancella le voci esistenti prima di inserire le nuove versioni con update()
        try { // prova ad eseguire il codice
            bioService.deleteBulkByPageid(vociDaRegistrareInQuestoBlocco);
        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch

        if (listaBio.size() > 0) {
            try { // prova ad eseguire il codice
                mongo.insert(listaBio, Bio.class);
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(" ");
                log.error(unErrore.toString());
                log.error("Numero voci (bio) da registrare: " + listaBio.size());
                log.error(listaBio.toString());
                for (int k = 0; k < 10; k++) {
                    log.error(" ");
                    log.error(listaBio.get(k).wikiTitle + " " + listaBio.get(k).pageid);
                }// end of for cycle
                log.error(" ");

            }// fine del blocco try-catch
        }// end of if cycle

        return result;
    }// end of method


    public Bio creaBio(Page page) {
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

        entity = bioService.findByKeyUnica(wikiTitle);
        if (entity != null) {
            entity.setWikiTitle(wikiTitle);
            entity.setTmplBioServer(template);
            entity.setLastLettura(LocalDateTime.now());
            entity = elaboraService.esegueNoSave(entity);
            bioService.save(entity);
        } else {
            log.warn("Algos - Ciclo UPDATE - non esiste una voce che si dovrebbe modificare: " + wikiTitle);
        }// end of if/else cycle

    }// end of method

}// end of class
