package it.algos.vaadwiki.download;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.enumeration.EALogLivello;
import it.algos.vaadflow.enumeration.EALogType;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.service.ABioService;
import it.algos.vaadwiki.service.LibBio;
import it.algos.vaadwiki.service.ParBio;
import it.algos.wiki.Page;
import it.algos.wiki.web.AQueryWrite;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.*;

/**
 * Project vaadbio2
 * Created by Algos
 * User: gac
 * Date: sab, 11-ago-2018
 * Time: 15:44
 * <p>
 * Elaborazione:
 * da mongoDB elaborazione EAElabora.ordinaNormaliNoLoss
 * - parte dal tmpl e lo riordina (aggiunge parametri normali mancanti ed elimina quelli vuoti)
 * - SENZA modificare i valori o i parametri presenti nel mongoDB
 * da mongoDB elaborazione EAElabora.parametriRipuliti
 * da mongoDB elaborazione EAElabora.parametriModificati
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class ElaboraService extends ABioService {

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    public LibBio libBio;


    /**
     * Elabora le pagine biografiche <br>
     * Parte dal tmplBioServer e costruisce tutti parametri significativi <br>
     * Ogni parametro viene 'pulito' se presentato in maniera 'impropria' <br>
     * Quello che resta è affidabile ed utilizzabile per le liste <br>
     */
    public void esegue() {
        esegueAll();
    }// end of method


    /**
     * Elabora una pagina biografica <br>
     * Scarica dal server l'ultima versione della voce <br>
     * Parte dal mongoDB e costruisce i relativi parametri <br>
     * Ogni parametro viene 'pulito' se presentato in maniera 'impropria' <br>
     * Quello che resta è affidabile ed utilizzabile per le liste <br>
     */
    public void reverse() {
    }// end of method


    /**
     * Elabora le pagine biografiche <br>
     * Parte dal tmplBioServer e costruisce tutti parametri significativi <br>
     * Ogni parametro viene 'pulito' se presentato in maniera 'impropria' <br>
     * Quello che resta è affidabile ed utilizzabile per le liste <br>
     */
    public void esegueAll() {
        long inizio = System.currentTimeMillis();
        int size = 1000;
        int totale = bioService.count();
        Sort sort = new Sort(Sort.Direction.ASC, "pageid");
        List<Bio> lista = null;
        int tot = 0;

        for (int k = 0; k < array.numCicli(totale, size); k++) {
            lista = mongo.mongoOp.find(new Query().with(PageRequest.of(k, size, sort)), Bio.class);
            for (Bio bio : lista) {
                esegueSave(bio);
                tot++;
            }// end of for cycle
            log.info("ELABORA - Elaborate " + text.format(tot) + " voci biografiche su " + text.format(totale) + " in " + date.deltaText(inizio));
        }// end of for cycle
    }// end of method


    /**
     * Controlla se il tmpl del mongoDB di tutte le istanze è uguale a quello 'previsto' <br>
     */
    public void checkAll() {
        long inizio = System.currentTimeMillis();
        int size = 1000;
        int totale = bioService.count();
        Sort sort = new Sort(Sort.Direction.ASC, "pageid");
        List<Bio> lista = null;
        int tot = 0;

        for (int k = 0; k < array.numCicli(totale, size); k++) {
            lista = mongo.mongoOp.find(new Query().with(PageRequest.of(k, size, sort)), Bio.class);
            for (Bio bio : lista) {
                if (check(bio)) {
                    tot++;
                }// end of if cycle
            }// end of for cycle
        }// end of for cycle

        logger.sendTerminale(EALogLivello.debug, "Check EAElabora.ordinaNormaliNoLoss. Su " + text.format(totale) + " voci ce ne sono " + text.format(tot) + " col template diverso da quello standard", ElaboraService.class, "checkAll");
        logger.crea(EALogType.elabora, "Su " + text.format(totale) + " voci ci sono " + text.format(tot) + " template diversi. Controllati", inizio);
    }// end of method


    /**
     * Controlla se il tmpl del mongoDB di tutte le istanze è uguale a quello 'previsto' <br>
     * Se è diverso, lo modifica sul server <br>
     */
    public void uploadAllNormaliNoLoss() {
        long inizio = System.currentTimeMillis();
        int size = 1000;
        int totale = bioService.count();
        Sort sort = new Sort(Sort.Direction.ASC, "pageid");
        List<Bio> lista = null;
        int tot = 0;

        logger.sendTerminale(EALogLivello.debug, "Iniziato EAElabora.ordinaNormaliNoLoss con eventuale upload di " + text.format(totale) + " voci.", ElaboraService.class, "uploadAllNormaliNoLoss");
        for (int k = 0; k < array.numCicli(totale, size); k++) {
            lista = mongo.mongoOp.find(new Query().with(PageRequest.of(k, size, sort)), Bio.class);
            for (Bio bio : lista) {
                if (check(bio)) {
                    tot++;
                    uploadNormaliNoLoss(bio);
                }// end of if cycle
            }// end of for cycle
        }// end of for cycle
        logger.sendTerminale(EALogLivello.debug, "Su " + text.format(totale) + " voci ci sono " + text.format(tot) + " template diversi. Uploadati sul server", ElaboraService.class, "uploadAllNormaliNoLoss");
        logger.crea(EALogType.upload, "Su " + text.format(totale) + " voci ci sono " + text.format(tot) + " template diversi. Uploadati sul server", inizio);
    }// end of method


    /**
     * Controlla se il tmpl del mongoDB è uguale a quello 'previsto' <br>
     */
    public boolean check(Bio bio) {
        return check(bio.getTmplBioServer());
    }// end of method


    /**
     * Controlla se il tmpl del mongoDB è uguale a quello 'previsto' <br>
     */
    public boolean check(String templateOriginale) {
        boolean diverso = false;
        String tmplOrdinato = elaboraService.ordinaNormaliNoLoss(templateOriginale);

        if (!tmplOrdinato.equals(templateOriginale)) {
            diverso = true;
        }// end of if cycle

        return diverso;
    }// end of method


    /**
     * EAElabora.ordinaNormaliNoLoss
     * <p>
     * Parte da una entity Bio esistente sul mongoDB <br>
     * Il tmpl del mongoDB è diverso da quello 'previsto' <br>
     * Riordina il template SENZA nessuna modifica dei valori preesistenti <br>
     * Riordina i parametri <br>
     * Aggiunge quelli 'normali' mancanti vuoti (sono 11) <br>
     * Elimina quelli esistenti vuoti, senza valore <br>
     * Registra le modifiche sul server wiki <br>
     */
    public void uploadNormaliNoLoss(Bio bioEsistente) {
        String wikiTitle = bioEsistente.getWikiTitle();
        String summary = "fixTmpl";
        Page page;
        Bio bio;
        String oldTmpl;
        String newTmpl;
        String oldTesto;
        String newTesto;

        page = api.leggePage(wikiTitle);

        if (page != null && page.isBioValida()) {
            oldTesto = page.getText();
            bio = pageService.creaBio(page);
            oldTmpl = bio.getTmplBioServer();
            if (ordinaNormaliNoLoss(bio)) {
                newTmpl = bio.getTmplBioServer();
                bioService.save(bio);
                newTesto = text.sostituisce(oldTesto, oldTmpl, newTmpl);
                appContext.getBean(AQueryWrite.class, wikiTitle, newTesto, summary);
            }// end of if cycle
        }// end of if cycle
    }// end of method


    /**
     * Elabora le pagine biografiche <br>
     * Parte dal tmplBioServer e costruisce tutti parametri significativi <br>
     * Ogni parametro viene 'pulito' se presentato in maniera 'impropria' <br>
     * Quello che resta è affidabile ed utilizzabile per le liste <br>
     */
    public void esegueAllOld() {
        ArrayList<String> lista = bioService.findAllTitles();

        if (array.isValid(lista)) {
            esegue(lista);
        }// end of if cycle

    }// end of method


    /**
     * Elabora le pagine biografiche indicate <br>
     * Parte dal tmplBioServer e costruisce tutti parametri significativi <br>
     * Ogni parametro viene 'pulito' se presentato in maniera 'impropria' <br>
     * Quello che resta è affidabile ed utilizzabile per le liste <br>
     */
    public void esegue(ArrayList<String> lista) {
        long inizio = System.currentTimeMillis();
        int parz = 0;
        int tot = 0;

        if (array.isValid(lista)) {
            if (pref.isBool(USA_DEBUG)) {
                log.info("ELABORA - Inizio dell'elaborazione dei parametri di tutte le " + text.format(lista.size()) + " biografie");
            }// end of if cycle

            for (String wikiTitle : lista) {
                esegue(wikiTitle);

                parz++;
                tot++;
                if (parz == 10000) {
                    log.info("ELABORA - Elaborate " + text.format(tot) + " voci biografiche su " + text.format(lista.size()) + " in " + date.deltaText(inizio));
                    parz = 0;
                }// end of if cycle
            }// end of for cycle

            log.debug("ELABORA - elaborati i parametri delle nuove pagine (" + text.format(lista.size()) + " elementi) in " + date.deltaText(inizio));
            if (pref.isBool(USA_DEBUG)) {
                log.info("ELABORA - finito in " + date.deltaText(inizio));
            }// end of if cycle
        }// end of if cycle

    }// end of method


    /**
     * Elabora la singola voce biografica<br>
     * Parte dal tmplBioServer e costruisce tutti parametri significativi <br>
     * Ogni parametro viene 'pulito' se presentato in maniera 'impropria' <br>
     * Quello che resta è affidabile ed utilizzabile per le liste <br>
     *
     * @param wikiTitle della pagina wiki (obbligatorio, unico)
     */
    public void esegue(String wikiTitle) {
        Bio entity = bioService.findByKeyUnica(wikiTitle);

        if (entity != null) {
            esegueSave(entity);
        }// end of if cycle
    }// end of method


    /**
     * Elabora la singola voce biografica<br>
     * Estrae dal tmplBioServer i singoli parametri previsti nella enumeration ParBio <br>
     * Ogni parametro viene 'pulito' se presentato in maniera 'impropria' <br>
     * Quello che resta è affidabile ed utilizzabile per le liste <br>
     */
    public Bio esegue(Bio bio, boolean registra) {
        HashMap<String, String> mappa;

        //--Recupera i valori base di tutti i parametri dal tmplBioServer
        mappa = getMappaBio(bio);

//        //--Elabora valori validi dei parametri significativi
//        if (mappa != null) {
//            elaboraValidi(mappa);
//        }// end of if cycle

        //--Elabora valori validi dei parametri significativi
        //--Inserisce i valori nella entity Bio
        if (mappa != null) {
            setValue(bio, mappa, registra);
        }// end of if cycle

        //--Elabora i link alle tavole collegate
//        new ElaboraLink(bio);

        //--Elabora tutte le didascalie della voce
//        new ElaboraDidascalie(bio);
        int a = 87;

        return bio;
    }// end of method


    /**
     * Elabora la singola voce biografica<br>
     * Estrae dal tmplBioServer i singoli parametri previsti nella enumeration ParBio <br>
     * Ogni parametro viene 'pulito' se presentato in maniera 'impropria' <br>
     * Quello che resta è affidabile ed utilizzabile per le liste <br>
     */
    public Bio esegueNoSave(Bio bio) {
        return esegue(bio, false);
    }// end of method


    /**
     * Elabora la singola voce biografica<br>
     * Estrae dal tmplBioServer i singoli parametri previsti nella enumeration ParBio <br>
     * Ogni parametro viene 'pulito' se presentato in maniera 'impropria' <br>
     * Quello che resta è affidabile ed utilizzabile per le liste <br>
     *
     * @param wikiTitle della pagina wiki (obbligatorio, unico)
     */
    public Bio esegueSave(String wikiTitle) {
        return esegue(bioService.findByKeyUnica(wikiTitle), true);
    }// end of method


    /**
     * Elabora la singola voce biografica<br>
     * Estrae dal tmplBioServer i singoli parametri previsti nella enumeration ParBio <br>
     * Ogni parametro viene 'pulito' se presentato in maniera 'impropria' <br>
     * Quello che resta è affidabile ed utilizzabile per le liste <br>
     */
    public void esegueSave(Bio bio) {
        esegue(bio, true);
    }// end of method


    /**
     * Estrae dal templateServer una mappa di parametri corrispondenti ai campi della tavola Bio
     * Crea un templateStandard con i parametri
     */
    private void doInit() {

    }// end of method


    /**
     * Estrae dal templateServer una mappa di parametri corrispondenti ai campi della tavola Bio
     */
    public HashMap<String, String> getMappaBio(Bio bio) {
        HashMap<String, String> mappa = null;
        String tmplBioServer = bio.getTmplBioServer();

        if (text.isValid(tmplBioServer)) {
            mappa = new HashMap<>();
            mappa = libBio.getMappaBio(tmplBioServer);
        }// end of if cycle

        return mappa;
    }// end of method


    //--Elabora valori validi dei parametri significativi
    private void elaboraValidi(HashMap<String, String> mappa) {
    }// end of method


    //--Inserisce i valori nella entity Bio
    public void setValue(Bio bio, HashMap<String, String> mappa, boolean registra) {
        String value = null;

        try { // prova ad eseguire il codice
            if (bio != null) {

//                // patch per i luoghi di nascita e morte
//                // se è pieno il parametro link, lo usa
//                if (text.isValid(mappa.get(ParBio.luogoNascitaLink.getTag()))) {
//                    mappa.put(ParBio.luogoNascita.getTag(), mappa.get(ParBio.luogoNascitaLink.getTag()));
//                }// end of if cycle
//                if (text.isValid(mappa.get(ParBio.luogoMorteLink.getTag()))) {
//                    mappa.put(ParBio.luogoMorte.getTag(), mappa.get(ParBio.luogoMorteLink.getTag()));
//                }// end of if cycle

                for (ParBio par : ParBio.values()) {
                    value = mappa.get(par.getTag());
                    if (value != null) {
                        par.setValue(bio, value, libBio);
                    }// end of if cycle
                } // fine del ciclo for-each
            }// fine del blocco if

            if (registra) {
                bioService.save(bio);
            }// end of if cycle

        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch

    }// end of method


    /**
     * Dalla pagina wiki recupera una voce e registra la entity sul mongoDB <br>
     */
    public void downloadSave(String wikiTitle) {
        Bio bio = creaBioMemory(wikiTitle);
        String tmpl = getTmplBioServer(bio);
        bioService.save(bio);
    }// end of method


    /**
     * Dal server wiki al bio (non registrato) <br>
     */
    public Bio creaBioMemory(String wikiTitle) {
        return api.leggeBio(wikiTitle);
    }// end of method


    /**
     * Dal bio il tmplBioServer <br>
     */
    public String getTmplBioServer(Bio bio) {
        return bio.getTmplBioServer();
    }// end of method


    /**
     * Dalla entity mongoDB al tmplBioMongo <br>
     * Costruisce un template con SOLO i parametri gestiti <br>
     */
    public String getTmplBioMongo(Bio bio) {
        String tmplBioMongo = VUOTA;
        String value = VUOTA;
        String iniTemplate = "{{Bio";
        String endTemplate = "}}";

        if (bio != null) {
            tmplBioMongo = iniTemplate;
            tmplBioMongo += A_CAPO;

            for (ParBio par : ParBio.values()) {
                value = par.getValue(bio);
                if (text.isValid(value) || par.isCampoNormale()) {
                    tmplBioMongo += par.getRiga(bio);
                }// end of if cycle
            }// end of for cycle

            tmplBioMongo += endTemplate;
        }// end of if cycle

        return tmplBioMongo;
    }// end of method


    /**
     * Merge dei template <br>
     * Costruisce un template con i parametri di tmplBioMongo PIU quelli di tmplBioServer <br>
     */
    public String getTmplMerged(String tmplBioMongo, String tmplBioServer) {
        String tmplMerged = VUOTA;
        HashMap<String, String> mappaServer = null;
        HashMap<String, String> mappaMongo = null;
        String iniTemplate = "{{Bio";
        String endTemplate = "}}";
        String tag = "|";

        if (text.isValid(tmplBioMongo) && text.isValid(tmplBioServer)) {
            mappaMongo = libBio.getMappaBio(tmplBioMongo);
            mappaServer = libBio.getMappaBio(tmplBioServer);

            tmplMerged = iniTemplate;
            tmplMerged += A_CAPO;

            for (ParBio par : ParBio.values()) {
                if (mappaMongo.get(par.getTag()) != null) {
                    tmplMerged += par.getRiga(mappaMongo.get(par.getTag()));
                } else {
                    if (mappaServer.get(par.getTag()) != null && text.isValid(mappaServer.get(par.getTag()))) {
                        tmplMerged += par.getRiga(mappaServer.get(par.getTag()));
                    } else {
                        if (par.isCampoNormale()) {
                        } else {
                        }// end of if/else cycle
                    }// end of if/else cycle
                }// end of if/else cycle
            }// end of for cycle
            tmplMerged += endTemplate;
        }// end of if cycle

        return tmplMerged;
    }// end of method


    /**
     * Elabora una voce biografica del server wiki <br>
     * Dalla pagina wiki recupera una voce e registra la entity sul mongoDB <br>
     * Costruisce un template dalla entity sul mongoDB <br>
     * Elabora la voce, modificando eventualmente alcuni parametri ed ordinandoli tutti <br>
     * Merge il tmplBioServer scaricato ed il tmplBioServer elaborato <br>
     */
    public String getTmplNuovo(String wikiTitle) {
        String tmplNuovo = VUOTA;

        return tmplNuovo;
    }// end of method


    /**
     * Elabora una voce biografica del server wiki <br>
     * Dalla pagina wiki recupera una voce e registra la entity sul mongoDB <br>
     * Costruisce un template dalla entity sul mongoDB <br>
     * Elabora la voce, modificando eventualmente alcuni parametri ed ordinandoli tutti <br>
     * Merge il tmplBioServer scaricato ed il tmplBioServer elaborato <br>
     * Registra le modifiche sul server wiki <br>
     */
    public void elaboraVoce(String wikiTitle) {

    }// end of method


    /**
     * EAElabora.ordinaNormaliNoLoss
     * <p>
     * Riordina il template SENZA nessuna modifica dei valori preesistenti <br>
     * Riordina i parametri <br>
     * Aggiunge quelli 'normali' mancanti vuoti (sono 11) <br>
     * Elimina quelli esistenti vuoti, senza valore <br>
     * Registra le modifiche sul mongoDB <br>
     */
    public boolean ordinaNormaliNoLoss(Bio bio) {
        boolean modificato = false;
        String oldTmpl = bio.getTmplBioServer();
        String newTmpl = ordinaNormaliNoLoss(oldTmpl);

        if (!newTmpl.equals(oldTmpl)) {
            bio.setTmplBioServer(newTmpl);
            bioService.save(bio);
            modificato = true;
        }// end of if cycle

        return modificato;
    }// end of method


    /**
     * EAElabora.ordinaNormaliNoLoss
     * <p>
     * Riordina il template SENZA nessuna modifica dei valori preesistenti <br>
     * Riordina i parametri <br>
     * Aggiunge quelli 'normali' mancanti vuoti (sono 11) <br>
     * Elimina quelli esistenti vuoti, senza valore <br>
     */
    public String ordinaNormaliNoLoss(String tmplEntrata) {
        StringBuilder tmplOrdinato = new StringBuilder(VUOTA);
        LinkedHashMap<String, String> mappa = null;
        String iniTemplate = "{{Bio";
        String endTemplate = "}}";

        if (text.isValid(tmplEntrata)) {
            mappa = libBio.getMappaBio(tmplEntrata);
        }// end of if cycle

        if (mappa != null) {
            tmplOrdinato = new StringBuilder(iniTemplate);
            tmplOrdinato.append(A_CAPO);
            for (ParBio par : ParBio.values()) {
                if (par.isCampoNormale() || text.isValid(mappa.get(par.getTag()))) {
                    tmplOrdinato.append(par.getRiga(mappa.get(par.getTag())));
                }// end of if cycle
            }// end of for cycle
            tmplOrdinato.append(endTemplate);
        }// end of if cycle

        return tmplOrdinato.toString();
    }// end of method


}// end of class
