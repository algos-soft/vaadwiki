package it.algos.vaadwiki.modules.attnazprofcat;

import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadflow.service.AService;
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
        String testo = "";
        testo = text.estrae(testo, tagIni, tagEnd);
        String[] righe = null;
        String[] parti;
        String singolare;
        String plurale;
        String message = "";

        //--legge la pagina wiki
        testo = ((AQueryVoce) appContext.getBean("AQueryVoce", titoloModulo)).urlRequest();
        righe = testo.split("\n");

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
            if (pref.isBool(FlowCost.USA_DEBUG)) {
                message += "Download modulo ";
                message += entityClass.getSimpleName();
                message += " (";
                message += text.format(righe.length);
                message += " elementi in ";
                message += date.deltaText(inizio);
                message += "), con AQueryVoce, senza login, senza cookies, urlRequest di tipo GET";

                log.info(message);
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
        Sort sort = new Sort(Sort.Direction.ASC, "pageid");

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


//    /**
//     * Fetches the entities whose 'main text property' matches the given filter text.
//     * <p>
//     * Se esiste la company, filtrate secondo la company <br>
//     * The matching is case insensitive. When passed an empty filter text,
//     * the method returns all categories. The returned list is ordered by name.
//     * The 'main text property' is different in each entity class and chosen in the specific subclass
//     *
//     * @param filter the filter text
//     *
//     * @return the list of matching entities
//     */
//    @Override
//    public List<? extends AEntity> findFilter(String filter) {
//        List<? extends AEntity> lista = null;
//        String normalizedFilter = filter.toLowerCase();
//
//        lista = findAll();
//        if (lista != null) {
//            lista = lista.stream()
//                    .filter(entity -> {
//                        return getKeyUnica(entity).toLowerCase().startsWith(normalizedFilter);
//                    })
//                    .collect(Collectors.toList());
//        }// end of if cycle
//
//        return lista;
//    }// end of method

}// end of class
