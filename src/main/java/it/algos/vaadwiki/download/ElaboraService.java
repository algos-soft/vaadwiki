package it.algos.vaadwiki.download;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.service.ABioService;
import it.algos.vaadwiki.service.LibBio;
import it.algos.vaadwiki.service.ParBio;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Project vaadbio2
 * Created by Algos
 * User: gac
 * Date: sab, 11-ago-2018
 * Time: 15:44
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
     * Elabora le voci biografiche <br>
     * Parte dal tmplBioServer e costruisce tutti parametri significativi <br>
     * Ogni parametro viene 'pulito' se presentato in maniera 'impropria' <br>
     * Quello che resta è affidabile ed utilizzabile per le liste <br>
     */
    public void esegue() {
        esegueAll();
    }// end of method


    /**
     * Elabora le voci biografiche <br>
     * Parte dal tmplBioServer e costruisce tutti parametri significativi <br>
     * Ogni parametro viene 'pulito' se presentato in maniera 'impropria' <br>
     * Quello che resta è affidabile ed utilizzabile per le liste <br>
     */
    public void esegueAll() {
        ArrayList<String> lista = bioService.findAllTitles();

        if (array.isValid(lista)) {
            esegue(lista);
        }// end of if cycle

    }// end of method


    /**
     * Elabora le voci biografiche indicate <br>
     * Parte dal tmplBioServer e costruisce tutti parametri significativi <br>
     * Ogni parametro viene 'pulito' se presentato in maniera 'impropria' <br>
     * Quello che resta è affidabile ed utilizzabile per le liste <br>
     */
    public void esegue(ArrayList<String> lista) {
        long inizio = System.currentTimeMillis();

        if (array.isValid(lista)) {
            for (String title : lista) {
                esegue(title);
            }// end of for cycle
            log.info("ELABORA - elaborati i parametri delle nuove voci (" + text.format(lista.size()) + " elementi) in " + date.deltaText(inizio));
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
    private HashMap<String, String> getMappaBio(Bio bio) {
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
    private void setValue(Bio bio, HashMap<String, String> mappa, boolean registra) {
        String value = null;

        try { // prova ad eseguire il codice
            if (bio != null) {

                // patch per i luoghi di nascita e morte
                // se è pieno il parametro link, lo usa
                if (text.isValid(mappa.get(ParBio.luogoNascitaLink.getTag()))) {
                    mappa.put(ParBio.luogoNascita.getTag(), mappa.get(ParBio.luogoNascitaLink.getTag()));
                }// end of if cycle
                if (text.isValid(mappa.get(ParBio.luogoMorteLink.getTag()))) {
                    mappa.put(ParBio.luogoMorte.getTag(), mappa.get(ParBio.luogoMorteLink.getTag()));
                }// end of if cycle

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

}// end of class
