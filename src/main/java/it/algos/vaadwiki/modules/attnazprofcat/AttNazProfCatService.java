package it.algos.vaadwiki.modules.attnazprofcat;

import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadflow.service.AService;
import it.algos.wiki.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;

import static it.algos.vaadwiki.application.WikiCost.*;


/**
 * Project vaadbio2
 * Created by Algos
 * User: gac
 * Date: gio, 12-lug-2018
 * Time: 16:39
 */
//@SpringComponent
//@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public abstract class AttNazProfCatService extends AService {

    public String titoloModuloAttivita = PATH_MODULO_PLURALE + ATT.toLowerCase();
    public String titoloModuloNazionalita = PATH_MODULO_PLURALE + NAZ.toLowerCase();
    public String titoloModuloProfessione = PATH_MODULO_LINK + ATT.toLowerCase();
    public String titoloPaginaStatisticheAttivita = PATH_PROGETTO + ATT;
    public String titoloPaginaStatisticheNazionalita = PATH_PROGETTO + NAZ;

    protected String titoloModulo;
    protected String codeLastDownload;
    protected String durataLastDownload;

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
    public AttNazProfCatService(MongoRepository repository) {
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
        String testo = api.leggeVoce(titoloModulo);
        testo = text.estrae(testo, tagIni, tagEnd);
        String[] righe = testo.split("\n");
        String[] parti;
        String singolare;
        String plurale;

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
                    this.findOrCrea(singolare, plurale);
                }// end of if cycle
            }// end of for cycle

            setLastDownload(inizio);
            log.info("Algos - Download del modulo " + entityClass.getSimpleName() + " (" + text.format(righe.length) + " elementi) in " + date.deltaText(inizio));
        }// end of if cycle
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

}// end of class
