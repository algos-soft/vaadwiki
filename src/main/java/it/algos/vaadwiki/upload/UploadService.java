package it.algos.vaadwiki.upload;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.modules.anno.Anno;
import it.algos.vaadflow.modules.anno.AnnoService;
import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadflow.modules.giorno.GiornoService;
import it.algos.vaadflow.service.AMailService;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.modules.cognome.Cognome;
import it.algos.vaadwiki.modules.cognome.CognomeService;
import it.algos.vaadwiki.modules.nome.Nome;
import it.algos.vaadwiki.modules.nome.NomeService;
import it.algos.vaadwiki.service.ABioService;
import it.algos.wiki.Api;
import it.algos.wiki.web.AQueryWrite;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.time.LocalDateTime;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.SPAZIO;
import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadwiki.application.WikiCost.*;

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
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;

    /**
     * Private final property
     */
    private static final UploadService INSTANCE = new UploadService();

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
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected NomeService nomeService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected CognomeService cognomeService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected AMailService mailService;


    /**
     * Private constructor to avoid client applications to use constructor
     */
    private UploadService() {
    }// end of constructor


    /**
     * Gets the unique instance of this Singleton.
     *
     * @return the unique instance of this Singleton
     */
    public static UploadService getInstance() {
        return INSTANCE;
    }// end of static method


    /**
     * Esegue un ciclo di creazione (UPLOAD) delle liste di nati e morti per ogni giorno dell'anno <br>
     * Controlla che il mongoDb delle voci biografiche non sia vuoto <br>
     */
    public void uploadAllGiorni() {
        if (checkBioScarso()) {
            mailService.send("Upload giorni", "Abortito l'upload dei giorni perché il mongoDb delle biografie sembra vuoto o comunque carente di voci che invece dovrebbero esserci.");
            return;
        }// end of if cycle

        long inizio = System.currentTimeMillis();
        List<Giorno> listaGiorni = giornoService.findAll();

        for (Giorno giorno : listaGiorni) {
            uploadGiornoNato(giorno);
            uploadGiornoMorto(giorno);
        }// end of for cycle

        setLastUpload(inizio, LAST_UPLOAD_GIORNI, DURATA_UPLOAD_GIORNI);
    }// end of method


    /**
     * Esegue un ciclo di creazione (UPLOAD) delle liste di nati e morti per ogni giorno dell'anno
     */
    public void uploadAllAnni() {
        if (checkBioScarso()) {
            mailService.send("Upload anni", "Abortito l'upload degli anni perché il mongoDb delle biografie sembra vuoto o comunque carente di voci che invece dovrebbero esserci.");
            return;
        }// end of if cycle

        long inizio = System.currentTimeMillis();
        List<Anno> listaAnni = annoService.findAll();

        for (Anno anno : listaAnni) {
            uploadAnnoNato(anno);
            uploadAnnoMorto(anno);
        }// end of for cycle

        setLastUpload(inizio, LAST_UPLOAD_ANNI, DURATA_UPLOAD_ANNI);
    }// end of method


    /**
     * Esegue un ciclo di creazione (UPLOAD) delle liste persone per ogni nome superiore alla soglia fissata <br>
     * Ricrea al volo (per sicurezza di aggiornamento) tutta la collezione mongoDb dei nomi <br>
     */
    public void uploadAllNomi() {
        List<Nome> listaNomi = null;

        //--Controlla che il mongoDb delle voci biografiche abbia una dimensione accettabile, altrimenti non esegue
        if (checkBioScarso()) {
            mailService.send("Upload nomi", "Abortito l'upload dei nomi perché il mongoDb delle biografie sembra vuoto o comunque carente di voci che invece dovrebbero esserci.");
            return;
        }// end of if cycle

        //--Ricrea al volo (per sicurezza di aggiornamento) tutta la collezione mongoDb dei nomi (circa due minuti)
        nomeService.crea();
        listaNomi = nomeService.findAll();

        for (Nome nome : listaNomi) {
            if (nome.voci > pref.getInt(SOGLIA_NOMI_PAGINA_WIKI)) {
                uploadNome(nome);
            }// end of if cycle
        }// end of for cycle
    }// end of method


    /**
     * Esegue un ciclo di creazione (UPLOAD) delle liste persone per ogni cognome superiore alla soglia fissata
     */
    public void uploadAllCognomi() {
        if (checkBioScarso()) {
            mailService.send("Upload cognomi", "Abortito l'upload dei cognomi perché il mongoDb delle biografie sembra vuoto o comunque carente di voci che invece dovrebbero esserci.");
            return;
        }// end of if cycle

        List<Cognome> listaCognomi = cognomeService.findAll();

        for (Cognome cognome : listaCognomi) {
            if (cognome.voci > pref.getInt(SOGLIA_COGNOMI_PAGINA_WIKI)) {
                uploadCognome(cognome);
            }// end of if cycle
        }// end of for cycle
    }// end of method


    /**
     * Carica sul server wiki la entity indicata
     * <p>
     * 1) Recupera la entity dal mongoDB (parametri eventualmente modificati dal programma)
     * 2) Scarica la voce dal server (senza modificare il template)
     * 4) Esegue un merge (ragionato) tra il template del server e la entity
     * 5) Sostituisce il templateMerged al testoServerNew nel testo della voce
     * 6) Upload del testo
     *
     * @param wikiTitle della pagina wiki (obbligatorio, unico)
     */
    public void uploadBio(String wikiTitle) {
        String testoServerNew;
        String summary = "fixParametri";
        Bio entity = bioService.findByKeyUnica(wikiTitle);
        String testoServerOld = Api.leggeVoce(wikiTitle);
        String templateServer = Api.estraeTmplBio(testoServerOld);
        String templateMerged = libBio.mergeTemplates(templateServer, entity);

        testoServerNew = text.sostituisce(testoServerOld, templateServer, templateMerged);

        if (pref.isBool(FlowCost.USA_DEBUG)) {
            appContext.getBean(AQueryWrite.class, Upload.PAGINA_PROVA, testoServerNew, summary);
        } else {
            appContext.getBean(AQueryWrite.class, wikiTitle, testoServerNew, summary);
        }// end of if/else cycle

    }// end of method


    /**
     * Carica sul server wiki la entity indicata
     * <p>
     * 1) Recupera la entity dal mongoDB (parametri eventualmente modificati dal programma)
     * 2) Scarica la voce dal server (senza modificare il template)
     * 4) Esegue un merge (ragionato) tra il template del server e la entity
     * 5) Sostituisce il templateMerged al testoServerNew nel testo della voce
     * 6) Upload del testo
     *
     * @param wikiTitle della pagina wiki (obbligatorio, unico)
     */
    public void uploadBioDebug(Bio entity) {
        String testoServerNew;
        String summary = "fixParametri";
        String testoServerOld = Api.leggeVoce(entity.wikiTitle);
        String templateServer = Api.estraeTmplBio(testoServerOld);
        String templateMerged = libBio.mergeTemplates(templateServer, entity);

        testoServerNew = text.sostituisce(testoServerOld, templateServer, templateMerged);

        appContext.getBean(AQueryWrite.class, Upload.PAGINA_PROVA, testoServerNew, summary);
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


    /**
     * Controlla che il mongoDb delle voci biografiche abbia una dimensione accettabile <br>
     * Per evitare di 'sparare' sul server pagine con biografie 'mancanti' <br>
     */
    private boolean checkBioScarso() {
        int minimo = BIO_NEEDED_MINUMUM_SIZE;
        int numVoci = bioService.count();

        return numVoci < minimo;
    }// end of method


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


    /**
     * Registra nelle preferenze la data dell'ultimo upload effettuato <br>
     * Registra nelle preferenze la durata dell'ultimo upload effettuato <br>
     * Valore registrato in minuti <br>
     */
    protected void setLastUpload(long inizio, String codeLastUpload, String durataLastUpload) {
        int delta = 1000 * 60;
        LocalDateTime lastUpload = LocalDateTime.now();
        pref.saveValue(codeLastUpload, lastUpload);

        long fine = System.currentTimeMillis();
        long durata = fine - inizio;
        int value = 0;
        if (durata > delta) {
            value = (int) durata / delta;
        } else {
            value = 1;
        }// end of if/else cycle
        pref.saveValue(durataLastUpload, value);
    }// end of method

}// end of class
