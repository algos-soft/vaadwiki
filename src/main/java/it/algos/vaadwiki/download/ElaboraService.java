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

    public static String DEFAULT_GENERE = "M";

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
        int cont = 0;

        for (int k = 0; k < array.numCicli(totale, size); k++) {
            lista = mongo.mongoOp.find(new Query().with(PageRequest.of(k, size, sort)), Bio.class);
            for (Bio bio : lista) {
                if (check(bio)) {
                    cont++;
                    logger.sendTerminale(EALogLivello.debug, "Check EAElabora.ordinaNormaliNoLoss. (" + cont + ") La voce " + bio.getWikiTitle() + " ha il template diverso da quello standard.", ElaboraService.class, "checkAll");
                }// end of if cycle
            }// end of for cycle
        }// end of for cycle

        logger.sendTerminale(EALogLivello.debug, "Check EAElabora.ordinaNormaliNoLoss. Su " + text.format(totale) + " voci ce ne sono " + text.format(cont) + " col template diverso da quello standard", ElaboraService.class, "checkAll");
        logger.crea(EALogType.elabora, "Su " + text.format(totale) + " voci ci sono " + text.format(cont) + " template diversi. Controllati", inizio);
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
        HashMap<String, String> mappaGrezza;

        //--Recupera i valori base di tutti i parametri dal tmplBioServer
        mappaGrezza = libBio.getMappaGrezzaBio(bio);

//        //--Elabora valori validi dei parametri significativi
//        if (mappa != null) {
//            elaboraValidi(mappa);
//        }// end of if cycle

        //--Elabora valori validi dei parametri significativi
        //--Inserisce i valori nella entity Bio
        if (mappaGrezza != null) {
            setValue(bio, mappaGrezza, registra);
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


//    /**
//     * Estrae dal templateServer una mappa di parametri corrispondenti ai campi della tavola Bio
//     */
//    public HashMap<String, String> getMappaGrezzaBio(Bio bio) {
//        HashMap<String, String> mappa = null;
//        String tmplBioServer = bio.getTmplBioServer();
//
//        if (text.isValid(tmplBioServer)) {
//            mappa = new HashMap<>();
//            mappa = libBio.getMappaGrezzaBio(tmplBioServer);
//        }// end of if cycle
//
//        return mappa;
//    }// end of method


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

        if (bio != null) {
            for (ParBio par : ParBio.values()) {
                value = par.getValue(bio);
                if (text.isValid(value) || par.isCampoNormale()) {
                    tmplBioMongo += par.getRiga(bio);
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return addTagTemplate(tmplBioMongo);
    }// end of method


    /**
     * Costruisce un template da una mappa di parametri <br>
     * ATTENZIONE - Non è merged con eventuali parametri non normali <br>
     */
    public String getTmpl(HashMap<String, String> mappa) {
        String tmplBio = VUOTA;
        String value;

        if (mappa.get(ParBio.sesso.getTag()) == null) {
            mappa.put(ParBio.sesso.getTag(), DEFAULT_GENERE);
        }// end of if cycle

        if (mappa != null) {
            for (ParBio par : ParBio.values()) {
                value = mappa.get(par.getTag());
                if (value != null || par.isCampoNormale()) {
                    tmplBio += (par.getRiga(value));
                }// end of if cycle
            } // fine del ciclo for-each
        }// end of if cycle

        return addTagTemplate(tmplBio);
    }// end of method


    /**
     * Merge di un template con i parametri di mappaMongo PIU quelli di mappaServer <br>
     * <p>
     * Se esiste un parametro 'normale' di mappaMongo, lo usa <br>
     * Se non esiste un parametro 'normale' di mappaMongo ma esiste di mappaServer, lo usa <br>
     * Se non esiste un parametro 'normale' ne di mappaMongo ne di mappaServer, usa un stringa di valore VUOTA <br>
     * Non esistono parametri 'extra' di mappaMongo <br>
     * Se esiste un parametro 'extra' di mappaServer, lo usa <br>
     * Se non esiste un parametro 'extra' di mappaServer, non inserisce la riga  <br>
     */
    public String getMerged(String tmplBioMongo, String tmplBioServer) {
        return getMerged(libBio.getMappaGrezzaBio(tmplBioMongo), libBio.getMappaGrezzaBio(tmplBioServer));
    }// end of method


    /**
     * Merge di una mappa di parametri PIU i parametri del tmplBioServer <br>
     * <p>
     * Se esiste un parametro 'normale' di mappaMongo, lo usa <br>
     * Se non esiste un parametro 'normale' di mappaMongo ma esiste di mappaServer, lo usa <br>
     * Se non esiste un parametro 'normale' ne di mappaMongo ne di mappaServer, usa un stringa di valore VUOTA <br>
     * Non esistono parametri 'extra' di mappaMongo <br>
     * Se esiste un parametro 'extra' di mappaServer, lo usa <br>
     * Se non esiste un parametro 'extra' di mappaServer, non inserisce la riga  <br>
     */
    public String getMerged(HashMap<String, String> mappa, String tmplBioServer) {
        return getMerged(mappa, libBio.getMappaGrezzaBio(tmplBioServer));
    }// end of method


    /**
     * Merge di una mappa di parametri mappaMongo PIU i parametri di una mappa tmplBioServer <br>
     * <p>
     * Se esiste un parametro 'normale' di mappaMongo, lo usa <br>
     * Se non esiste un parametro 'normale' di mappaMongo ma esiste di mappaServer, lo usa <br>
     * Se non esiste un parametro 'normale' ne di mappaMongo ne di mappaServer, usa un stringa di valore VUOTA <br>
     * Non esistono parametri 'extra' di mappaMongo <br>
     * Se esiste un parametro 'extra' di mappaServer, lo usa <br>
     * Se non esiste un parametro 'extra' di mappaServer, non inserisce la riga  <br>
     */
    public String getMerged(HashMap<String, String> mappaMongo, HashMap<String, String> mappaServer) {
        StringBuilder tmplMerged = new StringBuilder(VUOTA);
        String valueMongo;
        String valueServer;
        String valueMerged;
        String valueRiga;

        if (mappaMongo != null && mappaServer != null) {

            if (text.isEmpty(mappaServer.get(ParBio.sesso.getTag()))) {
                mappaServer.put(ParBio.sesso.getTag(), DEFAULT_GENERE);
            }// end of if cycle

            for (ParBio par : ParBio.values()) {
                valueMongo = mappaMongo.get(par.getTag());
                valueServer = mappaServer.get(par.getTag());

                if (par.isCampoValido()) {
                    if (text.isValid(valueMongo)) {
                        valueMerged = par.sostituisceParteValida(valueServer, valueMongo);
                    } else {
                        if (text.isValid(valueServer)) {
//                            valueMerged = eliminaDopoVirgola(par, valueServer);
                            valueMerged = par.elaboraParteValida(valueServer);
                        } else {
                            valueMerged = VUOTA;
                        }// end of if/else cycle
                    }// end of if/else cycle
                    if (text.isValid(valueMerged) || par.isCampoNormale()) {
                        valueRiga = par.getRiga(valueMerged);
                        tmplMerged.append(valueRiga);
                    }// end of if cycle
                } else {
                    if (text.isValid(valueServer)) {
                        valueRiga = par.getRiga(valueServer);
                        tmplMerged.append(valueRiga);
                    }// end of if cycle
                }// end of if/else cycle
            }// end of for cycle
        }// end of if cycle

        return addTagTemplate(tmplMerged.toString());
    }// end of method


    /**
     *
     */
    public String estraeValore(ParBio par, String testoOriginale) {
        return par.estraeValoreInizialeValido(testoOriginale);
    }// end of method


    /**
     * testo dopo virgola scartato (in alcune property solamente) <br>
     */
    public String eliminaDopoVirgola(ParBio par, String testoOriginale) {
        String testoValido = testoOriginale;

        if (par == ParBio.luogoNascita || par == ParBio.luogoMorte) {
            testoValido = text.levaCodaDa(testoValido, VIRGOLA);
        }// end of if cycle

        return testoValido;
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
     * Riordina il template SENZA nessuna modifica SIGNIFICATIVA dei valori preesistenti <br>
     * Riordina i parametri <br>
     * Aggiunge quelli 'normali' mancanti vuoti (sono 11) <br>
     * Elimina quelli esistenti vuoti, senza valore <br>
     */
    public String ordinaNormaliNoLoss(String tmplEntrata) {
        String tmplOrdinato = VUOTA;
        StringBuilder builder = new StringBuilder(VUOTA);
        LinkedHashMap<String, String> mappa = null;
        String tag;
        String value = VUOTA;
        String riga = VUOTA;

        if (text.isValid(tmplEntrata)) {
            mappa = libBio.getMappaGrezzaBio(tmplEntrata);
        }// end of if cycle

        if (mappa != null) {
            for (ParBio par : ParBio.values()) {
                tag = par.getTag();
                value = mappa.get(tag);
                if (par.isCampoNormale() || text.isValid(value)) {
                    riga = par.getRiga(value);
                    builder.append(riga);
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        tmplOrdinato = builder.toString();
        tmplOrdinato = addTagTemplate(tmplOrdinato);
        return tmplOrdinato;
    }// end of method


    /**
     * EAElabora.ordinaNormaliNoLoss
     * <p>
     * Riordina il template CON modifiche ai valori preesistenti <br>
     * Riordina i parametri <br>
     * Aggiunge quelli 'normali' mancanti vuoti (sono 11) <br>
     * Elimina quelli esistenti vuoti, senza valore <br>
     * Modifica i parametri secondo le regole base (minuscole, 1° del mese, parentesi quadre) <br>
     */
    public String ordinaNormaliWithLoss(String tmplEntrata) {
        String tmplOrdinato = VUOTA;
        StringBuilder builder = new StringBuilder(VUOTA);
        LinkedHashMap<String, String> mappa = null;
        String tag;
        String value = VUOTA;
        String riga = VUOTA;

        if (text.isValid(tmplEntrata)) {
            mappa = libBio.getMappaGrezzaBio(tmplEntrata);
        }// end of if cycle

        if (mappa != null) {
            for (ParBio par : ParBio.values()) {
                tag = par.getTag();
                value = mappa.get(tag);

                if (par.isCampoNormale() || text.isValid(value)) {
                    riga = par.getRiga(value);
                    builder.append(riga);
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        tmplOrdinato = builder.toString();
        tmplOrdinato = addTagTemplate(tmplOrdinato);
        return tmplOrdinato;
    }// end of method


    /**
     * Aggiunge tag iniziali e finali del template <br>
     */
    public String addTagTemplate(String tmplGrezzo) {
        String tmplFinale = VUOTA;
        String iniTemplate = "{{Bio";
        String endTemplate = "}}";

        if (tmplGrezzo != null && tmplGrezzo.length() > 0) {
            tmplFinale += iniTemplate;
            tmplFinale += A_CAPO;
            tmplFinale += tmplGrezzo;
            tmplFinale += endTemplate;
        }// end of if cycle

        return tmplFinale;
    }// end of method

}// end of class
