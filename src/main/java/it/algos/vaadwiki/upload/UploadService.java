package it.algos.vaadwiki.upload;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.enumeration.EALogType;
import it.algos.vaadflow.modules.anno.Anno;
import it.algos.vaadflow.modules.anno.AnnoService;
import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadflow.modules.giorno.GiornoService;
import it.algos.vaadflow.modules.log.LogService;
import it.algos.vaadflow.modules.logtype.LogtypeService;
import it.algos.vaadflow.service.AMailService;
import it.algos.vaadwiki.modules.attivita.Attivita;
import it.algos.vaadwiki.modules.attivita.AttivitaService;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.modules.cognome.Cognome;
import it.algos.vaadwiki.modules.cognome.CognomeService;
import it.algos.vaadwiki.modules.nazionalita.Nazionalita;
import it.algos.vaadwiki.modules.nazionalita.NazionalitaService;
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
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected AttivitaService attivitaService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected NazionalitaService nazionalitaService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected AMailService mailService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected LogService logger;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    private LogtypeService logtypeService;


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
        if (checkMongo()) {
            return;
        }// end of if cycle

        long inizio = System.currentTimeMillis();
        for (Giorno giorno : giornoService.findAll()) {
            uploadGiornoNato(giorno);
            uploadGiornoMorto(giorno);
        }// end of for cycle

        setLastUpload(inizio, LAST_UPLOAD_GIORNI, DURATA_UPLOAD_GIORNI);
        logger.crea(EALogType.upload, "Upload delle liste per i nati e morti nei 366 giorni previsti", inizio);
    }// end of method


    /**
     * Esegue un ciclo di creazione (UPLOAD) delle liste di nati e morti per ogni giorno dell'anno
     * Controlla che il mongoDb delle voci biografiche non sia vuoto <br>
     */
    public void uploadAllAnni() {
        if (checkMongo()) {
            return;
        }// end of if cycle

        long inizio = System.currentTimeMillis();
        for (Anno anno : annoService.findAll()) {
            uploadAnnoNato(anno);
            uploadAnnoMorto(anno);
        }// end of for cycle

        setLastUpload(inizio, LAST_UPLOAD_ANNI, DURATA_UPLOAD_ANNI);
        logger.crea(EALogType.upload, "Upload delle liste per i nati e morti nei 3030 anni teoricamente possibili", inizio);
    }// end of method


    /**
     * Esegue un ciclo di creazione (UPLOAD) delle liste persone per ogni nome superiore alla soglia fissata <br>
     * Controlla che il mongoDb delle voci biografiche non sia vuoto <br>
     * Ricrea al volo (per sicurezza di aggiornamento) tutta la collezione mongoDb dei nomi <br>
     */
    public void uploadAllNomi() {
        List<Nome> listaNomi = null;
        int numBio = pref.getInt(SOGLIA_NOMI_PAGINA_WIKI);

        //--Controlla che il mongoDb delle voci biografiche abbia una dimensione accettabile, altrimenti non esegue
        if (checkMongo()) {
            return;
        }// end of if cycle

        //--Ricrea al volo (per sicurezza di aggiornamento) tutta la collezione mongoDb dei nomi (circa due minuti)
        long inizio = System.currentTimeMillis();
        nomeService.elabora();
        listaNomi = nomeService.findAll();

        for (Nome nome : listaNomi) {
            if (nome.voci > pref.getInt(SOGLIA_NOMI_PAGINA_WIKI)) {
                uploadNome(nome);
            }// end of if cycle
        }// end of for cycle

        setLastUpload(inizio, LAST_UPLOAD_NOMI, DURATA_UPLOAD_NOMI);
        logger.crea(EALogType.upload, "Upload delle liste per ogni nome con più di " + numBio + " persone", inizio);
    }// end of method


    /**
     * Esegue un ciclo di creazione (UPLOAD) delle liste persone per ogni cognome superiore alla soglia fissata
     * Controlla che il mongoDb delle voci biografiche non sia vuoto <br>
     */
    public void uploadAllCognomi() {
        int numBio = pref.getInt(SOGLIA_COGNOMI_PAGINA_WIKI);

        if (checkMongo()) {
            return;
        }// end of if cycle

        long inizio = System.currentTimeMillis();
        for (Cognome cognome : cognomeService.findAll()) {
            if (cognome.voci > pref.getInt(SOGLIA_COGNOMI_PAGINA_WIKI)) {
                uploadCognome(cognome);
            }// end of if cycle
        }// end of for cycle

        setLastUpload(inizio, LAST_UPLOAD_COGNOMI, DURATA_UPLOAD_COGNOMI);
        logger.crea(EALogType.upload, "Upload delle liste per ogni cognome con più di " + numBio + " persone", inizio);
    }// end of method


    /**
     * Esegue un ciclo di creazione (UPLOAD) delle liste attività per ogni attività superiore alla soglia fissata <br>
     * Controlla che il mongoDb delle voci biografiche non sia vuoto <br>
     */
    public void uploadAllAttivita() {
        if (checkMongo()) {
            return;
        }// end of if cycle

        long inizio = System.currentTimeMillis();
        for (Attivita attivita : attivitaService.findAll()) {
            uploadAttivita(attivita);
        }// end of for cycle

        setLastUpload(inizio, LAST_UPLOAD_ATTIVITA, DURATA_UPLOAD_ATTIVITA);
        logger.crea(EALogType.upload, "Upload delle liste per attività", inizio);
    }// end of method


    /**
     * Esegue un ciclo di creazione (UPLOAD) delle liste nazionalità per ogni nazionalità superiore alla soglia fissata <br>
     * Controlla che il mongoDb delle voci biografiche non sia vuoto <br>
     */
    public void uploadAllNazionalita() {
        if (checkMongo()) {
            return;
        }// end of if cycle

        long inizio = System.currentTimeMillis();
        for (Nazionalita nazionalità : nazionalitaService.findAll()) {
            uploadNazionalita(nazionalità);
        }// end of for cycle

        setLastUpload(inizio, LAST_UPLOAD_NAZIONALITA, DURATA_UPLOAD_NAZIONALITA);
        logger.crea(EALogType.upload, "Upload delle liste per nazionalità", inizio);
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
     * Modifica sul server wiki il template della bio indicata
     * <p>
     * 2) Recupera dal server il testo della voce
     * 2) Recupera il tmplOld dal testo della voce
     * 5) Sostituisce il tmplNew al tmplOld nel testo della voce
     * 6) Aggiorna la voce
     *
     * @param wikiTitle della pagina wiki (obbligatorio, unico)
     * @param tmplNew   da sostituire a quello esistente
     */
    public void uploadTmpl(String wikiTitle, String tmplNew) {
        String summary = "[[utente:Biobot/fixTmplBio|fixTmplBio]]";
        String testoServerOld = Api.leggeVoce(wikiTitle);
        String tmplOld = Api.estraeTmplBio(testoServerOld);
        String  testoServerNew = text.sostituisce(testoServerOld, tmplOld, tmplNew);

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


    public void uploadGiornoNato(Giorno giorno) {
        appContext.getBean(UploadGiornoNato.class, giorno);
    }// end of method


    public void uploadGiornoMorto(Giorno giorno) {
        appContext.getBean(UploadGiornoMorto.class, giorno);
    }// end of method


    public void uploadAnnoNato(Anno anno) {
        appContext.getBean(UploadAnnoNato.class, anno);
    }// end of method


    public void uploadAnnoMorto(Anno anno) {
        appContext.getBean(UploadAnnoMorto.class, anno);
    }// end of method


    public void uploadNome(Nome nome) {
        appContext.getBean(UploadNome.class, nome);
    }// end of method


    public void uploadCognome(Cognome cognome) {
        appContext.getBean(UploadCognome.class, cognome);
    }// end of method


    public void uploadAttivita(Attivita attivita) {
        appContext.getBean(UploadAttivita.class, attivita);
    }// end of method


    public void uploadNazionalita(Nazionalita nazionalita) {
        appContext.getBean(UploadNazionalita.class, nazionalita);
    }// end of method


    /**
     * Titolo della pagina Nati/Morti da creare/caricare su wikipedia
     */
    public String getTitoloGiorno(Giorno giorno, String tag) {
        String titoloLista = VUOTA;
        String titolo = giorno.titolo;
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
        String titolo = anno.titolo;
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
        return getTitoloNome(nome.nome);
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
        return getTitoloCognome(cognome.cognome);
    }// fine del metodo


    /**
     * Titolo della pagina Attivita da creare/caricare su wikipedia
     */
    public String getTitoloAttivita(String titolo) {
        return "Progetto:Biografie/Attività/" + text.primaMaiuscola(titolo);
    }// fine del metodo


    /**
     * Titolo della pagina Attivita da creare/caricare su wikipedia
     */
    public String getTitoloAttivita(Attivita attivita) {
        return getTitoloAttivita(attivita.plurale);
    }// fine del metodo


    /**
     * Titolo della pagina Nazionalita da creare/caricare su wikipedia
     */
    public String getTitoloNazionalita(String titolo) {
        return "Progetto:Biografie/Nazionalità/" + text.primaMaiuscola(titolo);
    }// fine del metodo


    /**
     * Titolo della pagina Nazionalita da creare/caricare su wikipedia
     */
    public String getTitoloNazionalita(Nazionalita nazionalita) {
        return getTitoloNazionalita(nazionalita.plurale);
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
