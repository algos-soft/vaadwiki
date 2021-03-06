package it.algos.vaadwiki.modules.wiki;

import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EATempo;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadflow.service.AService;
import it.algos.vaadwiki.modules.attivita.AttivitaService;
import it.algos.vaadwiki.modules.genere.GenereService;
import it.algos.vaadwiki.modules.nazionalita.NazionalitaService;
import it.algos.vaadwiki.modules.professione.ProfessioneService;
import it.algos.wiki.Api;
import it.algos.wiki.web.AQueryVoce;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.A_CAPO;
import static it.algos.vaadwiki.application.WikiCost.*;


/**
 * Project vaadbio2
 * Created by Algos
 * User: gac
 * Date: gio, 12-lug-2018
 * Time: 16:39
 */
@Slf4j
public abstract class WikiService extends AService {

    public String titoloModuloAttivita = PATH_MODULO_PLURALE + ATT.toLowerCase();

    public String titoloModuloNazionalita = PATH_MODULO_PLURALE + NAZ.toLowerCase();

    public String titoloModuloProfessione = PATH_MODULO_LINK + ATT.toLowerCase();

    public String titoloModuloGenere = titoloModuloAttivita + " genere";

    public String titoloModuloDoppiNomi = "Progetto:Antroponimi/Nomi_doppi";

    public String titoloPaginaStatisticheAttivita = PATH_PROGETTO + ATT;

    public String titoloPaginaStatisticheNazionalita = PATH_PROGETTO + NAZ;

    public String titoloPaginaStatisticheGiorni = PATH_PROGETTO + GIORNI;

    public String titoloPaginaStatisticheAnni = PATH_PROGETTO + ANNI;

    public String titoloPaginaStatisticheNomi = PATH_PROGETTO + NOME;

    public String titoloPaginaStatisticheCognomi = PATH_PROGETTO + COGNOME;

    protected String titoloModulo;

    protected String codeLastDownload;

    protected String durataLastDownload;

    protected String codeLastElabora;

    protected String durataLastElabora;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected AttivitaService attivitaService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected NazionalitaService nazionalitaService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected ProfessioneService professioneService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected GenereService genereService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected PreferenzaService pref;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected Api api;


    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected ADateService date;


    /**
     * Costruttore <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     *
     * @param repository per la persistenza dei dati
     */
    public WikiService(MongoRepository repository) {
        super(repository);
    }// end of Spring constructor


    /**
     * Download completo del modulo da Wiki <br>
     * Cancella tutte le precedenti entities <br>
     * Registra su Mongo DB tutte le occorrenze di attività/nazionalità <br>
     */
    public void download() {
        String tagIni = "{";
        String tagEnd = "}";
        String tagVir = ",";
        String tagUgu = "=";
        String tagApi = "\"";
        long inizio = System.currentTimeMillis();
        String testo = "";
        testo = text.estrae(testo, tagIni, tagEnd);
        String[] righe = null;
        String[] parti;
        String singolare;
        String plurale;
        String message = "";

        deleteAll();

        //--legge la pagina wiki
        testo = ((AQueryVoce) appContext.getBean("AQueryVoce", titoloModulo)).urlRequest();
        if (text.isValid(testo)) {
            righe = testo.split(A_CAPO);
        }// end of if cycle

        if (array.isValid(righe)) {
            this.deleteAll();
            for (String riga : righe) {
                riga = text.levaCoda(riga, tagVir);
                parti = riga.split(tagUgu);
                if (array.isValid(parti) && parti.length == 2) {
                    singolare = parti[0].trim();
                    singolare = text.estrae(singolare, tagApi);
                    plurale = parti[1].trim();
                    plurale = text.estrae(plurale, tagApi);
                    this.findOrCrea(singolare.toLowerCase(), plurale);
                }// end of if cycle
            }// end of for cycle

            setLastDownload(inizio);
            if (pref.isBool(FlowCost.USA_DEBUG)) {
                message += "Download modulo ";
                message += entityClass.getSimpleName();
                message += " (";
                message += text.format(righe.length);
                message += " elementi in ";
                message += date.deltaText(inizio);
                message += "), con AQueryVoce, senza login, senza cookies, urlRequest di tipo GET";

                logger.debug(message);
            }// end of if cycle
        } else {
            logger.error(entityClass.getSimpleName() + " - Qualcosa non ha funzionato");
        }// end of if/else cycle
    }// end of method


    /**
     * Ricerca di una entity (la crea se non la trova) <br>
     *
     * @param singolare maschile e femminile (obbligatorio ed unico)
     * @param plurale   neutro (obbligatorio NON unico)
     *
     * @return la entity trovata o appena creata
     */
    public AEntity findOrCrea(String singolare, String plurale) {
        return null;
    }// end of method


    /**
     * Registra nelle preferenze la data dell'ultimo download effettuato <br>
     * Registra nelle preferenze la durata dell'ultimo download effettuato <br>
     */
    protected void setLastDownload(long inizio) {
        int delta = 1000;
        LocalDateTime lastDownload = LocalDateTime.now();
        pref.saveValue(codeLastDownload, lastDownload);

        long fine = System.currentTimeMillis();
        long durata = fine - inizio;
        int value = 0;
        if (durata > delta) {
            value = (int) durata / delta;
        } else {
            value = 1;
        }// end of if/else cycle

        pref.saveValue(durataLastDownload, value);
    }// end of method


    /**
     * Registra nelle preferenze la data dell'ultima elaborazione effettuata <br>
     * Registra nelle preferenze la durata dell'ultima elaborazione effettuata <br>
     */
    protected void setLastElabora(EATempo eaTempoType, long inizio) {
        int delta = 1;
        LocalDateTime lastDownload = LocalDateTime.now();
        pref.saveValue(codeLastElabora, lastDownload);

        long fine = System.currentTimeMillis();
        long durata = fine - inizio;
        int value = 0;

        switch (eaTempoType) {
            case nessuno:
            case millisecondi:
                break;
            case secondi:
                delta = 1000;
                break;
            case minuti:
                delta = 1000 * 60;
                break;
            case ore:
                delta = 1000 * 60 * 60;
                break;
            case giorni:
                delta = 1000 * 60 * 60 * 24;
                break;
            default:
                logger.warn("Switch - caso non definito");
                break;
        } // end of switch statement

        if (durata > delta) {
            value = (int) durata / delta;
        } else {
            value = 1;
        }// end of if/else cycle

        pref.saveValue(durataLastElabora, value);
    }// end of method


//    String message = VUOTA;
//    int durata = 0;
//
//        if (eaTempoType == EATempo.nessuno || text.isEmpty(flagDurata)) {
//        return VUOTA;
//    }// end of if cycle
//
//    durata = pref.getInt(flagDurata);
//    message = ", in ";
//
//        switch (eaTempoType) {
//        case nessuno:
//        case millisecondi:
//            message += date.toText(durata);
//            break;
//        case secondi:
//            message += durata > 1 ? date.toTextSecondi(durata) : INFERIORE_SECONDO;
//            break;
//        case minuti:
//            message += durata > 1 ? date.toTextMinuti(durata) : INFERIORE_MINUTO;
//            break;
//        case ore:
//            message += date.toText(durata);
//            break;
//        case giorni:
//            message += date.toText(durata);
//            break;
//        default:
//            log.warn("Switch - caso non definito");
//            break;
//    } // end of switch statement


    /**
     * Returns pageid list of all entities of the type.
     * <p>
     * Senza filtri
     * Ordinati per sort
     *
     * @return all entities
     */
    public ArrayList<Long> findAllPageid() {
        ArrayList<Long> lista = null;
        int recNum = count();
        int size = 100;
        int giri = (recNum / size) + 1;
        Sort sort = new Sort(Sort.Direction.ASC, "page");

        for (int k = 0; k < giri; k++) {
            lista = array.somma(lista, findAllPageid(k, size, sort));
        }// end of for cycle

        return lista;
    }// end of method


    /**
     * Returns pageid list of the requested page.
     * <p>
     * Senza filtri
     * Ordinati per sort
     *
     * @param offset numero di pagine da saltare, parte da zero
     * @param size   numero di elementi per ogni pagina
     * @param sort   ordinamento degli elementi
     *
     * @return all entities
     */
    public ArrayList<Long> findAllPageid(int offset, int size, Sort sort) {
        return null;
    }// end of method


    /**
     * Costruisce una lista di nomi delle properties del Search nell'ordine:
     * 1) Sovrascrive la lista nella sottoclasse specifica di xxxService
     *
     * @param context legato alla sessione
     *
     * @return lista di nomi di properties
     */
    @Override
    public List<String> getSearchPropertyNamesList(AContext context) {
        return Arrays.asList("plurale");
    }// end of method


}// end of class
