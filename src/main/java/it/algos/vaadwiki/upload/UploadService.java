package it.algos.vaadwiki.upload;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.modules.anno.Anno;
import it.algos.vaadflow.modules.anno.AnnoService;
import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadflow.modules.giorno.GiornoService;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.modules.cognome.Cognome;
import it.algos.vaadwiki.modules.nome.Nome;
import it.algos.vaadwiki.service.ABioService;
import it.algos.wiki.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;

import static it.algos.vaadflow.application.FlowCost.SPAZIO;
import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadwiki.application.WikiCost.USA_REGISTRA_SEMPRE_CRONO;

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
public class UploadService extends ABioService {

    /**
     * tag per il titolo di una lista cronologica
     */
    public static final String NATI = "Nati";

    /**
     * tag per il titolo di una lista cronologica
     */
    public static final String MORTI = "Morti";

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected GiornoService giornoService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected AnnoService annoService;


    /**
     * Esegue un ciclo di creazione (UPLOAD) delle liste di nati e morti per ogni giorno dell'anno
     */
    public void uploadAllGiorni() {
        ArrayList<Giorno> listaGiorni = giornoService.findAll();
        long inizio = System.currentTimeMillis();
        int modNati = 0;
        int modMorti = 0;
        String modTxt;

        for (Giorno giorno : listaGiorni) {
            uploadGiornoNato(giorno);
            modNati++;

            uploadGiornoMorto(giorno);
            modMorti++;
        }// end of for cycle

//        if (Pref.getBool(CostBio.USA_LOG_DEBUG, false)) {
//            modTxt = LibNum.format(modNati) + "+" + LibNum.format(modMorti);
//            if (Pref.getBool(CostBio.USA_REGISTRA_SEMPRE_CRONO, true)) {
//                Log.debug("upload", "Aggiornate tutte (366*2) le pagine dei giorni (nati e morti) in " + LibTime.difText(inizio));
//            } else {
//                Log.debug("upload", "Aggiornate solo le pagine modificate (" + modTxt + ") dei giorni (nati e morti) in " + LibTime.difText(inizio));
//            }// end of if/else cycle
//        }// end of if cycle
    }// end of method


    /**
     * Esegue un ciclo di creazione (UPLOAD) delle liste di nati e morti per ogni giorno dell'anno
     */
    public void uploadAllAnni() {
        ArrayList<Anno> listaAnni = annoService.findAll();
        long inizio = System.currentTimeMillis();
        int modNati = 0;
        int modMorti = 0;
        String modTxt;

        for (Anno anno : listaAnni) {
            uploadAnnoNato(anno);
            modNati++;

            uploadAnnoMorto(anno);
            modMorti++;
        }// end of for cycle

//        if (Pref.getBool(CostBio.USA_LOG_DEBUG, false)) {
//            modTxt = LibNum.format(modNati) + "+" + LibNum.format(modMorti);
//            if (Pref.getBool(CostBio.USA_REGISTRA_SEMPRE_CRONO, true)) {
//                Log.debug("upload", "Aggiornate tutte (366*2) le pagine dei giorni (nati e morti) in " + LibTime.difText(inizio));
//            } else {
//                Log.debug("upload", "Aggiornate solo le pagine modificate (" + modTxt + ") dei giorni (nati e morti) in " + LibTime.difText(inizio));
//            }// end of if/else cycle
//        }// end of if cycle
    }// end of method

    /**
     * Esegue un ciclo di creazione (UPLOAD) delle liste persone per ogni nome superiore alla soglia fissata
     */
    public void uploadAllNomi() {
    }// end of method

    /**
     * Esegue un ciclo di creazione (UPLOAD) delle liste persone per ogni cognome superiore alla soglia fissata
     */
    public void uploadAllCognomi() {
    }// end of method

    /**
     * Carica sul servere wiki la entity indicata
     *
     * @param wikiTitle della pagina wiki (obbligatorio, unico)
     */
    public void esegue(String wikiTitle) {
        Bio entity = bioService.findByKeyUnica(wikiTitle);
    }// end of method


    /**
     * Legg
     * Crea un nuovo template dai dati dellla entity
     */
    public String creaTemplate(Bio entity) {
        String newTemplate = "";
//        Bio entity = bioService.findByKeyUnica(pageid);

        return newTemplate;
    }// end of method


    /**
     * Controlla che esistano modifiche sostanziali (non solo la data)
     *
     * @param titoloVoce eventualmente da modificare
     * @param testoNew   della modifica
     * @param tagIni     inizio del testo iniziale (incipit) da considerare NON sostanziale
     * @param tagEnd     fine del testo iniziale (incipit) da considerare NON sostanziale
     *
     * @return la modifica va effettuata
     */
    public boolean checkModificaSostanziale(String titoloVoce, String testoNew, String tagIni, String tagEnd) {
        boolean status = false;
        String testoOldSignificativo = VUOTA;
        String testoNewSignificativo = VUOTA;
        String testoOld = "";
        int pos1 = 0;
        int pos2 = 0;

        if (pref.isBool(USA_REGISTRA_SEMPRE_CRONO)) {
            return true;
        }// end of if cycle

        testoOld = Api.leggeVoce(titoloVoce);
        if (text.isEmpty(testoOld)) {
            return true;
        }// end of if cycle

        if (text.isValid(testoOld) && text.isValid(testoNew)) {
            pos1 = testoOld.indexOf(tagIni);
            pos2 = testoOld.indexOf(tagEnd, pos1);
            try { // prova ad eseguire il codice
                testoOldSignificativo = testoOld.substring(pos2);
            } catch (Exception unErrore) { // intercetta l'errore
                int a = 87; //todo per ora niente (spedire mail)
            }// fine del blocco try-catch

            pos1 = testoNew.indexOf(tagIni);
            pos2 = testoNew.indexOf(tagEnd, pos1);
            try { // prova ad eseguire il codice
                testoNewSignificativo = testoNew.substring(pos2);
            } catch (Exception unErrore) { // intercetta l'errore
                int a = 87; //todo per ora niente (spedire mail)
            }// fine del blocco try-catch
        }// fine del blocco if

        if (text.isValid(testoOldSignificativo) && text.isValid(testoNewSignificativo)) {
            if (!testoNewSignificativo.equals(testoOldSignificativo)) {
                status = true;
            }// fine del blocco if
        }// fine del blocco if

        return status;
    } // fine del metodo


    public UploadGiornoNato uploadGiornoNato(Giorno giorno) {
        return appContext.getBean(UploadGiornoNato.class, giorno);
    }// end of method


    public UploadGiornoMorto uploadGiornoMorto(Giorno giorno) {
        return appContext.getBean(UploadGiornoMorto.class, giorno);
    }// end of method


    public UploadAnnoNato uploadAnnoNato(Anno anno) {
        return appContext.getBean(UploadAnnoNato.class, anno);
    }// end of method


    public UploadAnnoMorto uploadAnnoMorto(Anno anno) {
        return appContext.getBean(UploadAnnoMorto.class, anno);
    }// end of method


    public UploadNome uploadNome(Nome nome) {
        return appContext.getBean(UploadNome.class, nome);
    }// end of method


    public UploadCognome uploadCognome(Cognome cognome) {
        return appContext.getBean(UploadCognome.class, cognome);
    }// end of method


    /**
     * Titolo della pagina Nati/Morti da creare/caricare su wikipedia
     */
    public String getTitoloGiorno(Giorno giorno, String tag) {
        String titoloLista = VUOTA;
        String titolo = giorno.getTitolo();
        String articolo = "il";
        String articoloBis = "l'";

        tag = tag.trim();
        if (!titolo.equals(VUOTA)) {
            if (titolo.startsWith("8") || titolo.startsWith("11")) {
                titoloLista = tag + SPAZIO + articoloBis + titolo;
            } else {
                titoloLista = tag + SPAZIO + articolo + SPAZIO + titolo;
            }// fine del blocco if-else
        }// fine del blocco if

        return titoloLista;
    }// fine del metodo


    /**
     * Titolo della pagina Nati da creare/caricare su wikipedia
     */
    public String getTitoloGiornoNato(Giorno giorno) {
        return getTitoloGiorno(giorno, NATI);
    }// fine del metodo


    /**
     * Titolo della pagina Morti da creare/caricare su wikipedia
     */
    public String getTitoloGiornoMorto(Giorno giorno) {
        return getTitoloGiorno(giorno, MORTI);
    }// fine del metodo


    /**
     * Titolo della pagina Nati/Morti da creare/caricare su wikipedia
     */
    public String getTitoloAnno(Anno anno, String tag) {
        String titoloLista = VUOTA;
        String titolo = anno.getTitolo();
        String articolo = "nel";
        String articoloBis = "nell'";
        String TAG_AC = " a.C.";

        tag = tag.trim();
        if (!titolo.equals(VUOTA)) {
            if (titolo.equals("1")
                    || titolo.equals("1" + TAG_AC)
                    || titolo.equals("11")
                    || titolo.equals("11" + TAG_AC)
                    || titolo.startsWith("8")
            ) {
                titoloLista = tag + SPAZIO + articoloBis + titolo;
            } else {
                titoloLista = tag + SPAZIO + articolo + SPAZIO + titolo;
            }// fine del blocco if-else
        }// fine del blocco if

        return titoloLista;
    }// fine del metodo


    /**
     * Titolo della pagina Nati da creare/caricare su wikipedia
     */
    public String getTitoloAnnoNato(Anno anno) {
        return getTitoloAnno(anno, NATI);
    }// fine del metodo


    /**
     * Titolo della pagina Morti da creare/caricare su wikipedia
     */
    public String getTitoloAnnoMorto(Anno anno) {
        return getTitoloAnno(anno, MORTI);
    }// fine del metodo


    /**
     * Titolo della pagina Nomi da creare/caricare su wikipedia
     */
    public String getTitoloNome(String titolo) {
        return "Persone di nome " + text.primaMaiuscola(titolo);
    }// fine del metodo


    /**
     * Titolo della pagina Nomi da creare/caricare su wikipedia
     */
    public String getTitoloNome(Nome nome) {
        return getTitoloNome(nome.getNome());
    }// fine del metodo


    /**
     * Titolo della pagina Cognome da creare/caricare su wikipedia
     */
    public String getTitoloCognome(String titolo) {
        return "Persone di cognome " + text.primaMaiuscola(titolo);
    }// fine del metodo


    /**
     * Titolo della pagina Cognome da creare/caricare su wikipedia
     */
    public String getTitoloCognome(Cognome cognome) {
        return getTitoloCognome(cognome.getCognome());
    }// fine del metodo

}// end of class
